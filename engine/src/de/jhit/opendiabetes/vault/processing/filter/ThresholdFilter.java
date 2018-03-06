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

/**
 * This Filter is the superclass for the Thresholdfilter.
 *
 * @author juehv, Daniel, tiweGH
 */
public class ThresholdFilter extends Filter {

    double minThreshold = 0;
    double maxThreshold = 0;
    private int option = 0;

    public static final int OVER = 0;
    public static final int UNDER = 1;
    public static final int BANDPASS = 2;

    public ThresholdFilter(double minThresholdValue, double maxThresholdValue, int option) {
        this.minThreshold = minThresholdValue;
        this.maxThreshold = maxThresholdValue;
        this.option = option;
    }

    public ThresholdFilter(double thresholdValue, int option) {
        this.option = option;

        if (option == OVER) {
            this.minThreshold = thresholdValue;
        } else if (option == UNDER) {
            this.maxThreshold = thresholdValue;
        }
    }

    @Override
    public FilterType getType() {
        return FilterType.THRESHOLD;
    }

    @Override
    boolean matchesFilterParameters(VaultEntry entry) {

        return (option == UNDER && (entry.getValue() < maxThreshold))
                || (option == OVER && (entry.getValue() > minThreshold))
                || (option == BANDPASS && ((entry.getValue() > minThreshold) && (entry.getValue() < maxThreshold)));
    }

    @Override
    Filter update(VaultEntry vaultEntry) {
        return new ThresholdFilter(minThreshold, maxThreshold, option);
    }
}
