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

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;
import java.lang.reflect.Type;

/**
 *
 * @author juehv
 */
public class VaultEntryAnnotationGsonAdapter implements JsonSerializer<VaultEntryAnnotation>, JsonDeserializer<VaultEntryAnnotation> {

    @Override
    public JsonElement serialize(VaultEntryAnnotation t, Type type, JsonSerializationContext jsc) {
        JsonObject obj = new JsonObject();
        obj.addProperty("t", t.getType().ordinal());
        obj.addProperty("v", t.getValue());
        return obj;
    }

    @Override
    public VaultEntryAnnotation deserialize(JsonElement je, Type type, JsonDeserializationContext jdc) throws JsonParseException {
        VaultEntryAnnotation.TYPE veType = VaultEntryAnnotation.TYPE.values()[je.getAsJsonObject().get("t").getAsInt()];
        String weValue = je.getAsJsonObject().get("v").getAsString();
        return new VaultEntryAnnotation(weValue, veType);
    }

}
