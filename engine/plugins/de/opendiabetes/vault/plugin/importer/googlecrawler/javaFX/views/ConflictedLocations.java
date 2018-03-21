package de.opendiabetes.vault.plugin.importer.googlecrawler.javaFX.views;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import de.opendiabetes.vault.plugin.importer.googlecrawler.helper.Constants;
import de.opendiabetes.vault.plugin.importer.googlecrawler.models.ResolvedLocations;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.nio.charset.StandardCharsets;

/**
 * Conflicted locations view.
 */
public final class ConflictedLocations extends Application {

    /**
     * Stage presenting the view scenes.
     */
    private Stage primaryStage;

    /**
     * Primary entry point.
     * @param args - Arguments given to the application
     */
    public static void main(final String[] args) {
        launch(args);
    }

    @Override
    public void start(final Stage primaryStage) {
        this.primaryStage = primaryStage;
        this.primaryStage.setTitle("Conflicted Locations");
        initRootLayout();
        primaryStage.show();
    }

    /**
     * Initiates the view.
     */
    private void initRootLayout() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getClassLoader().getResource("views/ConflictView.fxml"));
            AnchorPane rootLayout = (AnchorPane) loader.load();

            Scene scene = new Scene(rootLayout);
            primaryStage.setScene(scene);
            primaryStage.show();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void stop() throws Exception {
        super.stop();
        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        String output = gson.toJson(ResolvedLocations.getInstance());
        File file = new File(System.getProperty("user.home") + Constants.RESOLVED_LOCATION_PATH);

        try {
            OutputStreamWriter writer = new OutputStreamWriter(new FileOutputStream(file, false),
                    StandardCharsets.UTF_8);
            writer.write(output);
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    /**
     * Getter for the primary stage.
     * @return the stage presenting the view scene
     */
    public Stage getPrimaryStage() {
        return primaryStage;
    }
}
