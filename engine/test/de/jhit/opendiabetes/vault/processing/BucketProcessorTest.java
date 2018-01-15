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
                assertTrue(result.get(i).getTimeCountDown(m) == wantedListOfBuckets.get(i).getTimeCountDown(m));
                assertTrue(result.get(i).getOnehotInformationArray(m) == wantedListOfBuckets.get(i).getOnehotInformationArray(m));
                assertTrue(result.get(i).getFindNextArray(m) == wantedListOfBuckets.get(i).getFindNextArray(m));
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
        wantedListOfBuckets.add(new BucketEntry(1, new VaultEntry(VaultEntryType.GLUCOSE_CGM_ALERT, TestFunctions.creatNewDateToCheckFor("2000.01.01-00:01"), 170) ));
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
        wantedListOfBuckets.add(new BucketEntry(1, new VaultEntry(VaultEntryType.GLUCOSE_CGM_ALERT, TestFunctions.creatNewDateToCheckFor("2000.01.01-00:01"), 170) ));
        wantedListOfBuckets.get(0).setTimeCountDown(0, 1);
        wantedListOfBuckets.get(0).setOnehotInformationArray(0, 1);
        wantedListOfBuckets.add(new BucketEntry(2, new VaultEntry(VaultEntryType.GLUCOSE_CGM_ALERT, TestFunctions.creatNewDateToCheckFor("2000.01.01-00:01"), 182) ));
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
        wantedListOfBuckets.add(new BucketEntry(1, new VaultEntry(VaultEntryType.GLUCOSE_CGM_ALERT, TestFunctions.creatNewDateToCheckFor("2000.01.01-00:01"), 170) ));
        wantedListOfBuckets.get(0).setTimeCountDown(0, 1);
        wantedListOfBuckets.get(0).setOnehotInformationArray(0, 1);
        wantedListOfBuckets.add(new BucketEntry(2, new VaultEntry(VaultEntryType.GLUCOSE_CGM_ALERT, TestFunctions.creatNewDateToCheckFor("2000.01.01-00:02"), 182) ));
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
        wantedListOfBuckets.add(new BucketEntry(1, new VaultEntry(VaultEntryType.GLUCOSE_CGM_ALERT, TestFunctions.creatNewDateToCheckFor("2000.01.01-00:01"), 170) ));
        wantedListOfBuckets.get(0).setTimeCountDown(0, 1);
        wantedListOfBuckets.get(0).setOnehotInformationArray(0, 1);
        wantedListOfBuckets.add(new BucketEntry(2, new VaultEntry(VaultEntryType.EMPTY, TestFunctions.creatNewDateToCheckFor("2000.01.01-00:02")) ));
        wantedListOfBuckets.add(new BucketEntry(3, new VaultEntry(VaultEntryType.GLUCOSE_CGM_ALERT, TestFunctions.creatNewDateToCheckFor("2000.01.01-00:03"), 182) ));
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
        wantedListOfBuckets.add(new BucketEntry(1, new VaultEntry(VaultEntryType.GLUCOSE_CGM_ALERT, TestFunctions.creatNewDateToCheckFor("2000.01.01-00:01"), 170) ));
        wantedListOfBuckets.get(0).setTimeCountDown(0, 1);
        wantedListOfBuckets.get(0).setOnehotInformationArray(0, 1);
        tmpAnnotations = new ArrayList<>();
        tmpAnnotations.add(new VaultEntryAnnotation(VaultEntryAnnotation.TYPE.GLUCOSE_BG_METER_SERIAL).setValue("BG1140084B"));
        wantedListOfBuckets.add(new BucketEntry(2, new VaultEntry(VaultEntryType.GLUCOSE_BG, TestFunctions.creatNewDateToCheckFor("2000.01.01-00:02"), 109.0, tmpAnnotations) ));
        // wantedListOfBuckets.get(1).setTimeCountDown(0, 0);
        // wantedListOfBuckets.get(1).setOnehoteInformationArray(0, 0);
        wantedListOfBuckets.add(new BucketEntry(3, new VaultEntry(VaultEntryType.GLUCOSE_CGM_ALERT, TestFunctions.creatNewDateToCheckFor("2000.01.01-00:03"), 182) ));
        wantedListOfBuckets.get(2).setTimeCountDown(0, 1);
        wantedListOfBuckets.get(2).setOnehotInformationArray(0, 1);
        
        testBucketInformation(result, wantedListOfBuckets);
    }
    
    @Test
    public void testBucketProcessor_replace_a_bucket_entry() throws ParseException {
        List<VaultEntry> vaultEntries = new ArrayList<>();
        
        vaultEntries.add(new VaultEntry(VaultEntryType.GLUCOSE_CGM_ALERT, TestFunctions.creatNewDateToCheckFor("2000.01.01-00:08"), 170));        
        vaultEntries.add(new VaultEntry(VaultEntryType.STRESS, TestFunctions.creatNewDateToCheckFor("2000.01.01-00:08"), 21,5));
        
        instance = new BucketProcessor();
        result = instance.createListOfBuckets(vaultEntries);
        
        List<BucketEntry> wantedListOfBuckets = new ArrayList<>();
        
        wantedListOfBuckets.add(new BucketEntry(1, new VaultEntry(VaultEntryType.GLUCOSE_CGM_ALERT, TestFunctions.creatNewDateToCheckFor("2000.01.01-00:08"), 170) ));
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
        vaultEntries.add(new VaultEntry(VaultEntryType.STRESS, TestFunctions.creatNewDateToCheckFor("2000.01.01-00:03"), 21,5));        
        //
        vaultEntries.add(new VaultEntry(VaultEntryType.GLUCOSE_CGM_ALERT, TestFunctions.creatNewDateToCheckFor("2000.01.01-00:05"), 170));    
        //
        vaultEntries.add(new VaultEntry(VaultEntryType.GLUCOSE_CGM_ALERT, TestFunctions.creatNewDateToCheckFor("2000.01.01-00:08"), 170));        
        vaultEntries.add(new VaultEntry(VaultEntryType.STRESS, TestFunctions.creatNewDateToCheckFor("2000.01.01-00:08"), 21,5));
        
        instance = new BucketProcessor();
        result = instance.createListOfBuckets(vaultEntries);
        
        // 9 BucketEntrys -- 1 replaced and 3 timestamps missing -- 5 ML-relevant
        List<BucketEntry> wantedListOfBuckets = new ArrayList<>();
        wantedListOfBuckets.add(new BucketEntry(1, new VaultEntry(VaultEntryType.GLUCOSE_CGM_ALERT, TestFunctions.creatNewDateToCheckFor("2000.01.01-00:01"), 170) ));
        wantedListOfBuckets.get(0).setTimeCountDown(0, 1);
        wantedListOfBuckets.get(0).setOnehotInformationArray(0, 1);
        wantedListOfBuckets.add(new BucketEntry(2, new VaultEntry(VaultEntryType.GLUCOSE_CGM_ALERT, TestFunctions.creatNewDateToCheckFor("2000.01.01-00:01"), 170) ));
        wantedListOfBuckets.get(1).setTimeCountDown(0, 1);
        wantedListOfBuckets.get(1).setOnehotInformationArray(0, 1);
        wantedListOfBuckets.add(new BucketEntry(3, new VaultEntry(VaultEntryType.GLUCOSE_CGM_ALERT, TestFunctions.creatNewDateToCheckFor("2000.01.01-00:02"), 170) ));
        wantedListOfBuckets.get(2).setTimeCountDown(0, 1);
        wantedListOfBuckets.get(2).setOnehotInformationArray(0, 1);
        wantedListOfBuckets.add(new BucketEntry(4, new VaultEntry(VaultEntryType.EMPTY, TestFunctions.creatNewDateToCheckFor("2000.01.01-00:03")) ));
        wantedListOfBuckets.add(new BucketEntry(5, new VaultEntry(VaultEntryType.EMPTY, TestFunctions.creatNewDateToCheckFor("2000.01.01-00:04")) ));
        wantedListOfBuckets.add(new BucketEntry(6, new VaultEntry(VaultEntryType.GLUCOSE_CGM_ALERT, TestFunctions.creatNewDateToCheckFor("2000.01.01-00:05"), 170) ));
        wantedListOfBuckets.get(5).setTimeCountDown(0, 1);
        wantedListOfBuckets.get(5).setOnehotInformationArray(0, 1);
        wantedListOfBuckets.add(new BucketEntry(7, new VaultEntry(VaultEntryType.EMPTY, TestFunctions.creatNewDateToCheckFor("2000.01.01-00:06")) ));
        wantedListOfBuckets.add(new BucketEntry(8, new VaultEntry(VaultEntryType.EMPTY, TestFunctions.creatNewDateToCheckFor("2000.01.01-00:07")) ));
        wantedListOfBuckets.add(new BucketEntry(9, new VaultEntry(VaultEntryType.GLUCOSE_CGM_ALERT, TestFunctions.creatNewDateToCheckFor("2000.01.01-00:08"), 170) ));
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
        vaultEntries.add(new VaultEntry(VaultEntryType.STRESS, TestFunctions.creatNewDateToCheckFor("2000.01.01-00:03"), 21,5));        
        //
        vaultEntries.add(new VaultEntry(VaultEntryType.GLUCOSE_CGM_ALERT, TestFunctions.creatNewDateToCheckFor("2000.01.01-00:05"), 170));    
        //        
        vaultEntries.add(new VaultEntry(VaultEntryType.STRESS, TestFunctions.creatNewDateToCheckFor("2000.01.01-00:08"), 21,5));
        vaultEntries.add(new VaultEntry(VaultEntryType.GLUCOSE_CGM_ALERT, TestFunctions.creatNewDateToCheckFor("2000.01.01-00:08"), 170));
        
        instance = new BucketProcessor();
        result = instance.createListOfBuckets(vaultEntries);
        
        // 9 BucketEntrys -- 2 replaced and 3 timestamps missing -- 5 ML-relevant 
        List<BucketEntry> wantedListOfBuckets = new ArrayList<>();
        wantedListOfBuckets.add(new BucketEntry(1, new VaultEntry(VaultEntryType.GLUCOSE_CGM_ALERT, TestFunctions.creatNewDateToCheckFor("2000.01.01-00:01"), 170) ));
        wantedListOfBuckets.get(0).setTimeCountDown(0, 1);
        wantedListOfBuckets.get(0).setOnehotInformationArray(0, 1);
        wantedListOfBuckets.add(new BucketEntry(2, new VaultEntry(VaultEntryType.GLUCOSE_CGM_ALERT, TestFunctions.creatNewDateToCheckFor("2000.01.01-00:01"), 170) ));
        wantedListOfBuckets.get(1).setTimeCountDown(0, 1);
        wantedListOfBuckets.get(1).setOnehotInformationArray(0, 1);
        wantedListOfBuckets.add(new BucketEntry(3, new VaultEntry(VaultEntryType.GLUCOSE_CGM_ALERT, TestFunctions.creatNewDateToCheckFor("2000.01.01-00:02"), 170) ));
        wantedListOfBuckets.get(2).setTimeCountDown(0, 1);
        wantedListOfBuckets.get(2).setOnehotInformationArray(0, 1);
        wantedListOfBuckets.add(new BucketEntry(4, new VaultEntry(VaultEntryType.EMPTY, TestFunctions.creatNewDateToCheckFor("2000.01.01-00:03")) ));
        wantedListOfBuckets.add(new BucketEntry(5, new VaultEntry(VaultEntryType.EMPTY, TestFunctions.creatNewDateToCheckFor("2000.01.01-00:04")) ));
        wantedListOfBuckets.add(new BucketEntry(6, new VaultEntry(VaultEntryType.GLUCOSE_CGM_ALERT, TestFunctions.creatNewDateToCheckFor("2000.01.01-00:05"), 170) ));
        wantedListOfBuckets.get(5).setTimeCountDown(0, 1);
        wantedListOfBuckets.get(5).setOnehotInformationArray(0, 1);
        wantedListOfBuckets.add(new BucketEntry(7, new VaultEntry(VaultEntryType.EMPTY, TestFunctions.creatNewDateToCheckFor("2000.01.01-00:06")) ));
        wantedListOfBuckets.add(new BucketEntry(8, new VaultEntry(VaultEntryType.EMPTY, TestFunctions.creatNewDateToCheckFor("2000.01.01-00:07")) ));
        wantedListOfBuckets.add(new BucketEntry(9, new VaultEntry(VaultEntryType.GLUCOSE_CGM_ALERT, TestFunctions.creatNewDateToCheckFor("2000.01.01-00:08"), 170) ));
        wantedListOfBuckets.get(8).setTimeCountDown(0, 1);
        wantedListOfBuckets.get(8).setOnehotInformationArray(0, 1);
        
        testBucketInformation(result, wantedListOfBuckets);
    }
    
    @Test
    public void testBucketProcessor_6() throws ParseException {
        List<VaultEntry> vaultEntries = new ArrayList<>();
        
        vaultEntries.add(new VaultEntry(VaultEntryType.GLUCOSE_CGM_ALERT, TestFunctions.creatNewDateToCheckFor("2000.01.01-00:01"), 170));        
        vaultEntries.add(new VaultEntry(VaultEntryType.GLUCOSE_CGM_ALERT, TestFunctions.creatNewDateToCheckFor("2000.01.01-00:01"), 170));        
        vaultEntries.add(new VaultEntry(VaultEntryType.STRESS, TestFunctions.creatNewDateToCheckFor("2000.01.01-00:01"), 21,5));        
        
        vaultEntries.add(new VaultEntry(VaultEntryType.GLUCOSE_CGM_ALERT, TestFunctions.creatNewDateToCheckFor("2000.01.01-00:02"), 170));    
        vaultEntries.add(new VaultEntry(VaultEntryType.STRESS, TestFunctions.creatNewDateToCheckFor("2000.01.01-00:02"), 21,5));
        vaultEntries.add(new VaultEntry(VaultEntryType.GLUCOSE_CGM_ALERT, TestFunctions.creatNewDateToCheckFor("2000.01.01-00:02"), 170));
        
        vaultEntries.add(new VaultEntry(VaultEntryType.STRESS, TestFunctions.creatNewDateToCheckFor("2000.01.01-00:03"), 21,5));
        vaultEntries.add(new VaultEntry(VaultEntryType.GLUCOSE_CGM_ALERT, TestFunctions.creatNewDateToCheckFor("2000.01.01-00:04"), 170));        
        vaultEntries.add(new VaultEntry(VaultEntryType.GLUCOSE_CGM_ALERT, TestFunctions.creatNewDateToCheckFor("2000.01.01-00:05"), 170));      
        
        vaultEntries.add(new VaultEntry(VaultEntryType.STRESS, TestFunctions.creatNewDateToCheckFor("2000.01.01-00:06"), 21,5));
        //
        //
        
        //
        vaultEntries.add(new VaultEntry(VaultEntryType.STRESS, TestFunctions.creatNewDateToCheckFor("2000.01.01-00:10"), 21,5));
        vaultEntries.add(new VaultEntry(VaultEntryType.GLUCOSE_CGM_ALERT, TestFunctions.creatNewDateToCheckFor("2000.01.01-00:10"), 170));    
        
        //
        vaultEntries.add(new VaultEntry(VaultEntryType.GLUCOSE_CGM_ALERT, TestFunctions.creatNewDateToCheckFor("2000.01.01-00:12"), 170));    
        vaultEntries.add(new VaultEntry(VaultEntryType.STRESS, TestFunctions.creatNewDateToCheckFor("2000.01.01-00:13"), 21,5));
        
        //
        //  1 min -- 00:13 bis 01:12
        //
        
        vaultEntries.add(new VaultEntry(VaultEntryType.STRESS, TestFunctions.creatNewDateToCheckFor("2000.01.01-01:13"), 21,5));
        vaultEntries.add(new VaultEntry(VaultEntryType.STRESS, TestFunctions.creatNewDateToCheckFor("2000.01.01-01:13"), 21,5));
        vaultEntries.add(new VaultEntry(VaultEntryType.STRESS, TestFunctions.creatNewDateToCheckFor("2000.01.01-01:13"), 21,5));
        
        instance = new BucketProcessor();
        result = instance.createListOfBuckets(vaultEntries);
        
        List<BucketEntry> wantedListOfBuckets = new ArrayList<>();
        wantedListOfBuckets.add(new BucketEntry(1, new VaultEntry(VaultEntryType.GLUCOSE_CGM_ALERT, TestFunctions.creatNewDateToCheckFor("2000.01.01-00:01"), 170) ));
        wantedListOfBuckets.get(0).setTimeCountDown(0, 1);
        wantedListOfBuckets.get(0).setOnehotInformationArray(0, 1);
        wantedListOfBuckets.add(new BucketEntry(2, new VaultEntry(VaultEntryType.GLUCOSE_CGM_ALERT, TestFunctions.creatNewDateToCheckFor("2000.01.01-00:01"), 170) ));
        wantedListOfBuckets.get(1).setTimeCountDown(0, 1);
        wantedListOfBuckets.get(1).setOnehotInformationArray(0, 1);
        
        wantedListOfBuckets.add(new BucketEntry(3, new VaultEntry(VaultEntryType.GLUCOSE_CGM_ALERT, TestFunctions.creatNewDateToCheckFor("2000.01.01-00:02"), 170) ));
        wantedListOfBuckets.get(2).setTimeCountDown(0, 1);
        wantedListOfBuckets.get(2).setOnehotInformationArray(0, 1);
        wantedListOfBuckets.add(new BucketEntry(4, new VaultEntry(VaultEntryType.GLUCOSE_CGM_ALERT, TestFunctions.creatNewDateToCheckFor("2000.01.01-00:02"), 170) ));
        wantedListOfBuckets.get(3).setTimeCountDown(0, 1);
        wantedListOfBuckets.get(3).setOnehotInformationArray(0, 1);
        
        wantedListOfBuckets.add(new BucketEntry(5, new VaultEntry(VaultEntryType.EMPTY, TestFunctions.creatNewDateToCheckFor("2000.01.01-00:03")) ));
        wantedListOfBuckets.add(new BucketEntry(6, new VaultEntry(VaultEntryType.GLUCOSE_CGM_ALERT, TestFunctions.creatNewDateToCheckFor("2000.01.01-00:04"), 170) ));
        wantedListOfBuckets.get(5).setTimeCountDown(0, 1);
        wantedListOfBuckets.get(5).setOnehotInformationArray(0, 1);
        wantedListOfBuckets.add(new BucketEntry(7, new VaultEntry(VaultEntryType.GLUCOSE_CGM_ALERT, TestFunctions.creatNewDateToCheckFor("2000.01.01-00:05"), 170) ));
        wantedListOfBuckets.get(6).setTimeCountDown(0, 1);
        wantedListOfBuckets.get(6).setOnehotInformationArray(0, 1);
        
        wantedListOfBuckets.add(new BucketEntry(8, new VaultEntry(VaultEntryType.EMPTY, TestFunctions.creatNewDateToCheckFor("2000.01.01-00:06")) ));
        wantedListOfBuckets.add(new BucketEntry(9, new VaultEntry(VaultEntryType.EMPTY, TestFunctions.creatNewDateToCheckFor("2000.01.01-00:07")) ));
        wantedListOfBuckets.add(new BucketEntry(10, new VaultEntry(VaultEntryType.EMPTY, TestFunctions.creatNewDateToCheckFor("2000.01.01-00:08")) ));
        
        wantedListOfBuckets.add(new BucketEntry(11, new VaultEntry(VaultEntryType.EMPTY, TestFunctions.creatNewDateToCheckFor("2000.01.01-00:09")) ));
        wantedListOfBuckets.add(new BucketEntry(12, new VaultEntry(VaultEntryType.GLUCOSE_CGM_ALERT, TestFunctions.creatNewDateToCheckFor("2000.01.01-00:10"), 170) ));
        wantedListOfBuckets.get(11).setTimeCountDown(0, 1);
        wantedListOfBuckets.get(11).setOnehotInformationArray(0, 1);
        
        wantedListOfBuckets.add(new BucketEntry(13, new VaultEntry(VaultEntryType.EMPTY, TestFunctions.creatNewDateToCheckFor("2000.01.01-00:11")) ));        
        wantedListOfBuckets.add(new BucketEntry(14, new VaultEntry(VaultEntryType.GLUCOSE_CGM_ALERT, TestFunctions.creatNewDateToCheckFor("2000.01.01-00:12"), 170) ));
        wantedListOfBuckets.get(13).setTimeCountDown(0, 1);
        wantedListOfBuckets.get(13).setOnehotInformationArray(0, 1);
        wantedListOfBuckets.add(new BucketEntry(15, new VaultEntry(VaultEntryType.EMPTY, TestFunctions.creatNewDateToCheckFor("2000.01.01-00:13")) ));
        
        wantedListOfBuckets.add(new BucketEntry(16, new VaultEntry(VaultEntryType.EMPTY, TestFunctions.creatNewDateToCheckFor("2000.01.01-00:14")) ));
        wantedListOfBuckets.add(new BucketEntry(17, new VaultEntry(VaultEntryType.EMPTY, TestFunctions.creatNewDateToCheckFor("2000.01.01-00:15")) ));
        wantedListOfBuckets.add(new BucketEntry(18, new VaultEntry(VaultEntryType.EMPTY, TestFunctions.creatNewDateToCheckFor("2000.01.01-00:16")) ));
        wantedListOfBuckets.add(new BucketEntry(19, new VaultEntry(VaultEntryType.EMPTY, TestFunctions.creatNewDateToCheckFor("2000.01.01-00:17")) ));
        wantedListOfBuckets.add(new BucketEntry(20, new VaultEntry(VaultEntryType.EMPTY, TestFunctions.creatNewDateToCheckFor("2000.01.01-00:18")) ));
        wantedListOfBuckets.add(new BucketEntry(21, new VaultEntry(VaultEntryType.EMPTY, TestFunctions.creatNewDateToCheckFor("2000.01.01-00:19")) ));
        wantedListOfBuckets.add(new BucketEntry(22, new VaultEntry(VaultEntryType.EMPTY, TestFunctions.creatNewDateToCheckFor("2000.01.01-00:20")) ));
        wantedListOfBuckets.add(new BucketEntry(23, new VaultEntry(VaultEntryType.EMPTY, TestFunctions.creatNewDateToCheckFor("2000.01.01-00:21")) ));
        wantedListOfBuckets.add(new BucketEntry(24, new VaultEntry(VaultEntryType.EMPTY, TestFunctions.creatNewDateToCheckFor("2000.01.01-00:22")) ));
        wantedListOfBuckets.add(new BucketEntry(25, new VaultEntry(VaultEntryType.EMPTY, TestFunctions.creatNewDateToCheckFor("2000.01.01-00:23")) ));
        wantedListOfBuckets.add(new BucketEntry(26, new VaultEntry(VaultEntryType.EMPTY, TestFunctions.creatNewDateToCheckFor("2000.01.01-00:24")) ));
        wantedListOfBuckets.add(new BucketEntry(27, new VaultEntry(VaultEntryType.EMPTY, TestFunctions.creatNewDateToCheckFor("2000.01.01-00:25")) ));
        wantedListOfBuckets.add(new BucketEntry(28, new VaultEntry(VaultEntryType.EMPTY, TestFunctions.creatNewDateToCheckFor("2000.01.01-00:26")) ));
        wantedListOfBuckets.add(new BucketEntry(29, new VaultEntry(VaultEntryType.EMPTY, TestFunctions.creatNewDateToCheckFor("2000.01.01-00:27")) ));
        wantedListOfBuckets.add(new BucketEntry(30, new VaultEntry(VaultEntryType.EMPTY, TestFunctions.creatNewDateToCheckFor("2000.01.01-00:28")) ));
        wantedListOfBuckets.add(new BucketEntry(31, new VaultEntry(VaultEntryType.EMPTY, TestFunctions.creatNewDateToCheckFor("2000.01.01-00:29")) ));
        wantedListOfBuckets.add(new BucketEntry(32, new VaultEntry(VaultEntryType.EMPTY, TestFunctions.creatNewDateToCheckFor("2000.01.01-00:30")) ));
        wantedListOfBuckets.add(new BucketEntry(33, new VaultEntry(VaultEntryType.EMPTY, TestFunctions.creatNewDateToCheckFor("2000.01.01-00:31")) ));
        wantedListOfBuckets.add(new BucketEntry(34, new VaultEntry(VaultEntryType.EMPTY, TestFunctions.creatNewDateToCheckFor("2000.01.01-00:32")) ));
        wantedListOfBuckets.add(new BucketEntry(35, new VaultEntry(VaultEntryType.EMPTY, TestFunctions.creatNewDateToCheckFor("2000.01.01-00:33")) ));
        wantedListOfBuckets.add(new BucketEntry(36, new VaultEntry(VaultEntryType.EMPTY, TestFunctions.creatNewDateToCheckFor("2000.01.01-00:34")) ));
        wantedListOfBuckets.add(new BucketEntry(37, new VaultEntry(VaultEntryType.EMPTY, TestFunctions.creatNewDateToCheckFor("2000.01.01-00:35")) ));
        wantedListOfBuckets.add(new BucketEntry(38, new VaultEntry(VaultEntryType.EMPTY, TestFunctions.creatNewDateToCheckFor("2000.01.01-00:36")) ));
        wantedListOfBuckets.add(new BucketEntry(39, new VaultEntry(VaultEntryType.EMPTY, TestFunctions.creatNewDateToCheckFor("2000.01.01-00:37")) ));
        wantedListOfBuckets.add(new BucketEntry(40, new VaultEntry(VaultEntryType.EMPTY, TestFunctions.creatNewDateToCheckFor("2000.01.01-00:38")) ));
        wantedListOfBuckets.add(new BucketEntry(41, new VaultEntry(VaultEntryType.EMPTY, TestFunctions.creatNewDateToCheckFor("2000.01.01-00:39")) ));
        wantedListOfBuckets.add(new BucketEntry(42, new VaultEntry(VaultEntryType.EMPTY, TestFunctions.creatNewDateToCheckFor("2000.01.01-00:40")) ));
        wantedListOfBuckets.add(new BucketEntry(43, new VaultEntry(VaultEntryType.EMPTY, TestFunctions.creatNewDateToCheckFor("2000.01.01-00:41")) ));
        wantedListOfBuckets.add(new BucketEntry(44, new VaultEntry(VaultEntryType.EMPTY, TestFunctions.creatNewDateToCheckFor("2000.01.01-00:42")) ));
        wantedListOfBuckets.add(new BucketEntry(45, new VaultEntry(VaultEntryType.EMPTY, TestFunctions.creatNewDateToCheckFor("2000.01.01-00:43")) ));
        wantedListOfBuckets.add(new BucketEntry(46, new VaultEntry(VaultEntryType.EMPTY, TestFunctions.creatNewDateToCheckFor("2000.01.01-00:44")) ));
        wantedListOfBuckets.add(new BucketEntry(47, new VaultEntry(VaultEntryType.EMPTY, TestFunctions.creatNewDateToCheckFor("2000.01.01-00:45")) ));
        wantedListOfBuckets.add(new BucketEntry(48, new VaultEntry(VaultEntryType.EMPTY, TestFunctions.creatNewDateToCheckFor("2000.01.01-00:46")) ));
        wantedListOfBuckets.add(new BucketEntry(49, new VaultEntry(VaultEntryType.EMPTY, TestFunctions.creatNewDateToCheckFor("2000.01.01-00:47")) ));
        wantedListOfBuckets.add(new BucketEntry(50, new VaultEntry(VaultEntryType.EMPTY, TestFunctions.creatNewDateToCheckFor("2000.01.01-00:48")) ));
        wantedListOfBuckets.add(new BucketEntry(51, new VaultEntry(VaultEntryType.EMPTY, TestFunctions.creatNewDateToCheckFor("2000.01.01-00:49")) ));
        wantedListOfBuckets.add(new BucketEntry(52, new VaultEntry(VaultEntryType.EMPTY, TestFunctions.creatNewDateToCheckFor("2000.01.01-00:50")) ));
        wantedListOfBuckets.add(new BucketEntry(53, new VaultEntry(VaultEntryType.EMPTY, TestFunctions.creatNewDateToCheckFor("2000.01.01-00:51")) ));
        wantedListOfBuckets.add(new BucketEntry(54, new VaultEntry(VaultEntryType.EMPTY, TestFunctions.creatNewDateToCheckFor("2000.01.01-00:52")) ));
        wantedListOfBuckets.add(new BucketEntry(55, new VaultEntry(VaultEntryType.EMPTY, TestFunctions.creatNewDateToCheckFor("2000.01.01-00:53")) ));
        wantedListOfBuckets.add(new BucketEntry(56, new VaultEntry(VaultEntryType.EMPTY, TestFunctions.creatNewDateToCheckFor("2000.01.01-00:54")) ));
        wantedListOfBuckets.add(new BucketEntry(57, new VaultEntry(VaultEntryType.EMPTY, TestFunctions.creatNewDateToCheckFor("2000.01.01-00:55")) ));
        wantedListOfBuckets.add(new BucketEntry(58, new VaultEntry(VaultEntryType.EMPTY, TestFunctions.creatNewDateToCheckFor("2000.01.01-00:56")) ));
        wantedListOfBuckets.add(new BucketEntry(59, new VaultEntry(VaultEntryType.EMPTY, TestFunctions.creatNewDateToCheckFor("2000.01.01-00:57")) ));
        wantedListOfBuckets.add(new BucketEntry(60, new VaultEntry(VaultEntryType.EMPTY, TestFunctions.creatNewDateToCheckFor("2000.01.01-00:58")) ));
        wantedListOfBuckets.add(new BucketEntry(61, new VaultEntry(VaultEntryType.EMPTY, TestFunctions.creatNewDateToCheckFor("2000.01.01-00:59")) ));
        wantedListOfBuckets.add(new BucketEntry(62, new VaultEntry(VaultEntryType.EMPTY, TestFunctions.creatNewDateToCheckFor("2000.01.01-01:00")) ));
        wantedListOfBuckets.add(new BucketEntry(63, new VaultEntry(VaultEntryType.EMPTY, TestFunctions.creatNewDateToCheckFor("2000.01.01-01:01")) ));
        wantedListOfBuckets.add(new BucketEntry(64, new VaultEntry(VaultEntryType.EMPTY, TestFunctions.creatNewDateToCheckFor("2000.01.01-01:02")) ));
        wantedListOfBuckets.add(new BucketEntry(65, new VaultEntry(VaultEntryType.EMPTY, TestFunctions.creatNewDateToCheckFor("2000.01.01-01:03")) ));
        wantedListOfBuckets.add(new BucketEntry(66, new VaultEntry(VaultEntryType.EMPTY, TestFunctions.creatNewDateToCheckFor("2000.01.01-01:04")) ));
        wantedListOfBuckets.add(new BucketEntry(67, new VaultEntry(VaultEntryType.EMPTY, TestFunctions.creatNewDateToCheckFor("2000.01.01-01:05")) ));
        wantedListOfBuckets.add(new BucketEntry(68, new VaultEntry(VaultEntryType.EMPTY, TestFunctions.creatNewDateToCheckFor("2000.01.01-01:06")) ));
        wantedListOfBuckets.add(new BucketEntry(69, new VaultEntry(VaultEntryType.EMPTY, TestFunctions.creatNewDateToCheckFor("2000.01.01-01:07")) ));
        wantedListOfBuckets.add(new BucketEntry(70, new VaultEntry(VaultEntryType.EMPTY, TestFunctions.creatNewDateToCheckFor("2000.01.01-01:08")) ));
        wantedListOfBuckets.add(new BucketEntry(71, new VaultEntry(VaultEntryType.EMPTY, TestFunctions.creatNewDateToCheckFor("2000.01.01-01:09")) ));
        wantedListOfBuckets.add(new BucketEntry(72, new VaultEntry(VaultEntryType.EMPTY, TestFunctions.creatNewDateToCheckFor("2000.01.01-01:10")) ));
        wantedListOfBuckets.add(new BucketEntry(73, new VaultEntry(VaultEntryType.EMPTY, TestFunctions.creatNewDateToCheckFor("2000.01.01-01:11")) ));
        wantedListOfBuckets.add(new BucketEntry(74, new VaultEntry(VaultEntryType.EMPTY, TestFunctions.creatNewDateToCheckFor("2000.01.01-01:12")) ));
        
        wantedListOfBuckets.add(new BucketEntry(75, new VaultEntry(VaultEntryType.EMPTY, TestFunctions.creatNewDateToCheckFor("2000.01.01-01:13")) ));
        
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
    
    @Test (expected = NullPointerException.class)
    public void testBucketProcessor_NULL_TEST() throws ParseException {
        List<VaultEntry> vaultEntries = null;
        
        instance = new BucketProcessor();
        result = instance.createListOfBuckets(vaultEntries);
        
        List<BucketEntry> wantedListOfBuckets = new ArrayList<>();
        
        testBucketInformation(result, wantedListOfBuckets);
    }
}
