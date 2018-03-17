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
import de.jhit.opendiabetes.vault.util.VaultEntryUtils;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.logging.Level;

/**
 *
 * @author tiweGH
 */
public class ClusterPreprocessor extends Preprocessor {

    private final long clusterTimeInMinutes;
    private final VaultEntryType clusterType;
    private final VaultEntryType typeToBeClustered;

    public ClusterPreprocessor(long clusterTimeInMinutes, VaultEntryType typeToBeClustered, VaultEntryType clusterType) {
        //clusterParams.add(new Pair<>(clusterTimeInMillis, new Pair<>(typeToBeClustered, clusterType)));
        this.clusterTimeInMinutes = clusterTimeInMinutes;
        this.clusterType = typeToBeClustered;
        this.typeToBeClustered = typeToBeClustered;
    }

    @Override
    public List<VaultEntry> preprocess(List<VaultEntry> data) {
        long temp1, temp2;
        temp1 = System.nanoTime();

        List<VaultEntry> result = data;
        if (clusterTimeInMinutes > 0 && typeToBeClustered != null) {
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
                    tmp = VaultEntryUtils.getWeightedMiddle(VaultEntryUtils.subList(data, startTime, compareDate), typeToBeClustered);
                    // tmp =
                    if (tmp != null) {
                        VaultEntry tmpVaultEntry = new VaultEntry(clusterType, tmp, sumOfValue);
                        LOG.log(Level.INFO, "New Cluster between {0} and {1} : {2}", new Object[]{startTime, compareDate, tmpVaultEntry.getTimestamp()});
                        clusteredList.add(tmpVaultEntry);
                    }
                    //Alternative cluster the day in whole blocks, starting with 00:00 and not with the first entry
                    startTime = compareDate;//vaultEntry.getTimestamp();
                    compareDate = TimestampUtils.addMinutesToTimestamp(startTime, clusterTimeInMinutes);
                    sumOfValue = 0;
                }
                clusteredList.add(vaultEntry);
                if (vaultEntry.getType() == typeToBeClustered) {
                    sumOfValue += vaultEntry.getValue();
                }
            }
            result = clusteredList;
            VaultEntryUtils.sort(result);
        }
        temp2 = System.nanoTime();
        System.out.println(temp2 - temp1);
        return result;
    }

}
