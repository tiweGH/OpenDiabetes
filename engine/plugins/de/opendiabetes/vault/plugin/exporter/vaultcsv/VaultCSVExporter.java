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
package de.opendiabetes.vault.plugin.exporter.vaultcsv;

import de.opendiabetes.vault.plugin.exporter.VaultExporter;
import org.pf4j.Extension;
import org.pf4j.Plugin;
import org.pf4j.PluginWrapper;


/**
 * Wrapper class for the VaultCSVExporter plugin.
 *
 * @author Lucas Buschlinger
 */
public class VaultCSVExporter extends Plugin {

    /**
     * Constructor for the {@link org.pf4j.PluginManager}.
     *
     * @param wrapper The {@link org.pf4j.PluginWrapper}.
     */
    public VaultCSVExporter(final PluginWrapper wrapper) {
        super(wrapper);
    }

    /**
     * Actual implementation of the VaultCSV exporter plugin.
     */
    @Extension
    public static final class VaultCSVExporterImplementation extends VaultExporter {

    }
}
