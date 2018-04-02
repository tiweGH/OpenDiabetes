/**
 * Copyright (C) 2017 OpenDiabetes
 * <p>
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 * <p>
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * <p>
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package de.opendiabetes.vault.container;

import com.j256.ormlite.field.DataType;
import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;
import static de.opendiabetes.vault.plugin.util.TimestampUtils.copyTimestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * This class defines the SliceEntry data type.
 */
@DatabaseTable(tableName = "SliceEntries")
public class SliceEntry {

    /**
     * Default value for uninitialized doubles.
     */
    public static final double UNINITIALIZED_DOUBLE = -5.0;

    // For the QueryBuilder to be able to find the fields.
    /**
     * Name of the field holding the starting timestamp.
     */
    public static final String START_TIMESTAMP_FIELD_NAME = "start_timestamp";
    /**
     * Name of the filed holding the duration.
     */
    public static final String DURATION_FIELD_NAME = "duration";
    /**
     * Name of the field holding the type.
     */
    public static final String TYPE_FIELD_NAME = "type";
    /**
     * Name of the filed holding the type of the used slice filter.
     */
    public static final String SLICE_FILTER_FIELD_NAME = "sliceFilterType";

    /**
     * The ID of the SliceEntry.
     */
    @DatabaseField(generatedId = true)
    private long id;

    /**
     * The timestamp of the SliceEntry.
     */
    @DatabaseField(columnName = START_TIMESTAMP_FIELD_NAME, canBeNull = false)
    private Date timestamp;

    /**
     * The duration of the SliceEntry.
     */
    @DatabaseField(columnName = DURATION_FIELD_NAME, canBeNull = false)
    private long duration;

//    @DatabaseField(columnName = TYPE_FIELD_NAME, canBeNull = false)
//    private SliceType type;

    /**
     * An ArrayList of SliceFilterType.
     */
    @DatabaseField(dataType = DataType.SERIALIZABLE)
    private ArrayList<SliceFilterType> filterTypes = new ArrayList<>();

    /**
     * The constructor for SliceEntry, setting the timestamp and the duration.
     *
     * @param timestamp The value that timestamp will be set to.
     * @param duration  The value that duration will be set to.
     */
    public SliceEntry(final Date timestamp, final long duration) {
        this.timestamp = copyTimestamp(timestamp);
        this.duration = duration;
//        this.type = type;
    }

    /**
     * Default constructor for SliceEntry.
     */
    public SliceEntry() {
    }

    /**
     * Getter for ID.
     *
     * @return The ID of the SliceEntry.
     */
    public long getId() {
        return id;
    }

    /**
     * Getter for timestamp.
     *
     * @return The timestamp of the SliceEntry.
     */
    public Date getTimestamp() {
        return copyTimestamp(timestamp);
    }

    /**
     * Setter for timestamp.
     *
     * @param timestamp The value that the timestamp will be set to.
     */
    public void setTimestamp(final Date timestamp) {
        this.timestamp = copyTimestamp(timestamp);
    }

    /**
     * Getter for duration.
     *
     * @return The duration of the SliceEntry.
     */
    public long getDuration() {
        return duration;
    }

    /**
     * Setter for duration.
     *
     * @param duration The value that duration will be set to.
     */
    public void setDuration(final long duration) {
        this.duration = duration;
    }

//    public SliceType getType() {
//        return type;
//    }
//
//    public void setType(SliceType type) {
//        this.type = type;
//    }

    /**
     * Getter for filterType.
     *
     * @return The SliceEntry's List of SliceFilterType.
     */
    public List<SliceFilterType> getFilterTypes() {
        return filterTypes;
    }

    /**
     * Setter for filterType.
     *
     * @param filterTypes The value that filterTypes will be set to.
     */
    public void setFilterType(final ArrayList<SliceFilterType> filterTypes) {
        this.filterTypes = filterTypes;
    }

    /**
     * Adds a new SliceFilterType to filterTypes.
     *
     * @param filterType The SliceFilterType that will be added to the ArrayList of SliceFilterType.
     */
    public void addFilterType(final SliceFilterType filterType) {
        this.filterTypes.add(filterType);
    }

}
