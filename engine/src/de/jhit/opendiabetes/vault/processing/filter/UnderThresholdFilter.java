/*
 * Copyright (C) 2017 AChikhale
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
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javafx.util.Pair;

/**
 *
 * @author AChikhale
 */
public class UnderThresholdFilter implements Filter {

    private double thresholdValue;
    private FilterType type;

    public List<VaultEntry> data;
    public VaultEntryType GenericType;
    public FilterType availabledatatype;
    public FilterType TH;

// GenericType I am taking it as package de.jhit.opendiabetes.vault.container.VaultEntry as private VaultEntryType type;
    // thresholdValue is used to check if the value from DB is greater or less than given threshold
    public UnderThresholdFilter(VaultEntryType GenericType, Double thresholdValue, FilterType availabledatatype, FilterType TH) {
        if (!(((GenericType == VaultEntryType.BASAL_PROFILE || GenericType == VaultEntryType.BASAL_MANUAL || GenericType == VaultEntryType.BASAL_INTERPRETER) && (TH == FilterType.BASAL_TH))
                || ((GenericType == VaultEntryType.BOLUS_NORMAL || GenericType == VaultEntryType.BOLUS_SQARE || GenericType == VaultEntryType.GLUCOSE_BOLUS_CALCULATION) && (TH == FilterType.BOLUS_TH))
                || ((GenericType == VaultEntryType.GLUCOSE_BG || GenericType == VaultEntryType.GLUCOSE_BG_MANUAL) && (TH == FilterType.BG_TH))
                || ((GenericType == VaultEntryType.GLUCOSE_CGM || GenericType == VaultEntryType.GLUCOSE_CGM_RAW || GenericType == VaultEntryType.GLUCOSE_CGM_ALERT
                || GenericType == VaultEntryType.GLUCOSE_CGM_CALIBRATION) && (TH == FilterType.CGM_TH))
                || ((GenericType == VaultEntryType.HEART_RATE || GenericType == VaultEntryType.HEART_RATE_VARIABILITY) && (TH == FilterType.HR_TH))
                || ((GenericType == VaultEntryType.STRESS) && (TH == FilterType.STRESS_TH)))) {

            return;
        } else if (!((availabledatatype == FilterType.BASAL_AVAILABLE && TH == FilterType.BASAL_TH)
                || (availabledatatype == FilterType.BG_AVAILABLE && TH == FilterType.BG_TH)
                || (availabledatatype == FilterType.BOLUS_AVAILABLE && TH == FilterType.BOLUS_TH)
                || (availabledatatype == FilterType.CGM_AVAILABLE && TH == FilterType.CGM_TH)
                || (availabledatatype == FilterType.HR_AVAILABLE && TH == FilterType.HR_TH)
                || (availabledatatype == FilterType.STRESS_AVAILABLE && TH == FilterType.STRESS_TH))) {
            // System.out.println("Parameters passed for FilterType are not in correct combination");

            return;
        } else {

            this.thresholdValue = thresholdValue;
            this.type = TH;
            this.TH = TH;
            this.GenericType = GenericType;
            this.availabledatatype = availabledatatype;

        }
    }

    /**
     * **********************
     * "Genericvalue" and "availabledatatype" can be used for below
     * BOLUS_NORMAL, BOLUS_SQARE, // Basal BASAL_PROFILE, BASAL_MANUAL,
     * BASAL_INTERPRETER, // Glucose GLUCOSE_CGM, GLUCOSE_CGM_RAW,
     * GLUCOSE_CGM_ALERT, GLUCOSE_CGM_CALIBRATION, GLUCOSE_BG,
     * GLUCOSE_BG_MANUAL, GLUCOSE_BOLUS_CALCULATION, // Heart HEART_RATE,
     * HEART_RATE_VARIABILITY, STRESS,
     *
     */
    @Override
    public FilterResult filter(List<VaultEntry> data) {
        if (!(((GenericType == VaultEntryType.BASAL_PROFILE || GenericType == VaultEntryType.BASAL_MANUAL || GenericType == VaultEntryType.BASAL_INTERPRETER) && (TH == FilterType.BASAL_TH))
                || ((GenericType == VaultEntryType.BOLUS_NORMAL || GenericType == VaultEntryType.BOLUS_SQARE || GenericType == VaultEntryType.GLUCOSE_BOLUS_CALCULATION) && (TH == FilterType.BOLUS_TH))
                || ((GenericType == VaultEntryType.GLUCOSE_BG || GenericType == VaultEntryType.GLUCOSE_BG_MANUAL) && (TH == FilterType.BG_TH))
                || ((GenericType == VaultEntryType.GLUCOSE_CGM || GenericType == VaultEntryType.GLUCOSE_CGM_RAW || GenericType == VaultEntryType.GLUCOSE_CGM_ALERT
                || GenericType == VaultEntryType.GLUCOSE_CGM_CALIBRATION) && (TH == FilterType.CGM_TH))
                || ((GenericType == VaultEntryType.HEART_RATE || GenericType == VaultEntryType.HEART_RATE_VARIABILITY) && (TH == FilterType.HR_TH))
                || ((GenericType == VaultEntryType.STRESS) && (TH == FilterType.STRESS_TH)))) {
            System.out.println("Parameters passed for VaultEntryType and FilterType are not in correct combination");

            return null;
        } else if (!((availabledatatype == FilterType.BASAL_AVAILABLE && TH == FilterType.BASAL_TH)
                || (availabledatatype == FilterType.BG_AVAILABLE && TH == FilterType.BG_TH)
                || (availabledatatype == FilterType.BOLUS_AVAILABLE && TH == FilterType.BOLUS_TH)
                || (availabledatatype == FilterType.CGM_AVAILABLE && TH == FilterType.CGM_TH)
                || (availabledatatype == FilterType.HR_AVAILABLE && TH == FilterType.HR_TH)
                || (availabledatatype == FilterType.STRESS_AVAILABLE && TH == FilterType.STRESS_TH))) {
            System.out.println("Parameters passed for FilterType are not in correct combination");

            return null;
        }

        // List<Pair<Date, Date>> timeSeries = new ArrayList<>();
        List<VaultEntry> filteredData = new ArrayList<>();
        List<Pair<Date, Date>> timeSeries = new ArrayList<>();
        Date startOfCuttenTimeSeries = null;
        Date lastTimeStamp = null;

        GenericFilter tt = new GenericFilter(GenericType);

        for (VaultEntry entry : tt.tempfunction(data) /*filter.filter(data).filteredData*/) {
            if (entry.getValue() < thresholdValue) {
                filteredData.add(entry);
                if (startOfCuttenTimeSeries == null) {
                    startOfCuttenTimeSeries = entry.getTimestamp();
                }
                lastTimeStamp = entry.getTimestamp();
            }
        }

        if (startOfCuttenTimeSeries != null) {
            timeSeries.add(new Pair<>(startOfCuttenTimeSeries, lastTimeStamp));
        }

        return new FilterResult(filteredData, timeSeries);
    }

    @Override
    public FilterType getType() {
        return type;
    }
}
