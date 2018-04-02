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

import de.opendiabetes.vault.container.VaultEntry;
import de.opendiabetes.vault.container.VaultEntryAnnotation;
import de.opendiabetes.vault.container.VaultEntryType;
import de.opendiabetes.vault.processing.filter.options.DateTimePointFilterOption;
import de.opendiabetes.vault.testhelper.StaticDataset;
import de.opendiabetes.vault.util.TimestampUtils;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import static java.util.concurrent.TimeUnit.MILLISECONDS;
import static java.util.concurrent.TimeUnit.MINUTES;
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
public class DateTimePointFilterTest extends Assert {

    static List<VaultEntry> data;
    DateTimePointFilter instance;
    List<VaultEntryAnnotation> tmpAnnotations;

    public DateTimePointFilterTest() {
    }

    @BeforeClass
    public static void setUpClass() throws ParseException {
        data = StaticDataset.getStaticDataset();
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

    DateTimePointFilter setUpFilter(Date date, int margin) {
        return setUpFilter(date, margin, margin);
    }

    DateTimePointFilter setUpFilter(Date date, int marginBefore, int marginAfter) {
        return new DateTimePointFilter(new DateTimePointFilterOption(date, marginBefore, marginAfter));
    }

    /**
     * Test of filter method, of class DateTimePointFilter.
     *
     * @author juehv, a.a.aponte
     */
    @Test
    public void testDateTimePointFilter_margin_5() throws ParseException {
        //System.out.println("filter");
        Date dateTimePoint = TestFunctions.creatNewDateToCheckFor("2017.06.29-05:26");
        int marginInMinutes = 5;
        long margin = MILLISECONDS.convert(marginInMinutes, MINUTES);
        instance = setUpFilter(dateTimePoint, marginInMinutes);
        FilterResult result = instance.filter(data);

        for (VaultEntry entry : result.filteredData) {
            assertTrue(TimestampUtils.withinDateTimeSpan(new Date(dateTimePoint.getTime() - margin),
                    new Date(dateTimePoint.getTime() + margin), entry.getTimestamp()));
        }
    }

    @Test
    public void testDateTimePointFilter_margin_10() throws ParseException {
        Date dateTimePoint = TestFunctions.creatNewDateToCheckFor("2017.06.29-12:15");
        int marginInMinutes = 10;
        // 2017.06.29-12:05 - 2017.06.29-12:25
        instance = setUpFilter(dateTimePoint, marginInMinutes);
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
    public void testDateTimePointFilter_margin_240() throws ParseException {
        Date dateTimePoint = TestFunctions.creatNewDateToCheckFor("2017.06.29-07:56");
        int marginInMinutes = 240;
        // 2017.06.29-03:56 - 2017.06.29-11:56
        instance = setUpFilter(dateTimePoint, marginInMinutes);
        FilterResult result = instance.filter(data);

        List<VaultEntry> filteredData = new ArrayList<>();
        filteredData.add(new VaultEntry(VaultEntryType.STRESS, TestFunctions.creatNewDateToCheckFor("2017.06.29-04:46"), 21.5));
        tmpAnnotations = new ArrayList<>();
        tmpAnnotations.add(new VaultEntryAnnotation(VaultEntryAnnotation.TYPE.GLUCOSE_BG_METER_SERIAL).setValue("BG1140084B"));
        filteredData.add(new VaultEntry(VaultEntryType.GLUCOSE_BG, TestFunctions.creatNewDateToCheckFor("2017.06.29-04:53"), 109.0, tmpAnnotations));
        filteredData.add(new VaultEntry(VaultEntryType.STRESS, TestFunctions.creatNewDateToCheckFor("2017.06.29-04:56"), 36.25));
        filteredData.add(new VaultEntry(VaultEntryType.HEART_RATE, TestFunctions.creatNewDateToCheckFor("2017.06.29-04:56"), 72.0));
        filteredData.add(new VaultEntry(VaultEntryType.HEART_RATE_VARIABILITY, TestFunctions.creatNewDateToCheckFor("2017.06.29-04:56"), 50.0, 85.0));
        filteredData.add(new VaultEntry(VaultEntryType.SLEEP_LIGHT, TestFunctions.creatNewDateToCheckFor("2017.06.29-04:58"), 24.0));
        filteredData.add(new VaultEntry(VaultEntryType.BASAL_PROFILE, TestFunctions.creatNewDateToCheckFor("2017.06.29-05:00"), 1.05));
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
        filteredData.add(new VaultEntry(VaultEntryType.BASAL_PROFILE, TestFunctions.creatNewDateToCheckFor("2017.06.29-07:00"), 1.15));
        filteredData.add(new VaultEntry(VaultEntryType.HEART_RATE, TestFunctions.creatNewDateToCheckFor("2017.06.29-07:06"), 56.0));
        filteredData.add(new VaultEntry(VaultEntryType.HEART_RATE, TestFunctions.creatNewDateToCheckFor("2017.06.29-07:16"), 57.0));
        filteredData.add(new VaultEntry(VaultEntryType.SLEEP_LIGHT, TestFunctions.creatNewDateToCheckFor("2017.06.29-07:22"), 1.0));
        filteredData.add(new VaultEntry(VaultEntryType.STRESS, TestFunctions.creatNewDateToCheckFor("2017.06.29-07:27"), 25.75));
        filteredData.add(new VaultEntry(VaultEntryType.HEART_RATE_VARIABILITY, TestFunctions.creatNewDateToCheckFor("2017.06.29-07:27"), 50.0, 99.0));
        filteredData.add(new VaultEntry(VaultEntryType.HEART_RATE, TestFunctions.creatNewDateToCheckFor("2017.06.29-07:27"), 58.0));
        filteredData.add(new VaultEntry(VaultEntryType.HEART_RATE, TestFunctions.creatNewDateToCheckFor("2017.06.29-07:36"), 68.0));
        filteredData.add(new VaultEntry(VaultEntryType.SLEEP_LIGHT, TestFunctions.creatNewDateToCheckFor("2017.06.29-07:37"), 43.0));
        filteredData.add(new VaultEntry(VaultEntryType.STRESS, TestFunctions.creatNewDateToCheckFor("2017.06.29-07:45"), 30.25));
        filteredData.add(new VaultEntry(VaultEntryType.HEART_RATE_VARIABILITY, TestFunctions.creatNewDateToCheckFor("2017.06.29-07:45"), 50.0, 93.0));
        filteredData.add(new VaultEntry(VaultEntryType.HEART_RATE, TestFunctions.creatNewDateToCheckFor("2017.06.29-07:46"), 62.0));
        filteredData.add(new VaultEntry(VaultEntryType.HEART_RATE_VARIABILITY, TestFunctions.creatNewDateToCheckFor("2017.06.29-07:56"), 50.0, 96.0));
        filteredData.add(new VaultEntry(VaultEntryType.STRESS, TestFunctions.creatNewDateToCheckFor("2017.06.29-07:56"), 28.0));
        filteredData.add(new VaultEntry(VaultEntryType.HEART_RATE, TestFunctions.creatNewDateToCheckFor("2017.06.29-07:56"), 63.0));
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

        List<Pair<Date, Date>> timeSeries = new ArrayList<>();
        timeSeries.add(new Pair<>(TestFunctions.creatNewDateToCheckFor("2017.06.29-04:46"), TestFunctions.creatNewDateToCheckFor("2017.06.29-11:51")));

        FilterResult checkForThisResult = new FilterResult(filteredData, timeSeries);

        assertEquals(result.filteredData, checkForThisResult.filteredData);
        assertEquals(result.timeSeries, checkForThisResult.timeSeries);
        //assertEquals(result, checkForThisResult);
    }

    @Test
    public void testDateTimePointFilter_margin_120() throws ParseException {
        Date dateTimePoint = TestFunctions.creatNewDateToCheckFor("2017.06.29-12:40");
        int marginInMinutes = 120;
        // 2017.06.29-10:40 - 2017.06.29-14:40
        instance = setUpFilter(dateTimePoint, marginInMinutes);
        FilterResult result = instance.filter(data);

        List<VaultEntry> filteredData = new ArrayList<>();
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
        filteredData.add(new VaultEntry(VaultEntryType.STRESS, TestFunctions.creatNewDateToCheckFor("2017.06.29-12:11"), 17.25));
        filteredData.add(new VaultEntry(VaultEntryType.HEART_RATE_VARIABILITY, TestFunctions.creatNewDateToCheckFor("2017.06.29-12:11"), 41.0, 131.0));
        filteredData.add(new VaultEntry(VaultEntryType.HEART_RATE, TestFunctions.creatNewDateToCheckFor("2017.06.29-12:21"), 51.0));
        filteredData.add(new VaultEntry(VaultEntryType.HEART_RATE_VARIABILITY, TestFunctions.creatNewDateToCheckFor("2017.06.29-12:21"), 44.0, 127.0));
        filteredData.add(new VaultEntry(VaultEntryType.STRESS, TestFunctions.creatNewDateToCheckFor("2017.06.29-12:21"), 18.25));
        filteredData.add(new VaultEntry(VaultEntryType.HEART_RATE, TestFunctions.creatNewDateToCheckFor("2017.06.29-12:31"), 60.0));
        filteredData.add(new VaultEntry(VaultEntryType.HEART_RATE, TestFunctions.creatNewDateToCheckFor("2017.06.29-12:40"), 68.0));
        filteredData.add(new VaultEntry(VaultEntryType.HEART_RATE_VARIABILITY, TestFunctions.creatNewDateToCheckFor("2017.06.29-12:40"), 43.0, 77.0));

        List<Pair<Date, Date>> timeSeries = new ArrayList<>();
        timeSeries.add(new Pair<>(TestFunctions.creatNewDateToCheckFor("2017.06.29-10:41"), TestFunctions.creatNewDateToCheckFor("2017.06.29-12:40")));

        FilterResult checkForThisResult = new FilterResult(filteredData, timeSeries);

        assertEquals(result.filteredData, checkForThisResult.filteredData);
        assertEquals(result.timeSeries, checkForThisResult.timeSeries);
        //assertEquals(result, checkForThisResult);
    }
}
