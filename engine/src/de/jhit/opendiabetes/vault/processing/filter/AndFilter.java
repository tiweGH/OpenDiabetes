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
import java.util.List;

public class AndFilter extends Filter {

    private final List<Filter> filters;

    /**
     * Constructor just set:
     *
     * @param filters; These will used in the matchesFilterParameters Method
     */
    public AndFilter(List<Filter> filters) {
        this.filters = filters;
    }

    public AndFilter(Filter firstFilter, Filter secondFilter) {
        this.filters = new ArrayList<>();
        filters.add(firstFilter);
        filters.add(secondFilter);
    }

    @Override
    FilterType getType() {
        return FilterType.AND;
    }

    @Override
    boolean matchesFilterParameters(VaultEntry vaultEntry) {
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
    protected List<VaultEntry> setUpBeforeFilter(List<VaultEntry> data) {
        List<VaultEntry> temp = data;
        for (Filter filter : this.filters) {
            temp = filter.setUpBeforeFilter(temp);
        }
        return data;
    }

    @Override
    Filter update(VaultEntry vaultEntry) {
        List<Filter> tempFilters = new ArrayList<>();
        for (Filter filter : this.filters) {
            tempFilters.add(filter.update(vaultEntry));
        }
        return new AndFilter(tempFilters);
    }
}
