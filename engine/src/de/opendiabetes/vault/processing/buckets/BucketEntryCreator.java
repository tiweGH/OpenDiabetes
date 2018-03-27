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
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javafx.util.Pair;

/**
 * This class contains all methods needed to create a new BucketEntry.
 *
 * @author Chryat1s
 */
public class BucketEntryCreator {

    /**
     * This method creates a new BucketEntry out of the given int and
     * VaultEntry. The int is the consecutive numbering of the BucketEntry.
     * During the process the VaultEntryType will be checked and all necessary
     * inputs for the arrays inside the BucketEntry will be generated
     * accordingly.
     *
     * The generated information inside the BucketEntry does not contain the
     * information of other BucketEntrys needed for further processing.
     *
     * @param bucketNumber The consecutive number of the BucketEntry that is
     * being created.
     * @param entry The VaultEntry that will be stored inside the BucketEntry.
     * @return This method returns a newly created BucketEntry
     */
    protected BucketEntry createNewBucketEntry(int bucketNumber, VaultEntry entry) {

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

                // ======================
                // =ML-rev + NOT one hot=
                // ======================
                // catch VaultEntryTypes that have to be interpolated
            } else if (HASHSET_FOR_LINEAR_INTERPOLATION.contains(entry.getType())) {
                // at the moment the act time for there VaultEntryTypes are only 1 frame ... this might change later on if new types are added to the hashset
                newBucket.setValueTimer(arrayPosition, 1);
                // set value
                newBucket.setValues(arrayPosition, KEEP_EMPTY_FOR_FOLLOWING_CALCULATION);

                // new entries for this category are added to the listOfValuesForTheInterpolator list in the Bucket
                // Pair< BucketEntryNumber, new Pair< VaultEntryType, Value >>
                List<Pair<Integer, Pair<VaultEntryType, Double>>> tempList = new ArrayList<>();
                tempList.add(new Pair(bucketNumber, new Pair(entry.getType(), entry.getValue())));
                newBucket.setValuesForTheInterpolator(tempList);

                // set to EMPTY
                newBucket.setFindNextVaultEntryType(arrayPosition, VaultEntryType.EMPTY);

                // is the act time set?
            } else if (TRIGGER_EVENT_NOT_ONE_HOT_ACT_TIME_SET.containsKey(entry.getType())) {

                if (entry.getType().equals(VaultEntryType.BASAL_PROFILE)) {

                    // ************************************************************************************************************
                    // BASAL_PROFILE is resetable ... this means new values and value timers will overwrite the previous valid ones
                    // ************************************************************************************************************
                    // set act time to set act time in hashmap
                    newBucket.setValueTimer(arrayPosition, TRIGGER_EVENT_NOT_ONE_HOT_ACT_TIME_SET.get(entry.getType()));
                    // set value
                    newBucket.setValues(arrayPosition, entry.getValue());
                    // set to EMPTY
                    newBucket.setFindNextVaultEntryType(arrayPosition, VaultEntryType.EMPTY);

                } else {
                    // *************************************************************************************************************************
                    // at the moment BASAL_PROFILE is the only type in this hashset ... this might change and this might be the correct handling
                    // *************************************************************************************************************************

                    // set act time to set act time in hashmap
                    newBucket.setValueTimer(arrayPosition, TRIGGER_EVENT_NOT_ONE_HOT_ACT_TIME_SET.get(entry.getType()));
                    // set value
                    newBucket.setValues(arrayPosition, KEEP_EMPTY_FOR_FOLLOWING_CALCULATION);

                    // new entries are added to the runningComputation list in the Bucket
                    // Pair< VaultEntrytype (getMergeTo), new Pair< ValueTimer, Value >>
                    // ValueTimer is in VaultEntry value2
                    // Value is in VaultEntry value1
                    List<Pair<VaultEntryType, Pair<Double, Double>>> tempList = new ArrayList<>();
                    tempList.add(new Pair(entry.getType().getMergeTo(), new Pair(newBucket.getValueTimer(arrayPosition), entry.getValue())));
                    newBucket.setValuesForRunningComputation(tempList);

                    // set to EMPTY
                    newBucket.setFindNextVaultEntryType(arrayPosition, VaultEntryType.EMPTY);

                }

                // is the act time given?
            } else if (TRIGGER_EVENT_NOT_ONE_HOT_ACT_TIME_GIVEN.contains(entry.getType())) {
                // set act time
                newBucket.setValueTimer(arrayPosition, entry.getValue2());
                // set value
                newBucket.setValues(arrayPosition, KEEP_EMPTY_FOR_FOLLOWING_CALCULATION);

                // new entries are added to the runningComputation list in the Bucket
                // Pair< VaultEntrytype (getMergeTo), new Pair< ValueTimer, Value >>
                // ValueTimer is in VaultEntry value2
                // Value is in VaultEntry value1
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
    protected BucketEntry createEmptyBucketEntry(int bucketNumber, Date date) throws ParseException {

        BucketEntry newBucket = new BucketEntry(bucketNumber, new VaultEntry(VaultEntryType.EMPTY, date));
        return newBucket;

    }

    /**
     * This method gets a BucketEntry and creates a new BucketEntry with the
     * same information.
     *
     * @param oldBucketEntry This is the BucketEntry that will be recreated.
     * @return The method returns the recreated BucketEntry.
     */
    protected BucketEntry recreateBucketEntry(BucketEntry oldBucketEntry) {
        BucketEntry newEntry = new BucketEntry(oldBucketEntry.getBucketNumber(), oldBucketEntry.getVaultEntry());

        for (int j = 0; j < BucketEntry.getNumberOfVaultEntryTriggerTypes(); j++) {
            newEntry.setValueTimer(j, oldBucketEntry.getValueTimer(j));
            newEntry.setValues(j, oldBucketEntry.getValues(j));
            newEntry.setFindNextVaultEntryType(j, oldBucketEntry.getFindNextVaultEntryType(j));
        }

        List<Pair<VaultEntryType, Pair<Double, Double>>> tempListValuesForRunningComputation = new ArrayList<>();
        for (Pair<VaultEntryType, Pair<Double, Double>> thisPair : oldBucketEntry.getValuesForRunningComputation()) {
            Pair<VaultEntryType, Pair<Double, Double>> newPair;
            newPair = new Pair(thisPair.getKey(), new Pair(thisPair.getValue().getKey(), thisPair.getValue().getValue()));
            tempListValuesForRunningComputation.add(newPair);
        }
        newEntry.setValuesForRunningComputation(tempListValuesForRunningComputation);

        List<Pair<VaultEntryType, Double>> tempComputedValuesForTheFinalBucketEntry = new ArrayList<>();
        for (Pair<VaultEntryType, Double> thisPair : oldBucketEntry.getComputedValuesForTheFinalBucketEntry()) {
            Pair<VaultEntryType, Double> newPair;
            newPair = new Pair(thisPair.getKey(), thisPair.getValue());
            tempComputedValuesForTheFinalBucketEntry.add(newPair);
        }
        newEntry.setComputedValuesForTheFinalBucketEntry(tempComputedValuesForTheFinalBucketEntry);

        List<Pair<Integer, Pair<VaultEntryType, Double>>> tempValuesForTheInterpolator = new ArrayList<>();
        for (Pair<Integer, Pair<VaultEntryType, Double>> thisPair : oldBucketEntry.getValuesForTheInterpolator()) {
            Pair<Integer, Pair<VaultEntryType, Double>> newPair;
            newPair = new Pair(thisPair.getKey(), new Pair(thisPair.getValue().getKey(), thisPair.getValue().getValue()));
            tempValuesForTheInterpolator.add(newPair);
        }
        newEntry.setValuesForTheInterpolator(tempValuesForTheInterpolator);

        return newEntry;
    }
}
