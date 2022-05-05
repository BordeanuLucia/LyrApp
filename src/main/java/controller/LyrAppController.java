package controller;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import model.Song;
import repository.IPlaylistsRepository;
import repository.ISongsRepository;
import repository.PlaylistsRepository;
import repository.SongsRepository;
import service.LyrAppService;

import java.net.URL;
import java.util.ResourceBundle;

public class LyrAppController implements Initializable {
    private static final LyrAppService lyrAppService;

    @FXML
    private TextField songSearchTextField;
    @FXML
    private ListView<Song> songsListView;
    ObservableList<Song> songsModel = FXCollections.observableArrayList();

    static {
        ISongsRepository songsRepository = new SongsRepository();
        IPlaylistsRepository playlistRepository = new PlaylistsRepository();
        lyrAppService = new LyrAppService(songsRepository, playlistRepository);
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        songsListView.setItems(songsModel);
        songsListView.setCellFactory(param -> new ListCell<>() {
            @Override
            protected void updateItem(Song item, boolean empty) {
                super.updateItem(item, empty);

                if (empty || item == null) {
                    setText(null);
                } else {
                    if (item.getTitle() == null || item.getTitle().strip().equals(""))
                        setText("No title");
                    else
                        setText(item.getTitle());
                }
            }
        });
    }

    @FXML
    public void searchKeyPressed(KeyEvent key){
        String keyWords = songSearchTextField.getText().strip();
        if (!key.getCode().equals(KeyCode.ENTER) || keyWords.equals(""))
            return;
        songsModel.setAll(lyrAppService.getFilteredSongs(keyWords));
    }
}