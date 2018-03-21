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
package de.opendiabetes.vault.plugin.exporter.slicelayoutcsv;

import de.opendiabetes.vault.container.SliceEntry;
import de.opendiabetes.vault.container.VaultEntry;
import de.opendiabetes.vault.container.csv.ExportEntry;
import de.opendiabetes.vault.container.csv.SliceCsVEntry;
import de.opendiabetes.vault.plugin.exporter.CSVFileExporter;
import org.pf4j.Extension;
import org.pf4j.Plugin;
import org.pf4j.PluginWrapper;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;

/**
 * Wrapper class for the SliceLayoutCSVExporter plugin.
 *
 * @author Lucas Buschlinger
 */
public class SliceLayoutCSVExporter extends Plugin {

    /**
     * Constructor used by the {@link org.pf4j.PluginManager} to instantiate.
     *
     * @param wrapper The {@link PluginWrapper}.
     */
    public SliceLayoutCSVExporter(final PluginWrapper wrapper) {
        super(wrapper);
    }

    /**
     * Actual implementation of the SliceLayoutCSVExporter.
     */
    @Extension
    public static final class SliceLayoutCSVExporterImplementation extends CSVFileExporter {

        /**
         * The entries to be exported by the SliceLayoutCSVExporter plugins.
         */
        private List<SliceEntry> entries;


        /**
         * {@inheritDoc}
         */
        @Override
        protected List<ExportEntry> prepareData(final List<VaultEntry> data) {
            List<ExportEntry> retVal = new ArrayList<>();
            for (SliceEntry item : entries) {
                retVal.add(new SliceCsVEntry(item));
            }
            return retVal;
        }

        /**
         * This sets the List of {@link SliceEntry} which will be exported by this exporter.
         *
         * @param entries The entries which will be exported by the SliceLayoutCSVExporter.
         * @throws IllegalArgumentException Thrown if the entries of the supplied list are not of type {@link SliceEntry}
         */
        @Override
        public void setEntries(final List<?> entries) throws IllegalArgumentException {
            if (entries != null && !entries.isEmpty()) {
                if (entries.get(0) instanceof SliceEntry) {
                    this.entries = (List<SliceEntry>) entries;
                } else {
                    LOG.log(Level.SEVERE, "Entries are not of type SliceEntry");
                    throw new IllegalArgumentException("Entries are not of type SliceEntry");
                }
            } else {
                LOG.log(Level.SEVERE, "No data supplied to be set");
            }
        }

    }
}
