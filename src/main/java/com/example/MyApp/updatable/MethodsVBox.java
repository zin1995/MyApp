package com.example.MyApp.updatable;

import com.example.MyApp.controllers.CombiningChartsController;
import com.example.MyApp.сontentAnchor.MethodChart;
import javafx.collections.ListChangeListener;
import javafx.geometry.Insets;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import net.rgielen.fxweaver.core.FxWeaver;
import org.springframework.context.ApplicationContext;

import static javafx.scene.layout.HBox.setHgrow;

public class MethodsVBox extends VBox implements Updatable {
    private HBox hBox = new HBox();
    private Button addChartButton = new Button("+");
    private ColorPicker colorPicker = new ColorPicker(Color.BLACK);
    private ComboBox<String> comboBox = new ComboBox<>();
    private Slider slider = new Slider(150, 750, 150);
    private AnchorPane canvas = new AnchorPane();
    private ScrollPane scrollPane = new ScrollPane();
    private MethodChart methodChart;

    public MethodsVBox(String methodName, ScrollBar vScroll, ApplicationContext applicationContext) {
        getChildren().add(hBox);
        getChildren().add(slider);
        getChildren().add(scrollPane);

        methodChart = applicationContext.getBean(MethodChart.class);
        methodChart.setColor(methodName, Color.BLACK);

        setCanvasSettings();
        setHBoxSettings();
        setButtonSettings(applicationContext);
        setColorPickerSettings();
        setComboBoxSettings(methodName);
        setSliderSettings();
        setScrollSettings(vScroll);
    }

    private void setCanvasSettings() {
        setMinWidth(slider.getValue()+20);
        setMaxWidth(slider.getValue()+20);
        canvas.setMaxWidth(slider.getValue()+20);
        canvas.setMinWidth(slider.getValue()+20);
    }

    private void setHBoxSettings() {
        hBox.getChildren().add(addChartButton);
        hBox.getChildren().add(colorPicker);
        hBox.getChildren().add(comboBox);
    }


    private void setButtonSettings(ApplicationContext applicationContext) {
        addChartButton.setOnAction(event -> {
            FxWeaver fxWeaver = applicationContext.getBean(FxWeaver.class);
            CombiningChartsController combiningChartsController = fxWeaver.loadController(CombiningChartsController.class);
            combiningChartsController.setComboBox(comboBox);
            combiningChartsController.show();
        });
    }

    private void setColorPickerSettings() {
        colorPicker.setMaxWidth(28);
        colorPicker.setMinWidth(28);
        colorPicker.setOnAction(event -> {
            methodChart.setColor(comboBox.getValue(), colorPicker.getValue());
            update();
        });
    }

    private void setComboBoxSettings(String methodName) {
        setHgrow(comboBox, Priority.ALWAYS);
        comboBox.getItems().add(methodName);
        comboBox.setValue(methodName);
        comboBox.setMaxWidth(Double.MAX_VALUE);
        comboBox.getItems().addListener((ListChangeListener<String>) c -> update());
        comboBox.setOnAction(event -> {
            colorPicker.setValue(methodChart.getColor(comboBox.getValue()));
            update();
        });
    }

    private void setSliderSettings() {
        slider.setMajorTickUnit(150);
        slider.valueProperty().addListener((observable, oldValue, newValue) -> {
            canvas.setMaxWidth(newValue.doubleValue() + 20);
            canvas.setMinWidth(newValue.doubleValue() + 20);
            setMinWidth(newValue.doubleValue() + 20);
            setMaxWidth(newValue.doubleValue() + 20);
            update();
        });
    }

    private void setScrollSettings(ScrollBar vScroll) {
        setVgrow(scrollPane, Priority.ALWAYS);
        setMargin(scrollPane, new Insets(10, 0, 0, 0));
        scrollPane.setFitToHeight(true);
        scrollPane.setFitToWidth(true);
        scrollPane.setStyle("-fx-background-insets: 0");
        scrollPane.setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
        scrollPane.setVbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
        scrollPane.setContent(canvas);
        scrollPane.setVmax(vScroll.getMax());
        scrollPane.setVmin(vScroll.getMin());
        scrollPane.vvalueProperty().addListener((observable, oldValue, newValue) -> vScroll.setValue(newValue.doubleValue()));
        vScroll.valueProperty().addListener((observable, oldValue, newValue) -> scrollPane.setVvalue(newValue.doubleValue()));
    }


    public void update() {
        canvas.getChildren().clear();
        for (String methodName : comboBox.getItems()) {
            methodChart.drawChart(canvas, methodName, slider.getValue());
        }
        colorPicker.setValue(methodChart.getColor(comboBox.getValue()));
        methodChart.drawMouseCoordinate(canvas, comboBox.getValue(), slider.getValue());
    }

}