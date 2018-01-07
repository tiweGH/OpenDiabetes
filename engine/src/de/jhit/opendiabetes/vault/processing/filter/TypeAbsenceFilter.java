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
package de.jhit.opendiabetes.vault.processing.filter;

import de.jhit.opendiabetes.vault.container.VaultEntry;
import de.jhit.opendiabetes.vault.container.VaultEntryType;
import de.jhit.opendiabetes.vault.util.TimestampUtils;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javafx.util.Pair;

/**
 *
 * @author tiweGH
 */
public class TypeAbsenceFilter extends Filter {

    private final long marginAfterTrigger; // minutes after a trigger until data becomes interesting again.
    private final List<VaultEntryType> types;
    Date lastMealFound = null;

    /**
     * The Filter gets a List of EntryTypes and excludes all entries from the
     * FilterResult, whose EntryType belong to the list or are located in the
     * time margin after a trigger of the group occurs
     *
     * @param types a List of EntryTypes
     * @param marginAfterTrigger minutes after a trigger until data becomes
     * interesting again
     */
    public TypeAbsenceFilter(List<VaultEntryType> types, long marginAfterTrigger) {
        this.marginAfterTrigger = marginAfterTrigger;
        this.types = types;
    }

    /**
     * The filter either gets a group of EntryTypes, or a specific EntryType and
     * excludes all entries from the FilterResult, whose EntryType belong to the
     * group or are located in the time margin after a trigger of the group
     * occurs.
     * <p>
     * For more information about groups, look at VaultEntryType.java.
     *
     * @param typeGroup either a group of EntryTypes, or a specific EntryType
     * @param marginAfterTrigger minutes after a trigger until data becomes
     * interesting again
     */
    public TypeAbsenceFilter(VaultEntryType typeGroup, long marginAfterTrigger) {
        //this(VaultEntryType.getTypesOfGroup(typeGroup), marginAfterTrigger);
        if (typeGroup.getGROUP() != typeGroup) {
            this.types = new ArrayList<>();
            types.add(typeGroup);
        } else {
            this.types = VaultEntryType.getTypesOfGroup(typeGroup);
        }
        this.marginAfterTrigger = marginAfterTrigger;
    }

    @Override
    boolean matchesFilterParameters(VaultEntry entry) {
        boolean result = true;
        if (types.contains(entry.getType())) {
            lastMealFound = entry.getTimestamp();
            result = false;
        } else if ((lastMealFound != null
                && !TimestampUtils
                        .addMinutesToTimestamp(lastMealFound, marginAfterTrigger)
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
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

}
