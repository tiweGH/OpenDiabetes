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
package de.opendiabetes.vault.plugin.importer.medtronic;

import de.opendiabetes.vault.container.VaultEntry;

import java.util.Objects;

/**
 * This class implements the annotated VaultEntry data structure for medtronic data.
 */
public class MedtronicAnnotatedVaultEntry extends VaultEntry {

    /**
     * Type used by the validator.
     */
    private final MedtronicCSVValidator.TYPE rawType;

    /**
     * Constructor for annotated Medtronic VaultEntries.
     * @param copy Copied VaultEntry to be annotated.
     * @param rawType Raw type of the entry.
     */
    public MedtronicAnnotatedVaultEntry(final VaultEntry copy, final MedtronicCSVValidator.TYPE rawType) {
        super(copy);
        this.rawType = rawType;
    }

    /**
     * Getter for the event duration.
     * @return The duration.
     */
    public double getDuration() { // in medtronic data, the duration is in milliseconds
        return super.getValue2();
    }

    /**
     * Getter for the raw type.
     * @return The raw type.
     */
    public MedtronicCSVValidator.TYPE getRawType() {
        return rawType;
    }

    /**
     * Converts the MedtronicAnnotatedVaultEntry to a string.
     *
     * @return The MedtronicAnnotatedVaultEntry.
     */
    @Override
    public String toString() {
        return super.toString() + " MedtronicAnnotatedVaultEntry{" + "rawType=" + rawType + '}';
    }

    /**
     * Hashcode generator for a MedtronicAnnotatedVaultEntry.
     *
     * @return The hashcode.
     */
    @Override
    public int hashCode() {
        final int magicNumber = 67;
        return magicNumber * super.hashCode() + Objects.hashCode(this.rawType);
    }

    /**
     * Checks whether two MedtronicAnnotatedVaultEntry are equal.
     *
     * @param obj The MedtronicAnnotatedVaultEntry to be compared against to MedtronicAnnotatedVaultEntry this gets called on.
     * @return True of the entries are equal, false otherwise.
     */
    @Override
    public boolean equals(final Object obj) {
        if (!super.equals(obj)) {
            return false;
        }
        return this.rawType.equals(((MedtronicAnnotatedVaultEntry) obj).getRawType());
    }

}
