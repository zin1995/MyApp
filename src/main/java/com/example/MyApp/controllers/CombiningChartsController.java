package com.example.MyApp.controllers;

import com.example.MyApp.LasParser;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.scene.control.ComboBox;
import javafx.scene.control.ListView;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import net.rgielen.fxweaver.core.FxmlView;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
@FxmlView("/fxml/chartsCombiningWindow.fxml")
public class CombiningChartsController {

    @Autowired
    LasParser lasParser;
    @FXML
    AnchorPane anchorPane;
    @FXML
    ListView listView1;
    @FXML
    ListView listView2;

    private ComboBox<String> comboBox;

    @FXML
    private void rightActions(ActionEvent actionEvent) {
        String methodName = (String) listView1.getSelectionModel().getSelectedItem();
        if (methodName != null) {
            if (methodName != listView1.getItems().get(0)) {
                listView2.getItems().add(methodName);
                comboBox.getItems().remove(methodName);
                listView1.getItems().remove(methodName);
            }
        }
    }

    @FXML
    private void leftActions(ActionEvent actionEvent) {
        String methodName = (String) listView2.getSelectionModel().getSelectedItem();
        if (methodName != null) {
            comboBox.getItems().add(methodName);
            listView1.getItems().add(methodName);
            listView2.getItems().remove(methodName);
        }
    }

    public void setComboBox(ComboBox<String> comboBox) {
        this.comboBox = comboBox;
        listView1.getItems().addAll(comboBox.getItems());
        listView2.getItems().addAll(lasParser.getMethodsNames());
        listView2.getItems().removeAll(listView1.getItems());
    }


    public void show() {
        Stage stage = new Stage();
        stage.setScene(new Scene(anchorPane));
        stage.initModality(Modality.APPLICATION_MODAL);
        stage.show();
    }
}
