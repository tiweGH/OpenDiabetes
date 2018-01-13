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
            assertEquals(result.get(i).getBucketNumber(), wantedListOfBuckets.get(i).getBucketNumber());
            assertEquals(result.get(i).getVaultEntry(), wantedListOfBuckets.get(i).getVaultEntry());
            int m;
            for (m = 0; m < getNumberOfVaultEntryTriggerTypes(); m++) {
                assertTrue(result.get(i).getTimeCountDown(m) == wantedListOfBuckets.get(i).getTimeCountDown(m));
                assertTrue(result.get(i).getOnehoteInformationArray(m) == wantedListOfBuckets.get(i).getOnehoteInformationArray(m));
                assertTrue(result.get(i).getFindNextArray(m) == wantedListOfBuckets.get(i).getFindNextArray(m));
            }
        }
    }
    
    @Test
    public void testBucketProcessor_1() throws ParseException {
        List<VaultEntry> vaultEntries = new ArrayList<>();        
        vaultEntries.add(new VaultEntry(VaultEntryType.GLUCOSE_CGM_ALERT, TestFunctions.creatNewDateToCheckFor("2000.01.01-00:01"), 170));
        
        List<BucketEntry> result = BucketProcessor.createListOfBuckets(vaultEntries);
        
        List<BucketEntry> wantedListOfBuckets = new ArrayList<>();
        wantedListOfBuckets.add(new BucketEntry(1, new VaultEntry(VaultEntryType.GLUCOSE_CGM_ALERT, TestFunctions.creatNewDateToCheckFor("2000.01.01-00:01"), 170) ));
        wantedListOfBuckets.get(0).setTimeCountDown(0, 1);
        wantedListOfBuckets.get(0).setOnehoteInformationArray(0, 1);
        
        testBucketInformation(result, wantedListOfBuckets);
    }
    
    @Test
    public void testBucketProcessor_2() throws ParseException {
        List<VaultEntry> vaultEntries = new ArrayList<>();
        List<VaultEntryAnnotation> tmpAnnotations;
        
        vaultEntries.add(new VaultEntry(VaultEntryType.GLUCOSE_CGM_ALERT, TestFunctions.creatNewDateToCheckFor("2000.01.01-00:01"), 170));
        vaultEntries.add(new VaultEntry(VaultEntryType.GLUCOSE_CGM_ALERT, TestFunctions.creatNewDateToCheckFor("2000.01.01-00:03"), 182));
        
        List<BucketEntry> result = BucketProcessor.createListOfBuckets(vaultEntries);
        
        List<BucketEntry> wantedListOfBuckets = new ArrayList<>();
        wantedListOfBuckets.add(new BucketEntry(1, new VaultEntry(VaultEntryType.GLUCOSE_CGM_ALERT, TestFunctions.creatNewDateToCheckFor("2000.01.01-00:01"), 170) ));
        wantedListOfBuckets.get(0).setTimeCountDown(0, 1);
        wantedListOfBuckets.get(0).setOnehoteInformationArray(0, 1);
        wantedListOfBuckets.add(new BucketEntry(3, new VaultEntry(VaultEntryType.GLUCOSE_CGM_ALERT, TestFunctions.creatNewDateToCheckFor("2000.01.01-00:02"), 182) ));
        wantedListOfBuckets.get(2).setTimeCountDown(2, 1);
        wantedListOfBuckets.get(2).setOnehoteInformationArray(2, 1);
        
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
        
        //List<BucketEntry> result = BucketProcessor.createListOfBuckets(vaultEntries);
        
        List<BucketEntry> wantedListOfBuckets = new ArrayList<>();
        wantedListOfBuckets.add(new BucketEntry(1, new VaultEntry(VaultEntryType.GLUCOSE_CGM_ALERT, TestFunctions.creatNewDateToCheckFor("2000.01.01-00:01"), 170) ));
        wantedListOfBuckets.get(0).setTimeCountDown(0, 1);
        wantedListOfBuckets.get(0).setOnehoteInformationArray(0, 1);
        tmpAnnotations = new ArrayList<>();
        tmpAnnotations.add(new VaultEntryAnnotation(VaultEntryAnnotation.TYPE.GLUCOSE_BG_METER_SERIAL).setValue("BG1140084B"));
        wantedListOfBuckets.add(new BucketEntry(2, new VaultEntry(VaultEntryType.GLUCOSE_BG, TestFunctions.creatNewDateToCheckFor("2000.01.01-00:02"), 109.0, tmpAnnotations) ));
        // wantedListOfBuckets.get(1).setTimeCountDown(1, 0);
        // wantedListOfBuckets.get(1).setOnehoteInformationArray(1, 0);
        wantedListOfBuckets.add(new BucketEntry(3, new VaultEntry(VaultEntryType.GLUCOSE_CGM_ALERT, TestFunctions.creatNewDateToCheckFor("2000.01.01-00:03"), 182) ));
        wantedListOfBuckets.get(2).setTimeCountDown(2, 1);
        wantedListOfBuckets.get(2).setOnehoteInformationArray(2, 1);
        
        //testBucketInformation(result, wantedListOfBuckets);
    }
    
}
