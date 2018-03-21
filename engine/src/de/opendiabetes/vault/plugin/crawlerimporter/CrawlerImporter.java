package de.opendiabetes.vault.plugin.crawlerimporter;

import de.opendiabetes.vault.container.VaultEntry;
import de.opendiabetes.vault.plugin.importer.Importer;

import java.util.List;

/**
 * @author ocastx
 * This interface specifies the methods shared by all file importers.
 * It also serves as the {@link org.pf4j.ExtensionPoint} where the plugins hook up.
 * Therefore all file importer plugins must implement this interface to get recognized as crawler importer.
 */
public interface CrawlerImporter extends Importer {

    /**
     * Imports the data from a external site using the given credentials.
     *
     * @param username Name of the user to use at log in
     * @param password Password of the user to use at log in
     * @return List of VaultEntry consisting of the imported data.
     * @throws Exception Thrown if any kind of error occurs while importing
     * @see de.opendiabetes.vault.container.VaultEntry
     */
    List<VaultEntry> importData(String username, String password) throws Exception;
}
