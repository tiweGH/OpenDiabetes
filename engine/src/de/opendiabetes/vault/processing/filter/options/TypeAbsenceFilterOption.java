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

import de.opendiabetes.vault.container.VaultEntryType;
import de.opendiabetes.vault.container.VaultEntryTypeGroup;

/**
 *
 * @author tiweGH
 */
public class TypeAbsenceFilterOption extends FilterOption {

    private final long marginAfterTrigger; // minutes after a trigger until data becomes interesting again.
    private final VaultEntryType vaultEntryType;
    private final VaultEntryTypeGroup vaultEntryTypeGroup;

    /**
     * The Filter gets an EntryType and excludes all entries from the
     * FilterResult, whose EntryType match or are located in the time margin
     * after a trigger of the group occurs
     *
     * @param marginAfterTrigger minutes after a trigger until data becomes
     * interesting again
     * @param vaultEntryType
     * @param vaultEntryTypeGroup
     */
    public TypeAbsenceFilterOption(VaultEntryTypeGroup vaultEntryTypeGroup, VaultEntryType vaultEntryType, long marginAfterTrigger) {
        this.vaultEntryTypeGroup = vaultEntryTypeGroup;
        this.vaultEntryType = vaultEntryType;
        this.marginAfterTrigger = marginAfterTrigger;
    }

    public TypeAbsenceFilterOption(VaultEntryTypeGroup vaultEntryTypeGroup, long marginAfterTrigger) {
        this.vaultEntryTypeGroup = vaultEntryTypeGroup;
        this.vaultEntryType = null;
        this.marginAfterTrigger = marginAfterTrigger;
    }

    public VaultEntryTypeGroup getVaultEntryTypeGroup() {
        return vaultEntryTypeGroup;
    }

    public VaultEntryType getVaultEntryType() {
        return vaultEntryType;
    }

    public long getMargingAfterTrigger() {
        return marginAfterTrigger;
    }

}
