package de.opendiabetes.vault.testhelper;

import de.opendiabetes.vault.container.VaultEntryType;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.layout.GridPane;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

/**
 * This class is an programm, which visualize the csv-data, which will be
 * created by the machinelearner
 *
 * @author Daniel
 */
public class MLVisualizer1 extends Application {

    private File file;
    private HashMap<String, List<Double>> tempVaultEntryMap;

    @Override
    public void start(Stage stage) {

        //set Grid
        GridPane grid = new GridPane();
        grid.setVgap(4);
        grid.setHgap(10);
        Scene scene = new Scene(new Group(), 1600, 800);
        scene.getStylesheets().add(getClass().getResource("chart.css").toExternalForm());

        stage.setTitle("ML - Visualisierung");
        //defining the axes
        final NumberAxis xAxis = new NumberAxis();
        final NumberAxis yAxis = new NumberAxis();
        xAxis.setLabel("Nummer");
        yAxis.setLabel("Wert");

        //creating the chart
        final LineChart<Number, Number> lineChart
                = new LineChart<>(xAxis, yAxis);
        lineChart.setTitle("ML - Visualisierung");
        lineChart.setMinHeight(500);
        lineChart.setMinWidth(1590);

        //add FileChooser
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Open Resource File");

        //add Button for import
        Button importFileAndPlotButton = new Button("Importieren");
        importFileAndPlotButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent e) {
                file = fileChooser.showOpenDialog(stage);

                //only csv file
                if (file.getName().endsWith(".csv")) {
                    tempVaultEntryMap = new HashMap();
                    List<String> headerNames = new ArrayList<>();
                    //read all lines
                    int counter = 0;
                    try (BufferedReader br = new BufferedReader(new FileReader(file))) {
                        String line;
                        while ((line = br.readLine()) != null) {

                            if (counter == 0) {
                                System.out.print("Parsing Header-names");
                                String[] lineSplit = line.split(", ");

                                for (String string : lineSplit) {
                                    tempVaultEntryMap.put(string, new ArrayList<Double>());
                                    headerNames.add(string);
                                }
                                System.out.print(" - done");
                                System.out.println("");
                                System.out.print("Parsing data");
                            } else {
                                String[] lineSplit = line.split(",");
                                int lineCounter = 0;
                                for (String string : lineSplit) {
                                    List<Double> tempList = tempVaultEntryMap.get(headerNames.get(lineCounter));
                                    tempList.add(Double.parseDouble(string.trim()));
                                    tempVaultEntryMap.put(headerNames.get(lineCounter), tempList);
                                    lineCounter++;

                                }
                            }
                            counter++;
                        }
                        System.out.print(" - done");
                    } catch (Throwable t) {
                        System.out.println("counter = " + counter);
                        t.printStackTrace();
                    }

                    if (tempVaultEntryMap != null) {
                        boolean firstElement = true;
                        System.out.println("");
                        System.out.println("Plotting data");
                        int counter2 = 0;
                        int theSize = headerNames.size();
                        XYChart.Series newSeries;
                        XYChart.Data tempData;
                        for (String headerName : headerNames) {
                            if (firstElement) {
                                firstElement = false;
                            } else {
                                boolean isOnheHot = VaultEntryType.valueOf(headerName).isOneHot();
                                System.out.println("Start: " + headerName + " " + counter2 + " of " + theSize);
                                newSeries = new XYChart.Series();
                                String name = headerName;
                                if (isOnheHot) {
                                    name = name + " - OH";
                                }
                                newSeries.setName(name);
                                counter = 0;
                                for (Double entryValue : tempVaultEntryMap.get(headerName)) {
                                    if (isOnheHot) {
                                        entryValue = entryValue * 200;
                                    }
                                    tempData = new XYChart.Data(new Integer(counter), entryValue);
                                    newSeries.getData().add(tempData);
                                    counter++;
                                }
                                //tempVaultEntryMap.remove(headerName);
                                System.out.println("Add " + headerName);
                                lineChart.getData().add(newSeries);
                                counter2++;
                            }
                        }
                        //tiweGH hotfix
//                        if (headerNames.contains(" GLUCOSE_CGM")) {
//                            System.out.println(" GLUCOSE_CGM");
//                            newSeries = new XYChart.Series();
//                            newSeries.setName(" GLUCOSE_CGM");
//                            counter = 0;
//                            for (Double entryValue : tempVaultEntryMap.get(" GLUCOSE_CGM")) {
//                                tempData = new XYChart.Data(new Integer(counter), entryValue);
//                                newSeries.getData().add(tempData);
//                                counter++;
//                            }
//                            lineChart.getData().add(newSeries);
//                        }
//                        if (headerNames.contains(" HEART_RATE")) {
//                            System.out.println(" HEART_RATE");
//                            newSeries = new XYChart.Series();
//                            newSeries.setName(" HEART_RATE");
//                            counter = 0;
//                            for (Double entryValue : tempVaultEntryMap.get(" HEART_RATE")) {
//                                tempData = new XYChart.Data(new Integer(counter), entryValue);
//                                newSeries.getData().add(tempData);
//                                counter++;
//                            }
//                            lineChart.getData().add(newSeries);
//                        }
                        //---
                        System.out.print(" - done");
                        System.out.println("");
//                        System.out.print("Plotting tooltips");
//                        //adding tooltip to linechart
//                        for (XYChart.Series<Number, Number> series : lineChart.getData()) {
//                            for (XYChart.Data<Number, Number> data : series.getData()) {
//                                Alert information = new Alert(AlertType.CONFIRMATION);
//                                information.setContentText(series.getName() + " Nummer: " + data.getXValue() + " Wert:" + data.getYValue());
//
//                                data.getNode().setOnMouseEntered(event -> data.getNode().getStyleClass().add("onHover"));
//                                data.getNode().setOnMouseEntered(event -> information.show());
//                                data.getNode().setOnMouseExited(event -> data.getNode().getStyleClass().remove("onHover"));
//
//                            }
//
//                        }
//                        System.out.print(" - done");
                    }
                } else {
                    Alert alert = new Alert(AlertType.ERROR);
                    alert.setTitle("Error");
                    alert.setHeaderText("Wrong File");
                    alert.setContentText("only use csv are allowed");

                    alert.showAndWait();
                }
            }
        });

        try {
            grid.add(lineChart, 0, 0, 2, 1);
            grid.add(importFileAndPlotButton, 0, 1, 1, 1);

            Group root = (Group) scene.getRoot();
            root.getChildren().add(grid);
            stage.setScene(scene);
            stage.show();

        } catch (Throwable t) {
            t.printStackTrace();
        }

    }

    public static void main(String[] args) {
        launch(args);
    }
}
