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

import de.jhit.opendiabetes.vault.container.BucketEntry;
import de.jhit.opendiabetes.vault.container.BucketEventTriggers;
import de.jhit.opendiabetes.vault.container.VaultEntry;
import de.jhit.opendiabetes.vault.container.VaultEntryType;
import java.io.FileWriter;
import java.io.IOException;
import java.sql.SQLException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

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
        String[] header = new String[oneHotHeader.size()];
        
        int i = 0;
        for(Map.Entry<VaultEntryType, Integer> entry : oneHotHeader.entrySet()){
            VaultEntryType key = entry.getKey();
            int value = entry.getValue();
            header[value] = key.toString();
            i++;
        }
        
        String result = Arrays.toString(header);
        result = result.replace("[", "");
        result = result.replace("]", "");
        
        return result;
    }

    public static void bucketsToCsv(List<BucketEntry> buckets, String filename) throws IOException, ParseException {

        FileWriter fw;
        fw = new FileWriter(filename);
        fw.write("index, " + createHeader() + "\n");
        try {
            for (int i = 0; i < buckets.size(); i++) {
                String line = Arrays.toString(buckets.get(i).getFullOnehotInformationArray());
                line = line.replace("[", "");
                line = line.replace("]", "");
                fw.write(i + ", " + line);
                fw.write(System.lineSeparator());
            }
        } catch (IOException ex) {
            Logger.getLogger(MLExporter.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            fw.close();
        }
    }

    public static void main(String[] args) throws ParseException, SQLException, IOException {

        List<BucketEntry> buckets = new ArrayList<>();
        buckets.add(new BucketEntry(0, StaticDataset.getStaticDataset().get(8)));
        buckets.add(new BucketEntry(0, StaticDataset.getStaticDataset().get(9)));
        buckets.add(new BucketEntry(0, StaticDataset.getStaticDataset().get(10)));
        buckets.add(new BucketEntry(0, StaticDataset.getStaticDataset().get(11)));

        bucketsToCsv(buckets, "toll.csv");


    }
}
