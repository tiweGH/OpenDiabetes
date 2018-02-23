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
package de.jhit.opendiabetes.vault.processing.filter;

import de.jhit.opendiabetes.vault.container.VaultEntry;
import de.jhit.opendiabetes.vault.util.TimestampUtils;
import de.jhit.opendiabetes.vault.util.VaultEntryUtils;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javafx.util.Pair;

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
     * Filters given to the TimeClusterFilter will be applied to each timespan
     * seperately. <br>
     * The timespans can be a fragmentation of the whole entry dataset, or, with
     * a gap greater than 0, spans between the clusters will be excluded from
     * filtering.<p>
     * For example, use <code>TimeClusterFilter.DAY</code> for
     * <code>clusterSpacing</code>, to filter the same timespan of each day
     * independently.
     *
     * @param filters list of filters, which will be applied to each timespan in
     * a slicing process.
     * @param startTime start time of the timespan
     * @param clusterTimeInMinutes length of the timespan in minutes
     * @param clusterSpacing length of the gaps between each timespan in minutes
     */
    public TimeClusterFilter(List<Filter> filters, LocalTime startTime, long clusterTimeInMinutes, long clusterSpacing) {
        this.filters = filters;
        this.clusterTimeInMinutes = clusterTimeInMinutes;
        this.startTime = startTime;
        this.clusterSpacing = clusterSpacing;
    }

    /**
     * Filters given to the TimeClusterFilter will be applied to each timespan
     * seperately<br>
     * The timespans can be a fragmentation of the whole entry dataset, or, with
     * a gap greater than 0, spans between the clusters will be excluded from
     * filtering.<p>
     * For example, use <code>TimeClusterFilter.DAY</code> for
     * <code>clusterSpacing</code>, to filter the same timespan of each day
     * independently.
     *
     * @param filters list of filters, which will be applied to each timespan in
     * a slicing process.
     * @param clusterTimeInMinutes length of the timespan in minutes
     * @param clusterSpacing length of the gaps between each timespan in minutes
     */
    public TimeClusterFilter(List<Filter> filters, long clusterTimeInMinutes, long clusterSpacing) {
        this.filters = filters;
        this.clusterTimeInMinutes = clusterTimeInMinutes;
        this.startTime = null;
        this.clusterSpacing = clusterSpacing;
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

            for (VaultEntry vaultEntry : result) {
                if (startTimestamp == null) {
                    startTimestamp = (this.startTime != null) ? TimestampUtils.setTimeOfDate(vaultEntry.getTimestamp(), this.startTime) : vaultEntry.getTimestamp();
                    compareDate = TimestampUtils.addMinutesToTimestamp(startTimestamp, clusterTimeInMinutes);
                }
                isLastIndex = result.indexOf(vaultEntry) == result.size() - 1;
                // compareDate = TimestampUtils.addMinutesToTimestamp(startTimestamp, clusterTimeInMinutes);
                if (!vaultEntry.getTimestamp().before(startTimestamp) && vaultEntry.getTimestamp().before(compareDate)) {
                    clusteredList.add(vaultEntry);
                } //else {
//                    System.out.println("not " + vaultEntry.getTimestamp());
//                    System.out.println(!vaultEntry.getTimestamp().before(startTimestamp) + " && " + vaultEntry.getTimestamp().before(compareDate) + " || " + isLastIndex);
//                }
                if (isLastIndex || !vaultEntry.getTimestamp().before(compareDate)) {

                    if (clusteredList.size() > 0) {
//                        System.out.println("New Cluster between " + startTimestamp + " and " + compareDate);
//                        System.out.println("with " + clusteredList.size() + " entries");
//                        System.out.println("first " + clusteredList.get(0).getTimestamp() + " last " + clusteredList.get(clusteredList.size() - 1).getTimestamp());
//                        System.out.println();
                        clusteredList = VaultEntryUtils.slice(clusteredList, filters).filteredData;
                    }
//                    else {
//                        System.out.println("wo? " + vaultEntry.getTimestamp());
//                    }

                    startTimestamp = TimestampUtils.addMinutesToTimestamp(compareDate, clusterSpacing);
//                    System.out.println("new start: " + startTimestamp);
//                    System.out.println("we're here: " + vaultEntry.getTimestamp());
                    compareDate = TimestampUtils.addMinutesToTimestamp(startTimestamp, clusterTimeInMinutes);
                    clusterResult.addAll(clusteredList);
                    clusteredList = new ArrayList<>();

                    if (!vaultEntry.getTimestamp().before(startTimestamp) && vaultEntry.getTimestamp().before(compareDate)) {
                        clusteredList.add(vaultEntry);
                        if (isLastIndex && !clusterResult.contains(vaultEntry)) {
                            clusteredList = VaultEntryUtils.slice(clusteredList, filters).filteredData;
                            clusterResult.addAll(clusteredList);
//                            System.out.println("New Cluster between " + startTimestamp + " and " + compareDate);
//                            System.out.println("with " + clusteredList.size() + " entries");
//                            System.out.println("first " + clusteredList.get(0).getTimestamp() + " last " + clusteredList.get(clusteredList.size() - 1).getTimestamp());
//                            System.out.println();
                        }
                    }
                }

            }
            result = clusterResult;
            this.clusterFilterResult = clusterResult;
            //result = VaultEntryUtils.sort(result);
        }
        return data;
    }

    @Override
    FilterType getType() {
        return FilterType.COMBINATION_FILTER;
    }

    @Override
    boolean matchesFilterParameters(VaultEntry entry) {
        return clusterFilterResult.contains(entry);
    }

    @Override
    Filter update(VaultEntry vaultEntry) {
        return new TimeClusterFilter(this.filters, this.startTime, this.clusterTimeInMinutes, this.clusterSpacing);
    }

}
