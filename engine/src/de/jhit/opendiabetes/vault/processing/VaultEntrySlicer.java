/*
 * Copyright (C) 2017 Jens Heuschkel
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
package de.jhit.opendiabetes.vault.processing;

import de.jhit.opendiabetes.vault.container.SliceEntry;
import de.jhit.opendiabetes.vault.container.VaultEntry;
import de.jhit.opendiabetes.vault.processing.filter.Filter;
import de.jhit.opendiabetes.vault.processing.filter.FilterResult;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.logging.Logger;
import javafx.util.Pair;

/**
 *
 * @author juehv
 */
public class VaultEntrySlicer {

    private final List<Filter> registeredFilter = new ArrayList<>();

    public VaultEntrySlicer() {
    }

    /**
     * Slices dataset with respect to registered filters.
     *
     * @param data the data set which should be filtered
     * @return a list of slice entrys matching the registered filters or an
     * empty list if no filter matches
     */
    public FilterResult sliceData(List<VaultEntry> data) {
        FilterResult lastResult = null;

        for (Filter filter : registeredFilter) {
            System.out.println(filter);
            if (lastResult == null) {
                lastResult = filter.filter(data);
            } else {
                lastResult = filter.filter(lastResult.filteredData);
            }
        }

        return lastResult;
    }

    /**
     * Registeres a filter for slicing. Should be called before slicing.
     * Registered filteres are always combined as logical AND.
     *
     * @param filter
     */
    public void registerFilter(Filter filter) {
        registeredFilter.add(filter);
    }

}
