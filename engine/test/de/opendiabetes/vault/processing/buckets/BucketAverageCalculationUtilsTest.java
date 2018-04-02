/*
 * Copyright (C) 2018 Chryat1s
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
package de.opendiabetes.vault.processing.buckets;

import de.opendiabetes.vault.container.BucketEntry;
import static de.opendiabetes.vault.container.BucketEntry.getNumberOfVaultEntryTriggerTypes;
import static de.opendiabetes.vault.container.BucketEventTriggers.ARRAY_ENTRIES_AFTER_MERGE_TO;
import static de.opendiabetes.vault.container.BucketEventTriggers.ARRAY_ENTRY_TRIGGER_HASHMAP;
import de.opendiabetes.vault.container.FinalBucketEntry;
import de.opendiabetes.vault.container.VaultEntry;
import de.opendiabetes.vault.container.VaultEntryType;
import de.opendiabetes.vault.processing.filter.TestFunctions;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;
import javafx.util.Pair;
import org.junit.After;
import org.junit.AfterClass;
import static org.junit.Assert.assertEquals;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

/**
 *
 * @author Chryat1s
 */
public class BucketAverageCalculationUtilsTest {

    BucketAverageCalculationUtils averageCalc;

    public BucketAverageCalculationUtilsTest() {
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

    /**
     * This test checks if calculateAverageForSmallestBucketSize correctly
     * creates the list of computed values for the FinalBucketEntry.
     *
     * @throws ParseException
     */
    @Test
    public void testSmallestSize() throws ParseException {
        averageCalc = new BucketAverageCalculationUtils();

        BucketEntry bucketEntry = new BucketEntry(0, new VaultEntry(VaultEntryType.EMPTY, TestFunctions.creatNewDateToCheckFor("2000.01.01-00:01")));

        // fill the bucket with information
        for (int i = 0; i < getNumberOfVaultEntryTriggerTypes(); i++) {

            if (i == ARRAY_ENTRY_TRIGGER_HASHMAP.get(VaultEntryType.BASAL_PROFILE)) {
                bucketEntry.setValues(i, 100.0);
            } else if (i == ARRAY_ENTRY_TRIGGER_HASHMAP.get(VaultEntryType.BASAL_INTERPRETER)) {
                bucketEntry.setValues(i, 100.0);
            } else if (i == ARRAY_ENTRY_TRIGGER_HASHMAP.get(VaultEntryType.BASAL_MANUAL)) {
                bucketEntry.setValues(i, 100.0);
            } else if (i == ARRAY_ENTRY_TRIGGER_HASHMAP.get(VaultEntryType.BOLUS_NORMAL)) {
                bucketEntry.setValues(i, 100.0);
            } else if (i == ARRAY_ENTRY_TRIGGER_HASHMAP.get(VaultEntryType.MEAL_BOLUS_CALCULATOR)) {
                bucketEntry.setValues(i, 100.0);
            } else if (i == ARRAY_ENTRY_TRIGGER_HASHMAP.get(VaultEntryType.MEAL_MANUAL)) {
                bucketEntry.setValues(i, 100.0);
            } else {
                bucketEntry.setValues(i, 0.0);
            }
        }

        Pair<VaultEntryType, Pair<Double, Double>> informationPair_1 = new Pair(VaultEntryType.BOLUS_SQARE, new Pair(1, 100.0));
        Pair<VaultEntryType, Pair<Double, Double>> informationPair_2 = new Pair(VaultEntryType.BOLUS_SQARE, new Pair(1, 100.0));
        // first double == timer
        // second double == value
        List<Pair<VaultEntryType, Pair<Double, Double>>> newValuesForRunningComputation = new ArrayList<>();
        newValuesForRunningComputation.add(informationPair_1);
        newValuesForRunningComputation.add(informationPair_2);
        bucketEntry.setValuesForRunningComputation(newValuesForRunningComputation);

        averageCalc.calculateAverageForSmallestBucketSize(bucketEntry);

        BucketEntry wantedBucketEntry = new BucketEntry(0, new VaultEntry(VaultEntryType.EMPTY, TestFunctions.creatNewDateToCheckFor("2000.01.01-00:01")));
        Pair<VaultEntryType, Double> pairMeal = new Pair(VaultEntryType.MEAL_BOLUS_CALCULATOR, 200.0);
        Pair<VaultEntryType, Double> pairBasal = new Pair(VaultEntryType.BASAL_PROFILE, 300.0);
        Pair<VaultEntryType, Double> pairBolus = new Pair(VaultEntryType.BOLUS_NORMAL, 300.0);
        List<Pair<VaultEntryType, Double>> newcomputedValuesForTheFinalBucketEntry = new ArrayList<>();
        newcomputedValuesForTheFinalBucketEntry.add(pairMeal);
        newcomputedValuesForTheFinalBucketEntry.add(pairBolus);
        newcomputedValuesForTheFinalBucketEntry.add(pairBasal);

        wantedBucketEntry.setComputedValuesForTheFinalBucketEntry(newcomputedValuesForTheFinalBucketEntry);

        // the output order may vary ... because of this the test my say false but the output is correct
        // for confirmation check the test error output
        assertEquals(wantedBucketEntry.getComputedValuesForTheFinalBucketEntry(), bucketEntry.getComputedValuesForTheFinalBucketEntry());
    }

    /**
     * This test checks if calculateAverageForSmallestBucketSize correctly
     * creates the FinalBucketEntrys.
     *
     * @throws ParseException
     */
    @Test
    public void testWantedSize() throws ParseException {
        averageCalc = new BucketAverageCalculationUtils();

        BucketEntry bucketEntry_1 = new BucketEntry(0, new VaultEntry(VaultEntryType.EMPTY, TestFunctions.creatNewDateToCheckFor("2000.01.01-00:01")));

        // fill the bucket with information
        for (int i = 0; i < getNumberOfVaultEntryTriggerTypes(); i++) {

            if (i == ARRAY_ENTRY_TRIGGER_HASHMAP.get(VaultEntryType.BASAL_PROFILE)) {
                bucketEntry_1.setValues(i, 100.0);
            } else if (i == ARRAY_ENTRY_TRIGGER_HASHMAP.get(VaultEntryType.BASAL_INTERPRETER)) {
                bucketEntry_1.setValues(i, 100.0);
            } else if (i == ARRAY_ENTRY_TRIGGER_HASHMAP.get(VaultEntryType.BASAL_MANUAL)) {
                bucketEntry_1.setValues(i, 100.0);
            } else if (i == ARRAY_ENTRY_TRIGGER_HASHMAP.get(VaultEntryType.BOLUS_NORMAL)) {
                bucketEntry_1.setValues(i, 100.0);
            } else if (i == ARRAY_ENTRY_TRIGGER_HASHMAP.get(VaultEntryType.MEAL_BOLUS_CALCULATOR)) {
                bucketEntry_1.setValues(i, 200.0);
            } else if (i == ARRAY_ENTRY_TRIGGER_HASHMAP.get(VaultEntryType.MEAL_MANUAL)) {
                bucketEntry_1.setValues(i, 100.0);
            } else {
                bucketEntry_1.setValues(i, 0.0);
            }
        }

        Pair<VaultEntryType, Pair<Double, Double>> informationPair_1 = new Pair(VaultEntryType.BOLUS_SQARE, new Pair(1, 100.0));
        Pair<VaultEntryType, Pair<Double, Double>> informationPair_2 = new Pair(VaultEntryType.BOLUS_SQARE, new Pair(1, 100.0));
        // first double == timer
        // second double == value
        List<Pair<VaultEntryType, Pair<Double, Double>>> newValuesForRunningComputation_1 = new ArrayList<>();
        newValuesForRunningComputation_1.add(informationPair_1);
        newValuesForRunningComputation_1.add(informationPair_2);
        bucketEntry_1.setValuesForRunningComputation(newValuesForRunningComputation_1);

        averageCalc.calculateAverageForSmallestBucketSize(bucketEntry_1);

        BucketEntry bucketEntry_2 = new BucketEntry(1, new VaultEntry(VaultEntryType.EMPTY, TestFunctions.creatNewDateToCheckFor("2000.01.01-00:02")));
        averageCalc.calculateAverageForSmallestBucketSize(bucketEntry_2);

        BucketEntry bucketEntry_3 = new BucketEntry(2, new VaultEntry(VaultEntryType.EMPTY, TestFunctions.creatNewDateToCheckFor("2000.01.01-00:03")));
        averageCalc.calculateAverageForSmallestBucketSize(bucketEntry_3);

        BucketEntry bucketEntry_4 = new BucketEntry(3, new VaultEntry(VaultEntryType.EMPTY, TestFunctions.creatNewDateToCheckFor("2000.01.01-00:04")));

        // fill the bucket with information
        for (int i = 0; i < getNumberOfVaultEntryTriggerTypes(); i++) {

            if (i == ARRAY_ENTRY_TRIGGER_HASHMAP.get(VaultEntryType.BASAL_PROFILE)) {
                bucketEntry_4.setValues(i, 100.0);
            } else if (i == ARRAY_ENTRY_TRIGGER_HASHMAP.get(VaultEntryType.BASAL_INTERPRETER)) {
                bucketEntry_4.setValues(i, 100.0);
            } else if (i == ARRAY_ENTRY_TRIGGER_HASHMAP.get(VaultEntryType.BASAL_MANUAL)) {
                bucketEntry_4.setValues(i, 100.0);
            } else if (i == ARRAY_ENTRY_TRIGGER_HASHMAP.get(VaultEntryType.BOLUS_NORMAL)) {
                bucketEntry_4.setValues(i, 100.0);
            } else if (i == ARRAY_ENTRY_TRIGGER_HASHMAP.get(VaultEntryType.MEAL_BOLUS_CALCULATOR)) {
                bucketEntry_4.setValues(i, 100.0);
            } else if (i == ARRAY_ENTRY_TRIGGER_HASHMAP.get(VaultEntryType.MEAL_MANUAL)) {
                bucketEntry_4.setValues(i, 200.0);
            } else {
                bucketEntry_4.setValues(i, 0.0);
            }
        }

        Pair<VaultEntryType, Pair<Double, Double>> informationPair_7 = new Pair(VaultEntryType.BOLUS_SQARE, new Pair(1, 100.0));
        Pair<VaultEntryType, Pair<Double, Double>> informationPair_8 = new Pair(VaultEntryType.BOLUS_SQARE, new Pair(1, 100.0));
        // first double == timer
        // second double == value
        List<Pair<VaultEntryType, Pair<Double, Double>>> newValuesForRunningComputation_4 = new ArrayList<>();
        newValuesForRunningComputation_4.add(informationPair_7);
        newValuesForRunningComputation_4.add(informationPair_8);
        bucketEntry_4.setValuesForRunningComputation(newValuesForRunningComputation_4);

        averageCalc.calculateAverageForSmallestBucketSize(bucketEntry_4);

        BucketEntry bucketEntry_5 = new BucketEntry(4, new VaultEntry(VaultEntryType.EMPTY, TestFunctions.creatNewDateToCheckFor("2000.01.01-00:05")));
        averageCalc.calculateAverageForSmallestBucketSize(bucketEntry_5);

        BucketEntry bucketEntry_6 = new BucketEntry(5, new VaultEntry(VaultEntryType.EMPTY, TestFunctions.creatNewDateToCheckFor("2000.01.01-00:06")));

        // fill the bucket with information
        for (int i = 0; i < getNumberOfVaultEntryTriggerTypes(); i++) {

            if (i == ARRAY_ENTRY_TRIGGER_HASHMAP.get(VaultEntryType.BASAL_PROFILE)) {
                bucketEntry_6.setValues(i, 100.0);
            } else if (i == ARRAY_ENTRY_TRIGGER_HASHMAP.get(VaultEntryType.BASAL_INTERPRETER)) {
                bucketEntry_6.setValues(i, 100.0);
            } else if (i == ARRAY_ENTRY_TRIGGER_HASHMAP.get(VaultEntryType.BASAL_MANUAL)) {
                bucketEntry_6.setValues(i, 100.0);
            } else if (i == ARRAY_ENTRY_TRIGGER_HASHMAP.get(VaultEntryType.BOLUS_NORMAL)) {
                bucketEntry_6.setValues(i, 100.0);
            } else if (i == ARRAY_ENTRY_TRIGGER_HASHMAP.get(VaultEntryType.MEAL_BOLUS_CALCULATOR)) {
                bucketEntry_6.setValues(i, 150.0);
            } else if (i == ARRAY_ENTRY_TRIGGER_HASHMAP.get(VaultEntryType.MEAL_MANUAL)) {
                bucketEntry_6.setValues(i, 150.0);
            } else {
                bucketEntry_6.setValues(i, 0.0);
            }
        }

        Pair<VaultEntryType, Pair<Double, Double>> informationPair_11 = new Pair(VaultEntryType.BOLUS_SQARE, new Pair(1, 100.0));
        Pair<VaultEntryType, Pair<Double, Double>> informationPair_12 = new Pair(VaultEntryType.BOLUS_SQARE, new Pair(1, 100.0));
        // first double == timer
        // second double == value
        List<Pair<VaultEntryType, Pair<Double, Double>>> newValuesForRunningComputation_6 = new ArrayList<>();
        newValuesForRunningComputation_6.add(informationPair_11);
        newValuesForRunningComputation_6.add(informationPair_12);
        bucketEntry_6.setValuesForRunningComputation(newValuesForRunningComputation_6);

        averageCalc.calculateAverageForSmallestBucketSize(bucketEntry_6);

        List<BucketEntry> bucketsToMerge_1 = new ArrayList<>();
        bucketsToMerge_1.add(bucketEntry_1);
        bucketsToMerge_1.add(bucketEntry_2);
        bucketsToMerge_1.add(bucketEntry_3);
        List<BucketEntry> bucketsToMerge_2 = new ArrayList<>();
        bucketsToMerge_2.add(bucketEntry_4);
        bucketsToMerge_2.add(bucketEntry_5);
        bucketsToMerge_2.add(bucketEntry_6);

        FinalBucketEntry result_1 = averageCalc.calculateAverageForWantedBucketSize(0, 3, bucketsToMerge_1);
        FinalBucketEntry result_2 = averageCalc.calculateAverageForWantedBucketSize(0, 3, bucketsToMerge_2);
        result_2.setBucketNumber(1);

        List<FinalBucketEntry> result = new ArrayList<>();
        result.add(result_1);
        result.add(result_2);

        FinalBucketEntry finalBucketEntry_1 = new FinalBucketEntry(0);
        // fill the bucket with information
        for (int i = 0; i < ARRAY_ENTRIES_AFTER_MERGE_TO.size(); i++) {

            if (i == ARRAY_ENTRIES_AFTER_MERGE_TO.get(VaultEntryType.BASAL_PROFILE)) {
                finalBucketEntry_1.setValues(i, 100.0);
            } else if (i == ARRAY_ENTRIES_AFTER_MERGE_TO.get(VaultEntryType.BOLUS_NORMAL)) {
                finalBucketEntry_1.setValues(i, 100.0);
            } else if (i == ARRAY_ENTRIES_AFTER_MERGE_TO.get(VaultEntryType.MEAL_BOLUS_CALCULATOR)) {
                finalBucketEntry_1.setValues(i, 100.0);
            } else {
                finalBucketEntry_1.setValues(i, 0.0);
            }
        }
        FinalBucketEntry finalBucketEntry_2 = new FinalBucketEntry(1);
        // fill the bucket with information
        for (int i = 0; i < ARRAY_ENTRIES_AFTER_MERGE_TO.size(); i++) {

            if (i == ARRAY_ENTRIES_AFTER_MERGE_TO.get(VaultEntryType.BASAL_PROFILE)) {
                finalBucketEntry_2.setValues(i, 200.0);
            } else if (i == ARRAY_ENTRIES_AFTER_MERGE_TO.get(VaultEntryType.BOLUS_NORMAL)) {
                finalBucketEntry_2.setValues(i, 200.0);
            } else if (i == ARRAY_ENTRIES_AFTER_MERGE_TO.get(VaultEntryType.MEAL_BOLUS_CALCULATOR)) {
                finalBucketEntry_2.setValues(i, 200.0);
            } else {
                finalBucketEntry_2.setValues(i, 0.0);
            }
        }

        List<FinalBucketEntry> wantedResult = new ArrayList<>();
        wantedResult.add(finalBucketEntry_1);
        wantedResult.add(finalBucketEntry_2);

        // use this instead of assert
        for (int i = 0; i < wantedResult.size(); i++) {
            assertEquals(wantedResult.get(i).getBucketNumber(), result.get(i).getBucketNumber());
            for (int m = 0; m < ARRAY_ENTRIES_AFTER_MERGE_TO.size(); m++) {
                assertEquals(Double.valueOf(wantedResult.get(i).getValues(m)), Double.valueOf(result.get(i).getValues(m)));
            }
        }
    }
}
