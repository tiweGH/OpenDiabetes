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
import de.jhit.opendiabetes.vault.container.BucketEventTriggers;
import de.jhit.opendiabetes.vault.container.VaultEntryType;
import java.io.FileWriter;
import java.io.IOException;
import java.text.ParseException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import org.apache.commons.csv.*;

/**
 *
 * @author Jorg
 */
public class ApacheCommonsCsvExporter {

    protected static String deleteComma(VaultEntry entry) {
        String result = entry.toString();
        return result.replace(",", " ");
    }

    protected static Object[] createHeader() {
        
        
        // TO DO: Sort HashMap by Value
        HashMap<VaultEntryType, Integer> oneHotHeader = BucketEventTriggers.ARRAY_ENTRY_TRIGGER_HASHMAP;
        String[] manualEntries = {"entry", "kjadskjdjkla"};
        Object[] result = new Object[oneHotHeader.size() + manualEntries.length];
        for (int k = 0; k < manualEntries.length; k++) {
            result[k] = manualEntries[k];
        }
        int i = manualEntries.length;
        for (VaultEntryType entryType : oneHotHeader.keySet()) {
            result[i] = entryType;
            i++;
        }
        return result;
    }

    protected static void writeFile(List<VaultEntry> data, int[] oha, String filePath) throws IOException {

//        Object[] header = {"x", "y"};
        Object[] header = createHeader();
        String NEW_LINE_SEPARATOR = "\n";

        FileWriter fileWriter = new FileWriter(filePath);
        CSVFormat csvFormat = CSVFormat.DEFAULT.withRecordSeparator(NEW_LINE_SEPARATOR);

        String oneHotArrayHeaderAsString = Arrays.toString(oha);
        oneHotArrayHeaderAsString = oneHotArrayHeaderAsString.replace("[", "");
        oneHotArrayHeaderAsString = oneHotArrayHeaderAsString.replace("]", "");
        System.out.println(oneHotArrayHeaderAsString);
        try (CSVPrinter csvPrinter = new CSVPrinter(fileWriter, csvFormat)) {
            csvPrinter.printRecord(header);
            for (VaultEntry en : data) {
                String entryString = deleteComma(en);
                csvPrinter.printRecord(entryString + ", " + oneHotArrayHeaderAsString);
            }
            csvPrinter.flush();
        }
    }

    public static void main(String[] args) throws ParseException, IOException {
        List<VaultEntry> d = StaticDataset.getStaticDataset();
        
        int[] oha = new int[23];
        for (int i = 0; i < oha.length ; i++) {
            oha[i] = i;
        }
        writeFile(d, oha, "asdas.csv");
    }
}
