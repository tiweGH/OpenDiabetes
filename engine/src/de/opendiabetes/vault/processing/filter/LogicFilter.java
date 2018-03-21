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
package de.opendiabetes.vault.processing.filter;

import de.opendiabetes.vault.container.VaultEntry;
import de.opendiabetes.vault.processing.filter.options.FilterOption;
import de.opendiabetes.vault.processing.filter.options.LogicFilterOption;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Daniel This class extends filter and checks if the given Filters are
 * positiv in the given order. The Filter will be switched to the next, if the
 * first Filter returns true.
 */
public class LogicFilter extends Filter {

    private LogicFilterOption option;

    private int currentFilter;

    public LogicFilter(FilterOption option) {
        super(option);
        if (option instanceof LogicFilterOption) {
            this.option = (LogicFilterOption) option;
            currentFilter = 0;
        } else {
            String msg = "Option has to be an instance of LogicFilterOption";
            Logger.getLogger(this.getClass().getName()).log(Level.SEVERE, msg);
            throw new Error(msg);//IllegalArgumentException("Option has to be an instance of CombinationFilterOption");
        }
    }

    @Override
    FilterType getType() {
        return FilterType.LOGIC;
    }

    @Override
    boolean matchesFilterParameters(VaultEntry entry) {
        boolean result = false;

        if (option.getFilters().size() - 1 >= currentFilter && option.getFilters().get(currentFilter).matchesFilterParameters(entry) == true) {

            if (option.getFilters().size() - 1 == currentFilter) {
                result = true;

                if (!option.isOnlyOneResult()) {
                    currentFilter = -1;
                }
            }
            currentFilter++;
        }

        return result;
    }

    @Override
    protected List<VaultEntry> setUpBeforeFilter(List<VaultEntry> data) {
        for (Filter filter : option.getFilters()) {
            filter.setUpBeforeFilter(data);
        }
        return data;
    }

    @Override
    Filter update(VaultEntry vaultEntry
    ) {
        return new LogicFilter(option);
    }
}
