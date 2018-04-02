/*
 * Copyright (C) 2017 tiweGH
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
import de.opendiabetes.vault.processing.filter.options.ContinuousWrapperOption;
import de.opendiabetes.vault.processing.filter.options.VaultEntryTypeFilterOption;
import de.opendiabetes.vault.testhelper.StaticDataset;
import de.opendiabetes.vault.util.TimestampUtils;
import java.text.ParseException;
import java.util.ArrayList;
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
public class ContinuousWrapper_2Test {

    ContinuousWrapper filterUnderTest;

    static List<VaultEntry> dataSet;

    public ContinuousWrapper_2Test() {
    }

    @BeforeClass
    public static void setUpClass() throws ParseException {

        dataSet = StaticDataset.getStaticDataset();

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

    private void setUpFilterUnderTest(int margin) {
        filterUnderTest = new ContinuousWrapper(new ContinuousWrapperOption(dataSet, margin));
    }

    @Test
    public void testFilter_BASICFUNCTIONALITY() throws ParseException {
        FilterResult result;
        List<Pair<Date, Date>> expectedTimeSeries = new ArrayList<>();
        expectedTimeSeries.add(new Pair(TimestampUtils.createCleanTimestamp("2017.06.29-04:46", "yyyy.MM.dd-HH:mm"),
                TimestampUtils.createCleanTimestamp("2017.06.29-12:40", "yyyy.MM.dd-HH:mm")));

        setUpFilterUnderTest(0);
        result = filterUnderTest.filter(dataSet);
        assertEquals(result.filteredData, dataSet);
        assertEquals(result.timeSeries, expectedTimeSeries);

        setUpFilterUnderTest(5);
        result = filterUnderTest.filter(dataSet);
        assertEquals(result.filteredData, dataSet);
        assertEquals(result.timeSeries, expectedTimeSeries);

        setUpFilterUnderTest(100);
        result = filterUnderTest.filter(dataSet);
        assertEquals(result.filteredData, dataSet);
        assertEquals(result.timeSeries, expectedTimeSeries);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testFilter_BASICINVALIDMARGIN() throws ParseException {

        setUpFilterUnderTest(-1);
        filterUnderTest.filter(dataSet);
    }

    @Test
    //Filtered results are spread over the dataset, not located directly at the beginning nor the end of the dataset
    //Only small pre-result list, e.g. 2 entrys
    public void testFilterSmallInputInMidMiddleZeroMargin_GLUCOSE_BG() throws ParseException {
        FilterResult result;
        FilterResult expectedResult;

        setUpFilterUnderTest(0);
        expectedResult = new VaultEntryTypeFilter(new VaultEntryTypeFilterOption(VaultEntryType.GLUCOSE_BG)).filter(dataSet);
        result = filterUnderTest.filter(expectedResult.filteredData);
        assertEquals(expectedResult.filteredData, result.filteredData);
        assertEquals(expectedResult.timeSeries, result.timeSeries);
    }

    @Test
    //Filtered results are spread over the dataset, not located directly at the beginning nor the end of the dataset
    //Only small pre-result list, e.g. 2 entrys
    public void testFilterSmallInputInMidMiddleNormalMargin_GLUCOSE_BG() throws ParseException {
        FilterResult result;
        FilterResult expectedResult;
        List<Pair<Date, Date>> expectedTimeSeries = new ArrayList<>();
        List<VaultEntry> vaultEntries = new ArrayList<>();
        List<VaultEntryAnnotation> tmpAnnotations;

        setUpFilterUnderTest(5);
        result = filterUnderTest.filter(new VaultEntryTypeFilter(new VaultEntryTypeFilterOption(VaultEntryType.GLUCOSE_BG)).filter(dataSet).filteredData);

        tmpAnnotations = new ArrayList<>();
        tmpAnnotations.add(new VaultEntryAnnotation(VaultEntryAnnotation.TYPE.GLUCOSE_BG_METER_SERIAL).setValue("BG1140084B"));
        vaultEntries.add(new VaultEntry(VaultEntryType.GLUCOSE_BG, TimestampUtils.createCleanTimestamp("2017.06.29-04:53", "yyyy.MM.dd-HH:mm"), 109.0, tmpAnnotations));
        vaultEntries.add(new VaultEntry(VaultEntryType.STRESS, TimestampUtils.createCleanTimestamp("2017.06.29-04:56", "yyyy.MM.dd-HH:mm"), 36.25));
        vaultEntries.add(new VaultEntry(VaultEntryType.HEART_RATE, TimestampUtils.createCleanTimestamp("2017.06.29-04:56", "yyyy.MM.dd-HH:mm"), 72.0));
        vaultEntries.add(new VaultEntry(VaultEntryType.HEART_RATE_VARIABILITY, TimestampUtils.createCleanTimestamp("2017.06.29-04:56", "yyyy.MM.dd-HH:mm"), 50.0, 85.0));
        vaultEntries.add(new VaultEntry(VaultEntryType.SLEEP_LIGHT, TimestampUtils.createCleanTimestamp("2017.06.29-04:58", "yyyy.MM.dd-HH:mm"), 24.0));
        tmpAnnotations = new ArrayList<>();
        tmpAnnotations.add(new VaultEntryAnnotation(VaultEntryAnnotation.TYPE.GLUCOSE_BG_METER_SERIAL).setValue("BG1140084B"));
        vaultEntries.add(new VaultEntry(VaultEntryType.GLUCOSE_BG, TimestampUtils.createCleanTimestamp("2017.06.29-08:39", "yyyy.MM.dd-HH:mm"), 181.0, tmpAnnotations));
        vaultEntries.add(new VaultEntry(VaultEntryType.GLUCOSE_BOLUS_CALCULATION, TimestampUtils.createCleanTimestamp("2017.06.29-08:40", "yyyy.MM.dd-HH:mm"), 181.0));
        vaultEntries.add(new VaultEntry(VaultEntryType.BOLUS_NORMAL, TimestampUtils.createCleanTimestamp("2017.06.29-08:42", "yyyy.MM.dd-HH:mm"), 3.2));

        expectedTimeSeries.add(new Pair(TimestampUtils.createCleanTimestamp("2017.06.29-04:53", "yyyy.MM.dd-HH:mm"),
                TimestampUtils.createCleanTimestamp("2017.06.29-04:58", "yyyy.MM.dd-HH:mm")));
        expectedTimeSeries.add(new Pair(TimestampUtils.createCleanTimestamp("2017.06.29-08:39", "yyyy.MM.dd-HH:mm"),
                TimestampUtils.createCleanTimestamp("2017.06.29-08:42", "yyyy.MM.dd-HH:mm")));
        expectedResult = new FilterResult(vaultEntries, expectedTimeSeries);

//                System.out.println(expectedResult.filteredData.size() + " " + result.filteredData.size());
//        System.out.println(expectedResult.timeSeries.size() + " " + result.timeSeries.size());
//        for (int i = 0; i < expectedResult.filteredData.size(); i++) {
//            System.out.println(expectedResult.filteredData.get(i).equals(result.filteredData.get(i)));
//        }
//        for (int i = 0; i < expectedResult.timeSeries.size(); i++) {
//            System.out.println(expectedResult.timeSeries.get(i).equals(result.timeSeries.get(i)));
//        }
        assertEquals(expectedResult.filteredData, result.filteredData);
        assertEquals(expectedResult.timeSeries, result.timeSeries);
    }

    @Test
    //Filtered results are spread over the dataset, not located directly at the beginning nor the end of the dataset
    //Only small pre-result list, e.g. 2 entrys
    public void testFilterSmallInputInMidBigMargin_GLUCOSE_BG() throws ParseException {
        FilterResult result;
        FilterResult expectedResult;
        List<Pair<Date, Date>> expectedTimeSeries = new ArrayList<>();
        List<VaultEntry> vaultEntries = new ArrayList<>(dataSet);
        List<VaultEntryAnnotation> tmpAnnotations;

        setUpFilterUnderTest(4 * 60);
        result = filterUnderTest.filter(new VaultEntryTypeFilter(new VaultEntryTypeFilterOption(VaultEntryType.GLUCOSE_BG)).filter(dataSet).filteredData);

        vaultEntries.remove(vaultEntries.size() - 1);
        vaultEntries.remove(vaultEntries.size() - 1);
        expectedTimeSeries.add(new Pair(TimestampUtils.createCleanTimestamp("2017.06.29-04:46", "yyyy.MM.dd-HH:mm"),
                TimestampUtils.createCleanTimestamp("2017.06.29-12:31", "yyyy.MM.dd-HH:mm")));
        expectedResult = new FilterResult(vaultEntries, expectedTimeSeries);

        assertEquals(expectedResult.filteredData, result.filteredData);
        assertEquals(expectedResult.timeSeries, result.timeSeries);

    }

}
