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
import de.opendiabetes.vault.container.VaultEntry;
import de.opendiabetes.vault.container.VaultEntryType;
import de.opendiabetes.vault.util.SplineInterpolator;
import static de.opendiabetes.vault.util.TimestampUtils.addMinutesToTimestamp;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import javafx.util.Pair;

/**
 * This class contains all the methods to create the list of FinalBucketEntrys
 * for the ML-exporter.
 *
 * @author Chryat1s
 */
public class BucketProcessor {

    final BucketEntryCreator bucketEntryCreator = new BucketEntryCreator();
    final ListOfBucketEntriesCreator bucketListCreator = new ListOfBucketEntriesCreator();
    final BucketAverageCalculationUtils averageCalculationMethods = new BucketAverageCalculationUtils();

    /**
     * This method receives a list of VaultEntrys and a wanted step size (in
     * minutes) for the resulting list of FinalBucketEntrys. In this method the
     * given list of VaultEntrys will be transformed into a list of BucketEntrys
     * through the createListOfBuckets method which will then be stripped-down
     * to the necessary BucketEntrys via the removeUnneededBucketEntries method.
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
     * @param firstBucketNumber starting index.
     * @param entryList The list of VaultEntrys that will be transformed into a
     * list of FinalBucketEntrys.
     * @param wantedBucketSize This is the wanted bucket size (in minutes).
     * @return This method returns a list of FinalBucketEntrys in the desired
     * bucket size (time step size).
     * @throws ParseException
     */
    public List<FinalBucketEntry> runProcess(int firstBucketNumber, List<VaultEntry> entryList, int wantedBucketSize) throws ParseException {
        List<FinalBucketEntry> outputFinalBucketList = new ArrayList<>();
        if (entryList != null && entryList.size() > 0) {
            // FinalBucketEntry counter
            int finalBucketEntryListCounter = firstBucketNumber;

            // Bucket size == 1 min.
            List<BucketEntry> listOfBucketEntries = bucketListCreator.createListOfBuckets(firstBucketNumber, entryList);
            // set all the array information
            bucketListCreator.transferBucketEntryValues(listOfBucketEntries);

            // remove duplicate timestamp BucketEntrys
            listOfBucketEntries = removeUnneededBucketEntries_old(firstBucketNumber, listOfBucketEntries);
            // calculate averages
            // for each BucketEntry in the list
            for (BucketEntry entry : listOfBucketEntries) {
                averageCalculationMethods.calculateAverageForSmallestBucketSize(entry);
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
                for (int j = 0; j < interpolationMatrix[0].length; j++) {
                    interpolationMatrix[i][j] = 0.0;
                }
            }
            // ==============================
            // =====SET UP INTERPOLATION=====
            // ==============================

            // ===========================
            // ===RUN THE INTERPOLATION===
            // ===========================
            // get data
            List<Pair<Integer, Pair<VaultEntryType, Double>>> rawData = collectInterpolationDataFromBucketEntries(listOfBucketEntries);
            //
            // sort rawData
            for (VaultEntryType type : HASHSET_FOR_LINEAR_INTERPOLATION) {
                // sort rawData for the wanted VaultEntryType
                List<Pair<Integer, Pair<VaultEntryType, Double>>> sortedRawData = sortDataByTypeForInterpolation(type, rawData);

                // list for sorted data from start till last entry ... not found entries will be set so that the interpolateGaps method know what to compute
                List<Pair<Integer, Pair<VaultEntryType, Double>>> sortedData = new ArrayList();

                // sort the data so that the pairs are storted according to the bucket number ... BUCKET_START_NUMBER to x
                // (i - BUCKET_START_NUMBER) < listOfBucketEntries.size() because i - BUCKET_START_NUMBER == 0 on first run e.g. 0 - 0 = 0 and 1 - 1 = 0
                for (int i = 0; i < listOfBucketEntries.size(); i++) {

                    // if list is empty then don't add anymore entries
                    if (!sortedRawData.isEmpty()) {
                        Pair<Boolean, Integer> tempTest = checkIfBucketEntryNumberIsAvailableAndAtWhichPositionTheNumberIsFoundInsideTheList(i, sortedRawData);

                        // BucketEntry number has been found inside the list of sortedRawData
                        if (tempTest.getKey()) {
                            //            sortedData.add(sortedRawData.remove((int) tempTest.getValue()));
                            Pair<List<Pair<Integer, Pair<VaultEntryType, Double>>>, Pair<Integer, Pair<VaultEntryType, Double>>> getTheRestOfTheListAndTheRemovedPair = removePairOutOfSortedRawData(sortedRawData, (int) tempTest.getValue());
                            sortedRawData = getTheRestOfTheListAndTheRemovedPair.getKey();
                            sortedData.add(getTheRestOfTheListAndTheRemovedPair.getValue());
                        } else {
                            // BucketEntry number not found
                            // if the sorted list is not empty then will with entries telling the interpolateGaps method that this entry is missing
                            if (!sortedData.isEmpty()) {
                                sortedData.add(new Pair(i, new Pair(type, null)));
                            }
                        }
                    } else {
                        // sortedRawData is empty
                    }
                }

                // data is now ready for the interpolateGaps method
                List<Pair<Integer, Pair<VaultEntryType, Double>>> interpolatedData = interpolateGaps(sortedData);

                // ===============================================================
                // ==interpolatedData contains all data for this mergeToThisType==
                // ====there is a value for every BucketEntry that is available===
                // ========fill the interpolationMatrix with these values=========
                // ===============================================================
                // mergeToThisType position inside of the matrix
                int typePositionInsideTheMatrix = tempHashMapForMatrixPositionsOfTheVaultEntryTypes.get(type);

                // fill the matrix at the positions that have availabe values
                for (Pair<Integer, Pair<VaultEntryType, Double>> pair : interpolatedData) {
                    // typePositionInsideTheMatrix gives the position of the VaultEntryType in the matrix
                    // pair.getKey() gives the BucketEntry number that is also the position inside the matrix for this bucket
                    // e.g. BucketEntry number = 1 matrix position = 1 - BUCKET_START_NUMBER = 0
                    interpolationMatrix[typePositionInsideTheMatrix][pair.getKey()] = (double) pair.getValue().getValue();          // TODO check if this position is ok (pair.getKey() - BUCKET_START_NUMBER)
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
                    // mergeToThisType position inside of the matrix
                    int typePositionInsideTheMatrix = tempHashMapForMatrixPositionsOfTheVaultEntryTypes.get(type);

                    bucket.setValues(ARRAY_ENTRY_TRIGGER_HASHMAP.get(type), interpolationMatrix[typePositionInsideTheMatrix][bucketNumber]);
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
                        outputFinalBucketList.add(averageCalculationMethods.calculateAverageForWantedBucketSize(finalBucketEntryListCounter, wantedBucketSize, listOfWantedBucketSize));
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
                outputFinalBucketList.add(averageCalculationMethods.calculateAverageForWantedBucketSize(finalBucketEntryListCounter, wantedBucketSize, listOfWantedBucketSize));
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
                            // not a merged mergeToThisType
                            outputFinalBucketList.get(outputFinalBucketList.size() - 1).setValues(ARRAY_ENTRIES_AFTER_MERGE_TO.get(type), entry.getValues(ARRAY_ENTRY_TRIGGER_HASHMAP.get(type)));
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
                        outputFinalBucketList.get(outputFinalBucketList.size() - 1).setValues(ARRAY_ENTRIES_AFTER_MERGE_TO.get(pair.getKey()), pair.getValue());
                    }
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
    protected List<Pair<Integer, Pair<VaultEntryType, Double>>> collectInterpolationDataFromBucketEntries(List<BucketEntry> listOfBucketEntrys) {
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
            // if the wanted mergeToThisType is not found move to the next pair
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
            positionInsideTheList++;
        }

        return new Pair(false, 0);
    }

    /**
     * This method removes the Pair at the wanted position and returns the
     * removed Pair and the given list without the removed Pair.
     *
     * @param oldSortedRawData This is the list of Pairs from which the Pair at
     * the given position will be removed from.
     * @param position The Pair at this position will be removed.
     * @return This method returns a Pair containing the new list of pairs
     * without the removed pair and the removed pair.
     */
    protected Pair<List<Pair<Integer, Pair<VaultEntryType, Double>>>, Pair<Integer, Pair<VaultEntryType, Double>>> removePairOutOfSortedRawData(List<Pair<Integer, Pair<VaultEntryType, Double>>> oldSortedRawData, int position) {
        List<Pair<Integer, Pair<VaultEntryType, Double>>> newSortedRawData = new ArrayList<>();
        Pair<Integer, Pair<VaultEntryType, Double>> removedPair = null;
        int counter = 0;

        for (Pair<Integer, Pair<VaultEntryType, Double>> thisPair : oldSortedRawData) {

            if (counter != position) {
                Pair<Integer, Pair<VaultEntryType, Double>> newPair = new Pair(thisPair.getKey(), new Pair(thisPair.getValue().getKey(), thisPair.getValue().getValue()));
                // add to the outputList
                newSortedRawData.add(newPair);
            } else {
                // this is the Pair that is to be removed
                removedPair = new Pair(thisPair.getKey(), new Pair(thisPair.getValue().getKey(), thisPair.getValue().getValue()));
            }

            counter++;
        }

        if (removedPair == null) {
            throw new Error("The_pair_that_is_to_be_removed_was_not_found!");
        } else {
            return new Pair(newSortedRawData, removedPair);
        }
    }

    // =========================================================================
    // =========================================================================
    // =========================================================================
    /**
     * This method iterates through all BucketEntrys of the given list of
     * BucketEntrys and creates a new list of BucketEntrys that only contains
     * the last found BucketEntry of each timestamp found in the given list of
     * BucketEntrys. All the BucketEntrys in the new created list of
     * BucketEntrys will have the correct numeration set to thier new position
     * in the list.
     *
     * NEW: This method will now also update the listOfValuesForTheInterpolator
     * list by setting the bucketNumbers inside this list to the new
     * bucketNumber.
     *
     * This method might still create a return list with incorrect values for
     * the interpolation. to be sure that the list is correct use the
     * removeUnneededBucketEntries_old method.
     *
     * @param firstBucketEntryNumber This is the first BucketEntry number in the
     * returned list.
     * @param bucketList This is the list of BucketEntrys that will be used to
     * create the new normalized (minimal) list of BucketEntrys.
     * @return This method returns a list of BucketEntrys with only one
     * BucketEntry per timestamp.
     */
    protected List<BucketEntry> removeUnneededBucketEntries(int firstBucketEntryNumber, List<BucketEntry> bucketList) {
        List<BucketEntry> outputBucketList = new ArrayList<>();
        List<BucketEntry> inverseOutputBucketList = new ArrayList<>();
        Date firstBucketEntryDate = bucketList.get(0).getVaultEntry().getTimestamp();
        Date lastBucketEntryDate = bucketList.get(bucketList.size() - 1).getVaultEntry().getTimestamp();
        Date currentBucketEntryDate = null;
        int newConsecutiveBucketEntryNumber = firstBucketEntryNumber;

        // loop through the list and search for a new date
        for (int i = (bucketList.size() - 1); i > -1; i--) {
            Date thisTimestamp = bucketList.get(i).getVaultEntry().getTimestamp();

            // last element
            if (thisTimestamp.equals(lastBucketEntryDate)) {
                inverseOutputBucketList.add(bucketEntryCreator.recreateBucketEntry(bucketList.get(i)));
                currentBucketEntryDate = thisTimestamp;
            }

            // normal walk through
            if (thisTimestamp.before(currentBucketEntryDate)) {
                inverseOutputBucketList.add(bucketEntryCreator.recreateBucketEntry(bucketList.get(i)));
                currentBucketEntryDate = thisTimestamp;
            }
        }

        // generate the correct output
        for (int i = (inverseOutputBucketList.size() - 1); i > -1; i--) {
            outputBucketList.add(bucketEntryCreator.recreateBucketEntry(inverseOutputBucketList.get(i)));
            outputBucketList.get(outputBucketList.size() - 1).setBucketNumber(newConsecutiveBucketEntryNumber);

            // =======================
            // ==INTERPOLATOR UPDATE==
            // =======================
            // the bucket number of all entries inside the listOfValuesForTheInterpolator list must be updated to the new bucket number
            if (!outputBucketList.get(outputBucketList.size() - 1).getValuesForTheInterpolator().isEmpty()) {
                // input
                List<Pair<Integer, Pair<VaultEntryType, Double>>> updateThisList = outputBucketList.get(outputBucketList.size() - 1).getValuesForTheInterpolator();
                // output
                List<Pair<Integer, Pair<VaultEntryType, Double>>> updatedList = new ArrayList<>();
                // run through the list that has to be updated pair by pair
                for (Pair<Integer, Pair<VaultEntryType, Double>> updateThisPair : updateThisList) {
                    Pair<Integer, Pair<VaultEntryType, Double>> updatedPair = new Pair(newConsecutiveBucketEntryNumber, updateThisPair.getValue());
                    updatedList.add(updatedPair);
                }
                // update the BucketEntry listOfValuesForTheInterpolator list
                outputBucketList.get(outputBucketList.size() - 1).setValuesForTheInterpolator(updatedList);
            }
            // =======================
            // ==INTERPOLATOR UPDATE==
            // =======================

            newConsecutiveBucketEntryNumber++;
        }

        // check if everything is correct
        Date checkThisFirstDate = outputBucketList.get(0).getVaultEntry().getTimestamp();
        Date checkThisLAstDate = outputBucketList.get(outputBucketList.size() - 1).getVaultEntry().getTimestamp();
        if (checkThisFirstDate.equals(firstBucketEntryDate) && checkThisLAstDate.equals(lastBucketEntryDate)) {
            return outputBucketList;
        } else {
            throw new Error("An_Error_occurred_while_removing_unneeded_BucketEntrys!");
        }
    }

    /**
     * This method iterates through all BucketEntrys of the given list of
     * BucketEntrys and creates a new list of BucketEntrys that only contains
     * the last found BucketEntry of each timestamp found in the given list of
     * BucketEntrys. All the BucketEntrys in the new created list of
     * BucketEntrys will have the correct numeration set to thier new position
     * in the list.
     *
     * NEW: This method will now also update the listOfValuesForTheInterpolator
     * list by setting the bucketNumbers inside this list to the new
     * bucketNumber.
     *
     * @param firstBucketEntryNumber This is the first BucketEntry number in the
     * returned list.
     * @param bucketList This is the list of BucketEntrys that will be used to
     * create the new normalized (minimal) list of BucketEntrys.
     * @return This method returns a list of BucketEntrys with only one
     * BucketEntry per timestamp.
     */
    protected List<BucketEntry> removeUnneededBucketEntries_old(int firstBucketEntryNumber, List<BucketEntry> bucketList) {
        List<BucketEntry> outputBucketList = new ArrayList<>();
        Date checkingThisBucketEntryDate = bucketList.get(0).getVaultEntry().getTimestamp();
        Date lastBucketEntryDate = bucketList.get(bucketList.size() - 1).getVaultEntry().getTimestamp();
        int currentBucketListPosition = 0;
        int currentBucketOutputListPosition = firstBucketEntryNumber;

        // loop ends on Date.equals(last Date)
        // leave loop when on last date to prevent NullPointerException
        while (checkingThisBucketEntryDate.before(lastBucketEntryDate)) {
            // move through the timestamps
            while (checkingThisBucketEntryDate.equals(bucketList.get(currentBucketListPosition).getVaultEntry().getTimestamp())) {
                currentBucketListPosition++;
            }
            // currentBucketListPosition - 1 is now the position of the needed BucketEntry
            outputBucketList.add(bucketList.get(currentBucketListPosition - 1));
            // update the BucketEntryNumber to the new position
            outputBucketList.get(outputBucketList.size() - 1).setBucketNumber(currentBucketOutputListPosition);

            // =======================
            // ==INTERPOLATOR UPDATE==
            // =======================
            // the bucketNumer of all entries inside the listOfValuesForTheInterpolator list must be updated to the new bucket number
            if (!outputBucketList.get(outputBucketList.size() - 1).getValuesForTheInterpolator().isEmpty()) {
                // input
                List<Pair<Integer, Pair<VaultEntryType, Double>>> updateThisList = outputBucketList.get(outputBucketList.size() - 1).getValuesForTheInterpolator();
                // output
                List<Pair<Integer, Pair<VaultEntryType, Double>>> updatedList = new ArrayList<>();
                // run through the list that has to be updated pair by pair
                for (Pair<Integer, Pair<VaultEntryType, Double>> updateThisPair : updateThisList) {
                    Pair<Integer, Pair<VaultEntryType, Double>> updatedPair = new Pair(currentBucketOutputListPosition, updateThisPair.getValue());
                    updatedList.add(updatedPair);
                }
                // update the BucketEntry listOfValuesForTheInterpolator list
                outputBucketList.get(outputBucketList.size() - 1).setValuesForTheInterpolator(updatedList);
            }

            // =======================
            // ==INTERPOLATOR UPDATE==
            // =======================
            // set checkingThisBucketEntryDate to the next minute
            checkingThisBucketEntryDate = addMinutesToTimestamp(checkingThisBucketEntryDate, 1);
            // DO NOT UPDATE THE currentBucketListPosition BECAUSE THIS IS ALREADY THE NEXT POSITION
            //
            // set currentBucketListPosition to the next BucketEntry position
            // currentBucketListPosition++;
            //
            // update currentBucketOutputListPosition to the next BucketEntry number to be set
            currentBucketOutputListPosition++;
        }
        // since on last date just take last entry
        // add the last BucketEntry to the output list
        outputBucketList.add(bucketList.get(bucketList.size() - 1));
        // update the BucketEntryNumber to the new position
        outputBucketList.get(outputBucketList.size() - 1).setBucketNumber(currentBucketOutputListPosition);

        // =======================
        // ==INTERPOLATOR UPDATE==
        // =======================
        // the bucketNumer of all entries inside the listOfValuesForTheInterpolator list must be updated to the new bucket number
        if (!outputBucketList.get(outputBucketList.size() - 1).getValuesForTheInterpolator().isEmpty()) {
            // input
            List<Pair<Integer, Pair<VaultEntryType, Double>>> updateThisList = outputBucketList.get(outputBucketList.size() - 1).getValuesForTheInterpolator();
            // output
            List<Pair<Integer, Pair<VaultEntryType, Double>>> updatedList = new ArrayList<>();
            // run through the list that has to be updated pair by pair
            for (Pair<Integer, Pair<VaultEntryType, Double>> updateThisPair : updateThisList) {
                Pair<Integer, Pair<VaultEntryType, Double>> updatedPair = new Pair(currentBucketOutputListPosition, updateThisPair.getValue());
                updatedList.add(updatedPair);
            }
            // update the BucketEntry listOfValuesForTheInterpolator list
            outputBucketList.get(outputBucketList.size() - 1).setValuesForTheInterpolator(updatedList);
        }

        // =======================
        // ==INTERPOLATOR UPDATE==
        // =======================
        return outputBucketList;
    }

    /**
     *
     * Interpolates gaps (as null values) of a specific VaultEntryType in a
     * given list.<br>
     * Regarding the Structure: <br>
     * List(Index, (Type, Value))<br>
     * whereas the value being null means that it's a gap which has to be
     * interpolated
     *
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
        return result;
    }
}
