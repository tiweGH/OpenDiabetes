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
import de.opendiabetes.vault.container.VaultEntryType;
import de.opendiabetes.vault.processing.filter.AndFilter;
import de.opendiabetes.vault.processing.filter.CombinationFilter;
import de.opendiabetes.vault.processing.filter.Filter;
import de.opendiabetes.vault.processing.filter.ThresholdFilter;
import de.opendiabetes.vault.processing.filter.TimePointFilter;
import de.opendiabetes.vault.processing.filter.VaultEntryTypeFilter;
import de.opendiabetes.vault.processing.filter.options.AndFilterOption;
import de.opendiabetes.vault.processing.filter.options.CombinationFilterOption;
import de.opendiabetes.vault.processing.filter.options.ThresholdFilterOption;
import de.opendiabetes.vault.processing.filter.options.TimePointFilterOption;
import de.opendiabetes.vault.processing.filter.options.VaultEntryTypeFilterOption;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author tiweGH
 */
public class GlucoseElevation extends FilterFactory {

    List<Filter> filters = new ArrayList<>();

    public GlucoseElevation(List<VaultEntry> data, double cgm_value, double rise_value, int minutes) {
        filters.add(
                new CombinationFilter(new CombinationFilterOption(
                        data,
                        new AndFilter(new AndFilterOption(
                                new VaultEntryTypeFilter(new VaultEntryTypeFilterOption(
                                        VaultEntryType.GLUCOSE_ELEVATION_30)),
                                new ThresholdFilter(new ThresholdFilterOption(
                                        rise_value,
                                        ThresholdFilter.OVER)))),
                        new TimePointFilter(new TimePointFilterOption(
                                LocalTime.MIN, minutes)))));
        filters.add(
                new CombinationFilter(new CombinationFilterOption(
                        data,
                        new VaultEntryTypeFilter(new VaultEntryTypeFilterOption(
                                VaultEntryType.GLUCOSE_CGM)),
                        new ThresholdFilter(new ThresholdFilterOption(
                                cgm_value,
                                ThresholdFilter.UNDER))
                )));

    }

    @Override
    protected List<Filter> factoryMethod() {

        return filters;
    }
}
