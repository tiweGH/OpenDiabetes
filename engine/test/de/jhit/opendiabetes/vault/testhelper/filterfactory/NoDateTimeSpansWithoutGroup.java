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
import de.jhit.opendiabetes.vault.container.VaultEntryTypeGroup;
import de.jhit.opendiabetes.vault.processing.filter.CombinationFilter;
import de.jhit.opendiabetes.vault.processing.filter.DateTimeSpanFilter;
import de.jhit.opendiabetes.vault.processing.filter.Filter;
import de.jhit.opendiabetes.vault.processing.filter.NoneFilter;
import de.jhit.opendiabetes.vault.processing.filter.QueryFilter;
import de.jhit.opendiabetes.vault.processing.filter.TimeClusterFilter;
import de.jhit.opendiabetes.vault.processing.filter.TypeGroupFilter;
import de.jhit.opendiabetes.vault.processing.filter.options.CombinationFilterOption;
import de.jhit.opendiabetes.vault.processing.filter.options.DateTimeSpanFilterOption;
import de.jhit.opendiabetes.vault.processing.filter.options.QueryFilterOption;
import de.jhit.opendiabetes.vault.processing.filter.options.TimeClusterFilterOption;
import de.jhit.opendiabetes.vault.processing.filter.options.TypeGroupFilterOption;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 *
 * @author tiweGH
 */
public class NoDateTimeSpansWithoutGroup extends FilterFactory {

    List<Filter> filters = new ArrayList<>();

    public NoDateTimeSpansWithoutGroup(List<VaultEntry> data, VaultEntryTypeGroup group, int spanMinutes) {
//        filters.add(
//                new CombinationFilter(new CombinationFilterOption(
//                        data,
//                        new TypeGroupFilter(new TypeGroupFilterOption(group)),
//                        new DateTimeSpanFilter(new DateTimeSpanFilterOption(startTime, endTime)))));
        List<Filter> innerfilters = new ArrayList<>();
        innerfilters.add(new QueryFilter(new QueryFilterOption(
                new TypeGroupFilter(new TypeGroupFilterOption(group)),
                1,
                QueryFilterOption.DONT_CARE)));
        filters.add(
                new TimeClusterFilter(new TimeClusterFilterOption(
                        innerfilters,
                        null,
                        spanMinutes,
                        0)));

    }

    @Override
    protected List<Filter> factoryMethod() {

        return filters;
    }
}
