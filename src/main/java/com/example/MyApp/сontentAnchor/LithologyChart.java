package com.example.MyApp.сontentAnchor;

import javafx.scene.Cursor;
import javafx.scene.image.Image;
import javafx.scene.layout.*;
import org.springframework.stereotype.Component;

@Component
public class LithologyChart {
    private int depthMultiplier = 2;

    public void drawLithology(AnchorPane anchorPane, String[] lithologyData) {
        anchorPane.setMinHeight(lithologyData.length * depthMultiplier);
        anchorPane.setMaxHeight(lithologyData.length * depthMultiplier);

        String lithologyName = lithologyData[0];
        int start = 0;
        for (int i = 0; i < lithologyData.length; i++) {
            if (lithologyName != null && !lithologyName.equals(lithologyData[i])) {
                double height = (i - 1) * depthMultiplier - start * depthMultiplier;
                double topAnchor = (double) start * depthMultiplier;
                double botAnchor = lithologyData.length * depthMultiplier - topAnchor - height;

                AnchorPane lithologyAnchor = new LithologyAnchor(topAnchor, botAnchor, lithologyName);
                anchorPane.getChildren().add(lithologyAnchor);

                start = i;
                lithologyName = lithologyData[i];
            }
            if (lithologyName == null) {
                lithologyName = lithologyData[i];
                start = i;
            }
        }
    }

    public void setDepthMultiplier(int depthMultiplier) {
        this.depthMultiplier = depthMultiplier;
    }



    private class LithologyAnchor extends AnchorPane {

        public LithologyAnchor(double topAnchor, double botAnchor, String imageName) {
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
                if (event.getY() > getHeight()/2) {
                    AnchorPane.setBottomAnchor(this, AnchorPane.getBottomAnchor(this) + (getHeight() - event.getY()));
                }
                if (event.getY() < getHeight()/2) {
                    AnchorPane.setTopAnchor(this, AnchorPane.getTopAnchor(this) + event.getY());
                }
            });
        }
    }

}
