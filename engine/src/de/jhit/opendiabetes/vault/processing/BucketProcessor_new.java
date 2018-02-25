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
import de.jhit.opendiabetes.vault.container.FinalBucketEntry;
import de.jhit.opendiabetes.vault.container.VaultEntry;
import de.jhit.opendiabetes.vault.container.VaultEntryType;
import de.jhit.opendiabetes.vault.util.SplineInterpolator;
import static de.jhit.opendiabetes.vault.util.TimestampUtils.addMinutesToTimestamp;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import javafx.util.Pair;

/**
 *
 * @author Chryat1s
 */
public class BucketProcessor_new {
    
    final CreateListOfBucketEntries bucketListCreator;
    public BucketProcessor_new() throws ParseException {
        this.bucketListCreator = new CreateListOfBucketEntries();
    }
    
    final BucketAverageCalculationMethods averageCalculationMethods = new BucketAverageCalculationMethods();
    
    /**
     * This method receives a list of VaultEntrys and a wanted step size (in
     * minutes) for the resulting list of FinalBucketEntrys. In this method the
     * given list of VaultEntrys will be transformed into a list of BucketEntrys
     * through the createListOfBuckets method which will then be stripped-down
     * to the necessary BucketEntrys via the removeUnneededBucketEntrys method.
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
     * @param entryList The list of VaultEntrys that will be transformed into a
     * list of FinalBucketEntrys.
     * @param wantedBucketSize This is the wanted bucket size (in minutes).
     * @return This method returns a list of FinalBucketEntrys in the desired
     * bucket size (time step size).
     * @throws ParseException
     */
    public List<FinalBucketEntry> bucketProcessor(int firstBucketNumber, List<VaultEntry> entryList, int wantedBucketSize) throws ParseException {
        List<FinalBucketEntry> outputFinalBucketList = new ArrayList<>();
        // FinalBucketEntry counter
        int finalBucketEntryListCounter = firstBucketNumber;

        // Bucket size == 1 min.
        List<BucketEntry> listOfBucketEntries = bucketListCreator.createListOfBuckets(firstBucketNumber, entryList);
        // remove duplicate timestamp BucketEntrys
        listOfBucketEntries = removeUnneededBucketEntrys(firstBucketNumber, listOfBucketEntries);
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
        List<Pair<Integer, Pair<VaultEntryType, Double>>> rawData = collectInterpolationDataFromBucketEntrys(listOfBucketEntries);
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
                        sortedData.add(sortedRawData.remove((int) tempTest.getValue()));
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

                bucket.setOnehotInformationArray(ARRAY_ENTRY_TRIGGER_HASHMAP.get(type), interpolationMatrix[typePositionInsideTheMatrix][bucketNumber]);
            }
        }

        // now all BucketEntrys have the interpolated values inside there setOnehotInformationArray
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
                        outputFinalBucketList.get(outputFinalBucketList.size() - 1).setOnehotInformationArray(ARRAY_ENTRIES_AFTER_MERGE_TO.get(type), entry.getOnehotInformationArray(ARRAY_ENTRY_TRIGGER_HASHMAP.get(type)));
                    } else {
                        // a merged mergeToThisType

                        // ignore for now since listOfComputedValuesForTheFinalBucketEntry contains the valid values
                    }
                }

                // second run through the whole listOfComputedValuesForTheFinalBucketEntry
                for (Pair<VaultEntryType, Double> pair : entry.getListOfComputedValuesForTheFinalBucketEntry()) {
                    // place found entries into the right array position
                    // look for the position of the entry that matches this merge-to VaultEntryType
                    //
                    outputFinalBucketList.get(outputFinalBucketList.size() - 1).setOnehotInformationArray(ARRAY_ENTRIES_AFTER_MERGE_TO.get(pair.getKey()), pair.getValue());
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
    protected List<Pair<Integer, Pair<VaultEntryType, Double>>> collectInterpolationDataFromBucketEntrys(List<BucketEntry> listOfBucketEntrys) {
        List<Pair<Integer, Pair<VaultEntryType, Double>>> outputList = new ArrayList<>();

        for (BucketEntry bucket : listOfBucketEntrys) {
            // collect all Data from each bucket that has data stored
            if (!bucket.getListOfValuesForTheInterpolator().isEmpty()) {
                outputList.addAll(bucket.getListOfValuesForTheInterpolator());
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
     * @param bucketList This is the list of BucketEntrys that will be used to
     * create the new normalized (minimal) list of BucketEntrys.
     * @return This method returns a list of BucketEntrys with only one
     * BucketEntry per timestamp.
     */
    protected List<BucketEntry> removeUnneededBucketEntrys(int firstBucketEntryNumber, List<BucketEntry> bucketList) {
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
            if (!outputBucketList.get(outputBucketList.size() - 1).getListOfValuesForTheInterpolator().isEmpty()) {
                // input
                List<Pair<Integer, Pair<VaultEntryType, Double>>> updateThisList = outputBucketList.get(outputBucketList.size() - 1).getListOfValuesForTheInterpolator();
                // output
                List<Pair<Integer, Pair<VaultEntryType, Double>>> updatedList = new ArrayList<>();
                // run through the list that has to be updated pair by pair
                for (Pair<Integer, Pair<VaultEntryType, Double>> updateThisPair : updateThisList) {
                    Pair<Integer, Pair<VaultEntryType, Double>> updatedPair = new Pair(currentBucketOutputListPosition, updateThisPair.getValue());
                    updatedList.add(updatedPair);
                }
                // update the BucketEntry listOfValuesForTheInterpolator list
                outputBucketList.get(outputBucketList.size() - 1).setListOfValuesForTheInterpolator(updatedList);
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
        if (!outputBucketList.get(outputBucketList.size() - 1).getListOfValuesForTheInterpolator().isEmpty()) {
            // input
            List<Pair<Integer, Pair<VaultEntryType, Double>>> updateThisList = outputBucketList.get(outputBucketList.size() - 1).getListOfValuesForTheInterpolator();
            // output
            List<Pair<Integer, Pair<VaultEntryType, Double>>> updatedList = new ArrayList<>();
            // run through the list that has to be updated pair by pair
            for (Pair<Integer, Pair<VaultEntryType, Double>> updateThisPair : updateThisList) {
                Pair<Integer, Pair<VaultEntryType, Double>> updatedPair = new Pair(currentBucketOutputListPosition, updateThisPair.getValue());
                updatedList.add(updatedPair);
            }
            // update the BucketEntry listOfValuesForTheInterpolator list
            outputBucketList.get(outputBucketList.size() - 1).setListOfValuesForTheInterpolator(updatedList);
        }

        // =======================
        // ==INTERPOLATOR UPDATE==
        // =======================
        return outputBucketList;
    }

    /**
     *
     * This method receives a list that begins with a
     *
     *
     * liste beinhält alles vom ersten auftretten eines wertes bis zum letzten
     * vorkommenden wert anfang ohne NULL und ende ohne NULL
     *
     *
     * TODO für Adrian: Im averageForSmallesBucketSize werden alle werte die für
     * diese methode benötigt werden in eine liste gepackt und später in teil
     * listen unterteilt um dieser methode übergeben zu werden. Die werte werden
     * willkührlich nach fund in die liste aufgenommen und vor der übergabe
     * zwischen den einzelnen werten mit sinn vollen listen einträgen befüllt
     * die mit NULL symbolisieren das noch kein double Wert für diesen
     * BucketEntry vorhanden ist. Die rückgabe dieser methode wird dann zurück
     * in die zugehörigen BucketEntrys (anhand der bucketNumber) in die arrays
     * an der richtigen position eingetragen ... ggf. erst im FinalBucketEntry.
     *
     * Anmerkung von Timm: wegen der Art und weise wie wir die Methode benutzen
     * (Dh wir überprüfen ja nicht nochmal ob die liste korrekt ist, also ob
     * alle typen gleich sind) Sollten wir die Methode vllt private machen ->
     * protected um sie testen zu können
     *
     * @param input
     * @return
     */
//    protected List<Pair<Integer, Pair<VaultEntryType, Double>>> interpolateGaps(List<Pair<Integer, Pair<VaultEntryType, Double>>> input){
//        List<Pair<Double, Double>> calcValues = new ArrayList<>();
//        List<Pair<Integer, Pair<VaultEntryType, Double>>> result = new ArrayList<>();
//        VaultEntryType resultType = null;
//        Double tmpValue;
//        Integer tmpIndex;
//
//        //prepare the input data for interpolation, exclude null-values
//        for (Pair<Integer, Pair<VaultEntryType, Double>> pair : input) {
//            if(pair!=null && pair.getValue()!=null && pair.getKey()!=null){
//                tmpIndex = pair.getKey();
//                tmpValue = pair.getValue().getValue();
//                resultType = pair.getValue().getKey();
//
//                if(tmpValue != null){
//                    calcValues.add(new Pair(tmpIndex.doubleValue(), tmpValue));
//                }
//            }
//        }
//        SplineInterpolator sI = new SplineInterpolator(calcValues);
//
//        //compute each value that is null
//        for (Pair<Integer, Pair<VaultEntryType, Double>> pair : input) {
//            if(pair!=null && pair.getValue()!=null && pair.getKey()!=null){
//                tmpIndex = pair.getKey();
//                tmpValue = pair.getValue().getValue();
//
//                if(tmpValue == null){
//            //interpolation call: tmpValue = interpolate(tmpIndex.doubleValue()); vllt mit Runden?
//                    tmpValue = sI.interpolate(tmpIndex.doubleValue());
//                }
//                result.add(new Pair(tmpIndex, new Pair(resultType, tmpValue)));
//            }
//        }
//
//        return result;
//    }
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
        //    System.out.println(resultType);

        //    for (int i = 0; i < input.size(); i++) {
        //        Pair<Integer, Pair<VaultEntryType, Double>> tmp1 = input.get(i);
        //        Pair<Integer, Pair<VaultEntryType, Double>> tmp2 = result.get(i);
        //        System.out.println(tmp1.getKey() + " " + tmp1.getValue().getValue() + " " + tmp2.getValue().getValue());
        //    }
        //    System.out.println("interpolation end");
        return result;
    }
}