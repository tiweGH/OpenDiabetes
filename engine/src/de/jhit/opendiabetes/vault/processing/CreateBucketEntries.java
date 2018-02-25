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
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javafx.util.Pair;

/**
 *
 * @author Chryat1s
 */
public class CreateBucketEntries {
    
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
        final double KEEP_EMPTY_FOR_FOLLOWING_CALCULATION = 0.0;

        // create new BucketEntry
        BucketEntry newBucket = new BucketEntry(bucketNumber, entry);

        // set Array information accroding to the VaultEntryType
        // is this a trigger event?
        if (ARRAY_ENTRY_TRIGGER_HASHMAP.containsKey(entry.getType())) {

            // get VaultEntryType position from the HashMap
            int arrayPosition = ARRAY_ENTRY_TRIGGER_HASHMAP.get(entry.getType());

            // ==================
            // =ML-rev + one hot=
            // ==================
            
            // is the act time given?
            if (TRIGGER_EVENT_ACT_TIME_GIVEN.contains(entry.getType())) {
                // set act time
                newBucket.setTimeCountDown(arrayPosition, entry.getValue());
                // set boolean true
                newBucket.setOnehotInformationArray(arrayPosition, 1);
                // set to EMPTY
                newBucket.setFindNextArray(arrayPosition, VaultEntryType.EMPTY);

                // is the the act time till some next event?
            } else if (TRIGGER_EVENT_ACT_TIME_TILL_NEXT_EVENT.containsKey(entry.getType())) {
                // set to 0 (no direct Act Time)
                newBucket.setTimeCountDown(arrayPosition, 0);
                // set boolean true
                newBucket.setOnehotInformationArray(arrayPosition, 1);
                // set find next to the needed VaultEntryType
                newBucket.setFindNextArray(arrayPosition, TRIGGER_EVENT_ACT_TIME_TILL_NEXT_EVENT.get(entry.getType()));

                // is the act time just for one frame?
            } else if (TRIGGER_EVENT_ACT_TIME_ONE.contains(entry.getType())) {
                // set act time to 1 minute
                newBucket.setTimeCountDown(arrayPosition, 1);
                // set boolean true
                newBucket.setOnehotInformationArray(arrayPosition, 1);
                // set to EMPTY
                newBucket.setFindNextArray(arrayPosition, VaultEntryType.EMPTY);

                // ======================
                // =ML-rev + NOT one hot=
                // ======================
                
                // catch VaultEntryTypes that have to be interpolated
            } else if (HASHSET_FOR_LINEAR_INTERPOLATION.contains(entry.getType())) {
                // at the moment the act time for there VaultEntryTypes are only 1 frame ... this might change later on if new types are added to the hashset
                newBucket.setTimeCountDown(arrayPosition, 1);
                // set value
                newBucket.setOnehotInformationArray(arrayPosition, KEEP_EMPTY_FOR_FOLLOWING_CALCULATION);

                // new entries for this category are added to the listOfValuesForTheInterpolator list in the Bucket
                // Pair< BucketEntryNumber, new Pair< VaultEntryType, Value >>
                List<Pair<Integer, Pair<VaultEntryType, Double>>> tempList = new ArrayList<>();
                tempList.add(new Pair(bucketNumber, new Pair(entry.getType(), entry.getValue())));
                newBucket.setListOfValuesForTheInterpolator(tempList);

                // set to EMPTY
                newBucket.setFindNextArray(arrayPosition, VaultEntryType.EMPTY);

                // is the act time set?
            } else if (TRIGGER_EVENT_NOT_ONE_HOT_ACT_TIME_SET.containsKey(entry.getType())) {
                
                if (entry.getType().equals(VaultEntryType.BASAL_PROFILE)) {
                    
                    // ************************************************************************************************************
                    // BASAL_PROFILE is resetable ... this means new values and value timers will overwrite the previous valid ones
                    // ************************************************************************************************************
                    
                    // set act time to set act time in hashmap
                    newBucket.setTimeCountDown(arrayPosition, TRIGGER_EVENT_NOT_ONE_HOT_ACT_TIME_SET.get(entry.getType()));
                    // set value
                    newBucket.setOnehotInformationArray(arrayPosition, entry.getValue());
                    // set to EMPTY
                    newBucket.setFindNextArray(arrayPosition, VaultEntryType.EMPTY);
                    
                } else {
                    // *************************************************************************************************************************
                    // at the moment BASAL_PROFILE is the only type in this hashset ... this might change and this might be the correct handling
                    // *************************************************************************************************************************
                
                    // set act time to set act time in hashmap
                    newBucket.setTimeCountDown(arrayPosition, TRIGGER_EVENT_NOT_ONE_HOT_ACT_TIME_SET.get(entry.getType()));
                    // set value
                    newBucket.setOnehotInformationArray(arrayPosition, KEEP_EMPTY_FOR_FOLLOWING_CALCULATION);

                    // new entries are added to the runningComputation list in the Bucket
                    // Pair< VaultEntrytype (mergeTo), new Pair< ValueTimer, Value >>
                    // ValueTimer is in VaultEntry value2
                    // Value is in VaultEntry value1
                    List<Pair<VaultEntryType, Pair<Double, Double>>> tempList = new ArrayList<>();
                    tempList.add(new Pair(entry.getType().mergeTo(), new Pair(newBucket.getTimeCountDown(arrayPosition), entry.getValue())));
                    newBucket.setRunningComputation(tempList);

                    // set to EMPTY
                    newBucket.setFindNextArray(arrayPosition, VaultEntryType.EMPTY);
                    
                }

                // ===
                // TODO CHECK
                // ===
                
                // is the act time given?
            } else if (TRIGGER_EVENT_NOT_ONE_HOT_ACT_TIME_GIVEN.contains(entry.getType())) {
                // set act time
                newBucket.setTimeCountDown(arrayPosition, entry.getValue2());
                // set value
                newBucket.setOnehotInformationArray(arrayPosition, KEEP_EMPTY_FOR_FOLLOWING_CALCULATION);

                // new entries are added to the runningComputation list in the Bucket
                // Pair< VaultEntrytype (mergeTo), new Pair< ValueTimer, Value >>
                // ValueTimer is in VaultEntry value2
                // Value is in VaultEntry value1
                List<Pair<VaultEntryType, Pair<Double, Double>>> tempList = new ArrayList<>();
                tempList.add(new Pair(entry.getType().mergeTo(), new Pair(newBucket.getTimeCountDown(arrayPosition), entry.getValue())));
                newBucket.setRunningComputation(tempList);

                // set to EMPTY
                newBucket.setFindNextArray(arrayPosition, VaultEntryType.EMPTY);
                
                // ===
                // TODO CHECK
                // ===

                // is the act time till some next event?
            } else if (TRIGGER_EVENT_NOT_ONE_HOT_ACT_TIME_TILL_NEXT_EVENT.contains(entry.getType())) {
                // set to 0 (no direct Act Time)
                newBucket.setTimeCountDown(arrayPosition, 0);
                // set value
                newBucket.setOnehotInformationArray(arrayPosition, entry.getValue());
                // set to same VaultEntryType
                newBucket.setFindNextArray(arrayPosition, entry.getType());

                // is the act time just for one frame?
            } else if (TRIGGER_EVENT_NOT_ONE_HOT_ACT_TIME_ONE.contains(entry.getType())) {
                // set act time to 1 minute
                newBucket.setTimeCountDown(arrayPosition, 1);
                // set value
                newBucket.setOnehotInformationArray(arrayPosition, entry.getValue());
                // set to EMPTY
                newBucket.setFindNextArray(arrayPosition, VaultEntryType.EMPTY);

                // is the given value a timestamp?
            } else if (TRIGGER_EVENT_NOT_ONE_HOT_VALUE_IS_A_TIMESTAMP.contains(entry.getType())) {
                // set to 0 (no direct Act Time)
                newBucket.setTimeCountDown(arrayPosition, 0);
                // set ???
                newBucket.setOnehotInformationArray(arrayPosition, 0);
                //        newBucket.setOnehotInformationArray(arrayPosition, ???);
                // set to EMPTY
                newBucket.setFindNextArray(arrayPosition, VaultEntryType.EMPTY);

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
}
