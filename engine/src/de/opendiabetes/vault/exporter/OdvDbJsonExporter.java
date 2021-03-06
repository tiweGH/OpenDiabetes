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
package de.opendiabetes.vault.exporter;

import de.opendiabetes.vault.container.VaultEntry;
import de.opendiabetes.vault.container.csv.ExportEntry;
import de.opendiabetes.vault.container.csv.OdvDbJsonPseudoEntry;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author juehv
 */
public class OdvDbJsonExporter extends FileExporter {

    public OdvDbJsonExporter(ExporterOptions options, String filePath) {
        super(options, filePath);
    }

    @Override
    protected List<ExportEntry> prepareData(List<VaultEntry> data) {
        List<ExportEntry> container = new ArrayList<>();
        container.add(OdvDbJsonPseudoEntry.fromVaultEntryList(data));
        return container;
    }

}
