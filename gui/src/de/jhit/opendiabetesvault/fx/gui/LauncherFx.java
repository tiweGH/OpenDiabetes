/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package de.jhit.opendiabetesvault.fx.gui;

import de.jhit.opendiabetes.vault.data.VaultDao;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

/**
 *
 * @author mswin
 */
public class LauncherFx extends Application {

    @Override
    public void start(Stage stage) throws Exception {
        // Init DB
        VaultDao.initializeDb();

        // Setup Gui
        Parent root = FXMLLoader.load(getClass().getResource("MainGui.fxml"));

        Scene scene = new Scene(root);

        stage.setScene(scene);
        stage.setTitle("Open Diabetes Vault");
        stage.show();
    }

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {

        // doesnt work
//        Thread.setDefaultUncaughtExceptionHandler(new Thread.UncaughtExceptionHandler() {
//            @Override
//            public void uncaughtException(Thread t, Throwable e) {
//                System.err.println(e.toString());
//            }
//        });
        launch(args);
    }

}
