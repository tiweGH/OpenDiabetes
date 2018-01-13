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
import static de.jhit.opendiabetes.vault.util.TimestampUtils.addMinutesToTimestamp;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
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
                assertTrue(result.get(i).getOnehoteInformationArray(m) == wantedListOfBuckets.get(i).getOnehoteInformationArray(m));
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
        wantedListOfBuckets.get(0).setOnehoteInformationArray(0, 1);
        
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
        wantedListOfBuckets.get(0).setOnehoteInformationArray(0, 1);
        wantedListOfBuckets.add(new BucketEntry(2, new VaultEntry(VaultEntryType.GLUCOSE_CGM_ALERT, TestFunctions.creatNewDateToCheckFor("2000.01.01-00:01"), 182) ));
        wantedListOfBuckets.get(1).setTimeCountDown(0, 1);
        wantedListOfBuckets.get(1).setOnehoteInformationArray(0, 1);
        
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
        wantedListOfBuckets.get(0).setOnehoteInformationArray(0, 1);
        wantedListOfBuckets.add(new BucketEntry(2, new VaultEntry(VaultEntryType.GLUCOSE_CGM_ALERT, TestFunctions.creatNewDateToCheckFor("2000.01.01-00:02"), 182) ));
        wantedListOfBuckets.get(1).setTimeCountDown(0, 1);
        wantedListOfBuckets.get(1).setOnehoteInformationArray(0, 1);
        
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
        wantedListOfBuckets.get(0).setOnehoteInformationArray(0, 1);
        wantedListOfBuckets.add(new BucketEntry(2, new VaultEntry(VaultEntryType.EMPTY, TestFunctions.creatNewDateToCheckFor("2000.01.01-00:02")) ));
        wantedListOfBuckets.add(new BucketEntry(3, new VaultEntry(VaultEntryType.GLUCOSE_CGM_ALERT, TestFunctions.creatNewDateToCheckFor("2000.01.01-00:03"), 182) ));
        wantedListOfBuckets.get(2).setTimeCountDown(0, 1);
        wantedListOfBuckets.get(2).setOnehoteInformationArray(0, 1);
        
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
        wantedListOfBuckets.get(0).setOnehoteInformationArray(0, 1);
        tmpAnnotations = new ArrayList<>();
        tmpAnnotations.add(new VaultEntryAnnotation(VaultEntryAnnotation.TYPE.GLUCOSE_BG_METER_SERIAL).setValue("BG1140084B"));
        wantedListOfBuckets.add(new BucketEntry(2, new VaultEntry(VaultEntryType.GLUCOSE_BG, TestFunctions.creatNewDateToCheckFor("2000.01.01-00:02"), 109.0, tmpAnnotations) ));
        // wantedListOfBuckets.get(1).setTimeCountDown(0, 0);
        // wantedListOfBuckets.get(1).setOnehoteInformationArray(0, 0);
        wantedListOfBuckets.add(new BucketEntry(3, new VaultEntry(VaultEntryType.GLUCOSE_CGM_ALERT, TestFunctions.creatNewDateToCheckFor("2000.01.01-00:03"), 182) ));
        wantedListOfBuckets.get(2).setTimeCountDown(0, 1);
        wantedListOfBuckets.get(2).setOnehoteInformationArray(0, 1);
        
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
        wantedListOfBuckets.get(0).setOnehoteInformationArray(0, 1);
        
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
        wantedListOfBuckets.get(0).setOnehoteInformationArray(0, 1);
        wantedListOfBuckets.add(new BucketEntry(2, new VaultEntry(VaultEntryType.GLUCOSE_CGM_ALERT, TestFunctions.creatNewDateToCheckFor("2000.01.01-00:01"), 170) ));
        wantedListOfBuckets.get(1).setTimeCountDown(0, 1);
        wantedListOfBuckets.get(1).setOnehoteInformationArray(0, 1);
        wantedListOfBuckets.add(new BucketEntry(3, new VaultEntry(VaultEntryType.GLUCOSE_CGM_ALERT, TestFunctions.creatNewDateToCheckFor("2000.01.01-00:02"), 170) ));
        wantedListOfBuckets.get(2).setTimeCountDown(0, 1);
        wantedListOfBuckets.get(2).setOnehoteInformationArray(0, 1);
        wantedListOfBuckets.add(new BucketEntry(4, new VaultEntry(VaultEntryType.EMPTY, TestFunctions.creatNewDateToCheckFor("2000.01.01-00:03")) ));
        wantedListOfBuckets.add(new BucketEntry(5, new VaultEntry(VaultEntryType.EMPTY, TestFunctions.creatNewDateToCheckFor("2000.01.01-00:04")) ));
        wantedListOfBuckets.add(new BucketEntry(6, new VaultEntry(VaultEntryType.GLUCOSE_CGM_ALERT, TestFunctions.creatNewDateToCheckFor("2000.01.01-00:05"), 170) ));
        wantedListOfBuckets.get(5).setTimeCountDown(0, 1);
        wantedListOfBuckets.get(5).setOnehoteInformationArray(0, 1);
        wantedListOfBuckets.add(new BucketEntry(7, new VaultEntry(VaultEntryType.EMPTY, TestFunctions.creatNewDateToCheckFor("2000.01.01-00:06")) ));
        wantedListOfBuckets.add(new BucketEntry(8, new VaultEntry(VaultEntryType.EMPTY, TestFunctions.creatNewDateToCheckFor("2000.01.01-00:07")) ));
        wantedListOfBuckets.add(new BucketEntry(9, new VaultEntry(VaultEntryType.GLUCOSE_CGM_ALERT, TestFunctions.creatNewDateToCheckFor("2000.01.01-00:08"), 170) ));
        wantedListOfBuckets.get(8).setTimeCountDown(0, 1);
        wantedListOfBuckets.get(8).setOnehoteInformationArray(0, 1);
        
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
        wantedListOfBuckets.get(0).setOnehoteInformationArray(0, 1);
        wantedListOfBuckets.add(new BucketEntry(2, new VaultEntry(VaultEntryType.GLUCOSE_CGM_ALERT, TestFunctions.creatNewDateToCheckFor("2000.01.01-00:01"), 170) ));
        wantedListOfBuckets.get(1).setTimeCountDown(0, 1);
        wantedListOfBuckets.get(1).setOnehoteInformationArray(0, 1);
        wantedListOfBuckets.add(new BucketEntry(3, new VaultEntry(VaultEntryType.GLUCOSE_CGM_ALERT, TestFunctions.creatNewDateToCheckFor("2000.01.01-00:02"), 170) ));
        wantedListOfBuckets.get(2).setTimeCountDown(0, 1);
        wantedListOfBuckets.get(2).setOnehoteInformationArray(0, 1);
        wantedListOfBuckets.add(new BucketEntry(4, new VaultEntry(VaultEntryType.EMPTY, TestFunctions.creatNewDateToCheckFor("2000.01.01-00:03")) ));
        wantedListOfBuckets.add(new BucketEntry(5, new VaultEntry(VaultEntryType.EMPTY, TestFunctions.creatNewDateToCheckFor("2000.01.01-00:04")) ));
        wantedListOfBuckets.add(new BucketEntry(6, new VaultEntry(VaultEntryType.GLUCOSE_CGM_ALERT, TestFunctions.creatNewDateToCheckFor("2000.01.01-00:05"), 170) ));
        wantedListOfBuckets.get(5).setTimeCountDown(0, 1);
        wantedListOfBuckets.get(5).setOnehoteInformationArray(0, 1);
        wantedListOfBuckets.add(new BucketEntry(7, new VaultEntry(VaultEntryType.EMPTY, TestFunctions.creatNewDateToCheckFor("2000.01.01-00:06")) ));
        wantedListOfBuckets.add(new BucketEntry(8, new VaultEntry(VaultEntryType.EMPTY, TestFunctions.creatNewDateToCheckFor("2000.01.01-00:07")) ));
        wantedListOfBuckets.add(new BucketEntry(9, new VaultEntry(VaultEntryType.GLUCOSE_CGM_ALERT, TestFunctions.creatNewDateToCheckFor("2000.01.01-00:08"), 170) ));
        wantedListOfBuckets.get(8).setTimeCountDown(0, 1);
        wantedListOfBuckets.get(8).setOnehoteInformationArray(0, 1);
        
        testBucketInformation(result, wantedListOfBuckets);
    }
}
