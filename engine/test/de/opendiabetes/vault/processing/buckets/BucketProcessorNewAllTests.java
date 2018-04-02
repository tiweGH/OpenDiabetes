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
import de.opendiabetes.vault.container.FinalBucketEntry;
import de.opendiabetes.vault.container.VaultEntry;
import de.opendiabetes.vault.container.VaultEntryType;
import de.opendiabetes.vault.processing.filter.TestFunctions;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Assert;
import static org.junit.Assert.assertEquals;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

/**
 *
 * @author Chryat1s
 */
public class BucketProcessorNewAllTests extends Assert {

    List<BucketEntry> resultBuckets;
    List<FinalBucketEntry> resultFinalBuckets;
    BucketProcessor bp;
    ListOfBucketEntriesCreator lbe;

    public BucketProcessorNewAllTests() {
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
    private void testBucketInformation(List<BucketEntry> wantedListOfBuckets, List<BucketEntry> result) {
        // assertEquals(result, wantedListOfBuckets);
        int i;
        for (i = 0; i < wantedListOfBuckets.size(); i++) {
            assertEquals(wantedListOfBuckets.get(i).getBucketNumber(), result.get(i).getBucketNumber());
            assertEquals(wantedListOfBuckets.get(i).getVaultEntry(), result.get(i).getVaultEntry());
            int m;
            for (m = 0; m < getNumberOfVaultEntryTriggerTypes(); m++) {
                assertEquals(Double.valueOf(wantedListOfBuckets.get(i).getValueTimer(m)), Double.valueOf(result.get(i).getValueTimer(m)));
                assertEquals(Double.valueOf(wantedListOfBuckets.get(i).getValues(m)), Double.valueOf(result.get(i).getValues(m)));
                assertEquals(wantedListOfBuckets.get(i).getFindNextVaultEntryType(m), result.get(i).getFindNextVaultEntryType(m));
//                assertTrue(result.get(i).getValueTimer(m) == wantedListOfBuckets.get(i).getValueTimer(m));
//                assertTrue(result.get(i).getValues(m) == wantedListOfBuckets.get(i).getValues(m));
//                assertTrue(result.get(i).getFindNextVaultEntryType(m) == wantedListOfBuckets.get(i).getFindNextVaultEntryType(m));
            }
        }
    }

    /**
     * This test checks if the method checkPreviousBucketEntry works correctly.
     *
     * @throws ParseException
     */
    @Test
    public void testBucketProcessor_checkPreviousBucketEntry() throws ParseException {
        lbe = new ListOfBucketEntriesCreator();
        Boolean result;
        Date date;

        List<BucketEntry> listOfBuckets = new ArrayList<>();
        listOfBuckets.add(new BucketEntry(1, new VaultEntry(VaultEntryType.GLUCOSE_CGM_ALERT, TestFunctions.creatNewDateToCheckFor("2000.01.01-00:01"), 170)));
        listOfBuckets.get(0).setValueTimer(0, 1);
        listOfBuckets.get(0).setValues(0, 1);
        listOfBuckets.add(new BucketEntry(2, new VaultEntry(VaultEntryType.GLUCOSE_CGM_ALERT, TestFunctions.creatNewDateToCheckFor("2000.01.01-00:01"), 170)));
        listOfBuckets.get(1).setValueTimer(0, 1);
        listOfBuckets.get(1).setValues(0, 1);

        date = TestFunctions.creatNewDateToCheckFor("2000.01.01-00:01");

        result = lbe.checkPreviousBucketEntry(date, listOfBuckets);

        assertEquals(false, (boolean) result);

        listOfBuckets = new ArrayList<>();
        listOfBuckets.add(new BucketEntry(1, new VaultEntry(VaultEntryType.GLUCOSE_CGM_ALERT, TestFunctions.creatNewDateToCheckFor("2000.01.01-00:01"), 170)));
        listOfBuckets.get(0).setValueTimer(0, 1);
        listOfBuckets.get(0).setValues(0, 1);
        listOfBuckets.add(new BucketEntry(2, new VaultEntry(VaultEntryType.EMPTY, TestFunctions.creatNewDateToCheckFor("2000.01.01-00:01"))));
        listOfBuckets.get(1).setValueTimer(0, 1);
        listOfBuckets.get(1).setValues(0, 1);

        date = TestFunctions.creatNewDateToCheckFor("2000.01.01-00:01");

        result = lbe.checkPreviousBucketEntry(date, listOfBuckets);

        assertEquals(true, (boolean) result);

        listOfBuckets = new ArrayList<>();
        listOfBuckets.add(new BucketEntry(1, new VaultEntry(VaultEntryType.GLUCOSE_CGM_ALERT, TestFunctions.creatNewDateToCheckFor("2000.01.01-00:01"), 170)));
        listOfBuckets.get(0).setValueTimer(0, 1);
        listOfBuckets.get(0).setValues(0, 1);
        listOfBuckets.add(new BucketEntry(2, new VaultEntry(VaultEntryType.EMPTY, TestFunctions.creatNewDateToCheckFor("2110.10.27-00:01"))));
        listOfBuckets.get(1).setValueTimer(0, 1);
        listOfBuckets.get(1).setValues(0, 1);

        date = TestFunctions.creatNewDateToCheckFor("2110.10.27-00:01");

        result = lbe.checkPreviousBucketEntry(date, listOfBuckets);

        assertEquals(true, (boolean) result);
    }

    /**
     * This test checks if the compareTwoDates method works correctly.
     *
     * @throws ParseException
     */
    @Test
    public void testBucketProcessor_compareTwoDates() throws ParseException {
        Date date_1;
        Date date_2;
        Integer result;
        lbe = new ListOfBucketEntriesCreator();

        date_1 = TestFunctions.creatNewDateToCheckFor("2000.01.01-00:01");
        date_2 = TestFunctions.creatNewDateToCheckFor("2000.01.01-00:02");

        result = lbe.compareTwoDates(date_1, date_2);
        assertTrue(result == -1);

        date_1 = TestFunctions.creatNewDateToCheckFor("2000.01.01-00:02");
        date_2 = TestFunctions.creatNewDateToCheckFor("2000.01.01-00:02");

        result = lbe.compareTwoDates(date_1, date_2);
        assertTrue(result == 0);

        date_1 = TestFunctions.creatNewDateToCheckFor("2000.01.01-00:03");
        date_2 = TestFunctions.creatNewDateToCheckFor("2000.01.01-00:02");

        result = lbe.compareTwoDates(date_1, date_2);
        assertTrue(result == 1);

        date_1 = TestFunctions.creatNewDateToCheckFor("2000.01.01-00:01");
        date_2 = TestFunctions.creatNewDateToCheckFor("2100.12.15-14:39");

        result = lbe.compareTwoDates(date_1, date_2);
        assertTrue(result == -1);

        date_1 = TestFunctions.creatNewDateToCheckFor("1999.10.20-20:01");
        date_2 = TestFunctions.creatNewDateToCheckFor("1999.10.20-20:01");

        result = lbe.compareTwoDates(date_1, date_2);
        assertTrue(result == 0);

        date_1 = TestFunctions.creatNewDateToCheckFor("2496.11.27-23:45");
        date_2 = TestFunctions.creatNewDateToCheckFor("2000.01.01-00:01");

        result = lbe.compareTwoDates(date_1, date_2);
        assertTrue(result == 1);
    }

    /**
     * This test checks if a correct list of BucketEntrys starting with the
     * bucket number 0 is created.
     *
     * @throws ParseException
     */
    @Test
    public void testBucketProcessor_createListOfBuckets_start_number_0() throws ParseException {
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

        lbe = new ListOfBucketEntriesCreator();
        List<BucketEntry> result = lbe.createListOfBuckets(0, vaultEntries);

        // 9 BucketEntrys -- 2 replaced and 3 timestamps missing -- 5 ML-relevant
        List<BucketEntry> wantedListOfBuckets = new ArrayList<>();
        wantedListOfBuckets.add(new BucketEntry(0, new VaultEntry(VaultEntryType.GLUCOSE_CGM_ALERT, TestFunctions.creatNewDateToCheckFor("2000.01.01-00:01"), 170)));
        wantedListOfBuckets.get(0).setValueTimer(0, 1);
        wantedListOfBuckets.get(0).setValues(0, 1);
        wantedListOfBuckets.add(new BucketEntry(1, new VaultEntry(VaultEntryType.GLUCOSE_CGM_ALERT, TestFunctions.creatNewDateToCheckFor("2000.01.01-00:01"), 170)));
        wantedListOfBuckets.get(1).setValueTimer(0, 1);
        wantedListOfBuckets.get(1).setValues(0, 1);
        wantedListOfBuckets.add(new BucketEntry(2, new VaultEntry(VaultEntryType.GLUCOSE_CGM_ALERT, TestFunctions.creatNewDateToCheckFor("2000.01.01-00:02"), 170)));
        wantedListOfBuckets.get(2).setValueTimer(0, 1);
        wantedListOfBuckets.get(2).setValues(0, 1);
        wantedListOfBuckets.add(new BucketEntry(3, new VaultEntry(VaultEntryType.EMPTY, TestFunctions.creatNewDateToCheckFor("2000.01.01-00:03"))));
        // reset onehot and timer
        wantedListOfBuckets.get(3).setValueTimer(0, 0);
        wantedListOfBuckets.get(3).setValues(0, 0);
        wantedListOfBuckets.add(new BucketEntry(4, new VaultEntry(VaultEntryType.EMPTY, TestFunctions.creatNewDateToCheckFor("2000.01.01-00:04"))));
        // reset onehot and timer
        wantedListOfBuckets.get(4).setValueTimer(0, 0);
        wantedListOfBuckets.get(4).setValues(0, 0);
        wantedListOfBuckets.add(new BucketEntry(5, new VaultEntry(VaultEntryType.GLUCOSE_CGM_ALERT, TestFunctions.creatNewDateToCheckFor("2000.01.01-00:05"), 170)));
        wantedListOfBuckets.get(5).setValueTimer(0, 1);
        wantedListOfBuckets.get(5).setValues(0, 1);
        wantedListOfBuckets.add(new BucketEntry(6, new VaultEntry(VaultEntryType.EMPTY, TestFunctions.creatNewDateToCheckFor("2000.01.01-00:06"))));
        // reset onehot and timer
        wantedListOfBuckets.get(6).setValueTimer(0, 0);
        wantedListOfBuckets.get(6).setValues(0, 0);
        wantedListOfBuckets.add(new BucketEntry(7, new VaultEntry(VaultEntryType.EMPTY, TestFunctions.creatNewDateToCheckFor("2000.01.01-00:07"))));
        // reset onehot and timer
        wantedListOfBuckets.get(7).setValueTimer(0, 0);
        wantedListOfBuckets.get(7).setValues(0, 0);
        wantedListOfBuckets.add(new BucketEntry(8, new VaultEntry(VaultEntryType.GLUCOSE_CGM_ALERT, TestFunctions.creatNewDateToCheckFor("2000.01.01-00:08"), 170)));
        wantedListOfBuckets.get(8).setValueTimer(0, 1);
        wantedListOfBuckets.get(8).setValues(0, 1);

        testBucketInformation(wantedListOfBuckets, result);
    }

    /**
     * This test checks if a correct list of BucketEntrys starting with the
     * bucket number 1 is created.
     *
     * @throws ParseException
     */
    @Test
    public void testBucketProcessor_createListOfBuckets_start_number_1() throws ParseException {
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

        lbe = new ListOfBucketEntriesCreator();
        List<BucketEntry> result = lbe.createListOfBuckets(1, vaultEntries);

        // 9 BucketEntrys -- 2 replaced and 3 timestamps missing -- 5 ML-relevant
        List<BucketEntry> wantedListOfBuckets = new ArrayList<>();
        wantedListOfBuckets.add(new BucketEntry(1, new VaultEntry(VaultEntryType.GLUCOSE_CGM_ALERT, TestFunctions.creatNewDateToCheckFor("2000.01.01-00:01"), 170)));
        wantedListOfBuckets.get(0).setValueTimer(0, 1);
        wantedListOfBuckets.get(0).setValues(0, 1);
        wantedListOfBuckets.add(new BucketEntry(2, new VaultEntry(VaultEntryType.GLUCOSE_CGM_ALERT, TestFunctions.creatNewDateToCheckFor("2000.01.01-00:01"), 170)));
        wantedListOfBuckets.get(1).setValueTimer(0, 1);
        wantedListOfBuckets.get(1).setValues(0, 1);
        wantedListOfBuckets.add(new BucketEntry(3, new VaultEntry(VaultEntryType.GLUCOSE_CGM_ALERT, TestFunctions.creatNewDateToCheckFor("2000.01.01-00:02"), 170)));
        wantedListOfBuckets.get(2).setValueTimer(0, 1);
        wantedListOfBuckets.get(2).setValues(0, 1);
        wantedListOfBuckets.add(new BucketEntry(4, new VaultEntry(VaultEntryType.EMPTY, TestFunctions.creatNewDateToCheckFor("2000.01.01-00:03"))));
        // reset onehot and timer
        wantedListOfBuckets.get(3).setValueTimer(0, 0);
        wantedListOfBuckets.get(3).setValues(0, 0);
        wantedListOfBuckets.add(new BucketEntry(5, new VaultEntry(VaultEntryType.EMPTY, TestFunctions.creatNewDateToCheckFor("2000.01.01-00:04"))));
        // reset onehot and timer
        wantedListOfBuckets.get(4).setValueTimer(0, 0);
        wantedListOfBuckets.get(4).setValues(0, 0);
        wantedListOfBuckets.add(new BucketEntry(6, new VaultEntry(VaultEntryType.GLUCOSE_CGM_ALERT, TestFunctions.creatNewDateToCheckFor("2000.01.01-00:05"), 170)));
        wantedListOfBuckets.get(5).setValueTimer(0, 1);
        wantedListOfBuckets.get(5).setValues(0, 1);
        wantedListOfBuckets.add(new BucketEntry(7, new VaultEntry(VaultEntryType.EMPTY, TestFunctions.creatNewDateToCheckFor("2000.01.01-00:06"))));
        // reset onehot and timer
        wantedListOfBuckets.get(6).setValueTimer(0, 0);
        wantedListOfBuckets.get(6).setValues(0, 0);
        wantedListOfBuckets.add(new BucketEntry(8, new VaultEntry(VaultEntryType.EMPTY, TestFunctions.creatNewDateToCheckFor("2000.01.01-00:07"))));
        // reset onehot and timer
        wantedListOfBuckets.get(7).setValueTimer(0, 0);
        wantedListOfBuckets.get(7).setValues(0, 0);
        wantedListOfBuckets.add(new BucketEntry(9, new VaultEntry(VaultEntryType.GLUCOSE_CGM_ALERT, TestFunctions.creatNewDateToCheckFor("2000.01.01-00:08"), 170)));
        wantedListOfBuckets.get(8).setValueTimer(0, 1);
        wantedListOfBuckets.get(8).setValues(0, 1);

        testBucketInformation(wantedListOfBuckets, result);
    }

}
