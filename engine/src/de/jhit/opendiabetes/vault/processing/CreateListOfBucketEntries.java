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
package de.jhit.opendiabetes.vault.processing;

import de.jhit.opendiabetes.vault.container.BucketEntry;
import static de.jhit.opendiabetes.vault.container.BucketEventTriggers.*;
import de.jhit.opendiabetes.vault.container.VaultEntry;
import de.jhit.opendiabetes.vault.container.VaultEntryType;
import static de.jhit.opendiabetes.vault.util.TimestampUtils.addMinutesToTimestamp;
import static de.jhit.opendiabetes.vault.util.TimestampUtils.createCleanTimestamp;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import javafx.util.Pair;

/**
 *
 * @author Chryat1s
 */
public class CreateListOfBucketEntries {
    
    protected final CreateBucketEntries instance = new CreateBucketEntries();
    
    // part of the information array for ML-rev + one hot
    private final int ML_REV_AND_ONE_HOT = TRIGGER_EVENT_ACT_TIME_GIVEN.size() + TRIGGER_EVENT_ACT_TIME_TILL_NEXT_EVENT.size()
            + TRIGGER_EVENT_ACT_TIME_ONE.size() + TRIGGER_EVENTS_NOT_YET_SET.size();
    // part of the information array for ML-rev + NOT one hot
    // this part comes after the ML_REV_AND_ONE_HOT part
    private final int ML_REV_AND_NOT_ONE_HOT = TRIGGER_EVENT_NOT_ONE_HOT_ACT_TIME_SET.size() + TRIGGER_EVENT_NOT_ONE_HOT_ACT_TIME_GIVEN.size()
            + TRIGGER_EVENT_NOT_ONE_HOT_ACT_TIME_TILL_NEXT_EVENT.size() + TRIGGER_EVENT_NOT_ONE_HOT_ACT_TIME_ONE.size()
            + TRIGGER_EVENT_NOT_ONE_HOT_VALUE_IS_A_TIMESTAMP.size();

    // Date check for setBucketArrayInformation (merge-to and onehot)
    private Date lastDate = null;
    // boolean switch for setBucketArrayInformation (merge-to and onehot)
    private boolean sameDatesGetNoTimerArrayUpdate_MLRevAndOneHot = false;
    private boolean sameDatesGetNoTimerArrayUpdate_MLRevAndNOTOneHot = false;

    // this BucketEntry replaces all the needed internal lists and arrays
    private final BucketEntry bucketEntryForInternalComputation;
    protected CreateListOfBucketEntries() throws ParseException {
        // BucketEntry number and timestamp are random Values
        this.bucketEntryForInternalComputation = new BucketEntry(-5, new VaultEntry(VaultEntryType.EMPTY, createCleanTimestamp("0000.01.01-00:00", "yyyy.MM.dd-HH:mm")));
    }
    
    /**
     * 
     * @param firstBucketNumber
     * @param entryList
     * @return
     * @throws ParseException 
     */
    public List<BucketEntry> createListOfBuckets(int firstBucketNumber, List<VaultEntry> entryList) throws ParseException {
        List<BucketEntry> outputBucketList = new ArrayList<>();
        Date lookForThisDateInsideTheGivenVaultEntryList;
        
        // case input is empty
        if (entryList.isEmpty()) {return outputBucketList;}
        else {lookForThisDateInsideTheGivenVaultEntryList = entryList.get(0).getTimestamp();}

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
                boolean isOneHot = thisEntry.getType().isOneHot();
                
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
                                outputBucketList.remove(outputBucketList.size() - 1);                                           // TODO remove last thisBucketEntry method
                                // create a new Bucket with the given VaultEntry
                                // the new BucketEntry has the currentBucketEntryNumber from the removed BucketEntry
                                newBucketEntry = instance.createNewBucket((currentBucketEntryNumber - 1), entryList.get(vaultEntryListPosition));
                                outputBucketList.add(newBucketEntry);

                                // DO NOT UPDATE currentBucketEntryNumber SINCE THE LAST POSITION HAS BEEN OVERWRITTEN
                            } else {
                                // add a new BucketEntry to the output list
                                
                                newBucketEntry = instance.createNewBucket(currentBucketEntryNumber, entryList.get(vaultEntryListPosition));
                                outputBucketList.add(newBucketEntry);
                                // update currentBucketEntryNumber
                                currentBucketEntryNumber++;
                            }
                            
                            // call this method BEFORE setting the new lookForThisDateInsideTheGivenVaultEntryList date
            //                setBucketArrayInformation(firstBucketNumber, lookForThisDateInsideTheGivenVaultEntryList, newBucketEntry);
                            
                        } // else not ML-rev
                        
                        // DO NOT UPDATE lookForThisDateInsideTheGivenVaultEntryList! entryList may contain more VaultEntrys with the same timestamp
                        // move to the next VaultEntry in the list
                        vaultEntryListPosition++;
                        
                        break;
                        
                    case 0:
                        // found the same Date
                        
                        if  (isMLrelevant) {
                            // create a new BucketEntry
                            BucketEntry newBucketEntry;
                            
                            newBucketEntry = instance.createNewBucket(currentBucketEntryNumber, entryList.get(vaultEntryListPosition));
                            outputBucketList.add(newBucketEntry);
                            // update currentBucketEntryNumber
                            currentBucketEntryNumber++;

                            // call this method BEFORE setting the new lookForThisDateInsideTheGivenVaultEntryList date
            //                setBucketArrayInformation(firstBucketNumber, lookForThisDateInsideTheGivenVaultEntryList, newBucketEntry);

                            // update lookForThisDateInsideTheGivenVaultEntryList
                            lookForThisDateInsideTheGivenVaultEntryList = addMinutesToTimestamp(lookForThisDateInsideTheGivenVaultEntryList, 1);
                            // move to the next VaultEntry in the list
                            vaultEntryListPosition++;
                        } else {
                            // create a new empty Bucket
                            BucketEntry newBucketEntry;
                            
                            newBucketEntry = instance.createEmptyBucket(currentBucketEntryNumber, lookForThisDateInsideTheGivenVaultEntryList);
                            outputBucketList.add(newBucketEntry);
                            // update currentBucketEntryNumber
                            currentBucketEntryNumber++;

                            // call this method BEFORE setting the new lookForThisDateInsideTheGivenVaultEntryList date
            //                setBucketArrayInformation(firstBucketNumber, lookForThisDateInsideTheGivenVaultEntryList, newBucketEntry);

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
                        newBucketEntry = instance.createEmptyBucket(currentBucketEntryNumber, lookForThisDateInsideTheGivenVaultEntryList);
                        outputBucketList.add(newBucketEntry);
                        // update currentBucketEntryNumber
                        currentBucketEntryNumber++;

                        // call this method BEFORE setting the new lookForThisDateInsideTheGivenVaultEntryList date
            //            setBucketArrayInformation(firstBucketNumber, lookForThisDateInsideTheGivenVaultEntryList, newBucketEntry);

                        // update lookForThisDateInsideTheGivenVaultEntryList
                        lookForThisDateInsideTheGivenVaultEntryList = addMinutesToTimestamp(lookForThisDateInsideTheGivenVaultEntryList, 1);

                        // DO NOT UPDATE vaultEntryListPosition! ... the given list position has not been reached yet
                        
                        // listPosition is counted up every iteration ... subtract 1 to not move on in the list
                        listPosition--;
                        
                        break;
                        
                }
            }
        }
        
        return outputBucketList;
    }
    
    /**
     * This method compares the two given Dates with eachother.
     * 
     * If the first given Date is before the second given Date this method will return -1.
     * If the first given Date is equal to the second given Date this method will return 0.
     * If the first given Date is after the second given Date this method will return 1.
     * 
     * If the Dates are uncomparable null will be returned.
     * 
     * @param thisDate This Date will be compared to the comparisoneDate. (First Date in the description.)
     * @param comparisonDate This Date is the Date that the other Date will be compared to. (Second Date in the description.)
     * @return Integer - If thisDate is before comparisonDate -1 will be returned.
     *                   If thisDate is equal to comparisonDate 0 will be returned.
     *                   If thisDate is after comparisonDate 1 will be returned.
     *                   If the two Dates are uncomparable null will be returned.
     */
    protected Integer compareTwoDates(Date thisDate, Date comparisonDate) {
        
        if (thisDate.before(comparisonDate)) {return -1;}
        if (thisDate.equals(comparisonDate)) {return 0;}
        if (thisDate.after(comparisonDate)) {return 1;}
        
        // comparison error
        return null;
    }
    
    /**
     * This method checks if the last BucketEntry inside the given list of BucketEntrys contains needed VaultEntry information for the given Date.
     * If the found VaultEntryType is EMPTY and the Date equals the given Date then it can be overwrtten and true will be returned.
     * If the input list is empty or the BucketEntry contains needed information false will be returned.
     * 
     * @param comparisonDate This is the Date that will be compared with the Date inside BucketEntry.
     * @param listToCheckIn This is the list of BucketEntrys that the BucketEntry for the comparison will be taken out of.
     * @return Boolean - This method will return true if the BucketEntry can be overwritten and false if it should not be overwritten.
     */
    protected Boolean checkPreviousBucketEntry(Date comparisonDate, List<BucketEntry> listToCheckIn) {

        // input is empty
        if (listToCheckIn.isEmpty()) {return false;}
        
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
     * 
     * @param listOfBuckets 
     */
    protected void setBucketArrayInformation_new(List<BucketEntry> listOfBuckets) {
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
        List<Pair<VaultEntryType, Pair<Double, Double>>> valuesForRunningComputation = new ArrayList<>();
        // list of values for the interpolation
        // Pair< BucketEntryNumber, Pair< VaultEntryType, Value >>
        List<Pair<Integer, Pair<VaultEntryType, Double>>> valuesForTheInterpolator = new ArrayList<>();
        
        // *****************************
        // INTERNAL VALUE HANDLING - END
        // *****************************
        
        Date lastFoundDate = null;
        boolean newDateFound = false;
        final int firstBucketEntryNumber = listOfBuckets.get(0).getBucketNumber();

        // get the BucketEntry
        for (BucketEntry thisBucketEntry : listOfBuckets) {

            // get VaultEntry information from the BucketEntry
            VaultEntryType thisVaultEntryType = thisBucketEntry.getVaultEntry().getType();
            Date thisBucketEntryTimestamp = thisBucketEntry.getVaultEntry().getTimestamp();
            
            if (lastFoundDate == null) {lastFoundDate = thisBucketEntryTimestamp; newDateFound = true;}
            if (lastFoundDate.before(thisBucketEntryTimestamp)) {
                // reset valuesForTheInterpolator
                valuesForTheInterpolator = new ArrayList<>();
                // set lastFoundDate
                lastFoundDate = thisBucketEntryTimestamp;
                // set newDateFound
                newDateFound = true;
            }
            
            // ***********************************
            //  VALUES FOR THE INTERPOLATOR UPDATE
            // ***********************************
            
            // check for new values inside the valuesForTheInterpolator list in the BucketEntry
            if (!thisBucketEntry.getListOfValuesForTheInterpolator().isEmpty()) {
                List<Pair<Integer, Pair<VaultEntryType, Double>>> tempList = new ArrayList<>();
                for (Pair<Integer, Pair<VaultEntryType, Double>> pair : thisBucketEntry.getListOfValuesForTheInterpolator()){
                    Pair<Integer, Pair<VaultEntryType, Double>> tempPair;
                    tempPair = new Pair(pair.getKey(), new Pair(pair.getValue().getKey(), pair.getValue().getValue()));
                    tempList.add(tempPair);
                }
                valuesForTheInterpolator.addAll(tempList);
            }
            // transfer the data from the last BucketEntrys with the same timestamp into the current BucketEntry
            if (!valuesForTheInterpolator.isEmpty()) {
                List<Pair<Integer, Pair<VaultEntryType, Double>>> tempList = new ArrayList<>();
                for (Pair<Integer, Pair<VaultEntryType, Double>> pair : valuesForTheInterpolator){
                    Pair<Integer, Pair<VaultEntryType, Double>> tempPair;
                    tempPair = new Pair(pair.getKey(), new Pair(pair.getValue().getKey(), pair.getValue().getValue()));
                    tempList.add(tempPair);
                }
                thisBucketEntry.setListOfValuesForTheInterpolator(tempList);
            }
            
            // **************************
            // RUNNING COMPUTAIONS UPDATE
            // **************************

            // update all value timers inside each entry before adding new entries
            if (!valuesForRunningComputation.isEmpty()) {
                // new temp list with value timer - 1
                List<Pair<VaultEntryType, Pair<Double, Double>>> tempList = new ArrayList<>();
                for (Pair<VaultEntryType, Pair<Double, Double>> pair : valuesForRunningComputation) {
                    // Pair< VaultEntryType , Pair< ValueTimer, Value >>
                    Pair<VaultEntryType, Pair<Double, Double>> tempPair = new Pair(pair.getKey(), new Pair(pair.getValue().getKey() - 1, pair.getValue().getValue()));
                    if (tempPair.getValue().getKey() > 0) {
                        // new valid pair
                        tempList.add(tempPair);
                    } else {
                        // do nothing pair is obsolete since timer is 0 or less
                    }
                }
                valuesForRunningComputation = tempList;
            }
            
            // *************
            // ARRAY UPDATES
            // *************

            // iterate through the array positions                    
            for (int i = 0; i < maxArraySize; i++){
                VaultEntryType vaultEntryTypeAtThisPosition = null;

                // get the VaultEntryType at this position
                for (VaultEntryType thisType : ARRAY_ENTRY_TRIGGER_HASHMAP.keySet()){
                        if (ARRAY_ENTRY_TRIGGER_HASHMAP.get(thisType) == i) {vaultEntryTypeAtThisPosition = thisType; break;}  // TODO break out of for?
                }
                
                // catch NullPointerException
                if (vaultEntryTypeAtThisPosition == null) {throw new Error("No_vaild_VaultEntryType_found!");}

                // get VaultEntryType info
                boolean isOneHot = vaultEntryTypeAtThisPosition.isOneHot();
                VaultEntryType mergeToThis = vaultEntryTypeAtThisPosition.mergeTo();
                
                // ************
                // TIMER UPDATE
                // ************
                
                // running timers
                if(newDateFound) {
                    if (tempValueTimer[i] > 0) {tempValueTimer[i] = tempValueTimer[i] - 1;}
                    // when reaching the last thisBucketEntry for this timestamp 
                    if ((i + 1) == maxArraySize) {newDateFound = false;}
                }
                
                // get new timers
                // new greater timer found
                if (thisBucketEntry.getTimeCountDown(i) > tempValueTimer[i] + 1 ) {tempValueTimer[i] = thisBucketEntry.getTimeCountDown(i);}
                // new timer found
                if (thisVaultEntryType == vaultEntryTypeAtThisPosition) {tempValueTimer[i] = thisBucketEntry.getTimeCountDown(i);}
                
                // set timers into the BucketEntry
                thisBucketEntry.setTimeCountDown(i, tempValueTimer[i]);
                
                // *******************************
                // FIND NEXT VAULTENTRYTYPE UPDATE
                // *******************************
                
                // get a new VaultEntryTpye
                if (!thisBucketEntry.getFindNextArray(i).equals(VaultEntryType.EMPTY)) {tempFindNextVaultEntryType[i] = thisBucketEntry.getFindNextArray(i);}
                
                // set a given VaultEntryType
                if (!tempFindNextVaultEntryType[i].equals(VaultEntryType.EMPTY)) {thisBucketEntry.setFindNextArray(i, tempFindNextVaultEntryType[i]);}
                
                // *************
                // VALUE UPDATES
                // *************
                
                boolean conditionValueTimer = false;
                boolean conditionFindNextVaultEntryType = false;

                // set boolean values for conditions
                if (tempValueTimer[i] == 0) {conditionValueTimer = true;}
                if (tempFindNextVaultEntryType[i].equals(VaultEntryType.EMPTY) 
                        || !tempFindNextVaultEntryType[i].equals(VaultEntryType.EMPTY) 
                            && tempFindNextVaultEntryType[i].equals(thisVaultEntryType)) {conditionFindNextVaultEntryType = true;}
                        
                if (isOneHot) {
                    
                    if (thisVaultEntryType.equals(vaultEntryTypeAtThisPosition)) {
                        // new incoming value
                        tempValues[i] = thisBucketEntry.getOnehotInformationArray(i);
                    } else {
                        // update other values
                        if (conditionValueTimer && conditionFindNextVaultEntryType) {
                            // correct the FindNextVaultEntryType info 
                            // VaultEntryType has been found
                            if (!tempFindNextVaultEntryType[i].equals(VaultEntryType.EMPTY)) {
                                tempFindNextVaultEntryType[i] = VaultEntryType.EMPTY;
                                thisBucketEntry.setFindNextArray(i, VaultEntryType.EMPTY);
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
                            tempValues[i] = thisBucketEntry.getOnehotInformationArray(i);
                        } else {
                            // new entries are set in the createNewBucket method
                            // add them to the internal valuesForRunningComputation list
                            
                            List<Pair<VaultEntryType, Pair<Double, Double>>> tempList = new ArrayList<>();
                            
                            // get the old list
                            for (Pair<VaultEntryType, Pair<Double, Double>> pair : valuesForRunningComputation) {
                                // Pair< VaultEntryType , Pair< ValueTimer, Value >>
                                Pair<VaultEntryType, Pair<Double, Double>> tempPair;
                                tempPair = new Pair(pair.getKey(), new Pair(pair.getValue().getKey(), pair.getValue().getValue()));
                                tempList.add(tempPair);
                            }
                            // get the new list
                            for (Pair<VaultEntryType, Pair<Double, Double>> pair : thisBucketEntry.getRunningComputation()) {
                                // Pair< VaultEntryType , Pair< ValueTimer, Value >>
                                Pair<VaultEntryType, Pair<Double, Double>> tempPair;
                                tempPair = new Pair(pair.getKey(), new Pair(pair.getValue().getKey(), pair.getValue().getValue()));
                                tempList.add(tempPair);
                            }
                            // save the new list with old and new pairs
                            valuesForRunningComputation = tempList;
                            
                            // DO NOT SET onehotInformationArray[i] HERE BECAUSE THE VALUE HAS TO BE COMPUTED (calculateAverageForSmallestBucketSize method)
                        }
                        
                    } else if (TRIGGER_EVENT_NOT_ONE_HOT_ACT_TIME_GIVEN.contains(thisVaultEntryType)) {
                        // new entries are set in the createNewBucket method
                        // add them to the internal valuesForRunningComputation list

                        List<Pair<VaultEntryType, Pair<Double, Double>>> tempList = new ArrayList<>();

                        // get the old list
                        for (Pair<VaultEntryType, Pair<Double, Double>> pair : valuesForRunningComputation) {
                            // Pair< VaultEntryType , Pair< ValueTimer, Value >>
                            Pair<VaultEntryType, Pair<Double, Double>> tempPair;
                            tempPair = new Pair(pair.getKey(), new Pair(pair.getValue().getKey(), pair.getValue().getValue()));
                            tempList.add(tempPair);
                        }
                        // get the new list
                        for (Pair<VaultEntryType, Pair<Double, Double>> pair : thisBucketEntry.getRunningComputation()) {
                            // Pair< VaultEntryType , Pair< ValueTimer, Value >>
                            Pair<VaultEntryType, Pair<Double, Double>> tempPair;
                            tempPair = new Pair(pair.getKey(), new Pair(pair.getValue().getKey(), pair.getValue().getValue()));
                            tempList.add(tempPair);
                        }
                        // save the new list with old and new pairs
                        valuesForRunningComputation = tempList;

                        // DO NOT SET onehotInformationArray[i] HERE BECAUSE THE VALUE HAS TO BE COMPUTED (calculateAverageForSmallestBucketSize method)
                        
                    } else if (TRIGGER_EVENT_NOT_ONE_HOT_ACT_TIME_TILL_NEXT_EVENT.contains(thisVaultEntryType)){
                        // TRIGGER_EVENT_NOT_ONE_HOT_ACT_TIME_TILL_NEXT_EVENT : value is set when BucketEntry is created
                        tempValues[i] = thisBucketEntry.getOnehotInformationArray(i);
                        
                    } else if (TRIGGER_EVENT_NOT_ONE_HOT_ACT_TIME_ONE.contains(thisVaultEntryType)) {
                        // TRIGGER_EVENT_NOT_ONE_HOT_ACT_TIME_ONE : value is set when BucketEntry is created
                        tempValues[i] = thisBucketEntry.getOnehotInformationArray(i);
                        
                    } else if (TRIGGER_EVENT_NOT_ONE_HOT_VALUE_IS_A_TIMESTAMP.contains(thisVaultEntryType)) {
                        tempValues[i] = thisBucketEntry.getOnehotInformationArray(i);
                        // onehotInformationArray[i] = ???
                        
                    } else {
                        // there is no new value in this BucketEntry position
                        tempValues[i] = thisBucketEntry.getOnehotInformationArray(i);
                    }
                    
                }
                
                // set the BucketEntry value
                thisBucketEntry.setOnehotInformationArray(i, tempValues[i]);
            }
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
     * @param currentBucketEntryDate This is the current Date from the method createListOfBuckets
     * (timecounter).
     * @param currentBucketEntry This is the BucketEntry that will have it's arrays updated.
     */
    protected void setBucketArrayInformation_old(int firstBucketNumber, Date currentBucketEntryDate, BucketEntry currentBucketEntry) {

        // this prevents that the first BucketEntry has the chance of couting down the timer.
        // sameDatesGetNoTimerArrayUpdate_MLRevAndOneHot is initially set to false.
        if (currentBucketEntry.getBucketNumber() == firstBucketNumber + 1) {
            sameDatesGetNoTimerArrayUpdate_MLRevAndOneHot = true;
            sameDatesGetNoTimerArrayUpdate_MLRevAndNOTOneHot = true;
        }

        // since it is possible to have multiple timestamps with the same currentBucketEntryDate the timers should not be updated untill the next minute has started.
        // set lastDate on the first call
        if (lastDate == null) {
            lastDate = currentBucketEntryDate;
        }
        // if lastDate is 2 minutes off then there is a new line of timestamps starting
        // e.g. lastDate = 00:01 and currentBucketEntryDate = 00:02 then bucketEntrys for the timestamp of 00:01 are being created.
        //      if lastDate = 00:01 and currentBucketEntryDate = 00:03 then bucketEntrys with the timestamp of 00:02
        //          are being created and that's why lastdate need to be updated.
        if (addMinutesToTimestamp(lastDate, 2).equals(currentBucketEntryDate)) {
            lastDate = addMinutesToTimestamp(lastDate, 1);            // TODO not reached with EMPTY
            sameDatesGetNoTimerArrayUpdate_MLRevAndOneHot = true;
            sameDatesGetNoTimerArrayUpdate_MLRevAndNOTOneHot = true;
            // new timestamp == new list
            bucketEntryForInternalComputation.setListOfValuesForTheInterpolator(new ArrayList<>());
        }                         // TODO not reached with EMPTY

        // set internal arrays through 1st BucketEntry
        if (currentBucketEntry.getBucketNumber() == firstBucketNumber) {
            for (int i = 0; i < BucketEntry.getNumberOfVaultEntryTriggerTypes(); i++) {
                bucketEntryForInternalComputation.setTimeCountDown(i, currentBucketEntry.getTimeCountDown(i));
                bucketEntryForInternalComputation.setOnehotInformationArray(i, currentBucketEntry.getOnehotInformationArray(i));
                bucketEntryForInternalComputation.setFindNextArray(i, currentBucketEntry.getFindNextArray(i));
            }
            if (!currentBucketEntry.getRunningComputation().isEmpty()) {
                List<Pair<VaultEntryType, Pair<Double, Double>>> tmpList = currentBucketEntry.getRunningComputation();
                bucketEntryForInternalComputation.setRunningComputation(tmpList);
            }
            if (!currentBucketEntry.getListOfValuesForTheInterpolator().isEmpty()) {
                List<Pair<Integer, Pair<VaultEntryType, Double>>> tmpList = currentBucketEntry.getListOfValuesForTheInterpolator();
                bucketEntryForInternalComputation.setListOfValuesForTheInterpolator(tmpList);
            }

        } else {
            // after 1st BucketEntry

            // check for new values inside the valuesForTheInterpolator list in the BucketEntry
            if (!currentBucketEntry.getListOfValuesForTheInterpolator().isEmpty()) {
                List<Pair<Integer, Pair<VaultEntryType, Double>>> tmpList = currentBucketEntry.getListOfValuesForTheInterpolator();
                bucketEntryForInternalComputation.setListOfValuesForTheInterpolator(tmpList);
            }
            // transfer the data from the last BucketEntrys with the same timestamp into the current BucketEntry
            if (!bucketEntryForInternalComputation.getListOfValuesForTheInterpolator().isEmpty()) {
                List<Pair<Integer, Pair<VaultEntryType, Double>>> tmpList = bucketEntryForInternalComputation.getListOfValuesForTheInterpolator();
                currentBucketEntry.setListOfValuesForTheInterpolator(tmpList);
            }

            // timer countdown for all entries inside the runningComputation list this is only done here before adding new upcoming entries
            if (!bucketEntryForInternalComputation.getRunningComputation().isEmpty()) {                
                // new temp list with timer - 1
                List<Pair<VaultEntryType, Pair<Double, Double>>> tempList = new ArrayList<>();
                for (Pair<VaultEntryType, Pair<Double, Double>> pair : bucketEntryForInternalComputation.getRunningComputation()) {
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
                bucketEntryForInternalComputation.setRunningComputation(tempList);
            }

            // =======================================================
            // this part only goes through the ML-rev and one hot part
            // =======================================================
            
            for (int i = 0; i < ML_REV_AND_ONE_HOT; i++) {
                // DO NOT REPEAT TIMER ARRAY UPDATES ON SAME TIMESTAMP
                if (sameDatesGetNoTimerArrayUpdate_MLRevAndOneHot) {

                    // set false to not enter this part till lastDate update
                    // set this after the first run of all array positions
                    if (i == ML_REV_AND_ONE_HOT - 1) {
                        sameDatesGetNoTimerArrayUpdate_MLRevAndOneHot = false;
                    }
                    // set timers
                    if (bucketEntryForInternalComputation.getTimeCountDown(i) > 0) {
                        bucketEntryForInternalComputation.setTimeCountDown(i, bucketEntryForInternalComputation.getTimeCountDown(i) - 1);
                    }
                }
                //
                // update info array stats
                // initial onehots are set when the Bucket is created
                //
                // set timer
                if (currentBucketEntry.getTimeCountDown(i) > bucketEntryForInternalComputation.getTimeCountDown(i)
                        && ARRAY_ENTRY_TRIGGER_HASHMAP.get(currentBucketEntry.getVaultEntry().getType()) == i) {
                    bucketEntryForInternalComputation.setTimeCountDown(i, currentBucketEntry.getTimeCountDown(i));
                }            /////

                // set onehot to false
                if (bucketEntryForInternalComputation.getTimeCountDown(i) == 0 && bucketEntryForInternalComputation.getFindNextArray(i).equals(VaultEntryType.EMPTY)) {
                    bucketEntryForInternalComputation.setOnehotInformationArray(i, 0);
                }
                // set onehot to true
                if (bucketEntryForInternalComputation.getTimeCountDown(i) >= 1) {
                    bucketEntryForInternalComputation.setOnehotInformationArray(i, 1);
                }
                // check for "till next array"
                // VaultEntryType is the standard for an empty BucketEntry
                if (!bucketEntryForInternalComputation.getFindNextArray(i).equals(VaultEntryType.EMPTY)
                        && bucketEntryForInternalComputation.getFindNextArray(i).equals(currentBucketEntry.getVaultEntry().getType())) {
                    bucketEntryForInternalComputation.setOnehotInformationArray(i, 0);
                    bucketEntryForInternalComputation.setFindNextArray(i, VaultEntryType.EMPTY);
                }

                // set findNextEntry
                if (!currentBucketEntry.getFindNextArray(i).equals(VaultEntryType.EMPTY)
                        && !currentBucketEntry.getFindNextArray(i).equals(bucketEntryForInternalComputation.getFindNextArray(i))) {
                    bucketEntryForInternalComputation.setFindNextArray(i, currentBucketEntry.getFindNextArray(i));
                }

                //
                // update BucketEntry arrays
                //
                // set timer
                // TODO if case is wrong???
                // if this timer is longer than the one saved in the BucketEntry take this one
                // if this timer is equal to the one saven in the BucketEntry - 1
                //      and this VaultEntryType is not onehot then update the BucketEntry (onehot might have just been set during the creation of the new BucketEntry).
                if (bucketEntryForInternalComputation.getTimeCountDown(i) > currentBucketEntry.getTimeCountDown(i)
                        || bucketEntryForInternalComputation.getTimeCountDown(i) == currentBucketEntry.getTimeCountDown(i) - 1 && !ARRAY_ENTRY_TRIGGER_HASHMAP.containsKey(currentBucketEntry.getVaultEntry().getType())) {
                    currentBucketEntry.setTimeCountDown(i, bucketEntryForInternalComputation.getTimeCountDown(i));
                }

                // set onehotInformationArray
                currentBucketEntry.setOnehotInformationArray(i, bucketEntryForInternalComputation.getOnehotInformationArray(i));
                // set findNextArray
                // if findNextArray is EMPTY then it is filled with the needed information
                // if findNextArray is filled with something else then nothing has to be done
                if (currentBucketEntry.getFindNextArray(i).equals(VaultEntryType.EMPTY)) {
                    currentBucketEntry.setFindNextArray(i, bucketEntryForInternalComputation.getFindNextArray(i));
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
                    if (bucketEntryForInternalComputation.getTimeCountDown(i) > 0) {
                        bucketEntryForInternalComputation.setTimeCountDown(i, bucketEntryForInternalComputation.getTimeCountDown(i) - 1);
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
                if (currentBucketEntry.getTimeCountDown(i) > bucketEntryForInternalComputation.getTimeCountDown(i)
                        && !currentBucketEntry.getVaultEntry().getType().equals(VaultEntryType.EMPTY)
                        && ARRAY_ENTRY_TRIGGER_HASHMAP.get(currentBucketEntry.getVaultEntry().getType()) == i) {
                    bucketEntryForInternalComputation.setTimeCountDown(i, currentBucketEntry.getTimeCountDown(i));
                }

                // set Vaule to 0
                if (bucketEntryForInternalComputation.getTimeCountDown(i) == 0 && bucketEntryForInternalComputation.getFindNextArray(i).equals(VaultEntryType.EMPTY)) {
                    bucketEntryForInternalComputation.setOnehotInformationArray(i, 0);
                }

                // check for "till next array" <in case of TRIGGER_EVENT_NOT_ONE_HOT_ACT_TIME_TILL_NEXT_EVENT>
                // VaultEntryType is the standard for an empty BucketEntry
                if (!bucketEntryForInternalComputation.getFindNextArray(i).equals(VaultEntryType.EMPTY)
                        && bucketEntryForInternalComputation.getFindNextArray(i).equals(currentBucketEntry.getVaultEntry().getType())) {
                    bucketEntryForInternalComputation.setOnehotInformationArray(i, 0);
                    bucketEntryForInternalComputation.setFindNextArray(i, VaultEntryType.EMPTY);
                }

                // set the needed value
                // the original BucketEntry will contain the VaultEntry with the VaultEntryType
                // on first call of this method with a new event the needed values are set
                if (!currentBucketEntry.getVaultEntry().getType().equals(VaultEntryType.EMPTY)
                        && (bucketEntryForInternalComputation.getTimeCountDown(i) >= 1 || !bucketEntryForInternalComputation.getFindNextArray(i).equals(VaultEntryType.EMPTY))) {
                    // this is the first encounter of this thisBucketEntry so all average must be set
                    if (ARRAY_ENTRY_TRIGGER_HASHMAP.get(currentBucketEntry.getVaultEntry().getType()) == i) {
                        //
                        // check which VaultEntryType is given and calculate as intended
                        // atm this timer will be reseted every time a new (same)event is started
                        //
                        if (TRIGGER_EVENT_NOT_ONE_HOT_ACT_TIME_SET.containsKey(currentBucketEntry.getVaultEntry().getType())) {
                            // new entries are set in the createNewBucket method
                            // add them to the internal runningComputation list
                            List<Pair<VaultEntryType, Pair<Double, Double>>> tmpList = bucketEntryForInternalComputation.getRunningComputation();
                            tmpList.addAll(currentBucketEntry.getRunningComputation());
                            bucketEntryForInternalComputation.setRunningComputation(tmpList);
                            // DO NOT SET onehotInformationArray[i] HERE BECAUSE THE VALUE HAS TO BE COMPUTED (calculateAverageForSmallestBucketSize method)
                        } else if (TRIGGER_EVENT_NOT_ONE_HOT_ACT_TIME_GIVEN.contains(currentBucketEntry.getVaultEntry().getType())) {
                            // new entries are set in the createNewBucket method
                            // add them to the internal runningComputation list
                            List<Pair<VaultEntryType, Pair<Double, Double>>> tmpList = bucketEntryForInternalComputation.getRunningComputation();
                            tmpList.addAll(currentBucketEntry.getRunningComputation());
                            bucketEntryForInternalComputation.setRunningComputation(tmpList);
                            // DO NOT SET onehotInformationArray[i] HERE BECAUSE THE VALUE HAS TO BE COMPUTED (calculateAverageForSmallestBucketSize method)
                        } else if (TRIGGER_EVENT_NOT_ONE_HOT_ACT_TIME_TILL_NEXT_EVENT.contains(currentBucketEntry.getVaultEntry().getType())) {
                            // TRIGGER_EVENT_NOT_ONE_HOT_ACT_TIME_TILL_NEXT_EVENT : value is set when BucketEntry is created
                            bucketEntryForInternalComputation.setOnehotInformationArray(i, currentBucketEntry.getOnehotInformationArray(i));
                        } else if (TRIGGER_EVENT_NOT_ONE_HOT_ACT_TIME_ONE.contains(currentBucketEntry.getVaultEntry().getType())) {
                            // TRIGGER_EVENT_NOT_ONE_HOT_ACT_TIME_ONE : value is set when BucketEntry is created
                            bucketEntryForInternalComputation.setOnehotInformationArray(i, currentBucketEntry.getOnehotInformationArray(i));
                        } else if (TRIGGER_EVENT_NOT_ONE_HOT_VALUE_IS_A_TIMESTAMP.contains(currentBucketEntry.getVaultEntry().getType())) {
                            // onehotInformationArray[i] = ???
                            bucketEntryForInternalComputation.setOnehotInformationArray(i, currentBucketEntry.getOnehotInformationArray(i));
                        }

                    }
                } else {
                    // there is no new value in this BucketEntry position
                    bucketEntryForInternalComputation.setOnehotInformationArray(i, currentBucketEntry.getOnehotInformationArray(i));
                }

                // set findNextEntry <in case of TRIGGER_EVENT_NOT_ONE_HOT_ACT_TIME_TILL_NEXT_EVENT>
                if (!currentBucketEntry.getFindNextArray(i).equals(VaultEntryType.EMPTY)
                        && !currentBucketEntry.getFindNextArray(i).equals(bucketEntryForInternalComputation.getFindNextArray(i))) {
                    bucketEntryForInternalComputation.setFindNextArray(i, currentBucketEntry.getFindNextArray(i));
                }

                //
                // update BucketEntry arrays
                //
                // set timer
                // TODO if case is wrong???
                // if this timer is longer than the one saved in the BucketEntry take this one
                // if this timer is equal to the one saven in the BucketEntry - 1
                //      and this VaultEntryType is not onehot then update the BucketEntry (onehot might have just been set during the creation of the new BucketEntry).
                if ((bucketEntryForInternalComputation.getTimeCountDown(i) > currentBucketEntry.getTimeCountDown(i) || bucketEntryForInternalComputation.getTimeCountDown(i) == currentBucketEntry.getTimeCountDown(i) - 1) // this seems unnecessary if so delete
                        //               && !ARRAY_ENTRY_TRIGGER_HASHMAP.containsKey(currentBucketEntry.getVaultEntry().getType())
                        ) {
                    currentBucketEntry.setTimeCountDown(i, bucketEntryForInternalComputation.getTimeCountDown(i));
                }

                // set onehotInformationArray
                currentBucketEntry.setOnehotInformationArray(i, bucketEntryForInternalComputation.getOnehotInformationArray(i));
                // set findNextArray
                // if findNextArray is EMPTY then it is filled with the needed information
                // if findNextArray is filled with something else then nothing has to be done
                if (currentBucketEntry.getFindNextArray(i).equals(VaultEntryType.EMPTY)) {
                    currentBucketEntry.setFindNextArray(i, bucketEntryForInternalComputation.getFindNextArray(i));
                }
            }
        }

        // merge-to
        currentBucketEntry.getVaultEntry().setType(currentBucketEntry.getVaultEntry().getType().mergeTo());

        // save the new created list of runningComputation
        List<Pair<VaultEntryType, Pair<Double, Double>>> tmpList = bucketEntryForInternalComputation.getRunningComputation();
        currentBucketEntry.setRunningComputation(tmpList);
    }
    
    

}
