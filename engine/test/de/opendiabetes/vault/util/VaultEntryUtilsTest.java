/*
 * Copyright (C) 2018 tiweGH
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
package de.opendiabetes.vault.util;

import java.text.ParseException;
import java.util.Date;
import org.junit.After;
import org.junit.AfterClass;
import static org.junit.Assert.assertEquals;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

/**
 *
 * @author tiweGH
 */
public class VaultEntryUtilsTest {

    public VaultEntryUtilsTest() {
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

    @Test
    public void testSetDayOfDate() throws ParseException {
        Date date1 = TimestampUtils.createCleanTimestamp("2000.01.01-11:11", "yyyy.MM.dd-HH:mm");
        Date date2 = TimestampUtils.createCleanTimestamp("2017.06.28-00:00", "yyyy.MM.dd-HH:mm");
        Date expectedResult = TimestampUtils.createCleanTimestamp("2017.06.28-11:11", "yyyy.MM.dd-HH:mm");

        assertEquals(expectedResult, TimestampUtils.setDayOfDate(date1, date2));
    }

}
