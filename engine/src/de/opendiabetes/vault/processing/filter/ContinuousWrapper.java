/*
 * Copyright (C) 2017 tiweGH
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
package de.opendiabetes.vault.processing.filter;

import de.opendiabetes.vault.container.VaultEntry;
import de.opendiabetes.vault.processing.filter.options.ContinuousWrapperOption;
import de.opendiabetes.vault.processing.filter.options.FilterOption;
import de.opendiabetes.vault.util.TimestampUtils;
import java.util.Date;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.util.Pair;

/**
 *
 * @author tiweGH
 */
public class ContinuousWrapper extends Filter {

    List<VaultEntry> baseData;
    protected int marginBefore;
    protected int marginAfter;
    List<Pair<Date, Date>> timeSpansForContinuousData;

    /**
     * Filter subclass, uses the <code>timeSeries</code> of the previous
     * FilterResult, together with a before-margin value and after-margin value
     * in minutes, on the initial List of VaultEntrys and returns only entries
     * located in these time spans
     *
     * @param option
     */
    public ContinuousWrapper(FilterOption option) {

        super(option);
        if (option instanceof ContinuousWrapperOption) {

            this.marginBefore = ((ContinuousWrapperOption) option).getMarginBefore();
            this.marginAfter = ((ContinuousWrapperOption) option).getMarginAfter();
            this.baseData = ((ContinuousWrapperOption) option).getBaseData();
        } else {
            String msg = "Option has to be an instance of VaultEntryTypeFilterOption";
            Logger.getLogger(this.getClass().getName()).log(Level.SEVERE, msg);
            throw new Error(msg);//IllegalArgumentException("Option has to be an instance of CombinationFilterOption");
        }
    }

    @Override
    FilterType getType() {
        return FilterType.COMBINATION_FILTER;
    }

    //maybe put this in TimestampUtils class?
    @Override
    boolean matchesFilterParameters(VaultEntry entry) {
        boolean result = false;
        for (Pair<Date, Date> p : timeSpansForContinuousData) {
            if (TimestampUtils.withinDateTimeSpan(p.getKey(), p.getValue(), entry.getTimestamp())) {
                result = true;
                break;
            }
            if (entry.getTimestamp().before(p.getKey())) {
                //breaks if entry's Timestamp is located before every TimeSpan that will follow now
                break;
            }
        }
        return result;
    }

    @Override
    public List<VaultEntry> setUpBeforeFilter(List<VaultEntry> data) {
        timeSpansForContinuousData = TimestampUtils.getNormalizedTimeSeries(data, marginBefore, marginAfter);
        return baseData;
    }

    @Override
    Filter update(VaultEntry vaultEntry) {
        return new ContinuousWrapper(super.option);
    }
}
