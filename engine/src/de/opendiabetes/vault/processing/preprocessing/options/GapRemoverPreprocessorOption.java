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
public class GapRemoverPreprocessorOption extends PreprocessorOption {

    private final VaultEntryType removeType;
    private final long gapTimeInMinutes;

    /**
     * Get's a dataset of VaultEntries and removes gaps between entries of the
     * given types, which are bigger than the given time in minutes
     *
     * @param removeType type which will be used for the gap calculation
     * @param gapTimeInMinutes given maximum gap-length
     */
    public GapRemoverPreprocessorOption(VaultEntryType removeType, long gapTimeInMinutes) {
        this.gapTimeInMinutes = gapTimeInMinutes;
        this.removeType = removeType;
    }

    public VaultEntryType getGapType() {
        return removeType;
    }

    public long getGapTimeInMinutes() {
        return gapTimeInMinutes;
    }

}
