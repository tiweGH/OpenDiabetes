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
import de.opendiabetes.vault.processing.filter.options.CombinationFilterOption;
import de.opendiabetes.vault.processing.filter.options.FilterOption;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

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

    private CombinationFilterOption option;
    private List<Filter> filters;

    /**
     * 
     * @param option CombinationFilterOption contains all options, which will be used later on.
     */
    public CombinationFilter(FilterOption option) {
        super(option);
        if (option instanceof CombinationFilterOption) {
            this.option = (CombinationFilterOption) option;
        } else {
            String msg = "Option has to be an instance of CombinationFilterOption";
            Logger.getLogger(this.getClass().getName()).log(Level.SEVERE, msg);
            throw new Error(msg);
        }
    }

    @Override
    protected List<VaultEntry> setUpBeforeFilter(List<VaultEntry> data) {
        List<VaultEntry> firstResult = option.getFirstFilter().filter(data).filteredData;

        Filter tempFilter;
        // generates an List of Filters from the first found dataset. The secondFilter will be used as Mask.
        filters = new ArrayList<>();
        for (VaultEntry vaultEntry : firstResult) {
            tempFilter = option.getSecondFilter().update(vaultEntry);
            filters.add(tempFilter);
            //this can potentially slow down the process, if the setUp is complex and the dataset is big
            tempFilter.setUpBeforeFilter(firstResult);
        }

        //filters with the basic method
        return option.getDataPointer() != null ? option.getDataPointer().getDataset() : data;
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
    Filter update(VaultEntry vaultEntry) {
        return new CombinationFilter(option);
    }

}
