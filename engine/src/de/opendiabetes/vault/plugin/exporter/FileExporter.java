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

import de.opendiabetes.vault.container.VaultEntry;
import de.opendiabetes.vault.container.csv.ExportEntry;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.charset.Charset;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Properties;
import java.util.logging.Level;

import static java.lang.Boolean.parseBoolean;

/**
 * This class defines the default structure how data gets exported to a file.
 *
 * @author Lucas Buschlinger
 */
public abstract class FileExporter extends AbstractExporter {

    /**
     * The fileOutputStream used to write to the file.
     */
    private FileOutputStream fileOutputStream;
    /**
     * Option whether the data to export is period restricted.
     * By default the export data is not period restricted.
     */
    private boolean isPeriodRestricted = false;
    /**
     * Option which indicates from which point in time the data is period restricted.
     */
    private Date exportPeriodFrom = new Date();
    /**
     * Option which indicates to which point in time the data is period restricted.
     */
    private Date exportPeriodTo = new Date();

    /**
     * {@inheritDoc}
     */
    @Override
    public int exportDataToFile(final String filePath, final List<VaultEntry> data) throws IOException {
        // Status update constants.
        final int startWriteProgress = 80;
        final int writeDoneProgress = 100;
        // check file stuff
        File checkFile = new File(filePath);
        if (checkFile.exists()
                && (!checkFile.isFile() || !checkFile.canWrite())) {
            this.notifyStatus(-1, "An error occurred while accessing file " + filePath + ".");
            return ReturnCode.RESULT_FILE_ACCESS_ERROR.getCode();
        }
        fileOutputStream = new FileOutputStream(checkFile);
        // create csv data
        List<ExportEntry> exportData = prepareData(data);
        if (exportData == null || exportData.isEmpty()) {
            this.notifyStatus(-1, "Could not find data to export.");
            return ReturnCode.RESULT_NO_DATA.getCode();
        }
        this.notifyStatus(startWriteProgress, "Starting writing to file");
        // write to file
        writeToFile(filePath, exportData);
        try {
            fileOutputStream.close();
        } catch (IOException exception) {
            LOG.log(Level.WARNING, "Error while closing the fileOutputStream, uncritical.", exception);
        }

        this.notifyStatus(writeDoneProgress, "Writing to file successful, all done.");

        return ReturnCode.RESULT_OK.getCode();
    }

    /**
     * Writes the export data to the file.
     *
     * @param filePath File path where the exported data should be written to.
     * @param data The data to be written.
     * @throws IOException Thrown if something goes wrong when writing the file.
     */
    protected void writeToFile(final String filePath, final List<ExportEntry> data) throws IOException {
        FileChannel channel = fileOutputStream.getChannel();
        byte[] lineFeed = "\n".getBytes(Charset.forName("UTF-8"));

        for (ExportEntry entry : data) {
            byte[] messageBytes = entry.toByteEntryLine();
            channel.write(ByteBuffer.wrap(messageBytes));
            channel.write(ByteBuffer.wrap(lineFeed));
        }

        channel.close();
    }

    /**
     * Prepares data for the export by putting it into exportable containers.
     *
     * @param data The data to be prepared.
     * @return The data in exportable containers.
     */
    protected abstract List<ExportEntry> prepareData(List<VaultEntry> data);

    /**
     * Most generic loading of configurations of exporter plugins.
     * They usually have the following three properties:
     * <ul>
     *     <li>isPeriodRestricted - Indicates whether the data that is to be exported shall be filtered by a time period. <br>
     *                              Naturally requires that the dates are set accordingly</li>
     *     <li>isPeriodRestrictedFrom - The start date of the date restriction, expected date format is dd/MM/yyyy</li>
     *     <li>isPeriodRestrictedTo   - The end date of the date restriction, expected date format is dd/MM/yyyy</li>
     * </ul>
     * If it is necessary to set special properties for any given plugin this may be done within the plugins actual implementation.
     *
     * @param configuration The configuration to be set.
     * @return True if the configuration could be set successfully, false otherwise.
     */
    @Override
    public boolean loadPluginSpecificConfiguration(final Properties configuration) {
        // Status update constant
        final int loadConfigProgress = 0;
        // Format of dates which must be used.
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");

        this.notifyStatus(loadConfigProgress, "Loading configuration");

        if (!configuration.containsKey("periodRestriction")
                || configuration.getProperty("periodRestriction") == null
                || configuration.getProperty("periodRestriction").length() == 0) {
            LOG.log(Level.WARNING, "The exporter's configuration does not specify whether the data is period restricted, "
                    + "defaulting to no period restriction");
            setIsPeriodRestricted(false);
            return true;
        }
        boolean restriction = parseBoolean(configuration.getProperty("periodRestriction"));
        this.setIsPeriodRestricted(restriction);

        // Only necessary to look for dates if data is period restricted
        if (restriction) {
            Date dateFrom;
            Date dateTo;
            String startDate = configuration.getProperty("periodRestrictionFrom");
            String endDate = configuration.getProperty("periodRestrictionTo");
            if (startDate == null || endDate == null) {
                LOG.log(Level.SEVERE, "The exporter's configuration specified a period restriction on the data but no correct"
                        + " dates were specified.");
                return false;
            }
            // Parsing to actual dates
            try {
                dateFrom = dateFormat.parse(startDate);
                dateTo = dateFormat.parse(endDate);
            } catch (ParseException exception) {
                LOG.log(Level.SEVERE, "Either of the dates specified in the exporter's configuration is malformed."
                        + " The expected format is dd/mm/yyyy.");
                return false;
            }

            // Check whether the start time lies before the end time
            if (dateFrom.after(dateTo)) {
                LOG.log(Level.WARNING, "The date the data is period restricted from lies after the date it is restricted to,"
                        + " check order.");
                return false;
            }

            this.setExportPeriodFrom(dateFrom);
            this.setExportPeriodTo(dateTo);
            LOG.log(Level.INFO, "Data is period restricted from " + dateFrom.toString() + " to " + dateTo.toString());
            return true;
        } else {
            LOG.log(Level.INFO, "Export data is not period restricted by the exporter's configuration.");
            return true;
        }
    }

    /**
     * Getter for the fileOutputStream for descending classes.
     *
     * @return The {@link #fileOutputStream}.
     */
    protected FileOutputStream getFileOutputStream() {
        return fileOutputStream;
    }

    /**
     * Getter for the period restriction flag.
     *
     * @return True if the data is period restricted, false otherwise.
     */
    protected boolean getIsPeriodRestricted() {
        return isPeriodRestricted;
    }

    /**
     * Setter for the period restriction flag {@link #isPeriodRestricted}.
     *
     * @param periodRestricted The value to set the flag to,
     *                         true if the data is period restricted, false otherwise.
     */
    protected void setIsPeriodRestricted(final boolean periodRestricted) {
        this.isPeriodRestricted = periodRestricted;
    }

    /**
     * Setter for the option {@link #exportPeriodFrom}.
     *
     * @param exportPeriodFrom The date to be set.
     */
    protected void setExportPeriodFrom(final Date exportPeriodFrom) {
        this.exportPeriodFrom = exportPeriodFrom;
    }

    /**
     * Getter for the option {@link #exportPeriodFrom}.
     *
     * @return The date the data is period limited from.
     */
    protected Date getExportPeriodFrom() {
        return exportPeriodFrom;
    }

    /**
     * Setter for the option {@link #exportPeriodTo}.
     *
     * @param exportPeriodTo The date to be set.
     */
    protected void setExportPeriodTo(final Date exportPeriodTo) {
        this.exportPeriodTo = exportPeriodTo;
    }

    /**
     * Getter for the option {@link #exportPeriodTo}.
     *
     * @return The date the data is period limited from.
     */
    protected Date getExportPeriodTo() {
        return exportPeriodTo;
    }

    /**
     * This filters the data if the options indicate a period restriction on the data.
     *
     * @param data The data to filter.
     * @return The filtered data.
     */
    protected List<VaultEntry> filterPeriodRestriction(final List<VaultEntry> data) {
        List<VaultEntry> tempData = new ArrayList<>();
        Date begin = getExportPeriodFrom();
        Date end = getExportPeriodTo();
        for (VaultEntry entry : data) {
            Date timestamp = entry.getTimestamp();
            if (timestamp.before(begin) || timestamp.after(end)) {
                continue;
            }
            tempData.add(entry);
        }
        return tempData;
    }
}
