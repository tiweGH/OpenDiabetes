/*
 * Copyright (C) 2017 aa80hifa
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
import de.jhit.opendiabetes.vault.exporter.NewDataset;
import java.text.ParseException;
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
public class OverThresholdFilterTest extends Assert {

    static List<VaultEntry> data;

    @BeforeClass
    public static void setUpClass() throws ParseException {
        data = NewDataset.getNewDataset();
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
     * Test of filter method, of class OverThresholdFilter.
     *
     * @author aa80hifa
     */
    @Test
    public void testOverThresholdFilter_STRESS_25_00_STRESS_AVAILABLE_STRESS_TH() throws ParseException {

        OverThresholdFilter instance = new OverThresholdFilter(VaultEntryType.STRESS, 25.00, FilterType.STRESS_AVAILABLE, FilterType.STRESS_TH);
        FilterResult result = instance.filter(data);

        List<VaultEntry> filteredData = new ArrayList<>();
        filteredData.add(new VaultEntry(VaultEntryType.STRESS, TestFunctions.creatNewDateToCheckFor("2017.06.29-04:56"), 36.25));
        filteredData.add(new VaultEntry(VaultEntryType.STRESS, TestFunctions.creatNewDateToCheckFor("2017.06.29-06:06"), 35.5));
        filteredData.add(new VaultEntry(VaultEntryType.STRESS, TestFunctions.creatNewDateToCheckFor("2017.06.29-06:16"), 30.25));
        filteredData.add(new VaultEntry(VaultEntryType.STRESS, TestFunctions.creatNewDateToCheckFor("2017.06.29-06:26"), 27.25));
        filteredData.add(new VaultEntry(VaultEntryType.STRESS, TestFunctions.creatNewDateToCheckFor("2017.06.29-06:36"), 26.5));
        filteredData.add(new VaultEntry(VaultEntryType.STRESS, TestFunctions.creatNewDateToCheckFor("2017.06.29-06:46"), 27.25));
        filteredData.add(new VaultEntry(VaultEntryType.STRESS, TestFunctions.creatNewDateToCheckFor("2017.06.29-07:27"), 25.75));
        filteredData.add(new VaultEntry(VaultEntryType.STRESS, TestFunctions.creatNewDateToCheckFor("2017.06.29-07:45"), 30.25));
        filteredData.add(new VaultEntry(VaultEntryType.STRESS, TestFunctions.creatNewDateToCheckFor("2017.06.29-07:56"), 28.0));
        filteredData.add(new VaultEntry(VaultEntryType.STRESS, TestFunctions.creatNewDateToCheckFor("2017.06.29-08:06"), 27.25));
        filteredData.add(new VaultEntry(VaultEntryType.STRESS, TestFunctions.creatNewDateToCheckFor("2017.06.29-10:50"), 52.75));
        filteredData.add(new VaultEntry(VaultEntryType.STRESS, TestFunctions.creatNewDateToCheckFor("2017.06.29-11:00"), 73.0));
        filteredData.add(new VaultEntry(VaultEntryType.STRESS, TestFunctions.creatNewDateToCheckFor("2017.06.29-11:10"), 61.0));
        filteredData.add(new VaultEntry(VaultEntryType.STRESS, TestFunctions.creatNewDateToCheckFor("2017.06.29-11:21"), 55.75));
        filteredData.add(new VaultEntry(VaultEntryType.STRESS, TestFunctions.creatNewDateToCheckFor("2017.06.29-11:51"), 75.25));

        List<Pair<Date, Date>> timeSeries = new ArrayList<>();
        timeSeries.add(new Pair<>(TestFunctions.creatNewDateToCheckFor("2017.06.29-04:56"), TestFunctions.creatNewDateToCheckFor("2017.06.29-04:56")));
        timeSeries.add(new Pair<>(TestFunctions.creatNewDateToCheckFor("2017.06.29-06:06"), TestFunctions.creatNewDateToCheckFor("2017.06.29-06:06")));
        timeSeries.add(new Pair<>(TestFunctions.creatNewDateToCheckFor("2017.06.29-06:16"), TestFunctions.creatNewDateToCheckFor("2017.06.29-06:16")));
        timeSeries.add(new Pair<>(TestFunctions.creatNewDateToCheckFor("2017.06.29-06:26"), TestFunctions.creatNewDateToCheckFor("2017.06.29-06:26")));
        timeSeries.add(new Pair<>(TestFunctions.creatNewDateToCheckFor("2017.06.29-06:36"), TestFunctions.creatNewDateToCheckFor("2017.06.29-06:36")));
        timeSeries.add(new Pair<>(TestFunctions.creatNewDateToCheckFor("2017.06.29-06:46"), TestFunctions.creatNewDateToCheckFor("2017.06.29-06:46")));
        timeSeries.add(new Pair<>(TestFunctions.creatNewDateToCheckFor("2017.06.29-07:27"), TestFunctions.creatNewDateToCheckFor("2017.06.29-07:27")));
        timeSeries.add(new Pair<>(TestFunctions.creatNewDateToCheckFor("2017.06.29-07:45"), TestFunctions.creatNewDateToCheckFor("2017.06.29-07:45")));
        timeSeries.add(new Pair<>(TestFunctions.creatNewDateToCheckFor("2017.06.29-07:56"), TestFunctions.creatNewDateToCheckFor("2017.06.29-07:56")));
        timeSeries.add(new Pair<>(TestFunctions.creatNewDateToCheckFor("2017.06.29-08:06"), TestFunctions.creatNewDateToCheckFor("2017.06.29-08:06")));
        timeSeries.add(new Pair<>(TestFunctions.creatNewDateToCheckFor("2017.06.29-10:50"), TestFunctions.creatNewDateToCheckFor("2017.06.29-10:50")));
        timeSeries.add(new Pair<>(TestFunctions.creatNewDateToCheckFor("2017.06.29-11:00"), TestFunctions.creatNewDateToCheckFor("2017.06.29-11:00")));
        timeSeries.add(new Pair<>(TestFunctions.creatNewDateToCheckFor("2017.06.29-11:10"), TestFunctions.creatNewDateToCheckFor("2017.06.29-11:10")));
        timeSeries.add(new Pair<>(TestFunctions.creatNewDateToCheckFor("2017.06.29-11:21"), TestFunctions.creatNewDateToCheckFor("2017.06.29-11:21")));
        timeSeries.add(new Pair<>(TestFunctions.creatNewDateToCheckFor("2017.06.29-11:51"), TestFunctions.creatNewDateToCheckFor("2017.06.29-11:51")));

        FilterResult checkForThisResult = new FilterResult(filteredData, timeSeries);

        assertEquals(result.filteredData, checkForThisResult.filteredData);
        assertEquals(result.timeSeries, checkForThisResult.timeSeries);
        //assertEquals(result, checkForThisResult);
    }

//    @Test(expected = NullPointerException.class)
//    public void testOverThresholdFilter_STRESS_25_00_HR_AVAILABLE_STRESS_TH() throws ParseException{
//
//        OverThresholdFilter instance = new OverThresholdFilter(VaultEntryType.STRESS, 25.00, FilterType.HR_AVAILABLE, FilterType.STRESS_TH);
//        FilterResult result = instance.filter(data);
//
//        assertEquals(result.filteredData, null);
//
//    }
    @Test
    public void testOverThresholdFilter_GLUCOSE_125_00_CGM_CGM_AVAILABLE_CGM_TH() throws ParseException {

        OverThresholdFilter instance = new OverThresholdFilter(VaultEntryType.GLUCOSE_CGM, 125.00, FilterType.CGM_AVAILABLE, FilterType.CGM_TH);
        FilterResult result = instance.filter(data);

        List<VaultEntry> filteredData = new ArrayList<>();
        filteredData.add(new VaultEntry(VaultEntryType.GLUCOSE_CGM, TestFunctions.creatNewDateToCheckFor("2016.04.18-06:58"), 322));
        //
        filteredData.add(new VaultEntry(VaultEntryType.GLUCOSE_CGM, TestFunctions.creatNewDateToCheckFor("2016.04.18-07:13"), 297));
        filteredData.add(new VaultEntry(VaultEntryType.GLUCOSE_CGM, TestFunctions.creatNewDateToCheckFor("2016.04.18-07:28"), 253));
        filteredData.add(new VaultEntry(VaultEntryType.GLUCOSE_CGM, TestFunctions.creatNewDateToCheckFor("2016.04.18-07:43"), 226));
        filteredData.add(new VaultEntry(VaultEntryType.GLUCOSE_CGM, TestFunctions.creatNewDateToCheckFor("2016.04.18-07:58"), 219));
        //
        filteredData.add(new VaultEntry(VaultEntryType.GLUCOSE_CGM, TestFunctions.creatNewDateToCheckFor("2016.04.18-08:13"), 206));
        filteredData.add(new VaultEntry(VaultEntryType.GLUCOSE_CGM, TestFunctions.creatNewDateToCheckFor("2016.04.18-08:28"), 193));
        filteredData.add(new VaultEntry(VaultEntryType.GLUCOSE_CGM, TestFunctions.creatNewDateToCheckFor("2016.04.18-08:43"), 180));
        filteredData.add(new VaultEntry(VaultEntryType.GLUCOSE_CGM, TestFunctions.creatNewDateToCheckFor("2016.04.18-08:58"), 168));
        //
        filteredData.add(new VaultEntry(VaultEntryType.GLUCOSE_CGM, TestFunctions.creatNewDateToCheckFor("2016.04.18-09:13"), 156));
        filteredData.add(new VaultEntry(VaultEntryType.GLUCOSE_CGM, TestFunctions.creatNewDateToCheckFor("2016.04.18-09:29"), 141));
        filteredData.add(new VaultEntry(VaultEntryType.GLUCOSE_CGM, TestFunctions.creatNewDateToCheckFor("2016.04.18-10:14"), 138));
        //
        filteredData.add(new VaultEntry(VaultEntryType.GLUCOSE_CGM, TestFunctions.creatNewDateToCheckFor("2016.04.18-11:44"), 198));

        List<Pair<Date, Date>> timeSeries = new ArrayList<>();
        timeSeries.add(new Pair<>(TestFunctions.creatNewDateToCheckFor("2016.04.18-06:58"), TestFunctions.creatNewDateToCheckFor("2016.04.18-06:58")));
        timeSeries.add(new Pair<>(TestFunctions.creatNewDateToCheckFor("2016.04.18-07:13"), TestFunctions.creatNewDateToCheckFor("2016.04.18-07:58")));
        timeSeries.add(new Pair<>(TestFunctions.creatNewDateToCheckFor("2016.04.18-08:13"), TestFunctions.creatNewDateToCheckFor("2016.04.18-08:58")));
        timeSeries.add(new Pair<>(TestFunctions.creatNewDateToCheckFor("2016.04.18-09:13"), TestFunctions.creatNewDateToCheckFor("2016.04.18-09:29")));
        timeSeries.add(new Pair<>(TestFunctions.creatNewDateToCheckFor("2016.04.18-10:14"), TestFunctions.creatNewDateToCheckFor("2016.04.18-10:14")));
        timeSeries.add(new Pair<>(TestFunctions.creatNewDateToCheckFor("2016.04.18-11:44"), TestFunctions.creatNewDateToCheckFor("2016.04.18-11:44")));

        FilterResult checkForThisResult = new FilterResult(filteredData, timeSeries);

        assertEquals(result.filteredData, checkForThisResult.filteredData);
        assertEquals(result.timeSeries, checkForThisResult.timeSeries);
        //assertEquals(result, checkForThisResult);
    }
}
