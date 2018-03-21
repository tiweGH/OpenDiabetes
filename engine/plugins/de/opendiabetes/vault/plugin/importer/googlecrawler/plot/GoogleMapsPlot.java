package de.opendiabetes.vault.plugin.importer.googlecrawler.plot;

import com.google.maps.model.LatLng;
import de.opendiabetes.vault.plugin.importer.googlecrawler.helper.Constants;
import de.opendiabetes.vault.plugin.importer.googlecrawler.helper.Credentials;

import java.awt.Desktop;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

/**
 * Google maps plotter.
 */
public final class GoogleMapsPlot {

    /**
     * Default location multiplicator viewing coordinates.
     */
    private static final double LOCATION_MULTIPLIER = 100.0;

    /**
     * Singleton instance.
     */
    private static GoogleMapsPlot instance;

    /**
     * List of coordinates that are plotted.
     */
    private List<LatLng> locations;

    /**
     * List of location names that are plotted.
     */
    private List<String> locationNames;

    /**
     * HTML string that holds the maps view.
     */
    private String htmlFile;

    /**
     * Getter for the singleton instance.
     * @return the singleton instance
     */
    public static GoogleMapsPlot getInstance() {
        if (GoogleMapsPlot.instance == null) {
            GoogleMapsPlot.instance = new GoogleMapsPlot();
        }
        return GoogleMapsPlot.instance;
    }

    /**
     * Constructor.
     */
    private GoogleMapsPlot() {
        locations = new ArrayList<>();
        locationNames = new ArrayList<>();
    }

    /**
     * Adds a location to the list of locations.
     * @param location the location to be added
     */
    public void addLocation(final LatLng location) {
        locations.add(location);
    }

    /**
     * Adds a location name to the list of location names.
     * @param name the location name to be added
     */
    public void addLocationNames(final String name) {
        locationNames.add(name);
    }

    /**
     * Creates the map HTML string with the location data.
     */
    public void createMap() {
        htmlFile = Constants.MAPS_PRE;
        htmlFile += "var locations = [\n";
        for (int i = 0; i < locations.size(); i++) {
            htmlFile += "['"
                    + locationNames.get(i)
                    + "', "
                    + locations.get(i).lat
                    + ", "
                    + locations.get(i).lng
                    + ", "
                    + String.valueOf(locations.size() - i)
                    + "]";

            if (locations.size() - i != 1) {
                htmlFile += ",\n";
            } else {
                htmlFile += "];\n";
            }
        }

        htmlFile += "var map = new google.maps.Map(document.getElementById('map'), {\n"
                + "      zoom: 12,\n"
                + "      center: new google.maps.LatLng(";
        htmlFile += String.valueOf(
                Math.round(locations.get(0).lat * LOCATION_MULTIPLIER) / LOCATION_MULTIPLIER)
                + ", "
                + String.valueOf(Math.round(locations.get(0).lng * LOCATION_MULTIPLIER) / LOCATION_MULTIPLIER)
                + "),\n";
        htmlFile += Constants.MAPS_POST;
        htmlFile += Credentials.getInstance().getAPIKey();
        htmlFile += Constants.MAPS_END;
    }

    /**
     * Writes the created html to a file and opens it.
     */
    public void openMap() {
        File file = new File("map.html");

        try {
            OutputStreamWriter writer = new OutputStreamWriter(new FileOutputStream(file, false),
                    StandardCharsets.UTF_8);
            writer.write(htmlFile);
            writer.close();
            File htmlFile = new File("map.html");
            Desktop.getDesktop().browse(htmlFile.toURI());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
