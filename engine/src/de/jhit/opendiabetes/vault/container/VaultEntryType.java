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
 * @author mswin, tiweGH
 */
public enum VaultEntryType {

    //Merge Types
    BASAL,
    MEAL,
    SLEEP,
    // Bolus
    BOLUS_NORMAL(false, "true"),
    BOLUS_SQARE(false, "true"),
    // Basal
    BASAL_PROFILE(false, "true", BASAL),
    BASAL_MANUAL(false, "true", BASAL),
    BASAL_INTERPRETER(false, "true", BASAL),
    // Exercise
    EXERCISE_MANUAL(true, "true"),
    EXERCISE_OTHER(true, "true"),
    EXERCISE_WALK(true, "true"),
    EXERCISE_BICYCLE(true, "true"),
    EXERCISE_RUN(true, "true"),
    // Glucose
    GLUCOSE_CGM(false, "true"),
    GLUCOSE_CGM_RAW,
    GLUCOSE_CGM_ALERT(true, "maybe"),
    GLUCOSE_CGM_CALIBRATION(false, "maybe"),
    GLUCOSE_BG(false, "maybe"),
    GLUCOSE_BG_MANUAL(false, "maybe"),
    GLUCOSE_BOLUS_CALCULATION(false, "maybe"),
    GLUCOSE_ELEVATION_30,
    // CGM system
    CGM_SENSOR_FINISHED(true, "maybe"),
    CGM_SENSOR_START(true, "maybe"),
    CGM_CONNECTION_ERROR(true, "maybe"),
    CGM_CALIBRATION_ERROR(true, "true"),
    CGM_TIME_SYNC(false, "maybe"),
    // Meal
    MEAL_BOLUS_CALCULATOR(false, "true", MEAL),
    MEAL_MANUAL(false, "true", MEAL),
    // Pump Events
    PUMP_REWIND(true, "maybe"),
    PUMP_PRIME(false, "maybe"),
    PUMP_FILL(true, "maybe"),
    PUMP_FILL_INTERPRETER(true, "maybe", PUMP_FILL),
    PUMP_NO_DELIVERY(true, "true"),
    PUMP_SUSPEND(true, "true"),
    PUMP_UNSUSPEND(false, "true"),
    PUMP_UNTRACKED_ERROR,
    PUMP_RESERVOIR_EMPTY(true, "maybe"),
    PUMP_TIME_SYNC(false, "maybe"),
    PUMP_AUTONOMOUS_SUSPEND(true, "true", PUMP_SUSPEND),
    PUMP_CGM_PREDICTION,
    // Sleep
    SLEEP_LIGHT(true, "true", SLEEP),
    SLEEP_REM(true, "true", SLEEP),
    SLEEP_DEEP(true, "true", SLEEP),
    // Heart
    HEART_RATE(false, "maybe"),
    HEART_RATE_VARIABILITY(false, "maybe"),
    STRESS,
    // Location (Geocoding)
    LOC_TRANSISTION(true, "true"),
    LOC_HOME(true, "true"),
    LOC_WORK(true, "true"),
    LOC_FOOD(true, "true"),
    LOC_SPORTS(true, "true"),
    LOC_OTHER,
    // Machine Learning
    ML_CGM_PREDICTION,
    // Date Mining
    DM_INSULIN_SENSITIVTY(false, "maybe"),
    // More unspecific input
    OTHER_ANNOTATION;

    private final boolean ISONEHOT;
    private final boolean ISMLRELEVANT;
    private final VaultEntryType MERGETYPE;

    /**
     * Handles the possibility of ISMLRELEVANT being "maybe"
     *
     * @param isMLrelevant information about the type's ML relevance, currently
     * a String
     * @return true if isMLrelevant is "true" or "maybe"
     */
    private boolean setMLrelevance(String isMLrelevant) {
        boolean result;
        //current handling of "maybe" being true
        result = isMLrelevant.equalsIgnoreCase("maybe") || isMLrelevant.equalsIgnoreCase("true");
        return result;
    }

    VaultEntryType(boolean isOneHot, String isMLrelevant, VaultEntryType mergeType) {
        this.ISONEHOT = isOneHot;
        this.ISMLRELEVANT = setMLrelevance(isMLrelevant);
        this.MERGETYPE = mergeType;
    }

    VaultEntryType(boolean isOneHot, String isMLrelevant) {
        this.ISONEHOT = isOneHot;
        this.ISMLRELEVANT = setMLrelevance(isMLrelevant);
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
