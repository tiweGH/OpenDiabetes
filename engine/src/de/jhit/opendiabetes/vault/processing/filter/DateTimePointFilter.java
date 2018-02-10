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
package de.jhit.opendiabetes.vault.processing.filter;

import de.jhit.opendiabetes.vault.container.VaultEntry;
import de.jhit.opendiabetes.vault.util.TimestampUtils;
import java.util.Date;
import java.util.List;
import static java.util.concurrent.TimeUnit.MILLISECONDS;
import static java.util.concurrent.TimeUnit.MINUTES;

/**
 *
 * @author gizem
 */
public class DateTimePointFilter extends Filter {

    private Date startTime;
    private Date endTime;
    private long marginBefore;
    private long marginAfter;

    /**
     * Initialize fields and calculates: startTime: date - marginInMinutes
     * endTime: date + marginInMinutes
     *
     * @param dateTimePoint
     * @param marginInMinutes
     */
    public DateTimePointFilter(Date dateTimePoint, int marginInMinutes) {
        this(dateTimePoint, marginInMinutes, marginInMinutes);
    }

    /**
     * Initialize fields and calculates:
     * <p>
     * startTime: date - marginBeforeInMinutes<p>
     * endTime: date + marginAfterInMinutes
     *
     * @param dateTimePoint
     * @param marginBeforeInMinutes
     * @param marginAfterInMinutes
     */
    public DateTimePointFilter(Date dateTimePoint, int marginBeforeInMinutes, int marginAfterInMinutes) {
        marginBefore = MILLISECONDS.convert(marginBeforeInMinutes, MINUTES);
        marginAfter = MILLISECONDS.convert(marginAfterInMinutes, MINUTES);
        startTime = new Date(dateTimePoint.getTime() - marginBeforeInMinutes);
        endTime = new Date(dateTimePoint.getTime() + marginAfterInMinutes);
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
        return new DateTimePointFilter(vaultEntry.getTimestamp(), (int) marginBefore, (int) marginAfter);
    }

}
