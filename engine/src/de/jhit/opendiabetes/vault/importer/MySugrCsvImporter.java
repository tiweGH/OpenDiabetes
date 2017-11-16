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

import de.jhit.opendiabetes.vault.importer.validator.MySugrCsvValidator;
import com.csvreader.CsvReader;
import de.jhit.opendiabetes.vault.container.VaultEntry;
import static de.jhit.opendiabetes.vault.importer.Importer.LOG;
import java.io.IOException;
import java.nio.charset.Charset;
import java.util.List;
import java.util.logging.Level;

/**
 *
 * @author juehv
 */
public class MySugrCsvImporter extends CsvImporter {

    public MySugrCsvImporter(String importFilePath) {
        this(importFilePath, ',');
    }

    public MySugrCsvImporter(String importFilePath, char delimiter) {
        super(importFilePath, new MySugrCsvValidator(), delimiter);
    }

    @Override
    protected void preprocessingIfNeeded(String filePath) {
        // test for delimiter
        CsvReader creader = null;
        try {
            // test for , delimiter
            creader = new CsvReader(filePath, ',', Charset.forName("UTF-8"));
            for (int i = 0; i < 15; i++) { // just scan the first 15 lines for a valid header
                if (creader.readHeaders()) {
                    if (validator.validateHeader(creader.getHeaders())) {
                        // found valid header --> finish
                        delimiter = ',';
                        creader.close();
                        LOG.log(Level.FINE, "Use ',' as delimiter for MySugr CSV: {0}", filePath);
                        return;
                    }
                }
            }
            // if you end up here there was no valid header within the range
            // try the other delimiter in normal operation
            delimiter = ';';
            LOG.log(Level.FINE, "Use ';' as delimiter for MySugr CSV: {0}", filePath);
        } catch (IOException ex) {
            LOG.log(Level.WARNING, "Error while parsing MySugr CSV in delimiter check: "
                    + filePath, ex);
        } finally {
            if (creader != null) {
                creader.close();
            }
        }
    }

    @Override
    protected List<VaultEntry> parseEntry(CsvReader creader) throws Exception {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

}
