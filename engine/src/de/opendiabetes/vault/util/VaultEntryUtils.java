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
package de.opendiabetes.vault.util;

import de.opendiabetes.vault.container.VaultEntry;
import de.opendiabetes.vault.container.VaultEntryType;
import de.opendiabetes.vault.container.VaultEntryTypeGroup;
import de.opendiabetes.vault.processing.filter.Filter;
import de.opendiabetes.vault.processing.filter.FilterResult;
import java.util.Date;
import java.util.List;

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
     * and the last timestamp or null if the list is empty
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
     * @return an entry nearest to the given timestamp, or null if the list is
     * empty
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

    /**
     * Returns the index of the VaultEntry with a date closest to the given
     * timestamp. The found date can be before or after the given timestamp.
     *
     * @param data list of VaultEntries
     * @param timestamp searched date
     * @return the index of the nearest date, or -1 if the list is empty
     */
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

    /**
     * Returns the index of the nearest VaultEntry located before the given
     * timestamp.
     *
     * @param data sorted list of VaultEntries
     * @param timestamp searched date
     * @return the index of the nearest date, or -1 if the first entry of the
     * list is after the given timestamp or the list is empty
     */
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

    /**
     * Returns the index of the nearest VaultEntry located after the given
     * timestamp.
     *
     * @param data sorted list of VaultEntries
     * @param timestamp searched date
     * @return the index of the nearest date, or -1 if the last entry of the
     * list is before the given timestamp or the list is empty
     */
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

    /**
     * Calculates the timepoint of the weighted middle of a specific Type in a
     * given VaultEntry list, using the <code>value</code> of a VaultEntry as
     * weight.<p>
     * More precisely, it calculates:<br>
     * <code>(date1*value1 + date2*value2 +...) / (value1 + value2 + ...)</code>
     *
     * @param data List in which the weighted middle is searched for
     * @param type specific type which will be weighted
     * @return date of the weighted middle, not necessarily present in
     * <code>data</code>. Returns null if input data is empty, or doesn't have
     * entries with the given type
     */
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
        }

        return result;
    }

    /**
     * Returns a sublist of the given VaultEntry List, consisting of Entries
     * between the both given VaultEntries. Might be slow with large Lists!
     *
     * @param data
     * @param fromIndex earliest VaultEntry in the sublist
     * @param toIndex latest VaultEntry in the sublist
     * @return
     */
    public static List<VaultEntry> subList(List<VaultEntry> data, VaultEntry fromIndex, VaultEntry toIndex) {
        return data.subList(data.indexOf(fromIndex), data.indexOf(toIndex));
    }

    /**
     * Returns a sublist of the given VaultEntry List, consisting of Entries
     * between the both given timepoints. Might be slow with large Lists!
     *
     * @param data
     * @param fromTimestamp earliest date of the sublist
     * @param toTimestamp latest date of the sublist
     * @return
     */
    public static List<VaultEntry> subList(List<VaultEntry> data, Date fromTimestamp, Date toTimestamp) {
        return data.subList(getIndexOfNearestEntryAt(data, fromTimestamp), getIndexOfNearestEntryAt(data, toTimestamp));
    }

    /**
     * Used as a shorthand for Filters, to test if Group and/or Type match.
     *
     * @param entry entry under test
     * @param type tested Type, can be <code>null</code> for DON'T CARE
     * @param group tested Group, can be <code>null</code> for DON'T CARE
     * @return
     */
    public static boolean equalsTypeAndGroup(VaultEntry entry, VaultEntryType type, VaultEntryTypeGroup group) {
        boolean result;
        result = (type == null || entry.getType() == type) && (group == null || entry.getType().getGroup() == group);
        return result;
    }

    /**
     * Shorthand, sorts the given list of VaultEntries by Date
     *
     * @param data the List to be sorted
     *
     * @return
     */
    public static List<VaultEntry> sort(List<VaultEntry> data) {
        if (data != null) {
            data.sort(new SortVaultEntryByDate());
        }
        return data;
    }

    /**
     * The standard Slicing process for usage in filters. Note that this method
     * isn't used in the Slicer itself, but is a copy of it.
     *
     * @param data input to be sliced
     * @param filters filters to be used
     * @return sliced FilterResult
     */
    public static FilterResult slice(List<VaultEntry> data, List<Filter> filters) {
        FilterResult result = new FilterResult();
        if (data != null && filters != null) {
            System.out.println("Start Slicing");
            FilterResult lastResult = null;
            for (Filter filter : filters) {
                System.out.println(filter);
                if (lastResult == null) {
                    lastResult = filter.filter(data);
                } else {
                    lastResult = filter.filter(lastResult.filteredData);
                }
            }
            result = lastResult;
            System.out.println("Finished Slicing: Removed " + (data.size() - result.size()));
        }

        return result;
    }
}
