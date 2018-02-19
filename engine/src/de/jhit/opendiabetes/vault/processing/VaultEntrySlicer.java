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
package de.jhit.opendiabetes.vault.processing;

import de.jhit.opendiabetes.vault.container.SliceEntry;
import de.jhit.opendiabetes.vault.container.VaultEntry;
import de.jhit.opendiabetes.vault.container.VaultEntryType;
import de.jhit.opendiabetes.vault.processing.filter.Filter;
import de.jhit.opendiabetes.vault.processing.filter.FilterResult;
import de.jhit.opendiabetes.vault.util.TimestampUtils;
import de.jhit.opendiabetes.vault.util.VaultEntryUtils;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.logging.Logger;
import javafx.util.Pair;

/**
 *
 * @author juehv, tiweGH
 */
public class VaultEntrySlicer {

    private final List<Filter> registeredFilter = new ArrayList<>();
    //    private long clusterTimeInMillis = 0;
    //    private VaultEntryType clusterType;
    private long gapTimeInMinutes = 0;
    //Preprocessing start
    private List<Filter> queryFilters;
    private List<Pair<Long, Pair<VaultEntryType, VaultEntryType>>> clusterParams = new ArrayList<>();
    private VaultEntryType gapType;

    public VaultEntrySlicer() {
    }

    /**
     * Slices dataset with respect to registered filters.
     *
     * @param data the data set which should be filtered
     * @return a list of slice entrys matching the registered filters or an
     * empty list if no filter matches
     */
    public FilterResult sliceEntries(List<VaultEntry> data) {

        data = preprocessing(data);

        FilterResult lastResult = null;

        for (Filter filter : registeredFilter) {
            System.out.println(filter);
            if (lastResult == null) {
                lastResult = filter.filter(data);
            } else {
                lastResult = filter.filter(lastResult.filteredData);
            }
        }

        return lastResult;
    }

    /**
     * Registeres a filter for slicing. Should be called before slicing.
     * Registered filteres are always combined as logical AND.
     *
     * @param filter
     */
    public void registerFilter(Filter filter) {
        registeredFilter.add(filter);
    }

    /**
     * Registeres a filter for slicing. Should be called before slicing.
     * Registered filteres are always combined as logical AND.
     *
     * @param filters
     */
    public void registerFilter(List<Filter> filters) {
        registeredFilter.addAll(filters);
    }

    public void setGapRemoving(long gapTimeInMinutes, VaultEntryType removeType) {
        this.gapTimeInMinutes = gapTimeInMinutes;
        this.gapType = removeType;
    }

    /**
     * This method will remove gaps between two timestamps from a given
     * vaultentrytpye. If there is an vaultentry in the given timerange the new
     * Vaultentry will be the start for the new gap.
     *
     * @param vaultEntries
     * @return
     */
    public List<VaultEntry> removeGaps(List<VaultEntry> vaultEntries) {
        List<VaultEntry> result = new ArrayList<VaultEntry>();
        List<VaultEntry> tempList = new ArrayList<VaultEntry>();
        Date startTime = null;
        Date endDate = null;
        if (gapType != null && gapTimeInMinutes > 0) {
            System.out.println("Preprocessing: Removing Gaps");
            for (VaultEntry vaultEntry : vaultEntries) {
                if (vaultEntry.getType() == gapType && startTime == null) {
                    startTime = vaultEntry.getTimestamp();
                    tempList.add(vaultEntry);
                    endDate = TimestampUtils.addMinutesToTimestamp(startTime, gapTimeInMinutes);//new Date(startTime.getTime() + gapTimeInMinutes);
                } else if (vaultEntry.getType() == gapType && startTime != null) {
                    if (TimestampUtils.withinDateTimeSpan(startTime, endDate, vaultEntry.getTimestamp())) {
                        tempList.add(vaultEntry);
                        result.addAll(tempList);
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
                if (vaultEntries.indexOf(vaultEntry) == vaultEntries.size() - 1 && TimestampUtils.withinDateTimeSpan(startTime, endDate, vaultEntry.getTimestamp())) {
                    result.addAll(tempList);
                }
            }
        } else {
            result = vaultEntries;
        }
        return result;
    }

    /**
     * This Method checks if the given vaultEntry are correct with the given
     * Querry. If the queery is wong the result will be null. This method will
     * only works, if the parameters are set correctly in the setQuerying
     * method.
     *
     *
     * @param data
     * @return
     */
    public List<VaultEntry> query(List<VaultEntry> data) {
        List<VaultEntry> result = data;
        if (queryFilters != null && queryFilters.size() > 0) {
            System.out.println("Preprocessing: Query pre filter");
            for (Filter queryFilter : queryFilters) {
                if (queryFilter.filter(data).size() == 0) {
                    result = null;
                }
            }
        }
        return result;
    }

    public void setQuerying(List<Filter> queryFilters) {
        this.queryFilters = queryFilters;
    }

    public void addClustering(long clusterTimeInMillis, VaultEntryType typeToBeClustered, VaultEntryType clusterType) {
        clusterParams.add(new Pair<>(clusterTimeInMillis, new Pair<>(typeToBeClustered, clusterType)));
        //        this.clusterTimeInMillis = clusterTimeInMillis;
        //        this.clusterType = typeToBeClustered;
    }

    /**
     * This Method add clustered Vaultentry from the setType in the
     * setClusteringMethod. This Method will only work if the parameters are set
     * correctly. The clsteredVaultEntry is at the end of the clustered Series. 
     * This method sums up all entries ind the given Timerange and creates a new Vaultentry.
     *
     * @param data
     * @param clusterTimeInMinutes
     * @param searchedType
     * @param clusterType
     * @return
     */
    public List<VaultEntry> cluster(List<VaultEntry> data, long clusterTimeInMinutes, VaultEntryType searchedType, VaultEntryType clusterType) {
        long temp1, temp2;
        temp1 = System.nanoTime();

        List<VaultEntry> result = data;
        if (clusterTimeInMinutes > 0 && searchedType != null) {
            System.out.println("Preprocessing: Clustering");
            List<VaultEntry> clusteredList = new ArrayList<>();
            Date startTime = null;
            Date compareDate = null;
            Date tmp;
            double sumOfValue = 0;
            for (VaultEntry vaultEntry : result) {
                if (startTime == null) {
                    startTime = vaultEntry.getTimestamp();
                    compareDate = TimestampUtils.addMinutesToTimestamp(startTime, clusterTimeInMinutes);
                }
                //compareDate = TimestampUtils.addMinutesToTimestamp(startTime, clusterTimeInMinutes);
                if (compareDate.before(vaultEntry.getTimestamp()) || result.indexOf(vaultEntry) == result.size() - 1) {
                    //clustertype?
                    tmp = VaultEntryUtils.getWeightedMiddle(VaultEntryUtils.subList(data, startTime, compareDate), searchedType);
                    // tmp =
                    if (tmp != null) {
                        VaultEntry tmpVaultEntry = new VaultEntry(clusterType, tmp, sumOfValue);
                        System.out.println("New Cluster between " + startTime + " and " + compareDate + " : " + tmpVaultEntry.getTimestamp());
                        clusteredList.add(tmpVaultEntry);
                    }
                    //Alternative cluster the day in whole blocks, starting with 00:00 and not with the first entry
                    startTime = compareDate;//vaultEntry.getTimestamp();
                    compareDate = TimestampUtils.addMinutesToTimestamp(startTime, clusterTimeInMinutes);
                    sumOfValue = 0;
                }
                clusteredList.add(vaultEntry);
                if (vaultEntry.getType() == searchedType) {
                    sumOfValue += vaultEntry.getValue();
                }
            }
            result = clusteredList;
            result = VaultEntryUtils.sort(result);
        }
        temp2 = System.nanoTime();
        System.out.println(temp2 - temp1);
        return result;
    }

    /**
     * Preprocessing for slicing. Prerocessing calls different Methods, which
     * will be set specific sst methods.
     *
     * @param data
     * @return
     */
    public List<VaultEntry> preprocessing(List<VaultEntry> data) {
        List<VaultEntry> result = data;
        result = removeGaps(result);
        result = query(result);
        for (Pair<Long, Pair<VaultEntryType, VaultEntryType>> clusterParam : clusterParams) {
            result = cluster(result, clusterParam.getKey(), clusterParam.getValue().getKey(), clusterParam.getValue().getValue());
        }
        return result;
    }

}
