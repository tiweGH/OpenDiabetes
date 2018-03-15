/*
 * Copyright (C) 2017 a.a.aponte
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
import de.jhit.opendiabetes.vault.exporter.NewDataset;
import de.jhit.opendiabetes.vault.processing.filter.options.PositionFilterOption;
import java.text.ParseException;
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
public class PositionFilterTest extends Assert {

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

    PositionFilter setUpFilter(int pos) {
        return new PositionFilter(new PositionFilterOption(pos));
    }

    /**
     * Test of filter method, of class UnderThresholdFilter.
     *
     * @author joerg
     * @throws java.text.ParseException
     */
    @Test
    public void testPositionFilterFirst() throws ParseException {

        PositionFilter instance = setUpFilter(PositionFilter.FIRST);
        FilterResult result = instance.filter(data);
        assertEquals(result.filteredData.get(0), data.get(0));
    }

    @Test
    public void testPositionFilterMiddle() throws ParseException {

        PositionFilter instance = setUpFilter(PositionFilterOption.MIDDLE);
        FilterResult result = instance.filter(data);
        assertEquals(result.filteredData.get(0), data.get(data.size() / 2));
    }

}
