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
package de.jhit.opendiabetes.vault.importer.interpreter;

import de.jhit.opendiabetes.vault.container.RawEntry;
import de.jhit.opendiabetes.vault.importer.Importer;
import de.jhit.opendiabetes.vault.container.VaultEntry;
import de.jhit.opendiabetes.vault.data.VaultDao;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

/**
 *
 * @author mswin
 */
public abstract class VaultInterpreter {

    protected static final Logger LOG = Logger.getLogger(VaultInterpreter.class.getName());

    protected Importer importer;
    protected InterpreterOptions options;
    protected VaultDao db;

    public VaultInterpreter(Importer importer, InterpreterOptions options, VaultDao db) {
        this.importer = importer;
        this.options = options;
        this.db = db;
    }

    private List<VaultEntry> dateFiltering(List<VaultEntry> result) {
        if (options.isImportPeriodRestricted) {
            List<VaultEntry> retVal = new ArrayList<>();
            for (VaultEntry item : result) {
                if (item.getTimestamp().after(options.importPeriodFrom)
                        && item.getTimestamp().before(options.importPeriodTo)) {
                    retVal.add(item);
                }
            }
            return retVal;
        } else {
            return result;
        }
    }

    public List<VaultEntry> importAndInterpretWithoutDb() {
        // parse file
        if (!importer.importData()) {
            return null;
        }

        List<VaultEntry> result = importer.getImportedData();
        if (result.isEmpty()) { // not null since importFile is called
            return null;
        }

        // filter unwanted dates
        result = dateFiltering(result);
        // interpret stuff
        result = interpret(result);
        if (result == null) {
            return null;
        }

        for (RawEntry item : importer.getImportedRawData()) {// not null since importFile is called
            item.setId(db.putRawEntry(item));
        }

        return result;
    }

    public void importAndInterpret() {
        // parse file
        if (!importer.importData()) {
            return;
        }

        List<VaultEntry> result = importer.getImportedData();
        if (result.isEmpty()) { // not null since importFile is called
            return;
        }

        // filter unwanted dates
        result = dateFiltering(result);
        // interpret stuff
        result = interpret(result);
        if (result == null) {
            return;
        }

        for (RawEntry item : importer.getImportedRawData()) {// not null since importFile is called
            item.setId(db.putRawEntry(item));
        }

        // update DB
        for (VaultEntry item : result) {
            // update raw id (if there is a corresponding raw entry)
            if (item.getRawId() > 0) {
                RawEntry rawEntry = importer.getImportedRawData()
                        .get((int) item.getRawId());
                item.setRawId(rawEntry.getId());
            }
            // put in db
            db.putEntry(item);
        }

        db.removeDublicates();
    }

    protected abstract List<VaultEntry> interpret(List<VaultEntry> result);

    public Importer getImporter() {
        return importer;
    }

}
