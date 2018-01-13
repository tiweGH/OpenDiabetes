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
public class ExporterManipulator_1 {

    public static List<VaultEntry> fillBuckets333(List<VaultEntry> liste) {
        List<VaultEntry> result = new ArrayList<>(liste);
        Date tempDate;
        for (int i = 0; i < result.size() - 1; i++) {
            tempDate = result.get(i).getTimestamp();
            if (result.get(i + 1).getTimestamp().getTime() - tempDate.getTime() > 60000) {
                result.add(i + 1, new VaultEntry(VaultEntryType.OTHER_ANNOTATION, TimestampUtils.addMinutesToTimestamp(tempDate, 1)));
            }
        }
        return result;
    }

    public static void main(String[] args) throws ParseException, SQLException, IOException {

        List<VaultEntry> dataTW = new ArrayList<>();
        dataTW.add(new VaultEntry(VaultEntryType.MEAL_MANUAL, TimestampUtils.createCleanTimestamp("2017.06.29-04:50", "yyyy.MM.dd-HH:mm")));
        dataTW.add(new VaultEntry(VaultEntryType.MEAL_MANUAL, TimestampUtils.createCleanTimestamp("2017.06.29-04:53", "yyyy.MM.dd-HH:mm")));

        List<VaultEntry> result = fillBuckets333(dataTW);

        for (VaultEntry vaultEntry : result) {
            System.out.println(vaultEntry.getTimestamp().toString());
        }
    }
}
