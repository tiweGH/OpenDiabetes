/*
 * Copyright (C) 2017 juehv
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

import de.jhit.opendiabetes.vault.importer.interpreter.VaultInterpreter;
import java.util.ArrayList;

/**
 *
 * @author juehv
 *
 * used to cascade interpreter
 */
public class InterpreterContainer extends Importer {

    private final VaultInterpreter interpreter;

    public InterpreterContainer(VaultInterpreter interpreter) {
        this.interpreter = interpreter;
    }

    @Override
    public boolean importData() {
        importedData = interpreter.importAndInterpretWithoutDb();
        importedRawData = new ArrayList<>();

        if (importedData == null) {
            importedData = new ArrayList<>();
            return false;
        }

        return true;
    }

}
