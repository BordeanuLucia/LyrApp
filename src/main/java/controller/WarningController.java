package controller;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.control.Label;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.net.URL;
import java.util.ResourceBundle;

public class WarningController implements Initializable {
    private Stage currentStage;
    private Parent currentRoot;
    private Stage previousStage;
    private double posX = 0;
    private double posY = 0;

    @FXML
    public Label warningMessageLabel;

    @Override
    public void initialize(URL location, ResourceBundle resources) { }

    public void configure(Parent root, Stage stage, Stage previousStage, String warningMessage){
        this.currentStage = stage;
        this.currentRoot = root;
        this.previousStage = previousStage;
        warningMessageLabel.setText(warningMessage);
        configureWindow();
    }

    private void configureWindow(){
        this.currentStage.initStyle(StageStyle.UNDECORATED);
        currentStage.initOwner(previousStage);
        currentStage.initModality(Modality.WINDOW_MODAL);

        currentStage.requestFocus();

        currentRoot.setOnMousePressed(e -> {
            posX = currentStage.getX() - e.getScreenX();
            posY = currentStage.getY() - e.getScreenY();
        });
        currentRoot.setOnMouseDragged(e -> {
            currentStage.setX(e.getScreenX() + posX);
            currentStage.setY(e.getScreenY() + posY);
        });
    }

    public void handleExitButtonClicked() { currentStage.close(); }
}
