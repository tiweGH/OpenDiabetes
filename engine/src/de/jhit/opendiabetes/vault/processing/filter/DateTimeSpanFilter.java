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
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javafx.util.Pair;

/**
 *
 * @author gizem
 */
public class DateTimeSpanFilter implements Filter {

    private final Date startTime;
    private final Date endTime;

    public DateTimeSpanFilter(Date startTime, Date endTime) {
        this.startTime = startTime;
        this.endTime = endTime;
    }

    @Override
    public FilterResult filter(List<VaultEntry> data) {
        List<VaultEntry> filteredData = new ArrayList<>();
        List<Pair<Date, Date>> timeSeries = new ArrayList<>();

        Date startOfCuttenTimeSeries = null;
        Date lastTimeStamp = null;
        for (VaultEntry entry : data) {
            if (TimestampUtils.withinDateTimeSpan(startTime, endTime, entry.getTimestamp())) {
                filteredData.add(entry);
                if (startOfCuttenTimeSeries == null) {
                    startOfCuttenTimeSeries = entry.getTimestamp();
                }
                lastTimeStamp = entry.getTimestamp();
            } else if (startOfCuttenTimeSeries != null) {
                timeSeries.add(new Pair<>(startOfCuttenTimeSeries, lastTimeStamp));
                startOfCuttenTimeSeries = null;
            }
        }

        if (startOfCuttenTimeSeries != null) {
            timeSeries.add(new Pair<>(startOfCuttenTimeSeries, lastTimeStamp));
        }

        return new FilterResult(filteredData, timeSeries);
    }

    @Override
    public FilterType getType() {
        return FilterType.DATE_TIME_SPAN;
    }

}
