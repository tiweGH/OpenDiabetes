/*
 * Copyright (C) 2018 aa80hifa
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
package de.jhit.opendiabetes.vault.processing;

import de.jhit.opendiabetes.vault.container.BucketEntry;
import static de.jhit.opendiabetes.vault.container.BucketEntry.getNumberOfVaultEntryTriggerTypes;
import de.jhit.opendiabetes.vault.container.VaultEntry;
import de.jhit.opendiabetes.vault.container.VaultEntryAnnotation;
import de.jhit.opendiabetes.vault.container.VaultEntryType;
import de.jhit.opendiabetes.vault.processing.filter.TestFunctions;
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
 * @author aa80hifa
 */
public class BucketProcessorTest extends Assert {

    List<BucketEntry> result;
    BucketProcessor instance;

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
    private void testBucketInformation(List<BucketEntry> result, List<BucketEntry> wantedListOfBuckets) {
        // assertEquals(result, wantedListOfBuckets);
        int i;
        for (i = 0; i < wantedListOfBuckets.size(); i++) {
            assertEquals(wantedListOfBuckets.get(i).getBucketNumber(), result.get(i).getBucketNumber());
            assertEquals(wantedListOfBuckets.get(i).getVaultEntry(), result.get(i).getVaultEntry());
            int m;
            for (m = 0; m < getNumberOfVaultEntryTriggerTypes(); m++) {
                assertEquals(Double.valueOf(wantedListOfBuckets.get(i).getTimeCountDown(m)), Double.valueOf(result.get(i).getTimeCountDown(m)));
                assertEquals(Double.valueOf(wantedListOfBuckets.get(i).getOnehotInformationArray(m)), Double.valueOf(result.get(i).getOnehotInformationArray(m)));
                assertEquals(wantedListOfBuckets.get(i).getFindNextArray(m), result.get(i).getFindNextArray(m));
//                assertTrue(result.get(i).getTimeCountDown(m) == wantedListOfBuckets.get(i).getTimeCountDown(m));
//                assertTrue(result.get(i).getOnehotInformationArray(m) == wantedListOfBuckets.get(i).getOnehotInformationArray(m));
//                assertTrue(result.get(i).getFindNextArray(m) == wantedListOfBuckets.get(i).getFindNextArray(m));
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
    @Test
    public void testBucketProcessor_1() throws ParseException {
        List<VaultEntry> vaultEntries = new ArrayList<>();
        vaultEntries.add(new VaultEntry(VaultEntryType.GLUCOSE_CGM_ALERT, TestFunctions.creatNewDateToCheckFor("2000.01.01-00:01"), 170));

        instance = new BucketProcessor();
        result = instance.createListOfBuckets(vaultEntries);

        List<BucketEntry> wantedListOfBuckets = new ArrayList<>();
        wantedListOfBuckets.add(new BucketEntry(1, new VaultEntry(VaultEntryType.GLUCOSE_CGM_ALERT, TestFunctions.creatNewDateToCheckFor("2000.01.01-00:01"), 170)));
        wantedListOfBuckets.get(0).setTimeCountDown(0, 1);
        wantedListOfBuckets.get(0).setOnehotInformationArray(0, 1);

        testBucketInformation(result, wantedListOfBuckets);
    }

    @Test
    public void testBucketProcessor_2_date_before() throws ParseException {
        List<VaultEntry> vaultEntries = new ArrayList<>();

        vaultEntries.add(new VaultEntry(VaultEntryType.GLUCOSE_CGM_ALERT, TestFunctions.creatNewDateToCheckFor("2000.01.01-00:01"), 170));
        vaultEntries.add(new VaultEntry(VaultEntryType.GLUCOSE_CGM_ALERT, TestFunctions.creatNewDateToCheckFor("2000.01.01-00:01"), 182));

        instance = new BucketProcessor();
        result = instance.createListOfBuckets(vaultEntries);

        List<BucketEntry> wantedListOfBuckets = new ArrayList<>();
        wantedListOfBuckets.add(new BucketEntry(1, new VaultEntry(VaultEntryType.GLUCOSE_CGM_ALERT, TestFunctions.creatNewDateToCheckFor("2000.01.01-00:01"), 170)));
        wantedListOfBuckets.get(0).setTimeCountDown(0, 1);
        wantedListOfBuckets.get(0).setOnehotInformationArray(0, 1);
        wantedListOfBuckets.add(new BucketEntry(2, new VaultEntry(VaultEntryType.GLUCOSE_CGM_ALERT, TestFunctions.creatNewDateToCheckFor("2000.01.01-00:01"), 182)));
        wantedListOfBuckets.get(1).setTimeCountDown(0, 1);
        wantedListOfBuckets.get(1).setOnehotInformationArray(0, 1);

        testBucketInformation(result, wantedListOfBuckets);
    }

    @Test
    public void testBucketProcessor_2_date_equal() throws ParseException {
        List<VaultEntry> vaultEntries = new ArrayList<>();

        vaultEntries.add(new VaultEntry(VaultEntryType.GLUCOSE_CGM_ALERT, TestFunctions.creatNewDateToCheckFor("2000.01.01-00:01"), 170));
        vaultEntries.add(new VaultEntry(VaultEntryType.GLUCOSE_CGM_ALERT, TestFunctions.creatNewDateToCheckFor("2000.01.01-00:02"), 182));

        instance = new BucketProcessor();
        result = instance.createListOfBuckets(vaultEntries);

        List<BucketEntry> wantedListOfBuckets = new ArrayList<>();
        wantedListOfBuckets.add(new BucketEntry(1, new VaultEntry(VaultEntryType.GLUCOSE_CGM_ALERT, TestFunctions.creatNewDateToCheckFor("2000.01.01-00:01"), 170)));
        wantedListOfBuckets.get(0).setTimeCountDown(0, 1);
        wantedListOfBuckets.get(0).setOnehotInformationArray(0, 1);
        wantedListOfBuckets.add(new BucketEntry(2, new VaultEntry(VaultEntryType.GLUCOSE_CGM_ALERT, TestFunctions.creatNewDateToCheckFor("2000.01.01-00:02"), 182)));
        wantedListOfBuckets.get(1).setTimeCountDown(0, 1);
        wantedListOfBuckets.get(1).setOnehotInformationArray(0, 1);

        testBucketInformation(result, wantedListOfBuckets);
    }

    @Test
    public void testBucketProcessor_2_date_after() throws ParseException {
        List<VaultEntry> vaultEntries = new ArrayList<>();

        vaultEntries.add(new VaultEntry(VaultEntryType.GLUCOSE_CGM_ALERT, TestFunctions.creatNewDateToCheckFor("2000.01.01-00:01"), 170));
        vaultEntries.add(new VaultEntry(VaultEntryType.GLUCOSE_CGM_ALERT, TestFunctions.creatNewDateToCheckFor("2000.01.01-00:03"), 182));

        instance = new BucketProcessor();
        result = instance.createListOfBuckets(vaultEntries);

        List<BucketEntry> wantedListOfBuckets = new ArrayList<>();
        wantedListOfBuckets.add(new BucketEntry(1, new VaultEntry(VaultEntryType.GLUCOSE_CGM_ALERT, TestFunctions.creatNewDateToCheckFor("2000.01.01-00:01"), 170)));
        wantedListOfBuckets.get(0).setTimeCountDown(0, 1);
        wantedListOfBuckets.get(0).setOnehotInformationArray(0, 1);
        wantedListOfBuckets.add(new BucketEntry(2, new VaultEntry(VaultEntryType.EMPTY, TestFunctions.creatNewDateToCheckFor("2000.01.01-00:02"))));
        // reset onehot and timer
        wantedListOfBuckets.get(1).setTimeCountDown(0, 0);
        wantedListOfBuckets.get(1).setOnehotInformationArray(0, 0);
        wantedListOfBuckets.add(new BucketEntry(3, new VaultEntry(VaultEntryType.GLUCOSE_CGM_ALERT, TestFunctions.creatNewDateToCheckFor("2000.01.01-00:03"), 182)));
        wantedListOfBuckets.get(2).setTimeCountDown(0, 1);
        wantedListOfBuckets.get(2).setOnehotInformationArray(0, 1);

        testBucketInformation(result, wantedListOfBuckets);
    }

    @Test
    public void testBucketProcessor_3() throws ParseException {
        List<VaultEntry> vaultEntries = new ArrayList<>();
        List<VaultEntryAnnotation> tmpAnnotations;

        vaultEntries.add(new VaultEntry(VaultEntryType.GLUCOSE_CGM_ALERT, TestFunctions.creatNewDateToCheckFor("2000.01.01-00:01"), 170));
        tmpAnnotations = new ArrayList<>();
        tmpAnnotations.add(new VaultEntryAnnotation(VaultEntryAnnotation.TYPE.GLUCOSE_BG_METER_SERIAL).setValue("BG1140084B"));
        vaultEntries.add(new VaultEntry(VaultEntryType.GLUCOSE_BG, TestFunctions.creatNewDateToCheckFor("2000.01.01-00:02"), 109.0, tmpAnnotations));
        vaultEntries.add(new VaultEntry(VaultEntryType.GLUCOSE_CGM_ALERT, TestFunctions.creatNewDateToCheckFor("2000.01.01-00:03"), 182));

        instance = new BucketProcessor();
        result = instance.createListOfBuckets(vaultEntries);

        List<BucketEntry> wantedListOfBuckets = new ArrayList<>();
        wantedListOfBuckets.add(new BucketEntry(1, new VaultEntry(VaultEntryType.GLUCOSE_CGM_ALERT, TestFunctions.creatNewDateToCheckFor("2000.01.01-00:01"), 170)));
        wantedListOfBuckets.get(0).setTimeCountDown(0, 1);
        wantedListOfBuckets.get(0).setOnehotInformationArray(0, 1);
        tmpAnnotations = new ArrayList<>();
        tmpAnnotations.add(new VaultEntryAnnotation(VaultEntryAnnotation.TYPE.GLUCOSE_BG_METER_SERIAL).setValue("BG1140084B"));
        wantedListOfBuckets.add(new BucketEntry(2, new VaultEntry(VaultEntryType.GLUCOSE_BG, TestFunctions.creatNewDateToCheckFor("2000.01.01-00:02"), 109.0, tmpAnnotations)));
        // reset onehot and timer
        wantedListOfBuckets.get(1).setTimeCountDown(0, 0);
        wantedListOfBuckets.get(1).setOnehotInformationArray(0, 0);
        wantedListOfBuckets.add(new BucketEntry(3, new VaultEntry(VaultEntryType.GLUCOSE_CGM_ALERT, TestFunctions.creatNewDateToCheckFor("2000.01.01-00:03"), 182)));
        wantedListOfBuckets.get(2).setTimeCountDown(0, 1);
        wantedListOfBuckets.get(2).setOnehotInformationArray(0, 1);

        testBucketInformation(result, wantedListOfBuckets);
    }

    @Test
    public void testBucketProcessor_replace_a_bucket_entry() throws ParseException {
        List<VaultEntry> vaultEntries = new ArrayList<>();

        vaultEntries.add(new VaultEntry(VaultEntryType.GLUCOSE_CGM_ALERT, TestFunctions.creatNewDateToCheckFor("2000.01.01-00:08"), 170));
        vaultEntries.add(new VaultEntry(VaultEntryType.STRESS, TestFunctions.creatNewDateToCheckFor("2000.01.01-00:08"), 21, 5));

        instance = new BucketProcessor();
        result = instance.createListOfBuckets(vaultEntries);

        List<BucketEntry> wantedListOfBuckets = new ArrayList<>();

        wantedListOfBuckets.add(new BucketEntry(1, new VaultEntry(VaultEntryType.GLUCOSE_CGM_ALERT, TestFunctions.creatNewDateToCheckFor("2000.01.01-00:08"), 170)));
        wantedListOfBuckets.get(0).setTimeCountDown(0, 1);
        wantedListOfBuckets.get(0).setOnehotInformationArray(0, 1);

        testBucketInformation(result, wantedListOfBuckets);
    }

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

        instance = new BucketProcessor();
        result = instance.createListOfBuckets(vaultEntries);

        // 9 BucketEntrys -- 1 replaced and 3 timestamps missing -- 5 ML-relevant
        List<BucketEntry> wantedListOfBuckets = new ArrayList<>();
        wantedListOfBuckets.add(new BucketEntry(1, new VaultEntry(VaultEntryType.GLUCOSE_CGM_ALERT, TestFunctions.creatNewDateToCheckFor("2000.01.01-00:01"), 170)));
        wantedListOfBuckets.get(0).setTimeCountDown(0, 1);
        wantedListOfBuckets.get(0).setOnehotInformationArray(0, 1);
        wantedListOfBuckets.add(new BucketEntry(2, new VaultEntry(VaultEntryType.GLUCOSE_CGM_ALERT, TestFunctions.creatNewDateToCheckFor("2000.01.01-00:01"), 170)));
        wantedListOfBuckets.get(1).setTimeCountDown(0, 1);
        wantedListOfBuckets.get(1).setOnehotInformationArray(0, 1);
        wantedListOfBuckets.add(new BucketEntry(3, new VaultEntry(VaultEntryType.GLUCOSE_CGM_ALERT, TestFunctions.creatNewDateToCheckFor("2000.01.01-00:02"), 170)));
        wantedListOfBuckets.get(2).setTimeCountDown(0, 1);
        wantedListOfBuckets.get(2).setOnehotInformationArray(0, 1);
        wantedListOfBuckets.add(new BucketEntry(4, new VaultEntry(VaultEntryType.EMPTY, TestFunctions.creatNewDateToCheckFor("2000.01.01-00:03"))));
        // reset onehot and timer
        wantedListOfBuckets.get(3).setTimeCountDown(0, 0);
        wantedListOfBuckets.get(3).setOnehotInformationArray(0, 0);
        wantedListOfBuckets.add(new BucketEntry(5, new VaultEntry(VaultEntryType.EMPTY, TestFunctions.creatNewDateToCheckFor("2000.01.01-00:04"))));
        // reset onehot and timer
        wantedListOfBuckets.get(4).setTimeCountDown(0, 0);
        wantedListOfBuckets.get(4).setOnehotInformationArray(0, 0);
        wantedListOfBuckets.add(new BucketEntry(6, new VaultEntry(VaultEntryType.GLUCOSE_CGM_ALERT, TestFunctions.creatNewDateToCheckFor("2000.01.01-00:05"), 170)));
        wantedListOfBuckets.get(5).setTimeCountDown(0, 1);
        wantedListOfBuckets.get(5).setOnehotInformationArray(0, 1);
        wantedListOfBuckets.add(new BucketEntry(7, new VaultEntry(VaultEntryType.EMPTY, TestFunctions.creatNewDateToCheckFor("2000.01.01-00:06"))));
        // reset onehot and timer
        wantedListOfBuckets.get(6).setTimeCountDown(0, 0);
        wantedListOfBuckets.get(6).setOnehotInformationArray(0, 0);
        wantedListOfBuckets.add(new BucketEntry(8, new VaultEntry(VaultEntryType.EMPTY, TestFunctions.creatNewDateToCheckFor("2000.01.01-00:07"))));
        // reset onehot and timer
        wantedListOfBuckets.get(7).setTimeCountDown(0, 0);
        wantedListOfBuckets.get(7).setOnehotInformationArray(0, 0);
        wantedListOfBuckets.add(new BucketEntry(9, new VaultEntry(VaultEntryType.GLUCOSE_CGM_ALERT, TestFunctions.creatNewDateToCheckFor("2000.01.01-00:08"), 170)));
        wantedListOfBuckets.get(8).setTimeCountDown(0, 1);
        wantedListOfBuckets.get(8).setOnehotInformationArray(0, 1);

        testBucketInformation(result, wantedListOfBuckets);
    }

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

        instance = new BucketProcessor();
        result = instance.createListOfBuckets(vaultEntries);

        // 9 BucketEntrys -- 2 replaced and 3 timestamps missing -- 5 ML-relevant
        List<BucketEntry> wantedListOfBuckets = new ArrayList<>();
        wantedListOfBuckets.add(new BucketEntry(1, new VaultEntry(VaultEntryType.GLUCOSE_CGM_ALERT, TestFunctions.creatNewDateToCheckFor("2000.01.01-00:01"), 170)));
        wantedListOfBuckets.get(0).setTimeCountDown(0, 1);
        wantedListOfBuckets.get(0).setOnehotInformationArray(0, 1);
        wantedListOfBuckets.add(new BucketEntry(2, new VaultEntry(VaultEntryType.GLUCOSE_CGM_ALERT, TestFunctions.creatNewDateToCheckFor("2000.01.01-00:01"), 170)));
        wantedListOfBuckets.get(1).setTimeCountDown(0, 1);
        wantedListOfBuckets.get(1).setOnehotInformationArray(0, 1);
        wantedListOfBuckets.add(new BucketEntry(3, new VaultEntry(VaultEntryType.GLUCOSE_CGM_ALERT, TestFunctions.creatNewDateToCheckFor("2000.01.01-00:02"), 170)));
        wantedListOfBuckets.get(2).setTimeCountDown(0, 1);
        wantedListOfBuckets.get(2).setOnehotInformationArray(0, 1);
        wantedListOfBuckets.add(new BucketEntry(4, new VaultEntry(VaultEntryType.EMPTY, TestFunctions.creatNewDateToCheckFor("2000.01.01-00:03"))));
        // reset onehot and timer
        wantedListOfBuckets.get(3).setTimeCountDown(0, 0);
        wantedListOfBuckets.get(3).setOnehotInformationArray(0, 0);
        wantedListOfBuckets.add(new BucketEntry(5, new VaultEntry(VaultEntryType.EMPTY, TestFunctions.creatNewDateToCheckFor("2000.01.01-00:04"))));
        // reset onehot and timer
        wantedListOfBuckets.get(4).setTimeCountDown(0, 0);
        wantedListOfBuckets.get(4).setOnehotInformationArray(0, 0);
        wantedListOfBuckets.add(new BucketEntry(6, new VaultEntry(VaultEntryType.GLUCOSE_CGM_ALERT, TestFunctions.creatNewDateToCheckFor("2000.01.01-00:05"), 170)));
        wantedListOfBuckets.get(5).setTimeCountDown(0, 1);
        wantedListOfBuckets.get(5).setOnehotInformationArray(0, 1);
        wantedListOfBuckets.add(new BucketEntry(7, new VaultEntry(VaultEntryType.EMPTY, TestFunctions.creatNewDateToCheckFor("2000.01.01-00:06"))));
        // reset onehot and timer
        wantedListOfBuckets.get(6).setTimeCountDown(0, 0);
        wantedListOfBuckets.get(6).setOnehotInformationArray(0, 0);
        wantedListOfBuckets.add(new BucketEntry(8, new VaultEntry(VaultEntryType.EMPTY, TestFunctions.creatNewDateToCheckFor("2000.01.01-00:07"))));
        // reset onehot and timer
        wantedListOfBuckets.get(7).setTimeCountDown(0, 0);
        wantedListOfBuckets.get(7).setOnehotInformationArray(0, 0);
        wantedListOfBuckets.add(new BucketEntry(9, new VaultEntry(VaultEntryType.GLUCOSE_CGM_ALERT, TestFunctions.creatNewDateToCheckFor("2000.01.01-00:08"), 170)));
        wantedListOfBuckets.get(8).setTimeCountDown(0, 1);
        wantedListOfBuckets.get(8).setOnehotInformationArray(0, 1);

        testBucketInformation(result, wantedListOfBuckets);
    }

    //
    // TEST_6
    //
    @Test
    public void testBucketProcessor_7_replace_first_BucketEntry() throws ParseException {
        List<VaultEntry> vaultEntries = new ArrayList<>();

        vaultEntries.add(new VaultEntry(VaultEntryType.STRESS, TestFunctions.creatNewDateToCheckFor("2000.01.01-00:01"), 21, 5));
        vaultEntries.add(new VaultEntry(VaultEntryType.GLUCOSE_CGM_ALERT, TestFunctions.creatNewDateToCheckFor("2000.01.01-00:01"), 170));
        vaultEntries.add(new VaultEntry(VaultEntryType.GLUCOSE_CGM_ALERT, TestFunctions.creatNewDateToCheckFor("2000.01.01-00:01"), 170));

        instance = new BucketProcessor();
        result = instance.createListOfBuckets(vaultEntries);

        List<BucketEntry> wantedListOfBuckets = new ArrayList<>();
        wantedListOfBuckets.add(new BucketEntry(1, new VaultEntry(VaultEntryType.GLUCOSE_CGM_ALERT, TestFunctions.creatNewDateToCheckFor("2000.01.01-00:01"), 170)));
        wantedListOfBuckets.get(0).setTimeCountDown(0, 1);
        wantedListOfBuckets.get(0).setOnehotInformationArray(0, 1);
        wantedListOfBuckets.add(new BucketEntry(2, new VaultEntry(VaultEntryType.GLUCOSE_CGM_ALERT, TestFunctions.creatNewDateToCheckFor("2000.01.01-00:01"), 170)));
        wantedListOfBuckets.get(1).setTimeCountDown(0, 1);
        wantedListOfBuckets.get(1).setOnehotInformationArray(0, 1);

        testBucketInformation(result, wantedListOfBuckets);
    }

    @Test
    public void testBucketProcessor_7_replace_second_BucketEntry() throws ParseException {
        List<VaultEntry> vaultEntries = new ArrayList<>();

        vaultEntries.add(new VaultEntry(VaultEntryType.GLUCOSE_CGM_ALERT, TestFunctions.creatNewDateToCheckFor("2000.01.01-00:02"), 170));
        vaultEntries.add(new VaultEntry(VaultEntryType.STRESS, TestFunctions.creatNewDateToCheckFor("2000.01.01-00:02"), 21, 5));
        vaultEntries.add(new VaultEntry(VaultEntryType.GLUCOSE_CGM_ALERT, TestFunctions.creatNewDateToCheckFor("2000.01.01-00:02"), 170));

        instance = new BucketProcessor();
        result = instance.createListOfBuckets(vaultEntries);

        List<BucketEntry> wantedListOfBuckets = new ArrayList<>();
        wantedListOfBuckets.add(new BucketEntry(1, new VaultEntry(VaultEntryType.GLUCOSE_CGM_ALERT, TestFunctions.creatNewDateToCheckFor("2000.01.01-00:02"), 170)));
        wantedListOfBuckets.get(0).setTimeCountDown(0, 1);
        wantedListOfBuckets.get(0).setOnehotInformationArray(0, 1);
        wantedListOfBuckets.add(new BucketEntry(2, new VaultEntry(VaultEntryType.GLUCOSE_CGM_ALERT, TestFunctions.creatNewDateToCheckFor("2000.01.01-00:02"), 170)));
        wantedListOfBuckets.get(1).setTimeCountDown(0, 1);
        wantedListOfBuckets.get(1).setOnehotInformationArray(0, 1);

        testBucketInformation(result, wantedListOfBuckets);
    }

    @Test
    public void testBucketProcessor_7_replace_third_BucketEntry() throws ParseException {
        List<VaultEntry> vaultEntries = new ArrayList<>();

        vaultEntries.add(new VaultEntry(VaultEntryType.GLUCOSE_CGM_ALERT, TestFunctions.creatNewDateToCheckFor("2000.01.01-00:03"), 170));
        vaultEntries.add(new VaultEntry(VaultEntryType.GLUCOSE_CGM_ALERT, TestFunctions.creatNewDateToCheckFor("2000.01.01-00:03"), 170));
        vaultEntries.add(new VaultEntry(VaultEntryType.STRESS, TestFunctions.creatNewDateToCheckFor("2000.01.01-00:03"), 21, 5));

        instance = new BucketProcessor();
        result = instance.createListOfBuckets(vaultEntries);

        List<BucketEntry> wantedListOfBuckets = new ArrayList<>();
        wantedListOfBuckets.add(new BucketEntry(1, new VaultEntry(VaultEntryType.GLUCOSE_CGM_ALERT, TestFunctions.creatNewDateToCheckFor("2000.01.01-00:03"), 170)));
        wantedListOfBuckets.get(0).setTimeCountDown(0, 1);
        wantedListOfBuckets.get(0).setOnehotInformationArray(0, 1);
        wantedListOfBuckets.add(new BucketEntry(2, new VaultEntry(VaultEntryType.GLUCOSE_CGM_ALERT, TestFunctions.creatNewDateToCheckFor("2000.01.01-00:03"), 170)));
        wantedListOfBuckets.get(1).setTimeCountDown(0, 1);
        wantedListOfBuckets.get(1).setOnehotInformationArray(0, 1);

        testBucketInformation(result, wantedListOfBuckets);
    }

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

        instance = new BucketProcessor();
        result = instance.createListOfBuckets(vaultEntries);

        List<BucketEntry> wantedListOfBuckets = new ArrayList<>();
        wantedListOfBuckets.add(new BucketEntry(1, new VaultEntry(VaultEntryType.GLUCOSE_CGM_ALERT, TestFunctions.creatNewDateToCheckFor("2000.01.01-00:01"), 170)));
        wantedListOfBuckets.get(0).setTimeCountDown(0, 1);
        wantedListOfBuckets.get(0).setOnehotInformationArray(0, 1);
        wantedListOfBuckets.add(new BucketEntry(2, new VaultEntry(VaultEntryType.GLUCOSE_CGM_ALERT, TestFunctions.creatNewDateToCheckFor("2000.01.01-00:01"), 170)));
        wantedListOfBuckets.get(1).setTimeCountDown(0, 1);
        wantedListOfBuckets.get(1).setOnehotInformationArray(0, 1);

        wantedListOfBuckets.add(new BucketEntry(3, new VaultEntry(VaultEntryType.GLUCOSE_CGM_ALERT, TestFunctions.creatNewDateToCheckFor("2000.01.01-00:02"), 170)));
        wantedListOfBuckets.get(2).setTimeCountDown(0, 1);
        wantedListOfBuckets.get(2).setOnehotInformationArray(0, 1);
        wantedListOfBuckets.add(new BucketEntry(4, new VaultEntry(VaultEntryType.GLUCOSE_CGM_ALERT, TestFunctions.creatNewDateToCheckFor("2000.01.01-00:02"), 170)));
        wantedListOfBuckets.get(3).setTimeCountDown(0, 1);
        wantedListOfBuckets.get(3).setOnehotInformationArray(0, 1);

        wantedListOfBuckets.add(new BucketEntry(5, new VaultEntry(VaultEntryType.GLUCOSE_CGM_ALERT, TestFunctions.creatNewDateToCheckFor("2000.01.01-00:03"), 170)));
        wantedListOfBuckets.get(4).setTimeCountDown(0, 1);
        wantedListOfBuckets.get(4).setOnehotInformationArray(0, 1);
        wantedListOfBuckets.add(new BucketEntry(6, new VaultEntry(VaultEntryType.GLUCOSE_CGM_ALERT, TestFunctions.creatNewDateToCheckFor("2000.01.01-00:03"), 170)));
        wantedListOfBuckets.get(5).setTimeCountDown(0, 1);
        wantedListOfBuckets.get(5).setOnehotInformationArray(0, 1);

        testBucketInformation(result, wantedListOfBuckets);
    }

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

        instance = new BucketProcessor();
        result = instance.createListOfBuckets(vaultEntries);

        List<BucketEntry> wantedListOfBuckets = new ArrayList<>();
        wantedListOfBuckets.add(new BucketEntry(1, new VaultEntry(VaultEntryType.GLUCOSE_CGM_ALERT, TestFunctions.creatNewDateToCheckFor("2000.01.01-00:01"), 170)));
        wantedListOfBuckets.get(0).setTimeCountDown(0, 1);
        wantedListOfBuckets.get(0).setOnehotInformationArray(0, 1);
        wantedListOfBuckets.add(new BucketEntry(2, new VaultEntry(VaultEntryType.GLUCOSE_CGM_ALERT, TestFunctions.creatNewDateToCheckFor("2000.01.01-00:01"), 170)));
        wantedListOfBuckets.get(1).setTimeCountDown(0, 1);
        wantedListOfBuckets.get(1).setOnehotInformationArray(0, 1);

        wantedListOfBuckets.add(new BucketEntry(3, new VaultEntry(VaultEntryType.GLUCOSE_CGM_ALERT, TestFunctions.creatNewDateToCheckFor("2000.01.01-00:02"), 170)));
        wantedListOfBuckets.get(2).setTimeCountDown(0, 1);
        wantedListOfBuckets.get(2).setOnehotInformationArray(0, 1);
        wantedListOfBuckets.add(new BucketEntry(4, new VaultEntry(VaultEntryType.GLUCOSE_CGM_ALERT, TestFunctions.creatNewDateToCheckFor("2000.01.01-00:02"), 170)));
        wantedListOfBuckets.get(3).setTimeCountDown(0, 1);
        wantedListOfBuckets.get(3).setOnehotInformationArray(0, 1);

        wantedListOfBuckets.add(new BucketEntry(5, new VaultEntry(VaultEntryType.GLUCOSE_CGM_ALERT, TestFunctions.creatNewDateToCheckFor("2000.01.01-00:03"), 170)));
        wantedListOfBuckets.get(4).setTimeCountDown(0, 1);
        wantedListOfBuckets.get(4).setOnehotInformationArray(0, 1);
        wantedListOfBuckets.add(new BucketEntry(6, new VaultEntry(VaultEntryType.GLUCOSE_CGM_ALERT, TestFunctions.creatNewDateToCheckFor("2000.01.01-00:04"), 170)));
        wantedListOfBuckets.get(5).setTimeCountDown(0, 1);
        wantedListOfBuckets.get(5).setOnehotInformationArray(0, 1);
        wantedListOfBuckets.add(new BucketEntry(7, new VaultEntry(VaultEntryType.EMPTY, TestFunctions.creatNewDateToCheckFor("2000.01.01-00:05"))));
        wantedListOfBuckets.get(6).setTimeCountDown(0, 0);
        wantedListOfBuckets.get(6).setOnehotInformationArray(0, 0);

        testBucketInformation(result, wantedListOfBuckets);
    }

    @Test
    public void testBucketProcessor_9_generate_empty_BucketEntrys_1() throws ParseException {
        List<VaultEntry> vaultEntries = new ArrayList<>();

        vaultEntries.add(new VaultEntry(VaultEntryType.STRESS, TestFunctions.creatNewDateToCheckFor("2000.01.01-00:01"), 21, 5));
        // missing timestamps will be filled
        vaultEntries.add(new VaultEntry(VaultEntryType.STRESS, TestFunctions.creatNewDateToCheckFor("2000.01.01-00:30"), 21, 5));

        instance = new BucketProcessor();
        result = instance.createListOfBuckets(vaultEntries);

        List<BucketEntry> wantedListOfBuckets = new ArrayList<>();
        wantedListOfBuckets.add(new BucketEntry(1, new VaultEntry(VaultEntryType.EMPTY, TestFunctions.creatNewDateToCheckFor("2000.01.01-00:01"))));
        // reset onehot and timer
        wantedListOfBuckets.get(0).setTimeCountDown(0, 0);
        wantedListOfBuckets.get(0).setOnehotInformationArray(0, 0);
        wantedListOfBuckets.add(new BucketEntry(2, new VaultEntry(VaultEntryType.EMPTY, TestFunctions.creatNewDateToCheckFor("2000.01.01-00:02"))));
        // reset onehot and timer
        wantedListOfBuckets.get(1).setTimeCountDown(0, 0);
        wantedListOfBuckets.get(1).setOnehotInformationArray(0, 0);
        wantedListOfBuckets.add(new BucketEntry(3, new VaultEntry(VaultEntryType.EMPTY, TestFunctions.creatNewDateToCheckFor("2000.01.01-00:03"))));
        // reset onehot and timer
        wantedListOfBuckets.get(2).setTimeCountDown(0, 0);
        wantedListOfBuckets.get(2).setOnehotInformationArray(0, 0);
        wantedListOfBuckets.add(new BucketEntry(4, new VaultEntry(VaultEntryType.EMPTY, TestFunctions.creatNewDateToCheckFor("2000.01.01-00:04"))));
        // reset onehot and timer
        wantedListOfBuckets.get(3).setTimeCountDown(0, 0);
        wantedListOfBuckets.get(3).setOnehotInformationArray(0, 0);
        wantedListOfBuckets.add(new BucketEntry(5, new VaultEntry(VaultEntryType.EMPTY, TestFunctions.creatNewDateToCheckFor("2000.01.01-00:05"))));
        // reset onehot and timer
        wantedListOfBuckets.get(4).setTimeCountDown(0, 0);
        wantedListOfBuckets.get(4).setOnehotInformationArray(0, 0);
        wantedListOfBuckets.add(new BucketEntry(6, new VaultEntry(VaultEntryType.EMPTY, TestFunctions.creatNewDateToCheckFor("2000.01.01-00:06"))));
        // reset onehot and timer
        wantedListOfBuckets.get(5).setTimeCountDown(0, 0);
        wantedListOfBuckets.get(5).setOnehotInformationArray(0, 0);
        wantedListOfBuckets.add(new BucketEntry(7, new VaultEntry(VaultEntryType.EMPTY, TestFunctions.creatNewDateToCheckFor("2000.01.01-00:07"))));
        // reset onehot and timer
        wantedListOfBuckets.get(6).setTimeCountDown(0, 0);
        wantedListOfBuckets.get(6).setOnehotInformationArray(0, 0);
        wantedListOfBuckets.add(new BucketEntry(8, new VaultEntry(VaultEntryType.EMPTY, TestFunctions.creatNewDateToCheckFor("2000.01.01-00:08"))));
        // reset onehot and timer
        wantedListOfBuckets.get(7).setTimeCountDown(0, 0);
        wantedListOfBuckets.get(7).setOnehotInformationArray(0, 0);
        wantedListOfBuckets.add(new BucketEntry(9, new VaultEntry(VaultEntryType.EMPTY, TestFunctions.creatNewDateToCheckFor("2000.01.01-00:09"))));
        // reset onehot and timer
        wantedListOfBuckets.get(8).setTimeCountDown(0, 0);
        wantedListOfBuckets.get(8).setOnehotInformationArray(0, 0);
        wantedListOfBuckets.add(new BucketEntry(10, new VaultEntry(VaultEntryType.EMPTY, TestFunctions.creatNewDateToCheckFor("2000.01.01-00:10"))));
        // reset onehot and timer
        wantedListOfBuckets.get(9).setTimeCountDown(0, 0);
        wantedListOfBuckets.get(9).setOnehotInformationArray(0, 0);
        wantedListOfBuckets.add(new BucketEntry(11, new VaultEntry(VaultEntryType.EMPTY, TestFunctions.creatNewDateToCheckFor("2000.01.01-00:11"))));
        // reset onehot and timer
        wantedListOfBuckets.get(10).setTimeCountDown(0, 0);
        wantedListOfBuckets.get(10).setOnehotInformationArray(0, 0);
        wantedListOfBuckets.add(new BucketEntry(12, new VaultEntry(VaultEntryType.EMPTY, TestFunctions.creatNewDateToCheckFor("2000.01.01-00:12"))));
        // reset onehot and timer
        wantedListOfBuckets.get(11).setTimeCountDown(0, 0);
        wantedListOfBuckets.get(11).setOnehotInformationArray(0, 0);
        wantedListOfBuckets.add(new BucketEntry(13, new VaultEntry(VaultEntryType.EMPTY, TestFunctions.creatNewDateToCheckFor("2000.01.01-00:13"))));
        // reset onehot and timer
        wantedListOfBuckets.get(12).setTimeCountDown(0, 0);
        wantedListOfBuckets.get(12).setOnehotInformationArray(0, 0);
        wantedListOfBuckets.add(new BucketEntry(14, new VaultEntry(VaultEntryType.EMPTY, TestFunctions.creatNewDateToCheckFor("2000.01.01-00:14"))));
        // reset onehot and timer
        wantedListOfBuckets.get(13).setTimeCountDown(0, 0);
        wantedListOfBuckets.get(13).setOnehotInformationArray(0, 0);
        wantedListOfBuckets.add(new BucketEntry(15, new VaultEntry(VaultEntryType.EMPTY, TestFunctions.creatNewDateToCheckFor("2000.01.01-00:15"))));
        // reset onehot and timer
        wantedListOfBuckets.get(14).setTimeCountDown(0, 0);
        wantedListOfBuckets.get(14).setOnehotInformationArray(0, 0);
        wantedListOfBuckets.add(new BucketEntry(16, new VaultEntry(VaultEntryType.EMPTY, TestFunctions.creatNewDateToCheckFor("2000.01.01-00:16"))));
        // reset onehot and timer
        wantedListOfBuckets.get(15).setTimeCountDown(0, 0);
        wantedListOfBuckets.get(15).setOnehotInformationArray(0, 0);
        wantedListOfBuckets.add(new BucketEntry(17, new VaultEntry(VaultEntryType.EMPTY, TestFunctions.creatNewDateToCheckFor("2000.01.01-00:17"))));
        // reset onehot and timer
        wantedListOfBuckets.get(16).setTimeCountDown(0, 0);
        wantedListOfBuckets.get(16).setOnehotInformationArray(0, 0);
        wantedListOfBuckets.add(new BucketEntry(18, new VaultEntry(VaultEntryType.EMPTY, TestFunctions.creatNewDateToCheckFor("2000.01.01-00:18"))));
        // reset onehot and timer
        wantedListOfBuckets.get(17).setTimeCountDown(0, 0);
        wantedListOfBuckets.get(17).setOnehotInformationArray(0, 0);
        wantedListOfBuckets.add(new BucketEntry(19, new VaultEntry(VaultEntryType.EMPTY, TestFunctions.creatNewDateToCheckFor("2000.01.01-00:19"))));
        // reset onehot and timer
        wantedListOfBuckets.get(18).setTimeCountDown(0, 0);
        wantedListOfBuckets.get(18).setOnehotInformationArray(0, 0);
        wantedListOfBuckets.add(new BucketEntry(20, new VaultEntry(VaultEntryType.EMPTY, TestFunctions.creatNewDateToCheckFor("2000.01.01-00:20"))));
        // reset onehot and timer
        wantedListOfBuckets.get(19).setTimeCountDown(0, 0);
        wantedListOfBuckets.get(19).setOnehotInformationArray(0, 0);
        wantedListOfBuckets.add(new BucketEntry(21, new VaultEntry(VaultEntryType.EMPTY, TestFunctions.creatNewDateToCheckFor("2000.01.01-00:21"))));
        // reset onehot and timer
        wantedListOfBuckets.get(20).setTimeCountDown(0, 0);
        wantedListOfBuckets.get(20).setOnehotInformationArray(0, 0);
        wantedListOfBuckets.add(new BucketEntry(22, new VaultEntry(VaultEntryType.EMPTY, TestFunctions.creatNewDateToCheckFor("2000.01.01-00:22"))));
        // reset onehot and timer
        wantedListOfBuckets.get(21).setTimeCountDown(0, 0);
        wantedListOfBuckets.get(21).setOnehotInformationArray(0, 0);
        wantedListOfBuckets.add(new BucketEntry(23, new VaultEntry(VaultEntryType.EMPTY, TestFunctions.creatNewDateToCheckFor("2000.01.01-00:23"))));
        // reset onehot and timer
        wantedListOfBuckets.get(22).setTimeCountDown(0, 0);
        wantedListOfBuckets.get(22).setOnehotInformationArray(0, 0);
        wantedListOfBuckets.add(new BucketEntry(24, new VaultEntry(VaultEntryType.EMPTY, TestFunctions.creatNewDateToCheckFor("2000.01.01-00:24"))));
        // reset onehot and timer
        wantedListOfBuckets.get(23).setTimeCountDown(0, 0);
        wantedListOfBuckets.get(23).setOnehotInformationArray(0, 0);
        wantedListOfBuckets.add(new BucketEntry(25, new VaultEntry(VaultEntryType.EMPTY, TestFunctions.creatNewDateToCheckFor("2000.01.01-00:25"))));
        // reset onehot and timer
        wantedListOfBuckets.get(24).setTimeCountDown(0, 0);
        wantedListOfBuckets.get(24).setOnehotInformationArray(0, 0);
        wantedListOfBuckets.add(new BucketEntry(26, new VaultEntry(VaultEntryType.EMPTY, TestFunctions.creatNewDateToCheckFor("2000.01.01-00:26"))));
        // reset onehot and timer
        wantedListOfBuckets.get(25).setTimeCountDown(0, 0);
        wantedListOfBuckets.get(25).setOnehotInformationArray(0, 0);
        wantedListOfBuckets.add(new BucketEntry(27, new VaultEntry(VaultEntryType.EMPTY, TestFunctions.creatNewDateToCheckFor("2000.01.01-00:27"))));
        // reset onehot and timer
        wantedListOfBuckets.get(26).setTimeCountDown(0, 0);
        wantedListOfBuckets.get(26).setOnehotInformationArray(0, 0);
        wantedListOfBuckets.add(new BucketEntry(28, new VaultEntry(VaultEntryType.EMPTY, TestFunctions.creatNewDateToCheckFor("2000.01.01-00:28"))));
        // reset onehot and timer
        wantedListOfBuckets.get(27).setTimeCountDown(0, 0);
        wantedListOfBuckets.get(27).setOnehotInformationArray(0, 0);
        wantedListOfBuckets.add(new BucketEntry(29, new VaultEntry(VaultEntryType.EMPTY, TestFunctions.creatNewDateToCheckFor("2000.01.01-00:29"))));
        // reset onehot and timer
        wantedListOfBuckets.get(28).setTimeCountDown(0, 0);
        wantedListOfBuckets.get(28).setOnehotInformationArray(0, 0);
        wantedListOfBuckets.add(new BucketEntry(30, new VaultEntry(VaultEntryType.EMPTY, TestFunctions.creatNewDateToCheckFor("2000.01.01-00:30"))));
        // reset onehot and timer
        wantedListOfBuckets.get(29).setTimeCountDown(0, 0);
        wantedListOfBuckets.get(29).setOnehotInformationArray(0, 0);

        testBucketInformation(result, wantedListOfBuckets);
    }

    @Test
    public void testBucketProcessor_9_generate_empty_BucketEntrys_2() throws ParseException {
        List<VaultEntry> vaultEntries = new ArrayList<>();

        vaultEntries.add(new VaultEntry(VaultEntryType.GLUCOSE_CGM_ALERT, TestFunctions.creatNewDateToCheckFor("2000.01.01-00:01"), 170));
        // missing timestamps will be filled
        vaultEntries.add(new VaultEntry(VaultEntryType.GLUCOSE_CGM_ALERT, TestFunctions.creatNewDateToCheckFor("2000.01.01-00:030"), 170));

        instance = new BucketProcessor();
        result = instance.createListOfBuckets(vaultEntries);

        List<BucketEntry> wantedListOfBuckets = new ArrayList<>();
        wantedListOfBuckets.add(new BucketEntry(1, new VaultEntry(VaultEntryType.GLUCOSE_CGM_ALERT, TestFunctions.creatNewDateToCheckFor("2000.01.01-00:01"), 170)));
        wantedListOfBuckets.get(0).setTimeCountDown(0, 1);
        wantedListOfBuckets.get(0).setOnehotInformationArray(0, 1);
        wantedListOfBuckets.add(new BucketEntry(2, new VaultEntry(VaultEntryType.EMPTY, TestFunctions.creatNewDateToCheckFor("2000.01.01-00:02"))));
        // reset onehot and timer
        wantedListOfBuckets.get(1).setTimeCountDown(0, 0);
        wantedListOfBuckets.get(1).setOnehotInformationArray(0, 0);
        wantedListOfBuckets.add(new BucketEntry(3, new VaultEntry(VaultEntryType.EMPTY, TestFunctions.creatNewDateToCheckFor("2000.01.01-00:03"))));
        // reset onehot and timer
        wantedListOfBuckets.get(2).setTimeCountDown(0, 0);
        wantedListOfBuckets.get(2).setOnehotInformationArray(0, 0);
        wantedListOfBuckets.add(new BucketEntry(4, new VaultEntry(VaultEntryType.EMPTY, TestFunctions.creatNewDateToCheckFor("2000.01.01-00:04"))));
        // reset onehot and timer
        wantedListOfBuckets.get(3).setTimeCountDown(0, 0);
        wantedListOfBuckets.get(3).setOnehotInformationArray(0, 0);
        wantedListOfBuckets.add(new BucketEntry(5, new VaultEntry(VaultEntryType.EMPTY, TestFunctions.creatNewDateToCheckFor("2000.01.01-00:05"))));
        // reset onehot and timer
        wantedListOfBuckets.get(4).setTimeCountDown(0, 0);
        wantedListOfBuckets.get(4).setOnehotInformationArray(0, 0);
        wantedListOfBuckets.add(new BucketEntry(6, new VaultEntry(VaultEntryType.EMPTY, TestFunctions.creatNewDateToCheckFor("2000.01.01-00:06"))));
        // reset onehot and timer
        wantedListOfBuckets.get(5).setTimeCountDown(0, 0);
        wantedListOfBuckets.get(5).setOnehotInformationArray(0, 0);
        wantedListOfBuckets.add(new BucketEntry(7, new VaultEntry(VaultEntryType.EMPTY, TestFunctions.creatNewDateToCheckFor("2000.01.01-00:07"))));
        // reset onehot and timer
        wantedListOfBuckets.get(6).setTimeCountDown(0, 0);
        wantedListOfBuckets.get(6).setOnehotInformationArray(0, 0);
        wantedListOfBuckets.add(new BucketEntry(8, new VaultEntry(VaultEntryType.EMPTY, TestFunctions.creatNewDateToCheckFor("2000.01.01-00:08"))));
        // reset onehot and timer
        wantedListOfBuckets.get(7).setTimeCountDown(0, 0);
        wantedListOfBuckets.get(7).setOnehotInformationArray(0, 0);
        wantedListOfBuckets.add(new BucketEntry(9, new VaultEntry(VaultEntryType.EMPTY, TestFunctions.creatNewDateToCheckFor("2000.01.01-00:09"))));
        // reset onehot and timer
        wantedListOfBuckets.get(8).setTimeCountDown(0, 0);
        wantedListOfBuckets.get(8).setOnehotInformationArray(0, 0);
        wantedListOfBuckets.add(new BucketEntry(10, new VaultEntry(VaultEntryType.EMPTY, TestFunctions.creatNewDateToCheckFor("2000.01.01-00:10"))));
        // reset onehot and timer
        wantedListOfBuckets.get(9).setTimeCountDown(0, 0);
        wantedListOfBuckets.get(9).setOnehotInformationArray(0, 0);
        wantedListOfBuckets.add(new BucketEntry(11, new VaultEntry(VaultEntryType.EMPTY, TestFunctions.creatNewDateToCheckFor("2000.01.01-00:11"))));
        // reset onehot and timer
        wantedListOfBuckets.get(10).setTimeCountDown(0, 0);
        wantedListOfBuckets.get(10).setOnehotInformationArray(0, 0);
        wantedListOfBuckets.add(new BucketEntry(12, new VaultEntry(VaultEntryType.EMPTY, TestFunctions.creatNewDateToCheckFor("2000.01.01-00:12"))));
        // reset onehot and timer
        wantedListOfBuckets.get(11).setTimeCountDown(0, 0);
        wantedListOfBuckets.get(11).setOnehotInformationArray(0, 0);
        wantedListOfBuckets.add(new BucketEntry(13, new VaultEntry(VaultEntryType.EMPTY, TestFunctions.creatNewDateToCheckFor("2000.01.01-00:13"))));
        // reset onehot and timer
        wantedListOfBuckets.get(12).setTimeCountDown(0, 0);
        wantedListOfBuckets.get(12).setOnehotInformationArray(0, 0);
        wantedListOfBuckets.add(new BucketEntry(14, new VaultEntry(VaultEntryType.EMPTY, TestFunctions.creatNewDateToCheckFor("2000.01.01-00:14"))));
        // reset onehot and timer
        wantedListOfBuckets.get(13).setTimeCountDown(0, 0);
        wantedListOfBuckets.get(13).setOnehotInformationArray(0, 0);
        wantedListOfBuckets.add(new BucketEntry(15, new VaultEntry(VaultEntryType.EMPTY, TestFunctions.creatNewDateToCheckFor("2000.01.01-00:15"))));
        // reset onehot and timer
        wantedListOfBuckets.get(14).setTimeCountDown(0, 0);
        wantedListOfBuckets.get(14).setOnehotInformationArray(0, 0);
        wantedListOfBuckets.add(new BucketEntry(16, new VaultEntry(VaultEntryType.EMPTY, TestFunctions.creatNewDateToCheckFor("2000.01.01-00:16"))));
        // reset onehot and timer
        wantedListOfBuckets.get(15).setTimeCountDown(0, 0);
        wantedListOfBuckets.get(15).setOnehotInformationArray(0, 0);
        wantedListOfBuckets.add(new BucketEntry(17, new VaultEntry(VaultEntryType.EMPTY, TestFunctions.creatNewDateToCheckFor("2000.01.01-00:17"))));
        // reset onehot and timer
        wantedListOfBuckets.get(16).setTimeCountDown(0, 0);
        wantedListOfBuckets.get(16).setOnehotInformationArray(0, 0);
        wantedListOfBuckets.add(new BucketEntry(18, new VaultEntry(VaultEntryType.EMPTY, TestFunctions.creatNewDateToCheckFor("2000.01.01-00:18"))));
        // reset onehot and timer
        wantedListOfBuckets.get(17).setTimeCountDown(0, 0);
        wantedListOfBuckets.get(17).setOnehotInformationArray(0, 0);
        wantedListOfBuckets.add(new BucketEntry(19, new VaultEntry(VaultEntryType.EMPTY, TestFunctions.creatNewDateToCheckFor("2000.01.01-00:19"))));
        // reset onehot and timer
        wantedListOfBuckets.get(18).setTimeCountDown(0, 0);
        wantedListOfBuckets.get(18).setOnehotInformationArray(0, 0);
        wantedListOfBuckets.add(new BucketEntry(20, new VaultEntry(VaultEntryType.EMPTY, TestFunctions.creatNewDateToCheckFor("2000.01.01-00:20"))));
        // reset onehot and timer
        wantedListOfBuckets.get(19).setTimeCountDown(0, 0);
        wantedListOfBuckets.get(19).setOnehotInformationArray(0, 0);
        wantedListOfBuckets.add(new BucketEntry(21, new VaultEntry(VaultEntryType.EMPTY, TestFunctions.creatNewDateToCheckFor("2000.01.01-00:21"))));
        // reset onehot and timer
        wantedListOfBuckets.get(20).setTimeCountDown(0, 0);
        wantedListOfBuckets.get(20).setOnehotInformationArray(0, 0);
        wantedListOfBuckets.add(new BucketEntry(22, new VaultEntry(VaultEntryType.EMPTY, TestFunctions.creatNewDateToCheckFor("2000.01.01-00:22"))));
        // reset onehot and timer
        wantedListOfBuckets.get(21).setTimeCountDown(0, 0);
        wantedListOfBuckets.get(21).setOnehotInformationArray(0, 0);
        wantedListOfBuckets.add(new BucketEntry(23, new VaultEntry(VaultEntryType.EMPTY, TestFunctions.creatNewDateToCheckFor("2000.01.01-00:23"))));
        // reset onehot and timer
        wantedListOfBuckets.get(22).setTimeCountDown(0, 0);
        wantedListOfBuckets.get(22).setOnehotInformationArray(0, 0);
        wantedListOfBuckets.add(new BucketEntry(24, new VaultEntry(VaultEntryType.EMPTY, TestFunctions.creatNewDateToCheckFor("2000.01.01-00:24"))));
        // reset onehot and timer
        wantedListOfBuckets.get(23).setTimeCountDown(0, 0);
        wantedListOfBuckets.get(23).setOnehotInformationArray(0, 0);
        wantedListOfBuckets.add(new BucketEntry(25, new VaultEntry(VaultEntryType.EMPTY, TestFunctions.creatNewDateToCheckFor("2000.01.01-00:25"))));
        // reset onehot and timer
        wantedListOfBuckets.get(24).setTimeCountDown(0, 0);
        wantedListOfBuckets.get(24).setOnehotInformationArray(0, 0);
        wantedListOfBuckets.add(new BucketEntry(26, new VaultEntry(VaultEntryType.EMPTY, TestFunctions.creatNewDateToCheckFor("2000.01.01-00:26"))));
        // reset onehot and timer
        wantedListOfBuckets.get(25).setTimeCountDown(0, 0);
        wantedListOfBuckets.get(25).setOnehotInformationArray(0, 0);
        wantedListOfBuckets.add(new BucketEntry(27, new VaultEntry(VaultEntryType.EMPTY, TestFunctions.creatNewDateToCheckFor("2000.01.01-00:27"))));
        // reset onehot and timer
        wantedListOfBuckets.get(26).setTimeCountDown(0, 0);
        wantedListOfBuckets.get(26).setOnehotInformationArray(0, 0);
        wantedListOfBuckets.add(new BucketEntry(28, new VaultEntry(VaultEntryType.EMPTY, TestFunctions.creatNewDateToCheckFor("2000.01.01-00:28"))));
        // reset onehot and timer
        wantedListOfBuckets.get(27).setTimeCountDown(0, 0);
        wantedListOfBuckets.get(27).setOnehotInformationArray(0, 0);
        wantedListOfBuckets.add(new BucketEntry(29, new VaultEntry(VaultEntryType.EMPTY, TestFunctions.creatNewDateToCheckFor("2000.01.01-00:29"))));
        // reset onehot and timer
        wantedListOfBuckets.get(28).setTimeCountDown(0, 0);
        wantedListOfBuckets.get(28).setOnehotInformationArray(0, 0);
        wantedListOfBuckets.add(new BucketEntry(30, new VaultEntry(VaultEntryType.GLUCOSE_CGM_ALERT, TestFunctions.creatNewDateToCheckFor("2000.01.01-00:30"), 170)));
        wantedListOfBuckets.get(29).setTimeCountDown(0, 1);
        wantedListOfBuckets.get(29).setOnehotInformationArray(0, 1);

        testBucketInformation(result, wantedListOfBuckets);
    }

    @Test
    public void testBucketProcessor_EMPTY_TEST() throws ParseException {
        List<VaultEntry> vaultEntries = new ArrayList<>();

        instance = new BucketProcessor();
        result = instance.createListOfBuckets(vaultEntries);

        List<BucketEntry> wantedListOfBuckets = new ArrayList<>();

        testBucketInformation(result, wantedListOfBuckets);
    }

    @Test(expected = NullPointerException.class)
    public void testBucketProcessor_NULL_TEST() throws ParseException {
        List<VaultEntry> vaultEntries = null;

        instance = new BucketProcessor();
        result = instance.createListOfBuckets(vaultEntries);

        List<BucketEntry> wantedListOfBuckets = new ArrayList<>();

        testBucketInformation(result, wantedListOfBuckets);
    }
}
