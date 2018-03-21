/*
 * Copyright (C) 2017 OpenDiabetes
 * <p>
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 * <p>
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * <p>
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package de.opendiabetes.vault.container.csv;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import de.opendiabetes.vault.container.VaultEntry;
import de.opendiabetes.vault.container.VaultEntryGsonAdapter;

import java.io.IOException;
import java.nio.charset.Charset;
import java.util.List;

/**
 * This class implements the pseudo ODV DB Json entry data structure.
 */
public final class OdvDbJsonPseudoEntry implements ExportEntry {

    /**
     * The data.
     */
    private final List<VaultEntry> data;

    /**
     * Constructor for ODVDBJsonPseudo entries.
     * @param data The data to be converted to ODVDBJsonPseudo entries.
     */
    private OdvDbJsonPseudoEntry(final List<VaultEntry> data) {
        this.data = data;
    }

    /**
     * Method to hand the VaultEntries over.
     * @param data The VaultEntries.
     * @return The ODVDBJsonPseudoEntry with the data.
     */
    public static OdvDbJsonPseudoEntry fromVaultEntryList(final List<VaultEntry> data) {
        return new OdvDbJsonPseudoEntry(data);
    }

    /**
     * {@inheritDoc}
     */
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
