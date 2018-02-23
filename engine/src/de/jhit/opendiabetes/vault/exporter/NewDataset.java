/*
 * Copyright (C) 2017 a.a.aponte
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
package de.jhit.opendiabetes.vault.exporter;

import de.jhit.opendiabetes.vault.container.VaultEntry;
import de.jhit.opendiabetes.vault.container.VaultEntryAnnotation;
import de.jhit.opendiabetes.vault.container.VaultEntryType;
import de.jhit.opendiabetes.vault.util.TimestampUtils;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author a.a.aponte
 */
public class NewDataset {
    
    public static List<VaultEntry> getNewDataset() throws ParseException {
        List<VaultEntry> vaultEntries = new ArrayList<>();
        List<VaultEntryAnnotation> tmpAnnotations;
        
        // 2016
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
        vaultEntries.add(new VaultEntry(VaultEntryType.GLUCOSE_CGM_ALERT, TimestampUtils.createCleanTimestamp("2016.04.18-11:01", "yyyy.MM.dd-HH:mm"), 170));
        vaultEntries.add(new VaultEntry(VaultEntryType.GLUCOSE_CGM_ALERT, TimestampUtils.createCleanTimestamp("2016.04.18-10:13", "yyyy.MM.dd-HH:mm"), 182));
        vaultEntries.add(new VaultEntry(VaultEntryType.GLUCOSE_CGM, TimestampUtils.createCleanTimestamp("2016.04.18-11:14", "yyyy.MM.dd-HH:mm"), 120));
        vaultEntries.add(new VaultEntry(VaultEntryType.GLUCOSE_CGM, TimestampUtils.createCleanTimestamp("2016.04.18-11:29", "yyyy.MM.dd-HH:mm"), 103));
        vaultEntries.add(new VaultEntry(VaultEntryType.GLUCOSE_CGM_ALERT, TimestampUtils.createCleanTimestamp("2016.04.18-11:39", "yyyy.MM.dd-HH:mm"), 192));
        vaultEntries.add(new VaultEntry(VaultEntryType.BOLUS_NORMAL, TimestampUtils.createCleanTimestamp("2016.04.18-11:40", "yyyy.MM.dd-HH:mm"), 0.5));
        vaultEntries.add(new VaultEntry(VaultEntryType.GLUCOSE_BG, TimestampUtils.createCleanTimestamp("2016.04.18-11:40", "yyyy.MM.dd-HH:mm"), 129));
        vaultEntries.add(new VaultEntry(VaultEntryType.GLUCOSE_CGM, TimestampUtils.createCleanTimestamp("2016.04.18-11:44", "yyyy.MM.dd-HH:mm"), 198));

        // 2017
        vaultEntries.add(new VaultEntry(VaultEntryType.STRESS, TimestampUtils.createCleanTimestamp("2017.06.29-04:46", "yyyy.MM.dd-HH:mm"), 21.5));
        tmpAnnotations = new ArrayList<>();
        tmpAnnotations.add(new VaultEntryAnnotation(VaultEntryAnnotation.TYPE.GLUCOSE_BG_METER_SERIAL).setValue("BG1140084B"));
        vaultEntries.add(new VaultEntry(VaultEntryType.GLUCOSE_BG, TimestampUtils.createCleanTimestamp("2017.06.29-04:53", "yyyy.MM.dd-HH:mm"), 109.0, tmpAnnotations));
        vaultEntries.add(new VaultEntry(VaultEntryType.STRESS, TimestampUtils.createCleanTimestamp("2017.06.29-04:56", "yyyy.MM.dd-HH:mm"), 36.25));
        vaultEntries.add(new VaultEntry(VaultEntryType.HEART_RATE, TimestampUtils.createCleanTimestamp("2017.06.29-04:56", "yyyy.MM.dd-HH:mm"), 72.0));
        vaultEntries.add(new VaultEntry(VaultEntryType.HEART_RATE_VARIABILITY, TimestampUtils.createCleanTimestamp("2017.06.29-04:56", "yyyy.MM.dd-HH:mm"), 50.0, 85.0));
        vaultEntries.add(new VaultEntry(VaultEntryType.SLEEP_LIGHT, TimestampUtils.createCleanTimestamp("2017.06.29-04:58", "yyyy.MM.dd-HH:mm"), 24.0));
        vaultEntries.add(new VaultEntry(VaultEntryType.BASAL_PROFILE, TimestampUtils.createCleanTimestamp("2017.06.29-05:00", "yyyy.MM.dd-HH:mm"), 1.05));
        vaultEntries.add(new VaultEntry(VaultEntryType.HEART_RATE, TimestampUtils.createCleanTimestamp("2017.06.29-05:06", "yyyy.MM.dd-HH:mm"), 59.0));
        vaultEntries.add(new VaultEntry(VaultEntryType.STRESS, TimestampUtils.createCleanTimestamp("2017.06.29-05:06", "yyyy.MM.dd-HH:mm"), 23.75));
        vaultEntries.add(new VaultEntry(VaultEntryType.HEART_RATE_VARIABILITY, TimestampUtils.createCleanTimestamp("2017.06.29-05:06", "yyyy.MM.dd-HH:mm"), 50.0, 105.0));
        vaultEntries.add(new VaultEntry(VaultEntryType.HEART_RATE, TimestampUtils.createCleanTimestamp("2017.06.29-05:16", "yyyy.MM.dd-HH:mm"), 53.0));
        vaultEntries.add(new VaultEntry(VaultEntryType.HEART_RATE_VARIABILITY, TimestampUtils.createCleanTimestamp("2017.06.29-05:16", "yyyy.MM.dd-HH:mm"), 50.0, 123.0));
        vaultEntries.add(new VaultEntry(VaultEntryType.STRESS, TimestampUtils.createCleanTimestamp("2017.06.29-05:16", "yyyy.MM.dd-HH:mm"), 19.25));
        vaultEntries.add(new VaultEntry(VaultEntryType.SLEEP_LIGHT, TimestampUtils.createCleanTimestamp("2017.06.29-05:22", "yyyy.MM.dd-HH:mm"), 9.0));
        vaultEntries.add(new VaultEntry(VaultEntryType.HEART_RATE_VARIABILITY, TimestampUtils.createCleanTimestamp("2017.06.29-05:26", "yyyy.MM.dd-HH:mm"), 50.0, 113.0));
        vaultEntries.add(new VaultEntry(VaultEntryType.STRESS, TimestampUtils.createCleanTimestamp("2017.06.29-05:26", "yyyy.MM.dd-HH:mm"), 21.75));
        vaultEntries.add(new VaultEntry(VaultEntryType.HEART_RATE, TimestampUtils.createCleanTimestamp("2017.06.29-05:26", "yyyy.MM.dd-HH:mm"), 58.0));
        vaultEntries.add(new VaultEntry(VaultEntryType.SLEEP_DEEP, TimestampUtils.createCleanTimestamp("2017.06.29-05:31", "yyyy.MM.dd-HH:mm"), 10.0));
        vaultEntries.add(new VaultEntry(VaultEntryType.HEART_RATE, TimestampUtils.createCleanTimestamp("2017.06.29-05:36", "yyyy.MM.dd-HH:mm"), 59.0));
        vaultEntries.add(new VaultEntry(VaultEntryType.SLEEP_LIGHT, TimestampUtils.createCleanTimestamp("2017.06.29-05:41", "yyyy.MM.dd-HH:mm"), 22.0));
        vaultEntries.add(new VaultEntry(VaultEntryType.HEART_RATE, TimestampUtils.createCleanTimestamp("2017.06.29-05:46", "yyyy.MM.dd-HH:mm"), 57.0));
        vaultEntries.add(new VaultEntry(VaultEntryType.HEART_RATE, TimestampUtils.createCleanTimestamp("2017.06.29-05:56", "yyyy.MM.dd-HH:mm"), 56.0));
        vaultEntries.add(new VaultEntry(VaultEntryType.BASAL_PROFILE, TimestampUtils.createCleanTimestamp("2017.06.29-06:00", "yyyy.MM.dd-HH:mm"), 1.05));
        vaultEntries.add(new VaultEntry(VaultEntryType.SLEEP_DEEP, TimestampUtils.createCleanTimestamp("2017.06.29-06:03", "yyyy.MM.dd-HH:mm"), 10.0));
        vaultEntries.add(new VaultEntry(VaultEntryType.HEART_RATE, TimestampUtils.createCleanTimestamp("2017.06.29-06:06", "yyyy.MM.dd-HH:mm"), 71.0));
        vaultEntries.add(new VaultEntry(VaultEntryType.HEART_RATE_VARIABILITY, TimestampUtils.createCleanTimestamp("2017.06.29-06:06", "yyyy.MM.dd-HH:mm"), 50.0, 86.0));
        vaultEntries.add(new VaultEntry(VaultEntryType.STRESS, TimestampUtils.createCleanTimestamp("2017.06.29-06:06", "yyyy.MM.dd-HH:mm"), 35.5));
        vaultEntries.add(new VaultEntry(VaultEntryType.SLEEP_LIGHT, TimestampUtils.createCleanTimestamp("2017.06.29-06:13", "yyyy.MM.dd-HH:mm"), 9.0));
        vaultEntries.add(new VaultEntry(VaultEntryType.HEART_RATE, TimestampUtils.createCleanTimestamp("2017.06.29-06:16", "yyyy.MM.dd-HH:mm"), 66.0));
        vaultEntries.add(new VaultEntry(VaultEntryType.HEART_RATE_VARIABILITY, TimestampUtils.createCleanTimestamp("2017.06.29-06:16", "yyyy.MM.dd-HH:mm"), 50.0, 93.0));
        vaultEntries.add(new VaultEntry(VaultEntryType.STRESS, TimestampUtils.createCleanTimestamp("2017.06.29-06:16", "yyyy.MM.dd-HH:mm"), 30.25));
        vaultEntries.add(new VaultEntry(VaultEntryType.SLEEP_LIGHT, TimestampUtils.createCleanTimestamp("2017.06.29-06:22", "yyyy.MM.dd-HH:mm"), 7.0));
        vaultEntries.add(new VaultEntry(VaultEntryType.STRESS, TimestampUtils.createCleanTimestamp("2017.06.29-06:26", "yyyy.MM.dd-HH:mm"), 27.25));
        vaultEntries.add(new VaultEntry(VaultEntryType.HEART_RATE_VARIABILITY, TimestampUtils.createCleanTimestamp("2017.06.29-06:26", "yyyy.MM.dd-HH:mm"), 50.0, 97.0));
        vaultEntries.add(new VaultEntry(VaultEntryType.HEART_RATE, TimestampUtils.createCleanTimestamp("2017.06.29-06:26", "yyyy.MM.dd-HH:mm"), 62.0));
        vaultEntries.add(new VaultEntry(VaultEntryType.SLEEP_DEEP, TimestampUtils.createCleanTimestamp("2017.06.29-06:29", "yyyy.MM.dd-HH:mm"), 11.0));
        vaultEntries.add(new VaultEntry(VaultEntryType.HEART_RATE, TimestampUtils.createCleanTimestamp("2017.06.29-06:36", "yyyy.MM.dd-HH:mm"), 63.0));
        vaultEntries.add(new VaultEntry(VaultEntryType.HEART_RATE_VARIABILITY, TimestampUtils.createCleanTimestamp("2017.06.29-06:36", "yyyy.MM.dd-HH:mm"), 50.0, 98.0));
        vaultEntries.add(new VaultEntry(VaultEntryType.STRESS, TimestampUtils.createCleanTimestamp("2017.06.29-06:36", "yyyy.MM.dd-HH:mm"), 26.5));
        vaultEntries.add(new VaultEntry(VaultEntryType.SLEEP_LIGHT, TimestampUtils.createCleanTimestamp("2017.06.29-06:40", "yyyy.MM.dd-HH:mm"), 15.0));
        vaultEntries.add(new VaultEntry(VaultEntryType.HEART_RATE, TimestampUtils.createCleanTimestamp("2017.06.29-06:46", "yyyy.MM.dd-HH:mm"), 58.0));
        vaultEntries.add(new VaultEntry(VaultEntryType.HEART_RATE_VARIABILITY, TimestampUtils.createCleanTimestamp("2017.06.29-06:46", "yyyy.MM.dd-HH:mm"), 50.0, 97.0));
        vaultEntries.add(new VaultEntry(VaultEntryType.STRESS, TimestampUtils.createCleanTimestamp("2017.06.29-06:46", "yyyy.MM.dd-HH:mm"), 27.25));
        vaultEntries.add(new VaultEntry(VaultEntryType.SLEEP_DEEP, TimestampUtils.createCleanTimestamp("2017.06.29-06:55", "yyyy.MM.dd-HH:mm"), 27.0));
        vaultEntries.add(new VaultEntry(VaultEntryType.HEART_RATE, TimestampUtils.createCleanTimestamp("2017.06.29-06:56", "yyyy.MM.dd-HH:mm"), 59.0));
        vaultEntries.add(new VaultEntry(VaultEntryType.BASAL_PROFILE, TimestampUtils.createCleanTimestamp("2017.06.29-07:00", "yyyy.MM.dd-HH:mm"), 1.15));
        vaultEntries.add(new VaultEntry(VaultEntryType.HEART_RATE, TimestampUtils.createCleanTimestamp("2017.06.29-07:06", "yyyy.MM.dd-HH:mm"), 56.0));
        vaultEntries.add(new VaultEntry(VaultEntryType.HEART_RATE, TimestampUtils.createCleanTimestamp("2017.06.29-07:16", "yyyy.MM.dd-HH:mm"), 57.0));
        vaultEntries.add(new VaultEntry(VaultEntryType.SLEEP_LIGHT, TimestampUtils.createCleanTimestamp("2017.06.29-07:22", "yyyy.MM.dd-HH:mm"), 1.0));
        vaultEntries.add(new VaultEntry(VaultEntryType.STRESS, TimestampUtils.createCleanTimestamp("2017.06.29-07:27", "yyyy.MM.dd-HH:mm"), 25.75));
        vaultEntries.add(new VaultEntry(VaultEntryType.HEART_RATE_VARIABILITY, TimestampUtils.createCleanTimestamp("2017.06.29-07:27", "yyyy.MM.dd-HH:mm"), 50.0, 99.0));
        vaultEntries.add(new VaultEntry(VaultEntryType.HEART_RATE, TimestampUtils.createCleanTimestamp("2017.06.29-07:27", "yyyy.MM.dd-HH:mm"), 58.0));
        vaultEntries.add(new VaultEntry(VaultEntryType.HEART_RATE, TimestampUtils.createCleanTimestamp("2017.06.29-07:36", "yyyy.MM.dd-HH:mm"), 68.0));
        vaultEntries.add(new VaultEntry(VaultEntryType.SLEEP_LIGHT, TimestampUtils.createCleanTimestamp("2017.06.29-07:37", "yyyy.MM.dd-HH:mm"), 43.0));
        vaultEntries.add(new VaultEntry(VaultEntryType.STRESS, TimestampUtils.createCleanTimestamp("2017.06.29-07:45", "yyyy.MM.dd-HH:mm"), 30.25));
        vaultEntries.add(new VaultEntry(VaultEntryType.HEART_RATE_VARIABILITY, TimestampUtils.createCleanTimestamp("2017.06.29-07:45", "yyyy.MM.dd-HH:mm"), 50.0, 93.0));
        vaultEntries.add(new VaultEntry(VaultEntryType.HEART_RATE, TimestampUtils.createCleanTimestamp("2017.06.29-07:46", "yyyy.MM.dd-HH:mm"), 62.0));
        vaultEntries.add(new VaultEntry(VaultEntryType.HEART_RATE_VARIABILITY, TimestampUtils.createCleanTimestamp("2017.06.29-07:56", "yyyy.MM.dd-HH:mm"), 50.0, 96.0));
        vaultEntries.add(new VaultEntry(VaultEntryType.STRESS, TimestampUtils.createCleanTimestamp("2017.06.29-07:56", "yyyy.MM.dd-HH:mm"), 28.0));
        vaultEntries.add(new VaultEntry(VaultEntryType.HEART_RATE, TimestampUtils.createCleanTimestamp("2017.06.29-07:56", "yyyy.MM.dd-HH:mm"), 63.0));
        vaultEntries.add(new VaultEntry(VaultEntryType.BASAL_PROFILE, TimestampUtils.createCleanTimestamp("2017.06.29-08:00", "yyyy.MM.dd-HH:mm"), 1.25));
        vaultEntries.add(new VaultEntry(VaultEntryType.STRESS, TimestampUtils.createCleanTimestamp("2017.06.29-08:06", "yyyy.MM.dd-HH:mm"), 27.25));
        vaultEntries.add(new VaultEntry(VaultEntryType.HEART_RATE_VARIABILITY, TimestampUtils.createCleanTimestamp("2017.06.29-08:06", "yyyy.MM.dd-HH:mm"), 50.0, 97.0));
        vaultEntries.add(new VaultEntry(VaultEntryType.HEART_RATE, TimestampUtils.createCleanTimestamp("2017.06.29-08:06", "yyyy.MM.dd-HH:mm"), 62.0));
        vaultEntries.add(new VaultEntry(VaultEntryType.STRESS, TimestampUtils.createCleanTimestamp("2017.06.29-08:16", "yyyy.MM.dd-HH:mm"), 0.0));
        tmpAnnotations = new ArrayList<>();
        tmpAnnotations.add(new VaultEntryAnnotation(VaultEntryAnnotation.TYPE.GLUCOSE_BG_METER_SERIAL).setValue("BG1140084B"));
        vaultEntries.add(new VaultEntry(VaultEntryType.GLUCOSE_BG, TimestampUtils.createCleanTimestamp("2017.06.29-08:39", "yyyy.MM.dd-HH:mm"), 181.0, tmpAnnotations));
        vaultEntries.add(new VaultEntry(VaultEntryType.GLUCOSE_BOLUS_CALCULATION, TimestampUtils.createCleanTimestamp("2017.06.29-08:40", "yyyy.MM.dd-HH:mm"), 181.0));
        vaultEntries.add(new VaultEntry(VaultEntryType.BOLUS_NORMAL, TimestampUtils.createCleanTimestamp("2017.06.29-08:42", "yyyy.MM.dd-HH:mm"), 3.2));
        vaultEntries.add(new VaultEntry(VaultEntryType.BASAL_PROFILE, TimestampUtils.createCleanTimestamp("2017.06.29-09:00", "yyyy.MM.dd-HH:mm"), 1.3));
        vaultEntries.add(new VaultEntry(VaultEntryType.BASAL_PROFILE, TimestampUtils.createCleanTimestamp("2017.06.29-10:00", "yyyy.MM.dd-HH:mm"), 1.1));
        vaultEntries.add(new VaultEntry(VaultEntryType.HEART_RATE, TimestampUtils.createCleanTimestamp("2017.06.29-10:30", "yyyy.MM.dd-HH:mm"), 65.0));
        vaultEntries.add(new VaultEntry(VaultEntryType.HEART_RATE, TimestampUtils.createCleanTimestamp("2017.06.29-10:41", "yyyy.MM.dd-HH:mm"), 66.0));
        vaultEntries.add(new VaultEntry(VaultEntryType.STRESS, TimestampUtils.createCleanTimestamp("2017.06.29-10:50", "yyyy.MM.dd-HH:mm"), 52.75));
        vaultEntries.add(new VaultEntry(VaultEntryType.HEART_RATE_VARIABILITY, TimestampUtils.createCleanTimestamp("2017.06.29-10:50", "yyyy.MM.dd-HH:mm"), 50.0, 63.0));
        vaultEntries.add(new VaultEntry(VaultEntryType.HEART_RATE, TimestampUtils.createCleanTimestamp("2017.06.29-10:50", "yyyy.MM.dd-HH:mm"), 85.0));
        vaultEntries.add(new VaultEntry(VaultEntryType.STRESS, TimestampUtils.createCleanTimestamp("2017.06.29-11:00", "yyyy.MM.dd-HH:mm"), 73.0));
        vaultEntries.add(new VaultEntry(VaultEntryType.HEART_RATE_VARIABILITY, TimestampUtils.createCleanTimestamp("2017.06.29-11:00", "yyyy.MM.dd-HH:mm"), 47.0, 36.0));
        vaultEntries.add(new VaultEntry(VaultEntryType.HEART_RATE, TimestampUtils.createCleanTimestamp("2017.06.29-11:00", "yyyy.MM.dd-HH:mm"), 91.0));
        vaultEntries.add(new VaultEntry(VaultEntryType.BASAL_PROFILE, TimestampUtils.createCleanTimestamp("2017.06.29-11:00", "yyyy.MM.dd-HH:mm"), 0.9));
        vaultEntries.add(new VaultEntry(VaultEntryType.STRESS, TimestampUtils.createCleanTimestamp("2017.06.29-11:10", "yyyy.MM.dd-HH:mm"), 61.0));
        vaultEntries.add(new VaultEntry(VaultEntryType.HEART_RATE, TimestampUtils.createCleanTimestamp("2017.06.29-11:10", "yyyy.MM.dd-HH:mm"), 77.0));
        vaultEntries.add(new VaultEntry(VaultEntryType.HEART_RATE_VARIABILITY, TimestampUtils.createCleanTimestamp("2017.06.29-11:10", "yyyy.MM.dd-HH:mm"), 43.0, 52.0));
        vaultEntries.add(new VaultEntry(VaultEntryType.HEART_RATE, TimestampUtils.createCleanTimestamp("2017.06.29-11:21", "yyyy.MM.dd-HH:mm"), 68.0));
        vaultEntries.add(new VaultEntry(VaultEntryType.HEART_RATE_VARIABILITY, TimestampUtils.createCleanTimestamp("2017.06.29-11:21", "yyyy.MM.dd-HH:mm"), 43.0, 59.0));
        vaultEntries.add(new VaultEntry(VaultEntryType.STRESS, TimestampUtils.createCleanTimestamp("2017.06.29-11:21", "yyyy.MM.dd-HH:mm"), 55.75));
        vaultEntries.add(new VaultEntry(VaultEntryType.HEART_RATE, TimestampUtils.createCleanTimestamp("2017.06.29-11:30", "yyyy.MM.dd-HH:mm"), 51.0));
        vaultEntries.add(new VaultEntry(VaultEntryType.HEART_RATE_VARIABILITY, TimestampUtils.createCleanTimestamp("2017.06.29-11:40", "yyyy.MM.dd-HH:mm"), 43.0, 162.0));
        vaultEntries.add(new VaultEntry(VaultEntryType.HEART_RATE, TimestampUtils.createCleanTimestamp("2017.06.29-11:40", "yyyy.MM.dd-HH:mm"), 94.0));
        vaultEntries.add(new VaultEntry(VaultEntryType.STRESS, TimestampUtils.createCleanTimestamp("2017.06.29-11:40", "yyyy.MM.dd-HH:mm"), 9.5));
        vaultEntries.add(new VaultEntry(VaultEntryType.HEART_RATE_VARIABILITY, TimestampUtils.createCleanTimestamp("2017.06.29-11:51", "yyyy.MM.dd-HH:mm"), 41.0, 33.0));
        vaultEntries.add(new VaultEntry(VaultEntryType.STRESS, TimestampUtils.createCleanTimestamp("2017.06.29-11:51", "yyyy.MM.dd-HH:mm"), 75.25));
        vaultEntries.add(new VaultEntry(VaultEntryType.HEART_RATE, TimestampUtils.createCleanTimestamp("2017.06.29-11:51", "yyyy.MM.dd-HH:mm"), 58.0));
        vaultEntries.add(new VaultEntry(VaultEntryType.HEART_RATE, TimestampUtils.createCleanTimestamp("2017.06.29-12:00", "yyyy.MM.dd-HH:mm"), 55.0));
        vaultEntries.add(new VaultEntry(VaultEntryType.BASAL_PROFILE, TimestampUtils.createCleanTimestamp("2017.06.29-12:00", "yyyy.MM.dd-HH:mm"), 0.9));
        vaultEntries.add(new VaultEntry(VaultEntryType.STRESS, TimestampUtils.createCleanTimestamp("2017.06.29-12:11", "yyyy.MM.dd-HH:mm"), 17.25));
        vaultEntries.add(new VaultEntry(VaultEntryType.HEART_RATE_VARIABILITY, TimestampUtils.createCleanTimestamp("2017.06.29-12:11", "yyyy.MM.dd-HH:mm"), 41.0, 131.0));
        vaultEntries.add(new VaultEntry(VaultEntryType.HEART_RATE, TimestampUtils.createCleanTimestamp("2017.06.29-12:21", "yyyy.MM.dd-HH:mm"), 51.0));
        vaultEntries.add(new VaultEntry(VaultEntryType.HEART_RATE_VARIABILITY, TimestampUtils.createCleanTimestamp("2017.06.29-12:21", "yyyy.MM.dd-HH:mm"), 44.0, 127.0));
        vaultEntries.add(new VaultEntry(VaultEntryType.STRESS, TimestampUtils.createCleanTimestamp("2017.06.29-12:21", "yyyy.MM.dd-HH:mm"), 18.25));
        vaultEntries.add(new VaultEntry(VaultEntryType.HEART_RATE, TimestampUtils.createCleanTimestamp("2017.06.29-12:31", "yyyy.MM.dd-HH:mm"), 60.0));
        vaultEntries.add(new VaultEntry(VaultEntryType.HEART_RATE, TimestampUtils.createCleanTimestamp("2017.06.29-12:40", "yyyy.MM.dd-HH:mm"), 68.0));
        vaultEntries.add(new VaultEntry(VaultEntryType.HEART_RATE_VARIABILITY, TimestampUtils.createCleanTimestamp("2017.06.29-12:40", "yyyy.MM.dd-HH:mm"), 43.0, 77.0));

        return vaultEntries;
    }
    
}
