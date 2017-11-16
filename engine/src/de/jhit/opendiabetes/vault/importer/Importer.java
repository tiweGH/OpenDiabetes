/*
 * Copyright (C) 2017 Jens Heuschkel
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
package de.jhit.opendiabetes.vault.importer;

import de.jhit.opendiabetes.vault.container.RawEntry;
import de.jhit.opendiabetes.vault.container.VaultEntry;
import java.util.List;
import java.util.logging.Logger;

/**
 *
 * @author mswin
 */
public abstract class Importer {

    protected final static Logger LOG = Logger.getLogger(Importer.class.getName());
    protected List<VaultEntry> importedData;
    protected List<RawEntry> importedRawData;

    public Importer() {
    }
    
    public abstract boolean importData();

    public List<VaultEntry> getImportedData() {
        return importedData;
    }

    public List<RawEntry> getImportedRawData() {
        return importedRawData;
    }
    
    
}
