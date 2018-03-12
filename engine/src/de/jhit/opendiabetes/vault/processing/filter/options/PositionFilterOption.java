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
package de.jhit.opendiabetes.vault.processing.filter.options;

import de.jhit.opendiabetes.vault.processing.filter.Filter;
import de.jhit.opendiabetes.vault.processing.filter.NoneFilter;
import de.jhit.opendiabetes.vault.processing.filter.PositionFilter;

/**
 *
 * @author tiweGH
 */
public class PositionFilterOption extends FilterOption {

    private Filter filter;
    private final int filterMode;

    /**
     * The first entry of the given input data
     */
    public static final int FIRST = PositionFilter.FIRST;
    /**
     * The last entry of the given input data
     */
    public static final int LAST = PositionFilter.LAST;
    /**
     * The entry with the middle index of the given input data
     */
    public static final int MIDDLE = PositionFilter.MIDDLE;
    /**
     * The entry with the timestamp located in the middle between the first and
     * the last entry of the given input data
     */
    public static final int DATE_MIDDLE = PositionFilter.DATE_MIDDLE;

    /**
     * Searches the given dataset for one specific position.
     *
     * @param filter The FilterResult of this Filter will be used as working
     * data for the position search
     * @param filterMode Configures the position search:
     * <p>
     * <code>FIRST</code>: The first entry of the input data<p>
     * <code>LAST</code>: The last entry<p>
     * <code>MIDDLE</code>: The entry with the middle index<p>
     * <code>DATE_MIDDLE</code>: The entry with the timestamp located in the
     * middle between the first and the last entry
     */
    public PositionFilterOption(Filter filter, int filterMode) {
        this.filter = filter;
        this.filterMode = filterMode;
    }

    /**
     * Searches the given dataset for one specific position.
     *
     * @param filterMode Configures the position search:
     * <p>
     * <code>FIRST</code>: The first entry of the input data<p>
     * <code>LAST</code>: The last entry<p>
     * <code>MIDDLE</code>: The entry with the middle index<p>
     * <code>DATE_MIDDLE</code>: The entry with the timestamp located in the
     * middle between the first and the last entry
     */
    public PositionFilterOption(int filterMode) {
        this(new NoneFilter(), filterMode);
    }

    public Filter getFilter() {
        return filter;
    }

    public int getFilterMode() {
        return filterMode;
    }

}
