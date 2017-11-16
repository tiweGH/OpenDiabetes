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
package de.jhit.opendiabetes.vault.exporter;

import com.csvreader.CsvWriter;
import de.jhit.opendiabetes.vault.container.VaultEntry;
import de.jhit.opendiabetes.vault.container.csv.CsvEntry;
import de.jhit.opendiabetes.vault.container.csv.ExportEntry;
import de.jhit.opendiabetes.vault.container.csv.VaultCsvEntry;
import de.jhit.opendiabetes.vault.data.VaultDao;
import java.io.IOException;
import java.nio.charset.Charset;
import java.security.DigestOutputStream;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.List;
import java.util.logging.Level;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;
import javax.xml.bind.annotation.adapters.HexBinaryAdapter;

/**
 *
 * @author juehv
 */
public class VaultOdvExporter extends VaultCsvExporter {

    public static final String DATA_ZIP_ENTRY = "data.csv";
    public static final String SIGNATURE_ZIP_ENTRY = "sig.txt";

    public VaultOdvExporter(ExporterOptions options, VaultDao db, String filePath) {
        super(options, db, filePath);
    }

    @Override
    public int exportDataToFile(List<VaultEntry> data) {
        return super.exportDataToFile(data);
    }

    @Override
    protected void writeToFile(List<ExportEntry> csvEntries) throws IOException {
        // Setup compression stuff
        ZipOutputStream zos = new ZipOutputStream(fileOutpuStream);
        zos.setMethod(ZipOutputStream.DEFLATED);
        zos.setLevel(9);

        // Setup signature stuff
        MessageDigest md;
        try {
            md = MessageDigest.getInstance("SHA-512");
        } catch (NoSuchAlgorithmException ex) {
            LOG.log(Level.SEVERE, "Missing hash algorithm for signature. No file exported!", ex);
            throw new IOException("Missing hash algorithm for signature.");
        }
        DigestOutputStream dos = new DigestOutputStream(zos, md);

        // write data        
        zos.putNextEntry(new ZipEntry(DATA_ZIP_ENTRY));
        CsvWriter cwriter = new CsvWriter(dos, VaultCsvEntry.CSV_DELIMITER,
                Charset.forName("UTF-8"));

        cwriter.writeRecord(((CsvEntry) csvEntries.get(0)).getCsvHeaderRecord());
        for (ExportEntry item : csvEntries) {
            cwriter.writeRecord(((CsvEntry) item).toCsvRecord());
        }
        cwriter.flush();

        // add signature file
        zos.putNextEntry(new ZipEntry(SIGNATURE_ZIP_ENTRY));
        String sigString = (new HexBinaryAdapter()).marshal(md.digest());
        zos.write(sigString.getBytes(), 0, sigString.getBytes().length);

        // close everything
        cwriter.close();
        dos.close();
        zos.close();
        fileOutpuStream.close();
    }
}
