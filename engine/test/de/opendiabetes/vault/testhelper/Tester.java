package de.opendiabetes.vault.testhelper;

import de.opendiabetes.vault.container.VaultEntry;
import de.opendiabetes.vault.exporter.MLExporter;
import de.opendiabetes.vault.plugin.exporter.FileExporter;
import java.io.IOException;
import java.text.ParseException;
import java.util.List;

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
/**
 *
 * @author
 */
public class Tester {

    public static void main(String[] args) throws ParseException, IOException {
        FileExporter mLExporter = new MLExporter();
        List<VaultEntry> data = StaticDataset.getStaticDataset();
        mLExporter.exportDataToFile("export", data);

    }

}
