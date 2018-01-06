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

/**
 *
 * @author mswin, tiweGH, aa80hifa
 */
public enum VaultEntryType {
    
    // Empty Type for BucketEntry
    EMPTY,

    //Merge Types
    BASAL,
    MEAL,
    SLEEP,
    // Bolus
    BOLUS_NORMAL(false, true),
    BOLUS_SQARE(false, true),
    // Basal
    BASAL_PROFILE(false, true, BASAL),
    BASAL_MANUAL(false, true, BASAL),
    BASAL_INTERPRETER(false, true, BASAL),
    // Exercise
    EXERCISE_MANUAL(true, true),
    EXERCISE_OTHER(true, true),
    EXERCISE_WALK(true, true),
    EXERCISE_BICYCLE(true, true),
    EXERCISE_RUN(true, true),
    // Glucose
    GLUCOSE_CGM(false, true),
    GLUCOSE_CGM_RAW,
    GLUCOSE_CGM_ALERT(true, VaultEntryType.MAYBE),
    GLUCOSE_CGM_CALIBRATION(false, VaultEntryType.MAYBE),
    GLUCOSE_BG(false, VaultEntryType.MAYBE),
    GLUCOSE_BG_MANUAL(false, VaultEntryType.MAYBE),
    GLUCOSE_BOLUS_CALCULATION(false, VaultEntryType.MAYBE),
    GLUCOSE_ELEVATION_30,
    // CGM system
    CGM_SENSOR_FINISHED(true, VaultEntryType.MAYBE),
    CGM_SENSOR_START(true, VaultEntryType.MAYBE),
    CGM_CONNECTION_ERROR(true, VaultEntryType.MAYBE),
    CGM_CALIBRATION_ERROR(true, true),
    CGM_TIME_SYNC(false, VaultEntryType.MAYBE),
    // Meal
    MEAL_BOLUS_CALCULATOR(false, true, MEAL),
    MEAL_MANUAL(false, true, MEAL),
    // Pump Events
    PUMP_REWIND(true, VaultEntryType.MAYBE),
    PUMP_PRIME(false, VaultEntryType.MAYBE),
    PUMP_FILL(true, VaultEntryType.MAYBE),
    PUMP_FILL_INTERPRETER(true, VaultEntryType.MAYBE, PUMP_FILL),
    PUMP_NO_DELIVERY(true, true),
    PUMP_SUSPEND(true, true),
    PUMP_UNSUSPEND(false, true),
    PUMP_UNTRACKED_ERROR,
    PUMP_RESERVOIR_EMPTY(true, VaultEntryType.MAYBE),
    PUMP_TIME_SYNC(false, VaultEntryType.MAYBE),
    PUMP_AUTONOMOUS_SUSPEND(true, true, PUMP_SUSPEND),
    PUMP_CGM_PREDICTION,
    // Sleep
    SLEEP_LIGHT(true, true, SLEEP),
    SLEEP_REM(true, true, SLEEP),
    SLEEP_DEEP(true, true, SLEEP),
    // Heart
    HEART_RATE(false, VaultEntryType.MAYBE),
    HEART_RATE_VARIABILITY(false, VaultEntryType.MAYBE),
    STRESS,
    // Location (Geocoding)
    LOC_TRANSISTION(true, true),
    LOC_HOME(true, true),
    LOC_WORK(true, true),
    LOC_FOOD(true, true),
    LOC_SPORTS(true, true),
    LOC_OTHER,
    // Machine Learning
    ML_CGM_PREDICTION,
    // Date Mining
    DM_INSULIN_SENSITIVTY(false, VaultEntryType.MAYBE),
    // More unspecific input
    OTHER_ANNOTATION;

    //current handling of MAYBE being true
    private final static boolean MAYBE = true;

    private final boolean ISONEHOT;
    private final boolean ISMLRELEVANT;
    private final VaultEntryType MERGETYPE;

    /**
     *
     * @param isOneHot
     * @param isMLrelevant information about the type's ML relevance
     * @param mergeType
     */
    VaultEntryType(boolean isOneHot, boolean isMLrelevant, VaultEntryType mergeType) {
        this.ISONEHOT = isOneHot;
        this.ISMLRELEVANT = isMLrelevant;
        this.MERGETYPE = mergeType;
    }

    VaultEntryType(boolean isOneHot, boolean isMLrelevant) {
        this.ISONEHOT = isOneHot;
        this.ISMLRELEVANT = isMLrelevant;
        this.MERGETYPE = this;
    }

    VaultEntryType() {
        this.ISONEHOT = false;
        this.ISMLRELEVANT = false;
        this.MERGETYPE = this;
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

}
