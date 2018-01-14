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
package de.jhit.opendiabetes.vault.processing.filter;

import de.jhit.opendiabetes.vault.container.VaultEntry;
import de.jhit.opendiabetes.vault.container.VaultEntryType;
import de.jhit.opendiabetes.vault.util.TimestampUtils;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import static java.util.concurrent.TimeUnit.MILLISECONDS;
import static java.util.concurrent.TimeUnit.MINUTES;
import javafx.util.Pair;

/**
 *
 * @author tiweGH
 */
public class ContinuousWrapper_3 extends Filter {

    static List<VaultEntry> baseData;
    protected int marginBefore;
    protected int marginAfter;
    List<Pair<Date, Date>> timeSpansForContinuousData;

    /**
     * Filter subclass, uses the <code>timeSeries</code> of the previous
     * FilterResult, together with a before-margin value and after-margin value
     * in minutes, on the initial List of VaultEntrys and returns only entries
     * located in these time spans
     *
     * @param marginBefore margin before each timespamp
     * @param marginAfter margin after each timespamp
     */
    public ContinuousWrapper_3(int marginBefore, int marginAfter) {
        if (marginBefore < 0 || marginAfter < 0) {
            throw new IllegalArgumentException("Expected a margin >= 0 but was " + marginBefore + " and " + marginAfter);
        }
        this.marginBefore = marginBefore;
        this.marginAfter = marginAfter;
    }

    /**
     * Filter subclass, uses the <code>timeSeries</code> of the previous
     * FilterResult, together with a margin value in minutes, on the initial
     * List of VaultEntrys and returns only entries located in these time spans
     *
     * @param marginInMinutes time range in minutes applied to the resulting
     * time spans of <code>registeredFilter</code>
     */
    public ContinuousWrapper_3(int marginInMinutes) {
        this(marginInMinutes, marginInMinutes);
    }

    @Override
    FilterType getType() {
        return FilterType.EVENT_SPAN_FILTER;
    }

    @Override
    boolean matchesFilterParameters(VaultEntry entry) {
        boolean result = false;
        if (baseData == null) {
            result = true;
        } else {
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
        }
        return result;
    }

    @Override
    public FilterResult filter(List<VaultEntry> data) {
        FilterResult result;
        if (baseData == null) {
            NoneFilter noneFilter = new NoneFilter();
            result = noneFilter.filter(data);
            baseData = data;
        } else {

            timeSpansForContinuousData = TimestampUtils.getNormalizedTimeSeries(data, marginBefore, marginAfter);
            result = super.filter(data);
            baseData = null;
        }
        return result;
    }

    @Override
    Filter update(VaultEntry vaultEntry) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
}
