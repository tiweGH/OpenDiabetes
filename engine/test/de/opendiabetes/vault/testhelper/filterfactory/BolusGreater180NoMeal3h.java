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
import de.opendiabetes.vault.processing.filter.AndFilter;
import de.opendiabetes.vault.processing.filter.CombinationFilter;
import de.opendiabetes.vault.processing.filter.DateTimePointFilter;
import de.opendiabetes.vault.processing.filter.Filter;
import de.opendiabetes.vault.processing.filter.NegateFilter;
import de.opendiabetes.vault.processing.filter.ThresholdFilter;
import de.opendiabetes.vault.processing.filter.TypeGroupFilter;
import de.opendiabetes.vault.processing.filter.options.AndFilterOption;
import de.opendiabetes.vault.processing.filter.options.CombinationFilterOption;
import de.opendiabetes.vault.processing.filter.options.DateTimePointFilterOption;
import de.opendiabetes.vault.processing.filter.options.NegateFilterOption;
import de.opendiabetes.vault.processing.filter.options.ThresholdFilterOption;
import de.opendiabetes.vault.processing.filter.options.TypeGroupFilterOption;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Date;
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
                                new DateTimePointFilter(new DateTimePointFilterOption(
                                        Date.from(Instant.MIN), 0, absenceMargin)))))));
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
