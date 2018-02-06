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
package de.jhit.opendiabetes.vault.processing.filter;

import de.jhit.opendiabetes.vault.container.VaultEntry;
import de.jhit.opendiabetes.vault.container.VaultEntryType;
import de.jhit.opendiabetes.vault.container.VaultEntryTypeGroup;

/**
 *
 * @author AChikhale
 */
public class UnderThresholdFilter extends ThresholdFilter {

// GenericType I am taking it as package de.jhit.opendiabetes.vault.container.VaultEntry as private VaultEntryType type;
    /**
     * Filters entries with a <code>value</code> <b>less</b> than
     * <code>thresholdValue</code>
     *
     * @param GenericType
     * @param thresholdValue
     * @param availabledatatype
     * @param TH
     */
    public UnderThresholdFilter(VaultEntryType GenericType, Double thresholdValue, FilterType availabledatatype, FilterType TH) {
        if (super.checkThresholdCombination(GenericType, availabledatatype, TH)) {

            this.thresholdValue = thresholdValue;
            this.type = TH;
            this.filterType = TH;
            this.filteredType = GenericType;
            this.availabledatatype = availabledatatype;

        }
    }

    public UnderThresholdFilter(VaultEntryTypeGroup filteredGroup, VaultEntryType fillteredType, Double thresholdValue) {
        this.filteredType = fillteredType;
        this.filteredGroup = filteredGroup;
        this.thresholdValue = thresholdValue;
    }

    public UnderThresholdFilter(VaultEntryType fillteredType, Double thresholdValue) {
        this(null, fillteredType, thresholdValue);

    }

    public UnderThresholdFilter(VaultEntryTypeGroup filteredGroup, Double thresholdValue) {
        this(filteredGroup, null, thresholdValue);

    }

    public UnderThresholdFilter(Double thresholdValue) {
        this(null, null, thresholdValue);
    }

    @Override

    public FilterType getType() {
        return FilterType.UNDER_TH;
    }

    @Override
    boolean matchesFilterParameters(VaultEntry entry) {
        return matchesThresholdFilter(entry, entry.getValue() < thresholdValue);
    }

    @Override
    Filter update(VaultEntry vaultEntry) {
        return new UnderThresholdFilter(vaultEntry.getType(), thresholdValue);
    }
}
