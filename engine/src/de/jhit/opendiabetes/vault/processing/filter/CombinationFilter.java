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
 *
 * @author Daniel
 */
public class CombinationFilter extends Filter {

    private List<VaultEntry> basicData;
    private Filter firstFilter;
    private Filter secondFilter;
    private List<Filter> filters;

    public CombinationFilter(List<VaultEntry> data, Filter firstFilter, Filter secondFilter) {
        this.basicData = data;
        this.firstFilter = firstFilter;
        this.secondFilter = secondFilter;
    }

    @Override
    public FilterResult filter(List<VaultEntry> data) {
        FilterResult result = firstFilter.filter(data);

        filters = new ArrayList<>();
        for (VaultEntry vaultEntry : result.filteredData) {
            filters.add(secondFilter.update(vaultEntry));
        }
        
        result = super.filter(basicData);
        
        return result;
    }

    @Override
    FilterType getType() {
        return FilterType.COMBINATION_FILTER;
    }

    @Override
    boolean matchesFilterParameters(VaultEntry entry) {
        boolean result = false;
        
        for (Filter filter : filters) {
            if(filter.matchesFilterParameters(entry))
                result= true;
        }
        
        return result;
    }

    @Override
    Filter update(VaultEntry vaultEntry) {
        return new CombinationFilter(basicData, firstFilter, secondFilter);
    }

}
