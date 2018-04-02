/*
 * Copyright (C) 2018 Chryat1s
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
package de.opendiabetes.vault.processing.buckets;

import de.opendiabetes.vault.container.BucketEntry;
import static de.opendiabetes.vault.container.BucketEventTriggers.*;
import de.opendiabetes.vault.container.VaultEntry;
import de.opendiabetes.vault.container.VaultEntryType;
import static de.opendiabetes.vault.util.TimestampUtils.addMinutesToTimestamp;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import javafx.util.Pair;

/**
 * This class contains all methods dealing with the creation of list of
 * BucketEntrys and updating their internal information.
 *
 * @author Chryat1s
 */
public class ListOfBucketEntriesCreator {

    protected final BucketEntryCreator bucketEntryCreator = new BucketEntryCreator();

    /**
     * This method receives a first bucket number and a list of VaultEntrys.
     * This method will create a BucketEntry out of each given VaultEntry if it
     * is ML-relevant. The resulting list of BucketEntrys will have at least one
     * BucketEntry per timestamp.
     *
     * @param firstBucketNumber This is the BucketEntry number of the first
     * BucketEntry in the resulting list. This int number should be 0 or 1.
     * @param entryList This is the list of VaultEntrys that will be turned into
     * a list of BucketEntrys.
     * @return This method returns a list of BucketEntrys.
     * @throws ParseException
     */
    // timestamp < counter ... create BucketEntry / move to next elem. in list
    // timestamp = counter ... create BucketEntry / move to next elem. in list
    // timestamp > counder ... create emtpy BucketEntry / DO NOT MOVE TO NEXT ELEM. IN LIST
    public List<BucketEntry> createListOfBuckets(int firstBucketNumber, List<VaultEntry> entryList) throws ParseException {
        List<BucketEntry> outputBucketList = new ArrayList<>();
        Date lookForThisDateInsideTheGivenVaultEntryList;

        // case input is empty
        if (entryList.isEmpty()) {
            return outputBucketList;
        } else {
            lookForThisDateInsideTheGivenVaultEntryList = entryList.get(0).getTimestamp();
        }

        // BucketEntry number counter
        int currentBucketEntryNumber = firstBucketNumber;

        // position in the VaultEntry list
        int vaultEntryListPosition = 0;

        // create a BucketEntry for each VaultEntry in the list
        // if there is no VaultEntry for a needed timestamp then an empty BucketEntry is created
        for (int listPosition = 0; listPosition < entryList.size(); listPosition++) {

            // get the timestamp out of the VaultEntry
            Date thisDate = entryList.get(listPosition).getTimestamp();
            // run comparison
            Integer dateComparisonResult = compareTwoDates(thisDate, lookForThisDateInsideTheGivenVaultEntryList);

            if (dateComparisonResult == null) {
                // *****************************************
                // no Date comparison possible ... DEAD_LOCK
                // *****************************************
                throw new Error("Date_comparison_failed_:_DEAD_LOCK");
            } else {
                // get VaultEntry
                VaultEntry thisEntry = entryList.get(listPosition);
                // get VaultEntry information
                boolean isMLrelevant = thisEntry.getType().isMLrelevant();

                // *****************************************************
                // case according to the Timestamp inside the VaultEntry
                // *****************************************************
                switch ((int) dateComparisonResult) {
                    case -1:
                        // found an earlier Date

                        if (isMLrelevant) {
                            // check if last created BucketEntry is an empty BucketEntry
                            Boolean overwrite = checkPreviousBucketEntry(thisEntry.getTimestamp(), outputBucketList);

                            // new BucketEntry
                            BucketEntry newBucketEntry;

                            if (overwrite) {
                                // replace the last BucketEntry in the output list

                                // outputBucketEntry.size() - 1 == currentBucketEntryNumber - 1
                                // TODO remove last thisBucketEntry method
                                //outputBucketList.remove(outputBucketList.size() - 1);
                                // TODO check if this method is ok
                                outputBucketList = removeLastBucketEntry(outputBucketList);
                                // create a new Bucket with the given VaultEntry
                                // the new BucketEntry has the currentBucketEntryNumber from the removed BucketEntry
                                newBucketEntry = bucketEntryCreator.createNewBucketEntry((currentBucketEntryNumber - 1), entryList.get(vaultEntryListPosition));
                                outputBucketList.add(newBucketEntry);

                                // DO NOT UPDATE currentBucketEntryNumber SINCE THE LAST POSITION HAS BEEN OVERWRITTEN
                            } else {
                                // add a new BucketEntry to the output list

                                newBucketEntry = bucketEntryCreator.createNewBucketEntry(currentBucketEntryNumber, entryList.get(vaultEntryListPosition));
                                outputBucketList.add(newBucketEntry);
                                // update currentBucketEntryNumber
                                currentBucketEntryNumber++;
                            }

                        } // else not ML-rev

                        // DO NOT UPDATE lookForThisDateInsideTheGivenVaultEntryList! entryList may contain more VaultEntrys with the same timestamp
                        // move to the next VaultEntry in the list
                        vaultEntryListPosition++;

                        break;

                    case 0:
                        // found the same Date

                        if (isMLrelevant) {
                            // create a new BucketEntry
                            BucketEntry newBucketEntry;

                            newBucketEntry = bucketEntryCreator.createNewBucketEntry(currentBucketEntryNumber, entryList.get(vaultEntryListPosition));
                            outputBucketList.add(newBucketEntry);
                            // update currentBucketEntryNumber
                            currentBucketEntryNumber++;

                            // update lookForThisDateInsideTheGivenVaultEntryList
                            lookForThisDateInsideTheGivenVaultEntryList = addMinutesToTimestamp(lookForThisDateInsideTheGivenVaultEntryList, 1);
                            // move to the next VaultEntry in the list
                            vaultEntryListPosition++;
                        } else {
                            // create a new empty Bucket
                            BucketEntry newBucketEntry;

                            newBucketEntry = bucketEntryCreator.createEmptyBucketEntry(currentBucketEntryNumber, lookForThisDateInsideTheGivenVaultEntryList);
                            outputBucketList.add(newBucketEntry);
                            // update currentBucketEntryNumber
                            currentBucketEntryNumber++;

                            // update lookForThisDateInsideTheGivenVaultEntryList
                            lookForThisDateInsideTheGivenVaultEntryList = addMinutesToTimestamp(lookForThisDateInsideTheGivenVaultEntryList, 1);
                            // move to the next VaultEntry in the list
                            vaultEntryListPosition++;
                        }

                        break;

                    case 1:
                        // found a later Date
                        BucketEntry newBucketEntry;

                        // create a new empty Bucket
                        newBucketEntry = bucketEntryCreator.createEmptyBucketEntry(currentBucketEntryNumber, lookForThisDateInsideTheGivenVaultEntryList);
                        outputBucketList.add(newBucketEntry);
                        // update currentBucketEntryNumber
                        currentBucketEntryNumber++;

                        // update lookForThisDateInsideTheGivenVaultEntryList
                        lookForThisDateInsideTheGivenVaultEntryList = addMinutesToTimestamp(lookForThisDateInsideTheGivenVaultEntryList, 1);

                        // DO NOT UPDATE vaultEntryListPosition! ... the given list position has not been reached yet
                        // listPosition is counted up every iteration ... subtract 1 to not move on in the list
                        listPosition--;

                        break;

                    default:
                        break;
                }
            }
        }

        return outputBucketList;
    }

    /**
     * This method compares the two given Dates with eachother.
     *
     * If the first given Date is before the second given Date this method will
     * return -1. If the first given Date is equal to the second given Date this
     * method will return 0. If the first given Date is after the second given
     * Date this method will return 1.
     *
     * If the Dates are uncomparable null will be returned.
     *
     * @param thisDate This Date will be compared to the comparisoneDate. (First
     * Date in the description.)
     * @param comparisonDate This Date is the Date that the other Date will be
     * compared to. (Second Date in the description.)
     * @return Integer - If thisDate is before comparisonDate -1 will be
     * returned. If thisDate is equal to comparisonDate 0 will be returned. If
     * thisDate is after comparisonDate 1 will be returned. If the two Dates are
     * uncomparable null will be returned.
     */
    protected Integer compareTwoDates(Date thisDate, Date comparisonDate) {

        if (thisDate.before(comparisonDate)) {
            return -1;
        }
        if (thisDate.equals(comparisonDate)) {
            return 0;
        }
        if (thisDate.after(comparisonDate)) {
            return 1;
        }

        // comparison error
        return null;
    }

    /**
     * This method checks if the last BucketEntry inside the given list of
     * BucketEntrys contains needed VaultEntry information for the given Date.
     * If the found VaultEntryType is EMPTY and the Date equals the given Date
     * then it can be overwrtten and true will be returned. If the input list is
     * empty or the BucketEntry contains needed information false will be
     * returned.
     *
     * @param comparisonDate This is the Date that will be compared with the
     * Date inside BucketEntry.
     * @param listToCheckIn This is the list of BucketEntrys that the
     * BucketEntry for the comparison will be taken out of.
     * @return Boolean - This method will return true if the BucketEntry can be
     * overwritten and false if it should not be overwritten.
     */
    protected Boolean checkPreviousBucketEntry(Date comparisonDate, List<BucketEntry> listToCheckIn) {

        // input is empty
        if (listToCheckIn.isEmpty()) {
            return false;
        }

        // get last BucketEntry
        BucketEntry checkThisBucketEntry = listToCheckIn.get(listToCheckIn.size() - 1);

        // get BucketEntry entries
        VaultEntryType thisType = checkThisBucketEntry.getVaultEntry().getType();
        Date thisDate = checkThisBucketEntry.getVaultEntry().getTimestamp();

        // compare the Dates
        Integer comparisonResult = compareTwoDates(thisDate, comparisonDate);

        // if thisType is VaultEntryType.EMPTY and comparisonResult == 0 then return true
        return thisType.equals(VaultEntryType.EMPTY) && comparisonResult == 0;

    }

    /**
     * This method receives a list of BucketEntrys and sets all the needed
     * information into each BucketEntry depending on the inputs from the given
     * BucketEntrys.
     *
     * @param listOfBuckets This is the list of BucketEntrys that will be
     * updated.
     */
    protected void transferBucketEntryValues(List<BucketEntry> listOfBuckets) {
        final int maxArraySize = ARRAY_ENTRY_TRIGGER_HASHMAP.size();

        // *******************************
        // INTERNAL VALUE HANDLING - START
        // *******************************
        // timer
        double[] tempValueTimer = new double[maxArraySize];
        // values
        double[] tempValues = new double[maxArraySize];
        // till next thisBucketEntry
        VaultEntryType[] tempFindNextVaultEntryType = new VaultEntryType[maxArraySize];
        // Fill findNextArray with EMPTY
        Arrays.fill(tempFindNextVaultEntryType, VaultEntryType.EMPTY);

        // for parallel act times computing
        // Pair< VaultEntryType , Pair< ValueTimer, Value >>
        List<Pair<VaultEntryType, Pair<Double, Double>>> tempValuesForRunningComputation = new ArrayList<>();
        // list of values for the interpolation
        // Pair< BucketEntryNumber, Pair< VaultEntryType, Value >>
        List<Pair<Integer, Pair<VaultEntryType, Double>>> tempValuesForTheInterpolator = new ArrayList<>();

        // *****************************
        // INTERNAL VALUE HANDLING - END
        // *****************************
        Date lastFoundDate = null;
        boolean newDateFound = false;
//        final int firstBucketEntryNumber = listOfBuckets.get(0).getBucketNumber();

        // get the BucketEntry
        for (BucketEntry thisBucketEntry : listOfBuckets) {

            // get VaultEntry information from the BucketEntry
            VaultEntryType thisVaultEntryType = thisBucketEntry.getVaultEntry().getType();
            Date thisBucketEntryTimestamp = thisBucketEntry.getVaultEntry().getTimestamp();

            if (lastFoundDate == null) {
                lastFoundDate = thisBucketEntryTimestamp;
                newDateFound = true;
            }
            if (lastFoundDate.before(thisBucketEntryTimestamp)) {
                // reset tempValuesForTheInterpolator
                tempValuesForTheInterpolator = new ArrayList<>();
                // set lastFoundDate
                lastFoundDate = thisBucketEntryTimestamp;
                // set newDateFound
                newDateFound = true;
            }

            // ***********************************
            //  VALUES FOR THE INTERPOLATOR UPDATE
            // ***********************************
            // check for new values inside the tempValuesForTheInterpolator list in the BucketEntry
            if (!thisBucketEntry.getValuesForTheInterpolator().isEmpty()) {
                List<Pair<Integer, Pair<VaultEntryType, Double>>> tempList = new ArrayList<>();
                for (Pair<Integer, Pair<VaultEntryType, Double>> pair : thisBucketEntry.getValuesForTheInterpolator()) {
                    Pair<Integer, Pair<VaultEntryType, Double>> tempPair;
                    tempPair = new Pair(pair.getKey(), new Pair(pair.getValue().getKey(), pair.getValue().getValue()));
                    tempList.add(tempPair);
                }
                tempValuesForTheInterpolator.addAll(tempList);
            }
            // transfer the data from the last BucketEntrys with the same timestamp into the current BucketEntry
            if (!tempValuesForTheInterpolator.isEmpty()) {
                List<Pair<Integer, Pair<VaultEntryType, Double>>> tempList = new ArrayList<>();
                for (Pair<Integer, Pair<VaultEntryType, Double>> pair : tempValuesForTheInterpolator) {
                    Pair<Integer, Pair<VaultEntryType, Double>> tempPair;
                    tempPair = new Pair(pair.getKey(), new Pair(pair.getValue().getKey(), pair.getValue().getValue()));
                    tempList.add(tempPair);
                }
                thisBucketEntry.setValuesForTheInterpolator(tempList);
            }

            // **************************
            // RUNNING COMPUTAIONS UPDATE
            // **************************
            // update all value timers inside each entry before adding new entries
            if (!tempValuesForRunningComputation.isEmpty()) {
                // new temp list with value timer - 1
                List<Pair<VaultEntryType, Pair<Double, Double>>> tempList = new ArrayList<>();
                for (Pair<VaultEntryType, Pair<Double, Double>> pair : tempValuesForRunningComputation) {
                    // Pair< VaultEntryType , Pair< ValueTimer, Value >>
                    Pair<VaultEntryType, Pair<Double, Double>> tempPair = new Pair(pair.getKey(), new Pair(pair.getValue().getKey() - 1, pair.getValue().getValue()));
                    if (tempPair.getValue().getKey() > 0) {
                        // new valid pair
                        tempList.add(tempPair);
                    } else {
                        // do nothing pair is obsolete since timer is 0 or less
                    }
                }
                tempValuesForRunningComputation = tempList;
            }

            // *************
            // ARRAY UPDATES
            // *************
            // iterate through the array positions
            for (int i = 0; i < maxArraySize; i++) {
                VaultEntryType vaultEntryTypeAtThisPosition = null;

                // get the VaultEntryType at this position
                for (VaultEntryType thisType : ARRAY_ENTRY_TRIGGER_HASHMAP.keySet()) {
                    if (ARRAY_ENTRY_TRIGGER_HASHMAP.get(thisType) == i) {
                        vaultEntryTypeAtThisPosition = thisType;
                        break;
                    }  // TODO break out of for?
                }

                // catch NullPointerException
                if (vaultEntryTypeAtThisPosition == null) {
                    throw new Error("No_vaild_VaultEntryType_found!");
                }

                // get VaultEntryType info
                boolean isOneHot = vaultEntryTypeAtThisPosition.isOneHot();
                VaultEntryType mergeToThis = vaultEntryTypeAtThisPosition.getMergeTo();

                // ************
                // TIMER UPDATE
                // ************
                // running timers
                if (newDateFound) {
                    if (tempValueTimer[i] > 0) {
                        tempValueTimer[i] = tempValueTimer[i] - 1;
                    }
                    // when reaching the last thisBucketEntry for this timestamp
                    if ((i + 1) == maxArraySize) {
                        newDateFound = false;
                    }
                }

                // get new timers
                // new greater timer found
                if (thisBucketEntry.getValueTimer(i) > tempValueTimer[i] + 1) {
                    tempValueTimer[i] = thisBucketEntry.getValueTimer(i);
                }
                // new timer found
                if (thisVaultEntryType == vaultEntryTypeAtThisPosition) {
                    tempValueTimer[i] = thisBucketEntry.getValueTimer(i);
                }

                // set timers into the BucketEntry
                thisBucketEntry.setValueTimer(i, tempValueTimer[i]);

                // *******************************
                // FIND NEXT VAULTENTRYTYPE UPDATE
                // *******************************
                // get a new VaultEntryTpye
                if (!thisBucketEntry.getFindNextVaultEntryType(i).equals(VaultEntryType.EMPTY)) {
                    tempFindNextVaultEntryType[i] = thisBucketEntry.getFindNextVaultEntryType(i);
                }

                // set a given VaultEntryType
                if (!tempFindNextVaultEntryType[i].equals(VaultEntryType.EMPTY)) {
                    thisBucketEntry.setFindNextVaultEntryType(i, tempFindNextVaultEntryType[i]);
                }

                // *************
                // VALUE UPDATES
                // *************
                boolean conditionValueTimer = false;
                boolean conditionFindNextVaultEntryType = false;

                // set boolean values for conditions
                if (tempValueTimer[i] == 0) {
                    conditionValueTimer = true;
                }
                if (tempFindNextVaultEntryType[i].equals(VaultEntryType.EMPTY)
                        || !tempFindNextVaultEntryType[i].equals(VaultEntryType.EMPTY)
                        && tempFindNextVaultEntryType[i].equals(thisVaultEntryType)) {
                    conditionFindNextVaultEntryType = true;
                }

                if (isOneHot) {

                    if (thisVaultEntryType.equals(vaultEntryTypeAtThisPosition)) {
                        // new incoming value
                        tempValues[i] = thisBucketEntry.getValues(i);
                    } else {
                        // update other values
                        if (conditionValueTimer && conditionFindNextVaultEntryType) {
                            // correct the FindNextVaultEntryType info
                            // VaultEntryType has been found
                            if (!tempFindNextVaultEntryType[i].equals(VaultEntryType.EMPTY)) {
                                tempFindNextVaultEntryType[i] = VaultEntryType.EMPTY;
                                thisBucketEntry.setFindNextVaultEntryType(i, VaultEntryType.EMPTY);
                            }
                            tempValues[i] = 0.0;
                        } else {
                            tempValues[i] = 1.0;
                        }
                    }

                } else {
                    // not onehot
                    // real values have to be handled according to thier type

                    // ==============================
                    // handling via hashmap / hashset
                    // ==============================
                    if (TRIGGER_EVENT_NOT_ONE_HOT_ACT_TIME_SET.containsKey(thisVaultEntryType)) {

                        // BASAL_PROFILE extra handling
                        if (thisVaultEntryType.equals(VaultEntryType.BASAL_PROFILE)) {
                            tempValues[i] = thisBucketEntry.getValues(i);
                        } else {
                            // new entries are set in the createNewBucketEntry method
                            // add them to the internal tempValuesForRunningComputation list

                            List<Pair<VaultEntryType, Pair<Double, Double>>> tempList = new ArrayList<>();

                            // get the old list
                            for (Pair<VaultEntryType, Pair<Double, Double>> pair : tempValuesForRunningComputation) {
                                // Pair< VaultEntryType , Pair< ValueTimer, Value >>
                                Pair<VaultEntryType, Pair<Double, Double>> tempPair;
                                tempPair = new Pair(pair.getKey(), new Pair(pair.getValue().getKey(), pair.getValue().getValue()));
                                tempList.add(tempPair);
                            }
                            // get the new list
                            for (Pair<VaultEntryType, Pair<Double, Double>> pair : thisBucketEntry.getValuesForRunningComputation()) {
                                // Pair< VaultEntryType , Pair< ValueTimer, Value >>
                                Pair<VaultEntryType, Pair<Double, Double>> tempPair;
                                tempPair = new Pair(pair.getKey(), new Pair(pair.getValue().getKey(), pair.getValue().getValue()));
                                tempList.add(tempPair);
                            }
                            // save the new list with old and new pairs
                            tempValuesForRunningComputation = tempList;

                            // DO NOT SET onehotInformationArray[i] HERE BECAUSE THE VALUE HAS TO BE COMPUTED (calculateAverageForSmallestBucketSize method)
                        }

                    } else if (TRIGGER_EVENT_NOT_ONE_HOT_ACT_TIME_GIVEN.contains(thisVaultEntryType)) {
                        // new entries are set in the createNewBucketEntry method
                        // add them to the internal tempValuesForRunningComputation list

                        List<Pair<VaultEntryType, Pair<Double, Double>>> tempList = new ArrayList<>();

                        // get the old list
                        for (Pair<VaultEntryType, Pair<Double, Double>> pair : tempValuesForRunningComputation) {
                            // Pair< VaultEntryType , Pair< ValueTimer, Value >>
                            Pair<VaultEntryType, Pair<Double, Double>> tempPair;
                            tempPair = new Pair(pair.getKey(), new Pair(pair.getValue().getKey(), pair.getValue().getValue()));
                            tempList.add(tempPair);
                        }
                        // get the new list
                        for (Pair<VaultEntryType, Pair<Double, Double>> pair : thisBucketEntry.getValuesForRunningComputation()) {
                            // Pair< VaultEntryType , Pair< ValueTimer, Value >>
                            Pair<VaultEntryType, Pair<Double, Double>> tempPair;
                            tempPair = new Pair(pair.getKey(), new Pair(pair.getValue().getKey(), pair.getValue().getValue()));
                            tempList.add(tempPair);
                        }
                        // save the new list with old and new pairs
                        tempValuesForRunningComputation = tempList;

                        // DO NOT SET onehotInformationArray[i] HERE BECAUSE THE VALUE HAS TO BE COMPUTED (calculateAverageForSmallestBucketSize method)
                    } else if (TRIGGER_EVENT_NOT_ONE_HOT_ACT_TIME_TILL_NEXT_EVENT.contains(thisVaultEntryType)) {
                        // TRIGGER_EVENT_NOT_ONE_HOT_ACT_TIME_TILL_NEXT_EVENT : value is set when BucketEntry is created
                        tempValues[i] = thisBucketEntry.getValues(i);

                    } else if (TRIGGER_EVENT_NOT_ONE_HOT_ACT_TIME_ONE.contains(thisVaultEntryType)) {
                        // TRIGGER_EVENT_NOT_ONE_HOT_ACT_TIME_ONE : value is set when BucketEntry is created
                        tempValues[i] = thisBucketEntry.getValues(i);

                    } else if (TRIGGER_EVENT_NOT_ONE_HOT_VALUE_IS_A_TIMESTAMP.contains(thisVaultEntryType)) {
                        tempValues[i] = thisBucketEntry.getValues(i);
                        // onehotInformationArray[i] = ???

                    } else {
                        // there is no new value in this BucketEntry position
                        tempValues[i] = thisBucketEntry.getValues(i);
                    }

                }

                // set the BucketEntry value
                thisBucketEntry.setValues(i, tempValues[i]);
            }
        }
    }

    /**
     * This method receives a list of BucketEntrys and removes the last entry of
     * the list. This method creates a new BucketEntry for each BucketEntry that
     * is to stay inside the list.
     *
     * @param listOfBuckets This is the list of BucketEntrys from which the last
     * BucketEntry will be removed.
     * @return This method returns the given list without the last BucketEntry.
     */
    protected List<BucketEntry> removeLastBucketEntry(List<BucketEntry> listOfBuckets) {
        List<BucketEntry> outputList = new ArrayList<>();

        for (int i = 0; i < (listOfBuckets.size() - 1); i++) {
            BucketEntry thisEntry = listOfBuckets.get(i);

            BucketEntry newEntry = bucketEntryCreator.recreateBucketEntry(thisEntry);

            // add to the outputList
            outputList.add(newEntry);
        }

        return outputList;
    }
}
