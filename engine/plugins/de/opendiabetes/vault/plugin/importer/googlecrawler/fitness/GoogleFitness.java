package de.opendiabetes.vault.plugin.importer.googlecrawler.fitness;

import com.google.api.services.fitness.Fitness;
import com.google.api.services.fitness.model.AggregateBy;
import com.google.api.services.fitness.model.AggregateRequest;
import com.google.api.services.fitness.model.AggregateResponse;
import com.google.api.services.fitness.model.DataPoint;
import de.opendiabetes.vault.plugin.importer.googlecrawler.helper.Credentials;
import de.opendiabetes.vault.plugin.importer.googlecrawler.location.LocationHistory;
import de.opendiabetes.vault.plugin.importer.googlecrawler.models.Activity;
import de.opendiabetes.vault.plugin.importer.googlecrawler.models.Coordinate;
import de.opendiabetes.vault.plugin.importer.googlecrawler.models.HeartRate;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.List;

/**
 * Google Fitness class for retrieving data from the Google Fitness API.
 *
 * @author ocastx
 */
public final class GoogleFitness {

    /**
     * The number of milliseconds a day lasts.
     */
    private static final int DAY_MILLISECONDS = 86400000;

    /**
     * Divisor to convert nanoseconds to milliseconds.
     */
    private static final int NANO_TO_MILLISECONDS_DIVISOR = 1000000;

    /**
     * Five minutes represented in a unix timestamp.
     */
    private static final int UNIX_TIME_FIVE_MINUTES = 300000;

    /**
     * The last hour of a 24h format day.
     */
    private static final int DAY_LAST_HOUR = 23;

    /**
     * The last minute of an hour.
     */
    private static final int LAST_MINUTE = 59;

    /**
     * The last second of a minute.
     */
    private static final int LAST_SECOND = 59;

    /**
     * Singleton instance of the GoogleFitness Crawler.
     */
    private static GoogleFitness instance;

    /**
     * Fitness Service constructed by the Google service.
     */
    private Fitness fitnessService;

    /**
     * Constructor.
     */
    private GoogleFitness() {
        construct();
    }

    /**
     * Getter for the singleton instance.
     * @return the singleton
     */
    public static GoogleFitness getInstance() {
        if (GoogleFitness.instance == null) {
            GoogleFitness.instance = new GoogleFitness();
        }
        return GoogleFitness.instance;
    }

    /**
     * Initiates the Google Fitness Service.
     */
    private void construct() {
        fitnessService = new Fitness.Builder(
                Credentials.getInstance().getHttpTransport(),
                Credentials.getInstance().getJsonFactory(),
                Credentials.getInstance().getCredential())
                .setApplicationName(Credentials.getInstance().getApplicationName())
                .build();
    }

    /**
     * Fetches the data between the given date timestamps.
     * @param start - beginning date of the data as a unix timestamp
     * @param end - end date of the data as a unix timestamp
     */
    public void fetchData(final long start, final long end) {
        long startIterator = start;
        while (startIterator <= end) {
            fetchActivitiesPerDay(start);
            fetchLocationsPerDay(start);
            fetchHeartRatePerDay(start);
            startIterator += DAY_MILLISECONDS;
        }

        LocationHistory.getInstance().refineLocations();
        LocationHistory.getInstance().determineActivityIntensity();
    }

    /**
     * Fetches all activities at a specific day.
     * @param day - a date as unix timestamp
     */
    private void fetchActivitiesPerDay(final long day) {
        long[] startEnd = getStartEndDay(day);

        AggregateBy aggregate = new AggregateBy();
        aggregate.setDataTypeName("com.google.activity.segment");

        AggregateRequest agg = new AggregateRequest();
        agg.setStartTimeMillis(startEnd[0]);
        agg.setEndTimeMillis(startEnd[1]);
        agg.setAggregateBy(Arrays.asList(aggregate));

        try {
            Fitness.Users.Dataset.Aggregate request = fitnessService.users().dataset().aggregate("me", agg);
            AggregateResponse rep = request.execute();

            List<DataPoint> activitiesByDataSource = rep.getBucket().get(0).getDataset().get(0).getPoint();
            List<Activity> activities = new ArrayList<>();
            for (DataPoint dp : activitiesByDataSource) {
                activities.add(new Activity(
                        dp.getStartTimeNanos() / NANO_TO_MILLISECONDS_DIVISOR,
                        dp.getEndTimeNanos() / NANO_TO_MILLISECONDS_DIVISOR,
                        dp.getValue().get(0).getIntVal()));
            }

            for (int i = 0; i < activities.size() - 1; i++) {
                Activity currentActivity = activities.get(i);
                Activity nextActivity = activities.get(i + 1);

                if (currentActivity.getActivityId() == nextActivity.getActivityId()
                        && currentActivity.getEndTime() - nextActivity.getStartTime() <= UNIX_TIME_FIVE_MINUTES) {
                    currentActivity.setEndTime(nextActivity.getEndTime());
                    activities.remove(i + 1);
                }
            }

            LocationHistory.getInstance().addActivities(startEnd[0], activities);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Fetches all locations at a specific day.
     * @param day - a date as unix timestamp
     */
    private void fetchLocationsPerDay(final long day) {
        long[] startEnd = getStartEndDay(day);

        AggregateBy aggregate = new AggregateBy();
        aggregate.setDataTypeName("com.google.location.sample");

        AggregateRequest agg = new AggregateRequest();
        agg.setStartTimeMillis(startEnd[0]);
        agg.setEndTimeMillis(startEnd[1]);
        agg.setAggregateBy(Arrays.asList(aggregate));

        try {
            Fitness.Users.Dataset.Aggregate request = fitnessService.users().dataset().aggregate("me", agg);
            AggregateResponse rep = request.execute();

            List<DataPoint> activitiesByDataSource = rep.getBucket().get(0).getDataset().get(0).getPoint();
            List<Coordinate> locations = new ArrayList<>();
            for (DataPoint dp : activitiesByDataSource) {
                Coordinate coord = new Coordinate(
                        dp.getStartTimeNanos() / NANO_TO_MILLISECONDS_DIVISOR,
                        dp.getValue().get(1).getFpVal(), dp.getValue().get(0).getFpVal(),
                        (int) Math.ceil(dp.getValue().get(2).getFpVal()));
                if (dp.getValue().size() > 3) {
                    coord.setAltitude(dp.getValue().get(3).getFpVal().intValue());
                }
                locations.add(coord);
            }

            LocationHistory.getInstance().addLocations(startEnd[0], locations);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Fetches the heart rate at a specific day.
     * @param day - a date as unix timestamp
     */
    private void fetchHeartRatePerDay(final long day) {
        long[] startEnd = getStartEndDay(day);

        AggregateBy aggregate = new AggregateBy();
        aggregate.setDataTypeName("com.google.heart_rate.bpm");

        AggregateRequest aggregateRequest = new AggregateRequest();
        aggregateRequest.setStartTimeMillis(startEnd[0]);
        aggregateRequest.setEndTimeMillis(startEnd[1]);
        aggregateRequest.setAggregateBy(Arrays.asList(aggregate));


        try {
            Fitness.Users.Dataset.Aggregate request = fitnessService.users().dataset().aggregate("me", aggregateRequest);
            AggregateResponse rep = request.execute();

            if (rep.getBucket().get(0).getDataset().get(0).getPoint().size() != 0) {
                List<DataPoint> activitiesByDataSource = rep.getBucket().get(0).getDataset().get(0).getPoint();
                List<HeartRate> heartRates = new ArrayList<>();
                for (DataPoint dp : activitiesByDataSource) {
                    heartRates.add(new HeartRate(
                            dp.getStartTimeNanos() / NANO_TO_MILLISECONDS_DIVISOR,
                            dp.getValue().get(0).getIntVal()));
                }

                LocationHistory.getInstance().addHeartRates(startEnd[0], heartRates);
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Returns the beginning and the end unix timestamp of the date which the given timestamp defines.
     * @param day - a date as unix timestamp
     * @return the two unix timestamps
     */
    private long[] getStartEndDay(final long day) {
        long[] startEnd = new long[2];

        Calendar cal = new GregorianCalendar();
        cal.setTimeInMillis(day);
        cal.set(Calendar.HOUR_OF_DAY, 0);
        cal.set(Calendar.MINUTE, 0);
        cal.set(Calendar.SECOND, 0);
        startEnd[0] = cal.getTimeInMillis();

        cal.set(Calendar.HOUR_OF_DAY, DAY_LAST_HOUR);
        cal.set(Calendar.MINUTE, LAST_MINUTE);
        cal.set(Calendar.SECOND, LAST_SECOND);
        startEnd[1] = cal.getTimeInMillis();

        return startEnd;
    }
}
