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
package de.opendiabetes.vault.testhelper.filterfactory;

import de.opendiabetes.vault.container.VaultEntry;
import de.opendiabetes.vault.container.VaultEntryTypeGroup;
import de.opendiabetes.vault.processing.filter.Filter;
import de.opendiabetes.vault.processing.filter.QueryFilter;
import de.opendiabetes.vault.processing.filter.TimeClusterFilter;
import de.opendiabetes.vault.processing.filter.TypeGroupFilter;
import de.opendiabetes.vault.processing.filter.options.QueryFilterOption;
import de.opendiabetes.vault.processing.filter.options.TimeClusterFilterOption;
import de.opendiabetes.vault.processing.filter.options.TypeGroupFilterOption;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author tiweGH
 */
public class NoDateTimeSpansWithoutGroup extends FilterFactory {
//Entferne Tage an denen kein Sport stattfand

    List<Filter> filters = new ArrayList<>();

    public NoDateTimeSpansWithoutGroup(List<VaultEntry> data) {
        List<Filter> innerfilters = new ArrayList<>();
        innerfilters.add(new QueryFilter(new QueryFilterOption(
                new TypeGroupFilter(new TypeGroupFilterOption(VaultEntryTypeGroup.EXERCISE)),
                1,
                QueryFilterOption.DONT_CARE)));
        filters.add(
                new TimeClusterFilter(new TimeClusterFilterOption(
                        innerfilters,
                        null,
                        24 * 60,
                        0)));
    }

    @Override
    protected List<Filter> factoryMethod() {

        return filters;
    }
}
