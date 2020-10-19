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
import java.util.List;

/**
 *
 * @author tiweGH
 */
public class LogicFilterOption extends FilterOption {

    private List<Filter> filters;
    private boolean onlyOneResult;

    /**
     * Standard Constructor
     *
     * @param filters; Filters, which are in an specific order for checking
     * @param onlyOneResult; If there should be the first or all results.
     */
    public LogicFilterOption(List<Filter> filters, boolean onlyOneResult) {
        this.filters = filters;
        this.onlyOneResult = onlyOneResult;
    }

    public List<Filter> getFilters() {
        return filters;
    }

    public boolean isOnlyOneResult() {
        return onlyOneResult;
    }

}