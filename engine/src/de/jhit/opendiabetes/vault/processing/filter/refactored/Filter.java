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
package de.jhit.opendiabetes.vault.processing.filter.refactored;

import de.jhit.opendiabetes.vault.container.VaultEntry;
import de.jhit.opendiabetes.vault.util.TimestampUtils;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javafx.util.Pair;

/**
 *
 * @author juehv
 */
public abstract class Filter {
    
    abstract FilterType getType();

    abstract boolean matchesFilterParameters(VaultEntry entry);

    public FilterResult filter(List<VaultEntry> data) {

        List<VaultEntry> result = new ArrayList<>();
        List<Pair<Date, Date>> timeSeries = new ArrayList<>();

        Date startOfCurentTimeSeries = null;
        Date lastTimeStamp = null;
        for (VaultEntry entry : data) {
            if (matchesFilterParameters(entry)) {
                result.add(entry);
                if (startOfCurentTimeSeries == null) {
                    startOfCurentTimeSeries = entry.getTimestamp();
                }
                lastTimeStamp = entry.getTimestamp();
            } else if (startOfCurentTimeSeries != null) {
                timeSeries.add(new Pair<>(startOfCurentTimeSeries, lastTimeStamp));
                startOfCurentTimeSeries = null;
            }
        }

        if (startOfCurentTimeSeries != null) {
            timeSeries.add(new Pair<>(startOfCurentTimeSeries, lastTimeStamp));
        }

        return new FilterResult(result, timeSeries);
    }
    
    
   
}
