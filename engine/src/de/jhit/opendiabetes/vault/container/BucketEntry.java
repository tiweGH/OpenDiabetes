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
import java.util.Arrays;

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
    // onehot trigger
//    private VaultEntryType[] entryTypeArray;
    // wait till next entry
    private VaultEntryType[] findNextArray;

    
    public BucketEntry(int bucketNumber,VaultEntry entry) {
        vaultEntry = entry;

        // counter
        bucketEntryNumber = bucketNumber;
        
        // Arrays containing OneHot information
        timeCountDownArray = new double[NUMBER_OF_VAULT_ENTRY_TRIGGER_TYPES];
        onehotInformationArray = new double[NUMBER_OF_VAULT_ENTRY_TRIGGER_TYPES];
//        entryTypeArray = new VaultEntryType[NUMBEROFVAULTENTRYTRIGGERTYPES];
        findNextArray = new VaultEntryType[NUMBER_OF_VAULT_ENTRY_TRIGGER_TYPES];
        
        // Fill findNextArray with EMPTY
        Arrays.fill(findNextArray, VaultEntryType.EMPTY);
    }
    
    // 
    // GETTER bucket entry number
    // only getter since bucket entry number is set when BucketEntry is created
    // 
    // get bucket entry number
    public int getBucketNumber() {
        return bucketEntryNumber;
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
        return timeCountDownArray;
    }
    // get full time countdown
    public double[] getFullOnehotInformationArray() {
        return onehotInformationArray;
    }
    // get full time countdown
    public VaultEntryType[] getFullFindNextArray() {
        return findNextArray;
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
    // get VaultEntryType
    // ArrayOutOfBounds
//    public VaultEntryType getVaultEntryType(int position) {
//        return entryTypeArray[position];
//    }
    // get find next
    // ArrayOutOfBounds
    public VaultEntryType getFindNextArray(int position) {
        return findNextArray[position];
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
    public void setOnehotInformationArray(int position, double bool) {
        onehotInformationArray[position] = bool;
    }
    // set VaultEntryType
    // ArrayOutOfBounds
//    public void setVaultEntryType(int position, VaultEntryType entry) {
//        entryTypeArray[position] = entry;
//    }
    // set find next
    // ArrayOutOfBounds
    public void setFindNextArray(int position, VaultEntryType entry) {
        findNextArray[position] = entry;
    }
    
}
