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
import de.jhit.opendiabetes.vault.util.TimestampUtils;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

/**
 * Check if the given VaultEntry Timestamp is in a specified range
 * @author Daniel
 */
public class EventTimeSpanFilter extends Filter {

    private VaultEntryType vaultEntryType;
    private final LocalTime startTime;
    private final LocalTime endTime;
    
    /**
     * Initialize fields for functions.
     * 
     * @param vaultEntryType
     * @param startTime
     * @param endTime 
     */
    public EventTimeSpanFilter(VaultEntryType vaultEntryType, LocalTime startTime, LocalTime endTime ){
        this.vaultEntryType = vaultEntryType;
        this.startTime = startTime;
        this.endTime = endTime;
    }

    @Override
    FilterType getType() {
        return FilterType.EVENT_SPAN_FILTER;
    }

    @Override
    boolean matchesFilterParameters(VaultEntry entry) {
        return  entry.getType().equals(vaultEntryType) && TimestampUtils.withinTimeSpan(startTime, endTime, entry.getTimestamp());
    }

    @Override
    Filter update(VaultEntry vaultEntry) {
        return new EventTimeSpanFilter(vaultEntry.getType(), TimestampUtils.dateToLocalTime(vaultEntry.getTimestamp()), endTime);
    }
}
