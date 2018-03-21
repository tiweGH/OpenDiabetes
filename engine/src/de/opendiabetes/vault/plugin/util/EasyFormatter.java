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
package de.opendiabetes.vault.plugin.util;

import java.util.Locale;

/**
 * This class implements an easy formatter for double representation.
 */
public final class EasyFormatter {

    /**
     * Private constructor to hinder default constructor creation.
     */
    private EasyFormatter() {
    }

    /**
     * Format of the output.
     */
    public static final String DOUBLE_FORMAT = "%1$.2f";

    /**
     * Formats doubles from x,yz to x.yz.
     *
     * @param input The double to be formatted.
     * @return The formatted double as a string.
     */
    public static String formatDouble(final double input) {
        return String.format(Locale.ENGLISH, DOUBLE_FORMAT, input).replace(",", "");
    }
}
