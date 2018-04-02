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
package de.opendiabetes.vault.processing.preprocessing;

import de.opendiabetes.vault.container.VaultEntry;
import de.opendiabetes.vault.container.VaultEntryType;
import de.opendiabetes.vault.processing.preprocessing.options.ClusterPreprocessorOption;
import de.opendiabetes.vault.processing.preprocessing.options.PreprocessorOption;
import de.opendiabetes.vault.util.TimestampUtils;
import de.opendiabetes.vault.util.VaultEntryUtils;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author tiweGH
 */
public class ClusterPreprocessor extends Preprocessor {

    private final long clusterTimeInMinutes;
    private final VaultEntryType clusterType;
    private final VaultEntryType typeToBeClustered;

    public ClusterPreprocessor(PreprocessorOption preprocessorOption) {

        super(preprocessorOption);
        if (preprocessorOption instanceof ClusterPreprocessorOption) {
            ClusterPreprocessorOption clusterPreprocessorOption = (ClusterPreprocessorOption) preprocessorOption;

            this.clusterTimeInMinutes = clusterPreprocessorOption.getClusterTimeInMinutes();
            this.clusterType = clusterPreprocessorOption.getClusterType();
            this.typeToBeClustered = clusterPreprocessorOption.getTypeToBeClustered();
        } else {
            String msg = "Option has to be an instance of ClusterPreprocessorOption";
            Logger.getLogger(this.getClass().getName()).log(Level.SEVERE, msg);
            throw new Error(msg);//IllegalArgumentException("Option has to be an instance of CombinationFilterOption");
        }

    }

    @Override
    public List<VaultEntry> preprocess(List<VaultEntry> data) {
        long temp1, temp2;
        //temp1 = System.currentTimeMillis();

        List<VaultEntry> result = data;
        if (data != null && clusterTimeInMinutes > 0 && typeToBeClustered != null) {
            System.out.println("Preprocessing: Clustering");
            List<VaultEntry> clusteredList = new ArrayList<>();
            Date startTime = null;
            Date compareDate = null;
            Date tmpDate;
            List<VaultEntry> tmpSubList;

            int dataSize = data.size();
//            double tenthCounter = 1.0;
//            int nextTenth = (int) Math.round(((double) dataSize / 10.0));
            int index = 0;
            int startTimeIndex = 0;

            double sumOfValue = 0;
            for (VaultEntry vaultEntry : result) {
                if (startTime == null) {
                    startTime = vaultEntry.getTimestamp();
                    compareDate = TimestampUtils.addMinutesToTimestamp(startTime, clusterTimeInMinutes);
                }
                //compareDate = TimestampUtils.addMinutesToTimestamp(startTime, clusterTimeInMinutes);
                if (compareDate.before(vaultEntry.getTimestamp()) || index == dataSize - 1) {
                    //clustertype?
                    //tmpSubList = VaultEntryUtils.subList(data, startTime, compareDate); //very slow with large Lists, since indexOf() is called multiple times
                    tmpSubList = result.subList(startTimeIndex, index);
                    tmpDate = VaultEntryUtils.getWeightedMiddle(tmpSubList, typeToBeClustered);
                    // tmp =
                    if (tmpDate != null) {
                        VaultEntry tmpVaultEntry = new VaultEntry(clusterType, tmpDate, sumOfValue);
                        //LOG.log(Level.INFO, "New Cluster between {0} and {1} : {2}", new Object[]{startTime, compareDate, tmpVaultEntry.getTimestamp()});
                        clusteredList.add(tmpVaultEntry);
                    }
                    //Alternative cluster the day in whole blocks, starting with 00:00 and not with the first entry
                    startTime = compareDate;//vaultEntry.getTimestamp();
                    startTimeIndex = index;
                    compareDate = TimestampUtils.addMinutesToTimestamp(startTime, clusterTimeInMinutes);
                    sumOfValue = 0;
                }
                clusteredList.add(vaultEntry);
                if (vaultEntry.getType() == typeToBeClustered) {
                    sumOfValue += vaultEntry.getValue();
                }

//                if (index == nextTenth) {
//                    System.out.println("Processed " + (int) (((double) index / (double) dataSize) * 100) + "% at " + (new Date(System.currentTimeMillis())));
//                    //System.out.println("Processed " + index + " of " + dataSize + " at " + (new Date(System.currentTimeMillis())));
//                    tenthCounter++;
//                    nextTenth = (int) Math.round(tenthCounter * ((double) dataSize / 10.0));
//                }
                index++;
            }
            result = clusteredList;
            VaultEntryUtils.sort(result);
            System.out.println("Added " + (result.size() - data.size()) + " Cluster-Entries");
        }
//        temp2 = System.currentTimeMillis();
        //System.out.println(temp2 - temp1);
        return result;
    }

}
