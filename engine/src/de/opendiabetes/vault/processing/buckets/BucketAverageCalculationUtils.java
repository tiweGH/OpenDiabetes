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
import de.opendiabetes.vault.container.FinalBucketEntry;
import de.opendiabetes.vault.container.VaultEntryType;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import javafx.util.Pair;

/**
 * This class contains all the methods needed to compute the average of the
 * values inside of BucketEntrys.
 *
 * @author Chryat1s
 */
public class BucketAverageCalculationUtils {

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
        // this pair will contain the information if the type is found in the list and if where in the list
        Pair<Boolean, Integer> doesListOfComputedValuesContainOutput;

        // this list contains all calculated values before they are set into the FinalBucketEntrys
        List<Pair<VaultEntryType, Double>> listOfComputedValues = new ArrayList<>();

        // Pair< VaultEntryType , Pair< ValueTimer, Value >>
        // iterate over all given pairs in the runningComputation list and add them up acording to their VaultEntryType
        for (Pair<VaultEntryType, Pair<Double, Double>> iteratorPair : bucket.getValuesForRunningComputation()) {
            VaultEntryType type = iteratorPair.getKey();
            VaultEntryType mergeToThisType = type.getMergeTo();

            doesListOfComputedValuesContainOutput = doesListOfComputedValuesContain(mergeToThisType, listOfComputedValues);
            if (doesListOfComputedValuesContainOutput.getKey()) {
                // a pair for the given mergeToThisType is already created
                // sum up values

                // first get the needed pair out of the list ... remove it during the process the replace it later
                int removeEntryAtThisPosition = doesListOfComputedValuesContainOutput.getValue();
                Pair<VaultEntryType, Double> tempPair = listOfComputedValues.remove(removeEntryAtThisPosition);
                // create a new pair with the summed up values
                tempPair = new Pair(tempPair.getKey(), tempPair.getValue() + iteratorPair.getValue().getValue());
                // now put the pair back into the list
                listOfComputedValues.add(tempPair);
            } else {
                // create a new pair for the given mergeToThisType
                Pair<VaultEntryType, Double> tempPair = new Pair(mergeToThisType, iteratorPair.getValue().getValue());
                // add the pair to the list
                listOfComputedValues.add(tempPair);
            }
        }

        // now get all entries from the onehot array that should be summed up too
        for (VaultEntryType vaultEntryType : HASHSETS_TO_SUM_UP) {
            VaultEntryType mergeToThisType = vaultEntryType.getMergeTo();
            int hashMapArrayPosition = ARRAY_ENTRY_TRIGGER_HASHMAP.get(vaultEntryType);

            doesListOfComputedValuesContainOutput = doesListOfComputedValuesContain(mergeToThisType, listOfComputedValues);
            if (doesListOfComputedValuesContainOutput.getKey()) {
                // add the rest into this pair
                int removeEntryAtThisPosition = doesListOfComputedValuesContainOutput.getValue();
                Pair<VaultEntryType, Double> tempPair = listOfComputedValues.remove(removeEntryAtThisPosition);
                // sum up the value in the pair with that in the bucket from the given mergeToThisType
                tempPair = new Pair(tempPair.getKey(), tempPair.getValue() + bucket.getValues(hashMapArrayPosition));
                // put the pair bach into the list of computed values
                listOfComputedValues.add(tempPair);
            } else {
                // check if there is a value for the wanted mergeToThisType
                // ... if there is create a new pair
                // ... if not then move on
                double valueInsideTheBucketEntry = bucket.getValues(hashMapArrayPosition);
                if (valueInsideTheBucketEntry != 0.0) {
                    // create a new pair
                    Pair<VaultEntryType, Double> tempPair = new Pair(mergeToThisType, valueInsideTheBucketEntry);
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
     * VaultEntryType and Double. This method returns a pair of Boolean and
     * Integer. If the VaultEntryType is found within the list of pairs a pair
     * with true and the pair position inside the list will be returned and if
     * the VaultEntryType is not found a pair with false and -1 will be
     * returned.
     *
     * @param findThisType This is the VaultEntryType that is searched for.
     * @param listOfComputedValues This is the list of pairs in which the
     * VaultEntryType will be searched for.
     * @return Returns a Pair with Boolean and Integer. If the VaultEntryType is
     * found within the list of pairs a pair with true and the pair position
     * inside the list will be returned and if the VaultEntryType is not found a
     * pair with false and -1 will be returned.
     */
    protected Pair<Boolean, Integer> doesListOfComputedValuesContain(VaultEntryType findThisType, List<Pair<VaultEntryType, Double>> listOfComputedValues) {
        int pairPosition = 0;
        for (Pair<VaultEntryType, Double> pair : listOfComputedValues) {
            if (pair.getKey().equals(findThisType)) {
                return new Pair(true, pairPosition);
            } else {
                // wait till end
            }
            pairPosition++;
        }
        // mergeToThisType not found
        return new Pair(false, -1);
    }

    /**
     * This method receives a first BucketEntry number, a wanted BucketEntry
     * size, and a list of BucketEntrys. This method computes the average value
     * of the information found inside the BucketEntrys of the list. The given
     * list of BucketEntrys has to be smaller or eqaul to the wanted BucketEntry
     * size. If this is false an error will be thrown.
     *
     * There are two cases for the average computation. The first is that the
     * computed value is a onehot value (0 or 1). The second is that the
     * computed value is a normal value. If the value is onehot and greater than
     * or equal to 0,5 a 1 will be displayed, other values will be taken as they
     * are.
     *
     * After the average is computed the values are saved inside a
     * FinalBucketEntry which is then returned.
     *
     * @param bucketNumber This is the consecutive FinalBucketEntry number.
     * @param wantedBucketSize This is the wanted FinalBucketEntry size. This
     * number will be used to calculate the average value for the
     * FinalBucketEntry.
     * @param bucketsToMerge This is the list of BucketEntrys that will be
     * merged and averaged into the resulting FinalBucketEntry.
     * @return This method returns a FinalBucketEntry containing all the
     * averaged information of the given BucketEntrys inside the list.
     */
    protected FinalBucketEntry calculateAverageForWantedBucketSize(int bucketNumber, double wantedBucketSize, List<BucketEntry> bucketsToMerge) {
        // the list of BucketEntrys contains all entries that are relavent for the average computation
        final int MAX_ARRAY_SIZE = ARRAY_ENTRIES_AFTER_MERGE_TO.size();
        final double WANTED_BUCKET_SIZE = wantedBucketSize;

        // check for the list size
        if (bucketsToMerge.size() > WANTED_BUCKET_SIZE) {
            // data will be lost if continued
            throw new Error("The_given_list_size_is_greater_than_the_wanted_size_data_will_be_lost!");
        }

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
                    // not a merged mergeToThisType
                    tempValue[ARRAY_ENTRIES_AFTER_MERGE_TO.get(type)] = entry.getValues(ARRAY_ENTRY_TRIGGER_HASHMAP.get(type));
                } else {
                    // a merged mergeToThisType

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
            // onehot is OK ... check for other normal values and avg values TODO
            double avgValue = valueComputaion[arrayPos] / WANTED_BUCKET_SIZE;

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
                // other values TODO check
                valueComputaion[arrayPos] = avgValue;

            }
        }

        // create FinalBucketEntry
        FinalBucketEntry result = new FinalBucketEntry(bucketNumber);
        // clone the computed Values into the FinalBucketEntry
        result.setFullValues(valueComputaion.clone());

        return result;
    }
}
