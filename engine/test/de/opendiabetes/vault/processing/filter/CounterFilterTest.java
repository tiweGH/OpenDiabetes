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
package de.opendiabetes.vault.processing.filter;

import de.opendiabetes.vault.container.VaultEntry;
import de.opendiabetes.vault.container.VaultEntryType;
import de.opendiabetes.vault.processing.filter.options.CounterFilterOption;
import de.opendiabetes.vault.processing.filter.options.VaultEntryTypeFilterOption;
import de.opendiabetes.vault.testhelper.StaticDataset;
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
 * @author juehv
 */
public class CounterFilterTest extends Assert {

    public CounterFilterTest() {
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

    CounterFilter setUpFilter(Filter filter, int counter, boolean onlyOneResult) {
        return new CounterFilter(new CounterFilterOption(filter, counter, onlyOneResult));
    }

    /**
     * Test of filter method, of class TimeSpanFilter.
     */
    @Test
    public void testOnlyOneResultFilter() throws ParseException {
        System.out.println("filter");
        List<VaultEntry> data = StaticDataset.getStaticDataset();
        CounterFilter instance = setUpFilter(new VaultEntryTypeFilter(new VaultEntryTypeFilterOption(VaultEntryType.HEART_RATE)), 2, true);
        FilterResult result = instance.filter(data);

        assertTrue(result.filteredData.size() == 1);
    }

    @Test
    public void testFilter() throws ParseException {
        System.out.println("filter");
        List<VaultEntry> data = StaticDataset.getStaticDataset();
        CounterFilter instance = setUpFilter(new VaultEntryTypeFilter(new VaultEntryTypeFilterOption(VaultEntryType.HEART_RATE)), 2, false);
        FilterResult result = instance.filter(data);

        for (VaultEntry vaultEntry : result.filteredData) {
            assertTrue(vaultEntry.getType().equals(VaultEntryType.HEART_RATE));
        }
    }

}
