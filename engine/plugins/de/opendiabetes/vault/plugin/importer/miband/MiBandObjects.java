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


import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.util.List;

/**
 * This class holds the objects used in MiBands JSON files.
 * These objects are represented as classes, to see why we do it this way: take a look into GSON.
 *
 * @author Lucas Buschlinger
 */
public class MiBandObjects {

    /**
     * This object will hold the data if there was some of the {@link HeartMonitorData} type.
     */
    private List<HeartMonitorData> heartMonitorData;
    /**
     * This object will hold the data if there was some of the {@link SleepIntervalData} type.
     */
    private List<SleepIntervalData> sleepIntervalData;
    /**
     * This object will hold the data if there was some of the {@link Workout} type.
     */
    private List<Workout> workout;
//    Currently unused.
//     /**
//     * This object will hold the data if there was some of the {@link StepsData} type.
//     */
//    private List<StepsData> stepsData;
    /**
     * This object will hold the data if there was some of the {@link Weight} type.
     */
    private List<Weight> weight;

    /**
     * Getter for the heart monitor data.
     *
     * @return The heart monitor data.
     */
    public List<HeartMonitorData> getHeartMonitorData() {
        return heartMonitorData;
    }

    /**
     * Getter for the sleep data.
     *
     * @return The sleep data.
     */
    public List<SleepIntervalData> getSleepIntervalData() {
        return sleepIntervalData;
    }

    /**
     * Getter for the workout data.
     *
     * @return The workout data.
     */
    public List<MiBandObjects.Workout> getWorkout() {
        return workout;
    }

//    /**
//     * Getter for the steps data.
//    *
//     * @return The steps data.
//     */
//    public List<MiBandObjects.StepsData> getStepsData() {
//        return stepsData;
//    }

    /**
     * Getter for the weight data.
     *
     * @return The weight data.
     */
    public List<MiBandObjects.Weight> getWeight() {
        return weight;
    }

    /**
     * This class resembles the data structure within MiBand heart rate logs.
     * But only the used fields are present, if others are needed they have to be added along with their respective getters.
     */
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class HeartMonitorData {
        /**
         * Boolean to indicate whether the entry is hidden.
         */
        private String hidden;
        /**
         * The heart rate.
         */
        private String intensity;
        /**
         * The timestamp of the recorded heart rate.
         */
        private String timestamp;

        /**
         * This shows whether the entry is hidden.
         *
         * @return True if the entry is hidden, false otherwise.
         */
        public boolean isHidden() {
            return Boolean.parseBoolean(hidden);
        }

        /**
         * Getter for the heart rate held in {@link #intensity}.
         *
         * @return The heart rate parsed from String.
         */
        public double getIntensity() {
            return Double.parseDouble(intensity);
        }

        /**
         * Getter for the {@link #timestamp}.
         *
         * @return The timestamp parsed from String.
         */
        public long getTimestamp() {
            return Long.parseLong(timestamp);
        }
    }

    /**
     * This class resembles the data structured within MiBand sleep logs.
     * But only the used fields are present, if others are needed they have to be added along with their respective getters.
     */
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class SleepIntervalData {

        /**
         * The ending time of the sleep interval.
         */
        private String endDateTime;
        /**
         * The starting time of the sleep interval.
         */
        private String startDateTime;
        /**
         * The type of the sleep interval.
         */
        private String type;

        /**
         * Getter for the starting time of the entry.
         *
         * @return The timestamp of the entry as long.
         */
        public long getStartDateTime() {
            return Long.parseLong(startDateTime);
        }

        /**
         * Getter for the ending time of the entry.
         *
         * @return The timestamp of the entry as long.
         */
        public long getEndDateTime() {
            return Long.parseLong(endDateTime);
        }

        /**
         * Getter for the duration of the sleep interval.
         * Computed by the difference between start and end times of the entry.
         *
         * @return The duration of the sleep interval.
         */
        public double getDuration() {
            final double msPerMin = 60000;
            double start = Double.parseDouble(startDateTime) / msPerMin;
            double end = Double.parseDouble(endDateTime) / msPerMin;
            return end - start;
        }

        /**
         * Getter for the {@link #type} of sleep within the sleep interval.
         *
         * @return The type of sleep parsed from String.
         */
        public int getType() {
            return Integer.parseInt(type);
        }
    }

    /**
     * This class resembles the data structure within MiBand workout logs.
     * But only the used fields are present, if others are needed they have to be added along with their respective getters.
     */
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class Workout {

        /**
         * The ending time of the workout.
         */
        private String endDateTime;
        /**
         * The average heart rate during the workout.
         */
        private String heartAvg;
        /**
         * The starting time of the workout.
         */
        private String startDateTime;

        /**
         * Getter for the ending time of the activity.
         *
         * @return The time represented as a long.
         */
        public Double getEndDateTime() {
            return Double.parseDouble(endDateTime);
        }

        /**
         * Getter for the starting time of the activity.
         *
         * @return The time represented as a long.
         */
        public Double getStartDateTime() {
            return Double.parseDouble(startDateTime);
        }

        /**
         * Getter for the timestamp, which is hereby defined by the end time of the entry.
         *
         * @return The timestamp of the entry.
         */
        public long getTimestamp() {
            return Long.parseLong(startDateTime);
        }

        /**
         * Getter for the duration of the entry in seconds.
         * Computed by the difference between start and end times of the entry.
         * As the timestamps are given in UNIX timestamps, the difference between them is in milliseconds.
         * This gets converted to seconds herein.
         *
         * @return The duration of the workout entry.
         */
        public double getDuration() {
            final double msPerMin = 60000;
            double start = Double.parseDouble(startDateTime) / msPerMin;
            double end = Double.parseDouble(endDateTime) / msPerMin;
            return end - start;
        }

        /**
         * Getter for the average heart rate held in {@link #heartAvg}.
         *
         * @return The average heart rate in the entry.
         */
        public String getHeartAvg() {
            return heartAvg;
        }
    }

    /**
     * This class resembles the data structure within MiBand step logs.
     * But only the used fields are present, if others are needed they have to be added along with their respective getters.
     *
     * Currently not used.
     */
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class StepsData {

        /**
         * The timestamp of the data.
         */
        private String dateTime;
        /**
         * Indicates whether the entry is supposed to be hidden.
         */
        private String hidden;
        /**
         * Indicates whether the entry is the last for the day.
         */
        private String last;
        /**
         * Number of steps recorded in this entry.
         */
        private String steps;

        /**
         * Getter for the timestamp.
         * Converts the String to a Long.
         *
         * @return The timestamp.
         */
        public long getTimestamp() {
            return Long.parseLong(dateTime);
        }

        /**
         * Getter for the steps.
         * Converts the String to a Double.
         *
         * @return The number of steps recorded in this entry.
         */
        public int getSteps() {
            return Integer.parseInt(steps);
        }

        /**
         * This shows whether the entry is hidden.
         *
         * @return True if the entry is hidden, false otherwise.
         */
        public boolean isHidden() {
            return Boolean.parseBoolean(hidden);
        }

        /**
         * This shows whether the entry is the last for a given day.
         *
         * @return True if it's the last entry of the day, false otherwise.
         */
        public boolean isLast() {
            return Boolean.parseBoolean(last);
        }
    }

    /**
     * This class resembles the data structure within MiBand weight logs.
     * But only the used fields are present, if others are needed they have to be added along with their respective getters.
     */
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class Weight {

        /**
         * The timestamp of the entry.
         */
        private String timestamp;
        /**
         * The value of the entry.
         */
        private String value;

        /**
         * Getter for the timestamp.
         * Converts the String to a Long.
         *
         * @return The timestamp.
         */
        public long getTimestamp() {
            return Long.parseLong(timestamp);
        }

        /**
         * Getter for the weight.
         * Converts the String to a Double.
         *
         * @return The number of steps recorded in this entry.
         */
        public double getValue() {
            return Double.parseDouble(value);
        }
    }
}
