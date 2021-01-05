package com.example.MyApp.charts;

import com.example.MyApp.controllers.LithologyVBoxController;
import javafx.scene.Cursor;
import javafx.scene.image.Image;
import javafx.scene.layout.*;

public class ChangeableAnchor extends AnchorPane {

    private LithologyVBoxController lithologyVBoxController;
    private String imageName;
    private int id;

    public ChangeableAnchor(double topAnchor, double botAnchor, String imageName, int id, LithologyVBoxController parent) {
        this.imageName = imageName;
        this.id = id;
        this.lithologyVBoxController = parent;

        AnchorPane.setTopAnchor(this, topAnchor);
        AnchorPane.setBottomAnchor(this, botAnchor);
        AnchorPane.setLeftAnchor(this, 0.0);
        AnchorPane.setRightAnchor(this, 0.0);

        setBackground(new Background(new BackgroundImage(
                new Image("file:src/main/resources/image/" + imageName, 20, 20, false, true),
                BackgroundRepeat.REPEAT, BackgroundRepeat.REPEAT, BackgroundPosition.DEFAULT, BackgroundSize.DEFAULT)));

        setMouseEvent();
    }



    private void setMouseEvent() {
        setOnMouseMoved(event -> {
            if (event.getY() < 3 || event.getY() > getHeight() - 3) {
                setCursor(Cursor.N_RESIZE);
            } else setCursor(Cursor.DEFAULT);
        });

        setOnMouseDragged(event -> {
            if (event.getY() > getHeight() / 2 && getCursor().equals(Cursor.N_RESIZE)) {
                AnchorPane.setBottomAnchor(this, AnchorPane.getBottomAnchor(this) + (getHeight() - event.getY()));
            }
            if (event.getY() < getHeight() / 2 && getCursor().equals(Cursor.N_RESIZE)) {
                AnchorPane.setTopAnchor(this, AnchorPane.getTopAnchor(this) + event.getY());
            }
        });

        setOnMouseReleased(event -> {
            lithologyVBoxController.setChanges(imageName, id, getTopAnchor(this), getHeight());
        });
    }
}

