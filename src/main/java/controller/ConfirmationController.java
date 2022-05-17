package controller;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;
import model.Song;
import service.ILyrAppService;
import utils.SongWindowType;

import java.net.URL;
import java.util.ResourceBundle;

public class ConfirmationController implements Initializable {
    private Song currentSong;
    private Stage currentStage;
    private ILyrAppService service;

    @Override
    public void initialize(URL location, ResourceBundle resources) { }

    public void configure(Song song, ILyrAppService service, Stage stage){
        this.currentSong = song;
        this.service = service;
        this.currentStage = stage;
    }

    public void handleDeleteSong(MouseEvent mouseEvent) {
        service.deleteSong(currentSong);
        currentStage.close();
    }

    public void handleNotDeleteSong(MouseEvent mouseEvent) {
        currentStage.close();
    }
}
