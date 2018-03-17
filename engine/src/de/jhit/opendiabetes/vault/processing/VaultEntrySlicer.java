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

import de.jhit.opendiabetes.vault.container.VaultEntry;
import de.jhit.opendiabetes.vault.container.VaultEntryType;
import de.jhit.opendiabetes.vault.processing.filter.Filter;
import de.jhit.opendiabetes.vault.processing.filter.FilterResult;
import de.jhit.opendiabetes.vault.processing.preprocessing.ClusterPreprocessor;
import de.jhit.opendiabetes.vault.processing.preprocessing.GapRemover;
import de.jhit.opendiabetes.vault.processing.preprocessing.Preprocessor;
import de.jhit.opendiabetes.vault.processing.preprocessing.QueryPreprocessor;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

/**
 *
 * @author juehv, tiweGH
 */
public class VaultEntrySlicer {

    private final List<Filter> registeredFilter = new ArrayList<>();

    private Preprocessor gapRemover;
    private List<Preprocessor> clusterPreprocessors = new ArrayList<>();
    private List<Preprocessor> queries = new ArrayList<>();

    private static final Logger LOG = Logger.getLogger(VaultEntrySlicer.class.getName());

    public VaultEntrySlicer() {
    }

    /**
     * Slices dataset with respect to registered filters.
     *
     * @param data the data set which should be filtered
     * @return a list of slice entrys matching the registered filters or an
     * empty list if no filter matches
     */
    public FilterResult sliceEntries(List<VaultEntry> data) {

        data = preprocessing(data);

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

    /**
     * Registeres a filter for slicing. Should be called before slicing.
     * Registered filteres are always combined as logical AND.
     *
     * @param filters
     */
    public void registerFilter(List<Filter> filters) {
        registeredFilter.addAll(filters);
    }

    public void setGapRemoving(VaultEntryType removeType, long gapTimeInMinutes) {
        gapRemover = new GapRemover(removeType, gapTimeInMinutes);
    }

//    /**
//     * This method will remove gaps between two timestamps from a given
//     * vaultentrytpye. If there is an vaultentry in the given timerange the new
//     * Vaultentry will be the start for the new gap.
//     *
//     * @param vaultEntries
//     * @return
//     */
//    /**
//     * This Method checks if the given vaultEntry are correct with the given
//     * Querry. If the queery is wong the result will be null. This method will
//     * only works, if the parameters are set correctly in the setQuerying
//     * method.
//     *
//     *
//     * @param data
//     * @return
//     */
    public void setQuerying(List<Filter> queryFilters) {
        queries.add(new QueryPreprocessor(queryFilters));
    }

    public void setQuerying(Filter queryFilter) {
        queries.add(new QueryPreprocessor(queryFilter));
    }

    public void addClustering(long clusterTimeInMinutes, VaultEntryType typeToBeClustered, VaultEntryType clusterType) {
        clusterPreprocessors.add(new ClusterPreprocessor(clusterTimeInMinutes, typeToBeClustered, clusterType));
    }

//    /**
//     * This Method add clustered Vaultentry from the setType in the
//     * setClusteringMethod. This Method will only work if the parameters are set
//     * correctly. The clsteredVaultEntry is at the end of the clustered Series.
//     * This method sums up all entries ind the given Timerange and creates a new
//     * Vaultentry.
//     *
//     * @param data
//     * @param clusterTimeInMinutes
//     * @param searchedType
//     * @param clusterType
//     * @return
//     */
    /**
     * Preprocessing for slicing. Prerocessing calls different Methods, which
     * will be set specific sst methods.
     *
     * @param data
     * @return
     */
    public List<VaultEntry> preprocessing(List<VaultEntry> data) {
        List<VaultEntry> result = data;

        if (gapRemover != null) {
            result = gapRemover.preprocess(result);
        }
        for (Preprocessor preprocessor : queries) {
            result = preprocessor.preprocess(result);
        }
        for (Preprocessor clusterPreprocessor : clusterPreprocessors) {
            result = clusterPreprocessor.preprocess(result);
        }
        return result;
    }

}
