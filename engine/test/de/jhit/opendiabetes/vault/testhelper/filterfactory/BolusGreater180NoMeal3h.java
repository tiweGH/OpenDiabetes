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
import de.jhit.opendiabetes.vault.processing.filter.AndFilter;
import de.jhit.opendiabetes.vault.processing.filter.CombinationFilter;
import de.jhit.opendiabetes.vault.processing.filter.Filter;
import de.jhit.opendiabetes.vault.processing.filter.NegateFilter;
import de.jhit.opendiabetes.vault.processing.filter.ThresholdFilter;
import de.jhit.opendiabetes.vault.processing.filter.TimePointFilter;
import de.jhit.opendiabetes.vault.processing.filter.TypeGroupFilter;
import de.jhit.opendiabetes.vault.processing.filter.options.AndFilterOption;
import de.jhit.opendiabetes.vault.processing.filter.options.CombinationFilterOption;
import de.jhit.opendiabetes.vault.processing.filter.options.NegateFilterOption;
import de.jhit.opendiabetes.vault.processing.filter.options.ThresholdFilterOption;
import de.jhit.opendiabetes.vault.processing.filter.options.TimePointFilterOption;
import de.jhit.opendiabetes.vault.processing.filter.options.TypeGroupFilterOption;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author tiweGH
 */
public class BolusGreater180NoMeal3h extends FilterFactory {
//... einen Bolus bei einem CGM Wert > 180 enthalten und 3h davor oder danach keine Meal-Events sind.

    List<Filter> filters = new ArrayList<>();

    public BolusGreater180NoMeal3h(List<VaultEntry> data, VaultEntryTypeGroup group, int absenceMargin, double value) {
        filters.add(
                new NegateFilter(new NegateFilterOption(
                        new CombinationFilter(new CombinationFilterOption(
                                data,
                                new TypeGroupFilter(new TypeGroupFilterOption(group)),
                                new TimePointFilter(new TimePointFilterOption(LocalTime.MIN, absenceMargin)))))));
        filters.add(
                new AndFilter(new AndFilterOption(
                        new TypeGroupFilter(new TypeGroupFilterOption(VaultEntryTypeGroup.BOLUS)),
                        new ThresholdFilter(new ThresholdFilterOption(value, ThresholdFilter.OVER)))));
    }

    @Override
    protected List<Filter> factoryMethod() {

        return filters;
    }
}
