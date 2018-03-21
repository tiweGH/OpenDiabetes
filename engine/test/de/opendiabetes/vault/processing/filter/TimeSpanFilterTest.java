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
package de.opendiabetes.vault.processing.filter;

import de.opendiabetes.vault.processing.filter.TimeSpanFilter;
import de.opendiabetes.vault.processing.filter.FilterResult;
import de.opendiabetes.vault.container.VaultEntry;
import de.opendiabetes.vault.container.VaultEntryAnnotation;
import de.opendiabetes.vault.container.VaultEntryType;
import de.opendiabetes.vault.exporter.NewDataset;
import de.opendiabetes.vault.processing.filter.options.TimeSpanFilterOption;
import de.opendiabetes.vault.testhelper.StaticDataset;
import de.opendiabetes.vault.util.TimestampUtils;
import java.text.ParseException;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javafx.util.Pair;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Assert;
import static org.junit.Assert.assertEquals;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

/**
 *
 * @author juehv, a.a.aponte
 */
public class TimeSpanFilterTest extends Assert {

    static List<VaultEntry> data;
    static List<VaultEntry> newData;
    List<VaultEntryAnnotation> tmpAnnotations;

    public TimeSpanFilterTest() {
    }

    @BeforeClass
    public static void setUpClass() throws ParseException {
        data = StaticDataset.getStaticDataset();
        newData = NewDataset.getNewDataset();
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

    TimeSpanFilter setUpFilter(LocalTime start, LocalTime end) {

        return new TimeSpanFilter(new TimeSpanFilterOption(start, end));
    }

    /**
     * Test of filter method, of class TimeSpanFilter.
     *
     * @author juehv, a.a.aponte
     */
    @Test
    public void testDateTimeSpanFilterTest_1200_1230() throws ParseException {
        //System.out.println("filter");
        TimeSpanFilter instance = setUpFilter(LocalTime.parse("12:00"),
                LocalTime.parse("12:30"));
        FilterResult result = instance.filter(data);

        for (VaultEntry entry : result.filteredData) {
            assertTrue(TimestampUtils.withinTimeSpan(LocalTime.parse("12:00"),
                    LocalTime.parse("12:30"), entry.getTimestamp()));
        }

        List<VaultEntry> filteredData = new ArrayList<>();
        filteredData.add(new VaultEntry(VaultEntryType.HEART_RATE, TestFunctions.creatNewDateToCheckFor("2017.06.29-12:00"), 55.0));
        filteredData.add(new VaultEntry(VaultEntryType.BASAL_PROFILE, TestFunctions.creatNewDateToCheckFor("2017.06.29-12:00"), 0.9));
        filteredData.add(new VaultEntry(VaultEntryType.STRESS, TestFunctions.creatNewDateToCheckFor("2017.06.29-12:11"), 17.25));
        filteredData.add(new VaultEntry(VaultEntryType.HEART_RATE_VARIABILITY, TestFunctions.creatNewDateToCheckFor("2017.06.29-12:11"), 41.0, 131.0));
        filteredData.add(new VaultEntry(VaultEntryType.HEART_RATE, TestFunctions.creatNewDateToCheckFor("2017.06.29-12:21"), 51.0));
        filteredData.add(new VaultEntry(VaultEntryType.HEART_RATE_VARIABILITY, TestFunctions.creatNewDateToCheckFor("2017.06.29-12:21"), 44.0, 127.0));
        filteredData.add(new VaultEntry(VaultEntryType.STRESS, TestFunctions.creatNewDateToCheckFor("2017.06.29-12:21"), 18.25));

        List<Pair<Date, Date>> timeSeries = new ArrayList<>();
        timeSeries.add(new Pair<>(TestFunctions.creatNewDateToCheckFor("2017.06.29-12:00"), TestFunctions.creatNewDateToCheckFor("2017.06.29-12:21")));

        FilterResult checkForThisResult = new FilterResult(filteredData, timeSeries);

        assertEquals(result.filteredData, checkForThisResult.filteredData);
        assertEquals(result.timeSeries, checkForThisResult.timeSeries);
        //assertEquals(result, checkForThisResult);
    }

    @Test
    public void testDateTimeSpanFilterTest_0800_1200() throws ParseException {
        TimeSpanFilter instance = setUpFilter(LocalTime.parse("08:00"),
                LocalTime.parse("12:00"));
        FilterResult result = instance.filter(data);

        List<VaultEntry> filteredData = new ArrayList<>();
        filteredData.add(new VaultEntry(VaultEntryType.BASAL_PROFILE, TestFunctions.creatNewDateToCheckFor("2017.06.29-08:00"), 1.25));
        filteredData.add(new VaultEntry(VaultEntryType.STRESS, TestFunctions.creatNewDateToCheckFor("2017.06.29-08:06"), 27.25));
        filteredData.add(new VaultEntry(VaultEntryType.HEART_RATE_VARIABILITY, TestFunctions.creatNewDateToCheckFor("2017.06.29-08:06"), 50.0, 97.0));
        filteredData.add(new VaultEntry(VaultEntryType.HEART_RATE, TestFunctions.creatNewDateToCheckFor("2017.06.29-08:06"), 62.0));
        filteredData.add(new VaultEntry(VaultEntryType.STRESS, TestFunctions.creatNewDateToCheckFor("2017.06.29-08:16"), 0.0));
        tmpAnnotations = new ArrayList<>();
        tmpAnnotations.add(new VaultEntryAnnotation(VaultEntryAnnotation.TYPE.GLUCOSE_BG_METER_SERIAL).setValue("BG1140084B"));
        filteredData.add(new VaultEntry(VaultEntryType.GLUCOSE_BG, TestFunctions.creatNewDateToCheckFor("2017.06.29-08:39"), 181.0, tmpAnnotations));
        filteredData.add(new VaultEntry(VaultEntryType.GLUCOSE_BOLUS_CALCULATION, TestFunctions.creatNewDateToCheckFor("2017.06.29-08:40"), 181.0));
        filteredData.add(new VaultEntry(VaultEntryType.BOLUS_NORMAL, TestFunctions.creatNewDateToCheckFor("2017.06.29-08:42"), 3.2));
        filteredData.add(new VaultEntry(VaultEntryType.BASAL_PROFILE, TestFunctions.creatNewDateToCheckFor("2017.06.29-09:00"), 1.3));
        filteredData.add(new VaultEntry(VaultEntryType.BASAL_PROFILE, TestFunctions.creatNewDateToCheckFor("2017.06.29-10:00"), 1.1));
        filteredData.add(new VaultEntry(VaultEntryType.HEART_RATE, TestFunctions.creatNewDateToCheckFor("2017.06.29-10:30"), 65.0));
        filteredData.add(new VaultEntry(VaultEntryType.HEART_RATE, TestFunctions.creatNewDateToCheckFor("2017.06.29-10:41"), 66.0));
        filteredData.add(new VaultEntry(VaultEntryType.STRESS, TestFunctions.creatNewDateToCheckFor("2017.06.29-10:50"), 52.75));
        filteredData.add(new VaultEntry(VaultEntryType.HEART_RATE_VARIABILITY, TestFunctions.creatNewDateToCheckFor("2017.06.29-10:50"), 50.0, 63.0));
        filteredData.add(new VaultEntry(VaultEntryType.HEART_RATE, TestFunctions.creatNewDateToCheckFor("2017.06.29-10:50"), 85.0));
        filteredData.add(new VaultEntry(VaultEntryType.STRESS, TestFunctions.creatNewDateToCheckFor("2017.06.29-11:00"), 73.0));
        filteredData.add(new VaultEntry(VaultEntryType.HEART_RATE_VARIABILITY, TestFunctions.creatNewDateToCheckFor("2017.06.29-11:00"), 47.0, 36.0));
        filteredData.add(new VaultEntry(VaultEntryType.HEART_RATE, TestFunctions.creatNewDateToCheckFor("2017.06.29-11:00"), 91.0));
        filteredData.add(new VaultEntry(VaultEntryType.BASAL_PROFILE, TestFunctions.creatNewDateToCheckFor("2017.06.29-11:00"), 0.9));
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
        filteredData.add(new VaultEntry(VaultEntryType.HEART_RATE, TestFunctions.creatNewDateToCheckFor("2017.06.29-12:00"), 55.0));
        filteredData.add(new VaultEntry(VaultEntryType.BASAL_PROFILE, TestFunctions.creatNewDateToCheckFor("2017.06.29-12:00"), 0.9));

        List<Pair<Date, Date>> timeSeries = new ArrayList<>();
        timeSeries.add(new Pair<>(TestFunctions.creatNewDateToCheckFor("2017.06.29-08:00"), TestFunctions.creatNewDateToCheckFor("2017.06.29-12:00")));

        FilterResult checkForThisResult = new FilterResult(filteredData, timeSeries);

        assertEquals(result.filteredData, checkForThisResult.filteredData);
        assertEquals(result.timeSeries, checkForThisResult.timeSeries);
        //assertEquals(result, checkForThisResult);
    }

    @Test
    public void testDateTimeSpanFilterTest_0830_1130() throws ParseException {
        TimeSpanFilter instance = setUpFilter(LocalTime.parse("08:30"),
                LocalTime.parse("11:30"));
        FilterResult result = instance.filter(newData);

        List<VaultEntry> filteredData = new ArrayList<>();
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
        filteredData.add(new VaultEntry(VaultEntryType.BASAL_PROFILE, TestFunctions.creatNewDateToCheckFor("2016.04.18-10:00"), 1.1));
        filteredData.add(new VaultEntry(VaultEntryType.BOLUS_NORMAL, TestFunctions.creatNewDateToCheckFor("2016.04.18-10:01"), 2));
        filteredData.add(new VaultEntry(VaultEntryType.GLUCOSE_CGM, TestFunctions.creatNewDateToCheckFor("2016.04.18-10:14"), 138));
        filteredData.add(new VaultEntry(VaultEntryType.GLUCOSE_CGM_ALERT, TestFunctions.creatNewDateToCheckFor("2016.04.18-10:26"), 148));
        filteredData.add(new VaultEntry(VaultEntryType.GLUCOSE_CGM, TestFunctions.creatNewDateToCheckFor("2016.04.18-10:29"), 110));
        filteredData.add(new VaultEntry(VaultEntryType.GLUCOSE_CGM, TestFunctions.creatNewDateToCheckFor("2016.04.18-10:43"), 105));
        filteredData.add(new VaultEntry(VaultEntryType.GLUCOSE_CGM_ALERT, TestFunctions.creatNewDateToCheckFor("2016.04.18-10:52"), 169));
        filteredData.add(new VaultEntry(VaultEntryType.GLUCOSE_CGM, TestFunctions.creatNewDateToCheckFor("2016.04.18-10:59"), 100));
        filteredData.add(new VaultEntry(VaultEntryType.BASAL_PROFILE, TestFunctions.creatNewDateToCheckFor("2016.04.18-11:00"), 1.1));
        filteredData.add(new VaultEntry(VaultEntryType.GLUCOSE_CGM_ALERT, TestFunctions.creatNewDateToCheckFor("2016.04.18-11:01"), 170));
        filteredData.add(new VaultEntry(VaultEntryType.GLUCOSE_CGM_ALERT, TestFunctions.creatNewDateToCheckFor("2016.04.18-10:13"), 182));
        filteredData.add(new VaultEntry(VaultEntryType.GLUCOSE_CGM, TestFunctions.creatNewDateToCheckFor("2016.04.18-11:14"), 120));
        filteredData.add(new VaultEntry(VaultEntryType.GLUCOSE_CGM, TestFunctions.creatNewDateToCheckFor("2016.04.18-11:29"), 103));

        tmpAnnotations = new ArrayList<>();
        tmpAnnotations.add(new VaultEntryAnnotation(VaultEntryAnnotation.TYPE.GLUCOSE_BG_METER_SERIAL).setValue("BG1140084B"));
        filteredData.add(new VaultEntry(VaultEntryType.GLUCOSE_BG, TestFunctions.creatNewDateToCheckFor("2017.06.29-08:39"), 181.0, tmpAnnotations));
        filteredData.add(new VaultEntry(VaultEntryType.GLUCOSE_BOLUS_CALCULATION, TestFunctions.creatNewDateToCheckFor("2017.06.29-08:40"), 181.0));
        filteredData.add(new VaultEntry(VaultEntryType.BOLUS_NORMAL, TestFunctions.creatNewDateToCheckFor("2017.06.29-08:42"), 3.2));
        filteredData.add(new VaultEntry(VaultEntryType.BASAL_PROFILE, TestFunctions.creatNewDateToCheckFor("2017.06.29-09:00"), 1.3));
        filteredData.add(new VaultEntry(VaultEntryType.BASAL_PROFILE, TestFunctions.creatNewDateToCheckFor("2017.06.29-10:00"), 1.1));
        filteredData.add(new VaultEntry(VaultEntryType.HEART_RATE, TestFunctions.creatNewDateToCheckFor("2017.06.29-10:30"), 65.0));
        filteredData.add(new VaultEntry(VaultEntryType.HEART_RATE, TestFunctions.creatNewDateToCheckFor("2017.06.29-10:41"), 66.0));
        filteredData.add(new VaultEntry(VaultEntryType.STRESS, TestFunctions.creatNewDateToCheckFor("2017.06.29-10:50"), 52.75));
        filteredData.add(new VaultEntry(VaultEntryType.HEART_RATE_VARIABILITY, TestFunctions.creatNewDateToCheckFor("2017.06.29-10:50"), 50.0, 63.0));
        filteredData.add(new VaultEntry(VaultEntryType.HEART_RATE, TestFunctions.creatNewDateToCheckFor("2017.06.29-10:50"), 85.0));
        filteredData.add(new VaultEntry(VaultEntryType.STRESS, TestFunctions.creatNewDateToCheckFor("2017.06.29-11:00"), 73.0));
        filteredData.add(new VaultEntry(VaultEntryType.HEART_RATE_VARIABILITY, TestFunctions.creatNewDateToCheckFor("2017.06.29-11:00"), 47.0, 36.0));
        filteredData.add(new VaultEntry(VaultEntryType.HEART_RATE, TestFunctions.creatNewDateToCheckFor("2017.06.29-11:00"), 91.0));
        filteredData.add(new VaultEntry(VaultEntryType.BASAL_PROFILE, TestFunctions.creatNewDateToCheckFor("2017.06.29-11:00"), 0.9));
        filteredData.add(new VaultEntry(VaultEntryType.STRESS, TestFunctions.creatNewDateToCheckFor("2017.06.29-11:10"), 61.0));
        filteredData.add(new VaultEntry(VaultEntryType.HEART_RATE, TestFunctions.creatNewDateToCheckFor("2017.06.29-11:10"), 77.0));
        filteredData.add(new VaultEntry(VaultEntryType.HEART_RATE_VARIABILITY, TestFunctions.creatNewDateToCheckFor("2017.06.29-11:10"), 43.0, 52.0));
        filteredData.add(new VaultEntry(VaultEntryType.HEART_RATE, TestFunctions.creatNewDateToCheckFor("2017.06.29-11:21"), 68.0));
        filteredData.add(new VaultEntry(VaultEntryType.HEART_RATE_VARIABILITY, TestFunctions.creatNewDateToCheckFor("2017.06.29-11:21"), 43.0, 59.0));
        filteredData.add(new VaultEntry(VaultEntryType.STRESS, TestFunctions.creatNewDateToCheckFor("2017.06.29-11:21"), 55.75));
        filteredData.add(new VaultEntry(VaultEntryType.HEART_RATE, TestFunctions.creatNewDateToCheckFor("2017.06.29-11:30"), 51.0));

        List<Pair<Date, Date>> timeSeries = new ArrayList<>();
        timeSeries.add(new Pair<>(TestFunctions.creatNewDateToCheckFor("2016.04.18-08:43"), TestFunctions.creatNewDateToCheckFor("2016.04.18-11:29")));
        timeSeries.add(new Pair<>(TestFunctions.creatNewDateToCheckFor("2017.06.29-08:39"), TestFunctions.creatNewDateToCheckFor("2017.06.29-11:30")));

        FilterResult checkForThisResult = new FilterResult(filteredData, timeSeries);

        assertEquals(result.filteredData, checkForThisResult.filteredData);
        assertEquals(result.timeSeries, checkForThisResult.timeSeries);
        //assertEquals(result, checkForThisResult);
    }
}
