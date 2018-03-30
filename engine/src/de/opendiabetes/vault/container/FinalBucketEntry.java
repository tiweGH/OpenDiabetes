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
package de.opendiabetes.vault.container;

import static de.opendiabetes.vault.container.BucketEventTriggers.ARRAY_ENTRIES_AFTER_MERGE_TO;
import java.io.IOException;
import java.util.Arrays;

/**
 * This class contains the constructor and all needed getters and setters for a
 * FinalBucketEntry.
 *
 * @author a.a.aponte
 */
public class FinalBucketEntry implements de.opendiabetes.vault.container.csv.ExportEntry {

    // BucketEntry list counter
    private int bucketEntryNumber;

    // Array size settings
    // numberOfVaultEntryTriggerTypes
    private static final int NUMBER_OF_VAULT_ENTRY_TRIGGER_TYPES_AFTER_MERGE = ARRAY_ENTRIES_AFTER_MERGE_TO.size();

    // onehot information Array
    private double[] values;

    /**
     * This constructor generates an empty FinalBucketEntry and creates the
     * needed array with the correct size to save all information needed for the
     * machine lerner.
     *
     * @param bucketNumber This is the consecutive BucketEntry number.
     */
    public FinalBucketEntry(int bucketNumber) {

        // counter
        bucketEntryNumber = bucketNumber;

        // Arrays containing OneHot information
        values = new double[NUMBER_OF_VAULT_ENTRY_TRIGGER_TYPES_AFTER_MERGE];

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
    // get values
    public double[] getFullValues() {
        return values.clone();
    }

    // set values
    public void setFullValues(double[] array) {
        values = array;
    }

    //
    // GETTER
    //
    // get value
    // ArrayOutOfBounds
    public double getValues(int position) {
        return values[position];
    }

    //
    // SETTER
    //
    // set value
    // ArrayOutOfBounds
    public void setValues(int position, double value) {
        values[position] = value;
    }

    @Override
    public byte[] toByteEntryLine() throws IOException {
        String line = Arrays.toString(this.values);
        line = line.replace("[", "");
        line = line.replace("]", "");
        line = bucketEntryNumber + ", " + line;

        return line.getBytes();
    }
}
