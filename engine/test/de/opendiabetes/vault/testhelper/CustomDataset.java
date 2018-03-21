/*
 * Copyright (C) 2017 gizem
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
package de.opendiabetes.vault.testhelper;

import de.opendiabetes.vault.container.VaultEntry;
import de.opendiabetes.vault.container.VaultEntryAnnotation;
import de.opendiabetes.vault.container.VaultEntryType;
import de.opendiabetes.vault.util.TimestampUtils;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author gizem
 */
public class CustomDataset {

    public static List<VaultEntry> getCustomDataset() throws ParseException {
        List<VaultEntry> vaultEntries = new ArrayList<>();
        List<VaultEntryAnnotation> tmpAnnotations;

        vaultEntries.add(new VaultEntry(VaultEntryType.GLUCOSE_BG_MANUAL, TimestampUtils.createCleanTimestamp("2016.04.18-06:48", "yyyy.MM.dd-HH:mm"), 288));
        vaultEntries.add(new VaultEntry(VaultEntryType.BOLUS_NORMAL, TimestampUtils.createCleanTimestamp("2016.04.18-06:48", "yyyy.MM.dd-HH:mm"), 6.2));
        tmpAnnotations = new ArrayList<>();
        tmpAnnotations.add(new VaultEntryAnnotation(VaultEntryAnnotation.TYPE.GLUCOSE_BG_METER_SERIAL).setValue("BG1140084B"));
        vaultEntries.add(new VaultEntry(VaultEntryType.GLUCOSE_CGM, TimestampUtils.createCleanTimestamp("2016.04.18-06:58", "yyyy.MM.dd-HH:mm"), 322));
        vaultEntries.add(new VaultEntry(VaultEntryType.BASAL_PROFILE, TimestampUtils.createCleanTimestamp("2016.04.18-07:00", "yyyy.MM.dd-HH:mm"), 1.15));
        vaultEntries.add(new VaultEntry(VaultEntryType.GLUCOSE_CGM, TimestampUtils.createCleanTimestamp("2016.04.18-07:13", "yyyy.MM.dd-HH:mm"), 297));
        vaultEntries.add(new VaultEntry(VaultEntryType.GLUCOSE_CGM, TimestampUtils.createCleanTimestamp("2016.04.18-07:28", "yyyy.MM.dd-HH:mm"), 253));
        vaultEntries.add(new VaultEntry(VaultEntryType.GLUCOSE_CGM, TimestampUtils.createCleanTimestamp("2016.04.18-07:43", "yyyy.MM.dd-HH:mm"), 226));
        vaultEntries.add(new VaultEntry(VaultEntryType.GLUCOSE_CGM, TimestampUtils.createCleanTimestamp("2016.04.18-07:58", "yyyy.MM.dd-HH:mm"), 219));
        vaultEntries.add(new VaultEntry(VaultEntryType.BASAL_PROFILE, TimestampUtils.createCleanTimestamp("2016.04.18-08:00", "yyyy.MM.dd-HH:mm"), 1.25));
        vaultEntries.add(new VaultEntry(VaultEntryType.GLUCOSE_CGM_ALERT, TimestampUtils.createCleanTimestamp("2016.04.18-08:03", "yyyy.MM.dd-HH:mm"), 213));
        vaultEntries.add(new VaultEntry(VaultEntryType.GLUCOSE_BG_MANUAL, TimestampUtils.createCleanTimestamp("2016.04.18-08:05", "yyyy.MM.dd-HH:mm"), 152));
        vaultEntries.add(new VaultEntry(VaultEntryType.GLUCOSE_CGM, TimestampUtils.createCleanTimestamp("2016.04.18-08:13", "yyyy.MM.dd-HH:mm"), 206));
        vaultEntries.add(new VaultEntry(VaultEntryType.GLUCOSE_CGM, TimestampUtils.createCleanTimestamp("2016.04.18-08:28", "yyyy.MM.dd-HH:mm"), 193));
        vaultEntries.add(new VaultEntry(VaultEntryType.GLUCOSE_CGM, TimestampUtils.createCleanTimestamp("2016.04.18-08:43", "yyyy.MM.dd-HH:mm"), 180));
        vaultEntries.add(new VaultEntry(VaultEntryType.GLUCOSE_CGM, TimestampUtils.createCleanTimestamp("2016.04.18-08:58", "yyyy.MM.dd-HH:mm"), 168));
        vaultEntries.add(new VaultEntry(VaultEntryType.BASAL_PROFILE, TimestampUtils.createCleanTimestamp("2016.04.18-09:00", "yyyy.MM.dd-HH:mm"), 1.3));
        vaultEntries.add(new VaultEntry(VaultEntryType.GLUCOSE_CGM, TimestampUtils.createCleanTimestamp("2016.04.18-09:13", "yyyy.MM.dd-HH:mm"), 156));
        vaultEntries.add(new VaultEntry(VaultEntryType.GLUCOSE_CGM, TimestampUtils.createCleanTimestamp("2016.04.18-09:29", "yyyy.MM.dd-HH:mm"), 141));
        vaultEntries.add(new VaultEntry(VaultEntryType.GLUCOSE_CGM_ALERT, TimestampUtils.createCleanTimestamp("2016.04.18-09:31", "yyyy.MM.dd-HH:mm"), 145));
        vaultEntries.add(new VaultEntry(VaultEntryType.BOLUS_NORMAL, TimestampUtils.createCleanTimestamp("2016.04.18-09:33", "yyyy.MM.dd-HH:mm"), 2));
        vaultEntries.add(new VaultEntry(VaultEntryType.GLUCOSE_CGM, TimestampUtils.createCleanTimestamp("2016.04.18-09:44", "yyyy.MM.dd-HH:mm"), 125));
        vaultEntries.add(new VaultEntry(VaultEntryType.GLUCOSE_CGM_ALERT, TimestampUtils.createCleanTimestamp("2016.04.18-09:44", "yyyy.MM.dd-HH:mm"), 126));
        vaultEntries.add(new VaultEntry(VaultEntryType.BOLUS_NORMAL, TimestampUtils.createCleanTimestamp("2016.04.18-09:45", "yyyy.MM.dd-HH:mm"), 5));
        vaultEntries.add(new VaultEntry(VaultEntryType.GLUCOSE_CGM, TimestampUtils.createCleanTimestamp("2016.04.18-09:59", "yyyy.MM.dd-HH:mm"), 118));
        vaultEntries.add(new VaultEntry(VaultEntryType.BASAL_PROFILE, TimestampUtils.createCleanTimestamp("2016.04.18-10:00", "yyyy.MM.dd-HH:mm"), 1.1));
        vaultEntries.add(new VaultEntry(VaultEntryType.BOLUS_NORMAL, TimestampUtils.createCleanTimestamp("2016.04.18-10:01", "yyyy.MM.dd-HH:mm"), 2));
        vaultEntries.add(new VaultEntry(VaultEntryType.GLUCOSE_CGM, TimestampUtils.createCleanTimestamp("2016.04.18-10:14", "yyyy.MM.dd-HH:mm"), 138));
        vaultEntries.add(new VaultEntry(VaultEntryType.GLUCOSE_CGM_ALERT, TimestampUtils.createCleanTimestamp("2016.04.18-10:26", "yyyy.MM.dd-HH:mm"), 148));
        vaultEntries.add(new VaultEntry(VaultEntryType.GLUCOSE_CGM, TimestampUtils.createCleanTimestamp("2016.04.18-10:29", "yyyy.MM.dd-HH:mm"), 110));
        vaultEntries.add(new VaultEntry(VaultEntryType.GLUCOSE_CGM, TimestampUtils.createCleanTimestamp("2016.04.18-10:43", "yyyy.MM.dd-HH:mm"), 105));
        vaultEntries.add(new VaultEntry(VaultEntryType.GLUCOSE_CGM_ALERT, TimestampUtils.createCleanTimestamp("2016.04.18-10:52", "yyyy.MM.dd-HH:mm"), 169));
        vaultEntries.add(new VaultEntry(VaultEntryType.GLUCOSE_CGM, TimestampUtils.createCleanTimestamp("2016.04.18-10:59", "yyyy.MM.dd-HH:mm"), 100));
        vaultEntries.add(new VaultEntry(VaultEntryType.BASAL_PROFILE, TimestampUtils.createCleanTimestamp("2016.04.18-11:00", "yyyy.MM.dd-HH:mm"), 1.1));
        vaultEntries.add(new VaultEntry(VaultEntryType.GLUCOSE_ELEVATION_30, TimestampUtils.createCleanTimestamp("2016.04.18-11:01", "yyyy.MM.dd-HH:mm"), 70));
        vaultEntries.add(new VaultEntry(VaultEntryType.GLUCOSE_CGM_ALERT, TimestampUtils.createCleanTimestamp("2016.04.18-11:01", "yyyy.MM.dd-HH:mm"), 170));
        vaultEntries.add(new VaultEntry(VaultEntryType.GLUCOSE_CGM_ALERT, TimestampUtils.createCleanTimestamp("2016.04.18-10:13", "yyyy.MM.dd-HH:mm"), 182));
        vaultEntries.add(new VaultEntry(VaultEntryType.GLUCOSE_CGM, TimestampUtils.createCleanTimestamp("2016.04.18-11:14", "yyyy.MM.dd-HH:mm"), 120));
        vaultEntries.add(new VaultEntry(VaultEntryType.GLUCOSE_CGM, TimestampUtils.createCleanTimestamp("2016.04.18-11:29", "yyyy.MM.dd-HH:mm"), 103));
        vaultEntries.add(new VaultEntry(VaultEntryType.GLUCOSE_CGM_ALERT, TimestampUtils.createCleanTimestamp("2016.04.18-11:39", "yyyy.MM.dd-HH:mm"), 192));
        vaultEntries.add(new VaultEntry(VaultEntryType.BOLUS_NORMAL, TimestampUtils.createCleanTimestamp("2016.04.18-11:40", "yyyy.MM.dd-HH:mm"), 0.5));
        //vaultEntries.add(new VaultEntry(VaultEntryType.STRESS, TimestampUtils.createCleanTimestamp("2016.04.18-11:40", "yyyy.MM.dd-HH:mm"), 129));
        vaultEntries.add(new VaultEntry(VaultEntryType.GLUCOSE_CGM, TimestampUtils.createCleanTimestamp("2016.04.18-11:44", "yyyy.MM.dd-HH:mm"), 198));

        vaultEntries.add(new VaultEntry(VaultEntryType.GLUCOSE_CGM_ALERT, TimestampUtils.createCleanTimestamp("2016.04.19-11:01", "yyyy.MM.dd-HH:mm"), 170));
        vaultEntries.add(new VaultEntry(VaultEntryType.GLUCOSE_CGM_ALERT, TimestampUtils.createCleanTimestamp("2016.04.19-10:13", "yyyy.MM.dd-HH:mm"), 182));
        vaultEntries.add(new VaultEntry(VaultEntryType.BOLUS_NORMAL, TimestampUtils.createCleanTimestamp("2016.04.19-11:14", "yyyy.MM.dd-HH:mm"), 120));
        vaultEntries.add(new VaultEntry(VaultEntryType.GLUCOSE_CGM, TimestampUtils.createCleanTimestamp("2016.04.19-11:29", "yyyy.MM.dd-HH:mm"), 103));
        vaultEntries.add(new VaultEntry(VaultEntryType.GLUCOSE_CGM_ALERT, TimestampUtils.createCleanTimestamp("2016.04.19-11:39", "yyyy.MM.dd-HH:mm"), 192));
        vaultEntries.add(new VaultEntry(VaultEntryType.BOLUS_NORMAL, TimestampUtils.createCleanTimestamp("2016.04.19-11:40", "yyyy.MM.dd-HH:mm"), 0.5));
        vaultEntries.add(new VaultEntry(VaultEntryType.STRESS, TimestampUtils.createCleanTimestamp("2016.04.19-11:40", "yyyy.MM.dd-HH:mm"), 129));
        vaultEntries.add(new VaultEntry(VaultEntryType.GLUCOSE_CGM, TimestampUtils.createCleanTimestamp("2016.04.19-11:44", "yyyy.MM.dd-HH:mm"), 198));
        return vaultEntries;
    }

}
