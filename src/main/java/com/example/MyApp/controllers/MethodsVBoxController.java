package com.example.MyApp.controllers;

import com.example.MyApp.charts.MethodChart;
import javafx.collections.ListChangeListener;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import net.rgielen.fxweaver.core.FxWeaver;
import net.rgielen.fxweaver.core.FxmlView;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

@Component
@Scope("prototype")
@FxmlView("/fxml/methodsVBox.fxml")
public class MethodsVBoxController extends VBox implements Updatable {

    @Autowired
    ApplicationContext applicationContext;
    @Autowired
    MethodChart methodChart;
    @FXML
    VBox vBox;
    @FXML
    ColorPicker colorPicker;
    @FXML
    ComboBox<String> comboBox;
    @FXML
    ScrollPane scrollPane;
    @FXML
    AnchorPane anchorPane;


    @FXML
    public void initialize() {
        getChildren().add(vBox);
        setVgrow(vBox, Priority.ALWAYS);

        vBox.widthProperty().addListener((observable, oldValue, newValue) -> {
            update();
        });

        setScrollSettings();
    }


    public void setMethodName(String methodName) {
        setComboBoxSettings(methodName);
        methodChart.setColor(methodName, Color.BLACK);
    }


    @FXML
    private void addChart(ActionEvent actionEvent) {
        FxWeaver fxWeaver = applicationContext.getBean(FxWeaver.class);
        CombiningChartsController combiningChartsController = fxWeaver.loadController(CombiningChartsController.class);
        combiningChartsController.setComboBox(comboBox);
        combiningChartsController.show();
    }

    @FXML
    private void chooseColor(ActionEvent actionEvent) {
        methodChart.setColor(comboBox.getValue(), colorPicker.getValue());
        update();
    }

    private void setComboBoxSettings(String methodName) {
        comboBox.getItems().add(methodName);
        comboBox.setValue(methodName);
        comboBox.getItems().addListener((ListChangeListener<String>) c -> update());
        comboBox.setOnAction(event -> {
            colorPicker.setValue(methodChart.getColor(comboBox.getValue()));
            update();
        });
    }

    private void setScrollSettings() {
        ContentController contentController = applicationContext.getBean(ContentController.class);

        scrollPane.setVmax(contentController.getVScroll().getMax());
        scrollPane.setVmin(contentController.getVScroll().getMin());
        scrollPane.vvalueProperty().addListener((observable, oldValue, newValue) -> contentController.getVScroll().setValue(newValue.doubleValue()));
        contentController.getVScroll().valueProperty().addListener((observable, oldValue, newValue) -> scrollPane.setVvalue(newValue.doubleValue()));
    }


    public void update() {
        anchorPane.getChildren().clear();
        for (String methodName : comboBox.getItems()) {
            methodChart.drawChart(anchorPane, methodName, vBox.getWidth()-20);
        }
        colorPicker.setValue(methodChart.getColor(comboBox.getValue()));
        methodChart.drawMouseCoordinate(anchorPane, comboBox.getValue(), vBox.getWidth()-20);
    }
}
