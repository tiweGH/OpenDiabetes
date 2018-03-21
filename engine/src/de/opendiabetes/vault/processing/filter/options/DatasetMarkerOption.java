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

import de.opendiabetes.vault.processing.filter.DatasetMarker;
import de.opendiabetes.vault.processing.filter.Filter;

/**
 *
 * @author tiweGH
 */
public class DatasetMarkerOption extends FilterOption {

    private final DatasetMarker dataPointer;
    private final Filter firstFilter;
    private final Filter secondFilter;

    public DatasetMarkerOption(DatasetMarker dataPointer, Filter firstFilter, Filter secondFilter) {
        this.dataPointer = dataPointer;
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
