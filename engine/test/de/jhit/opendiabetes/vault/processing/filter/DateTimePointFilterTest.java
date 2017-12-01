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
import de.jhit.opendiabetes.vault.testhelper.StaticDataset;
import de.jhit.opendiabetes.vault.util.TimestampUtils;
import java.text.ParseException;
import java.time.LocalTime;
import java.util.Date;
import java.util.List;
import static java.util.concurrent.TimeUnit.MILLISECONDS;
import static java.util.concurrent.TimeUnit.MINUTES;
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
public class DateTimePointFilterTest extends Assert {

    public DateTimePointFilterTest() {
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
     * Test of filter method, of class TimeSpanFilter.
     */
    @Test
    public void testFilter() throws ParseException {
        System.out.println("filter");
        Date dateTimePoint = TimestampUtils.createCleanTimestamp("2017.06.29-05:26", "yyyy.MM.dd-HH:mm");
        int marginInMinutes = 5;
        long margin = MILLISECONDS.convert(marginInMinutes, MINUTES);
        List<VaultEntry> data = StaticDataset.getStaticDataset();
        DateTimePointFilter instance = new DateTimePointFilter(dateTimePoint,marginInMinutes);
        FilterResult result = instance.filter(data);

        for (VaultEntry entry : result.filteredData) {
            assertTrue(TimestampUtils.withinDateTimeSpan(new Date(dateTimePoint.getTime() - margin),
                    new Date(dateTimePoint.getTime() + margin), entry.getTimestamp()));
        }
    }

}
