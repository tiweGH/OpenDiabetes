/*
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
package de.opendiabetes.vault.plugin.importer.validator;

import java.util.Arrays;
import java.util.Set;
import java.util.TreeSet;
import java.util.logging.Logger;

/**
 * The abstract validator for CSV data.
 *
 * @author Jens Heuschkel
 */
public abstract class CSVValidator {

    /**
     * The logger used by the validator.
     */
    protected static final Logger LOG = Logger.getLogger(CSVValidator.class.getName());
    /**
     * The German header.
     */
    private final String[] headerDe;
    /**
     * The English header.
     */
    private final String[] headerEn;
    /**
     * The selected language.
     */
    private Language languageSelection;

    /**
     * Constructor for CSV validators.
     *
     * @param germanHeader The German header.
     * @param englishHeader The English header.
     */
    protected CSVValidator(final String[] germanHeader, final String[] englishHeader) {
        this.headerDe = germanHeader;
        this.headerEn = englishHeader;
    }

    /**
     * Validator for CSV headers.
     *
     * @param header The header to check for.
     * @return True if a valid header is present, false otherwise.
     */
    public boolean validateHeader(final String[] header) {

        boolean result = true;
        Set<String> headerSet = new TreeSet<>(Arrays.asList(header));

        // Check german header
        for (String item : headerDe) {
            result &= headerSet.contains(item);
            if (!result) {
                break;
            }
        }
        if (result) {
            languageSelection = Language.DE;
        } else {
            // try again with english header
            result = true;
            for (String item : headerEn) {
                result &= headerSet.contains(item);
                if (!result) {
                    break;
                }
            }
            if (result) {
                languageSelection = Language.EN;
            }
        }

        return result;
    }

    /**
     * The possible languages.
     */
    public enum Language {
        /**
         * German.
         */
        DE,
        /**
         * English.
         */
        EN
    }

    /**
     * Getter for the {@link #languageSelection}.
     *
     * @return The set language.
     */
    public Language getLanguageSelection() {
        return languageSelection;
    }
}
