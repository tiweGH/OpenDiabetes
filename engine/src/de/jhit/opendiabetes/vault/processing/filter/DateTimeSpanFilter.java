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
public class DateTimeSpanFilter extends Filter {

    private final Date startTime;
    private final Date endTime;

    public DateTimeSpanFilter(Date startTime, Date endTime) {
        this.startTime = startTime;
        this.endTime = endTime;
    }
    @Override
    public FilterType getType() {
        return FilterType.DATE_TIME_SPAN;
    }

    @Override
    boolean matchesFilterParameters(VaultEntry entry) {
        return TimestampUtils.withinDateTimeSpan(startTime, endTime, entry.getTimestamp());
    }

    @Override
    Filter update(VaultEntry vaultEntry) {
        return new DateTimeSpanFilter(vaultEntry.getTimestamp(), endTime);
    }

}
