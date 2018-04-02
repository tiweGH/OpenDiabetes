/*
 * Copyright (C) 2018 tiweGH
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
package de.opendiabetes.vault.util;

import de.opendiabetes.vault.container.VaultEntry;
import de.opendiabetes.vault.container.VaultEntryType;
import java.text.ParseException;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import javafx.util.Pair;
import org.junit.After;
import org.junit.AfterClass;
import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

/**
 *
 * @author tiweGH
 */
public class TimestampUtilsTest {

    public TimestampUtilsTest() {
    }

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

    @Test
    public void testNormalizeTimeSeriesZeroMargin() throws ParseException {

        List<Pair<Date, Date>> timeSeries = new ArrayList<>();
        List<Pair<Date, Date>> expectedResult = new ArrayList<>();
        timeSeries.add(new Pair(TimestampUtils.createCleanTimestamp("2017.06.29-04:53", "yyyy.MM.dd-HH:mm"),
                TimestampUtils.createCleanTimestamp("2017.06.29-04:56", "yyyy.MM.dd-HH:mm")));
        timeSeries.add(new Pair(TimestampUtils.createCleanTimestamp("2017.06.29-04:56", "yyyy.MM.dd-HH:mm"),
                TimestampUtils.createCleanTimestamp("2017.06.29-04:58", "yyyy.MM.dd-HH:mm")));

        expectedResult.add(new Pair(TimestampUtils.createCleanTimestamp("2017.06.29-04:53", "yyyy.MM.dd-HH:mm"),
                TimestampUtils.createCleanTimestamp("2017.06.29-04:58", "yyyy.MM.dd-HH:mm")));
        //no margin, overlapping of entries
        assertEquals(expectedResult, TimestampUtils.normalizeTimeSeries(timeSeries, 0));

        timeSeries = new ArrayList<>();
        timeSeries.add(new Pair(TimestampUtils.createCleanTimestamp("2017.06.29-04:53", "yyyy.MM.dd-HH:mm"),
                TimestampUtils.createCleanTimestamp("2017.06.29-04:56", "yyyy.MM.dd-HH:mm")));
        timeSeries.add(new Pair(TimestampUtils.createCleanTimestamp("2017.06.29-04:57", "yyyy.MM.dd-HH:mm"),
                TimestampUtils.createCleanTimestamp("2017.06.29-04:58", "yyyy.MM.dd-HH:mm")));
        //no margin, no overlapping of entries
        assertEquals(expectedResult, TimestampUtils.normalizeTimeSeries(timeSeries, 0));

    }

    @Test
    public void testNormalizeTimeSeriesMargin() throws ParseException {
        List<Pair<Date, Date>> timeSeries = new ArrayList<>();
        List<Pair<Date, Date>> expectedResult = new ArrayList<>();
        timeSeries.add(new Pair(TimestampUtils.createCleanTimestamp("2017.06.29-04:53", "yyyy.MM.dd-HH:mm"),
                TimestampUtils.createCleanTimestamp("2017.06.29-04:53", "yyyy.MM.dd-HH:mm")));
        timeSeries.add(new Pair(TimestampUtils.createCleanTimestamp("2017.06.29-04:54", "yyyy.MM.dd-HH:mm"),
                TimestampUtils.createCleanTimestamp("2017.06.29-04:54", "yyyy.MM.dd-HH:mm")));
        expectedResult.add(new Pair(TimestampUtils.createCleanTimestamp("2017.06.29-04:48", "yyyy.MM.dd-HH:mm"),
                TimestampUtils.createCleanTimestamp("2017.06.29-04:59", "yyyy.MM.dd-HH:mm")));
        //margin 5, single entry
        assertEquals(expectedResult, TimestampUtils.normalizeTimeSeries(timeSeries, 5));

        timeSeries = new ArrayList<>();
        expectedResult = new ArrayList<>();
        timeSeries.add(new Pair(TimestampUtils.createCleanTimestamp("2017.06.29-04:53", "yyyy.MM.dd-HH:mm"),
                TimestampUtils.createCleanTimestamp("2017.06.29-04:59", "yyyy.MM.dd-HH:mm")));
        timeSeries.add(new Pair(TimestampUtils.createCleanTimestamp("2017.06.29-04:56", "yyyy.MM.dd-HH:mm"),
                TimestampUtils.createCleanTimestamp("2017.06.29-05:00", "yyyy.MM.dd-HH:mm")));
        timeSeries.add(new Pair(TimestampUtils.createCleanTimestamp("2017.06.29-05:05", "yyyy.MM.dd-HH:mm"),
                TimestampUtils.createCleanTimestamp("2017.06.29-05:05", "yyyy.MM.dd-HH:mm")));
        timeSeries.add(new Pair(TimestampUtils.createCleanTimestamp("2017.06.29-05:16", "yyyy.MM.dd-HH:mm"),
                TimestampUtils.createCleanTimestamp("2017.06.29-05:17", "yyyy.MM.dd-HH:mm")));
        expectedResult.add(new Pair(TimestampUtils.createCleanTimestamp("2017.06.29-04:48", "yyyy.MM.dd-HH:mm"),
                TimestampUtils.createCleanTimestamp("2017.06.29-05:22", "yyyy.MM.dd-HH:mm")));
        //margin 5, two segments
        assertEquals(expectedResult, TimestampUtils.normalizeTimeSeries(timeSeries, 5));

        timeSeries = new ArrayList<>();
        expectedResult = new ArrayList<>();
        timeSeries.add(new Pair(TimestampUtils.createCleanTimestamp("2017.06.29-04:53", "yyyy.MM.dd-HH:mm"),
                TimestampUtils.createCleanTimestamp("2017.06.29-04:59", "yyyy.MM.dd-HH:mm")));
        timeSeries.add(new Pair(TimestampUtils.createCleanTimestamp("2017.06.29-04:56", "yyyy.MM.dd-HH:mm"),
                TimestampUtils.createCleanTimestamp("2017.06.29-05:00", "yyyy.MM.dd-HH:mm")));
        timeSeries.add(new Pair(TimestampUtils.createCleanTimestamp("2017.06.29-05:04", "yyyy.MM.dd-HH:mm"),
                TimestampUtils.createCleanTimestamp("2017.06.29-05:04", "yyyy.MM.dd-HH:mm")));
        timeSeries.add(new Pair(TimestampUtils.createCleanTimestamp("2017.06.29-05:16", "yyyy.MM.dd-HH:mm"),
                TimestampUtils.createCleanTimestamp("2017.06.29-05:17", "yyyy.MM.dd-HH:mm")));
        expectedResult.add(new Pair(TimestampUtils.createCleanTimestamp("2017.06.29-04:48", "yyyy.MM.dd-HH:mm"),
                TimestampUtils.createCleanTimestamp("2017.06.29-05:09", "yyyy.MM.dd-HH:mm")));
        expectedResult.add(new Pair(TimestampUtils.createCleanTimestamp("2017.06.29-05:11", "yyyy.MM.dd-HH:mm"),
                TimestampUtils.createCleanTimestamp("2017.06.29-05:22", "yyyy.MM.dd-HH:mm")));
        //margin 5, two segments, 1 minute between
        assertEquals(expectedResult, TimestampUtils.normalizeTimeSeries(timeSeries, 5));
    }

    @Test
    public void testNormalizeTimeSeriesEmpty() {
        List<Pair<Date, Date>> timeSeries = new ArrayList<>();
        List<Pair<Date, Date>> expectedResult = new ArrayList<>();
        assertEquals(expectedResult, TimestampUtils.normalizeTimeSeries(timeSeries, 0));
    }

    @Test
    public void testGetNormalizedTimeSeries_Empty() {
        List<VaultEntry> entries = new ArrayList<>();
        List<Pair<Date, Date>> expectedResult = new ArrayList<>();
        assertEquals(expectedResult, TimestampUtils.getNormalizedTimeSeries(entries, 0));
    }

    @Test
    public void testGetNormalizedTimeSeries_ZeroMargin() throws ParseException {
        List<VaultEntry> vaultEntries = new ArrayList<>();
        vaultEntries.add(new VaultEntry(VaultEntryType.GLUCOSE_BG, TimestampUtils.createCleanTimestamp("2017.06.29-04:53", "yyyy.MM.dd-HH:mm"), 109.0));
        vaultEntries.add(new VaultEntry(VaultEntryType.STRESS, TimestampUtils.createCleanTimestamp("2017.06.29-04:56", "yyyy.MM.dd-HH:mm"), 36.25));
        vaultEntries.add(new VaultEntry(VaultEntryType.HEART_RATE, TimestampUtils.createCleanTimestamp("2017.06.29-04:56", "yyyy.MM.dd-HH:mm"), 72.0));
        vaultEntries.add(new VaultEntry(VaultEntryType.HEART_RATE_VARIABILITY, TimestampUtils.createCleanTimestamp("2017.06.29-04:56", "yyyy.MM.dd-HH:mm"), 50.0, 85.0));
        vaultEntries.add(new VaultEntry(VaultEntryType.SLEEP_LIGHT, TimestampUtils.createCleanTimestamp("2017.06.29-04:58", "yyyy.MM.dd-HH:mm"), 24.0));
        vaultEntries.add(new VaultEntry(VaultEntryType.BASAL_PROFILE, TimestampUtils.createCleanTimestamp("2017.06.29-04:59", "yyyy.MM.dd-HH:mm"), 1.05));
        vaultEntries.add(new VaultEntry(VaultEntryType.HEART_RATE, TimestampUtils.createCleanTimestamp("2017.06.29-05:06", "yyyy.MM.dd-HH:mm"), 59.0));
        vaultEntries.add(new VaultEntry(VaultEntryType.STRESS, TimestampUtils.createCleanTimestamp("2017.06.29-05:06", "yyyy.MM.dd-HH:mm"), 23.75));
        vaultEntries.add(new VaultEntry(VaultEntryType.HEART_RATE_VARIABILITY, TimestampUtils.createCleanTimestamp("2017.06.29-05:06", "yyyy.MM.dd-HH:mm"), 50.0, 105.0));
        vaultEntries.add(new VaultEntry(VaultEntryType.HEART_RATE, TimestampUtils.createCleanTimestamp("2017.06.29-05:16", "yyyy.MM.dd-HH:mm"), 53.0));
        vaultEntries.add(new VaultEntry(VaultEntryType.HEART_RATE_VARIABILITY, TimestampUtils.createCleanTimestamp("2017.06.29-05:16", "yyyy.MM.dd-HH:mm"), 50.0, 123.0));
        vaultEntries.add(new VaultEntry(VaultEntryType.STRESS, TimestampUtils.createCleanTimestamp("2017.06.29-05:16", "yyyy.MM.dd-HH:mm"), 19.25));
        vaultEntries.add(new VaultEntry(VaultEntryType.SLEEP_LIGHT, TimestampUtils.createCleanTimestamp("2017.06.29-05:22", "yyyy.MM.dd-HH:mm"), 9.0));

        List<Pair<Date, Date>> expectedTimeSeries = new ArrayList<>();
        expectedTimeSeries.add(new Pair(TimestampUtils.createCleanTimestamp("2017.06.29-04:53", "yyyy.MM.dd-HH:mm"),
                TimestampUtils.createCleanTimestamp("2017.06.29-04:53", "yyyy.MM.dd-HH:mm")));
        expectedTimeSeries.add(new Pair(TimestampUtils.createCleanTimestamp("2017.06.29-04:56", "yyyy.MM.dd-HH:mm"),
                TimestampUtils.createCleanTimestamp("2017.06.29-04:56", "yyyy.MM.dd-HH:mm")));
        expectedTimeSeries.add(new Pair(TimestampUtils.createCleanTimestamp("2017.06.29-04:58", "yyyy.MM.dd-HH:mm"),
                TimestampUtils.createCleanTimestamp("2017.06.29-04:59", "yyyy.MM.dd-HH:mm")));
        expectedTimeSeries.add(new Pair(TimestampUtils.createCleanTimestamp("2017.06.29-05:06", "yyyy.MM.dd-HH:mm"),
                TimestampUtils.createCleanTimestamp("2017.06.29-05:06", "yyyy.MM.dd-HH:mm")));
        expectedTimeSeries.add(new Pair(TimestampUtils.createCleanTimestamp("2017.06.29-05:16", "yyyy.MM.dd-HH:mm"),
                TimestampUtils.createCleanTimestamp("2017.06.29-05:16", "yyyy.MM.dd-HH:mm")));
        expectedTimeSeries.add(new Pair(TimestampUtils.createCleanTimestamp("2017.06.29-05:22", "yyyy.MM.dd-HH:mm"),
                TimestampUtils.createCleanTimestamp("2017.06.29-05:22", "yyyy.MM.dd-HH:mm")));

        assertEquals(expectedTimeSeries, TimestampUtils.getNormalizedTimeSeries(vaultEntries, 0));
    }

    @Test
    public void testGetMidDate_DifferentDays() throws ParseException {
        Date date1 = TimestampUtils.createCleanTimestamp("2017.06.28-00:00", "yyyy.MM.dd-HH:mm");
        Date date2 = TimestampUtils.createCleanTimestamp("2017.06.30-00:00", "yyyy.MM.dd-HH:mm");
        Date expectedResult = TimestampUtils.createCleanTimestamp("2017.06.29-00:00", "yyyy.MM.dd-HH:mm");

        assertEquals(expectedResult, TimestampUtils.getMidDate(date1, date2));
    }

    @Test
    public void testGetMidDate_DifferentTimes() throws ParseException {
        Date date1 = TimestampUtils.createCleanTimestamp("2017.06.28-00:00", "yyyy.MM.dd-HH:mm");
        Date date2 = TimestampUtils.createCleanTimestamp("2017.06.28-12:33", "yyyy.MM.dd-HH:mm");
        Date expectedResult = TimestampUtils.createCleanTimestamp("2017.06.28-06:16", "yyyy.MM.dd-HH:mm");

        assertEquals(expectedResult, TimestampUtils.getMidDate(date1, date2));
    }

    @Test
    public void testGetMidDate_SameDate() throws ParseException {
        Date date1 = TimestampUtils.createCleanTimestamp("2017.06.28-00:00", "yyyy.MM.dd-HH:mm");
        Date date2 = TimestampUtils.createCleanTimestamp("2017.06.28-00:00", "yyyy.MM.dd-HH:mm");
        Date expectedResult = TimestampUtils.createCleanTimestamp("2017.06.28-00:00", "yyyy.MM.dd-HH:mm");

        assertEquals(expectedResult, TimestampUtils.getMidDate(date1, date2));
    }

    @Test
    public void testGetMidDate_SwitchArguments() throws ParseException {
        Date date1 = TimestampUtils.createCleanTimestamp("2017.06.28-00:00", "yyyy.MM.dd-HH:mm");
        Date date2 = TimestampUtils.createCleanTimestamp("2017.06.28-12:33", "yyyy.MM.dd-HH:mm");

        assertEquals(TimestampUtils.getMidDate(date2, date1), TimestampUtils.getMidDate(date1, date2));
    }

    @Test
    public void testSetTimeOfDate() throws ParseException {
        LocalTime newTime = LocalTime.of(14, 30, 12);
        Date date = TimestampUtils.createCleanTimestamp("2017.06.28-00:00", "yyyy.MM.dd-HH:mm");
        Date expectedResult = TimestampUtils.createCleanTimestamp("2017.06.28-14:30", "yyyy.MM.dd-HH:mm");

        assertEquals(expectedResult, TimestampUtils.setTimeOfDate(date, newTime));
    }

    @Test
    public void testSetDayOfDate() throws ParseException {
        Date date1 = TimestampUtils.createCleanTimestamp("2000.01.01-11:11", "yyyy.MM.dd-HH:mm");
        Date date2 = TimestampUtils.createCleanTimestamp("2017.06.28-00:00", "yyyy.MM.dd-HH:mm");
        Date expectedResult = TimestampUtils.createCleanTimestamp("2017.06.28-11:11", "yyyy.MM.dd-HH:mm");

        assertEquals(expectedResult, TimestampUtils.setDayOfDate(date1, date2));
    }

    @Test
    public void testWithinTimeSeries() throws ParseException {
        List<Pair<Date, Date>> timeSeries = new ArrayList<>();
        timeSeries.add(new Pair(TimestampUtils.createCleanTimestamp("2017.06.29-04:53", "yyyy.MM.dd-HH:mm"),
                TimestampUtils.createCleanTimestamp("2017.06.29-04:53", "yyyy.MM.dd-HH:mm")));
        timeSeries.add(new Pair(TimestampUtils.createCleanTimestamp("2017.06.29-04:56", "yyyy.MM.dd-HH:mm"),
                TimestampUtils.createCleanTimestamp("2017.06.29-04:56", "yyyy.MM.dd-HH:mm")));
        timeSeries.add(new Pair(TimestampUtils.createCleanTimestamp("2017.06.29-04:58", "yyyy.MM.dd-HH:mm"),
                TimestampUtils.createCleanTimestamp("2017.06.29-05:01", "yyyy.MM.dd-HH:mm")));
        timeSeries.add(new Pair(TimestampUtils.createCleanTimestamp("2017.06.29-05:06", "yyyy.MM.dd-HH:mm"),
                TimestampUtils.createCleanTimestamp("2017.06.29-05:06", "yyyy.MM.dd-HH:mm")));
        timeSeries.add(new Pair(TimestampUtils.createCleanTimestamp("2017.06.29-05:16", "yyyy.MM.dd-HH:mm"),
                TimestampUtils.createCleanTimestamp("2017.06.29-05:16", "yyyy.MM.dd-HH:mm")));
        timeSeries.add(new Pair(TimestampUtils.createCleanTimestamp("2017.06.29-05:22", "yyyy.MM.dd-HH:mm"),
                TimestampUtils.createCleanTimestamp("2017.06.29-05:22", "yyyy.MM.dd-HH:mm")));

        Date date = TimestampUtils.createCleanTimestamp("2000.01.01-11:11", "yyyy.MM.dd-HH:mm");
        assertFalse(TimestampUtils.withinTimeSeries(timeSeries, date));

        date = TimestampUtils.createCleanTimestamp("2017.06.29-05:03", "yyyy.MM.dd-HH:mm");
        assertFalse(TimestampUtils.withinTimeSeries(timeSeries, date));

        date = TimestampUtils.createCleanTimestamp("2017.06.29-05:16", "yyyy.MM.dd-HH:mm");
        assertTrue(TimestampUtils.withinTimeSeries(timeSeries, date));

        date = TimestampUtils.createCleanTimestamp("2017.06.29-05:00", "yyyy.MM.dd-HH:mm");
        assertTrue(TimestampUtils.withinTimeSeries(timeSeries, date));
    }

    @Test
    public void testCreateCleanTimestamp() throws ParseException {
        Date testDate = TimestampUtils.createCleanTimestamp("2018.03.28-04:56", "yyyy.MM.dd-HH:mm");

        Calendar cal = Calendar.getInstance();
        cal.set(2018, 02, 28, 04, 56, 0);
        cal.set(Calendar.MILLISECOND, 0);

        Date expectedResult = cal.getTime();

        assertEquals(expectedResult, testDate);

        testDate = TimestampUtils.createCleanTimestamp("2018.03.28-04:56");

        cal = Calendar.getInstance();
        cal.set(2018, 02, 28, 04, 56, 0);
        cal.set(Calendar.MILLISECOND, 0);

        expectedResult = cal.getTime();

        assertEquals(expectedResult, testDate);

    }

    @Test
    public void testFromLocalDate() {
        LocalDate localDate = LocalDate.of(2018, 3, 28);
        Date testDate = TimestampUtils.fromLocalDate(localDate, 100);

        Calendar cal = Calendar.getInstance();
        cal.set(2018, 02, 28, 0, 0, 0);
        cal.set(Calendar.MILLISECOND, 100);

        Date expectedResult = cal.getTime();

        assertEquals(expectedResult, testDate);

        localDate = LocalDate.of(2018, 3, 28);
        testDate = TimestampUtils.fromLocalDate(localDate);

        cal = Calendar.getInstance();
        cal.set(2018, 02, 28, 0, 0, 0);
        cal.set(Calendar.MILLISECOND, 0);

        expectedResult = cal.getTime();

        assertEquals(expectedResult, testDate);
    }

    @Test
    public void testDateToLocalDate() {
        Calendar cal = Calendar.getInstance();
        cal.set(2018, 02, 28, 4, 50, 0);
        cal.set(Calendar.MILLISECOND, 100);

        LocalDate expectedResult = LocalDate.of(2018, 3, 28);
        LocalDate testDate = TimestampUtils.dateToLocalDate(cal.getTime());

        assertEquals(expectedResult, testDate);
    }

    @Test
    public void testDateToLocalTime() {
        Calendar cal = Calendar.getInstance();
        cal.set(2018, 02, 28, 4, 50, 0);
        cal.set(Calendar.MILLISECOND, 0);

        LocalTime expectedResult = LocalTime.of(4, 50, 0);
        LocalTime testTime = TimestampUtils.dateToLocalTime(cal.getTime());

        assertEquals(expectedResult, testTime);
    }

    @Test
    public void testTimestampToString() {
        Calendar cal = Calendar.getInstance();
        cal.set(2018, 02, 28, 4, 50, 0);
        cal.set(Calendar.MILLISECOND, 0);

        String expectedResult = "2018.03.28-04:50";
        String testString = TimestampUtils.timestampToString(cal.getTime(), TimestampUtils.TIME_FORMAT_DATASETS);

        assertEquals(expectedResult, testString);
    }

    @Test
    public void testGetHourOfDay() {
        Calendar cal = Calendar.getInstance();
        cal.set(2018, 02, 28, 4, 50, 0);
        cal.set(Calendar.MILLISECOND, 0);

        Integer expectedResult = 4;
        Integer testVal = TimestampUtils.getHourOfDay(cal.getTime());

        assertEquals(expectedResult, testVal);
    }

    @Test
    public void testMinuteOfHour() {
        Calendar cal = Calendar.getInstance();
        cal.set(2018, 02, 28, 4, 50, 0);
        cal.set(Calendar.MILLISECOND, 0);

        Integer expectedResult = 50;
        Integer testVal = TimestampUtils.getMinuteOfHour(cal.getTime());

        assertEquals(expectedResult, testVal);
    }

    @Test
    public void testWithinTimeSpan() throws ParseException {
        Date start = TimestampUtils.createCleanTimestamp("2018.03.28-00:00");
        Date end = TimestampUtils.createCleanTimestamp("2018.03.28-12:00");
        Date date1 = TimestampUtils.createCleanTimestamp("2018.03.28-04:50");
        Date date2 = TimestampUtils.createCleanTimestamp("2018.03.28-13:00");

        assertTrue(TimestampUtils.withinTimeSpan(TimestampUtils.dateToLocalTime(start), TimestampUtils.dateToLocalTime(end), date1));
        assertTrue(TimestampUtils.withinTimeSpan(TimestampUtils.dateToLocalTime(start), TimestampUtils.dateToLocalTime(end), TimestampUtils.dateToLocalTime(date1)));

        assertFalse(TimestampUtils.withinTimeSpan(TimestampUtils.dateToLocalTime(start), TimestampUtils.dateToLocalTime(end), date2));
        assertFalse(TimestampUtils.withinTimeSpan(TimestampUtils.dateToLocalTime(start), TimestampUtils.dateToLocalTime(end), TimestampUtils.dateToLocalTime(date2)));
    }

    @Test
    public void testWithinTimeSpan_MultipleDays() throws ParseException {
        Date start = TimestampUtils.createCleanTimestamp("2018.03.28-23:00");
        Date end = TimestampUtils.createCleanTimestamp("2018.03.29-12:00");
        Date date1 = TimestampUtils.createCleanTimestamp("2018.03.29-04:50");
        Date date2 = TimestampUtils.createCleanTimestamp("2018.03.30-13:00");

        assertTrue(TimestampUtils.withinTimeSpan(TimestampUtils.dateToLocalTime(start), TimestampUtils.dateToLocalTime(end), date1));
        assertTrue(TimestampUtils.withinTimeSpan(TimestampUtils.dateToLocalTime(start), TimestampUtils.dateToLocalTime(end), TimestampUtils.dateToLocalTime(date1)));

        assertFalse(TimestampUtils.withinTimeSpan(TimestampUtils.dateToLocalTime(start), TimestampUtils.dateToLocalTime(end), date2));
        assertFalse(TimestampUtils.withinTimeSpan(TimestampUtils.dateToLocalTime(start), TimestampUtils.dateToLocalTime(end), TimestampUtils.dateToLocalTime(date2)));
    }

}
