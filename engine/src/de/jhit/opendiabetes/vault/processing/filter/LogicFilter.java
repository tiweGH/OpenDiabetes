/*
 * Copyright (C) 2017 Jorg
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
import de.jhit.opendiabetes.vault.container.VaultEntryType;
import java.util.List;
import javax.naming.spi.DirStateFactory;

/**
 *
 * @author Daniel This class extends filter and checks if the given Filters are
 * positiv in the given order.
 */
public class LogicFilter extends Filter {

    private List<Filter> filters;
    private int currentFilter;
    private boolean onlyOneResult;

    /**
     * Standard Constructor
     *
     * @param filters; Filters, which are in an specific order for checking
     * @param onlyOneResult; If there should be the first or all results.
     */
    public LogicFilter(List<Filter> filters, boolean onlyOneResult) {
        this.filters = filters;
        currentFilter = 0;
        this.onlyOneResult = onlyOneResult;
    }

    @Override
    FilterType getType() {
        return FilterType.EVENT_FILTER;
    }

    @Override
    boolean matchesFilterParameters(VaultEntry entry) {
        boolean result = false;

        if (filters.size() - 1 >= currentFilter && filters.get(currentFilter).matchesFilterParameters(entry) == true) {

            if (filters.size() - 1 == currentFilter) {
                result = true;

                if (!onlyOneResult) {
                    currentFilter = -1;
                }
            }
            currentFilter++;
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
    Filter update(VaultEntry vaultEntry
    ) {
        return new LogicFilter(filters, onlyOneResult);
    }
}
