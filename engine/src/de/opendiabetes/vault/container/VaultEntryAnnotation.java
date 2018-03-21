/**
 * Copyright (C) 2017 OpenDiabetes
 * <p>
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 * <p>
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * <p>
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package de.opendiabetes.vault.container;

import java.io.Serializable;
import java.util.Objects;
import java.util.regex.Pattern;

/**
 * This class models annotations of vault entries.
 */
public class VaultEntryAnnotation implements Serializable {

    /**
     * The pattern of the VaultEntryAnnotation's value.
     */
    private final Pattern valuePattern;
    /**
     * The type of the VaultEntryAnnotation.
     */
    private TYPE type;
    /**
     * The value of the VaultEntryAnnotation.
     */
    private String value = "";

    /**
     * The no-argument constructor of VaultEntryAnnotation, setting a default annotation type.
     */
    public VaultEntryAnnotation() {
        this("", TYPE.EXERCISE_AUTOMATIC_OTHER);
    }

    /**
     * A constructor of VaultEntryAnnotation, setting the annotation type.
     *
     * @param type The parameter that type will be set to.
     */
    public VaultEntryAnnotation(final TYPE type) {
        this("", type);
    }

    /**
     * A constructor of VaultEntryAnnotation, setting the annotation type and value.
     *
     * @param value The parameter that value will be set to.
     * @param type  The parameter that type will be set to.
     */
    public VaultEntryAnnotation(final String value, final TYPE type) {
        this.value = value;
        this.type = type;
        valuePattern = Pattern.compile(".*" + type.toString() + "(=([\\w\\.]+))?.");
    }

    /**
     * Getter for type.
     *
     * @return The type of the VaultEntryAnnotation.
     */
    public TYPE getType() {
        return type;
    }

    /**
     * Getter for value.
     *
     * @return The value of the VaultEntryAnnotation.
     */
    public String getValue() {
        return value;
    }

    /**
     * Sets the VaultEntryAnnotation's value.
     *
     * @param value The parameter that value will be set to.
     * @return The same VaultEntryAnnotation with the modified value.
     */
    public VaultEntryAnnotation setValue(final String value) {
        this.value = value;
        return this;
    }

    /**
     * Converting VaultEntries to string.
     *
     * @return the VaultEntry as string.
     */
    @Override
    public String toString() {
        return "VaultEntryAnnotation{"
                + "valuePattern=" + valuePattern
                + ", type=" + type
                + ", value='" + value + '\''
                + '}';
    }

    // TO DO reimplement with pattern matching, if needed to do
//    /**
//     *
//     * @param annotationString representing VaultEntryAnnotation as string
//     *
//     * @return VaultEntryAnnotation represented by the string or null if no (or
//     * more than one) VaultEntryAnnotation found
//     */
//    public static VaultEntryAnnotation fromString(String annotationString) {
//        VaultEntryAnnotation returnValue = null;
//        for (VaultEntryAnnotation item : VaultEntryAnnotation.values()) {
//            if (annotationString.toUpperCase().contains(item.toString().toUpperCase())) {
//                if (returnValue != null) {
//                    returnValue = item;
//                } else {
//                    // found more than one --> error
//                    return null;
//                }
//            }
//        }
//        return returnValue;
//    }

    /**
     * Converts VaultEntry to string with the value.
     * @return The VaultEntry as string.
     */
    public String toStringWithValue() {
        if (value.isEmpty()) {
            return this.getType().toString();
        } else {
            return this.getType().toString() + "=" + value;
        }
    }

    /**
     * Getter for a hash code.
     * @return The hash code.
     */
    @Override
    public int hashCode() {
        final int hash = 7;
        return hash;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean equals(final Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final VaultEntryAnnotation other = (VaultEntryAnnotation) obj;
        if (!Objects.equals(this.value, other.value)) {
            return false;
        }
        return this.type == other.type;
    }

    /**
     * This enum defines different types of vault entry annotations.
     */
    public enum TYPE {
        /**
         * Indicates lase time glucose level rose.
         */
        GLUCOSE_RISE_LAST,
        /**
         * Indicates glucose rising for 20 minutes.
         */
        GLUCOSE_RISE_20_MIN,
        /**
         * Serial blood glucose meter.
         */
        GLUCOSE_BG_METER_SERIAL,
        /**
         * Indicates walking exercise recorded by a tracker.
         */
        EXERCISE_TrackerWalk,
        /**
         * Indicates cycling exercise recorded by a tracker.
         */
        EXERCISE_TrackerBicycle,
        /**
         * Indicates running exercise recorded by a tracker.
         */
        EXERCISE_TrackerRun,
        /**
         * Indicates walking exercise recorded by google fit.
         */
        EXERCISE_GoogleWalk,
        /**
         * Indicates cycling exercise recorded by a google fit.
         */
        EXERCISE_GoogleBicycle,
        /**
         * Indicates running exercise recorded by a google fit.
         */
        EXERCISE_GoogleRun,
        /**
         * Indicates other exercise.
         */
        EXERCISE_AUTOMATIC_OTHER,
        /**
         * The average heart rate during the exercise.
         */
        AVERAGE_HEART_RATE,
        /**
         * The pump error code.
         */
        PUMP_ERROR_CODE,
        /**
         * The pump information code.
         */
        PUMP_INFORMATION_CODE,
        /**
         * Indicates medtronic data.
         */
        CGM_VENDOR_MEDTRONIC,
        /**
         * Indicates libre data.
         */
        CGM_VENDOR_LIBRE,
        /**
         * Indicates dexcom data.
         */
        CGM_VENDOR_DEXCOM,
        /**
         * The prediction time for the machine learner.
         */
        ML_PREDICTION_TIME_BUCKET_SIZE,
        /**
         * User text.
         */
        USER_TEXT,
        /**
         * Bolus correction.
         */
        BOLUS_CORRECTION,
        /**
         * Meal bolus.
         */
        BOLUS_MEAL,
        /**
         * Systolic blood pressure.
         */
        BLOOD_PRESSURE_Systolic,
        /**
         * Diastolic blood pressure.
         */
        BLOOD_PRESSURE_Diastolic,
        /**
         * Cosy exercise.
         */
        EXERCISE_cosy,
        /**
         * Ordinary exercise.
         */
        EXERCISE_ordinary,
        /**
         * Demanding exercise.
         */
        EXERCISE_demanding,
        /**
         * Meal information.
         */
        MEAL_Information,
        /**
         * BeforeTheMeal tag.
         */
        TAG_BeforeTheMeal,
        /**
         * Breakfast tag.
         */
        TAG_Breakfast,
        /**
         * Correction tag.
         */
        TAG_Correction,
        /**
         * Sports tag.
         */
        TAG_Sports,
        /**
         * Snack tag.
         */
        TAG_Snack,
        /**
         * Dinner tag.
         */
        TAG_Dinner,
        /**
         * Office work tag.
         */
        TAG_OfficeWork,
        /**
         * Lunch tag.
         */
        TAG_Lunch,
        /**
         * Bedtime tag.
         */
        TAG_Bedtime,
        /**
         * Hypo feeling tag.
         */
        TAG_HypoFeeling,
        /**
         * Sad tag.
         */
        TAG_Sad,
        /**
         * Sick tag.
         */
        TAG_Sick,
        /**
         * AfterTheMeal tag.
         */
        TAG_AfterTheMeal,
        /**
         * Hyper feeling tag.
         */
        TAG_HyperFeeling,
        /**
         * Party tag.
         */
        TAG_Party,
        /**
         * Bingeing tag.
         */
        TAG_Bingeing,
        /**
         * Alcohol tag.
         */
        TAG_Alcohol,
        /**
         * Nervous tag.
         */
        TAG_Nervous,
        /**
         * Strss tag.
         */
        TAG_Stress,
        /**
         * Note tag.
         */
        TAG_Note,
        /**
         * Unhandled Tags get this value.
         */
        TAG_Unknown
    }

}
