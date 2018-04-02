/*
 * Copyright (C) 2018 a.a.aponte
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
import de.opendiabetes.vault.container.VaultEntry;
import de.opendiabetes.vault.container.VaultEntryAnnotation;
import de.opendiabetes.vault.container.VaultEntryType;
import de.opendiabetes.vault.processing.filter.TestFunctions;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;
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
public class ListOfBucketEntriesCreatorTest extends Assert {

    List<BucketEntry> result;
    ListOfBucketEntriesCreator bucketCreator;

    public ListOfBucketEntriesCreatorTest() {
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

    //
    // please ignore
    //
    /*
    @Test
    public void testTimeCounter_one_minute_later() throws ParseException {
        Date timeCounter = TestFunctions.creatNewDateToCheckFor("2000.01.01-00:01");
        Date timeCounter_2 = addMinutesToTimestamp(timeCounter, 1);
        Date timeCounterWanted = TestFunctions.creatNewDateToCheckFor("2000.01.01-00:02");

        assertEquals(timeCounter_2, timeCounterWanted);
    }
    @Test
    public void testTimeCounter_the_next_day() throws ParseException {
        Date timeCounter = TestFunctions.creatNewDateToCheckFor("2000.01.01-23:59");
        Date timeCounter_2 = addMinutesToTimestamp(timeCounter, 1);
        Date timeCounterWanted = TestFunctions.creatNewDateToCheckFor("2000.01.02-00:00");

        assertEquals(timeCounter_2, timeCounterWanted);
    }
    @Test
    public void testTimeCounter_the_next_year() throws ParseException {
        Date timeCounter = TestFunctions.creatNewDateToCheckFor("2000.12.31-23:59");
        Date timeCounter_2 = addMinutesToTimestamp(timeCounter, 1);
        Date timeCounterWanted = TestFunctions.creatNewDateToCheckFor("2001.01.01-00:00");

        assertEquals(timeCounter_2, timeCounterWanted);
    }
     */
    //
    // please ignore
    //
    /**
     * This test checks if a correct list of BucketEntrys is created.
     *
     * @throws ParseException
     */
    @Test
    public void testBucketProcessor_1() throws ParseException {
        List<VaultEntry> vaultEntries = new ArrayList<>();
        vaultEntries.add(new VaultEntry(VaultEntryType.GLUCOSE_CGM_ALERT, TestFunctions.creatNewDateToCheckFor("2000.01.01-00:01"), 170));

        bucketCreator = new ListOfBucketEntriesCreator();
        result = bucketCreator.createListOfBuckets(1, vaultEntries);

        List<BucketEntry> wantedListOfBuckets = new ArrayList<>();
        wantedListOfBuckets.add(new BucketEntry(1, new VaultEntry(VaultEntryType.GLUCOSE_CGM_ALERT, TestFunctions.creatNewDateToCheckFor("2000.01.01-00:01"), 170)));
        wantedListOfBuckets.get(0).setValueTimer(0, 1);
        wantedListOfBuckets.get(0).setValues(0, 1);

        testBucketInformation(wantedListOfBuckets, result);
    }

    /**
     * This test checks if a correct list of BucketEntrys is created.
     *
     * @throws ParseException
     */
    @Test
    public void testBucketProcessor_2_date_before() throws ParseException {
        List<VaultEntry> vaultEntries = new ArrayList<>();

        vaultEntries.add(new VaultEntry(VaultEntryType.GLUCOSE_CGM_ALERT, TestFunctions.creatNewDateToCheckFor("2000.01.01-00:01"), 170));
        vaultEntries.add(new VaultEntry(VaultEntryType.GLUCOSE_CGM_ALERT, TestFunctions.creatNewDateToCheckFor("2000.01.01-00:01"), 182));

        bucketCreator = new ListOfBucketEntriesCreator();
        result = bucketCreator.createListOfBuckets(1, vaultEntries);

        List<BucketEntry> wantedListOfBuckets = new ArrayList<>();
        wantedListOfBuckets.add(new BucketEntry(1, new VaultEntry(VaultEntryType.GLUCOSE_CGM_ALERT, TestFunctions.creatNewDateToCheckFor("2000.01.01-00:01"), 170)));
        wantedListOfBuckets.get(0).setValueTimer(0, 1);
        wantedListOfBuckets.get(0).setValues(0, 1);
        wantedListOfBuckets.add(new BucketEntry(2, new VaultEntry(VaultEntryType.GLUCOSE_CGM_ALERT, TestFunctions.creatNewDateToCheckFor("2000.01.01-00:01"), 182)));
        wantedListOfBuckets.get(1).setValueTimer(0, 1);
        wantedListOfBuckets.get(1).setValues(0, 1);

        testBucketInformation(wantedListOfBuckets, result);
    }

    /**
     * This test checks if a correct list of BucketEntrys is created.
     *
     * @throws ParseException
     */
    @Test
    public void testBucketProcessor_2_date_equal() throws ParseException {
        List<VaultEntry> vaultEntries = new ArrayList<>();

        vaultEntries.add(new VaultEntry(VaultEntryType.GLUCOSE_CGM_ALERT, TestFunctions.creatNewDateToCheckFor("2000.01.01-00:01"), 170));
        vaultEntries.add(new VaultEntry(VaultEntryType.GLUCOSE_CGM_ALERT, TestFunctions.creatNewDateToCheckFor("2000.01.01-00:02"), 182));

        bucketCreator = new ListOfBucketEntriesCreator();
        result = bucketCreator.createListOfBuckets(1, vaultEntries);

        List<BucketEntry> wantedListOfBuckets = new ArrayList<>();
        wantedListOfBuckets.add(new BucketEntry(1, new VaultEntry(VaultEntryType.GLUCOSE_CGM_ALERT, TestFunctions.creatNewDateToCheckFor("2000.01.01-00:01"), 170)));
        wantedListOfBuckets.get(0).setValueTimer(0, 1);
        wantedListOfBuckets.get(0).setValues(0, 1);
        wantedListOfBuckets.add(new BucketEntry(2, new VaultEntry(VaultEntryType.GLUCOSE_CGM_ALERT, TestFunctions.creatNewDateToCheckFor("2000.01.01-00:02"), 182)));
        wantedListOfBuckets.get(1).setValueTimer(0, 1);
        wantedListOfBuckets.get(1).setValues(0, 1);

        testBucketInformation(wantedListOfBuckets, result);
    }

    /**
     * This test checks if a correct list of BucketEntrys is created.
     *
     * @throws ParseException
     */
    @Test
    public void testBucketProcessor_2_date_after() throws ParseException {
        List<VaultEntry> vaultEntries = new ArrayList<>();

        vaultEntries.add(new VaultEntry(VaultEntryType.GLUCOSE_CGM_ALERT, TestFunctions.creatNewDateToCheckFor("2000.01.01-00:01"), 170));
        vaultEntries.add(new VaultEntry(VaultEntryType.GLUCOSE_CGM_ALERT, TestFunctions.creatNewDateToCheckFor("2000.01.01-00:03"), 182));

        bucketCreator = new ListOfBucketEntriesCreator();
        result = bucketCreator.createListOfBuckets(1, vaultEntries);

        List<BucketEntry> wantedListOfBuckets = new ArrayList<>();
        wantedListOfBuckets.add(new BucketEntry(1, new VaultEntry(VaultEntryType.GLUCOSE_CGM_ALERT, TestFunctions.creatNewDateToCheckFor("2000.01.01-00:01"), 170)));
        wantedListOfBuckets.get(0).setValueTimer(0, 1);
        wantedListOfBuckets.get(0).setValues(0, 1);
        wantedListOfBuckets.add(new BucketEntry(2, new VaultEntry(VaultEntryType.EMPTY, TestFunctions.creatNewDateToCheckFor("2000.01.01-00:02"))));
        // reset onehot and timer
        wantedListOfBuckets.get(1).setValueTimer(0, 0);
        wantedListOfBuckets.get(1).setValues(0, 0);
        wantedListOfBuckets.add(new BucketEntry(3, new VaultEntry(VaultEntryType.GLUCOSE_CGM_ALERT, TestFunctions.creatNewDateToCheckFor("2000.01.01-00:03"), 182)));
        wantedListOfBuckets.get(2).setValueTimer(0, 1);
        wantedListOfBuckets.get(2).setValues(0, 1);

        testBucketInformation(wantedListOfBuckets, result);
    }

    /**
     * This test checks if a correct list of BucketEntrys is created.
     *
     * @throws ParseException
     */
    @Test
    public void testBucketProcessor_3() throws ParseException {
        List<VaultEntry> vaultEntries = new ArrayList<>();
        List<VaultEntryAnnotation> tmpAnnotations;

        vaultEntries.add(new VaultEntry(VaultEntryType.GLUCOSE_CGM_ALERT, TestFunctions.creatNewDateToCheckFor("2000.01.01-00:01"), 170));
        tmpAnnotations = new ArrayList<>();
        tmpAnnotations.add(new VaultEntryAnnotation(VaultEntryAnnotation.TYPE.GLUCOSE_BG_METER_SERIAL).setValue("BG1140084B"));
        vaultEntries.add(new VaultEntry(VaultEntryType.GLUCOSE_BG, TestFunctions.creatNewDateToCheckFor("2000.01.01-00:02"), 109.0, tmpAnnotations));
        vaultEntries.add(new VaultEntry(VaultEntryType.GLUCOSE_CGM_ALERT, TestFunctions.creatNewDateToCheckFor("2000.01.01-00:03"), 182));

        bucketCreator = new ListOfBucketEntriesCreator();
        result = bucketCreator.createListOfBuckets(1, vaultEntries);

        List<BucketEntry> wantedListOfBuckets = new ArrayList<>();
        wantedListOfBuckets.add(new BucketEntry(1, new VaultEntry(VaultEntryType.GLUCOSE_CGM_ALERT, TestFunctions.creatNewDateToCheckFor("2000.01.01-00:01"), 170)));
        wantedListOfBuckets.get(0).setValueTimer(0, 1);
        wantedListOfBuckets.get(0).setValues(0, 1);
        tmpAnnotations = new ArrayList<>();
        tmpAnnotations.add(new VaultEntryAnnotation(VaultEntryAnnotation.TYPE.GLUCOSE_BG_METER_SERIAL).setValue("BG1140084B"));
        wantedListOfBuckets.add(new BucketEntry(2, new VaultEntry(VaultEntryType.GLUCOSE_BG, TestFunctions.creatNewDateToCheckFor("2000.01.01-00:02"), 109.0, tmpAnnotations)));
        wantedListOfBuckets.get(1).setValueTimer(36, 1);
        wantedListOfBuckets.get(1).setValues(36, 109);
        // reset onehot and timer
        wantedListOfBuckets.get(1).setValueTimer(0, 0);
        wantedListOfBuckets.get(1).setValues(0, 0);
        wantedListOfBuckets.add(new BucketEntry(3, new VaultEntry(VaultEntryType.GLUCOSE_CGM_ALERT, TestFunctions.creatNewDateToCheckFor("2000.01.01-00:03"), 182)));
        wantedListOfBuckets.get(2).setValueTimer(0, 1);
        wantedListOfBuckets.get(2).setValues(0, 1);
        // reset onehot and timer
        wantedListOfBuckets.get(2).setValueTimer(36, 0);
        wantedListOfBuckets.get(2).setValues(36, 0);

        testBucketInformation(wantedListOfBuckets, result);
    }

    /**
     * This test checks if a correct list of BucketEntrys is created.
     *
     * @throws ParseException
     */
    @Test
    public void testBucketProcessor_replace_a_bucket_entry() throws ParseException {
        List<VaultEntry> vaultEntries = new ArrayList<>();

        vaultEntries.add(new VaultEntry(VaultEntryType.GLUCOSE_CGM_ALERT, TestFunctions.creatNewDateToCheckFor("2000.01.01-00:08"), 170));
        vaultEntries.add(new VaultEntry(VaultEntryType.STRESS, TestFunctions.creatNewDateToCheckFor("2000.01.01-00:08"), 21, 5));

        bucketCreator = new ListOfBucketEntriesCreator();
        result = bucketCreator.createListOfBuckets(1, vaultEntries);

        List<BucketEntry> wantedListOfBuckets = new ArrayList<>();

        wantedListOfBuckets.add(new BucketEntry(1, new VaultEntry(VaultEntryType.GLUCOSE_CGM_ALERT, TestFunctions.creatNewDateToCheckFor("2000.01.01-00:08"), 170)));
        wantedListOfBuckets.get(0).setValueTimer(0, 1);
        wantedListOfBuckets.get(0).setValues(0, 1);

        testBucketInformation(wantedListOfBuckets, result);
    }

    /**
     * This test checks if a correct list of BucketEntrys is created.
     *
     * @throws ParseException
     */
    @Test
    public void testBucketProcessor_4() throws ParseException {
        List<VaultEntry> vaultEntries = new ArrayList<>();

        // 7 VaultEntrys -- 2 not ML-relevant and 5 ML-relevant -- 3 timestamps missing
        vaultEntries.add(new VaultEntry(VaultEntryType.GLUCOSE_CGM_ALERT, TestFunctions.creatNewDateToCheckFor("2000.01.01-00:01"), 170));
        vaultEntries.add(new VaultEntry(VaultEntryType.GLUCOSE_CGM_ALERT, TestFunctions.creatNewDateToCheckFor("2000.01.01-00:01"), 170));
        vaultEntries.add(new VaultEntry(VaultEntryType.GLUCOSE_CGM_ALERT, TestFunctions.creatNewDateToCheckFor("2000.01.01-00:02"), 170));
        vaultEntries.add(new VaultEntry(VaultEntryType.STRESS, TestFunctions.creatNewDateToCheckFor("2000.01.01-00:03"), 21, 5));
        //
        vaultEntries.add(new VaultEntry(VaultEntryType.GLUCOSE_CGM_ALERT, TestFunctions.creatNewDateToCheckFor("2000.01.01-00:05"), 170));
        //
        vaultEntries.add(new VaultEntry(VaultEntryType.GLUCOSE_CGM_ALERT, TestFunctions.creatNewDateToCheckFor("2000.01.01-00:08"), 170));
        vaultEntries.add(new VaultEntry(VaultEntryType.STRESS, TestFunctions.creatNewDateToCheckFor("2000.01.01-00:08"), 21, 5));

        bucketCreator = new ListOfBucketEntriesCreator();
        result = bucketCreator.createListOfBuckets(1, vaultEntries);

        // 9 BucketEntrys -- 1 replaced and 3 timestamps missing -- 5 ML-relevant
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

    /**
     * This test checks if a correct list of BucketEntrys is created.
     *
     * @throws ParseException
     */
    @Test
    public void testBucketProcessor_5() throws ParseException {
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

        bucketCreator = new ListOfBucketEntriesCreator();
        result = bucketCreator.createListOfBuckets(1, vaultEntries);

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

    //
    // TEST_6
    //
    /**
     * This test checks if a correct list of BucketEntrys is created.
     *
     * @throws ParseException
     */
    @Test
    public void testBucketProcessor_7_replace_first_BucketEntry() throws ParseException {
        List<VaultEntry> vaultEntries = new ArrayList<>();

        vaultEntries.add(new VaultEntry(VaultEntryType.STRESS, TestFunctions.creatNewDateToCheckFor("2000.01.01-00:01"), 21, 5));
        vaultEntries.add(new VaultEntry(VaultEntryType.GLUCOSE_CGM_ALERT, TestFunctions.creatNewDateToCheckFor("2000.01.01-00:01"), 170));
        vaultEntries.add(new VaultEntry(VaultEntryType.GLUCOSE_CGM_ALERT, TestFunctions.creatNewDateToCheckFor("2000.01.01-00:01"), 170));

        bucketCreator = new ListOfBucketEntriesCreator();
        result = bucketCreator.createListOfBuckets(1, vaultEntries);

        List<BucketEntry> wantedListOfBuckets = new ArrayList<>();
        wantedListOfBuckets.add(new BucketEntry(1, new VaultEntry(VaultEntryType.GLUCOSE_CGM_ALERT, TestFunctions.creatNewDateToCheckFor("2000.01.01-00:01"), 170)));
        wantedListOfBuckets.get(0).setValueTimer(0, 1);
        wantedListOfBuckets.get(0).setValues(0, 1);
        wantedListOfBuckets.add(new BucketEntry(2, new VaultEntry(VaultEntryType.GLUCOSE_CGM_ALERT, TestFunctions.creatNewDateToCheckFor("2000.01.01-00:01"), 170)));
        wantedListOfBuckets.get(1).setValueTimer(0, 1);
        wantedListOfBuckets.get(1).setValues(0, 1);

        testBucketInformation(wantedListOfBuckets, result);
    }

    /**
     * This test checks if a correct list of BucketEntrys is created.
     *
     * @throws ParseException
     */
    @Test
    public void testBucketProcessor_7_replace_second_BucketEntry() throws ParseException {
        List<VaultEntry> vaultEntries = new ArrayList<>();

        vaultEntries.add(new VaultEntry(VaultEntryType.GLUCOSE_CGM_ALERT, TestFunctions.creatNewDateToCheckFor("2000.01.01-00:02"), 170));
        vaultEntries.add(new VaultEntry(VaultEntryType.STRESS, TestFunctions.creatNewDateToCheckFor("2000.01.01-00:02"), 21, 5));
        vaultEntries.add(new VaultEntry(VaultEntryType.GLUCOSE_CGM_ALERT, TestFunctions.creatNewDateToCheckFor("2000.01.01-00:02"), 170));

        bucketCreator = new ListOfBucketEntriesCreator();
        result = bucketCreator.createListOfBuckets(1, vaultEntries);

        List<BucketEntry> wantedListOfBuckets = new ArrayList<>();
        wantedListOfBuckets.add(new BucketEntry(1, new VaultEntry(VaultEntryType.GLUCOSE_CGM_ALERT, TestFunctions.creatNewDateToCheckFor("2000.01.01-00:02"), 170)));
        wantedListOfBuckets.get(0).setValueTimer(0, 1);
        wantedListOfBuckets.get(0).setValues(0, 1);
        wantedListOfBuckets.add(new BucketEntry(2, new VaultEntry(VaultEntryType.GLUCOSE_CGM_ALERT, TestFunctions.creatNewDateToCheckFor("2000.01.01-00:02"), 170)));
        wantedListOfBuckets.get(1).setValueTimer(0, 1);
        wantedListOfBuckets.get(1).setValues(0, 1);

        testBucketInformation(wantedListOfBuckets, result);
    }

    /**
     * This test checks if a correct list of BucketEntrys is created.
     *
     * @throws ParseException
     */
    @Test
    public void testBucketProcessor_7_replace_third_BucketEntry() throws ParseException {
        List<VaultEntry> vaultEntries = new ArrayList<>();

        vaultEntries.add(new VaultEntry(VaultEntryType.GLUCOSE_CGM_ALERT, TestFunctions.creatNewDateToCheckFor("2000.01.01-00:03"), 170));
        vaultEntries.add(new VaultEntry(VaultEntryType.GLUCOSE_CGM_ALERT, TestFunctions.creatNewDateToCheckFor("2000.01.01-00:03"), 170));
        vaultEntries.add(new VaultEntry(VaultEntryType.STRESS, TestFunctions.creatNewDateToCheckFor("2000.01.01-00:03"), 21, 5));

        bucketCreator = new ListOfBucketEntriesCreator();
        result = bucketCreator.createListOfBuckets(1, vaultEntries);

        List<BucketEntry> wantedListOfBuckets = new ArrayList<>();
        wantedListOfBuckets.add(new BucketEntry(1, new VaultEntry(VaultEntryType.GLUCOSE_CGM_ALERT, TestFunctions.creatNewDateToCheckFor("2000.01.01-00:03"), 170)));
        wantedListOfBuckets.get(0).setValueTimer(0, 1);
        wantedListOfBuckets.get(0).setValues(0, 1);
        wantedListOfBuckets.add(new BucketEntry(2, new VaultEntry(VaultEntryType.GLUCOSE_CGM_ALERT, TestFunctions.creatNewDateToCheckFor("2000.01.01-00:03"), 170)));
        wantedListOfBuckets.get(1).setValueTimer(0, 1);
        wantedListOfBuckets.get(1).setValues(0, 1);

        testBucketInformation(wantedListOfBuckets, result);
    }

    /**
     * This test checks if a correct list of BucketEntrys is created.
     *
     * @throws ParseException
     */
    @Test
    public void testBucketProcessor_8_do_all_number_7_tests() throws ParseException {
        List<VaultEntry> vaultEntries = new ArrayList<>();

        vaultEntries.add(new VaultEntry(VaultEntryType.STRESS, TestFunctions.creatNewDateToCheckFor("2000.01.01-00:01"), 21, 5));
        vaultEntries.add(new VaultEntry(VaultEntryType.GLUCOSE_CGM_ALERT, TestFunctions.creatNewDateToCheckFor("2000.01.01-00:01"), 170));
        vaultEntries.add(new VaultEntry(VaultEntryType.GLUCOSE_CGM_ALERT, TestFunctions.creatNewDateToCheckFor("2000.01.01-00:01"), 170));

        vaultEntries.add(new VaultEntry(VaultEntryType.GLUCOSE_CGM_ALERT, TestFunctions.creatNewDateToCheckFor("2000.01.01-00:02"), 170));
        vaultEntries.add(new VaultEntry(VaultEntryType.STRESS, TestFunctions.creatNewDateToCheckFor("2000.01.01-00:02"), 21, 5));
        vaultEntries.add(new VaultEntry(VaultEntryType.GLUCOSE_CGM_ALERT, TestFunctions.creatNewDateToCheckFor("2000.01.01-00:02"), 170));

        vaultEntries.add(new VaultEntry(VaultEntryType.GLUCOSE_CGM_ALERT, TestFunctions.creatNewDateToCheckFor("2000.01.01-00:03"), 170));
        vaultEntries.add(new VaultEntry(VaultEntryType.GLUCOSE_CGM_ALERT, TestFunctions.creatNewDateToCheckFor("2000.01.01-00:03"), 170));
        vaultEntries.add(new VaultEntry(VaultEntryType.STRESS, TestFunctions.creatNewDateToCheckFor("2000.01.01-00:03"), 21, 5));

        bucketCreator = new ListOfBucketEntriesCreator();
        result = bucketCreator.createListOfBuckets(1, vaultEntries);

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
        wantedListOfBuckets.add(new BucketEntry(4, new VaultEntry(VaultEntryType.GLUCOSE_CGM_ALERT, TestFunctions.creatNewDateToCheckFor("2000.01.01-00:02"), 170)));
        wantedListOfBuckets.get(3).setValueTimer(0, 1);
        wantedListOfBuckets.get(3).setValues(0, 1);

        wantedListOfBuckets.add(new BucketEntry(5, new VaultEntry(VaultEntryType.GLUCOSE_CGM_ALERT, TestFunctions.creatNewDateToCheckFor("2000.01.01-00:03"), 170)));
        wantedListOfBuckets.get(4).setValueTimer(0, 1);
        wantedListOfBuckets.get(4).setValues(0, 1);
        wantedListOfBuckets.add(new BucketEntry(6, new VaultEntry(VaultEntryType.GLUCOSE_CGM_ALERT, TestFunctions.creatNewDateToCheckFor("2000.01.01-00:03"), 170)));
        wantedListOfBuckets.get(5).setValueTimer(0, 1);
        wantedListOfBuckets.get(5).setValues(0, 1);

        testBucketInformation(wantedListOfBuckets, result);
    }

    /**
     * This test checks if a correct list of BucketEntrys is created.
     *
     * @throws ParseException
     */
    @Test
    public void testBucketProcessor_8_do_all_number_7_tests_2() throws ParseException {
        List<VaultEntry> vaultEntries = new ArrayList<>();

        vaultEntries.add(new VaultEntry(VaultEntryType.STRESS, TestFunctions.creatNewDateToCheckFor("2000.01.01-00:01"), 21, 5));
        vaultEntries.add(new VaultEntry(VaultEntryType.GLUCOSE_CGM_ALERT, TestFunctions.creatNewDateToCheckFor("2000.01.01-00:01"), 170));
        vaultEntries.add(new VaultEntry(VaultEntryType.GLUCOSE_CGM_ALERT, TestFunctions.creatNewDateToCheckFor("2000.01.01-00:01"), 170));

        vaultEntries.add(new VaultEntry(VaultEntryType.GLUCOSE_CGM_ALERT, TestFunctions.creatNewDateToCheckFor("2000.01.01-00:02"), 170));
        vaultEntries.add(new VaultEntry(VaultEntryType.STRESS, TestFunctions.creatNewDateToCheckFor("2000.01.01-00:02"), 21, 5));
        vaultEntries.add(new VaultEntry(VaultEntryType.GLUCOSE_CGM_ALERT, TestFunctions.creatNewDateToCheckFor("2000.01.01-00:02"), 170));

        vaultEntries.add(new VaultEntry(VaultEntryType.GLUCOSE_CGM_ALERT, TestFunctions.creatNewDateToCheckFor("2000.01.01-00:03"), 170));
        vaultEntries.add(new VaultEntry(VaultEntryType.GLUCOSE_CGM_ALERT, TestFunctions.creatNewDateToCheckFor("2000.01.01-00:04"), 170));
        vaultEntries.add(new VaultEntry(VaultEntryType.STRESS, TestFunctions.creatNewDateToCheckFor("2000.01.01-00:05"), 21, 5));

        bucketCreator = new ListOfBucketEntriesCreator();
        result = bucketCreator.createListOfBuckets(1, vaultEntries);

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
        wantedListOfBuckets.add(new BucketEntry(4, new VaultEntry(VaultEntryType.GLUCOSE_CGM_ALERT, TestFunctions.creatNewDateToCheckFor("2000.01.01-00:02"), 170)));
        wantedListOfBuckets.get(3).setValueTimer(0, 1);
        wantedListOfBuckets.get(3).setValues(0, 1);

        wantedListOfBuckets.add(new BucketEntry(5, new VaultEntry(VaultEntryType.GLUCOSE_CGM_ALERT, TestFunctions.creatNewDateToCheckFor("2000.01.01-00:03"), 170)));
        wantedListOfBuckets.get(4).setValueTimer(0, 1);
        wantedListOfBuckets.get(4).setValues(0, 1);
        wantedListOfBuckets.add(new BucketEntry(6, new VaultEntry(VaultEntryType.GLUCOSE_CGM_ALERT, TestFunctions.creatNewDateToCheckFor("2000.01.01-00:04"), 170)));
        wantedListOfBuckets.get(5).setValueTimer(0, 1);
        wantedListOfBuckets.get(5).setValues(0, 1);
        wantedListOfBuckets.add(new BucketEntry(7, new VaultEntry(VaultEntryType.EMPTY, TestFunctions.creatNewDateToCheckFor("2000.01.01-00:05"))));
        wantedListOfBuckets.get(6).setValueTimer(0, 0);
        wantedListOfBuckets.get(6).setValues(0, 0);

        testBucketInformation(wantedListOfBuckets, result);
    }

    /**
     * This test checks if a correct list of BucketEntrys is created.
     *
     * @throws ParseException
     */
    @Test
    public void testBucketProcessor_9_generate_empty_BucketEntrys_1() throws ParseException {
        List<VaultEntry> vaultEntries = new ArrayList<>();

        vaultEntries.add(new VaultEntry(VaultEntryType.STRESS, TestFunctions.creatNewDateToCheckFor("2000.01.01-00:01"), 21, 5));
        // missing timestamps will be filled
        vaultEntries.add(new VaultEntry(VaultEntryType.STRESS, TestFunctions.creatNewDateToCheckFor("2000.01.01-00:30"), 21, 5));

        bucketCreator = new ListOfBucketEntriesCreator();
        result = bucketCreator.createListOfBuckets(1, vaultEntries);

        List<BucketEntry> wantedListOfBuckets = new ArrayList<>();
        wantedListOfBuckets.add(new BucketEntry(1, new VaultEntry(VaultEntryType.EMPTY, TestFunctions.creatNewDateToCheckFor("2000.01.01-00:01"))));
        // reset onehot and timer
        wantedListOfBuckets.get(0).setValueTimer(0, 0);
        wantedListOfBuckets.get(0).setValues(0, 0);
        wantedListOfBuckets.add(new BucketEntry(2, new VaultEntry(VaultEntryType.EMPTY, TestFunctions.creatNewDateToCheckFor("2000.01.01-00:02"))));
        // reset onehot and timer
        wantedListOfBuckets.get(1).setValueTimer(0, 0);
        wantedListOfBuckets.get(1).setValues(0, 0);
        wantedListOfBuckets.add(new BucketEntry(3, new VaultEntry(VaultEntryType.EMPTY, TestFunctions.creatNewDateToCheckFor("2000.01.01-00:03"))));
        // reset onehot and timer
        wantedListOfBuckets.get(2).setValueTimer(0, 0);
        wantedListOfBuckets.get(2).setValues(0, 0);
        wantedListOfBuckets.add(new BucketEntry(4, new VaultEntry(VaultEntryType.EMPTY, TestFunctions.creatNewDateToCheckFor("2000.01.01-00:04"))));
        // reset onehot and timer
        wantedListOfBuckets.get(3).setValueTimer(0, 0);
        wantedListOfBuckets.get(3).setValues(0, 0);
        wantedListOfBuckets.add(new BucketEntry(5, new VaultEntry(VaultEntryType.EMPTY, TestFunctions.creatNewDateToCheckFor("2000.01.01-00:05"))));
        // reset onehot and timer
        wantedListOfBuckets.get(4).setValueTimer(0, 0);
        wantedListOfBuckets.get(4).setValues(0, 0);
        wantedListOfBuckets.add(new BucketEntry(6, new VaultEntry(VaultEntryType.EMPTY, TestFunctions.creatNewDateToCheckFor("2000.01.01-00:06"))));
        // reset onehot and timer
        wantedListOfBuckets.get(5).setValueTimer(0, 0);
        wantedListOfBuckets.get(5).setValues(0, 0);
        wantedListOfBuckets.add(new BucketEntry(7, new VaultEntry(VaultEntryType.EMPTY, TestFunctions.creatNewDateToCheckFor("2000.01.01-00:07"))));
        // reset onehot and timer
        wantedListOfBuckets.get(6).setValueTimer(0, 0);
        wantedListOfBuckets.get(6).setValues(0, 0);
        wantedListOfBuckets.add(new BucketEntry(8, new VaultEntry(VaultEntryType.EMPTY, TestFunctions.creatNewDateToCheckFor("2000.01.01-00:08"))));
        // reset onehot and timer
        wantedListOfBuckets.get(7).setValueTimer(0, 0);
        wantedListOfBuckets.get(7).setValues(0, 0);
        wantedListOfBuckets.add(new BucketEntry(9, new VaultEntry(VaultEntryType.EMPTY, TestFunctions.creatNewDateToCheckFor("2000.01.01-00:09"))));
        // reset onehot and timer
        wantedListOfBuckets.get(8).setValueTimer(0, 0);
        wantedListOfBuckets.get(8).setValues(0, 0);
        wantedListOfBuckets.add(new BucketEntry(10, new VaultEntry(VaultEntryType.EMPTY, TestFunctions.creatNewDateToCheckFor("2000.01.01-00:10"))));
        // reset onehot and timer
        wantedListOfBuckets.get(9).setValueTimer(0, 0);
        wantedListOfBuckets.get(9).setValues(0, 0);
        wantedListOfBuckets.add(new BucketEntry(11, new VaultEntry(VaultEntryType.EMPTY, TestFunctions.creatNewDateToCheckFor("2000.01.01-00:11"))));
        // reset onehot and timer
        wantedListOfBuckets.get(10).setValueTimer(0, 0);
        wantedListOfBuckets.get(10).setValues(0, 0);
        wantedListOfBuckets.add(new BucketEntry(12, new VaultEntry(VaultEntryType.EMPTY, TestFunctions.creatNewDateToCheckFor("2000.01.01-00:12"))));
        // reset onehot and timer
        wantedListOfBuckets.get(11).setValueTimer(0, 0);
        wantedListOfBuckets.get(11).setValues(0, 0);
        wantedListOfBuckets.add(new BucketEntry(13, new VaultEntry(VaultEntryType.EMPTY, TestFunctions.creatNewDateToCheckFor("2000.01.01-00:13"))));
        // reset onehot and timer
        wantedListOfBuckets.get(12).setValueTimer(0, 0);
        wantedListOfBuckets.get(12).setValues(0, 0);
        wantedListOfBuckets.add(new BucketEntry(14, new VaultEntry(VaultEntryType.EMPTY, TestFunctions.creatNewDateToCheckFor("2000.01.01-00:14"))));
        // reset onehot and timer
        wantedListOfBuckets.get(13).setValueTimer(0, 0);
        wantedListOfBuckets.get(13).setValues(0, 0);
        wantedListOfBuckets.add(new BucketEntry(15, new VaultEntry(VaultEntryType.EMPTY, TestFunctions.creatNewDateToCheckFor("2000.01.01-00:15"))));
        // reset onehot and timer
        wantedListOfBuckets.get(14).setValueTimer(0, 0);
        wantedListOfBuckets.get(14).setValues(0, 0);
        wantedListOfBuckets.add(new BucketEntry(16, new VaultEntry(VaultEntryType.EMPTY, TestFunctions.creatNewDateToCheckFor("2000.01.01-00:16"))));
        // reset onehot and timer
        wantedListOfBuckets.get(15).setValueTimer(0, 0);
        wantedListOfBuckets.get(15).setValues(0, 0);
        wantedListOfBuckets.add(new BucketEntry(17, new VaultEntry(VaultEntryType.EMPTY, TestFunctions.creatNewDateToCheckFor("2000.01.01-00:17"))));
        // reset onehot and timer
        wantedListOfBuckets.get(16).setValueTimer(0, 0);
        wantedListOfBuckets.get(16).setValues(0, 0);
        wantedListOfBuckets.add(new BucketEntry(18, new VaultEntry(VaultEntryType.EMPTY, TestFunctions.creatNewDateToCheckFor("2000.01.01-00:18"))));
        // reset onehot and timer
        wantedListOfBuckets.get(17).setValueTimer(0, 0);
        wantedListOfBuckets.get(17).setValues(0, 0);
        wantedListOfBuckets.add(new BucketEntry(19, new VaultEntry(VaultEntryType.EMPTY, TestFunctions.creatNewDateToCheckFor("2000.01.01-00:19"))));
        // reset onehot and timer
        wantedListOfBuckets.get(18).setValueTimer(0, 0);
        wantedListOfBuckets.get(18).setValues(0, 0);
        wantedListOfBuckets.add(new BucketEntry(20, new VaultEntry(VaultEntryType.EMPTY, TestFunctions.creatNewDateToCheckFor("2000.01.01-00:20"))));
        // reset onehot and timer
        wantedListOfBuckets.get(19).setValueTimer(0, 0);
        wantedListOfBuckets.get(19).setValues(0, 0);
        wantedListOfBuckets.add(new BucketEntry(21, new VaultEntry(VaultEntryType.EMPTY, TestFunctions.creatNewDateToCheckFor("2000.01.01-00:21"))));
        // reset onehot and timer
        wantedListOfBuckets.get(20).setValueTimer(0, 0);
        wantedListOfBuckets.get(20).setValues(0, 0);
        wantedListOfBuckets.add(new BucketEntry(22, new VaultEntry(VaultEntryType.EMPTY, TestFunctions.creatNewDateToCheckFor("2000.01.01-00:22"))));
        // reset onehot and timer
        wantedListOfBuckets.get(21).setValueTimer(0, 0);
        wantedListOfBuckets.get(21).setValues(0, 0);
        wantedListOfBuckets.add(new BucketEntry(23, new VaultEntry(VaultEntryType.EMPTY, TestFunctions.creatNewDateToCheckFor("2000.01.01-00:23"))));
        // reset onehot and timer
        wantedListOfBuckets.get(22).setValueTimer(0, 0);
        wantedListOfBuckets.get(22).setValues(0, 0);
        wantedListOfBuckets.add(new BucketEntry(24, new VaultEntry(VaultEntryType.EMPTY, TestFunctions.creatNewDateToCheckFor("2000.01.01-00:24"))));
        // reset onehot and timer
        wantedListOfBuckets.get(23).setValueTimer(0, 0);
        wantedListOfBuckets.get(23).setValues(0, 0);
        wantedListOfBuckets.add(new BucketEntry(25, new VaultEntry(VaultEntryType.EMPTY, TestFunctions.creatNewDateToCheckFor("2000.01.01-00:25"))));
        // reset onehot and timer
        wantedListOfBuckets.get(24).setValueTimer(0, 0);
        wantedListOfBuckets.get(24).setValues(0, 0);
        wantedListOfBuckets.add(new BucketEntry(26, new VaultEntry(VaultEntryType.EMPTY, TestFunctions.creatNewDateToCheckFor("2000.01.01-00:26"))));
        // reset onehot and timer
        wantedListOfBuckets.get(25).setValueTimer(0, 0);
        wantedListOfBuckets.get(25).setValues(0, 0);
        wantedListOfBuckets.add(new BucketEntry(27, new VaultEntry(VaultEntryType.EMPTY, TestFunctions.creatNewDateToCheckFor("2000.01.01-00:27"))));
        // reset onehot and timer
        wantedListOfBuckets.get(26).setValueTimer(0, 0);
        wantedListOfBuckets.get(26).setValues(0, 0);
        wantedListOfBuckets.add(new BucketEntry(28, new VaultEntry(VaultEntryType.EMPTY, TestFunctions.creatNewDateToCheckFor("2000.01.01-00:28"))));
        // reset onehot and timer
        wantedListOfBuckets.get(27).setValueTimer(0, 0);
        wantedListOfBuckets.get(27).setValues(0, 0);
        wantedListOfBuckets.add(new BucketEntry(29, new VaultEntry(VaultEntryType.EMPTY, TestFunctions.creatNewDateToCheckFor("2000.01.01-00:29"))));
        // reset onehot and timer
        wantedListOfBuckets.get(28).setValueTimer(0, 0);
        wantedListOfBuckets.get(28).setValues(0, 0);
        wantedListOfBuckets.add(new BucketEntry(30, new VaultEntry(VaultEntryType.EMPTY, TestFunctions.creatNewDateToCheckFor("2000.01.01-00:30"))));
        // reset onehot and timer
        wantedListOfBuckets.get(29).setValueTimer(0, 0);
        wantedListOfBuckets.get(29).setValues(0, 0);

        testBucketInformation(wantedListOfBuckets, result);
    }

    /**
     * This test checks if a correct list of BucketEntrys is created.
     *
     * @throws ParseException
     */
    @Test
    public void testBucketProcessor_9_generate_empty_BucketEntrys_2() throws ParseException {
        List<VaultEntry> vaultEntries = new ArrayList<>();

        vaultEntries.add(new VaultEntry(VaultEntryType.GLUCOSE_CGM_ALERT, TestFunctions.creatNewDateToCheckFor("2000.01.01-00:01"), 170));
        // missing timestamps will be filled
        vaultEntries.add(new VaultEntry(VaultEntryType.GLUCOSE_CGM_ALERT, TestFunctions.creatNewDateToCheckFor("2000.01.01-00:030"), 170));

        bucketCreator = new ListOfBucketEntriesCreator();
        result = bucketCreator.createListOfBuckets(1, vaultEntries);

        List<BucketEntry> wantedListOfBuckets = new ArrayList<>();
        wantedListOfBuckets.add(new BucketEntry(1, new VaultEntry(VaultEntryType.GLUCOSE_CGM_ALERT, TestFunctions.creatNewDateToCheckFor("2000.01.01-00:01"), 170)));
        wantedListOfBuckets.get(0).setValueTimer(0, 1);
        wantedListOfBuckets.get(0).setValues(0, 1);
        wantedListOfBuckets.add(new BucketEntry(2, new VaultEntry(VaultEntryType.EMPTY, TestFunctions.creatNewDateToCheckFor("2000.01.01-00:02"))));
        // reset onehot and timer
        wantedListOfBuckets.get(1).setValueTimer(0, 0);
        wantedListOfBuckets.get(1).setValues(0, 0);
        wantedListOfBuckets.add(new BucketEntry(3, new VaultEntry(VaultEntryType.EMPTY, TestFunctions.creatNewDateToCheckFor("2000.01.01-00:03"))));
        // reset onehot and timer
        wantedListOfBuckets.get(2).setValueTimer(0, 0);
        wantedListOfBuckets.get(2).setValues(0, 0);
        wantedListOfBuckets.add(new BucketEntry(4, new VaultEntry(VaultEntryType.EMPTY, TestFunctions.creatNewDateToCheckFor("2000.01.01-00:04"))));
        // reset onehot and timer
        wantedListOfBuckets.get(3).setValueTimer(0, 0);
        wantedListOfBuckets.get(3).setValues(0, 0);
        wantedListOfBuckets.add(new BucketEntry(5, new VaultEntry(VaultEntryType.EMPTY, TestFunctions.creatNewDateToCheckFor("2000.01.01-00:05"))));
        // reset onehot and timer
        wantedListOfBuckets.get(4).setValueTimer(0, 0);
        wantedListOfBuckets.get(4).setValues(0, 0);
        wantedListOfBuckets.add(new BucketEntry(6, new VaultEntry(VaultEntryType.EMPTY, TestFunctions.creatNewDateToCheckFor("2000.01.01-00:06"))));
        // reset onehot and timer
        wantedListOfBuckets.get(5).setValueTimer(0, 0);
        wantedListOfBuckets.get(5).setValues(0, 0);
        wantedListOfBuckets.add(new BucketEntry(7, new VaultEntry(VaultEntryType.EMPTY, TestFunctions.creatNewDateToCheckFor("2000.01.01-00:07"))));
        // reset onehot and timer
        wantedListOfBuckets.get(6).setValueTimer(0, 0);
        wantedListOfBuckets.get(6).setValues(0, 0);
        wantedListOfBuckets.add(new BucketEntry(8, new VaultEntry(VaultEntryType.EMPTY, TestFunctions.creatNewDateToCheckFor("2000.01.01-00:08"))));
        // reset onehot and timer
        wantedListOfBuckets.get(7).setValueTimer(0, 0);
        wantedListOfBuckets.get(7).setValues(0, 0);
        wantedListOfBuckets.add(new BucketEntry(9, new VaultEntry(VaultEntryType.EMPTY, TestFunctions.creatNewDateToCheckFor("2000.01.01-00:09"))));
        // reset onehot and timer
        wantedListOfBuckets.get(8).setValueTimer(0, 0);
        wantedListOfBuckets.get(8).setValues(0, 0);
        wantedListOfBuckets.add(new BucketEntry(10, new VaultEntry(VaultEntryType.EMPTY, TestFunctions.creatNewDateToCheckFor("2000.01.01-00:10"))));
        // reset onehot and timer
        wantedListOfBuckets.get(9).setValueTimer(0, 0);
        wantedListOfBuckets.get(9).setValues(0, 0);
        wantedListOfBuckets.add(new BucketEntry(11, new VaultEntry(VaultEntryType.EMPTY, TestFunctions.creatNewDateToCheckFor("2000.01.01-00:11"))));
        // reset onehot and timer
        wantedListOfBuckets.get(10).setValueTimer(0, 0);
        wantedListOfBuckets.get(10).setValues(0, 0);
        wantedListOfBuckets.add(new BucketEntry(12, new VaultEntry(VaultEntryType.EMPTY, TestFunctions.creatNewDateToCheckFor("2000.01.01-00:12"))));
        // reset onehot and timer
        wantedListOfBuckets.get(11).setValueTimer(0, 0);
        wantedListOfBuckets.get(11).setValues(0, 0);
        wantedListOfBuckets.add(new BucketEntry(13, new VaultEntry(VaultEntryType.EMPTY, TestFunctions.creatNewDateToCheckFor("2000.01.01-00:13"))));
        // reset onehot and timer
        wantedListOfBuckets.get(12).setValueTimer(0, 0);
        wantedListOfBuckets.get(12).setValues(0, 0);
        wantedListOfBuckets.add(new BucketEntry(14, new VaultEntry(VaultEntryType.EMPTY, TestFunctions.creatNewDateToCheckFor("2000.01.01-00:14"))));
        // reset onehot and timer
        wantedListOfBuckets.get(13).setValueTimer(0, 0);
        wantedListOfBuckets.get(13).setValues(0, 0);
        wantedListOfBuckets.add(new BucketEntry(15, new VaultEntry(VaultEntryType.EMPTY, TestFunctions.creatNewDateToCheckFor("2000.01.01-00:15"))));
        // reset onehot and timer
        wantedListOfBuckets.get(14).setValueTimer(0, 0);
        wantedListOfBuckets.get(14).setValues(0, 0);
        wantedListOfBuckets.add(new BucketEntry(16, new VaultEntry(VaultEntryType.EMPTY, TestFunctions.creatNewDateToCheckFor("2000.01.01-00:16"))));
        // reset onehot and timer
        wantedListOfBuckets.get(15).setValueTimer(0, 0);
        wantedListOfBuckets.get(15).setValues(0, 0);
        wantedListOfBuckets.add(new BucketEntry(17, new VaultEntry(VaultEntryType.EMPTY, TestFunctions.creatNewDateToCheckFor("2000.01.01-00:17"))));
        // reset onehot and timer
        wantedListOfBuckets.get(16).setValueTimer(0, 0);
        wantedListOfBuckets.get(16).setValues(0, 0);
        wantedListOfBuckets.add(new BucketEntry(18, new VaultEntry(VaultEntryType.EMPTY, TestFunctions.creatNewDateToCheckFor("2000.01.01-00:18"))));
        // reset onehot and timer
        wantedListOfBuckets.get(17).setValueTimer(0, 0);
        wantedListOfBuckets.get(17).setValues(0, 0);
        wantedListOfBuckets.add(new BucketEntry(19, new VaultEntry(VaultEntryType.EMPTY, TestFunctions.creatNewDateToCheckFor("2000.01.01-00:19"))));
        // reset onehot and timer
        wantedListOfBuckets.get(18).setValueTimer(0, 0);
        wantedListOfBuckets.get(18).setValues(0, 0);
        wantedListOfBuckets.add(new BucketEntry(20, new VaultEntry(VaultEntryType.EMPTY, TestFunctions.creatNewDateToCheckFor("2000.01.01-00:20"))));
        // reset onehot and timer
        wantedListOfBuckets.get(19).setValueTimer(0, 0);
        wantedListOfBuckets.get(19).setValues(0, 0);
        wantedListOfBuckets.add(new BucketEntry(21, new VaultEntry(VaultEntryType.EMPTY, TestFunctions.creatNewDateToCheckFor("2000.01.01-00:21"))));
        // reset onehot and timer
        wantedListOfBuckets.get(20).setValueTimer(0, 0);
        wantedListOfBuckets.get(20).setValues(0, 0);
        wantedListOfBuckets.add(new BucketEntry(22, new VaultEntry(VaultEntryType.EMPTY, TestFunctions.creatNewDateToCheckFor("2000.01.01-00:22"))));
        // reset onehot and timer
        wantedListOfBuckets.get(21).setValueTimer(0, 0);
        wantedListOfBuckets.get(21).setValues(0, 0);
        wantedListOfBuckets.add(new BucketEntry(23, new VaultEntry(VaultEntryType.EMPTY, TestFunctions.creatNewDateToCheckFor("2000.01.01-00:23"))));
        // reset onehot and timer
        wantedListOfBuckets.get(22).setValueTimer(0, 0);
        wantedListOfBuckets.get(22).setValues(0, 0);
        wantedListOfBuckets.add(new BucketEntry(24, new VaultEntry(VaultEntryType.EMPTY, TestFunctions.creatNewDateToCheckFor("2000.01.01-00:24"))));
        // reset onehot and timer
        wantedListOfBuckets.get(23).setValueTimer(0, 0);
        wantedListOfBuckets.get(23).setValues(0, 0);
        wantedListOfBuckets.add(new BucketEntry(25, new VaultEntry(VaultEntryType.EMPTY, TestFunctions.creatNewDateToCheckFor("2000.01.01-00:25"))));
        // reset onehot and timer
        wantedListOfBuckets.get(24).setValueTimer(0, 0);
        wantedListOfBuckets.get(24).setValues(0, 0);
        wantedListOfBuckets.add(new BucketEntry(26, new VaultEntry(VaultEntryType.EMPTY, TestFunctions.creatNewDateToCheckFor("2000.01.01-00:26"))));
        // reset onehot and timer
        wantedListOfBuckets.get(25).setValueTimer(0, 0);
        wantedListOfBuckets.get(25).setValues(0, 0);
        wantedListOfBuckets.add(new BucketEntry(27, new VaultEntry(VaultEntryType.EMPTY, TestFunctions.creatNewDateToCheckFor("2000.01.01-00:27"))));
        // reset onehot and timer
        wantedListOfBuckets.get(26).setValueTimer(0, 0);
        wantedListOfBuckets.get(26).setValues(0, 0);
        wantedListOfBuckets.add(new BucketEntry(28, new VaultEntry(VaultEntryType.EMPTY, TestFunctions.creatNewDateToCheckFor("2000.01.01-00:28"))));
        // reset onehot and timer
        wantedListOfBuckets.get(27).setValueTimer(0, 0);
        wantedListOfBuckets.get(27).setValues(0, 0);
        wantedListOfBuckets.add(new BucketEntry(29, new VaultEntry(VaultEntryType.EMPTY, TestFunctions.creatNewDateToCheckFor("2000.01.01-00:29"))));
        // reset onehot and timer
        wantedListOfBuckets.get(28).setValueTimer(0, 0);
        wantedListOfBuckets.get(28).setValues(0, 0);
        wantedListOfBuckets.add(new BucketEntry(30, new VaultEntry(VaultEntryType.GLUCOSE_CGM_ALERT, TestFunctions.creatNewDateToCheckFor("2000.01.01-00:30"), 170)));
        wantedListOfBuckets.get(29).setValueTimer(0, 1);
        wantedListOfBuckets.get(29).setValues(0, 1);

        testBucketInformation(wantedListOfBuckets, result);
    }

    /**
     * This test checks if a correct list of BucketEntrys is created.
     *
     * @throws ParseException
     */
    @Test
    public void testBucketProcessor_EMPTY_TEST() throws ParseException {
        List<VaultEntry> vaultEntries = new ArrayList<>();

        bucketCreator = new ListOfBucketEntriesCreator();
        result = bucketCreator.createListOfBuckets(1, vaultEntries);

        List<BucketEntry> wantedListOfBuckets = new ArrayList<>();

        testBucketInformation(wantedListOfBuckets, result);
    }

    /**
     * This test checks if a correct list of BucketEntrys is created.
     *
     * @throws ParseException
     */
    @Test(expected = NullPointerException.class)
    public void testBucketProcessor_NULL_TEST() throws ParseException {
        List<VaultEntry> vaultEntries = null;

        bucketCreator = new ListOfBucketEntriesCreator();
        result = bucketCreator.createListOfBuckets(1, vaultEntries);

        List<BucketEntry> wantedListOfBuckets = new ArrayList<>();

        testBucketInformation(wantedListOfBuckets, result);
    }

    // ======================================
    // ======================================
    // ======================================
//    @Test
//    public void testBucketProcessor_runable() throws ParseException {
//        List<VaultEntry> vaultEntries = new ArrayList<>();
//
//        vaultEntries = StaticDataset.getStaticDataset();
//
//        BucketProcessor_runable instance_runable = new BucketProcessor_runable();
//        List<FinalBucketEntry> result_runable = instance_runable.processor(vaultEntries, 1);
//
//        List<FinalBucketEntry> wantedListOfBuckets = null;
//
//    //    testBucketInformation(wantedListOfBuckets, result_runable);
//    }
//
//    @Test
//    public void testBucketProcessor_original() throws ParseException {
//        List<VaultEntry> vaultEntries = new ArrayList<>();
//
//        vaultEntries = StaticDataset.getStaticDataset();
//
//        BucketProcessor_old instance = new BucketProcessor_old();
//        List<FinalBucketEntry> result = instance.processor(vaultEntries, 1);
//
//        List<FinalBucketEntry> wantedListOfBuckets = null;
//
//    //    testBucketInformation(wantedListOfBuckets, result_runable);
//    }
}
