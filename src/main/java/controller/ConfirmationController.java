package controller;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.control.Label;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import model.Song;
import service.ILyrAppService;

import java.net.URL;
import java.util.ResourceBundle;

public class ConfirmationController implements Initializable {
    private Song currentSong;
    private Stage currentStage;
    private Stage previousStage;
    private ILyrAppService service;
    private Parent currentRoot;
    private double posX = 0;
    private double posY = 0;

    @FXML
    private Label titleLabel;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        //we have nothing to initialize
    }

    public void configure(Song song, ILyrAppService service, Stage stage, Stage previousStage) {
        this.currentSong = song;
        this.service = service;
        this.currentStage = stage;
        this.previousStage = previousStage;
        this.currentRoot = currentStage.getScene().getRoot();

        titleLabel.setText(song.getTitle());
        configureWindow();
    }

    private void configureWindow(){
        currentStage.initStyle(StageStyle.UNDECORATED);
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

    @FXML
    public void handleDeleteSong() {
        service.deleteSong(currentSong);
        currentStage.close();
    }

    @FXML
    public void handleNotDeleteSong() {
        currentStage.close();
    }

    @FXML
    public void handleExitButtonClicked(){
        currentStage.close();
    }
}