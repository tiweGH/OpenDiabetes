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
import de.jhit.opendiabetes.vault.processing.filter.options.FilterOption;
import de.jhit.opendiabetes.vault.processing.filter.options.ThresholdFilterOption;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * This Filter is the superclass for the Thresholdfilter.
 *
 * @author juehv, Daniel, tiweGH
 */
public class ThresholdFilter extends Filter {

    double minThreshold = 0;
    double maxThreshold = 0;
    private int mode = 0;

    public static final int OVER = 0;
    public static final int UNDER = 1;
    public static final int BANDPASS = 2;

    public ThresholdFilter(FilterOption option) {
        super(option);
        if (option instanceof ThresholdFilterOption) {
            this.minThreshold = ((ThresholdFilterOption) option).getMinThresholdValue();
            this.maxThreshold = ((ThresholdFilterOption) option).getMaxThresholdValue();
            this.mode = ((ThresholdFilterOption) option).getMode();

        } else {
            String msg = "Option has to be an instance of ThresholdFilterOption";
            Logger.getLogger(this.getClass().getName()).log(Level.SEVERE, msg);
            throw new Error(msg);//IllegalArgumentException("Option has to be an instance of CombinationFilterOption");
        }
    }

    @Override
    public FilterType getType() {
        return FilterType.THRESHOLD;
    }

    @Override
    boolean matchesFilterParameters(VaultEntry entry) {

        return (mode == UNDER && (entry.getValue() < maxThreshold))
                || (mode == OVER && (entry.getValue() > minThreshold))
                || (mode == BANDPASS && ((entry.getValue() > minThreshold) && (entry.getValue() < maxThreshold)));
    }

    @Override
    Filter update(VaultEntry vaultEntry) {
        return new ThresholdFilter(super.option);
    }
}
