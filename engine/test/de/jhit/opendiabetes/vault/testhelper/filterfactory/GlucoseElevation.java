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
import de.jhit.opendiabetes.vault.processing.filter.AndFilter;
import de.jhit.opendiabetes.vault.processing.filter.CombinationFilter;
import de.jhit.opendiabetes.vault.processing.filter.VaultEntryTypeFilter;
import de.jhit.opendiabetes.vault.processing.filter.Filter;
import de.jhit.opendiabetes.vault.processing.filter.ThresholdFilter;
import de.jhit.opendiabetes.vault.processing.filter.TimePointFilter;
import de.jhit.opendiabetes.vault.processing.filter.TypeGroupFilter;
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
                new CombinationFilter(data,
                        new AndFilter(
                                new VaultEntryTypeFilter(VaultEntryType.GLUCOSE_ELEVATION_30),
                                new ThresholdFilter(rise_value, ThresholdFilter.OVER)),
                        new TimePointFilter(LocalTime.MIN, minutes)));
        filters.add(
                new CombinationFilter(data,
                        new VaultEntryTypeFilter(VaultEntryType.GLUCOSE_CGM),
                        new ThresholdFilter(cgm_value, ThresholdFilter.UNDER)));

    }

    @Override
    protected List<Filter> factoryMethod() {

        return filters;
    }
}
