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
import de.jhit.opendiabetes.vault.processing.filter.options.FilterOption;
import de.jhit.opendiabetes.vault.processing.filter.options.TimePointFilterOption;
import de.jhit.opendiabetes.vault.util.TimestampUtils;
import java.time.LocalTime;
import java.time.temporal.ChronoUnit;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * This Filter checks if the given VaultEntry is in an given range of Time.
 *
 * @author Daniel
 */
public class TimePointFilter extends Filter {

    private LocalTime endTime;
    private LocalTime startTime;
    private LocalTime timePoint;
    private final int marginBeforeInMinutes;
    private final int marginAfterInMinutes;

    public TimePointFilter(FilterOption option) {

        super(option);
        if (option instanceof TimePointFilterOption) {
            this.marginBeforeInMinutes = ((TimePointFilterOption) option).getMarginBeforeInMinutes();
            this.marginAfterInMinutes = ((TimePointFilterOption) option).getmarginAfterInMinutes();
            timePoint = ((TimePointFilterOption) option).getTimePoint();
            startTime = timePoint.minus(marginBeforeInMinutes, ChronoUnit.MINUTES);
            endTime = timePoint.plus(marginAfterInMinutes, ChronoUnit.MINUTES);
        } else {
            String msg = "Option has to be an instance of TimePointFilterOption";
            Logger.getLogger(this.getClass().getName()).log(Level.SEVERE, msg);
            throw new Error(msg);//IllegalArgumentException("Option has to be an instance of CombinationFilterOption");
        }
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
        option = new TimePointFilterOption(TimestampUtils.dateToLocalTime(vaultEntry.getTimestamp()), marginBeforeInMinutes, marginAfterInMinutes);
        return new TimePointFilter(option);
    }

}
