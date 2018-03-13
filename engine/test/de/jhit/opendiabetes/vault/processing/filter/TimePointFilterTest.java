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
import de.jhit.opendiabetes.vault.container.VaultEntryAnnotation;
import de.jhit.opendiabetes.vault.container.VaultEntryType;
import de.jhit.opendiabetes.vault.exporter.NewDataset;
import de.jhit.opendiabetes.vault.processing.filter.options.TimePointFilterOption;
import de.jhit.opendiabetes.vault.testhelper.StaticDataset;
import de.jhit.opendiabetes.vault.util.TimestampUtils;
import java.text.ParseException;
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
 * @author juehv, a.a.aponte
 */
public class TimePointFilterTest extends Assert {

    static List<VaultEntry> data;
    static List<VaultEntry> newData;
    List<VaultEntryAnnotation> tmpAnnotations;
    TimePointFilter instance;

    public TimePointFilterTest() {
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

    TimePointFilter setUpFilter(LocalTime timepoint, int margin) {
        return setUpFilter(timepoint, margin, margin);
    }

    TimePointFilter setUpFilter(LocalTime timepoint, int marginBefore, int marginAfter) {
        return new TimePointFilter(new TimePointFilterOption(timepoint, marginBefore, marginAfter));
    }

    /**
     * Test of filter method, of class TimePointFilterTest.
     *
     * @author juehv, a.a.aponte
     */
    @Test
    public void testTimePointFilter_1200_15() throws ParseException {
        // System.out.println("filter");
        instance = setUpFilter(LocalTime.parse("12:00"), 15);
        FilterResult result = instance.filter(data);

        for (VaultEntry entry : result.filteredData) {
            assertTrue(TimestampUtils.withinTimeSpan(LocalTime.parse("11:45"),
                    LocalTime.parse("12:15"), entry.getTimestamp()));
        }

        List<VaultEntry> filteredData = new ArrayList<>();
        filteredData.add(new VaultEntry(VaultEntryType.HEART_RATE_VARIABILITY, TestFunctions.creatNewDateToCheckFor("2017.06.29-11:51"), 41.0, 33.0));
        filteredData.add(new VaultEntry(VaultEntryType.STRESS, TestFunctions.creatNewDateToCheckFor("2017.06.29-11:51"), 75.25));
        filteredData.add(new VaultEntry(VaultEntryType.HEART_RATE, TestFunctions.creatNewDateToCheckFor("2017.06.29-11:51"), 58.0));
        filteredData.add(new VaultEntry(VaultEntryType.HEART_RATE, TestFunctions.creatNewDateToCheckFor("2017.06.29-12:00"), 55.0));
        filteredData.add(new VaultEntry(VaultEntryType.BASAL_PROFILE, TestFunctions.creatNewDateToCheckFor("2017.06.29-12:00"), 0.9));
        filteredData.add(new VaultEntry(VaultEntryType.STRESS, TestFunctions.creatNewDateToCheckFor("2017.06.29-12:11"), 17.25));
        filteredData.add(new VaultEntry(VaultEntryType.HEART_RATE_VARIABILITY, TestFunctions.creatNewDateToCheckFor("2017.06.29-12:11"), 41.0, 131.0));

        List<Pair<Date, Date>> timeSeries = new ArrayList<>();
        timeSeries.add(new Pair<>(TestFunctions.creatNewDateToCheckFor("2017.06.29-11:51"), TestFunctions.creatNewDateToCheckFor("2017.06.29-12:11")));

        FilterResult checkForThisResult = new FilterResult(filteredData, timeSeries);

        assertEquals(result.filteredData, checkForThisResult.filteredData);
        assertEquals(result.timeSeries, checkForThisResult.timeSeries);
        //assertEquals(result, checkForThisResult);
    }

    @Test
    public void testTimePointFilter_1215_10() throws ParseException {
        TimePointFilter instance = setUpFilter(LocalTime.parse("12:15"), 10);
        FilterResult result = instance.filter(data);

        List<VaultEntry> filteredData = new ArrayList<>();
        filteredData.add(new VaultEntry(VaultEntryType.STRESS, TestFunctions.creatNewDateToCheckFor("2017.06.29-12:11"), 17.25));
        filteredData.add(new VaultEntry(VaultEntryType.HEART_RATE_VARIABILITY, TestFunctions.creatNewDateToCheckFor("2017.06.29-12:11"), 41.0, 131.0));
        filteredData.add(new VaultEntry(VaultEntryType.HEART_RATE, TestFunctions.creatNewDateToCheckFor("2017.06.29-12:21"), 51.0));
        filteredData.add(new VaultEntry(VaultEntryType.HEART_RATE_VARIABILITY, TestFunctions.creatNewDateToCheckFor("2017.06.29-12:21"), 44.0, 127.0));
        filteredData.add(new VaultEntry(VaultEntryType.STRESS, TestFunctions.creatNewDateToCheckFor("2017.06.29-12:21"), 18.25));

        List<Pair<Date, Date>> timeSeries = new ArrayList<>();
        timeSeries.add(new Pair<>(TestFunctions.creatNewDateToCheckFor("2017.06.29-12:11"), TestFunctions.creatNewDateToCheckFor("2017.06.29-12:21")));

        FilterResult checkForThisResult = new FilterResult(filteredData, timeSeries);

        assertEquals(result.filteredData, checkForThisResult.filteredData);
        assertEquals(result.timeSeries, checkForThisResult.timeSeries);
        //assertEquals(result, checkForThisResult);
    }

    @Test
    public void testDateTimePointFilter_0640_30() throws ParseException {
        TimePointFilter instance = setUpFilter(LocalTime.parse("06:40"), 30);
        FilterResult result = instance.filter(data);

        List<VaultEntry> filteredData = new ArrayList<>();
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
        filteredData.add(new VaultEntry(VaultEntryType.BASAL_PROFILE, TestFunctions.creatNewDateToCheckFor("2017.06.29-07:00"), 1.15));
        filteredData.add(new VaultEntry(VaultEntryType.HEART_RATE, TestFunctions.creatNewDateToCheckFor("2017.06.29-07:06"), 56.0));

        List<Pair<Date, Date>> timeSeries = new ArrayList<>();
        timeSeries.add(new Pair<>(TestFunctions.creatNewDateToCheckFor("2017.06.29-06:13"), TestFunctions.creatNewDateToCheckFor("2017.06.29-07:06")));

        FilterResult checkForThisResult = new FilterResult(filteredData, timeSeries);

        assertEquals(result.filteredData, checkForThisResult.filteredData);
        assertEquals(result.timeSeries, checkForThisResult.timeSeries);
        //assertEquals(result, checkForThisResult);
    }

    @Test
    public void testDateTimePointFilter_0900_60() throws ParseException {
        TimePointFilter instance = setUpFilter(LocalTime.parse("09:00"), 60);
        FilterResult result = instance.filter(newData);

        List<VaultEntry> filteredData = new ArrayList<>();
        filteredData.add(new VaultEntry(VaultEntryType.BASAL_PROFILE, TestFunctions.creatNewDateToCheckFor("2016.04.18-08:00"), 1.25));
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
        filteredData.add(new VaultEntry(VaultEntryType.BASAL_PROFILE, TestFunctions.creatNewDateToCheckFor("2016.04.18-10:00"), 1.1));

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

        List<Pair<Date, Date>> timeSeries = new ArrayList<>();
        timeSeries.add(new Pair<>(TestFunctions.creatNewDateToCheckFor("2016.04.18-08:00"), TestFunctions.creatNewDateToCheckFor("2016.04.18-10:00")));
        timeSeries.add(new Pair<>(TestFunctions.creatNewDateToCheckFor("2017.06.29-08:00"), TestFunctions.creatNewDateToCheckFor("2017.06.29-10:00")));

        FilterResult checkForThisResult = new FilterResult(filteredData, timeSeries);

        assertEquals(result.filteredData, checkForThisResult.filteredData);
        assertEquals(result.timeSeries, checkForThisResult.timeSeries);
        //assertEquals(result, checkForThisResult);
    }
}
