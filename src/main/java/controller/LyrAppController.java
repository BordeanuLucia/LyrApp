package controller;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import repository.IPlaylistsRepository;
import repository.ISongsRepository;
import repository.PlaylistsRepository;
import repository.SongsRepository;
import service.LyrAppService;

import java.net.URL;
import java.util.ResourceBundle;

public class LyrAppController implements Initializable {
    private static LyrAppService lyrAppService;

    @FXML
    private TextField songSearchTextField;

    static {
        ISongsRepository songsRepository = new SongsRepository();
        IPlaylistsRepository playlistRepository = new PlaylistsRepository();
        lyrAppService = new LyrAppService(songsRepository, playlistRepository);
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
    }

    @FXML
    public void searchKeyPressed(KeyEvent key){
        String keyWords = songSearchTextField.getText().strip();
        if (!key.getCode().equals(KeyCode.ENTER) || keyWords.equals(""))
            return;
        System.out.println(lyrAppService.getFilteredSongs(keyWords));
    }
}