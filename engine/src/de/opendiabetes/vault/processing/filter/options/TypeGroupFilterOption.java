/*
 * Copyright (C) 2018 tiweGH
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
package de.opendiabetes.vault.processing.filter.options;

import de.opendiabetes.vault.container.VaultEntryTypeGroup;

/**
 *
 * @author tiweGH
 */
public class TypeGroupFilterOption extends FilterOption {

    private final VaultEntryTypeGroup vaultEntryTypeGroup;

    /**
     * Filters entries with a type in the matching type-group
     *
     * @param vaultEntryTypeGroup
     */
    public TypeGroupFilterOption(VaultEntryTypeGroup vaultEntryTypeGroup) {
        this.vaultEntryTypeGroup = vaultEntryTypeGroup;

    }

    public VaultEntryTypeGroup getVaultEntryTypeGroup() {
        return vaultEntryTypeGroup;
    }

}
