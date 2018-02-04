/*
 * Copyright (C) 2017 AChikhale
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
//diesen Filter ueberarbeiten
package de.jhit.opendiabetes.vault.processing.filter;

import de.jhit.opendiabetes.vault.container.VaultEntry;
import de.jhit.opendiabetes.vault.container.VaultEntryType;

/**
 *
 * @author AChikhale
 */
public class OverThresholdFilter extends ThresholdFilter {

    // GenericType I am taking it as package de.jhit.opendiabetes.vault.container.VaultEntry as private VaultEntryType type;
    // thresholdValue is used to check if the value from DB is greater or less than given threshold
    /**
     * Filters entries with a <code>value</code> <b>greater</b> than
     * <code>thresholdValue</code>
     *
     * @param GenericType
     * @param thresholdValue
     * @param availabledatatype
     * @param TH
     */
    public OverThresholdFilter(VaultEntryType GenericType, Double thresholdValue, FilterType availabledatatype, FilterType TH) {

        if (super.checkThresholdCombination(GenericType, availabledatatype, TH)) {

            this.thresholdValue = thresholdValue;
            this.type = TH;
            this.filterType = TH;
            this.GenericType = GenericType;
            this.availabledatatype = availabledatatype;

        }

    }

    public OverThresholdFilter(Double thresholdValue) {
        this.thresholdValue = thresholdValue;
    }

    @Override
    public FilterType getType() {
        return FilterType.OVER_TH;
    }

    @Override
    boolean matchesFilterParameters(VaultEntry entry) {
        return //entry.getType() == GenericType &&
                entry.getValue() > thresholdValue;
    }

    @Override
    Filter update(VaultEntry vaultEntry) {
        return new OverThresholdFilter(vaultEntry.getType(), thresholdValue, availabledatatype, filterType);
    }
}
