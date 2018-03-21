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
import de.opendiabetes.vault.container.VaultEntry;
import de.opendiabetes.vault.container.VaultEntryAnnotation;
import de.opendiabetes.vault.container.VaultEntryType;
import de.opendiabetes.vault.plugin.fileimporter.CSVImporter;
import de.opendiabetes.vault.plugin.util.EasyFormatter;
import org.pf4j.Extension;
import org.pf4j.Plugin;
import org.pf4j.PluginWrapper;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.logging.Logger;

/**
 * Wrapper class for the SonySWR12 Importer plugin.
 *
 * @author Lucas Buschlinger
 */
public class SonySWR12Importer extends Plugin {

    /**
     * Constructor for the PluginManager.
     *
     * @param wrapper The PluginWrapper.
     */
    public SonySWR12Importer(final PluginWrapper wrapper) {
        super(wrapper);
    }

    /**
     * Actual implementation of the Sony SWR12 importer plugin.
     */
    @Extension
    public static final class SonySWR12ImporterImplementation extends CSVImporter {

        /**
         * Constructor.
         */
        public SonySWR12ImporterImplementation() {
            super(new SonySWR12Validator());
        }

        /**
         * Parser for Sony SWR12 CSV data.
         *
         * @param creader The CSV reader.
         * @return List of VaultEntry holding the parsed data.
         * @throws Exception If Sony SWR12 CSV file can not be parsed.
         */
        @Override
        protected List<VaultEntry> parseEntry(final CsvReader creader) throws Exception {
            List<VaultEntry> retVal = new ArrayList<>();
            SonySWR12Validator parseValidator = (SonySWR12Validator) getValidator();

            SonySWR12Validator.TYPE type = parseValidator.getSmartbandType(creader);
            if (type == null) {
                return null;
            }

            Date timestamp;
            try {
                timestamp = parseValidator.getTimestamp(creader);
            } catch (ParseException exception) {
                return null;
            }

            final int millisecondsPerHour = 60000;
            int rawValue = parseValidator.getValue(creader);
            long startTime = parseValidator.getStartTime(creader);
            long endTime = parseValidator.getEndTime(creader);
            double durationInMinutes = (endTime - startTime) / (double) millisecondsPerHour;
            VaultEntry tmpEntry = null;

            switch (type) {
                case SLEEP_LIGHT:
                    tmpEntry = new VaultEntry(
                            VaultEntryType.SLEEP_LIGHT,
                            timestamp,
                            durationInMinutes);
                    break;
                case SLEEP_DEEP:
                    tmpEntry = new VaultEntry(
                            VaultEntryType.SLEEP_DEEP,
                            timestamp,
                            durationInMinutes);
                    break;
                case HEART_RATE:
                    tmpEntry = new VaultEntry(
                            VaultEntryType.HEART_RATE,
                            timestamp,
                            rawValue);
                    break;
                case HEART_RATE_VARIABILITY:
                    // Algorithm see decompiled SWR12 app --> RelaxStressIntensity Class
                    /*
                     * Constants.
                     */
                    final int maxValue8Bits = 255;
                    final int shift = 8;
                    final int heartRateLowerBound = 100;
                    final int heartRateUpperBound = 200;
                    final int uninitializedStressValue = -100;
                    final int baseStressValue = 25;
                    final double highWeight = 0.75;
                    final double lowWeight = 0.25;

                    int value1 = (rawValue >>> shift) & maxValue8Bits;
                    int value2 = maxValue8Bits & rawValue;

                    if (value1 > 0 && value1 < heartRateLowerBound
                            && value2 > 0 && value2 < heartRateUpperBound) {
                        tmpEntry = new VaultEntry(
                                VaultEntryType.HEART_RATE_VARIABILITY,
                                timestamp,
                                value1);
                        tmpEntry.setValue2(value2);
                        retVal.add(tmpEntry);

                        // calculate stress value
                        value2 -= uninitializedStressValue;
                        double weight;
                        if (value2 < 0) {
                            weight = highWeight;
                        } else {
                            weight = lowWeight;
                        }

                        double stressValue = baseStressValue - value2 * weight;
                        tmpEntry = new VaultEntry(
                                VaultEntryType.STRESS,
                                timestamp,
                                stressValue);
                    }

                    break;
                case RUN:
                    tmpEntry = new VaultEntry(
                            VaultEntryType.EXERCISE_OTHER,
                            timestamp,
                            durationInMinutes);
                    tmpEntry.addAnnotation(new VaultEntryAnnotation(
                            VaultEntryAnnotation.TYPE.EXERCISE_TrackerRun)
                            .setValue(EasyFormatter.formatDouble(durationInMinutes)));
                    break;
                case WALK:
                    tmpEntry = new VaultEntry(
                            VaultEntryType.EXERCISE_OTHER,
                            timestamp,
                            durationInMinutes);
                    tmpEntry.addAnnotation(new VaultEntryAnnotation(
                            VaultEntryAnnotation.TYPE.EXERCISE_TrackerWalk)
                            .setValue(EasyFormatter.formatDouble(durationInMinutes)));
                    break;
                default:
                    Logger.getLogger(this.getClass().getName()).fine("AssertionError");
                    throw new AssertionError();
            }

            if (tmpEntry != null) {
                retVal.add(tmpEntry);
            }
            return retVal;
        }

        /**
         * Empty preprocessing for Sony data, as it is not necessary for this type of data.
         *
         * @param filePath Path to the import file.
         */
        @Override
        protected void preprocessingIfNeeded(final String filePath) {
        }

    }
}
