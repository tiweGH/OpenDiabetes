/*
 * Copyright (C) 2017 Jorg
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
import de.jhit.opendiabetes.vault.container.VaultEntryType;
import de.jhit.opendiabetes.vault.container.csv.ExportEntry;
import de.jhit.opendiabetes.vault.data.VaultDao;
import de.jhit.opendiabetes.vault.util.TimestampUtils;
import java.io.FileWriter;
import java.io.IOException;
import java.sql.SQLException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
//import org.apache.commons.csv.*;

/**
 *
 * @author Jorg
 */
public class ExporterManipulator {

    public static List<VaultEntry> fillBuckets(List<VaultEntry> liste) {

        long last = liste.get(liste.size() - 1).getTimestamp().getTime();
        int i = 0;
        while (liste.get(i).getTimestamp().getTime() < last) {
            Date tempDate1 = liste.get(i).getTimestamp();
            Date tempDate2 = liste.get(i + 1).getTimestamp();

            if ((tempDate2.getTime() - tempDate1.getTime()) > 60000) {
                tempDate1.setTime(tempDate1.getTime() + 60000);
                VaultEntry empty = new VaultEntry(VaultEntryType.BOLUS_SQARE, TimestampUtils.createCleanTimestamp(tempDate1));
                liste.add(i + 1, empty);
            }
            i = i + 1;
        }
        return liste;
    }

    public static List<VaultEntry> fillBuckets2(List<VaultEntry> liste) {
        List<VaultEntry> blah = new ArrayList<>();

        long last = liste.get(liste.size() - 1).getTimestamp().getTime();
        int i = 0;
        while (liste.get(i).getTimestamp().getTime() < last) {
            Date tempDate1 = liste.get(i).getTimestamp();
            Date tempDate2 = liste.get(i + 1).getTimestamp();

            int times = Math.round(((tempDate2.getTime() - tempDate1.getTime()) / 60000));
            if (times > 0) {
                for (int j = 0; j < times - 1; j++) {
                    tempDate1.setTime(tempDate1.getTime() + 60000);
                    VaultEntry empty = new VaultEntry(VaultEntryType.BOLUS_SQARE, TimestampUtils.createCleanTimestamp(tempDate1));
                    blah.add(empty);
                    // System.out.println(empty);
                }
            }
            i = i + 1;
        }
        return blah;
    }

    public static void main(String[] args) throws ParseException, SQLException, IOException {
        Date d = new Date(100000);
        Date to = new Date();
        List<VaultEntry> data = StaticDataset.getStaticDataset();

        VaultDao.initializeDb();
        VaultDao v = VaultDao.getInstance();
        System.out.println(data.size());
        for (VaultEntry entry : data) {
            v.putEntry(entry);
        }

        /*
        ExporterOptions opt = new ExporterOptions(true, d, to);
        VaultCsvExporter vcsv = new VaultCsvExporter(opt, v, "csvdatei.csv");
        vcsv.exportDataToFile(blah);

        for (int i = 0; i < 10; i++) {
        System.out.println(data.get(i));
        }
         */
//        List<VaultEntry> xxx = fillBuckets(data);
//        for (VaultEntry entry : xxx) {
//            System.out.println(entry);
//        }
//        Object[] header = {"x", "y"};
//        String NEW_LINE_SEPARATOR = "\n";
//
//        FileWriter  fileWriter= new FileWriter("yay.csv");
//        CSVFormat csvFormat = CSVFormat.DEFAULT.withRecordSeparator(NEW_LINE_SEPARATOR);
//
//        try (CSVPrinter csvPrinter = new CSVPrinter(fileWriter, csvFormat)) {
//            csvPrinter.printRecord(header);
//            for (VaultEntry en : data){
//
//                csvPrinter.printRecord(en);
//            }
//            csvPrinter.flush();
//        }
    }
}
