/*
 * Copyright (C) 2018 Daniel
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
package de.opendiabetes.vault.processing.exporter;

import de.opendiabetes.vault.container.VaultEntry;
import de.opendiabetes.vault.exporter.MLExporter;
import de.opendiabetes.vault.testhelper.StaticDataset;
import java.io.File;
import java.io.IOException;
import java.text.ParseException;
import java.util.List;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author Daniel
 */
public class MLExporterTest {
    
    public MLExporterTest() {
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

    // TODO add test methods here.
    // The methods must be annotated with annotation @Test. For example:
    //
    @Test
    public void testHello() throws ParseException, IOException {
    
        MLExporter mLExporter = new MLExporter();
        List<VaultEntry> data = StaticDataset.getStaticDataset();
        mLExporter.exportDataToFile("C:\\Users\\Daniel\\Desktop\\export",data);
        
        File file = new File("C:\\Users\\Daniel\\Desktop\\export.csv");
        assertTrue(file.exists());
    }
}
