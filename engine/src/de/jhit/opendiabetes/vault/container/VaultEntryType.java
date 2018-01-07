/*
 * Copyright (C) 2017 Jens Heuschkel
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
import java.util.List;

/**
 *
 * @author mswin, tiweGH, aa80hifa
 */
public enum VaultEntryType {

    // Empty Type for BucketEntry
    EMPTY,
    //Merge Types and Groups
    CGM_SYSTEM,
    PUMP_SYSTEM,
    SLEEP,
    LOCATION,
    EXERCISE,
    GLUCOSE,
    MACHINE_LEARNING,
    DATA_MINING,
    BASAL,
    HEART,
    BOLUS,
    MEAL,
    // Bolus
    BOLUS_NORMAL(BOLUS, false, true),
    BOLUS_SQARE(BOLUS, false, true),
    // Basal
    BASAL_PROFILE(BASAL, false, true, BASAL),
    BASAL_MANUAL(BASAL, false, true, BASAL),
    BASAL_INTERPRETER(BASAL, false, true, BASAL),
    // Exercise
    EXERCISE_MANUAL(EXERCISE, true, true),
    EXERCISE_OTHER(EXERCISE, true, true),
    EXERCISE_WALK(EXERCISE, true, true),
    EXERCISE_BICYCLE(EXERCISE, true, true),
    EXERCISE_RUN(EXERCISE, true, true),
    // Glucose
    GLUCOSE_CGM(GLUCOSE, false, true),
    GLUCOSE_CGM_RAW(GLUCOSE),
    GLUCOSE_CGM_ALERT(GLUCOSE, true, VaultEntryType.MAYBE),
    GLUCOSE_CGM_CALIBRATION(GLUCOSE, false, VaultEntryType.MAYBE),
    GLUCOSE_BG(GLUCOSE, false, VaultEntryType.MAYBE),
    GLUCOSE_BG_MANUAL(GLUCOSE, false, VaultEntryType.MAYBE),
    GLUCOSE_BOLUS_CALCULATION(GLUCOSE, false, VaultEntryType.MAYBE),
    GLUCOSE_ELEVATION_30(GLUCOSE),
    // CGM system
    CGM_SENSOR_FINISHED(CGM_SYSTEM, true, VaultEntryType.MAYBE),
    CGM_SENSOR_START(CGM_SYSTEM, true, VaultEntryType.MAYBE),
    CGM_CONNECTION_ERROR(CGM_SYSTEM, true, VaultEntryType.MAYBE),
    CGM_CALIBRATION_ERROR(CGM_SYSTEM, true, true),
    CGM_TIME_SYNC(CGM_SYSTEM, false, VaultEntryType.MAYBE),
    // Meal
    MEAL_BOLUS_CALCULATOR(MEAL, false, true, MEAL),
    MEAL_MANUAL(MEAL, false, true, MEAL),
    // Pump Events
    PUMP_REWIND(PUMP_SYSTEM, true, VaultEntryType.MAYBE),
    PUMP_PRIME(PUMP_SYSTEM, false, VaultEntryType.MAYBE),
    PUMP_FILL(PUMP_SYSTEM, true, VaultEntryType.MAYBE),
    PUMP_FILL_INTERPRETER(PUMP_SYSTEM, true, VaultEntryType.MAYBE, PUMP_FILL),
    PUMP_NO_DELIVERY(PUMP_SYSTEM, true, true),
    PUMP_SUSPEND(PUMP_SYSTEM, true, true),
    PUMP_UNSUSPEND(PUMP_SYSTEM, false, true),
    PUMP_UNTRACKED_ERROR(PUMP_SYSTEM),
    PUMP_RESERVOIR_EMPTY(PUMP_SYSTEM, true, VaultEntryType.MAYBE),
    PUMP_TIME_SYNC(PUMP_SYSTEM, false, VaultEntryType.MAYBE),
    PUMP_AUTONOMOUS_SUSPEND(PUMP_SYSTEM, true, true, PUMP_SUSPEND),
    PUMP_CGM_PREDICTION(PUMP_SYSTEM),
    // Sleep
    SLEEP_LIGHT(SLEEP, true, true, SLEEP),
    SLEEP_REM(SLEEP, true, true, SLEEP),
    SLEEP_DEEP(SLEEP, true, true, SLEEP),
    // Heart
    HEART_RATE(HEART, false, VaultEntryType.MAYBE),
    HEART_RATE_VARIABILITY(HEART, false, VaultEntryType.MAYBE),
    STRESS(HEART),
    // Location (Geocoding)
    LOC_TRANSISTION(LOCATION, true, true),
    LOC_HOME(LOCATION, true, true),
    LOC_WORK(LOCATION, true, true),
    LOC_FOOD(LOCATION, true, true),
    LOC_SPORTS(LOCATION, true, true),
    LOC_OTHER(LOCATION),
    // Machine Learning
    ML_CGM_PREDICTION(MACHINE_LEARNING),
    // Date Mining
    DM_INSULIN_SENSITIVTY(DATA_MINING, false, VaultEntryType.MAYBE),
    // More unspecific input
    OTHER_ANNOTATION;

    //current handling of MAYBE being true
    private final static boolean MAYBE = true;

    private final boolean ISONEHOT;
    private final boolean ISMLRELEVANT;
    private final VaultEntryType MERGETYPE;
    private final VaultEntryType GROUP;

    /**
     *
     * @param isOneHot
     * @param isMLrelevant information about the type's ML relevance
     * @param mergeType
     */
    VaultEntryType(VaultEntryType group, boolean isOneHot, boolean isMLrelevant, VaultEntryType mergeType) {
        this.ISONEHOT = isOneHot;
        this.ISMLRELEVANT = isMLrelevant;
        this.MERGETYPE = mergeType;
        this.GROUP = group;
    }

    VaultEntryType(VaultEntryType group, boolean isOneHot, boolean isMLrelevant) {
        this.ISONEHOT = isOneHot;
        this.ISMLRELEVANT = isMLrelevant;
        this.MERGETYPE = this;
        this.GROUP = group;
    }

    VaultEntryType(VaultEntryType group) {
        this.ISONEHOT = false;
        this.ISMLRELEVANT = false;
        this.MERGETYPE = this;
        this.GROUP = group;
    }

    VaultEntryType() {
        this.ISONEHOT = false;
        this.ISMLRELEVANT = false;
        this.MERGETYPE = this;
        this.GROUP = this;
    }

    /**
     * Returns if type-specific values are one-hot encoded.
     *
     * @return true if type is one-hot
     */
    public boolean isOneHot() {
        return ISONEHOT;
    }

    /**
     * Returns if the EntryType is relevant for ML-processing
     *
     * @return True if ML-relevant
     */
    public boolean isMLrelevant() {
        return ISMLRELEVANT;
    }

    /**
     * If the type has to be merged to another <code>VaultEntryType</code>
     * during exporting, it will be returned, if not, the instance of the Enum
     * will be returned.
     *
     * @return the merge type
     */
    public VaultEntryType mergeTo() {
        return MERGETYPE;
    }

    /**
     * Returns the Group of the VaultEntryType
     *
     * @return the Group of the VaultEntryType
     */
    public VaultEntryType getGROUP() {
        return GROUP;
    }

    public static List<VaultEntryType> getTypesOfGroup(VaultEntryType group) {
        ArrayList<VaultEntryType> result = new ArrayList<>();
        for (VaultEntryType type : VaultEntryType.values()) {
            if (type.getGROUP() == group) {
                result.add(type);
            }
        }
        return result;
    }

}
