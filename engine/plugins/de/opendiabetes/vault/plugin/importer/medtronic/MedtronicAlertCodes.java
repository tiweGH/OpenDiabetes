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
package de.opendiabetes.vault.plugin.importer.medtronic;

/**
 * This enum defines Medtronic alerts.
 */
public enum MedtronicAlertCodes {
    /**
     * Indicates an unknown alert.
     */
    UNKNOWN_ALERT(0),
    /**
     * Indicates no delivery.
     */
    NO_DELIVERY(4),
    /**
     * Indicates blocked insulin flow.
     */
    INSULIN_FLOW_BLOCKED(7),
    /**
     * Indicates empty battery.
     */
    BATTERY_EMPTY_30_MIN_LEFT(73),
    /**
     * Indicates suspended battery empty alert.
     */
    BATTERY_EMPTY_SUSPENDED(84),
    /**
     * Indicates no delivered bolus due to input threshold.
     */
    NO_BOLUS_DELIVERED_INPUT_THRESHOLD(100),
    /**
     * Indicates low pump battery.
     */
    PUMP_BATTERY_WEAK(104),
    /**
     * Indicates low reservoir stand.
     */
    RESERVOIR_EMPTY_SOON(105),
    /**
     * Indicates reminder to change catheter.
     */
    CHANGE_CATHETER_REMINDER(109),
    /**
     * Indicates empty reservoir.
     */
    EMPTY_RESERVOIR(113),
    /**
     * Indicates calibration.
     */
    CALIBRATE_NOW(775),
    /**
     * Indicates error while calibrating.
     */
    CALIBRATION_ERROR(776),
    /**
     * Indicates sensor should be changed.
     */
    CHANGE_SENSOR(778),
    /**
     * Indicates loss of connection to sensor.
     */
    NO_SENSOR_CONNECTION(780),
    /**
     * Indicates a rise.
     */
    RISE_ALERT(784),
    /**
     * Indicates expiration of sensor.
     */
    SENSOR_EXPIRED(794),
    /**
     * Indicates sensor alert.
     */
    SENSOR_ALERT_1(797),
    /**
     * Indicates end of sensor activity.
     */
    SENSOR_FINISHED(798),
    /**
     * Indicates start of sensor initialization.
     */
    SENSOR_INITIALIZATION_STARTED(799),
    /**
     * Indicates a low.
     */
    LOW(802),
    /**
     * Indicates a low when suspended.
     */
    LOW_WHEN_SUSPENDED(803),
    /**
     * Indicates unsuspeding after a low.
     */
    UNSUSPEND_AFTER_LOW_PROTECTION(806),
    /**
     * Indicates unsuspending after a low for a maximum timespan.
     */
    UNSUSPEND_AFTER_LOW_PROTECTION_MAX_TIMESPAN(808),
    /**
     * Indicates suspension on low.
     */
    SUSPEND_ON_LOW(809),
    /**
     * Indicates suspension before low.
     */
    SUSPEND_BEFORE_LOW(810),
    /**
     * Indicates a high.
     */
    HIGH(816),
    /**
     * Indicates approaching high.
     */
    APPROACHING_HIGH(817),
    /**
     * Indicates a reminder to calibrate sensor.
     */
    REMINDER_ON_SENSOR_CALIBRATION(869);

    /**
     * The numeric code for the alert.
     */
    private final int code;

    /**
     * Sets the numeric code.
     *
     * @param code The value that code will be set to.
     */
    MedtronicAlertCodes(final int code) {
        this.code = code;
    }

    /**
     * Finds the correct alert.
     *
     * @param code Code that specifies which alert to return.
     * @return Alert that corresponds to code (or UNKNOWN_ALERT).
     */
    public static MedtronicAlertCodes fromCode(final int code) {
        for (MedtronicAlertCodes codeObj : MedtronicAlertCodes.values()) {
            if (codeObj.getCode() == code) {
                return codeObj;
            }
        }
        return UNKNOWN_ALERT; // nothing found
    }

    /**
     * Getter for code.
     *
     * @return The numeric code of the alert
     */
    public int getCode() {
        return code;
    }

}
