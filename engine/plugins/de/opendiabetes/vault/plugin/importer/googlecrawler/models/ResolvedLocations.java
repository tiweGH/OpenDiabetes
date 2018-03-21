package de.opendiabetes.vault.plugin.importer.googlecrawler.models;

import java.util.ArrayList;
import java.util.List;

/**
 * Resolved locations model.
 */
public final class ResolvedLocations {

    /**
     * Singleton instance.
     */
    private static ResolvedLocations instance;

    /**
     * List of locations that are resolved.
     */
    private List<Location> locations;

    /**
     * Constructor.
     */
    private ResolvedLocations() {
        locations = new ArrayList<>();
    }

    /**
     * Getter for the singleton instance.
     * @return the singleton instance
     */
    public static ResolvedLocations getInstance() {
        if (ResolvedLocations.instance == null) {
            ResolvedLocations.instance = new ResolvedLocations();
        }
        return ResolvedLocations.instance;
    }

    /**
     * Adds a location to the list of resolved locations.
     * @param loc a location object
     */
    public void addLocation(final Location loc) {
        locations.add(loc);
    }

    /**
     * Imports the locations from the given model.
     * @param resLoc a resolved locations model
     */
    public void importFromJson(final ResolvedLocations resLoc) {
        for (Location loc : resLoc.locations) {
            locations.add(loc);
        }
    }

    /**
     * Getter for the resolved locations.
     * @return a list of locations.
     */
    public List<Location> getLocations() {
        return locations;
    }
}
