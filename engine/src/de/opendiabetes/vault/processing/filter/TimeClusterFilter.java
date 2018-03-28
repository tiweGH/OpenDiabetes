/*
 * Copyright (C) 2018 Daniel
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
import de.opendiabetes.vault.processing.filter.options.FilterOption;
import de.opendiabetes.vault.processing.filter.options.TimeClusterFilterOption;
import de.opendiabetes.vault.util.TimestampUtils;
import de.opendiabetes.vault.util.VaultEntryUtils;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * The TimeClusterFilter is a more complex timespan-filter, filters given to the
 * TimeClusterFilter will be applied to each timespan seperately, similar to the
 * slicing process.<br>
 * The timespans can be a fragmentation of the whole entry dataset, or, with a
 * gap greater than 0, spans between the clusters will be excluded from
 * filtering.<p>
 * For example, use <code>TimeClusterFilter.DAY</code> for
 * <code>clusterSpacing</code>, to filter the same timespan of each day
 * independently.
 *
 * @author tiweGH
 */
public class TimeClusterFilter extends Filter {

//    private DatasetMarker dataPointer;
    private List<VaultEntry> clusterFilterResult;
    private List<Filter> filters;
    private final LocalTime startTime;
    private final long clusterTimeInMinutes;
    private long clusterSpacing;

    public static final long HOUR = 60;
    public static final long DAY = 24 * HOUR;
    public static final long WEEK = 7 * DAY;
    public static final long YEAR = 365 * DAY;

    /**
     * Sets the filters, clusterTime, startTime an Clusterspacing from the given
     * Options Object.
     *
     * @param option TimeClusterFilterOption
     */
    public TimeClusterFilter(FilterOption option) {
        super(option);
        if (option instanceof TimeClusterFilterOption) {
            this.filters = ((TimeClusterFilterOption) option).getFilters();
            this.clusterTimeInMinutes = ((TimeClusterFilterOption) option).getClusterTimeInMinutes();
            this.startTime = ((TimeClusterFilterOption) option).getStartTime();
            this.clusterSpacing = ((TimeClusterFilterOption) option).getClusterSpacing();
        } else {
            String msg = "Option has to be an instance of TimeClusterFilterOption";
            Logger.getLogger(this.getClass().getName()).log(Level.SEVERE, msg);
            throw new Error(msg);
        }
    }

    @Override
    protected List<VaultEntry> setUpBeforeFilter(List<VaultEntry> data) {
        List<VaultEntry> result = data;
        if (clusterTimeInMinutes > 0 && filters != null && data != null) {
            List<VaultEntry> clusteredList = new ArrayList<>();
            List<VaultEntry> clusterResult = new ArrayList<>();
            Date startTimestamp = null;
            Date compareDate = null;
            boolean isLastIndex;
            int index = 0;

            for (VaultEntry vaultEntry : result) {
                if (startTimestamp == null) {
                    startTimestamp = (this.startTime != null) ? TimestampUtils.setTimeOfDate(vaultEntry.getTimestamp(), this.startTime) : vaultEntry.getTimestamp();
                    compareDate = TimestampUtils.addMinutesToTimestamp(startTimestamp, clusterTimeInMinutes);
                }
                isLastIndex = index == result.size() - 1;
                // compareDate = TimestampUtils.addMinutesToTimestamp(startTimestamp, clusterTimeInMinutes);
                if (!vaultEntry.getTimestamp().before(startTimestamp) && vaultEntry.getTimestamp().before(compareDate)) {
                    clusteredList.add(vaultEntry);
                }
                if (isLastIndex || !vaultEntry.getTimestamp().before(compareDate)) {

                    if (clusteredList.size() > 0) {

                        clusteredList = VaultEntryUtils.slice(clusteredList, filters).filteredData;
                    }

                    startTimestamp = TimestampUtils.addMinutesToTimestamp(compareDate, clusterSpacing);

                    compareDate = TimestampUtils.addMinutesToTimestamp(startTimestamp, clusterTimeInMinutes);
                    clusterResult.addAll(clusteredList);
                    clusteredList = new ArrayList<>();

                    if (!vaultEntry.getTimestamp().before(startTimestamp) && vaultEntry.getTimestamp().before(compareDate)) {
                        clusteredList.add(vaultEntry);
                        if (isLastIndex && !clusterResult.contains(vaultEntry)) {
                            clusteredList = VaultEntryUtils.slice(clusteredList, filters).filteredData;
                            clusterResult.addAll(clusteredList);

                        }
                    }
                }
                index++;

            }
            //result = clusterResult;
            this.clusterFilterResult = clusterResult;
            //result = VaultEntryUtils.sort(result);
        }
        return data;
    }

    @Override
    FilterType getType() {
        return FilterType.CLUSTER;
    }

    @Override
    boolean matchesFilterParameters(VaultEntry entry) {
        return clusterFilterResult.contains(entry);
    }

    @Override
    Filter update(VaultEntry vaultEntry) {
        return new TimeClusterFilter(option);
    }

}
