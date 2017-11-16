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

import de.jhit.opendiabetes.vault.exporter.VaultOdvExporter;
import static de.jhit.opendiabetes.vault.importer.Importer.LOG;
import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.security.DigestInputStream;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.logging.Level;
import java.util.stream.Collectors;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;
import javax.xml.bind.annotation.adapters.HexBinaryAdapter;

/**
 *
 * @author juehv
 */
public class VaultOdvImporter extends VaultCsvImporter {

    public VaultOdvImporter(String importFilePath) {
        super(importFilePath);
    }

    @Override
    public boolean importData() {
        preprocessingIfNeeded(importFilePath);

        try {
            // open zip package
            ZipInputStream zis = new ZipInputStream(new FileInputStream(importFilePath));

            // Setup signature stuff
            MessageDigest md;
            try {
                md = MessageDigest.getInstance("SHA-512");
            } catch (NoSuchAlgorithmException ex) {
                LOG.log(Level.SEVERE, "Missing hash algorithm for signature. No file exported!", ex);
                return false;
            }
            DigestInputStream dis = new DigestInputStream(zis, md);

            ZipEntry tmpEntry = null;
            String signature = null;
            boolean processingResult = false;
            while ((tmpEntry = zis.getNextEntry()) != null) {
                switch (tmpEntry.getName()) {
                    case VaultOdvExporter.DATA_ZIP_ENTRY:
                        // process data while creating signature
                        processingResult = super.processImport(dis, importFilePath);
                        break;
                    case VaultOdvExporter.SIGNATURE_ZIP_ENTRY:
                        // read signature as string
                        signature = new BufferedReader(new InputStreamReader(zis))
                                .lines().collect(Collectors.joining("\n"));
                        break;
                    default:
                        LOG.warning("Found unexpected entry: " + tmpEntry.getName());
                        break;
                }
            }
            zis.close();

            if (processingResult) { // if not, data entry is missing
                // check signature
                String sigString = (new HexBinaryAdapter()).marshal(md.digest());
                if (sigString.equalsIgnoreCase(signature)) {
                    return true;
                } else {
                    LOG.severe("Signature check failed! File will be dropped.");
                    return false;
                }
            } else {
                return false;
            }
        } catch (FileNotFoundException ex) {
            LOG.log(Level.SEVERE, "Error opening a FileInputStream for file: "
                    + importFilePath, ex);
            return false;
        } catch (IOException ex) {
            LOG.log(Level.SEVERE, "Error reading compressed file: "
                    + importFilePath, ex);
            return false;
        }
    }
}
