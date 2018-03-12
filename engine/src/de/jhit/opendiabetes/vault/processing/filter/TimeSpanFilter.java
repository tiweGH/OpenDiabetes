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
import de.jhit.opendiabetes.vault.processing.filter.options.FilterOption;
import de.jhit.opendiabetes.vault.processing.filter.options.TimeSpanFilterOption;
import de.jhit.opendiabetes.vault.util.TimestampUtils;
import java.time.LocalTime;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author juehv
 */
public class TimeSpanFilter extends Filter {

    private final LocalTime startTime;
    private final LocalTime endTime;

    public TimeSpanFilter(FilterOption option) {
        super(option);
        if (option instanceof TimeSpanFilterOption) {
            this.startTime = ((TimeSpanFilterOption) option).getStartTime();
            this.endTime = ((TimeSpanFilterOption) option).getEndTime();
        } else {
            String msg = "Option has to be an instance of TimeSpanFilterOption";
            Logger.getLogger(this.getClass().getName()).log(Level.SEVERE, msg);
            throw new Error(msg);//IllegalArgumentException("Option has to be an instance of CombinationFilterOption");
        }
    }

    @Override
    public FilterType getType() {
        return FilterType.TIME_SPAN;
    }

    @Override
    boolean matchesFilterParameters(VaultEntry entry) {
        return TimestampUtils.withinTimeSpan(startTime, endTime, entry.getTimestamp());
    }

    @Override
    Filter update(VaultEntry vaultEntry) {
        option = new TimeSpanFilterOption(TimestampUtils.dateToLocalTime(vaultEntry.getTimestamp()), endTime);
        return new TimeSpanFilter(option);
    }

}
