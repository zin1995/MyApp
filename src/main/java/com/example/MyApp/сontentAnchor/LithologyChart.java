package com.example.MyApp.сontentAnchor;

import javafx.scene.layout.AnchorPane;
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
                double minHeight = (i - 1) * depthMultiplier - start * depthMultiplier;
                double topAnchor = (double) start * depthMultiplier;
                double botAnchor = lithologyData.length * depthMultiplier - topAnchor - minHeight;

                AnchorPane lithologyAnchor = new LithologyAnchor(minHeight, topAnchor, botAnchor, lithologyName);
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

}
