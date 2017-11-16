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

import de.jhit.opendiabetes.vault.container.SliceEntry;
import de.jhit.opendiabetes.vault.container.VaultEntry;
import de.jhit.opendiabetes.vault.container.csv.ExportEntry;
import de.jhit.opendiabetes.vault.container.csv.SliceCsVEntry;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author juehv
 */
public class SliceLayoutCsvExporter extends CsvFileExporter {

    private List<SliceEntry> entries;

    public SliceLayoutCsvExporter(ExporterOptions options, String filePath, List<SliceEntry> entries) {
        super(options, filePath);
        this.entries = entries;
    }

    @Override
    protected List<ExportEntry> prepareData(List<VaultEntry> data) {
        List<ExportEntry> retVal = new ArrayList<>();
        for (SliceEntry item : entries) {
            retVal.add(new SliceCsVEntry(item));
        }
        return retVal;
    }
}
