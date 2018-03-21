/*
 * Copyright (C) 2018 Daniel
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
import de.opendiabetes.vault.processing.filter.Filter;
import java.text.SimpleDateFormat;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Daniel
 */
public class TimeSpanOverThreshouldCounterFilterFactory extends FilterFactory {

    private Filter overThresholdFilter;
    private Filter counterFilter;
    private Filter timeSpanFilter;

    //Nachmittag mit wert hoch / zählen
    public TimeSpanOverThreshouldCounterFilterFactory(LocalTime startTime, LocalTime endTime, Filter filterForCounter, int hits, VaultEntryType vaultEntryType, double thresholdValue) {
//        timeSpanFilter = new TimeSpanFilter(startTime, endTime);
//        counterFilter = new CounterFilter(filterForCounter, hits, true);
//        overThresholdFilter = new AndFilter(
//                new VaultEntryTypeFilter(vaultEntryType),
//                new ThresholdFilter(thresholdValue, ThresholdFilter.OVER));

    }

    @Override
    protected List<Filter> factoryMethod() {
        return null;
    }

    public List<VaultEntry> filter(List<VaultEntry> vaultEntries) {
        List<VaultEntry> result = new ArrayList<>();
        List<VaultEntry> tempList = new ArrayList<>();
        tempList = timeSpanFilter.filter(vaultEntries).filteredData;

        List<VaultEntry> dayVaultEntries = new ArrayList<>();

        SimpleDateFormat sdf = new SimpleDateFormat("dd");

        //Tage zusammenführen
//        for (VaultEntry vaultEntry : tempList) {
//            if (dayVaultEntries.size() > 0 && tempList.indexOf(vaultEntry) == tempList.size() - 1 || !sdf.format(vaultEntry.getTimestamp()).equals(sdf.format(dayVaultEntries.get(dayVaultEntries.size() - 1).getTimestamp()))) {
//                if (tempList.indexOf(vaultEntry) == tempList.size() - 1) {
//                    dayVaultEntries.add(vaultEntry);
//                }
//
//                if (counterFilter.filter(dayVaultEntries).size() > 0 && overThresholdFilter.filter(dayVaultEntries).size() > 0) {
//                    result.addAll(dayVaultEntries);
//                }
//
//                dayVaultEntries = new ArrayList<>();
//                dayVaultEntries.add(vaultEntry);
//            } else {
//                dayVaultEntries.add(vaultEntry);
//            }
//        }
        return result;
    }

}
