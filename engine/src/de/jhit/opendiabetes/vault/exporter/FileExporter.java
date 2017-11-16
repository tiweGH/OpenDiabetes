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

import de.jhit.opendiabetes.vault.container.VaultEntry;
import de.jhit.opendiabetes.vault.container.csv.ExportEntry;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.charset.Charset;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author juehv
 */
public abstract class FileExporter {

    public final static int RESULT_OK = 0;
    public final static int RESULT_ERROR = -1;
    public final static int RESULT_NO_DATA = -2;
    public final static int RESULT_FILE_ACCESS_ERROR = -3;

    protected static final Logger LOG = Logger.getLogger(VaultCsvExporter.class.getName());

    protected final ExporterOptions options;
    protected final String filePath;
    protected FileOutputStream fileOutpuStream;

    protected FileExporter(ExporterOptions options, String filePath) {
        this.options = options;
        this.filePath = filePath;
    }

    public int exportDataToFile(List<VaultEntry> data) {
        // check file stuff        
        File checkFile = new File(filePath);
        if (checkFile.exists()
                && (!checkFile.isFile() || !checkFile.canWrite())) {
            return RESULT_FILE_ACCESS_ERROR;
        }
        try {
            fileOutpuStream = new FileOutputStream(checkFile);
        } catch (FileNotFoundException ex) {
            LOG.log(Level.SEVERE, "Error accessing file for output stream", ex);
            return RESULT_FILE_ACCESS_ERROR;
        }

        // create csv data
        List<ExportEntry> exportData = prepareData(data);
        if (exportData == null || exportData.isEmpty()) {
            return RESULT_NO_DATA;
        }

        // write to file
        try {
            writeToFile(exportData);
        } catch (IOException ex) {
            LOG.log(Level.SEVERE, "Error writing odv csv file: {0}" + filePath, ex);
            return RESULT_ERROR;
        } finally {
            try {
                fileOutpuStream.close();
            } catch (IOException ex) {
                //don't care
            }
        }
        return RESULT_OK;
    }

    /**
     * Prepare eata for export (put it into a exportable container)
     *
     * @return
     */
    protected abstract List<ExportEntry> prepareData(List<VaultEntry> data);

    /**
     * Writes data to a file.
     *
     * @param data
     * @throws IOException
     */
    protected void writeToFile(List<ExportEntry> data) throws IOException {
        FileChannel fc = fileOutpuStream.getChannel();
        byte[] lineFeed = "\n".getBytes(Charset.forName("UTF-8"));

        for (ExportEntry entry : data) {
            byte[] messageBytes = entry.toByteEntryLine();
            fc.write(ByteBuffer.wrap(messageBytes));
            fc.write(ByteBuffer.wrap(lineFeed));
        }

        fc.close();
    }
}
