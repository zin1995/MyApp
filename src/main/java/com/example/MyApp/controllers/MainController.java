package com.example.MyApp.controllers;

import com.example.MyApp.LasParser;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;
import net.rgielen.fxweaver.core.FxWeaver;
import net.rgielen.fxweaver.core.FxmlView;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

import java.io.File;

@Component
@FxmlView("/fxml/main.fxml")
public class MainController {

    @Autowired
    ApplicationContext applicationContext;
    @Autowired
    ContentController contentController;
    @Autowired
    LasParser lasParser;
    @FXML
    VBox root;
    @FXML
    MenuItem openFile;
    @FXML
    Menu methodsMenu;
    @FXML
    Menu editMenu;
    @FXML
    Button lithology;
    @FXML
    AnchorPane content;

    @FXML
    private void openFile(ActionEvent e) {
        FileChooser chooser = new FileChooser();
        chooser.setTitle("Open File");
        chooser.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter(".las", "*.las"));
        chooser.setInitialDirectory(new File("D:\\Study\\Примеры для камертона"));
        File file = chooser.showOpenDialog(openFile.getParentPopup().getScene().getWindow());

        if (file != null) {
            lasParser.clearData();
            lasParser.parsing(file);

            content.getChildren().clear();
            addContent();

            lithology.setDisable(false);
            updateMethodMenu();
        }
    }

    @FXML
    private void showLithology() {
        FxWeaver fxWeaver = applicationContext.getBean(FxWeaver.class);
        fxWeaver.loadController(LithologyController.class).show();
    }

    private void addContent() {
        FxWeaver fxWeaver = applicationContext.getBean(FxWeaver.class);
        content.getChildren().add(fxWeaver.loadView(ContentController.class));
    }

    private void updateMethodMenu() {
        for (String s : lasParser.getMethodsNames()) {
            RadioMenuItem radioMenuItem = new RadioMenuItem(s.trim());
            radioMenuItem.setSelected(true);
            methodsMenu.getItems().add(radioMenuItem);
            radioMenuItem.setOnAction(event -> {
                if (radioMenuItem.isSelected()) {
                    contentController.restorePanel(s);
                } else contentController.deletePanel(s);
            });
        }
    }

}
