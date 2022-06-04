package controller;

import javafx.scene.Parent;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

public abstract class AbstractUndecoratedController {
    private double posX = 0;
    private double posY = 0;

    protected void configureUndecoratedWindow(Stage currentStage, Stage previousStage){
        Parent currentRoot = currentStage.getScene().getRoot();
        currentStage.initStyle(StageStyle.UNDECORATED);
        if (previousStage != null)
            currentStage.initOwner(previousStage);
        currentStage.initModality(Modality.WINDOW_MODAL);
        currentStage.requestFocus();
        currentStage.centerOnScreen();

        currentRoot.setOnMousePressed(e -> {
            posX = currentStage.getX() - e.getScreenX();
            posY = currentStage.getY() - e.getScreenY();
        });
        currentRoot.setOnMouseDragged(e -> {
            currentStage.setX(e.getScreenX() + posX);
            currentStage.setY(e.getScreenY() + posY);
        });
    }
}
