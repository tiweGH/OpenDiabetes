/*
 * Copyright (C) 2017 juehv
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
package de.opendiabetes.vault.plugin.exporter.odvdbjsonexporter;

import de.opendiabetes.vault.container.VaultEntry;
import de.opendiabetes.vault.container.csv.ExportEntry;
import de.opendiabetes.vault.container.csv.OdvDbJsonPseudoEntry;
import de.opendiabetes.vault.plugin.exporter.FileExporter;
import org.pf4j.Extension;
import org.pf4j.Plugin;
import org.pf4j.PluginWrapper;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;

/**
 * Wrapper class for the OdvDbJsonExporter plugin.
 *
 * @author juehv
 */
public class OdvDbJsonExporter extends Plugin {

    /**
     * Constructor for the {@link org.pf4j.PluginManager}.
     *
     * @param wrapper The {@link org.pf4j.PluginWrapper}.
     */
    public OdvDbJsonExporter(final PluginWrapper wrapper) {
        super(wrapper);
    }

    /**
     * Actual implementation of the OdvDbJsonExporter.
     */
    @Extension
    public static final class OdvDbJsonExporterImplementation extends FileExporter {

        /**
         * Prepares data for the export by putting it into exportable containers.
         *
         * @param data The data to be prepared.
         * @return The data in exportable containers.
         */
        @Override
        protected List<ExportEntry> prepareData(final List<VaultEntry> data) {
            List<ExportEntry> container = new ArrayList<>();
            List<VaultEntry> tempData;
            if (getIsPeriodRestricted()) {
               tempData = filterPeriodRestriction(data);
            } else {
                tempData = data;
            }
            container.add(OdvDbJsonPseudoEntry.fromVaultEntryList(tempData));
            return container;
        }

        /**
         * Unused, thus unimplemented.
         *
         * @param entries Nothing here.
         * @throws IllegalArgumentException No thrown as this will not change the state of the exporter.
         */
        @Override
        public void setEntries(final List<?> entries) throws IllegalArgumentException {
            LOG.log(Level.WARNING, "Tried to set entries but this it not possible with this exporter");
        }

    }
}
