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
package de.opendiabetes.vault.processing;

import de.opendiabetes.vault.container.VaultEntry;
import de.opendiabetes.vault.processing.filter.Filter;
import de.opendiabetes.vault.processing.filter.FilterResult;
import de.opendiabetes.vault.processing.preprocessing.Preprocessor;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

/**
 * Slices dataset with respect to registered filters. And preprocess the data if wished.
 *
 * @author juehv, tiweGH
 */
public class VaultEntrySlicer {

    private final List<Filter> registeredFilter = new ArrayList<>();
    private final List<Preprocessor> preprocessors = new ArrayList<>();

    private Preprocessor gapRemover;
    private List<Preprocessor> clusterPreprocessors = new ArrayList<>();
    private List<Preprocessor> queries = new ArrayList<>();

    private static final Logger LOG = Logger.getLogger(VaultEntrySlicer.class.getName());

    public VaultEntrySlicer() {
    }

    /**
     * Slices dataset with respect to registered filters. At the beginning the 
     * data will be preprocessed, if no Preprocessors is register the data will not change.
     * After that all Filters will be used after each other.
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

    /**
     * Registeres a PreProcess for slicing. Should be called before slicing.
     * Registered PreProcess are always combined as logical AND.
     *
     * @param preprocess
     */
    public void registerPreProcess(Preprocessor preprocess) {
        preprocessors.add(preprocess);
    }

    /**
     * Registeres a PreProcess for slicing. Should be called before slicing.
     * Registered PreProcess are always combined as logical AND.
     *
     * @param preprocessors
     */
    public void registerPreProcess(List<Preprocessor> preprocessors) {
        this.preprocessors.addAll(preprocessors);
    }

    /**
     * Preprocessing for slicing. Prerocessing calls different Methods, which
     * will be set specific sst methods.
     *
     * @param data
     * @return
     */
    public List<VaultEntry> preprocessing(List<VaultEntry> data) {
        List<VaultEntry> result = data;

        for (Preprocessor preprocessor : preprocessors) {
            result = preprocessor.preprocess(result);
        }

        return result;
    }

}
