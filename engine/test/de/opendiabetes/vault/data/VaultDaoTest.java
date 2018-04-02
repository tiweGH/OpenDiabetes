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
package de.opendiabetes.vault.data;

import de.opendiabetes.vault.container.VaultEntry;
import de.opendiabetes.vault.container.VaultEntryAnnotation;
import de.opendiabetes.vault.container.VaultEntryType;
import java.io.IOException;
import java.sql.SQLException;
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
public class VaultDaoTest extends Assert {

    public VaultDaoTest() {
    }

    @BeforeClass
    public static void setUpClass() throws SQLException {
        VaultDao.initializeDb();
    }

    @AfterClass
    public static void tearDownClass() throws IOException {
        VaultDao.finalizeDb();
    }

    @Before
    public void setUp() throws SQLException {
    }

    @After
    public void tearDown() throws IOException {
    }

    /**
     * Test of propper db initialization
     */
    @Test
    public void testGetInstance() throws Exception {
        System.out.println("testGetInstance");
        VaultDao result = VaultDao.getInstance();
        assertNotNull(result);
    }

    /**
     * Test of correct annotation writing and reading
     */
    @Test
    public void testVaultEntryAnnotation() throws Exception {
        System.out.println("testVaultEntryAnnotation");
        VaultDao result = VaultDao.getInstance();
        assertNotNull(result);

        Date timestamp = new Date();
        VaultEntryAnnotation annotation = new VaultEntryAnnotation("23",
                VaultEntryAnnotation.TYPE.GLUCOSE_RISE_LAST);
        VaultEntry entry = new VaultEntry(VaultEntryType.EXERCISE_OTHER,
                timestamp, 100.0);
        for (int i = 0; i < 1000; i++) { // test if there is a overflow in the db
            entry.addAnnotation(annotation);
        }
        long id = result.putEntry(entry);
        assertNotEquals(id, VaultDao.RESULT_ERROR);

        VaultEntry qEntry = result.queryVaultEntryById(id);
        assertNotNull(qEntry);
        assertEquals(timestamp, qEntry.getTimestamp());
        assertEquals(100.0, qEntry.getValue(), 0.1);
        assertEquals(annotation, qEntry.getAnnotations().get(0));

    }

//    /**
//     * Test of putEntry method, of class VaultDao.
//     */
//    @Test
//    public void testPutEntry() {
//        System.out.println("putEntry");
//        VaultEntry entry = null;
//        VaultDao instance = null;
//        long expResult = 0L;
//        long result = instance.putEntry(entry);
//        assertEquals(expResult, result);
//        // TODO review the generated test code and remove the default call to fail.
//        fail("The test case is a prototype.");
//    }
//
//    /**
//     * Test of putRawEntry method, of class VaultDao.
//     */
//    @Test
//    public void testPutRawEntry() {
//        System.out.println("putRawEntry");
//        RawEntry entry = null;
//        VaultDao instance = null;
//        long expResult = 0L;
//        long result = instance.putRawEntry(entry);
//        assertEquals(expResult, result);
//        // TODO review the generated test code and remove the default call to fail.
//        fail("The test case is a prototype.");
//    }
//
//    /**
//     * Test of removeDublicates method, of class VaultDao.
//     */
//    @Test
//    public void testRemoveDublicates() {
//        System.out.println("removeDublicates");
//        VaultDao instance = null;
//        boolean expResult = false;
//        boolean result = instance.removeDublicates();
//        assertEquals(expResult, result);
//        // TODO review the generated test code and remove the default call to fail.
//        fail("The test case is a prototype.");
//    }
//
//    /**
//     * Test of queryGlucoseBetween method, of class VaultDao.
//     */
//    @Test
//    public void testQueryGlucoseBetween() {
//        System.out.println("queryGlucoseBetween");
//        Date from = null;
//        Date to = null;
//        VaultDao instance = null;
//        List<VaultEntry> expResult = null;
//        List<VaultEntry> result = instance.queryGlucoseBetween(from, to);
//        assertEquals(expResult, result);
//        // TODO review the generated test code and remove the default call to fail.
//        fail("The test case is a prototype.");
//    }
//
//    /**
//     * Test of queryExerciseBetween method, of class VaultDao.
//     */
//    @Test
//    public void testQueryExerciseBetween() {
//        System.out.println("queryExerciseBetween");
//        Date from = null;
//        Date to = null;
//        VaultDao instance = null;
//        List<VaultEntry> expResult = null;
//        List<VaultEntry> result = instance.queryExerciseBetween(from, to);
//        assertEquals(expResult, result);
//        // TODO review the generated test code and remove the default call to fail.
//        fail("The test case is a prototype.");
//    }
//
//    /**
//     * Test of queryLatestEventBefore method, of class VaultDao.
//     */
//    @Test
//    public void testQueryLatestEventBefore() {
//        System.out.println("queryLatestEventBefore");
//        Date timestamp = null;
//        VaultEntryType type = null;
//        VaultDao instance = null;
//        VaultEntry expResult = null;
//        VaultEntry result = instance.queryLatestEventBefore(timestamp, type);
//        assertEquals(expResult, result);
//        // TODO review the generated test code and remove the default call to fail.
//        fail("The test case is a prototype.");
//    }
//
//    /**
//     * Test of queryAllVaultEntrys method, of class VaultDao.
//     */
//    @Test
//    public void testQueryAllVaultEntrys() {
//        System.out.println("queryAllVaultEntrys");
//        VaultDao instance = null;
//        List<VaultEntry> expResult = null;
//        List<VaultEntry> result = instance.queryAllVaultEntrys();
//        assertEquals(expResult, result);
//        // TODO review the generated test code and remove the default call to fail.
//        fail("The test case is a prototype.");
//    }
//
//    /**
//     * Test of queryVaultEntrysBetween method, of class VaultDao.
//     */
//    @Test
//    public void testQueryVaultEntrysBetween() {
//        System.out.println("queryVaultEntrysBetween");
//        Date from = null;
//        Date to = null;
//        VaultDao instance = null;
//        List<VaultEntry> expResult = null;
//        List<VaultEntry> result = instance.queryVaultEntrysBetween(from, to);
//        assertEquals(expResult, result);
//        // TODO review the generated test code and remove the default call to fail.
//        fail("The test case is a prototype.");
//    }
//
//    /**
//     * Test of queryBasalBetween method, of class VaultDao.
//     */
//    @Test
//    public void testQueryBasalBetween() {
//        System.out.println("queryBasalBetween");
//        Date from = null;
//        Date to = null;
//        VaultDao instance = null;
//        List<VaultEntry> expResult = null;
//        List<VaultEntry> result = instance.queryBasalBetween(from, to);
//        assertEquals(expResult, result);
//        // TODO review the generated test code and remove the default call to fail.
//        fail("The test case is a prototype.");
//    }
//
//    /**
//     * Test of removeEntry method, of class VaultDao.
//     */
//    @Test
//    public void testRemoveEntry() {
//        System.out.println("removeEntry");
//        VaultEntry historyEntry = null;
//        VaultDao instance = null;
//        boolean expResult = false;
//        boolean result = instance.removeEntry(historyEntry);
//        assertEquals(expResult, result);
//        // TODO review the generated test code and remove the default call to fail.
//        fail("The test case is a prototype.");
//    }
}
