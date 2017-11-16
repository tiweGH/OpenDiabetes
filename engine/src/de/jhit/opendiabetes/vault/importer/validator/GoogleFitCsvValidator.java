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
package de.jhit.opendiabetes.vault.importer.validator;

import com.csvreader.CsvReader;
import de.jhit.opendiabetes.vault.util.TimestampUtils;
import java.io.File;
import java.io.IOException;
import java.text.ParseException;
import java.util.Date;

/**
 *
 * @author mswin
 */
public class GoogleFitCsvValidator extends CsvValidator {

    public static final String HEADER_START_TIME_DE = "Beginn";
    public static final String HEADER_END_TIME_DE = "Ende";
    public static final String HEADER_BIKE_VALUE_DE = "Radfahren – Dauer (ms)";
    public static final String HEADER_WALK_VALUE_DE = "Gehen – Dauer (ms)";
    public static final String HEADER_RUN_VALUE_DE = "Laufen – Dauer (ms)";
    public static final String HEADER_MAX_SPEED_VALUE_DE = "Maximale Geschwindigkeit (m/s)";
    public static final String TIME_FORMAT_DE = "yyyy-MM-dd HHmmss.SSSZ";        //01:00:00.000+01:00

    public static final String[] HEADER_DE = {
        HEADER_START_TIME_DE, HEADER_END_TIME_DE,
        // files without this values doesn't contain this headers ... --> json ?
        //        HEADER_BIKE_VALUE_DE,
        //        HEADER_WALK_VALUE_DE,
        //        HEADER_RUN_VALUE_DE,
        HEADER_MAX_SPEED_VALUE_DE
    };

    // TODO add english header
    public static final String[] HEADER_EN = {
        HEADER_START_TIME_DE, HEADER_END_TIME_DE,
        HEADER_BIKE_VALUE_DE,
        HEADER_WALK_VALUE_DE,
        HEADER_RUN_VALUE_DE,
        HEADER_MAX_SPEED_VALUE_DE
    };

    public GoogleFitCsvValidator() {
        super(HEADER_DE, HEADER_EN);
    }

    // TODO add language selection
    public long getBikeValue(CsvReader creader) throws IOException {
        String tmpValue = creader.get(HEADER_BIKE_VALUE_DE);
        if (tmpValue != null && !tmpValue.isEmpty()) {
            return Long.parseLong(tmpValue);
        } else {
            return 0;
        }
    }

    public long getWalkValue(CsvReader creader) throws IOException {
        String tmpValue = creader.get(HEADER_WALK_VALUE_DE);
        if (tmpValue != null && !tmpValue.isEmpty()) {
            return Long.parseLong(tmpValue);
        } else {
            return 0;
        }
    }

    public long getRunValue(CsvReader creader) throws IOException {
        String tmpValue = creader.get(HEADER_RUN_VALUE_DE);
        if (tmpValue != null && !tmpValue.isEmpty()) {
            return Long.parseLong(tmpValue);
        } else {
            return 0;
        }
    }

    public double getMaxSpeedValue(CsvReader creader) throws IOException {
        String tmpValue = creader.get(HEADER_MAX_SPEED_VALUE_DE);
        if (tmpValue != null && !tmpValue.isEmpty()) {
            return Double.parseDouble(tmpValue);
        } else {
            return 0;
        }
    }

    public long getStartTime(CsvReader creader, String fileName) throws IOException, ParseException {
        return timestampHack(creader.get(HEADER_START_TIME_DE), fileName);
    }

    public long getEndTime(CsvReader creader, String fileName) throws IOException, ParseException {
        return timestampHack(creader.get(HEADER_END_TIME_DE), fileName);
    }

    private long timestampHack(String dbString, String fileName) throws ParseException {
        if (dbString == null || dbString.isEmpty()) {
            return 0;
        }
        fileName = new File(fileName).getName();
        String dateString = fileName.split("\\.")[0]; //dirty hack --> change to json would solve this problem
        Date timestamp = TimestampUtils.createCleanTimestamp(
                dateString + " " + dbString.replaceAll(":", ""),
                TIME_FORMAT_DE);
        return timestamp.getTime();
    }
}
