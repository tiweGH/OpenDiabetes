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

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.nio.charset.Charset;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Abstract class implementing the CSV Entry data structure.
 */
public abstract class CsvEntry implements ExportEntry {

    /**
     * Declaration of the decimal format.
     */
    public static final String DECIMAL_FORMAT = "%1$.2f";
    /**
     * Declaration of the delimiter used.
     */
    public static final char CSV_DELIMITER = ',';
    /**
     * Declaration of the list delimiter used.
     */
    public static final char CSV_LIST_DELIMITER = ':';

    /**
     * Method to convert entries to CSV records.
     * @return The CSV records.
     */
    public abstract String[] toCsvRecord();

    /**
     * Method to get the CSV header.
     * @return The CSV header.
     */
    public abstract String[] getCsvHeaderRecord();

    /**
     * {@inheritDoc}
     */
    @Override
    public byte[] toByteEntryLine() throws IOException {
        ByteArrayOutputStream os = new ByteArrayOutputStream();

        os = writeStringArray(os, toCsvRecord());

        return os.toByteArray();
    }

    /**
     * Method to write a string array onto an output stream.
     * @param outputStream The output stream.
     * @param array The string array.
     * @return Output stream as a byte array.
     * @throws IOException Thrown if string can not be converted to UTF-8.
     */
    private ByteArrayOutputStream writeStringArray(final ByteArrayOutputStream outputStream, final String[] array) throws IOException {
        ByteArrayOutputStream os = outputStream;
        byte[] delimiterConverted = new String(new char[] {CSV_DELIMITER}).getBytes(Charset.forName("UTF-8"));
        byte[] delimiter = new byte[] {};

        for (String line : array) {
            try {
                os.write(delimiter);
                os.write(line.getBytes(Charset.forName("UTF-8")));
                delimiter = delimiterConverted;
            } catch (IOException ex) {
                Logger.getLogger(CsvEntry.class.getName()).log(Level.SEVERE,
                        "Error converting String in UTF8", ex);
                throw ex;
            }
        }

        return os;
    }

}
