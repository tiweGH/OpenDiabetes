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
package de.jhit.opendiabetes.vault.testhelper.filterfactory;

import de.jhit.opendiabetes.vault.container.VaultEntry;
import de.jhit.opendiabetes.vault.container.VaultEntryType;
import de.jhit.opendiabetes.vault.container.VaultEntryTypeGroup;
import de.jhit.opendiabetes.vault.processing.filter.CounterFilter;
import de.jhit.opendiabetes.vault.processing.filter.Filter;
import de.jhit.opendiabetes.vault.processing.filter.LogicFilter;
import de.jhit.opendiabetes.vault.processing.filter.PositionFilter;
import de.jhit.opendiabetes.vault.processing.filter.TypeGroupFilter;
import de.jhit.opendiabetes.vault.processing.filter.VaultEntryTypeFilter;
import de.jhit.opendiabetes.vault.processing.filter.options.CounterFilterOption;
import de.jhit.opendiabetes.vault.processing.filter.options.LogicFilterOption;
import de.jhit.opendiabetes.vault.processing.filter.options.PositionFilterOption;
import de.jhit.opendiabetes.vault.processing.filter.options.TypeGroupFilterOption;
import de.jhit.opendiabetes.vault.processing.filter.options.VaultEntryTypeFilterOption;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author tiweGH
 */
public class TypeAfterNthEventAfterEvent extends FilterFactory {

    List<Filter> filters = new ArrayList<>();

    public TypeAfterNthEventAfterEvent(List<VaultEntry> data) {
        List<Filter> innerFilters = new ArrayList<>();

        innerFilters.add(
                new PositionFilter(new PositionFilterOption(
                        new TypeGroupFilter(new TypeGroupFilterOption(
                                VaultEntryTypeGroup.SLEEP)),
                        PositionFilter.LAST)));
        innerFilters.add(
                new CounterFilter(new CounterFilterOption(
                        new VaultEntryTypeFilter(new VaultEntryTypeFilterOption(VaultEntryType.HEART_RATE)),
                        2, false)));
        innerFilters.add(new VaultEntryTypeFilter(new VaultEntryTypeFilterOption(VaultEntryType.STRESS)));
        filters.add(new LogicFilter(new LogicFilterOption(innerFilters, true)));

    }

    @Override
    protected List<Filter> factoryMethod() {

        return filters;
    }
}
