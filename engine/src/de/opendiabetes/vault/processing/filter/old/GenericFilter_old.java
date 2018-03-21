/*
 * Copyright (C) 2017 AChikhale
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
package de.opendiabetes.vault.processing.filter.old;

import de.opendiabetes.vault.container.VaultEntry;
import de.opendiabetes.vault.container.VaultEntryType;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author AChikhale
 */
public class GenericFilter_old {

    private final VaultEntryType GenericType;

    public GenericFilter_old(VaultEntryType GenericType) {

        this.GenericType = GenericType;
    }

    public List<VaultEntry> tempfunction(List<VaultEntry> data) {
        List<VaultEntry> filteredData = new ArrayList<>();

        for (VaultEntry entry : data) {
            if (entry.getType() == GenericType) //// GenericType I am taking it as package de.jhit.opendiabetes.vault.container.VaultEntry as private VaultEntryType type;
            {
                filteredData.add(entry);
            }

        }
        return filteredData;
    }
}
