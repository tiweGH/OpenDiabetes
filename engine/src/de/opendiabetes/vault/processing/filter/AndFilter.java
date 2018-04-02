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
package de.opendiabetes.vault.processing.filter;

import de.opendiabetes.vault.container.VaultEntry;
import de.opendiabetes.vault.processing.filter.options.AndFilterOption;
import de.opendiabetes.vault.processing.filter.options.FilterOption;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * All Filters must be True for an entry to get filtered
 *
 * @author tiweGH
 */
public class AndFilter extends Filter {

    private final List<Filter> filters;

    public AndFilter(FilterOption option) {
        super(option);
        if (option instanceof AndFilterOption) {
            this.filters = ((AndFilterOption) option).getFilters();
        } else {
            String msg = "Option has to be an instance of AndFilterOption";
            Logger.getLogger(this.getClass().getName()).log(Level.SEVERE, msg);
            throw new Error(msg);//IllegalArgumentException("Option has to be an instance of CombinationFilterOption");
        }
    }

    @Override
    FilterType getType() {
        return FilterType.AND;
    }

    @Override
    boolean matchesFilterParameters(VaultEntry vaultEntry
    ) {
        boolean result = true;

        for (Filter filter : filters) {
            if (!filter.matchesFilterParameters(vaultEntry)) {
                result = false;
                break;
            }

        }

        return result;
    }

    @Override
    protected List<VaultEntry> setUpBeforeFilter(List<VaultEntry> data
    ) {
        List<VaultEntry> temp = data;
        for (Filter filter : this.filters) {
            temp = filter.setUpBeforeFilter(temp);
        }
        return data;
    }

    @Override
    Filter update(VaultEntry vaultEntry
    ) {
        List<Filter> tempFilters = new ArrayList<>();
        for (Filter filter : this.filters) {
            tempFilters.add(filter.update(vaultEntry));
        }
        AndFilterOption tempOption = new AndFilterOption(tempFilters);
        return new AndFilter(tempOption);
    }
}
