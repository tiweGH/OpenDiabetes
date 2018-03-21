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
package de.opendiabetes.vault.plugin.importer.googlefit;

import com.csvreader.CsvReader;
import de.opendiabetes.vault.plugin.importer.validator.CSVValidator;
import de.opendiabetes.vault.plugin.util.TimestampUtils;

import java.io.File;
import java.io.IOException;
import java.text.ParseException;
import java.util.Date;
import java.util.logging.Logger;

/**
 * Validator for Google Fit CSV data.
 *
 * @author Jens Heuschkel
 */
public class GoogleFitCSVValidator extends CSVValidator {


    /**
     * Field of the German Google Fit CSV header containing the start time of an activity.
     */
    private static final String HEADER_START_TIME_DE = "Beginn";
    /**
     * Field of the German Google Fit CSV header containing the end time of an activity.
     */
    private static final String HEADER_END_TIME_DE = "Ende";
    /**
     * Field of the German Google Fit CSV header containing biking duration.
     */
    private static final String HEADER_BIKE_VALUE_DE = "Radfahren – Dauer (ms)";
    /**
     * Field of the German Google Fit CSV header containing walking duration.
     */
    private static final String HEADER_WALK_VALUE_DE = "Gehen – Dauer (ms)";
    /**
     * Field of the German Google Fit CSV header containing running duration.
     */
    private static final String HEADER_RUN_VALUE_DE = "Laufen – Dauer (ms)";
    /**
     * Field of the German Google Fit CSV header containing maximum velocity.
     */
    private static final String HEADER_MAX_SPEED_VALUE_DE = "Maximale Geschwindigkeit (m/s)";
    /**
     * Time format used in German Google Fit CSV data.
     */
    private static final String TIME_FORMAT_DE = "yyyy-MM-dd HHmmss.SSSZ";        //01:00:00.000+01:00

    /**
     * The composed German header used in Google Fit CSV data.
     */
    private static final String[] HEADER_DE = {
            HEADER_START_TIME_DE, HEADER_END_TIME_DE,
            // Files without these values don't contain these headers --> json ?
            //        HEADER_BIKE_VALUE_DE,
            //        HEADER_WALK_VALUE_DE,
            //        HEADER_RUN_VALUE_DE,
            HEADER_MAX_SPEED_VALUE_DE
    };

    /**
     * Field of the English Google Fit CSV header containing the start time of an activity.
     */
    private static final String HEADER_START_TIME_EN = "Start time";
    /**
     * Field of the English Google Fit CSV header containing the end time of an activity.
     */
    private static final String HEADER_END_TIME_EN = "End time";
    /**
     * Field of the English Google Fit CSV header containing biking duration.
     */
    private static final String HEADER_BIKE_VALUE_EN = "Cycling duration (ms)";
    /**
     * Field of the English Google Fit CSV header containing walking duration.
     */
    private static final String HEADER_WALK_VALUE_EN = "Walking duration (ms)";
    /**
     * Field of the English Google Fit CSV header containing running duration.
     */
    private static final String HEADER_RUN_VALUE_EN = "Running duration (ms)";
    /**
     * Field of the English Google Fit CSV header containing maximum velocity.
     */
    private static final String HEADER_MAX_SPEED_VALUE_EN = "Max speed (m/s)";
    /**
     * Time format used in English Google Fit CSV data.
     */
    private static final String TIME_FORMAT_EN = "yyyy-MM-dd HHmmss.SSSZ";

    /**
     * The composed English header used in Google fit CSV data.
     */

    private static final String[] HEADER_EN = {
            HEADER_START_TIME_EN, HEADER_END_TIME_EN,
            // Files without these values don't contain these headers ... --> json ?
            //        HEADER_BIKE_VALUE_EN,
            //        HEADER_WALK_VALUE_EN,
            //        HEADER_RUN_VALUE_EN,
            HEADER_MAX_SPEED_VALUE_EN
    };

    /**
     * Constructor.
     */
    public GoogleFitCSVValidator() {
        super(HEADER_DE, HEADER_EN);
    }

    /**
     * Getter for the biking value.
     *
     * @param creader The CSV reader to use.
     * @return The biking value.
     * @throws IOException Thrown when reading the data goes wrong.
     */
    public long getBikeValue(final CsvReader creader) throws IOException {
        Language language = getLanguageSelection();
        String tmpValue;
        switch (language) {
            case DE:
                tmpValue = creader.get(HEADER_BIKE_VALUE_DE);
                break;
            case EN:
                tmpValue = creader.get(HEADER_BIKE_VALUE_EN);
                break;
            default:
                Logger.getLogger(this.getClass().getName()).severe("ASSERTION ERROR!");
                throw new AssertionError();
        }

        if (tmpValue != null && !tmpValue.isEmpty()) {
            return Long.parseLong(tmpValue);
        } else {
            return 0;
        }
    }

    /**
     * Getter for the walking value.
     *
     * @param creader The CSV reader to use.
     * @return The walking value.
     * @throws IOException Thrown when reading the data goes wrong.
     */
    public long getWalkValue(final CsvReader creader) throws IOException {
        Language language = getLanguageSelection();
        String tmpValue;
        switch (language) {
            case DE:
                tmpValue = creader.get(HEADER_WALK_VALUE_DE);
                break;
            case EN:
                tmpValue = creader.get(HEADER_WALK_VALUE_EN);
                break;
            default:
                Logger.getLogger(this.getClass().getName()).severe("ASSERTION ERROR!");
                throw new AssertionError();
        }
        if (tmpValue != null && !tmpValue.isEmpty()) {
            return Long.parseLong(tmpValue);
        } else {
            return 0;
        }
    }

    /**
     * Getter for the running value.
     *
     * @param creader The CSV reader to use.
     * @return The running value.
     * @throws IOException Thrown when reading the data goes wrong.
     */
    public long getRunValue(final CsvReader creader) throws IOException {
        Language language = getLanguageSelection();
        String tmpValue;
        switch (language) {
            case DE:
                tmpValue = creader.get(HEADER_RUN_VALUE_DE);
                break;
            case EN:
                tmpValue = creader.get(HEADER_RUN_VALUE_EN);
                break;
            default:
                Logger.getLogger(this.getClass().getName()).severe("ASSERTION ERROR!");
                throw new AssertionError();
        }
        if (tmpValue != null && !tmpValue.isEmpty()) {
            return Long.parseLong(tmpValue);
        } else {
            return 0;
        }
    }

    /**
     * Getter for the maximum velocity.
     *
     * @param creader The CSV reader to use.
     * @return The maximum velocity.
     * @throws IOException Thrown when reading the data goes wrong.
     */
    public double getMaxSpeedValue(final CsvReader creader) throws IOException {
        Language language = getLanguageSelection();
        String tmpValue;
        switch (language) {
            case DE:
                tmpValue = creader.get(HEADER_MAX_SPEED_VALUE_DE);
                break;
            case EN:
                tmpValue = creader.get(HEADER_MAX_SPEED_VALUE_EN);
                break;
            default:
                Logger.getLogger(this.getClass().getName()).severe("ASSERTION ERROR!");
                throw new AssertionError();
        }
        if (tmpValue != null && !tmpValue.isEmpty()) {
            return Double.parseDouble(tmpValue);
        } else {
            return 0;
        }
    }

    /**
     * Getter for the starting time of an activity.
     *
     * @param creader The CSV reader to use.
     * @param fileName The name of the file to extract the time from.
     * @return The starting time.
     * @throws IOException Thrown when reading the data goes wrong.
     * @throws ParseException Thrown when a {@link TimestampUtils#createCleanTimestamp(String, String)} can not be created.
     */
    public long getStartTime(final CsvReader creader, final String fileName) throws IOException, ParseException {
        Language language = getLanguageSelection();
        switch (language) {
            case DE:
                return timestampHack(creader.get(HEADER_START_TIME_DE), fileName, language);
            case EN:
                return timestampHack(creader.get(HEADER_START_TIME_EN), fileName, language);
            default:
                Logger.getLogger(this.getClass().getName()).severe("ASSERTION ERROR!");
                throw new AssertionError();
        }
    }

    /**
     * Getter for the ending time of an activity.
     *
     * @param creader The CSV reader to use.
     * @param fileName The name of the file to extract the time from.
     * @return The ending time.
     * @throws IOException Thrown when reading the data goes wrong.
     * @throws ParseException Thrown when a {@link TimestampUtils#createCleanTimestamp(String, String)} can not be created.
     */
    public long getEndTime(final CsvReader creader, final String fileName) throws IOException, ParseException {
        Language language = getLanguageSelection();
        switch (language) {
            case DE:
                return timestampHack(creader.get(HEADER_END_TIME_DE), fileName, language);
            case EN:
                return timestampHack(creader.get(HEADER_END_TIME_EN), fileName, language);
            default:
                Logger.getLogger(this.getClass().getName()).severe("ASSERTION ERROR!");
                throw new AssertionError();
        }
    }

    /**
     * A hack to extract a timestamp for the file.
     *
     * @param dbString The field to get the timestamp from.
     * @param fileName The name of the file to extract the time from.
     * @param language The language used in the file.
     * @return The timestamp.
     * @throws ParseException Thrown when a {@link TimestampUtils#createCleanTimestamp(String, String)} can not be created.
     */
    private long timestampHack(final String dbString, final String fileName, final Language language)
            throws ParseException {
        if (dbString == null || dbString.isEmpty()) {
            return 0;
        }
        String tempFileName = new File(fileName).getName();
        String dateString = tempFileName.split("\\.")[0]; //dirty hack --> change to json would solve this problem
        Date timestamp;
        switch (language) {
            case DE:
                timestamp = TimestampUtils.createCleanTimestamp(
                        dateString + " " + dbString.replaceAll(":", ""),
                        TIME_FORMAT_DE);
                break;
            case EN:
                timestamp = TimestampUtils.createCleanTimestamp(
                        dateString + " " + dbString.replaceAll(":", ""),
                        TIME_FORMAT_EN);
                break;
            default:
                Logger.getLogger(this.getClass().getName()).severe("ASSERTION ERROR!");
                throw new AssertionError();
        }
        return timestamp.getTime();
    }
}
