package de.opendiabetes.vault.plugin.importer.googlecrawler.models;

import com.google.maps.model.LatLng;

/**
 * Location model.
 */
public class Location {

    /**
     * Latitude/Longitude pair of the location.
     */
    private LatLng coordinate;

    /**
     * Name of the location.
     */
    private String name;

    /**
     * Constructor.
     * @param coord lat/long pair of the location
     * @param name name of the location
     */
    public Location(final LatLng coord, final String name) {
        this.coordinate = coord;
        this.name = name;
    }

    /**
     * Constructor.
     */
    public Location() {
    }

    /**
     * Getter for the lat/long pair of the location.
     * @return the coordinate of the location
     */
    public LatLng getCoordinate() {
        return coordinate;
    }

    /**
     * Setter for the lat/long pair of the location.
     * @param coordinate the coordinate of the location
     */
    public void setCoordinate(final LatLng coordinate) {
        this.coordinate = coordinate;
    }

    /**
     * Getter for the name of the location.
     * @return the name of the location
     */
    public String getName() {
        return name;
    }

    /**
     * Setter for the name of the location.
     * @param name the name of the location
     */
    public void setName(final String name) {
        this.name = name;
    }
}
