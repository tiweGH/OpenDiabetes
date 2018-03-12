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
package de.jhit.opendiabetes.vault.processing.filter.options;

import de.jhit.opendiabetes.vault.processing.filter.DatasetMarker;
import de.jhit.opendiabetes.vault.processing.filter.Filter;
import static de.jhit.opendiabetes.vault.processing.filter.ThresholdFilter.OVER;
import static de.jhit.opendiabetes.vault.processing.filter.ThresholdFilter.UNDER;

/**
 *
 * @author tiweGH
 */
public class ThresholdFilterOption extends FilterOption {

    double minThreshold = 0;
    double maxThreshold = 0;
    private int option = 0;
    
    public static final int OVER = 0;
    public static final int UNDER = 1;
    public static final int BANDPASS = 2;
    
    public ThresholdFilterOption(double minThreshold, double maxThreshold, double option) {
        this.minThreshold = minThreshold;
        this.maxThreshold = maxThreshold;
        this.option = this.option;
    }
    
    public ThresholdFilterOption(double thresholdValue, int option) {
        this.option = option;

        if (option == OVER) {
            this.minThreshold = thresholdValue;
        } else if (option == UNDER) {
            this.maxThreshold = thresholdValue;
        }
    }

    public double getMinThresholdValue() {
        return minThreshold;
    }

    public double getMaxThresholdValue() {
        return maxThreshold;
    }

    public int getOption() {
        return option;
    }

}
