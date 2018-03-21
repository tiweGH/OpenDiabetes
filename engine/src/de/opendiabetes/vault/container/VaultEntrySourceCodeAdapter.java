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
package de.opendiabetes.vault.container;

import de.opendiabetes.vault.util.TimestampUtils;

/*
import de.jhit.opendiabetes.vault.util.TimestampUtils;
*/

/**
 * @author juehv
 */
public class VaultEntrySourceCodeAdapter {

    public static final String TIME_FORMAT = "yyyy.MM.dd-HH:mm";
    private final VaultEntry data;

    public VaultEntrySourceCodeAdapter(VaultEntry data) {
        this.data = data;
    }

    public String toListCode() {
        StringBuilder sb = new StringBuilder();

        if (!data.getAnnotations().isEmpty()) {
            sb.append("tmpAnnotations = new ArrayList<>();\n");
            for (VaultEntryAnnotation annotation : data.getAnnotations()) {
                sb.append("tmpAnnotations.add(new VaultEntryAnnotation(VaultEntryAnnotation.TYPE.");
                sb.append(annotation.toString());
                if (!annotation.getValue().isEmpty()) {
                    sb.append(").setValue(\"");
                    sb.append(annotation.getValue());
                }
                sb.append("\"));\n");
            }
        }

        sb.append("vaultEntries.add(new VaultEntry(");
        sb.append("VaultEntryType.").append(data.getType()).append(",");
        sb.append("TimestampUtils.createCleanTimestamp(\"");
        sb.append(TimestampUtils.timestampToString(data.getTimestamp(), TIME_FORMAT));
        sb.append("\",\"").append(TIME_FORMAT).append("\"),");
        sb.append(data.getValue());
        if (data.getValue2() != VaultEntry.VALUE_UNUSED) {
            sb.append(",").append(data.getValue2());
        }
        if (!data.getAnnotations().isEmpty()) {
            sb.append(",").append("tmpAnnotations");
        }
        sb.append("));\n");
        return sb.toString();
    }

    public static String getListInitCode() {
        StringBuilder sb = new StringBuilder();
        sb.append("List<VaultEntry> vaultEntries = new ArrayList<>();\n");
        sb.append("List<VaultEntryAnnotation> tmpAnnotations;\n");
        return sb.toString();
    }

    public static String getReturnStatementCode() {
        StringBuilder sb = new StringBuilder();
        sb.append("return vaultEntries;\n");
        return sb.toString();
    }
    
}
