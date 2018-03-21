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
package de.opendiabetes.vault.plugin.interpreter;

import de.opendiabetes.vault.data.VaultDao;
import de.opendiabetes.vault.plugin.common.AbstractPlugin;

/**
 * Abstract interpreter, implements the Interpreter interface.
 * All actual interpreter plugins are descendants of this class.
 * Supplies a Logger {@link de.jhit.opendiabetes.vault.plugin.common.OpenDiabetesPlugin#LOG }.
 * Handles status listeners {@link AbstractPlugin#listeners}.
 * @author OpenDiabetes
 */
public abstract class VaultInterpreter extends AbstractPlugin implements Interpreter {
    /**
     * The data access object {@link VaultDao} used by the interpreter.
     */
    private VaultDao database;

    /**
     * Initializes the database by setting its field to the given argument.
     * @param database the database to be set
     */
    @Override
    public void init(final VaultDao database) {
        this.database = database;
    }

    /**
     * Getter for the database.
     * @return the database
     */
    protected VaultDao getDatabase() {
        return database;
    }
}
