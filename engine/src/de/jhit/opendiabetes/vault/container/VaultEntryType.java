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
 * @author mswin, tiweGH, a.a.aponte
 */
public enum VaultEntryType {

    // Empty Type for BucketEntry
    EMPTY,
    // Bolus
    BOLUS_NORMAL(VaultEntryTypeGroup.BOLUS, false, true),
    BOLUS_SQARE(VaultEntryTypeGroup.BOLUS, false, true, BOLUS_NORMAL),
    // Basal
    BASAL_PROFILE(VaultEntryTypeGroup.BASAL, false, true),
    BASAL_MANUAL(VaultEntryTypeGroup.BASAL, false, true, BASAL_PROFILE),
    BASAL_INTERPRETER(VaultEntryTypeGroup.BASAL, false, true, BASAL_PROFILE),
    // Exercise
    EXERCISE_MANUAL(VaultEntryTypeGroup.EXERCISE, true, true),
    EXERCISE_OTHER(VaultEntryTypeGroup.EXERCISE, true, true),
    EXERCISE_WALK(VaultEntryTypeGroup.EXERCISE, true, true),
    EXERCISE_BICYCLE(VaultEntryTypeGroup.EXERCISE, true, true),
    EXERCISE_RUN(VaultEntryTypeGroup.EXERCISE, true, true),
    // Glucose
    GLUCOSE_CGM(VaultEntryTypeGroup.GLUCOSE, false, true),
    GLUCOSE_CGM_RAW(VaultEntryTypeGroup.GLUCOSE),
    GLUCOSE_CGM_ALERT(VaultEntryTypeGroup.GLUCOSE, true, VaultEntryType.MAYBE),
    GLUCOSE_CGM_CALIBRATION(VaultEntryTypeGroup.GLUCOSE, false, VaultEntryType.MAYBE),
    GLUCOSE_BG(VaultEntryTypeGroup.GLUCOSE, false, VaultEntryType.MAYBE),
    GLUCOSE_BG_MANUAL(VaultEntryTypeGroup.GLUCOSE, false, VaultEntryType.MAYBE),
    GLUCOSE_BOLUS_CALCULATION(VaultEntryTypeGroup.GLUCOSE, false, VaultEntryType.MAYBE),
    GLUCOSE_ELEVATION_30(VaultEntryTypeGroup.GLUCOSE),
    // CGM system
    CGM_SENSOR_FINISHED(VaultEntryTypeGroup.CGM_SYSTEM, true, VaultEntryType.MAYBE),
    CGM_SENSOR_START(VaultEntryTypeGroup.CGM_SYSTEM, true, VaultEntryType.MAYBE),
    CGM_CONNECTION_ERROR(VaultEntryTypeGroup.CGM_SYSTEM, true, VaultEntryType.MAYBE),
    CGM_CALIBRATION_ERROR(VaultEntryTypeGroup.CGM_SYSTEM, true, true),
    CGM_TIME_SYNC(VaultEntryTypeGroup.CGM_SYSTEM, false, VaultEntryType.MAYBE),
    // Meal
    MEAL_BOLUS_CALCULATOR(VaultEntryTypeGroup.MEAL, false, true),
    MEAL_MANUAL(VaultEntryTypeGroup.MEAL, false, true, MEAL_BOLUS_CALCULATOR),
    // Pump Events
    PUMP_REWIND(VaultEntryTypeGroup.PUMP_SYSTEM, true, VaultEntryType.MAYBE),
    PUMP_PRIME(VaultEntryTypeGroup.PUMP_SYSTEM, false, VaultEntryType.MAYBE),
    PUMP_FILL(VaultEntryTypeGroup.PUMP_SYSTEM, true, VaultEntryType.MAYBE),
    PUMP_FILL_INTERPRETER(VaultEntryTypeGroup.PUMP_SYSTEM, true, VaultEntryType.MAYBE, PUMP_FILL),
    PUMP_NO_DELIVERY(VaultEntryTypeGroup.PUMP_SYSTEM, true, true),
    PUMP_SUSPEND(VaultEntryTypeGroup.PUMP_SYSTEM, true, true),
    PUMP_UNSUSPEND(VaultEntryTypeGroup.PUMP_SYSTEM, false, true),
    PUMP_UNTRACKED_ERROR(VaultEntryTypeGroup.PUMP_SYSTEM),
    PUMP_RESERVOIR_EMPTY(VaultEntryTypeGroup.PUMP_SYSTEM, true, VaultEntryType.MAYBE),
    PUMP_TIME_SYNC(VaultEntryTypeGroup.PUMP_SYSTEM, false, VaultEntryType.MAYBE),
    PUMP_AUTONOMOUS_SUSPEND(VaultEntryTypeGroup.PUMP_SYSTEM, true, true, PUMP_SUSPEND),
    PUMP_CGM_PREDICTION(VaultEntryTypeGroup.PUMP_SYSTEM),
    // Sleep
    SLEEP_LIGHT(VaultEntryTypeGroup.SLEEP, true, true),
    SLEEP_REM(VaultEntryTypeGroup.SLEEP, true, true, SLEEP_LIGHT),
    SLEEP_DEEP(VaultEntryTypeGroup.SLEEP, true, true, SLEEP_LIGHT),
    // Heart
    HEART_RATE(VaultEntryTypeGroup.HEART, false, VaultEntryType.MAYBE),
    HEART_RATE_VARIABILITY(VaultEntryTypeGroup.HEART, false, VaultEntryType.MAYBE),
    STRESS(VaultEntryTypeGroup.HEART),
    // Location (VaultEntryTypeGroup.Geocoding)
    LOC_TRANSISTION(VaultEntryTypeGroup.LOCATION, true, true),
    LOC_HOME(VaultEntryTypeGroup.LOCATION, true, true),
    LOC_WORK(VaultEntryTypeGroup.LOCATION, true, true),
    LOC_FOOD(VaultEntryTypeGroup.LOCATION, true, true),
    LOC_SPORTS(VaultEntryTypeGroup.LOCATION, true, true),
    LOC_OTHER(VaultEntryTypeGroup.LOCATION),
    // Machine Learning
    ML_CGM_PREDICTION(VaultEntryTypeGroup.MACHINE_LEARNING),
    // Date Mining
    DM_INSULIN_SENSITIVTY(VaultEntryTypeGroup.DATA_MINING, false, VaultEntryType.MAYBE),
    // More unspecific input
    OTHER_ANNOTATION,
    CLUSTER_MEAL,
    CLUSTER_GLUCOSE_CGM;

    //current handling of MAYBE being true
    private final static boolean MAYBE = true;

    private final boolean ISONEHOT;
    private final boolean ISMLRELEVANT;
    private final VaultEntryType MERGETYPE;
    private final VaultEntryTypeGroup GROUP;

    /**
     *
     * @param isOneHot
     * @param isMLrelevant information about the type's ML relevance
     * @param mergeType
     */
    VaultEntryType(VaultEntryTypeGroup group, boolean isOneHot, boolean isMLrelevant, VaultEntryType mergeType) {
        this.ISONEHOT = isOneHot;
        this.ISMLRELEVANT = isMLrelevant;
        this.MERGETYPE = mergeType;
        this.GROUP = group;
    }

    VaultEntryType(VaultEntryTypeGroup group, boolean isOneHot, boolean isMLrelevant) {
        this.ISONEHOT = isOneHot;
        this.ISMLRELEVANT = isMLrelevant;
        this.MERGETYPE = this;
        this.GROUP = group;
    }

    VaultEntryType(VaultEntryTypeGroup group) {
        this.ISONEHOT = false;
        this.ISMLRELEVANT = false;
        this.MERGETYPE = this;
        this.GROUP = group;
    }

    VaultEntryType() {
        this.ISONEHOT = false;
        this.ISMLRELEVANT = false;
        this.MERGETYPE = this;
        this.GROUP = VaultEntryTypeGroup.EMPTY;
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
    public VaultEntryTypeGroup getGroup() {
        return GROUP;
    }

}
