/*
 * Copyright (C) 2017 gizem
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
import de.opendiabetes.vault.processing.filter.options.DateTimeSpanFilterOption;
import de.opendiabetes.vault.testhelper.SensitivityDataset;
import de.opendiabetes.vault.testhelper.StaticDataset;
import de.opendiabetes.vault.util.TimestampUtils;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javafx.util.Pair;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Assert;
import static org.junit.Assert.assertTrue;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

/**
 *
 * @author gizem, a.a.aponte
 */
public class DateTimeSpanFilterTest extends Assert {

    static List<VaultEntry> data;
    static List<VaultEntry> dataSet;

    public DateTimeSpanFilterTest() {
    }

    @BeforeClass
    public static void setUpClass() throws ParseException {
        data = SensitivityDataset.getSensitivityDataset();
        dataSet = StaticDataset.getStaticDataset();
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

    DateTimeSpanFilter setUpFilter(Date start, Date end) {
        return new DateTimeSpanFilter(new DateTimeSpanFilterOption(start, end));
    }

    /**
     * Test of filter method, of class DateTimeSpanFilter.
     *
     * @author gizem, a.a.aponte
     */
    @Test
    public void testDateTimeSpanFilterTest_0107_0122() throws ParseException {
        // System.out.println("filter");
        String dateTimePointBegin = "2017.08.02-01:07";
        String dateTimePointEnd = "2017.08.03-01:22";

        DateTimeSpanFilter instance = setUpFilter(TestFunctions.creatNewDateToCheckFor(dateTimePointBegin), TestFunctions.creatNewDateToCheckFor(dateTimePointEnd));
        FilterResult result = instance.filter(data);

        for (VaultEntry entry : result.filteredData) {
            assertTrue(TimestampUtils.withinDateTimeSpan(TestFunctions.creatNewDateToCheckFor(dateTimePointBegin),
                    TestFunctions.creatNewDateToCheckFor(dateTimePointEnd), entry.getTimestamp()));
        }
    }

    @Test
    public void testDateTimeSpanFilterTest_0803_0959() throws ParseException {
        String dateTimePointBegin = "2016.04.18-08:03";
        String dateTimePointEnd = "2016.04.18-09:59";

        DateTimeSpanFilter instance = setUpFilter(TestFunctions.creatNewDateToCheckFor(dateTimePointBegin), TestFunctions.creatNewDateToCheckFor(dateTimePointEnd));
        FilterResult result = instance.filter(data);

        List<VaultEntry> filteredData = new ArrayList<>();
        filteredData.add(new VaultEntry(VaultEntryType.GLUCOSE_CGM_ALERT, TestFunctions.creatNewDateToCheckFor("2016.04.18-08:03"), 213));
        filteredData.add(new VaultEntry(VaultEntryType.GLUCOSE_BG_MANUAL, TestFunctions.creatNewDateToCheckFor("2016.04.18-08:05"), 152));
        filteredData.add(new VaultEntry(VaultEntryType.GLUCOSE_CGM, TestFunctions.creatNewDateToCheckFor("2016.04.18-08:13"), 206));
        filteredData.add(new VaultEntry(VaultEntryType.GLUCOSE_CGM, TestFunctions.creatNewDateToCheckFor("2016.04.18-08:28"), 193));
        filteredData.add(new VaultEntry(VaultEntryType.GLUCOSE_CGM, TestFunctions.creatNewDateToCheckFor("2016.04.18-08:43"), 180));
        filteredData.add(new VaultEntry(VaultEntryType.GLUCOSE_CGM, TestFunctions.creatNewDateToCheckFor("2016.04.18-08:58"), 168));
        filteredData.add(new VaultEntry(VaultEntryType.BASAL_PROFILE, TestFunctions.creatNewDateToCheckFor("2016.04.18-09:00"), 1.3));
        filteredData.add(new VaultEntry(VaultEntryType.GLUCOSE_CGM, TestFunctions.creatNewDateToCheckFor("2016.04.18-09:13"), 156));
        filteredData.add(new VaultEntry(VaultEntryType.GLUCOSE_CGM, TestFunctions.creatNewDateToCheckFor("2016.04.18-09:29"), 141));
        filteredData.add(new VaultEntry(VaultEntryType.GLUCOSE_CGM_ALERT, TestFunctions.creatNewDateToCheckFor("2016.04.18-09:31"), 145));
        filteredData.add(new VaultEntry(VaultEntryType.BOLUS_NORMAL, TestFunctions.creatNewDateToCheckFor("2016.04.18-09:33"), 2));
        filteredData.add(new VaultEntry(VaultEntryType.GLUCOSE_CGM, TestFunctions.creatNewDateToCheckFor("2016.04.18-09:44"), 125));
        filteredData.add(new VaultEntry(VaultEntryType.GLUCOSE_CGM_ALERT, TestFunctions.creatNewDateToCheckFor("2016.04.18-09:44"), 126));
        filteredData.add(new VaultEntry(VaultEntryType.BOLUS_NORMAL, TestFunctions.creatNewDateToCheckFor("2016.04.18-09:45"), 5));
        filteredData.add(new VaultEntry(VaultEntryType.GLUCOSE_CGM, TestFunctions.creatNewDateToCheckFor("2016.04.18-09:59"), 118));

        List<Pair<Date, Date>> timeSeries = new ArrayList<>();
        timeSeries.add(new Pair<>(TestFunctions.creatNewDateToCheckFor("2016.04.18-08:03"), TestFunctions.creatNewDateToCheckFor("2016.04.18-09:59")));

        FilterResult checkForThisResult = new FilterResult(filteredData, timeSeries);

        assertEquals(result.filteredData, checkForThisResult.filteredData);
        assertEquals(result.timeSeries, checkForThisResult.timeSeries);
        //assertEquals(result, checkForThisResult);
    }

    @Test
    public void testDateTimeSpanFilterTest_0506_0656() throws ParseException {
        String dateTimePointBegin = "2017.06.29-05:06";
        String dateTimePointEnd = "2017.06.29-06:56";

        DateTimeSpanFilter instance = setUpFilter(TestFunctions.creatNewDateToCheckFor(dateTimePointBegin), TestFunctions.creatNewDateToCheckFor(dateTimePointEnd));
        FilterResult result = instance.filter(dataSet);

        List<VaultEntry> filteredData = new ArrayList<>();
        filteredData.add(new VaultEntry(VaultEntryType.HEART_RATE, TestFunctions.creatNewDateToCheckFor("2017.06.29-05:06"), 59.0));
        filteredData.add(new VaultEntry(VaultEntryType.STRESS, TestFunctions.creatNewDateToCheckFor("2017.06.29-05:06"), 23.75));
        filteredData.add(new VaultEntry(VaultEntryType.HEART_RATE_VARIABILITY, TestFunctions.creatNewDateToCheckFor("2017.06.29-05:06"), 50.0, 105.0));
        filteredData.add(new VaultEntry(VaultEntryType.HEART_RATE, TestFunctions.creatNewDateToCheckFor("2017.06.29-05:16"), 53.0));
        filteredData.add(new VaultEntry(VaultEntryType.HEART_RATE_VARIABILITY, TestFunctions.creatNewDateToCheckFor("2017.06.29-05:16"), 50.0, 123.0));
        filteredData.add(new VaultEntry(VaultEntryType.STRESS, TestFunctions.creatNewDateToCheckFor("2017.06.29-05:16"), 19.25));
        filteredData.add(new VaultEntry(VaultEntryType.SLEEP_LIGHT, TestFunctions.creatNewDateToCheckFor("2017.06.29-05:22"), 9.0));
        filteredData.add(new VaultEntry(VaultEntryType.HEART_RATE_VARIABILITY, TestFunctions.creatNewDateToCheckFor("2017.06.29-05:26"), 50.0, 113.0));
        filteredData.add(new VaultEntry(VaultEntryType.STRESS, TestFunctions.creatNewDateToCheckFor("2017.06.29-05:26"), 21.75));
        filteredData.add(new VaultEntry(VaultEntryType.HEART_RATE, TestFunctions.creatNewDateToCheckFor("2017.06.29-05:26"), 58.0));
        filteredData.add(new VaultEntry(VaultEntryType.SLEEP_DEEP, TestFunctions.creatNewDateToCheckFor("2017.06.29-05:31"), 10.0));
        filteredData.add(new VaultEntry(VaultEntryType.HEART_RATE, TestFunctions.creatNewDateToCheckFor("2017.06.29-05:36"), 59.0));
        filteredData.add(new VaultEntry(VaultEntryType.SLEEP_LIGHT, TestFunctions.creatNewDateToCheckFor("2017.06.29-05:41"), 22.0));
        filteredData.add(new VaultEntry(VaultEntryType.HEART_RATE, TestFunctions.creatNewDateToCheckFor("2017.06.29-05:46"), 57.0));
        filteredData.add(new VaultEntry(VaultEntryType.HEART_RATE, TestFunctions.creatNewDateToCheckFor("2017.06.29-05:56"), 56.0));
        filteredData.add(new VaultEntry(VaultEntryType.BASAL_PROFILE, TestFunctions.creatNewDateToCheckFor("2017.06.29-06:00"), 1.05));
        filteredData.add(new VaultEntry(VaultEntryType.SLEEP_DEEP, TestFunctions.creatNewDateToCheckFor("2017.06.29-06:03"), 10.0));
        filteredData.add(new VaultEntry(VaultEntryType.HEART_RATE, TestFunctions.creatNewDateToCheckFor("2017.06.29-06:06"), 71.0));
        filteredData.add(new VaultEntry(VaultEntryType.HEART_RATE_VARIABILITY, TestFunctions.creatNewDateToCheckFor("2017.06.29-06:06"), 50.0, 86.0));
        filteredData.add(new VaultEntry(VaultEntryType.STRESS, TestFunctions.creatNewDateToCheckFor("2017.06.29-06:06"), 35.5));
        filteredData.add(new VaultEntry(VaultEntryType.SLEEP_LIGHT, TestFunctions.creatNewDateToCheckFor("2017.06.29-06:13"), 9.0));
        filteredData.add(new VaultEntry(VaultEntryType.HEART_RATE, TestFunctions.creatNewDateToCheckFor("2017.06.29-06:16"), 66.0));
        filteredData.add(new VaultEntry(VaultEntryType.HEART_RATE_VARIABILITY, TestFunctions.creatNewDateToCheckFor("2017.06.29-06:16"), 50.0, 93.0));
        filteredData.add(new VaultEntry(VaultEntryType.STRESS, TestFunctions.creatNewDateToCheckFor("2017.06.29-06:16"), 30.25));
        filteredData.add(new VaultEntry(VaultEntryType.SLEEP_LIGHT, TestFunctions.creatNewDateToCheckFor("2017.06.29-06:22"), 7.0));
        filteredData.add(new VaultEntry(VaultEntryType.STRESS, TestFunctions.creatNewDateToCheckFor("2017.06.29-06:26"), 27.25));
        filteredData.add(new VaultEntry(VaultEntryType.HEART_RATE_VARIABILITY, TestFunctions.creatNewDateToCheckFor("2017.06.29-06:26"), 50.0, 97.0));
        filteredData.add(new VaultEntry(VaultEntryType.HEART_RATE, TestFunctions.creatNewDateToCheckFor("2017.06.29-06:26"), 62.0));
        filteredData.add(new VaultEntry(VaultEntryType.SLEEP_DEEP, TestFunctions.creatNewDateToCheckFor("2017.06.29-06:29"), 11.0));
        filteredData.add(new VaultEntry(VaultEntryType.HEART_RATE, TestFunctions.creatNewDateToCheckFor("2017.06.29-06:36"), 63.0));
        filteredData.add(new VaultEntry(VaultEntryType.HEART_RATE_VARIABILITY, TestFunctions.creatNewDateToCheckFor("2017.06.29-06:36"), 50.0, 98.0));
        filteredData.add(new VaultEntry(VaultEntryType.STRESS, TestFunctions.creatNewDateToCheckFor("2017.06.29-06:36"), 26.5));
        filteredData.add(new VaultEntry(VaultEntryType.SLEEP_LIGHT, TestFunctions.creatNewDateToCheckFor("2017.06.29-06:40"), 15.0));
        filteredData.add(new VaultEntry(VaultEntryType.HEART_RATE, TestFunctions.creatNewDateToCheckFor("2017.06.29-06:46"), 58.0));
        filteredData.add(new VaultEntry(VaultEntryType.HEART_RATE_VARIABILITY, TestFunctions.creatNewDateToCheckFor("2017.06.29-06:46"), 50.0, 97.0));
        filteredData.add(new VaultEntry(VaultEntryType.STRESS, TestFunctions.creatNewDateToCheckFor("2017.06.29-06:46"), 27.25));
        filteredData.add(new VaultEntry(VaultEntryType.SLEEP_DEEP, TestFunctions.creatNewDateToCheckFor("2017.06.29-06:55"), 27.0));
        filteredData.add(new VaultEntry(VaultEntryType.HEART_RATE, TestFunctions.creatNewDateToCheckFor("2017.06.29-06:56"), 59.0));

        List<Pair<Date, Date>> timeSeries = new ArrayList<>();
        timeSeries.add(new Pair<>(TestFunctions.creatNewDateToCheckFor("2017.06.29-05:06"), TestFunctions.creatNewDateToCheckFor("2017.06.29-06:56")));

        FilterResult checkForThisResult = new FilterResult(filteredData, timeSeries);

        assertEquals(result.filteredData, checkForThisResult.filteredData);
        assertEquals(result.timeSeries, checkForThisResult.timeSeries);
        //assertEquals(result, checkForThisResult);
    }

    @Test
    public void testDateTimeSpanFilterTest_1110_1151() throws ParseException {
        String dateTimePointBegin = "2017.06.29-11:10";
        String dateTimePointEnd = "2017.06.29-11:51";

        DateTimeSpanFilter instance = setUpFilter(TestFunctions.creatNewDateToCheckFor(dateTimePointBegin), TestFunctions.creatNewDateToCheckFor(dateTimePointEnd));
        FilterResult result = instance.filter(dataSet);

        List<VaultEntry> filteredData = new ArrayList<>();
        filteredData.add(new VaultEntry(VaultEntryType.STRESS, TestFunctions.creatNewDateToCheckFor("2017.06.29-11:10"), 61.0));
        filteredData.add(new VaultEntry(VaultEntryType.HEART_RATE, TestFunctions.creatNewDateToCheckFor("2017.06.29-11:10"), 77.0));
        filteredData.add(new VaultEntry(VaultEntryType.HEART_RATE_VARIABILITY, TestFunctions.creatNewDateToCheckFor("2017.06.29-11:10"), 43.0, 52.0));
        filteredData.add(new VaultEntry(VaultEntryType.HEART_RATE, TestFunctions.creatNewDateToCheckFor("2017.06.29-11:21"), 68.0));
        filteredData.add(new VaultEntry(VaultEntryType.HEART_RATE_VARIABILITY, TestFunctions.creatNewDateToCheckFor("2017.06.29-11:21"), 43.0, 59.0));
        filteredData.add(new VaultEntry(VaultEntryType.STRESS, TestFunctions.creatNewDateToCheckFor("2017.06.29-11:21"), 55.75));
        filteredData.add(new VaultEntry(VaultEntryType.HEART_RATE, TestFunctions.creatNewDateToCheckFor("2017.06.29-11:30"), 51.0));
        filteredData.add(new VaultEntry(VaultEntryType.HEART_RATE_VARIABILITY, TestFunctions.creatNewDateToCheckFor("2017.06.29-11:40"), 43.0, 162.0));
        filteredData.add(new VaultEntry(VaultEntryType.HEART_RATE, TestFunctions.creatNewDateToCheckFor("2017.06.29-11:40"), 94.0));
        filteredData.add(new VaultEntry(VaultEntryType.STRESS, TestFunctions.creatNewDateToCheckFor("2017.06.29-11:40"), 9.5));
        filteredData.add(new VaultEntry(VaultEntryType.HEART_RATE_VARIABILITY, TestFunctions.creatNewDateToCheckFor("2017.06.29-11:51"), 41.0, 33.0));
        filteredData.add(new VaultEntry(VaultEntryType.STRESS, TestFunctions.creatNewDateToCheckFor("2017.06.29-11:51"), 75.25));
        filteredData.add(new VaultEntry(VaultEntryType.HEART_RATE, TestFunctions.creatNewDateToCheckFor("2017.06.29-11:51"), 58.0));

        List<Pair<Date, Date>> timeSeries = new ArrayList<>();
        timeSeries.add(new Pair<>(TestFunctions.creatNewDateToCheckFor("2017.06.29-11:10"), TestFunctions.creatNewDateToCheckFor("2017.06.29-11:51")));

        FilterResult checkForThisResult = new FilterResult(filteredData, timeSeries);

        assertEquals(result.filteredData, checkForThisResult.filteredData);
        assertEquals(result.timeSeries, checkForThisResult.timeSeries);
        //assertEquals(result, checkForThisResult);
    }
}
