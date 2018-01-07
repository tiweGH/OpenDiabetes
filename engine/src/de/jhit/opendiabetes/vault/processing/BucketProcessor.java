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
import static de.jhit.opendiabetes.vault.container.BucketEventTriggers.ARRAYENTRYTRIGGERHASHMAP;
import static de.jhit.opendiabetes.vault.container.BucketEventTriggers.TRIGGEREVENTACTTIMEGIVEN;
import static de.jhit.opendiabetes.vault.container.BucketEventTriggers.TRIGGEREVENTACTTIMETILLNEXTEVENT;
import static de.jhit.opendiabetes.vault.container.BucketEventTriggers.TRIGGEREVENTACTTIMEONE;
import de.jhit.opendiabetes.vault.container.VaultEntry;
import de.jhit.opendiabetes.vault.util.TimestampUtils;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 *
 * @author aa80hifa
 */
public class BucketProcessor {

    // time countdown Array
    private int[] timeCountDownArray = new int[BucketEntry.getNumberOfVaultEntryTriggerTypes()];
    // onehot Boolean
    private boolean[] booleanArray = new boolean[BucketEntry.getNumberOfVaultEntryTriggerTypes()];
    // onehot Annotaion
//    private VaultEntryType[] entryTypeArray = new VaultEntryType[BucketEntry.getNumberOfVaultEntryTriggerTypes()];
    // TODO missing wait till next entry
    private VaultEntryType[] findNextArray = new VaultEntryType[BucketEntry.getNumberOfVaultEntryTriggerTypes()];
    
    
    
    

    public BucketEntry createNewBucket(VaultEntry entry) {
        
        // create new BucketEntry
        BucketEntry newBucket = new BucketEntry(entry);
        
        // set Array information accroding to the VaultEntryType
        // is this a Trigger Event?
        if (ARRAYENTRYTRIGGERHASHMAP.containsKey(entry.getType())) {
            
            // get VaultEntryType position from the HashMap
            int arrayPosition = ARRAYENTRYTRIGGERHASHMAP.get(entry.getType());
            
            // is the Act Time given?
            if (TRIGGEREVENTACTTIMEGIVEN.contains(entry.getType())) {
                // set Act Time
            newBucket.setTimeCountDown(arrayPosition, entry.getValue());
                // set boolean true
            newBucket.setBoolean(arrayPosition, true);
                // set to EMPTY
            newBucket.setFindNextArray(arrayPosition, VaultEntryType.EMPTY);
                
            // is the the Act Time till some next Event?
            } else if (TRIGGEREVENTACTTIMETILLNEXTEVENT.containsKey(entry.getType())) {
                // set to 0 (no direct Act Time)
            newBucket.setTimeCountDown(arrayPosition, 0);
                // set boolean true
            newBucket.setBoolean(arrayPosition, true);
                // set find next to the needed VaultEntryType
            newBucket.setFindNextArray(arrayPosition, TRIGGEREVENTACTTIMETILLNEXTEVENT.get(entry.getType()));
                
            // is the Act Time just for one Frame?    
            } else if (TRIGGEREVENTACTTIMEONE.contains(entry.getType())) {
                // set Act Time to 1 Frame
            newBucket.setTimeCountDown(arrayPosition, 1);
                // set boolean true
            newBucket.setBoolean(arrayPosition, true);
                // set to EMPTY
            newBucket.setFindNextArray(arrayPosition, VaultEntryType.EMPTY);
                
            }
        }
        
        return newBucket;

    }
    
    public BucketEntry createEmptyBucket(VaultEntry entry, Date date) throws ParseException {
        
        BucketEntry newBucket = new BucketEntry(new VaultEntry(VaultEntryType.EMPTY, date));
        return newBucket;

    }

    // TODO check for date but iterate over local time
    public List<BucketEntry> createListOfBuckets(List<VaultEntry> entryList) {

        // TODO Liste aus Buckets erstellen aus der gegebenen VaultEnty Liste
        List<BucketEntry> outputBucketList = new ArrayList<>();
        Date timeCounter = entryList.get(0).getTimestamp();

        // 
        // TODO multiple VaultEntrys possible for one timestamp
        // TODO is ML-relevant??? .isMLrelevant()
        // 
        // compare Trigger (time or Event) with EventType before starting a new Trigger
        // 
        // timestamp < counter == just create bucket
        // timestamp = counter == create bucket / count up
        // timestamp > counder == create emtpy bucket / count up
        while (!entryList.isEmpty()) {
                // create a new Bucket or continue
                if (timeCounter == getLocalVaultEntryTime()) {
                        // create an new Bucket with the given entry
                        outputBucketList.add(CreateNewBucket(entryList.get(0)));
                        // TODO move to the next VaultEntry in the list
                } else {
                        // create an new empty Bucket
                        outputBucketList.add(CreateNewBucket(null)); // TODO missing localtime entry
                }
                // TODO timeCounter um eine minute hoch zählen
                //			timestampUtil
            }
        return null;

    }

    public BucketEntry setBucketArrayInformation(int annotationPosition, BucketEntry bucket) {

        // TODO check for onehot
        if (getTimeCountDown(annotationPosition) > 0) {
               // setTimeCountDown(annotationPosition) = getTimeCountDown(annotationPosition) - 1;
               // setBoolean(annotationPosition) = true;
        }

        // TODO check for merge-to
        // TODO get merge-to name and rename the bucket
        return null;

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
