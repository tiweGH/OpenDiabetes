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
    
    // triggerEventActTimeGiven (Value given in VaultEntry)
    public static final HashSet<VaultEntryType> TRIGGEREVENTACTTIMEGIVEN;
    static
    {  
        TRIGGEREVENTACTTIMEGIVEN = new HashSet<>();
        TRIGGEREVENTACTTIMEGIVEN.add(VaultEntryType.SLEEP_LIGHT);
        TRIGGEREVENTACTTIMEGIVEN.add(VaultEntryType.SLEEP_REM);
        TRIGGEREVENTACTTIMEGIVEN.add(VaultEntryType.SLEEP_DEEP);
        TRIGGEREVENTACTTIMEGIVEN.add(VaultEntryType.LOC_TRANSISTION);
        TRIGGEREVENTACTTIMEGIVEN.add(VaultEntryType.LOC_HOME);
        TRIGGEREVENTACTTIMEGIVEN.add(VaultEntryType.LOC_WORK);
        TRIGGEREVENTACTTIMEGIVEN.add(VaultEntryType.LOC_FOOD);
        TRIGGEREVENTACTTIMEGIVEN.add(VaultEntryType.LOC_SPORTS);
        TRIGGEREVENTACTTIMEGIVEN.add(VaultEntryType.EXERCISE_OTHER);
        TRIGGEREVENTACTTIMEGIVEN.add(VaultEntryType.EXERCISE_WALK);
        TRIGGEREVENTACTTIMEGIVEN.add(VaultEntryType.EXERCISE_BICYCLE);
        TRIGGEREVENTACTTIMEGIVEN.add(VaultEntryType.EXERCISE_RUN);
        TRIGGEREVENTACTTIMEGIVEN.add(VaultEntryType.EXERCISE_MANUAL);
    }
    
    // triggerEventActTimeTillNextEvent
    public static final HashMap<VaultEntryType, VaultEntryType> TRIGGEREVENTACTTIMETILLNEXTEVENT;
    static
    {  
        TRIGGEREVENTACTTIMETILLNEXTEVENT = new HashMap<>();
        TRIGGEREVENTACTTIMETILLNEXTEVENT.put(VaultEntryType.CGM_CONNECTION_ERROR, VaultEntryType.GLUCOSE_CGM);
        TRIGGEREVENTACTTIMETILLNEXTEVENT.put(VaultEntryType.CGM_CALIBRATION_ERROR, VaultEntryType.GLUCOSE_CGM_CALIBRATION);
        TRIGGEREVENTACTTIMETILLNEXTEVENT.put(VaultEntryType.PUMP_RESERVOIR_EMPTY, VaultEntryType.PUMP_REWIND);
        TRIGGEREVENTACTTIMETILLNEXTEVENT.put(VaultEntryType.PUMP_AUTONOMOUS_SUSPEND, VaultEntryType.PUMP_UNSUSPEND);
        TRIGGEREVENTACTTIMETILLNEXTEVENT.put(VaultEntryType.PUMP_REWIND, VaultEntryType.PUMP_PRIME);
        TRIGGEREVENTACTTIMETILLNEXTEVENT.put(VaultEntryType.PUMP_SUSPEND, VaultEntryType.PUMP_UNSUSPEND);
    }
    
    // triggerEventActTimeOne (Alert for 1 Frame)
    public static final HashSet<VaultEntryType> TRIGGEREVENTACTTIMEONE;
    static
    {  
        TRIGGEREVENTACTTIMEONE = new HashSet<>();
        TRIGGEREVENTACTTIMEONE.add(VaultEntryType.GLUCOSE_CGM_ALERT);
        TRIGGEREVENTACTTIMEONE.add(VaultEntryType.CGM_SENSOR_FINISHED);
        TRIGGEREVENTACTTIMEONE.add(VaultEntryType.CGM_SENSOR_START);
        TRIGGEREVENTACTTIMEONE.add(VaultEntryType.PUMP_FILL_INTERPRETER);
    //    TRIGGEREVENTACTTIMEONE.add(VaultEntryType.PUMP_NO_DELIVERY);
        TRIGGEREVENTACTTIMEONE.add(VaultEntryType.PUMP_FILL);
    }
    
}
