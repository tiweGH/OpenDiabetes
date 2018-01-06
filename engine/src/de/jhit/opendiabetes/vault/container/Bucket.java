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

/**
 *
 * @author aa80hifa
 */
public class Bucket {
    
    private VaultEntry vaultEntry;
    
    // Array size settings
    private static final int numberOfVaultEntryTriggerTypes = 2;

    // time countdown Array
    private int[] timeCountDownArray;
    // onehot Boolean
    private boolean[] booleanArray;
    // onehot trigger
    private VaultEntryType[] entryTypeArray;
    // wait till next entry
    private VaultEntryType[] findNextArray;

    
    public Bucket(VaultEntry entry) {
        vaultEntry = entry;

        // Arrays containing OneHot information
        timeCountDownArray = new int[numberOfVaultEntryTriggerTypes];
        booleanArray = new boolean[numberOfVaultEntryTriggerTypes];
        entryTypeArray = new VaultEntryType[numberOfVaultEntryTriggerTypes];
        findNextArray = new VaultEntryType[numberOfVaultEntryTriggerTypes];
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
        return numberOfVaultEntryTriggerTypes;
    }

    //
    // GETTER
    //
    // get time countdown
    // ArrayOutOfBounds
    public int getTimeCountDown(int position) {
        return timeCountDownArray[position];
    }
    // get boolean
    // ArrayOutOfBounds
    public boolean getBoolean(int position) {
        return booleanArray[position];
    }
    // get VaultEntryType
    // ArrayOutOfBounds
    public VaultEntryType getVaultEntryType(int position) {
        return entryTypeArray[position];
    }
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
    public void setTimeCountDown(int position, int time) {
        timeCountDownArray[position] = time;
    }
    // set boolean
    // ArrayOutOfBounds
    public void setBoolean(int position, boolean bool) {
        booleanArray[position] = bool;
    }
    // set VaultEntryType
    // ArrayOutOfBounds
    public void setVaultEntryType(int position, VaultEntryType entry) {
        entryTypeArray[position] = entry;
    }
    // set find next
    // ArrayOutOfBounds
    public void setFindNextArray(int position, VaultEntryType entry) {
        findNextArray[position] = entry;
    }
    

}
