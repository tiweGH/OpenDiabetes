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
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javafx.util.Pair;

/**
 * The CombinationFilter is a special kind of Filter. The CombinationFilter
 * combines two Filter on different datasets. Another of it’s special Features
 * is, that the CombinationFilter has two lists of VaultEntry, which will be
 * used later in the .filter method. The first list of VaultEntry is set during
 * the Constructor, this set is the basicData. The Second list is only used
 * during the filter. In the Constructor there will also be set two Filter
 * (firstFilter and secondFilter).
 *
 * @author Daniel
 */
public class CombinationFilter extends Filter {

    private DatasetMarker dataPointer;
    private Filter firstFilter;
    private Filter secondFilter;
    private List<Filter> filters;

    /**
     * The Constructor will set the Filters and basic data, which will be used
     * later in the filter method.
     *
     * @param data original input entry data
     * @param firstFilter; first Filter from the data in the filter method
     * @param secondFilter; Filter mask for the list of Filters in the filter
     * method
     */
    public CombinationFilter(List<VaultEntry> data, Filter firstFilter, Filter secondFilter) {
        this(new DatasetMarker(data), firstFilter, secondFilter);
    }

    /**
     *
     * @param data original input entry data
     * @param combinatedFilter Filter mask for the previous result
     */
    public CombinationFilter(List<VaultEntry> data, Filter combinatedFilter) {
        this(data, new NoneFilter(), combinatedFilter);
    }

    /**
     * The Constructor will set the Filters and basic data, which will be used
     * later in the filter method.
     *
     * @param dataPointer pointer to the dataset to work on
     * @param firstFilter first Filter from the data in the filter method
     * @param secondFilter Filter mask for the list of Filters in the filter
     */
    public CombinationFilter(DatasetMarker dataPointer, Filter firstFilter, Filter secondFilter) {
        this.dataPointer = dataPointer;
        this.firstFilter = firstFilter;
        this.secondFilter = secondFilter;
    }

    @Override
    protected List<VaultEntry> setUpBeforeFilter(List<VaultEntry> data) {
        List<VaultEntry> firstResult = firstFilter.filter(data).filteredData;

        Filter tempFilter;
        // generates an List of Filters from the first found dataset. The secondFilter will be used as Mask.
        filters = new ArrayList<>();
        for (VaultEntry vaultEntry : firstResult) {
            tempFilter = secondFilter.update(vaultEntry);
            filters.add(tempFilter);
            //this can potentially slow down the process, if the setUp is complex and the dataset is big
            tempFilter.setUpBeforeFilter(firstResult);
        }

        //filters with the basic method
        return dataPointer.getDataset();
    }

    @Override
    FilterType getType() {
        return FilterType.COMBINATION_FILTER;
    }

    @Override
    boolean matchesFilterParameters(VaultEntry entry
    ) {
        boolean result = false;

        //Checks if one of the new generated Filters is True
        for (Filter filter : filters) {
            if (filter.matchesFilterParameters(entry)) {
                result = true;
            }
        }

        return result;
    }

    @Override
    Filter update(VaultEntry vaultEntry
    ) {
        return new CombinationFilter(dataPointer, firstFilter, secondFilter);
    }

}
