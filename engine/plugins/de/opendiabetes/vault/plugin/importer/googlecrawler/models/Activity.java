package de.opendiabetes.vault.plugin.importer.googlecrawler.models;

import java.util.Arrays;

/**
 * Activity model.
 */
public class Activity {

    /**
     * Start time of the activity.
     */
    private long startTime;

    /**
     * End time of the activity.
     */
    private long endTime;

    /**
     * Activity identifier index.
     */
    private int activityId;

    /**
     * Intensity of the exercise activity.
     */
    private int intensity;

    /**
     * Heart rate minimum, maximum and average.
     */
    private int[] heartRate;

    /**
     * Location at which the activity occurred.
     */
    private String location = "";

    /**
     * Constructor.
     * @param startTime start time of the activity as unix timestamp
     * @param endTime end time of the activity as unix timestamp
     * @param activity the identifier index of the activity
     */
    public Activity(final long startTime, final long endTime, final int activity) {
        this.startTime = startTime;
        this.endTime = endTime;
        this.activityId = activity;
        this.intensity = -1;
        this.heartRate = new int[3];
    }

    /**
     * Getter for the start time of the activity.
     * @return unix timestamp representation of the start time
     */
    public long getStartTime() {
        return startTime;
    }

    /**
     * Getter for the end time of the activity.
     * @return unix timestamp representation of the end time
     */
    public long getEndTime() {
        return endTime;
    }

    /**
     * Getter for the activity identifier index.
     * @return activity identifier index
     */
    public int getActivityId() {
        return activityId;
    }

    /**
     * Getter for location of the activity.
     * @return location at which the activity occurred.
     */
    public String getLocation() {
        return location;
    }

    /**
     * Setter for the location of the activity.
     * @param location a string location
     */
    public void setLocation(final String location) {
        this.location = location;
    }

    /**
     * Getter for the intensity of the activity.
     * @return the intensity as an integer
     */
    public int getIntensity() {
        return intensity;
    }

    /**
     * Setter for the intensity of the activity.
     * @param intensity an intensity as integer
     */
    public void setIntensity(final int intensity) {
        this.intensity = intensity;
    }

    /**
     * Getter for the minimum, maximum and average heart rates of the activity.
     * @return the minimum, maximum and average of heart rates
     */
    public int[] getHeartRate() {
        return Arrays.copyOf(heartRate, heartRate.length);
    }

    /**
     * Setter for the minimum, maximum and average heart rates of the activity.
     * @param heartRate an integer array containing the heart rates
     */
    public void setHeartRate(final int[] heartRate) {
        this.heartRate = Arrays.copyOf(heartRate, heartRate.length);
    }

    /**
     * Setter for the end time of the activity.
     * @param endTime the end time of the activity as unix timestamp
     */
    public void setEndTime(final long endTime) {
        this.endTime = endTime;
    }
}
