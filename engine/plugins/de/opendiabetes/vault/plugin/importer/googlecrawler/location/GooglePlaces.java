package de.opendiabetes.vault.plugin.importer.googlecrawler.location;

import com.google.api.services.people.v1.model.Address;
import com.google.api.services.people.v1.model.Person;
import com.google.maps.GeoApiContext;
import com.google.maps.GeocodingApi;
import com.google.maps.PlacesApi;
import com.google.maps.errors.ApiException;
import com.google.maps.model.GeocodingResult;
import com.google.maps.model.LatLng;
import com.google.maps.model.PlaceType;
import com.google.maps.model.PlacesSearchResult;
import de.opendiabetes.vault.plugin.importer.googlecrawler.helper.Credentials;
import de.opendiabetes.vault.plugin.importer.googlecrawler.models.Contact;
import de.opendiabetes.vault.plugin.importer.googlecrawler.people.AddressBook;
import de.opendiabetes.vault.plugin.importer.googlecrawler.people.GooglePeople;
import de.opendiabetes.vault.plugin.importer.googlecrawler.plot.GoogleMapsPlot;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Retrieves data from the Google Places API.
 */
public final class GooglePlaces {

    /**
     * Singleton instance.
     */
    private static GooglePlaces instance;

    /**
     * Earth radius in meters.
     */
    private static final long EARTH_RADIUS_METERS = 6371000;

    /**
     * Maximum number of queries per second to the places API.
     */
    private static final int QUERY_RATE_LIMIT = 100;

    /**
     * Default radius for places queries, in meters.
     */
    private static final int DEFAULT_RADIUS = 50;

    /**
     * The GeoApiContext used to query the API.
     */
    private GeoApiContext context;

    // /**
    // * Search query template.
    // */
    // private String keywordSearch = "(verein) OR (sport) OR (bad)";

    /**
     * List of addresses of the current user.
     */
    private List<Contact> ownAddresses;

    /**
     * Getter for the singleton instance.
     * @return the singleton instance
     */
    public static GooglePlaces getInstance() {
        if (GooglePlaces.instance == null) {
            GooglePlaces.instance = new GooglePlaces();
        }
        return GooglePlaces.instance;
    }

    /**
     * Constructor.
     */
    private GooglePlaces() {
        ownAddresses = new ArrayList<>();
        construct(Credentials.getInstance().getAPIKey());
    }

    /**
     * Constructs the Google managed Geo API context.
     * @param apiKey the api key to be used with the Google API
     */
    private void construct(final String apiKey) {
        context = new GeoApiContext.Builder()
                .apiKey(apiKey).queryRateLimit(QUERY_RATE_LIMIT)
                .build();
    }

//    /**
//     * Converts the given latitude and longitude to a Geocoding address.
//     * @param latitude a valid latitude
//     * @param longitude a valid longitude
//     * @return the resolved address
//     */
//    public GeocodingResult[] gpsToAddress(final double latitude, final double longitude) {
//        if (context == null) {
//            return null;
//        }
//
//        try {
//            return GeocodingApi.reverseGeocode(context, new LatLng(latitude,
//                    longitude)).await();
//
//        } catch (ApiException | InterruptedException | IOException e) {
//            e.printStackTrace();
//        }
//
//        return null;
//    }

    /**
     * Converts the given address in a latitude and longitude pair.
     * @param address a query address
     * @return a latitude and longitude pair
     */
    public GeocodingResult[] addressToGPS(final String address) {
        if (context == null) {
            return null;
        }

        try {
            return GeocodingApi.geocode(context, address).await();

        } catch (ApiException | InterruptedException | IOException e) {
            e.printStackTrace();
        }

        return null;
    }

    /**
     * Checks if a gym is located within the default radius of the given location (defined by latitude and longitude).
     * @param latitude a valid latitude
     * @param longitude a valid longitude
     * @return boolean value indicating if the given location is a gym or not
     */
    public boolean isGym(final double latitude, final double longitude) {
        try {
            PlacesSearchResult[] results = PlacesApi.nearbySearchQuery(context, new LatLng(latitude,
                    longitude)).radius(DEFAULT_RADIUS).type(PlaceType.GYM).await().results;

            if (results.length > 0) {
                return true;
            }
        } catch (ApiException | InterruptedException | IOException e) {
            e.printStackTrace();
        }

        return false;
    }

//    /**
//     * Setter for the query keyword search params.
//     * @param keywordSearchParams valid search params that will be appended to the query
//     */
//    public void setKeywordSearchParams(final String[] keywordSearchParams) {
//        for (String param : keywordSearchParams) {
//            keywordSearch = keywordSearch + " OR (" + param.toLowerCase() + ")";
//        }
//    }
//
//    /**
//     * Searches the context with the previously set keyword search params
//     * and the given location (latitude, longitude) and radius (accuracy).
//     * @param latitude a valid latitude
//     * @param longitude a valid longitude
//     * @param accuracy a radius in meters
//     * @return The name of the result if there was a place found with the given query params, otherwise "AWAY"
//     */
//    public String keywordSearch(final double latitude, final double longitude, final int accuracy) {
//        try {
//            PlacesSearchResult[] results = PlacesApi
//                    .nearbySearchQuery(context, new LatLng(latitude, longitude))
//                    .radius(accuracy)
//                    .keyword(keywordSearch)
//                    .await().results;
//            if (results.length == 1) {
//                return results[0].name;
//            } else {
//                for (PlacesSearchResult sr : results) {
//                    boolean isPolitical = false;
//                    for (String st : sr.types) {
//                        if (st.equals("political")) {
//                            isPolitical = true;
//                        }
//                    }
//
//                    if (!isPolitical) {
//                        return sr.name;
//                    }
//                }
//            }
//        } catch (ApiException | InterruptedException | IOException e) {
//            e.printStackTrace();
//        }
//
//        return "AWAY";
//    }

    /**
     * Retrieves the own addresses of the current user.
     */
    public void fetchOwnAddresses() {
        if (context != null) {
            Person me = GooglePeople.getInstance().getProfile();

            if (me.getAddresses() != null) {
                for (Address res : me.getAddresses()) {
                    String name = res.getFormattedType();
                    List<Address> address = Arrays.asList(res);
                    ownAddresses.add(new Contact(name, address));
                }
            }

        }
    }

    /**
     * Returns the name of the place if the user is at one of his own addresses.
     * @param latitude a valid latitude
     * @param longitude a valid longitude
     * @param accuracy a radius in meters
     * @return the name of the own address if the user is located there, otherwise "AWAY"
     */
    public String atOwnPlaces(final double latitude, final double longitude, final int accuracy) {
        for (Contact ad : ownAddresses) {
            if (calculateDistance(latitude, longitude, ad.getCoordinateById(0).lat, ad.getCoordinateById(0).lng) <= accuracy) {
                GoogleMapsPlot.getInstance().addLocation(new LatLng(ad.getCoordinateById(0).lat, ad.getCoordinateById(0).lng));
                return ad.getName();
            }

        }
        return "AWAY";
    }

    /**
     * Returns the places search results if there were places found.
     * @param latitude a valid latitude
     * @param longitude a valid longitude
     * @param accuracy a radius in meters
     * @return the places search results or null if there was an error
     */
    public PlacesSearchResult[] getPlaces(final double latitude, final double longitude, final int accuracy) {
        try {
            return PlacesApi.nearbySearchQuery(context, new LatLng(latitude, longitude)).radius(accuracy).await().results;
        } catch (ApiException | InterruptedException | IOException e) {
            e.printStackTrace();
        }

        return null;
    }

    /**
     * Gets the names of the nearest food location at the given address.
     * @param latitude a valid latitude
     * @param longitude a valid longitude
     * @return the name of the nearest food location, if there were non found or if there was an error "AWAY"
     */
    public String getFoodLocation(final double latitude, final double longitude) {
        try {
            PlacesSearchResult[] results = PlacesApi
                    .nearbySearchQuery(context, new LatLng(latitude, longitude))
                    .radius(DEFAULT_RADIUS)
                    .type(PlaceType.RESTAURANT, PlaceType.FOOD, PlaceType.CAFE, PlaceType.MEAL_TAKEAWAY)
                    .await().results;
            if (results.length > 0) {
                return results[0].name;
            }
        } catch (ApiException | InterruptedException | IOException e) {
            e.printStackTrace();
        }

        return "AWAY";
    }

    /**
     * Returns the name of the contact if the current user is at one of his contacts' locations with the given address.
     * @param latitude a valid latitude
     * @param longitude a valid longitude
     * @param searchRadius a valid search radius in meters
     * @return the name of the contact if within the search radius of the given location, otherwise "AWAY"
     */
    public String isAtContact(final double latitude, final double longitude, final int searchRadius) {
        for (int i = 0; i < AddressBook.getInstance().size(); i++) {
            Contact ref = AddressBook.getInstance().getContactById(i);
            for (LatLng ll : ref.getCoordinates()) {
                if (GooglePlaces.getInstance().calculateDistance(latitude, longitude, ll.lat, ll.lng) < searchRadius) {
                    GoogleMapsPlot.getInstance().addLocation(new LatLng(ll.lat, ll.lng));
                    return ref.getName();
                }
            }

        }

        return "AWAY";
    }

    /**
     * Calculates the distance in meters between two given latitudes and longitudes.
     * @param latitude a valid latitude
     * @param longitude a valid longitude
     * @param latRef a valid latitude
     * @param longitudeRef a valid longitude
     * @return the distance between the two locations in meters.
     */
    public double calculateDistance(final double latitude, final double longitude, final double latRef, final double longitudeRef) {
        double latRefRadian = Math.toRadians(latRef);

        double latOneRadian = Math.toRadians(latitude);

        double deltaLat = Math.toRadians(latitude - latRef);
        double deltaLong = Math.toRadians(longitude - longitudeRef);

        double a = Math.sin(deltaLat / 2.0) * Math.sin(deltaLat / 2.0)
                + Math.cos(latRefRadian) * Math.cos(latOneRadian)
                * Math.sin(deltaLong / 2.0) * Math.sin(deltaLong / 2.0);

        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));

        return EARTH_RADIUS_METERS * c;
    }
}
