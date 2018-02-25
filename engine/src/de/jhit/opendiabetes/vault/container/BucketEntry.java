/*
 * Copyright (C) 2018 a.a.aponte
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
 * @author a.a.aponte
 */
public class BucketEntry {

    private VaultEntry vaultEntry;

    // BucketEntry list counter
    private int bucketEntryNumber;

    // Array size settings
    // numberOfVaultEntryTriggerTypes
    private static final int NUMBER_OF_VAULT_ENTRY_TRIGGER_TYPES = ARRAY_ENTRY_TRIGGER_HASHMAP.size();

    // time countdown Array
    private double[] valueTimer;
    // onehot information Array
    private double[] values;
    // arrays for ML-rev and NOT one hot - parallel computing act times
    // first double == timer
    // second double == value
    private List<Pair<VaultEntryType, Pair<Double, Double>>> valuesForRunningComputation;
    // wait till next entry
    private VaultEntryType[] findNextVaultEntryType;
    
    // this is the list of final sum and avg calculations
    private List<Pair<VaultEntryType, Double>> computedValuesForTheFinalBucketEntry;
    // this list is for calculating the linear interpolator
    // this list must be sorted for each VaultEntryType and filled will NULLs to show the interpolateGaps method what needs to be calculated
    private List<Pair<Integer, Pair<VaultEntryType, Double>>> valuesForTheInterpolator;

    public BucketEntry(int bucketNumber, VaultEntry entry) {
        vaultEntry = entry;

        // counter
        bucketEntryNumber = bucketNumber;

        // Arrays containing OneHot information
        valueTimer = new double[NUMBER_OF_VAULT_ENTRY_TRIGGER_TYPES];
        values = new double[NUMBER_OF_VAULT_ENTRY_TRIGGER_TYPES];
        valuesForRunningComputation = new ArrayList<>();
        findNextVaultEntryType = new VaultEntryType[NUMBER_OF_VAULT_ENTRY_TRIGGER_TYPES];
        
        computedValuesForTheFinalBucketEntry = new ArrayList<>();
        valuesForTheInterpolator = new ArrayList<>();

        // Fill findNextVaultEntryType with EMPTY
        Arrays.fill(findNextVaultEntryType, VaultEntryType.EMPTY);
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
    public double[] getFullValueTimer() {
        return valueTimer.clone();
    }

    // get full time countdown
    public double[] getFullValues() {
        return values.clone();
    }

    // get full time countdown
    public VaultEntryType[] getFullFindNextVaultEntryType() {
        return findNextVaultEntryType.clone();
    }

    //
    // GETTER
    //
    // get time countdown
    // ArrayOutOfBounds
    public double getValueTimer(int position) {
        return valueTimer[position];
    }

    // get boolean
    // ArrayOutOfBounds
    public double getValues(int position) {
        return values[position];
    }

    // get runningComputations
    public List<Pair<VaultEntryType, Pair<Double, Double>>> getValuesForRunningComputation() {
        return valuesForRunningComputation;
    }
    
    // get find next
    // ArrayOutOfBounds
    public VaultEntryType getFindNextVaultEntryType(int position) {
        return findNextVaultEntryType[position];
    }
    
    // get list of computed values for the final bucket entry
    public List<Pair<VaultEntryType, Double>> getComputedValuesForTheFinalBucketEntry(){
        return computedValuesForTheFinalBucketEntry;
    }
    
    // get list of values for the interpolator
    public List<Pair<Integer, Pair<VaultEntryType, Double>>> getValuesForTheInterpolator(){
        return valuesForTheInterpolator;
    }


    //
    // SETTER
    //
    // set time countdown
    // ArrayOutOfBounds
    public void setValueTimer(int position, double time) {
        valueTimer[position] = time;
    }

    // set boolean
    // ArrayOutOfBounds
    public void setValues(int position, double value) {
        values[position] = value;
    }

    // set runningComputations
    // list will be newly generated with the given input
    public void setValuesForRunningComputation(List<Pair<VaultEntryType, Pair<Double, Double>>> newList) {
        valuesForRunningComputation =  new ArrayList<>();
        valuesForRunningComputation = newList;
    }
    
    // set find next
    // ArrayOutOfBounds
    public void setFindNextVaultEntryType(int position, VaultEntryType entry) {
        findNextVaultEntryType[position] = entry;
    }
    
    // set list of computed values for the final bucket entry
    public void setComputedValuesForTheFinalBucketEntry(List<Pair<VaultEntryType, Double>> list){
        computedValuesForTheFinalBucketEntry =  new ArrayList<>();
        computedValuesForTheFinalBucketEntry = list;
    }

    // set list of values for the interpolator
    public void setValuesForTheInterpolator(List<Pair<Integer, Pair<VaultEntryType, Double>>> list){
        valuesForTheInterpolator =  new ArrayList<>();
        valuesForTheInterpolator = list;
    }
    
    @Override
    public String toString(){
        String result = "BucketEntry{id="+this.getBucketNumber()
                +", "+this.getVaultEntry().toString()
                +", findNextArray="+Arrays.toString(this.findNextVaultEntryType)
                +", onehotInformationArray="+Arrays.toString(this.values)
                +", timeCountDownArray="+Arrays.toString(this.valueTimer)
                +", runningComputation="+this.valuesForRunningComputation.toString()
                +", listOfComputedValuesForTheFinalBucketEntry="+this.computedValuesForTheFinalBucketEntry.toString()
                +", listOfValuesForTheInterpolator="+this.valuesForTheInterpolator.toString()
                +"}";
        return result;//System.lineSeparator();
    }
}
