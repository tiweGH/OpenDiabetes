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
    
    // BucketEntry list counter
    // BucketEntryNumber starts with entry number 1
    // 
    // if changed to 0 then checkPreviousBucketEntry must be changed as well
    // 
    private int bucketEntryNumber = 1;

    // time countdown Array
    private int[] timeCountDownArray = new int[BucketEntry.getNumberOfVaultEntryTriggerTypes()];
    // onehot information Array
    private double[] booleanArray = new double[BucketEntry.getNumberOfVaultEntryTriggerTypes()];
    // onehot Annotaion
//    private VaultEntryType[] entryTypeArray = new VaultEntryType[BucketEntry.getNumberOfVaultEntryTriggerTypes()];
    // TODO missing wait till next entry
    private VaultEntryType[] findNextArray = new VaultEntryType[BucketEntry.getNumberOfVaultEntryTriggerTypes()];
    
    
    // Create a new Bucket with a given VaultEntry
    public BucketEntry createNewBucket(int bucketNumber,VaultEntry entry) {
        
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
    
    // Create a new empty Bucket containing VaultEntryType.EMPTY and the given Date
    public BucketEntry createEmptyBucket(int bucketNumber, Date date) throws ParseException {
        
        BucketEntry newBucket = new BucketEntry(bucketNumber, new VaultEntry(VaultEntryType.EMPTY, date));
        return newBucket;

    }
    
    // TODO test for Date change when reaching 00:00 of the next day
    public List<BucketEntry> createListOfBuckets(List<VaultEntry> entryList) throws ParseException {

        // TODO Liste aus Buckets erstellen aus der gegebenen VaultEnty Liste
        List<BucketEntry> outputBucketList = new ArrayList<>();
        Date timeCounter = entryList.get(0).getTimestamp();
        // position in the VaultEntry list
        int VaultEntryListPosition = 0;
        
        // compare Trigger (time or Event) with EventType before starting a new Trigger
        // 
        
        while (VaultEntryListPosition < entryList.size()) {
                
                // found an earlier Date        TODO check for hours and minutes ?
                if (entryList.get(VaultEntryListPosition).getTimestamp().before(timeCounter)) {
                    
                    // check is ML-relevant
                    if (entryList.get(VaultEntryListPosition).getType().isMLrelevant()) {
                        // create a new Bucket with the given entry
                        outputBucketList.add(createNewBucket(bucketEntryNumber, entryList.get(VaultEntryListPosition)));
                        // update bucketEntryNumber
                        bucketEntryNumber++;

                        //
                        // TODO onehot - merge-to
                        //
                        
                    } // else do nothing ... VaultEntry is not ML-relevant
                    
    
                    // DO NOT UPDATE TIMECOUNTER! entryList may contain more VaultEntrys with the same timestamp
                    
                    // move to the next VaultEntry in the list
                    VaultEntryListPosition++;
                
                // found the same Date
                } else if (timeCounter == entryList.get(0).getTimestamp()) {
                    
                    if (entryList.get(VaultEntryListPosition).getType().isMLrelevant()) {
                        
                        // check if there already is an earlier BucketEntry with the same VaultEntry timestamp and if this BucketEntry can be removed
                        if (checkPreviousBucketEntry(bucketEntryNumber - 1, timeCounter, outputBucketList)) {
                            // create new BucketEntry
                            
                        // chekcBucketEntry == false
                        } else {
                            // create new BucketEntry
                            
                        }
                        
                        // create a new Bucket with the given entry
                        outputBucketList.add(createNewBucket(bucketEntryNumber, entryList.get(VaultEntryListPosition)));
                        // update bucketEntryNumber
                        bucketEntryNumber++;

                        //
                        // TODO onehot - merge-to
                        //

                        // update timecounter
                        timeCounter = addMinutesToTimestamp(timeCounter, 1);
                        // move to the next VaultEntry in the list
                        VaultEntryListPosition++;
                        
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
                        VaultEntryListPosition++;
                        
                    }                    
                    
                // found a later Date           TODO check for hours and minutes ?
                } else if (entryList.get(0).getTimestamp().after(timeCounter)){
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
                    
                // Date not found
                } else {
                }
                
            }
        
        
        
        return outputBucketList;

    }
    
    // checks the BucketEntry at the previous position in the List of BucketEntrys
    public boolean checkPreviousBucketEntry(int bucketListPosition, Date date, List<BucketEntry> listToCheckIn) {
        
        // TODO check entry at position - 1 for the timestamp and if BucketEntry has VaultEntryType EMPTY
        //      if yes then remove that bucket and replace it with the new BucketEntry with a real VaultEntryType
        //          set bucketEntryNumber-- and return ???
        //      else don't remove the BucketEntry since it has a vaild VaultEntry inside
        //          keep this BucketEntry and create a new BucketEntry at the given position
        
        return false;
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

    private boolean setBoolean(int annotationPosition) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    private int getTimeCountDown(int annotationPosition) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    private int setTimeCountDown(int annotationPosition) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    private Date getLocalVaultEntryTime() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    private BucketEntry CreateNewBucket(Object object) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
}
