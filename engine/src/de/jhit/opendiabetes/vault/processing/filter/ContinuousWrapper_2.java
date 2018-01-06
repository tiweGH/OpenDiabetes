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
public class ContinuousWrapper_2 extends Filter {

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
     * @param baseData Dataset which provides the original entries for the
     * margin applied after other filters where used
     * @param marginBefore margin before each timespamp
     * @param marginAfter margin after each timespamp
     */
    public ContinuousWrapper_2(List<VaultEntry> baseData, int marginBefore, int marginAfter) {
        if (marginBefore < 0 || marginAfter < 0) {
            throw new IllegalArgumentException("Expected a margin >= 0 but was " + marginBefore + " and " + marginAfter);
        }
        this.marginBefore = marginBefore;
        this.marginAfter = marginAfter;
        this.baseData = baseData;
    }

    /**
     * Filter subclass, uses the <code>timeSeries</code> of the previous
     * FilterResult, together with a margin value in minutes, on the initial
     * List of VaultEntrys and returns only entries located in these time spans
     *
     * @param baseData Dataset which provides the original entries for the
     * margin applied after other filters where used
     * @param marginInMinutes time range in minutes applied to the resulting
     * time spans of <code>registeredFilter</code>
     */
    public ContinuousWrapper_2(List<VaultEntry> baseData, int marginInMinutes) {
        this(baseData, marginInMinutes, marginInMinutes);
    }

    @Override
    FilterType getType() {
        return FilterType.EVENT_SPAN_FILTER;
    }

    //maybe put this in TimestampUtils class?
    /**
     * Gets a series of time spans and merges overlapping spans according to the
     * <code>marginInMinutes</code> value set in the constructor
     *
     * @param timeSeries time spans to be merged
     * @param marginBefore margin before each timespamp
     * @param marginAfter margin after each timespamp
     * @return merged time series
     */
    protected List<Pair<Date, Date>> normalizeTimeSeries(List<Pair<Date, Date>> timeSeries, int marginBefore, int marginAfter) {
        List<Pair<Date, Date>> result = new ArrayList<>();
        Date startOfCurentTimeSeries = null;
        Date lastTimeStamp = null;
        Date tempTimeStamp = null;
        for (Pair<Date, Date> p : timeSeries) {
            if (startOfCurentTimeSeries == null) {
                //initial run of the loop
                startOfCurentTimeSeries = TimestampUtils.addMinutesToTimestamp(p.getKey(), -1 * marginBefore);
                lastTimeStamp = TimestampUtils.addMinutesToTimestamp(p.getValue(), marginAfter);
                //for the comparison, adds 1 to lastTimeStamp to include Timestamps starting directly the minute after the last
            } else if (TimestampUtils.withinDateTimeSpan(startOfCurentTimeSeries, TimestampUtils.addMinutesToTimestamp(lastTimeStamp, 1), p.getKey())
                    || TimestampUtils.withinDateTimeSpan(startOfCurentTimeSeries, TimestampUtils.addMinutesToTimestamp(lastTimeStamp, 1), TimestampUtils.addMinutesToTimestamp(p.getKey(), -1 * marginBefore))) {
                //Dates which start within the current time span, or would start within after margin has been applied
                tempTimeStamp = TimestampUtils.addMinutesToTimestamp(p.getValue(), marginAfter);
                if (!(TimestampUtils.withinDateTimeSpan(startOfCurentTimeSeries, lastTimeStamp, tempTimeStamp))) {
                    //the current time span extends to the end of the merged time span
                    lastTimeStamp = tempTimeStamp;
                }
            } else {
                //if no othe time span can be merged to the current span, it will be added to the result and the next span starts
                result.add(new Pair<>(startOfCurentTimeSeries, lastTimeStamp));
                startOfCurentTimeSeries = TimestampUtils.addMinutesToTimestamp(p.getKey(), -1 * marginBefore);
                lastTimeStamp = TimestampUtils.addMinutesToTimestamp(p.getValue(), marginAfter);
            }

//            if (timeSeries.lastIndexOf(p) == timeSeries.size() - 1) {
//                //since, contrary to filter, every element of the list has to be in the result, this ensures that the last time span will be in the result
//                result.add(new Pair<>(startOfCurentTimeSeries, lastTimeStamp));
//            }
        }
        if (timeSeries.size() > 0) {
            result.add(new Pair<>(startOfCurentTimeSeries, lastTimeStamp));
        }
        return result;
    }

    /**
     * Gets a series of time spans and merges overlapping spans according to the
     * <code>marginInMinutes</code> value set in the constructor
     *
     * @param timeSeries time spans to be merged
     * @param margin margin before and after each timespamp
     * @return merged time series
     */
    protected List<Pair<Date, Date>> normalizeTimeSeries(List<Pair<Date, Date>> timeSeries, int margin) {
        return normalizeTimeSeries(timeSeries, margin, margin);
    }

    /**
     * Generates a normalized TimeSeries with entry-Timestamps as input if no
     * pre-processed TimeSeries is aviable
     *
     * @param data List of VaultEntrys, containing Timestamps
     * @param marginBefore margin before each timespamp
     * @param marginAfter margin after each timespamp
     * @return merged time series
     */
    protected List<Pair<Date, Date>> getNormalizedTimeSeries(List<VaultEntry> data, int marginBefore, int marginAfter) {
        List<Pair<Date, Date>> result = new ArrayList<>();
        for (VaultEntry vaultEntry : data) {
            result.add(new Pair<>(vaultEntry.getTimestamp(), vaultEntry.getTimestamp()));
        }
        result = normalizeTimeSeries(result, marginBefore, marginAfter);
        return result;
    }

    /**
     * Generates a normalized TimeSeries with entry-Timestamps as input if no
     * pre-processed TimeSeries is aviable
     *
     * @param data List of VaultEntrys, containing Timestamps
     * @param margin margin before and after each timespamp
     * @return merged time series
     */
    protected List<Pair<Date, Date>> getNormalizedTimeSeries(List<VaultEntry> data, int margin) {
        return getNormalizedTimeSeries(data, margin, margin);
    }

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
    public FilterResult filter(List<VaultEntry> data) {
        FilterResult result;
        timeSpansForContinuousData = getNormalizedTimeSeries(data, marginBefore, marginAfter);
        result = super.filter(baseData);
        return result;
    }
}