/*
 * Copyright (C) 2017 juehv
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
import de.opendiabetes.vault.container.VaultEntryTypeGroup;
import de.opendiabetes.vault.processing.filter.options.FilterOption;
import de.opendiabetes.vault.processing.filter.options.TypeAbsenceFilterOption;
import de.opendiabetes.vault.util.TimestampUtils;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author tiweGH
 */
public class TypeAbsenceFilter extends Filter {

    private final long marginAfterTrigger; // minutes after a trigger until data becomes interesting again.
    private final VaultEntryType type;
    private final VaultEntryTypeGroup typeGroup;

    Date lastEntryTimeFound = null;

    public TypeAbsenceFilter(FilterOption option) {
        super(option);
        if (option instanceof TypeAbsenceFilterOption) {
            this.marginAfterTrigger = ((TypeAbsenceFilterOption) option).getMargingAfterTrigger();
            this.type = ((TypeAbsenceFilterOption) option).getVaultEntryType();
            this.typeGroup = ((TypeAbsenceFilterOption) option).getVaultEntryTypeGroup();
        } else {
            String msg = "Option has to be an instance of TypeAbsenceFilterOption";
            Logger.getLogger(this.getClass().getName()).log(Level.SEVERE, msg);
            throw new Error(msg);//IllegalArgumentException("Option has to be an instance of CombinationFilterOption");
        }
    }

    @Override
    boolean matchesFilterParameters(VaultEntry entry) {
        boolean result = true;

        //maybe buggy behaviour. Whole class better be refactored in separate filter, e.g. TypeFilter, groupFilter and ExclusionFilter
        if ((type != null && type == entry.getType()) || typeGroup == entry.getType().getGroup()) {
            lastEntryTimeFound = entry.getTimestamp();
            result = false;
        } else if ((lastEntryTimeFound != null
                && !TimestampUtils
                        .addMinutesToTimestamp(lastEntryTimeFound, marginAfterTrigger)
                        .before(entry.getTimestamp()))) {
            result = false;
        }

        return result;
    }

    @Override
    public FilterType getType() {
        return FilterType.TYPE_ABSENCE;
    }

    @Override
    Filter update(VaultEntry vaultEntry) {
        return new TypeAbsenceFilter(option);
    }

}
