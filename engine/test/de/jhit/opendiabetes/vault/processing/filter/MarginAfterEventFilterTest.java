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
package de.jhit.opendiabetes.vault.processing.filter;

import de.jhit.opendiabetes.vault.container.VaultEntry;
import de.jhit.opendiabetes.vault.container.VaultEntryType;
import de.jhit.opendiabetes.vault.testhelper.StaticDataset;
import de.jhit.opendiabetes.vault.util.TimestampUtils;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javafx.util.Pair;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Assert;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

/**
 *
 * @author a.a.aponte
 */
public class MarginAfterEventFilterTest extends Assert {

    List<VaultEntry> data;

    @BeforeClass
    public static void setUpClass() {
    }

    @AfterClass
    public static void tearDownClass() {
    }

    @Before
    public void setUp() {
    }

    @After
    public void tearDown() {
    }

    /**
     * Test of filter method, of class FilterDecoratorTest.
     *
     * @author daniel, a.a.aponte
     */
    @Test
    public void checkFilterMethod() throws ParseException {
        Filter filter = new MarginAfterEventFilter(60, VaultEntryType.STRESS);

        data = StaticDataset.getStaticDataset();
        FilterResult result = filter.filter(data);

        List<VaultEntry> filteredData = new ArrayList<>();
        // 22
        filteredData.add(new VaultEntry(VaultEntryType.MEAL_MANUAL, TestFunctions.creatNewDateToCheckFor("2017.06.29-04:46"), 21.5));
        filteredData.add(new VaultEntry(VaultEntryType.MEAL_MANUAL, TestFunctions.creatNewDateToCheckFor("2017.06.29-06:06"), 35.5));
        filteredData.add(new VaultEntry(VaultEntryType.MEAL_MANUAL, TestFunctions.creatNewDateToCheckFor("2017.06.29-07:27"), 25.75));
        filteredData.add(new VaultEntry(VaultEntryType.MEAL_MANUAL, TestFunctions.creatNewDateToCheckFor("2017.06.29-10:50"), 52.75));
        filteredData.add(new VaultEntry(VaultEntryType.MEAL_MANUAL, TestFunctions.creatNewDateToCheckFor("2017.06.29-11:51"), 75.25));

        List<Pair<Date, Date>> timeSeries = new ArrayList<>();
        timeSeries.add(new Pair<>(TestFunctions.creatNewDateToCheckFor("2017.06.29-04:46"), TestFunctions.creatNewDateToCheckFor("2017.06.29-04:46")));
        timeSeries.add(new Pair<>(TestFunctions.creatNewDateToCheckFor("2017.06.29-06:06"), TestFunctions.creatNewDateToCheckFor("2017.06.29-06:06")));
        timeSeries.add(new Pair<>(TestFunctions.creatNewDateToCheckFor("2017.06.29-07:27"), TestFunctions.creatNewDateToCheckFor("2017.06.29-07:27")));
        timeSeries.add(new Pair<>(TestFunctions.creatNewDateToCheckFor("2017.06.29-10:50"), TestFunctions.creatNewDateToCheckFor("2017.06.29-10:50")));
        timeSeries.add(new Pair<>(TestFunctions.creatNewDateToCheckFor("2017.06.29-11:51"), TestFunctions.creatNewDateToCheckFor("2017.06.29-11:51")));

        FilterResult checkForThisResult = new FilterResult(filteredData, timeSeries);

        assertEquals(result.filteredData, checkForThisResult.filteredData);
        assertEquals(result.timeSeries, checkForThisResult.timeSeries);
        //assertEquals(result, checkForThisResult);
    }
}
