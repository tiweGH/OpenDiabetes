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

package de.opendiabetes.vault.plugin.util;

import de.opendiabetes.vault.container.VaultEntry;
import de.opendiabetes.vault.container.VaultEntryType;
import java.util.ArrayList;
import java.util.Date;

/**
 * This class implement a sliding windows to filter VaultEntries from a time period.
 */
public class SlidingWindow {

    /**
     * The window size.
     */
    private final long windowSizeInMinutes;
    /**
     * Type to filter for.
     */
    private final VaultEntryType typeToReactOn;
    /**
     * Size of the output filter.
     */
    private final double outputFilterSize;
    /**
     * Buffer for VaultEntries.
     */
    private ArrayList<VaultEntry> buffer = new ArrayList<>();

    /**
     * Constructor for a sliding window.
     *
     * @param windowSizeInMinutes The window size.
     * @param typeToReactOn       Type to filter for.
     * @param outputFilterSize    Size of the output filter.
     */
    public SlidingWindow(final long windowSizeInMinutes, final VaultEntryType typeToReactOn, final double outputFilterSize) {
        this.windowSizeInMinutes = windowSizeInMinutes;
        this.typeToReactOn = typeToReactOn;
        this.outputFilterSize = outputFilterSize;
    }

    /**
     * Updates the internal buffer and calculates the value 1 elevation (absolute).
     *
     * @param entry The VaultEntry to update.
     * @return Absolute elevation within the window or 0 if wrong type or buffer
     * not statuated.
     */
    public double updateValue1WindowElevation(final VaultEntry entry) {
        if (entry.getType() != typeToReactOn) {
            return 0.0;
        }

        buffer.add(entry);

        // Clean buffer according to window size
        Date bufferStartTime = TimestampUtils.addMinutesToTimestamp(entry.getTimestamp(), -windowSizeInMinutes);
        ArrayList<VaultEntry> itemsToKill = new ArrayList<>();
        for (VaultEntry item : buffer) {
            if (item.getTimestamp().before(bufferStartTime)) {
                // Remove items which are to old for the window
                itemsToKill.add(item);
            } else {
                if (itemsToKill.isEmpty()) {
                    // Buffer is not saturated --> no calculation
                    return 0.0;
                } else {
                    // Reached values in window
                    break;
                }
            }
        }
        buffer.removeAll(itemsToKill);

        // Calculate elevation
        double elevation = 0.0;
        double lastValue = 0.0;
        for (VaultEntry item : buffer) {
            if (lastValue == 0.0) {
                // Warm up
                lastValue = item.getValue();
            } else {
                // Calculate elevation
                elevation += item.getValue() - lastValue;
                lastValue = item.getValue();
            }
        }

        // Return with small filtering
        if (Math.abs(elevation) > outputFilterSize) {
            return elevation;
        } else {
            return 0.0;
        }
    }
}
