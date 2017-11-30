/*
 * Copyright (C) 2017 mswin
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
package de.jhit.opendiabetes.vault.processing.filter;

/**
 *
 * @author mswin
 */
public enum FilterType {
    // basic time
    TIME_SPAN,
    TIME_POINT,
    DATE_TIME_POINT,
    DATE_TIME_SPAN,
    // available data
    BOLUS_AVAILABLE,
    BASAL_AVAILABLE,
    BG_AVAILABLE,
    CGM_AVAILABLE,
    HR_AVAILABLE,
    SLEEP_AVAILABLE,
    STRESS_AVAILABLE,
    EXERCISE_AVAILABLE,
    // data absence
    MEAL_ABSENCE,
    BOLUS_ABSENCE,
    // threshould
    BOLUS_TH,
    BASAL_TH,
    BG_TH,
    CGM_TH,
    HR_TH,
    STRESS_TH,
    // filter nothing
    NONE;

}
