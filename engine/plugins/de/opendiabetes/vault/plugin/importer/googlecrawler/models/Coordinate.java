package de.opendiabetes.vault.plugin.importer.googlecrawler.models;

/**
 * Coordinate model.
 */
public class Coordinate {

    /**
     * Timestamp at which the user reached the coordinate.
     */
    private long timestamp;

    /**
     * Longitude of the coordinate.
     */
    private double longitude;

    /**
     * Latitude of the coordinate.
     */
    private double latitude;

    /**
     * Altitude of the coordinate.
     */
    private int altitude;

    /**
     * Accuracy of the coordinate in meters.
     */
    private int accuracy;

    /**
     * Default altitude if the altitude was not given.
     */
    private static final int NO_ALTITUDE_AVAILABLE = -1;


    /**
     * Constructor.
     * @param timestamp timestamp at which the user reached the coordinate
     * @param longitude longitude of the coordinate
     * @param latitude latitude of the coordinate
     * @param accuracy accuracy of the coordinate in meters
     * @param altitude altitude of the coordinate
     */
    public Coordinate(final long timestamp, final double longitude, final double latitude, final int accuracy, final int altitude) {
        this.timestamp = timestamp;
        this.longitude = longitude;
        this.latitude = latitude;
        this.altitude = altitude;
        this.accuracy = accuracy;
    }

    /**
     * Constructor.
     * @param timestamp timestamp at which the user reached the coordinate
     * @param longitude longitude of the coordinate
     * @param latitude latitude of the coordinate
     * @param accuracy accuracy of the coordinate in meters
     */
    public Coordinate(final long timestamp, final double longitude, final double latitude, final int accuracy) {
        this(timestamp, longitude, latitude, accuracy, NO_ALTITUDE_AVAILABLE);
    }


    /**
     * Getter for the longitude.
     * @return the longitude of the coordinate
     */
    public double getLongitude() {
        return longitude;
    }

    /**
     * Getter for the latitude.
     * @return the latitude of the coordinate
     */
    public double getLatitude() {
        return latitude;
    }

    /**
     * Getter for the altitude.
     * @return the altitude of the coordinate
     */
    public int getAltitude() {
        return altitude;
    }

    /**
     * Getter for the accuracy.
     * @return the accuracy of the coordinate in meters
     */
    public int getAccuracy() {
        return accuracy;
    }

    /**
     * Setter for the altitude.
     * @param altitude an altitude in meters
     */
    public void setAltitude(final int altitude) {
        this.altitude = altitude;
    }

    /**
     * Getter for the timestamp.
     * @return the timestamp of the coordinate
     */
    public long getTimestamp() {
        return timestamp;
    }

}
