/*
 * Copyright (C) 2017 OpenDiabetes
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package de.opendiabetes.vault.plugin.interpreter.dateInterpreter;

import de.opendiabetes.vault.container.VaultEntry;
import de.opendiabetes.vault.plugin.interpreter.VaultInterpreter;
import org.pf4j.Extension;
import org.pf4j.Plugin;
import org.pf4j.PluginWrapper;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Properties;

/**
 * Wrapper class for the DateInterpreter plugin.
 *
 * @author Magnus Gärtner
 */
public class DateInterpreter extends Plugin {

    /**
     * Constructor for the PluginManager.
     *
     * @param wrapper The PluginWrapper.
     */
    public DateInterpreter(final PluginWrapper wrapper) {
        super(wrapper);
    }

    /**
     * Actual implementation of the DateInterpreter plugin.
     */
    @Extension
    public static class DateInterpreterImplementation extends VaultInterpreter {

        /**
         * Option which indicates whether the import is period restricted.
         */
        private boolean isImportPeriodRestricted;

        /**
         * The date which the import is period restricted from.
         */
        private Date importPeriodFrom;

        /**
         * The date which the import is period restricted to.
         */
        private Date importPeriodTo;

        /**
         * Interprets the given List of VaultEntry and returns a List of VaultEntry containing only entries
         * from the specified period of time.
         * {@inheritDoc}
         */
        @Override
        public List<VaultEntry> interpret(final List<VaultEntry> input) {
            if (isImportPeriodRestricted) {
                List<VaultEntry> retVal = new ArrayList<>();
                for (VaultEntry item : input) {
                    if (item.getTimestamp().after(importPeriodFrom)
                            && item.getTimestamp().before(importPeriodTo)) {
                        retVal.add(item);
                    }
                }
                return retVal;
            } else {
                return input;
            }

        }

        /**
         * Template method to load plugin specific configurations from the config file.
         *
         * @param configuration The configuration object.
         * @return wheter a valid configuration could be read from the config file
         */
        @Override
        protected boolean loadPluginSpecificConfiguration(final Properties configuration) {
            if (!configuration.containsKey("ImportPeriodRestricted")) {
                return false;
            }
            String restriction = configuration.getProperty("ImportPeriodRestricted");
            isImportPeriodRestricted = Boolean.parseBoolean(restriction);

            if (!isImportPeriodRestricted) {
                return true;
            }
            if (!configuration.containsKey("importPeriodFrom") || !configuration.containsKey("importPeriodTo")) {
                return false;
            }

            SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");
            try {
                importPeriodFrom = dateFormat.parse(configuration.getProperty("importPeriodFrom"));
                importPeriodTo = dateFormat.parse(configuration.getProperty("importPeriodTo"));
            } catch (ParseException e) {
                return false;
            }
            return true;
        }
    }
}
