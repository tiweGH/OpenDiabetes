/*
 * Copyright (C) 2017 Jens Heuschkel
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

import de.jhit.opendiabetes.vault.container.BucketEventTriggers;
import de.jhit.opendiabetes.vault.container.VaultEntry;
import de.jhit.opendiabetes.vault.container.VaultEntryAnnotation;
import de.jhit.opendiabetes.vault.container.VaultEntryType;
import de.jhit.opendiabetes.vault.container.csv.ExportEntry;
import de.jhit.opendiabetes.vault.container.csv.VaultCsvEntry;
import de.jhit.opendiabetes.vault.data.VaultDao;
import static de.jhit.opendiabetes.vault.exporter.FileExporter.LOG;
import de.jhit.opendiabetes.vault.util.EasyFormatter;
import de.jhit.opendiabetes.vault.util.TimestampUtils;
import java.io.FileWriter;
import java.io.IOException;
import java.sql.SQLException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;


// TO DO: Sort HashMap by Value
// Shape of Header: only index number and oha
// Klammern aus Header entfernen
// Statt VaultEntry Buckets einlesen



/**
 *
 * @author jorg
 */
public class MLExporter {

    protected static String deleteComma(VaultEntry entry) {
        String result = entry.toString();
        return result.replace(",", " ");
    }

    protected static String createHeader() {
        
        HashMap<VaultEntryType, Integer> oneHotHeader = BucketEventTriggers.ARRAY_ENTRY_TRIGGER_HASHMAP;
        String[] result = new String[oneHotHeader.size()];

//        String[] manualEntries = {"entry", "kjadskjdjkla"};
//        String[] result = new String[oneHotHeader.size() + manualEntries.length];
//        for (int k = 0; k < manualEntries.length; k++) {
//            result[k] = manualEntries[k];
//        }
//        int i = manualEntries.length;
        int i = 0;
        for (VaultEntryType entryType : oneHotHeader.keySet()) {
            result[i] = entryType.toString();
            result[i] = result[i].replace("[", "");
            result[i] = result[i].replace("]", "");
            i++;
        }
        //String res = Arrays.toString(result);
        return Arrays.toString(result);
    }

    public static void bucketsToCsv(String filename) throws IOException, ParseException {
        List<VaultEntry> data = StaticDataset.getStaticDataset();
        List<String> dataString = new ArrayList<>();

        for (VaultEntry entry : data) {
            dataString.add(entry.toString());
        }

        FileWriter fw;
        fw = new FileWriter(filename);
        fw.write(createHeader() + "\n");
        try {
            for (String x : dataString) {
                fw.write(x);
                fw.write(System.lineSeparator());
            }
        } catch (IOException ex) {
            Logger.getLogger(MLExporter.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            fw.close();
        }
    }

    public static void main(String[] args) throws ParseException, SQLException, IOException {
        bucketsToCsv("toll.csv");

//        VaultDao.initializeDb();
//        VaultDao v = VaultDao.getInstance();
//        Date from = new Date(100);
//        Date to = new Date();
//        List<VaultEntry> data = StaticDataset.getStaticDataset();
//
//        for (VaultEntry entry : data) {
//            v.putEntry(entry);
//        }
//
//        System.out.println(from);
//        System.out.println(to);
//
//        ExporterOptions opt = new ExporterOptions(true, from, to);
//        MLExporter mle = new MLExporter(opt, v, "lulz.csv");
//        mle.exportDataToFile(data);
    }
}
