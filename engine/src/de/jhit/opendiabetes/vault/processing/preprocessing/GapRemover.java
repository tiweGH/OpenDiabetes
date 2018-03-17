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
package de.jhit.opendiabetes.vault.processing.preprocessing;

import de.jhit.opendiabetes.vault.container.VaultEntry;
import de.jhit.opendiabetes.vault.container.VaultEntryType;
import de.jhit.opendiabetes.vault.util.TimestampUtils;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 *
 * @author tiweGH
 */
public class GapRemover extends Preprocessor {

    private final long gapTimeInMinutes;
    private final VaultEntryType gapType;

    public GapRemover(VaultEntryType removeType, long gapTimeInMinutes) {
        this.gapTimeInMinutes = gapTimeInMinutes;
        this.gapType = removeType;
    }

    @Override
    public List<VaultEntry> preprocess(List<VaultEntry> data) {
        List<VaultEntry> result = new ArrayList<>();
        List<VaultEntry> tempList = new ArrayList<>();
        Date startTime = null;
        Date endDate = null;
        if (gapType != null && gapTimeInMinutes > 0) {
            int dataSize = data.size();
            int nextTenth = dataSize / 10;
            int i = 0;
            System.out.println("Preprocessing: Removing Gaps");
            for (VaultEntry vaultEntry : data) {
                if (vaultEntry.getType() == gapType && startTime == null) {
                    startTime = vaultEntry.getTimestamp();
                    tempList.add(vaultEntry);
                    endDate = TimestampUtils.addMinutesToTimestamp(startTime, gapTimeInMinutes);//new Date(startTime.getTime() + gapTimeInMinutes);
                } else if (vaultEntry.getType() == gapType && startTime != null) {
                    if (TimestampUtils.withinDateTimeSpan(startTime, endDate, vaultEntry.getTimestamp())) {
                        tempList.add(vaultEntry);
                        result.addAll(tempList);
                    } else if (tempList.size() > 0) {
                        System.out.println("Removed from " + startTime + " to " + endDate + " with " + tempList.size());
                    }
                    tempList = new ArrayList<>();
                    startTime = vaultEntry.getTimestamp();
                    endDate = TimestampUtils.addMinutesToTimestamp(startTime, gapTimeInMinutes);//new Date(startTime.getTime() + gapTimeInMinutes);
                } else if (startTime == null && vaultEntry.getType() != gapType) {
                    result.add(vaultEntry);
                } else {
                    tempList.add(vaultEntry);
                }
                //add last temp List if in time span
                if (i == dataSize - 1 && TimestampUtils.withinDateTimeSpan(startTime, endDate, vaultEntry.getTimestamp())) {
                    result.addAll(tempList);
                } else if (i == dataSize - 1 && tempList.size() > 0) {
                    System.out.println("Removed LAST from " + startTime + " to " + endDate + " with " + tempList.size());
                }

                if (i == nextTenth) {
                    System.out.println("Processed " + (int) (((double) i / (double) dataSize) * 100) + "% at " + (new Date(System.currentTimeMillis())));
                    nextTenth = nextTenth + dataSize / 10;
                }
                i++;
            }
            System.out.println("Removed " + (dataSize - result.size()) + " entries");
            System.out.println("");
        } else {
            result = data;
        }
        return result;
    }

}
