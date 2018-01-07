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
import de.jhit.opendiabetes.vault.util.TimestampUtils;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javafx.util.Pair;

/**
 *abstract Filter implements filter method for all Filters. All other Filter have to extend this Filter.
 * 
 * @author Daniel
 */
public abstract class Filter {
    
    /**
     * Returns the name of the extended Filter
     * @return FilterType
     */
    abstract FilterType getType();

    /**
     * Check if the given VaultEntry matches the given criteria. Needs to be implemented. Is used in Method filter. 
     * @param entry
     * @return boolean
     */
    abstract boolean matchesFilterParameters(VaultEntry entry);

    /**
     * Check if the given List of VaultEntrys matches the criteria of the Filter.
     * @param data
     * @return Filterresult
     */
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

    /**
     * This Method returns a new Filter from the Filtertype.
     * @param vaultEntry; Which the The Filter should work with
     * @return 
     */
    abstract Filter update(VaultEntry vaultEntry);
   
}
