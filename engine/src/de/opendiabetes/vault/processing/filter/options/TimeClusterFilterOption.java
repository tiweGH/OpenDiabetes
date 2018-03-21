/*
 * Copyright (C) 2018 tiweGH
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
package de.opendiabetes.vault.processing.filter.options;

import de.opendiabetes.vault.processing.filter.Filter;
import de.opendiabetes.vault.processing.filter.TimeClusterFilter;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author tiweGH
 */
public class TimeClusterFilterOption extends FilterOption {

    private List<Filter> filters;
    private final LocalTime startTime;
    private final long clusterTimeInMinutes;
    private long clusterSpacing;

    public static final long HOUR = TimeClusterFilter.HOUR;
    public static final long DAY = TimeClusterFilter.DAY;
    public static final long WEEK = TimeClusterFilter.WEEK;
    public static final long YEAR = TimeClusterFilter.YEAR;

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
    public TimeClusterFilterOption(List<Filter> filters, LocalTime startTime, long clusterTimeInMinutes, long clusterSpacing) {
        this.filters = filters;
        this.clusterTimeInMinutes = clusterTimeInMinutes;
        this.startTime = startTime;
        this.clusterSpacing = clusterSpacing;
    }

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
     * @param filter filter, which will be applied to each timespan in a slicing
     * process.
     * @param startTime start time of the timespan
     * @param clusterTimeInMinutes length of the timespan in minutes
     * @param clusterSpacing length of the gaps between each timespan in minutes
     */
    public TimeClusterFilterOption(Filter filter, LocalTime startTime, long clusterTimeInMinutes, long clusterSpacing) {
        List<Filter> filters = new ArrayList<>();
        filters.add(filter);
        this.filters = filters;
        this.clusterTimeInMinutes = clusterTimeInMinutes;
        this.startTime = startTime;
        this.clusterSpacing = clusterSpacing;
    }

    public List<Filter> getFilters() {
        return filters;
    }

    public LocalTime getStartTime() {
        return startTime;
    }

    public long getClusterTimeInMinutes() {
        return clusterTimeInMinutes;
    }

    public long getClusterSpacing() {
        return clusterSpacing;
    }

}
