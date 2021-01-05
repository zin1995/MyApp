package com.example.MyApp.controllers;

import com.example.MyApp.LasParser;
import com.example.MyApp.charts.ChangeableAnchor;
import com.example.MyApp.charts.LithologyChart;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.ComboBox;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import net.rgielen.fxweaver.core.FxmlView;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import java.util.ArrayList;
import java.util.HashMap;

@Component
@Scope("prototype")
@FxmlView("/fxml/lithologyVBox.fxml")
public class LithologyVBoxController extends VBox implements Updatable {
    @Autowired
    ApplicationContext applicationContext;
    @Autowired
    ContentController contentController;
    @Autowired
    LithologyChart lithologyChart;
    @FXML
    VBox vBox;
    @FXML
    ComboBox<String> comboBox;
    @FXML
    ScrollPane scrollPane;
    @FXML
    AnchorPane anchorPane;
    private HashMap<String, ArrayList<Double[]>> lithologyIntervalsMap;

    @FXML
    public void initialize() {
        getChildren().add(vBox);
        setVgrow(vBox, Priority.ALWAYS);

        vBox.widthProperty().addListener((observable, oldValue, newValue) -> {
            update();
        });

        setScrollSettings();
    }

    public void setLithologyIntervals(HashMap<String, ArrayList<Double[]>> lithologyIntervalsMap) {
        this.lithologyIntervalsMap = lithologyIntervalsMap;
    }

    public void setLithologyName(String lithologyName) {
        comboBox.getItems().add(lithologyName);
        comboBox.setValue(lithologyName);
    }

    private void setScrollSettings() {
        ContentController contentController = applicationContext.getBean(ContentController.class);

        scrollPane.setVmax(contentController.getVScroll().getMax());
        scrollPane.setVmin(contentController.getVScroll().getMin());
        scrollPane.vvalueProperty().addListener((observable, oldValue, newValue) -> contentController.getVScroll().setValue(newValue.doubleValue()));
        contentController.getVScroll().valueProperty().addListener((observable, oldValue, newValue) -> scrollPane.setVvalue(newValue.doubleValue()));
    }

    public void setChanges(String name, int id, double topAnchor, double height){
        lithologyIntervalsMap.get(name).get(id)[0] = topAnchor / contentController.getDepthMultiplier();
        lithologyIntervalsMap.get(name).get(id)[1] = (topAnchor + height) / contentController.getDepthMultiplier();
    }

    @Override
    public void update() {
        anchorPane.getChildren().clear();
        lithologyChart.drawLithology(anchorPane, lithologyIntervalsMap, this);
    }
}
