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
package de.jhit.opendiabetes.vault.container.csv;

import de.jhit.opendiabetes.vault.container.SliceEntry;
import java.text.SimpleDateFormat;
import java.util.Date;
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
public class SliceCsVEntryTest extends Assert {

    public SliceCsVEntryTest() {
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
     * Test of toCsvRecord method, of class SliceCsVEntry.
     *
     * errors history: Number format exeption while creating the double from
     * long
     */
    @Test
    public void testToCsvRecord() {
        System.out.println("toCsvRecord");
        SliceEntry data = new SliceEntry(new Date(), 60);
        SliceCsVEntry instance = new SliceCsVEntry(data);
        String[] expResult = new String[]{
            new SimpleDateFormat("dd.MM.yy").format(data.getTimestamp()),
            new SimpleDateFormat("HH:mm").format(data.getTimestamp()),
            "60.00",
            ""
        };
        String[] result = instance.toCsvRecord();
        assertArrayEquals(expResult, result);
    }

    /**
     * Test of getCsvHeaderRecord method, of class SliceCsVEntry.
     */
    @Test
    public void testGetCsvHeaderRecord() {
    }

}
