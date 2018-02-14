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
import java.util.Comparator;
import javafx.util.Pair;

/**
 *
 * @author Jens
 */
public class BucketInterpolatorPairComparator implements Comparator<Pair<Integer, Pair<VaultEntryType, Double>>> {

    @Override
    public int compare(Pair<Integer, Pair<VaultEntryType, Double>> o1, Pair<Integer, Pair<VaultEntryType, Double>> o2) {
        return o1.getKey().compareTo(o2.getKey());
    }

}