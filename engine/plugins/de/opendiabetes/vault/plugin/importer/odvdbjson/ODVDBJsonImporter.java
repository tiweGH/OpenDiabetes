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
package de.opendiabetes.vault.plugin.importer.odvdbjson;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import de.opendiabetes.vault.container.VaultEntry;
import de.opendiabetes.vault.container.VaultEntryGsonAdapter;
import de.opendiabetes.vault.plugin.fileimporter.AbstractFileImporter;
import org.pf4j.Extension;
import org.pf4j.Plugin;
import org.pf4j.PluginWrapper;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
import java.util.logging.Level;

/**
 * Wrapper class for the ODVDBJsonImporter plugin.
 *
 * @author Lucas Buschlinger
 */
public class ODVDBJsonImporter extends Plugin {

    /**
     * Constructor for the PluginManager.
     *
     * @param wrapper The PluginWrapper.
     */
    public ODVDBJsonImporter(final PluginWrapper wrapper) {
        super(wrapper);
    }

    /**
     * Actual implementation of the ODVDBJson importer plugin.
     */
    @Extension
    public static final class ODVDBJsonImporterImplementation extends AbstractFileImporter {

        /**
         * Empty preprocessing for ODVDB Json data, as it is not necessary for this type of data.
         *
         * @param filePath Path to the import file.
         */
        @Override
        protected void preprocessingIfNeeded(final String filePath) {
        }

        /**
         * {@inheritDoc}
         */
        @Override
        protected List<VaultEntry> processImport(final InputStream fileInputStream, final String filenameForLogging)
                throws UnsupportedEncodingException {
            // prepare libs
            GsonBuilder builder = new GsonBuilder();
            builder.registerTypeAdapter(VaultEntry.class, new VaultEntryGsonAdapter());
            Gson gson = builder.create();

            // open stream
            BufferedReader reader = new BufferedReader(new InputStreamReader(fileInputStream, "UTF-8"));

            // import
            Type listType = new TypeToken<ArrayList<VaultEntry>>() {
            }.getType();
            List<VaultEntry> importDb = gson.fromJson(reader, listType);

            if (importDb != null && !importDb.isEmpty()) {
                LOG.log(Level.FINE, "Successfully imported json file.");
                return importDb;
            }
            LOG.log(Level.SEVERE, "Got no data from json import.");
            return null;
        }

        /**
         * {@inheritDoc}
         */
        @Override
        public boolean loadPluginSpecificConfiguration(final Properties configuration) {
            return true;
        }

    }
}
