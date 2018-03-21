/*
 * Copyright (C) 2017 mswin
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
package de.opendiabetes.vault.util;

import de.opendiabetes.vault.container.VaultEntry;
import de.opendiabetes.vault.container.VaultEntryType;
import java.util.ArrayList;
import java.util.Date;

/**
 *
 * @author mswin
 */
public class SlidingWindow {

    private ArrayList<VaultEntry> buffer = new ArrayList<>();
    private final long windowSizeInMinutes;
    private final VaultEntryType typeToReactOn;
    private final double outputFilterSize;

    public SlidingWindow(long windowSizeInMinutes, VaultEntryType typeToReactOn, double outputFilterSize) {
        this.windowSizeInMinutes = windowSizeInMinutes;
        this.typeToReactOn = typeToReactOn;
        this.outputFilterSize = outputFilterSize;
    }

    /**
     * Updates the internal buffer and calculates the value 1 elevation
     * (absolut)
     *
     * @param entry
     * @return absolut elevation within the window or 0 if wrong type or buffer
     * not statuated
     */
    public double updateValue1WindowElevation(VaultEntry entry) {
        if (entry.getType() != typeToReactOn) {
            return 0.0;
        }

        buffer.add(entry);

        // clean buffer accourding to window size
        Date bufferStartTime = TimestampUtils.addMinutesToTimestamp(entry.getTimestamp(), -windowSizeInMinutes);
        ArrayList<VaultEntry> itemsToKill = new ArrayList<>();
        for (VaultEntry item : buffer) {
            if (item.getTimestamp().before(bufferStartTime)) {
                // remove items which are to old for the window
                itemsToKill.add(item);
            } else {
                if (itemsToKill.isEmpty()) {
                    // buffer is not satuated --> no calculation
                    return 0.0;
                } else {
                    // reached values in window
                    break;
                }
            }
        }
        buffer.removeAll(itemsToKill);

        // calculate elevation
        double elevation = 0.0;
        double lastValue = 0.0;
        for (VaultEntry item : buffer) {
            if (lastValue == 0.0) {
                // warmup
                lastValue = item.getValue();
            } else {
                // calculate elevation
                elevation += item.getValue() - lastValue;
                lastValue = item.getValue();
            }
        }

        // return with small filtering
        if (Math.abs(elevation) > outputFilterSize) {
            return elevation;
        } else {
            return 0.0;
        }
    }
}
