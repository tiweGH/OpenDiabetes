package de.jhit.opendiabetes.vault.testhelper;

import de.jhit.opendiabetes.vault.container.VaultEntry;
import de.jhit.opendiabetes.vault.container.VaultEntryType;
import de.jhit.opendiabetes.vault.processing.filter.EventFilter;
import de.jhit.opendiabetes.vault.processing.filter.EventSpanFilter;
import de.jhit.opendiabetes.vault.processing.filter.FilterResult;
import de.jhit.opendiabetes.vault.util.TimestampUtils;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;

public class TestGui extends Application {

    @Override
    public void start(Stage stage) {

        GridPane grid = new GridPane();
        grid.setVgap(4);
        grid.setHgap(10);
        Scene scene = new Scene(new Group(), 500, 700);

        stage.setTitle("Test For Filter");
        //defining the axes
        final CategoryAxis xAxis = new CategoryAxis();
        final NumberAxis yAxis = new NumberAxis();
        xAxis.setLabel("Datum");
        //creating the chart
        final LineChart<String, Number> lineChart
                = new LineChart<String, Number>(xAxis, yAxis);

        SimpleDateFormat dateFormat = new SimpleDateFormat("HH:mm:ss yyyy.MM.dd");

        lineChart.setTitle("Test For Filter");
        //defining a series
        XYChart.Series series = new XYChart.Series();
        series.setName("EntryType Filter");

        try {
            List<VaultEntry> data = StaticDataset.getStaticDataset();
            EventSpanFilter instance = new EventSpanFilter(VaultEntryType.HEART_RATE, 0, Integer.MAX_VALUE);
            FilterResult result = instance.filter(data);

            int i = 0;

            for (VaultEntry entry : result.filteredData) {
                
                series.getData().add(new XYChart.Data(dateFormat.format(entry.getTimestamp()), entry.getValue()));
            }
            //Scene scene = new Scene(lineChart, 800, 600);
            lineChart.getData().add(series);

            // Dropdown Menu
            Label labelVaultEntryType = new Label("VaultEntryType:");
            ObservableList<VaultEntryType> options
                    = FXCollections.observableArrayList(
                            VaultEntryType.HEART_RATE,
                            VaultEntryType.STRESS,
                            VaultEntryType.BOLUS_NORMAL,
                            VaultEntryType.PUMP_FILL,
                            VaultEntryType.BASAL_INTERPRETER
                    );

            final ComboBox comboBox = new ComboBox(options);

            //Value 1 and 2
            Label labelValue = new Label("Value:");
            TextField textFieldValue = new TextField();
            Label labelMargin = new Label("Margin:");
            TextField textFieldMargin = new TextField();

            //Button
            Button filterDataButton = new Button("Filter");
            filterDataButton.setOnAction(new EventHandler<ActionEvent>() {
                @Override
                public void handle(ActionEvent e) {
                    try {
                        if (comboBox.getSelectionModel().getSelectedItem() != null && textFieldValue.getText() != null && !textFieldValue.getText().isEmpty() && textFieldMargin != null && !textFieldMargin.getText().isEmpty()) {
                            List<VaultEntry> data = StaticDataset.getStaticDataset();
                            EventSpanFilter instance = new EventSpanFilter((VaultEntryType) comboBox.getSelectionModel().getSelectedItem(), Float.parseFloat(textFieldValue.getText()), Float.parseFloat(textFieldMargin.getText()));
                            FilterResult result = instance.filter(data);

                            XYChart.Series newSeries = new XYChart.Series();
                            series.setName("EntryType Filter");

                            for (VaultEntry entry : result.filteredData) {
                                newSeries.getData().add(new XYChart.Data(dateFormat.format(entry.getTimestamp()), entry.getValue()));
                            }
                            //Scene scene = new Scene(lineChart, 800, 600);
                            lineChart.getData().retainAll();
                            lineChart.getData().add(newSeries);
                        }
                    } catch (Throwable t) {
                        t.printStackTrace();
                    }
                }
            });

            grid.add(lineChart, 0, 0, 2, 1);
            grid.add(labelVaultEntryType, 0, 1);
            grid.add(comboBox, 1, 1);
            grid.add(labelValue, 0, 2);
            grid.add(textFieldValue, 1, 2);
            grid.add(labelMargin, 0, 3);
            grid.add(textFieldMargin, 1, 3);
            grid.add(filterDataButton, 0, 4);

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
