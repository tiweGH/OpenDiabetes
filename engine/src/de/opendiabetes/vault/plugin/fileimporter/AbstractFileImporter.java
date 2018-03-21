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
package de.opendiabetes.vault.plugin.fileimporter;

import de.opendiabetes.vault.container.VaultEntry;
import de.opendiabetes.vault.plugin.common.AbstractPlugin;

import java.io.FileInputStream;
import java.io.InputStream;
import java.util.List;
import java.util.logging.Level;

/**
 * This class defines the default structure how data gets imported from a file.
 * It implements file handling of the data source
 * All descendants must implement the template methods.
 * {@link AbstractFileImporter#preprocessingIfNeeded(String)}.
 * {@link AbstractFileImporter#processImport(InputStream, String)}.
 */
public abstract class AbstractFileImporter extends AbstractPlugin implements de.opendiabetes.vault.plugin.fileimporter.FileImporter {

    /**
     * {@inheritDoc}
     */
    @Override
    public List<VaultEntry> importData() {
        throw new UnsupportedOperationException("The importData() method of a FileImporter cannot be used."
                + " Use importData(filePath) instead.");
    }

    /**
     * Imports the data from a specified file path.
     *
     * @param filePath File path from which the data should be imported to
     * @return List of VaultEntry consisting of the imported data.
     * @throws Exception Thrown if there was an error reading the file
     */
    public List<VaultEntry> importData(final String filePath) throws Exception {
        if (filePath == null) {
            throw new IllegalArgumentException("No path specified from where to import data.");
        }
        preprocessingIfNeeded(filePath);
        this.notifyStatus(0, "Preprocessing done.");

        FileInputStream inputStream = new FileInputStream(filePath);
        List<VaultEntry> result = processImport(inputStream, filePath);

        try {
            inputStream.close();
        } catch (Exception ex) {
            LOG.log(Level.WARNING, "Error closing the FileInputStream for File" + filePath, ex);
        }

        return result;
    }

    /**
     * Method for preprocessing the data if needed.
     *
     * @param filePath Path to the import file.
     */
    protected abstract void preprocessingIfNeeded(String filePath);

    /**
     * Method for processing the data.
     *
     * @param fileInputStream    The input stream for the imported data.
     * @param filenameForLogging Filename to which the logger should write.
     * @return List of VaultEntry consisting of the imported data.
     * @throws Exception Thrown if there was an error reading the file
     */
    protected abstract List<VaultEntry> processImport(InputStream fileInputStream, String filenameForLogging) throws Exception;

}
