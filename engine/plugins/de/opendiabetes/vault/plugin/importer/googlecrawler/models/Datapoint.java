package de.opendiabetes.vault.plugin.importer.googlecrawler.models;

/**
 * Datapoint model.
 */
public class Datapoint {

    /**
     * Timestamp at which the datapoint was recorded.
     */
    private long timestamp;

    /**
     * Location coordinate at which the datapoint was recorded.
     */
    private Coordinate coordinate;

    /**
     * Activity that was done.
     */
    private String activity;

    /**
     * Constructor.
     */
    public Datapoint() {
    }

    /**
     * Constructor.
     * @param coord location coordinate which was recorded
     * @param timestamp timestamp at which the datapoint occurred
     */
    public Datapoint(final Coordinate coord, final long timestamp) {
        this.timestamp = timestamp;
        this.coordinate = coord;
    }

    /**
     * Getter for the location coordinate of the datapoint.
     * @return the location coordinate
     */
    public Coordinate getCoordinate() {
        return coordinate;
    }

    /**
     * Setter for the location coordinate.
     * @param coordinate a location coordinate
     */
    public void setCoordinate(final Coordinate coordinate) {
        this.coordinate = coordinate;
    }

    /**
     * Getter for the activity.
     * @return the activity that was done
     */
    public String getActivity() {
        return activity;
    }

    /**
     * Setter for the activity.
     * @param activity the activity that was done
     */
    public void setActivity(final String activity) {
        this.activity = activity;
    }

    /**
     * Getter for the timestamp of the datapoint.
     * @return the timestamp at which the datapoint occurred.
     */
    public long getTimestamp() {
        return timestamp;
    }
}
