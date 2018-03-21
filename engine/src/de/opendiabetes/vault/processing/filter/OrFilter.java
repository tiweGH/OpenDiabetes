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
import de.opendiabetes.vault.processing.filter.options.FilterOption;
import de.opendiabetes.vault.processing.filter.options.OrFilterOption;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Daniel This Filter is an Filter, which gains multiple Filter. These
 * Filters will be combined by an or. This means,that if an matches
 * Filterparameters method retursn true the vaultEntry will be in the result
 * List.
 */
public class OrFilter extends Filter {

    private final List<Filter> filters;

    public OrFilter(FilterOption option) {
        super(option);
        if (option instanceof OrFilterOption) {
            this.filters = ((OrFilterOption) option).getFilters();
        } else {
            String msg = "Option has to be an instance of OrFilterOption";
            Logger.getLogger(this.getClass().getName()).log(Level.SEVERE, msg);
            throw new Error(msg);//IllegalArgumentException("Option has to be an instance of CombinationFilterOption");
        }
    }

    @Override
    FilterType getType() {
        return FilterType.OR;
    }

    @Override
    boolean matchesFilterParameters(VaultEntry vaultEntry) {
        boolean result = false;

        for (Filter filter : filters) {
            if (filter.matchesFilterParameters(vaultEntry)) {
                result = true;
                break;
            }

        }

        return result;
    }

    @Override
    protected List<VaultEntry> setUpBeforeFilter(List<VaultEntry> data) {
        for (Filter filter : filters) {
            filter.setUpBeforeFilter(data);
        }
        return data;
    }

    @Override
    Filter update(VaultEntry vaultEntry) {
        List<Filter> tempFilters = new ArrayList<>();
        for (Filter filter : this.filters) {
            tempFilters.add(filter.update(vaultEntry));
        }
        OrFilterOption tempOption = new OrFilterOption(tempFilters);
        return new OrFilter(tempOption);
    }
}
