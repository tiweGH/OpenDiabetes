/*
 * Copyright (C) 2017 Jorg
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
package de.jhit.opendiabetes.vault.exporter;

import de.jhit.opendiabetes.vault.container.VaultEntry;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;
import org.apache.commons.csv.*;

/**
 *
 * @author Jorg
 */
public class ApacheCommonsCsvExporter {
    
//    protected List<VaultEntry> data;
//    protected String filePath;
    
    public ApacheCommonsCsvExporter(){
//        this.data = data;
//        this.filePath = filePath;
    }
    
    
    protected void writeFile(List<VaultEntry> data, String filePath) throws IOException{
        
        Object[] header = {"x", "y"};
        String NEW_LINE_SEPARATOR = "\n";
        
        FileWriter  fileWriter= new FileWriter(filePath);
        CSVFormat csvFormat = CSVFormat.DEFAULT.withRecordSeparator(NEW_LINE_SEPARATOR);
        
        try (CSVPrinter csvPrinter = new CSVPrinter(fileWriter, csvFormat)) {
            csvPrinter.printRecord(header);
            for (VaultEntry en : data){
                
                csvPrinter.printRecord(en);
            }
            csvPrinter.flush();
        }
    }
    
}