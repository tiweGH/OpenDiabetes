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
import static de.opendiabetes.vault.container.BucketEventTriggers.ARRAY_ENTRIES_AFTER_MERGE_TO;
import de.opendiabetes.vault.container.FinalBucketEntry;
import de.opendiabetes.vault.container.VaultEntry;
import de.opendiabetes.vault.container.VaultEntryType;
import de.opendiabetes.vault.processing.filter.TestFunctions;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;
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
public class BucketProcessorTest {

    List<BucketEntry> resultBuckets;
    List<FinalBucketEntry> resultFinalBuckets;
    BucketProcessor bp;
    ListOfBucketEntriesCreator lbe;

    public BucketProcessorTest() {
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

    // use this instead of assert
    private void testBucketInformation(List<FinalBucketEntry> wantedResult, List<FinalBucketEntry> result) {
        // assertEquals(result, wantedListOfBuckets);
        int i;
        for (i = 0; i < wantedResult.size(); i++) {
            assertEquals(wantedResult.get(i).getBucketNumber(), result.get(i).getBucketNumber());
            int m;
            for (m = 0; m < ARRAY_ENTRIES_AFTER_MERGE_TO.size(); m++) {
                assertEquals(Double.valueOf(wantedResult.get(i).getValues(m)), Double.valueOf(result.get(i).getValues(m)));
            }
        }
    }

    /**
     * This test checks if a correct list of FinalBucketEntrys is created.
     *
     * @throws ParseException
     */
    @Test
    public void testRunBucketProcessor() throws ParseException {
        List<VaultEntry> vaultEntries = new ArrayList<>();

        // 7 VaultEntrys -- 2 not ML-relevant and 5 ML-relevant -- 3 timestamps missing
        vaultEntries.add(new VaultEntry(VaultEntryType.GLUCOSE_CGM_ALERT, TestFunctions.creatNewDateToCheckFor("2000.01.01-00:01"), 170));
        vaultEntries.add(new VaultEntry(VaultEntryType.GLUCOSE_CGM_ALERT, TestFunctions.creatNewDateToCheckFor("2000.01.01-00:01"), 170));
        vaultEntries.add(new VaultEntry(VaultEntryType.GLUCOSE_CGM_ALERT, TestFunctions.creatNewDateToCheckFor("2000.01.01-00:02"), 170));
        vaultEntries.add(new VaultEntry(VaultEntryType.STRESS, TestFunctions.creatNewDateToCheckFor("2000.01.01-00:03"), 21, 5));
        //
        vaultEntries.add(new VaultEntry(VaultEntryType.GLUCOSE_CGM_ALERT, TestFunctions.creatNewDateToCheckFor("2000.01.01-00:05"), 170));
        //
        vaultEntries.add(new VaultEntry(VaultEntryType.STRESS, TestFunctions.creatNewDateToCheckFor("2000.01.01-00:08"), 21, 5));
        vaultEntries.add(new VaultEntry(VaultEntryType.GLUCOSE_CGM_ALERT, TestFunctions.creatNewDateToCheckFor("2000.01.01-00:08"), 170));

        bp = new BucketProcessor();
        List<FinalBucketEntry> result = bp.runProcess(0, vaultEntries, 1);

        List<FinalBucketEntry> wantedResult = new ArrayList<>();

        wantedResult.add(new FinalBucketEntry(0));
        wantedResult.get(0).setValues(0, 1);
        wantedResult.add(new FinalBucketEntry(1));
        wantedResult.get(1).setValues(0, 1);
        wantedResult.add(new FinalBucketEntry(2));
        wantedResult.get(2).setValues(0, 0);
        wantedResult.add(new FinalBucketEntry(3));
        wantedResult.get(3).setValues(0, 0);
        wantedResult.add(new FinalBucketEntry(4));
        wantedResult.get(4).setValues(0, 1);
        wantedResult.add(new FinalBucketEntry(5));
        wantedResult.get(5).setValues(0, 0);
        wantedResult.add(new FinalBucketEntry(6));
        wantedResult.get(6).setValues(0, 0);
        wantedResult.add(new FinalBucketEntry(7));
        wantedResult.get(7).setValues(0, 1);

        // check results
        testBucketInformation(wantedResult, result);
    }

    /**
     * This test checks if a correct list of FinalBucketEntrys is created.
     *
     * @throws ParseException
     */
    @Test
    public void testRunBucketProcessor_EMPTY() throws ParseException {
        List<VaultEntry> vaultEntries = new ArrayList<>();

        bp = new BucketProcessor();
        List<FinalBucketEntry> result = bp.runProcess(0, vaultEntries, 1);

        List<FinalBucketEntry> wantedResult = new ArrayList<>();

        // check results
        assertEquals(wantedResult, result);
    }
}
