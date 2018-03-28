/*
 * Copyright (C) 2018 tiweGH
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
package de.opendiabetes.vault.processing.preprocessing;

import de.opendiabetes.vault.container.VaultEntry;
import de.opendiabetes.vault.processing.VaultEntrySlicer;
import de.opendiabetes.vault.processing.preprocessing.options.PreprocessorOption;
import java.util.List;
import java.util.logging.Logger;

/**
 * This class is used to preprocess the data for the slicer.
 *
 * @author tiweGH
 */
public abstract class Preprocessor {

    protected static final Logger LOG = Logger.getLogger(VaultEntrySlicer.class.getName());

    private final PreprocessorOption preprocessorOption;

    public Preprocessor(PreprocessorOption preprocessorOption) {
        this.preprocessorOption = preprocessorOption;
    }

    public abstract List<VaultEntry> preprocess(List<VaultEntry> data);

    public PreprocessorOption getPreprocessorOption() {
        return preprocessorOption;
    }
}
