package com.example.MyApp.charts;

import com.example.MyApp.LasParser;
import javafx.scene.layout.AnchorPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Line;
import javafx.scene.text.Text;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

@Component
public class MethodChart {

    @Autowired
    LasParser lasParser;
    private int depthMultiplier = 2;
    private Map<String, Color> colorMap = new HashMap();

    public void setDepthMultiplier(int depthMultiplier) {
        this.depthMultiplier = depthMultiplier;
    }

    public Color getColor(String methodName) {
        return colorMap.get(methodName);
    }

    public void setColor(String methodName, Color color) {
        colorMap.put(methodName, color);
    }

    public void drawChart(AnchorPane anchorPane, String methodName, double width) {
        double[] methodData = lasParser.getMethodsData().get(methodName);
        double minXValue = calculateMinValue(methodData);
        double maxXValue = calculateMaxValue(methodData);
        double xDivider = (maxXValue - minXValue) / width;
        if (minXValue == maxXValue) minXValue = 0.0;
        Color color = colorMap.get(methodName);

        double lastXPoint = (methodData[0] - minXValue) / xDivider + 10;
        double lastYPoint = 0;
        for (int i = 0; i < methodData.length; i++) {
            double currentYPoint = i * depthMultiplier;
            double currentXPoint = (methodData[i] - minXValue) / xDivider + 10;

            if (i % 5 == 0) {
                Line line = new Line(0, currentYPoint, width + 20, currentYPoint);
                line.setStrokeWidth(0.1);
                anchorPane.getChildren().add(line);
            }

            Line line = new Line(lastXPoint, lastYPoint, currentXPoint, currentYPoint);
            line.setStroke(color);
            anchorPane.getChildren().add(line);
            lastXPoint = currentXPoint;
            lastYPoint = currentYPoint;
        }
    }


    private double calculateMinValue(double[] methodData) {
        double minXValue = Double.MAX_VALUE;
        for (double xValue : methodData) {
            if (xValue < 0) continue;
            if (minXValue > xValue) minXValue = xValue;
        }
        return minXValue;
    }

    private double calculateMaxValue(double[] methodData) {
        double maxXValue = 0.0;
        for (double xValue : methodData) {
            if (maxXValue < xValue) maxXValue = xValue;
        }
        return maxXValue;
    }


    public void drawMouseCoordinate(AnchorPane anchorPane, String methodName, double widthMultiplier) {
        double[] methodData = lasParser.getMethodsData().get(methodName);
        double minXValue = calculateMinValue(methodData);
        double maxXValue = calculateMaxValue(methodData);
        double xDivider = (maxXValue - minXValue) / (widthMultiplier);
        double deptStep = lasParser.getDeptStep();
        double dept0 = lasParser.getDeptData()[0];

        anchorPane.setOnMouseExited(event -> {
            if (anchorPane.getChildren().get(anchorPane.getChildren().size() - 1) instanceof Text) {
                anchorPane.getChildren().remove(anchorPane.getChildren().size() - 4, anchorPane.getChildren().size());
            }
        });

        anchorPane.setOnMouseMoved(event -> {
            if (anchorPane.getChildren().get(anchorPane.getChildren().size() - 1) instanceof Text) {
                anchorPane.getChildren().remove(anchorPane.getChildren().size() - 4, anchorPane.getChildren().size());
            }

            if (event.getY() / depthMultiplier > 0 && event.getY() / depthMultiplier < methodData.length - 1) {
                Line line1 = new Line(event.getX(), 0, event.getX(), event.getY());
                line1.setStrokeWidth(0.1);
                double xPoint = (methodData[(int) Math.round(event.getY() / depthMultiplier)] - minXValue) / xDivider + 10;
                Line line2 = new Line(xPoint, event.getY(), event.getX(), event.getY());
                line2.setStrokeWidth(0.1);

                anchorPane.getChildren().add(line1);
                anchorPane.getChildren().add(line2);
                anchorPane.getChildren().add(new Text(event.getX(), event.getY() - 1, String.format("%.1f", event.getY() * deptStep / depthMultiplier + dept0)));
                anchorPane.getChildren().add(new Text(event.getX(), event.getY() - 10, methodData[(int) Math.round(event.getY() / depthMultiplier)] + ""));
            }
        });
    }
}

