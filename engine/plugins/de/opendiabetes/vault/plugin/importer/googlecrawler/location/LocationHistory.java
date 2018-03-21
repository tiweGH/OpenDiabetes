package de.opendiabetes.vault.plugin.importer.googlecrawler.location;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.maps.model.LatLng;
import com.google.maps.model.PlacesSearchResult;
import de.opendiabetes.vault.plugin.importer.googlecrawler.models.ConflictedLocationIdentifier;
import de.opendiabetes.vault.plugin.importer.googlecrawler.models.ResolvedLocations;
import de.opendiabetes.vault.plugin.importer.googlecrawler.helper.Constants;
import de.opendiabetes.vault.plugin.importer.googlecrawler.models.Activity;
import de.opendiabetes.vault.plugin.importer.googlecrawler.models.Coordinate;
import de.opendiabetes.vault.plugin.importer.googlecrawler.models.HeartRate;
import de.opendiabetes.vault.plugin.importer.googlecrawler.models.Location;
import de.opendiabetes.vault.plugin.importer.googlecrawler.models.ActivityTypes;
import de.opendiabetes.vault.plugin.importer.googlecrawler.plot.GoogleMapsPlot;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.ArrayList;
import java.util.Map;
import java.util.HashMap;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.Comparator;
import java.util.Collections;
import java.util.stream.Collectors;

/**
 * Manages the user's locations, activity and heart rate history.
 */
public final class LocationHistory {

    /**
     * Default search radius for places queries.
     */
    private static final int DEFAULT_RADIUS = 50;

    /**
     * Maximum possible human heart rate.
     */
    private static final int MAXIMUM_HUMAN_HEART_RATE = 220;

    /**
     * The number of milliseconds a day lasts.
     */
    private static final int DAY_MILLISECONDS = 86400000;

    /**
     * Five minutes represented in a unix timestamp.
     */
    private static final int UNIX_TIME_FIVE_MINUTES = 300000;

    /**
     * Twenty minutes represented in a unix timestamp.
     */
    private static final int UNIX_TIME_TWENTY_MINUTES = 1200000;

    /**
     * Singleton instance.
     */
    private static LocationHistory instance;

    /**
     * Location history of the user.
     */
    private Map<Long, List<Coordinate>> locationHistory;

    /**
     * Activity history of the user.
     */
    private Map<Long, List<Activity>> activityHistory;

    /**
     * Heart rate history of the user.
     */
    private Map<Long, List<HeartRate>> heartRateHistory;

    /**
     * Training heart rate history of the user.
     */
    private Map<Long, int[]> trainingHRHistory;

    /**
     * Conflicted locations of the current user.
     */
    private transient Map<ConflictedLocationIdentifier, List<PlacesSearchResult>> conflictLocations;

    /**
     * Age of the user.
     */
    private int age;

    /**
     * Constructor.
     */
    private LocationHistory() {
        locationHistory = new HashMap<>();
        activityHistory = new HashMap<>();
        heartRateHistory = new HashMap<>();
        trainingHRHistory = new HashMap<>();
        conflictLocations = new HashMap<>();

        File file = new File(System.getProperty("user.home") + Constants.RESOLVED_LOCATION_PATH);
        if (file.exists() && !file.isDirectory()) {
            Gson gson = new GsonBuilder().setPrettyPrinting().create();
            try (FileInputStream inputStream = new FileInputStream(file.getAbsolutePath())) {
                ResolvedLocations resLoc = gson.fromJson(new InputStreamReader(inputStream, StandardCharsets.UTF_8),
                        ResolvedLocations.class);
                ResolvedLocations.getInstance().importFromJson(resLoc);
                inputStream.close();
            } catch (IOException exception) {
                exception.printStackTrace();
            }
        }
    }

    /**
     * Returns the singleton instance.
     * @return singleton instance
     */
    public static LocationHistory getInstance() {
        if (LocationHistory.instance == null) {
            LocationHistory.instance = new LocationHistory();
            GooglePlaces.getInstance().fetchOwnAddresses();
        }
        return LocationHistory.instance;
    }

    /**
     * Returns all location coordinates at the given day.
     * @param day a valid day as unix timestamp
     * @return the list of all location coordinates from the history
     */
    private List<Coordinate> getLocationsPerDay(final long day) {
        long normalized = normalizeDate(day);
        if (locationHistory.get(normalized) != null) {
            return locationHistory.get(normalized);
        } else {
            return null;
        }
    }

    /**
     * Returns all location coordinates within the given timespan.
     * @param start a valid start day as unix timestamp
     * @param end a valid end day as unix timestamp
     * @return the list of all location coordinates from the history
     */
    public List<Coordinate> getLocationsForMultipleDays(final long start, final long end) {
        List<Coordinate> returnLocations = new ArrayList<>();
        long normalizedStart = normalizeDate(start);
        long normalizeEnd = normalizeDate(end);

        do {
            if (getLocationsPerDay(normalizedStart) != null) {
                returnLocations.addAll(getLocationsPerDay(normalizedStart));
            }
            normalizedStart += DAY_MILLISECONDS;
        } while (normalizedStart <= normalizeEnd);

        return returnLocations;
    }

    /**
     * Adds locations to the history.
     * @param day a valid day as unix timestamp
     * @param locations a list of location coordinates to be added
     */
    public void addLocations(final long day, final List<Coordinate> locations) {
        this.locationHistory.put(normalizeDate(day), locations);
    }

    /**
     * Returns all activities at the given day.
     * @param day a valid day as unix timestamp
     * @return the list of all activities from the history
     */
    private List<Activity> getActivitiesPerDay(final long day) {
        long normalized = normalizeDate(day);
        if (activityHistory.get(normalized) != null) {
            return activityHistory.get(normalized);
        } else {
            return null;
        }
    }

    /**
     * Returns all activities within the given timespan.
     * @param start a valid start day as unix timestamp
     * @param end a valid end day as unix timestamp
     * @return the list of all activities from the history
     */
    public List<Activity> getActivitiesForMultipleDays(final long start, final long end) {
        List<Activity> returnActivities = new ArrayList<>();
        long normalizedStart = normalizeDate(start);
        long normalizedEnd = normalizeDate(end);

        do {
            if (getActivitiesPerDay(normalizedStart) != null) {
                returnActivities.addAll(getActivitiesPerDay(normalizedStart));
            }
            normalizedStart += DAY_MILLISECONDS;
        } while (normalizedStart <= normalizedEnd);

        return returnActivities;
    }

    /**
     * Adds heart rates to the history.
     * @param day a valid day as unix timestamp
     * @param heartRates a list of heart rates to be added
     */
    public void addHeartRates(final long day, final List<HeartRate> heartRates) {
        this.heartRateHistory.put(normalizeDate(day), heartRates);
        this.trainingHRHistory.put(normalizeDate(day), determineRestHeartRate(day));
    }

    /**
     * Returns all heart rates at the given day.
     * @param day a valid day as unix timestamp
     * @return the list of all heart rates from the history
     */
    private List<HeartRate> getHeartRatesPerDay(final long day) {
        long normalized = normalizeDate(day);
        if (heartRateHistory.get(normalized) != null) {
            return heartRateHistory.get(normalized);
        } else {
            return null;
        }
    }

    /**
     * Returns all heart rates within the given timespan.
     * @param start a valid start day as unix timestamp
     * @param end a valid end day as unix timestamp
     * @return the list of all heart rates from the history
     */
    public List<HeartRate> getHeartRatesForMultipleDays(final long start, final long end) {
        List<HeartRate> returnHeartRates = new ArrayList<>();
        long normalizedStart = normalizeDate(start);
        long normalizedEnd = normalizeDate(end);

        do {
            if (getHeartRatesPerDay(normalizedStart) != null) {
                returnHeartRates.addAll(getHeartRatesPerDay(normalizedStart));
            }
            normalizedStart += DAY_MILLISECONDS;
        } while (normalizedStart <= normalizedEnd);

        return returnHeartRates;
    }

    /**
     * Adds activities to the history.
     * @param day a valid day as unix timestamp
     * @param activities a list of activities to be added
     */
    public void addActivities(final long day, final List<Activity> activities) {
        this.activityHistory.put(normalizeDate(day), activities);
    }

    /**
     * Gets the start of the day for the given unix timestamp.
     * @param day a valid unix timestamp
     * @return a normalized date as unix timestamp
     */
    private long normalizeDate(final long day) {
        Calendar cal = new GregorianCalendar();
        cal.setTimeInMillis(day);
        cal.set(Calendar.HOUR_OF_DAY, 0);
        cal.set(Calendar.MINUTE, 0);
        cal.set(Calendar.SECOND, 0);
        cal.set(Calendar.MILLISECOND, 0);
        return cal.getTimeInMillis();
    }

    /**
     * Determines the resting heart rate at a given day.
     * @param day a valid unix timestamp
     * @return an integer array containing the resting, target and maximum heart rate
     */
    private int[] determineRestHeartRate(final long day) {
        int[] hr = new int[3];
        long time = -1;
        List<Activity> activities = getActivityHistory(day);

        for (Activity act : activities) {
            Calendar cal = new GregorianCalendar();
            cal.setTimeInMillis(act.getEndTime());
            if (act.getActivityId() == ActivityTypes.SLEEPING
                    || act.getActivityId() == ActivityTypes.LIGHT_SLEEP
                    || act.getActivityId() == ActivityTypes.DEEP_SLEEP
                    || act.getActivityId() == ActivityTypes.REM_SLEEP
                    || act.getActivityId() == ActivityTypes.AWAKE_DURING_SLEEP) {
                time = act.getEndTime();
            }
        }

        final long wakeUpTime = time;
        List<Integer> rates = new ArrayList<>();

        if (time == -1) {
            getHeartRateHistory(day).forEach(r -> rates.add(r.getRate()));
        } else {
            getHeartRateHistory(day).forEach(r -> {
                if (r.getTimestamp() < wakeUpTime + UNIX_TIME_FIVE_MINUTES && r.getTimestamp() > wakeUpTime - UNIX_TIME_FIVE_MINUTES) {
                    rates.add(r.getRate());
                }
            });
        }

        Collections.sort(rates);

        double quantilePos = (rates.size() * 0.3);

        if (quantilePos % 1 == 0) {
            int a = rates.get((int) quantilePos - 1);
            int b = rates.get((int) quantilePos);
            hr[Constants.REST_HR] = (int) (0.5 * (a + b)); // 0,5*(x(np)*x(np+1))
        } else {
            int pos = (int) Math.floor(quantilePos); // floor(np) + 1
            hr[Constants.REST_HR] = rates.get(pos);
        }

        hr[Constants.MAX_HR] = MAXIMUM_HUMAN_HEART_RATE - age;
        //getHeartRateHistory(day).stream().max(Comparator.comparing(HeartRate::getRate)).get().getRate();
        hr[Constants.TARGET_HR] = hr[Constants.MAX_HR] - hr[Constants.REST_HR];

        return hr;
    }

    /**
     * Populates the activity history by fetching the location names of coordinates
     * from the Google Places API.
     */
    public void refineLocations() {
        for (Map.Entry<Long, List<Activity>> entry : activityHistory.entrySet()) {
            List<Coordinate> coords = getLocationsPerDay(entry.getKey());
            for (Activity act : entry.getValue()) {
                if (act.getActivityId() == ActivityTypes.STILL
                        || act.getActivityId() == ActivityTypes.UNKNOWN
                        || act.getActivityId() == ActivityTypes.SLEEPING
                        || act.getActivityId() == ActivityTypes.LIGHT_SLEEP
                        || act.getActivityId() == ActivityTypes.DEEP_SLEEP
                        || act.getActivityId() == ActivityTypes.REM_SLEEP
                        || act.getActivityId() == ActivityTypes.MEDITATION) {
                    List<Coordinate> activityCoords = new ArrayList<>();
                    long startTime = act.getStartTime();
                    long endTime = act.getEndTime();


                    for (Coordinate c : coords) {
                        if (c.getTimestamp() >= startTime && c.getTimestamp() <= endTime) {
                            activityCoords.add(c);
                        }
                    }

                    if (activityCoords.size() > 0) {
                        activityCoords.sort(Comparator.comparing(Coordinate::getAccuracy));
                        int threshold = activityCoords.get((int) (activityCoords.size() * 0.75)).getAccuracy();

                        activityCoords = activityCoords.stream().filter(c -> c.getAccuracy() <= threshold).collect(Collectors.toList());

                        double weightedLatitude = 0;
                        double weightedLongitude = 0;
                        double weight = 0;

                        for (Coordinate c : activityCoords) {
                            weightedLatitude += c.getLatitude() * c.getAccuracy();
                            weightedLongitude += c.getLongitude() * c.getAccuracy();
                            weight += c.getAccuracy();
                        }

                        final double lat = weightedLatitude / weight;
                        final double lng = weightedLongitude / weight;

                        int searchRadius = DEFAULT_RADIUS;
                        if (searchRadius < threshold) {
                            searchRadius = threshold;
                        }

                        String place = GooglePlaces.getInstance().atOwnPlaces(lat, lng, searchRadius);

                        if (place.equals("AWAY")) {
                            place = GooglePlaces.getInstance().isAtContact(lat, lng, searchRadius);

                            if (place.equals("AWAY")) {

                                PlacesSearchResult[] results = GooglePlaces.getInstance().getPlaces(lat, lng, searchRadius);

                                if (results != null) {
                                    if (results.length == 1) {
                                        place = results[0].name;
                                    } else {
                                        List<PlacesSearchResult> places = extractRealLocations(results);
                                        places.sort((PlacesSearchResult o1, PlacesSearchResult o2) -> {
                                            double o1Distance = GooglePlaces.getInstance().calculateDistance(
                                                    o1.geometry.location.lat,
                                                    o1.geometry.location.lng,
                                                    lat, lng);
                                            double o2Distance = GooglePlaces.getInstance().calculateDistance(
                                                    o2.geometry.location.lat,
                                                    o2.geometry.location.lng,
                                                    lat, lng);
                                            return Double.compare(o1Distance, o2Distance);
                                        });

                                        List<PlacesSearchResult> sportRelatedPlaces = getGymOrSportClub(places);
                                        GoogleMapsPlot.getInstance().addLocation(new LatLng(lat, lng));

                                        if (sportRelatedPlaces.size() == 1) {
                                            place = sportRelatedPlaces.get(0).name;
                                        } else {
                                            Location location = checkForResolvedLocations(lat, lng, searchRadius * 2);
                                            if (location != null) {
                                                place = location.getName();
                                            } else if (sportRelatedPlaces.size() > 1) {
                                                conflictLocations.put(new ConflictedLocationIdentifier(
                                                        act.getStartTime(),
                                                        new LatLng(lat, lng)),
                                                        sportRelatedPlaces);
                                                place = "CONFLICT";
                                            } else if (places.size() > 0) {
                                                results = GooglePlaces.getInstance().getPlaces(lat, lng, searchRadius * 2);
                                                places = extractRealLocations(results);
                                                places.sort((PlacesSearchResult o1, PlacesSearchResult o2) -> {
                                                    double o1Distance = GooglePlaces.getInstance().calculateDistance(
                                                            o1.geometry.location.lat,
                                                            o1.geometry.location.lng,
                                                            lat, lng);
                                                    double o2Distance = GooglePlaces.getInstance().calculateDistance(
                                                            o2.geometry.location.lat,
                                                            o2.geometry.location.lng,
                                                            lat, lng);
                                                    return Double.compare(o1Distance, o2Distance);
                                                });

                                                conflictLocations.put(new ConflictedLocationIdentifier(
                                                        act.getStartTime(),
                                                        new LatLng(lat, lng)),
                                                        places);

                                                place = "CONFLICT";
                                            } else {
                                                place = "UNKNOWN";
                                            }
                                        }
                                    }
                                } else {
                                    place = "UNKNOWN";
                                }
                            }
                        }
                        act.setLocation(place);
                    }
                    GoogleMapsPlot.getInstance().addLocationNames(act.getLocation());
                }
            }
        }
    }

    /**
     * Checks if the given location can be resolved automatically by defining
     * the location and a search radius for querying the Google Places API.
     * @param lat a valid latitude
     * @param lng a valid longitude
     * @param searchRadius a search radius in meters
     * @return the resolved location, otherwise null if the location could not be resolved
     */
    private Location checkForResolvedLocations(final double lat, final double lng, final int searchRadius) {
        List<Location> locations = ResolvedLocations.getInstance().getLocations();
        if (!locations.isEmpty()) {
            GooglePlaces instance = GooglePlaces.getInstance();
            for (Location loc : locations) {
                if (instance.calculateDistance(loc.getCoordinate().lat, loc.getCoordinate().lng, lat, lng) <= searchRadius) {
                    return loc;
                }
            }
        }

        return null;
    }

    /**
     * Checks if the given places are gyms or sports clubs by querying the Google Places API.
     * @param places a list of places search results
     * @return the list of places search results containing only results that are gyms or sports clubs
     */
    private List<PlacesSearchResult> getGymOrSportClub(final List<PlacesSearchResult> places) {
        List<PlacesSearchResult> sportRelatedPlaces = new ArrayList<>();

        for (PlacesSearchResult sr : places) {
            boolean isSportRelated = false;
            for (String st : sr.types) {
                if (st.equals("gym")) {
                    isSportRelated = true;
                }
            }

            if (isSportRelated
                    || sr.name.toLowerCase().contains("verein")
                    || sr.name.toLowerCase().contains("club")
                    || sr.name.toLowerCase().contains("bad")) {
                sportRelatedPlaces.add(sr);
            }
        }

        return sportRelatedPlaces;
    }

    /**
     * Filters the real locations from the given places search results.
     * @param places a list of places search results
     * @return the filtered search results list
     */
    private List<PlacesSearchResult> extractRealLocations(final PlacesSearchResult[] places) {
        List<PlacesSearchResult> results = new ArrayList<>();

        for (PlacesSearchResult sr : places) {
            boolean isPolitical = false;
            for (String st : sr.types) {
                if (st.toLowerCase().equals("political")
                        || st.toLowerCase().equals("route")
                        || st.toLowerCase().equals("locality")
                        || st.toLowerCase().equals("street_address")) {
                    isPolitical = true;
                }
            }

            if (!isPolitical) {
                results.add(sr);
            }
        }

        return results;
    }

    /**
     * Determines how intense the activity was by comparing the heart rate to previous data.
     */
    public void determineActivityIntensity() {
        for (Map.Entry<Long, List<Activity>> entry : activityHistory.entrySet()) {
            long day = entry.getKey();
            for (Activity act : entry.getValue()) {
                if (act.getEndTime() - act.getStartTime() >= UNIX_TIME_TWENTY_MINUTES) {
                    act.setHeartRate(getMinMaxAvgHeartRate(act.getStartTime(), act.getEndTime()));
                    if (act.getHeartRate()[Constants.MIN_HR] != -1) {
                        int[] trainingHR = trainingHRHistory.get(day);
                        int targetHR = trainingHR[Constants.TARGET_HR];
                        int restHR = trainingHR[Constants.REST_HR];
                        int averageHR = act.getHeartRate()[Constants.AVG_HR];

                        if ((0.6 * targetHR) + restHR <= averageHR && (0.7 * targetHR) + restHR > averageHR) {
                            act.setIntensity(1);
                        } else if ((0.7 * targetHR) + restHR <= averageHR && (0.75 * targetHR) + restHR >= averageHR) {
                            act.setIntensity(2);
                        } else if ((0.75 * targetHR) + restHR < averageHR && (0.84 * targetHR) + restHR >= averageHR) {
                            act.setIntensity(3);
                        } else if ((0.84 * targetHR) + restHR < averageHR && (0.88 * targetHR) + restHR >= averageHR) {
                            act.setIntensity(4);
                        } else if ((0.88 * targetHR) + restHR < averageHR && (0.92 * targetHR) + restHR >= averageHR) {
                            act.setIntensity(5);
                        } else {
                            act.setIntensity(0);
                        }
                    }
                }
            }
        }
    }

    /**
     * Returns the minimum, maximum and average heart rate from
     * the heart rate history between the given timestamps.
     * @param start a valid start day as unix timestamp
     * @param end a valid end day as unix timestamp
     * @return an integer array containing the minimum, maximum and average heart rate
     */
    private int[] getMinMaxAvgHeartRate(final long start, final long end) {
        int[] heartRate = new int[]{-1, -1, -1};
        List<Integer> rates = new ArrayList<>();

        if (getHeartRateHistory(start) != null) {
            getHeartRateHistory(start).forEach(r -> {
                if (r.getTimestamp() >= start && r.getTimestamp() <= end) {
                    rates.add(r.getRate());
                }
            });

            heartRate[Constants.MAX_HR] = rates.stream().max(Comparator.naturalOrder()).get();
            heartRate[Constants.MIN_HR] = rates.stream().min(Comparator.reverseOrder()).get();
            heartRate[Constants.AVG_HR] = (int) rates.stream().mapToInt(Integer::intValue).average().getAsDouble();
        }

        return heartRate;
    }

    /**
     * Setter for the age param.
     * @param age the user's age
     */
    public void setAge(final int age) {
        this.age = age;
    }

    /**
     * Exports the activity location history to a json file.
     */
    public void export() {
        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        String output = gson.toJson(this);

        File file = new File("activity_location_history_"
                + activityHistory.keySet().toArray()[0]
                + "-"
                + activityHistory.keySet().toArray()[activityHistory.size() - 1]
                + ".json");

        try {
            OutputStreamWriter writer = new OutputStreamWriter(new FileOutputStream(file, false),
                    StandardCharsets.UTF_8);
            writer.write(output);
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Getter for the activity history at a specific day.
     * @param day a valid day unix timestamp.
     * @return the activities at the given day.
     */
    public List<Activity> getActivityHistory(final long day) {
        return activityHistory.get(normalizeDate(day));
    }

    /**
     * Getter for the heart rate history at a specific day.
     * @param day a valid day unix timestamp.
     * @return the heart rate at the given day.
     */
    public List<HeartRate> getHeartRateHistory(final long day) {
        return heartRateHistory.get(normalizeDate(day));
    }

    /**
     * Getter for the conflicting activities/locations.
     * @return the conflicting locations.
     */
    public Map<ConflictedLocationIdentifier, List<PlacesSearchResult>> getConflictedActivities() {
        return conflictLocations;
    }

}

