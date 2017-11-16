/*
 * Copyright (C) 2017 juehv
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
package de.jhit.opendiabetes.vault.processing.filter;

import de.jhit.opendiabetes.vault.container.VaultEntry;
import de.jhit.opendiabetes.vault.container.VaultEntryType;
import de.jhit.opendiabetes.vault.util.TimestampUtils;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javafx.util.Pair;

/**
 *
 * @author juehv
 */
public class MealAbsenceFilter implements Filter {

    private final long marginAfterMeal; // minutes after a meal until data becomes interesting again.

    public MealAbsenceFilter(long marginAfterMeal) {
        this.marginAfterMeal = marginAfterMeal;
    }

    @Override
    public FilterResult filter(List<VaultEntry> data) {
        List<VaultEntry> filteredData = new ArrayList<>();
        List<Pair<Date, Date>> timeSeries = new ArrayList<>();

        Date startOfTimeSeries = null;
        Date lastTimeStamp = null;
        Date lastMealFound = null;
        for (VaultEntry entry : data) {
            if (entry.getType() == VaultEntryType.MEAL_BOLUS_CALCULATOR
                    || entry.getType() == VaultEntryType.MEAL_MANUAL) {
                // found a meal, stop time slices
                lastMealFound = entry.getTimestamp();

                if (startOfTimeSeries != null && lastTimeStamp != null) {
                    timeSeries.add(new Pair<>(startOfTimeSeries, lastTimeStamp));
                    startOfTimeSeries = null;
                    lastTimeStamp = null;
                }
            } else if ((lastMealFound != null
                    && !TimestampUtils
                    .addMinutesToTimestamp(lastMealFound, marginAfterMeal)
                    .before(entry.getTimestamp()))
                    || (startOfTimeSeries == null
                    && lastTimeStamp == null
                    && lastMealFound == null)) {
                // cooldown from last found meal over? --> reset stuff and start series
                // or warmup
                lastMealFound = null;
                startOfTimeSeries = entry.getTimestamp();
                lastTimeStamp = entry.getTimestamp();
                filteredData.add(entry);
            } else if (startOfTimeSeries != null) {
                // within a series --> update timestamps
                lastTimeStamp = entry.getTimestamp();
                filteredData.add(entry);
            }
        }

        if (startOfTimeSeries != null) {
            timeSeries.add(new Pair<>(startOfTimeSeries, lastTimeStamp));
        }

        return new FilterResult(filteredData, timeSeries);
    }

    @Override
    public FilterType getType() {
        return FilterType.MEAL_ABSENCE;
    }

}
