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
import java.time.LocalTime;
import java.time.temporal.ChronoUnit;
import java.util.List;

/**
 *
 * @author juehv
 */
public class TimePointFilter implements Filter {

    private final TimeSpanFilter filter;

    public TimePointFilter(LocalTime timePoint, int marginInMinutes) {
        LocalTime startTime = timePoint.minus(marginInMinutes, ChronoUnit.MINUTES);
        LocalTime endTime = timePoint.plus(marginInMinutes, ChronoUnit.MINUTES);
        filter = new TimeSpanFilter(startTime, endTime);
    }

    @Override
    public FilterResult filter(List<VaultEntry> data) {
        return filter.filter(data);
    }

    @Override
    public FilterType getType() {
        return FilterType.TIME_POINT;
    }

}
