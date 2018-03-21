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
package de.opendiabetes.vault.exporter;

import de.opendiabetes.vault.container.BucketEventTriggers;
import de.opendiabetes.vault.container.FinalBucketEntry;
import de.opendiabetes.vault.container.VaultEntry;
import de.opendiabetes.vault.container.VaultEntryType;
import de.opendiabetes.vault.container.csv.ExportEntry;
import de.opendiabetes.vault.plugin.exporter.AbstractExporter;
import de.opendiabetes.vault.plugin.exporter.Exporter;
import de.opendiabetes.vault.processing.buckets.BucketProcessor;
import de.opendiabetes.vault.processing.buckets.BucketProcessor_old;
import de.opendiabetes.vault.processing.buckets.BucketProcessor_runable;
import java.io.FileWriter;
import java.io.IOException;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author jorg
 */
public class MLExporter extends de.opendiabetes.vault.plugin.exporter.FileExporter{

    private int wantedBucketSize;
    private String filePath;

    public MLExporter(int wantedBucketSize, String filePath) {
        this.wantedBucketSize = wantedBucketSize;
        this.filePath = filePath;
    }

    protected String deleteComma(VaultEntry entry) {
        String result = entry.toString();
        return result.replace(",", " ");
    }

    protected String createHeader() {

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

    private void shortenValues(FinalBucketEntry bucket, int i) throws ParseException {
        if ((bucket.getOnehotInformationArray(i) != 0.0) && (bucket.getOnehotInformationArray(i) != 1.0)) {
            BigDecimal bd = new BigDecimal(bucket.getOnehotInformationArray(i));
            bd = bd.setScale(2, RoundingMode.HALF_UP);
            bucket.setOnehotInformationArray(i, bd.doubleValue());
        }
    }

    public void exportDataToFile(List<VaultEntry> data) throws IOException, ParseException {
        String temp = filePath;
        long start;
        start = System.currentTimeMillis();
        System.out.println("Start new BProc at " + new Date(start));
        BucketProcessor processor = new BucketProcessor();
        writeToFile(processor.runProcess(0, data, wantedBucketSize));
        System.out.println("Writing took " + (System.currentTimeMillis() - start));
    }

    private void writeToFile(List<FinalBucketEntry> buckets) throws IOException, ParseException {
        List<ExportEntry> exportEntrys = new ArrayList<>();
        //int x = buckets.get(1).getFullOnehotInformationArray().length;
        FileWriter fw;
        //System.out.println("writing File to " + filePath + ".csv");
        fw = new FileWriter(filePath + ".csv");
        fw.write("index, " + createHeader() + "\n");
        try {
            //int j = 0;
            for (FinalBucketEntry bucket : buckets) {
                for (int i = 0; i < bucket.getFullOnehotInformationArray().length; i++) {
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
            Logger.getLogger(MLExporter.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            fw.close();
        }
    }

    @Override
    public void setEntries(List<?> entries) throws IllegalArgumentException {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public int exportDataToFile(String filePath, List<VaultEntry> data) throws IOException {
        this.filePath = filePath;
        //vermutlich über loadConfiguration
        wantedBucketSize = 1;
        try {
            exportDataToFile(data);
        } catch (ParseException ex) {
            Logger.getLogger(MLExporter.class.getName()).log(Level.SEVERE, null, ex);
            return ReturnCode.RESULT_ERROR.getCode();
        }
        return ReturnCode.RESULT_OK.getCode();
    }

    @Override
    protected List<ExportEntry> prepareData(List<VaultEntry> data) {
        
        BucketProcessor processor = new BucketProcessor();
        
        List<ExportEntry> exportEntrys = new ArrayList<>();
        
        try{            
            for (FinalBucketEntry runProces : processor.runProcess(0, data, wantedBucketSize)) {
                exportEntrys.add(runProces);
            }
        }catch (Throwable t)
        {
            t.printStackTrace();
        }
        
        return exportEntrys;
    }
}
