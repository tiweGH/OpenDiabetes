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
package de.jhit.opendiabetes.vault.importer;

import com.csvreader.CsvReader;
import de.jhit.opendiabetes.vault.container.VaultEntry;
import de.jhit.opendiabetes.vault.importer.validator.CsvValidator;
import de.jhit.opendiabetes.vault.importer.validator.VaultCsvValidator;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author juehv
 */
public class VaultCsvImporter extends CsvImporter {

    public VaultCsvImporter(String importFilePath) {
        this(importFilePath, new VaultCsvValidator(), ',');
    }

    private VaultCsvImporter(String importFilePath, CsvValidator validator, char delimiter) {
        super(importFilePath,validator, delimiter);
    }

    @Override
    protected void preprocessingIfNeeded(String filePath) {
        // not needed
    }

    @Override
    protected List<VaultEntry> parseEntry(CsvReader creader) throws Exception {
        //throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
        return new ArrayList<>();
    }

}
