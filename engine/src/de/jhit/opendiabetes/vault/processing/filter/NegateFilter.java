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
package de.jhit.opendiabetes.vault.processing.filter;

import de.jhit.opendiabetes.vault.container.VaultEntry;
import java.util.List;

/**
 *
 * @author tiweGH
 */
public class NegateFilter extends Filter {

    private Filter filter;

    /**
     * Negates basic filters due negating matchesFilterParameters output.
     *
     * @param filter Filter, which will be negated
     */
    public NegateFilter(Filter filter) {
        this.filter = filter;
    }

    @Override
    FilterType getType() {
        return filter.getType();
    }

    @Override
    boolean matchesFilterParameters(VaultEntry entry) {
        return !filter.matchesFilterParameters(entry);
    }

    @Override
    Filter update(VaultEntry vaultEntry) {
        return new NegateFilter(filter.update(vaultEntry));
    }

    @Override
    protected List<VaultEntry> setUpBeforeFilter(List<VaultEntry> data) {
        return filter.setUpBeforeFilter(data);
    }

    @Override
    protected FilterResult tearDownAfterFilter(FilterResult givenResult) {
        return filter.tearDownAfterFilter(givenResult);
    }

}
