/*
 * Copyright (C) 2017 Jorg
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
package de.jhit.opendiabetes.vault.processing.filter;

import de.jhit.opendiabetes.vault.container.VaultEntry;
import de.jhit.opendiabetes.vault.container.VaultEntryType;
import java.util.List;

/**
 *
 * @author Daniel aber an Jörgs Pc wie immer halt
 */
public class BolusNormalSpanFilter extends Filter {

    private final float value;
    private final int margin;
    private final VaultEntryType vaultEntryType;
    
    public BolusNormalSpanFilter(float value, int margin) {
        vaultEntryType = VaultEntryType.BOLUS_NORMAL;
        this.value = value;
        this.margin = margin;
    }

    @Override
    FilterType getType() {
        return FilterType.BOLUS_NORMAL;
    }

    @Override
    boolean matchesFilterParameters(VaultEntry entry) {
        return entry.getType().equals(vaultEntryType) && entry.getValue()>=value-margin && entry.getValue()<= value + margin;
    }
}
