package de.opendiabetes.vault.testhelper;

import javafx.application.Application;
import javafx.stage.Stage;

public class TestGui extends Application {

    @Override
    public void start(Stage stage) {
        /*
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
            EventPointFilter instance = new EventPointFilter(VaultEntryType.HEART_RATE, 0, Integer.MAX_VALUE);
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
                            VaultEntryType.BASAL_INTERPRETER,
                            VaultEntryType.GLUCOSE_BG
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
                            EventPointFilter instance = new EventPointFilter((VaultEntryType) comboBox.getSelectionModel().getSelectedItem(), Float.parseFloat(textFieldValue.getText()), Float.parseFloat(textFieldMargin.getText()));
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

            Button filterTimePointButton = new Button("TimePointFilter");
            filterTimePointButton.setOnAction(new EventHandler<ActionEvent>() {
                @Override
                public void handle(ActionEvent e) {
                    try {
                        List<VaultEntry> data = StaticDataset.getStaticDataset();
                        TimePointFilter instance = new TimePointFilter(LocalTime.NOON, 60);
                        FilterResult result = instance.filter(data);

                        XYChart.Series newSeries = new XYChart.Series();
                        series.setName("Timepoint Filter");

                        for (VaultEntry entry : result.filteredData) {
                            newSeries.getData().add(new XYChart.Data(dateFormat.format(entry.getTimestamp()), entry.getValue()));
                        }
                        //Scene scene = new Scene(lineChart, 800, 600);
                        lineChart.getData().retainAll();
                        lineChart.getData().add(newSeries);

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
            grid.add(filterTimePointButton, 0, 5);

            Group root = (Group) scene.getRoot();
            root.getChildren().add(grid);
            stage.setScene(scene);
            stage.show();

        } catch (Throwable t) {
            t.printStackTrace();
        }
         */
    }

    public static void main(String[] args) {
        launch(args);
    }
}
