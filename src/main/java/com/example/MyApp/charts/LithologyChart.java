package com.example.MyApp.charts;

import com.example.MyApp.LasParser;
import com.example.MyApp.controllers.LithologyVBoxController;
import com.example.MyApp.controllers.MethodsVBoxController;
import javafx.scene.layout.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

@Component
public class LithologyChart {
    @Autowired
    LasParser lasParser;
    private int depthMultiplier = 2;

    public void setDepthMultiplier(int depthMultiplier) {
        this.depthMultiplier = depthMultiplier;
    }

    public void drawLithology(AnchorPane anchorPane, HashMap<String, ArrayList<Double[]>> lithologyIntervalsMap, LithologyVBoxController lithologyVBoxController) {
        anchorPane.setMinHeight(lasParser.getDeptData().length * depthMultiplier);
        anchorPane.setMaxHeight(lasParser.getDeptData().length * depthMultiplier);

        for(Map.Entry<String, ArrayList<Double[]>> pair: lithologyIntervalsMap.entrySet()){
            for(int id = 0; id<pair.getValue().size(); id++){
                double start = pair.getValue().get(id)[0];
                double end = pair.getValue().get(id)[1];

                double topAnchor = start * depthMultiplier;
                double botAnchor = lasParser.getDeptData().length * depthMultiplier - end*depthMultiplier;

                AnchorPane changeableAnchor = new ChangeableAnchor(topAnchor, botAnchor, pair.getKey(), id, lithologyVBoxController);
                anchorPane.getChildren().add(changeableAnchor);
            }
        }
    }
}
