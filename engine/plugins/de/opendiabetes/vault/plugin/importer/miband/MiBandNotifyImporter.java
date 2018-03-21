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
package de.opendiabetes.vault.plugin.importer.miband;

import com.fasterxml.jackson.databind.MapperFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import de.opendiabetes.vault.container.VaultEntry;
import de.opendiabetes.vault.container.VaultEntryAnnotation;
import de.opendiabetes.vault.container.VaultEntryType;
import de.opendiabetes.vault.plugin.fileimporter.AbstractFileImporter;
import org.pf4j.Extension;
import org.pf4j.Plugin;
import org.pf4j.PluginWrapper;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Properties;
import java.util.logging.Level;

/**
 * Wrapper class for the MiBandNotifyImporter plugin.
 *
 * @author Lucas Buschlinger
 */
public class MiBandNotifyImporter extends Plugin {

    /**
     * Constructor for the {@link org.pf4j.PluginManager}.
     *
     * @param wrapper The {@link PluginWrapper}.
     */
    public MiBandNotifyImporter(final PluginWrapper wrapper) {
        super(wrapper);
    }

    /**
     * Actual implementation of the MiBandNotify importer plugin.
     */
    @Extension
    public static final class MiBandNotifyImporterImplementation extends AbstractFileImporter {

        /**
         * The default value for the lower bound of the heart rate.
         */
        private static final int DEFAULT_HEART_RATE_LOWER_BOUND = 40;
        /**
         * The default value for upper bound for the heart rate.
         */
        private static final int DEFAULT_HEART_RATE_UPPER_BOUND = 250;
        /**
         * The default value for the threshold from where an exercise will be classed as {@link VaultEntryType#EXERCISE_MID}.
         */
        private static final int DEFAULT_EXERCISE_HEART_THRESHOLD_MID = 90;
        /**
         * The default value for the threshold from where an exercise will be classed as {@link VaultEntryType#EXERCISE_HIGH}.
         */
        private static final int DEFAULT_EXERCISE_HEART_THRESHOLD_HIGH = 130;
        /**
         * The default value for the time span in which entries will be joined together.
         */
        private static final int DEFAULT_MAX_TIME_GAP_MINUTES = 10;

        /**
         * This status percentage when the config has been loaded.
         */
        private static final int STATUS_LOADED_CONFIG = 25;
        /**
         * This status percentage when the JSON file has been read.
         */
        private static final int STATUS_READ_JSON = 50;
        /**
         * This status percentage when the JSON entries have been imported to VaultEntries.
         */
        private static final int STATUS_IMPORTED_ENTRIES = 75;
        /**
         * This status percentage when the VaultEntries have been interpreted.
         */
        private static final int STATUS_INTERPRETED_ENTRIES = 100;

        /**
         * The value for the lower bound of the heart rate.
         */
        private int heartRateLowerBound;
        /**
         * The value for the upper bound of the heart rate.
         */
        private int heartRateUpperBound;
        /**
         * The value for the threshold from where an exercise will be classed as {@link VaultEntryType#EXERCISE_MID}.
         */
        private int exerciseHeartThresholdMid;
        /**
         * The value for the threshold from where an exercise will be classed as {@link VaultEntryType#EXERCISE_HIGH}.
         */
        private int exerciseHeartThresholdHigh;
        /**
         * The value for the time span in which entries will be joined together.
         */
        private int maxTimeGapMinutes;

        /**
         * Constructor.
         * This also sets the default values for the thresholds between different kinds of exercises and heart rate bounds.
         */
        public MiBandNotifyImporterImplementation() {
            this.heartRateLowerBound = DEFAULT_HEART_RATE_LOWER_BOUND;
            this.heartRateUpperBound = DEFAULT_HEART_RATE_UPPER_BOUND;
            this.exerciseHeartThresholdMid = DEFAULT_EXERCISE_HEART_THRESHOLD_MID;
            this.exerciseHeartThresholdHigh = DEFAULT_EXERCISE_HEART_THRESHOLD_HIGH;
            this.maxTimeGapMinutes = DEFAULT_MAX_TIME_GAP_MINUTES;
        }

        /**
         * Preprocessing not needed.
         */
        @Override
        protected void preprocessingIfNeeded(final String filePath) { }

        /**
         * {@inheritDoc}
         */
        @Override
        protected List<VaultEntry> processImport(final InputStream fileInputStream, final String filenameForLogging) throws IOException {
            BufferedReader reader = new BufferedReader(new InputStreamReader(fileInputStream, "UTF-8"));
            ObjectMapper mapper = new ObjectMapper();
            mapper.configure(MapperFeature.ACCEPT_CASE_INSENSITIVE_PROPERTIES, true);
            MiBandObjects data = mapper.readValue(reader, MiBandObjects.class);

            // Reading the JSON file
            this.notifyStatus(STATUS_READ_JSON, "Read JSON file.");
            List<VaultEntry> importedData = new ArrayList<>();

            // Seeing, whether the data contained heart rate related data
            if (data.getHeartMonitorData() != null) {
                importedData = processHeartData(data);
                this.notifyStatus(STATUS_IMPORTED_ENTRIES, "Successfully imported MiBand data to VaultEntries");
                this.notifyStatus(STATUS_INTERPRETED_ENTRIES, "Interpreted MiBand data");
            } else if (data.getSleepIntervalData() != null) {
                importedData = processSleepData(data);
                this.notifyStatus(STATUS_IMPORTED_ENTRIES, "Successfully imported MiBand data to VaultEntries");
                importedData = interpretMiBandSleep(importedData);
                this.notifyStatus(STATUS_INTERPRETED_ENTRIES, "Interpreted MiBand data");
            } else if (data.getWorkout() != null) {
                importedData = processWorkoutData(data);
                this.notifyStatus(STATUS_IMPORTED_ENTRIES, "Successfully imported MiBand data to VaultEntries");
                this.notifyStatus(STATUS_INTERPRETED_ENTRIES, "Interpreted MiBand data");
            } else if (data.getWeight() != null) {
                importedData = processWeightData(data);
                this.notifyStatus(STATUS_IMPORTED_ENTRIES, "Successfully imported MiBand data to VaultEntries");
                this.notifyStatus(STATUS_INTERPRETED_ENTRIES, "Interpreted MiBand data");
            } else {
                LOG.log(Level.SEVERE, "Got no data from JSON import!");
                this.notifyStatus(-1, "Got no data from JSON import!");
                return null;
            }
            return importedData;
        }

        /**
         * This method is used to convert the JSON/GSON object to {@link VaultEntry}.
         * In particular this converts heart rate data.
         *
         * @param data The GSON/JSON data.
         * @return The data as {@link VaultEntry}.
         */
        private List<VaultEntry> processHeartData(final MiBandObjects data) {
            List<VaultEntry> entries = new ArrayList<>();
            for (MiBandObjects.HeartMonitorData item : data.getHeartMonitorData()) {
                if (!item.isHidden()) {
                    double heartRate = item.getIntensity();
                    if (heartRate > heartRateLowerBound && heartRate < heartRateUpperBound) {
                        Date timestamp = new Date(item.getTimestamp());
                        VaultEntry entry = new VaultEntry(VaultEntryType.HEART_RATE, timestamp, heartRate);
                        entries.add(entry);
                    } else {
                        LOG.log(Level.INFO, String.format("Abnormal heart rate (%.1f BPM) while exercising, skipping entry.", heartRate));
                    }
                }
            }
            return entries;
        }

        /**
         * This method is used to convert the JSON/GSON object to {@link VaultEntry}.
         * In particular this converts sleeping data.
         *
         * @param data The GSON/JSON data.
         * @return The data as {@link VaultEntry}.
         */
        private List<VaultEntry> processSleepData(final MiBandObjects data) {
            List<VaultEntry> entries = new ArrayList<>();
            final int deepSleep = 5;
            final int lightSleep = 4;
            for (MiBandObjects.SleepIntervalData item : data.getSleepIntervalData()) {
                int typeInt = item.getType();
                Date timestamp = new Date(item.getStartDateTime());
                double duration = item.getDuration();
                VaultEntryType type;
                if (typeInt == lightSleep) {
                    type = VaultEntryType.SLEEP_LIGHT;
                } else if (typeInt == deepSleep) {
                    type = VaultEntryType.SLEEP_DEEP;
                } else {
                    LOG.log(Level.INFO, "Unknown sleep type, skipping entry.");
                    continue;
                }
                entries.add(new VaultEntry(type, timestamp, duration));
            }
            return entries;
        }

        /**
         * This method is used to convert the JSON/GSON object to {@link VaultEntry}.
         * In particular this converts workout data.
         *
         * @param data The GSON/JSON data.
         * @return The data as {@link VaultEntry}.
         */
        private List<VaultEntry> processWorkoutData(final MiBandObjects data) {
            List<VaultEntry> entries = new ArrayList<>();
            for (MiBandObjects.Workout item : data.getWorkout()) {
                String heartRate = item.getHeartAvg();
                List<VaultEntryAnnotation> annotation = new ArrayList<>();
                annotation.add(new VaultEntryAnnotation(heartRate, VaultEntryAnnotation.TYPE.AVERAGE_HEART_RATE));
                int heartRateValue = Integer.parseInt(heartRate);
                double duration = item.getDuration();
                Date timestamp = new Date(item.getTimestamp());
                VaultEntryType type;
                if (heartRateValue < heartRateLowerBound || heartRateValue > heartRateUpperBound) {
                    LOG.log(Level.INFO, String.format("Abnormal heart rate (%d BPM) while exercising, skipping entry.", heartRateValue));
                    continue;
                }
                if (heartRateValue < exerciseHeartThresholdMid) {
                    type = VaultEntryType.EXERCISE_LOW;
                } else if (heartRateValue < exerciseHeartThresholdHigh) {
                    type = VaultEntryType.EXERCISE_MID;
                } else {
                    type = VaultEntryType.EXERCISE_HIGH;
                }
                entries.add(new VaultEntry(type, timestamp, duration, annotation));
            }
            return entries;
        }

        /**
         * This method is used to convert the JSON/GSON object to {@link VaultEntry}.
         * In particular this converts weight data.
         *
         * @param data The GSON/JSON data.
         * @return The data as {@link VaultEntry}.
         */
        private List<VaultEntry> processWeightData(final MiBandObjects data) {
            List<VaultEntry> entries = new ArrayList<>();
            for (MiBandObjects.Weight item : data.getWeight()) {
                entries.add(new VaultEntry(VaultEntryType.WEIGHT, new Date((item.getTimestamp())), item.getValue()));
            }
            return entries;
        }

        /**
         * This interprets the MiBand data by grouping together entries
         * who's gap is less than the maximum defined in {@link #maxTimeGapMinutes}.
         *
         * @param entries The imported entries which will get interpreted.
         * @return The list of the imported entries with filled gaps.
         */
        private List<VaultEntry> interpretMiBandSleep(final List<VaultEntry> entries) {
            final double msPerMin = 60000;
            List<VaultEntry> returnList = entries;
            int i = 0;
            int initialLength = entries.size();
            while (i < returnList.size() - 1) {
                VaultEntry thisEntry = returnList.get(i);
                VaultEntry nextEntry = returnList.get(i + 1);
                double gap = ((nextEntry.getTimestamp().getTime() - thisEntry.getTimestamp().getTime()) / msPerMin) - thisEntry.getValue();
                if (gap < 0) {
                    LOG.log(Level.INFO, "Detected entry which is later than the next, excluding it. Gap: " + gap);
                    returnList.remove(i);
                } else if (thisEntry.getType().equals(nextEntry.getType()) && gap < maxTimeGapMinutes) {
                    double newDuration = thisEntry.getValue() + gap + nextEntry.getValue();
                    thisEntry.setValue(newDuration);
                    returnList.remove(i + 1);
                } else {
                    i++;
                }
            }
            LOG.log(Level.INFO, "Removed entries due to merge: " + (initialLength - returnList.size()));
            return returnList;
        }

        /**
         * {@inheritDoc}
         */
        @Override
        public boolean loadPluginSpecificConfiguration(final Properties configuration) {
            if (configuration.containsKey("heartRateLowerBound")) {
                heartRateLowerBound = Integer.parseInt(configuration.getProperty("heartRateLowerBound"));
            }
            if (configuration.containsKey("heartRateUpperBound")) {
                heartRateUpperBound = Integer.parseInt(configuration.getProperty("heartRateUpperBound"));
            }
            if (configuration.containsKey("exerciseHeartThresholdMid")) {
                exerciseHeartThresholdMid = Integer.parseInt(configuration.getProperty("exerciseHeartThresholdMid"));
            }
            if (configuration.containsKey("exerciseHeartThresholdHigh")) {
                exerciseHeartThresholdHigh = Integer.parseInt(configuration.getProperty("exerciseHeartThresholdHigh"));
            }
            if (configuration.containsKey("maxTimeGap")) {
                maxTimeGapMinutes = Integer.parseInt(configuration.getProperty("maxTimeGap"));
            }
            LOG.log(Level.INFO, "Successfully loaded configuration.");
            this.notifyStatus(STATUS_LOADED_CONFIG, "Loaded configuration from properties file");
            return true;
        }

    }
}
