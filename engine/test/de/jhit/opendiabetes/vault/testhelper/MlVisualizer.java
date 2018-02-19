package de.jhit.opendiabetes.vault.testhelper;

import de.jhit.opendiabetes.vault.container.VaultEntry;
import de.jhit.opendiabetes.vault.container.VaultEntryType;
import de.jhit.opendiabetes.vault.processing.filter.EventFilter;
import de.jhit.opendiabetes.vault.processing.filter.EventPointFilter;
import de.jhit.opendiabetes.vault.processing.filter.FilterResult;
import de.jhit.opendiabetes.vault.processing.filter.TimePointFilter;
import de.jhit.opendiabetes.vault.util.TimestampUtils;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Side;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.Tooltip;
import javafx.scene.layout.GridPane;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

/**
 * This class is an programm, which visualize the csv-data, which will be created by the machinelearner
 * @author Daniel
 */
public class MlVisualizer extends Application {

    private File file;
    private HashMap<String, List<Double>> tempVaultEntryMap;
    
    @Override
    public void start(Stage stage) {

        //set Grid
        GridPane grid = new GridPane();
        grid.setVgap(4);
        grid.setHgap(10);
        Scene scene = new Scene(new Group(), 800, 600);
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
        lineChart.setMinWidth(800);
                
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
                    if(file.getName().endsWith(".csv"))
                    {
                        tempVaultEntryMap = new HashMap();
                        List<String> headerNames = new ArrayList<>();
                        //read all lines
                        try (BufferedReader br = new BufferedReader(new FileReader(file))) {
                            String line;
                            int counter = 0;
                            while ((line = br.readLine()) != null) {
                                
                                if(counter == 0){
                                    String[] lineSplit = line.split(",");
                                    
                                    for (String string : lineSplit) {
                                        tempVaultEntryMap.put(string, new ArrayList<Double>());
                                        headerNames.add(string);
                                    }         
                                }
                                else
                                {                                    
                                    String[] lineSplit = line.split(",");
                                    int lineCounter = 0;
                                    for (String string : lineSplit) {
                                        List <Double>  tempList = tempVaultEntryMap.get(headerNames.get(lineCounter));
                                        tempList.add(Double.parseDouble(string.trim()));
                                        tempVaultEntryMap.put(headerNames.get(lineCounter), tempList);
                                        lineCounter++;
                                        
                                        
                                    }   
                                }
                                counter++;
                            }
                        }
                        catch(Throwable t)
                        {
                            t.printStackTrace();
                        }
                        
                        if(tempVaultEntryMap != null)
                        {
                            boolean firstElement = true;
                            for (String headerName : headerNames) {
                                if(firstElement)
                                    firstElement = false;
                                else
                                {
                                    XYChart.Series newSeries = new XYChart.Series();
                                    newSeries.setName(headerName);
                                    int counter = 0;
                                    
                                    for (Double entryValue : tempVaultEntryMap.get(headerName)) {
                                        XYChart.Data tempData = new XYChart.Data(new Integer(counter), entryValue);
                                        newSeries.getData().add(tempData);
                                        counter++;
                                    }
                                    
                                    lineChart.getData().add(newSeries);
                                }                                
                            }
                            //adding tooltip to linechart
                            for (XYChart.Series<Number, Number> series : lineChart.getData()) {
                                for (XYChart.Data<Number, Number> data : series.getData()) {
                                    Alert information = new Alert(AlertType.CONFIRMATION);
                                    information.setContentText(series.getName()+ " Nummer: " + data.getXValue() + " Wert:" + data.getYValue());
                                    
                                    data.getNode().setOnMouseEntered(event -> data.getNode().getStyleClass().add("onHover"));
                                    data.getNode().setOnMouseEntered(event -> information.show());
                                    data.getNode().setOnMouseExited(event -> data.getNode().getStyleClass().remove("onHover"));
                                    
                                    
                                }
                                
                            }
                        }
                    }
                    else
                    {
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
            grid.add(importFileAndPlotButton,0,1,1,1);
            
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
