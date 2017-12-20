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
 * @author juehv
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
    private void checkForTimestamp(String startDate, String endDate, FilterResult toTest) throws ParseException{
        Date dateBegin = creatNewDateToCheckFor(startDate);
        Date dateEnd = creatNewDateToCheckFor(endDate);

        // New pair with the date's to check for
        List<Pair<Date, Date>> pairToCheckFor = new ArrayList<>();
        pairToCheckFor.add(new Pair<>(dateBegin, dateBegin));
        pairToCheckFor.add(new Pair<>(dateEnd, dateEnd));

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
        String dateTimePointBegin = "2017.06.29-04:46";
        String dateTimePointEnd = "2017.06.29-12:21";
        
        List<VaultEntry> data = StaticDataset.getStaticDataset();
        EventFilter instance = new EventFilter(VaultEntryType.STRESS);
        FilterResult result = instance.filter(data);
        
        //System.out.println(result);
        checkForVaultEntryType(result, VaultEntryType.STRESS);
        checkForTimestamp(dateTimePointBegin, dateTimePointEnd, result);
        assertTrue(result.size() == 23);
    }
    
    @Test
    public void testEventFilter_GLUCOSE_BG() throws ParseException {
        //System.out.println("filter");
        String dateTimePointBegin = "2017.06.29-04:53";
        String dateTimePointEnd = "2017.06.29-08:39";
        
        List<VaultEntry> data = StaticDataset.getStaticDataset();
        EventFilter instance = new EventFilter(VaultEntryType.GLUCOSE_BG);
        FilterResult result = instance.filter(data);
        
        //System.out.println(result);
        checkForVaultEntryType(result, VaultEntryType.GLUCOSE_BG);
        checkForTimestamp(dateTimePointBegin, dateTimePointEnd, result);
        assertTrue(result.size() == 2);
    }
    
    @Test
    public void testEventFilter_HEART_RATE() throws ParseException {
        //System.out.println("filter");
        String dateTimePointBegin = "2017.06.29-04:56";
        String dateTimePointEnd = "2017.06.29-12:40";
        
        List<VaultEntry> data = StaticDataset.getStaticDataset();
        EventFilter instance = new EventFilter(VaultEntryType.HEART_RATE);
        FilterResult result = instance.filter(data);
        
        //System.out.println(result);
        checkForVaultEntryType(result, VaultEntryType.HEART_RATE);
        checkForTimestamp(dateTimePointBegin, dateTimePointEnd, result);
        assertTrue(result.size() == 33);
    }
    
    @Test
    public void testEventFilter_HEART_RATE_VARIABILITY() throws ParseException {
        //System.out.println("filter");
        String dateTimePointBegin = "2017.06.29-04:56";
        String dateTimePointEnd = "2017.06.29-12:40";
        
        List<VaultEntry> data = StaticDataset.getStaticDataset();
        EventFilter instance = new EventFilter(VaultEntryType.HEART_RATE_VARIABILITY);
        FilterResult result = instance.filter(data);
        
        //System.out.println(result);
        checkForVaultEntryType(result, VaultEntryType.HEART_RATE_VARIABILITY);
        checkForTimestamp(dateTimePointBegin, dateTimePointEnd, result);
        assertTrue(result.size() == 22);
    }
    
    @Test
    public void testEventFilter_BASAL_PROFILE() throws ParseException {
        //System.out.println("filter");
        String dateTimePointBegin = "2017.06.29-05:00";
        String dateTimePointEnd = "2017.06.29-12:00";
        
        List<VaultEntry> data = StaticDataset.getStaticDataset();
        EventFilter instance = new EventFilter(VaultEntryType.BASAL_PROFILE);
        FilterResult result = instance.filter(data);
        
        //System.out.println(result);
        checkForVaultEntryType(result, VaultEntryType.BASAL_PROFILE);
        checkForTimestamp(dateTimePointBegin, dateTimePointEnd, result);
        assertTrue(result.size() == 8);
    }
    
    @Test
    public void testEventFilter_GLUCOSE_BOLUS_CALCULATION() throws ParseException {
        //System.out.println("filter");
        String dateTimePointBegin = "2017.06.29-08:40";
        String dateTimePointEnd = "2017.06.29-08:40";
        
        List<VaultEntry> data = StaticDataset.getStaticDataset();
        EventFilter instance = new EventFilter(VaultEntryType.GLUCOSE_BOLUS_CALCULATION);
        FilterResult result = instance.filter(data);
        
        //System.out.println(result);
        checkForVaultEntryType(result, VaultEntryType.GLUCOSE_BOLUS_CALCULATION);
        checkForTimestamp(dateTimePointBegin, dateTimePointEnd, result);
        assertTrue(result.size() == 1);
    }
    
    @Test
    // Empty test
    public void testEventFilter_EMPTY() throws ParseException {
        //System.out.println("filter");
        String dateTimePointBegin = "2010.01.01-10:10";
        String dateTimePointEnd = "2010.01.01-10:10";
        
        List<VaultEntry> data = new ArrayList<VaultEntry>();
        EventFilter instance = new EventFilter(VaultEntryType.GLUCOSE_BG);
        FilterResult result = instance.filter(data);
        
        //System.out.println(result);
        checkForVaultEntryType(result, VaultEntryType.GLUCOSE_BG);
        // no timestamps given
        //checkForTimestamp(dateTimePointBegin, dateTimePointEnd, result);
        assertTrue(result.size() == 0);
        assertTrue(result.filteredData.isEmpty());
        assertTrue(result.timeSeries.isEmpty());
    }
}
