/*
 * Copyright (C) 2017 OpenDiabetes
 * <p>
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 * <p>
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * <p>
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package de.opendiabetes.vault.plugin.importer.googlecrawler;

import de.opendiabetes.vault.container.VaultEntry;
import de.opendiabetes.vault.plugin.crawlerimporter.AbstractCrawlerImporter;
import de.opendiabetes.vault.plugin.importer.googlecrawler.fitness.GoogleFitness;
import de.opendiabetes.vault.plugin.importer.googlecrawler.helper.Credentials;
import de.opendiabetes.vault.plugin.importer.googlecrawler.javaFX.views.ConflictedLocations;
import de.opendiabetes.vault.plugin.importer.googlecrawler.location.LocationHistory;
import de.opendiabetes.vault.plugin.importer.googlecrawler.people.GooglePeople;
import de.opendiabetes.vault.plugin.importer.googlecrawler.plot.GoogleMapsPlot;
import de.opendiabetes.vault.plugin.importer.googlecrawler.plot.Plotter;
import org.pf4j.Extension;
import org.pf4j.Plugin;
import org.pf4j.PluginWrapper;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.Properties;
import java.util.logging.Level;

/**
 * Wrapper class for the GoogleFitCrawlerImporter plugin.
 *
 * @author ocastx
 */
public class GoogleFitCrawlerImporter extends Plugin {

    /**
     * Constructor for the PluginManager.
     *
     * @param wrapper The PluginWrapper.
     */
    public GoogleFitCrawlerImporter(final PluginWrapper wrapper) {
        super(wrapper);
    }

    /**
     * Actual implementation of the Medtronic importer plugin.
     */
    @Extension
    public static final class GoogleFitCrawlerImporterImplementation extends AbstractCrawlerImporter {

        /**
         * The minimum default year for gathering data.
         */
        private static final int DEFAULT_MIN_YEAR = 2014;

        /**
         * Path to the file holding the client credentials exported from google.
         * See https://console.developers.google.com/apis/credentials
         */
        private String clientSecretPath;

        /**
         * API Key provided by Google.
         * See https://console.developers.google.com/apis/credentials
         */
        private String apiKey;

        /**
         * Age to calculate the target heart rate.
         */
        private int age;

        /**
         * Timeframe that should be looked at.
         * Either a day dd.mm.yyyy or a longer
         * period with dd.mm.yyyy-dd.mm.yyyy or write "all" to get all data
         */
        private String timeframe;

        /**
         * Further search parameters for sports location,
         * already included params are: verein, club, sport and bad.
         */
        private String[] keywordSearchParams;

        /**
         * Export whole history as json to the current working directory.
         */
        private boolean exportHistory;

        /**
         * Plot chosen time period, select a day with dd.mm.yyyy or a timeframe with dd.mm.yyyy-dd.mm.yyyy.
         */
        private String plotTimeframe;

        /**
         * Export plot as png to the current working directory.
         * Property "plotTimeframe" has to be set to use this.
         */
        private boolean exportPlot;

        /**
         * View the plot in a separate window.
         * Property "plotTimeframe" has to be set to use this.
         */
        private boolean viewPlot;

        /**
         * View the map in a separate window.
         */
        private boolean viewMap;

        /**
         * Constructor.
         */
        public GoogleFitCrawlerImporterImplementation() {


        }

        /**
         * {@inheritDoc}
         */
        @Override
        public List<VaultEntry> importData(final String username, final String password) throws  Exception {

            Credentials credentialsInstance = Credentials.getInstance();

            try {

                if (clientSecretPath != null) {
                    credentialsInstance.authorize(clientSecretPath);
                }

                if (apiKey != null) {
                    credentialsInstance.setAPIkey(apiKey);
                }

                LocationHistory.getInstance().setAge(age);
                GooglePeople.getInstance().getAllProfiles();

                // if (keywordSearchParams != null) {
                //    GooglePlaces.getInstance().setKeywordSearchParams(keywordSearchParams);
                // }

                if (timeframe != null) {
                    Calendar start = new GregorianCalendar();
                    Calendar end = new GregorianCalendar();

                    if (timeframe.equals("all")) {
                        start.set(DEFAULT_MIN_YEAR, Calendar.JANUARY, 1, 0, 0, 0);
                        start.set(Calendar.MILLISECOND, 0);
                        end = GregorianCalendar.getInstance();


                        System.out.println("set all as timeframe");
                    } else if (timeframe.contains("-")) {
                        String[] help = timeframe.split("-");

                        String[] startDate = help[0].split("\\.");
                        start.set(Integer.parseInt(startDate[2]),
                                Integer.parseInt(startDate[1]) - 1,
                                Integer.parseInt(startDate[0]), 0, 0, 0);
                        start.set(Calendar.MILLISECOND, 0);

                        String[] endDate = help[1].split("\\.");
                        end.set(Integer.parseInt(endDate[2]), Integer.parseInt(endDate[1]) - 1, Integer.parseInt(endDate[0]), 0, 0, 0);
                        end.set(Calendar.MILLISECOND, 0);
                    } else {
                        String[] date = timeframe.split("\\.");
                        start.set(Integer.parseInt(date[2]), Integer.parseInt(date[1]) - 1, Integer.parseInt(date[0]), 0, 0, 0);
                        start.set(Calendar.MILLISECOND, 0);

                        end.set(Integer.parseInt(date[2]), Integer.parseInt(date[1]) - 1, Integer.parseInt(date[0]), 0, 0, 0);
                        end.set(Calendar.MILLISECOND, 0);
                    }

                    GoogleFitness.getInstance().fetchData(start.getTimeInMillis(), end.getTimeInMillis());
                }


                if (exportHistory) {
                    LocationHistory.getInstance().export();
                }

                if (plotTimeframe != null) {
                    Plotter plot = new Plotter(plotTimeframe);

                    if (exportPlot) {
                        plot.export();
                    }

                    if (viewPlot) {
                        plot.viewPlot();
                    }

                }

                if (viewMap) {
                    GoogleMapsPlot.getInstance().createMap();
                    GoogleMapsPlot.getInstance().openMap();
                }

                if (!LocationHistory.getInstance().getConflictedActivities().isEmpty()) {
                    ConflictedLocations.main(new String[]{});
                }

            } catch (RuntimeException exception) {
              throw exception;
            } catch (Exception exception) {
                LOG.log(Level.SEVERE, "Exception happened: " + exception);
                return null;
            }

            return new ArrayList<>();
        }

        /**
         * {@inheritDoc}
         */
        @Override
        public boolean loadPluginSpecificConfiguration(final Properties configuration) {
            if (configuration.containsKey("clientSecretPath")) {
                clientSecretPath = configuration.getProperty("clientSecretPath");
            }
            if (configuration.containsKey("apiKey")) {
                apiKey = configuration.getProperty("apiKey");
            }
            if (configuration.containsKey("age")) {
                age = Integer.parseInt(configuration.getProperty("age"));
            }
            if (configuration.containsKey("timeframe")) {
                timeframe = configuration.getProperty("timeframe");
            }
            // if (configuration.containsKey("keywordSearchParams")) {
            //    keywordSearchParams = configuration.getProperty("keywordSearchParams").split(",");
            // }
            if (configuration.containsKey("exportHistory")) {
                exportHistory = Boolean.parseBoolean(configuration.getProperty("exportHistory"));
            }
            if (configuration.containsKey("plotTimeframe")) {
                plotTimeframe = configuration.getProperty("plotTimeframe");
            }
            if (configuration.containsKey("exportPlot")) {
                exportPlot = Boolean.parseBoolean(configuration.getProperty("exportPlot"));
            }
            if (configuration.containsKey("viewPlot")) {
                viewPlot = Boolean.parseBoolean(configuration.getProperty("viewPlot"));
            }
            if (configuration.containsKey("viewMap")) {
                viewMap = Boolean.parseBoolean(configuration.getProperty("viewMap"));
            }
            return true;
        }

    }
}
