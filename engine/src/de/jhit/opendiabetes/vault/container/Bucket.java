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
    private static final int numberOfEntries = 2;

    // time countdown Array
    private int[] timeCountDownArray;
    // onehot Boolean
    private boolean[] booleanArray;
    // onehot trigger
    private VaultEntryType[] entryTypeArray;
    // TODO missing wait till next entry
    private VaultEntryType[] findNextArray;


    public Bucket(VaultEntry entry) {
        vaultEntry = entry;

        // Arrays containing OneHot information
        timeCountDownArray = new int[numberOfEntries];
        booleanArray = new boolean[numberOfEntries];
        entryTypeArray = new VaultEntryType[numberOfEntries];
        findNextArray = new VaultEntryType[numberOfEntries];
    }

    //
    // GETTER VaultEntry
    //
    public VaultEntry getVaultEntry() {
        return vaultEntry;
    }

    //
    // GETTER - SETTER onhotArraySize
    //
    public static int getNumberOfEntries() {
        return numberOfEntries;
    }

    //
    // GETTER
    //
    // TODO get time countdown
    // ArrayOutOfBounds
    public int getTimeCountDown(int position) {
        return timeCountDownArray[position];
    }
    // TODO get boolean
    // ArrayOutOfBounds
    public Boolean getBoolean(int position) {
        return booleanArray[position];
    }
    // TODO get VaultEntryType
    // ArrayOutOfBounds
    public VaultEntryType getVaultEntryType(int position) {
        return entryTypeArray[position];
    }
    // TODO get find next
    // ArrayOutOfBounds
    public VaultEntryType getFindNextArray(int position) {
        return findNextArray[position];
    }

    //
    // SETTER
    //
    // TODO set time countdown
    // ArrayOutOfBounds
    public void setTimeCountDown(int position, int time) {
        timeCountDownArray[position] = time;
    }
    // TODO set boolean
    // ArrayOutOfBounds
    public void setBoolean(int position, Boolean bool) {
        booleanArray[position] = bool;
    }
    // TODO set VaultEntryType
    // ArrayOutOfBounds
    public void setVaultEntryType(int position, VaultEntryType entry) {
        entryTypeArray[position] = entry;
    }
    // TODO set find next
    // ArrayOutOfBounds
    public void setFindNextArray(int position, VaultEntryType entry) {
        findNextArray[position] = entry;
    }
    

}
