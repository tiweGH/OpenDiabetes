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
package de.opendiabetes.vault.plugin.exporter.sourcecode;

import de.opendiabetes.vault.container.VaultEntry;
import de.opendiabetes.vault.container.VaultEntryAnnotation;
import de.opendiabetes.vault.container.csv.CsvEntry;
import de.opendiabetes.vault.container.csv.ExportEntry;
import de.opendiabetes.vault.plugin.exporter.VaultExporter;
import de.opendiabetes.vault.plugin.util.TimestampUtils;
import org.pf4j.Extension;
import org.pf4j.Plugin;
import org.pf4j.PluginWrapper;

import java.io.BufferedWriter;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

/**
 * Wrapper class for the SourceCodeExporter plugin.
 *
 * @author Tina Lu
 */

public class SourceCodeExporter extends Plugin {
    /**
     * Constructor for the {@link org.pf4j.PluginManager}.
     *
     * @param wrapper The {@link org.pf4j.PluginWrapper}.
     */
    public SourceCodeExporter(final PluginWrapper wrapper) {
        super(wrapper);
    }

    /**
     * Actual implementation of the SourceCode exporter plugin.
     */
    @Extension
    public static final class SourceCodeExporterImplementation extends VaultExporter {

        /**
         * List to hold all the entries queried from the database in {@link #prepareData(List)}
         * and written on {@link #writeToFile(String, List)}.
         */
        private final List<String> entries = new ArrayList<>();

        /**
         * Method to get the ListInitCode.
         *
         * @return The List of Entries.
         */
        private static String getListInitCode() {
            StringBuilder sb = new StringBuilder();
            sb.append("List<VaultEntry> vaultEntries = new ArrayList<>();\n");
            sb.append("List<VaultEntryAnnotation> tmpAnnotations;\n");
            return sb.toString();
        }

        /**
         * Method to get the return statement code.
         *
         * @return StringBuilder.
         */
        private static String getReturnStatementCode() {
            StringBuilder sb = new StringBuilder();
            sb.append("return vaultEntries;\n");
            return sb.toString();
        }

        /**
         * Get the list of vault entries.
         *
         * @param data Vault entries.
         * @return The list of the vault entries.
         */
        private String toListCode(final VaultEntry data) {
            String timeFormat = "yyyy.MM.dd-HH:mm";
            StringBuilder sb = new StringBuilder();

            if (!data.getAnnotations().isEmpty()) {
                sb.append("tmpAnnotations = new ArrayList<>();\n");
                for (VaultEntryAnnotation annotation : data.getAnnotations()) {
                    sb.append("tmpAnnotations.add(new VaultEntryAnnotation(VaultEntryAnnotation.TYPE.");
                    sb.append(annotation.getType().toString());
                    if (!annotation.getValue().isEmpty()) {
                        sb.append(").setValue(\"");
                        sb.append(annotation.getValue());
                    }
                    sb.append("\"));\n");
                }
            }

            sb.append("vaultEntries.add(new VaultEntry(");
            sb.append("VaultEntryType.").append(data.getType()).append(",");
            sb.append("TimestampUtils.createCleanTimestamp(\"");
            sb.append(TimestampUtils.timestampToString(data.getTimestamp(), timeFormat));
            sb.append("\",\"").append(timeFormat).append("\"),");
            sb.append(data.getValue());
            if (data.getValue2() != VaultEntry.VALUE_UNUSED) {
                sb.append(",").append(data.getValue2());
            }
            if (!data.getAnnotations().isEmpty()) {
                sb.append(",").append("tmpAnnotations");
            }
            sb.append("));\n");
            return sb.toString();
        }

        /**
         * Writes the data to a CSV file and generates a signature hash.
         * Then it puts both of this into a ZIP archive file.
         *
         * @param filePath File path where the data should be exported to.
         * @param csvEntries The {@link ExportEntry} to be exported.
         * @throws IOException Thrown if the SHA-512 hash algorithm is missing.
         */
        protected void writeToFile(final String filePath, final List<ExportEntry> csvEntries) throws IOException {
            FileOutputStream fileOutputStream = getFileOutputStream();

            BufferedWriter writer = Files.newBufferedWriter(Paths.get(filePath), Charset.forName("UTF-8"));

            writer.write("  public static List<VaultEntry> getStaticDataset() throws ParseException {\n");
            writer.write(getListInitCode());
            for (String entry : entries) {
                writer.write(entry);
            }
            writer.write(getReturnStatementCode());
            writer.write("}");
            writer.flush();
            writer.close();
            fileOutputStream.close();
        }

        /**
         * {@inheritDoc}
         */
        @Override
        protected List<ExportEntry> prepareData(final List<VaultEntry> data) {
            if (data == null || data.isEmpty()) {
                return null;
            }
            List<VaultEntry> tmpValues;
            if (getIsPeriodRestricted()) {
                tmpValues = filterPeriodRestriction(data);
            } else {
                tmpValues = data;
            }
            for (VaultEntry value : tmpValues) {
                entries.add(toListCode(value));
            }

            // Dirty hack again to overcome safety features
            ArrayList<ExportEntry> dummy = new ArrayList<>();
            dummy.add(new CsvEntry() {
                @Override
                public String[] toCsvRecord() {
                    return new String[] {};
                }

                @Override
                public String[] getCsvHeaderRecord() {
                    return new String[] {};
                }
            });

            return dummy;
        }

    }
}
