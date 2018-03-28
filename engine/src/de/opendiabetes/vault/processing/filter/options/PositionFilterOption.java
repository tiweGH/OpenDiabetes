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

import de.opendiabetes.vault.container.VaultEntryType;
import de.opendiabetes.vault.processing.filter.Filter;
import de.opendiabetes.vault.processing.filter.PositionFilter;

/**
 *
 * @author tiweGH
 */
public class PositionFilterOption extends FilterOption {

    private Filter filter;
    private final int filterMode;
    private VaultEntryType weightedType;

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
     * The entry with the timestamp located nearest to the calculated weighted
     * middle of a given type.
     */
    public static final int WEIGHTED_MIDDLE = PositionFilter.WEIGHTED_MIDDLE;

    /**
     * Searches the given dataset for one specific position.
     *
     * @param filter The FilterResult of this Filter will be used as working
     * data for the position search. Use a <code>NoneFilter</code> if you don't
     * want to run an extra Filter.<p>
     * @param filterMode Configures the position search:
     * <p>
     * <code>FIRST</code>: The first entry of the input data<br>
     * <code>LAST</code>: The last entry<br>
     * <code>MIDDLE</code>: The entry with the middle index<br>
     * <code>DATE_MIDDLE</code>: The entry with the timestamp located in the
     * middle between the first and the last entry<br>
     * <code>WEIGHTED_MIDDLE</code>: The entry with the timestamp located
     * nearest to the calculated weighted middle of a given type.<br>
     * @param type VaultEntryType only relevant for
     * <code>WEIGHTED_MIDDLE</code>, ignored otherwise
     */
    public PositionFilterOption(Filter filter, int filterMode, VaultEntryType type) {
        this.filter = filter;
        this.filterMode = filterMode;
        this.weightedType = type;
    }

    /**
     * Searches the given dataset for one specific position.
     *
     * @param filter The FilterResult of this Filter will be used as working
     * data for the position search. Use a <code>NoneFilter</code> if you don't
     * want to run an extra Filter.<p>
     * @param filterMode Configures the position search:
     * <p>
     * <code>FIRST</code>: The first entry of the input data<br>
     * <code>LAST</code>: The last entry<br>
     * <code>MIDDLE</code>: The entry with the middle index<br>
     * <code>DATE_MIDDLE</code>: The entry with the timestamp located in the
     * middle between the first and the last entry<br>
     */
    public PositionFilterOption(Filter filter, int filterMode) {
        this(filter, filterMode, null);
    }

    /**
     * Searches the given dataset for the weighted middle.
     *
     * @param filter The FilterResult of this Filter will be used as working
     * data for the position search
     * @param type type for the weighted middle calculation
     */
    public PositionFilterOption(Filter filter, VaultEntryType type) {
        this(filter, WEIGHTED_MIDDLE, type);
    }

    public Filter getFilter() {
        return filter;
    }

    public int getFilterMode() {
        return filterMode;
    }

    public VaultEntryType getWeightedType() {
        return weightedType;
    }

}
