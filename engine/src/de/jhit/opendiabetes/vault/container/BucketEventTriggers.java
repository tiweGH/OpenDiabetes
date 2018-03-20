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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;

/**
 * overview of all hashsets and hashmaps =====================================
 *
 * ARRAY_ENTRY_TRIGGER_HASHMAP -> All triggers (ML + OH && ML + !OH)
 *
 * === ML + OH ===
 *
 * TRIGGER_EVENT_ACT_TIME_GIVEN -> triggers (ML + OH) with a given act time in
 * value
 *
 * TRIGGER_EVENT_ACT_TIME_TILL_NEXT_EVENT -> triggers (ML + OH) with an act time
 * till a certain VaultEntryType
 *
 * TRIGGER_EVENT_ACT_TIME_ONE -> triggers (ML + OH) with an act time of one
 * frame (Bucket // one minute)
 *
 * TRIGGER_EVENTS_NOT_YET_SET -> triggers (ML + OH) that are not set yet
 *
 * === ML + !OH ===
 *
 * TRIGGER_EVENT_NOT_ONE_HOT_ACT_TIME_SET -> triggers (ML + !OH) with a set act
 * time
 *
 * TRIGGER_EVENT_NOT_ONE_HOT_ACT_TIME_GIVEN -> triggers (ML + !OH) with a given
 * act time in value2
 *
 * TRIGGER_EVENT_NOT_ONE_HOT_ACT_TIME_TILL_NEXT_EVENT -> triggers (ML + !OH)
 * with an act time till ...
 *
 * TRIGGER_EVENT_NOT_ONE_HOT_ACT_TIME_ONE -> triggers (ML + !OH) with an act
 * time of one frame (bucket // one minute) value will be displayed
 *
 * TRIGGER_EVENT_NOT_ONE_HOT_VALUE_IS_A_TIMESTAMP -> triggers (ML + !OH) with a
 * timestamp as value ???
 */

/**
 * This class contains all the HashSets and HashMaps needed during the creation
 * of BucketEntrys and for the computation of internal data.
 * 
 * @author a.a.aponte
 */
public class BucketEventTriggers {
    public static final HashMap<VaultEntryType, Integer> ARRAY_ENTRY_TRIGGER_HASHMAP__TEST = new HashMap<>();
    public static final HashMap<VaultEntryType, Integer> ARRAY_ENTRIES_AFTER_MERGE_TO__TEST = new HashMap<>();
    
    /**
     * This method fills the HashMaps for the VaultEntryTypes and 
     * VaultEntry-merge-to-Types.
     */
    public BucketEventTriggers() {
        int indexType = 0;
        int indexMergeTo = 0;
        for (VaultEntryType type : VaultEntryType.values()) {
            // fill VaultEntryType HashMap
            ARRAY_ENTRY_TRIGGER_HASHMAP__TEST.put(type, indexType);
            indexType++;
            /*
            if (!ARRAY_ENTRY_TRIGGER_HASHMAP__TEST.containsKey(type)) {
                ARRAY_ENTRY_TRIGGER_HASHMAP__TEST.put(type, indexType);
                indexType++;
            }
            */
            // fill VaultEntryType merge-to HashMap
            VaultEntryType mergeToType = type.getMergeTo();
            if (!ARRAY_ENTRIES_AFTER_MERGE_TO__TEST.containsKey(mergeToType)) {
                ARRAY_ENTRIES_AFTER_MERGE_TO__TEST.put(mergeToType, indexMergeTo);
                indexMergeTo++;
            }
        }
    }
    
    // ArrayEntryTriggerHashMap
    // FIRST
    //      ML-relevant and one hot
    // SECOND
    //      ML-relevant and NOT one hot
    //
    // HashMap key   == VaultEntryType
    // HashMap value == position in the info array (array used in BucketEntry)
    
    /**
     * This HashMap contains all the VaultEntryTypes and an int number for the 
     * position in the resulting array created to match the size of this HashMap.
     */
    public static final HashMap<VaultEntryType, Integer> ARRAY_ENTRY_TRIGGER_HASHMAP;

    static {
        ARRAY_ENTRY_TRIGGER_HASHMAP = new HashMap<>();
        // ML-relevant and one hot
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

        // ML-relevant but NOT one hot
        ARRAY_ENTRY_TRIGGER_HASHMAP.put(VaultEntryType.BASAL_INTERPRETER, 25);
        ARRAY_ENTRY_TRIGGER_HASHMAP.put(VaultEntryType.BASAL_MANUAL, 26);
        ARRAY_ENTRY_TRIGGER_HASHMAP.put(VaultEntryType.BASAL_PROFILE, 27);
        ARRAY_ENTRY_TRIGGER_HASHMAP.put(VaultEntryType.BOLUS_NORMAL, 28);
        ARRAY_ENTRY_TRIGGER_HASHMAP.put(VaultEntryType.BOLUS_SQARE, 29);
        ARRAY_ENTRY_TRIGGER_HASHMAP.put(VaultEntryType.GLUCOSE_CGM, 30);
        ARRAY_ENTRY_TRIGGER_HASHMAP.put(VaultEntryType.MEAL_BOLUS_CALCULATOR, 31);
        ARRAY_ENTRY_TRIGGER_HASHMAP.put(VaultEntryType.MEAL_MANUAL, 32);
        ARRAY_ENTRY_TRIGGER_HASHMAP.put(VaultEntryType.PUMP_UNSUSPEND, 33);
        ARRAY_ENTRY_TRIGGER_HASHMAP.put(VaultEntryType.CGM_TIME_SYNC, 34);
        ARRAY_ENTRY_TRIGGER_HASHMAP.put(VaultEntryType.DM_INSULIN_SENSITIVTY, 35);
        ARRAY_ENTRY_TRIGGER_HASHMAP.put(VaultEntryType.GLUCOSE_BG, 36);
        ARRAY_ENTRY_TRIGGER_HASHMAP.put(VaultEntryType.GLUCOSE_BG_MANUAL, 37);
        ARRAY_ENTRY_TRIGGER_HASHMAP.put(VaultEntryType.GLUCOSE_BOLUS_CALCULATION, 38);
        ARRAY_ENTRY_TRIGGER_HASHMAP.put(VaultEntryType.GLUCOSE_CGM_CALIBRATION, 39);
        ARRAY_ENTRY_TRIGGER_HASHMAP.put(VaultEntryType.HEART_RATE, 40);
        ARRAY_ENTRY_TRIGGER_HASHMAP.put(VaultEntryType.HEART_RATE_VARIABILITY, 41);
        ARRAY_ENTRY_TRIGGER_HASHMAP.put(VaultEntryType.PUMP_PRIME, 42);
        ARRAY_ENTRY_TRIGGER_HASHMAP.put(VaultEntryType.PUMP_TIME_SYNC, 43);
    }

    // ArrayEntrysAfterMergeTo
    // FIRST
    //      ML-relevant and one hot
    // SECOND
    //      ML-relevant and NOT one hot
    //
    // HashMap key   == VaultEntryType
    // HashMap value == position in the info array (array used in BucketEntry)
    
    /**
     * This HashMap contains all the merge-to VaultEntryTypes and an int number 
     * for the position in the resulting array created to match the size of this
     * HashMap.
     */
    public static final HashMap<VaultEntryType, Integer> ARRAY_ENTRIES_AFTER_MERGE_TO;

    static {
        ARRAY_ENTRIES_AFTER_MERGE_TO = new HashMap<>();
        // ML-relevant and one hot
        ARRAY_ENTRIES_AFTER_MERGE_TO.put(VaultEntryType.GLUCOSE_CGM_ALERT, 0);
        ARRAY_ENTRIES_AFTER_MERGE_TO.put(VaultEntryType.CGM_SENSOR_FINISHED, 1);
        ARRAY_ENTRIES_AFTER_MERGE_TO.put(VaultEntryType.CGM_SENSOR_START, 2);
        ARRAY_ENTRIES_AFTER_MERGE_TO.put(VaultEntryType.CGM_CONNECTION_ERROR, 3);
        ARRAY_ENTRIES_AFTER_MERGE_TO.put(VaultEntryType.CGM_CALIBRATION_ERROR, 4);
        ARRAY_ENTRIES_AFTER_MERGE_TO.put(VaultEntryType.PUMP_FILL, 5); // merged
        ARRAY_ENTRIES_AFTER_MERGE_TO.put(VaultEntryType.PUMP_NO_DELIVERY, 6);
        ARRAY_ENTRIES_AFTER_MERGE_TO.put(VaultEntryType.PUMP_RESERVOIR_EMPTY, 7);
        ARRAY_ENTRIES_AFTER_MERGE_TO.put(VaultEntryType.PUMP_SUSPEND, 8); // merged
        ARRAY_ENTRIES_AFTER_MERGE_TO.put(VaultEntryType.SLEEP_LIGHT, 9); // merged
        ARRAY_ENTRIES_AFTER_MERGE_TO.put(VaultEntryType.LOC_TRANSISTION, 10);
        ARRAY_ENTRIES_AFTER_MERGE_TO.put(VaultEntryType.LOC_HOME, 11);
        ARRAY_ENTRIES_AFTER_MERGE_TO.put(VaultEntryType.LOC_WORK, 12);
        ARRAY_ENTRIES_AFTER_MERGE_TO.put(VaultEntryType.LOC_FOOD, 13);
        ARRAY_ENTRIES_AFTER_MERGE_TO.put(VaultEntryType.LOC_SPORTS, 14);
        ARRAY_ENTRIES_AFTER_MERGE_TO.put(VaultEntryType.EXERCISE_OTHER, 15);
        ARRAY_ENTRIES_AFTER_MERGE_TO.put(VaultEntryType.EXERCISE_WALK, 16);
        ARRAY_ENTRIES_AFTER_MERGE_TO.put(VaultEntryType.EXERCISE_BICYCLE, 17);
        ARRAY_ENTRIES_AFTER_MERGE_TO.put(VaultEntryType.EXERCISE_RUN, 18);
        ARRAY_ENTRIES_AFTER_MERGE_TO.put(VaultEntryType.EXERCISE_MANUAL, 19);
        ARRAY_ENTRIES_AFTER_MERGE_TO.put(VaultEntryType.PUMP_REWIND, 20);

        // ML-relevant but NOT one hot
        ARRAY_ENTRIES_AFTER_MERGE_TO.put(VaultEntryType.BASAL_PROFILE, 21); // merged
        ARRAY_ENTRIES_AFTER_MERGE_TO.put(VaultEntryType.BOLUS_NORMAL, 22); // merged
        ARRAY_ENTRIES_AFTER_MERGE_TO.put(VaultEntryType.GLUCOSE_CGM, 23);
        ARRAY_ENTRIES_AFTER_MERGE_TO.put(VaultEntryType.MEAL_BOLUS_CALCULATOR, 24); // merged
        ARRAY_ENTRIES_AFTER_MERGE_TO.put(VaultEntryType.PUMP_UNSUSPEND, 25);
        ARRAY_ENTRIES_AFTER_MERGE_TO.put(VaultEntryType.CGM_TIME_SYNC, 26);
        ARRAY_ENTRIES_AFTER_MERGE_TO.put(VaultEntryType.DM_INSULIN_SENSITIVTY, 27);
        ARRAY_ENTRIES_AFTER_MERGE_TO.put(VaultEntryType.GLUCOSE_BG, 28);
        ARRAY_ENTRIES_AFTER_MERGE_TO.put(VaultEntryType.GLUCOSE_BG_MANUAL, 29);
        ARRAY_ENTRIES_AFTER_MERGE_TO.put(VaultEntryType.GLUCOSE_BOLUS_CALCULATION, 30);
        ARRAY_ENTRIES_AFTER_MERGE_TO.put(VaultEntryType.GLUCOSE_CGM_CALIBRATION, 31);
        ARRAY_ENTRIES_AFTER_MERGE_TO.put(VaultEntryType.HEART_RATE, 32);
        ARRAY_ENTRIES_AFTER_MERGE_TO.put(VaultEntryType.HEART_RATE_VARIABILITY, 33);
        ARRAY_ENTRIES_AFTER_MERGE_TO.put(VaultEntryType.PUMP_PRIME, 34);
        ARRAY_ENTRIES_AFTER_MERGE_TO.put(VaultEntryType.PUMP_TIME_SYNC, 35);
    }

    // ======================================
    // single hashmaps for ML-rev and one hot
    // ======================================
    // triggerEventActTimeGiven (act time given as a value in VaultEntry)
    
    /**
     * This HashSet contains all VaultEntryTypes that are onehot and ML-relevant
     * and have a given act time which is inside the VaultEntry as a Double.
     */
    public static final HashSet<VaultEntryType> TRIGGER_EVENT_ACT_TIME_GIVEN;

    static {
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
    //
    // HashMap key   == VaultEntryType
    // HashMap value == VaultEntryType till which the key VaultEntryType is to be set to 1
    
    /**
     * This HashMap contains all VaultEntryTypes that are onehot and ML-relevant
     * and have an act time till the occurrence of a specific VaultEntryType. 
     * The first VaultEntryType is the VaultEntryType with an act time. The 
     * second VaultEntryType is the VaultEntryType that ends the act time of the
     * first VaultEntryType. 
     * 
     * The VaultEntrytype that ends the act time will not change throughout the 
     * run time of this program and thus is hardcoded here.
     */
    public static final HashMap<VaultEntryType, VaultEntryType> TRIGGER_EVENT_ACT_TIME_TILL_NEXT_EVENT;

    static {
        TRIGGER_EVENT_ACT_TIME_TILL_NEXT_EVENT = new HashMap<>();
        TRIGGER_EVENT_ACT_TIME_TILL_NEXT_EVENT.put(VaultEntryType.CGM_CONNECTION_ERROR, VaultEntryType.GLUCOSE_CGM);
        TRIGGER_EVENT_ACT_TIME_TILL_NEXT_EVENT.put(VaultEntryType.CGM_CALIBRATION_ERROR, VaultEntryType.GLUCOSE_CGM_CALIBRATION);
        TRIGGER_EVENT_ACT_TIME_TILL_NEXT_EVENT.put(VaultEntryType.PUMP_RESERVOIR_EMPTY, VaultEntryType.PUMP_REWIND);
        TRIGGER_EVENT_ACT_TIME_TILL_NEXT_EVENT.put(VaultEntryType.PUMP_AUTONOMOUS_SUSPEND, VaultEntryType.PUMP_UNSUSPEND);
        TRIGGER_EVENT_ACT_TIME_TILL_NEXT_EVENT.put(VaultEntryType.PUMP_REWIND, VaultEntryType.PUMP_PRIME);
        TRIGGER_EVENT_ACT_TIME_TILL_NEXT_EVENT.put(VaultEntryType.PUMP_SUSPEND, VaultEntryType.PUMP_UNSUSPEND);

    }

    // triggerEventActTimeOne (Alert for 1 frame)
    
    /**
     * This HashSet contains all VaultEntryTypes that are onehot and ML-relevant
     * and have an act time of only one minute (one BucketEntry).
     */
    public static final HashSet<VaultEntryType> TRIGGER_EVENT_ACT_TIME_ONE;

    static {
        TRIGGER_EVENT_ACT_TIME_ONE = new HashSet<>();
        TRIGGER_EVENT_ACT_TIME_ONE.add(VaultEntryType.GLUCOSE_CGM_ALERT);
        TRIGGER_EVENT_ACT_TIME_ONE.add(VaultEntryType.CGM_SENSOR_FINISHED);
        TRIGGER_EVENT_ACT_TIME_ONE.add(VaultEntryType.CGM_SENSOR_START);
        TRIGGER_EVENT_ACT_TIME_ONE.add(VaultEntryType.PUMP_FILL);
        TRIGGER_EVENT_ACT_TIME_ONE.add(VaultEntryType.PUMP_FILL_INTERPRETER);
    }

    // triggerEventsNotYetSet
    
    /**
     * This HashSet contains all VaultEntryTypes that are onehot and ML-relevant
     * and to this point don't havea given or set act time.
     */
    public static final HashSet<VaultEntryType> TRIGGER_EVENTS_NOT_YET_SET;

    static {
        TRIGGER_EVENTS_NOT_YET_SET = new HashSet<>();
        TRIGGER_EVENTS_NOT_YET_SET.add(VaultEntryType.PUMP_NO_DELIVERY);
    }

    // ==========================================
    // single hashmaps for ML-rev and NOT one hot
    // ==========================================
    // triggerEventNotOneHotActTimeSet (act time (in minutes) is set to THIS time, value is found in the VaultEntry)
    
    /**
     * This HashSet contains all VaultEntryTypes that are not onehot but are 
     * ML-relevant and have a set act time.
     * 
     * Since the act time does not change throughout the run time of this 
     * program it is hardcoded here.
     */
    public static final HashMap<VaultEntryType, Integer> TRIGGER_EVENT_NOT_ONE_HOT_ACT_TIME_SET;

    static {
        TRIGGER_EVENT_NOT_ONE_HOT_ACT_TIME_SET = new HashMap<>();
        // injected through a pump ... can be reset ... no parallel act times
        TRIGGER_EVENT_NOT_ONE_HOT_ACT_TIME_SET.put(VaultEntryType.BASAL_PROFILE, 60);
        // moved to TRIGGER_EVENT_NOT_ONE_HOT_ACT_TIME_ONE
        // TRIGGER_EVENT_NOT_ONE_HOT_ACT_TIME_SET.put(VaultEntryType.BOLUS_NORMAL, 1);
    }

    // triggerEventNotOneHotActTimeGiven (act time given as a value in VaultEntry as value2)
    
    /**
     * This HashSet contains all VaultEntryTypes that are not onehot but are 
     * ML-relevant and have a set act time.
     * 
     * Since the act time does not change throughout the run time of this 
     * program it is hard coded here.
     */
    public static final HashSet<VaultEntryType> TRIGGER_EVENT_NOT_ONE_HOT_ACT_TIME_GIVEN;

    static {
        TRIGGER_EVENT_NOT_ONE_HOT_ACT_TIME_GIVEN = new HashSet<>();
        TRIGGER_EVENT_NOT_ONE_HOT_ACT_TIME_GIVEN.add(VaultEntryType.BOLUS_SQARE);
    }

    // triggerEventNotOneHotActTimeTillNextEvent
    
    /**
     * This HashSet contains all VaultEntryTypes that are not onehot but are 
     * ML-relevant and have an act time till the occurrence of the next 
     * same VaultEntryType.
     */
    public static final HashSet<VaultEntryType> TRIGGER_EVENT_NOT_ONE_HOT_ACT_TIME_TILL_NEXT_EVENT;

    static {
        TRIGGER_EVENT_NOT_ONE_HOT_ACT_TIME_TILL_NEXT_EVENT = new HashSet<>();
        TRIGGER_EVENT_NOT_ONE_HOT_ACT_TIME_TILL_NEXT_EVENT.add(VaultEntryType.BASAL_INTERPRETER);
        TRIGGER_EVENT_NOT_ONE_HOT_ACT_TIME_TILL_NEXT_EVENT.add(VaultEntryType.BASAL_MANUAL);
    }

    // triggerEventNotOneHotActTimeOne (value only displayed for 1 frame)
    
    /**
     * This HashSet contains all VaultEntryTypes that are not onehot but are 
     * ML-relevant and have an act time of only one minute (one BucketEntry).
     */
    public static final HashSet<VaultEntryType> TRIGGER_EVENT_NOT_ONE_HOT_ACT_TIME_ONE;

    static {
        TRIGGER_EVENT_NOT_ONE_HOT_ACT_TIME_ONE = new HashSet<>();
        TRIGGER_EVENT_NOT_ONE_HOT_ACT_TIME_ONE.add(VaultEntryType.BOLUS_NORMAL);
        TRIGGER_EVENT_NOT_ONE_HOT_ACT_TIME_ONE.add(VaultEntryType.GLUCOSE_CGM);
        TRIGGER_EVENT_NOT_ONE_HOT_ACT_TIME_ONE.add(VaultEntryType.MEAL_BOLUS_CALCULATOR);
        TRIGGER_EVENT_NOT_ONE_HOT_ACT_TIME_ONE.add(VaultEntryType.MEAL_MANUAL);
        TRIGGER_EVENT_NOT_ONE_HOT_ACT_TIME_ONE.add(VaultEntryType.PUMP_UNSUSPEND);
        TRIGGER_EVENT_NOT_ONE_HOT_ACT_TIME_ONE.add(VaultEntryType.DM_INSULIN_SENSITIVTY);
        TRIGGER_EVENT_NOT_ONE_HOT_ACT_TIME_ONE.add(VaultEntryType.GLUCOSE_BG);
        TRIGGER_EVENT_NOT_ONE_HOT_ACT_TIME_ONE.add(VaultEntryType.GLUCOSE_BG_MANUAL);
        TRIGGER_EVENT_NOT_ONE_HOT_ACT_TIME_ONE.add(VaultEntryType.GLUCOSE_BOLUS_CALCULATION);
        TRIGGER_EVENT_NOT_ONE_HOT_ACT_TIME_ONE.add(VaultEntryType.GLUCOSE_CGM_CALIBRATION);
        TRIGGER_EVENT_NOT_ONE_HOT_ACT_TIME_ONE.add(VaultEntryType.HEART_RATE);
        TRIGGER_EVENT_NOT_ONE_HOT_ACT_TIME_ONE.add(VaultEntryType.HEART_RATE_VARIABILITY);
        TRIGGER_EVENT_NOT_ONE_HOT_ACT_TIME_ONE.add(VaultEntryType.PUMP_PRIME);
    }

    // triggerEventNotOneHotVauleIsATimestamp ??? TODO the given value might be a timestamp
    
    /**
     * This HashSet contains all VaultEntryTypes that are not onehot but are 
     * ML-relevant and have a timestamp as a value.
     */
    public static final HashSet<VaultEntryType> TRIGGER_EVENT_NOT_ONE_HOT_VALUE_IS_A_TIMESTAMP;

    static {
        TRIGGER_EVENT_NOT_ONE_HOT_VALUE_IS_A_TIMESTAMP = new HashSet<>();
        TRIGGER_EVENT_NOT_ONE_HOT_VALUE_IS_A_TIMESTAMP.add(VaultEntryType.CGM_TIME_SYNC);
        TRIGGER_EVENT_NOT_ONE_HOT_VALUE_IS_A_TIMESTAMP.add(VaultEntryType.PUMP_TIME_SYNC);
    }

    //
    // Hashsets average or sum calculation
    //
    // !!!
    // IF NEW HASHSETS ARE ADDED PLEASE ALSO ADD THE NEW HASHSETS INTO THE HASHSETS_TO_SUM_UP HASHSET BELOW !!!
    // !!!
    // basalHashset
    
    /**
     * This HashSet contains all BASAL_* VaultEntryTypes for later computation.
     */
    public static final HashSet<VaultEntryType> BASAL_PROFILE_HASHSET;

    static {
        BASAL_PROFILE_HASHSET = new HashSet<>();
        BASAL_PROFILE_HASHSET.add(VaultEntryType.BASAL_INTERPRETER);
        BASAL_PROFILE_HASHSET.add(VaultEntryType.BASAL_MANUAL);
        BASAL_PROFILE_HASHSET.add(VaultEntryType.BASAL_PROFILE);
    }

    // bolusHashset
    
    /**
     * This HashSet contains all BOLUS_* VaultEntryTypes for later computation.
     */
    public static final HashSet<VaultEntryType> BOLUS_HASHSET;

    static {
        BOLUS_HASHSET = new HashSet<>();
        BOLUS_HASHSET.add(VaultEntryType.BOLUS_NORMAL);
        BOLUS_HASHSET.add(VaultEntryType.BOLUS_SQARE);
    }

    // mealHashset
    
    /**
     * This HashSet contains all MEAL_* VaultEntryTypes for later computation.
     */
    public static final HashSet<VaultEntryType> MEAL_BOLUS_CALCULATOR_HASHSET;

    static {
        MEAL_BOLUS_CALCULATOR_HASHSET = new HashSet<>();
        MEAL_BOLUS_CALCULATOR_HASHSET.add(VaultEntryType.MEAL_BOLUS_CALCULATOR);
        MEAL_BOLUS_CALCULATOR_HASHSET.add(VaultEntryType.MEAL_MANUAL);
    }

    /**
     * This HashSet contains all VaultEntryTypes that are to be sumed up.
     */
    public static final HashSet<VaultEntryType> HASHSETS_TO_SUM_UP;

    static {
        HASHSETS_TO_SUM_UP = new HashSet<>();
        // !!!
        // NEW HASHSETS ARE ADDED HERE !!!
        // !!!
        HASHSETS_TO_SUM_UP.addAll(BASAL_PROFILE_HASHSET);
        HASHSETS_TO_SUM_UP.addAll(BOLUS_HASHSET);
        HASHSETS_TO_SUM_UP.addAll(MEAL_BOLUS_CALCULATOR_HASHSET);
    }

    // hashset für lineare interpolation (avg)
    
    /**
     * This HashSet contains all VaultEntryTypes that are to be interpolated.
     */
    public static final HashSet<VaultEntryType> HASHSET_FOR_LINEAR_INTERPOLATION;

    static {
        HASHSET_FOR_LINEAR_INTERPOLATION = new HashSet<>();
        HASHSET_FOR_LINEAR_INTERPOLATION.add(VaultEntryType.GLUCOSE_CGM);
        HASHSET_FOR_LINEAR_INTERPOLATION.add(VaultEntryType.HEART_RATE);
    }
}
