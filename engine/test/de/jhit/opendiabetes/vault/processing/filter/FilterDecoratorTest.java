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
public class FilterDecoratorTest extends Assert {

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
        boolean output = false;
        for (VaultEntry entry : result.filteredData) {
            assertTrue(entry.getType()==typeToCheckFor);
        }
    }
    
    // Checks for the correct start and end timestams
    private void checkForTimestamp(String startDate, String endDate, FilterResult toTest) throws ParseException{
        Date dateBegin = creatNewDateToCheckFor(startDate);
        Date dateEnd = creatNewDateToCheckFor(endDate);
        for (VaultEntry entry : toTest.filteredData) {
            assertTrue(TimestampUtils.withinDateTimeSpan(dateBegin, dateEnd, entry.getTimestamp()));
        }        
    }
    
    // Should not throw any exceptions.
    // Creats a new date
    private Date creatNewDateToCheckFor(String date) throws ParseException{
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy.MM.dd-HH:mm");
        return  sdf.parse(date);
    }
    
    //-----
    //For false test
    //-----
    // Checks for the correct VaultEntryType in all result entries.
    private void checkForVaultEntryType_False(FilterResult result, VaultEntryType typeToCheckFor){
        boolean output = false;
        for (VaultEntry entry : result.filteredData) {
            assertFalse(entry.getType()==typeToCheckFor);
        }
    }
    
    // Checks for the correct start and end timestams
    private void checkForTimestamp_False(String startDate, String endDate, FilterResult toTest) throws ParseException{
        Date dateBegin = creatNewDateToCheckFor(startDate);
        Date dateEnd = creatNewDateToCheckFor(endDate);
        for (VaultEntry entry : toTest.filteredData) {
            assertFalse(TimestampUtils.withinDateTimeSpan(dateBegin, dateEnd, entry.getTimestamp()));
        }        
    }
    //-----
    //For false test END
    //-----
    
    /**
    // Should not throw any exceptions.
    // Creats a new timestamp with the pair of dates that should be tested.
    private List<Pair<Date, Date>> creatRealTimestamp(String start, String end) throws ParseException{
        List<Pair<Date, Date>> realTimestamp = new ArrayList<>();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy.MM.dd-HH:mm");
        Date dateBegin = sdf.parse(start);
        Date dateEnd = sdf.parse(end);
        realTimestamp.add(new Pair<>(dateBegin, dateEnd));
        return realTimestamp;
    }
    */
    
    /**
     * Test functions END
     */
    
    
    
    /**
     * Test of filter method, of class EventFilter.
     */
    @Test
    public void checkFilterMethod() throws ParseException{
        List<Filter> filters = new ArrayList<>();
        filters.add(new EventFilter(VaultEntryType.HEART_RATE));
        filters.add(new EventFilter(VaultEntryType.STRESS));
        Filter filter = new FilterDecorator(filters);
        
        List<VaultEntry> data = StaticDataset.getStaticDataset();
                
        FilterResult filterResult = filter.filter(data);
        System.out.println(filterResult.size());
    }
    
    
    
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
    // False test
    public void testEventFilter_GLUCOSE_BOLUS_CALCULATION_False() throws ParseException {
        //System.out.println("filter");
        String dateTimePointBegin = "2000.01.01-00:00";
        String dateTimePointEnd = "2000.01.01-00:00";
        
        List<VaultEntry> data = StaticDataset.getStaticDataset();
        EventFilter instance = new EventFilter(VaultEntryType.GLUCOSE_BOLUS_CALCULATION);
        FilterResult result = instance.filter(data);
        
        //System.out.println(result);
        checkForVaultEntryType_False(result, VaultEntryType.GLUCOSE_BG);
        checkForTimestamp_False(dateTimePointBegin, dateTimePointEnd, result);
        assertFalse(result.size() != 1);
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
        checkForTimestamp(dateTimePointBegin, dateTimePointEnd, result);
        assertTrue(result.size() == 0);
    }
}
