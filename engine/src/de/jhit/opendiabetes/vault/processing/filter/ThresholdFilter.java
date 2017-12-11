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
package de.jhit.opendiabetes.vault.processing.filter;

import de.jhit.opendiabetes.vault.container.VaultEntry;
import de.jhit.opendiabetes.vault.container.VaultEntryType;
import java.util.List;

/**
 * This Filter is the superclass for the Thresholdfilter.
 * @author juehv, Daniel
 */
public abstract class ThresholdFilter extends Filter{
    
    /**
     * This method checks if the given Data is valuable for a real scenario.
     * @param GenericType
     * @param availabledatatype
     * @param TH
     * @return boolean
     */
    public boolean checkThresholdCombination(VaultEntryType GenericType, FilterType availabledatatype, FilterType TH)
    {
        boolean result = true;
        
        if (!(((GenericType == VaultEntryType.BASAL_PROFILE || GenericType == VaultEntryType.BASAL_MANUAL || GenericType == VaultEntryType.BASAL_INTERPRETER) && (TH == FilterType.BASAL_TH))
                || ((GenericType == VaultEntryType.BOLUS_NORMAL || GenericType == VaultEntryType.BOLUS_SQARE || GenericType == VaultEntryType.GLUCOSE_BOLUS_CALCULATION) && (TH == FilterType.BOLUS_TH))
                || ((GenericType == VaultEntryType.GLUCOSE_BG || GenericType == VaultEntryType.GLUCOSE_BG_MANUAL) && (TH == FilterType.BG_TH))
                || ((GenericType == VaultEntryType.GLUCOSE_CGM || GenericType == VaultEntryType.GLUCOSE_CGM_RAW || GenericType == VaultEntryType.GLUCOSE_CGM_ALERT
                || GenericType == VaultEntryType.GLUCOSE_CGM_CALIBRATION) && (TH == FilterType.CGM_TH))
                || ((GenericType == VaultEntryType.HEART_RATE || GenericType == VaultEntryType.HEART_RATE_VARIABILITY) && (TH == FilterType.HR_TH))
                || ((GenericType == VaultEntryType.STRESS) && (TH == FilterType.STRESS_TH)))) {

            result = false;
        } else if (!((availabledatatype == FilterType.BASAL_AVAILABLE && TH == FilterType.BASAL_TH)
                || (availabledatatype == FilterType.BG_AVAILABLE && TH == FilterType.BG_TH)
                || (availabledatatype == FilterType.BOLUS_AVAILABLE && TH == FilterType.BOLUS_TH)
                || (availabledatatype == FilterType.CGM_AVAILABLE && TH == FilterType.CGM_TH)
                || (availabledatatype == FilterType.HR_AVAILABLE && TH == FilterType.HR_TH)
                || (availabledatatype == FilterType.STRESS_AVAILABLE && TH == FilterType.STRESS_TH))) {

            result = false;
        }
        
        return result;
    }

}
