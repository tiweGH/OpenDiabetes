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
import de.jhit.opendiabetes.vault.processing.filter.DatasetMarker;
import de.jhit.opendiabetes.vault.processing.filter.EventFilter;
import de.jhit.opendiabetes.vault.processing.filter.Filter;
import de.jhit.opendiabetes.vault.processing.filter.FilterType;
import de.jhit.opendiabetes.vault.processing.filter.NegateFilter;
import de.jhit.opendiabetes.vault.processing.filter.OverThresholdFilter;
import de.jhit.opendiabetes.vault.processing.filter.TimePointFilter;
import de.jhit.opendiabetes.vault.processing.filter.TimeSpanFilter;
import de.jhit.opendiabetes.vault.processing.filter.TypeGroupFilter;
import de.jhit.opendiabetes.vault.processing.filter.UnderThresholdFilter;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author tiweGH
 */
public class NigthAutoSuspendBolus4h extends FilterFactory {
//... nachts einen Autonomous Suspend haben bei dem innerhalb 4h vorher ein Bolus Event war.

    List<Filter> filters = new ArrayList<>();

    public NigthAutoSuspendBolus4h(List<VaultEntry> data, VaultEntryType searchedType, VaultEntryTypeGroup groupInMargin, int timeMarginMinutes, LocalTime startTime, LocalTime endTime) {
        filters.add(new TimeSpanFilter(startTime, endTime));
        DatasetMarker pointer = new DatasetMarker(data);
        filters.add(pointer);
        filters.add(new CombinationFilter1(pointer, new TypeGroupFilter(groupInMargin), new TimePointFilter(LocalTime.MIN, timeMarginMinutes)));
        filters.add(new EventFilter(searchedType));
    }

    @Override
    protected List<Filter> factoryMethod() {

        return filters;
    }
}
