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
package de.opendiabetes.vault.processing.preprocessing;

import de.opendiabetes.vault.container.VaultEntry;
import de.opendiabetes.vault.container.VaultEntryType;
import de.opendiabetes.vault.processing.preprocessing.options.GapRemoverPreprocessorOption;
import de.opendiabetes.vault.util.TimestampUtils;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;
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
public class GapRemover_enhancedTest {

    GapRemover instance;
    List<VaultEntry> data;
    List<VaultEntry> actualResult;
    List<VaultEntry> expectedResult;

    public GapRemover_enhancedTest() {
    }

    @BeforeClass
    public static void setUpClass() {
    }

    @AfterClass
    public static void tearDownClass() {
    }

    @Before
    public void setUp() {
        data = new ArrayList<>();
    }

    @After
    public void tearDown() {
    }

    @Test
    public void testEmptyList() {

        GapRemoverPreprocessorOption gapRemoverPreprocessorOption = new GapRemoverPreprocessorOption(VaultEntryType.GLUCOSE_CGM, 0);
        GapRemoverPreprocessorOption gapRemoverPreprocessorOptionTwo = new GapRemoverPreprocessorOption(VaultEntryType.GLUCOSE_CGM, 5);
        
        instance = new GapRemover(gapRemoverPreprocessorOption);
        assertEquals(instance.preprocess(data), new ArrayList<>());

        instance = new GapRemover(gapRemoverPreprocessorOptionTwo);
        assertEquals(instance.preprocess(data), new ArrayList<>());
    }

    @Test
    public void testData_WithoutMatchingEntry() throws ParseException {

        data.add(new VaultEntry(VaultEntryType.BASAL_PROFILE, TimestampUtils.createCleanTimestamp("2010.01.08-00:00", "yyyy.MM.dd-HH:mm"), 0.65));
        data.add(new VaultEntry(VaultEntryType.GLUCOSE_CGM_RAW, TimestampUtils.createCleanTimestamp("2010.01.08-00:00", "yyyy.MM.dd-HH:mm"), 30.0));
        data.add(new VaultEntry(VaultEntryType.PUMP_CGM_PREDICTION, TimestampUtils.createCleanTimestamp("2010.01.08-00:00", "yyyy.MM.dd-HH:mm"), 140.0));
        data.add(new VaultEntry(VaultEntryType.GLUCOSE_ELEVATION_30, TimestampUtils.createCleanTimestamp("2010.01.08-00:00", "yyyy.MM.dd-HH:mm"), -32.0));
        data.add(new VaultEntry(VaultEntryType.GLUCOSE_BG, TimestampUtils.createCleanTimestamp("2010.01.08-00:03", "yyyy.MM.dd-HH:mm"), 125.0));
        data.add(new VaultEntry(VaultEntryType.GLUCOSE_BOLUS_CALCULATION, TimestampUtils.createCleanTimestamp("2010.01.08-00:04", "yyyy.MM.dd-HH:mm"), 125.0));
        data.add(new VaultEntry(VaultEntryType.BOLUS_NORMAL, TimestampUtils.createCleanTimestamp("2010.01.08-00:04", "yyyy.MM.dd-HH:mm"), 0.3));

        GapRemoverPreprocessorOption gapRemoverPreprocessorOption = new GapRemoverPreprocessorOption(VaultEntryType.GLUCOSE_CGM, 5);
        instance = new GapRemover(gapRemoverPreprocessorOption);

        assertEquals(data, instance.preprocess(data));

    }

    @Test
    public void testData_OneMatchingEntry_RestOutOfRange() throws ParseException {

        data.add(new VaultEntry(VaultEntryType.BASAL_PROFILE, TimestampUtils.createCleanTimestamp("2010.01.08-00:00", "yyyy.MM.dd-HH:mm"), 0.65));
        data.add(new VaultEntry(VaultEntryType.GLUCOSE_CGM_RAW, TimestampUtils.createCleanTimestamp("2010.01.08-00:00", "yyyy.MM.dd-HH:mm"), 30.0));
        data.add(new VaultEntry(VaultEntryType.GLUCOSE_CGM, TimestampUtils.createCleanTimestamp("2010.01.08-00:00", "yyyy.MM.dd-HH:mm"), 151.0));
        data.add(new VaultEntry(VaultEntryType.PUMP_CGM_PREDICTION, TimestampUtils.createCleanTimestamp("2010.01.08-00:00", "yyyy.MM.dd-HH:mm"), 140.0));
        data.add(new VaultEntry(VaultEntryType.GLUCOSE_ELEVATION_30, TimestampUtils.createCleanTimestamp("2010.01.08-00:00", "yyyy.MM.dd-HH:mm"), -32.0));
        data.add(new VaultEntry(VaultEntryType.GLUCOSE_BG, TimestampUtils.createCleanTimestamp("2010.01.08-00:03", "yyyy.MM.dd-HH:mm"), 125.0));
        data.add(new VaultEntry(VaultEntryType.GLUCOSE_BOLUS_CALCULATION, TimestampUtils.createCleanTimestamp("2010.01.08-00:04", "yyyy.MM.dd-HH:mm"), 125.0));
        data.add(new VaultEntry(VaultEntryType.BOLUS_NORMAL, TimestampUtils.createCleanTimestamp("2010.01.08-00:11", "yyyy.MM.dd-HH:mm"), 0.3));

        expectedResult = new ArrayList<>();
        expectedResult.add(new VaultEntry(VaultEntryType.BASAL_PROFILE, TimestampUtils.createCleanTimestamp("2010.01.08-00:00", "yyyy.MM.dd-HH:mm"), 0.65));
        expectedResult.add(new VaultEntry(VaultEntryType.GLUCOSE_CGM_RAW, TimestampUtils.createCleanTimestamp("2010.01.08-00:00", "yyyy.MM.dd-HH:mm"), 30.0));
        expectedResult.add(new VaultEntry(VaultEntryType.GLUCOSE_CGM, TimestampUtils.createCleanTimestamp("2010.01.08-00:00", "yyyy.MM.dd-HH:mm"), 151.0));

        GapRemoverPreprocessorOption gapRemoverPreprocessorOption = new GapRemoverPreprocessorOption(VaultEntryType.GLUCOSE_CGM, 5);
        instance = new GapRemover(gapRemoverPreprocessorOption);

//        actualResult = instance.preprocess(data);
//        System.out.println("testData_OneMatchingEntry_RestOutOfRange");
//        for (VaultEntry vaultEntry : actualResult) {
//            System.out.println(vaultEntry.toString());
//        }
        assertEquals(data, actualResult);

    }

    @Test
    public void testData_TwoMatchingEntries_RestOutOfRange() throws ParseException {

        data.add(new VaultEntry(VaultEntryType.BASAL_PROFILE, TimestampUtils.createCleanTimestamp("2010.01.08-00:00", "yyyy.MM.dd-HH:mm"), 0.65));
        data.add(new VaultEntry(VaultEntryType.GLUCOSE_CGM, TimestampUtils.createCleanTimestamp("2010.01.08-00:00", "yyyy.MM.dd-HH:mm"), 151.0));
        data.add(new VaultEntry(VaultEntryType.GLUCOSE_CGM_RAW, TimestampUtils.createCleanTimestamp("2010.01.08-00:00", "yyyy.MM.dd-HH:mm"), 30.0));
        data.add(new VaultEntry(VaultEntryType.PUMP_CGM_PREDICTION, TimestampUtils.createCleanTimestamp("2010.01.08-00:00", "yyyy.MM.dd-HH:mm"), 140.0));
        data.add(new VaultEntry(VaultEntryType.GLUCOSE_ELEVATION_30, TimestampUtils.createCleanTimestamp("2010.01.08-00:00", "yyyy.MM.dd-HH:mm"), -32.0));
        data.add(new VaultEntry(VaultEntryType.GLUCOSE_BG, TimestampUtils.createCleanTimestamp("2010.01.08-00:03", "yyyy.MM.dd-HH:mm"), 125.0));
        data.add(new VaultEntry(VaultEntryType.GLUCOSE_BOLUS_CALCULATION, TimestampUtils.createCleanTimestamp("2010.01.08-00:04", "yyyy.MM.dd-HH:mm"), 125.0));
        data.add(new VaultEntry(VaultEntryType.BOLUS_NORMAL, TimestampUtils.createCleanTimestamp("2010.01.08-00:04", "yyyy.MM.dd-HH:mm"), 0.3));
        data.add(new VaultEntry(VaultEntryType.GLUCOSE_CGM, TimestampUtils.createCleanTimestamp("2010.01.08-00:05", "yyyy.MM.dd-HH:mm"), 144.0));
        data.add(new VaultEntry(VaultEntryType.GLUCOSE_CGM_RAW, TimestampUtils.createCleanTimestamp("2010.01.08-00:05", "yyyy.MM.dd-HH:mm"), 29.0));
        data.add(new VaultEntry(VaultEntryType.PUMP_CGM_PREDICTION, TimestampUtils.createCleanTimestamp("2010.01.08-00:05", "yyyy.MM.dd-HH:mm"), 135.0));
        data.add(new VaultEntry(VaultEntryType.GLUCOSE_ELEVATION_30, TimestampUtils.createCleanTimestamp("2010.01.08-00:05", "yyyy.MM.dd-HH:mm"), -41.0));
        data.add(new VaultEntry(VaultEntryType.STRESS, TimestampUtils.createCleanTimestamp("2010.01.08-00:06", "yyyy.MM.dd-HH:mm"), 64.75));
        data.add(new VaultEntry(VaultEntryType.HEART_RATE_VARIABILITY, TimestampUtils.createCleanTimestamp("2010.01.08-00:06", "yyyy.MM.dd-HH:mm"), 24.0));
        data.add(new VaultEntry(VaultEntryType.HEART_RATE, TimestampUtils.createCleanTimestamp("2010.01.08-00:11", "yyyy.MM.dd-HH:mm"), 85.0));

        expectedResult = new ArrayList<>();
        expectedResult.add(new VaultEntry(VaultEntryType.BASAL_PROFILE, TimestampUtils.createCleanTimestamp("2010.01.08-00:00", "yyyy.MM.dd-HH:mm"), 0.65));
        expectedResult.add(new VaultEntry(VaultEntryType.GLUCOSE_CGM, TimestampUtils.createCleanTimestamp("2010.01.08-00:00", "yyyy.MM.dd-HH:mm"), 151.0));
        expectedResult.add(new VaultEntry(VaultEntryType.GLUCOSE_CGM_RAW, TimestampUtils.createCleanTimestamp("2010.01.08-00:00", "yyyy.MM.dd-HH:mm"), 30.0));
        expectedResult.add(new VaultEntry(VaultEntryType.PUMP_CGM_PREDICTION, TimestampUtils.createCleanTimestamp("2010.01.08-00:00", "yyyy.MM.dd-HH:mm"), 140.0));
        expectedResult.add(new VaultEntry(VaultEntryType.GLUCOSE_ELEVATION_30, TimestampUtils.createCleanTimestamp("2010.01.08-00:00", "yyyy.MM.dd-HH:mm"), -32.0));
        expectedResult.add(new VaultEntry(VaultEntryType.GLUCOSE_BG, TimestampUtils.createCleanTimestamp("2010.01.08-00:03", "yyyy.MM.dd-HH:mm"), 125.0));
        expectedResult.add(new VaultEntry(VaultEntryType.GLUCOSE_BOLUS_CALCULATION, TimestampUtils.createCleanTimestamp("2010.01.08-00:04", "yyyy.MM.dd-HH:mm"), 125.0));
        expectedResult.add(new VaultEntry(VaultEntryType.BOLUS_NORMAL, TimestampUtils.createCleanTimestamp("2010.01.08-00:04", "yyyy.MM.dd-HH:mm"), 0.3));
        expectedResult.add(new VaultEntry(VaultEntryType.GLUCOSE_CGM, TimestampUtils.createCleanTimestamp("2010.01.08-00:05", "yyyy.MM.dd-HH:mm"), 144.0));
        expectedResult.add(new VaultEntry(VaultEntryType.GLUCOSE_CGM_RAW, TimestampUtils.createCleanTimestamp("2010.01.08-00:05", "yyyy.MM.dd-HH:mm"), 29.0));
        expectedResult.add(new VaultEntry(VaultEntryType.PUMP_CGM_PREDICTION, TimestampUtils.createCleanTimestamp("2010.01.08-00:05", "yyyy.MM.dd-HH:mm"), 135.0));
        expectedResult.add(new VaultEntry(VaultEntryType.GLUCOSE_ELEVATION_30, TimestampUtils.createCleanTimestamp("2010.01.08-00:05", "yyyy.MM.dd-HH:mm"), -41.0));

        GapRemoverPreprocessorOption gapRemoverPreprocessorOption = new GapRemoverPreprocessorOption(VaultEntryType.GLUCOSE_CGM, 5);
        instance = new GapRemover(gapRemoverPreprocessorOption);

        System.out.println("testData_TwoMatchingEntries_RestOutOfRange");
        actualResult = instance.preprocess(data);

        for (VaultEntry vaultEntry : expectedResult) {
            if (!actualResult.contains(vaultEntry)) {
                System.out.println(vaultEntry.toString());
            }
        }

        assertEquals(expectedResult, actualResult);
    }

    @Test
    public void testData_TwoMatchingEntries_RestInRange() throws ParseException {

        data.add(new VaultEntry(VaultEntryType.BASAL_PROFILE, TimestampUtils.createCleanTimestamp("2010.01.08-00:00", "yyyy.MM.dd-HH:mm"), 0.65));
        data.add(new VaultEntry(VaultEntryType.GLUCOSE_CGM, TimestampUtils.createCleanTimestamp("2010.01.08-00:00", "yyyy.MM.dd-HH:mm"), 151.0));
        data.add(new VaultEntry(VaultEntryType.GLUCOSE_CGM_RAW, TimestampUtils.createCleanTimestamp("2010.01.08-00:00", "yyyy.MM.dd-HH:mm"), 30.0));
        data.add(new VaultEntry(VaultEntryType.PUMP_CGM_PREDICTION, TimestampUtils.createCleanTimestamp("2010.01.08-00:00", "yyyy.MM.dd-HH:mm"), 140.0));
        data.add(new VaultEntry(VaultEntryType.GLUCOSE_ELEVATION_30, TimestampUtils.createCleanTimestamp("2010.01.08-00:00", "yyyy.MM.dd-HH:mm"), -32.0));
        data.add(new VaultEntry(VaultEntryType.GLUCOSE_BG, TimestampUtils.createCleanTimestamp("2010.01.08-00:03", "yyyy.MM.dd-HH:mm"), 125.0));
        data.add(new VaultEntry(VaultEntryType.GLUCOSE_BOLUS_CALCULATION, TimestampUtils.createCleanTimestamp("2010.01.08-00:04", "yyyy.MM.dd-HH:mm"), 125.0));
        data.add(new VaultEntry(VaultEntryType.BOLUS_NORMAL, TimestampUtils.createCleanTimestamp("2010.01.08-00:04", "yyyy.MM.dd-HH:mm"), 0.3));
        data.add(new VaultEntry(VaultEntryType.GLUCOSE_CGM, TimestampUtils.createCleanTimestamp("2010.01.08-00:05", "yyyy.MM.dd-HH:mm"), 144.0));
        data.add(new VaultEntry(VaultEntryType.GLUCOSE_CGM_RAW, TimestampUtils.createCleanTimestamp("2010.01.08-00:05", "yyyy.MM.dd-HH:mm"), 29.0));
        data.add(new VaultEntry(VaultEntryType.PUMP_CGM_PREDICTION, TimestampUtils.createCleanTimestamp("2010.01.08-00:05", "yyyy.MM.dd-HH:mm"), 135.0));
        data.add(new VaultEntry(VaultEntryType.GLUCOSE_ELEVATION_30, TimestampUtils.createCleanTimestamp("2010.01.08-00:05", "yyyy.MM.dd-HH:mm"), -41.0));
        data.add(new VaultEntry(VaultEntryType.STRESS, TimestampUtils.createCleanTimestamp("2010.01.08-00:06", "yyyy.MM.dd-HH:mm"), 64.75));
        data.add(new VaultEntry(VaultEntryType.HEART_RATE_VARIABILITY, TimestampUtils.createCleanTimestamp("2010.01.08-00:06", "yyyy.MM.dd-HH:mm"), 24.0));
        data.add(new VaultEntry(VaultEntryType.HEART_RATE, TimestampUtils.createCleanTimestamp("2010.01.08-00:10", "yyyy.MM.dd-HH:mm"), 85.0));

        expectedResult = data;

        GapRemoverPreprocessorOption gapRemoverPreprocessorOption = new GapRemoverPreprocessorOption(VaultEntryType.GLUCOSE_CGM, 5);
        instance = new GapRemover(gapRemoverPreprocessorOption);

        System.out.println("testData_TwoMatchingEntries_RestInRange");
        actualResult = instance.preprocess(data);

        for (VaultEntry vaultEntry : expectedResult) {
            if (!actualResult.contains(vaultEntry)) {
                System.out.println(vaultEntry.toString());
            }
        }

        assertEquals(expectedResult, actualResult);
    }

    @Test
    public void testData_MultipleMatchingEntries_GapInMiddle_MiddleCGMTooLate() throws ParseException {
        data.add(new VaultEntry(VaultEntryType.BASAL_PROFILE, TimestampUtils.createCleanTimestamp("2010.01.08-00:00", "yyyy.MM.dd-HH:mm"), 0.65));
        data.add(new VaultEntry(VaultEntryType.GLUCOSE_CGM, TimestampUtils.createCleanTimestamp("2010.01.08-00:00", "yyyy.MM.dd-HH:mm"), 151.0));
        data.add(new VaultEntry(VaultEntryType.GLUCOSE_CGM_RAW, TimestampUtils.createCleanTimestamp("2010.01.08-00:00", "yyyy.MM.dd-HH:mm"), 30.0));
        data.add(new VaultEntry(VaultEntryType.PUMP_CGM_PREDICTION, TimestampUtils.createCleanTimestamp("2010.01.08-00:00", "yyyy.MM.dd-HH:mm"), 140.0));
        data.add(new VaultEntry(VaultEntryType.GLUCOSE_ELEVATION_30, TimestampUtils.createCleanTimestamp("2010.01.08-00:00", "yyyy.MM.dd-HH:mm"), -32.0));
        data.add(new VaultEntry(VaultEntryType.GLUCOSE_BG, TimestampUtils.createCleanTimestamp("2010.01.08-00:03", "yyyy.MM.dd-HH:mm"), 125.0));
        data.add(new VaultEntry(VaultEntryType.GLUCOSE_BOLUS_CALCULATION, TimestampUtils.createCleanTimestamp("2010.01.08-00:04", "yyyy.MM.dd-HH:mm"), 125.0));
        data.add(new VaultEntry(VaultEntryType.BOLUS_NORMAL, TimestampUtils.createCleanTimestamp("2010.01.08-00:04", "yyyy.MM.dd-HH:mm"), 0.3));
        data.add(new VaultEntry(VaultEntryType.GLUCOSE_CGM, TimestampUtils.createCleanTimestamp("2010.01.08-00:05", "yyyy.MM.dd-HH:mm"), 144.0));
        data.add(new VaultEntry(VaultEntryType.GLUCOSE_CGM_RAW, TimestampUtils.createCleanTimestamp("2010.01.08-00:05", "yyyy.MM.dd-HH:mm"), 29.0));
        data.add(new VaultEntry(VaultEntryType.PUMP_CGM_PREDICTION, TimestampUtils.createCleanTimestamp("2010.01.08-00:05", "yyyy.MM.dd-HH:mm"), 135.0));
        data.add(new VaultEntry(VaultEntryType.GLUCOSE_ELEVATION_30, TimestampUtils.createCleanTimestamp("2010.01.08-00:05", "yyyy.MM.dd-HH:mm"), -41.0));
        data.add(new VaultEntry(VaultEntryType.STRESS, TimestampUtils.createCleanTimestamp("2010.01.08-00:06", "yyyy.MM.dd-HH:mm"), 64.75));
        data.add(new VaultEntry(VaultEntryType.HEART_RATE_VARIABILITY, TimestampUtils.createCleanTimestamp("2010.01.08-00:06", "yyyy.MM.dd-HH:mm"), 24.0));
        data.add(new VaultEntry(VaultEntryType.HEART_RATE, TimestampUtils.createCleanTimestamp("2010.01.08-00:06", "yyyy.MM.dd-HH:mm"), 85.0));
        data.add(new VaultEntry(VaultEntryType.GLUCOSE_CGM_RAW, TimestampUtils.createCleanTimestamp("2010.01.08-00:11", "yyyy.MM.dd-HH:mm"), 28.09));
        data.add(new VaultEntry(VaultEntryType.GLUCOSE_CGM, TimestampUtils.createCleanTimestamp("2010.01.08-00:11", "yyyy.MM.dd-HH:mm"), 139.0));
        data.add(new VaultEntry(VaultEntryType.PUMP_CGM_PREDICTION, TimestampUtils.createCleanTimestamp("2010.01.08-00:11", "yyyy.MM.dd-HH:mm"), 126.0));
        data.add(new VaultEntry(VaultEntryType.GLUCOSE_ELEVATION_30, TimestampUtils.createCleanTimestamp("2010.01.08-00:11", "yyyy.MM.dd-HH:mm"), -42.0));
        data.add(new VaultEntry(VaultEntryType.PUMP_CGM_PREDICTION, TimestampUtils.createCleanTimestamp("2010.01.08-00:15", "yyyy.MM.dd-HH:mm"), 114.0));
        data.add(new VaultEntry(VaultEntryType.GLUCOSE_CGM_RAW, TimestampUtils.createCleanTimestamp("2010.01.08-00:15", "yyyy.MM.dd-HH:mm"), 27.38));
        data.add(new VaultEntry(VaultEntryType.GLUCOSE_CGM, TimestampUtils.createCleanTimestamp("2010.01.08-00:15", "yyyy.MM.dd-HH:mm"), 126.0));
        data.add(new VaultEntry(VaultEntryType.GLUCOSE_CGM_CALIBRATION, TimestampUtils.createCleanTimestamp("2010.01.08-00:15", "yyyy.MM.dd-HH:mm"), 125.0));
        data.add(new VaultEntry(VaultEntryType.GLUCOSE_ELEVATION_30, TimestampUtils.createCleanTimestamp("2010.01.08-00:15", "yyyy.MM.dd-HH:mm"), -44.0));
        data.add(new VaultEntry(VaultEntryType.STRESS, TimestampUtils.createCleanTimestamp("2010.01.08-00:16", "yyyy.MM.dd-HH:mm"), 77.5));
        data.add(new VaultEntry(VaultEntryType.HEART_RATE_VARIABILITY, TimestampUtils.createCleanTimestamp("2010.01.08-00:16", "yyyy.MM.dd-HH:mm"), 24.0));
        data.add(new VaultEntry(VaultEntryType.HEART_RATE, TimestampUtils.createCleanTimestamp("2010.01.08-00:16", "yyyy.MM.dd-HH:mm"), 63.0));
        data.add(new VaultEntry(VaultEntryType.PUMP_CGM_PREDICTION, TimestampUtils.createCleanTimestamp("2010.01.08-00:20", "yyyy.MM.dd-HH:mm"), 108.0));
        data.add(new VaultEntry(VaultEntryType.GLUCOSE_ELEVATION_30, TimestampUtils.createCleanTimestamp("2010.01.08-00:20", "yyyy.MM.dd-HH:mm"), -46.0));
        data.add(new VaultEntry(VaultEntryType.GLUCOSE_CGM, TimestampUtils.createCleanTimestamp("2010.01.08-00:20", "yyyy.MM.dd-HH:mm"), 122.0));

        expectedResult = new ArrayList<>();
        expectedResult.add(new VaultEntry(VaultEntryType.BASAL_PROFILE, TimestampUtils.createCleanTimestamp("2010.01.08-00:00", "yyyy.MM.dd-HH:mm"), 0.65));
        expectedResult.add(new VaultEntry(VaultEntryType.GLUCOSE_CGM, TimestampUtils.createCleanTimestamp("2010.01.08-00:00", "yyyy.MM.dd-HH:mm"), 151.0));
        expectedResult.add(new VaultEntry(VaultEntryType.GLUCOSE_CGM_RAW, TimestampUtils.createCleanTimestamp("2010.01.08-00:00", "yyyy.MM.dd-HH:mm"), 30.0));
        expectedResult.add(new VaultEntry(VaultEntryType.PUMP_CGM_PREDICTION, TimestampUtils.createCleanTimestamp("2010.01.08-00:00", "yyyy.MM.dd-HH:mm"), 140.0));
        expectedResult.add(new VaultEntry(VaultEntryType.GLUCOSE_ELEVATION_30, TimestampUtils.createCleanTimestamp("2010.01.08-00:00", "yyyy.MM.dd-HH:mm"), -32.0));
        expectedResult.add(new VaultEntry(VaultEntryType.GLUCOSE_BG, TimestampUtils.createCleanTimestamp("2010.01.08-00:03", "yyyy.MM.dd-HH:mm"), 125.0));
        expectedResult.add(new VaultEntry(VaultEntryType.GLUCOSE_BOLUS_CALCULATION, TimestampUtils.createCleanTimestamp("2010.01.08-00:04", "yyyy.MM.dd-HH:mm"), 125.0));
        expectedResult.add(new VaultEntry(VaultEntryType.BOLUS_NORMAL, TimestampUtils.createCleanTimestamp("2010.01.08-00:04", "yyyy.MM.dd-HH:mm"), 0.3));
        expectedResult.add(new VaultEntry(VaultEntryType.GLUCOSE_CGM, TimestampUtils.createCleanTimestamp("2010.01.08-00:05", "yyyy.MM.dd-HH:mm"), 144.0));
        expectedResult.add(new VaultEntry(VaultEntryType.GLUCOSE_CGM_RAW, TimestampUtils.createCleanTimestamp("2010.01.08-00:05", "yyyy.MM.dd-HH:mm"), 29.0));
        expectedResult.add(new VaultEntry(VaultEntryType.PUMP_CGM_PREDICTION, TimestampUtils.createCleanTimestamp("2010.01.08-00:05", "yyyy.MM.dd-HH:mm"), 135.0));
        expectedResult.add(new VaultEntry(VaultEntryType.GLUCOSE_ELEVATION_30, TimestampUtils.createCleanTimestamp("2010.01.08-00:05", "yyyy.MM.dd-HH:mm"), -41.0));
        //--Gap--
        expectedResult.add(new VaultEntry(VaultEntryType.GLUCOSE_CGM_RAW, TimestampUtils.createCleanTimestamp("2010.01.08-00:11", "yyyy.MM.dd-HH:mm"), 28.09));
        expectedResult.add(new VaultEntry(VaultEntryType.GLUCOSE_CGM, TimestampUtils.createCleanTimestamp("2010.01.08-00:11", "yyyy.MM.dd-HH:mm"), 139.0));
        expectedResult.add(new VaultEntry(VaultEntryType.PUMP_CGM_PREDICTION, TimestampUtils.createCleanTimestamp("2010.01.08-00:11", "yyyy.MM.dd-HH:mm"), 126.0));
        expectedResult.add(new VaultEntry(VaultEntryType.GLUCOSE_ELEVATION_30, TimestampUtils.createCleanTimestamp("2010.01.08-00:11", "yyyy.MM.dd-HH:mm"), -42.0));
        expectedResult.add(new VaultEntry(VaultEntryType.PUMP_CGM_PREDICTION, TimestampUtils.createCleanTimestamp("2010.01.08-00:15", "yyyy.MM.dd-HH:mm"), 114.0));
        expectedResult.add(new VaultEntry(VaultEntryType.GLUCOSE_CGM_RAW, TimestampUtils.createCleanTimestamp("2010.01.08-00:15", "yyyy.MM.dd-HH:mm"), 27.38));
        expectedResult.add(new VaultEntry(VaultEntryType.GLUCOSE_CGM, TimestampUtils.createCleanTimestamp("2010.01.08-00:15", "yyyy.MM.dd-HH:mm"), 126.0));
        expectedResult.add(new VaultEntry(VaultEntryType.GLUCOSE_CGM_CALIBRATION, TimestampUtils.createCleanTimestamp("2010.01.08-00:15", "yyyy.MM.dd-HH:mm"), 125.0));
        expectedResult.add(new VaultEntry(VaultEntryType.GLUCOSE_ELEVATION_30, TimestampUtils.createCleanTimestamp("2010.01.08-00:15", "yyyy.MM.dd-HH:mm"), -44.0));
        expectedResult.add(new VaultEntry(VaultEntryType.STRESS, TimestampUtils.createCleanTimestamp("2010.01.08-00:16", "yyyy.MM.dd-HH:mm"), 77.5));
        expectedResult.add(new VaultEntry(VaultEntryType.HEART_RATE_VARIABILITY, TimestampUtils.createCleanTimestamp("2010.01.08-00:16", "yyyy.MM.dd-HH:mm"), 24.0));
        expectedResult.add(new VaultEntry(VaultEntryType.HEART_RATE, TimestampUtils.createCleanTimestamp("2010.01.08-00:16", "yyyy.MM.dd-HH:mm"), 63.0));
        expectedResult.add(new VaultEntry(VaultEntryType.PUMP_CGM_PREDICTION, TimestampUtils.createCleanTimestamp("2010.01.08-00:20", "yyyy.MM.dd-HH:mm"), 108.0));
        expectedResult.add(new VaultEntry(VaultEntryType.GLUCOSE_ELEVATION_30, TimestampUtils.createCleanTimestamp("2010.01.08-00:20", "yyyy.MM.dd-HH:mm"), -46.0));
        expectedResult.add(new VaultEntry(VaultEntryType.GLUCOSE_CGM, TimestampUtils.createCleanTimestamp("2010.01.08-00:20", "yyyy.MM.dd-HH:mm"), 122.0));

        GapRemoverPreprocessorOption gapRemoverPreprocessorOption = new GapRemoverPreprocessorOption(VaultEntryType.GLUCOSE_CGM, 5);
        instance = new GapRemover(gapRemoverPreprocessorOption);

        System.out.println("testData_MultipleMatchingEntries_GapInMiddle_MiddleCGMTooLate");
        actualResult = instance.preprocess(data);

        for (VaultEntry vaultEntry : expectedResult) {
            if (!actualResult.contains(vaultEntry)) {
                System.out.println(vaultEntry.toString());
            }
        }

        assertEquals(expectedResult, actualResult);
    }

    @Test
    public void testData_MultipleMatchingEntries_TwoGapsInMiddle_Successive() throws ParseException {
        data.add(new VaultEntry(VaultEntryType.BASAL_PROFILE, TimestampUtils.createCleanTimestamp("2010.01.08-00:00", "yyyy.MM.dd-HH:mm"), 0.65));
        data.add(new VaultEntry(VaultEntryType.GLUCOSE_CGM, TimestampUtils.createCleanTimestamp("2010.01.08-00:00", "yyyy.MM.dd-HH:mm"), 151.0));
        data.add(new VaultEntry(VaultEntryType.GLUCOSE_CGM_RAW, TimestampUtils.createCleanTimestamp("2010.01.08-00:00", "yyyy.MM.dd-HH:mm"), 30.0));
        data.add(new VaultEntry(VaultEntryType.PUMP_CGM_PREDICTION, TimestampUtils.createCleanTimestamp("2010.01.08-00:00", "yyyy.MM.dd-HH:mm"), 140.0));
        data.add(new VaultEntry(VaultEntryType.GLUCOSE_ELEVATION_30, TimestampUtils.createCleanTimestamp("2010.01.08-00:00", "yyyy.MM.dd-HH:mm"), -32.0));
        data.add(new VaultEntry(VaultEntryType.GLUCOSE_BG, TimestampUtils.createCleanTimestamp("2010.01.08-00:03", "yyyy.MM.dd-HH:mm"), 125.0));
        data.add(new VaultEntry(VaultEntryType.GLUCOSE_BOLUS_CALCULATION, TimestampUtils.createCleanTimestamp("2010.01.08-00:04", "yyyy.MM.dd-HH:mm"), 125.0));
        data.add(new VaultEntry(VaultEntryType.BOLUS_NORMAL, TimestampUtils.createCleanTimestamp("2010.01.08-00:04", "yyyy.MM.dd-HH:mm"), 0.3));
        data.add(new VaultEntry(VaultEntryType.GLUCOSE_CGM, TimestampUtils.createCleanTimestamp("2010.01.08-00:05", "yyyy.MM.dd-HH:mm"), 144.0));
        data.add(new VaultEntry(VaultEntryType.GLUCOSE_CGM_RAW, TimestampUtils.createCleanTimestamp("2010.01.08-00:05", "yyyy.MM.dd-HH:mm"), 29.0));
        data.add(new VaultEntry(VaultEntryType.PUMP_CGM_PREDICTION, TimestampUtils.createCleanTimestamp("2010.01.08-00:05", "yyyy.MM.dd-HH:mm"), 135.0));
        data.add(new VaultEntry(VaultEntryType.GLUCOSE_ELEVATION_30, TimestampUtils.createCleanTimestamp("2010.01.08-00:05", "yyyy.MM.dd-HH:mm"), -41.0));
        data.add(new VaultEntry(VaultEntryType.STRESS, TimestampUtils.createCleanTimestamp("2010.01.08-00:06", "yyyy.MM.dd-HH:mm"), 64.75));
        data.add(new VaultEntry(VaultEntryType.HEART_RATE_VARIABILITY, TimestampUtils.createCleanTimestamp("2010.01.08-00:06", "yyyy.MM.dd-HH:mm"), 24.0));
        data.add(new VaultEntry(VaultEntryType.HEART_RATE, TimestampUtils.createCleanTimestamp("2010.01.08-00:06", "yyyy.MM.dd-HH:mm"), 85.0));
        data.add(new VaultEntry(VaultEntryType.GLUCOSE_CGM_RAW, TimestampUtils.createCleanTimestamp("2010.01.08-00:11", "yyyy.MM.dd-HH:mm"), 28.09));
        data.add(new VaultEntry(VaultEntryType.GLUCOSE_CGM, TimestampUtils.createCleanTimestamp("2010.01.08-00:11", "yyyy.MM.dd-HH:mm"), 139.0));
        data.add(new VaultEntry(VaultEntryType.PUMP_CGM_PREDICTION, TimestampUtils.createCleanTimestamp("2010.01.08-00:11", "yyyy.MM.dd-HH:mm"), 126.0));
        data.add(new VaultEntry(VaultEntryType.GLUCOSE_ELEVATION_30, TimestampUtils.createCleanTimestamp("2010.01.08-00:11", "yyyy.MM.dd-HH:mm"), -42.0));
        data.add(new VaultEntry(VaultEntryType.PUMP_CGM_PREDICTION, TimestampUtils.createCleanTimestamp("2010.01.08-00:15", "yyyy.MM.dd-HH:mm"), 114.0));
        data.add(new VaultEntry(VaultEntryType.GLUCOSE_CGM_RAW, TimestampUtils.createCleanTimestamp("2010.01.08-00:15", "yyyy.MM.dd-HH:mm"), 27.38));
        data.add(new VaultEntry(VaultEntryType.GLUCOSE_CGM, TimestampUtils.createCleanTimestamp("2010.01.08-00:17", "yyyy.MM.dd-HH:mm"), 126.0));
        data.add(new VaultEntry(VaultEntryType.GLUCOSE_CGM_CALIBRATION, TimestampUtils.createCleanTimestamp("2010.01.08-00:17", "yyyy.MM.dd-HH:mm"), 125.0));
        data.add(new VaultEntry(VaultEntryType.GLUCOSE_ELEVATION_30, TimestampUtils.createCleanTimestamp("2010.01.08-00:17", "yyyy.MM.dd-HH:mm"), -44.0));
        data.add(new VaultEntry(VaultEntryType.STRESS, TimestampUtils.createCleanTimestamp("2010.01.08-00:17", "yyyy.MM.dd-HH:mm"), 77.5));
        data.add(new VaultEntry(VaultEntryType.HEART_RATE_VARIABILITY, TimestampUtils.createCleanTimestamp("2010.01.08-00:17", "yyyy.MM.dd-HH:mm"), 24.0));
        data.add(new VaultEntry(VaultEntryType.HEART_RATE, TimestampUtils.createCleanTimestamp("2010.01.08-00:17", "yyyy.MM.dd-HH:mm"), 63.0));
        data.add(new VaultEntry(VaultEntryType.PUMP_CGM_PREDICTION, TimestampUtils.createCleanTimestamp("2010.01.08-00:20", "yyyy.MM.dd-HH:mm"), 108.0));
        data.add(new VaultEntry(VaultEntryType.GLUCOSE_ELEVATION_30, TimestampUtils.createCleanTimestamp("2010.01.08-00:20", "yyyy.MM.dd-HH:mm"), -46.0));
        data.add(new VaultEntry(VaultEntryType.GLUCOSE_CGM, TimestampUtils.createCleanTimestamp("2010.01.08-00:20", "yyyy.MM.dd-HH:mm"), 122.0));

        expectedResult = new ArrayList<>();
        expectedResult.add(new VaultEntry(VaultEntryType.BASAL_PROFILE, TimestampUtils.createCleanTimestamp("2010.01.08-00:00", "yyyy.MM.dd-HH:mm"), 0.65));
        expectedResult.add(new VaultEntry(VaultEntryType.GLUCOSE_CGM, TimestampUtils.createCleanTimestamp("2010.01.08-00:00", "yyyy.MM.dd-HH:mm"), 151.0));
        expectedResult.add(new VaultEntry(VaultEntryType.GLUCOSE_CGM_RAW, TimestampUtils.createCleanTimestamp("2010.01.08-00:00", "yyyy.MM.dd-HH:mm"), 30.0));
        expectedResult.add(new VaultEntry(VaultEntryType.PUMP_CGM_PREDICTION, TimestampUtils.createCleanTimestamp("2010.01.08-00:00", "yyyy.MM.dd-HH:mm"), 140.0));
        expectedResult.add(new VaultEntry(VaultEntryType.GLUCOSE_ELEVATION_30, TimestampUtils.createCleanTimestamp("2010.01.08-00:00", "yyyy.MM.dd-HH:mm"), -32.0));
        expectedResult.add(new VaultEntry(VaultEntryType.GLUCOSE_BG, TimestampUtils.createCleanTimestamp("2010.01.08-00:03", "yyyy.MM.dd-HH:mm"), 125.0));
        expectedResult.add(new VaultEntry(VaultEntryType.GLUCOSE_BOLUS_CALCULATION, TimestampUtils.createCleanTimestamp("2010.01.08-00:04", "yyyy.MM.dd-HH:mm"), 125.0));
        expectedResult.add(new VaultEntry(VaultEntryType.BOLUS_NORMAL, TimestampUtils.createCleanTimestamp("2010.01.08-00:04", "yyyy.MM.dd-HH:mm"), 0.3));
        expectedResult.add(new VaultEntry(VaultEntryType.GLUCOSE_CGM, TimestampUtils.createCleanTimestamp("2010.01.08-00:05", "yyyy.MM.dd-HH:mm"), 144.0));
        expectedResult.add(new VaultEntry(VaultEntryType.GLUCOSE_CGM_RAW, TimestampUtils.createCleanTimestamp("2010.01.08-00:05", "yyyy.MM.dd-HH:mm"), 29.0));
        expectedResult.add(new VaultEntry(VaultEntryType.PUMP_CGM_PREDICTION, TimestampUtils.createCleanTimestamp("2010.01.08-00:05", "yyyy.MM.dd-HH:mm"), 135.0));
        expectedResult.add(new VaultEntry(VaultEntryType.GLUCOSE_ELEVATION_30, TimestampUtils.createCleanTimestamp("2010.01.08-00:05", "yyyy.MM.dd-HH:mm"), -41.0));
        //--2 Gaps--
        expectedResult.add(new VaultEntry(VaultEntryType.GLUCOSE_CGM, TimestampUtils.createCleanTimestamp("2010.01.08-00:17", "yyyy.MM.dd-HH:mm"), 126.0));
        expectedResult.add(new VaultEntry(VaultEntryType.GLUCOSE_CGM_CALIBRATION, TimestampUtils.createCleanTimestamp("2010.01.08-00:17", "yyyy.MM.dd-HH:mm"), 125.0));
        expectedResult.add(new VaultEntry(VaultEntryType.GLUCOSE_ELEVATION_30, TimestampUtils.createCleanTimestamp("2010.01.08-00:17", "yyyy.MM.dd-HH:mm"), -44.0));
        expectedResult.add(new VaultEntry(VaultEntryType.STRESS, TimestampUtils.createCleanTimestamp("2010.01.08-00:17", "yyyy.MM.dd-HH:mm"), 77.5));
        expectedResult.add(new VaultEntry(VaultEntryType.HEART_RATE_VARIABILITY, TimestampUtils.createCleanTimestamp("2010.01.08-00:17", "yyyy.MM.dd-HH:mm"), 24.0));
        expectedResult.add(new VaultEntry(VaultEntryType.HEART_RATE, TimestampUtils.createCleanTimestamp("2010.01.08-00:17", "yyyy.MM.dd-HH:mm"), 63.0));
        expectedResult.add(new VaultEntry(VaultEntryType.PUMP_CGM_PREDICTION, TimestampUtils.createCleanTimestamp("2010.01.08-00:20", "yyyy.MM.dd-HH:mm"), 108.0));
        expectedResult.add(new VaultEntry(VaultEntryType.GLUCOSE_ELEVATION_30, TimestampUtils.createCleanTimestamp("2010.01.08-00:20", "yyyy.MM.dd-HH:mm"), -46.0));
        expectedResult.add(new VaultEntry(VaultEntryType.GLUCOSE_CGM, TimestampUtils.createCleanTimestamp("2010.01.08-00:20", "yyyy.MM.dd-HH:mm"), 122.0));

        GapRemoverPreprocessorOption gapRemoverPreprocessorOption = new GapRemoverPreprocessorOption(VaultEntryType.GLUCOSE_CGM, 5);
        instance = new GapRemover(gapRemoverPreprocessorOption);

        System.out.println("testData_MultipleMatchingEntries_TwoGapsInMiddle_Successive");
        actualResult = instance.preprocess(data);

        for (VaultEntry vaultEntry : expectedResult) {
            if (!actualResult.contains(vaultEntry)) {
                System.out.println(vaultEntry.toString());
            }
        }

        assertEquals(expectedResult, actualResult);
    }

    @Test
    public void testData_MultipleMatchingEntries_TwoGapsInMiddle_Scattered() throws ParseException {
        data.add(new VaultEntry(VaultEntryType.BASAL_PROFILE, TimestampUtils.createCleanTimestamp("2010.01.08-00:00", "yyyy.MM.dd-HH:mm"), 0.65));
        data.add(new VaultEntry(VaultEntryType.GLUCOSE_CGM, TimestampUtils.createCleanTimestamp("2010.01.08-00:00", "yyyy.MM.dd-HH:mm"), 151.0));
        data.add(new VaultEntry(VaultEntryType.GLUCOSE_CGM_RAW, TimestampUtils.createCleanTimestamp("2010.01.08-00:00", "yyyy.MM.dd-HH:mm"), 30.0));
        data.add(new VaultEntry(VaultEntryType.PUMP_CGM_PREDICTION, TimestampUtils.createCleanTimestamp("2010.01.08-00:00", "yyyy.MM.dd-HH:mm"), 140.0));
        data.add(new VaultEntry(VaultEntryType.GLUCOSE_ELEVATION_30, TimestampUtils.createCleanTimestamp("2010.01.08-00:00", "yyyy.MM.dd-HH:mm"), -32.0));
        data.add(new VaultEntry(VaultEntryType.GLUCOSE_BG, TimestampUtils.createCleanTimestamp("2010.01.08-00:03", "yyyy.MM.dd-HH:mm"), 125.0));
        data.add(new VaultEntry(VaultEntryType.GLUCOSE_BOLUS_CALCULATION, TimestampUtils.createCleanTimestamp("2010.01.08-00:04", "yyyy.MM.dd-HH:mm"), 125.0));
        data.add(new VaultEntry(VaultEntryType.BOLUS_NORMAL, TimestampUtils.createCleanTimestamp("2010.01.08-00:04", "yyyy.MM.dd-HH:mm"), 0.3));
        data.add(new VaultEntry(VaultEntryType.GLUCOSE_CGM, TimestampUtils.createCleanTimestamp("2010.01.08-00:05", "yyyy.MM.dd-HH:mm"), 144.0));
        data.add(new VaultEntry(VaultEntryType.GLUCOSE_CGM_RAW, TimestampUtils.createCleanTimestamp("2010.01.08-00:05", "yyyy.MM.dd-HH:mm"), 29.0));
        data.add(new VaultEntry(VaultEntryType.PUMP_CGM_PREDICTION, TimestampUtils.createCleanTimestamp("2010.01.08-00:05", "yyyy.MM.dd-HH:mm"), 135.0));
        data.add(new VaultEntry(VaultEntryType.GLUCOSE_ELEVATION_30, TimestampUtils.createCleanTimestamp("2010.01.08-00:05", "yyyy.MM.dd-HH:mm"), -41.0));
        data.add(new VaultEntry(VaultEntryType.STRESS, TimestampUtils.createCleanTimestamp("2010.01.08-00:06", "yyyy.MM.dd-HH:mm"), 64.75));
        data.add(new VaultEntry(VaultEntryType.HEART_RATE_VARIABILITY, TimestampUtils.createCleanTimestamp("2010.01.08-00:06", "yyyy.MM.dd-HH:mm"), 24.0));
        data.add(new VaultEntry(VaultEntryType.HEART_RATE, TimestampUtils.createCleanTimestamp("2010.01.08-00:06", "yyyy.MM.dd-HH:mm"), 85.0));
        data.add(new VaultEntry(VaultEntryType.GLUCOSE_CGM_RAW, TimestampUtils.createCleanTimestamp("2010.01.08-00:11", "yyyy.MM.dd-HH:mm"), 28.09));
        data.add(new VaultEntry(VaultEntryType.GLUCOSE_CGM, TimestampUtils.createCleanTimestamp("2010.01.08-00:11", "yyyy.MM.dd-HH:mm"), 139.0));
        data.add(new VaultEntry(VaultEntryType.PUMP_CGM_PREDICTION, TimestampUtils.createCleanTimestamp("2010.01.08-00:11", "yyyy.MM.dd-HH:mm"), 126.0));
        data.add(new VaultEntry(VaultEntryType.GLUCOSE_ELEVATION_30, TimestampUtils.createCleanTimestamp("2010.01.08-00:11", "yyyy.MM.dd-HH:mm"), -42.0));
        data.add(new VaultEntry(VaultEntryType.PUMP_CGM_PREDICTION, TimestampUtils.createCleanTimestamp("2010.01.08-00:12", "yyyy.MM.dd-HH:mm"), 114.0));
        data.add(new VaultEntry(VaultEntryType.GLUCOSE_CGM_RAW, TimestampUtils.createCleanTimestamp("2010.01.08-00:12", "yyyy.MM.dd-HH:mm"), 27.38));
        data.add(new VaultEntry(VaultEntryType.GLUCOSE_CGM, TimestampUtils.createCleanTimestamp("2010.01.08-00:14", "yyyy.MM.dd-HH:mm"), 126.0));
        data.add(new VaultEntry(VaultEntryType.GLUCOSE_CGM_CALIBRATION, TimestampUtils.createCleanTimestamp("2010.01.08-00:15", "yyyy.MM.dd-HH:mm"), 125.0));
        data.add(new VaultEntry(VaultEntryType.GLUCOSE_ELEVATION_30, TimestampUtils.createCleanTimestamp("2010.01.08-00:15", "yyyy.MM.dd-HH:mm"), -44.0));
        data.add(new VaultEntry(VaultEntryType.STRESS, TimestampUtils.createCleanTimestamp("2010.01.08-00:16", "yyyy.MM.dd-HH:mm"), 77.5));
        data.add(new VaultEntry(VaultEntryType.HEART_RATE_VARIABILITY, TimestampUtils.createCleanTimestamp("2010.01.08-00:16", "yyyy.MM.dd-HH:mm"), 24.0));
        data.add(new VaultEntry(VaultEntryType.HEART_RATE, TimestampUtils.createCleanTimestamp("2010.01.08-00:16", "yyyy.MM.dd-HH:mm"), 63.0));
        data.add(new VaultEntry(VaultEntryType.PUMP_CGM_PREDICTION, TimestampUtils.createCleanTimestamp("2010.01.08-00:20", "yyyy.MM.dd-HH:mm"), 108.0));
        data.add(new VaultEntry(VaultEntryType.GLUCOSE_ELEVATION_30, TimestampUtils.createCleanTimestamp("2010.01.08-00:20", "yyyy.MM.dd-HH:mm"), -46.0));
        data.add(new VaultEntry(VaultEntryType.GLUCOSE_CGM, TimestampUtils.createCleanTimestamp("2010.01.08-00:20", "yyyy.MM.dd-HH:mm"), 122.0));
        data.add(new VaultEntry(VaultEntryType.GLUCOSE_CGM_RAW, TimestampUtils.createCleanTimestamp("2010.01.08-00:20", "yyyy.MM.dd-HH:mm"), 26.62));
        data.add(new VaultEntry(VaultEntryType.GLUCOSE_ELEVATION_30, TimestampUtils.createCleanTimestamp("2010.01.08-00:25", "yyyy.MM.dd-HH:mm"), -48.0));
        data.add(new VaultEntry(VaultEntryType.BASAL_INTERPRETER, TimestampUtils.createCleanTimestamp("2010.01.08-00:25", "yyyy.MM.dd-HH:mm"), 0.0));
        data.add(new VaultEntry(VaultEntryType.PUMP_SUSPEND, TimestampUtils.createCleanTimestamp("2010.01.08-00:25", "yyyy.MM.dd-HH:mm"), -5.0));
        data.add(new VaultEntry(VaultEntryType.PUMP_AUTONOMOUS_SUSPEND, TimestampUtils.createCleanTimestamp("2010.01.08-00:25", "yyyy.MM.dd-HH:mm"), -5.0));
        data.add(new VaultEntry(VaultEntryType.GLUCOSE_CGM_ALERT, TimestampUtils.createCleanTimestamp("2010.01.08-00:25", "yyyy.MM.dd-HH:mm"), 113.0));
        data.add(new VaultEntry(VaultEntryType.PUMP_CGM_PREDICTION, TimestampUtils.createCleanTimestamp("2010.01.08-00:25", "yyyy.MM.dd-HH:mm"), 90.0));
        data.add(new VaultEntry(VaultEntryType.GLUCOSE_CGM_RAW, TimestampUtils.createCleanTimestamp("2010.01.08-00:25", "yyyy.MM.dd-HH:mm"), 24.41));
        data.add(new VaultEntry(VaultEntryType.GLUCOSE_CGM, TimestampUtils.createCleanTimestamp("2010.01.08-00:25", "yyyy.MM.dd-HH:mm"), 113.0));

        expectedResult = new ArrayList<>();
        expectedResult.add(new VaultEntry(VaultEntryType.BASAL_PROFILE, TimestampUtils.createCleanTimestamp("2010.01.08-00:00", "yyyy.MM.dd-HH:mm"), 0.65));
        expectedResult.add(new VaultEntry(VaultEntryType.GLUCOSE_CGM, TimestampUtils.createCleanTimestamp("2010.01.08-00:00", "yyyy.MM.dd-HH:mm"), 151.0));
        expectedResult.add(new VaultEntry(VaultEntryType.GLUCOSE_CGM_RAW, TimestampUtils.createCleanTimestamp("2010.01.08-00:00", "yyyy.MM.dd-HH:mm"), 30.0));
        expectedResult.add(new VaultEntry(VaultEntryType.PUMP_CGM_PREDICTION, TimestampUtils.createCleanTimestamp("2010.01.08-00:00", "yyyy.MM.dd-HH:mm"), 140.0));
        expectedResult.add(new VaultEntry(VaultEntryType.GLUCOSE_ELEVATION_30, TimestampUtils.createCleanTimestamp("2010.01.08-00:00", "yyyy.MM.dd-HH:mm"), -32.0));
        expectedResult.add(new VaultEntry(VaultEntryType.GLUCOSE_BG, TimestampUtils.createCleanTimestamp("2010.01.08-00:03", "yyyy.MM.dd-HH:mm"), 125.0));
        expectedResult.add(new VaultEntry(VaultEntryType.GLUCOSE_BOLUS_CALCULATION, TimestampUtils.createCleanTimestamp("2010.01.08-00:04", "yyyy.MM.dd-HH:mm"), 125.0));
        expectedResult.add(new VaultEntry(VaultEntryType.BOLUS_NORMAL, TimestampUtils.createCleanTimestamp("2010.01.08-00:04", "yyyy.MM.dd-HH:mm"), 0.3));
        expectedResult.add(new VaultEntry(VaultEntryType.GLUCOSE_CGM, TimestampUtils.createCleanTimestamp("2010.01.08-00:05", "yyyy.MM.dd-HH:mm"), 144.0));
        expectedResult.add(new VaultEntry(VaultEntryType.GLUCOSE_CGM_RAW, TimestampUtils.createCleanTimestamp("2010.01.08-00:05", "yyyy.MM.dd-HH:mm"), 29.0));
        expectedResult.add(new VaultEntry(VaultEntryType.PUMP_CGM_PREDICTION, TimestampUtils.createCleanTimestamp("2010.01.08-00:05", "yyyy.MM.dd-HH:mm"), 135.0));
        expectedResult.add(new VaultEntry(VaultEntryType.GLUCOSE_ELEVATION_30, TimestampUtils.createCleanTimestamp("2010.01.08-00:05", "yyyy.MM.dd-HH:mm"), -41.0));
        //--First Gap--
        expectedResult.add(new VaultEntry(VaultEntryType.GLUCOSE_CGM_RAW, TimestampUtils.createCleanTimestamp("2010.01.08-00:11", "yyyy.MM.dd-HH:mm"), 28.09));
        expectedResult.add(new VaultEntry(VaultEntryType.GLUCOSE_CGM, TimestampUtils.createCleanTimestamp("2010.01.08-00:11", "yyyy.MM.dd-HH:mm"), 139.0));
        expectedResult.add(new VaultEntry(VaultEntryType.PUMP_CGM_PREDICTION, TimestampUtils.createCleanTimestamp("2010.01.08-00:11", "yyyy.MM.dd-HH:mm"), 126.0));
        expectedResult.add(new VaultEntry(VaultEntryType.GLUCOSE_ELEVATION_30, TimestampUtils.createCleanTimestamp("2010.01.08-00:11", "yyyy.MM.dd-HH:mm"), -42.0));
        expectedResult.add(new VaultEntry(VaultEntryType.PUMP_CGM_PREDICTION, TimestampUtils.createCleanTimestamp("2010.01.08-00:12", "yyyy.MM.dd-HH:mm"), 114.0));
        expectedResult.add(new VaultEntry(VaultEntryType.GLUCOSE_CGM_RAW, TimestampUtils.createCleanTimestamp("2010.01.08-00:12", "yyyy.MM.dd-HH:mm"), 27.38));
        expectedResult.add(new VaultEntry(VaultEntryType.GLUCOSE_CGM, TimestampUtils.createCleanTimestamp("2010.01.08-00:14", "yyyy.MM.dd-HH:mm"), 126.0));
        //--Second Gap--
        expectedResult.add(new VaultEntry(VaultEntryType.PUMP_CGM_PREDICTION, TimestampUtils.createCleanTimestamp("2010.01.08-00:20", "yyyy.MM.dd-HH:mm"), 108.0));
        expectedResult.add(new VaultEntry(VaultEntryType.GLUCOSE_ELEVATION_30, TimestampUtils.createCleanTimestamp("2010.01.08-00:20", "yyyy.MM.dd-HH:mm"), -46.0));
        expectedResult.add(new VaultEntry(VaultEntryType.GLUCOSE_CGM, TimestampUtils.createCleanTimestamp("2010.01.08-00:20", "yyyy.MM.dd-HH:mm"), 122.0));
        expectedResult.add(new VaultEntry(VaultEntryType.GLUCOSE_CGM_RAW, TimestampUtils.createCleanTimestamp("2010.01.08-00:20", "yyyy.MM.dd-HH:mm"), 26.62));
        expectedResult.add(new VaultEntry(VaultEntryType.GLUCOSE_ELEVATION_30, TimestampUtils.createCleanTimestamp("2010.01.08-00:25", "yyyy.MM.dd-HH:mm"), -48.0));
        expectedResult.add(new VaultEntry(VaultEntryType.BASAL_INTERPRETER, TimestampUtils.createCleanTimestamp("2010.01.08-00:25", "yyyy.MM.dd-HH:mm"), 0.0));
        expectedResult.add(new VaultEntry(VaultEntryType.PUMP_SUSPEND, TimestampUtils.createCleanTimestamp("2010.01.08-00:25", "yyyy.MM.dd-HH:mm"), -5.0));
        expectedResult.add(new VaultEntry(VaultEntryType.PUMP_AUTONOMOUS_SUSPEND, TimestampUtils.createCleanTimestamp("2010.01.08-00:25", "yyyy.MM.dd-HH:mm"), -5.0));
        expectedResult.add(new VaultEntry(VaultEntryType.GLUCOSE_CGM_ALERT, TimestampUtils.createCleanTimestamp("2010.01.08-00:25", "yyyy.MM.dd-HH:mm"), 113.0));
        expectedResult.add(new VaultEntry(VaultEntryType.PUMP_CGM_PREDICTION, TimestampUtils.createCleanTimestamp("2010.01.08-00:25", "yyyy.MM.dd-HH:mm"), 90.0));
        expectedResult.add(new VaultEntry(VaultEntryType.GLUCOSE_CGM_RAW, TimestampUtils.createCleanTimestamp("2010.01.08-00:25", "yyyy.MM.dd-HH:mm"), 24.41));
        expectedResult.add(new VaultEntry(VaultEntryType.GLUCOSE_CGM, TimestampUtils.createCleanTimestamp("2010.01.08-00:25", "yyyy.MM.dd-HH:mm"), 113.0));

        GapRemoverPreprocessorOption gapRemoverPreprocessorOption = new GapRemoverPreprocessorOption(VaultEntryType.GLUCOSE_CGM, 5);
        instance = new GapRemover(gapRemoverPreprocessorOption);

        System.out.println("testData_MultipleMatchingEntries_TwoGapsInMiddle_Successive");
        actualResult = instance.preprocess(data);

        for (VaultEntry vaultEntry : expectedResult) {
            if (!actualResult.contains(vaultEntry)) {
                System.out.println(vaultEntry.toString());
            }
        }

        assertEquals(expectedResult, actualResult);
    }

    @Test
    public void testData_MultipleMatchingEntries_TwoGapsInMiddle_Successive_WithoutDataInSecond() throws ParseException {
        data.add(new VaultEntry(VaultEntryType.BASAL_PROFILE, TimestampUtils.createCleanTimestamp("2010.01.08-00:00", "yyyy.MM.dd-HH:mm"), 0.65));
        data.add(new VaultEntry(VaultEntryType.GLUCOSE_CGM, TimestampUtils.createCleanTimestamp("2010.01.08-00:00", "yyyy.MM.dd-HH:mm"), 151.0));
        data.add(new VaultEntry(VaultEntryType.GLUCOSE_CGM_RAW, TimestampUtils.createCleanTimestamp("2010.01.08-00:00", "yyyy.MM.dd-HH:mm"), 30.0));
        data.add(new VaultEntry(VaultEntryType.PUMP_CGM_PREDICTION, TimestampUtils.createCleanTimestamp("2010.01.08-00:00", "yyyy.MM.dd-HH:mm"), 140.0));
        data.add(new VaultEntry(VaultEntryType.GLUCOSE_ELEVATION_30, TimestampUtils.createCleanTimestamp("2010.01.08-00:00", "yyyy.MM.dd-HH:mm"), -32.0));
        data.add(new VaultEntry(VaultEntryType.GLUCOSE_BG, TimestampUtils.createCleanTimestamp("2010.01.08-00:03", "yyyy.MM.dd-HH:mm"), 125.0));
        data.add(new VaultEntry(VaultEntryType.GLUCOSE_BOLUS_CALCULATION, TimestampUtils.createCleanTimestamp("2010.01.08-00:04", "yyyy.MM.dd-HH:mm"), 125.0));
        data.add(new VaultEntry(VaultEntryType.BOLUS_NORMAL, TimestampUtils.createCleanTimestamp("2010.01.08-00:04", "yyyy.MM.dd-HH:mm"), 0.3));
        data.add(new VaultEntry(VaultEntryType.GLUCOSE_CGM, TimestampUtils.createCleanTimestamp("2010.01.08-00:05", "yyyy.MM.dd-HH:mm"), 144.0));
        data.add(new VaultEntry(VaultEntryType.GLUCOSE_CGM_RAW, TimestampUtils.createCleanTimestamp("2010.01.08-00:05", "yyyy.MM.dd-HH:mm"), 29.0));
        data.add(new VaultEntry(VaultEntryType.PUMP_CGM_PREDICTION, TimestampUtils.createCleanTimestamp("2010.01.08-00:05", "yyyy.MM.dd-HH:mm"), 135.0));
        data.add(new VaultEntry(VaultEntryType.GLUCOSE_ELEVATION_30, TimestampUtils.createCleanTimestamp("2010.01.08-00:05", "yyyy.MM.dd-HH:mm"), -41.0));
        data.add(new VaultEntry(VaultEntryType.STRESS, TimestampUtils.createCleanTimestamp("2010.01.08-00:06", "yyyy.MM.dd-HH:mm"), 64.75));
        data.add(new VaultEntry(VaultEntryType.HEART_RATE_VARIABILITY, TimestampUtils.createCleanTimestamp("2010.01.08-00:06", "yyyy.MM.dd-HH:mm"), 24.0));
        data.add(new VaultEntry(VaultEntryType.HEART_RATE, TimestampUtils.createCleanTimestamp("2010.01.08-00:06", "yyyy.MM.dd-HH:mm"), 85.0));
        data.add(new VaultEntry(VaultEntryType.GLUCOSE_CGM_RAW, TimestampUtils.createCleanTimestamp("2010.01.08-00:11", "yyyy.MM.dd-HH:mm"), 28.09));
        data.add(new VaultEntry(VaultEntryType.GLUCOSE_CGM, TimestampUtils.createCleanTimestamp("2010.01.08-00:11", "yyyy.MM.dd-HH:mm"), 139.0));
//--no data here
        data.add(new VaultEntry(VaultEntryType.GLUCOSE_CGM, TimestampUtils.createCleanTimestamp("2010.01.08-00:17", "yyyy.MM.dd-HH:mm"), 126.0));
        data.add(new VaultEntry(VaultEntryType.GLUCOSE_CGM_CALIBRATION, TimestampUtils.createCleanTimestamp("2010.01.08-00:17", "yyyy.MM.dd-HH:mm"), 125.0));
        data.add(new VaultEntry(VaultEntryType.GLUCOSE_ELEVATION_30, TimestampUtils.createCleanTimestamp("2010.01.08-00:17", "yyyy.MM.dd-HH:mm"), -44.0));
        data.add(new VaultEntry(VaultEntryType.STRESS, TimestampUtils.createCleanTimestamp("2010.01.08-00:17", "yyyy.MM.dd-HH:mm"), 77.5));
        data.add(new VaultEntry(VaultEntryType.HEART_RATE_VARIABILITY, TimestampUtils.createCleanTimestamp("2010.01.08-00:17", "yyyy.MM.dd-HH:mm"), 24.0));
        data.add(new VaultEntry(VaultEntryType.HEART_RATE, TimestampUtils.createCleanTimestamp("2010.01.08-00:17", "yyyy.MM.dd-HH:mm"), 63.0));
        data.add(new VaultEntry(VaultEntryType.PUMP_CGM_PREDICTION, TimestampUtils.createCleanTimestamp("2010.01.08-00:20", "yyyy.MM.dd-HH:mm"), 108.0));
        data.add(new VaultEntry(VaultEntryType.GLUCOSE_ELEVATION_30, TimestampUtils.createCleanTimestamp("2010.01.08-00:20", "yyyy.MM.dd-HH:mm"), -46.0));
        data.add(new VaultEntry(VaultEntryType.GLUCOSE_CGM, TimestampUtils.createCleanTimestamp("2010.01.08-00:20", "yyyy.MM.dd-HH:mm"), 122.0));

        expectedResult = new ArrayList<>();
        expectedResult.add(new VaultEntry(VaultEntryType.BASAL_PROFILE, TimestampUtils.createCleanTimestamp("2010.01.08-00:00", "yyyy.MM.dd-HH:mm"), 0.65));
        expectedResult.add(new VaultEntry(VaultEntryType.GLUCOSE_CGM, TimestampUtils.createCleanTimestamp("2010.01.08-00:00", "yyyy.MM.dd-HH:mm"), 151.0));
        expectedResult.add(new VaultEntry(VaultEntryType.GLUCOSE_CGM_RAW, TimestampUtils.createCleanTimestamp("2010.01.08-00:00", "yyyy.MM.dd-HH:mm"), 30.0));
        expectedResult.add(new VaultEntry(VaultEntryType.PUMP_CGM_PREDICTION, TimestampUtils.createCleanTimestamp("2010.01.08-00:00", "yyyy.MM.dd-HH:mm"), 140.0));
        expectedResult.add(new VaultEntry(VaultEntryType.GLUCOSE_ELEVATION_30, TimestampUtils.createCleanTimestamp("2010.01.08-00:00", "yyyy.MM.dd-HH:mm"), -32.0));
        expectedResult.add(new VaultEntry(VaultEntryType.GLUCOSE_BG, TimestampUtils.createCleanTimestamp("2010.01.08-00:03", "yyyy.MM.dd-HH:mm"), 125.0));
        expectedResult.add(new VaultEntry(VaultEntryType.GLUCOSE_BOLUS_CALCULATION, TimestampUtils.createCleanTimestamp("2010.01.08-00:04", "yyyy.MM.dd-HH:mm"), 125.0));
        expectedResult.add(new VaultEntry(VaultEntryType.BOLUS_NORMAL, TimestampUtils.createCleanTimestamp("2010.01.08-00:04", "yyyy.MM.dd-HH:mm"), 0.3));
        expectedResult.add(new VaultEntry(VaultEntryType.GLUCOSE_CGM, TimestampUtils.createCleanTimestamp("2010.01.08-00:05", "yyyy.MM.dd-HH:mm"), 144.0));
        expectedResult.add(new VaultEntry(VaultEntryType.GLUCOSE_CGM_RAW, TimestampUtils.createCleanTimestamp("2010.01.08-00:05", "yyyy.MM.dd-HH:mm"), 29.0));
        expectedResult.add(new VaultEntry(VaultEntryType.PUMP_CGM_PREDICTION, TimestampUtils.createCleanTimestamp("2010.01.08-00:05", "yyyy.MM.dd-HH:mm"), 135.0));
        expectedResult.add(new VaultEntry(VaultEntryType.GLUCOSE_ELEVATION_30, TimestampUtils.createCleanTimestamp("2010.01.08-00:05", "yyyy.MM.dd-HH:mm"), -41.0));
        //--2 Gaps--
        expectedResult.add(new VaultEntry(VaultEntryType.GLUCOSE_CGM, TimestampUtils.createCleanTimestamp("2010.01.08-00:17", "yyyy.MM.dd-HH:mm"), 126.0));
        expectedResult.add(new VaultEntry(VaultEntryType.GLUCOSE_CGM_CALIBRATION, TimestampUtils.createCleanTimestamp("2010.01.08-00:17", "yyyy.MM.dd-HH:mm"), 125.0));
        expectedResult.add(new VaultEntry(VaultEntryType.GLUCOSE_ELEVATION_30, TimestampUtils.createCleanTimestamp("2010.01.08-00:17", "yyyy.MM.dd-HH:mm"), -44.0));
        expectedResult.add(new VaultEntry(VaultEntryType.STRESS, TimestampUtils.createCleanTimestamp("2010.01.08-00:17", "yyyy.MM.dd-HH:mm"), 77.5));
        expectedResult.add(new VaultEntry(VaultEntryType.HEART_RATE_VARIABILITY, TimestampUtils.createCleanTimestamp("2010.01.08-00:17", "yyyy.MM.dd-HH:mm"), 24.0));
        expectedResult.add(new VaultEntry(VaultEntryType.HEART_RATE, TimestampUtils.createCleanTimestamp("2010.01.08-00:17", "yyyy.MM.dd-HH:mm"), 63.0));
        expectedResult.add(new VaultEntry(VaultEntryType.PUMP_CGM_PREDICTION, TimestampUtils.createCleanTimestamp("2010.01.08-00:20", "yyyy.MM.dd-HH:mm"), 108.0));
        expectedResult.add(new VaultEntry(VaultEntryType.GLUCOSE_ELEVATION_30, TimestampUtils.createCleanTimestamp("2010.01.08-00:20", "yyyy.MM.dd-HH:mm"), -46.0));
        expectedResult.add(new VaultEntry(VaultEntryType.GLUCOSE_CGM, TimestampUtils.createCleanTimestamp("2010.01.08-00:20", "yyyy.MM.dd-HH:mm"), 122.0));

        GapRemoverPreprocessorOption gapRemoverPreprocessorOption = new GapRemoverPreprocessorOption(VaultEntryType.GLUCOSE_CGM, 5);
        instance = new GapRemover(gapRemoverPreprocessorOption);

        System.out.println("testData_MultipleMatchingEntries_TwoGapsInMiddle_Successive_WithoutDataInSecond");
        actualResult = instance.preprocess(data);

        for (VaultEntry vaultEntry : expectedResult) {
            if (!actualResult.contains(vaultEntry)) {
                System.out.println(vaultEntry.toString());
            }
        }

        assertEquals(expectedResult, actualResult);
    }

    @Test
    public void testData_MultipleMatchingEntries_TwoGapsInMiddle_Successive_WithoutDataInFirst() throws ParseException {
        data.add(new VaultEntry(VaultEntryType.BASAL_PROFILE, TimestampUtils.createCleanTimestamp("2010.01.08-00:00", "yyyy.MM.dd-HH:mm"), 0.65));
        data.add(new VaultEntry(VaultEntryType.GLUCOSE_CGM, TimestampUtils.createCleanTimestamp("2010.01.08-00:00", "yyyy.MM.dd-HH:mm"), 151.0));
        data.add(new VaultEntry(VaultEntryType.GLUCOSE_CGM_RAW, TimestampUtils.createCleanTimestamp("2010.01.08-00:00", "yyyy.MM.dd-HH:mm"), 30.0));
        data.add(new VaultEntry(VaultEntryType.PUMP_CGM_PREDICTION, TimestampUtils.createCleanTimestamp("2010.01.08-00:00", "yyyy.MM.dd-HH:mm"), 140.0));
        data.add(new VaultEntry(VaultEntryType.GLUCOSE_ELEVATION_30, TimestampUtils.createCleanTimestamp("2010.01.08-00:00", "yyyy.MM.dd-HH:mm"), -32.0));
        data.add(new VaultEntry(VaultEntryType.GLUCOSE_BG, TimestampUtils.createCleanTimestamp("2010.01.08-00:03", "yyyy.MM.dd-HH:mm"), 125.0));
        data.add(new VaultEntry(VaultEntryType.GLUCOSE_BOLUS_CALCULATION, TimestampUtils.createCleanTimestamp("2010.01.08-00:04", "yyyy.MM.dd-HH:mm"), 125.0));
        data.add(new VaultEntry(VaultEntryType.BOLUS_NORMAL, TimestampUtils.createCleanTimestamp("2010.01.08-00:04", "yyyy.MM.dd-HH:mm"), 0.3));
        data.add(new VaultEntry(VaultEntryType.GLUCOSE_CGM, TimestampUtils.createCleanTimestamp("2010.01.08-00:05", "yyyy.MM.dd-HH:mm"), 144.0));
//--no data here
        data.add(new VaultEntry(VaultEntryType.GLUCOSE_CGM, TimestampUtils.createCleanTimestamp("2010.01.08-00:11", "yyyy.MM.dd-HH:mm"), 139.0));
        data.add(new VaultEntry(VaultEntryType.PUMP_CGM_PREDICTION, TimestampUtils.createCleanTimestamp("2010.01.08-00:11", "yyyy.MM.dd-HH:mm"), 126.0));
        data.add(new VaultEntry(VaultEntryType.GLUCOSE_ELEVATION_30, TimestampUtils.createCleanTimestamp("2010.01.08-00:11", "yyyy.MM.dd-HH:mm"), -42.0));
        data.add(new VaultEntry(VaultEntryType.PUMP_CGM_PREDICTION, TimestampUtils.createCleanTimestamp("2010.01.08-00:15", "yyyy.MM.dd-HH:mm"), 114.0));
        data.add(new VaultEntry(VaultEntryType.GLUCOSE_CGM_RAW, TimestampUtils.createCleanTimestamp("2010.01.08-00:15", "yyyy.MM.dd-HH:mm"), 27.38));
        data.add(new VaultEntry(VaultEntryType.GLUCOSE_CGM, TimestampUtils.createCleanTimestamp("2010.01.08-00:17", "yyyy.MM.dd-HH:mm"), 126.0));
        data.add(new VaultEntry(VaultEntryType.GLUCOSE_CGM_CALIBRATION, TimestampUtils.createCleanTimestamp("2010.01.08-00:17", "yyyy.MM.dd-HH:mm"), 125.0));
        data.add(new VaultEntry(VaultEntryType.GLUCOSE_ELEVATION_30, TimestampUtils.createCleanTimestamp("2010.01.08-00:17", "yyyy.MM.dd-HH:mm"), -44.0));
        data.add(new VaultEntry(VaultEntryType.STRESS, TimestampUtils.createCleanTimestamp("2010.01.08-00:17", "yyyy.MM.dd-HH:mm"), 77.5));
        data.add(new VaultEntry(VaultEntryType.HEART_RATE_VARIABILITY, TimestampUtils.createCleanTimestamp("2010.01.08-00:17", "yyyy.MM.dd-HH:mm"), 24.0));
        data.add(new VaultEntry(VaultEntryType.HEART_RATE, TimestampUtils.createCleanTimestamp("2010.01.08-00:17", "yyyy.MM.dd-HH:mm"), 63.0));
        data.add(new VaultEntry(VaultEntryType.PUMP_CGM_PREDICTION, TimestampUtils.createCleanTimestamp("2010.01.08-00:20", "yyyy.MM.dd-HH:mm"), 108.0));
        data.add(new VaultEntry(VaultEntryType.GLUCOSE_ELEVATION_30, TimestampUtils.createCleanTimestamp("2010.01.08-00:20", "yyyy.MM.dd-HH:mm"), -46.0));
        data.add(new VaultEntry(VaultEntryType.GLUCOSE_CGM, TimestampUtils.createCleanTimestamp("2010.01.08-00:20", "yyyy.MM.dd-HH:mm"), 122.0));

        expectedResult = new ArrayList<>();
        expectedResult.add(new VaultEntry(VaultEntryType.BASAL_PROFILE, TimestampUtils.createCleanTimestamp("2010.01.08-00:00", "yyyy.MM.dd-HH:mm"), 0.65));
        expectedResult.add(new VaultEntry(VaultEntryType.GLUCOSE_CGM, TimestampUtils.createCleanTimestamp("2010.01.08-00:00", "yyyy.MM.dd-HH:mm"), 151.0));
        expectedResult.add(new VaultEntry(VaultEntryType.GLUCOSE_CGM_RAW, TimestampUtils.createCleanTimestamp("2010.01.08-00:00", "yyyy.MM.dd-HH:mm"), 30.0));
        expectedResult.add(new VaultEntry(VaultEntryType.PUMP_CGM_PREDICTION, TimestampUtils.createCleanTimestamp("2010.01.08-00:00", "yyyy.MM.dd-HH:mm"), 140.0));
        expectedResult.add(new VaultEntry(VaultEntryType.GLUCOSE_ELEVATION_30, TimestampUtils.createCleanTimestamp("2010.01.08-00:00", "yyyy.MM.dd-HH:mm"), -32.0));
        expectedResult.add(new VaultEntry(VaultEntryType.GLUCOSE_BG, TimestampUtils.createCleanTimestamp("2010.01.08-00:03", "yyyy.MM.dd-HH:mm"), 125.0));
        expectedResult.add(new VaultEntry(VaultEntryType.GLUCOSE_BOLUS_CALCULATION, TimestampUtils.createCleanTimestamp("2010.01.08-00:04", "yyyy.MM.dd-HH:mm"), 125.0));
        expectedResult.add(new VaultEntry(VaultEntryType.BOLUS_NORMAL, TimestampUtils.createCleanTimestamp("2010.01.08-00:04", "yyyy.MM.dd-HH:mm"), 0.3));
        expectedResult.add(new VaultEntry(VaultEntryType.GLUCOSE_CGM, TimestampUtils.createCleanTimestamp("2010.01.08-00:05", "yyyy.MM.dd-HH:mm"), 144.0));
        //--2 Gaps--
        expectedResult.add(new VaultEntry(VaultEntryType.GLUCOSE_CGM, TimestampUtils.createCleanTimestamp("2010.01.08-00:17", "yyyy.MM.dd-HH:mm"), 126.0));
        expectedResult.add(new VaultEntry(VaultEntryType.GLUCOSE_CGM_CALIBRATION, TimestampUtils.createCleanTimestamp("2010.01.08-00:17", "yyyy.MM.dd-HH:mm"), 125.0));
        expectedResult.add(new VaultEntry(VaultEntryType.GLUCOSE_ELEVATION_30, TimestampUtils.createCleanTimestamp("2010.01.08-00:17", "yyyy.MM.dd-HH:mm"), -44.0));
        expectedResult.add(new VaultEntry(VaultEntryType.STRESS, TimestampUtils.createCleanTimestamp("2010.01.08-00:17", "yyyy.MM.dd-HH:mm"), 77.5));
        expectedResult.add(new VaultEntry(VaultEntryType.HEART_RATE_VARIABILITY, TimestampUtils.createCleanTimestamp("2010.01.08-00:17", "yyyy.MM.dd-HH:mm"), 24.0));
        expectedResult.add(new VaultEntry(VaultEntryType.HEART_RATE, TimestampUtils.createCleanTimestamp("2010.01.08-00:17", "yyyy.MM.dd-HH:mm"), 63.0));
        expectedResult.add(new VaultEntry(VaultEntryType.PUMP_CGM_PREDICTION, TimestampUtils.createCleanTimestamp("2010.01.08-00:20", "yyyy.MM.dd-HH:mm"), 108.0));
        expectedResult.add(new VaultEntry(VaultEntryType.GLUCOSE_ELEVATION_30, TimestampUtils.createCleanTimestamp("2010.01.08-00:20", "yyyy.MM.dd-HH:mm"), -46.0));
        expectedResult.add(new VaultEntry(VaultEntryType.GLUCOSE_CGM, TimestampUtils.createCleanTimestamp("2010.01.08-00:20", "yyyy.MM.dd-HH:mm"), 122.0));

        GapRemoverPreprocessorOption gapRemoverPreprocessorOption = new GapRemoverPreprocessorOption(VaultEntryType.GLUCOSE_CGM, 5);
        instance = new GapRemover(gapRemoverPreprocessorOption);

        System.out.println("testData_MultipleMatchingEntries_TwoGapsInMiddle_Successive_WithoutDataInFirst");
        actualResult = instance.preprocess(data);

        for (VaultEntry vaultEntry : expectedResult) {
            if (!actualResult.contains(vaultEntry)) {
                System.out.println(vaultEntry.toString());
            }
        }

        assertEquals(expectedResult, actualResult);
    }

    @Test
    public void testData_MultipleMatchingEntries_GapInMiddle_WithoutData() throws ParseException {
        data.add(new VaultEntry(VaultEntryType.BASAL_PROFILE, TimestampUtils.createCleanTimestamp("2010.01.08-00:00", "yyyy.MM.dd-HH:mm"), 0.65));
        data.add(new VaultEntry(VaultEntryType.GLUCOSE_CGM, TimestampUtils.createCleanTimestamp("2010.01.08-00:00", "yyyy.MM.dd-HH:mm"), 151.0));
        data.add(new VaultEntry(VaultEntryType.GLUCOSE_CGM_RAW, TimestampUtils.createCleanTimestamp("2010.01.08-00:00", "yyyy.MM.dd-HH:mm"), 30.0));
        data.add(new VaultEntry(VaultEntryType.PUMP_CGM_PREDICTION, TimestampUtils.createCleanTimestamp("2010.01.08-00:00", "yyyy.MM.dd-HH:mm"), 140.0));
        data.add(new VaultEntry(VaultEntryType.GLUCOSE_ELEVATION_30, TimestampUtils.createCleanTimestamp("2010.01.08-00:00", "yyyy.MM.dd-HH:mm"), -32.0));
        data.add(new VaultEntry(VaultEntryType.GLUCOSE_BG, TimestampUtils.createCleanTimestamp("2010.01.08-00:03", "yyyy.MM.dd-HH:mm"), 125.0));
        data.add(new VaultEntry(VaultEntryType.GLUCOSE_BOLUS_CALCULATION, TimestampUtils.createCleanTimestamp("2010.01.08-00:04", "yyyy.MM.dd-HH:mm"), 125.0));
        data.add(new VaultEntry(VaultEntryType.BOLUS_NORMAL, TimestampUtils.createCleanTimestamp("2010.01.08-00:04", "yyyy.MM.dd-HH:mm"), 0.3));
        data.add(new VaultEntry(VaultEntryType.GLUCOSE_CGM, TimestampUtils.createCleanTimestamp("2010.01.08-00:05", "yyyy.MM.dd-HH:mm"), 144.0));
        //--no data here--
        data.add(new VaultEntry(VaultEntryType.GLUCOSE_CGM, TimestampUtils.createCleanTimestamp("2010.01.08-00:11", "yyyy.MM.dd-HH:mm"), 139.0));
        data.add(new VaultEntry(VaultEntryType.PUMP_CGM_PREDICTION, TimestampUtils.createCleanTimestamp("2010.01.08-00:11", "yyyy.MM.dd-HH:mm"), 126.0));
        data.add(new VaultEntry(VaultEntryType.GLUCOSE_ELEVATION_30, TimestampUtils.createCleanTimestamp("2010.01.08-00:11", "yyyy.MM.dd-HH:mm"), -42.0));
        data.add(new VaultEntry(VaultEntryType.PUMP_CGM_PREDICTION, TimestampUtils.createCleanTimestamp("2010.01.08-00:15", "yyyy.MM.dd-HH:mm"), 114.0));
        data.add(new VaultEntry(VaultEntryType.GLUCOSE_CGM_RAW, TimestampUtils.createCleanTimestamp("2010.01.08-00:15", "yyyy.MM.dd-HH:mm"), 27.38));
        data.add(new VaultEntry(VaultEntryType.GLUCOSE_CGM, TimestampUtils.createCleanTimestamp("2010.01.08-00:15", "yyyy.MM.dd-HH:mm"), 126.0));
        data.add(new VaultEntry(VaultEntryType.GLUCOSE_CGM_CALIBRATION, TimestampUtils.createCleanTimestamp("2010.01.08-00:15", "yyyy.MM.dd-HH:mm"), 125.0));
        data.add(new VaultEntry(VaultEntryType.GLUCOSE_ELEVATION_30, TimestampUtils.createCleanTimestamp("2010.01.08-00:15", "yyyy.MM.dd-HH:mm"), -44.0));
        data.add(new VaultEntry(VaultEntryType.STRESS, TimestampUtils.createCleanTimestamp("2010.01.08-00:16", "yyyy.MM.dd-HH:mm"), 77.5));
        data.add(new VaultEntry(VaultEntryType.HEART_RATE_VARIABILITY, TimestampUtils.createCleanTimestamp("2010.01.08-00:16", "yyyy.MM.dd-HH:mm"), 24.0));
        data.add(new VaultEntry(VaultEntryType.HEART_RATE, TimestampUtils.createCleanTimestamp("2010.01.08-00:16", "yyyy.MM.dd-HH:mm"), 63.0));
        data.add(new VaultEntry(VaultEntryType.PUMP_CGM_PREDICTION, TimestampUtils.createCleanTimestamp("2010.01.08-00:20", "yyyy.MM.dd-HH:mm"), 108.0));
        data.add(new VaultEntry(VaultEntryType.GLUCOSE_ELEVATION_30, TimestampUtils.createCleanTimestamp("2010.01.08-00:20", "yyyy.MM.dd-HH:mm"), -46.0));
        data.add(new VaultEntry(VaultEntryType.GLUCOSE_CGM, TimestampUtils.createCleanTimestamp("2010.01.08-00:20", "yyyy.MM.dd-HH:mm"), 122.0));

        expectedResult = new ArrayList<>();
        expectedResult.add(new VaultEntry(VaultEntryType.BASAL_PROFILE, TimestampUtils.createCleanTimestamp("2010.01.08-00:00", "yyyy.MM.dd-HH:mm"), 0.65));
        expectedResult.add(new VaultEntry(VaultEntryType.GLUCOSE_CGM, TimestampUtils.createCleanTimestamp("2010.01.08-00:00", "yyyy.MM.dd-HH:mm"), 151.0));
        expectedResult.add(new VaultEntry(VaultEntryType.GLUCOSE_CGM_RAW, TimestampUtils.createCleanTimestamp("2010.01.08-00:00", "yyyy.MM.dd-HH:mm"), 30.0));
        expectedResult.add(new VaultEntry(VaultEntryType.PUMP_CGM_PREDICTION, TimestampUtils.createCleanTimestamp("2010.01.08-00:00", "yyyy.MM.dd-HH:mm"), 140.0));
        expectedResult.add(new VaultEntry(VaultEntryType.GLUCOSE_ELEVATION_30, TimestampUtils.createCleanTimestamp("2010.01.08-00:00", "yyyy.MM.dd-HH:mm"), -32.0));
        expectedResult.add(new VaultEntry(VaultEntryType.GLUCOSE_BG, TimestampUtils.createCleanTimestamp("2010.01.08-00:03", "yyyy.MM.dd-HH:mm"), 125.0));
        expectedResult.add(new VaultEntry(VaultEntryType.GLUCOSE_BOLUS_CALCULATION, TimestampUtils.createCleanTimestamp("2010.01.08-00:04", "yyyy.MM.dd-HH:mm"), 125.0));
        expectedResult.add(new VaultEntry(VaultEntryType.BOLUS_NORMAL, TimestampUtils.createCleanTimestamp("2010.01.08-00:04", "yyyy.MM.dd-HH:mm"), 0.3));
        expectedResult.add(new VaultEntry(VaultEntryType.GLUCOSE_CGM, TimestampUtils.createCleanTimestamp("2010.01.08-00:05", "yyyy.MM.dd-HH:mm"), 144.0));
        //--Gap--
        expectedResult.add(new VaultEntry(VaultEntryType.GLUCOSE_CGM, TimestampUtils.createCleanTimestamp("2010.01.08-00:11", "yyyy.MM.dd-HH:mm"), 139.0));
        expectedResult.add(new VaultEntry(VaultEntryType.PUMP_CGM_PREDICTION, TimestampUtils.createCleanTimestamp("2010.01.08-00:11", "yyyy.MM.dd-HH:mm"), 126.0));
        expectedResult.add(new VaultEntry(VaultEntryType.GLUCOSE_ELEVATION_30, TimestampUtils.createCleanTimestamp("2010.01.08-00:11", "yyyy.MM.dd-HH:mm"), -42.0));
        expectedResult.add(new VaultEntry(VaultEntryType.PUMP_CGM_PREDICTION, TimestampUtils.createCleanTimestamp("2010.01.08-00:15", "yyyy.MM.dd-HH:mm"), 114.0));
        expectedResult.add(new VaultEntry(VaultEntryType.GLUCOSE_CGM_RAW, TimestampUtils.createCleanTimestamp("2010.01.08-00:15", "yyyy.MM.dd-HH:mm"), 27.38));
        expectedResult.add(new VaultEntry(VaultEntryType.GLUCOSE_CGM, TimestampUtils.createCleanTimestamp("2010.01.08-00:15", "yyyy.MM.dd-HH:mm"), 126.0));
        expectedResult.add(new VaultEntry(VaultEntryType.GLUCOSE_CGM_CALIBRATION, TimestampUtils.createCleanTimestamp("2010.01.08-00:15", "yyyy.MM.dd-HH:mm"), 125.0));
        expectedResult.add(new VaultEntry(VaultEntryType.GLUCOSE_ELEVATION_30, TimestampUtils.createCleanTimestamp("2010.01.08-00:15", "yyyy.MM.dd-HH:mm"), -44.0));
        expectedResult.add(new VaultEntry(VaultEntryType.STRESS, TimestampUtils.createCleanTimestamp("2010.01.08-00:16", "yyyy.MM.dd-HH:mm"), 77.5));
        expectedResult.add(new VaultEntry(VaultEntryType.HEART_RATE_VARIABILITY, TimestampUtils.createCleanTimestamp("2010.01.08-00:16", "yyyy.MM.dd-HH:mm"), 24.0));
        expectedResult.add(new VaultEntry(VaultEntryType.HEART_RATE, TimestampUtils.createCleanTimestamp("2010.01.08-00:16", "yyyy.MM.dd-HH:mm"), 63.0));
        expectedResult.add(new VaultEntry(VaultEntryType.PUMP_CGM_PREDICTION, TimestampUtils.createCleanTimestamp("2010.01.08-00:20", "yyyy.MM.dd-HH:mm"), 108.0));
        expectedResult.add(new VaultEntry(VaultEntryType.GLUCOSE_ELEVATION_30, TimestampUtils.createCleanTimestamp("2010.01.08-00:20", "yyyy.MM.dd-HH:mm"), -46.0));
        expectedResult.add(new VaultEntry(VaultEntryType.GLUCOSE_CGM, TimestampUtils.createCleanTimestamp("2010.01.08-00:20", "yyyy.MM.dd-HH:mm"), 122.0));

        GapRemoverPreprocessorOption gapRemoverPreprocessorOption = new GapRemoverPreprocessorOption(VaultEntryType.GLUCOSE_CGM, 5);
        instance = new GapRemover(gapRemoverPreprocessorOption);

        System.out.println("testData_MultipleMatchingEntries_GapInMiddle_WithoutData");
        actualResult = instance.preprocess(data);

        for (VaultEntry vaultEntry : expectedResult) {
            if (!actualResult.contains(vaultEntry)) {
                System.out.println(vaultEntry.toString());
            }
        }

        assertEquals(expectedResult, actualResult);
    }

}
