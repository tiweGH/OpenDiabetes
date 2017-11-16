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
package de.jhit.opendiabetes.vault.container;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import com.j256.ormlite.field.DataType;
import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Objects;

/**
 *
 * @author mswin
 */
@DatabaseTable(tableName = "VaultEntries")
public class VaultEntry implements Serializable {

    public static final double VALUE_UNUSED = -5.0;
    public static final long ID_UNUSED = -5L;
    private final Gson gson;

    // for QueryBuilder to be able to find the fields
    public static final String ID_FIELD_NAME = "id";
    public static final String TYPE_FIELD_NAME = "type";
    public static final String TIMESTAMP_FIELD_NAME = "timestamp";
    public static final String VALUE_FIELD_NAME = "value";
    public static final String VALUE2_FIELD_NAME = "value2";
    public static final String RAW_ID_FIELD_NAME = "rawId";
    public static final String ANNOTATION_FIELD_NAME = "annotation";

    @DatabaseField(columnName = ID_FIELD_NAME, generatedId = true)
    private long id;

    @DatabaseField(columnName = TYPE_FIELD_NAME, canBeNull = false, dataType = DataType.ENUM_INTEGER)
    private VaultEntryType type;

    @DatabaseField(columnName = TIMESTAMP_FIELD_NAME, canBeNull = false)
    private Date timestamp;

    @DatabaseField(columnName = VALUE_FIELD_NAME, canBeNull = false)
    private double value;

    @DatabaseField(columnName = VALUE2_FIELD_NAME, canBeNull = false)
    private double value2 = VALUE_UNUSED;

    @DatabaseField(columnName = RAW_ID_FIELD_NAME, canBeNull = false)
    private long rawId = ID_UNUSED;

    private List<VaultEntryAnnotation> annotations = new ArrayList<>();

    @DatabaseField(dataType = DataType.LONG_STRING, columnName = ANNOTATION_FIELD_NAME)
    private String annotationsAsJson = "";

    public VaultEntry() {
        // all persisted classes must define a no-arg constructor with at least package visibility
        GsonBuilder gb = new GsonBuilder();
        gb.registerTypeAdapter(VaultEntryAnnotation.class, new VaultEntryAnnotationGsonAdapter());
        gson = gb.create();
    }

    public VaultEntry(VaultEntryType type, Date timestamp, double value) {
        this();
        this.type = type;
        this.timestamp = timestamp;
        this.value = value;
    }

    public VaultEntry(VaultEntryType type, Date timestamp) {
        this();
        this.type = type;
        this.timestamp = timestamp;
        this.value = VALUE_UNUSED;
    }

    public VaultEntry(VaultEntryType type, Date timestamp, double value, double value2) {
        this(type, timestamp, value);
        this.value2 = value2;
    }

    public VaultEntry(VaultEntryType type, Date timestamp, double value, List<VaultEntryAnnotation> annotations) {
        this(type, timestamp, value);
        this.annotations = annotations;
    }

    public VaultEntry(VaultEntryType type, Date timestamp, double value, double value2, List<VaultEntryAnnotation> annotations) {
        this(type, timestamp, value);
        this.value2 = value2;
        this.annotations = annotations;
    }

    public VaultEntry(VaultEntry copy) {
        this();
        this.type = copy.type;
        this.timestamp = copy.timestamp;
        this.value = copy.value;
        this.value2 = copy.value2;
        this.annotations = copy.annotations;
    }

    public long getId() {
        return id;
    }

    public VaultEntryType getType() {
        return type;
    }

    public void setType(VaultEntryType type) {
        this.type = type;
    }

    public Date getTimestamp() {
        return timestamp;
    }

    public double getValue() {
        return value;
    }

    public void setValue(double value) {
        this.value = value;
    }

    public long getRawId() {
        return rawId;
    }

    public void setRawId(long rawId) {
        this.rawId = rawId;
    }

    public double getValue2() {
        return value2;
    }

    public void setValue2(double value2) {
        this.value2 = value2;
    }

    public List<VaultEntryAnnotation> getAnnotations() {
        annotationsFromJason();
        return annotations;
    }

    public void addAnnotation(VaultEntryAnnotation annotation) {
        this.annotations.add(annotation);
        annotationsToJson();
    }

    public void setAnnotation(ArrayList<VaultEntryAnnotation> annotations) {
        this.annotations = annotations;
        annotationsToJson();
    }

    public String getAnnotationsAsJson() {
        return annotationsAsJson;
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 67 * hash + Objects.hashCode(this.type);
        hash = 67 * hash + Objects.hashCode(this.timestamp);
        hash = 67 * hash + (int) (Double.doubleToLongBits(this.value) ^ (Double.doubleToLongBits(this.value) >>> 32));
        hash = 67 * hash + (int) (Double.doubleToLongBits(this.value2) ^ (Double.doubleToLongBits(this.value2) >>> 32));
        hash = 67 * hash + (int) (this.rawId ^ (this.rawId >>> 32));
        hash = 67 * hash + Objects.hashCode(this.annotations);
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final VaultEntry other = (VaultEntry) obj;
        if (!Objects.equals(this.type, other.type)) {
            return false;
        }
        if (!Objects.equals(this.timestamp, other.timestamp)) {
            return false;
        }
        if (!Objects.equals(this.value, other.value)) {
            return false;
        }
        if (!Objects.equals(this.value2, other.value2)) {
            return false;
        }
        if (!Objects.equals(this.annotations, other.annotations)) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "VaultEntry{" + "id=" + id + ", type=" + type + ", timestamp=" + timestamp + ", value=" + value + ", value2=" + value2 + ", rawId=" + rawId + ", annotaion=" + annotations + '}';
    }

    private void annotationsToJson() {
        annotationsAsJson = gson.toJson(annotations);
    }

    private void annotationsFromJason() {
        if (!annotationsAsJson.isEmpty()) {
            annotations = gson.fromJson(annotationsAsJson,
                    new TypeToken<List<VaultEntryAnnotation>>() {
            }.getType());
        }
        annotationsAsJson = "";
    }

    public void setAnnotaionFromJson(String asString) {
        if (asString != null && !asString.isEmpty()) {
            this.annotationsAsJson = asString;
            annotationsFromJason();
        }
    }

}
