package com.example.MyApp.updatable;

import com.example.MyApp.сontentAnchor.LithologyChart;
import javafx.geometry.Insets;
import javafx.scene.control.Button;
import javafx.scene.control.ScrollBar;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.Slider;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;

public class LithologyVBox extends VBox implements Updatable {
    private ScrollPane scrollPane = new ScrollPane();
    private AnchorPane anchorPane = new AnchorPane();
    private LithologyChart lithologyChart;
    private String[] lithologyData;

    public LithologyVBox(String[] lithologyData, LithologyChart lithologyChart, ScrollBar vScroll){
        this.lithologyData = lithologyData;
        this.lithologyChart = lithologyChart;

        //заглушка
        getChildren().add(new Button());
        getChildren().add(new Slider());
        getChildren().add(scrollPane);

        setScrollSettings(vScroll);

        setMaxWidth(70);
        setMinWidth(70);

        anchorPane.setStyle("-fx-background-color: grey");
        lithologyChart.drawLithology(anchorPane, lithologyData);
    }

    private void setScrollSettings(ScrollBar vScroll){
        setMargin(scrollPane, new Insets(10, 0, 0, 0));
        setVgrow(scrollPane, Priority.ALWAYS);
        scrollPane.setFitToHeight(true);
        scrollPane.setFitToWidth(true);
        scrollPane.setStyle("-fx-background-insets: 0");
        scrollPane.setVbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
        scrollPane.setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
        scrollPane.setContent(anchorPane);
        scrollPane.setVmax(vScroll.getMax());
        scrollPane.setVmin(vScroll.getMin());
        scrollPane.vvalueProperty().addListener((observable, oldValue, newValue) -> vScroll.setValue(newValue.doubleValue()));
        vScroll.valueProperty().addListener((observable, oldValue, newValue) -> scrollPane.setVvalue(newValue.doubleValue()));
    }

    @Override
    public void update() {
        anchorPane.getChildren().clear();
        lithologyChart.drawLithology(anchorPane, lithologyData);
    }

}
