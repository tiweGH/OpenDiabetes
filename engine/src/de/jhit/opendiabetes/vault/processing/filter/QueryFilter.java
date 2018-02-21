/*
 * Copyright (C) 2018 Daniel
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
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javafx.util.Pair;

/**
 * @author tiweGH
 */
public class QueryFilter extends Filter {

//    private DatasetMarker dataPointer;
    private Filter mainFilter;
    private Filter innerFilter;
    private boolean resultValid;
    private int minSize, maxSize;
    private List<VaultEntry> mainFilterResult;

    /**
     * This Filter runs its main filter and then checks if it matches the given
     * criteria, in detail, it filters the result with the inner filter and
     * checks if the inner result matches the given size. If not, the main
     * result won't be returned.
     *
     * @param mainFilter filter which will be checked
     * @param innerFilter used to check the result of the main filter
     * @param minSize minimum size of the result. use "-1" for don't care
     * @param maxSize maximum size of the result. use "-1" for don't care
     */
    public QueryFilter(Filter mainFilter, Filter innerFilter, int minSize, int maxSize) {
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
     * @param minSize minimum size of the result. use "-1" for don't care
     * @param maxSize maximum size of the result. use "-1" for don't care
     */
    public QueryFilter(Filter innerFilter, int minSize, int maxSize) {
        this(new NoneFilter(), innerFilter, minSize, maxSize);
    }

    @Override
    protected List<VaultEntry> setUpBeforeFilter(List<VaultEntry> data) {
        mainFilterResult = mainFilter.filter(data).filteredData;
        List<VaultEntry> innerResult = innerFilter.filter(mainFilterResult).filteredData;

        resultValid = (minSize == -1 || innerResult.size() >= minSize) && (maxSize == -1 || innerResult.size() <= maxSize);

        //filters with the basic method
        //result = (dataPointer != null) ? dataPointer.getDataset() : innerFilterResult;
        return data;
    }

    @Override
    FilterType getType() {
        return FilterType.COMBINATION_FILTER;
    }

    @Override
    boolean matchesFilterParameters(VaultEntry entry) {
        return (resultValid && mainFilterResult.contains(entry));
    }

    @Override
    Filter update(VaultEntry vaultEntry) {
        return new QueryFilter(mainFilter.update(vaultEntry), innerFilter, minSize, maxSize);
    }

}
