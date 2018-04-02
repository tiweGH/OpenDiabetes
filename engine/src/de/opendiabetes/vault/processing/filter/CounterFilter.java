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
import de.opendiabetes.vault.processing.filter.options.CounterFilterOption;
import de.opendiabetes.vault.processing.filter.options.FilterOption;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * This class extends filter and checks if the given Filter hits an specific
 * amount, after a result the hitcounter will be reset.
 *
 * @author Daniel
 */
public class CounterFilter extends Filter {

    private Filter filter;
    private int currentHit;
    private int hitCounter;
    private final boolean onlyOneResult;

    public CounterFilter(FilterOption option) {
        super(option);
        if (option instanceof CounterFilterOption) {
            this.filter = ((CounterFilterOption) option).getFilter();
            this.hitCounter = ((CounterFilterOption) option).getHitCounter();
            currentHit = 0;
            this.onlyOneResult = ((CounterFilterOption) option).isOnlyOneResult();
        } else {
            String msg = "Option has to be an instance of CounterFilterOption";
            Logger.getLogger(this.getClass().getName()).log(Level.SEVERE, msg);
            throw new Error(msg);//IllegalArgumentException("Option has to be an instance of CombinationFilterOption");
        }
    }

    @Override
    FilterType getType() {
        return FilterType.COUNTER;
    }

    @Override
    boolean matchesFilterParameters(VaultEntry entry) {
        boolean result = false;
        if (filter.matchesFilterParameters(entry)) {
            currentHit++;
            if (currentHit == hitCounter) {
                result = true;
                if (!onlyOneResult) {
                    currentHit = 0;
                }
            }
        }
        return result;
    }

    @Override
    Filter update(VaultEntry vaultEntry) {
        CounterFilterOption tempOption = new CounterFilterOption(filter, hitCounter, onlyOneResult);
        return new CounterFilter(tempOption);
    }
}
