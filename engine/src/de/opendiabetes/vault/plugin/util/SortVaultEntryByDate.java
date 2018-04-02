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
package de.opendiabetes.vault.plugin.util;

import de.opendiabetes.vault.container.VaultEntry;
import java.io.Serializable;
import java.util.Comparator;

/**
 * This class implements a comparator for VaultEntry timestamp.
 */
public class SortVaultEntryByDate implements Comparator<VaultEntry>, Serializable {

    /**
     * Method to compare two VaultEntries by their timestamps.
     *
     * @param entry1 First VaultEntry.
     * @param entry2 Second VaultEntry.
     * @return -1 when the second VaultEntry is larger, 0 if they are equal, 1 if the first VaultEntry is larger.
     */
    @Override
    public int compare(final VaultEntry entry1, final VaultEntry entry2) {
        return entry1.getTimestamp().compareTo(entry2.getTimestamp());
    }


}
