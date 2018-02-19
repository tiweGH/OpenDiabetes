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
package de.jhit.opendiabetes.vault.container;

import static de.jhit.opendiabetes.vault.container.BucketEventTriggers.ARRAY_ENTRY_TRIGGER_HASHMAP;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import javafx.util.Pair;

/**
 *
 * @author aa80hifa
 */
public class BucketEntry {

    private VaultEntry vaultEntry;

    // BucketEntry list counter
    private int bucketEntryNumber;

    // Array size settings
    // numberOfVaultEntryTriggerTypes
    private static final int NUMBER_OF_VAULT_ENTRY_TRIGGER_TYPES = ARRAY_ENTRY_TRIGGER_HASHMAP.size();

    // time countdown Array
    private double[] timeCountDownArray;
    // onehot information Array
    private double[] onehotInformationArray;
    // arrays for ML-rev and NOT one hot - parallel computing act times
    // first double == timer
    // second double == value
    private List<Pair<VaultEntryType, Pair<Double, Double>>> runningComputation;
    // wait till next entry
    private VaultEntryType[] findNextArray;
    
    // this is the list of final sum and avg calculations
    private List<Pair<VaultEntryType, Double>> listOfComputedValuesForTheFinalBucketEntry;
    // this list is for calculating the linear interpolator
    // this list must be sorted for each VaultEntryType and filled will NULLs to show the interpolateGaps method what needs to be calculated
    private List<Pair<Integer, Pair<VaultEntryType, Double>>> listOfValuesForTheInterpolator;

    public BucketEntry(int bucketNumber, VaultEntry entry) {
        vaultEntry = entry;

        // counter
        bucketEntryNumber = bucketNumber;

        // Arrays containing OneHot information
        timeCountDownArray = new double[NUMBER_OF_VAULT_ENTRY_TRIGGER_TYPES];
        onehotInformationArray = new double[NUMBER_OF_VAULT_ENTRY_TRIGGER_TYPES];
        runningComputation = new ArrayList<>();
        findNextArray = new VaultEntryType[NUMBER_OF_VAULT_ENTRY_TRIGGER_TYPES];
        
        listOfComputedValuesForTheFinalBucketEntry = new ArrayList<>();
        listOfValuesForTheInterpolator = new ArrayList<>();

        // Fill findNextArray with EMPTY
        Arrays.fill(findNextArray, VaultEntryType.EMPTY);
    }

    //
    // GETTER - SETTER bucket entry number
    //
    // get bucket entry number
    public int getBucketNumber() {
        return bucketEntryNumber;
    }

    // set bucket entry number
    public void setBucketNumber(int newNumber) {
        bucketEntryNumber = newNumber;
    }

    //
    // GETTER VaultEntry
    //
    public VaultEntry getVaultEntry() {
        return vaultEntry;
    }

    //
    // GETTER onehotArraySize
    //
    public static int getNumberOfVaultEntryTriggerTypes() {
        return NUMBER_OF_VAULT_ENTRY_TRIGGER_TYPES;
    }

    //
    // GETTER full arrays of a BucketEntry
    //
    // get full time countdown
    public double[] getFullTimeCountDown() {
        return timeCountDownArray.clone();
    }

    // get full time countdown
    public double[] getFullOnehotInformationArray() {
        return onehotInformationArray.clone();
    }

    // get full time countdown
    public VaultEntryType[] getFullFindNextArray() {
        return findNextArray.clone();
    }

    //
    // GETTER
    //
    // get time countdown
    // ArrayOutOfBounds
    public double getTimeCountDown(int position) {
        return timeCountDownArray[position];
    }

    // get boolean
    // ArrayOutOfBounds
    public double getOnehotInformationArray(int position) {
        return onehotInformationArray[position];
    }

    // get runningComputations
    public List<Pair<VaultEntryType, Pair<Double, Double>>> getRunningComputation() {
        return runningComputation;
    }
    
    // get find next
    // ArrayOutOfBounds
    public VaultEntryType getFindNextArray(int position) {
        return findNextArray[position];
    }
    
    // get list of computed values for the final bucket entry
    public List<Pair<VaultEntryType, Double>> getListOfComputedValuesForTheFinalBucketEntry(){
        return listOfComputedValuesForTheFinalBucketEntry;
    }
    
    // get list of values for the interpolator
    public List<Pair<Integer, Pair<VaultEntryType, Double>>> getListOfValuesForTheInterpolator(){
        return listOfValuesForTheInterpolator;
    }


    //
    // SETTER
    //
    // set time countdown
    // ArrayOutOfBounds
    public void setTimeCountDown(int position, double time) {
        timeCountDownArray[position] = time;
    }

    // set boolean
    // ArrayOutOfBounds
    public void setOnehotInformationArray(int position, double value) {
        onehotInformationArray[position] = value;
    }

    // set runningComputations
    // list will be newly generated with the given input
    public void setRunningComputation(List<Pair<VaultEntryType, Pair<Double, Double>>> newList) {
        runningComputation =  new ArrayList<>();
        runningComputation = newList;
    }
    
    // set find next
    // ArrayOutOfBounds
    public void setFindNextArray(int position, VaultEntryType entry) {
        findNextArray[position] = entry;
    }
    
    // set list of computed values for the final bucket entry
    public void setListOfComputedValuesForTheFinalBucketEntry(List<Pair<VaultEntryType, Double>> list){
        listOfComputedValuesForTheFinalBucketEntry =  new ArrayList<>();
        listOfComputedValuesForTheFinalBucketEntry = list;
    }

    // set list of values for the interpolator
    public void setListOfValuesForTheInterpolator(List<Pair<Integer, Pair<VaultEntryType, Double>>> list){
        listOfValuesForTheInterpolator =  new ArrayList<>();
        listOfValuesForTheInterpolator = list;
    }
    
    @Override
    public String toString(){
        String result = "BucketEntry{id="+this.getBucketNumber()
                +", "+this.getVaultEntry().toString()
                +", findNextArray="+Arrays.toString(this.findNextArray)
                +", onehotInformationArray="+Arrays.toString(this.onehotInformationArray)
                +", timeCountDownArray="+Arrays.toString(this.timeCountDownArray)
                +", runningComputation="+this.runningComputation.toString()
                +", listOfComputedValuesForTheFinalBucketEntry="+this.listOfComputedValuesForTheFinalBucketEntry.toString()
                +", listOfValuesForTheInterpolator="+this.listOfValuesForTheInterpolator.toString()
                +"}";
        return result;//System.lineSeparator();
    }
}
