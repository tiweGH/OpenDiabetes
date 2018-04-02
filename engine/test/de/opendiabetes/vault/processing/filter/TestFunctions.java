/*
 * Copyright (C) 2017 a.a.aponte
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
package de.opendiabetes.vault.processing.filter;

import de.opendiabetes.vault.container.VaultEntry;
import de.opendiabetes.vault.container.VaultEntryType;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javafx.util.Pair;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

/**
 *
 * @author a.a.aponte
 */
public class TestFunctions {
    
    // Checks for the correct VaultEntryType in all result entries.
    public static void checkForVaultEntryType(FilterResult result, VaultEntryType typeToCheckFor){
        for (VaultEntry entry : result.filteredData) {
            assertTrue(entry.getType()==typeToCheckFor);
        }
    }
    
    // Checks for the correct start and end timestams
    public static void checkForTimestamp(String startDateStart, String startDateEnd, String endDateStart, String endDateEnd, FilterResult toTest) throws ParseException{
        Date dateBeginStart = creatNewDateToCheckFor(startDateStart);
        Date dateBeginEnd = creatNewDateToCheckFor(startDateEnd);
        Date dateEndStart = creatNewDateToCheckFor(endDateStart);
        Date dateEndEnd = creatNewDateToCheckFor(endDateEnd);

        // New pair with the date's to check for
        List<Pair<Date, Date>> pairToCheckFor = new ArrayList<>();
        pairToCheckFor.add(new Pair<>(dateBeginStart, dateBeginEnd));
        pairToCheckFor.add(new Pair<>(dateEndStart, dateEndEnd));

        // New pair with the given date's to compare
        List<Pair<Date, Date>> pairGiven = new ArrayList<>();
        // get the first date pair
        pairGiven.add(toTest.timeSeries.get(0));
        // get the last date pair
        pairGiven.add(toTest.timeSeries.get(toTest.timeSeries.size() - 1));

        assertEquals(pairToCheckFor, pairGiven);
    }
    
    // Should not throw any exceptions.
    // Creats a new date
    public static Date creatNewDateToCheckFor(String date) throws ParseException{
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy.MM.dd-HH:mm");
        return  sdf.parse(date);
    }
    
}
