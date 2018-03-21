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
package de.opendiabetes.vault.container;

import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author tiweGH
 */
public enum VaultEntryTypeGroup {

    EMPTY, // Empty Group for Entries without a group, for null-safity regarding getTypes() calls
    CGM_SYSTEM,
    PUMP_SYSTEM,
    SLEEP,
    LOCATION,
    EXERCISE,
    GLUCOSE,
    MACHINE_LEARNING,
    DATA_MINING,
    BASAL,
    HEART,
    BOLUS,
    MEAL;

    /**
     * returns a List containing all VaultEntryTypes of this group
     *
     * @return a List containing all VaultEntryTypes of this group
     */
    public List<VaultEntryType> getTypes() {
        //better performance if handled one-time in constructor, but leads to initialization Error
        List<VaultEntryType> result = new ArrayList<>();
        for (VaultEntryType type : VaultEntryType.values()) {
            if (type.getGroup() == this) {
                result.add(type);
            }
        }
        return result;
    }
}
