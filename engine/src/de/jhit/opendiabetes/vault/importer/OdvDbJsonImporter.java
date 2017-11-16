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
package de.jhit.opendiabetes.vault.importer;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import de.jhit.opendiabetes.vault.container.VaultEntry;
import de.jhit.opendiabetes.vault.container.VaultEntryGsonAdapter;
import static de.jhit.opendiabetes.vault.importer.Importer.LOG;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;

/**
 *
 * @author juehv
 */
public class OdvDbJsonImporter extends FileImporter {

    public OdvDbJsonImporter(String importFilePath) {
        super(importFilePath);
    }

    @Override
    protected void preprocessingIfNeeded(String filePath) {
        // not needed
    }

    @Override
    protected boolean processImport(InputStream fis, String filenameForLogging) {
        importedData = new ArrayList<>();
        importedRawData = new ArrayList<>();

        // prepare libs
        GsonBuilder gb = new GsonBuilder();
        gb.registerTypeAdapter(VaultEntry.class, new VaultEntryGsonAdapter());
        Gson gs = gb.create();

        // open stream
        BufferedReader br = new BufferedReader(new InputStreamReader(fis));

        // import
        Type listType = new TypeToken<ArrayList<VaultEntry>>() {
        }.getType();
        List<VaultEntry> importDb = gs.fromJson(br, listType);

        if (importDb != null && !importDb.isEmpty()) {
            importedData = importDb;
            LOG.log(Level.FINE, "Successfully imported json file");
            return true;
        }
        LOG.log(Level.SEVERE, "Got no data from json import");
        return false;
    }

}
