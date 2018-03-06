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

/**
 *
 * @author Daniel This class extends filter and checks if the given Filter hits
 * an specific amount, after a result the hitcounter will be reset.
 */
public class CounterFilter extends Filter {

    private Filter filter;
    private int currentHit;
    private int hitCounter;
    private final boolean onlyOneResult;

    /**
     * Standard Constructor
     *
     * @param filter: Filter, which should be Counted
     * @param hitCounter: After how many Hits
     * @param onlyOneResult: When true only one result will returned. (first
     * appearence)
     */
    public CounterFilter(Filter filter, int hitCounter, boolean onlyOneResult) {
        this.filter = filter;
        this.hitCounter = hitCounter;
        currentHit = 0;
        this.onlyOneResult = onlyOneResult;
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
        return new CounterFilter(filter, hitCounter, onlyOneResult);
    }
}
