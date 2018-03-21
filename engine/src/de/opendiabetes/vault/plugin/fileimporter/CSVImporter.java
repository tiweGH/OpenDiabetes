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
package de.opendiabetes.vault.plugin.fileimporter;

import com.csvreader.CsvReader;
import de.opendiabetes.vault.container.VaultEntry;
import de.opendiabetes.vault.plugin.importer.validator.CSVValidator;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
import java.util.logging.Level;

/**
 * This class implements the functionality for importing CSV based data.
 */
public abstract class CSVImporter extends AbstractFileImporter {
    /**
     * The validator who handles CSV data.
     */
    private CSVValidator validator;

    /**
     * Use this delimiter to use automatic delimiter detection.
     */
    public static final char AUTO_DELIMITER = 0;

    /**
     * Delimiter used in the CSV file.
     */
    private char delimiter = AUTO_DELIMITER; //set delimiter to "null" to indicate that it is not valid yet


    /**
     * Constructor for a CSV validator. (automatic delimiter detection).
     *
     * @param csvValidator The validator to use.
     */
    public CSVImporter(final CSVValidator csvValidator) {
        this.validator = csvValidator;
    }

    /*HACK: uses filenameforlogging instead of fileInputStream*/

    /**
     * Method used to detect the valid delimiter by trying to validate the header using a given delimiter.
     *
     * @param delimiter   the delimiters to use to read the file
     * @param file        the file to read
     * @param metaEntries placeholder for future extensions
     * @return a CsvReader pointing to the headers, null if the headers could not be validated
     * @throws IOException if file reading goes wrong
     */
    private CsvReader getValidatedCreader(final char delimiter, final String file, final List<String[]> metaEntries)
            throws IOException {
        // open file
        CsvReader creader = new CsvReader(file, delimiter, Charset.forName("UTF-8"));

        do {
            if (!creader.readHeaders()) {
                LOG.log(Level.FINEST, "automatic delimiter detection detected invalid delimiter: " + delimiter);
                metaEntries.clear();
                return null;
            }
            metaEntries.add(creader.getHeaders());
        } while (!validator.validateHeader(creader.getHeaders()));
        metaEntries.remove(metaEntries.size() - 1); //remove valid header
        LOG.log(Level.INFO, "automatic delimiter detection detected valid delimiter: " + delimiter);
        return creader;
    }

    /**
     * {@inheritDoc}
     */
    public List<VaultEntry> processImport(final InputStream fileInputStream, final String filenameForLogging) throws Exception {
        List<VaultEntry> importedData = new ArrayList<>();
        final int maxProgress = 100;

        //This list is used as a placeholder for future extensions
        List<String[]> metaEntries = new ArrayList<>();

        this.notifyStatus(0, "Reading Header");
        CsvReader creader = null;
        if (getDelimiter() == 0) { //try to detect the delimiter by trial and error
            LOG.log(Level.INFO, "using automatic delimiter detection");
            char[] delimiterList = {',', ';', '\t'};
            for (char delimiter : delimiterList) {
                creader = getValidatedCreader(delimiter, filenameForLogging, metaEntries);
                if (null != creader) {
                    setDelimiter(delimiter);
                    break;
                }
            }
        } else { // use the delimiter that was set
            creader = getValidatedCreader(getDelimiter(), filenameForLogging, metaEntries);
        }
        if (creader == null) { //header could not be validated
            LOG.log(Level.WARNING, "No valid header found in File:{0}", filenameForLogging);
            return null;
        }
        // read entries
        while (creader.readRecord()) {
            /*here the method template is used to process all records */
            List<VaultEntry> entryList = parseEntry(creader);

            if (entryList != null && !entryList.isEmpty()) {
                importedData.addAll(entryList);
            }
        }
        this.notifyStatus(maxProgress, "Done importing all entries");

        return importedData;
    }

    /**
     * Method to parse entries in a file.
     *
     * @param csvReader Reader for CSV files.
     * @return List of VaultEntry holding the imported data.
     * @throws Exception If an entry could not be parsed.
     */
    protected abstract List<VaultEntry> parseEntry(CsvReader csvReader) throws Exception;

    /**
     * {@inheritDoc}
     */
    @Override
    protected abstract void preprocessingIfNeeded(String filePath);

    /**
     * Getter for the validator.
     *
     * @return The validator.
     */
    protected CSVValidator getValidator() {
        return validator;
    }

    /**
     * Setter for the validator.
     *
     * @param csvValidator The validator to set.
     */
    private void setValidator(final CSVValidator csvValidator) {
        this.validator = csvValidator;
    }

    /**
     * Getter for the delimiter.
     *
     * @return The delimiter.
     */
    protected char getDelimiter() {
        return delimiter;
    }

    /**
     * Setter for the delimiter.
     *
     * @param csvDelimiter The delimiter to set.
     */
    protected void setDelimiter(final char csvDelimiter) {
        this.delimiter = csvDelimiter;
    }

    /**
     * Allows to set the delimiter to use.
     * {@inheritDoc}
     */
    @Override
    public boolean loadPluginSpecificConfiguration(final Properties configuration) {
        if (configuration.containsKey("delimiter")) {
            String delimiter = configuration.getProperty("delimiter");
            if (delimiter != null && delimiter.length() == 1) {
                this.setDelimiter(delimiter.charAt(0));
                return true;
            } else {
                LOG.log(Level.WARNING,
                        "Please set the delimiter property in the following format: \n"
                                + "delimiter = ,\n"
                                + "where \",\" can be any char you want to use as delimiter\n"
                                + "or remove the delimiter property to use automatic delimiter detection");
                return false;
            }
        }
        return true;
    }
}
