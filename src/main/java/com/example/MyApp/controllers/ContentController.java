package com.example.MyApp.controllers;

import java.util.HashMap;
import com.example.MyApp.LasParser;
import com.example.MyApp.charts.SaturationChart;
import com.example.MyApp.charts.LithologyChart;
import com.example.MyApp.charts.MethodChart;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import javafx.scene.shape.Line;
import javafx.scene.text.Text;
import net.rgielen.fxweaver.core.FxWeaver;
import net.rgielen.fxweaver.core.FxmlView;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

@Component
@FxmlView("/fxml/content.fxml")
public class ContentController {

    @Autowired
    LasParser lasParser;
    @Autowired
    ApplicationContext applicationContext;
    @Autowired
    MethodChart methodChart;
    @Autowired
    LithologyChart lithologyChart;
    @Autowired
    SaturationChart saturationChart;
    @FXML
    SplitPane splitPane;
    @FXML
    AnchorPane depthPane;
    @FXML
    Slider depthSlider;
    @FXML
    ScrollPane depthScroll;
    @FXML
    ScrollBar vScroll;

    private HashMap<String, VBox> methodPanels = new HashMap<>();
    private int depthMultiplier = 2;

    public ScrollBar getVScroll() {
        return vScroll;
    }

    public int getDepthMultiplier() {
        return depthMultiplier;
    }

    @FXML
    public void initialize() {
        vScroll.setMax(depthScroll.getVmax());
        vScroll.setMin(depthScroll.getVmin());
        vScroll.valueProperty().addListener((observable, oldValue, newValue) -> {
            depthScroll.setVvalue(newValue.doubleValue());
        });
        depthScroll.vvalueProperty().addListener((observable, oldValue, newValue) -> {
                vScroll.setValue(newValue.doubleValue());
        });

        depthSlider.valueProperty().addListener((observable, oldValue, newValue) -> {
            depthMultiplier = newValue.intValue();
            methodChart.setDepthMultiplier(depthMultiplier);
            lithologyChart.setDepthMultiplier(depthMultiplier);
            saturationChart.setDeptMultiplier(depthMultiplier);
            drawAllPanelsContent();
        });

        for (String methodName: lasParser.getMethodsNames()) {
            addMethodPane(methodName);
        }

        drawAllPanelsContent();
    }


    public void deletePanel(String s) {
        splitPane.getItems().remove(methodPanels.get(s));
    }

    public void restorePanel(String s) {
        splitPane.getItems().add(methodPanels.get(s));
    }

    public void addSaturationPane(){
        splitPane.getItems().add(applicationContext.getBean(FxWeaver.class).loadView(SaturationVBoxController.class));
    }

    public void addLithologyPane(VBox lithologyVBox) {
        splitPane.getItems().add(lithologyVBox);
    }

    private void addMethodPane(String methodName) {
        MethodsVBoxController methodsVBoxController = applicationContext.getBean(FxWeaver.class).loadController(MethodsVBoxController.class);
        methodsVBoxController.setMethodName(methodName);
        methodPanels.put(methodName, methodsVBoxController);
        splitPane.getItems().add(methodsVBoxController);
    }

    private void drawContentsDepthPanel() {
        depthPane.getChildren().clear();
        double[] depthData = lasParser.getDeptData();

        for (int i = 0; i < depthData.length; i++) {
            double currentYPoint = i * depthMultiplier + 10;

            Line line = new Line(0, currentYPoint, 2, currentYPoint);
            line.setStrokeWidth(0.4);
            depthPane.getChildren().add(line);

            if (i % 5 == 0) {
                depthPane.getChildren().add(new Line(0, currentYPoint, 10, currentYPoint));
                Line line2 = new Line(0, currentYPoint, depthPane.getMaxWidth(), currentYPoint);
                line2.setStrokeWidth(0.1);
                depthPane.getChildren().add(line2);
                if (depthMultiplier >= 2) {
                    depthPane.getChildren().add(new Text(10, currentYPoint, depthData[i] + ""));
                }
                if (i % 10 == 0 && depthMultiplier < 2)
                    depthPane.getChildren().add(new Text(10, currentYPoint, depthData[i] + ""));
            }
        }
    }


    private void drawAllPanelsContent() {
        drawContentsDepthPanel();
        for(Node node: splitPane.getItems()){
            ((Updatable)node).update();
        }
    }
}
