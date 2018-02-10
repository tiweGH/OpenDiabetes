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
import de.jhit.opendiabetes.vault.util.VaultEntryUtils;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Daniel This Filter is an Filter, which gains multiple Filter. These
 * Filters will be combined by an or. This means,that if an matches
 * Filterparameters method retursn true the vaultEntry will be in the result
 * List.
 */
public class AndFilter extends Filter {

    private final List<Filter> filters;
    private List<VaultEntry> innerResult;

    /**
     * Constructor just set:
     *
     * @param filters; These will used in the matchesFilterParameters Method
     */
    public AndFilter(List<Filter> filters) {
        this.filters = filters;
        this.innerResult = new ArrayList<>();
    }

    @Override
    FilterType getType() {
        return FilterType.FILTER_DECORATOR;
    }

    @Override
    boolean matchesFilterParameters(VaultEntry vaultEntry) {
        return innerResult.contains(vaultEntry);
    }

    @Override
    protected List<VaultEntry> setUpBeforeFilter(List<VaultEntry> data) {
        innerResult = VaultEntryUtils.slice(data, filters).filteredData;
        return data;
    }

    @Override
    Filter update(VaultEntry vaultEntry) {
        return new AndFilter(filters);
    }
}
