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
package de.jhit.opendiabetes.vault.container.csv;

import de.jhit.opendiabetes.vault.container.VaultEntry;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

/**
 *
 * @author mswin
 */
public class VaultCsvEntry extends CsvEntry {

    public final static String VERSION_STRING = "v10_3";
    public final static double UNINITIALIZED_DOUBLE = VaultEntry.VALUE_UNUSED;

    private Date timestamp;
    private double bgValue = UNINITIALIZED_DOUBLE;
    private double cgmValue = UNINITIALIZED_DOUBLE;
    private double cgmRawValue = UNINITIALIZED_DOUBLE;
    private double cgmAlertValue = UNINITIALIZED_DOUBLE;
    private double pumpCgmPredictionValue = UNINITIALIZED_DOUBLE;
    private List<String> glucoseAnnotation = new ArrayList<>();
    private double basalValue = UNINITIALIZED_DOUBLE;
    private List<String> basalAnnotation = new ArrayList<>();
    private double bolusValue = UNINITIALIZED_DOUBLE;
    private double bolusCalculationValue = UNINITIALIZED_DOUBLE;
    private List<String> bolusAnnotation = new ArrayList<>();
    private double mealValue = UNINITIALIZED_DOUBLE;
    private List<String> pumpAnnotation = new ArrayList<>();
    private double exerciseTimeValue = UNINITIALIZED_DOUBLE;
    private List<String> exerciseAnnotation = new ArrayList<>();
    private double heartRateValue = UNINITIALIZED_DOUBLE;
    private double stressBalanceValue = UNINITIALIZED_DOUBLE;
    private double heartRateVariabilityValue = UNINITIALIZED_DOUBLE;
    private double stressValue = UNINITIALIZED_DOUBLE;
    private double sleepValue = UNINITIALIZED_DOUBLE;
    private List<String> sleepAnnotation = new ArrayList<>();
    private List<String> locationAnnotation = new ArrayList<>();
    private double mlCgmValue = UNINITIALIZED_DOUBLE;
    private List<String> mlAnnotation = new ArrayList<>();
    private double insulinSensitivityFactor = UNINITIALIZED_DOUBLE;
    private List<String> otherAnnotation = new ArrayList<>();

    public Date getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Date timestamp) {
        this.timestamp = timestamp;
    }

    public double getBgValue() {
        return bgValue;
    }

    public void setBgValue(double bgValue) {
        this.bgValue = bgValue;
    }

    public double getCgmValue() {
        return cgmValue;
    }

    public void setCgmValue(double cgmValue) {
        this.cgmValue = cgmValue;
    }

    public double getCgmRawValue() {
        return cgmRawValue;
    }

    public void setCgmRawValue(double cgmRawValue) {
        this.cgmRawValue = cgmRawValue;
    }

    public double getCgmAlertValue() {
        return cgmAlertValue;
    }

    public void setCgmAlertValue(double cgmAlertValue) {
        this.cgmAlertValue = cgmAlertValue;
    }

    public double getPumpCgmPredictionValue() {
        return pumpCgmPredictionValue;
    }

    public void setPumpCgmPredictionValue(double pumpCgmPredictionValue) {
        this.pumpCgmPredictionValue = pumpCgmPredictionValue;
    }

    public List<String> getGlucoseAnnotation() {
        return glucoseAnnotation;
    }

    public void setGlucoseAnnotation(List<String> cgmAnnotation) {
        this.glucoseAnnotation = cgmAnnotation;
    }

    public void addGlucoseAnnotation(String cgmAnnotation) {
        this.glucoseAnnotation.add(cgmAnnotation);
    }

    public double getBasalValue() {
        return basalValue;
    }

    public void setBasalValue(double basalValue) {
        this.basalValue = basalValue;
    }

    public double getBolusValue() {
        return bolusValue;
    }

    public void setBolusValue(double bolusValue) {
        this.bolusValue = bolusValue;
    }

    public double getBolusCalculationValue() {
        return bolusCalculationValue;
    }

    public void setBolusCalculationValue(double bolusCalculationValue) {
        this.bolusCalculationValue = bolusCalculationValue;
    }

    public double getMealValue() {
        return mealValue;
    }

    public void setMealValue(double mealValue) {
        this.mealValue = mealValue;
    }

    public List<String> getPumpAnnotation() {
        return pumpAnnotation;
    }

    public void setPumpAnnotation(List<String> pumpAnnotation) {
        this.pumpAnnotation = pumpAnnotation;
    }

    public void addPumpAnnotation(String pumpAnnotation) {
        this.pumpAnnotation.add(pumpAnnotation);
    }

    public double getExerciseTimeValue() {
        return exerciseTimeValue;
    }

    public void setExerciseTimeValue(double exerciseTimeValue) {
        this.exerciseTimeValue = exerciseTimeValue;
    }

    public void addExerciseAnnotation(String exerciseAnnotation) {
        this.exerciseAnnotation.add(exerciseAnnotation);
    }

    public List<String> getExerciseAnnotation() {
        return exerciseAnnotation;
    }

    public void setExerciseAnnotation(List<String> exerciseAnnotation) {
        this.exerciseAnnotation = exerciseAnnotation;
    }

    public List<String> getBasalAnnotation() {
        return basalAnnotation;
    }

    public void setBasalAnnotation(List<String> basalAnnotation) {
        this.basalAnnotation = basalAnnotation;
    }

    public void addBasalAnnotation(String basalAnnotation) {
        this.basalAnnotation.add(basalAnnotation);
    }

    public List<String> getBolusAnnotation() {
        return bolusAnnotation;
    }

    public void setBolusAnnotation(List<String> bolusAnnotation) {
        this.bolusAnnotation = bolusAnnotation;
    }

    public void addBolusAnnotation(String bolusAnnotation) {
        this.bolusAnnotation.add(bolusAnnotation);
    }

    public double getHeartRateValue() {
        return heartRateValue;
    }

    public void setHeartRateValue(double pulseValue) {
        this.heartRateValue = pulseValue;
    }

    public double getHeartRateVariabilityValue() {
        return heartRateVariabilityValue;
    }

    public void setHeartRateVariabilityValue(double hrvValue) {
        this.heartRateVariabilityValue = hrvValue;
    }

    public double getStressBalance() {
        return stressBalanceValue;
    }

    public void setStressBalanceValue(double stressBalance) {
        this.stressBalanceValue = stressBalance;
    }

    public double getStressValue() {
        return stressValue;
    }

    public void setStressValue(double stressValue) {
        this.stressValue = stressValue;
    }

    public double getSleepValue() {
        return sleepValue;
    }

    public void setSleepValue(double sleepValue) {
        this.sleepValue = sleepValue;
    }

    public List<String> getSleepAnnotation() {
        return sleepAnnotation;
    }

    public void setSleepAnnotation(List<String> sleepAnnotation) {
        this.sleepAnnotation = sleepAnnotation;
    }

    public void addSleepAnnotation(String sleepAnnotation) {
        this.sleepAnnotation.add(sleepAnnotation);
    }

    public List<String> getLocationAnnotation() {
        return locationAnnotation;
    }

    public void setLocationAnnotation(List<String> locationAnnotation) {
        this.locationAnnotation = locationAnnotation;
    }

    public void addLocationAnnotation(String locationAnnotation) {
        this.locationAnnotation.add(locationAnnotation);
    }

    public double getMlCgmValue() {
        return mlCgmValue;
    }

    public void setMlCgmValue(double mlCgmValue) {
        this.mlCgmValue = mlCgmValue;
    }

    public double getInsulinSensitivityFactor() {
        return insulinSensitivityFactor;
    }

    public void setInsulinSensitivityFactor(double insulinSensitivityFactor) {
        this.insulinSensitivityFactor = insulinSensitivityFactor;
    }

    public List<String> getMlAnnotation() {
        return mlAnnotation;
    }

    public void addMlAnnotation(String mlAnnotation) {
        this.mlAnnotation.add(mlAnnotation);
    }

    public void setMlAnnotation(List<String> mlAnnotation) {
        this.mlAnnotation = mlAnnotation;
    }

    public List<String> getOtherAnnotation() {
        return otherAnnotation;
    }

    public void setOtherAnnotation(List<String> otherAnnotation) {
        this.otherAnnotation = otherAnnotation;
    }

    public void addOtherAnnotation(String userAnnotation) {
        this.otherAnnotation.add(userAnnotation);
    }

    public boolean isEmpty() {
        return bgValue == UNINITIALIZED_DOUBLE
                && cgmValue == UNINITIALIZED_DOUBLE
                && cgmRawValue == UNINITIALIZED_DOUBLE
                && cgmAlertValue == UNINITIALIZED_DOUBLE
                && pumpCgmPredictionValue == UNINITIALIZED_DOUBLE
                && glucoseAnnotation.isEmpty()
                && basalValue == UNINITIALIZED_DOUBLE
                && basalAnnotation.isEmpty()
                && bolusValue == UNINITIALIZED_DOUBLE
                && bolusAnnotation.isEmpty()
                && bolusCalculationValue == UNINITIALIZED_DOUBLE
                && mealValue == UNINITIALIZED_DOUBLE
                && pumpAnnotation.isEmpty()
                && exerciseTimeValue == UNINITIALIZED_DOUBLE
                && exerciseAnnotation.isEmpty()
                && heartRateValue == UNINITIALIZED_DOUBLE
                && heartRateVariabilityValue == UNINITIALIZED_DOUBLE
                && stressBalanceValue == UNINITIALIZED_DOUBLE
                && stressValue == UNINITIALIZED_DOUBLE
                && sleepValue == UNINITIALIZED_DOUBLE
                && sleepAnnotation.isEmpty()
                && locationAnnotation.isEmpty()
                && mlCgmValue == UNINITIALIZED_DOUBLE
                && mlAnnotation.isEmpty()
                && insulinSensitivityFactor == UNINITIALIZED_DOUBLE
                && otherAnnotation.isEmpty();
    }

    public String toCsvString() {
        StringBuilder sb = new StringBuilder();

        String[] record = this.toCsvRecord();
        for (String item : record) {
            sb.append(item).append(CSV_DELIMITER);
        }
        sb.deleteCharAt(sb.length() - 1);

        return sb.toString();
    }

    @Override
    public String[] toCsvRecord() {
        return toCsvRecord(DECIMAL_FORMAT);
    }

    public String[] toCsvRecord(String decimalFormat) {
        ArrayList<String> csvRecord = new ArrayList<>();

        csvRecord.add(new SimpleDateFormat("dd.MM.yy").format(timestamp));
        csvRecord.add(new SimpleDateFormat("HH:mm").format(timestamp));
        if (bgValue > 0.0) {
            csvRecord.add(String.format(Locale.ENGLISH, decimalFormat, bgValue)
                    .replaceAll(",", ""));
        } else {
            csvRecord.add("");
        }
        if (cgmValue > UNINITIALIZED_DOUBLE) {
            csvRecord.add(String.format(Locale.ENGLISH, decimalFormat, cgmValue)
                    .replaceAll(",", ""));
        } else {
            csvRecord.add("");
        }
        if (cgmRawValue > UNINITIALIZED_DOUBLE) {
            csvRecord.add(String.format(Locale.ENGLISH, decimalFormat, cgmRawValue)
                    .replaceAll(",", ""));
        } else {
            csvRecord.add("");
        }
        if (cgmAlertValue > UNINITIALIZED_DOUBLE) {
            csvRecord.add(String.format(Locale.ENGLISH, decimalFormat, cgmAlertValue)
                    .replaceAll(",", ""));
        } else {
            csvRecord.add("");
        }
        if (pumpCgmPredictionValue > UNINITIALIZED_DOUBLE) {
            csvRecord.add(String.format(Locale.ENGLISH, decimalFormat, pumpCgmPredictionValue)
                    .replaceAll(",", ""));
        } else {
            csvRecord.add("");
        }
        if (!glucoseAnnotation.isEmpty()) {
            StringBuilder annotations = new StringBuilder();
            for (String item : glucoseAnnotation) {
                annotations.insert(0, CSV_LIST_DELIMITER).insert(0, item);
            }
            annotations.deleteCharAt(annotations.length() - 1);
            csvRecord.add(annotations.toString());
        } else {
            csvRecord.add("");
        }
        if (basalValue > UNINITIALIZED_DOUBLE) {
            csvRecord.add(String.format(Locale.ENGLISH, decimalFormat, basalValue)
                    .replaceAll(",", ""));
        } else {
            csvRecord.add("");
        }
        if (!basalAnnotation.isEmpty()) {
            StringBuilder annotations = new StringBuilder();
            for (String item : basalAnnotation) {
                annotations.insert(0, CSV_LIST_DELIMITER).insert(0, item);
            }
            annotations.deleteCharAt(annotations.length() - 1);
            csvRecord.add(annotations.toString());
        } else {
            csvRecord.add("");
        }
        if (bolusValue > UNINITIALIZED_DOUBLE) {
            csvRecord.add(String.format(Locale.ENGLISH, decimalFormat, bolusValue)
                    .replaceAll(",", ""));
        } else {
            csvRecord.add("");
        }
        if (!bolusAnnotation.isEmpty()) {
            StringBuilder annotations = new StringBuilder();
            for (String item : bolusAnnotation) {
                annotations.insert(0, CSV_LIST_DELIMITER).insert(0, item);
            }
            annotations.deleteCharAt(annotations.length() - 1);
            csvRecord.add(annotations.toString());
        } else {
            csvRecord.add("");
        }
        if (bolusCalculationValue > UNINITIALIZED_DOUBLE) {
            csvRecord.add(String.format(Locale.ENGLISH, decimalFormat, bolusCalculationValue)
                    .replaceAll(",", ""));
        } else {
            csvRecord.add("");
        }
        if (mealValue > UNINITIALIZED_DOUBLE) {
            csvRecord.add(String.format(Locale.ENGLISH, decimalFormat, mealValue)
                    .replaceAll(",", ""));
        } else {
            csvRecord.add("");
        }
        if (!pumpAnnotation.isEmpty()) {
            StringBuilder annotations = new StringBuilder();
            for (String item : pumpAnnotation) {
                annotations.insert(0, CSV_LIST_DELIMITER).insert(0, item);
            }
            annotations.deleteCharAt(annotations.length() - 1);
            csvRecord.add(annotations.toString());
        } else {
            csvRecord.add("");
        }
        if (exerciseTimeValue > UNINITIALIZED_DOUBLE) {
            csvRecord.add(String.format(Locale.ENGLISH, decimalFormat, exerciseTimeValue)
                    .replaceAll(",", ""));
        } else {
            csvRecord.add("");
        }
        if (!exerciseAnnotation.isEmpty()) {
            StringBuilder annotations = new StringBuilder();
            for (String item : exerciseAnnotation) {
                annotations.append(item).append(CSV_LIST_DELIMITER);
            }
            annotations.deleteCharAt(annotations.length() - 1);
            csvRecord.add(annotations.toString());
        } else {
            csvRecord.add("");
        }
        if (heartRateValue > UNINITIALIZED_DOUBLE) {
            csvRecord.add(String.format(Locale.ENGLISH, decimalFormat, heartRateValue)
                    .replaceAll(",", ""));
        } else {
            csvRecord.add("");
        }
        if (heartRateVariabilityValue > UNINITIALIZED_DOUBLE) {
            csvRecord.add(String.format(Locale.ENGLISH, decimalFormat, heartRateVariabilityValue)
                    .replaceAll(",", ""));
        } else {
            csvRecord.add("");
        }
        if (stressBalanceValue > UNINITIALIZED_DOUBLE) {
            csvRecord.add(String.format(Locale.ENGLISH, decimalFormat, stressBalanceValue)
                    .replaceAll(",", ""));
        } else {
            csvRecord.add("");
        }
        if (stressValue > UNINITIALIZED_DOUBLE) {
            csvRecord.add(String.format(Locale.ENGLISH, decimalFormat, stressValue)
                    .replaceAll(",", ""));
        } else {
            csvRecord.add("");
        }
        if (sleepValue > UNINITIALIZED_DOUBLE) {
            csvRecord.add(String.format(Locale.ENGLISH, decimalFormat, sleepValue)
                    .replaceAll(",", ""));
        } else {
            csvRecord.add("");
        }
        if (!sleepAnnotation.isEmpty()) {
            StringBuilder annotations = new StringBuilder();
            for (String item : sleepAnnotation) {
                annotations.insert(0, CSV_LIST_DELIMITER).insert(0, item);
            }
            annotations.deleteCharAt(annotations.length() - 1);
            csvRecord.add(annotations.toString());
        } else {
            csvRecord.add("");
        }
        if (!locationAnnotation.isEmpty()) {
            StringBuilder annotations = new StringBuilder();
            for (String item : locationAnnotation) {
                annotations.insert(0, CSV_LIST_DELIMITER).insert(0, item);
            }
            annotations.deleteCharAt(annotations.length() - 1);
            csvRecord.add(annotations.toString());
        } else {
            csvRecord.add("");
        }
        if (mlCgmValue > UNINITIALIZED_DOUBLE) {
            csvRecord.add(String.format(Locale.ENGLISH, decimalFormat, mlCgmValue)
                    .replaceAll(",", ""));
        } else {
            csvRecord.add("");
        }
        if (!mlAnnotation.isEmpty()) {
            StringBuilder annotations = new StringBuilder();
            for (String item : mlAnnotation) {
                annotations.insert(0, CSV_LIST_DELIMITER).insert(0, item);
            }
            annotations.deleteCharAt(annotations.length() - 1);
            csvRecord.add(annotations.toString());
        } else {
            csvRecord.add("");
        }
        if (insulinSensitivityFactor > UNINITIALIZED_DOUBLE) {
            csvRecord.add(String.format(Locale.ENGLISH, decimalFormat, insulinSensitivityFactor)
                    .replaceAll(",", ""));
        } else {
            csvRecord.add("");
        }
        if (!otherAnnotation.isEmpty()) {
            StringBuilder annotations = new StringBuilder();
            for (String item : otherAnnotation) {
                annotations.insert(0, CSV_LIST_DELIMITER).insert(0, item);
            }
            annotations.deleteCharAt(annotations.length() - 1);
            csvRecord.add(annotations.toString());
        } else {
            csvRecord.add("");
        }

        return csvRecord.toArray(new String[]{});
    }

    public String getCsvHeaderString() {
        StringBuilder sb = new StringBuilder();

        String[] header = getCsvHeaderRecord();
        for (String item : header) {
            sb.append(item).append(CSV_DELIMITER);
        }
        sb.deleteCharAt(sb.length() - 1);

        return sb.toString();
    }

    @Override
    public String[] getCsvHeaderRecord() {
        return new String[]{
            "date",
            "time",
            "bgValue",
            "cgmValue",
            "cgmRawValue",
            "cgmAlertValue",
            "pumpCgmPredictionValue",
            "glucoseAnnotation",
            "basalValue",
            "basalAnnotation",
            "bolusValue",
            "bolusAnnotation",
            "bolusCalculationValue",
            "mealValue",
            "pumpAnnotation",
            "exerciseTimeValue",
            "exerciseAnnotation",
            "heartRateValue",
            "heartRateVariabilityValue",
            "stressBalanceValue",
            "stressValue",
            "sleepValue",
            "sleepAnnotation",
            "locationAnnotation",
            "mlCgmValue",
            "mlAnnotation",
            "insulinSensitivityFactor",
            "otherAnnotation"
        };
    }

    @Override
    public String toString() {
        return "VaultCsvEntry{" + "timestamp=" + timestamp + ", bgValue=" + bgValue + ", cgmValue=" + cgmValue + ", cgmRawValue=" + cgmRawValue + ", cgmAlertValue=" + cgmAlertValue + ", pumpCgmPredictionValue=" + pumpCgmPredictionValue + ", glucoseAnnotation=" + glucoseAnnotation + ", basalValue=" + basalValue + ", basalAnnotation=" + basalAnnotation + ", bolusValue=" + bolusValue + ", bolusCalculationValue=" + bolusCalculationValue + ", bolusAnnotation=" + bolusAnnotation + ", mealValue=" + mealValue + ", pumpAnnotation=" + pumpAnnotation + ", exerciseTimeValue=" + exerciseTimeValue + ", exerciseAnnotation=" + exerciseAnnotation + ", heartRateValue=" + heartRateValue + ", stressBalanceValue=" + stressBalanceValue + ", heartRateVariabilityValue=" + heartRateVariabilityValue + ", stressValue=" + stressValue + ", sleepValue=" + sleepValue + ", sleepAnnotation=" + sleepAnnotation + ", locationAnnotation=" + locationAnnotation + ", mlCgmValue=" + mlCgmValue + ", mlAnnotation=" + mlAnnotation + ", insulinSensitivityFactor=" + insulinSensitivityFactor + ", otherAnnotation=" + otherAnnotation + '}';
    }

}
