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
package de.jhit.opendiabetes.vault.container.csv;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.nio.charset.Charset;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author juehv
 */
public abstract class CsvEntry implements ExportEntry {

    public static final String DECIMAL_FORMAT = "%1$.2f";
    public final static char CSV_DELIMITER = ',';
    public final static char CSV_LIST_DELIMITER = ':';

    public abstract String[] toCsvRecord();

    public abstract String[] getCsvHeaderRecord();

    @Override
    public byte[] toByteEntryLine() throws IOException {
        ByteArrayOutputStream os = new ByteArrayOutputStream();

        os = writeStringArray(os, toCsvRecord());

        return os.toByteArray();
    }

    private ByteArrayOutputStream writeStringArray(ByteArrayOutputStream os, String[] array) throws IOException {
        byte[] delimiterConverted = new String(new char[]{CSV_DELIMITER}).getBytes(Charset.forName("UTF-8"));
        byte[] delimiter = new byte[]{};

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
