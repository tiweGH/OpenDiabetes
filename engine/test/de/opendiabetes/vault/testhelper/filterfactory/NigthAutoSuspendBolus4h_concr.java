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
import de.opendiabetes.vault.container.VaultEntryTypeGroup;
import de.opendiabetes.vault.processing.filter.CombinationFilter;
import de.opendiabetes.vault.processing.filter.DateTimePointFilter;
import de.opendiabetes.vault.processing.filter.Filter;
import de.opendiabetes.vault.processing.filter.TimeSpanFilter;
import de.opendiabetes.vault.processing.filter.TypeGroupFilter;
import de.opendiabetes.vault.processing.filter.VaultEntryTypeFilter;
import de.opendiabetes.vault.processing.filter.options.CombinationFilterOption;
import de.opendiabetes.vault.processing.filter.options.DateTimePointFilterOption;
import de.opendiabetes.vault.processing.filter.options.TimeSpanFilterOption;
import de.opendiabetes.vault.processing.filter.options.TypeGroupFilterOption;
import de.opendiabetes.vault.processing.filter.options.VaultEntryTypeFilterOption;
import java.time.Instant;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 *
 * @author tiweGH
 */
public class NigthAutoSuspendBolus4h_concr extends FilterFactory {
//... nachts einen Autonomous Suspend haben bei dem innerhalb 4h vorher ein Bolus Event war.

    List<Filter> filters = new ArrayList<>();
    LocalTime nightStart = LocalTime.of(21, 0);
    LocalTime nightEnd = LocalTime.of(6, 0);

    public NigthAutoSuspendBolus4h_concr(List<VaultEntry> data) {
        filters.add(
                new CombinationFilter(new CombinationFilterOption(
                        data,
                        new TypeGroupFilter(new TypeGroupFilterOption(VaultEntryTypeGroup.BOLUS)),
                        new DateTimePointFilter(new DateTimePointFilterOption(
                                Date.from(Instant.MIN), 0, 4 * 60)))));
        filters.add(new TimeSpanFilter(new TimeSpanFilterOption(nightStart, nightEnd)));
        filters.add(new VaultEntryTypeFilter(new VaultEntryTypeFilterOption(VaultEntryType.PUMP_AUTONOMOUS_SUSPEND)));
    }

    @Override
    protected List<Filter> factoryMethod() {

        return filters;
    }
}
