/*
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
package de.opendiabetes.vault.container.csv;

import de.opendiabetes.vault.container.VaultEntry;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import static de.opendiabetes.vault.plugin.util.TimestampUtils.copyTimestamp;

/**
 * Definition of the VaultCSVEntry.
 *
 * @author Jens Heuschkel
 */
public class VaultCsvEntry extends CsvEntry {
    /**
     * The version of the VaultCSVEntry.
     */
    public static final String VERSION_STRING = "v10_3";
    /**
     * Uninitialized double value.
     */
    public static final double UNINITIALIZED_DOUBLE = VaultEntry.VALUE_UNUSED;
    /**
     * Timestamp of the entry.
     */
    private Date timestamp;
    /**
     * Value of the blood glucose.
     */
    private double bgValue = UNINITIALIZED_DOUBLE;
    /**
     * Value of the continuous glucose monitoring.
     */
    private double cgmValue = UNINITIALIZED_DOUBLE;
    /**
     * Raw value of the continuous glucose monitoring.
     */
    private double cgmRawValue = UNINITIALIZED_DOUBLE;
    /**
     * Alert value of the continuous glucose monitoring.
     */
    private double cgmAlertValue = UNINITIALIZED_DOUBLE;
    /**
     * Predicted pump value of the continuous glucose monitoring.
     */
    private double pumpCgmPredictionValue = UNINITIALIZED_DOUBLE;
    /**
     * Glucose annotations.
     */
    private List<String> glucoseAnnotation = new ArrayList<>();
    /**
     * Base value.
     */
    private double basalValue = UNINITIALIZED_DOUBLE;
    /**
     * Base annotations.
     */
    private List<String> basalAnnotation = new ArrayList<>();
    /**
     * Value of the bolus.
     */
    private double bolusValue = UNINITIALIZED_DOUBLE;
    /**
     * Value of the bolus calculation.
     */
    private double bolusCalculationValue = UNINITIALIZED_DOUBLE;
    /**
     * Bolus annotations.
     */
    private List<String> bolusAnnotation = new ArrayList<>();
    /**
     * Value of the meal.
     */
    private double mealValue = UNINITIALIZED_DOUBLE;
    /**
     * Pump annotations.
     */
    private List<String> pumpAnnotation = new ArrayList<>();
    /**
     * Exercise time.
     */
    private double exerciseTimeValue = UNINITIALIZED_DOUBLE;
    /**
     * Exercise annotations.
     */
    private List<String> exerciseAnnotation = new ArrayList<>();
    /**
     * Heart rate.
     */
    private double heartRateValue = UNINITIALIZED_DOUBLE;
    /**
     * Stress balance.
     */
    private double stressBalanceValue = UNINITIALIZED_DOUBLE;
    /**
     * Variability of the heart rate.
     */
    private double heartRateVariabilityValue = UNINITIALIZED_DOUBLE;
    /**
     * Stress value.
     */
    private double stressValue = UNINITIALIZED_DOUBLE;
    /**
     * Sleep value.
     */
    private double sleepValue = UNINITIALIZED_DOUBLE;
    /**
     * Sleep annotations.
     */
    private List<String> sleepAnnotation = new ArrayList<>();
    /**
     * Location annotations.
     */
    private List<String> locationAnnotation = new ArrayList<>();
    /**
     * Value of the continuous glucose monitoring in milliliters.
     */
    private double mlCgmValue = UNINITIALIZED_DOUBLE;
    /**
     * Milliliter annotations.
     */
    private List<String> mlAnnotation = new ArrayList<>();
    /**
     * Factor for insulin sensitivity.
     */
    private double insulinSensitivityFactor = UNINITIALIZED_DOUBLE;
    /**
     * Other annotations.
     */
    private List<String> otherAnnotation = new ArrayList<>();

    /**
     * The weight.
     */
    private double weight = UNINITIALIZED_DOUBLE;


    /**
     * Getter for entry's {@link #timestamp}.
     *
     * @return The entry's {@link #timestamp}.
     */
    public Date getTimestamp() {
        return copyTimestamp(timestamp);
    }

    /**
     * Setter for the entry's {@link #timestamp}.
     *
     * @param timestamp The {@link #timestamp} to be set.
     */
    public void setTimestamp(final Date timestamp) {
        this.timestamp = copyTimestamp(timestamp);
    }

    /**
     * Getter for entry's {@link #bgValue}.
     *
     * @return The entry's {@link #bgValue}.
     */
    public double getBgValue() {
        return bgValue;
    }

    /**
     * Setter for the entry's {@link #bgValue}.
     *
     * @param bgValue The {@link #bgValue} to be set.
     */
    public void setBgValue(final double bgValue) {
        this.bgValue = bgValue;
    }

    /**
     * Getter for entry's {@link #cgmRawValue}.
     *
     * @return The entry's {@link #cgmValue}.
     */
    public double getCgmValue() {
        return cgmValue;
    }

    /**
     * Setter for the entry's {@link #cgmValue}.
     *
     * @param cgmValue The {@link #cgmValue} to be set.
     */
    public void setCgmValue(final double cgmValue) {
        this.cgmValue = cgmValue;
    }

    /**
     * Getter for entry's {@link #cgmRawValue}.
     *
     * @return The entry's {@link #cgmRawValue}.
     */
    public double getCgmRawValue() {
        return cgmRawValue;
    }

    /**
     * Setter for the entry's {@link #cgmRawValue}.
     *
     * @param cgmRawValue The {@link #cgmRawValue} to be set.
     */
    public void setCgmRawValue(final double cgmRawValue) {
        this.cgmRawValue = cgmRawValue;
    }

    /**
     * Getter for entry's {@link #cgmAlertValue}.
     *
     * @return The entry's {@link #cgmAlertValue}.
     */
    public double getCgmAlertValue() {
        return cgmAlertValue;
    }

    /**
     * Setter for the entry's {@link #cgmAlertValue}.
     *
     * @param cgmAlertValue The {@link #cgmAlertValue} to be set.
     */
    public void setCgmAlertValue(final double cgmAlertValue) {
        this.cgmAlertValue = cgmAlertValue;
    }

    /**
     * Getter for entry's {@link #pumpAnnotation}.
     *
     * @return The entry's {@link #pumpCgmPredictionValue}.
     */
    public double getPumpCgmPredictionValue() {
        return pumpCgmPredictionValue;
    }

    /**
     * Setter for the entry's {@link #pumpCgmPredictionValue}.
     *
     * @param pumpCgmPredictionValue The {@link #pumpCgmPredictionValue} to be set.
     */
    public void setPumpCgmPredictionValue(final double pumpCgmPredictionValue) {
        this.pumpCgmPredictionValue = pumpCgmPredictionValue;
    }

    /**
     * Getter for entry's {@link #glucoseAnnotation}.
     *
     * @return The entry's {@link #glucoseAnnotation}.
     */
    public List<String> getGlucoseAnnotation() {
        return glucoseAnnotation;
    }

    /**
     * Setter for the entry's {@link #glucoseAnnotation}.
     *
     * @param cgmAnnotation The {@link #glucoseAnnotation} to be set.
     */
    public void setGlucoseAnnotation(final List<String> cgmAnnotation) {
        this.glucoseAnnotation = cgmAnnotation;
    }

    /**
     * Adds an annotation to the entry's {@link #glucoseAnnotation}.
     *
     * @param cgmAnnotation The annotation to be added.
     */
    public void addGlucoseAnnotation(final String cgmAnnotation) {
        this.glucoseAnnotation.add(cgmAnnotation);
    }

    /**
     * Getter for entry's {@link #basalValue}.
     *
     * @return The entry's {@link #basalValue}.
     */
    public double getBasalValue() {
        return basalValue;
    }

    /**
     * Setter for the entry's {@link #basalValue}.
     *
     * @param basalValue The {@link #basalValue} to be set.
     */
    public void setBasalValue(final double basalValue) {
        this.basalValue = basalValue;
    }

    /**
     * Getter for entry's {@link #bolusValue}.
     *
     * @return The entry's {@link #bolusValue}.
     */
    public double getBolusValue() {
        return bolusValue;
    }

    /**
     * Setter for the entry's {@link #bolusValue}.
     *
     * @param bolusValue The {@link #bolusValue} to be set.
     */
    public void setBolusValue(final double bolusValue) {
        this.bolusValue = bolusValue;
    }

    /**
     * Getter for entry's {@link #bolusCalculationValue}.
     *
     * @return The entry's {@link #bolusCalculationValue}.
     */
    public double getBolusCalculationValue() {
        return bolusCalculationValue;
    }

    /**
     * Setter for the entry's {@link #bolusCalculationValue}.
     *
     * @param bolusCalculationValue The {@link #bolusCalculationValue} to be set.
     */
    public void setBolusCalculationValue(final double bolusCalculationValue) {
        this.bolusCalculationValue = bolusCalculationValue;
    }

    /**
     * Getter for entry's {@link #mealValue}.
     *
     * @return The entry's {@link #mealValue}.
     */
    public double getMealValue() {
        return mealValue;
    }

    /**
     * Setter for the entry's {@link #mealValue}.
     *
     * @param mealValue The {@link #mealValue} to be set.
     */
    public void setMealValue(final double mealValue) {
        this.mealValue = mealValue;
    }

    /**
     * Getter for entry's {@link #pumpAnnotation}.
     *
     * @return The entry's {@link #pumpAnnotation}.
     */
    public List<String> getPumpAnnotation() {
        return pumpAnnotation;
    }

    /**
     * Setter for the entry's {@link #pumpAnnotation}.
     *
     * @param pumpAnnotation The {@link #pumpAnnotation} to be set.
     */
    public void setPumpAnnotation(final List<String> pumpAnnotation) {
        this.pumpAnnotation = pumpAnnotation;
    }

    /**
     * Adds an annotation to the entry's {@link #pumpAnnotation}.
     *
     * @param pumpAnnotation The annotation to be added.
     */
    public void addPumpAnnotation(final String pumpAnnotation) {
        this.pumpAnnotation.add(pumpAnnotation);
    }

    /**
     * Getter for entry's {@link #exerciseTimeValue}.
     *
     * @return The entry's {@link #exerciseTimeValue}.
     */
    public double getExerciseTimeValue() {
        return exerciseTimeValue;
    }

    /**
     * Setter for the entry's {@link #exerciseTimeValue}.
     *
     * @param exerciseTimeValue The {@link #exerciseTimeValue} to be set.
     */
    public void setExerciseTimeValue(final double exerciseTimeValue) {
        this.exerciseTimeValue = exerciseTimeValue;
    }

    /**
     * Adds an annotation to the entry's {@link #exerciseAnnotation}.
     *
     * @param exerciseAnnotation The annotation to be added.
     */
    public void addExerciseAnnotation(final String exerciseAnnotation) {
        this.exerciseAnnotation.add(exerciseAnnotation);
    }

    /**
     * Getter for entry's {@link #exerciseAnnotation}.
     *
     * @return The entry's {@link #exerciseAnnotation}.
     */
    public List<String> getExerciseAnnotation() {
        return exerciseAnnotation;
    }

    /**
     * Setter for the entry's {@link #exerciseAnnotation}.
     *
     * @param exerciseAnnotation The {@link #exerciseAnnotation} to be set.
     */
    public void setExerciseAnnotation(final List<String> exerciseAnnotation) {
        this.exerciseAnnotation = exerciseAnnotation;
    }

    /**
     * Getter for entry's {@link #basalAnnotation}.
     *
     * @return The entry's {@link #basalAnnotation}.
     */
    public List<String> getBasalAnnotation() {
        return basalAnnotation;
    }

    /**
     * Setter for the entry's {@link #basalAnnotation}.
     *
     * @param basalAnnotation The {@link #basalAnnotation} to be set.
     */
    public void setBasalAnnotation(final List<String> basalAnnotation) {
        this.basalAnnotation = basalAnnotation;
    }

    /**
     * Adds an annotation to the entry's {@link #basalAnnotation}.
     *
     * @param basalAnnotation The annotation to be added.
     */
    public void addBasalAnnotation(final String basalAnnotation) {
        this.basalAnnotation.add(basalAnnotation);
    }

    /**
     * Getter for entry's {@link #bolusAnnotation}.
     *
     * @return The entry's {@link #bolusAnnotation}.
     */
    public List<String> getBolusAnnotation() {
        return bolusAnnotation;
    }

    /**
     * Setter for the entry's {@link #bolusAnnotation}.
     *
     * @param bolusAnnotation The {@link #bolusAnnotation} to be set.
     */
    public void setBolusAnnotation(final List<String> bolusAnnotation) {
        this.bolusAnnotation = bolusAnnotation;
    }

    /**
     * Adds an annotation to the entry's {@link #bolusAnnotation}.
     *
     * @param bolusAnnotation The annotation to be added.
     */
    public void addBolusAnnotation(final String bolusAnnotation) {
        this.bolusAnnotation.add(bolusAnnotation);
    }

    /**
     * Getter for entry's {@link #heartRateValue}.
     *
     * @return The entry's {@link #heartRateValue}.
     */
    public double getHeartRateValue() {
        return heartRateValue;
    }

    /**
     * Setter for the entry's {@link #heartRateValue}.
     *
     * @param pulseValue The {@link #heartRateValue} to be set.
     */
    public void setHeartRateValue(final double pulseValue) {
        this.heartRateValue = pulseValue;
    }

    /**
     * Getter for entry's {@link #heartRateVariabilityValue}.
     *
     * @return The entry's {@link #heartRateVariabilityValue}.
     */
    public double getHeartRateVariabilityValue() {
        return heartRateVariabilityValue;
    }

    /**
     * Setter for the entry's {@link #heartRateVariabilityValue}.
     *
     * @param hrvValue The {@link #heartRateVariabilityValue} to be set.
     */
    public void setHeartRateVariabilityValue(final double hrvValue) {
        this.heartRateVariabilityValue = hrvValue;
    }

    /**
     * Getter for entry's {@link #stressBalanceValue}.
     *
     * @return The entry's {@link #stressBalanceValue}.
     */
    public double getStressBalance() {
        return stressBalanceValue;
    }

    /**
     * Setter for the entry's {@link #stressBalanceValue}.
     *
     * @param stressBalance The {@link #stressBalanceValue} to be set.
     */
    public void setStressBalanceValue(final double stressBalance) {
        this.stressBalanceValue = stressBalance;
    }

    /**
     * Getter for entry's {@link #stressValue}.
     *
     * @return The entry's {@link #stressValue}.
     */
    public double getStressValue() {
        return stressValue;
    }

    /**
     * Setter for the entry's {@link #stressValue}.
     *
     * @param stressValue The {@link #stressValue} to be set.
     */
    public void setStressValue(final double stressValue) {
        this.stressValue = stressValue;
    }

    /**
     * Getter for entry's {@link #sleepValue}.
     *
     * @return The entry's {@link #sleepValue}.
     */
    public double getSleepValue() {
        return sleepValue;
    }

    /**
     * Setter for the entry's {@link #sleepValue}.
     *
     * @param sleepValue The {@link #sleepValue} to be set.
     */
    public void setSleepValue(final double sleepValue) {
        this.sleepValue = sleepValue;
    }

    /**
     * Getter for entry's {@link #sleepAnnotation}.
     *
     * @return The entry's {@link #sleepAnnotation}.
     */
    public List<String> getSleepAnnotation() {
        return sleepAnnotation;
    }

    /**
     * Setter for the entry's {@link #sleepAnnotation}.
     *
     * @param sleepAnnotation The {@link #sleepAnnotation} to be set.
     */
    public void setSleepAnnotation(final List<String> sleepAnnotation) {
        this.sleepAnnotation = sleepAnnotation;
    }

    /**
     * Adds an annotation to the entry's {@link #sleepAnnotation}.
     *
     * @param sleepAnnotation The annotation to be added.
     */
    public void addSleepAnnotation(final String sleepAnnotation) {
        this.sleepAnnotation.add(sleepAnnotation);
    }

    /**
     * Getter for entry's {@link #locationAnnotation}.
     *
     * @return The entry's {@link #locationAnnotation}.
     */
    public List<String> getLocationAnnotation() {
        return locationAnnotation;
    }

    /**
     * Setter for the entry's {@link #locationAnnotation}.
     *
     * @param locationAnnotation The {@link #locationAnnotation} to be set.
     */
    public void setLocationAnnotation(final List<String> locationAnnotation) {
        this.locationAnnotation = locationAnnotation;
    }

    /**
     * Adds an annotation to the entry's {@link #locationAnnotation}.
     *
     * @param locationAnnotation The annotation to be added.
     */
    public void addLocationAnnotation(final String locationAnnotation) {
        this.locationAnnotation.add(locationAnnotation);
    }

    /**
     * Getter for entry's {@link #mlCgmValue}.
     *
     * @return The entry's {@link #mlCgmValue}.
     */
    public double getMlCgmValue() {
        return mlCgmValue;
    }

    /**
     * Setter for the entry's {@link #mlCgmValue}.
     *
     * @param mlCgmValue The {@link #mlCgmValue} to be set.
     */
    public void setMlCgmValue(final double mlCgmValue) {
        this.mlCgmValue = mlCgmValue;
    }

    /**
     * Getter for entry's {@link #insulinSensitivityFactor}.
     *
     * @return The entry's {@link #insulinSensitivityFactor}.
     */
    public double getInsulinSensitivityFactor() {
        return insulinSensitivityFactor;
    }

    /**
     * Setter for the entry's {@link #insulinSensitivityFactor}.
     *
     * @param insulinSensitivityFactor The insulin sensitivity to be set.
     */
    public void setInsulinSensitivityFactor(final double insulinSensitivityFactor) {
        this.insulinSensitivityFactor = insulinSensitivityFactor;
    }

    /**
     * Getter for the {@link #mlAnnotation}.
     *
     * @return The entry's {@link #mlAnnotation}.
     */
    public List<String> getMlAnnotation() {
        return mlAnnotation;
    }

    /**
     * Setter for the {@link #mlAnnotation}.
     *
     * @param mlAnnotation The annotations to be set.
     */
    public void setMlAnnotation(final List<String> mlAnnotation) {
        this.mlAnnotation = mlAnnotation;
    }

    /**
     * Adds an annotation to the {@link #mlAnnotation}.
     *
     * @param mlAnnotation The annotation to be added.
     */
    public void addMlAnnotation(final String mlAnnotation) {
        this.mlAnnotation.add(mlAnnotation);
    }

    /**
     * Getter for the entry's {@link #otherAnnotation}.
     *
     * @return The entry's {@link #otherAnnotation}.
     */
    public List<String> getOtherAnnotation() {
        return otherAnnotation;
    }

    /**
     * Setter for the entry's {@link #otherAnnotation}.
     *
     * @param otherAnnotation The {@link #otherAnnotation} to be set.
     */
    public void setOtherAnnotation(final List<String> otherAnnotation) {
        this.otherAnnotation = otherAnnotation;
    }

    /**
     * Adds an annotation to the entry's {@link #otherAnnotation}.
     *
     * @param userAnnotation The annotation to be added.
     */
    public void addOtherAnnotation(final String userAnnotation) {
        this.otherAnnotation.add(userAnnotation);
    }

    /**
     * Getter for the {@link #weight}.
     *
     * @return The weight recorded in the entry.
     */
    public double getWeight() {
        return weight;
    }

    /**
     * Setter for the {@link #weight}.
     *
     * @param weight The weight recoreded in the entry.
     */
    public void setWeight(final double weight) {
        this.weight = weight;
    }

    /**
     * Checks whether the entry is empty.
     *
     * @return True if the VaultCSVEntry is empty, false otherwise.
     */
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
                && otherAnnotation.isEmpty()
                && weight == UNINITIALIZED_DOUBLE;
    }

    /**
     * Converts the VaultCSVEntry to a CSV string.
     *
     * @return The converted VaultCSVEntry.
     */
    public String toCsvString() {
        StringBuilder sb = new StringBuilder();

        String[] record = this.toCsvRecord();
        for (String item : record) {
            sb.append(item).append(CSV_DELIMITER);
        }
        sb.deleteCharAt(sb.length() - 1);

        return sb.toString();
    }

    /**
     * Converts the VaultCSVEntry to a CSV record.
     *
     * @return The converted VaultCSVEntry.
     */
    @Override
    public String[] toCsvRecord() {
        return toCsvRecord(DECIMAL_FORMAT);
    }

    /**
     * Converts the VaultCSVEntry to a CSV record.
     *
     * @param decimalFormat Format of decimals to be used in the CSV record.
     * @return The converted VaultCSVEntry.
     */
    public String[] toCsvRecord(final String decimalFormat) {
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
        if (weight > UNINITIALIZED_DOUBLE) {
            csvRecord.add(String.format(Locale.ENGLISH, decimalFormat, weight).replaceAll(",", ""));
        } else {
            csvRecord.add("");
        }

        return csvRecord.toArray(new String[] {});
    }

    /**
     * Gets the VaultCSVEntry as CSV header string.
     *
     * @return The VaultCSVEntry as CSV header.
     */
    public String getCsvHeaderString() {
        StringBuilder sb = new StringBuilder();

        String[] header = getCsvHeaderRecord();
        for (String item : header) {
            sb.append(item).append(CSV_DELIMITER);
        }
        sb.deleteCharAt(sb.length() - 1);

        return sb.toString();
    }

    /**
     * Gets the VaultCSVEntry as CSV header record.
     *
     * @return The VaultCSVEntry as CSV header.
     */
    @Override
    public String[] getCsvHeaderRecord() {
        return new String[] {
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
                "otherAnnotation",
                "weight"
        };
    }

    /**
     * Converts VaultCSVEntry to string.
     *
     * @return The VaultCSVEntry as string.
     */
    @Override
    public String toString() {
        return "VaultCsvEntry{" + "timestamp=" + timestamp + ", bgValue=" + bgValue + ", cgmValue=" + cgmValue + ", cgmRawValue="
                + cgmRawValue + ", cgmAlertValue=" + cgmAlertValue + ", pumpCgmPredictionValue=" + pumpCgmPredictionValue
                + ", glucoseAnnotation=" + glucoseAnnotation + ", basalValue=" + basalValue + ", basalAnnotation=" + basalAnnotation
                + ", bolusValue=" + bolusValue + ", bolusCalculationValue=" + bolusCalculationValue + ", bolusAnnotation=" + bolusAnnotation
                + ", mealValue=" + mealValue + ", pumpAnnotation=" + pumpAnnotation + ", exerciseTimeValue=" + exerciseTimeValue
                + ", exerciseAnnotation=" + exerciseAnnotation + ", heartRateValue=" + heartRateValue + ", stressBalanceValue="
                + stressBalanceValue + ", heartRateVariabilityValue=" + heartRateVariabilityValue + ", stressValue=" + stressValue
                + ", sleepValue=" + sleepValue + ", sleepAnnotation=" + sleepAnnotation + ", locationAnnotation=" + locationAnnotation
                + ", mlCgmValue=" + mlCgmValue + ", mlAnnotation=" + mlAnnotation + ", insulinSensitivityFactor=" + insulinSensitivityFactor
                + ", otherAnnotation=" + otherAnnotation + ", weight=" + weight + '}';
    }

}
