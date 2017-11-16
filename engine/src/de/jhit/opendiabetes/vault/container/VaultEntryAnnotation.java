/*
 * Copyright (C) 2017 juehv
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

import java.io.Serializable;
import java.util.Objects;
import java.util.regex.Pattern;

/**
 *
 * @author juehv
 */
public class VaultEntryAnnotation implements Serializable {

    public static enum TYPE {
        GLUCOSE_RISE_LAST,
        GLUCOSE_RISE_20_MIN,
        GLUCOSE_BG_METER_SERIAL,
        //
        EXERCISE_TrackerWalk,
        EXERCISE_TrackerBicycle,
        EXERCISE_TrackerRun,
        EXERCISE_GoogleWalk,
        EXERCISE_GoogleBicycle,
        EXERCISE_GoogleRun,
        EXERCISE_AUTOMATIC_OTHER,
        //
        PUMP_ERROR_CODE,
        PUMP_INFORMATION_CODE,
        //
        CGM_VENDOR_MEDTRONIC,
        CGM_VENDOR_LIBRE,
        CGM_VENDOR_DEXCOM,
        //
        ML_PREDICTION_TIME_BUCKET_SIZE,
        //
        USER_TEXT;
    }

    private final Pattern valuePattern;

    private TYPE type;

    private String value = "";

    public VaultEntryAnnotation() {
        this("", TYPE.EXERCISE_AUTOMATIC_OTHER);
    }

    public VaultEntryAnnotation(TYPE type) {
        this("", type);
    }

    public VaultEntryAnnotation(String value, TYPE type) {
        this.value = value;
        this.type = type;
        valuePattern = Pattern.compile(".*" + type.toString() + "(=([\\w\\.]+))?.");
    }

    public TYPE getType() {
        return type;
    }

    public String getValue() {
        return value;
    }

    public VaultEntryAnnotation setValue(String value) {
        this.value = value;
        return this;
    }

    // TODO reimplement with pattern matching
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
    @Override
    public String toString() {
        return type.toString();
    }

    public String toStringWithValue() {
        return value.isEmpty() ? this.toString() : this.toString() + "=" + value;
    }

    @Override
    public int hashCode() {
        int hash = 7;
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
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
        if (this.type != other.type) {
            return false;
        }
        return true;
    }

}
