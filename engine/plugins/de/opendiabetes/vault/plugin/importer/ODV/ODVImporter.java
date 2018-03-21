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
package de.opendiabetes.vault.plugin.importer.ODV;

import de.opendiabetes.vault.container.VaultEntry;
import de.opendiabetes.vault.plugin.fileimporter.FileImporter;
import de.opendiabetes.vault.plugin.fileimporter.AbstractFileImporter;
import de.opendiabetes.vault.plugin.importer.Importer;
import org.pf4j.DefaultPluginManager;
import org.pf4j.Extension;
import org.pf4j.Plugin;
import org.pf4j.PluginManager;
import org.pf4j.PluginWrapper;

import javax.xml.bind.annotation.adapters.HexBinaryAdapter;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.logging.Level;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

/**
 * Wrapper class for the {@link ODVImporterImplementation} used by the {@link org.pf4j.PluginManager}.
 *
 * @author Lucas Buschlinger
 */
public class ODVImporter extends Plugin {

    /**
     * Constructor for the PluginManager.
     *
     * @param wrapper The PluginWrapper.
     */
    public ODVImporter(final PluginWrapper wrapper) {
        super(wrapper);
    }

    /**
     * Actual implementation of the ODV importer plugin.
     */
    @Extension
    public static final class ODVImporterImplementation extends AbstractFileImporter {

        /**
         * The buffer size to be used.
         */
        private static final int BUFFER_SIZE = 1024;
        /**
         * Progress percentage for showing that the configuration has been loaded.
         */
        private static final int PROGRESS_CONFIG_LOADED = 25;
        /**
         * Progress percentage for showing that the file has been unzipped.
         */
        private static final int PROGRESS_UNZIPPED = 50;
        /**
         * Progress percentage for showing that all available files were imported.
         */
        private static final int PROGRESS_AVAILABLE_IMPORTED = 100;
        /**
         * Progress percentage for showing that there are files that were not imported.
         */
        private static final int PROGRESS_UNIMPORTED_FILES = -1;
        /**
         * The default temporary directory to be used.
         */
        private static final String DEFAULT_TEMP_DIR = System.getProperty("java.io.tmpdir") + File.separator + "ODVImporter";
        /**
         * The default name of the meta file.
         */
        private static final String DEFAULT_META_FILE = "meta.info";
        /**
         * The temporary directory to use.
         */
        private String tempDir;
        /**
         * The default name of the meta file.
         */
        private String metaFile;

        /**
         * Constructor used to set default values.
         */
        public ODVImporterImplementation() {
            this.tempDir = DEFAULT_TEMP_DIR;
            this.metaFile = DEFAULT_META_FILE;
        }


        /**
         * This implementation imports the different files from the ZIP-archive.
         * It does so by unzipping the archive and checking the integrity of the contained files.
         * Files can only be imported if the therefore needed importer plugin is available, if not it gets omitted and reported.
         *
         * @param filePath Path to the ZIP-archive from which the files should be imported.
         * @return List of VaultEntry consisting of the imported data.
         * @throws Exception Throws if there was an error reading the files
         */
        @Override
        public List<VaultEntry> importData(final String filePath) throws Exception {
            Map<String, MetaValues> metaInfo;
            Map<String, String> unimportedFiles = new HashMap<>();
            List<VaultEntry> importedData = new ArrayList<>();
            String reasonNoPlugin = "No applicable importer plugin available for this file";
            String reasonChecksumFailed = "The integrity of the data could not be verified via the checksum";
            unzipArchive(filePath, tempDir);
            notifyStatus(PROGRESS_UNZIPPED, "Unzipped the archive");
            metaInfo = readMetaFile(tempDir + File.separator + metaFile);
            Iterator iterator = metaInfo.entrySet().iterator();
            PluginManager manager = new DefaultPluginManager();
            manager.loadPlugins();
            manager.startPlugins();
            while (iterator.hasNext()) {
                Map.Entry metaEntry = (Map.Entry) iterator.next();
                String plugin = (String) metaEntry.getKey();
                MetaValues metaValues = (MetaValues) metaEntry.getValue();
                String importFile = tempDir + File.separator + metaValues.file;
                Importer importer;
                try {
                    importer = (Importer) manager.getExtensions(plugin).get(0);
                } catch (Exception exception) {
                    LOG.log(Level.WARNING, "No Plugin named {0} available ", plugin);
                    unimportedFiles.put(importFile, reasonNoPlugin);
                    continue;
                }
                if (!verifyChecksum(importFile, metaValues.checksum)) {
                    unimportedFiles.put(importFile, reasonChecksumFailed);
                    continue;
                }
                List<VaultEntry> subImportedData;
                if (importer instanceof FileImporter) {
                    subImportedData = ((FileImporter) importer).importData(importFile);
                } else {
                    subImportedData = importer.importData();
                }
                importedData.addAll(subImportedData);
            }
            notifyStatus(PROGRESS_AVAILABLE_IMPORTED, "Imported all available data");
            reportUnimported(unimportedFiles);
            return importedData;
        }

        /**
         * This method loads the configuration and sets the values accordingly.
         *
         * @param configuration The configuration object that hold all configurations.
         * @return True if the configuration could be loaded, false otherwise.
         */
        @Override
        public boolean loadPluginSpecificConfiguration(final Properties configuration) {
            if (configuration.containsKey("temporaryDirectory")) {
                this.tempDir = configuration.getProperty("temporaryDirectory");
            }
            if (configuration.containsKey("metaFile")) {
                this.metaFile = configuration.getProperty("metaFile");
            }
            this.notifyStatus(PROGRESS_CONFIG_LOADED, "Loaded configuration");
            return true;
        }

        /**
         * This method is used to check the integrity of the specified file by comparing it to the supplied checksum.
         *
         * @param filePath Path to the file to be checked.
         * @param checksum The checksum to check the file against.
         * @return True if the integrity could be verified successfully, false otherwise.
         */
        private boolean verifyChecksum(final String filePath, final String checksum) {
            try (FileInputStream fileInputStream = new FileInputStream(filePath)) {
                MessageDigest digest;
                try {
                    digest = MessageDigest.getInstance("SHA-512");
                } catch (NoSuchAlgorithmException exception) {
                    LOG.log(Level.WARNING, "Could not verify integrity, SHA-512 algorithm not available");
                    return false;
                }
                byte[] dataBytes = new byte[BUFFER_SIZE];
                int nextRead;
                try {
                    while ((nextRead = fileInputStream.read(dataBytes)) != -1) {
                        digest.update(dataBytes, 0, nextRead);
                    }
                } catch (IOException exception) {
                    LOG.log(Level.WARNING, "Could not read file, integrity not verified");
                    return false;
                }

                String generateChecksum = (new HexBinaryAdapter()).marshal(digest.digest());
                if (!generateChecksum.equalsIgnoreCase(checksum)) {
                    LOG.log(Level.WARNING, "Checksum is not valid for the specified file");
                    return false;
                } else {
                    LOG.log(Level.INFO, "Checksum successfully verified for: " + filePath);
                }
            } catch (FileNotFoundException exception) {
                LOG.log(Level.WARNING, "Could not find file, integrity could not be verified: " + filePath);
                return false;
            } catch (IOException exception) {
                LOG.log(Level.SEVERE, "Error while handling the file input stream " + exception);
                return false;
            }
            return true;
        }

        /**
         * The methods unzips the contents of the specified zipFile to the specified directory.
         *
         * @param zipFile The file to unzip.
         * @param outputDirectory The directory where the files will be unzipped to.
         * @throws IOException Thrown if reading or writing any of the files goes wrong.
         */
        private void unzipArchive(final String zipFile, final String outputDirectory) throws IOException {
            File outputFolder = new File(outputDirectory);
            if (!outputFolder.exists()) {
                if (!outputFolder.mkdir()) {
                    LOG.log(Level.SEVERE, "Could not create " + outputDirectory);
                    throw new IOException("Could not create create directory: " + outputDirectory);
                }
            }
            ZipInputStream zipInputStream = new ZipInputStream(new FileInputStream(zipFile));
            ZipEntry entry = zipInputStream.getNextEntry();
            byte[] buffer = new byte[BUFFER_SIZE];
            while (entry != null) {
                String fileName = entry.getName();
                File extractedFile = new File(outputDirectory + File.separator + fileName);
                FileOutputStream fileOutputStream = null;
                try {
                    fileOutputStream = new FileOutputStream(extractedFile);
                    int length;
                    while ((length = zipInputStream.read(buffer)) > 0) {
                        fileOutputStream.write(buffer, 0, length);
                    }
                } catch (FileNotFoundException exception) {
                    LOG.log(Level.SEVERE, "Could not open file " + extractedFile);
                    throw exception;
                } finally {
                    if (fileOutputStream != null) {
                        fileOutputStream.close();
                    }
                    entry = zipInputStream.getNextEntry();
                }

            }
            zipInputStream.closeEntry();
            zipInputStream.close();
            LOG.log(Level.INFO, "Successfully unzipped archive " + zipFile);
        }

        /**
         * This method reads the meta file and creates a map holding the info contained in it.
         * The info is:
         * <ul>
         *     <li>The name of the Importer to use</li>
         *     <li>The file to import with the importer</li>
         *     <li>The checksum of the file</li>
         * </ul>
         *
         * @param metaFile The meta file to be read
         * @return A map containing all information.
         * @throws IOException Thrown if the meta file can not be read.
         */
        private Map<String, MetaValues> readMetaFile(final String metaFile) throws IOException {
            String line;
            Map<String, MetaValues> result = new HashMap<>();
            FileInputStream inputStream = new FileInputStream(metaFile);
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream, "UTF-8"));
            line = bufferedReader.readLine();
            while (line != null) {
                String[] lineEntries = line.split("=");
                String importer = lineEntries[0];
                MetaValues metaValues = new MetaValues();
                metaValues.file = lineEntries[1];
                metaValues.checksum = lineEntries[2];
                result.put(importer, metaValues);
                line = bufferedReader.readLine();
            }
            bufferedReader.close();
            inputStream.close();
            return result;
        }

        /**
         *  The reports all files that were not imported and the reason why.
         *
         * @param unimportedEntries Holds the information which file was omitted and why.
         */
        private void reportUnimported(final Map<String, String> unimportedEntries) {
            StringBuilder stringBuilder = new StringBuilder();
            for (Map.Entry<String, String> entry : unimportedEntries.entrySet()) {
                stringBuilder.append("Could not import ");
                stringBuilder.append(entry.getKey());
                stringBuilder.append(". Reason: ");
                stringBuilder.append(entry.getValue());
                stringBuilder.append("\n");
            }
            notifyStatus(PROGRESS_UNIMPORTED_FILES, stringBuilder.toString());
        }

        /**
         * {@inheritDoc}
         */
        @Override
        protected void preprocessingIfNeeded(final String filePath) {
            /* Not implemented */
        }

        /**
         * {@inheritDoc}
         */
        @Override
        protected List<VaultEntry> processImport(final InputStream fileInputStream, final String filenameForLogging) {
            return null;
        }

        /**
        * This class encapsulates the values used in the meta data.
        * Namely the file that the exporters generated as well as its checksum.
        */
        private static class MetaValues {
            /**
            * This name of the file generated an exporter.
            */
             private String file;
            /**
            * The checksum for the file generated by an exporter.
            */
            private String checksum;
        }

    }

}
