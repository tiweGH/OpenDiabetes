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
import java.util.HashSet;

/**
 *
 * @author aa80hifa
 */
public class BucketEventTriggers {
    
    // ArrayEntryTriggerHashMap
    public static final HashMap<VaultEntryType, Integer> ARRAY_ENTRY_TRIGGER_HASHMAP;
    static
    {  
        ARRAY_ENTRY_TRIGGER_HASHMAP = new HashMap<>();
        ARRAY_ENTRY_TRIGGER_HASHMAP.put(VaultEntryType.GLUCOSE_CGM_ALERT, 0);
        ARRAY_ENTRY_TRIGGER_HASHMAP.put(VaultEntryType.CGM_SENSOR_FINISHED, 1);
        ARRAY_ENTRY_TRIGGER_HASHMAP.put(VaultEntryType.CGM_SENSOR_START, 2);
        ARRAY_ENTRY_TRIGGER_HASHMAP.put(VaultEntryType.CGM_CONNECTION_ERROR, 3);
        ARRAY_ENTRY_TRIGGER_HASHMAP.put(VaultEntryType.CGM_CALIBRATION_ERROR, 4);
        ARRAY_ENTRY_TRIGGER_HASHMAP.put(VaultEntryType.PUMP_FILL_INTERPRETER, 5);
        ARRAY_ENTRY_TRIGGER_HASHMAP.put(VaultEntryType.PUMP_NO_DELIVERY, 6);
        ARRAY_ENTRY_TRIGGER_HASHMAP.put(VaultEntryType.PUMP_RESERVOIR_EMPTY, 7);
        ARRAY_ENTRY_TRIGGER_HASHMAP.put(VaultEntryType.PUMP_AUTONOMOUS_SUSPEND, 8);
        ARRAY_ENTRY_TRIGGER_HASHMAP.put(VaultEntryType.SLEEP_LIGHT, 9);
        ARRAY_ENTRY_TRIGGER_HASHMAP.put(VaultEntryType.SLEEP_REM, 10);
        ARRAY_ENTRY_TRIGGER_HASHMAP.put(VaultEntryType.SLEEP_DEEP, 11);
        ARRAY_ENTRY_TRIGGER_HASHMAP.put(VaultEntryType.LOC_TRANSISTION, 12);
        ARRAY_ENTRY_TRIGGER_HASHMAP.put(VaultEntryType.LOC_HOME, 13);
        ARRAY_ENTRY_TRIGGER_HASHMAP.put(VaultEntryType.LOC_WORK, 14);
        ARRAY_ENTRY_TRIGGER_HASHMAP.put(VaultEntryType.LOC_FOOD, 15);
        ARRAY_ENTRY_TRIGGER_HASHMAP.put(VaultEntryType.LOC_SPORTS, 16);
        ARRAY_ENTRY_TRIGGER_HASHMAP.put(VaultEntryType.EXERCISE_OTHER, 17);
        ARRAY_ENTRY_TRIGGER_HASHMAP.put(VaultEntryType.EXERCISE_WALK, 18);
        ARRAY_ENTRY_TRIGGER_HASHMAP.put(VaultEntryType.EXERCISE_BICYCLE, 19);
        ARRAY_ENTRY_TRIGGER_HASHMAP.put(VaultEntryType.EXERCISE_RUN, 20);
        ARRAY_ENTRY_TRIGGER_HASHMAP.put(VaultEntryType.EXERCISE_MANUAL, 21);
        ARRAY_ENTRY_TRIGGER_HASHMAP.put(VaultEntryType.PUMP_REWIND, 22);
        ARRAY_ENTRY_TRIGGER_HASHMAP.put(VaultEntryType.PUMP_FILL, 23);
        ARRAY_ENTRY_TRIGGER_HASHMAP.put(VaultEntryType.PUMP_SUSPEND, 24);
    }
    
    // triggerEventActTimeGiven (Value given in VaultEntry)
    public static final HashSet<VaultEntryType> TRIGGER_EVENT_ACT_TIME_GIVEN;
    static
    {  
        TRIGGER_EVENT_ACT_TIME_GIVEN = new HashSet<>();
        TRIGGER_EVENT_ACT_TIME_GIVEN.add(VaultEntryType.SLEEP_LIGHT);
        TRIGGER_EVENT_ACT_TIME_GIVEN.add(VaultEntryType.SLEEP_REM);
        TRIGGER_EVENT_ACT_TIME_GIVEN.add(VaultEntryType.SLEEP_DEEP);
        TRIGGER_EVENT_ACT_TIME_GIVEN.add(VaultEntryType.LOC_TRANSISTION);
        TRIGGER_EVENT_ACT_TIME_GIVEN.add(VaultEntryType.LOC_HOME);
        TRIGGER_EVENT_ACT_TIME_GIVEN.add(VaultEntryType.LOC_WORK);
        TRIGGER_EVENT_ACT_TIME_GIVEN.add(VaultEntryType.LOC_FOOD);
        TRIGGER_EVENT_ACT_TIME_GIVEN.add(VaultEntryType.LOC_SPORTS);
        TRIGGER_EVENT_ACT_TIME_GIVEN.add(VaultEntryType.EXERCISE_OTHER);
        TRIGGER_EVENT_ACT_TIME_GIVEN.add(VaultEntryType.EXERCISE_WALK);
        TRIGGER_EVENT_ACT_TIME_GIVEN.add(VaultEntryType.EXERCISE_BICYCLE);
        TRIGGER_EVENT_ACT_TIME_GIVEN.add(VaultEntryType.EXERCISE_RUN);
        TRIGGER_EVENT_ACT_TIME_GIVEN.add(VaultEntryType.EXERCISE_MANUAL);
    }
    
    // triggerEventActTimeTillNextEvent
    public static final HashMap<VaultEntryType, VaultEntryType> TRIGGER_EVENT_ACT_TIME_TILL_NEXT_EVENT;
    static
    {  
        TRIGGER_EVENT_ACT_TIME_TILL_NEXT_EVENT = new HashMap<>();
        TRIGGER_EVENT_ACT_TIME_TILL_NEXT_EVENT.put(VaultEntryType.CGM_CONNECTION_ERROR, VaultEntryType.GLUCOSE_CGM);
        TRIGGER_EVENT_ACT_TIME_TILL_NEXT_EVENT.put(VaultEntryType.CGM_CALIBRATION_ERROR, VaultEntryType.GLUCOSE_CGM_CALIBRATION);
        TRIGGER_EVENT_ACT_TIME_TILL_NEXT_EVENT.put(VaultEntryType.PUMP_RESERVOIR_EMPTY, VaultEntryType.PUMP_REWIND);
        TRIGGER_EVENT_ACT_TIME_TILL_NEXT_EVENT.put(VaultEntryType.PUMP_AUTONOMOUS_SUSPEND, VaultEntryType.PUMP_UNSUSPEND);
        TRIGGER_EVENT_ACT_TIME_TILL_NEXT_EVENT.put(VaultEntryType.PUMP_REWIND, VaultEntryType.PUMP_PRIME);
        TRIGGER_EVENT_ACT_TIME_TILL_NEXT_EVENT.put(VaultEntryType.PUMP_SUSPEND, VaultEntryType.PUMP_UNSUSPEND);
    }
    
    // triggerEventActTimeOne (Alert for 1 Frame)
    public static final HashSet<VaultEntryType> TRIGGER_EVENT_ACT_TIME_ONE;
    static
    {  
        TRIGGER_EVENT_ACT_TIME_ONE = new HashSet<>();
        TRIGGER_EVENT_ACT_TIME_ONE.add(VaultEntryType.GLUCOSE_CGM_ALERT);
        TRIGGER_EVENT_ACT_TIME_ONE.add(VaultEntryType.CGM_SENSOR_FINISHED);
        TRIGGER_EVENT_ACT_TIME_ONE.add(VaultEntryType.CGM_SENSOR_START);
        TRIGGER_EVENT_ACT_TIME_ONE.add(VaultEntryType.PUMP_FILL_INTERPRETER);
    //    TRIGGER_EVENT_ACT_TIME_ONE.add(VaultEntryType.PUMP_NO_DELIVERY);
        TRIGGER_EVENT_ACT_TIME_ONE.add(VaultEntryType.PUMP_FILL);
    }
    
}
