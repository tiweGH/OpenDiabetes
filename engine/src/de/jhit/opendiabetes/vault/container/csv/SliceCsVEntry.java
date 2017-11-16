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
package de.jhit.opendiabetes.vault.container.csv;

import de.jhit.opendiabetes.vault.container.SliceEntry;
import de.jhit.opendiabetes.vault.container.SliceFilterType;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Locale;

/**
 *
 * @author juehv
 */
public class SliceCsVEntry extends CsvEntry {

    public final static String VERSION_STRING = "v1";
    public final static double UNINITIALIZED_DOUBLE = SliceEntry.UNINITIALIZED_DOUBLE;

    private final SliceEntry data;

    public SliceCsVEntry(SliceEntry data) {
        this.data = data;
    }

    @Override
    public String[] toCsvRecord() {
        ArrayList<String> csvRecord = new ArrayList<>();
        csvRecord.add(new SimpleDateFormat("dd.MM.yy").format(data.getTimestamp()));
        csvRecord.add(new SimpleDateFormat("HH:mm").format(data.getTimestamp()));

        if (data.getDuration() > UNINITIALIZED_DOUBLE) {
            csvRecord.add(String.format(Locale.ENGLISH, DECIMAL_FORMAT, (double) data.getDuration()));
        } else {
            csvRecord.add("");
        }
//        if (data.getType() != null) {
//            csvRecord.add(data.getType().toString());
//        } else {
//            csvRecord.add("");
//        }
        if (!data.getFilterTypes().isEmpty()) {
            StringBuilder annotations = new StringBuilder();
            for (SliceFilterType item : data.getFilterTypes()) {
                annotations.insert(0, CSV_LIST_DELIMITER).insert(0, item);
            }
            annotations.deleteCharAt(annotations.length() - 1);
            csvRecord.add(annotations.toString());
        } else {
            csvRecord.add("");
        }

        return csvRecord.toArray(new String[]{});
    }

    @Override
    public String[] getCsvHeaderRecord() {
        return new String[]{
            "date",
            "time",
            "duration",
            "filter"
        };
    }

}
