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
package de.jhit.opendiabetes.vault.processing.filter;

import com.sun.javafx.scene.control.skin.VirtualFlow;
import de.jhit.opendiabetes.vault.container.VaultEntry;
import java.util.Date;
import java.util.List;
import javafx.util.Pair;

/**
 * Does nothing.
 *
 * @author juehv
 */
public class NoneFilter implements Filter {

    @Override
    public FilterResult filter(List<VaultEntry> data) {
        List<Pair<Date, Date>> timeSeries = new VirtualFlow.ArrayLinkedList<>();
        return new FilterResult(data, timeSeries);
    }

    @Override
    public FilterType getType() {
        return FilterType.NONE;
    }

}
