/*
 * Copyright (C) 2017 juehv
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
package de.opendiabetes.vault.processing.filter;

import de.opendiabetes.vault.container.VaultEntry;
import java.util.ArrayList;
import java.util.List;

/**
 * Filters Nothing. Used as a pointer to a specific state of the VaultEntry
 * Dataset given via filter. Use instances of this class in other Filters, which
 * need to have access to specific previous states.
 *
 * @author tiweGH
 */
public class DatasetMarker extends Filter {

    private List<VaultEntry> basedata;

    /**
     * Filters Nothing. Used as a pointer to a specific state of the VaultEntry
     * Dataset given via filter. Use instances of this class in other Filters,
     * which need to have access to specific previous states.
     *
     */
    public DatasetMarker() {
        super(null);
        basedata = new ArrayList<>();
    }

    @Override
    public FilterType getType() {
        return FilterType.MARKER;
    }

    @Override
    boolean matchesFilterParameters(VaultEntry entry) {
        return true;
    }

    @Override
    protected List<VaultEntry> setUpBeforeFilter(List<VaultEntry> data) {
        this.basedata = data;
        return data;
    }

    @Override
    Filter update(VaultEntry vaultEntry) {
        return new DatasetMarker();
    }

    /**
     * Returns the state of the input VaultEntry dataset, depending on where in
     * the filter-chain this instance is located
     *
     * @return a previous state of the entry dataset.
     */
    public List<VaultEntry> getDataset() {
        return basedata;
    }

}
