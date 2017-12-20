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
package de.jhit.opendiabetes.vault.processing.filter.old;

import de.jhit.opendiabetes.vault.container.VaultEntry;
import java.util.Date;
import java.util.List;
import static java.util.concurrent.TimeUnit.MILLISECONDS;
import static java.util.concurrent.TimeUnit.MINUTES;

/**
 *
 * @author gizem
 */
public class DateTimePointFilter implements Filter_old {

    private final DateTimeSpanFilter filter;

    public DateTimePointFilter(Date dateTimePoint, int marginInMinutes) {
        long margin = MILLISECONDS.convert(marginInMinutes, MINUTES);
        Date startTime = new Date(dateTimePoint.getTime() - margin);
        Date endTime = new Date(dateTimePoint.getTime() + margin);
        filter = new DateTimeSpanFilter(startTime, endTime);
    }

    @Override
    public FilterResult_old filter(List<VaultEntry> data) {
        return filter.filter(data);
    }

    @Override
    public FilterType_old getType() {
        return FilterType_old.DATE_TIME_POINT;
    }

}
