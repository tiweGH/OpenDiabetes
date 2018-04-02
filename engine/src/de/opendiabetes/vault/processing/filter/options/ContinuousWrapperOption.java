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
package de.opendiabetes.vault.processing.filter.options;

import de.opendiabetes.vault.container.VaultEntry;
import java.util.List;

/**
 *
 * @author tiweGH
 */
public class ContinuousWrapperOption extends FilterOption {

    List<VaultEntry> baseData;
    protected int marginBefore;
    protected int marginAfter;

    public ContinuousWrapperOption(List<VaultEntry> baseData, int marginBefore, int marginAfter) {
        if (marginBefore < 0 || marginAfter < 0) {
            throw new IllegalArgumentException("Expected a margin >= 0 but was " + marginBefore + " and " + marginAfter);
        }

        this.marginBefore = marginBefore;
        this.marginAfter = marginAfter;
        this.baseData = baseData;
    }

    public ContinuousWrapperOption(List<VaultEntry> baseData, int marginInMinutes) {
        this(baseData, marginInMinutes, marginInMinutes);
    }

    public List<VaultEntry> getBaseData() {
        return baseData;
    }

    public int getMarginAfter() {
        return marginAfter;
    }

    public int getMarginBefore() {
        return marginBefore;
    }

}
