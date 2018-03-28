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
import de.opendiabetes.vault.container.VaultEntryType;
import de.opendiabetes.vault.processing.filter.options.FilterOption;
import de.opendiabetes.vault.processing.filter.options.VaultEntryTypeFilterOption;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Daniel 
 * This class extends Filter. The filter() Method checks if the vaultentrytype of the vaultEntry equals the given vaultentry.
 */
public class VaultEntryTypeFilter extends Filter {

    private VaultEntryType vaultEntryType;

    /**
     * Sets the vaultEntryType which will later be used in the filter mechanism.
     * 
     * @param option VaultEntryTypeFilterOption
     */
    public VaultEntryTypeFilter(FilterOption option) {
        super(option);
        if (option instanceof VaultEntryTypeFilterOption) {
            this.vaultEntryType = ((VaultEntryTypeFilterOption) option).getVaultEntryType();
        } else {
            String msg = "Option has to be an instance of VaultEntryTypeFilterOption";
            Logger.getLogger(this.getClass().getName()).log(Level.SEVERE, msg);
            throw new Error(msg);//IllegalArgumentException("Option has to be an instance of CombinationFilterOption");
        }

    }

    @Override
    FilterType getType() {
        return FilterType.VAULT_ENTRY_TYPE_FILTER;
    }

    @Override
    boolean matchesFilterParameters(VaultEntry entry) {
        return entry.getType().equals(vaultEntryType);
    }

    @Override
    Filter update(VaultEntry vaultEntry) {
        return new VaultEntryTypeFilter(new VaultEntryTypeFilterOption(vaultEntry.getType()));
    }
}
