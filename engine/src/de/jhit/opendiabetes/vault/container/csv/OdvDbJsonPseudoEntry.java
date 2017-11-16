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
package de.jhit.opendiabetes.vault.container.csv;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import de.jhit.opendiabetes.vault.container.VaultEntry;
import de.jhit.opendiabetes.vault.container.VaultEntryGsonAdapter;
import java.io.IOException;
import java.nio.charset.Charset;
import java.util.List;

/**
 *
 * @author juehv
 */
public class OdvDbJsonPseudoEntry implements ExportEntry {

    private final List<VaultEntry> data;

    private OdvDbJsonPseudoEntry(List<VaultEntry> data) {
        this.data = data;
    }

    public static OdvDbJsonPseudoEntry fromVaultEntryList(List<VaultEntry> data) {
        return new OdvDbJsonPseudoEntry(data);
    }

    @Override
    public byte[] toByteEntryLine() throws IOException {
        GsonBuilder gb = new GsonBuilder();
        gb.registerTypeAdapter(VaultEntry.class, new VaultEntryGsonAdapter());
        Gson gs = gb.create();

        try {
            String jsonData = gs.toJson(data);
            return jsonData.getBytes(Charset.forName("UTF-8"));
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return null;
    }

}
