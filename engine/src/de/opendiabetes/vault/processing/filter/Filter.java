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
package de.opendiabetes.vault.processing.filter;

import de.opendiabetes.vault.container.VaultEntry;
import de.opendiabetes.vault.processing.filter.options.FilterOption;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javafx.util.Pair;

/**
 * abstract Filter implements filter method for all Filters. All other Filter
 * have to extend this Filter.
 *
 * @author Daniel
 */
public abstract class Filter {

    FilterOption option;

    public Filter(FilterOption option) {
        this.option = option;
    }

    /**
     * Returns the name of the extended Filter
     *
     * @return FilterType
     */
    abstract FilterType getType();

    /**
     * Check if the given VaultEntry matches the given criteria. Needs to be
     * implemented. Is used in Method filter.
     *
     * @param entry
     * @return boolean
     */
    abstract boolean matchesFilterParameters(VaultEntry entry);

    /**
     * Check if the given List of VaultEntrys matches the criteria of the
     * Filter.
     *
     * @param data
     * @return Filterresult
     */
    public FilterResult filter(List<VaultEntry> data) {
        FilterResult filterResult;
        List<Pair<Date, Date>> timeSeries = new ArrayList<>();
        List<VaultEntry> preprocessedData = setUpBeforeFilter(data);

        if (this.getType() == FilterType.NONE || this.getType() == FilterType.MARKER) {
            //Bypass, since nothing has to be filtered
            if (preprocessedData != null && preprocessedData.size() > 0) {
                timeSeries.add(new Pair<>(preprocessedData.get(0).getTimestamp(), preprocessedData.get(data.size() - 1).getTimestamp()));
            }
            filterResult = new FilterResult(preprocessedData, timeSeries);
        } else {
            //standard Filter process
            List<VaultEntry> entryResult = new ArrayList<>();
            Date startOfCurentTimeSeries = null;
            Date lastTimeStamp = null;

            for (VaultEntry entry : preprocessedData) {
                if (matchesFilterParameters(entry)) {
                    entryResult.add(entry);
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

            filterResult = new FilterResult(entryResult, timeSeries);
        }
        filterResult = tearDownAfterFilter(filterResult);
        return filterResult;
    }

    /**
     * This Method returns a new Filter from extended Filter. The Filter is
     * constructucted with the Data from the vaultEntry. Example: TimeFilter
     * Constructs a Filter with the startTime from the vaultEntry.
     *
     * @param vaultEntry; Which the The Filter should work with
     * @return
     */
    abstract Filter update(VaultEntry vaultEntry);

    /**
     * This method is run before the core filter process starts and CAN be
     * overridden if the specific filter needs to prepare the given entry data
     * to be filtered.<p>
     * If other preparations are necessary, which don't affect the given entry
     * data, just return it as it is.
     *
     * @param data the given initial entry data
     * @return modified or unmodified entry data
     */
    protected List<VaultEntry> setUpBeforeFilter(List<VaultEntry> data) {
        return data;
    }

    /**
     * This method is run after the core filter process ends and CAN be
     * overridden if the specific filter needs to prepare the FilterResult.
     * <p>
     * If other actions after filtering are necessary, which don't affect the
     * given FilterResult , just return it as it is.
     *
     * @param givenResult data after the filter process was executed
     * @return modified or unmodified FilterResult
     */
    protected FilterResult tearDownAfterFilter(FilterResult givenResult) {
        return givenResult;
    }

}
