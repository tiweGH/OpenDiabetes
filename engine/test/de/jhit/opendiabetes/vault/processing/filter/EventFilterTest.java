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
 * @author juehv, aa80hifa
 */
public class EventFilterTest extends Assert {

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
     * Test functions
     */
    
    // Checks for the correct VaultEntryType in all result entries.
    private void checkForVaultEntryType(FilterResult result, VaultEntryType typeToCheckFor){
        for (VaultEntry entry : result.filteredData) {
            assertTrue(entry.getType()==typeToCheckFor);
        }
    }
    
    // Checks for the correct start and end timestams
    private void checkForTimestamp(String startDateStart, String startDateEnd, String endDateStart, String endDateEnd, FilterResult toTest) throws ParseException{
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
    private Date creatNewDateToCheckFor(String date) throws ParseException{
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy.MM.dd-HH:mm");
        return  sdf.parse(date);
    }
    
    /**
     * Test functions END
     */
    
    
    
    /**
     * Test of filter method, of class EventFilter.
     */
    @Test
    public void testEventFilter_STRESS() throws ParseException {
        //System.out.println("filter");
        String dateTimePointBegin_1 = "2017.06.29-04:46";
        String dateTimePointBegin_2 = "2017.06.29-04:46";
        String dateTimePointEnd_1 = "2017.06.29-12:21";
        String dateTimePointEnd_2 = "2017.06.29-12:21";
        
        List<VaultEntry> data = StaticDataset.getStaticDataset();
        EventFilter instance = new EventFilter(VaultEntryType.STRESS);
        FilterResult result = instance.filter(data);
        
        //System.out.println(result);
        checkForVaultEntryType(result, VaultEntryType.STRESS);
        checkForTimestamp(dateTimePointBegin_1, dateTimePointBegin_2, dateTimePointEnd_1, dateTimePointEnd_2, result);
        assertTrue(result.size() == 23);
    }
    
    @Test
    public void testEventFilter_GLUCOSE_BG() throws ParseException {
        //System.out.println("filter");
        String dateTimePointBegin_1 = "2017.06.29-04:53";
        String dateTimePointBegin_2 = "2017.06.29-04:53";
        String dateTimePointEnd_1 = "2017.06.29-08:39";
        String dateTimePointEnd_2 = "2017.06.29-08:39";
        
        List<VaultEntry> data = StaticDataset.getStaticDataset();
        EventFilter instance = new EventFilter(VaultEntryType.GLUCOSE_BG);
        FilterResult result = instance.filter(data);
        
        //System.out.println(result);
        checkForVaultEntryType(result, VaultEntryType.GLUCOSE_BG);
        checkForTimestamp(dateTimePointBegin_1, dateTimePointBegin_2, dateTimePointEnd_1, dateTimePointEnd_2, result);
        assertTrue(result.size() == 2);
    }
    
    @Test
    public void testEventFilter_HEART_RATE() throws ParseException {
        //System.out.println("filter");
        String dateTimePointBegin_1 = "2017.06.29-04:56";
        String dateTimePointBegin_2 = "2017.06.29-04:56";
        String dateTimePointEnd_1 = "2017.06.29-12:31";
        String dateTimePointEnd_2 = "2017.06.29-12:40";
        
        List<VaultEntry> data = StaticDataset.getStaticDataset();
        EventFilter instance = new EventFilter(VaultEntryType.HEART_RATE);
        FilterResult result = instance.filter(data);
        
        //System.out.println(result);
        checkForVaultEntryType(result, VaultEntryType.HEART_RATE);
        checkForTimestamp(dateTimePointBegin_1, dateTimePointBegin_2, dateTimePointEnd_1, dateTimePointEnd_2, result);
        assertTrue(result.size() == 33);
    }
    
    @Test
    public void testEventFilter_HEART_RATE_VARIABILITY() throws ParseException {
        //System.out.println("filter");
        String dateTimePointBegin_1 = "2017.06.29-04:56";
        String dateTimePointBegin_2 = "2017.06.29-04:56";
        String dateTimePointEnd_1 = "2017.06.29-12:40";
        String dateTimePointEnd_2 = "2017.06.29-12:40";
        
        List<VaultEntry> data = StaticDataset.getStaticDataset();
        EventFilter instance = new EventFilter(VaultEntryType.HEART_RATE_VARIABILITY);
        FilterResult result = instance.filter(data);
        
        //System.out.println(result);
        checkForVaultEntryType(result, VaultEntryType.HEART_RATE_VARIABILITY);
        checkForTimestamp(dateTimePointBegin_1, dateTimePointBegin_2, dateTimePointEnd_1, dateTimePointEnd_2, result);
        assertTrue(result.size() == 22);
    }
    
    @Test
    public void testEventFilter_BASAL_PROFILE() throws ParseException {
        //System.out.println("filter");
        String dateTimePointBegin_1 = "2017.06.29-05:00";
        String dateTimePointBegin_2 = "2017.06.29-05:00";
        String dateTimePointEnd_1 = "2017.06.29-12:00";
        String dateTimePointEnd_2 = "2017.06.29-12:00";
        
        List<VaultEntry> data = StaticDataset.getStaticDataset();
        EventFilter instance = new EventFilter(VaultEntryType.BASAL_PROFILE);
        FilterResult result = instance.filter(data);
        
        //System.out.println(result);
        checkForVaultEntryType(result, VaultEntryType.BASAL_PROFILE);
        checkForTimestamp(dateTimePointBegin_1, dateTimePointBegin_2, dateTimePointEnd_1, dateTimePointEnd_2, result);
        assertTrue(result.size() == 8);
    }
    
    @Test
    public void testEventFilter_GLUCOSE_BOLUS_CALCULATION() throws ParseException {
        //System.out.println("filter");
        String dateTimePointBegin_1 = "2017.06.29-08:40";
        String dateTimePointBegin_2 = "2017.06.29-08:40";
        String dateTimePointEnd_1 = "2017.06.29-08:40";
        String dateTimePointEnd_2 = "2017.06.29-08:40";
        
        List<VaultEntry> data = StaticDataset.getStaticDataset();
        EventFilter instance = new EventFilter(VaultEntryType.GLUCOSE_BOLUS_CALCULATION);
        FilterResult result = instance.filter(data);
        
        //System.out.println(result);
        checkForVaultEntryType(result, VaultEntryType.GLUCOSE_BOLUS_CALCULATION);
        checkForTimestamp(dateTimePointBegin_1, dateTimePointBegin_2, dateTimePointEnd_1, dateTimePointEnd_2, result);
        assertTrue(result.size() == 1);
    }
    
    @Test
    // Empty test
    public void testEventFilter_EMPTY() throws ParseException {
        //System.out.println("filter");
        String dateTimePointBegin_1 = "2010.01.01-10:10";
        String dateTimePointBegin_2 = "2010.01.01-10:10";
        String dateTimePointEnd_1 = "2010.01.01-10:10";
        String dateTimePointEnd_2 = "2010.01.01-10:10";
        
        List<VaultEntry> data = new ArrayList<VaultEntry>();
        EventFilter instance = new EventFilter(VaultEntryType.GLUCOSE_BG);
        FilterResult result = instance.filter(data);
        
        //System.out.println(result);
        checkForVaultEntryType(result, VaultEntryType.GLUCOSE_BG);
        // no timestamps given
        //checkForTimestamp(dateTimePointBegin_1, dateTimePointBegin_2, dateTimePointEnd_1, dateTimePointEnd_2, result);
        assertTrue(result.size() == 0);
        assertTrue(result.filteredData.isEmpty());
        assertTrue(result.timeSeries.isEmpty());
    }
    
    
    @Test
    public void testEventFilter_HEART_RATE_complete() throws ParseException {
        
        List<VaultEntry> filteredData = new ArrayList<>();
        filteredData.add(new VaultEntry(VaultEntryType.HEART_RATE, TimestampUtils.createCleanTimestamp("2017.06.29-04:56", "yyyy.MM.dd-HH:mm"), 72.0));
        filteredData.add(new VaultEntry(VaultEntryType.HEART_RATE, TimestampUtils.createCleanTimestamp("2017.06.29-05:06", "yyyy.MM.dd-HH:mm"), 59.0));
        filteredData.add(new VaultEntry(VaultEntryType.HEART_RATE, TimestampUtils.createCleanTimestamp("2017.06.29-05:16", "yyyy.MM.dd-HH:mm"), 53.0));
        filteredData.add(new VaultEntry(VaultEntryType.HEART_RATE, TimestampUtils.createCleanTimestamp("2017.06.29-05:26", "yyyy.MM.dd-HH:mm"), 58.0));
        filteredData.add(new VaultEntry(VaultEntryType.HEART_RATE, TimestampUtils.createCleanTimestamp("2017.06.29-05:36", "yyyy.MM.dd-HH:mm"), 59.0));
        filteredData.add(new VaultEntry(VaultEntryType.HEART_RATE, TimestampUtils.createCleanTimestamp("2017.06.29-05:46", "yyyy.MM.dd-HH:mm"), 57.0));
        filteredData.add(new VaultEntry(VaultEntryType.HEART_RATE, TimestampUtils.createCleanTimestamp("2017.06.29-05:56", "yyyy.MM.dd-HH:mm"), 56.0));
        filteredData.add(new VaultEntry(VaultEntryType.HEART_RATE, TimestampUtils.createCleanTimestamp("2017.06.29-06:06", "yyyy.MM.dd-HH:mm"), 71.0));
        filteredData.add(new VaultEntry(VaultEntryType.HEART_RATE, TimestampUtils.createCleanTimestamp("2017.06.29-06:16", "yyyy.MM.dd-HH:mm"), 66.0));
        filteredData.add(new VaultEntry(VaultEntryType.HEART_RATE, TimestampUtils.createCleanTimestamp("2017.06.29-06:26", "yyyy.MM.dd-HH:mm"), 62.0));
        filteredData.add(new VaultEntry(VaultEntryType.HEART_RATE, TimestampUtils.createCleanTimestamp("2017.06.29-06:36", "yyyy.MM.dd-HH:mm"), 63.0));
        filteredData.add(new VaultEntry(VaultEntryType.HEART_RATE, TimestampUtils.createCleanTimestamp("2017.06.29-06:46", "yyyy.MM.dd-HH:mm"), 58.0));
        filteredData.add(new VaultEntry(VaultEntryType.HEART_RATE, TimestampUtils.createCleanTimestamp("2017.06.29-06:56", "yyyy.MM.dd-HH:mm"), 59.0));
        filteredData.add(new VaultEntry(VaultEntryType.HEART_RATE, TimestampUtils.createCleanTimestamp("2017.06.29-07:06", "yyyy.MM.dd-HH:mm"), 56.0));
        filteredData.add(new VaultEntry(VaultEntryType.HEART_RATE, TimestampUtils.createCleanTimestamp("2017.06.29-07:16", "yyyy.MM.dd-HH:mm"), 57.0));
        filteredData.add(new VaultEntry(VaultEntryType.HEART_RATE, TimestampUtils.createCleanTimestamp("2017.06.29-07:27", "yyyy.MM.dd-HH:mm"), 58.0));
        filteredData.add(new VaultEntry(VaultEntryType.HEART_RATE, TimestampUtils.createCleanTimestamp("2017.06.29-07:36", "yyyy.MM.dd-HH:mm"), 68.0));
        filteredData.add(new VaultEntry(VaultEntryType.HEART_RATE, TimestampUtils.createCleanTimestamp("2017.06.29-07:46", "yyyy.MM.dd-HH:mm"), 62.0));
        filteredData.add(new VaultEntry(VaultEntryType.HEART_RATE, TimestampUtils.createCleanTimestamp("2017.06.29-07:56", "yyyy.MM.dd-HH:mm"), 63.0));
        filteredData.add(new VaultEntry(VaultEntryType.HEART_RATE, TimestampUtils.createCleanTimestamp("2017.06.29-08:06", "yyyy.MM.dd-HH:mm"), 62.0));
        filteredData.add(new VaultEntry(VaultEntryType.HEART_RATE, TimestampUtils.createCleanTimestamp("2017.06.29-10:30", "yyyy.MM.dd-HH:mm"), 65.0));
        filteredData.add(new VaultEntry(VaultEntryType.HEART_RATE, TimestampUtils.createCleanTimestamp("2017.06.29-10:41", "yyyy.MM.dd-HH:mm"), 66.0));
        filteredData.add(new VaultEntry(VaultEntryType.HEART_RATE, TimestampUtils.createCleanTimestamp("2017.06.29-10:50", "yyyy.MM.dd-HH:mm"), 85.0));
        filteredData.add(new VaultEntry(VaultEntryType.HEART_RATE, TimestampUtils.createCleanTimestamp("2017.06.29-11:00", "yyyy.MM.dd-HH:mm"), 91.0));
        filteredData.add(new VaultEntry(VaultEntryType.HEART_RATE, TimestampUtils.createCleanTimestamp("2017.06.29-11:10", "yyyy.MM.dd-HH:mm"), 77.0));
        filteredData.add(new VaultEntry(VaultEntryType.HEART_RATE, TimestampUtils.createCleanTimestamp("2017.06.29-11:21", "yyyy.MM.dd-HH:mm"), 68.0));
        filteredData.add(new VaultEntry(VaultEntryType.HEART_RATE, TimestampUtils.createCleanTimestamp("2017.06.29-11:30", "yyyy.MM.dd-HH:mm"), 51.0));
        filteredData.add(new VaultEntry(VaultEntryType.HEART_RATE, TimestampUtils.createCleanTimestamp("2017.06.29-11:40", "yyyy.MM.dd-HH:mm"), 94.0));
        filteredData.add(new VaultEntry(VaultEntryType.HEART_RATE, TimestampUtils.createCleanTimestamp("2017.06.29-11:51", "yyyy.MM.dd-HH:mm"), 58.0));
        filteredData.add(new VaultEntry(VaultEntryType.HEART_RATE, TimestampUtils.createCleanTimestamp("2017.06.29-12:00", "yyyy.MM.dd-HH:mm"), 55.0));
        filteredData.add(new VaultEntry(VaultEntryType.HEART_RATE, TimestampUtils.createCleanTimestamp("2017.06.29-12:21", "yyyy.MM.dd-HH:mm"), 51.0));
        filteredData.add(new VaultEntry(VaultEntryType.HEART_RATE, TimestampUtils.createCleanTimestamp("2017.06.29-12:31", "yyyy.MM.dd-HH:mm"), 60.0));
        filteredData.add(new VaultEntry(VaultEntryType.HEART_RATE, TimestampUtils.createCleanTimestamp("2017.06.29-12:40", "yyyy.MM.dd-HH:mm"), 68.0));

        List<Pair<Date, Date>> timeSeries = new ArrayList<>();
        timeSeries.add(new Pair<>(creatNewDateToCheckFor("2017.06.29-04:56"), creatNewDateToCheckFor("2017.06.29-04:56")));
        timeSeries.add(new Pair<>(creatNewDateToCheckFor("2017.06.29-05:06"), creatNewDateToCheckFor("2017.06.29-05:06")));
        timeSeries.add(new Pair<>(creatNewDateToCheckFor("2017.06.29-05:16"), creatNewDateToCheckFor("2017.06.29-05:16")));
        timeSeries.add(new Pair<>(creatNewDateToCheckFor("2017.06.29-05:26"), creatNewDateToCheckFor("2017.06.29-05:26")));
        timeSeries.add(new Pair<>(creatNewDateToCheckFor("2017.06.29-05:36"), creatNewDateToCheckFor("2017.06.29-05:36")));
        timeSeries.add(new Pair<>(creatNewDateToCheckFor("2017.06.29-05:46"), creatNewDateToCheckFor("2017.06.29-05:56")));
        timeSeries.add(new Pair<>(creatNewDateToCheckFor("2017.06.29-06:06"), creatNewDateToCheckFor("2017.06.29-06:06")));
        timeSeries.add(new Pair<>(creatNewDateToCheckFor("2017.06.29-06:16"), creatNewDateToCheckFor("2017.06.29-06:16")));
        timeSeries.add(new Pair<>(creatNewDateToCheckFor("2017.06.29-06:26"), creatNewDateToCheckFor("2017.06.29-06:26")));
        timeSeries.add(new Pair<>(creatNewDateToCheckFor("2017.06.29-06:36"), creatNewDateToCheckFor("2017.06.29-06:36")));
        timeSeries.add(new Pair<>(creatNewDateToCheckFor("2017.06.29-06:46"), creatNewDateToCheckFor("2017.06.29-06:46")));
        timeSeries.add(new Pair<>(creatNewDateToCheckFor("2017.06.29-06:56"), creatNewDateToCheckFor("2017.06.29-06:56")));
        timeSeries.add(new Pair<>(creatNewDateToCheckFor("2017.06.29-07:06"), creatNewDateToCheckFor("2017.06.29-07:16")));
        timeSeries.add(new Pair<>(creatNewDateToCheckFor("2017.06.29-07:27"), creatNewDateToCheckFor("2017.06.29-07:36")));
        timeSeries.add(new Pair<>(creatNewDateToCheckFor("2017.06.29-07:46"), creatNewDateToCheckFor("2017.06.29-07:46")));
        timeSeries.add(new Pair<>(creatNewDateToCheckFor("2017.06.29-07:56"), creatNewDateToCheckFor("2017.06.29-07:56")));
        timeSeries.add(new Pair<>(creatNewDateToCheckFor("2017.06.29-08:06"), creatNewDateToCheckFor("2017.06.29-08:06")));
        timeSeries.add(new Pair<>(creatNewDateToCheckFor("2017.06.29-10:30"), creatNewDateToCheckFor("2017.06.29-10:41")));
        timeSeries.add(new Pair<>(creatNewDateToCheckFor("2017.06.29-10:50"), creatNewDateToCheckFor("2017.06.29-10:50")));
        timeSeries.add(new Pair<>(creatNewDateToCheckFor("2017.06.29-11:00"), creatNewDateToCheckFor("2017.06.29-11:00")));
        timeSeries.add(new Pair<>(creatNewDateToCheckFor("2017.06.29-11:10"), creatNewDateToCheckFor("2017.06.29-11:10")));
        timeSeries.add(new Pair<>(creatNewDateToCheckFor("2017.06.29-11:21"), creatNewDateToCheckFor("2017.06.29-11:21")));
        timeSeries.add(new Pair<>(creatNewDateToCheckFor("2017.06.29-11:30"), creatNewDateToCheckFor("2017.06.29-11:30")));
        timeSeries.add(new Pair<>(creatNewDateToCheckFor("2017.06.29-11:40"), creatNewDateToCheckFor("2017.06.29-11:40")));
        timeSeries.add(new Pair<>(creatNewDateToCheckFor("2017.06.29-11:51"), creatNewDateToCheckFor("2017.06.29-12:00")));
        timeSeries.add(new Pair<>(creatNewDateToCheckFor("2017.06.29-12:21"), creatNewDateToCheckFor("2017.06.29-12:21")));
        timeSeries.add(new Pair<>(creatNewDateToCheckFor("2017.06.29-12:31"), creatNewDateToCheckFor("2017.06.29-12:40")));

        FilterResult checkForThisResult = new FilterResult(filteredData, timeSeries);
        
        List<VaultEntry> data = StaticDataset.getStaticDataset();
        EventFilter instance = new EventFilter(VaultEntryType.HEART_RATE);
        FilterResult result = instance.filter(data);
        
        assertEquals(result.filteredData, checkForThisResult.filteredData);
        assertEquals(result.timeSeries, checkForThisResult.timeSeries);
        //assertEquals(result, checkForThisResult);
    }
}
