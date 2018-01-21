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
import de.jhit.opendiabetes.vault.util.TimestampUtils;
import java.time.LocalTime;
import java.time.temporal.ChronoUnit;

/**
 * This Filter checks if the given VaultEntry is in an given range of Time.
 *
 * @author Daniel
 */
public class TimePointFilter extends Filter {

    private LocalTime endTime;
    private LocalTime startTime;
    private final int marginBeforeInMinutes;
    private final int marginAfterInMinutes;

    /**
     * Initialize fields and calculates: startTime: timepoint- marginInMinutes
     * endTime: timepoint+ marginInMinutes
     *
     * @param timePoint
     * @param marginInMinutes
     */
    public TimePointFilter(LocalTime timePoint, int marginInMinutes) {
        this(timePoint, marginInMinutes, marginInMinutes);
    }

    /**
     * Initialize fields and calculates:
     * <p>
     * startTime: timepoint- marginBeforeInMinutes<p>
     * endTime: timepoint+ marginAfterInMinutes
     *
     * @param timePoint
     * @param marginBeforeInMinutes
     * @param marginAfterInMinutes
     */
    public TimePointFilter(LocalTime timePoint, int marginBeforeInMinutes, int marginAfterInMinutes) {
        this.marginBeforeInMinutes = marginBeforeInMinutes;
        this.marginAfterInMinutes = marginAfterInMinutes;
        startTime = timePoint.minus(marginBeforeInMinutes, ChronoUnit.MINUTES);
        endTime = timePoint.plus(marginAfterInMinutes, ChronoUnit.MINUTES);
    }

    @Override
    FilterType getType() {
        return FilterType.TIME_POINT;
    }

    @Override
    boolean matchesFilterParameters(VaultEntry entry) {
        return TimestampUtils.withinTimeSpan(startTime, endTime, entry.getTimestamp());
    }

    @Override
    Filter update(VaultEntry vaultEntry) {
        return new TimePointFilter(TimestampUtils.dateToLocalTime(vaultEntry.getTimestamp()), marginBeforeInMinutes, marginAfterInMinutes);
    }

}
