package controller;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.stage.Stage;
import model.Song;
import service.ILyrAppService;

import java.net.URL;
import java.util.ResourceBundle;

public class ConfirmationController extends AbstractUndecoratedController implements Initializable {
    private Song currentSong;
    private Stage currentStage;
    private ILyrAppService service;

    @FXML
    private Label titleLabel;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        //we have nothing to initialize
    }

    public void configure(Song song, ILyrAppService service, Stage currentStage, Stage previousStage) {
        this.currentSong = song;
        this.service = service;
        this.currentStage = currentStage;

        titleLabel.setText(song.getTitle());
        configureUndecoratedWindow(currentStage, previousStage);
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