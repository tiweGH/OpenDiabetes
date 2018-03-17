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
package de.jhit.opendiabetes.vault.processing.preprocessing;

import de.jhit.opendiabetes.vault.container.VaultEntry;
import de.jhit.opendiabetes.vault.processing.filter.Filter;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Daniel
 */
public class QueryPreprocessor extends Preprocessor {

    private final List<Filter> queryFilters;

    /**
     * Checks if the given vaultEntry are correct with the given Querry. If the
     * queery is wong the result will be an empty List. This method will only
     * work, if the parameters are set correctly in the setQuerying method.
     *
     * @param queryFilters
     */
    public QueryPreprocessor(List<Filter> queryFilters) {
        this.queryFilters = queryFilters;
    }

    /**
     * Checks if the given vaultEntry are correct with the given Querry. If the
     * queery is wong the result will be an empty List. This method will only
     * work, if the parameters are set correctly in the setQuerying method.
     *
     * @param filter
     */
    public QueryPreprocessor(Filter filter) {
        this.queryFilters = new ArrayList<>();
        queryFilters.add(filter);
    }

    @Override
    public List<VaultEntry> preprocess(List<VaultEntry> data) {
        List<VaultEntry> result = data;
        if (queryFilters != null && queryFilters.size() > 0) {
            //System.out.println("Preprocessing: Query pre filter");
            for (Filter queryFilter : queryFilters) {
                if (queryFilter.filter(data).size() == 0) {
                    result = new ArrayList<>();
                }
            }
        }
        return result;
    }

}
