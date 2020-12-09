package com.example.MyApp.сontentAnchor;

import javafx.scene.Cursor;
import javafx.scene.image.Image;
import javafx.scene.layout.*;

public class LithologyAnchor extends AnchorPane {

    public LithologyAnchor(double minHeight, double topAnchor, double botAnchor, String imageName) {
        setMinHeight(minHeight);
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
            if ((event.getY() < getMinHeight() + 3 && event.getY() > getMinHeight() - 3)
                    || (event.getY() < 0 + 3 && event.getY() > 0 - 3)) {
                setCursor(Cursor.N_RESIZE);
            } else setCursor(Cursor.DEFAULT);
        });

        setOnMouseDragged(event -> {
            if ((event.getY() < getMinHeight() + 3 && event.getY() > getMinHeight() - 3) || (event.getY() < 0 + 3 && event.getY() > 0 - 3)) {
                if (getMinHeight() < event.getY()) {
                    AnchorPane.setBottomAnchor(this, AnchorPane.getBottomAnchor(this) - 2);
                    setMinHeight(getMinHeight() + 2);
                }
                if (getMinHeight() > event.getY() && getMinHeight() - 3 < event.getY()) {
                    AnchorPane.setBottomAnchor(this, AnchorPane.getBottomAnchor(this) + 2);
                    setMinHeight(getMinHeight() - 2);
                }
                if (event.getY() < 0) {
                    setMinHeight(getMinHeight() + 2);
                    AnchorPane.setTopAnchor(this, AnchorPane.getTopAnchor(this) - 2);
                }
                if (event.getY() > 0 && event.getY() < 3) {
                    setMinHeight(getMinHeight() - 2);
                    AnchorPane.setTopAnchor(this, AnchorPane.getTopAnchor(this) + 2);
                }
            }
        });
    }
}
