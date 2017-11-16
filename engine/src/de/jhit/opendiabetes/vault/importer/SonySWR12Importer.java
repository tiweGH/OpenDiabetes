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
package de.jhit.opendiabetes.vault.importer;

import com.csvreader.CsvReader;
import de.jhit.opendiabetes.vault.container.VaultEntry;
import de.jhit.opendiabetes.vault.container.VaultEntryAnnotation;
import de.jhit.opendiabetes.vault.container.VaultEntryType;
import de.jhit.opendiabetes.vault.importer.validator.SonySWR12Validator;
import de.jhit.opendiabetes.vault.util.EasyFormatter;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.logging.Logger;

/**
 *
 * @author juehv
 */
public class SonySWR12Importer extends CsvImporter {

    public SonySWR12Importer(String importFilePath) {
        super(importFilePath, new SonySWR12Validator(), ',');
    }

    @Override
    protected void preprocessingIfNeeded(String filePath) {
        // not needed
    }

    @Override
    protected List<VaultEntry> parseEntry(CsvReader creader) throws Exception {
        List<VaultEntry> retVal = new ArrayList<>();
        SonySWR12Validator parseValidator = (SonySWR12Validator) validator;

        SonySWR12Validator.TYPE type = parseValidator.getSmartbandType(creader);
        if (type == null) {
            return null;
        }

        Date timestamp = parseValidator.getTimestamp(creader);
        if (timestamp == null) {
            return null;
        }

        int rawValue = parseValidator.getValue(creader);
        long startTime = parseValidator.getStartTime(creader);
        long endTime = parseValidator.getEndTime(creader);
        double durationInMinutes = (endTime - startTime) / 60000;
        VaultEntry tmpEntry = null;

        switch (type) {
            case SLEEP_LIGHT:
                tmpEntry = new VaultEntry(
                        VaultEntryType.SLEEP_LIGHT,
                        timestamp,
                        durationInMinutes);
                break;
            case SLEEP_DEEP:
                tmpEntry = new VaultEntry(
                        VaultEntryType.SLEEP_DEEP,
                        timestamp,
                        durationInMinutes);
                break;
            case HEART_RATE:
                tmpEntry = new VaultEntry(
                        VaultEntryType.HEART_RATE,
                        timestamp,
                        rawValue);
                break;
            case HEART_RATE_VARIABILITY:
                // Algorithm see decompiled SWR12 app --> RelaxStressIntensity Class
                int value1 = (int) ((rawValue >>> 8) & 255);
                int value2 = (int) (255 & rawValue);

                if (value1 > 0 && value1 < 100
                        && value2 > 0 && value2 < 200) {
                    tmpEntry = new VaultEntry(
                            VaultEntryType.HEART_RATE_VARIABILITY,
                            timestamp,
                            value1);
                    tmpEntry.setValue2(value2);
                    retVal.add(tmpEntry);

                    // calculate stress value
                    value2 -= 100;
                    double weight = value2 < 0 ? 0.75 : 0.25;

                    double stressValue = 25 - value2 * weight;
                    tmpEntry = new VaultEntry(
                            VaultEntryType.STRESS,
                            timestamp,
                            stressValue);
                }

                break;
            case RUN:
                tmpEntry = new VaultEntry(
                        VaultEntryType.EXERCISE_RUN,
                        timestamp,
                        durationInMinutes);
                tmpEntry.addAnnotation(new VaultEntryAnnotation(
                        VaultEntryAnnotation.TYPE.EXERCISE_TrackerRun)
                        .setValue(EasyFormatter.formatDouble(durationInMinutes)));
                break;
            case WALK:
                tmpEntry = new VaultEntry(
                        VaultEntryType.EXERCISE_WALK,
                        timestamp,
                        durationInMinutes);
                tmpEntry.addAnnotation(new VaultEntryAnnotation(
                        VaultEntryAnnotation.TYPE.EXERCISE_TrackerWalk)
                        .setValue(EasyFormatter.formatDouble(durationInMinutes)));
                break;
            default:
                Logger.getLogger(this.getClass().getName()).fine("AssertionError");
                throw new AssertionError();
        }

        if (tmpEntry != null) {
            retVal.add(tmpEntry);
        }
        return retVal;
    }

}
