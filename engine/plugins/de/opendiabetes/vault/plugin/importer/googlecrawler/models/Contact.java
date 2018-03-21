package de.opendiabetes.vault.plugin.importer.googlecrawler.models;

import com.google.api.services.people.v1.model.Address;
import com.google.maps.model.GeocodingResult;
import com.google.maps.model.LatLng;
import de.opendiabetes.vault.plugin.importer.googlecrawler.location.GooglePlaces;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Contact model.
 */
public class Contact {

    /**
     * Name of the contact.
     */
    private String name;

    /**
     * List of addresses of the contact.
     */
    private List<Address> address;

    /**
     * List of coordinates belonging to the address at the same index.
     */
    private List<LatLng> coordinates;

    /**
     * Constructor.
     * @param name the name of the contact
     */
    public Contact(final String name) {
        this.name = name;
    }

    /**
     * Constructor.
     * @param name the name of the contact
     * @param address a list of addresses
     */
    public Contact(final String name, final List<Address> address) {
        this.name = name;
        this.address = address;
        this.coordinates = getCoordinatesOfAddress(address);
    }

    /**
     * Constructor.
     * @param name the name of the contact
     * @param address a list of addresses
     * @param coordinates a list of coordinates belonging to the addresses
     */
    public Contact(final String name, final List<Address> address, final List<LatLng> coordinates) {
        this.name = name;
        this.address = address;
        this.coordinates = coordinates;
    }

    /**
     * Returns the list of coordinates at which the list of addresses are located.
     * @param address a list of addresses
     * @return the list of coordinates
     */
    private List<LatLng> getCoordinatesOfAddress(final List<Address> address) {
        return address.stream().map(a -> {
            GeocodingResult[] results = GooglePlaces.getInstance().addressToGPS(a.getFormattedValue());
            return new LatLng(results[0].geometry.location.lat, results[0].geometry.location.lng);
        }).collect(Collectors.toList());
    }

    /**
     * Getter for the name of the contact.
     * @return the name
     */
    public String getName() {
        return name;
    }

    /**
     * Getter for the list of addresses of the contact.
     * @return a list of addresses
     */
    public List<Address> getAddress() {
        return address;
    }

    /**
     * Gets a specific address by its ID.
     * @param id an ID
     * @return the address with the given ID from the list of addresses
     */
    public Address getAddressById(final int id) {
        return address.get(id);
    }

    /**
     * Getter for the list of coordinates of the addresses of the contact.
     * @return a list of coordinates
     */
    public List<LatLng> getCoordinates() {
        return coordinates;
    }

    /**
     * Gets a specific coordinate pair by its ID.
     * @param id an ID
     * @return the coordinate with the given ID from the list of coordinates
     */
    public LatLng getCoordinateById(final int id) {
        return coordinates.get(id);
    }
}
