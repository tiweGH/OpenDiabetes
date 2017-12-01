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
package de.jhit.opendiabetes.vault.processing.filter;

import de.jhit.opendiabetes.vault.container.VaultEntry;
import de.jhit.opendiabetes.vault.container.VaultEntryType;
import de.jhit.opendiabetes.vault.testhelper.StaticDataset;
import de.jhit.opendiabetes.vault.util.TimestampUtils;
import java.text.ParseException;
import java.time.LocalTime;
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
 * @author juehv
 */
public class EventFilterTest extends Assert {

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
     * Test of filter method, of class TimeSpanFilter.
     */
    @Test
    public void testFilter() throws ParseException {
        System.out.println("filter");
        List<VaultEntry> data = StaticDataset.getStaticDataset();
        EventFilter instance = new EventFilter(VaultEntryType.STRESS);
        FilterResult result = instance.filter(data);
        System.out.println(result);
        assertTrue(result.size() == 23);
    }
    /*
    @Test
    public void meinTest() throws ParseException{
        List<VaultEntry> data = StaticDataset.getStaticDataset();
        EventFilter eventFilter = new EventFilter();
        List<Date> results = eventFilter.filter(data, VaultEntryType.MEAL_MANUAL, 0);
        System.out.println(results);
        assertTrue(results.isEmpty());
        assertTrue(results.size()== 0);
        
    }*/

}
