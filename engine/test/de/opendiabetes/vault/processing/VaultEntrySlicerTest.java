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

import de.opendiabetes.vault.container.VaultEntry;
import de.opendiabetes.vault.container.VaultEntryType;
import de.opendiabetes.vault.processing.preprocessing.ClusterPreprocessor;
import de.opendiabetes.vault.processing.preprocessing.GapRemover;
import de.opendiabetes.vault.processing.preprocessing.options.ClusterPreprocessorOption;
import de.opendiabetes.vault.processing.preprocessing.options.GapRemoverPreprocessorOption;
import de.opendiabetes.vault.testhelper.SensitivityDataset;
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
public class VaultEntrySlicerTest extends Assert {

    public VaultEntrySlicerTest() {
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
//    @Test
//    public void testSliceData() throws ParseException {
//        System.out.println("sliceData");
//        Filter filter = new EventFilter(VaultEntryType.HEART_RATE);
//        Filter secondFilter = new TimePointFilter(LocalTime.parse("04:56:00"), 10);
//
//        VaultEntrySlicer instance = new VaultEntrySlicer();
//        instance.registerFilter(secondFilter);
//        instance.registerFilter(filter);
//
//        FilterResult result = instance.sliceEntries(StaticDataset.getStaticDataset());
//        assertTrue(result.size() == 2);
//    }
    @Test
    public void testVaultEntrySlicerClusterOneCluster() throws ParseException {
        List<VaultEntry> data = SensitivityDataset.getSensitivityDataset();
        VaultEntrySlicer slicer = new VaultEntrySlicer();
        List<VaultEntry> clustered = new ClusterPreprocessor(new ClusterPreprocessorOption(1000000, VaultEntryType.GLUCOSE_CGM, VaultEntryType.CLUSTER_GLUCOSE_CGM)).preprocess(data);

        assertEquals(data.size() + 1, clustered.size());

        System.out.println("size data: " + data.size());
        System.out.println("size: " + clustered.size());
    }

    @Test
    public void testVaultEntrySlicerClusterNoCluster() throws ParseException {
        List<VaultEntry> data = SensitivityDataset.getSensitivityDataset();
        VaultEntrySlicer slicer = new VaultEntrySlicer();
        List<VaultEntry> clustered = new ClusterPreprocessor(new ClusterPreprocessorOption(0, VaultEntryType.GLUCOSE_CGM, VaultEntryType.CLUSTER_GLUCOSE_CGM)).preprocess(data);

        assertEquals(data.size(), clustered.size());

        System.out.println("size data: " + data.size());
        System.out.println("size: " + clustered.size());
    }

    @Test
    public void testVaultEntrySlicerGap() throws ParseException {
        List<VaultEntry> data = SensitivityDataset.getSensitivityDataset();
        VaultEntrySlicer slicer = new VaultEntrySlicer();

        slicer.registerPreProcess(new GapRemover(new GapRemoverPreprocessorOption(VaultEntryType.BASAL_PROFILE, 1)));
        List<VaultEntry> clustered = new GapRemover(new GapRemoverPreprocessorOption(VaultEntryType.BASAL_PROFILE, 1)).preprocess(data);

//        assertEquals(data.size(), clustered.size());
        System.out.println("size data: " + data.size());
        System.out.println("size: " + clustered.size());
    }
}
