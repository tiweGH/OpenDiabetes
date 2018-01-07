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
 * @author aa80hifa
 */
public class OrFilterTest extends Assert {

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
     * @author daniel, aa80hifa
     */
    @Test
    public void checkFilterMethod() throws ParseException{
        List<Filter> filters = new ArrayList<>();
        filters.add(new EventFilter(VaultEntryType.HEART_RATE));
        filters.add(new EventFilter(VaultEntryType.STRESS));
        Filter filter = new OrFilter(filters);
        
        data = StaticDataset.getStaticDataset();                
        FilterResult result = filter.filter(data);
        
        List<VaultEntry> filteredData = new ArrayList<>();
        // 22
        filteredData.add(new VaultEntry(VaultEntryType.STRESS, TestFunctions.creatNewDateToCheckFor("2017.06.29-04:46"), 21.5));        
        filteredData.add(new VaultEntry(VaultEntryType.STRESS, TestFunctions.creatNewDateToCheckFor("2017.06.29-04:56"), 36.25));        
        filteredData.add(new VaultEntry(VaultEntryType.STRESS, TestFunctions.creatNewDateToCheckFor("2017.06.29-05:06"), 23.75));        
        filteredData.add(new VaultEntry(VaultEntryType.STRESS, TestFunctions.creatNewDateToCheckFor("2017.06.29-05:16"), 19.25));        
        filteredData.add(new VaultEntry(VaultEntryType.STRESS, TestFunctions.creatNewDateToCheckFor("2017.06.29-05:26"), 21.75));        
        filteredData.add(new VaultEntry(VaultEntryType.STRESS, TestFunctions.creatNewDateToCheckFor("2017.06.29-06:06"), 35.5));        
        filteredData.add(new VaultEntry(VaultEntryType.STRESS, TestFunctions.creatNewDateToCheckFor("2017.06.29-06:16"), 30.25));        
        filteredData.add(new VaultEntry(VaultEntryType.STRESS, TestFunctions.creatNewDateToCheckFor("2017.06.29-06:26"), 27.25));
        filteredData.add(new VaultEntry(VaultEntryType.STRESS, TestFunctions.creatNewDateToCheckFor("2017.06.29-06:36"), 26.5));        
        filteredData.add(new VaultEntry(VaultEntryType.STRESS, TestFunctions.creatNewDateToCheckFor("2017.06.29-06:46"), 27.25));        
        filteredData.add(new VaultEntry(VaultEntryType.STRESS, TestFunctions.creatNewDateToCheckFor("2017.06.29-07:27"), 25.75));        
        filteredData.add(new VaultEntry(VaultEntryType.STRESS, TestFunctions.creatNewDateToCheckFor("2017.06.29-07:45"), 30.25));        
        filteredData.add(new VaultEntry(VaultEntryType.STRESS, TestFunctions.creatNewDateToCheckFor("2017.06.29-07:56"), 28.0));
        filteredData.add(new VaultEntry(VaultEntryType.STRESS, TestFunctions.creatNewDateToCheckFor("2017.06.29-08:06"), 27.25));
        filteredData.add(new VaultEntry(VaultEntryType.STRESS, TestFunctions.creatNewDateToCheckFor("2017.06.29-08:16"), 0.0));
        filteredData.add(new VaultEntry(VaultEntryType.STRESS, TestFunctions.creatNewDateToCheckFor("2017.06.29-10:50"), 52.75));
        filteredData.add(new VaultEntry(VaultEntryType.STRESS, TestFunctions.creatNewDateToCheckFor("2017.06.29-11:00"), 73.0));
        filteredData.add(new VaultEntry(VaultEntryType.STRESS, TestFunctions.creatNewDateToCheckFor("2017.06.29-11:10"), 61.0));        
        filteredData.add(new VaultEntry(VaultEntryType.STRESS, TestFunctions.creatNewDateToCheckFor("2017.06.29-11:21"), 55.75));
        filteredData.add(new VaultEntry(VaultEntryType.STRESS, TestFunctions.creatNewDateToCheckFor("2017.06.29-11:40"), 9.5));        
        filteredData.add(new VaultEntry(VaultEntryType.STRESS, TestFunctions.creatNewDateToCheckFor("2017.06.29-11:51"), 75.25));        
        filteredData.add(new VaultEntry(VaultEntryType.STRESS, TestFunctions.creatNewDateToCheckFor("2017.06.29-12:11"), 17.25));        
        filteredData.add(new VaultEntry(VaultEntryType.STRESS, TestFunctions.creatNewDateToCheckFor("2017.06.29-12:21"), 18.25));
        
        // 33 - 6 == 27
        filteredData.add(new VaultEntry(VaultEntryType.HEART_RATE, TestFunctions.creatNewDateToCheckFor("2017.06.29-04:56"), 72.0));
        filteredData.add(new VaultEntry(VaultEntryType.HEART_RATE, TestFunctions.creatNewDateToCheckFor("2017.06.29-05:06"), 59.0));        
        filteredData.add(new VaultEntry(VaultEntryType.HEART_RATE, TestFunctions.creatNewDateToCheckFor("2017.06.29-05:16"), 53.0));
        filteredData.add(new VaultEntry(VaultEntryType.HEART_RATE, TestFunctions.creatNewDateToCheckFor("2017.06.29-05:26"), 58.0));        
        filteredData.add(new VaultEntry(VaultEntryType.HEART_RATE, TestFunctions.creatNewDateToCheckFor("2017.06.29-05:36"), 59.0));
        //
        filteredData.add(new VaultEntry(VaultEntryType.HEART_RATE, TestFunctions.creatNewDateToCheckFor("2017.06.29-05:46"), 57.0));
        filteredData.add(new VaultEntry(VaultEntryType.HEART_RATE, TestFunctions.creatNewDateToCheckFor("2017.06.29-05:56"), 56.0));
        //
        filteredData.add(new VaultEntry(VaultEntryType.HEART_RATE, TestFunctions.creatNewDateToCheckFor("2017.06.29-06:06"), 71.0));        
        filteredData.add(new VaultEntry(VaultEntryType.HEART_RATE, TestFunctions.creatNewDateToCheckFor("2017.06.29-06:16"), 66.0));        
        filteredData.add(new VaultEntry(VaultEntryType.HEART_RATE, TestFunctions.creatNewDateToCheckFor("2017.06.29-06:26"), 62.0));        
        filteredData.add(new VaultEntry(VaultEntryType.HEART_RATE, TestFunctions.creatNewDateToCheckFor("2017.06.29-06:36"), 63.0));        
        filteredData.add(new VaultEntry(VaultEntryType.HEART_RATE, TestFunctions.creatNewDateToCheckFor("2017.06.29-06:46"), 58.0));        
        filteredData.add(new VaultEntry(VaultEntryType.HEART_RATE, TestFunctions.creatNewDateToCheckFor("2017.06.29-06:56"), 59.0));
        //
        filteredData.add(new VaultEntry(VaultEntryType.HEART_RATE, TestFunctions.creatNewDateToCheckFor("2017.06.29-07:06"), 56.0));
        filteredData.add(new VaultEntry(VaultEntryType.HEART_RATE, TestFunctions.creatNewDateToCheckFor("2017.06.29-07:16"), 57.0));
        //
        filteredData.add(new VaultEntry(VaultEntryType.HEART_RATE, TestFunctions.creatNewDateToCheckFor("2017.06.29-07:27"), 58.0));
        filteredData.add(new VaultEntry(VaultEntryType.HEART_RATE, TestFunctions.creatNewDateToCheckFor("2017.06.29-07:36"), 68.0));
        //
        filteredData.add(new VaultEntry(VaultEntryType.HEART_RATE, TestFunctions.creatNewDateToCheckFor("2017.06.29-07:46"), 62.0));
        filteredData.add(new VaultEntry(VaultEntryType.HEART_RATE, TestFunctions.creatNewDateToCheckFor("2017.06.29-07:56"), 63.0));        
        filteredData.add(new VaultEntry(VaultEntryType.HEART_RATE, TestFunctions.creatNewDateToCheckFor("2017.06.29-08:06"), 62.0));
        //
        filteredData.add(new VaultEntry(VaultEntryType.HEART_RATE, TestFunctions.creatNewDateToCheckFor("2017.06.29-10:30"), 65.0));
        filteredData.add(new VaultEntry(VaultEntryType.HEART_RATE, TestFunctions.creatNewDateToCheckFor("2017.06.29-10:41"), 66.0));
        //
        filteredData.add(new VaultEntry(VaultEntryType.HEART_RATE, TestFunctions.creatNewDateToCheckFor("2017.06.29-10:50"), 85.0));        
        filteredData.add(new VaultEntry(VaultEntryType.HEART_RATE, TestFunctions.creatNewDateToCheckFor("2017.06.29-11:00"), 91.0));
        filteredData.add(new VaultEntry(VaultEntryType.HEART_RATE, TestFunctions.creatNewDateToCheckFor("2017.06.29-11:10"), 77.0));        
        filteredData.add(new VaultEntry(VaultEntryType.HEART_RATE, TestFunctions.creatNewDateToCheckFor("2017.06.29-11:21"), 68.0));
        filteredData.add(new VaultEntry(VaultEntryType.HEART_RATE, TestFunctions.creatNewDateToCheckFor("2017.06.29-11:30"), 51.0));        
        filteredData.add(new VaultEntry(VaultEntryType.HEART_RATE, TestFunctions.creatNewDateToCheckFor("2017.06.29-11:40"), 94.0));
        //
        filteredData.add(new VaultEntry(VaultEntryType.HEART_RATE, TestFunctions.creatNewDateToCheckFor("2017.06.29-11:51"), 58.0));
        filteredData.add(new VaultEntry(VaultEntryType.HEART_RATE, TestFunctions.creatNewDateToCheckFor("2017.06.29-12:00"), 55.0));
        //
        filteredData.add(new VaultEntry(VaultEntryType.HEART_RATE, TestFunctions.creatNewDateToCheckFor("2017.06.29-12:21"), 51.0));
        //
        filteredData.add(new VaultEntry(VaultEntryType.HEART_RATE, TestFunctions.creatNewDateToCheckFor("2017.06.29-12:31"), 60.0));
        filteredData.add(new VaultEntry(VaultEntryType.HEART_RATE, TestFunctions.creatNewDateToCheckFor("2017.06.29-12:40"), 68.0));
        
        
        List<Pair<Date, Date>> timeSeries = new ArrayList<>();
        // 22 + 27 == 49
        timeSeries.add(new Pair<>(TestFunctions.creatNewDateToCheckFor("2017.06.29-04:46"), TestFunctions.creatNewDateToCheckFor("2017.06.29-04:46")));
        timeSeries.add(new Pair<>(TestFunctions.creatNewDateToCheckFor("2017.06.29-04:56"), TestFunctions.creatNewDateToCheckFor("2017.06.29-04:56")));
        timeSeries.add(new Pair<>(TestFunctions.creatNewDateToCheckFor("2017.06.29-05:06"), TestFunctions.creatNewDateToCheckFor("2017.06.29-05:06")));
        timeSeries.add(new Pair<>(TestFunctions.creatNewDateToCheckFor("2017.06.29-05:16"), TestFunctions.creatNewDateToCheckFor("2017.06.29-05:16")));
        timeSeries.add(new Pair<>(TestFunctions.creatNewDateToCheckFor("2017.06.29-05:26"), TestFunctions.creatNewDateToCheckFor("2017.06.29-05:26")));
        timeSeries.add(new Pair<>(TestFunctions.creatNewDateToCheckFor("2017.06.29-06:06"), TestFunctions.creatNewDateToCheckFor("2017.06.29-06:06")));
        timeSeries.add(new Pair<>(TestFunctions.creatNewDateToCheckFor("2017.06.29-06:16"), TestFunctions.creatNewDateToCheckFor("2017.06.29-06:16")));
        timeSeries.add(new Pair<>(TestFunctions.creatNewDateToCheckFor("2017.06.29-06:26"), TestFunctions.creatNewDateToCheckFor("2017.06.29-06:26")));
        timeSeries.add(new Pair<>(TestFunctions.creatNewDateToCheckFor("2017.06.29-06:36"), TestFunctions.creatNewDateToCheckFor("2017.06.29-06:36")));
        timeSeries.add(new Pair<>(TestFunctions.creatNewDateToCheckFor("2017.06.29-06:46"), TestFunctions.creatNewDateToCheckFor("2017.06.29-06:46")));
        timeSeries.add(new Pair<>(TestFunctions.creatNewDateToCheckFor("2017.06.29-07:27"), TestFunctions.creatNewDateToCheckFor("2017.06.29-07:27")));
        timeSeries.add(new Pair<>(TestFunctions.creatNewDateToCheckFor("2017.06.29-07:45"), TestFunctions.creatNewDateToCheckFor("2017.06.29-07:45")));
        timeSeries.add(new Pair<>(TestFunctions.creatNewDateToCheckFor("2017.06.29-07:56"), TestFunctions.creatNewDateToCheckFor("2017.06.29-07:56")));
        timeSeries.add(new Pair<>(TestFunctions.creatNewDateToCheckFor("2017.06.29-08:06"), TestFunctions.creatNewDateToCheckFor("2017.06.29-08:06")));
        timeSeries.add(new Pair<>(TestFunctions.creatNewDateToCheckFor("2017.06.29-08:16"), TestFunctions.creatNewDateToCheckFor("2017.06.29-08:16")));
        timeSeries.add(new Pair<>(TestFunctions.creatNewDateToCheckFor("2017.06.29-10:50"), TestFunctions.creatNewDateToCheckFor("2017.06.29-10:50")));
        timeSeries.add(new Pair<>(TestFunctions.creatNewDateToCheckFor("2017.06.29-11:00"), TestFunctions.creatNewDateToCheckFor("2017.06.29-11:00")));
        timeSeries.add(new Pair<>(TestFunctions.creatNewDateToCheckFor("2017.06.29-11:10"), TestFunctions.creatNewDateToCheckFor("2017.06.29-11:10")));
        timeSeries.add(new Pair<>(TestFunctions.creatNewDateToCheckFor("2017.06.29-11:21"), TestFunctions.creatNewDateToCheckFor("2017.06.29-11:21")));
        timeSeries.add(new Pair<>(TestFunctions.creatNewDateToCheckFor("2017.06.29-11:40"), TestFunctions.creatNewDateToCheckFor("2017.06.29-11:40")));
        timeSeries.add(new Pair<>(TestFunctions.creatNewDateToCheckFor("2017.06.29-11:51"), TestFunctions.creatNewDateToCheckFor("2017.06.29-11:51")));
        timeSeries.add(new Pair<>(TestFunctions.creatNewDateToCheckFor("2017.06.29-12:11"), TestFunctions.creatNewDateToCheckFor("2017.06.29-12:11")));
        timeSeries.add(new Pair<>(TestFunctions.creatNewDateToCheckFor("2017.06.29-12:21"), TestFunctions.creatNewDateToCheckFor("2017.06.29-12:21")));
        
        timeSeries.add(new Pair<>(TestFunctions.creatNewDateToCheckFor("2017.06.29-04:56"), TestFunctions.creatNewDateToCheckFor("2017.06.29-04:56")));
        timeSeries.add(new Pair<>(TestFunctions.creatNewDateToCheckFor("2017.06.29-05:06"), TestFunctions.creatNewDateToCheckFor("2017.06.29-05:06")));
        timeSeries.add(new Pair<>(TestFunctions.creatNewDateToCheckFor("2017.06.29-05:16"), TestFunctions.creatNewDateToCheckFor("2017.06.29-05:16")));
        timeSeries.add(new Pair<>(TestFunctions.creatNewDateToCheckFor("2017.06.29-05:26"), TestFunctions.creatNewDateToCheckFor("2017.06.29-05:26")));
        timeSeries.add(new Pair<>(TestFunctions.creatNewDateToCheckFor("2017.06.29-05:36"), TestFunctions.creatNewDateToCheckFor("2017.06.29-05:36")));
        timeSeries.add(new Pair<>(TestFunctions.creatNewDateToCheckFor("2017.06.29-05:46"), TestFunctions.creatNewDateToCheckFor("2017.06.29-05:56")));
        timeSeries.add(new Pair<>(TestFunctions.creatNewDateToCheckFor("2017.06.29-06:06"), TestFunctions.creatNewDateToCheckFor("2017.06.29-06:06")));
        timeSeries.add(new Pair<>(TestFunctions.creatNewDateToCheckFor("2017.06.29-06:16"), TestFunctions.creatNewDateToCheckFor("2017.06.29-06:16")));
        timeSeries.add(new Pair<>(TestFunctions.creatNewDateToCheckFor("2017.06.29-06:26"), TestFunctions.creatNewDateToCheckFor("2017.06.29-06:26")));
        timeSeries.add(new Pair<>(TestFunctions.creatNewDateToCheckFor("2017.06.29-06:36"), TestFunctions.creatNewDateToCheckFor("2017.06.29-06:36")));
        timeSeries.add(new Pair<>(TestFunctions.creatNewDateToCheckFor("2017.06.29-06:46"), TestFunctions.creatNewDateToCheckFor("2017.06.29-06:46")));
        timeSeries.add(new Pair<>(TestFunctions.creatNewDateToCheckFor("2017.06.29-06:56"), TestFunctions.creatNewDateToCheckFor("2017.06.29-06:56")));
        timeSeries.add(new Pair<>(TestFunctions.creatNewDateToCheckFor("2017.06.29-07:06"), TestFunctions.creatNewDateToCheckFor("2017.06.29-07:16")));
        timeSeries.add(new Pair<>(TestFunctions.creatNewDateToCheckFor("2017.06.29-07:27"), TestFunctions.creatNewDateToCheckFor("2017.06.29-07:36")));
        timeSeries.add(new Pair<>(TestFunctions.creatNewDateToCheckFor("2017.06.29-07:46"), TestFunctions.creatNewDateToCheckFor("2017.06.29-07:46")));
        timeSeries.add(new Pair<>(TestFunctions.creatNewDateToCheckFor("2017.06.29-07:56"), TestFunctions.creatNewDateToCheckFor("2017.06.29-07:56")));
        timeSeries.add(new Pair<>(TestFunctions.creatNewDateToCheckFor("2017.06.29-08:06"), TestFunctions.creatNewDateToCheckFor("2017.06.29-08:06")));
        timeSeries.add(new Pair<>(TestFunctions.creatNewDateToCheckFor("2017.06.29-10:30"), TestFunctions.creatNewDateToCheckFor("2017.06.29-10:41")));
        timeSeries.add(new Pair<>(TestFunctions.creatNewDateToCheckFor("2017.06.29-10:50"), TestFunctions.creatNewDateToCheckFor("2017.06.29-10:50")));
        timeSeries.add(new Pair<>(TestFunctions.creatNewDateToCheckFor("2017.06.29-11:00"), TestFunctions.creatNewDateToCheckFor("2017.06.29-11:00")));
        timeSeries.add(new Pair<>(TestFunctions.creatNewDateToCheckFor("2017.06.29-11:10"), TestFunctions.creatNewDateToCheckFor("2017.06.29-11:10")));
        timeSeries.add(new Pair<>(TestFunctions.creatNewDateToCheckFor("2017.06.29-11:21"), TestFunctions.creatNewDateToCheckFor("2017.06.29-11:21")));
        timeSeries.add(new Pair<>(TestFunctions.creatNewDateToCheckFor("2017.06.29-11:30"), TestFunctions.creatNewDateToCheckFor("2017.06.29-11:30")));
        timeSeries.add(new Pair<>(TestFunctions.creatNewDateToCheckFor("2017.06.29-11:40"), TestFunctions.creatNewDateToCheckFor("2017.06.29-11:40")));
        timeSeries.add(new Pair<>(TestFunctions.creatNewDateToCheckFor("2017.06.29-11:51"), TestFunctions.creatNewDateToCheckFor("2017.06.29-12:00")));
        timeSeries.add(new Pair<>(TestFunctions.creatNewDateToCheckFor("2017.06.29-12:21"), TestFunctions.creatNewDateToCheckFor("2017.06.29-12:21")));
        timeSeries.add(new Pair<>(TestFunctions.creatNewDateToCheckFor("2017.06.29-12:31"), TestFunctions.creatNewDateToCheckFor("2017.06.29-12:40")));
        
        
        FilterResult checkForThisResult = new FilterResult(filteredData, timeSeries);
        
        assertEquals(result.filteredData, checkForThisResult.filteredData);
        assertEquals(result.timeSeries, checkForThisResult.timeSeries);
        //assertEquals(result, checkForThisResult);
    }
}
