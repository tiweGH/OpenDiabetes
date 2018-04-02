/*
 * Copyright (C) 2017 juehv
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
package de.opendiabetes.vault.processing;

import de.opendiabetes.vault.container.SliceEntry;
import de.opendiabetes.vault.processing.filter.Filter;
import de.opendiabetes.vault.processing.filter.NoneFilter;
import de.opendiabetes.vault.testhelper.StaticDataset;
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
 * @author juehv
 */
public class DataSlicerTest extends Assert {

    public DataSlicerTest() {
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
     * Test of sliceData method, of class DataSlicer.
     */
    @Test
    public void testSliceData() throws ParseException {
        System.out.println("sliceData");
        Filter filter = new NoneFilter();
        DataSlicerOptions options = new DataSlicerOptions(60, DataSlicerOptions.OutputFilter.FIRST_OF_SERIES);
        DataSlicer instance = new DataSlicer(options);
        instance.registerFilter(filter);

        List<SliceEntry> expResult = new ArrayList<>();
        List<SliceEntry> result = instance.sliceData(StaticDataset.getStaticDataset());
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        //fail("The test case is a prototype.");
    }

}
