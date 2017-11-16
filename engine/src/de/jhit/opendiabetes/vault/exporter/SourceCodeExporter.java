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
import de.jhit.opendiabetes.vault.container.VaultEntrySourceCodeAdapter;
import de.jhit.opendiabetes.vault.container.csv.CsvEntry;
import de.jhit.opendiabetes.vault.container.csv.ExportEntry;
import de.jhit.opendiabetes.vault.data.VaultDao;
import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

/**
 * Exports the data as big list of entries written in java source code TODO
 * reimplement
 *
 * @author juehv
 */
public class SourceCodeExporter extends CsvFileExporter {

    private final List<String> entries = new ArrayList<>(); //super dirty hack, since I am to lazy to update the architecture at the moment
    private final VaultDao db;

    public SourceCodeExporter(ExporterOptions options, VaultDao db, String filePath) {
        super(options, filePath);
        this.db = db;
    }

    public List<VaultEntry> queryData() {
        List<VaultEntry> entrys;

        // query entrys
        if (options.isImportPeriodRestricted) {
            entrys = db.queryVaultEntrysBetween(options.exportPeriodFrom,
                    options.exportPeriodTo);
        } else {
            entrys = db.queryAllVaultEntrys();
        }

        return entrys;
    }

    @Override
    protected void writeToFile(List<ExportEntry> csvEntries) throws IOException {
        BufferedWriter writer = Files.newBufferedWriter(Paths.get(filePath), Charset.forName("UTF-8"));

        writer.write("public static List<VaultEntry> getStaticDataset() throws ParseException {\n");
        writer.write(VaultEntrySourceCodeAdapter.getListInitCode());

        for (String entry : entries) {
            writer.write(entry);
        }

        writer.write(VaultEntrySourceCodeAdapter.getReturnStatementCode());
        writer.write("}");

        writer.flush();
        writer.close();
        fileOutpuStream.close();
    }

    @Override
    protected List<ExportEntry> prepareData(List<VaultEntry> data) {
        List<VaultEntry> tmpValues = queryData();
        if (tmpValues == null || tmpValues.isEmpty()) {
            return null;
        }

        for (VaultEntry value : tmpValues) {
            entries.add(new VaultEntrySourceCodeAdapter(value).toListCode());
        }

        // durty hack again to overcome savety features ...
        ArrayList<ExportEntry> dummy = new ArrayList<>();
        dummy.add(new CsvEntry() {
            @Override
            public String[] toCsvRecord() {
                return new String[]{};
            }

            @Override
            public String[] getCsvHeaderRecord() {
                return new String[]{};
            }
        });

        return dummy;
    }

}
