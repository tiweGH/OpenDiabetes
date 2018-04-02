/*
 * Copyright (C) 2017 gizem
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
import de.opendiabetes.vault.processing.filter.options.DateTimePointFilterOption;
import de.opendiabetes.vault.processing.filter.options.FilterOption;
import de.opendiabetes.vault.util.TimestampUtils;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Filters entries with a specific date (compared to the TimePointFilter)
 *
 * @author tiweGH
 */
public class DateTimePointFilter extends Filter {

    private Date startTime;
    private Date endTime;
    private Date dateTimePoint;
    private long marginBeforeInMinutes;
    private long marginAfterInMinutes;

    public DateTimePointFilter(FilterOption option) {
        super(option);
        if (option instanceof DateTimePointFilterOption) {

            marginBeforeInMinutes = ((DateTimePointFilterOption) option).getMarginBeforeInMinutes();
            marginAfterInMinutes = ((DateTimePointFilterOption) option).getAfterBeforeInMinutes();
            dateTimePoint = ((DateTimePointFilterOption) option).getDateTimePoint();

            startTime = TimestampUtils.addMinutesToTimestamp(dateTimePoint, (-1) * marginBeforeInMinutes);
            endTime = TimestampUtils.addMinutesToTimestamp(dateTimePoint, marginAfterInMinutes);
        } else {
            String msg = "Option has to be an instance of DateTimePointFilterOption";
            Logger.getLogger(this.getClass().getName()).log(Level.SEVERE, msg);
            throw new Error(msg);//IllegalArgumentException("Option has to be an instance of CombinationFilterOption");
        }
    }

    @Override
    public FilterType getType() {
        return FilterType.DATE_TIME_POINT;
    }

    @Override
    boolean matchesFilterParameters(VaultEntry entry) {
        return TimestampUtils.withinDateTimeSpan(startTime, endTime, entry.getTimestamp());

    }

    @Override
    Filter update(VaultEntry vaultEntry) {
        option = new DateTimePointFilterOption(vaultEntry.getTimestamp(), (int) marginBeforeInMinutes, (int) marginAfterInMinutes);
        return new DateTimePointFilter(option);
    }

}
