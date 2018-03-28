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
package de.opendiabetes.vault.processing.filter;

import de.opendiabetes.vault.container.VaultEntry;
import de.opendiabetes.vault.container.VaultEntryTypeGroup;
import de.opendiabetes.vault.processing.filter.options.FilterOption;
import de.opendiabetes.vault.processing.filter.options.TypeGroupFilterOption;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author tiweGH This class extends filter. The TypeGroupFilter will check if an given Group equals the Group from the vaultEntry.
 */
public class TypeGroupFilter extends Filter {

    private VaultEntryTypeGroup entryTypeGroup;

    /**
     * Sets the VaulEntryTypeGroup from the given Filteroption
     * 
     * @param option TypeGroupFilterOption
     */
    public TypeGroupFilter(FilterOption option) {
        super(option);
        if (option instanceof TypeGroupFilterOption) {
            this.entryTypeGroup = ((TypeGroupFilterOption) option).getVaultEntryTypeGroup();
        } else {
            String msg = "Option has to be an instance of TypeGroupFilterOption";
            Logger.getLogger(this.getClass().getName()).log(Level.SEVERE, msg);
            throw new Error(msg);//IllegalArgumentException("Option has to be an instance of CombinationFilterOption");
        }
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
        return new TypeGroupFilter(new TypeGroupFilterOption(vaultEntry.getType().getGroup()));
    }
}
