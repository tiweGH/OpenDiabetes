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
import java.time.LocalTime;
import java.time.temporal.ChronoUnit;

/**
 * Checks if the given VaultEntry is between two timepoints.
 * @author Jorg, Daniel
 */
public class EventTimePointFilter extends Filter {
    private LocalTime endTime;
    private LocalTime startTime;
    private final VaultEntryType vaultEntryType;

    public EventTimePointFilter(VaultEntryType vaultEntryType, LocalTime timePoint, int marginInMinutes) {
        startTime = timePoint.minus(marginInMinutes, ChronoUnit.MINUTES);
        endTime= timePoint.plus(marginInMinutes, ChronoUnit.MINUTES);
        this.vaultEntryType = vaultEntryType;
    }

    @Override
    FilterType getType() {
        return FilterType.TIME_POINT;
    }

    @Override
    boolean matchesFilterParameters(VaultEntry entry) {
        return entry.getType()== vaultEntryType && TimestampUtils.withinTimeSpan(startTime, endTime, entry.getTimestamp()); 
    }

}
