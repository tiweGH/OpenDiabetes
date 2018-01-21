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
import de.jhit.opendiabetes.vault.processing.filter.CombinationFilter1;
import de.jhit.opendiabetes.vault.processing.filter.Filter;
import de.jhit.opendiabetes.vault.processing.filter.FilterType;
import de.jhit.opendiabetes.vault.processing.filter.NegateFilter;
import de.jhit.opendiabetes.vault.processing.filter.OverThresholdFilter;
import de.jhit.opendiabetes.vault.processing.filter.TimePointFilter;
import de.jhit.opendiabetes.vault.processing.filter.TypeGroupFilter;
import de.jhit.opendiabetes.vault.processing.filter.UnderThresholdFilter;
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
        filters.add(new NegateFilter(new CombinationFilter1(data, new TypeGroupFilter(group), new TimePointFilter(LocalTime.MIN, absenceMargin))));
        filters.add(new OverThresholdFilter(VaultEntryType.BOLUS_NORMAL, value, FilterType.BOLUS_TH, FilterType.BOLUS_TH));
    }

    @Override
    protected List<Filter> factoryMethod() {

        return filters;
    }
}
