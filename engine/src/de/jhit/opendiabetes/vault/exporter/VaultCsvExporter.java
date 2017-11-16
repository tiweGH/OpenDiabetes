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
package de.jhit.opendiabetes.vault.exporter;

import de.jhit.opendiabetes.vault.container.VaultEntry;
import de.jhit.opendiabetes.vault.container.VaultEntryAnnotation;
import de.jhit.opendiabetes.vault.container.csv.ExportEntry;
import de.jhit.opendiabetes.vault.container.csv.VaultCsvEntry;
import de.jhit.opendiabetes.vault.data.VaultDao;
import de.jhit.opendiabetes.vault.util.EasyFormatter;
import de.jhit.opendiabetes.vault.util.TimestampUtils;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.logging.Level;

/**
 *
 * @author Jens
 */
public class VaultCsvExporter extends CsvFileExporter {

    private final VaultDao db;
    private List<VaultEntry> delayBuffer = new ArrayList<>();

    public VaultCsvExporter(ExporterOptions options, VaultDao db, String filePath) {
        super(options, filePath);
        this.db = db;
    }

    public List<VaultEntry> queryData() {
        List<VaultEntry> entrys;

        // query entrys
        if (options.isImportPeriodRestricted) {
            entrys = db.queryVaultEntrysBetween(options.exportPeriodFrom,
                    options.exportPeriodTo);
        } else {
            entrys = db.queryAllVaultEntrys();
        }

        return entrys;
    }

    @Override
    protected List<ExportEntry> prepareData(List<VaultEntry> data) {
        List<ExportEntry> returnValues = new ArrayList<>();

        List<VaultEntry> tmpValues = queryData();
        if (tmpValues == null || tmpValues.isEmpty()) {
            return null;
        }

        // list is ordered by timestamp from database (or should be ordered otherwise)
        Date fromTimestamp = tmpValues.get(0).getTimestamp();
        Date toTimestamp = tmpValues.get(tmpValues.size() - 1).getTimestamp();

        if (!tmpValues.isEmpty()) {
            int i = 0;
            delayBuffer = new ArrayList<>();
            while (!fromTimestamp.after(toTimestamp)) {
                // start new timeslot (1m slots)
                VaultCsvEntry tmpCsvEntry = new VaultCsvEntry();
                tmpCsvEntry.setTimestamp(fromTimestamp);

                // add delayed items
                if (!delayBuffer.isEmpty()) {
                    // need to copy the buffer since the loop may create new entries
                    VaultEntry[] delayedEntries = delayBuffer.toArray(new VaultEntry[]{});
                    delayBuffer.clear();
                    for (VaultEntry delayedItem : delayedEntries) {
                        tmpCsvEntry = processVaultEntry(tmpCsvEntry, delayedItem);

                    }
                }

                // search and add vault entries for this time slot
                VaultEntry tmpEntry;
                while (fromTimestamp.equals((tmpEntry = tmpValues.get(i)).getTimestamp())) {
                    if (i < tmpValues.size() - 1) {
                        i++;
                    } else {
                        i--;
                        break;
                    }
                    tmpCsvEntry = processVaultEntry(tmpCsvEntry, tmpEntry);
                }

                // save entry if not empty
                if (!tmpCsvEntry.isEmpty()) {
                    returnValues.add(tmpCsvEntry);
                    LOG.log(Level.FINE, "Export entry: {0}", tmpCsvEntry.toCsvString());
                }

                // add 1 minute to timestamp for next timeslot
                fromTimestamp = TimestampUtils.addMinutesToTimestamp(fromTimestamp, 1);
            }
        }
        return returnValues;
    }

    private VaultCsvEntry processVaultEntry(VaultCsvEntry tmpCsvEntry, VaultEntry tmpEntry) {
        switch (tmpEntry.getType()) {
            case GLUCOSE_CGM_ALERT:
                tmpCsvEntry.setCgmAlertValue(tmpEntry.getValue());
                break;
            case GLUCOSE_CGM:
                // TODO y does this happen
                // --> when more than one cgm value per minute is available
                // but cgm ticks are every 15 minutes ...
                if (tmpCsvEntry.getCgmValue()
                        == VaultCsvEntry.UNINITIALIZED_DOUBLE) {
                    tmpCsvEntry.setCgmValue(tmpEntry.getValue());
                } else {
                    LOG.log(Level.WARNING,
                            "Found multiple CGM Value for timepoint {0}, Drop {1}, hold: {2}",
                            new Object[]{tmpEntry.getTimestamp().toString(),
                                tmpEntry.toString(),
                                tmpCsvEntry.toString()});
                }
                break;
            case GLUCOSE_CGM_CALIBRATION:
                tmpCsvEntry.addGlucoseAnnotation(tmpEntry.getType().toString()
                        + "="
                        + EasyFormatter.formatDouble(tmpEntry.getValue()));
                break;
            case GLUCOSE_CGM_RAW:
                tmpCsvEntry.setCgmRawValue(tmpEntry.getValue());
                break;
            case GLUCOSE_BG:
            case GLUCOSE_BG_MANUAL:
                // TODO why does this happen ?
                // it often happens with identical values, but db has been cleaned bevore ...
                if (tmpCsvEntry.getBgValue()
                        == VaultCsvEntry.UNINITIALIZED_DOUBLE) {
                    tmpCsvEntry.setBgValue(tmpEntry.getValue());
                    tmpCsvEntry.addGlucoseAnnotation(tmpEntry.getType().toString());
                } else {
                    LOG.log(Level.WARNING,
                            "Found multiple BG Value for timepoint {0}, Drop {1}, hold: {2}",
                            new Object[]{tmpEntry.getTimestamp().toString(),
                                tmpEntry.toString(),
                                tmpCsvEntry.toString()});
                }
                break;
            case GLUCOSE_BOLUS_CALCULATION:
                tmpCsvEntry.setBolusCalculationValue(tmpEntry.getValue());
                break;
            case GLUCOSE_ELEVATION_30:
                tmpCsvEntry.addGlucoseAnnotation(tmpEntry.getType().toString()
                        + "="
                        + EasyFormatter.formatDouble(tmpEntry.getValue()));
                break;
            case CGM_CALIBRATION_ERROR:
            case CGM_SENSOR_FINISHED:
            case CGM_SENSOR_START:
            case CGM_CONNECTION_ERROR:
            case CGM_TIME_SYNC:
                tmpCsvEntry.addGlucoseAnnotation(tmpEntry.getType().toString());
                break;
            case BASAL_MANUAL:
            case BASAL_PROFILE:
            case BASAL_INTERPRETER:
                tmpCsvEntry.setBasalValue(tmpEntry.getValue());
                tmpCsvEntry.addBasalAnnotation(tmpEntry.getType().toString());
                break;
            case BOLUS_SQARE:
                if (tmpCsvEntry.getBolusValue() != VaultCsvEntry.UNINITIALIZED_DOUBLE) {
                    // delay entry if bolus is already set for this timeslot
                    delayBuffer.add(tmpEntry);
                    LOG.log(Level.INFO, "Delayed bolus entry: {0}", tmpEntry.toString());
                    break;
                }
                tmpCsvEntry.setBolusValue(tmpEntry.getValue());
                tmpCsvEntry.addBolusAnnotation(
                        tmpEntry.getType().toString()
                        + "="
                        + EasyFormatter.formatDouble(tmpEntry.getValue2()));
                break;
            case BOLUS_NORMAL:
                if (tmpCsvEntry.getBolusValue() != VaultCsvEntry.UNINITIALIZED_DOUBLE) {
                    // delay entry if bolus is already set for this timeslot
                    delayBuffer.add(tmpEntry);
                    LOG.log(Level.INFO, "Delayed bolus entry: {0}", tmpEntry.toString());
                    break;
                }
                tmpCsvEntry.setBolusValue(tmpEntry.getValue());
                tmpCsvEntry.addBolusAnnotation(
                        tmpEntry.getType().toString());
                break;
            case MEAL_BOLUS_CALCULATOR:
            case MEAL_MANUAL:
                tmpCsvEntry.setMealValue(tmpEntry.getValue());
                break;
            case EXERCISE_BICYCLE:
            case EXERCISE_WALK:
            case EXERCISE_RUN:
            case EXERCISE_MANUAL:
            case EXERCISE_OTHER:
                tmpCsvEntry.setExerciseTimeValue(tmpEntry.getValue());
                tmpCsvEntry.addExerciseAnnotation(tmpEntry.getType().toString());
                for (VaultEntryAnnotation item : tmpEntry.getAnnotations()) {
                    tmpCsvEntry.addExerciseAnnotation(item.toStringWithValue());
                }
                break;
            case PUMP_FILL:
            case PUMP_FILL_INTERPRETER:
            case PUMP_NO_DELIVERY:
            case PUMP_REWIND:
            case PUMP_UNTRACKED_ERROR:
            case PUMP_SUSPEND:
            case PUMP_UNSUSPEND:
            case PUMP_AUTONOMOUS_SUSPEND:
            case PUMP_RESERVOIR_EMPTY:
            case PUMP_TIME_SYNC:
                tmpCsvEntry.addPumpAnnotation(tmpEntry.getType().toString());
                break;
            case PUMP_PRIME:
                tmpCsvEntry.addPumpAnnotation(tmpEntry.getType().toString()
                        + "="
                        + EasyFormatter.formatDouble(tmpEntry.getValue()));
                break;
            case PUMP_CGM_PREDICTION:
                tmpCsvEntry.setPumpCgmPredictionValue(tmpEntry.getValue());
                break;
            case HEART_RATE:
                tmpCsvEntry.setHeartRateValue(tmpEntry.getValue());
                break;
            case HEART_RATE_VARIABILITY:
                tmpCsvEntry.setHeartRateVariabilityValue(tmpEntry.getValue());
                tmpCsvEntry.setStressBalanceValue(tmpEntry.getValue2());
                break;
            case STRESS:
                tmpCsvEntry.setStressValue(tmpEntry.getValue());
                break;
            case LOC_FOOD:
            case LOC_HOME:
            case LOC_OTHER:
            case LOC_SPORTS:
            case LOC_TRANSISTION:
            case LOC_WORK:
                tmpCsvEntry.addLocationAnnotation(tmpEntry.getType().toString());
                break;
            case SLEEP_DEEP:
            case SLEEP_LIGHT:
            case SLEEP_REM:
                tmpCsvEntry.addSleepAnnotation(tmpEntry.getType().toString());
                tmpCsvEntry.setSleepValue(tmpEntry.getValue());
                break;
            case ML_CGM_PREDICTION:
                tmpCsvEntry.setMlCgmValue(tmpEntry.getValue());
                break;
            case DM_INSULIN_SENSITIVTY:
                tmpCsvEntry.setInsulinSensitivityFactor(tmpEntry.getValue());
                break;
            case OTHER_ANNOTATION:
                // will be handled by annotations
                break;
            default:
                LOG.severe("TYPE ASSERTION ERROR!");
                throw new AssertionError();
        }

        if (!tmpEntry.getAnnotations().isEmpty()) {
            for (VaultEntryAnnotation annotation : tmpEntry.getAnnotations()) {
                switch (annotation.getType()) {
                    case GLUCOSE_BG_METER_SERIAL:
                    case GLUCOSE_RISE_20_MIN:
                    case GLUCOSE_RISE_LAST:
                        tmpCsvEntry.addGlucoseAnnotation(annotation.toStringWithValue());
                        break;
                    case EXERCISE_GoogleBicycle:
                    case EXERCISE_GoogleRun:
                    case EXERCISE_GoogleWalk:
                    case EXERCISE_TrackerBicycle:
                    case EXERCISE_TrackerRun:
                    case EXERCISE_TrackerWalk:
                    case EXERCISE_AUTOMATIC_OTHER:
                        tmpCsvEntry.addExerciseAnnotation(annotation.toStringWithValue());
                        break;
                    case ML_PREDICTION_TIME_BUCKET_SIZE:
                        tmpCsvEntry.addMlAnnotation(annotation.toStringWithValue());
                        break;
                    case PUMP_ERROR_CODE:
                    case PUMP_INFORMATION_CODE:
                        tmpCsvEntry.addPumpAnnotation(annotation.toStringWithValue());
                        break;
                    case CGM_VENDOR_DEXCOM:
                    case CGM_VENDOR_LIBRE:
                    case CGM_VENDOR_MEDTRONIC:
                        tmpCsvEntry.addGlucoseAnnotation(annotation.toStringWithValue());
                        break;
                    case USER_TEXT:
                        tmpCsvEntry.addOtherAnnotation(annotation.toStringWithValue());
                    default:
                        LOG.severe("ANNOTATION ASSERTION ERROR!");
                        throw new AssertionError();
                }
            }
        }
        return tmpCsvEntry;
    }

}
