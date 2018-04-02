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
import de.opendiabetes.vault.container.VaultEntryType;
import de.opendiabetes.vault.processing.filter.options.VaultEntryTypeFilterOption;
import de.opendiabetes.vault.testhelper.StaticDataset;
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
 * Test of filter method, of class EventFilter.
 *
 * @author juehv, a.a.aponte
 */
public class VaultEntryTypeFilterTest extends Assert {

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

    VaultEntryTypeFilter setUpFilter(VaultEntryType type) {
        return new VaultEntryTypeFilter(new VaultEntryTypeFilterOption(type));
    }

    /**
     * Test of filter method, of class EventFilter.
     *
     * @author juehv, a.a.aponte
     */
    @Test
    public void testEventFilter_STRESS() throws ParseException {
        //System.out.println("filter");
        String dateTimePointBegin_1 = "2017.06.29-04:46";
        String dateTimePointBegin_2 = "2017.06.29-04:46";
        String dateTimePointEnd_1 = "2017.06.29-12:21";
        String dateTimePointEnd_2 = "2017.06.29-12:21";

        List<VaultEntry> data = StaticDataset.getStaticDataset();
        VaultEntryTypeFilter instance = setUpFilter(VaultEntryType.STRESS);
        FilterResult result = instance.filter(data);

        //System.out.println(result);
        TestFunctions.checkForVaultEntryType(result, VaultEntryType.STRESS);
        TestFunctions.checkForTimestamp(dateTimePointBegin_1, dateTimePointBegin_2, dateTimePointEnd_1, dateTimePointEnd_2, result);
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
        VaultEntryTypeFilter instance = setUpFilter(VaultEntryType.GLUCOSE_BG);
        FilterResult result = instance.filter(data);

        //System.out.println(result);
        TestFunctions.checkForVaultEntryType(result, VaultEntryType.GLUCOSE_BG);
        TestFunctions.checkForTimestamp(dateTimePointBegin_1, dateTimePointBegin_2, dateTimePointEnd_1, dateTimePointEnd_2, result);
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
        VaultEntryTypeFilter instance = setUpFilter(VaultEntryType.HEART_RATE);
        FilterResult result = instance.filter(data);

        //System.out.println(result);
        TestFunctions.checkForVaultEntryType(result, VaultEntryType.HEART_RATE);
        TestFunctions.checkForTimestamp(dateTimePointBegin_1, dateTimePointBegin_2, dateTimePointEnd_1, dateTimePointEnd_2, result);
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
        VaultEntryTypeFilter instance = setUpFilter(VaultEntryType.HEART_RATE_VARIABILITY);
        FilterResult result = instance.filter(data);

        //System.out.println(result);
        TestFunctions.checkForVaultEntryType(result, VaultEntryType.HEART_RATE_VARIABILITY);
        TestFunctions.checkForTimestamp(dateTimePointBegin_1, dateTimePointBegin_2, dateTimePointEnd_1, dateTimePointEnd_2, result);
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
        VaultEntryTypeFilter instance = setUpFilter(VaultEntryType.BASAL_PROFILE);
        FilterResult result = instance.filter(data);

        //System.out.println(result);
        TestFunctions.checkForVaultEntryType(result, VaultEntryType.BASAL_PROFILE);
        TestFunctions.checkForTimestamp(dateTimePointBegin_1, dateTimePointBegin_2, dateTimePointEnd_1, dateTimePointEnd_2, result);
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
        VaultEntryTypeFilter instance = setUpFilter(VaultEntryType.GLUCOSE_BOLUS_CALCULATION);
        FilterResult result = instance.filter(data);

        //System.out.println(result);
        TestFunctions.checkForVaultEntryType(result, VaultEntryType.GLUCOSE_BOLUS_CALCULATION);
        TestFunctions.checkForTimestamp(dateTimePointBegin_1, dateTimePointBegin_2, dateTimePointEnd_1, dateTimePointEnd_2, result);
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
        VaultEntryTypeFilter instance = setUpFilter(VaultEntryType.GLUCOSE_BG);
        FilterResult result = instance.filter(data);

        //System.out.println(result);
        TestFunctions.checkForVaultEntryType(result, VaultEntryType.GLUCOSE_BG);
        // no timestamps given
        //checkForTimestamp(dateTimePointBegin_1, dateTimePointBegin_2, dateTimePointEnd_1, dateTimePointEnd_2, result);
        assertTrue(result.size() == 0);
        assertTrue(result.filteredData.isEmpty());
        assertTrue(result.timeSeries.isEmpty());
    }

    @Test
    public void testEventFilter_HEART_RATE_complete() throws ParseException {

        List<VaultEntry> filteredData = new ArrayList<>();
        filteredData.add(new VaultEntry(VaultEntryType.HEART_RATE, TestFunctions.creatNewDateToCheckFor("2017.06.29-04:56"), 72.0));
        filteredData.add(new VaultEntry(VaultEntryType.HEART_RATE, TestFunctions.creatNewDateToCheckFor("2017.06.29-05:06"), 59.0));
        filteredData.add(new VaultEntry(VaultEntryType.HEART_RATE, TestFunctions.creatNewDateToCheckFor("2017.06.29-05:16"), 53.0));
        filteredData.add(new VaultEntry(VaultEntryType.HEART_RATE, TestFunctions.creatNewDateToCheckFor("2017.06.29-05:26"), 58.0));
        filteredData.add(new VaultEntry(VaultEntryType.HEART_RATE, TestFunctions.creatNewDateToCheckFor("2017.06.29-05:36"), 59.0));
        filteredData.add(new VaultEntry(VaultEntryType.HEART_RATE, TestFunctions.creatNewDateToCheckFor("2017.06.29-05:46"), 57.0));
        filteredData.add(new VaultEntry(VaultEntryType.HEART_RATE, TestFunctions.creatNewDateToCheckFor("2017.06.29-05:56"), 56.0));
        filteredData.add(new VaultEntry(VaultEntryType.HEART_RATE, TestFunctions.creatNewDateToCheckFor("2017.06.29-06:06"), 71.0));
        filteredData.add(new VaultEntry(VaultEntryType.HEART_RATE, TestFunctions.creatNewDateToCheckFor("2017.06.29-06:16"), 66.0));
        filteredData.add(new VaultEntry(VaultEntryType.HEART_RATE, TestFunctions.creatNewDateToCheckFor("2017.06.29-06:26"), 62.0));
        filteredData.add(new VaultEntry(VaultEntryType.HEART_RATE, TestFunctions.creatNewDateToCheckFor("2017.06.29-06:36"), 63.0));
        filteredData.add(new VaultEntry(VaultEntryType.HEART_RATE, TestFunctions.creatNewDateToCheckFor("2017.06.29-06:46"), 58.0));
        filteredData.add(new VaultEntry(VaultEntryType.HEART_RATE, TestFunctions.creatNewDateToCheckFor("2017.06.29-06:56"), 59.0));
        filteredData.add(new VaultEntry(VaultEntryType.HEART_RATE, TestFunctions.creatNewDateToCheckFor("2017.06.29-07:06"), 56.0));
        filteredData.add(new VaultEntry(VaultEntryType.HEART_RATE, TestFunctions.creatNewDateToCheckFor("2017.06.29-07:16"), 57.0));
        filteredData.add(new VaultEntry(VaultEntryType.HEART_RATE, TestFunctions.creatNewDateToCheckFor("2017.06.29-07:27"), 58.0));
        filteredData.add(new VaultEntry(VaultEntryType.HEART_RATE, TestFunctions.creatNewDateToCheckFor("2017.06.29-07:36"), 68.0));
        filteredData.add(new VaultEntry(VaultEntryType.HEART_RATE, TestFunctions.creatNewDateToCheckFor("2017.06.29-07:46"), 62.0));
        filteredData.add(new VaultEntry(VaultEntryType.HEART_RATE, TestFunctions.creatNewDateToCheckFor("2017.06.29-07:56"), 63.0));
        filteredData.add(new VaultEntry(VaultEntryType.HEART_RATE, TestFunctions.creatNewDateToCheckFor("2017.06.29-08:06"), 62.0));
        filteredData.add(new VaultEntry(VaultEntryType.HEART_RATE, TestFunctions.creatNewDateToCheckFor("2017.06.29-10:30"), 65.0));
        filteredData.add(new VaultEntry(VaultEntryType.HEART_RATE, TestFunctions.creatNewDateToCheckFor("2017.06.29-10:41"), 66.0));
        filteredData.add(new VaultEntry(VaultEntryType.HEART_RATE, TestFunctions.creatNewDateToCheckFor("2017.06.29-10:50"), 85.0));
        filteredData.add(new VaultEntry(VaultEntryType.HEART_RATE, TestFunctions.creatNewDateToCheckFor("2017.06.29-11:00"), 91.0));
        filteredData.add(new VaultEntry(VaultEntryType.HEART_RATE, TestFunctions.creatNewDateToCheckFor("2017.06.29-11:10"), 77.0));
        filteredData.add(new VaultEntry(VaultEntryType.HEART_RATE, TestFunctions.creatNewDateToCheckFor("2017.06.29-11:21"), 68.0));
        filteredData.add(new VaultEntry(VaultEntryType.HEART_RATE, TestFunctions.creatNewDateToCheckFor("2017.06.29-11:30"), 51.0));
        filteredData.add(new VaultEntry(VaultEntryType.HEART_RATE, TestFunctions.creatNewDateToCheckFor("2017.06.29-11:40"), 94.0));
        filteredData.add(new VaultEntry(VaultEntryType.HEART_RATE, TestFunctions.creatNewDateToCheckFor("2017.06.29-11:51"), 58.0));
        filteredData.add(new VaultEntry(VaultEntryType.HEART_RATE, TestFunctions.creatNewDateToCheckFor("2017.06.29-12:00"), 55.0));
        filteredData.add(new VaultEntry(VaultEntryType.HEART_RATE, TestFunctions.creatNewDateToCheckFor("2017.06.29-12:21"), 51.0));
        filteredData.add(new VaultEntry(VaultEntryType.HEART_RATE, TestFunctions.creatNewDateToCheckFor("2017.06.29-12:31"), 60.0));
        filteredData.add(new VaultEntry(VaultEntryType.HEART_RATE, TestFunctions.creatNewDateToCheckFor("2017.06.29-12:40"), 68.0));

        List<Pair<Date, Date>> timeSeries = new ArrayList<>();
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

        List<VaultEntry> data = StaticDataset.getStaticDataset();
        VaultEntryTypeFilter instance = setUpFilter(VaultEntryType.HEART_RATE);
        FilterResult result = instance.filter(data);

        assertEquals(result.filteredData, checkForThisResult.filteredData);
        assertEquals(result.timeSeries, checkForThisResult.timeSeries);
        //assertEquals(result, checkForThisResult);
    }
}
