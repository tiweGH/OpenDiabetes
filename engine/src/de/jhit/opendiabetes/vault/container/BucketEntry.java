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

import static de.jhit.opendiabetes.vault.container.BucketEventTriggers.ARRAYENTRYTRIGGERHASHMAP;

/**
 *
 * @author aa80hifa
 */
public class BucketEntry {
    
    private VaultEntry vaultEntry;
    
    // Array size settings
    // numberOfVaultEntryTriggerTypes
    private static final int NUMBEROFVAULTENTRYTRIGGERTYPES = ARRAYENTRYTRIGGERHASHMAP.size();

    // time countdown Array
    private double[] timeCountDownArray;
    // onehot Boolean
    private boolean[] booleanArray;
    // onehot trigger
//    private VaultEntryType[] entryTypeArray;
    // wait till next entry
    private VaultEntryType[] findNextArray;

    
    public BucketEntry(VaultEntry entry) {
        vaultEntry = entry;

        // Arrays containing OneHot information
        timeCountDownArray = new double[NUMBEROFVAULTENTRYTRIGGERTYPES];
        booleanArray = new boolean[NUMBEROFVAULTENTRYTRIGGERTYPES];
//        entryTypeArray = new VaultEntryType[NUMBEROFVAULTENTRYTRIGGERTYPES];
        findNextArray = new VaultEntryType[NUMBEROFVAULTENTRYTRIGGERTYPES];
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
        return NUMBEROFVAULTENTRYTRIGGERTYPES;
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
    public boolean getBoolean(int position) {
        return booleanArray[position];
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
    public void setBoolean(int position, boolean bool) {
        booleanArray[position] = bool;
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
