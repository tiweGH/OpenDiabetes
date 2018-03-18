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
import de.jhit.opendiabetes.vault.container.FinalBucketEntry;
import de.jhit.opendiabetes.vault.container.VaultEntry;
import de.jhit.opendiabetes.vault.container.VaultEntryType;
import de.jhit.opendiabetes.vault.container.csv.ExportEntry;
import de.jhit.opendiabetes.vault.processing.buckets.BucketProcessor;
import de.jhit.opendiabetes.vault.processing.buckets.BucketProcessor_runable;
import java.io.FileWriter;
import java.io.IOException;
import java.math.BigDecimal;
import java.math.RoundingMode;
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
public class MLExporter1 extends FileExporter{

    protected static String deleteComma(VaultEntry entry) {
        String result = entry.toString();
        return result.replace(",", " ");
    }

    protected static String createHeader() {

        HashMap<VaultEntryType, Integer> oneHotHeader = BucketEventTriggers.ARRAY_ENTRIES_AFTER_MERGE_TO;
        String[] header = new String[oneHotHeader.size()];

        int i = 0;
        for (Map.Entry<VaultEntryType, Integer> entry : oneHotHeader.entrySet()) {
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

    private static void shortenValues(FinalBucketEntry bucket, int i) throws ParseException {
                if ((bucket.getOnehotInformationArray(i) != 0.0) && (bucket.getOnehotInformationArray(i) != 1.0)) {
                    BigDecimal bd = new BigDecimal(bucket.getOnehotInformationArray(i));
                    bd = bd.setScale(2, RoundingMode.HALF_UP);
                    bucket.setOnehotInformationArray(i, bd.doubleValue());
                }
            }

    public static void bucketsToCsv(List<FinalBucketEntry> buckets, String filename) throws IOException, ParseException {
        int x = buckets.get(1).getFullOnehotInformationArray().length;
        FileWriter fw;
        fw = new FileWriter(filename);
        fw.write("index, " + createHeader() + "\n");
        try {
            //int j = 0;
            for (FinalBucketEntry bucket : buckets) {
                for (int i = 0; i < x; i++) {
                    shortenValues(bucket, i);
                }
                String line = Arrays.toString(bucket.getFullOnehotInformationArray());
                line = line.replace("[", "");
                line = line.replace("]", "");
                fw.write(bucket.getBucketNumber() + ", " + line);
                fw.write(System.lineSeparator());
                //j++;
            }
        } catch (IOException ex) {
            Logger.getLogger(MLExporter1.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            fw.close();
        }
    }

    public static void main(String[] args) throws ParseException, SQLException, IOException {

        List<FinalBucketEntry> buckets = new BucketProcessor_runable().processor(StaticDataset.getStaticDataset(), 1);
//        for (int i = 0; i < StaticDataset.getStaticDataset().size(); i++) {
//
//            buckets.add(new BucketEntry(0, StaticDataset.getStaticDataset().get(i)));
//        }
        bucketsToCsv(buckets, "odv_exportxx.csv");

    }

    public MLExporter1(ExporterOptions options, String filePath) {
        super(options, filePath);
    }

    @Override
    protected List<ExportEntry> prepareData(List<VaultEntry> data) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
}
