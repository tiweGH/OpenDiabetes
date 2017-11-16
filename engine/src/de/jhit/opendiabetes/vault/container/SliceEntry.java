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
package de.jhit.opendiabetes.vault.container;

import com.j256.ormlite.field.DataType;
import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 *
 * @author juehv
 */
@DatabaseTable(tableName = "SliceEntries")
public class SliceEntry {

    public final static double UNINITIALIZED_DOUBLE = -5.0;

    // for QueryBuilder to be able to find the fields
    public static final String START_TIMESTAMP_FIELD_NAME = "start_timestamp";
    public static final String DURATION_FIELD_NAME = "duration";
    public static final String TYPE_FIELD_NAME = "type";
    public static final String SLICE_FILTER_FIELD_NAME = "sliceFilterType";

    @DatabaseField(generatedId = true)
    private long id;

    @DatabaseField(columnName = START_TIMESTAMP_FIELD_NAME, canBeNull = false)
    private Date timestamp;

    @DatabaseField(columnName = DURATION_FIELD_NAME, canBeNull = false)
    private long duration;

//    @DatabaseField(columnName = TYPE_FIELD_NAME, canBeNull = false)
//    private SliceType type;
    @DatabaseField(dataType = DataType.SERIALIZABLE)
    private ArrayList<SliceFilterType> filterTypes = new ArrayList<>();

    public SliceEntry(Date timestamp, long duration) {
        this.timestamp = timestamp;
        this.duration = duration;
//        this.type = type;
    }

    public SliceEntry() {
    }

    public long getId() {
        return id;
    }

    public Date getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Date timestamp) {
        this.timestamp = timestamp;
    }

    public long getDuration() {
        return duration;
    }

    public void setDuration(long duration) {
        this.duration = duration;
    }

//    public SliceType getType() {
//        return type;
//    }
//
//    public void setType(SliceType type) {
//        this.type = type;
//    }
    public List<SliceFilterType> getFilterTypes() {
        return filterTypes;
    }

    public void setFilterType(ArrayList<SliceFilterType> filterTypes) {
        this.filterTypes = filterTypes;
    }

    public void addFilterType(SliceFilterType filterType) {
        this.filterTypes.add(filterType);
    }

}
