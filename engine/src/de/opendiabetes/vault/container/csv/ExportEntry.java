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
package de.opendiabetes.vault.container.csv;

import java.io.IOException;

/**
 * Interface for export entries.
 */
public interface ExportEntry {

    /**
     * Converts entry to a entry in the export file.
     *
     * @return byte array representing the entry.
     * @throws IOException Thrown if no byte entry can be calculated.
     */
    byte[] toByteEntryLine() throws IOException;
}
