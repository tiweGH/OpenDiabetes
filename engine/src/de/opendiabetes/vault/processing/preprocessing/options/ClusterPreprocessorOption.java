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
package de.opendiabetes.vault.processing.preprocessing.options;

import de.opendiabetes.vault.container.VaultEntryType;

/**
 *
 * @author tiweGH
 */
public class ClusterPreprocessorOption extends PreprocessorOption {

    private final VaultEntryType clusterType;
    private final long clusterTimeInMinutes;
    private final VaultEntryType typeToBeClustered;

    /**
     * Clusters a given dataset with the following options
     *
     * @param clusterTimeInMinutes length of a cluster, used for the calculation
     * fo the cluster entry
     * @param typeToBeClustered entries of this type will be clustered
     * @param clusterType type of the new calculated entry
     */
    public ClusterPreprocessorOption(long clusterTimeInMinutes, VaultEntryType typeToBeClustered, VaultEntryType clusterType) {
        this.clusterTimeInMinutes = clusterTimeInMinutes;
        this.typeToBeClustered = typeToBeClustered;
        this.clusterType = clusterType;
    }

    public VaultEntryType getTypeToBeClustered() {
        return typeToBeClustered;
    }

    public VaultEntryType getClusterType() {
        return clusterType;
    }

    public long getClusterTimeInMinutes() {
        return clusterTimeInMinutes;
    }

}
