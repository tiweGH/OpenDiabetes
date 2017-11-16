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
package de.jhit.opendiabetes.vault.importer.validator;

import java.util.Arrays;
import java.util.Set;
import java.util.TreeSet;
import java.util.logging.Logger;

/**
 *
 * @author mswin
 */
public abstract class CsvValidator {

    protected static final Logger LOG = Logger.getLogger(CsvValidator.class.getName());

    public static enum Language {
        DE, EN;
    };

    protected Language languageSelection;
    private final String[] HEADER_DE;
    private final String[] HEADER_EN;

    protected CsvValidator(String[] HEADER_DE, String[] HEADER_EN) {
        this.HEADER_DE = HEADER_DE;
        this.HEADER_EN = HEADER_EN;
    }

    public boolean validateHeader(String[] header) {

        boolean result = true;
        Set<String> headerSet = new TreeSet<>(Arrays.asList(header));

        // Check german header
        for (String item : HEADER_DE) {
            result &= headerSet.contains(item);
            if (!result) {
                break;
            }
        }
        if (result == true) {
            languageSelection = Language.DE;
        } else {
            // try again with english header
            result = true;
            for (String item : HEADER_EN) {
                result &= headerSet.contains(item);
                if (!result) {
                    break;
                }
            }
            if (result == true) {
                languageSelection = Language.EN;
            }
        }

        return result;
    }
}
