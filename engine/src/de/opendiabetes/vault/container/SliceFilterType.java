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

/**
 * This enum lists different slice filter types.
 */
public enum SliceFilterType {
    /**
     * Unknown filter type.
     */
    UNKNOWN,
    /**
     * Bolus type.
     */
    BOLUS,
    /**
     * Indicates no bolus influence.
     */
    NO_BOLUS_INFLUENCE,
    /**
     * Carbohydrates type.
     */
    CARB,
    /**
     * Indicates no carbohydrates influence.
     */
    NO_CARB_INFLUENCE,
    /**
     * Sports type.
     */
    SPORTS,
    /**
     * Indicates no sport influence.
     */
    NO_SPORT_INFLUENCE,
    /**
     * Indicates low after bolus.
     */
    HYPO_AFTER_BOLUS,
    /**
     * Indicates low after sports.
     */
    HYPO_AFTER_SPORTS,
    /**
     * Indicates low without influence.
     */
    HYPO_WITHOUT_INFLUENCE,
    /**
     * Indicates high after carbohydrates intake.
     */
    HYPER_AFTER_CARB,
    /**
     * Indicates high after bolus.
     */
    HYPER_AFTER_BOLUS,
    /**
     * Indicates high after sports.
     */
    HYPER_AFTER_SPORTS,
    /**
     * Indicates high without influence.
     */
    HYPER_WITHOUT_INFLUENCE,
    /**
     * Indicates sleep.
     */
    SLEEP,
    /**
     * Indicates waking up.
     */
    WAKEUP,
    /**
     * Indicates event before bedtime.
     */
    BEFORE_BEDTIME,
    /**
     * Indicates threshold for a rise.
     */
    RISE_THRESHOLD,
    /**
     * Indicates alcoholic consume.
     */
    ALCOHOL_CONSUME
}
