package de.opendiabetes.vault.plugin.importer.googlecrawler.models;

/**
 * Heart rate model.
 */
public class HeartRate {

    /**
     * Timestamp of the recording of the heart rate.
     */
    private long timestamp;

    /**
     * Rate as integer number.
     */
    private int rate;

    /**
     * Constructor.
     * @param timestamp unix timestamp at which the heart rate was recorded
     * @param rate heart rate as integer number.
     */
    public HeartRate(final long timestamp, final int rate) {
        this.timestamp = timestamp;
        this.rate = rate;
    }

    /**
     * Getter for the timestamp.
     * @return a unix timestamp
     */
    public long getTimestamp() {
        return timestamp;
    }

    /**
     * Getter for the heart rate value.
     * @return an integer number
     */
    public int getRate() {
        return rate;
    }
}
