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
package de.opendiabetes.vault.plugin.interpreter;

import de.opendiabetes.vault.container.VaultEntry;
import de.opendiabetes.vault.data.VaultDao;
import de.opendiabetes.vault.plugin.common.OpenDiabetesPlugin;
import java.util.List;
import org.pf4j.ExtensionPoint;

/**
 * This interface defines the basic methods used by the interpreter plugins.
 */
public interface Interpreter extends ExtensionPoint, OpenDiabetesPlugin {

    /**
     * This method initilizes the database.
     * @param database The database
     */
    void init(VaultDao database);

    /**
     * This method interprets the input.
     * @param input the list to be interpreted
     * @return the interpreted list
     */
    List<VaultEntry> interpret(List<VaultEntry> input);
}
