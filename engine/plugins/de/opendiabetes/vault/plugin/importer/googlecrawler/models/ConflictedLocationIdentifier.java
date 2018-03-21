package de.opendiabetes.vault.plugin.importer.googlecrawler.models;

import com.google.maps.model.LatLng;

/**
 * Conflicted location identifier model.
 */
public final class ConflictedLocationIdentifier {

    /**
     * Timestamp of location conflict.
     */
    private long timestamp;

    /**
     * Location of the conflict.
     */
    private LatLng coordinate;

    /**
     * Constructor.
     * @param timestamp timestamp of the location conflict
     * @param coordinate location of the conflict
     */
    public ConflictedLocationIdentifier(final long timestamp, final LatLng coordinate) {
        this.timestamp = timestamp;
        this.coordinate = coordinate;
    }

    /**
     * Getter for the timestamp of the location conflict.
     * @return a unix timestamp
     */
    public long getTimestamp() {
        return timestamp;
    }

    /**
     * Getter for the location of the conflict.
     * @return the location of the conflict
     */
    public LatLng getCoordinate() {
        return coordinate;
    }
}
