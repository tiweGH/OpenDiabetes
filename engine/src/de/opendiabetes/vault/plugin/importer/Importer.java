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
package de.opendiabetes.vault.plugin.importer;

import de.opendiabetes.vault.container.VaultEntry;
import de.opendiabetes.vault.plugin.common.OpenDiabetesPlugin;
import org.pf4j.ExtensionPoint;

import java.util.List;


/**
 * @author Magnus Gärtner
 * @author Lucas Buschlinger
 * This interface specifies the methods shared by all importers.
 * It also serves as the {@link org.pf4j.ExtensionPoint} where the plugins hook up.
 * Therefore all importer plugins must implement this interface to get recognized as importer.
 */
public interface Importer extends ExtensionPoint, OpenDiabetesPlugin {

    /**
     * Imports the data.
     *
     * @return List of VaultEntry consisting of the imported data.
     * @throws Exception Thrown if any kind of error occurs while importing
     * @see de.opendiabetes.vault.container.VaultEntry
     */
    List<VaultEntry> importData() throws Exception;
}
