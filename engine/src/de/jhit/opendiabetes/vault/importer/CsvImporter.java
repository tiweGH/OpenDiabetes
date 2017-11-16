/*
 * Copyright (C) 2017 Jens Heuschkel
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
package de.jhit.opendiabetes.vault.importer;

import com.csvreader.CsvReader;
import de.jhit.opendiabetes.vault.container.RawEntry;
import de.jhit.opendiabetes.vault.container.VaultEntry;
import de.jhit.opendiabetes.vault.importer.validator.CsvValidator;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;

/**
 *
 * @author mswin
 */
public abstract class CsvImporter extends FileImporter {

    protected final CsvValidator validator;
    protected char delimiter;

    public CsvImporter(String importFilePath, CsvValidator validator, char delimiter) {
        super(importFilePath);
        this.validator = validator;
        this.delimiter = delimiter;
    }

    @Override
    public boolean importData() {
        preprocessingIfNeeded(importFilePath);

        FileInputStream fis = null;
        try {
            fis = new FileInputStream(importFilePath);
            return processImport(fis, importFilePath);
        } catch (FileNotFoundException ex) {
            LOG.log(Level.SEVERE, "Error opening a FileInputStream for File "
                    + importFilePath, ex);
            return false;
        } finally {
            if (fis != null) {
                try {
                    fis.close();
                } catch (Exception ex) {
                    // not interesting :D
                }
            }
        }

    }

    protected boolean processImport(InputStream fis, String filenameForLogging) {
        importedData = new ArrayList<>();
        importedRawData = new ArrayList<>();
        List<String[]> metaEntrys = new ArrayList<>();

        CsvReader creader = null;
        try {
            // open file
            creader = new CsvReader(fis, delimiter, Charset.forName("UTF-8"));

            //validate header
            do {
                if (!creader.readHeaders()) {
                    // no more lines --> no valid header
                    LOG.log(Level.WARNING, "No valid header found in File:{0}", filenameForLogging);
                    return false;
                }
                metaEntrys.add(creader.getHeaders());
            } while (!validator.validateHeader(creader.getHeaders()));
            metaEntrys.remove(metaEntrys.size() - 1); //remove valid header

            // read entries
            while (creader.readRecord()) {
                List<VaultEntry> entryList = parseEntry(creader);
                boolean parsed = false;
                if (entryList != null && !entryList.isEmpty()) {
                    for (VaultEntry item : entryList) {
                        item.setRawId(importedRawData.size()); // add array position as raw id
                        importedData.add(item);
                        LOG.log(Level.FINE, "Got Entry: {0}", entryList.toString());
                    }
                    parsed = true;
                }
                importedRawData.add(new RawEntry(creader.getRawRecord(), parsed));
                LOG.log(Level.FINER, "Put Raw: {0}", creader.getRawRecord());
            }

        } catch (Exception ex) {
            LOG.log(Level.WARNING, "Error while parsing CSV: "
                    + filenameForLogging, ex);
        }
        return true;
    }

    protected abstract List<VaultEntry> parseEntry(CsvReader creader) throws Exception;

    protected abstract void preprocessingIfNeeded(String filePath);

}
