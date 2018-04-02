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
package de.opendiabetes.vault.processing;

import de.opendiabetes.vault.container.SliceEntry;
import de.opendiabetes.vault.container.VaultEntry;
import de.opendiabetes.vault.processing.filter.Filter;
import de.opendiabetes.vault.processing.filter.FilterResult;
import de.opendiabetes.vault.processing.preprocessing.Preprocessor;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.logging.Logger;
import javafx.util.Pair;

/**
 *
 * @author juehv
 */
public class DataSlicer extends VaultEntrySlicer {

    private final List<Filter> registeredFilter = new ArrayList<>();
    private final List<Preprocessor> preprocessors = new ArrayList<>();
    private final DataSlicerOptions options;

    public DataSlicer(DataSlicerOptions options) {
        this.options = options;
    }

    /**
     * Slices dataset with respect to registered filters.
     *
     * @param data the data set which should be filtered
     * @return a list of slice entrys matching the registered filters or an
     * empty list if no filter matches
     */
    public List<SliceEntry> sliceData(List<VaultEntry> data) {

        data = preprocessing(data);

        List<SliceEntry> retVal = new ArrayList<>();
        FilterResult lastResult = sliceEntries(data);

        if (lastResult != null) {
            for (Pair<Date, Date> item : lastResult.timeSeries) {
                Date tmpTimestamp = null;
                switch (options.outputFilter) {
                    case FIRST_OF_SERIES:
                        tmpTimestamp = item.getKey();
                        break;
                    case MID_OF_SERIES:
                        // TODO find the mid TIMEPOINT (not the timestamp in the middle)
                        // calclulate timepoint in the middle between first and last timestamp of series
                        // find the timestamp nearest to the calculated timepoint in filtered data
                        tmpTimestamp = generateMidPoint(lastResult, new Date((item.getValue().getTime() + item.getKey().getTime()) / 2));
                        break;
                    case END_OF_SERIES:
                        tmpTimestamp = item.getValue();
                        break;
                    default:
                        Logger.getLogger(DataSlicer.class.getName()).severe("ASSERTION ERROR: Unknown filter case!");
                        throw new AssertionError("Unknown output filter");
                }
                if (tmpTimestamp != null) {
                    SliceEntry tmpEntry = new SliceEntry(tmpTimestamp, options.duration);
                    retVal.add(tmpEntry);
                }
            }
        }
        return retVal;
    }

    private Date generateMidPoint(FilterResult lastResult, Date temporary) {
        int localTempVar = -1;
        for (int i = 0; i < lastResult.filteredData.size(); i++) {
            if (lastResult.filteredData.get(i).getTimestamp().getTime() >= temporary.getTime()) {
                // return lastResult.filteredData.get(i).getTimestamp();
                localTempVar = i;
                break;

            }
        }
        if (localTempVar >= 0) {
            int previousValueofMidpoint = (int) (temporary.getTime() - lastResult.filteredData.get(localTempVar - 1).getTimestamp().getTime());
            int nextValueofMidpoint = (int) (lastResult.filteredData.get(localTempVar).getTimestamp().getTime() - temporary.getTime());
            if (previousValueofMidpoint <= nextValueofMidpoint) {
                return lastResult.filteredData.get(localTempVar - 1).getTimestamp(); //smaller value from midppoint calculated
            } else {
                return lastResult.filteredData.get(localTempVar).getTimestamp(); //higher value from  midpoint calculated
            }

        } else {
            return null;
        }
    }
}
