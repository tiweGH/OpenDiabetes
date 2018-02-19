/*
 * Copyright (C) 2017 aa80hifa
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
package de.jhit.opendiabetes.vault.processing.filter;

import de.jhit.opendiabetes.vault.container.VaultEntry;
import de.jhit.opendiabetes.vault.container.VaultEntryType;
import de.jhit.opendiabetes.vault.exporter.NewDataset;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javafx.util.Pair;
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
public class TypeAbsenceFilterTest extends Assert {

    static List<VaultEntry> data;

    @BeforeClass
    public static void setUpClass() throws ParseException {
        data = NewDataset.getNewDataset();
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
     * Test of filter method, of class UnderThresholdFilter.
     *
     * @author joerg
     * @throws java.text.ParseException
     */
    @Test
    public void testTypeAbsenceFilterWithList() throws ParseException {
        List<VaultEntryType> types = new ArrayList<>();
//        types.add(VaultEntryType.EMPTY);
//        types.add(VaultEntryType.HEART_RATE);
        types.add(VaultEntryType.BOLUS_NORMAL);
        TypeAbsenceFilter instance = new TypeAbsenceFilter(types, 0);
        FilterResult result = instance.filter(data);
        int missingValues = 0;

        for (VaultEntry entry: data){
            if (entry.getType().toString().equals(VaultEntryType.BOLUS_NORMAL.toString()) 
////                    entry.getType().toString().equals(VaultEntryType.HEART_RATE.toString()) ||
//                    entry.getType().toString().equals(VaultEntryType.BOLUS_NORMAL.toString())
                    ){
                missingValues++;
            }
        }
            System.out.println(missingValues);
            System.out.println(data.size());
            System.out.println(result.size());
        assertEquals(result.size(), data.size() - missingValues);
            
    }

//    @Test(expected = NullPointerException.class)
//    public void testUnderThresholdFilterTest_STRESS_25_00_HR_AVAILABLE_STRESS_TH() throws ParseException {
//
//        UnderThresholdFilter instance = new UnderThresholdFilter(VaultEntryType.STRESS, 25.00, FilterType.HR_AVAILABLE, FilterType.STRESS_TH);
//        FilterResult result = instance.filter(data);
//
//        assertEquals(result.filteredData, null);
//
    }
    

