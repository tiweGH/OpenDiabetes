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

import de.opendiabetes.vault.container.VaultEntry;
import de.opendiabetes.vault.processing.filter.DatasetMarker;
import de.opendiabetes.vault.processing.filter.Filter;
import de.opendiabetes.vault.processing.filter.NoneFilter;
import java.util.List;

/**
 *
 * @author tiweGH
 */
public class CombinationFilterOption extends FilterOption {

    private final DatasetMarker dataPointer;
    private final Filter firstFilter;
    private final Filter secondFilter;

    /**
     * The Constructor will set the Filters and basic data, which will be used
     * later in the filter method.
     *
     * @param data original input entry data
     * @param firstFilter; first Filter from the data in the filter method
     * @param secondFilter; Filter mask for the list of Filters in the filter
     * method
     */
    public CombinationFilterOption(List<VaultEntry> data, Filter firstFilter, Filter secondFilter) {
        this(new DatasetMarker(), firstFilter, secondFilter);
    }

    /**
     *
     * @param data original input entry data
     * @param combinatedFilter Filter mask for the previous result
     */
    public CombinationFilterOption(List<VaultEntry> data, Filter combinatedFilter) {
        this(data, new NoneFilter(), combinatedFilter);
    }

    /**
     * The Constructor will set the Filters and basic data, which will be used
     * later in the filter method.
     *
     * @param dataPointer pointer to the dataset to work on
     * @param firstFilter first Filter from the data in the filter method
     * @param secondFilter Filter mask for the list of Filters in the filter
     */
    public CombinationFilterOption(DatasetMarker dataPointer, Filter firstFilter, Filter secondFilter) {
        this.dataPointer = dataPointer;
        this.firstFilter = firstFilter;
        this.secondFilter = secondFilter;
    }

    /**
     * Doesn't work on the original dataset but on the previous result.
     *
     * @param firstFilter first Filter from the data in the filter method
     * @param secondFilter Filter mask for the list of Filters in the filter
     */
    public CombinationFilterOption(Filter firstFilter, Filter secondFilter) {
        this.dataPointer = null;
        this.firstFilter = firstFilter;
        this.secondFilter = secondFilter;
    }

    public DatasetMarker getDataPointer() {
        return dataPointer;
    }

    public Filter getFirstFilter() {
        return firstFilter;
    }

    public Filter getSecondFilter() {
        return secondFilter;
    }

}
