package controller;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.stage.Stage;

import java.net.URL;
import java.util.ResourceBundle;

public class WarningController extends AbstractUndecoratedController implements Initializable {
    private Stage currentStage;

    @FXML
    public Label warningMessageLabel;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        // we have nothing to initialize
    }

    public void configure(Stage stage, Stage previousStage, String warningMessage){
        this.currentStage = stage;
        warningMessageLabel.setText(warningMessage);
        configureUndecoratedWindow(currentStage, previousStage);
    }

    public void handleExitButtonClicked() { currentStage.close(); }
}
