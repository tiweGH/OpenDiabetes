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
package de.opendiabetes.vault.plugin.exporter;


import de.opendiabetes.vault.container.VaultEntry;
import de.opendiabetes.vault.container.csv.ExportEntry;
import de.opendiabetes.vault.plugin.common.AbstractPlugin;
import java.io.IOException;
import java.util.List;

/**
 * Most abstract exporter, implements the {@link Exporter} interface.
 * All actual exporter plugins are descendants of this class.
 * Supplies the Logger {@link Exporter#LOG}-
 * Takes care of handling status listeners {@link AbstractExporter#listeners}.
 *
 * @author Lucas Buschlinger
 */
public abstract class AbstractExporter extends AbstractPlugin implements  Exporter {

    /**

     * Writes the export data to the file.
     *
     * @param filePath File path where the exported data should be written to.
     * @param data The data to be written.
     * @throws IOException Thrown if something goes wrong when writing the file.
     */
    protected abstract void writeToFile(String filePath, List<ExportEntry> data) throws IOException;

    /**
     * Prepares data for the export by putting it into exportable containers.
     *
     * @param data The data to be prepared.
     * @return The data in exportable containers.
     */
    protected abstract List<ExportEntry> prepareData(List<VaultEntry> data);
}
