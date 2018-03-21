/*
 * Copyright (C) 2018 tiweGH
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
package de.opendiabetes.vault.processing.filter.options;

import de.opendiabetes.vault.processing.filter.Filter;

/**
 *
 * @author tiweGH
 */
public class CounterFilterOption extends FilterOption {

    private final Filter filter;
    private final int hitCounter;
    private final boolean onlyOneResult;

    /**
     * Standard Constructor
     *
     * @param filter: Filter, which should be Counted
     * @param hitCounter: After how many Hits
     * @param onlyOneResult: When true only one result will returned. (first
     * appearence)
     */
    public CounterFilterOption(Filter filter, int hitCounter, boolean onlyOneResult) {
        this.filter = filter;
        this.hitCounter = hitCounter;
        this.onlyOneResult = onlyOneResult;
    }

    public Filter getFilter() {
        return filter;
    }

    public int getHitCounter() {
        return hitCounter;
    }

    public boolean isOnlyOneResult() {
        return onlyOneResult;
    }

}
