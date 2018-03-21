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

import com.csvreader.CsvWriter;
import de.opendiabetes.vault.container.csv.CsvEntry;
import de.opendiabetes.vault.container.csv.ExportEntry;
import de.opendiabetes.vault.container.csv.VaultCsvEntry;

import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.charset.Charset;
import java.util.List;

/**
 * This defines a standard implementation for writing CSV data.
 *
 * @author Lucas Buschlinger
 */
public abstract class CSVFileExporter extends FileExporter {

    /**
     * {@inheritDoc}
     */
    @Override
    protected void writeToFile(final String filePath, final List<ExportEntry> csvEntries) throws IOException {
        FileOutputStream fileOutputStream = getFileOutputStream();
        CsvWriter cwriter = new CsvWriter(fileOutputStream, VaultCsvEntry.CSV_DELIMITER, Charset.forName("UTF-8"));

        cwriter.writeRecord(((CsvEntry) csvEntries.get(0)).getCsvHeaderRecord());
        for (ExportEntry item : csvEntries) {
            cwriter.writeRecord(((CsvEntry) item).toCsvRecord());
        }
        cwriter.flush();
        cwriter.close();
    }
}
