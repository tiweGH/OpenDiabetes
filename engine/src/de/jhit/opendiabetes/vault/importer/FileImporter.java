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

import static de.jhit.opendiabetes.vault.importer.Importer.LOG;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.logging.Level;

/**
 *
 * @author juehv
 */
public abstract class FileImporter extends Importer {

    protected String importFilePath;

    public FileImporter(String importFilePath) {
        this.importFilePath = importFilePath;
    }

    public String getImportFilePath() {
        return importFilePath;
    }

    public void setImportFilePath(String importFilePath) {
        this.importFilePath = importFilePath;
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

    protected abstract void preprocessingIfNeeded(String filePath);

    protected abstract boolean processImport(InputStream fis, String filenameForLogging);

}
