package com.example.MyApp.charts;

import com.example.MyApp.LasParser;
import javafx.scene.layout.AnchorPane;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class SaturationChart {
    @Autowired
    LasParser lasParser;

    private int deptMultiplier = 2;
    private int height;

    public void setDeptMultiplier(int deptMultiplier) {
        this.deptMultiplier = deptMultiplier;
    }

    public void drawSaturation(AnchorPane anchorPane, double y, String saturation) {
        height = deptMultiplier * lasParser.getDeptData().length;
        anchorPane.setMinHeight(height);
        anchorPane.setMaxHeight(height);

        int id = 1;
//
//        AnchorPane changeableAnchor = new ChangeableAnchor(y, height - y - 10, saturation, id);
//        anchorPane.getChildren().add(changeableAnchor);
    }

}
