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
package de.jhit.opendiabetes.vault.util;

import de.jhit.opendiabetes.vault.container.VaultEntry;
import de.jhit.opendiabetes.vault.container.VaultEntryType;
import de.jhit.opendiabetes.vault.processing.filter.NoneFilter;
import de.jhit.opendiabetes.vault.testhelper.StaticDataset;
import java.text.ParseException;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javafx.util.Pair;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;

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
    public void testGetNormalizedTimeSeriesEmpty() {
        List<VaultEntry> entries = new ArrayList<>();
        List<Pair<Date, Date>> expectedResult = new ArrayList<>();
        assertEquals(expectedResult, TimestampUtils.getNormalizedTimeSeries(entries, 0));
    }

    @Test
    public void testGetNormalizedTimeSeriesZeroMargin() throws ParseException {
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

}
