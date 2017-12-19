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
import de.jhit.opendiabetes.vault.container.csv.ExportEntry;
import de.jhit.opendiabetes.vault.data.VaultDao;
import de.jhit.opendiabetes.vault.util.TimestampUtils;
import java.io.IOException;
import java.sql.SQLException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 *
 * @author Jorg
 */
public class ExportierWas {

    /**
     * abstract class FileExporter —> abstract class CsvFileExporter —> abstract
     * class VaultCsvExporter —> class VaultOdvExporter Um eine Instanz eines
     * VaultOdvExporter zu erstellen benötigt man folgende Eingabeparameter:
     * ExporterOptions options —> ExporterOptions benötigt zwei Date-Objekte
     * VaultDao db —> Irgendeine Art von Datenbank wtf macht man damit? String
     * filePath —> Schreibt die Datei an den angegebenen Pfad
     *
     */
    public static void main(String[] args) throws SQLException, ParseException, IOException {
        Date from = new Date();
        Date d = new Date(100000);
        Date to = new Date();
        List<VaultEntry> data = StaticDataset.getStaticDataset();
        //TimestampUtils utils = new TimestampUtils();
        //String fromm = TimestampUtils.timestampToString(from, "TIME_FORMAT_LIBRE_DE");

        VaultDao.initializeDb();
        VaultDao v = VaultDao.getInstance();

        for (VaultEntry entry : data) {
            v.putEntry(entry);
        }

        ExporterOptions opt = new ExporterOptions(true, d, to);

        VaultOdvExporter exp = new VaultOdvExporter(opt, v, "datei.csv");

        // exp.writeToFile(csvEntries); // csvEntries muessen als Typ ExportEntry vorliegen. --> megastress
        //exp.toString();
        //exp.exportDataToFile(data);
        //System.out.println(exp);
        //System.out.println(d);
        //System.out.println(v.queryAllVaultEntrys());
        VaultCsvExporter vcsv = new VaultCsvExporter(opt, v, "csvdatei.csv");
        List<ExportEntry> listExpEnt = vcsv.prepareData(data);
        System.out.println(listExpEnt.size());

        exp.writeToFile(listExpEnt);

    }

}
