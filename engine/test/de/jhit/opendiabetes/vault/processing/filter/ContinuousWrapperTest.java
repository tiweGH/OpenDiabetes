/*
 * Copyright (C) 2017 tiweGH
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
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.util.Pair;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author tiweGH
 */
public class ContinuousWrapperTest {

    ContinuousWrapper filterUnderTest;
    List<Filter> paramFilterList;
    static List<VaultEntry> dataSet;

    public ContinuousWrapperTest() {
    }

    @BeforeClass
    public static void setUpClass() {
        try {
            dataSet = StaticDataset.getStaticDataset();
        } catch (ParseException ex) {
            Logger.getLogger(ContinuousWrapperTest.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

    @AfterClass
    public static void tearDownClass() {
    }

    @Before
    public void setUp() {
        paramFilterList = new ArrayList<>();
    }

    @After
    public void tearDown() {
    }

    @Test
    public void testFilter_BASICFUNCTIONALITY() throws ParseException {
        paramFilterList.add(new NoneFilter());
        FilterResult result;
        List<Pair<Date, Date>> expectedTimeSeries = new ArrayList<>();
        expectedTimeSeries.add(new Pair(TimestampUtils.createCleanTimestamp("2017.06.29-04:46", "yyyy.MM.dd-HH:mm"),
                TimestampUtils.createCleanTimestamp("2017.06.29-12:40", "yyyy.MM.dd-HH:mm")));

        filterUnderTest = new ContinuousWrapper(paramFilterList, 0);
        result = filterUnderTest.filter(dataSet);
        assertEquals(result.filteredData, dataSet);
        assertEquals(result.timeSeries, expectedTimeSeries);

        filterUnderTest = new ContinuousWrapper(paramFilterList, 5);
        result = filterUnderTest.filter(dataSet);
        assertEquals(result.filteredData, dataSet);
        assertEquals(result.timeSeries, expectedTimeSeries);

        filterUnderTest = new ContinuousWrapper(paramFilterList, 100);
        result = filterUnderTest.filter(dataSet);
        assertEquals(result.filteredData, dataSet);
        assertEquals(result.timeSeries, expectedTimeSeries);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testFilter_BASICINVALIDMARGIN() throws ParseException {
        paramFilterList.add(new NoneFilter());

        filterUnderTest = new ContinuousWrapper(paramFilterList, -1);
        filterUnderTest.filter(dataSet);
    }

}
