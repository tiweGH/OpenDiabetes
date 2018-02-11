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

import static de.jhit.opendiabetes.vault.container.BucketEventTriggers.ARRAY_ENTRIES_AFTER_MERGE_TO;

/**
 *
 * @author aa80hifa
 */
public class FinalBucketEntry {
    
    // BucketEntry list counter
    private int bucketEntryNumber;

    // Array size settings
    // numberOfVaultEntryTriggerTypes
    private static final int NUMBER_OF_VAULT_ENTRY_TRIGGER_TYPES_AFTER_MERGE = ARRAY_ENTRIES_AFTER_MERGE_TO.size();

    // onehot information Array
    private double[] onehotInformationArray;

    public FinalBucketEntry(int bucketNumber) {

        // counter
        bucketEntryNumber = bucketNumber;

        // Arrays containing OneHot information
        onehotInformationArray = new double[NUMBER_OF_VAULT_ENTRY_TRIGGER_TYPES_AFTER_MERGE];
        
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
    // GETTER onehotArraySize
    //
    public static int getNumberOfVaultEntryTriggerTypes() {
        return NUMBER_OF_VAULT_ENTRY_TRIGGER_TYPES_AFTER_MERGE;
    }

    //
    // GETTER - SETTER full arrays of a BucketEntry
    //
    // get full time countdown
    public double[] getFullOnehotInformationArray() {
        return onehotInformationArray.clone();
    }

    // set full time countdown
    public void setFullOnehotInformationArray(double[] array) {
        onehotInformationArray = array;
    }

    //
    // GETTER
    //
    // get boolean
    // ArrayOutOfBounds
    public double getOnehotInformationArray(int position) {
        return onehotInformationArray[position];
    }

    //
    // SETTER
    //
    // set boolean
    // ArrayOutOfBounds
    public void setOnehotInformationArray(int position, double bool) {
        onehotInformationArray[position] = bool;
    }
}
