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

import de.opendiabetes.vault.container.BucketEntry;
import de.opendiabetes.vault.container.BucketEventTriggers;
import de.opendiabetes.vault.container.FinalBucketEntry;
import de.opendiabetes.vault.container.VaultEntry;
import de.opendiabetes.vault.container.VaultEntryAnnotation;
import de.opendiabetes.vault.container.VaultEntryType;
import de.opendiabetes.vault.processing.buckets.BucketProcessor_old;
import de.opendiabetes.vault.processing.buckets.BucketProcessor_runable;
import de.opendiabetes.vault.util.TimestampUtils;
import java.io.FileWriter;
import java.io.IOException;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.sql.SQLException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author tiweGH
 */
public class VaultEntryJavacodeExporter {

    public static void compile(List<VaultEntry> entries, String filepath, String fileName) throws IOException, ParseException {

        FileWriter fw;
        String line;
        fw = new FileWriter(filepath);
        Calendar calTimestamp = Calendar.getInstance();
        String year, month, day, hour, minute;
        int dayNr = -1;
        fw.write("public class " + fileName + " {\n"
                + "\n"
                + "    public static List<VaultEntry> getExportDataset() throws ParseException {\n"
                + "        List<VaultEntry> vaultEntries = new ArrayList<>();\n"
                + "        List<VaultEntryAnnotation> tmpAnnotations;\n");
        try {

            for (VaultEntry entry : entries) {

                calTimestamp.setTime(entry.getTimestamp());
                year = "" + calTimestamp.get(Calendar.YEAR);
                month = "" + (calTimestamp.get(Calendar.MONTH) + 1);
                if (month.length() < 2) {
                    month = "0" + month;
                }
                day = "" + calTimestamp.get(Calendar.DAY_OF_MONTH);
                if (day.length() < 2) {
                    day = "0" + day;
                }
                hour = "" + calTimestamp.get(Calendar.HOUR_OF_DAY);
                if (hour.length() < 2) {
                    hour = "0" + hour;
                }
                minute = "" + calTimestamp.get(Calendar.MINUTE);
                if (minute.length() < 2) {
                    minute = "0" + minute;
                }
                if (dayNr != calTimestamp.get(Calendar.DAY_OF_YEAR)) {
                    fw.write("        //" + year + "." + month + "." + day + "\n");
                    dayNr = calTimestamp.get(Calendar.DAY_OF_YEAR);
                }
                line = ("        vaultEntries.add(new VaultEntry(");
                line = line + "VaultEntryType." + entry.getType();
                line = line + ",  TimestampUtils.createCleanTimestamp(\"" + year + "." + month + "." + day + "-" + hour + ":" + minute + "\", \"yyyy.MM.dd-HH:mm\")";
                line = line + ", " + entry.getValue() + "));";
                fw.write(line);
                fw.write(System.lineSeparator());
                //j++;
            }
        } catch (IOException ex) {
            Logger.getLogger(VaultEntryJavacodeExporter.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            fw.write("        return vaultEntries;\n"
                    + "    }\n"
                    + "}");
            fw.close();
        }
    }

    public static void main(String[] args) throws ParseException, SQLException, IOException {

        //compile(StaticDataset.getStaticDataset(), "C:/Users/Timm/Desktop/");
    }
}
