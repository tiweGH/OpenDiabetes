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
package de.opendiabetes.vault.plugin.importer.sony;

import com.csvreader.CsvReader;
import de.opendiabetes.vault.plugin.importer.validator.CSVValidator;
import de.opendiabetes.vault.plugin.util.TimestampUtils;

import java.io.IOException;
import java.text.ParseException;
import java.util.Date;

/**
 * Validator for Sony SWR12 data.
 *
 * @author Jens Heuschkel
 */
public class SonySWR12Validator extends CSVValidator {

    /**
     * Field of the Sony SWR12 CSV headers containing the activity type.
     */
    private static final String HEADER_TYPE = "activity_type";
    /**
     * Field of the Sony SWR12 CSV headers containing the activity data.
     */
    private static final String HEADER_VALUE = "activity_data";
    /**
     * Field of the Sony SWR12 CSV headers containing the time the activity started.
     */
    private static final String HEADER_START_TIME = "start_time";
    /**
     * Field of the Sony SWR12 CSV headers containing the time the activity ended.
     */
    private static final String HEADER_END_TIME = "end_time";
    /**
     * The time format used in Sony SWR12 CSV data.
     */
    private static final String TIME_FORMAT = "yyyy-MM-dd HH:mm:ss";

    /**
     * The composed header used in Sony SWR12 CSV data.
     */
    private static final String[] HEADER = {
            HEADER_START_TIME, HEADER_END_TIME,
            HEADER_TYPE,
            HEADER_VALUE
    };

    /**
     * Constructor.
     */
    public SonySWR12Validator() {
        //universal header
        super(HEADER, HEADER);
    }

    /**
     * Getter for the smartband type.
     *
     * @param creader The CSV reader to use.
     * @return The smartband type.
     * @throws IOException Thrown when reading the data goes wrong.
     */
    public TYPE getSmartbandType(final CsvReader creader) throws IOException {
        int typeInt = Integer.parseInt(creader.get(HEADER_TYPE).trim());
        return TYPE.fromInt(typeInt);
    }

    /**
     * Getter for the timestamp.
     *
     * @param creader The CSV reader to use.
     * @return The timestamp.
     * @throws IOException Thrown when reading the data goes wrong.
     * @throws ParseException Thrown when {@link TimestampUtils#createCleanTimestamp(Date)} goes wrong.
     */
    public Date getTimestamp(final CsvReader creader) throws IOException, ParseException {
        String timeString = creader.get(
                HEADER_START_TIME).trim();
        long timeStampMil = Long.parseLong(timeString);
        return TimestampUtils.createCleanTimestamp(new Date(timeStampMil));
    }

    /**
     * Getter for the value.
     *
     * @param creader The CSV reader to use.
     * @return The value.
     * @throws IOException Thrown when reading the data goes wrong.
     */
    public int getValue(final CsvReader creader) throws IOException {
        return Integer.parseInt(creader.get(HEADER_VALUE));
    }

    /**
     * Getter for the start time of an activity.
     *
     * @param creader The CSV reader to use.
     * @return The start time of an activity.
     * @throws IOException Thrown when reading the data goes wrong.
     */
    public long getStartTime(final CsvReader creader) throws IOException {
        return Long.parseLong(creader.get(HEADER_START_TIME));
    }

    /**
     * Getter for the end time of an activity.
     *
     * @param creader The CSV reader to use.
     * @return The end time of an activity.
     * @throws IOException Thrown when reading the data goes wrong.
     */
    public long getEndTime(final CsvReader creader) throws IOException {
        return Long.parseLong(creader.get(HEADER_END_TIME));
    }

    /**
     * The available types of Sony SWR12 data.
     */
    public enum TYPE {
        /**
         * Indicator for light sleep.
         */
        SLEEP_LIGHT(5),
        /**
         * Indicator for deep sleep.
         */
        SLEEP_DEEP(6),
        /**
         * Heart rate variability.
         */
        HEART_RATE_VARIABILITY(9),
        /**
         * Heart rate.
         */
        HEART_RATE(8),
        /**
         * Indicates walking activity.
         */
        WALK(0),
        /**
         * Indicates running activity.
         */
        RUN(1);

        /**
         * Ordinal representing the type.
         */
        private final int typeInt;

        /**
         * Constructor for types.
         * @param typeInt The ordinal of the type.
         */
        TYPE(final int typeInt) {
            this.typeInt = typeInt;
        }

        /**
         * Gets the corresponding type from the enum.
         *
         * @param typeInt The type to get.
         * @return The corresponding type.
         */
        static TYPE fromInt(final int typeInt) {
            for (TYPE item : TYPE.values()) {
                if (item.typeInt == typeInt) {
                    return item;
                }
            }
            return null;
        }
    }
}
