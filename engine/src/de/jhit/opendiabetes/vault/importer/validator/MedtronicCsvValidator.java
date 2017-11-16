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
package de.jhit.opendiabetes.vault.importer.validator;

import com.csvreader.CsvReader;
import de.jhit.opendiabetes.vault.util.TimestampUtils;
import java.io.IOException;
import java.text.ParseException;
import java.util.Date;
import java.util.logging.Logger;

/**
 *
 * @author juehv
 */
public class MedtronicCsvValidator extends CsvValidator {

    public static final String CARELINK_HEADER_DE_DATE = "Datum";
    public static final String CARELINK_HEADER_DE_TIME = "Zeit";
    public static final String CARELINK_HEADER_DE_TIMESTAMP = "Zeitstempel";
    public static final String CARELINK_HEADER_DE_TYPE = "Roh-Typ";
    public static final String CARELINK_HEADER_DE_VALUE = "Roh-Werte";
    public static final String CARELINK_HEADER_DE_SEQ_NUM = "Roh-Seq Num";
    public static final String TIME_FORMAT_DE = "dd.MM.yy HH:mm:ss";

    public static final String[] CARELINK_HEADER_DE = {
        CARELINK_HEADER_DE_DATE, CARELINK_HEADER_DE_TIME,
        CARELINK_HEADER_DE_TIMESTAMP, CARELINK_HEADER_DE_TYPE,
        CARELINK_HEADER_DE_VALUE
    };

    public static enum TYPE {
        REWIND("Rewind"), PRIME("Prime"),
        EXERCICE("JournalEntryExerciseMarker"),
        BG_CAPTURED_ON_PUMP("BGCapturedOnPump"), BG_RECEIVED("BGReceived"),
        SENSOR_CAL_BG("SensorCalBG"), SENSOR_CAL_FACTOR("SensorCalFactor"),
        SENSOR_VALUE("GlucoseSensorData"), SENSOR_ALERT("AlarmSensor"),
        BOLUS_WIZARD("BolusWizardBolusEstimate"), BOLUS_NORMAL("BolusNormal"),
        BOLUS_SQUARE("BolusSquare"),
        BASAL("BasalProfileStart"), BASAL_TMP_PERCENT("ChangeTempBasalPercent"),
        BASAL_TMP_RATE("ChangeTempBasal"),
        PUMP_ALERT("AlarmPump"), PUMP_SUSPEND_CHANGED("ChangeSuspendState"),
        PUMP_ALERT_NGP("AlarmPumpNGP"), PUMP_TYME_SYNC("ChangeTime");

        final String name;

        TYPE(String name) {
            this.name = name;
        }

        static TYPE fromString(String typeString) {
            if (typeString != null && !typeString.isEmpty()) {
                for (TYPE item : TYPE.values()) {
                    if (item.name.equalsIgnoreCase(typeString)) {
                        return item;
                    }
                }
            }
            return null;
        }
    }

    public MedtronicCsvValidator() {
        //TODO add english header
        super(CARELINK_HEADER_DE, CARELINK_HEADER_DE);
    }

    public String getRawValues(CsvReader creader) throws IOException {
        switch (languageSelection) {
            case DE:
                return creader.get(CARELINK_HEADER_DE_VALUE);
            case EN:
                throw new UnsupportedOperationException("Not supported yet.");
            default:
                Logger.getLogger(this.getClass().getName()).severe("ASSERTION ERROR!");
                throw new AssertionError();
        }
    }

    public String getRawSeqNum(CsvReader creader) throws IOException {
        switch (languageSelection) {
            case DE:
                return creader.get(CARELINK_HEADER_DE_SEQ_NUM);
            case EN:
                throw new UnsupportedOperationException("Not supported yet.");
            default:
                Logger.getLogger(this.getClass().getName()).severe("ASSERTION ERROR!");
                throw new AssertionError();
        }
    }

    public String getCarelinkTypeString(CsvReader creader) throws IOException {
        switch (languageSelection) {
            case DE:
                return creader.get(CARELINK_HEADER_DE_TYPE).trim();
            case EN:
                throw new UnsupportedOperationException("Not supported yet.");
            default:
                Logger.getLogger(this.getClass().getName()).severe("ASSERTION ERROR!");
                throw new AssertionError();
        }
    }

    public TYPE getCarelinkType(CsvReader creader) throws IOException {
        return TYPE.fromString(getCarelinkTypeString(creader));
    }

    public Date getTimestamp(CsvReader creader) throws IOException, ParseException {
        switch (languageSelection) {
            case DE:
                String timeString = creader.get(CARELINK_HEADER_DE_TIMESTAMP).trim();
                return TimestampUtils.createCleanTimestamp(timeString, TIME_FORMAT_DE);
            case EN:
                throw new UnsupportedOperationException("Not supported yet.");
            default:
                Logger.getLogger(this.getClass().getName()).severe("ASSERTION ERROR!");
                throw new AssertionError();
        }
    }

    public Date getManualTimestamp(CsvReader creader) throws IOException, ParseException {
        switch (languageSelection) {
            case DE:
                String timeString1 = creader.get(CARELINK_HEADER_DE_DATE).trim();
                String timeString2 = creader.get(CARELINK_HEADER_DE_TIME).trim();
                return TimestampUtils.createCleanTimestamp(
                        timeString1 + " " + timeString2, TIME_FORMAT_DE);
            case EN:
                throw new UnsupportedOperationException("Not supported yet.");
            default:
                Logger.getLogger(this.getClass().getName()).severe("ASSERTION ERROR!");
                throw new AssertionError();
        }
    }

}
