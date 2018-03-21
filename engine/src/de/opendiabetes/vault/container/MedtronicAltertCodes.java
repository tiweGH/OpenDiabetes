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
package de.opendiabetes.vault.container;

/**
 *
 * @author juehv
 */
public enum MedtronicAltertCodes {
    UNKNOWN_ALERT(0),
    NO_DELIVERY(4),
    INSULIN_FLOW_BLOCKED(7),
    BATTERY_EMPTY_30_MIN_LEFT(73),
    BATTERY_EMPTY_SUSPENDED(84),
    NO_BOLUS_DELIVERED_INPUT_THRESHOULD(100),
    PUMP_BATTERY_WEAK(104),
    RESERVOIR_EMPTY_SOON(105),
    CHANGE_KATHETER_REMINDER(109),
    EMPTY_RESERVOIR(113),
    CALIBRATE_NOW(775),
    CALIBRATION_ERROR(776),
    CHANGE_SENSOR(778),
    NO_SENSOR_CONNECTION(780),
    RISE_ALERT(784),
    SENSOR_EXPIRED(794),
    SENSOR_ALERT_1(797),
    SENSOR_FINISHED(798),
    SENSOR_INITIALIZATIN_STARTED(799),
    LOW(802),
    LOW_WHEN_SUSPENDED(803),
    UNSUSPEND_AFTER_LOW_PROTECTION(806),
    UNSUSPEND_AFTER_LOW_PROTECTION_MAX_TIMESPAN(808),
    SUSPEND_ON_LOW(809),
    SUSPEND_BEVORE_LOW(810),
    HIGH(816),
    APPROACHING_HIGH(817),
    REMINDER_ON_SENSOR_CALIBRATION(869);

    private final int code;

    MedtronicAltertCodes(int code) {
        this.code = code;
    }

    public int getCode() {
        return code;
    }

    public static MedtronicAltertCodes fromCode(int code) {
        for (MedtronicAltertCodes codeObj : MedtronicAltertCodes.values()) {
            if (codeObj.getCode() == code) {
                return codeObj;
            }
        }
        return UNKNOWN_ALERT; // nothing found
    }

}
