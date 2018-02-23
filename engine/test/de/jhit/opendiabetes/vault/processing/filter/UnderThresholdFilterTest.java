/*
 * Copyright (C) 2017 a.a.aponte
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
 * @author a.a.aponte
 */
public class UnderThresholdFilterTest extends Assert {

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
     * Test of filter method, of class UnderThresholdFilter.
     *
     * @author a.a.aponte
     */
    @Test
    public void testUnderThresholdFilterTest_STRESS_25_00_STRESS_AVAILABLE_STRESS_TH() throws ParseException {

        // UnderThresholdFilter instance = new UnderThresholdFilter(VaultEntryType.STRESS, 25.00, FilterType.STRESS_AVAILABLE, FilterType.STRESS_TH);
        UnderThresholdFilter instance = new UnderThresholdFilter(VaultEntryType.STRESS, 25.00, FilterType.STRESS_AVAILABLE, FilterType.STRESS_TH);
        FilterResult result = instance.filter(data);

        List<VaultEntry> filteredData = new ArrayList<>();
        filteredData.add(new VaultEntry(VaultEntryType.STRESS, TestFunctions.creatNewDateToCheckFor("2017.06.29-04:46"), 21.5));
        filteredData.add(new VaultEntry(VaultEntryType.STRESS, TestFunctions.creatNewDateToCheckFor("2017.06.29-05:06"), 23.75));
        filteredData.add(new VaultEntry(VaultEntryType.STRESS, TestFunctions.creatNewDateToCheckFor("2017.06.29-05:16"), 19.25));
        filteredData.add(new VaultEntry(VaultEntryType.STRESS, TestFunctions.creatNewDateToCheckFor("2017.06.29-05:26"), 21.75));
        filteredData.add(new VaultEntry(VaultEntryType.STRESS, TestFunctions.creatNewDateToCheckFor("2017.06.29-08:16"), 0.0));
        filteredData.add(new VaultEntry(VaultEntryType.STRESS, TestFunctions.creatNewDateToCheckFor("2017.06.29-11:40"), 9.5));
        filteredData.add(new VaultEntry(VaultEntryType.STRESS, TestFunctions.creatNewDateToCheckFor("2017.06.29-12:11"), 17.25));
        filteredData.add(new VaultEntry(VaultEntryType.STRESS, TestFunctions.creatNewDateToCheckFor("2017.06.29-12:21"), 18.25));

        List<Pair<Date, Date>> timeSeries = new ArrayList<>();
        timeSeries.add(new Pair<>(TestFunctions.creatNewDateToCheckFor("2017.06.29-04:46"), TestFunctions.creatNewDateToCheckFor("2017.06.29-04:46")));
        timeSeries.add(new Pair<>(TestFunctions.creatNewDateToCheckFor("2017.06.29-05:06"), TestFunctions.creatNewDateToCheckFor("2017.06.29-05:06")));
        timeSeries.add(new Pair<>(TestFunctions.creatNewDateToCheckFor("2017.06.29-05:16"), TestFunctions.creatNewDateToCheckFor("2017.06.29-05:16")));
        timeSeries.add(new Pair<>(TestFunctions.creatNewDateToCheckFor("2017.06.29-05:26"), TestFunctions.creatNewDateToCheckFor("2017.06.29-05:26")));
        timeSeries.add(new Pair<>(TestFunctions.creatNewDateToCheckFor("2017.06.29-08:16"), TestFunctions.creatNewDateToCheckFor("2017.06.29-08:16")));
        timeSeries.add(new Pair<>(TestFunctions.creatNewDateToCheckFor("2017.06.29-11:40"), TestFunctions.creatNewDateToCheckFor("2017.06.29-11:40")));
        timeSeries.add(new Pair<>(TestFunctions.creatNewDateToCheckFor("2017.06.29-12:11"), TestFunctions.creatNewDateToCheckFor("2017.06.29-12:11")));
        timeSeries.add(new Pair<>(TestFunctions.creatNewDateToCheckFor("2017.06.29-12:21"), TestFunctions.creatNewDateToCheckFor("2017.06.29-12:21")));

        FilterResult checkForThisResult = new FilterResult(filteredData, timeSeries);

        assertEquals(result.filteredData, checkForThisResult.filteredData);
        assertEquals(result.timeSeries, checkForThisResult.timeSeries);
        //assertEquals(result, checkForThisResult);
    }

//    @Test(expected = NullPointerException.class)
//    public void testUnderThresholdFilterTest_STRESS_25_00_HR_AVAILABLE_STRESS_TH() throws ParseException {
//
//        UnderThresholdFilter instance = new UnderThresholdFilter(VaultEntryType.STRESS, 25.00, FilterType.HR_AVAILABLE, FilterType.STRESS_TH);
//        FilterResult result = instance.filter(data);
//
//        assertEquals(result.filteredData, null);
//
//    }
    @Test
    public void testUnderThresholdFilterTest_GLUCOSE_125_00_CGM_CGM_AVAILABLE_CGM_TH() throws ParseException {

        UnderThresholdFilter instance = new UnderThresholdFilter(VaultEntryType.GLUCOSE_CGM, 125.00, FilterType.CGM_AVAILABLE, FilterType.CGM_TH);
        FilterResult result = instance.filter(data);

        List<VaultEntry> filteredData = new ArrayList<>();
        filteredData.add(new VaultEntry(VaultEntryType.GLUCOSE_CGM, TestFunctions.creatNewDateToCheckFor("2016.04.18-09:59"), 118));
        //
        filteredData.add(new VaultEntry(VaultEntryType.GLUCOSE_CGM, TestFunctions.creatNewDateToCheckFor("2016.04.18-10:29"), 110));
        filteredData.add(new VaultEntry(VaultEntryType.GLUCOSE_CGM, TestFunctions.creatNewDateToCheckFor("2016.04.18-10:43"), 105));
        //
        filteredData.add(new VaultEntry(VaultEntryType.GLUCOSE_CGM, TestFunctions.creatNewDateToCheckFor("2016.04.18-10:59"), 100));
        //
        filteredData.add(new VaultEntry(VaultEntryType.GLUCOSE_CGM, TestFunctions.creatNewDateToCheckFor("2016.04.18-11:14"), 120));
        filteredData.add(new VaultEntry(VaultEntryType.GLUCOSE_CGM, TestFunctions.creatNewDateToCheckFor("2016.04.18-11:29"), 103));

        List<Pair<Date, Date>> timeSeries = new ArrayList<>();
        timeSeries.add(new Pair<>(TestFunctions.creatNewDateToCheckFor("2016.04.18-09:59"), TestFunctions.creatNewDateToCheckFor("2016.04.18-09:59")));
        timeSeries.add(new Pair<>(TestFunctions.creatNewDateToCheckFor("2016.04.18-10:29"), TestFunctions.creatNewDateToCheckFor("2016.04.18-10:43")));
        timeSeries.add(new Pair<>(TestFunctions.creatNewDateToCheckFor("2016.04.18-10:59"), TestFunctions.creatNewDateToCheckFor("2016.04.18-10:59")));
        timeSeries.add(new Pair<>(TestFunctions.creatNewDateToCheckFor("2016.04.18-11:14"), TestFunctions.creatNewDateToCheckFor("2016.04.18-11:29")));
        FilterResult checkForThisResult = new FilterResult(filteredData, timeSeries);

        assertEquals(result.filteredData, checkForThisResult.filteredData);
        assertEquals(result.timeSeries, checkForThisResult.timeSeries);
        //assertEquals(result, checkForThisResult);
    }
}
