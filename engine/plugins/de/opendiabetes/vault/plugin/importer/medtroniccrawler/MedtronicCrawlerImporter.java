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
package de.opendiabetes.vault.plugin.importer.medtroniccrawler;

import de.opendiabetes.vault.container.VaultEntry;
import de.opendiabetes.vault.plugin.crawlerimporter.AbstractCrawlerImporter;
import de.opendiabetes.vault.plugin.fileimporter.FileImporter;
import de.opendiabetes.vault.plugin.management.OpenDiabetesPluginManager;
import org.pf4j.Plugin;
import org.pf4j.PluginWrapper;
import org.pf4j.Extension;

import java.io.File;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Properties;
import java.util.logging.FileHandler;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Wrapper class for the MedtronicCrawlerImporter plugin.
 */
public class MedtronicCrawlerImporter extends Plugin {

    /**
     * Constructor for the PluginManager.
     *
     * @param wrapper The PluginWrapper.
     */
    public MedtronicCrawlerImporter(final PluginWrapper wrapper) {
        super(wrapper);
    }

    /**
     * Actual implementation of the MedtronicCrawlerImporter plugin.
     */
    @Extension
    public static class MedtronicCrawlerImporterImplementation extends AbstractCrawlerImporter {

        /**
         * Progress percentage for showing that the configuration has been loaded.
         */
        private static final int PROGRESS_CONFIG_LOADED = 25;

        /**
         * Date string from when the data should start.
         */
        private String fromDate;

        /**
         * Date string until when the data should be imported.
         */
        private String toDate;

        /**
         * Constructor.
         *
         * @throws Exception Thrown if the log file could not be written.
         */
        public MedtronicCrawlerImporterImplementation() throws Exception {
            Logger logger = Logger.getLogger("MyLog");
            FileHandler fh;
            SimpleDateFormat formats = new SimpleDateFormat("dd-mm-HHMMSS");

        }

        /**
         * {@inheritDoc}
         */
        @Override
        public List<VaultEntry> importData(final String username, final String password) throws Exception {

            Authentication auth = new Authentication();
            if (!auth.checkConnection(username, password)) {
                LOG.log(Level.SEVERE, "Entered username/password are incorrect");
                return null;
            }
            String lang = auth.getLanguage();

            try {
                DateHelper dateHelper = new DateHelper(lang);
                if (!dateHelper.getStartDate(fromDate)) {
                    LOG.log(Level.SEVERE, "fromDate is incorrect");
                    return null;
                }

                if (!dateHelper.getEndDate(fromDate, toDate)) {
                    LOG.log(Level.SEVERE, "toDate is incorrect");
                    return null;
                }
            } catch (ParseException e) {
                LOG.log(Level.SEVERE, "Date parsing failed");
                return null;
            }

            Crawler crawler = new Crawler();

            String exportPath = System.getProperty("java.io.tmpdir") + "MedtronicCrawler";
            try {
                crawler.generateDocument(auth.getCookies(), fromDate, toDate, exportPath);
            } catch (Exception exception) {
                LOG.log(Level.SEVERE, "Error while crawling data.");
                return null;
            }


            String path = exportPath + File.separator + "careLink-Export";

            OpenDiabetesPluginManager manager = OpenDiabetesPluginManager.getInstance();
            return manager.getPluginFromString(FileImporter.class, "MedtronicImporter").importData(path);
        }

        /**
         * Template method to load plugin specific configurations from the config file.
         *
         * @param configuration The configuration object.
         * @return whether a valid configuration could be read from the config file
         */
        @Override
        protected boolean loadPluginSpecificConfiguration(final Properties configuration) {
            if (configuration.containsKey("fromDate")) {
                this.fromDate = configuration.getProperty("fromDate");
            }
            if (configuration.containsKey("toDate")) {
                this.toDate = configuration.getProperty("toDate");
            }
            this.notifyStatus(PROGRESS_CONFIG_LOADED, "Loaded configuration");
            return false;
        }
    }
}
