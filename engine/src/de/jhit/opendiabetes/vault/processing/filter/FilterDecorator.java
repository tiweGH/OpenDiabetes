/*
 * Copyright (C) 2017 Daniel
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
public class FilterDecorator extends Filter{

    private final List<Filter> filters;
    private int currentFilterPosition = 0;

    public FilterDecorator(List<Filter> filters) {
        this.filters = filters;
    }
        
    @Override
    FilterType getType() {
        return FilterType.FILTER_DECORATOR;
    }

    @Override
    boolean matchesFilterParameters(VaultEntry entry) {
        return filters.get(currentFilterPosition).matchesFilterParameters(entry);
    }
    
    
    /**
     * This methods override the normal Filtermethod. This Filter combines multiple Filters and adds an Or to the given Filters.
     */
    @Override
    public FilterResult filter(List<VaultEntry> data) {

        List<VaultEntry> result = new ArrayList<>();
        List<Pair<Date, Date>> timeSeries = new ArrayList<>();
        FilterResult filterResult = new FilterResult(result, timeSeries);
        FilterResult filterTmpResult = new FilterResult(result, timeSeries);
        
        for (Filter filter : filters) {
            if(currentFilterPosition == 0)
                filterResult = filter.filter(data);
            else
            {
                filterTmpResult = filter.filter(data);
                filterResult.filteredData.addAll(filterTmpResult.filteredData);
                filterResult.timeSeries.addAll(filterTmpResult.timeSeries);
            }
            
            currentFilterPosition++;
            
        }
        
        return filterResult;
    }
    
}
