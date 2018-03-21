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
import de.opendiabetes.vault.plugin.common.OpenDiabetesPlugin;
import org.pf4j.ExtensionPoint;

import java.io.IOException;
import java.util.List;

/**
 * This interface specifies the methods shared by all exporters.
 * Furthermore it serves as the {@link org.pf4j.ExtensionPoint} where the plugins hook up.
 * Thus all exporter plugins must implement this interface to get recognized as exporters.
 *
 * @author Lucas Buschlinger
 */
public interface Exporter extends ExtensionPoint, OpenDiabetesPlugin {
    /**
     * Return codes for exporting data.
     */
    enum ReturnCode {
        /**
         * Indicates that everything went right.
         */
        RESULT_OK(0),
        /**
         * Indicates that something went wrong.
         */
        RESULT_ERROR(-1),
        /**
         * Indicates that no data was specified to be exported.
         */
        RESULT_NO_DATA(-2),
        /**
         * Indicates an error accessing the export file.
         */
        RESULT_FILE_ACCESS_ERROR(-3);

        /**
         * The numeric representation of the return codes.
         */
        private final int code;

        /**
         * Constructor.
         *
         * @param code The numeric value of the return code.
         */
        ReturnCode(final int code) {
            this.code = code;
        }

        /**
         * Getter for the numeric representation of the return codes.
         * @return The numeric code associated to the ReturnCode this gets called on.
         */
        public int getCode() {
            return code;
        }
    }

    /**
     * This method is used to set the entries to export.
     * Should only be used with exporters that do not export from {@link VaultEntry}
     * but something different like {@link de.opendiabetes.vault.container.SliceEntry}.
     *
     * @param entries The entries which will be exported by the respective exporter.
     * @throws IllegalArgumentException Thrown if the wrong kind of entries were set.
     */
    void setEntries(List<?> entries) throws IllegalArgumentException;

    /**
     * Exports the data to a file.
     *
     * @param filePath File path where the data should be exported to.
     * @param data The data to export.
     * @return The return code as specified in {@link ReturnCode}.
     * @throws IOException Thrown if there was an error exporting the data
     */
    int exportDataToFile(String filePath, List<VaultEntry> data) throws IOException;
}
