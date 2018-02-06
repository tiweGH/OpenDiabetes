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
import de.jhit.opendiabetes.vault.util.TimestampUtils;
import de.jhit.opendiabetes.vault.util.VaultEntryUtils;
import java.util.Date;
import java.util.List;
import javax.naming.spi.DirStateFactory;

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

    /**
     * Searches the given dataset for one specific position.
     *
     * @param filter The FilterResult of this Filter will be used as working
     * data for the position search
     * @param filterMode Configures the position search:
     * <p>
     * <code>FIRST</code>: The first entry of the input data<p>
     * <code>LAST</code>: The last entry<p>
     * <code>MIDDLE</code>: The entry with the middle index<p>
     * <code>DATE_MIDDLE</code>: The entry with the timestamp located in the
     * middle between the first and the last entry
     */
    public PositionFilter(Filter filter, int filterMode) {
        this.filter = filter;
        this.filterMode = filterMode;
        positionResult = null;
    }

    /**
     * Searches the given dataset for one specific position.
     *
     * @param filterMode Configures the position search:
     * <p>
     * <code>FIRST</code>: The first entry of the input data<p>
     * <code>LAST</code>: The last entry<p>
     * <code>MIDDLE</code>: The entry with the middle index<p>
     * <code>DATE_MIDDLE</code>: The entry with the timestamp located in the
     * middle between the first and the last entry
     */
    public PositionFilter(int filterMode) {
        this(new NoneFilter(), filterMode);
    }

    @Override
    FilterType getType() {
        return FilterType.EVENT_FILTER;
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
        return new PositionFilter(filter, filterMode);
    }
}
