package de.opendiabetes.vault.plugin.importer.googlecrawler.javaFX.controller;

import com.google.maps.model.LatLng;
import com.google.maps.model.PlacesSearchResult;
import de.opendiabetes.vault.plugin.importer.googlecrawler.helper.Credentials;
import de.opendiabetes.vault.plugin.importer.googlecrawler.location.LocationHistory;
import de.opendiabetes.vault.plugin.importer.googlecrawler.models.ConflictedLocationIdentifier;
import de.opendiabetes.vault.plugin.importer.googlecrawler.models.Location;
import de.opendiabetes.vault.plugin.importer.googlecrawler.models.ResolvedLocations;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.scene.web.WebEngine;
import javafx.scene.web.WebView;

import java.text.SimpleDateFormat;
import java.util.stream.Collectors;
import java.util.List;
import java.util.Map;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.Arrays;
import java.util.ArrayList;

/**
 * Class showing the conflicted locations.
 */
public class ConflictViewController {
    /**
     * List view presenting the conflicted activities.
     */
    @FXML
    private ListView conflictedActivitiesListView;

    /**
     * List view presenting the conflicted locations.
     */
    @FXML
    private ListView conflictedLocationsListView;

    /**
     * Web view presenting the conflicted locations.
     */
    @FXML
    private WebView conflictedLocationWebView;

    /**
     * Text field for conflicted locations.
     */
    @FXML
    private TextField conflictedLocationTextField;

    /**
     * List of conflicted activities.
     */
    private Map<ConflictedLocationIdentifier, List<PlacesSearchResult>> conflictedActivities;

    /**
     * Constructor.
     */
    public ConflictViewController() {
    }

    /**
     * Initializes the view with conflicted activities.
     */
    @FXML
    private void initialize() {
        conflictedActivities = LocationHistory.getInstance().getConflictedActivities();
        setConflictedActivitiesListView();
    }

    /**
     * Prepares the conflicted activities list view.
     */
    private void setConflictedActivitiesListView() {
        if (!conflictedActivities.isEmpty()) {
            SimpleDateFormat timeFormat = new SimpleDateFormat("dd.MM.yyyy HH:mm");
            List<String> values = conflictedActivities.entrySet().stream().map(e -> {
                Calendar cal = new GregorianCalendar();
                cal.setTimeInMillis(e.getKey().getTimestamp());
                return timeFormat.format(cal.getTime());
            }).collect(Collectors.toList());

            conflictedActivitiesListView.setItems(FXCollections.observableList(values));
            conflictedActivitiesListView.getSelectionModel().selectFirst();
            onMouseClickedActivity(null);
        } else {
            conflictedActivitiesListView.getItems().clear();
        }
    }

    /**
     * List holding the places search results.
     */
    private List<PlacesSearchResult> places;

    /**
     * Selected activity key.
     */
    private Object selectedActivityKey;

    /**
     * Event handler for mouse clicks.
     * @param event - event sent by the mouse click
     */
    @FXML
    private void onMouseClickedActivity(final MouseEvent event) {
        selectedActivityKey = (conflictedActivities.keySet().toArray())[
                conflictedActivitiesListView.getSelectionModel().getSelectedIndex()];
        places = conflictedActivities.get(selectedActivityKey);

        List<String> values = places.stream().map(p -> p.name).collect(Collectors.toList());
        conflictedLocationsListView.setItems(FXCollections.observableList(values));

        StringBuilder urlBuilder = new StringBuilder("https://maps.googleapis.com/maps/api/staticmap?zoom=18&size=400x500&maptype=roadmap");

        List<String> colors = new ArrayList<>();
        colors.addAll(Arrays.asList("blue", "green", "red", "yellow"));

        urlBuilder.append("&center=").append(places.get(0).geometry.location.lat).append(",").append(places.get(0).geometry.location.lng);

        for (int i = 0; i < places.size(); i++) {
            urlBuilder.append("&markers=color:").append(colors.get(i % colors.size()));
            urlBuilder.append("%7Clabel:").append(places.get(i).name.substring(0, 1));
            urlBuilder.append("%7C").append(places.get(i).geometry.location.lat).append(",").append(places.get(i).geometry.location.lng);
        }

        urlBuilder.append("&key=").append(Credentials.getInstance().getAPIKey());


        WebEngine webEngine = conflictedLocationWebView.getEngine();
        webEngine.load(urlBuilder.toString());
    }

    /**
     * Saves the selected locations to the resolved locations instance.
     * @param event - event sent by the mouse click
     */
    @FXML
    public void saveSelectedLocation(final MouseEvent event) {
        if (!conflictedLocationsListView.getSelectionModel().isEmpty()) {
            Location loc = new Location();
            PlacesSearchResult sr = places.get(conflictedLocationsListView.getSelectionModel().getSelectedIndex());
            loc.setName(sr.name);
            loc.setCoordinate(new LatLng(sr.geometry.location.lat, sr.geometry.location.lng));

            ResolvedLocations.getInstance().addLocation(loc);

            conflictedActivities.remove(selectedActivityKey);

            conflictedLocationsListView.getItems().clear();
            conflictedLocationWebView.getEngine().load("");

            setConflictedActivitiesListView();
        }
    }

    /**
     * Saves the entered location to the resolved locations instance.
     * @param event - event sent by the mouse click
     */
    @FXML
    public void saveCustomLabel(final MouseEvent event) {
        if (!conflictedLocationTextField.getText().isEmpty()) {
            Location loc = new Location();
            loc.setName(conflictedLocationTextField.getText());
            loc.setCoordinate(((ConflictedLocationIdentifier) selectedActivityKey).getCoordinate());

            ResolvedLocations.getInstance().addLocation(loc);

            conflictedActivities.remove(selectedActivityKey);

            conflictedLocationsListView.getItems().clear();
            conflictedLocationWebView.getEngine().load("");

            setConflictedActivitiesListView();
        }
    }
}
