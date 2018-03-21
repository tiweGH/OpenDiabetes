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
import de.opendiabetes.vault.processing.filter.NoneFilter;
import de.opendiabetes.vault.processing.filter.QueryFilter;

/**
 *
 * @author tiweGH
 */
public class QueryFilterOption extends FilterOption {

    private Filter mainFilter;
    private Filter innerFilter;
    private int minSize, maxSize;

    public final static int DONT_CARE = QueryFilter.DONT_CARE;

    /**
     * This Filter runs its main filter and then checks if it matches the given
     * criteria, in detail, it filters the result with the inner filter and
     * checks if the inner result matches the given size. If not, the main
     * result won't be returned.
     *
     * @param mainFilter filter which will be checked
     * @param innerFilter used to check the result of the main filter
     * @param minSize minimum size of the result. use "DONT_CARE" for don't care
     * @param maxSize maximum size of the result. use "DONT_CARE" for don't care
     */
    public QueryFilterOption(Filter mainFilter, Filter innerFilter, int minSize, int maxSize) {
        this.mainFilter = mainFilter;
        this.innerFilter = innerFilter;
        this.minSize = minSize;
        this.maxSize = maxSize;
    }

    /**
     * This Filter checks if the previous result matches the given criteria, in
     * detail, it filters the result with the inner filter and checks if the
     * inner result matches the given size. If not, the previous result won't be
     * returned.
     *
     * @param innerFilter used to check the result of the main filter
     * @param minSize minimum size of the result. use "DONT_CARE" for don't care
     * @param maxSize maximum size of the result. use "DONT_CARE" for don't care
     */
    public QueryFilterOption(Filter innerFilter, int minSize, int maxSize) {
        this(new NoneFilter(), innerFilter, minSize, maxSize);
    }

    public Filter getMainFilter() {
        return mainFilter;
    }

    public Filter getInnerFilter() {
        return innerFilter;
    }

    public int getMinSize() {
        return minSize;
    }

    public int getMaxSize() {
        return maxSize;
    }

}
