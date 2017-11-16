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
import de.jhit.opendiabetes.vault.container.VaultEntryType;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import static java.util.concurrent.TimeUnit.MILLISECONDS;
import static java.util.concurrent.TimeUnit.MINUTES;

/**
 *
 * @author gizem
 */
public class EventFilter {

    public EventFilter() {
    }

    private boolean isInsideRange(List<VaultEntry> list, Date bolusDate, long buffer) {
        long threshold = MILLISECONDS.convert(buffer, MINUTES);
        long pre = bolusDate.getTime() - list.get(0).getTimestamp().getTime();
        long post = list.get(list.size() - 1).getTimestamp().getTime() - bolusDate.getTime();
        return (pre >= threshold && post >= threshold);
    }

    public List<Date> filter(List<VaultEntry> data, VaultEntryType event, long buffer) {
        List<Date> events = new ArrayList<>();
        if (data.isEmpty()) {
            return events;
        }

        Date begin = data.get(0).getTimestamp();
        Date end = data.get(data.size() - 1).getTimestamp();
        for (VaultEntry entry : data) {
            if (entry.getType().equals(event)) {
                if (isInsideRange(data, entry.getTimestamp(), buffer)) {
                    events.add(entry.getTimestamp());
                }
            }
        }
        return events;
    }

}
