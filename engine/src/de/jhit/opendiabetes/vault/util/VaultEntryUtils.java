/*
 * Copyright (C) 2017 Jens Heuschkel
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
package de.jhit.opendiabetes.vault.util;

import de.jhit.opendiabetes.vault.container.VaultEntry;
import de.jhit.opendiabetes.vault.container.VaultEntryType;
import de.jhit.opendiabetes.vault.container.VaultEntryTypeGroup;
import de.jhit.opendiabetes.vault.processing.filter.Filter;
import de.jhit.opendiabetes.vault.processing.filter.FilterResult;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;
import javafx.util.Pair;

/**
 *
 * @author tiweGH
 */
public class VaultEntryUtils {

    /**
     * Returns the VaultEntry with the timestamp located in the middle of the
     * given entry-timestamps
     *
     * @param data contains sorted entries with timestamps, first with the
     * oldest and last with the newest timestamp
     * @return an entry with a timestamp nearest the actual middle of the first
     * and the last timestamp
     */
    public static VaultEntry getNearestMidEntry(List<VaultEntry> data) {
        VaultEntry result = null;
        if (data != null) {
            Date actualMid = TimestampUtils.getMidDate(data.get(0).getTimestamp(), data.get(data.size() - 1).getTimestamp());
            result = getNearestEntryAt(data, actualMid);
        }
        return result;
    }

    /**
     * Returns the VaultEntry with the timestamp located nearest to the given
     * timestamp
     *
     * @param data contains sorted entries with timestamps, first with the
     * oldest and last with the newest timestamp
     * @param timestamp
     * @return an entry with a timestamp nearest the actual middle of the first
     * and the last timestamp
     */
    public static VaultEntry getNearestEntryAt(List<VaultEntry> data, Date timestamp) {
        VaultEntry result = null;
        if (data != null && timestamp != null) {
            long searchedTime = timestamp.getTime();
            long currentMin = Long.MAX_VALUE;
            long temp;
            for (VaultEntry vaultEntry : data) {
                temp = Math.abs(searchedTime - vaultEntry.getTimestamp().getTime());
                if (temp < currentMin) {
                    result = vaultEntry;
                    currentMin = temp;
                }
            }
        }
        return result;
    }

    public static int getIndexOfNearestEntryAt(List<VaultEntry> data, Date timestamp) {
        int result = -1;
        if (data != null && timestamp != null) {
            long searchedTime = timestamp.getTime();
            long currentMin = Long.MAX_VALUE;
            long temp;
            int i = 0;
            for (VaultEntry vaultEntry : data) {
                temp = Math.abs(searchedTime - vaultEntry.getTimestamp().getTime());
                if (temp < currentMin) {
                    result = i;
                    currentMin = temp;
                }
                i++;
            }
        }
        return result;
    }

    public static int getIndexOfNearestEntryBefore(List<VaultEntry> data, Date timestamp) {
        int result = -1;
        if (data != null && timestamp != null) {

            int i = 0;
            for (VaultEntry vaultEntry : data) {
                //if timestamp < entry return entry-1
                if (timestamp.before(vaultEntry.getTimestamp())) {
                    result = i - 1;
                    break;
                }
                i++;
            }
        }
        return result;
    }

    public static int getIndexOfNearestEntryAfter(List<VaultEntry> data, Date timestamp) {
        int result = -1;
        if (data != null && timestamp != null) {

            int i = 0;
            for (VaultEntry vaultEntry : data) {
                //if timestamp <= entry return entry
                if (!vaultEntry.getTimestamp().before(timestamp)) {
                    result = i;
                    break;
                }
                i++;
            }
        }
        return result;
    }

    public static Date getWeightedMiddle(List<VaultEntry> data, VaultEntryType type) {
        Date result = null;
        if (data != null) {
            long weightedTimeSum = 0;
            long weightSum = 0;
            for (VaultEntry vaultEntry : data) {
                if (vaultEntry.getType() == type) {
                    //(calculation is (elem1*weight1 + elem2*weight2+...) / (weight1 + weight2 + ...)
                    weightedTimeSum += (vaultEntry.getTimestamp().getTime()) * (long) vaultEntry.getValue();
                    weightSum += (long) vaultEntry.getValue();
                }
            }

            if (weightSum != 0) {
                result = TimestampUtils.createCleanTimestamp(new Date((weightedTimeSum / weightSum)));
                //System.out.println(result);
            }
            if (result == null) {
                System.out.println("NULL");
                for (VaultEntry vaultEntry : data) {
                    System.out.println(vaultEntry.toString());
                }
                System.out.println("size = " + data.size());
                System.out.println("weightedTimeSum = " + weightedTimeSum);
                System.out.println("weightSum = " + weightSum);

            }
        }

        return result;
    }

    public static List<VaultEntry> subList(List<VaultEntry> data, VaultEntry fromIndex, VaultEntry toIndex) {
        return data.subList(data.indexOf(fromIndex), data.indexOf(toIndex));
    }

    public static List<VaultEntry> subList(List<VaultEntry> data, Date fromTimestamp, Date toTimestamp) {
        return data.subList(getIndexOfNearestEntryAt(data, fromTimestamp), getIndexOfNearestEntryAt(data, toTimestamp));
    }

    public static boolean equalsTypeAndGroup(VaultEntry entry, VaultEntryType type, VaultEntryTypeGroup group) {
        boolean result;
        result = (type == null || entry.getType() == type) && (group == null || entry.getType().getGroup() == group);
        return result;
    }

    public static List<VaultEntry> sort(List<VaultEntry> data) {
        if (data != null) {
            data.sort(new SortVaultEntryByDate());
        }
        return data;
    }

    public static FilterResult slice(List<VaultEntry> data, List<Filter> filters) {
        FilterResult result = new FilterResult();
        if (data != null && filters != null) {
            FilterResult lastResult = null;
            for (Filter filter : filters) {
                //System.out.println(filter);
                if (lastResult == null) {
                    lastResult = filter.filter(data);
                } else {
                    lastResult = filter.filter(lastResult.filteredData);
                }
            }
            result = lastResult;
        }

        return result;
    }
}
