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
import de.jhit.opendiabetes.vault.exporter.CsvFileExporter;
import de.jhit.opendiabetes.vault.importer.validator.CsvValidator;
import de.jhit.opendiabetes.vault.importer.validator.GoogleFitCsvValidator;
import de.jhit.opendiabetes.vault.util.EasyFormatter;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

/**
 *
 * @author mswin
 */
public class GoogleFitCsvImporter extends CsvImporter {

    public GoogleFitCsvImporter(String importFilePath) {
        this(importFilePath, new GoogleFitCsvValidator(), ',');
    }

    public GoogleFitCsvImporter(String importFilePath, CsvValidator validator, char delimiter) {
        super(importFilePath, validator, delimiter);
    }

    @Override
    protected void preprocessingIfNeeded(String filePath) {
        //not needed yet.
    }

    @Override
    protected List<VaultEntry> parseEntry(CsvReader creader) throws Exception {
        List<VaultEntry> retVal = new ArrayList<>();
        GoogleFitCsvValidator parseValidator = (GoogleFitCsvValidator) validator;

        long walkTime = parseValidator.getWalkValue(creader);
        long runTime = parseValidator.getRunValue(creader);
        long bikeTime = parseValidator.getBikeValue(creader);

        // basic filtering of idle entries
        // --> relevant activity has to be more than 1m
        if ((runTime + bikeTime + walkTime) < 1000) {
            return retVal;
        }

        VaultEntry newVaultEntry;
        Date timestamp = new Date(parseValidator.getStartTime(creader, super.importFilePath));
        double durationInMinutes = Math.round((runTime + bikeTime + walkTime) / 1000 / 60);
        double maxSpeed = parseValidator.getMaxSpeedValue(creader);

        // estimate the activity within this slot
        if (runTime > bikeTime && runTime > walkTime) {
            newVaultEntry = new VaultEntry(VaultEntryType.EXERCISE_RUN,
                    timestamp, durationInMinutes);
            newVaultEntry.setValue2(maxSpeed);
            newVaultEntry.addAnnotation((new VaultEntryAnnotation(VaultEntryAnnotation.TYPE.EXERCISE_GoogleRun))
                    .setValue(EasyFormatter.formatDouble(durationInMinutes)));
            retVal.add(newVaultEntry);
        } else if (bikeTime > runTime && bikeTime > walkTime) {
            newVaultEntry = new VaultEntry(VaultEntryType.EXERCISE_BICYCLE,
                    timestamp, durationInMinutes);
            newVaultEntry.setValue2(maxSpeed);
            newVaultEntry.addAnnotation((new VaultEntryAnnotation(VaultEntryAnnotation.TYPE.EXERCISE_GoogleBicycle))
                    .setValue(EasyFormatter.formatDouble(durationInMinutes)));
            retVal.add(newVaultEntry);
        } else {
            newVaultEntry = new VaultEntry(VaultEntryType.EXERCISE_WALK,
                    timestamp, durationInMinutes);
            newVaultEntry.setValue2(maxSpeed);
            newVaultEntry.addAnnotation((new VaultEntryAnnotation(VaultEntryAnnotation.TYPE.EXERCISE_GoogleWalk))
                    .setValue(EasyFormatter.formatDouble(durationInMinutes)));
            retVal.add(newVaultEntry);
        }

        return retVal;
    }
}
