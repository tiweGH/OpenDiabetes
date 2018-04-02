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
import de.opendiabetes.vault.processing.filter.options.LogicFilterOption;
import de.opendiabetes.vault.processing.filter.options.VaultEntryTypeFilterOption;
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
public class LogicFilterTest extends Assert {

    public LogicFilterTest() {
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

    LogicFilter setUpFilter(List<Filter> filters, boolean onlyOneResult) {
        return new LogicFilter(new LogicFilterOption(filters, onlyOneResult));
    }

    /**
     * Test of filter method, of class TimeSpanFilter.
     */
    @Test
    public void testOnlyOneResultFilter() throws ParseException {
        System.out.println("filter");
        List<Filter> filters = new ArrayList<>();
        filters.add(new VaultEntryTypeFilter(new VaultEntryTypeFilterOption(VaultEntryType.HEART_RATE)));
        filters.add(new VaultEntryTypeFilter(new VaultEntryTypeFilterOption(VaultEntryType.HEART_RATE_VARIABILITY)));
        filters.add(new VaultEntryTypeFilter(new VaultEntryTypeFilterOption(VaultEntryType.SLEEP_LIGHT)));

        List<VaultEntry> data = StaticDataset.getStaticDataset();

        LogicFilter instance = setUpFilter(filters, true);
        FilterResult result = instance.filter(data);

        assertTrue(result.filteredData.size() == 1);
    }

    @Test
    public void testFilter() throws ParseException {
        System.out.println("filter");
        List<Filter> filters = new ArrayList<>();
        filters.add(new VaultEntryTypeFilter(new VaultEntryTypeFilterOption(VaultEntryType.HEART_RATE)));
        filters.add(new VaultEntryTypeFilter(new VaultEntryTypeFilterOption(VaultEntryType.HEART_RATE_VARIABILITY)));
        filters.add(new VaultEntryTypeFilter(new VaultEntryTypeFilterOption(VaultEntryType.SLEEP_LIGHT)));

        List<VaultEntry> data = StaticDataset.getStaticDataset();

        LogicFilter instance = setUpFilter(filters, true);
        FilterResult result = instance.filter(data);

        for (VaultEntry vaultEntry : result.filteredData) {
            assertTrue(vaultEntry.getType().equals(VaultEntryType.SLEEP_LIGHT));
        }
    }

}
