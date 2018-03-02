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
import de.jhit.opendiabetes.vault.util.TimestampUtils;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import static java.util.concurrent.TimeUnit.MILLISECONDS;
import static java.util.concurrent.TimeUnit.MINUTES;
import javafx.util.Pair;

/**
 *
 * @author tiweGH
 */
public class ContinuousWrapper extends Filter {

    List<Filter> registeredFilter;
    protected int marginBefore;
    protected int marginAfter;
    List<Pair<Date, Date>> timeSpansForContinuousData;

    //folgendes problem: die Filter werden dem Slicer scheinbar fertig übergeben,
    //dh der kann, da er nur filter() ausführt, die ursprüngliche EntryListe nicht übergeben
    //außer er wird als erstes  ausgeführt
    /**
     * Filter subclass, gets a filter and uses the <code>timeSeries</code> of
     * the internal FilterResult, together with a before-margin value and
     * after-margin value in minutes, on the initial List of VaultEntrys and
     * returns only entries located in these time spans
     *
     * @param registeredFilter Filter(s) which provide the results for the time
     * spans
     * @param marginBefore margin before each timespamp
     * @param marginAfter margin after each timespamp
     */
    public ContinuousWrapper(List<Filter> registeredFilter, int marginBefore, int marginAfter) {
        this.registeredFilter = registeredFilter;
        if (marginBefore < 0 || marginAfter < 0) {
            throw new IllegalArgumentException("Expected a margin >= 0 but was " + marginBefore + " " + marginAfter);
        }
        this.marginBefore = marginBefore;
        this.marginAfter = marginAfter;
    }

    /**
     * Filter subclass, gets a List of Filters and runs them similar to
     * <code>DataSlicer</code>, uses the <code>timeSeries</code> of the last
     * internal FilterResult, together with a margin value in minutes, on the
     * initial List of VaultEntrys and returns only entries located in these
     * time spans
     *
     * @param registeredFilter Filter(s) which provide the results for the time
     * spans
     * @param marginInMinutes time range in minutes applied to the resulting
     * time spans of <code>registeredFilter</code>
     */
    public ContinuousWrapper(List<Filter> registeredFilter, int marginInMinutes) {
        this(registeredFilter, marginInMinutes, marginInMinutes);
    }

    /**
     * Filter subclass, gets a filter and uses the <code>timeSeries</code> of
     * the internal FilterResult, together with a margin value in minutes, on
     * the initial List of VaultEntrys and returns only entries located in these
     * time spans
     *
     * @param filter Filter which provides the results for the time spans
     * @param marginInMinutes time range in minutes applied to the resulting
     * time spans of <code>registeredFilter</code>
     */
    public ContinuousWrapper(Filter filter, int marginInMinutes) {
        List<Filter> registeredFilter = new ArrayList<>();
        registeredFilter.add(filter);
        this.registeredFilter = registeredFilter;
        if (marginInMinutes < 0) {
            throw new IllegalArgumentException("Expected a margin >= 0 but was " + marginInMinutes);
        }
        this.marginBefore = this.marginAfter = marginInMinutes;
    }

    @Override
    FilterType getType() {
        return FilterType.EVENT_SPAN_FILTER;
    }

    @Override
    boolean matchesFilterParameters(VaultEntry entry) {
        boolean result = false;
        for (Pair<Date, Date> p : timeSpansForContinuousData) {
            if (TimestampUtils.withinDateTimeSpan(p.getKey(), p.getValue(), entry.getTimestamp())) {
                result = true;
                break;
            }
        }
        return result;
    }

    @Override
    public List<VaultEntry> setUpBeforeFilter(List<VaultEntry> data) {
        FilterResult result;
        FilterResult tempResult = null;
        //works similar to Slicer
        //TODO there has to be a better way than copying the Slicer
        for (Filter filter : registeredFilter) {
            if (tempResult == null) {
                tempResult = filter.filter(data);
            } else {
                tempResult = filter.filter(tempResult.filteredData);
            }
        }
        timeSpansForContinuousData = TimestampUtils.normalizeTimeSeries(tempResult.timeSeries, marginBefore, marginAfter);
        // timeSpansForContinuousData = TimestampUtils.getNormalizedTimeSeries(tempResult.filteredData, marginBefore, marginAfter);
        return data;
    }

    @Override
    Filter update(VaultEntry vaultEntry) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
}
