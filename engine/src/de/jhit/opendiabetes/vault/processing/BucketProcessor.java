/*
 * Copyright (C) 2018 aa80hifa
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

import de.jhit.opendiabetes.vault.container.VaultEntryType;
import de.jhit.opendiabetes.vault.container.BucketEntry;
import de.jhit.opendiabetes.vault.container.VaultEntry;
import static de.jhit.opendiabetes.vault.util.TimestampUtils.addMinutesToTimestamp;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import static de.jhit.opendiabetes.vault.container.BucketEventTriggers.ARRAY_ENTRY_TRIGGER_HASHMAP;
import static de.jhit.opendiabetes.vault.container.BucketEventTriggers.TRIGGER_EVENT_ACT_TIME_GIVEN;
import static de.jhit.opendiabetes.vault.container.BucketEventTriggers.TRIGGER_EVENT_ACT_TIME_TILL_NEXT_EVENT;
import static de.jhit.opendiabetes.vault.container.BucketEventTriggers.TRIGGER_EVENT_ACT_TIME_ONE;
import static de.jhit.opendiabetes.vault.container.BucketEventTriggers.*;

/**
 *
 * @author aa80hifa
 */

    // timestamp < counter == just create bucket
    // timestamp = counter == create bucket / count up
    // timestamp > counder == create emtpy bucket / count up

public class BucketProcessor {
    
    // start BucketEntry number ... needed in createListOfBuckets and setBucketArrayInformation
        // if changed to 0 then checkPreviousBucketEntry must be changed as well
    final int BUCKET_START_NUMBER = 1;
    
    // 
    // System.out mode (for debugging)
    private static final boolean DEBUG = true;
    // 
    
    // Date check for setBucketArrayInformation (merge-to and onehot)
    private Date lastDate = null;
    // boolean switch for setBucketArrayInformation (merge-to and onehot)
    private boolean sameDatesGetNoTimerArrayUpdate = false;

    // time countdown Array
    private double[] timeCountDownArray = new double[BucketEntry.getNumberOfVaultEntryTriggerTypes()];
    // onehot information Array
    private double[] onehotInformationArray = new double[BucketEntry.getNumberOfVaultEntryTriggerTypes()];
    // onehot Annotaion
//    private VaultEntryType[] entryTypeArray = new VaultEntryType[BucketEntry.getNumberOfVaultEntryTriggerTypes()];
    // TODO missing wait till next entry
    private VaultEntryType[] findNextArray = new VaultEntryType[BucketEntry.getNumberOfVaultEntryTriggerTypes()];
    
    /**
     * This method creates a new BucketEntry out of the given int and VaultEntry.
     * The int is the consecutive numbering of the BucketEntry.
     * During the process the VaultEntryType will be checked and all necessary inputs for the arrays inside the BucketEntry will be generated accordingly.
     * @param bucketNumber The consecutive number of the BucketEntry that is being created.
     * @param entry The VaultEntry that will be stored inside the BucketEntry.
     * @return This method returns a newly created BucketEntry
     */
    // TODO till now only onehot boolean is being set ... the standard vaules of other VaultEntryTypes still have to be set!!
    public BucketEntry createNewBucket(int bucketNumber, VaultEntry entry) {
        
        // create new BucketEntry
        BucketEntry newBucket = new BucketEntry(bucketNumber, entry);
        
        // set Array information accroding to the VaultEntryType
        // is this a Trigger Event?
        if (ARRAY_ENTRY_TRIGGER_HASHMAP.containsKey(entry.getType())) {
            
            // get VaultEntryType position from the HashMap
            int arrayPosition = ARRAY_ENTRY_TRIGGER_HASHMAP.get(entry.getType());
            
            // is the Act Time given?
            if (TRIGGER_EVENT_ACT_TIME_GIVEN.contains(entry.getType())) {
                // set Act Time
            newBucket.setTimeCountDown(arrayPosition, entry.getValue());
                // set boolean true
            newBucket.setOnehotInformationArray(arrayPosition, 1);
                // set to EMPTY
            newBucket.setFindNextArray(arrayPosition, VaultEntryType.EMPTY);
                
            // is the the Act Time till some next Event?
            } else if (TRIGGER_EVENT_ACT_TIME_TILL_NEXT_EVENT.containsKey(entry.getType())) {
                // set to 0 (no direct Act Time)
            newBucket.setTimeCountDown(arrayPosition, 0);
                // set boolean true
            newBucket.setOnehotInformationArray(arrayPosition, 1);
                // set find next to the needed VaultEntryType
            newBucket.setFindNextArray(arrayPosition, TRIGGER_EVENT_ACT_TIME_TILL_NEXT_EVENT.get(entry.getType()));
                
            // is the Act Time just for one Frame?    
            } else if (TRIGGER_EVENT_ACT_TIME_ONE.contains(entry.getType())) {
                // set Act Time to 1 Frame
            newBucket.setTimeCountDown(arrayPosition, 1);
                // set boolean true
            newBucket.setOnehotInformationArray(arrayPosition, 1);
                // set to EMPTY
            newBucket.setFindNextArray(arrayPosition, VaultEntryType.EMPTY);
                
            } else if(TRIGGER_NOT_ONE_HOT_ACT_TIME_ONE.contains(entry.getType())){
                newBucket.setTimeCountDown(arrayPosition, 1);
                newBucket.setOnehotInformationArray(arrayPosition, 0);
                newBucket.setFindNextArray(arrayPosition, VaultEntryType.EMPTY);
                
            
            } else if(TRIGGER_NOT_ONE_HOT_EVENT_ACT_TIME_GIVEN.contains(entry.getType())){
                newBucket.setTimeCountDown(arrayPosition, bucketNumber);
                newBucket.setOnehotInformationArray(arrayPosition, 0);
                newBucket.setFindNextArray(arrayPosition, VaultEntryType.EMPTY);
                
            } else if (TRIGGER_NOT_ONE_HOT_EVENT_ACT_TIME_TILL_NEXT_EVENT.containsKey(entry.getType())){
                newBucket.setTimeCountDown(arrayPosition, bucketNumber);
                newBucket.setOnehotInformationArray(arrayPosition, 0);
                newBucket.setFindNextArray(arrayPosition, VaultEntryType.EMPTY);
            }
            
        }
        
        return newBucket;

    }
    
    /**
     * This method creates a new empty BucketEntry out of the given int and Date.
     * The int is the consecutive numbering of the BucketEntry.
     * A new VaultEntry will be created containing the VauleEntryType EMPTY and the given Date.
     * @param bucketNumber The consecutive number of the BucketEntry that is being created.
     * @param date The Date (timestamp) that is needed to create a vaild VaultEntry
     * @return This method returns an newly created 'empty' BucketEntry.
     * @throws ParseException 
     */
    public BucketEntry createEmptyBucket(int bucketNumber, Date date) throws ParseException {
        
        BucketEntry newBucket = new BucketEntry(bucketNumber, new VaultEntry(VaultEntryType.EMPTY, date));
        return newBucket;

    }
    
    /**
     * This method creates a list of BucketEntrys out of a list of VaultEntrys.
     * This method checks if each VaultEntry is ML-relevant and if it is then a new BucketEntry with the given VaultEntry is created 
     * and if not then a new empty BucketEntry will be crated in it's place.
     * This method also creates a BucketEntry for every minute that passes starting from the timestamp of the first given VaultEntry 
     * and ending with the timestamp of the last given VaultEntry.
     * Do not call this method with null as an imput.
     * @param entryList This is the list of VaultEntrys that will be converted into a list of BucketEntrys.
     * @return  This method returns the list of BucketEntrys that is generated out of the given list of VaultEntrys.
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
        if (entryList.size() > 0) {timeCounter = entryList.get(0).getTimestamp();}
        // position in the VaultEntry list
        int vaultEntryListPosition = 0;
        
        if (DEBUG) {System.out.println("===============================================");
                    System.out.println("===============================================");
                    System.out.println("init"); System.out.println(bucketEntryNumber); System.out.println(vaultEntryListPosition); System.out.println(timeCounter); 
                    System.out.println("Bucket_output_size"); System.out.println(outputBucketList.size());
                    System.out.println("Bucket_input_size"); System.out.println(entryList.size());
                    System.out.println("===============================================");}
        
        // compare Trigger (time or Event) with EventType before starting a new Trigger
        // 
        
        // timeCounter == last timestamp
        while (vaultEntryListPosition < entryList.size()) {
            
        if (DEBUG) {System.out.println("While_new_loop_start"); System.out.println(bucketEntryNumber); System.out.println(vaultEntryListPosition); System.out.println(timeCounter); 
                    System.out.println("Bucket_output_size"); System.out.println(outputBucketList.size());
                    System.out.println("Bucket_input_size"); System.out.println(entryList.size());
                    System.out.println("===============================================");}
                
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
                            
        if (DEBUG) {System.out.println("Date_before_new_bucket_created"); System.out.println(bucketEntryNumber); System.out.println(vaultEntryListPosition); System.out.println(timeCounter); 
                    System.out.println("Bucket_output_size"); System.out.println(outputBucketList.size());
                    System.out.println("Bucket_input_size"); System.out.println(entryList.size());
                    System.out.println("===============================================");}
                
                        } else {
                            
        if (DEBUG) {System.out.println("===============================================");
                    System.out.println("Date_before_remove_and_new_bucket_created");
                    System.out.println("Date_before_remove_bucket_entry_being_removed"); System.out.println(bucketEntryNumber - 1);
                    System.out.println("Date_before_remove_removing_bucket_entry_from_array_size"); System.out.println(outputBucketList.size());
                    System.out.println("===============================================");}   
        
                            // the last BucketEntry is invalid and should be overwritten
                            // outputBucketEntry.size() - 1 == bucketEntryNumber - 1
                            outputBucketList.remove(outputBucketList.size() - 1);
                            // create a new Bucket with the given entry
                            // the new BucketEntry has the bucketEntryNumber from the removed BucketEntry
                            newBucketEntry = createNewBucket((bucketEntryNumber - 1), entryList.get(vaultEntryListPosition));
                            outputBucketList.add(newBucketEntry);
                            
                            // DO NOT UPDATE THE BUCKETENTRYNUMBER SINCE THE LAST POSITION HAS BEEN OVERWRITTEN
                            
        if (DEBUG) {System.out.println("Date_before_remove_and_new_bucket_created"); System.out.println(bucketEntryNumber); System.out.println(vaultEntryListPosition); System.out.println(timeCounter); 
                    System.out.println("Bucket_output_size"); System.out.println(outputBucketList.size());
                    System.out.println("Bucket_input_size"); System.out.println(entryList.size());
                    System.out.println("===============================================");}                    
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
                    
        if (DEBUG) {System.out.println("Date_before_end"); System.out.println(bucketEntryNumber); System.out.println(vaultEntryListPosition); System.out.println(timeCounter); 
                    System.out.println("Bucket_output_size"); System.out.println(outputBucketList.size());
                    System.out.println("Bucket_input_size"); System.out.println(entryList.size());
                    System.out.println("===============================================");}
                
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
                        
        if (DEBUG) {System.out.println("Date_equal_new_bucket_created"); System.out.println(bucketEntryNumber); System.out.println(vaultEntryListPosition); System.out.println(timeCounter); 
                    System.out.println("Bucket_output_size"); System.out.println(outputBucketList.size());
                    System.out.println("Bucket_input_size"); System.out.println(entryList.size());
                    System.out.println("===============================================");}                
                        
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
                        
        if (DEBUG) {System.out.println("Date_equal_empty_bucket_created"); System.out.println(bucketEntryNumber); System.out.println(vaultEntryListPosition); System.out.println(timeCounter); 
                    System.out.println("Bucket_output_size"); System.out.println(outputBucketList.size());
                    System.out.println("Bucket_input_size"); System.out.println(entryList.size());
                    System.out.println("===============================================");}
        
                    }
                    
                // found a later Date
                // e.g. given 2000.01.01-00:02 is after than the timeCounter date 2000.01.01-00:01
                } else if (entryList.get(vaultEntryListPosition).getTimestamp().after(timeCounter)){
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
                    
        if (DEBUG) {System.out.println("Date_after_empty_bucket_created"); System.out.println(bucketEntryNumber); System.out.println(vaultEntryListPosition); System.out.println(timeCounter); 
                    System.out.println("Bucket_output_size"); System.out.println(outputBucketList.size());
                    System.out.println("Bucket_input_size"); System.out.println(entryList.size());
                    System.out.println("===============================================");}
                    
                // Date not found
                } else {
                    
        if (DEBUG) {System.out.println("dead_lock");}
        
                }
                
            }
            
        if (DEBUG) {System.out.println("///////////////////////////////////////////////");
                    System.out.println("create_list_of_buckets_end");
                    System.out.println("///////////////////////////////////////////////");}
        
        return outputBucketList;

    }
    
    /**
     * This method checks the BucketEntry in a list of BucketEntrys that is prior to the bucket position that is given.
     * The method checks if the previous BucketEntry is an empty BucketEntry with the Date (timestamp) that is given to the method.
     * Returns true if the previous BucketEntry is ok. (Not empty BucketEntry)
     * Returns false if the previous BucketEntry is obsolete. (is an empty BucketEntry)
     * @param bucketListPosition The BucketEntry prior to this psoition will be checked from the list of BucketEntrys.
     * @param date The Date that is being searched for in the BucketEntry.
     * @param listToCheckIn This is the list of BucketEntrys in which the BucketEntry will be taken out of.
     * @return This method returns true if the previous BucketEntry is not an empty BucketEntry and the Date matches and returns false if the previous BucketEntry is an empty BucketEntry.
     */
    private boolean checkPreviousBucketEntry(int bucketListPosition, Date date, List<BucketEntry> listToCheckIn) {
        
        if (DEBUG) {System.out.println("check_prev_bucket");
                    System.out.println("incoming_bucket_position"); System.out.println(bucketListPosition + 1);
                    System.out.println("inside"); System.out.println(bucketListPosition);
                    System.out.println(date);
                    System.out.println("check_prev_output_bucket_size"); System.out.println(listToCheckIn.size());
                    System.out.println("===============================================");}
        
        // get position - 1 since entry x in the list is at the position x-1
        if (listToCheckIn.get(bucketListPosition - 1).getVaultEntry().getType() != VaultEntryType.EMPTY 
            && 
            listToCheckIn.get(bucketListPosition - 1).getVaultEntry().getTimestamp().equals(date)) {
            // VaultEntryType is usable
            // Timestamp is correct
            // BucketEntry is ok
            return true;
        } else {
            return false;
        }
        
    }
    
    // 
    // 
    // TODO only update one hot with this method ... find a work around for ML-relevant but not one hot
    // 
    // 

    /**
     * This method sets the necessary information into the BucketEntry arrays and 
     * manages the onehot markers, onehot timers, and merge-tos.
     * The Date expected is the timeCounter from createListOfBuckets.
     * The BucketEntry that is given to this method will have it's arrays updated 
     * according to the input of the previous BucketEntrys that have been in this method.
     * The first call of this method musst be done with the BucketEntry with the bucketEntryNumber 1.
     * @param date This is the current Date from the method createListOfBuckets (timecounter).
     * @param bucket This is the BucketEntry that will have it's arrays updated.
     */
    private void setBucketArrayInformation(Date date, BucketEntry bucket) {
        
        // this prevents that the first BucketEntry has the chance of couting down the timer.
        // sameDatesGetNoTimerArrayUpdate is initially set to false.
        if (bucket.getBucketNumber() == BUCKET_START_NUMBER + 1) {sameDatesGetNoTimerArrayUpdate = true;}
        
        // since it is possible to have multiple timestamps with the same date the timers should not be updated untill the next minute has started.
        // set lastDate on the first call
        if (lastDate == null) {lastDate = date;}
        // if lastDate is 2 minutes off then there is a new line of timestamps starting
        // e.g. lastDate = 00:01 and date = 00:02 then bucketEntrys for the timestamp of 00:01 are being created.
        //      if lastDate = 00:01 and date = 00:03 then bucketEntrys with the timestamp of 00:02 
        //          are being created and that's why lastdate need to be updated.
        if (addMinutesToTimestamp(lastDate, 2).equals(date)) {lastDate = addMinutesToTimestamp(lastDate, 1);            // TODO not reached with EMPTY
                                                        sameDatesGetNoTimerArrayUpdate = true;}                         // TODO not reached with EMPTY
        
        
        // set internal arrays through 1st BucketEntry
        if (bucket.getBucketNumber() == BUCKET_START_NUMBER) {
            timeCountDownArray = bucket.getFullTimeCountDown();
            onehotInformationArray = bucket.getFullOnehotInformationArray();
            findNextArray = bucket.getFullFindNextArray();
            
        if (DEBUG) {System.out.println("Set_Bucket_Array_Information_Init");
                    System.out.println("lastDate"); System.out.println(lastDate);
                    System.out.println("Date"); System.out.println(date);
                    System.out.println("timeCountDownArray_pos_0"); System.out.println(timeCountDownArray[0]);
                    System.out.println("onehotInformationArray_pos_0"); System.out.println(onehotInformationArray[0]);
                    System.out.println("findNextArray_pos_0"); System.out.println(findNextArray[0]);
                    System.out.println("===============================================");}
        
        } else {
            // after 1st BucketEntry
            for (int i = 0; i < timeCountDownArray.length; i++) {
                // DO NOT REPEAT TIMER ARRAY UPDATES ON SAME TIMESTAMP
                if (sameDatesGetNoTimerArrayUpdate) {
                    
        if (DEBUG) {System.out.println("Set_Bucket_Array_Information_first_timestamp");
                    System.out.println("lastDate"); System.out.println(lastDate);
                    System.out.println("Date"); System.out.println(date);
                    System.out.println("timeCountDownArray_pos_0_old"); System.out.println(timeCountDownArray[i]);}
        
                    // set false to not enter this part till lastDate update
                    sameDatesGetNoTimerArrayUpdate = false;
                    // set timers
                    if (timeCountDownArray[i] > 0) {timeCountDownArray[i] = timeCountDownArray[i] - 1;}                                                             /////
                    
        if (DEBUG) {System.out.println("Set_Bucket_Array_Information_first_timestamp");
                    System.out.println("lastDate"); System.out.println(lastDate);
                    System.out.println("Date"); System.out.println(date);
                    System.out.println("timeCountDownArray_pos_0_new"); System.out.println(timeCountDownArray[i]);
                    System.out.println("===============================================");}
        
                }
                // 
                // update info array stats
                // 
                // set timer
                // initial onehots are set when the Bucket is created
                if (bucket.getTimeCountDown(i) > timeCountDownArray[i]
                        && ARRAY_ENTRY_TRIGGER_HASHMAP.get(bucket.getVaultEntry().getType()) == i) {timeCountDownArray[i] = bucket.getTimeCountDown(i);}            /////
                
                // set onehot to false
                if (timeCountDownArray[i] == 0) {onehotInformationArray[i] = 0;}
                // set onehot to true
                if (timeCountDownArray[i] >= 1) {onehotInformationArray[i] = 1;}
                // check for "till next array"
                // VaultEntryType is the standard for an empty BucketEntry
                if (!findNextArray[i].equals(VaultEntryType.EMPTY) &&
                    findNextArray[i].equals(bucket.getVaultEntry().getType())) {onehotInformationArray[i] = 0;
                                                                                findNextArray[i] = VaultEntryType.EMPTY;}
                
                // set findNextEntry
                if (!bucket.getFindNextArray(i).equals(VaultEntryType.EMPTY) && 
                    !bucket.getFindNextArray(i).equals(findNextArray[i])) {findNextArray[i] = bucket.getFindNextArray(i);}
                
        if (DEBUG) {System.out.println("Set_Bucket_Array_Information_update_info_array_stats");
                    System.out.println("lastDate"); System.out.println(lastDate);
                    System.out.println("Date"); System.out.println(date);
                    System.out.println("timeCountDownArray_pos_i"); System.out.println(timeCountDownArray[i]);
                    System.out.println("onehotInformationArray_pos_i"); System.out.println(onehotInformationArray[i]);
                    System.out.println("findNextArray_pos_i"); System.out.println(findNextArray[i]);
                    System.out.println("===============================================");}
                
                // 
                // update BucketEntry arrays
                // 
                // set timer
                // TODO if case is wrong???
                // if this timer is longer than the one saved in the BucketEntry take this one
                // if this timer is equal to the one saven in the BucketEntry - 1 
                //      and this VaultEntryType is not onehot then update the BucketEntry (onehot might have just been set during the creation of the new BucketEntry).
                if (timeCountDownArray[i] > bucket.getTimeCountDown(i) ||
                    timeCountDownArray[i] == bucket.getTimeCountDown(i) - 1 && !ARRAY_ENTRY_TRIGGER_HASHMAP.containsKey(bucket.getVaultEntry().getType())) {bucket.setTimeCountDown(i, timeCountDownArray[i]);}
                
                // set onehotInformationArray
                bucket.setOnehotInformationArray(i, onehotInformationArray[i]);
                // set findNextArray
                // if findNextArray is EMPTY then it is filled with the needed information
                // if findNextArray is filled with something else then nothing has to be done
                if (bucket.getFindNextArray(i).equals(VaultEntryType.EMPTY)) {bucket.setFindNextArray(i, findNextArray[i]);}
                
        if (DEBUG) {System.out.println("Set_Bucket_Array_Information_update_BucketEntry_arrays");
                    System.out.println("lastDate"); System.out.println(lastDate);
                    System.out.println("Date"); System.out.println(date);
                    System.out.println("timeCountDownArray_pos_i"); System.out.println(bucket.getTimeCountDown(i));
                    System.out.println("onehotInformationArray_pos_i"); System.out.println(bucket.getOnehotInformationArray(i));
                    System.out.println("findNextArray_pos_i"); System.out.println(bucket.getFindNextArray(i));
                    System.out.println("===============================================");}
        
            }
        }
        
        if (DEBUG) {System.out.println("Set_Bucket_Array_Information_merge-to");
                    System.out.println("lastDate"); System.out.println(lastDate);
                    System.out.println("Date"); System.out.println(date);
                    System.out.println("name_before"); System.out.println(bucket.getVaultEntry().getType());}
        
        // merge-to
        bucket.getVaultEntry().setType(bucket.getVaultEntry().getType().mergeTo());
        
        if (DEBUG) {System.out.println("name_after"); System.out.println(bucket.getVaultEntry().getType().mergeTo());
                    System.out.println("===============================================");}
        
        // onehot
        // TODO only one change for each timestamp 
        //          same timestamps will have the same information in the Arrays
        
        // update info stats ... fill internal array with new info where needed ... update bucket arrays
        
        if (DEBUG && bucket.getBucketNumber() == BUCKET_START_NUMBER) {System.out.println("Set_Bucket_Array_Information_Init_array_inside");
                    System.out.println("lastDate"); System.out.println(lastDate);
                    System.out.println("Date"); System.out.println(date);
                    System.out.println("timeCountDownArray_pos_0"); System.out.println(bucket.getTimeCountDown(0));
                    System.out.println("onehotInformationArray_pos_0"); System.out.println(bucket.getOnehotInformationArray(0));
                    System.out.println("findNextArray_pos_0"); System.out.println(bucket.getFindNextArray(0));
                    System.out.println("===============================================");}
        
    }
    
    private void calculateAverageForSmallestBucketSize(BucketEntry bucket) {
        
    }
    
    /**
     * This method iterates through all BucketEntrys of the given list of BucketEntrys and creates a new list of BucketEntrys 
     * that only contains the last found BucketEntry of each timestamp foudn in the given list of BucketEntrys.
     * All the BucketEntrys in the new created list of BucketEntrys will have the correct numeration set thier new position in the list.
     * @param bucketList This is the list of BucketEntrys that will be used to create the new normalized (minimal) list of BucketEntrys.
     * @return This method returns a list of BucketEntrys with only one BucketEntry per timestamp.
     */
    private List<BucketEntry> removeUnneededBucketEntrys(List<BucketEntry> bucketList) {
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
    
    public List<BucketEntry> processor(List<VaultEntry> entryList, int wantedBucketSize) throws ParseException {
        // Bucket size == 1 min.
        List<BucketEntry> listOfBucketEntries = createListOfBuckets(entryList);
        // remove duplicate timestamp BucketEntrys
        listOfBucketEntries = removeUnneededBucketEntrys(listOfBucketEntries);
        // calculate averages
        calculateAverageForSmallestBucketSize(listOfBucketEntries.get(0));  //TODO
        // set to wanted bucket size
        // TODO
        
        return listOfBucketEntries;
    }
}
