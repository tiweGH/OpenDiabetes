package de.opendiabetes.vault.testhelper;

/*
 * Copyright (C) 2017 tiweGH
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
import de.opendiabetes.vault.container.VaultEntry;
import de.opendiabetes.vault.container.VaultEntryTypeGroup;
import de.opendiabetes.vault.data.VaultDao;
import de.opendiabetes.vault.exporter.MLExporter;
import de.opendiabetes.vault.exporter.VaultEntryJavacodeExporter;
import de.opendiabetes.vault.plugin.fileimporter.FileImporter;
import de.opendiabetes.vault.processing.VaultEntrySlicer;
import de.opendiabetes.vault.processing.filter.DateTimeSpanFilter;
import de.opendiabetes.vault.processing.filter.Filter;
import de.opendiabetes.vault.processing.filter.FilterResult;
import de.opendiabetes.vault.processing.filter.options.DateTimeSpanFilterOption;
import de.opendiabetes.vault.testhelper.filterfactory.NoDateTimeSpansWithoutGroup;
import de.opendiabetes.vault.util.TimestampUtils;
import de.opendiabetes.vault.plugin.importer.odvdbjson.ODVDBJsonImporter;

import java.io.IOException;
import java.sql.SQLException;
import java.text.ParseException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author tiweGH
 */
public class Tester {

    public static void main(String[] args) {
        
        /**
        try {
            List<VaultEntry> entryList = StaticDataset.getStaticDataset();
            List<VaultEntry> sensyList = SensitivityDataset.getSensitivityDataset();
            List<VaultEntry> custSet = CustomDataset.getCustomDataset();
            List<VaultEntry> workingSet = custSet;
            List<Filter> fl = new ArrayList<>();
            FilterResult res;
            String importPath = "C:\\Users\\Timm\\Desktop\\bp\\export_stuff\\test\\export-v10_3-201801-164406.json";
            String path = "C:\\Users\\Timm\\Desktop\\bp\\export_stuff\\test\\";
            MLExporter exporter = new MLExporter(10, path + "asd");
            VaultDao.initializeDb();

            
            PumpInterpreterOptions iOptions = new PumpInterpreterOptions(
                    false,
                    60,
                    false,
                    TimestampUtils.fromLocalDate(LocalDate.now()),
                    TimestampUtils.fromLocalDate(LocalDate.now(), 86399000));

            NonInterpreter interpreter = new NonInterpreter(
                    new ODVDBJsonImporter(null),
                    iOptions,
                    VaultDao.getInstance());
            if (importPath != null && !importPath.isEmpty()) {
                ((FileImporter) interpreter.getImporter()).setImportFilePath(importPath);
                interpreter.importAndInterpret();
            }
            workingSet = VaultDao.getInstance().queryAllVaultEntries();
            fl = new NoDateTimeSpansWithoutGroup(workingSet, VaultEntryTypeGroup.HEART, 24 * 60).createFilter();

            VaultEntrySlicer slicer = new VaultEntrySlicer();
            slicer.registerFilter(new DateTimeSpanFilter(new DateTimeSpanFilterOption(workingSet.get(0).getTimestamp(),
                    TimestampUtils.addMinutesToTimestamp(workingSet.get(0).getTimestamp(), 48 * 60))));
            slicer.registerFilter(fl);
            workingSet = slicer.sliceEntries(workingSet).filteredData;
            System.out.println(workingSet.toString());

            exporter.exportDataToFile(workingSet);
            VaultEntryJavacodeExporter.compile(workingSet, path + "ASDClass.java", "ASDClass");

        } catch (ParseException ex) {
            Logger.getLogger(Tester.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(Tester.class.getName()).log(Level.SEVERE, null, ex);
        } catch (SQLException ex) {
            Logger.getLogger(Tester.class.getName()).log(Level.SEVERE, null, ex);
        }
        **/
    }
    
}
