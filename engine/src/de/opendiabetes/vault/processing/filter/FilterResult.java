/*
 * Copyright (C) 2017 juehv
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
package de.opendiabetes.vault.processing.filter;

import de.opendiabetes.vault.container.VaultEntry;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javafx.util.Pair;

/**
 *
 * @author juehv
 */
public class FilterResult {

    public final List<VaultEntry> filteredData;
    public final List<Pair<Date, Date>> timeSeries;

    public FilterResult(List<VaultEntry> filteredData, List<Pair<Date, Date>> timeSeries) {
        this.filteredData = filteredData;
        this.timeSeries = timeSeries;
    }

    public FilterResult() {
        this.filteredData = new ArrayList<>();
        this.timeSeries = new ArrayList<>();
    }

    public int size() {
        return filteredData.size();
    }

}
