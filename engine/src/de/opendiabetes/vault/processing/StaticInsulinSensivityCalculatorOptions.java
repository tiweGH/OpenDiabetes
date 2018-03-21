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
package de.opendiabetes.vault.processing;

import static java.util.concurrent.TimeUnit.MILLISECONDS;
import static java.util.concurrent.TimeUnit.MINUTES;

/**
 *
 * @author juehv
 */
public class StaticInsulinSensivityCalculatorOptions {

    public final long observationRange; // Range for calculation, also for bolus filtering in minutes
    public final long bolusMergingSpan; // bolus merging span
    public final long bolusActingDelay; //time after bolus until cgm should decrese

    public StaticInsulinSensivityCalculatorOptions(long observationRange, long bolusMergingSpan, long bolusActingDelay) {
        this.observationRange = observationRange;
        this.bolusMergingSpan = bolusMergingSpan;
        this.bolusActingDelay = bolusActingDelay;
    }

    public long getBolusMergingSpanInMilliseconds() {
        return MILLISECONDS.convert(bolusMergingSpan, MINUTES);
    }

    public long getBolusActingDelayInMilliseconds() {
        return MILLISECONDS.convert(bolusActingDelay, MINUTES);
    }

}
