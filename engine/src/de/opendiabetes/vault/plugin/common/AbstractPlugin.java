package de.opendiabetes.vault.plugin.common;


import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Properties;

/**
 * Class of which every Plugin should be a descendant,
 * handles notify mechanism to {@link de.jhit.opendiabetes.vault.plugin.common.OpenDiabetesPlugin.StatusListener}.
 */
public abstract class AbstractPlugin implements  OpenDiabetesPlugin {

    /**
     * Progress value indicating loading of configuration.
     */
    private static final int PROGRESS_LOADED_CONFIG = 33;

    /**
     * List containing all compatible plugins that are listed in this plugins config.
     * The data of this field is returned by {@link this#getListOfCompatiblePluginIDs()}
     * and used to list compatibilities among plugins.
     */
    private List<String> compatiblePlugins = new ArrayList<>();
    /**
     * List of StatusListener which contains all listeners registered to the importer.
     */
    private List<StatusListener> listeners = new ArrayList<StatusListener>();

    /**
     * {@inheritDoc}
     *
     * @param listener to be notified on a status update of the plugin.
     */
    @Override
    public void registerStatusCallback(final StatusListener listener) {
        this.listeners.add(listener);
    }

    /**
     * Method to notify the registered listeners about progress.
     * See {@link de.jhit.opendiabetes.vault.plugin.common.OpenDiabetesPlugin.StatusListener#onStatusCallback} to register a listener.
     *
     * @param progress Percentage of completion.
     * @param status   Current status.
     */
    protected void notifyStatus(final int progress, final String status) {
        listeners.forEach(listener -> listener.onStatusCallback(progress, status));
    }

    /**
     * Uses the properties file specified in {@link OpenDiabetesPlugin#loadConfiguration(Properties)}
     * and returns the list mapped by the key "compatiblePlugins".
     * @see {@link this#loadConfiguration(Properties)}
     * {@inheritDoc}
     */
    @Override
    public List<String> getListOfCompatiblePluginIDs() {
        return compatiblePlugins;
    }

    /**
     * {@inheritDoc}
     * Tries to load the list of compatible plugins.
     * @param configuration The configuration object.
     * @return always true
     */
    @Override
    public final boolean loadConfiguration(final Properties configuration) {
        if (configuration.containsKey("compatiblePlugins") && !configuration.getProperty("compatiblePlugins").equals("")) {

            this.compatiblePlugins.addAll(Arrays.asList(configuration.getProperty("compatiblePlugins").split("\\s*,\\s*")));
        }
        boolean result = loadPluginSpecificConfiguration(configuration);
        notifyStatus(PROGRESS_LOADED_CONFIG, "Loaded configuration");
        return result;
    }

    /**
     * Template method to load plugin specific configurations from the config file.
     * @param configuration The configuration object.
     * @return wheter a valid configuration could be read from the config file
     */
    protected abstract boolean loadPluginSpecificConfiguration(Properties configuration);
}
