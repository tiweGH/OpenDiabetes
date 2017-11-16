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
package de.jhit.opendiabetes.vault.importer;

import com.csvreader.CsvReader;
import de.jhit.opendiabetes.vault.container.MedtronicAltertCodes;
import de.jhit.opendiabetes.vault.container.MedtronicAnnotatedVaultEntry;
import de.jhit.opendiabetes.vault.container.VaultEntry;
import de.jhit.opendiabetes.vault.container.VaultEntryAnnotation;
import de.jhit.opendiabetes.vault.container.VaultEntryType;
import static de.jhit.opendiabetes.vault.importer.Importer.LOG;
import de.jhit.opendiabetes.vault.importer.validator.MedtronicCsvValidator;
import java.io.IOException;
import java.nio.charset.Charset;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 *
 * @author Jens
 */
public class MedtronicCsvImporter extends CsvImporter {
    
    private static final Pattern AMOUNT_PATTERN = Pattern.compile("(.*\\s)?AMOUNT=(\\d+([\\.,]\\d+)?).*", Pattern.CASE_INSENSITIVE);
    private static final Pattern ISIG_PATTERN = Pattern.compile("(.*\\s)?ISIG=(\\d+([\\.,]\\d+)?).*", Pattern.CASE_INSENSITIVE);
    private static final Pattern RATE_PATTERN = Pattern.compile("(.*\\s)?RATE=(\\d+([\\.,]\\d+)?).*", Pattern.CASE_INSENSITIVE);
    private static final Pattern CARB_INPUT_PATTERN = Pattern.compile("(.*\\s)?CARB_INPUT=(\\d+([\\.,]\\d+)?).*", Pattern.CASE_INSENSITIVE);
    private static final Pattern BG_INPUT_PATTERN = Pattern.compile("(.*\\s)?BG_INPUT=(\\d+([\\.,]\\d+)?).*", Pattern.CASE_INSENSITIVE);
    private static final Pattern DURATION_PATTERN = Pattern.compile("(.*\\s)?DURATION=(\\d+([\\.,]\\d+)?).*", Pattern.CASE_INSENSITIVE);
    private static final Pattern RAW_TYPE_PATTERN = Pattern.compile("(.*\\s)?RAW_TYPE=(\\d+([\\.,]\\d+)?).*", Pattern.CASE_INSENSITIVE);
    private static final Pattern ALARM_TYPE_PATTERN = Pattern.compile("(.*\\s)?ALARM_TYPE=(\\d+([\\.,]\\d+)?).*", Pattern.CASE_INSENSITIVE);
    private static final Pattern STATE_PATTERN = Pattern.compile("(.*\\s)?STATE=(\\w*).*", Pattern.CASE_INSENSITIVE);
    private static final Pattern PERCENT_OF_RATE_PATTERN = Pattern.compile("(.*\\s)?PERCENT_OF_RATE=(\\w*).*", Pattern.CASE_INSENSITIVE);
    private static final Pattern METER_PATTERN = Pattern.compile("(.*\\s)?METER_SERIAL_NUMBER=(\\w*).*", Pattern.CASE_INSENSITIVE);
    private static final Pattern CALIBRATION_BG_PATTERN = Pattern.compile("(.*\\s)?LAST_CAL_BG=(\\w*).*", Pattern.CASE_INSENSITIVE);
    private static final Pattern PREDICTION_PATTERN = Pattern.compile("(.*\\s)?PREDICTED_SENSOR_GLUCOSE_AMOUNT=(\\w*).*", Pattern.CASE_INSENSITIVE);
    
    public MedtronicCsvImporter(String importFilePath) {
        this(importFilePath, ',');
    }
    
    public MedtronicCsvImporter(String importFilePath, char delimiter) {
        super(importFilePath, new MedtronicCsvValidator(), delimiter);
    }
    
    private static VaultEntry extractDoubleEntry(Date timestamp, VaultEntryType type,
            String rawValues, Pattern pattern, String[] fullEntry) {
        if (rawValues != null && !rawValues.isEmpty()) {
            Matcher m = pattern.matcher(rawValues);
            if (m.matches()) {
                String matchedString = m.group(2).replace(",", ".");
                try {
                    double value = Double.parseDouble(matchedString);
                    return new VaultEntry(type,
                            timestamp,
                            value);
                } catch (NumberFormatException ex) {
                    LOG.log(Level.WARNING, "{0} -- Record: {1}",
                            new Object[]{ex.getMessage(), Arrays.toString(fullEntry)});
                }
            }
        }
        return null;
    }
    
    public static VaultEntry extractSecondValue(VaultEntry entry,
            String rawValues, Pattern pattern, String[] fullEntry) {
        if (rawValues != null && !rawValues.isEmpty() && entry != null) {
            Matcher m = pattern.matcher(rawValues);
            if (m.matches()) {
                String matchedString = m.group(2).replace(",", ".");
                try {
                    double value = Double.parseDouble(matchedString);
                    entry.setValue2(value);
                    return entry;
                } catch (NumberFormatException ex) {
                    LOG.log(Level.WARNING, "{0} -- Record: {1}",
                            new Object[]{ex.getMessage(), Arrays.toString(fullEntry)});
                }
            }
        }
        return null;
    }
    
    private static MedtronicAnnotatedVaultEntry annotateBasalEntry(
            VaultEntry oldEntry, String rawValues, MedtronicCsvValidator.TYPE rawType,
            String[] fullEntry) {
        if (rawValues != null && !rawValues.isEmpty() && oldEntry != null) {
            Matcher m = DURATION_PATTERN.matcher(rawValues);
            if (m.matches()) {
                String matchedString = m.group(2).replace(",", ".");
                try {
                    double value = Double.parseDouble(matchedString);
                    oldEntry.setValue2(value);
                    return new MedtronicAnnotatedVaultEntry(
                            oldEntry, rawType);
                } catch (NumberFormatException ex) {
                    LOG.log(Level.WARNING, "{0} -- Record: {1}",
                            new Object[]{ex.getMessage(), Arrays.toString(fullEntry)});
                }
            }
        }
        return null;
    }
    
    @Override
    protected void preprocessingIfNeeded(String filePath) {
        // test for delimiter
        CsvReader creader = null;
        try {
            // test for , delimiter
            creader = new CsvReader(filePath, ',', Charset.forName("UTF-8"));
            for (int i = 0; i < 15; i++) { // just scan the first 15 lines for a valid header
                if (creader.readHeaders()) {
                    if (validator.validateHeader(creader.getHeaders())) {
                        // found valid header --> finish
                        delimiter = ',';
                        creader.close();
                        LOG.log(Level.FINE, "Use ',' as delimiter for Carelink CSV: {0}", filePath);
                        return;
                    }
                }
            }
            // if you end up here there was no valid header within the range
            // try the other delimiter in normal operation
            delimiter = ';';
            LOG.log(Level.FINE, "Use ';' as delimiter for Carelink CSV: {0}", filePath);
            
        } catch (IOException ex) {
            LOG.log(Level.WARNING, "Error while parsing Careling CSV in delimiter check: "
                    + filePath, ex);
        } finally {
            if (creader != null) {
                creader.close();
            }
        }
    }
    
    @Override
    protected List<VaultEntry> parseEntry(CsvReader creader) throws Exception {
        List<VaultEntry> retVal = new ArrayList<>();
        MedtronicCsvValidator parseValidator = (MedtronicCsvValidator) validator;
        
        MedtronicCsvValidator.TYPE type = parseValidator.getCarelinkType(creader);
        if (type == null) {
            LOG.log(Level.FINER, "Ignore Type: {0}",
                    parseValidator.getCarelinkTypeString(creader));
            return null;
        }
        Date timestamp;
        try {
            timestamp = parseValidator.getTimestamp(creader);
        } catch (ParseException ex) {
            // maybe old format without good timestamp
            // try again with seperated fields
            timestamp = parseValidator.getManualTimestamp(creader);
        }
        if (timestamp == null) {
            return null;
        }
        String rawValues = parseValidator.getRawValues(creader);
        VaultEntry tmpEntry;
        
        switch (type) {
            case BASAL:
                tmpEntry = extractDoubleEntry(timestamp,
                        VaultEntryType.BASAL_PROFILE, rawValues,
                        RATE_PATTERN, creader.getValues());
                if (tmpEntry != null) {
                    retVal.add(tmpEntry);
                }
                break;
            case BASAL_TMP_PERCENT:
                tmpEntry = extractDoubleEntry(timestamp,
                        VaultEntryType.BASAL_MANUAL, rawValues,
                        PERCENT_OF_RATE_PATTERN, creader.getValues());
                if (tmpEntry != null) {
                    tmpEntry = annotateBasalEntry(tmpEntry, rawValues, type,
                            creader.getValues());
                    retVal.add(tmpEntry);
                }
                break;
            case BASAL_TMP_RATE:
                tmpEntry = extractDoubleEntry(timestamp,
                        VaultEntryType.BASAL_MANUAL, rawValues,
                        RATE_PATTERN, creader.getValues());
                if (tmpEntry != null) {
                    tmpEntry = annotateBasalEntry(tmpEntry, rawValues, type,
                            creader.getValues());
                    retVal.add(tmpEntry);
                }
                break;
            case BG_CAPTURED_ON_PUMP:
                tmpEntry = extractDoubleEntry(timestamp,
                        VaultEntryType.GLUCOSE_BG_MANUAL, rawValues,
                        AMOUNT_PATTERN, creader.getValues());
                if (tmpEntry != null) {
                    // check if it was received by a meter (new format doesn't use BG_RECEIVED)
                    Matcher m = METER_PATTERN.matcher(rawValues);
                    if (m.matches()) {
                        String meterSerial = m.group(2);
                        if (!meterSerial.isEmpty()) {
                            VaultEntryAnnotation annotation
                                    = new VaultEntryAnnotation(
                                            VaultEntryAnnotation.TYPE.GLUCOSE_BG_METER_SERIAL);
                            annotation.setValue(meterSerial);
                            tmpEntry.addAnnotation(annotation);
                            tmpEntry.setType(VaultEntryType.GLUCOSE_BG);
                        }
                    }
                    retVal.add(tmpEntry);
                }
                break;
            case BG_RECEIVED:
                tmpEntry = extractDoubleEntry(timestamp,
                        VaultEntryType.GLUCOSE_BG, rawValues,
                        AMOUNT_PATTERN, creader.getValues());
                if (tmpEntry != null) {
                    retVal.add(tmpEntry);
                }
                break;
            case BOLUS_WIZARD:
                // meal information
                tmpEntry = extractDoubleEntry(timestamp,
                        VaultEntryType.MEAL_BOLUS_CALCULATOR, rawValues,
                        CARB_INPUT_PATTERN, creader.getValues());
                if (tmpEntry != null && tmpEntry.getValue() > 0.0) {
                    retVal.add(tmpEntry);
                }
                
                tmpEntry = extractDoubleEntry(timestamp,
                        VaultEntryType.GLUCOSE_BOLUS_CALCULATION, rawValues,
                        BG_INPUT_PATTERN, creader.getValues());
                if (tmpEntry != null && tmpEntry.getValue() > 0.0) {
                    retVal.add(tmpEntry);
                }
                break;
            case BOLUS_NORMAL:
                tmpEntry = extractDoubleEntry(timestamp,
                        VaultEntryType.BOLUS_NORMAL, rawValues,
                        AMOUNT_PATTERN, creader.getValues());
                if (tmpEntry != null) {
                    retVal.add(tmpEntry);
                }
                break;
            case BOLUS_SQUARE:
                tmpEntry = extractDoubleEntry(timestamp,
                        VaultEntryType.BOLUS_SQARE, rawValues,
                        AMOUNT_PATTERN, creader.getValues());
                if (tmpEntry != null) {
                    tmpEntry = extractSecondValue(tmpEntry, rawValues,
                            DURATION_PATTERN, creader.getValues());
                    if (tmpEntry != null) {
                        tmpEntry.setValue2(tmpEntry.getValue2() / 1000);
                        retVal.add(tmpEntry);
                    }
                }
                break;
            case EXERCICE:
                tmpEntry = extractDoubleEntry(timestamp,
                        VaultEntryType.EXERCISE_MANUAL, rawValues,
                        DURATION_PATTERN, creader.getValues());
                
                if (tmpEntry != null) {
                    retVal.add(tmpEntry);
                } else {
                    // add marker without duration for old pumps
                    retVal.add(new VaultEntry(VaultEntryType.EXERCISE_MANUAL,
                            timestamp, VaultEntry.VALUE_UNUSED));
                }
                break;
            case PRIME:
                tmpEntry = extractDoubleEntry(timestamp,
                        VaultEntryType.PUMP_PRIME, rawValues,
                        AMOUNT_PATTERN, creader.getValues());
                if (tmpEntry != null) {
                    retVal.add(tmpEntry);
                }
                
                break;
            case PUMP_ALERT:
            case PUMP_ALERT_NGP:
                tmpEntry = extractDoubleEntry(timestamp,
                        VaultEntryType.PUMP_NO_DELIVERY, rawValues,
                        RAW_TYPE_PATTERN, creader.getValues());
                
                if (tmpEntry != null) {
                    MedtronicAltertCodes codeObj = MedtronicAltertCodes.fromCode(
                            (int) Math.round(tmpEntry.getValue()));
                    String codeString = codeObj == MedtronicAltertCodes.UNKNOWN_ALERT
                            ? String.valueOf(Math.round(tmpEntry.getValue()))
                            : codeObj.toString();
                    
                    switch (codeObj) {
                        case NO_DELIVERY:
                            // already done
                            break;
                        case SUSPEND_ON_LOW:
                        case SUSPEND_BEVORE_LOW:
                            VaultEntry extraTmpEntry = new VaultEntry(VaultEntryType.PUMP_AUTONOMOUS_SUSPEND, timestamp);
                            retVal.add(extraTmpEntry);
                        case LOW_WHEN_SUSPENDED:
                        case LOW:
                        case RISE_ALERT:
                        case UNSUSPEND_AFTER_LOW_PROTECTION:
                        case UNSUSPEND_AFTER_LOW_PROTECTION_MAX_TIMESPAN:
                        case HIGH:
                        case APPROACHING_HIGH:
                            tmpEntry.setType(VaultEntryType.GLUCOSE_CGM_ALERT);
                            tmpEntry.setValue(VaultEntry.VALUE_UNUSED); // mark as unused to inform interpreter to add a BG value
                            tmpEntry.addAnnotation(new VaultEntryAnnotation(codeString,
                                    VaultEntryAnnotation.TYPE.PUMP_INFORMATION_CODE));
                            break;
                        case NO_SENSOR_CONNECTION:
                            tmpEntry.setType(VaultEntryType.CGM_CONNECTION_ERROR);
                            tmpEntry.setValue(VaultEntry.VALUE_UNUSED);
                            break;
                        case CALIBRATION_ERROR:
                            tmpEntry.setType(VaultEntryType.CGM_CALIBRATION_ERROR);
                            tmpEntry.setValue(VaultEntry.VALUE_UNUSED);
                            break;
                        case EMPTY_RESERVOIR:
                            tmpEntry.setType(VaultEntryType.PUMP_RESERVOIR_EMPTY);
                            tmpEntry.setValue(VaultEntry.VALUE_UNUSED);
                            break;
                        case INSULIN_FLOW_BLOCKED:
                            tmpEntry.setType(VaultEntryType.PUMP_NO_DELIVERY);
                            tmpEntry.setValue(VaultEntry.VALUE_UNUSED);
                            tmpEntry.addAnnotation(new VaultEntryAnnotation(codeString,
                                    VaultEntryAnnotation.TYPE.PUMP_ERROR_CODE));
                            break;
                        case SENSOR_EXPIRED:
                        case SENSOR_FINISHED:
                            tmpEntry.setType(VaultEntryType.CGM_SENSOR_FINISHED);
                            tmpEntry.setValue(VaultEntry.VALUE_UNUSED);
                            break;
                        case SENSOR_INITIALIZATIN_STARTED:
                            tmpEntry.setType(VaultEntryType.CGM_SENSOR_START);
                            tmpEntry.setValue(VaultEntry.VALUE_UNUSED);
                            break;
                        default:
                            tmpEntry.setType(VaultEntryType.PUMP_UNTRACKED_ERROR);
                            tmpEntry.setValue(VaultEntry.VALUE_UNUSED);
                            tmpEntry.addAnnotation(new VaultEntryAnnotation(codeString,
                                    VaultEntryAnnotation.TYPE.PUMP_ERROR_CODE));
                            break;
                    }
                    
                    retVal.add(tmpEntry);
                }
                
                break;
            case PUMP_SUSPEND_CHANGED:
                if (rawValues != null && !rawValues.isEmpty()) {
                    Matcher m = STATE_PATTERN.matcher(rawValues);
                    if (m.matches()) {
                        String matchedString = m.group(2);
                        VaultEntryType entryType;
                        if (matchedString.contains("suspend")
                                || matchedString.contains("predicted_low_sg")
                                || matchedString.contains("lowsg_suspend")) {
                            entryType = VaultEntryType.PUMP_SUSPEND;
                        } else if (matchedString.contains("normal")) {
                            entryType = VaultEntryType.PUMP_UNSUSPEND;
                        } else {
                            entryType = VaultEntryType.PUMP_UNTRACKED_ERROR;
                        }
                        tmpEntry = new VaultEntry(entryType,
                                timestamp,
                                VaultEntry.VALUE_UNUSED);
                        retVal.add(tmpEntry);
                    }
                }
                
                break;
            case PUMP_TYME_SYNC:
                retVal.add(new VaultEntry(VaultEntryType.PUMP_TIME_SYNC,
                        timestamp,
                        VaultEntry.VALUE_UNUSED));
                break;
            case REWIND:
                retVal.add(new VaultEntry(VaultEntryType.PUMP_REWIND, timestamp,
                        VaultEntry.VALUE_UNUSED));
                break;
            case SENSOR_ALERT:
                tmpEntry = extractDoubleEntry(timestamp,
                        VaultEntryType.GLUCOSE_CGM_ALERT, rawValues,
                        AMOUNT_PATTERN, creader.getValues());
                if (tmpEntry != null) {
                    // check if it is really a cgm-bg-alert
                    Matcher m = ALARM_TYPE_PATTERN.matcher(rawValues);
                    if (m.matches()) {
                        if (m.group(2).equalsIgnoreCase("102")
                                || m.group(2).equalsIgnoreCase("101")) {
                            retVal.add(tmpEntry);
                        }
                    }
                }
                
                break;
            case SENSOR_CAL_BG:
                tmpEntry = extractDoubleEntry(timestamp,
                        VaultEntryType.GLUCOSE_CGM_CALIBRATION, rawValues,
                        AMOUNT_PATTERN, creader.getValues());
                if (tmpEntry != null) {
                    tmpEntry.addAnnotation(new VaultEntryAnnotation(VaultEntryAnnotation.TYPE.CGM_VENDOR_MEDTRONIC));
                    retVal.add(tmpEntry);
                }
                
                break;
            case SENSOR_CAL_FACTOR:
                // new data format for calibration
                tmpEntry = extractDoubleEntry(timestamp,
                        VaultEntryType.GLUCOSE_CGM_CALIBRATION, rawValues,
                        CALIBRATION_BG_PATTERN, creader.getValues());
                if (tmpEntry != null) {
                    retVal.add(tmpEntry);
                }
                
                break;
            case SENSOR_VALUE:
                // calibrated cgm value
                tmpEntry = extractDoubleEntry(timestamp,
                        VaultEntryType.GLUCOSE_CGM, rawValues,
                        AMOUNT_PATTERN, creader.getValues());
                if (tmpEntry != null) {
                    retVal.add(tmpEntry);
                }

                // measured raw value
                tmpEntry = extractDoubleEntry(timestamp,
                        VaultEntryType.GLUCOSE_CGM_RAW, rawValues,
                        ISIG_PATTERN, creader.getValues());
                if (tmpEntry != null) {
                    retVal.add(tmpEntry);
                }

                // sensor value prediction
                tmpEntry = extractDoubleEntry(timestamp,
                        VaultEntryType.PUMP_CGM_PREDICTION, rawValues,
                        PREDICTION_PATTERN, creader.getValues());
                if (tmpEntry != null && tmpEntry.getValue() > 20.0) {
                    retVal.add(tmpEntry);
                }
                
                break;
            default:
                Logger.getLogger(this.getClass().getName()).severe("ASSERTION ERROR!");
                throw new AssertionError();
        }
        
        return retVal;
    }
    
}
