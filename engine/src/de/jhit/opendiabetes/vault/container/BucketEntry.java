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

import java.util.HashMap;

/**
 *
 * @author aa80hifa
 */
public class BucketEntry {
    
    // ArrayEntryTriggerHashMap
    public static final HashMap<VaultEntryType, Integer> ARRAYENTRYTRIGGERHASHMAP;
    static
    {  
        ARRAYENTRYTRIGGERHASHMAP = new HashMap<>();
        ARRAYENTRYTRIGGERHASHMAP.put(VaultEntryType.GLUCOSE_CGM_ALERT, 0);
        ARRAYENTRYTRIGGERHASHMAP.put(VaultEntryType.CGM_SENSOR_FINISHED, 1);
        ARRAYENTRYTRIGGERHASHMAP.put(VaultEntryType.CGM_SENSOR_START, 2);
        ARRAYENTRYTRIGGERHASHMAP.put(VaultEntryType.CGM_CONNECTION_ERROR, 3);
        ARRAYENTRYTRIGGERHASHMAP.put(VaultEntryType.CGM_CALIBRATION_ERROR, 4);
        ARRAYENTRYTRIGGERHASHMAP.put(VaultEntryType.PUMP_FILL_INTERPRETER, 5);
        ARRAYENTRYTRIGGERHASHMAP.put(VaultEntryType.PUMP_NO_DELIVERY, 6);
        ARRAYENTRYTRIGGERHASHMAP.put(VaultEntryType.PUMP_RESERVOIR_EMPTY, 7);
        ARRAYENTRYTRIGGERHASHMAP.put(VaultEntryType.PUMP_AUTONOMOUS_SUSPEND, 8);
        ARRAYENTRYTRIGGERHASHMAP.put(VaultEntryType.SLEEP_LIGHT, 9);
        ARRAYENTRYTRIGGERHASHMAP.put(VaultEntryType.SLEEP_REM, 10);
        ARRAYENTRYTRIGGERHASHMAP.put(VaultEntryType.SLEEP_DEEP, 11);
        ARRAYENTRYTRIGGERHASHMAP.put(VaultEntryType.LOC_TRANSISTION, 12);
        ARRAYENTRYTRIGGERHASHMAP.put(VaultEntryType.LOC_HOME, 13);
        ARRAYENTRYTRIGGERHASHMAP.put(VaultEntryType.LOC_WORK, 14);
        ARRAYENTRYTRIGGERHASHMAP.put(VaultEntryType.LOC_FOOD, 15);
        ARRAYENTRYTRIGGERHASHMAP.put(VaultEntryType.LOC_SPORTS, 16);
        ARRAYENTRYTRIGGERHASHMAP.put(VaultEntryType.EXERCISE_OTHER, 17);
        ARRAYENTRYTRIGGERHASHMAP.put(VaultEntryType.EXERCISE_WALK, 18);
        ARRAYENTRYTRIGGERHASHMAP.put(VaultEntryType.EXERCISE_BICYCLE, 19);
        ARRAYENTRYTRIGGERHASHMAP.put(VaultEntryType.EXERCISE_RUN, 20);
        ARRAYENTRYTRIGGERHASHMAP.put(VaultEntryType.EXERCISE_MANUAL, 21);
        ARRAYENTRYTRIGGERHASHMAP.put(VaultEntryType.PUMP_REWIND, 22);
        ARRAYENTRYTRIGGERHASHMAP.put(VaultEntryType.PUMP_FILL, 23);
        ARRAYENTRYTRIGGERHASHMAP.put(VaultEntryType.PUMP_SUSPEND, 24);
    }
    
    private VaultEntry vaultEntry;
    
    // Array size settings
    // numberOfVaultEntryTriggerTypes
    private static final int NUMBEROFVAULTENTRYTRIGGERTYPES = ARRAYENTRYTRIGGERHASHMAP.size();

    // time countdown Array
    private int[] timeCountDownArray;
    // onehot Boolean
    private boolean[] booleanArray;
    // onehot trigger
//    private VaultEntryType[] entryTypeArray;
    // wait till next entry
    private VaultEntryType[] findNextArray;

    
    public BucketEntry(VaultEntry entry) {
        vaultEntry = entry;

        // Arrays containing OneHot information
        timeCountDownArray = new int[NUMBEROFVAULTENTRYTRIGGERTYPES];
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
//    public void setVaultEntryType(int position, VaultEntryType entry) {
//        entryTypeArray[position] = entry;
//    }
    // set find next
    // ArrayOutOfBounds
    public void setFindNextArray(int position, VaultEntryType entry) {
        findNextArray[position] = entry;
    }
    

}
