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
package de.opendiabetes.vault.processing.filter;

import de.opendiabetes.vault.container.VaultEntry;
import de.opendiabetes.vault.processing.filter.options.FilterOption;
import de.opendiabetes.vault.processing.filter.options.NegateFilterOption;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Negates the matchesFilterParameters() return-value of the given Filter. Use
 * this Filter to run "exclude this"-filter operations.
 *
 * @author tiweGH
 */
public class NegateFilter extends Filter {

    private Filter filter;

    public NegateFilter(FilterOption option) {
        super(option);
        if (option instanceof NegateFilterOption) {
            this.filter = ((NegateFilterOption) option).getFilter();
        } else {
            String msg = "Option has to be an instance of NegateFilterOption";
            Logger.getLogger(this.getClass().getName()).log(Level.SEVERE, msg);
            throw new Error(msg);//IllegalArgumentException("Option has to be an instance of CombinationFilterOption");
        }
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
        return new NegateFilter(new NegateFilterOption(filter.update(vaultEntry)));
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
