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
import de.jhit.opendiabetes.vault.container.VaultEntryTypeGroup;

/**
 *
 * @author tiweGH This class extends filter and checks if the given
 * vaultEntryTypeGroup matches.
 */
public class TypeGroupFilter extends Filter {

    private VaultEntryTypeGroup entryTypeGroup;

    /**
     * Filters entries with a type in the matching type-group
     *
     * @param entryTypeGroup
     */
    public TypeGroupFilter(VaultEntryTypeGroup entryTypeGroup) {
        this.entryTypeGroup = entryTypeGroup;
    }

    @Override
    FilterType getType() {
        return FilterType.GROUP;
    }

    @Override
    boolean matchesFilterParameters(VaultEntry entry) {
        return entry.getType().getGroup() == entryTypeGroup;
    }

    @Override
    Filter update(VaultEntry vaultEntry) {
        return new TypeGroupFilter(vaultEntry.getType().getGroup());
    }
}
