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

import de.jhit.opendiabetes.vault.container.VaultEntryType;
import de.jhit.opendiabetes.vault.processing.filter.AndFilter;
import de.jhit.opendiabetes.vault.processing.filter.Filter;
import de.jhit.opendiabetes.vault.processing.filter.NegateFilter;
import de.jhit.opendiabetes.vault.processing.filter.QueryFilter;
import de.jhit.opendiabetes.vault.processing.filter.ThresholdFilter;
import de.jhit.opendiabetes.vault.processing.filter.TimeClusterFilter;
import de.jhit.opendiabetes.vault.processing.filter.VaultEntryTypeFilter;
import de.jhit.opendiabetes.vault.processing.filter.options.AndFilterOption;
import de.jhit.opendiabetes.vault.processing.filter.options.NegateFilterOption;
import de.jhit.opendiabetes.vault.processing.filter.options.QueryFilterOption;
import de.jhit.opendiabetes.vault.processing.filter.options.ThresholdFilterOption;
import de.jhit.opendiabetes.vault.processing.filter.options.TimeClusterFilterOption;
import de.jhit.opendiabetes.vault.processing.filter.options.VaultEntryTypeFilterOption;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author tiweGH
 */
public class ExcludeTimespanWithCriteria extends FilterFactory {

    List<Filter> filters = new ArrayList<>();

    public ExcludeTimespanWithCriteria(LocalTime startTime, long timespan) {

        List<Filter> innerFilters = new ArrayList<>();
        innerFilters.add(
                new QueryFilter(new QueryFilterOption(
                        new AndFilter(new AndFilterOption(
                                new VaultEntryTypeFilter(new VaultEntryTypeFilterOption(VaultEntryType.STRESS)),
                                new ThresholdFilter(new ThresholdFilterOption(100.0, ThresholdFilter.OVER)))),
                        1, QueryFilter.DONT_CARE))
        );
        innerFilters.add(
                new QueryFilter(new QueryFilterOption(
                        new AndFilter(new AndFilterOption(
                                new VaultEntryTypeFilter(new VaultEntryTypeFilterOption(VaultEntryType.GLUCOSE_CGM)),
                                new ThresholdFilter(new ThresholdFilterOption(120.0, ThresholdFilter.OVER)))),
                        1, QueryFilter.DONT_CARE))
        );
        innerFilters.add(
                new QueryFilter(new QueryFilterOption(
                        new VaultEntryTypeFilter(new VaultEntryTypeFilterOption(VaultEntryType.BOLUS_NORMAL)),
                        QueryFilter.DONT_CARE, 1))
        );
        filters.add(
                new NegateFilter(new NegateFilterOption(
                        new TimeClusterFilter(new TimeClusterFilterOption(
                                innerFilters, startTime, timespan, TimeClusterFilter.DAY - timespan)))));

    }

    @Override
    protected List<Filter> factoryMethod() {

        return filters;
    }
}
