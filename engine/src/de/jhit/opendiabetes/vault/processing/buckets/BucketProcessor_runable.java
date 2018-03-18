/*
 * Copyright (C) 2018 a.a.aponte
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package de.jhit.opendiabetes.vault.processing.buckets;

import de.jhit.opendiabetes.vault.container.VaultEntryType;
import de.jhit.opendiabetes.vault.container.BucketEntry;
import de.jhit.opendiabetes.vault.container.VaultEntry;
import static de.jhit.opendiabetes.vault.util.TimestampUtils.addMinutesToTimestamp;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import static de.jhit.opendiabetes.vault.container.BucketEventTriggers.*;
import de.jhit.opendiabetes.vault.container.FinalBucketEntry;
import de.jhit.opendiabetes.vault.util.BucketInterpolatorPairComparator;
import de.jhit.opendiabetes.vault.util.SplineInterpolator;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Objects;
import javafx.util.Pair;

/**
 *
 * @author a.a.aponte
 */
// timestamp < counter == just create bucket
// timestamp = counter == create bucket / count up
// timestamp > counder == create emtpy bucket / count up
public class BucketProcessor_runable {

    // part of the information array for ML-rev + one hot
    private final int ML_REV_AND_ONE_HOT = TRIGGER_EVENT_ACT_TIME_GIVEN.size() + TRIGGER_EVENT_ACT_TIME_TILL_NEXT_EVENT.size()
            + TRIGGER_EVENT_ACT_TIME_ONE.size() + TRIGGER_EVENTS_NOT_YET_SET.size();
    // part of the information array for ML-rev + NOT one hot
    // this part comes after the ML_REV_AND_ONE_HOT part
    private final int ML_REV_AND_NOT_ONE_HOT = TRIGGER_EVENT_NOT_ONE_HOT_ACT_TIME_SET.size() + TRIGGER_EVENT_NOT_ONE_HOT_ACT_TIME_GIVEN.size()
            + TRIGGER_EVENT_NOT_ONE_HOT_ACT_TIME_TILL_NEXT_EVENT.size() + TRIGGER_EVENT_NOT_ONE_HOT_ACT_TIME_ONE.size()
            + TRIGGER_EVENT_NOT_ONE_HOT_VALUE_IS_A_TIMESTAMP.size();

    // start BucketEntry number ... needed in createListOfBuckets and setBucketArrayInformation
    // if changed to 0 then checkPreviousBucketEntry must be changed as well
    final int BUCKET_START_NUMBER = 1;

    // Date check for setBucketArrayInformation (merge-to and onehot)
    private Date lastDate = null;
    // boolean switch for setBucketArrayInformation (merge-to and onehot)
    private boolean sameDatesGetNoTimerArrayUpdate_MLRevAndOneHot = false;
    private boolean sameDatesGetNoTimerArrayUpdate_MLRevAndNOTOneHot = false;

    // time countdown Array
    private double[] timeCountDownArray = new double[BucketEntry.getNumberOfVaultEntryTriggerTypes()];
    // onehot information Array
    private double[] onehotInformationArray = new double[BucketEntry.getNumberOfVaultEntryTriggerTypes()];
    // arrays for ML-rev and NOT one hot - parallel computing act times
    // first part of the pair == VaultEntryType
    // second part
    //      first double == timer
    //      second double == value
    private List<Pair<VaultEntryType, Pair<Double, Double>>> runningComputation = new ArrayList<>();
    // till next entry
    private VaultEntryType[] findNextArray = new VaultEntryType[BucketEntry.getNumberOfVaultEntryTriggerTypes()];
    // first part of the pair == bucket number for later placement
    // second part
    //          first VaultEntryType == type contained inside the hashset
    //          second Double == value
    private List<Pair<Integer, Pair<VaultEntryType, Double>>> listOfValuesForTheInterpolator = new ArrayList<>();

    /**
     * This method creates a new BucketEntry out of the given int and
     * VaultEntry. The int is the consecutive numbering of the BucketEntry.
     * During the process the VaultEntryType will be checked and all necessary
     * inputs for the arrays inside the BucketEntry will be generated
     * accordingly.
     *
     * @param bucketNumber The consecutive number of the BucketEntry that is
     * being created.
     * @param entry The VaultEntry that will be stored inside the BucketEntry.
     * @return This method returns a newly created BucketEntry
     */
    protected BucketEntry createNewBucket(int bucketNumber, VaultEntry entry) {

        // placeholder
        final double KEEP_EMPTY_FOR_FOLLOWING_CALCULATION = 0;

        // create new BucketEntry
        BucketEntry newBucket = new BucketEntry(bucketNumber, entry);

        // set Array information accroding to the VaultEntryType
        // is this a trigger event?
        if (ARRAY_ENTRY_TRIGGER_HASHMAP.containsKey(entry.getType())) {

            // get VaultEntryType position from the HashMap
            int arrayPosition = ARRAY_ENTRY_TRIGGER_HASHMAP.get(entry.getType());

            //
            // ML-rev + one hot
            //
            // is the act time given?
            if (TRIGGER_EVENT_ACT_TIME_GIVEN.contains(entry.getType())) {
                // set act time
                newBucket.setValueTimer(arrayPosition, entry.getValue());
                // set boolean true
                newBucket.setValues(arrayPosition, 1);
                // set to EMPTY
                newBucket.setFindNextVaultEntryType(arrayPosition, VaultEntryType.EMPTY);

                // is the the act time till some next event?
            } else if (TRIGGER_EVENT_ACT_TIME_TILL_NEXT_EVENT.containsKey(entry.getType())) {
                // set to 0 (no direct Act Time)
                newBucket.setValueTimer(arrayPosition, 0);
                // set boolean true
                newBucket.setValues(arrayPosition, 1);
                // set find next to the needed VaultEntryType
                newBucket.setFindNextVaultEntryType(arrayPosition, TRIGGER_EVENT_ACT_TIME_TILL_NEXT_EVENT.get(entry.getType()));

                // is the act time just for one frame?
            } else if (TRIGGER_EVENT_ACT_TIME_ONE.contains(entry.getType())) {
                // set act time to 1 minute
                newBucket.setValueTimer(arrayPosition, 1);
                // set boolean true
                newBucket.setValues(arrayPosition, 1);
                // set to EMPTY
                newBucket.setFindNextVaultEntryType(arrayPosition, VaultEntryType.EMPTY);

                //
                // ML-rev + NOT one hot
                //
                // catch VaultEntryTypes that have to be interpolated
            } else if (HASHSET_FOR_LINEAR_INTERPOLATION.contains(entry.getType())) {
                // at the moment the act time for there VaultEntryTypes are only 1 frame ... this might change later on if new types are added to the hashset
                newBucket.setValueTimer(arrayPosition, 1);
                // set value
                newBucket.setValues(arrayPosition, KEEP_EMPTY_FOR_FOLLOWING_CALCULATION);

                // new entries for this category are added to the listOfValuesForTheInterpolator list in the Bucket
                // first part of the pair == bucket number for later placement
                // second part
                //          first VaultEntryType == type contained inside the hashset
                //          second Double == value      (in VaultEntry value1)
                List<Pair<Integer, Pair<VaultEntryType, Double>>> tempList = new ArrayList<>();
                tempList.add(new Pair(bucketNumber, new Pair(entry.getType(), entry.getValue())));
                newBucket.setValuesForTheInterpolator(tempList);

                // set to EMPTY
                newBucket.setFindNextVaultEntryType(arrayPosition, VaultEntryType.EMPTY);

                // is the act time set?
            } else if (TRIGGER_EVENT_NOT_ONE_HOT_ACT_TIME_SET.containsKey(entry.getType())) {
                // set act time to set act time in hashmap
                newBucket.setValueTimer(arrayPosition, TRIGGER_EVENT_NOT_ONE_HOT_ACT_TIME_SET.get(entry.getType()));
                // set value
                newBucket.setValues(arrayPosition, KEEP_EMPTY_FOR_FOLLOWING_CALCULATION);

                // new entries are added to the runningComputation list in the Bucket
                // first part of the pair == VaultEntryType for later calculation
                // second part
                //          first double == timer       (in VaultEntry value2)
                //          second double == value      (in VaultEntry value1)
                List<Pair<VaultEntryType, Pair<Double, Double>>> tempList = new ArrayList<>();
                tempList.add(new Pair(entry.getType().getMergeTo(), new Pair(newBucket.getValueTimer(arrayPosition), entry.getValue())));
                newBucket.setValuesForRunningComputation(tempList);

                // set to EMPTY
                newBucket.setFindNextVaultEntryType(arrayPosition, VaultEntryType.EMPTY);

                // is the act time given?
            } else if (TRIGGER_EVENT_NOT_ONE_HOT_ACT_TIME_GIVEN.contains(entry.getType())) {
                // set act time
                newBucket.setValueTimer(arrayPosition, entry.getValue2());
                // set value
                newBucket.setValues(arrayPosition, KEEP_EMPTY_FOR_FOLLOWING_CALCULATION);

                // new entries are added to the runningComputation list in the Bucket
                // first part of the pair == VaultEntryType for later calculation
                // second part
                //          first double == timer       (in VaultEntry value2)
                //          second double == value      (in VaultEntry value1)
                List<Pair<VaultEntryType, Pair<Double, Double>>> tempList = new ArrayList<>();
                tempList.add(new Pair(entry.getType().getMergeTo(), new Pair(newBucket.getValueTimer(arrayPosition), entry.getValue())));
                newBucket.setValuesForRunningComputation(tempList);

                // set to EMPTY
                newBucket.setFindNextVaultEntryType(arrayPosition, VaultEntryType.EMPTY);

                // is the act time till some next event?
            } else if (TRIGGER_EVENT_NOT_ONE_HOT_ACT_TIME_TILL_NEXT_EVENT.contains(entry.getType())) {
                // set to 0 (no direct Act Time)
                newBucket.setValueTimer(arrayPosition, 0);
                // set value
                newBucket.setValues(arrayPosition, entry.getValue());
                // set to same VaultEntryType
                newBucket.setFindNextVaultEntryType(arrayPosition, entry.getType());

                // is the act time just for one frame?
            } else if (TRIGGER_EVENT_NOT_ONE_HOT_ACT_TIME_ONE.contains(entry.getType())) {
                // set act time to 1 minute
                newBucket.setValueTimer(arrayPosition, 1);
                // set value
                newBucket.setValues(arrayPosition, entry.getValue());
                // set to EMPTY
                newBucket.setFindNextVaultEntryType(arrayPosition, VaultEntryType.EMPTY);

                // is the given value a timestamp?
            } else if (TRIGGER_EVENT_NOT_ONE_HOT_VALUE_IS_A_TIMESTAMP.contains(entry.getType())) {
                // set to 0 (no direct Act Time)
                newBucket.setValueTimer(arrayPosition, 0);
                // set ???
                newBucket.setValues(arrayPosition, 0);
                //        newBucket.setValues(arrayPosition, ???);
                // set to EMPTY
                newBucket.setFindNextVaultEntryType(arrayPosition, VaultEntryType.EMPTY);

            }
        }

        return newBucket;

    }

    /**
     * This method creates a new empty BucketEntry out of the given int and
     * Date. The int is the consecutive numbering of the BucketEntry. A new
     * VaultEntry will be created containing the VauleEntryType EMPTY and the
     * given Date.
     *
     * @param bucketNumber The consecutive number of the BucketEntry that is
     * being created.
     * @param date The Date (timestamp) that is needed to create a vaild
     * VaultEntry
     * @return This method returns an newly created 'empty' BucketEntry.
     * @throws ParseException
     */
    protected BucketEntry createEmptyBucket(int bucketNumber, Date date) throws ParseException {

        BucketEntry newBucket = new BucketEntry(bucketNumber, new VaultEntry(VaultEntryType.EMPTY, date));
        return newBucket;

    }

    /**
     * This method creates a list of BucketEntrys out of a list of VaultEntrys.
     * This method checks if each VaultEntry is ML-relevant and if it is then a
     * new BucketEntry with the given VaultEntry is created and if not then a
     * new empty BucketEntry will be crated in it's place. This method also
     * creates a BucketEntry for every minute that passes starting from the
     * timestamp of the first given VaultEntry and ending with the timestamp of
     * the last given VaultEntry. Do not call this method with null as an imput.
     *
     * @param entryList This is the list of VaultEntrys that will be converted
     * into a list of BucketEntrys.
     * @return This method returns the list of BucketEntrys that is generated
     * out of the given list of VaultEntrys.
     * @throws ParseException
     */
    // TODO test for Date change when reaching 00:00 of the next day
    public List<BucketEntry> createListOfBuckets(List<VaultEntry> entryList) throws ParseException {

        // new created BucketEntry to store in the list
        BucketEntry newBucketEntry;

        // BucketEntry list counter
        int bucketEntryNumber = BUCKET_START_NUMBER;

        // TODO Liste aus Buckets erstellen aus der gegebenen VaultEnty Liste
        List<BucketEntry> outputBucketList = new ArrayList<>();
        Date timeCounter = null;
        if (entryList.size() > 0) {
            timeCounter = entryList.get(0).getTimestamp();
        }
        // position in the VaultEntry list
        int vaultEntryListPosition = 0;

        // compare Trigger (time or Event) with EventType before starting a new Trigger
        //
        // timeCounter == last timestamp
        while (vaultEntryListPosition < entryList.size()) {

            // found an earlier Date
            // e.g. given 2000.01.01-00:01 is earlier than the timeCounter date 2000.01.01-00:02
            if (entryList.get(vaultEntryListPosition).getTimestamp().before(timeCounter)) {

                // check is ML-relevant
                if (entryList.get(vaultEntryListPosition).getType().isMLrelevant()) {

                    // check if there already is a previous BucketEntry with the same VaultEntry timestamp and if this BucketEntry can be removed
                    if (checkPreviousBucketEntry((bucketEntryNumber - 1), entryList.get(vaultEntryListPosition).getTimestamp(), outputBucketList) && outputBucketList.size() >= 1) {
                        // the checked BucketEntry is vaild
                        // create a new Bucket with the given entry
                        newBucketEntry = createNewBucket(bucketEntryNumber, entryList.get(vaultEntryListPosition));
                        outputBucketList.add(newBucketEntry);
                        // update bucketEntryNumber
                        bucketEntryNumber++;

                    } else {

                        // the last BucketEntry is invalid and should be overwritten
                        // outputBucketEntry.size() - 1 == bucketEntryNumber - 1
                        outputBucketList.remove(outputBucketList.size() - 1);
                        // create a new Bucket with the given entry
                        // the new BucketEntry has the bucketEntryNumber from the removed BucketEntry
                        newBucketEntry = createNewBucket((bucketEntryNumber - 1), entryList.get(vaultEntryListPosition));
                        outputBucketList.add(newBucketEntry);

                        // DO NOT UPDATE THE BUCKETENTRYNUMBER SINCE THE LAST POSITION HAS BEEN OVERWRITTEN
                    }

                    //      ???
                    // TODO onehot - merge-to
                    // call this method BEFORE setting the new timeCounter date
                    setBucketArrayInformation(timeCounter, newBucketEntry);
                    // reset newBucketEntry
                    newBucketEntry = null;
                    //      ???

                } // else do nothing ... VaultEntry is not ML-relevant

                // DO NOT UPDATE TIMECOUNTER! entryList may contain more VaultEntrys with the same timestamp
                // move to the next VaultEntry in the list
                vaultEntryListPosition++;

                // found the same Date
                // e.g. given 2000.01.01-00:01 is equal to the timeCounter date 2000.01.01-00:01
            } else if (timeCounter.equals(entryList.get(vaultEntryListPosition).getTimestamp())) {

                if (entryList.get(vaultEntryListPosition).getType().isMLrelevant()) {

                    /* not possible since there hasn't been a BucketEntry till this time???
                        // check if there already is a previous BucketEntry with the same VaultEntry timestamp and if this BucketEntry can be removed
                        if (checkPreviousBucketEntry(bucketEntryNumber, timeCounter, outputBucketList)) {
                        } else {
                        }
                     */
                    // create a new Bucket with the given entry
                    newBucketEntry = createNewBucket(bucketEntryNumber, entryList.get(vaultEntryListPosition));
                    outputBucketList.add(newBucketEntry);
                    // update bucketEntryNumber
                    bucketEntryNumber++;

                    //
                    // TODO onehot - merge-to
                    // call this method BEFORE setting the new timeCounter date
                    setBucketArrayInformation(timeCounter, newBucketEntry);
                    // reset newBucketEntry
                    newBucketEntry = null;
                    //

                    // update timecounter
                    timeCounter = addMinutesToTimestamp(timeCounter, 1);
                    // move to the next VaultEntry in the list
                    vaultEntryListPosition++;

                    // create a new empty Bucket because VaultEntry is not ML-relevant
                } else {

                    // checkBucketEntry
                    // if true create a new BucketEntry
                    // if false then don't create a new BucketEntry
                    // TODO check if next VaultEntry has the same timestamp
                    //      if yes skip this Bucket to prevent multiple empty buckets for one timestamp
                    //      if not create a new empty Bucket for this timestamp
                    // maybe get last entry Date and check if current Date before last given Date##
                    // maybe just ignore case Date = Date and ML-relevant == false
                    //      since a skiped Date will fall under Date after Date case
                    //      and an empty bucket will be created for the skiped timestamp
                    // ---> check if possible
                    // last VaultEntry might be a problem if it is not ML-relevant
                    // create a new empty Bucket
                    newBucketEntry = createEmptyBucket(bucketEntryNumber, timeCounter);
                    outputBucketList.add(newBucketEntry);
                    // update bucketEntryNumber
                    bucketEntryNumber++;

                    //
                    // TODO onehot - merge-to
                    // call this method BEFORE setting the new timeCounter date
                    setBucketArrayInformation(timeCounter, newBucketEntry);
                    // reset newBucketEntry
                    newBucketEntry = null;
                    //

                    // update timecounter
                    timeCounter = addMinutesToTimestamp(timeCounter, 1);
                    // move to the next VaultEntry in the list
                    vaultEntryListPosition++;
                }

                // found a later Date
                // e.g. given 2000.01.01-00:02 is after than the timeCounter date 2000.01.01-00:01
            } else if (entryList.get(vaultEntryListPosition).getTimestamp().after(timeCounter)) {
                // create a new empty Bucket
                newBucketEntry = createEmptyBucket(bucketEntryNumber, timeCounter);
                outputBucketList.add(newBucketEntry);
                // update bucketEntryNumber
                bucketEntryNumber++;

                //
                // TODO onehot - merge-to
                // call this method BEFORE setting the new timeCounter date
                setBucketArrayInformation(timeCounter, newBucketEntry);
                // reset newBucketEntry
                newBucketEntry = null;
                //

                // update timecounter
                timeCounter = addMinutesToTimestamp(timeCounter, 1);

                // DO NOT UPDATE LIST POSITION! ... the given list position has not been reached yet
                // Date not found
            } else {

                // SHOULD NEVER BE ENTERED
                System.out.println("dead_lock");

            }

        }

        return outputBucketList;

    }

    /**
     * This method checks the BucketEntry in a list of BucketEntrys that is
     * prior to the bucket position that is given. The method checks if the
     * previous BucketEntry is an empty BucketEntry with the Date (timestamp)
     * that is given to the method. Returns true if the previous BucketEntry is
     * ok. (Not empty BucketEntry) Returns false if the previous BucketEntry is
     * obsolete. (is an empty BucketEntry)
     *
     * @param bucketListPosition The BucketEntry prior to this psoition will be
     * checked from the list of BucketEntrys.
     * @param date The Date that is being searched for in the BucketEntry.
     * @param listToCheckIn This is the list of BucketEntrys in which the
     * BucketEntry will be taken out of.
     * @return This method returns true if the previous BucketEntry is not an
     * empty BucketEntry and the Date matches and returns false if the previous
     * BucketEntry is an empty BucketEntry.
     */
    protected boolean checkPreviousBucketEntry(int bucketListPosition, Date date, List<BucketEntry> listToCheckIn) {

        // get position - 1 since entry x in the list is at the position x-1
        if (listToCheckIn.get(bucketListPosition - 1).getVaultEntry().getType() != VaultEntryType.EMPTY
                && listToCheckIn.get(bucketListPosition - 1).getVaultEntry().getTimestamp().equals(date)) {
            // VaultEntryType is usable
            // Timestamp is correct
            // BucketEntry is ok
            return true;
        } else {
            return false;
        }

    }

    /**
     * This method sets the necessary information into the BucketEntry arrays
     * and manages the onehot markers, onehot timers, and merge-tos. The Date
     * expected is the timeCounter from createListOfBuckets. The BucketEntry
     * that is given to this method will have it's arrays updated according to
     * the input of the previous BucketEntrys that have been in this method. The
     * first call of this method musst be done with the BucketEntry with the
     * bucketEntryNumber 1.
     *
     * @param date This is the current Date from the method createListOfBuckets
     * (timecounter).
     * @param bucket This is the BucketEntry that will have it's arrays updated.
     */
    protected void setBucketArrayInformation(Date date, BucketEntry bucket) {

        // this prevents that the first BucketEntry has the chance of couting down the timer.
        // sameDatesGetNoTimerArrayUpdate_MLRevAndOneHot is initially set to false.
        if (bucket.getBucketNumber() == BUCKET_START_NUMBER + 1) {
            sameDatesGetNoTimerArrayUpdate_MLRevAndOneHot = true;
            sameDatesGetNoTimerArrayUpdate_MLRevAndNOTOneHot = true;
        }

        // since it is possible to have multiple timestamps with the same date the timers should not be updated untill the next minute has started.
        // set lastDate on the first call
        if (lastDate == null) {
            lastDate = date;
        }
        // if lastDate is 2 minutes off then there is a new line of timestamps starting
        // e.g. lastDate = 00:01 and date = 00:02 then bucketEntrys for the timestamp of 00:01 are being created.
        //      if lastDate = 00:01 and date = 00:03 then bucketEntrys with the timestamp of 00:02
        //          are being created and that's why lastdate need to be updated.
        if (addMinutesToTimestamp(lastDate, 2).equals(date)) {
            lastDate = addMinutesToTimestamp(lastDate, 1);            // TODO not reached with EMPTY
            sameDatesGetNoTimerArrayUpdate_MLRevAndOneHot = true;
            sameDatesGetNoTimerArrayUpdate_MLRevAndNOTOneHot = true;
            // new timestamp == new list
            listOfValuesForTheInterpolator = new ArrayList<>();
        }                         // TODO not reached with EMPTY

        // set internal arrays through 1st BucketEntry
        if (bucket.getBucketNumber() == BUCKET_START_NUMBER) {
            timeCountDownArray = bucket.getFullValueTimer();
            onehotInformationArray = bucket.getFullValues();
            if (!bucket.getValuesForRunningComputation().isEmpty()) {
                runningComputation.addAll(bucket.getValuesForRunningComputation());
            }
            findNextArray = bucket.getFullFindNextVaultEntryType();
            if (!bucket.getValuesForTheInterpolator().isEmpty()) {
                listOfValuesForTheInterpolator.addAll(bucket.getValuesForTheInterpolator());
            }

        } else {
            // after 1st BucketEntry

            // check for new values inside the listOfValuesForTheInterpolator list in the BucketEntry
            if (!bucket.getValuesForTheInterpolator().isEmpty()) {
                listOfValuesForTheInterpolator.addAll(bucket.getValuesForTheInterpolator());
            }
            // transfer the data from the last BucketEntrys with the same timestamp into the current BucketEntry
            if (!listOfValuesForTheInterpolator.isEmpty()) {
                bucket.setValuesForTheInterpolator(listOfValuesForTheInterpolator);
            }

            // timer countdown for all entries inside the runningComputation list this is only done here before adding new upcoming entries
            if (!runningComputation.isEmpty()) {
                // new temp list with timer - 1
                List<Pair<VaultEntryType, Pair<Double, Double>>> tempList = new ArrayList<>();
                for (Pair<VaultEntryType, Pair<Double, Double>> pair : runningComputation) {
                    // first part of the pair == VaultEntryType for later calculation
                    // second part
                    //          first double == timer - 1
                    //          second double == value
                    Pair<Double, Double> tempPair = new Pair(pair.getValue().getKey() - 1, pair.getValue().getValue());             // TODO CHECK IF new Pair IS OK LIKE THIS
                    if (tempPair.getKey() > 0) {
                        // new valid pair
                        tempList.add(new Pair(pair.getKey(), tempPair));                                                            // TODO CHECK IF new Pair IS OK LIKE THIS
                    } else {
                        // do nothing pair is obsolete since timer is 0 or less
                    }
                }
                runningComputation = tempList;              // TODO might be a .clone()
            }

            // this part only goes through the ML-rev and one hot part
            for (int i = 0; i < ML_REV_AND_ONE_HOT; i++) {
                // DO NOT REPEAT TIMER ARRAY UPDATES ON SAME TIMESTAMP
                if (sameDatesGetNoTimerArrayUpdate_MLRevAndOneHot) {

                    // set false to not enter this part till lastDate update
                    // set this after the first run of all array positions
                    if (i == ML_REV_AND_ONE_HOT - 1) {
                        sameDatesGetNoTimerArrayUpdate_MLRevAndOneHot = false;
                    }
                    // set timers
                    if (timeCountDownArray[i] > 0) {
                        timeCountDownArray[i] = timeCountDownArray[i] - 1;
                    }
                }
                //
                // update info array stats
                // initial onehots are set when the Bucket is created
                //
                // set timer
                if (bucket.getValueTimer(i) > timeCountDownArray[i]
                        && ARRAY_ENTRY_TRIGGER_HASHMAP.get(bucket.getVaultEntry().getType()) == i) {
                    timeCountDownArray[i] = bucket.getValueTimer(i);
                }            /////

                // set onehot to false
                if (timeCountDownArray[i] == 0 && findNextArray[i].equals(VaultEntryType.EMPTY)) {
                    onehotInformationArray[i] = 0;
                }
                // set onehot to true
                if (timeCountDownArray[i] >= 1) {
                    onehotInformationArray[i] = 1;
                }
                // check for "till next array"
                // VaultEntryType is the standard for an empty BucketEntry
                if (!findNextArray[i].equals(VaultEntryType.EMPTY)
                        && findNextArray[i].equals(bucket.getVaultEntry().getType())) {
                    onehotInformationArray[i] = 0;
                    findNextArray[i] = VaultEntryType.EMPTY;
                }

                // set findNextEntry
                if (!bucket.getFindNextVaultEntryType(i).equals(VaultEntryType.EMPTY)
                        && !bucket.getFindNextVaultEntryType(i).equals(findNextArray[i])) {
                    findNextArray[i] = bucket.getFindNextVaultEntryType(i);
                }

                //
                // update BucketEntry arrays
                //
                // set timer
                // TODO if case is wrong???
                // if this timer is longer than the one saved in the BucketEntry take this one
                // if this timer is equal to the one saven in the BucketEntry - 1
                //      and this VaultEntryType is not onehot then update the BucketEntry (onehot might have just been set during the creation of the new BucketEntry).
                if (timeCountDownArray[i] > bucket.getValueTimer(i)
                        || timeCountDownArray[i] == bucket.getValueTimer(i) - 1 && !ARRAY_ENTRY_TRIGGER_HASHMAP.containsKey(bucket.getVaultEntry().getType())) {
                    bucket.setValueTimer(i, timeCountDownArray[i]);
                }

                // set onehotInformationArray
                bucket.setValues(i, onehotInformationArray[i]);
                // set findNextArray
                // if findNextArray is EMPTY then it is filled with the needed information
                // if findNextArray is filled with something else then nothing has to be done
                if (bucket.getFindNextVaultEntryType(i).equals(VaultEntryType.EMPTY)) {
                    bucket.setFindNextVaultEntryType(i, findNextArray[i]);
                }
            }

            // this part only goes through the ML-rev and NOT one hot part
            for (int i = ML_REV_AND_ONE_HOT; i < ML_REV_AND_NOT_ONE_HOT + ML_REV_AND_ONE_HOT; i++) {
                // DO NOT REPEAT TIMER ARRAY UPDATES ON SAME TIMESTAMP
                if (sameDatesGetNoTimerArrayUpdate_MLRevAndNOTOneHot) {

                    // set false to not enter this part till lastDate update
                    // set this after the first run of all array positions
                    if (i == ML_REV_AND_ONE_HOT - 1) {
                        sameDatesGetNoTimerArrayUpdate_MLRevAndNOTOneHot = false;
                    }
                    // set timers
                    if (timeCountDownArray[i] > 0) {
                        timeCountDownArray[i] = timeCountDownArray[i] - 1;
                    }
                }
                // =======================================
                // calculateAverageForSmallestBucketSize()
                // =======================================
                // ML-rev NOT one hot array positions need a differentiated handling
                // =================================================================
                //
                // update info array stats
                // initial Values are set when the Bucket is created
                //
                // set timer
                if (bucket.getValueTimer(i) > timeCountDownArray[i]
                        && ARRAY_ENTRY_TRIGGER_HASHMAP.get(bucket.getVaultEntry().getType()) == i) {
                    timeCountDownArray[i] = bucket.getValueTimer(i);
                }

                // set Vaule to 0
                if (timeCountDownArray[i] == 0 && findNextArray[i].equals(VaultEntryType.EMPTY)) {
                    onehotInformationArray[i] = 0;
                }

                // check for "till next array" <in case of TRIGGER_EVENT_NOT_ONE_HOT_ACT_TIME_TILL_NEXT_EVENT>
                // VaultEntryType is the standard for an empty BucketEntry
                if (!findNextArray[i].equals(VaultEntryType.EMPTY)
                        && findNextArray[i].equals(bucket.getVaultEntry().getType())) {
                    onehotInformationArray[i] = 0;
                    findNextArray[i] = VaultEntryType.EMPTY;
                }

                // set the needed value
                // the original BucketEntry will contain the VaultEntry with the VaultEntryType
                // on first call of this method with a new event the needed values are set
                if (timeCountDownArray[i] >= 1 || !findNextArray[i].equals(VaultEntryType.EMPTY)) {
                    // this is the first encounter of this entry so all average must be set

//                    if(bucket.getVaultEntry().getType()==VaultEntryType.EMPTY){
//                        System.out.println(bucket.toString());
//                        if(ARRAY_ENTRY_TRIGGER_HASHMAP.get(bucket.getVaultEntry().getType())==null)
//                            System.out.println("is null");
//                    }
                    //tiweGH hotfix: catch getType() = Empty leading to "null == i" NullpointerException
                    if (bucket.getVaultEntry().getType() != VaultEntryType.EMPTY && ARRAY_ENTRY_TRIGGER_HASHMAP.get(bucket.getVaultEntry().getType()) == i) {
                        //
                        // check which VaultEntryType is given and calculate as intended
                        // atm this timer will be reseted every time a new (same)event is started
                        //
                        if (TRIGGER_EVENT_NOT_ONE_HOT_ACT_TIME_SET.containsKey(bucket.getVaultEntry().getType())) {
                            // new entries are set in the createNewBucket method
                            // add them to the internal runningComputation list
                            runningComputation.addAll(bucket.getValuesForRunningComputation());
                            // DO NOT SET onehotInformationArray[i] HERE BECAUSE THE VALUE HAS TO BE COMPUTED (calculateAverageForSmallestBucketSize method)
                        } else if (TRIGGER_EVENT_NOT_ONE_HOT_ACT_TIME_GIVEN.contains(bucket.getVaultEntry().getType())) {
                            // new entries are set in the createNewBucket method
                            // add them to the internal runningComputation list
                            runningComputation.addAll(bucket.getValuesForRunningComputation());
                            // DO NOT SET onehotInformationArray[i] HERE BECAUSE THE VALUE HAS TO BE COMPUTED (calculateAverageForSmallestBucketSize method)
                        } else if (TRIGGER_EVENT_NOT_ONE_HOT_ACT_TIME_TILL_NEXT_EVENT.contains(bucket.getVaultEntry().getType())) {
                            // TRIGGER_EVENT_NOT_ONE_HOT_ACT_TIME_TILL_NEXT_EVENT : value is set when BucketEntry is created
                            onehotInformationArray[i] = bucket.getValues(i);
                        } else if (TRIGGER_EVENT_NOT_ONE_HOT_ACT_TIME_ONE.contains(bucket.getVaultEntry().getType())) {
                            // TRIGGER_EVENT_NOT_ONE_HOT_ACT_TIME_ONE : value is set when BucketEntry is created
                            onehotInformationArray[i] = bucket.getValues(i);
                        } else if (TRIGGER_EVENT_NOT_ONE_HOT_VALUE_IS_A_TIMESTAMP.contains(bucket.getVaultEntry().getType())) {
                            onehotInformationArray[i] = bucket.getValues(i);
                            // onehotInformationArray[i] = ???
                        }

                    }
                } else {
                    // there is no new value in this BucketEntry position
                    onehotInformationArray[i] = bucket.getValues(i);
                }

                // set findNextEntry <in case of TRIGGER_EVENT_NOT_ONE_HOT_ACT_TIME_TILL_NEXT_EVENT>
                if (!bucket.getFindNextVaultEntryType(i).equals(VaultEntryType.EMPTY)
                        && !bucket.getFindNextVaultEntryType(i).equals(findNextArray[i])) {
                    findNextArray[i] = bucket.getFindNextVaultEntryType(i);
                }

                //
                // update BucketEntry arrays
                //
                // set timer
                // TODO if case is wrong???
                // if this timer is longer than the one saved in the BucketEntry take this one
                // if this timer is equal to the one saven in the BucketEntry - 1
                //      and this VaultEntryType is not onehot then update the BucketEntry (onehot might have just been set during the creation of the new BucketEntry).
                if (timeCountDownArray[i] > bucket.getValueTimer(i)
                        || timeCountDownArray[i] == bucket.getValueTimer(i) - 1 && !ARRAY_ENTRY_TRIGGER_HASHMAP.containsKey(bucket.getVaultEntry().getType())) {
                    bucket.setValueTimer(i, timeCountDownArray[i]);
                }

                // set onehotInformationArray
                bucket.setValues(i, onehotInformationArray[i]);
                // set findNextArray
                // if findNextArray is EMPTY then it is filled with the needed information
                // if findNextArray is filled with something else then nothing has to be done
                if (bucket.getFindNextVaultEntryType(i).equals(VaultEntryType.EMPTY)) {
                    bucket.setFindNextVaultEntryType(i, findNextArray[i]);
                }
            }
        }

        // merge-to
        bucket.getVaultEntry().setType(bucket.getVaultEntry().getType().getMergeTo());

        // save the new created list of runningComputation
        bucket.setValuesForRunningComputation(runningComputation);
    }

    /**
     * This method computes the values taken from the runningComputation array
     * in the given BucketEntry. The values are calculated according to the set
     * HASHSETS_TO_SUM_UP hashset which contains all VaultEntryTypes that are to
     * be sumed up. If there are VaultEntryTypes that are to be merged this will
     * also be done in the process of totaling the found values. The results are
     * saved into the listOfComputedValuesForTheFinalBucketEntry array of the
     * same BucketEntry.
     *
     * This method is called for each BucketEntry after the unneeded
     * BucketEntrys are removed.
     *
     * @param bucket This is the BucketEntry in which the found values are sumed
     * up and saved into.
     */
    protected void calculateAverageForSmallestBucketSize(BucketEntry bucket) {
        // just add up ... values have already been set

        // ===== just for info
        // first part of the pair == VaultEntryType
        // second part
        //          first double == timer
        //          second double == value
        // ===== just for info
        // this list contains all calculated values before they are set into the FinalBucketEntrys
        List<Pair<VaultEntryType, Double>> listOfComputedValues = new ArrayList<>();

        // iterate over all given pairs in the runningComputation list and add them up acording to their VaultEntryType
        for (Pair<VaultEntryType, Pair<Double, Double>> iteratorPair : bucket.getValuesForRunningComputation()) {
            VaultEntryType type = iteratorPair.getKey();
            //tiweGH hotfix: catch index = -1
            if (listOfComputedValues.indexOf(iteratorPair.getKey()) != -1 && doesListOfComputedValuesContain(type, listOfComputedValues)) {
                // a pair for the given type is already created
                // sum up values

                // first get the needed pair out of the list ... remove it during the process the replace it later
                Pair<VaultEntryType, Double> tempPair = listOfComputedValues.remove(listOfComputedValues.indexOf(iteratorPair.getKey()));
                // create a new pair with the summed up values
                tempPair = new Pair(tempPair.getKey(), tempPair.getValue() + iteratorPair.getValue().getValue());
                // now put the pair back into the list
                listOfComputedValues.add(tempPair);
            } else {
                // create a new pair for the given type
                Pair<VaultEntryType, Double> tempPair = new Pair(type, iteratorPair.getValue().getValue());
                // add the pair to the list
                listOfComputedValues.add(tempPair);
            }
        }

        // now get all entries from the onehot array that should be summed up too
        for (VaultEntryType vaultEntryType : HASHSETS_TO_SUM_UP) {
            VaultEntryType type = vaultEntryType.getMergeTo();
            //tiweGH hotfix: catch index = -1
            if (listOfComputedValues.indexOf(type) != -1 && doesListOfComputedValuesContain(type, listOfComputedValues)) {
                // add the rest into this pair

                Pair<VaultEntryType, Double> tempPair = listOfComputedValues.remove(listOfComputedValues.indexOf(type));
                // sum up the value in the pair with that in the bucket from the given type
                tempPair = new Pair(tempPair.getKey(), tempPair.getValue() + bucket.getValues(ARRAY_ENTRY_TRIGGER_HASHMAP.get(vaultEntryType)));
                // put the pair bach into the list of computed values
                listOfComputedValues.add(tempPair);
            } else {
                // check if there is a value for the wanted type
                // ... if there is create a new pair
                // ... if not then move on
                if (bucket.getValues(ARRAY_ENTRY_TRIGGER_HASHMAP.get(vaultEntryType)) != 0.0) {
                    // create a new pair
                    Pair<VaultEntryType, Double> tempPair = new Pair(type, bucket.getValues(ARRAY_ENTRY_TRIGGER_HASHMAP.get(vaultEntryType)));
                    // add to the list of computed values
                    listOfComputedValues.add(tempPair);
                } // no value no pair needed

            }
        }

        // lineare interpolation
        // this is done in the processor method after all values have been collected from the BucketEntrys
        // save the generated list of values inside the BucketEntry for later use
        bucket.setComputedValuesForTheFinalBucketEntry(listOfComputedValues);
    }

    /**
     * This method gets a VaultEntryType and a list of pairs containing
     * VaultEntryType and Double. This method returns true if the VaultEntryType
     * is found within the list of pairs and false if it is not found.
     *
     * @param findThisType This is the VaultEntryType that is searched for.
     * @param listOfComputedValues This is the list of pairs in which the
     * VaultEntryType will be searched for.
     * @return Returns true if VaultEntryType is found and false if not.
     */
    protected boolean doesListOfComputedValuesContain(VaultEntryType findThisType, List<Pair<VaultEntryType, Double>> listOfComputedValues) {
        for (Pair<VaultEntryType, Double> pair : listOfComputedValues) {
            if (pair.getKey().equals(findThisType)) {
                return true;
            } else {
                // wait till end
            }
        }
        // type not found
        return false;
    }

    /**
     *
     * collects data out of array / list computes avg over given list size
     * creates FinalBucketEntry and sets the computed and needed values
     *
     * @param bucketNumber
     * @param bucketsToMerge
     * @return
     */
    protected FinalBucketEntry calculateAverageForWantedBucketSize(int bucketNumber, double wantedBucketSize, List<BucketEntry> bucketsToMerge) {
        // the list of BucketEntrys contains all entries that are relavent for the average computation
        final int MAX_ARRAY_SIZE = ARRAY_ENTRIES_AFTER_MERGE_TO.size();
        final double WANTED_BUCKET_SIZE = wantedBucketSize;

        double[] valueComputaion = new double[MAX_ARRAY_SIZE];
        // Fill valueComputaion with 0
        Arrays.fill(valueComputaion, 0.0);

        for (BucketEntry entry : bucketsToMerge) {

            // temp array to save the BucketEntry information
            double[] tempValue = new double[MAX_ARRAY_SIZE];

            // set all BucketEntry information into the tempValue array
            // go through all arrays / lists containing information (listOfComputedValuesForTheFinalBucketEntry and onehotInformationArray)
            // first run through the full length of the onehotInformationArray and set the entries into the after merge-to array form
            for (VaultEntryType type : ARRAY_ENTRY_TRIGGER_HASHMAP.keySet()) {

                // check if a merge-to VaultEntryType is found or not
                if (ARRAY_ENTRIES_AFTER_MERGE_TO.containsKey(type)) {
                    // not a merged type
                    tempValue[ARRAY_ENTRIES_AFTER_MERGE_TO.get(type)] = entry.getValues(ARRAY_ENTRY_TRIGGER_HASHMAP.get(type));
                } else {
                    // a merged type

                    // ignore for now since listOfComputedValuesForTheFinalBucketEntry contains the valid values
                }
            }

            // second run through the whole listOfComputedValuesForTheFinalBucketEntry
            for (Pair<VaultEntryType, Double> pair : entry.getComputedValuesForTheFinalBucketEntry()) {
                // place found entries into the right array position
                // look for the position of the entry that matches this merge-to VaultEntryType
                //
                tempValue[ARRAY_ENTRIES_AFTER_MERGE_TO.get(pair.getKey())] = pair.getValue();
            }

            // fill the valueComputation Array with the new data (add them up)
            for (int i = 0; i < MAX_ARRAY_SIZE - 1; i++) {
                valueComputaion[i] = valueComputaion[i] + tempValue[i];
            }
        }

        // all sumed up values are now inside the valueComputaion array
        // check what value gets saved inside the FinalBucketEntry
        for (VaultEntryType type : ARRAY_ENTRIES_AFTER_MERGE_TO.keySet()) {
            // array position according to the ARRAY_ENTRIES_AFTER_MERGE_TO hashmap
            int arrayPos = ARRAY_ENTRIES_AFTER_MERGE_TO.get(type);

            // comput the average Value
            double avgValue = valueComputaion[arrayPos] / WANTED_BUCKET_SIZE;                // onehot is OK ... check for other normal values and avg values TODO

            // one hots ... if avgValue >= 0.5 then 1 else 0
            if (type.isOneHot()) {
                if (avgValue >= 0.5) {
                    valueComputaion[arrayPos] = 1;
                } else {
                    valueComputaion[arrayPos] = 0;
                }
            } else {

                // other values
                // average of all other values should be valid if calculated this way
                valueComputaion[arrayPos] = avgValue;                                        // other values TODO check

            }
        }

        // create FinalBucketEntry
        FinalBucketEntry result = new FinalBucketEntry(bucketNumber);
        // clone the computed Values into the FinalBucketEntry
        result.setFullOnehotInformationArray(valueComputaion.clone());

        return result;
    }

    /**
     * This method iterates through all BucketEntrys of the given list of
     * BucketEntrys and creates a new list of BucketEntrys that only contains
     * the last found BucketEntry of each timestamp foudn in the given list of
     * BucketEntrys. All the BucketEntrys in the new created list of
     * BucketEntrys will have the correct numeration set thier new position in
     * the list.
     *
     * @param bucketList This is the list of BucketEntrys that will be used to
     * create the new normalized (minimal) list of BucketEntrys.
     * @return This method returns a list of BucketEntrys with only one
     * BucketEntry per timestamp.
     */
    protected List<BucketEntry> removeUnneededBucketEntrys(List<BucketEntry> bucketList) {
        List<BucketEntry> outputBucketList = new ArrayList<>();
        Date checkingThisBucketEntryDate = bucketList.get(0).getVaultEntry().getTimestamp();
        Date lastBucketEntryDate = bucketList.get(bucketList.size() - 1).getVaultEntry().getTimestamp();
        int currentBucketListPosition = 0;
        int currentBucketOutputListPosition = BUCKET_START_NUMBER;

        // loop ends on Date.equals(last Date)
        // leave loop when on last date to prevent NullPointerException
        while (checkingThisBucketEntryDate.before(lastBucketEntryDate)) {
            // move through the timestamps
            while (checkingThisBucketEntryDate.equals(bucketList.get(currentBucketListPosition).getVaultEntry().getTimestamp())) {
                currentBucketListPosition++;
            }
            // currentBucketListPosition is now the position of the needed BucketEntry
            outputBucketList.add(bucketList.get(currentBucketListPosition));
            // update the BucketEntryNumber to the new position
            outputBucketList.get(outputBucketList.size() - 1).setBucketNumber(currentBucketOutputListPosition);

            // set checkingThisBucketEntryDate to the next minute
            checkingThisBucketEntryDate = addMinutesToTimestamp(checkingThisBucketEntryDate, 1);
            // set currentBucketListPosition to the next BucketEntry position
            currentBucketListPosition++;
            // update currentBucketOutputListPosition to the next BucketEntry number to be set
            currentBucketOutputListPosition++;
        }
        // since on last date just take last entry
        // add the last BucketEntry to the output list
        outputBucketList.add(bucketList.get(bucketList.size() - 1));
        // update the BucketEntryNumber to the new position
        outputBucketList.get(outputBucketList.size() - 1).setBucketNumber(currentBucketOutputListPosition);

        return outputBucketList;
    }

    /**
     * This method receives a list of VaultEntrys and a wanted step size (in
     * minutes) for the resulting list of FinalBucketEntrys. In this method the
     * given list of VaultEntrys will be transformed into a list of BucketEntrys
     * through the createListOfBuckets method which will then be stripped-down
     * to the necessary BucketEntrys via the removeUnneededBucketEntrys method.
     * The list of BucketEntrys will then run through the
     * calculateAverageForSmallestBucketSize method to set all of the needed
     * average calculations. The last step is to transform the list of
     * BucketEntrys into a list of FinalBucketEntrys. If the wanted bucket size
     * is 1 the list will just be transformed. If the wanted bucket size is
     * greater than 1 the list will be sent through the !!!average to the wanted
     * bucket size!!! method and reduced to the needed FinalBucketEntrys.
     *
     * If the given list of VaultEntrys results in a list of BucketEntrys that
     * does not fulfill the given size % wanted size == 0 scheme the needed
     * BucketEntrys will be added as empty BucketEntrys at the end of the list
     * of BucketEntrys.
     *
     * @param entryList The list of VaultEntrys that will be transformed into a
     * list of FinalBucketEntrys.
     * @param wantedBucketSize This is the wanted bucket size (in minutes).
     * @return This method returns a list of FinalBucketEntrys in the desired
     * bucket size (time step size).
     * @throws ParseException
     */
    // fill up end of the list with empty buckets if x % y != 0
    public List<FinalBucketEntry> processor(List<VaultEntry> entryList, int wantedBucketSize) throws ParseException {
        List<FinalBucketEntry> outputFinalBucketList = new ArrayList<>();
        // FinalBucketEntry counter
        int finalBucketEntryListCounter = BUCKET_START_NUMBER;

        // Bucket size == 1 min.
        List<BucketEntry> listOfBucketEntries = createListOfBuckets(entryList);
        // remove duplicate timestamp BucketEntrys
        listOfBucketEntries = removeUnneededBucketEntrys(listOfBucketEntries);
        // calculate averages
        // for each BucketEntry in the list
        for (BucketEntry entry : listOfBucketEntries) {
            calculateAverageForSmallestBucketSize(entry);
        }

        // ==============================
        // =====SET UP INTERPOLATION=====
        // ==============================
        // create a new hashmap for creating a matrix that contains all computed interpolation information
        // array start position
        int arrayPositionInHashmap = 0;
        // HashMap containing matrix positions for the types
        HashMap<VaultEntryType, Integer> tempHashMapForMatrixPositionsOfTheVaultEntryTypes = new HashMap<>();
        // generate HashMap
        for (VaultEntryType type : HASHSET_FOR_LINEAR_INTERPOLATION) {
            tempHashMapForMatrixPositionsOfTheVaultEntryTypes.put(type, arrayPositionInHashmap);
            arrayPositionInHashmap++;
        }
        // create a matrix for the computed interpolation information
        // at this point there will be a interpolation value for every BucketEntry
        double[][] interpolationMatrix = new double[tempHashMapForMatrixPositionsOfTheVaultEntryTypes.size()][listOfBucketEntries.size()];       // TODO check if array sizes are ok
        // Fill interpolationMatrix with 0.0
        for (int i = 0; i < interpolationMatrix.length; i++) {

            Arrays.fill(interpolationMatrix[i], 0.0);
            //System.out.println(Arrays.toString(interpolationMatrix[i]));
        }
        // ==============================
        // =====SET UP INTERPOLATION=====
        // ==============================

        // ===========================
        // ===RUN THE INTERPOLATION===
        // ===========================
        // get data
        List<Pair<Integer, Pair<VaultEntryType, Double>>> rawData = new ArrayList<>();//collectInterpolationDataFromBucketEntrys(listOfBucketEntries);
        //
        // sort rawData
        for (VaultEntryType type : HASHSET_FOR_LINEAR_INTERPOLATION) {
            // sort rawData for the wanted VaultEntryType
            //List<Pair<Integer, Pair<VaultEntryType, Double>>> sortedRawData = sortDataByTypeForInterpolation(type, rawData);

            // list for sorted data from start till last entry ... not found entries will be set so that the interpolateGaps method know what to compute
            List<Pair<Integer, Pair<VaultEntryType, Double>>> sortedData = new ArrayList();

            // sort the data so that the pairs are storted according to the bucket number ... BUCKET_START_NUMBER to x
//            for (int i = BUCKET_START_NUMBER; i < listOfBucketEntries.size(); i++) {
//
//                // if list is empty then don't add anymore entries
//                if (!sortedRawData.isEmpty()) {
//                    Pair<Boolean, Integer> tempTest = checkIfBucketEntryNumberIsAvailableAndAtWhichPositionTheNumberIsFoundInsideTheList(i, sortedRawData);
//
//                    // BucketEntry number has been found inside the list of sortedRawData
//                    if (tempTest.getKey()) {
//                        sortedData.add(sortedRawData.remove( (int) tempTest.getValue()));
//                    } else {
//                        // BucketEntry number not found
//                        // if the sorted list is not empty then will with entries telling the interpolateGaps method that this entry is missing
//                        if (!sortedData.isEmpty()) {sortedData.add(new Pair(i, new Pair(type, null)));}
//                    }
//                } else {
//                    // sortedRawData is empty
//                }
//
//                // count i one up
//                i++;
//            }
            //--------------------------
            //tiweGH hotfix: data ISN'T YET READY!
            for (BucketEntry bucket : listOfBucketEntries) {
                if (bucket.getVaultEntry().getType() == type) {
                    sortedData.add(new Pair(bucket.getBucketNumber(), new Pair(type, bucket.getVaultEntry().getValue())));
                }
            }

            HashSet<Integer> redundancyChecker = new HashSet<>();
            List<Pair<Integer, Pair<VaultEntryType, Double>>> sortedDataCleared = new ArrayList();
            for (Pair<Integer, Pair<VaultEntryType, Double>> pair : sortedData) {
                if (!redundancyChecker.contains(pair.getKey())) {

                    sortedDataCleared.add(pair);
                    redundancyChecker.add(pair.getKey());
                }
            }
            sortedData = sortedDataCleared;
            sortedData.sort(new BucketInterpolatorPairComparator());
            List<Pair<Integer, Pair<VaultEntryType, Double>>> sortedDataFilledGaps = new ArrayList<>();
            Integer tmpLast = null;
            VaultEntryType tmpType;

            //fill missing indeces with empty entries
            for (int i = 0; i < sortedData.size();) {

                tmpType = sortedData.get(0).getValue().getKey();
                if (tmpLast == null) {

                    tmpLast = sortedData.get(0).getKey();
                    sortedDataFilledGaps.add(sortedData.get(0));

                    i++;
                } else if (sortedData.get(i).getKey() > tmpLast) {

                    sortedDataFilledGaps.add(new Pair(tmpLast, new Pair(tmpType, null)));

                } else {

                    sortedDataFilledGaps.add(sortedData.get(i));
                    i++;
                }

                tmpLast++;
            }

//            for (int i = 0; i < sortedDataFilledGaps.size(); i++) {
//                if (i > 0 && sortedDataFilledGaps.get(i - 1).getKey().intValue() + 1 != sortedDataFilledGaps.get(i).getKey().intValue()) {
//                    System.out.println("NEIN");
//                    System.out.println("erst " + sortedDataFilledGaps.get(i - 1).getKey());
//                    System.out.println("zweit " + sortedDataFilledGaps.get(i).getKey());
//                }
//
//            }
            //---------------------------------

            // data is now ready for the interpolateGaps method
            List<Pair<Integer, Pair<VaultEntryType, Double>>> interpolatedData = interpolateGaps(sortedDataFilledGaps);

            // ==========================================================
            // =====interpolatedData contains all data for this type=====
            // =there is a value for every BucketEntry that is available=
            // ======fill the interpolationMatrix with these values======
            // ==========================================================
            // type position inside of the matrix
            int typePositionInsideTheMatrix = tempHashMapForMatrixPositionsOfTheVaultEntryTypes.get(type);

            // fill the matrix at the positions that have availabe values
            for (Pair<Integer, Pair<VaultEntryType, Double>> pair : interpolatedData) {
                // typePositionInsideTheMatrix gives the position of the VaultEntryType in the matrix
                // pair.getKey() gives the BucketEntry number that is also the position inside the matrix for this bucket
                // e.g. BucketEntry number = 1 matrix position = 1 - BUCKET_START_NUMBER = 0
                interpolationMatrix[typePositionInsideTheMatrix][pair.getKey() - BUCKET_START_NUMBER] = (double) pair.getValue().getValue();          // TODO check if this position is ok (pair.getKey() - BUCKET_START_NUMBER)
            }
        }
        // ===========================
        // ===RUN THE INTERPOLATION===
        // ===========================

        // the interpolationMatrix now contains all the values for the BucketEntrys
        // set the values into the BucketEntrys
        for (BucketEntry bucket : listOfBucketEntries) {
            int bucketNumber = bucket.getBucketNumber();

            for (VaultEntryType type : HASHSET_FOR_LINEAR_INTERPOLATION) {
                // type position inside of the matrix
                int typePositionInsideTheMatrix = tempHashMapForMatrixPositionsOfTheVaultEntryTypes.get(type);

                bucket.setValues(ARRAY_ENTRY_TRIGGER_HASHMAP.get(type), interpolationMatrix[typePositionInsideTheMatrix][bucketNumber - 1]);
            }
        }

        // now all BucketEntrys have the interpolated values inside there setValues
        // if wantedBucketSize != 1 transform the list into the wanted size .. standard bucket size == 1
        if (wantedBucketSize != 1) {

            // set to wanted bucket size
            // update average to the wanted bucket size
            // TODO
            // list of BucketEntrys to give average for wanted bucket size
            List<BucketEntry> listOfWantedBucketSize = new ArrayList<>();
            // bucketEntry counter

            // for each BucketEntry
            for (BucketEntry entry : listOfBucketEntries) {
                // bucketNumber % wantedBucketSize
                if (listOfWantedBucketSize.size() % wantedBucketSize == 0 && !listOfWantedBucketSize.isEmpty()) {
                    // mod == 0
                    // call average to the wanted bucket size
                    // save output in outputFinalBucketList
                    // start new list for call

                    // call <average to the wanted bucket size> method
                    outputFinalBucketList.add(calculateAverageForWantedBucketSize(finalBucketEntryListCounter, wantedBucketSize, listOfWantedBucketSize));
                    // update FinalBucketEntry counter
                    finalBucketEntryListCounter++;
                    // start new list
                    listOfWantedBucketSize = new ArrayList<>();
                    listOfWantedBucketSize.add(entry);
                } else {
                    listOfWantedBucketSize.add(entry);
                }
            }
            // last call of the <average to the wanted bucket size> method
            outputFinalBucketList.add(calculateAverageForWantedBucketSize(finalBucketEntryListCounter, wantedBucketSize, listOfWantedBucketSize));
        } else {
            // wantedBucketSize == 1
            // transform all BucketEntrys into FinalBucketEntrys
            for (BucketEntry entry : listOfBucketEntries) {
                outputFinalBucketList.add(new FinalBucketEntry(entry.getBucketNumber()));
                // set all BucketEntry information into the new FinalBucketEntry array

                // go through all arrays / lists containing information (listOfComputedValuesForTheFinalBucketEntry and onehotInformationArray)
                // first run through the full length of the onehotInformationArray and set the entries into the after merge-to array form
                for (VaultEntryType type : ARRAY_ENTRY_TRIGGER_HASHMAP.keySet()) {

                    // check if a merge-to VaultEntryType is found or not
                    if (ARRAY_ENTRIES_AFTER_MERGE_TO.containsKey(type)) {
                        // not a merged type
                        outputFinalBucketList.get(outputFinalBucketList.size() - 1).setOnehotInformationArray(ARRAY_ENTRIES_AFTER_MERGE_TO.get(type), entry.getValues(ARRAY_ENTRY_TRIGGER_HASHMAP.get(type)));
                    } else {
                        // a merged type

                        // ignore for now since listOfComputedValuesForTheFinalBucketEntry contains the valid values
                    }
                }

                // second run through the whole listOfComputedValuesForTheFinalBucketEntry
                for (Pair<VaultEntryType, Double> pair : entry.getComputedValuesForTheFinalBucketEntry()) {
                    // place found entries into the right array position
                    // look for the position of the entry that matches this merge-to VaultEntryType
                    //
                    outputFinalBucketList.get(outputFinalBucketList.size() - 1).setOnehotInformationArray(ARRAY_ENTRIES_AFTER_MERGE_TO.get(pair.getKey()), pair.getValue());
                }
            }
        }

        return outputFinalBucketList;
    }

    /**
     * This method collects all data stored inside the BucketEntrys from the
     * given listOfBucketEntrys. The data collected is inside the
     * listOfValuesForTheInterpolator list.
     *
     * @param listOfBucketEntrys This is the list of BucketEntrys that the data
     * will be taken out of.
     * @return A list of linear interpolater relevant data ... this is raw data
     * and has to be formated into a format fit for the interpolateGaps method.
     */
    protected List<Pair<Integer, Pair<VaultEntryType, Double>>> collectInterpolationDataFromBucketEntrys(List<BucketEntry> listOfBucketEntrys) {
        List<Pair<Integer, Pair<VaultEntryType, Double>>> outputList = new ArrayList<>();

        for (BucketEntry bucket : listOfBucketEntrys) {
            // collect all Data from each bucket that has data stored
            if (!bucket.getValuesForTheInterpolator().isEmpty()) {
                outputList.addAll(bucket.getValuesForTheInterpolator());
            }
            // if no data saved then move to the next bucket
        }

        return outputList;
    }

    /**
     * This method collects all pairs in the given list that is equal to the
     * given VaultEntryType.
     *
     * @param sortForThisType This is the VaultEntryType that will be searched
     * for.
     * @param listOfPairs This is the List if pairs that the given
     * VaultEntryType will be searched in.
     * @return This method returns a list of pairs containing only pairs with
     * the given VaultEntryType.
     */
    protected List<Pair<Integer, Pair<VaultEntryType, Double>>> sortDataByTypeForInterpolation(VaultEntryType sortForThisType, List<Pair<Integer, Pair<VaultEntryType, Double>>> listOfPairs) {
        List<Pair<Integer, Pair<VaultEntryType, Double>>> outputList = new ArrayList<>();

        for (Pair<Integer, Pair<VaultEntryType, Double>> pair : listOfPairs) {
            // look for the wanted types
            if (pair.getValue().getKey().equals(sortForThisType)) {
                outputList.add(pair);
            }
            // if the wanted type is not found move to the next pair
        }

        return outputList;
    }

    /**
     * This method searches for the given BucketEntry number inside of the given
     * list of pairs.
     *
     * @param bucketNumber This is the BucketEntry number that will be looked
     * for.
     * @param listOfPairs This is the list of pairs that will be iterated over
     * searching for the BucketEntry number.
     * @return Returns a pair stating whether the BucketEntry number was found
     * and if so at which position inside the given list it is. e.g. Pair(true,
     * 25) or Pair(false, 0).
     */
    protected Pair<Boolean, Integer> checkIfBucketEntryNumberIsAvailableAndAtWhichPositionTheNumberIsFoundInsideTheList(int bucketNumber, List<Pair<Integer, Pair<VaultEntryType, Double>>> listOfPairs) {
        // lists start at position 0
        int positionInsideTheList = 0;

        // search for the bucket number
        for (Pair<Integer, Pair<VaultEntryType, Double>> pair : listOfPairs) {
            if (pair.getKey() == bucketNumber) {
                return new Pair(true, positionInsideTheList);
            }
        }

        return new Pair(false, 0);
    }

    /**
     *
     * This method receives a list that begins with a
     *
     *
     * liste beinhält alles vom ersten auftretten eines wertes bis zum letzten
     * vorkommenden wert anfang ohne NULL und ende ohne NULL
     *
     *
     * TODO für Adrian: Im averageForSmallesBucketSize werden alle werte die für
     * diese methode benötigt werden in eine liste gepackt und später in teil
     * listen unterteilt um dieser methode übergeben zu werden. Die werte werden
     * willkührlich nach fund in die liste aufgenommen und vor der übergabe
     * zwischen den einzelnen werten mit sinn vollen listen einträgen befüllt
     * die mit NULL symbolisieren das noch kein double Wert für diesen
     * BucketEntry vorhanden ist. Die rückgabe dieser methode wird dann zurück
     * in die zugehörigen BucketEntrys (anhand der bucketNumber) in die arrays
     * an der richtigen position eingetragen ... ggf. erst im FinalBucketEntry.
     *
     * Anmerkung von Timm: wegen der Art und weise wie wir die Methode benutzen
     * (Dh wir überprüfen ja nicht nochmal ob die liste korrekt ist, also ob
     * alle typen gleich sind) Sollten wir die Methode vllt private machen ->
     * protected um sie testen zu können
     *
     * @param input
     * @return
     */
    protected List<Pair<Integer, Pair<VaultEntryType, Double>>> interpolateGaps(List<Pair<Integer, Pair<VaultEntryType, Double>>> input) {
        List<Pair<Double, Double>> calcValues = new ArrayList<>();
        List<Pair<Integer, Pair<VaultEntryType, Double>>> result = new ArrayList<>();
        VaultEntryType resultType = null;
        Double tmpValue;
        Integer tmpIndex;

        //prepare the input data for interpolation, exclude null-values
        for (Pair<Integer, Pair<VaultEntryType, Double>> pair : input) {
            if (pair != null && pair.getValue() != null && pair.getKey() != null) {
                tmpIndex = pair.getKey();
                tmpValue = pair.getValue().getValue();
                resultType = pair.getValue().getKey();

                if (tmpValue != null) {
                    calcValues.add(new Pair(tmpIndex.doubleValue(), tmpValue));
                }
            }
        }
        SplineInterpolator sI = new SplineInterpolator(calcValues);

        //compute each value that is null
        for (Pair<Integer, Pair<VaultEntryType, Double>> pair : input) {
            if (pair != null && pair.getValue() != null && pair.getKey() != null) {
                tmpIndex = pair.getKey();
                tmpValue = pair.getValue().getValue();

                if (tmpValue == null) {
                    //interpolation call: tmpValue = interpolate(tmpIndex.doubleValue()); vllt mit Runden?
                    tmpValue = sI.interpolate(tmpIndex.doubleValue());
                }
                result.add(new Pair(tmpIndex, new Pair(resultType, tmpValue)));
            }
        }

        System.out.println(resultType);

        for (int i = 0; i < input.size(); i++) {
            Pair<Integer, Pair<VaultEntryType, Double>> tmp1 = input.get(i);
            Pair<Integer, Pair<VaultEntryType, Double>> tmp2 = result.get(i);
            //System.out.println(tmp1.getKey() + " " + tmp1.getValue().getValue() + " " + tmp2.getValue().getValue());

        }
        System.out.println("interpolation end");

        return result;
    }
}
