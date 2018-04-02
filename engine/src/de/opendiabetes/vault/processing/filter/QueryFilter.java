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
import de.opendiabetes.vault.processing.filter.options.QueryFilterOption;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * This Filter runs its main filter and then checks if it matches the given
 * criteria, in detail, it filters the result with the inner filter and checks
 * if the inner result matches the given size. If not, the main result won't be
 * returned.
 *
 * @author tiweGH
 */
public class QueryFilter extends Filter {

//    private DatasetMarker dataPointer;
    private Filter mainFilter;
    private Filter innerFilter;
    private boolean resultValid;
    private int minSize, maxSize;
    private List<VaultEntry> mainFilterResult;

    public final static int DONT_CARE = -1;

    public QueryFilter(FilterOption option) {
        super(option);
        if (option instanceof QueryFilterOption) {
            this.mainFilter = ((QueryFilterOption) option).getMainFilter();
            this.innerFilter = ((QueryFilterOption) option).getInnerFilter();
            this.minSize = ((QueryFilterOption) option).getMinSize();
            this.maxSize = ((QueryFilterOption) option).getMaxSize();
        } else {
            String msg = "Option has to be an instance of QueryFilterOption";
            Logger.getLogger(this.getClass().getName()).log(Level.SEVERE, msg);
            throw new Error(msg);//IllegalArgumentException("Option has to be an instance of CombinationFilterOption");
        }
    }

    @Override
    protected List<VaultEntry> setUpBeforeFilter(List<VaultEntry> data) {
        mainFilterResult = mainFilter.filter(data).filteredData;
        List<VaultEntry> innerResult = innerFilter.filter(mainFilterResult).filteredData;

        resultValid = (minSize == DONT_CARE || innerResult.size() >= minSize)
                && (maxSize == DONT_CARE || innerResult.size() <= maxSize);

        //filters with the basic method
        //result = (dataPointer != null) ? dataPointer.getDataset() : innerFilterResult;
        return data;
    }

    @Override
    FilterType getType() {
        return FilterType.QUERY;
    }

    @Override
    boolean matchesFilterParameters(VaultEntry entry) {
        return (resultValid && mainFilterResult.contains(entry));
    }

    @Override
    Filter update(VaultEntry vaultEntry) {
        return new QueryFilter(new QueryFilterOption(mainFilter.update(vaultEntry), innerFilter, minSize, maxSize));
    }

}
