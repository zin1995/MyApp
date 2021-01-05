package com.example.MyApp.controllers;

import com.example.MyApp.charts.SaturationChart;
import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.MenuItem;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import net.rgielen.fxweaver.core.FxmlView;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

@Component
@Scope("prototype")
@FxmlView("/fxml/saturationVBox.fxml")
public class SaturationVBoxController extends VBox implements Updatable {

    @Autowired
    SaturationChart saturationChart;
    @FXML
    VBox vBox;
    @FXML
    ComboBox<String> comboBox;
    @FXML
    ScrollPane scrollPane;
    @FXML
    AnchorPane anchorPane;

    @FXML
    public void initialize(){
        getChildren().add(vBox);
        setVgrow(vBox, Priority.ALWAYS);

        ContextMenu contextMenu = new ContextMenu();
        MenuItem menuItem = new MenuItem("вода.bmp");
        contextMenu.getItems().add(menuItem);

        scrollPane.setOnMousePressed(event -> {
            if (event.isSecondaryButtonDown()) {
                menuItem.setOnAction(event1 -> {
                    saturationChart.drawSaturation(anchorPane, event.getY(), menuItem.getText());
                });
                scrollPane.setContextMenu(contextMenu);
            }
        });
    }


    @Override
    public void update() {
//        anchorPane.getChildren().clear();
//        saturationChart.drawSaturation(anchorPane);
    }

}
