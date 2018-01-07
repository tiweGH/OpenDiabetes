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
 * Checks if the given VaulEntey is between two values.
 * @author Daniel
 */
public class EventSpanFilter extends Filter {

    private VaultEntryType vaultEntryType;
    private final float from;
    private final float to;

    /**
     * Initialize fields for functions.
     * 
     * @param vaultEntryType
     * @param from
     * @param to 
     */
    public EventSpanFilter(VaultEntryType vaultEntryType, float from, float to) {
        this.vaultEntryType = vaultEntryType;
        this.from = from;
        this.to = to;
    }

    @Override
    FilterType getType() {
        return FilterType.EVENT_SPAN_FILTER;
    }

    @Override
    boolean matchesFilterParameters(VaultEntry entry) {
        return entry.getType().equals(vaultEntryType) && entry.getValue()>=from && entry.getValue() <= to;
    }

    @Override
    Filter update(VaultEntry vaultEntry) {
        return new EventSpanFilter(vaultEntry.getType(), from, to);
    }
}
