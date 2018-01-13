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
import de.jhit.opendiabetes.vault.util.TimestampUtils;
import static de.jhit.opendiabetes.vault.util.TimestampUtils.addMinutesToTimestamp;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import static de.jhit.opendiabetes.vault.container.BucketEventTriggers.ARRAY_ENTRY_TRIGGER_HASHMAP;
import static de.jhit.opendiabetes.vault.container.BucketEventTriggers.TRIGGER_EVENT_ACT_TIME_GIVEN;
import static de.jhit.opendiabetes.vault.container.BucketEventTriggers.TRIGGER_EVENT_ACT_TIME_TILL_NEXT_EVENT;
import static de.jhit.opendiabetes.vault.container.BucketEventTriggers.TRIGGER_EVENT_ACT_TIME_ONE;

/**
 *
 * @author aa80hifa
 */


    // TODO multiple VaultEntrys possible for one timestamp
    // timestamp < counter == just create bucket
    // timestamp = counter == create bucket / count up
    // timestamp > counder == create emtpy bucket / count up

public class BucketProcessor {
    
    // 
    // System.out mode (for debugging)
    private static final boolean debug = true;
    // 

    // time countdown Array
    private int[] timeCountDownArray = new int[BucketEntry.getNumberOfVaultEntryTriggerTypes()];
    // onehot information Array
    private double[] booleanArray = new double[BucketEntry.getNumberOfVaultEntryTriggerTypes()];
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
            newBucket.setOnehoteInformationArray(arrayPosition, 1);
                // set to EMPTY
            newBucket.setFindNextArray(arrayPosition, VaultEntryType.EMPTY);
                
            // is the the Act Time till some next Event?
            } else if (TRIGGER_EVENT_ACT_TIME_TILL_NEXT_EVENT.containsKey(entry.getType())) {
                // set to 0 (no direct Act Time)
            newBucket.setTimeCountDown(arrayPosition, 0);
                // set boolean true
            newBucket.setOnehoteInformationArray(arrayPosition, 1);
                // set find next to the needed VaultEntryType
            newBucket.setFindNextArray(arrayPosition, TRIGGER_EVENT_ACT_TIME_TILL_NEXT_EVENT.get(entry.getType()));
                
            // is the Act Time just for one Frame?    
            } else if (TRIGGER_EVENT_ACT_TIME_ONE.contains(entry.getType())) {
                // set Act Time to 1 Frame
            newBucket.setTimeCountDown(arrayPosition, 1);
                // set boolean true
            newBucket.setOnehoteInformationArray(arrayPosition, 1);
                // set to EMPTY
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
     * @param entryList This is the list of VaultEntrys that will be converted into a list of BucketEntrys.
     * @return  This method returns the list of BucketEntrys that is generated out of the given list of VaultEntrys.
     * @throws ParseException 
     */
    // TODO test for Date change when reaching 00:00 of the next day
    public List<BucketEntry> createListOfBuckets(List<VaultEntry> entryList) throws ParseException {
        
        // BucketEntry list counter
        // BucketEntryNumber starts with entry number 1
        // 
        // if changed to 0 then checkPreviousBucketEntry must be changed as well
        // 
        int bucketEntryNumber = 1;

        // TODO Liste aus Buckets erstellen aus der gegebenen VaultEnty Liste
        List<BucketEntry> outputBucketList = new ArrayList<>();
        Date timeCounter = entryList.get(0).getTimestamp();
        // position in the VaultEntry list
        int vaultEntryListPosition = 0;
        
        if (debug) {System.out.println("===============================================");
                    System.out.println("===============================================");
                    System.out.println("init"); System.out.println(bucketEntryNumber); System.out.println(vaultEntryListPosition); System.out.println(timeCounter); 
                    System.out.println("Bucket_output_size"); System.out.println(outputBucketList.size());
                    System.out.println("Bucket_input_size"); System.out.println(entryList.size());
                    System.out.println("===============================================");}
        
        // compare Trigger (time or Event) with EventType before starting a new Trigger
        // 
        
        // timeCounter == last timestamp
        while (vaultEntryListPosition < entryList.size()) {
            
        if (debug) {System.out.println("While_new_loop_start"); System.out.println(bucketEntryNumber); System.out.println(vaultEntryListPosition); System.out.println(timeCounter); 
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
                            outputBucketList.add(createNewBucket(bucketEntryNumber, entryList.get(vaultEntryListPosition)));
                            // update bucketEntryNumber
                            bucketEntryNumber++;
                            
        if (debug) {System.out.println("Date_before_new_bucket_created"); System.out.println(bucketEntryNumber); System.out.println(vaultEntryListPosition); System.out.println(timeCounter); 
                    System.out.println("Bucket_output_size"); System.out.println(outputBucketList.size());
                    System.out.println("Bucket_input_size"); System.out.println(entryList.size());
                    System.out.println("===============================================");}
                
                        } else {
                            
        if (debug) {System.out.println("===============================================");
                    System.out.println("Date_before_remove_and_new_bucket_created");
                    System.out.println("Date_before_remove_bucket_entry_being_removed"); System.out.println(bucketEntryNumber - 1);
                    System.out.println("Date_before_remove_removing_bucket_entry_from_array_size"); System.out.println(outputBucketList.size());
                    System.out.println("===============================================");}   
        
                            // the last BucketEntry is invalid and should be overwritten
                            // outputBucketEntry.size() - 1 == bucketEntryNumber - 1
                            outputBucketList.remove(outputBucketList.size() - 1);
                            // create a new Bucket with the given entry
                            // the new BucketEntry has the bucketEntryNumber from the removed BucketEntry
                            outputBucketList.add(createNewBucket((bucketEntryNumber - 1), entryList.get(vaultEntryListPosition)));
                            
                            // DO NOT UPDATE THE BUCKETENTRYNUMBER SINCE THE LAST POSITION HAS BEEN OVERWRITTEN
                            
        if (debug) {System.out.println("Date_before_remove_and_new_bucket_created"); System.out.println(bucketEntryNumber); System.out.println(vaultEntryListPosition); System.out.println(timeCounter); 
                    System.out.println("Bucket_output_size"); System.out.println(outputBucketList.size());
                    System.out.println("Bucket_input_size"); System.out.println(entryList.size());
                    System.out.println("===============================================");}                    
                        }                        

                        //      ???
                        // TODO onehot - merge-to
                        //      ???
                        
                    } // else do nothing ... VaultEntry is not ML-relevant
                    
    
                    // DO NOT UPDATE TIMECOUNTER! entryList may contain more VaultEntrys with the same timestamp
                    
                    // move to the next VaultEntry in the list
                    vaultEntryListPosition++;
                    
        if (debug) {System.out.println("Date_before_end"); System.out.println(bucketEntryNumber); System.out.println(vaultEntryListPosition); System.out.println(timeCounter); 
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
                        outputBucketList.add(createNewBucket(bucketEntryNumber, entryList.get(vaultEntryListPosition)));
                        // update bucketEntryNumber
                        bucketEntryNumber++;

                        //
                        // TODO onehot - merge-to
                        //

                        // update timecounter
                        timeCounter = addMinutesToTimestamp(timeCounter, 1);
                        // move to the next VaultEntry in the list
                        vaultEntryListPosition++;
                        
        if (debug) {System.out.println("Date_equal_new_bucket_created"); System.out.println(bucketEntryNumber); System.out.println(vaultEntryListPosition); System.out.println(timeCounter); 
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
                        outputBucketList.add(createEmptyBucket(bucketEntryNumber, timeCounter));
                        // update bucketEntryNumber
                        bucketEntryNumber++;

                        //
                        // TODO onehot - merge-to
                        //
                        
                        // update timecounter
                        timeCounter = addMinutesToTimestamp(timeCounter, 1);
                        // move to the next VaultEntry in the list
                        vaultEntryListPosition++;
                        
        if (debug) {System.out.println("Date_equal_empty_bucket_created"); System.out.println(bucketEntryNumber); System.out.println(vaultEntryListPosition); System.out.println(timeCounter); 
                    System.out.println("Bucket_output_size"); System.out.println(outputBucketList.size());
                    System.out.println("Bucket_input_size"); System.out.println(entryList.size());
                    System.out.println("===============================================");}
        
                    }
                    
                // found a later Date
                // e.g. given 2000.01.01-00:02 is after than the timeCounter date 2000.01.01-00:01
                } else if (entryList.get(vaultEntryListPosition).getTimestamp().after(timeCounter)){
                    // create a new empty Bucket
                    outputBucketList.add(createEmptyBucket(bucketEntryNumber, timeCounter));
                        // update bucketEntryNumber
                        bucketEntryNumber++;
                    
                    //
                    // TODO onehot - merge-to
                    //
                    
                    // update timecounter
                    timeCounter = addMinutesToTimestamp(timeCounter, 1);
                    
                    // DO NOT UPDATE LIST POSITION! ... the given list position has not been reached yet
                    
        if (debug) {System.out.println("Date_after_empty_bucket_created"); System.out.println(bucketEntryNumber); System.out.println(vaultEntryListPosition); System.out.println(timeCounter); 
                    System.out.println("Bucket_output_size"); System.out.println(outputBucketList.size());
                    System.out.println("Bucket_input_size"); System.out.println(entryList.size());
                    System.out.println("===============================================");}
                    
                // Date not found
                } else {
                    
        if (debug) {System.out.println("dead_lock");}
        
                }
                
            }
            
        if (debug) {System.out.println("///////////////////////////////////////////////");
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
    private static boolean checkPreviousBucketEntry(int bucketListPosition, Date date, List<BucketEntry> listToCheckIn) {
        
        if (debug) {System.out.println("check_prev_bucket");
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

    public BucketEntry setBucketArrayInformation(int annotationPosition, BucketEntry bucket) 
            throws ParseException {
        
        BucketEntry outputBucket = createEmptyBucket(0, TimestampUtils.createCleanTimestamp("2016.04.18-06:48", "yyyy.MM.dd-HH:mm"));

        /*
        
        // TODO only one change for each timestamp 
        //          same timestamps will have the same information in the Arrays
        
        // TODO check for onehot
        if (getTimeCountDown(annotationPosition) > 0) {
               // setTimeCountDown(annotationPosition) = getTimeCountDown(annotationPosition) - 1;
               // setBoolean(annotationPosition) = true;
        }

        // TODO check for merge-to
        // TODO get merge-to name and rename the bucket
        return null;

        */
        
        return outputBucket;
        
    }
}
