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
package de.jhit.opendiabetes.vault.processing.filter;

import de.jhit.opendiabetes.vault.container.VaultEntry;
import de.jhit.opendiabetes.vault.container.VaultEntryType;
import java.util.List;

/**
 *
 * @author Daniel 
 * This class extends filter and checks if the given vaultEntryType is equal.
 */
public class EventFilter extends Filter {

    private VaultEntryType vaultEntryType;

    /**
     * Constructor initialize Parameter for comparing later
     * @param vaultEntryType 
     */
    public EventFilter(VaultEntryType vaultEntryType) {
        this.vaultEntryType = vaultEntryType;
    }

    @Override
    FilterType getType() {
        return FilterType.EVENT_FILTER;
    }

    @Override
    boolean matchesFilterParameters(VaultEntry entry) {
        return entry.getType().equals(vaultEntryType);
    }

    @Override
    Filter update(VaultEntry vaultEntry) {
        return new EventFilter(vaultEntry.getType());
    }
}
