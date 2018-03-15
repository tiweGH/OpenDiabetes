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
import de.jhit.opendiabetes.vault.processing.filter.options.FilterOption;
import de.jhit.opendiabetes.vault.processing.filter.options.PositionFilterOption;
import de.jhit.opendiabetes.vault.util.VaultEntryUtils;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author tiweGH
 */
public class PositionFilter extends Filter {

    private Filter filter;
    private final int filterMode;
    private VaultEntry positionResult;

    /**
     * The first entry of the given input data
     */
    public static final int FIRST = 0;
    /**
     * The last entry of the given input data
     */
    public static final int LAST = 1;
    /**
     * The entry with the middle index of the given input data
     */
    public static final int MIDDLE = 2;
    /**
     * The entry with the timestamp located in the middle between the first and
     * the last entry of the given input data
     */
    public static final int DATE_MIDDLE = 3;

    public PositionFilter(FilterOption option) {
        super(option);
        if (option instanceof PositionFilterOption) {
            this.filter = ((PositionFilterOption) option).getFilter();
            this.filterMode = ((PositionFilterOption) option).getFilterMode();
            positionResult = null;
        } else {
            String msg = "Option has to be an instance of PositionFilterOption";
            Logger.getLogger(this.getClass().getName()).log(Level.SEVERE, msg);
            throw new Error(msg);//IllegalArgumentException("Option has to be an instance of CombinationFilterOption");
        }
    }

    @Override
    FilterType getType() {
        return FilterType.POSITION;
    }

    @Override
    protected List<VaultEntry> setUpBeforeFilter(List<VaultEntry> data) {
        List<VaultEntry> tempData = filter.filter(data).filteredData;
        switch (filterMode) {
            case (FIRST):
                positionResult = tempData.get(0);
                break;
            case (LAST):
                positionResult = tempData.get(tempData.size() - 1);
                break;
            case (MIDDLE):
                positionResult = tempData.get(tempData.size() / 2);
                break;
            case (DATE_MIDDLE):
                positionResult = VaultEntryUtils.getNearestMidEntry(tempData);
                break;
            default:
                break;
        }

        return tempData;
    }

    @Override
    boolean matchesFilterParameters(VaultEntry entry) {

        return this.positionResult != null && entry.equals(this.positionResult);
    }

    @Override
    Filter update(VaultEntry vaultEntry) {
        return new PositionFilter(new PositionFilterOption(filter.update(vaultEntry), filterMode));
    }
}
