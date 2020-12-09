package com.example.MyApp.controllers;

import com.example.MyApp.LasParser;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.stage.Stage;
import javafx.util.converter.DoubleStringConverter;
import net.rgielen.fxweaver.core.FxmlView;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.File;
import java.util.ArrayList;
import java.util.regex.Pattern;

@Component
@FxmlView("/fxml/createLithology.fxml")
public class LithologyController {

    @Autowired
    LasParser lasParser;
    @Autowired
    ContentController contentController;
    @FXML
    VBox firstVBox;
    @FXML
    VBox secondVBox;
    @FXML
    ComboBox<String> comboBox1;
    @FXML
    ComboBox<String> comboBox2;
    @FXML
    ComboBox<String> comboBox3;
    @FXML
    AnchorPane anchorPane;

    private Stage stage;
    private ArrayList<HBox> firstPaneHBox = new ArrayList<>();
    private ArrayList<HBox> secondPaneHBox = new ArrayList<>();

    @FXML
    public void initialize() {
        this.stage = new Stage();
        stage.setScene(new Scene(anchorPane));

        setComboBoxSettings();
        new FirstLithologyIntervalsHBox(createTextField());
        new SecondLithologyIntervalsHBox(createTextField(), createTextField());
    }

    @FXML
    private void OkAction(ActionEvent actionEvent) {
        String[] lithologyData = new String[lasParser.getDeptData().length];

        if (comboBox1.getValue() != null) {
            for (HBox hBox : firstPaneHBox) {
                TextField tf1 = (TextField) hBox.getChildren().get(0);
                TextField tf2 = (TextField) hBox.getChildren().get(1);
                String lithologyName = ((ComboBox<String>) hBox.getChildren().get(2)).getValue();
                if (tf1.getText() != null && tf2.getText() != null && lithologyName != null) {
                    String[] data = calculateLithologyData(tf1.getText(), tf2.getText(), lithologyName, comboBox1.getValue());
                    for (int i = 0; i < data.length; i++) {
                        if (data[i] != null) lithologyData[i] = data[i];
                    }
                }
            }
            contentController.addLithologyPane(lithologyData);
        }

        if (comboBox2.getValue() != null && comboBox3.getValue() != null && comboBox2.getValue() != comboBox3.getValue()) {
            for (HBox hBox : secondPaneHBox) {
                TextField tf1 = (TextField) hBox.getChildren().get(1);
                TextField tf2 = (TextField) hBox.getChildren().get(2);
                TextField tf3 = (TextField) hBox.getChildren().get(4);
                TextField tf4 = (TextField) hBox.getChildren().get(5);
                String lithologyName = ((ComboBox<String>) hBox.getChildren().get(6)).getValue();
                if (tf1.getText() != null && tf2.getText() != null && tf3.getText() != null && tf4.getText() != null && lithologyName != null) {
                    String[] data1 = calculateLithologyData(tf1.getText(), tf2.getText(), lithologyName, comboBox2.getValue());
                    String[] data2 = calculateLithologyData(tf3.getText(), tf4.getText(), lithologyName, comboBox3.getValue());
                    for (int i = 0; i < data1.length; i++) {
                        if (data1[i] == null || data2 == null) continue;
                        if (data1[i].equals(data2[i])) lithologyData[i] = data1[i];
                    }
                }
            }
            contentController.addLithologyPane(lithologyData);
        }

        stage.close();
    }

    public String[] calculateLithologyData(String tf1, String tf2, String lithologyName, String methodName) {
        double[] methodData = lasParser.getMethodsData().get(methodName);
        String[] lithologyData = new String[methodData.length];
        double start = Double.parseDouble(tf1);
        double end = Double.parseDouble(tf2);
        for (int i = 0; i < methodData.length; i++) {
            if (methodData[i] >= start && methodData[i] < end) {
                lithologyData[i] = lithologyName;
            }
        }
        return lithologyData;
    }

    private void setComboBoxSettings() {
        comboBox1.getItems().addAll(lasParser.getMethodsNames());
        comboBox1.setOnAction(event -> {
            comboBox2.setValue(null);
            comboBox3.setValue(null);
            secondPaneHBox.clear();
            secondVBox.getChildren().clear();
            new SecondLithologyIntervalsHBox(createTextField(), createTextField());
        });

        comboBox2.getItems().addAll(lasParser.getMethodsNames());
        comboBox2.setOnAction(event -> {
            comboBox1.setValue(null);
            firstPaneHBox.clear();
            firstVBox.getChildren().clear();
            new FirstLithologyIntervalsHBox(createTextField());
        });
        comboBox3.getItems().addAll(lasParser.getMethodsNames());
        comboBox3.setOnAction(event -> {
            comboBox1.setValue(null);
            firstPaneHBox.clear();
            firstVBox.getChildren().clear();
            new FirstLithologyIntervalsHBox(createTextField());
        });
    }

    private TextField createTextField() {
        TextField textField = new TextField();
        textField.setMaxWidth(70);
        textField.setMinWidth(70);
        textField.setTextFormatter(new TextFormatter<Double>(new DoubleStringConverter(), 0.0, change -> {
            Pattern DIGIT_PATTERN = Pattern.compile("\\d*\\.*\\d*");
            return DIGIT_PATTERN.matcher(change.getText()).matches() ? change : null;
        }));
        return textField;
    }

    private ComboBox<String> createLithologyComboBox() {
        ComboBox<String> comboBox = new ComboBox<>();
        comboBox.setMaxWidth(200);
        File[] files = new File("src/main/resources/image/").listFiles();
        for (File file : files) {
            comboBox.getItems().add(file.getName());
        }
        return comboBox;
    }

    private class FirstLithologyIntervalsHBox extends HBox {
        TextField textField = createTextField();
        TextField textField2 = createTextField();
        ComboBox<String> comboBox = createLithologyComboBox();
        Button button = new Button("+");

        public FirstLithologyIntervalsHBox(TextField tf) {
            textField.setText(tf.getText());
            getChildren().add(textField);

            textField2.setText("999999");
            getChildren().add(textField2);

            setHgrow(comboBox, Priority.ALWAYS);
            comboBox.setMaxWidth(Double.MAX_VALUE);
            getChildren().add(comboBox);

            button.setMinWidth(28);
            button.setOnAction(event -> new FirstLithologyIntervalsHBox(textField2));
            getChildren().add(button);

            firstVBox.getChildren().add(this);
            firstPaneHBox.add(this);
        }
    }

    private class SecondLithologyIntervalsHBox extends HBox {
        Label labK1 = new Label("Kр1");
        TextField textField = createTextField();
        TextField textField2 = createTextField();
        Label labK2 = new Label("Kр2");
        TextField textField3 = createTextField();
        TextField textField4 = createTextField();
        ComboBox<String> comboBox = createLithologyComboBox();
        Button button = new Button("+");

        public SecondLithologyIntervalsHBox(TextField tf, TextField tf2) {
            setAlignment(Pos.CENTER_LEFT);

            labK1.setMinWidth(24);
            getChildren().add(labK1);

            textField.setText(tf.getText());
            getChildren().add(textField);

            textField2.setText("999999");
            getChildren().add(textField2);

            labK2.setMinWidth(24);
            getChildren().add(labK2);

            textField3.setText(tf2.getText());
            getChildren().add(textField3);

            textField4.setText("999999");
            getChildren().add(textField4);

            setHgrow(comboBox, Priority.ALWAYS);
            comboBox.setMaxWidth(Double.MAX_VALUE);
            getChildren().add(comboBox);

            button.setMinWidth(28);
            button.setOnAction(event -> new SecondLithologyIntervalsHBox(textField2, textField4));
            getChildren().add(button);

            secondVBox.getChildren().add(this);
            secondPaneHBox.add(this);
        }
    }

    @FXML
    private void closeAction(ActionEvent actionEvent) {
        stage.close();
    }

    public void show() {
        stage.show();
    }
}

