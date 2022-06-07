package controller;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.text.Font;
import javafx.stage.Stage;
import model.Playlist;
import model.Song;
import observer.PlaylistObservable;
import observer.SongPlaylistObserver;
import service.ILyrAppService;
import utils.Constants;

import java.util.HashSet;
import java.util.List;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.Set;


public class PlaylistController extends AbstractUndecoratedController implements Initializable, PlaylistObservable {
    private ILyrAppService service;
    private Stage currentStage;
    private Playlist currentPlaylist = null;

    @FXML
    private ListView<Song> playlistSongsListView;
    ObservableList<Song> playlistSongsModel = FXCollections.observableArrayList();
    @FXML
    private ListView<Song> songsListView;
    ObservableList<Song> songsModel = FXCollections.observableArrayList();
    @FXML
    private TextField playlistTitleTextField;
    @FXML
    private TextField searchSongTextField;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        //we have nothing to initialize
    }

    public void configure(ILyrAppService service, Stage currentStage, Stage previousStage, LyrAppController lyrAppController, Playlist currentPlaylist){
        this.service = service;
        this.currentStage = currentStage;
        this.currentPlaylist = currentPlaylist;
        songsModel.setAll(service.getAllSongs());
        songsListView.setItems(songsModel);
        if (this.currentPlaylist != null) {
            playlistSongsModel.setAll(currentPlaylist.getSongs());
            playlistTitleTextField.setText(currentPlaylist.getTitle());
        }
        playlistSongsListView.setItems(playlistSongsModel);
        addObserver(lyrAppController);
        songsListView.setCellFactory(param -> new ListCell<>() {
            @Override
            protected void updateItem(Song item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || item == null) {
                    setText(null);
                } else {
                    if (item.getTitle() == null || item.getTitle().strip().equals(""))
                        setText("No title");
                    else {
                        setMinWidth(param.getWidth() - 2);
                        setMaxWidth(param.getWidth() - 2);
                        setPrefWidth(param.getWidth() - 2);
                        setWrapText(true);
                        setText(item.getTitle());
                        setFont(Font.font(15));
                    }
                }
            }
        });
        playlistSongsListView.setCellFactory(param -> new ListCell<>() {
            @Override
            protected void updateItem(Song item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || item == null) {
                    setText(null);
                } else {
                    if (item.getTitle() == null || item.getTitle().strip().equals(""))
                        setText("No title");
                    else {
                        setMinWidth(param.getWidth() - 2);
                        setMaxWidth(param.getWidth() - 2);
                        setPrefWidth(param.getWidth() - 2);
                        setWrapText(true);
                        setText(item.getTitle());
                        setFont(Font.font(15));
                    }
                }
            }
        });
        configureUndecoratedWindow(currentStage, previousStage);
    }

    @FXML
    public void saveButtonClicked( ) {
        if (playlistTitleTextField.getText().strip().equals("")) {
            Constants.makeBorderRedForAWhile(playlistTitleTextField);
        } else {
            if (currentPlaylist == null) {
                Playlist playlist = new Playlist(playlistTitleTextField.getText().strip(), new HashSet<>(playlistSongsModel));
                long id = service.addPlaylist(playlist);
                playlist.setId(id);
                notifyPlaylistAdded(playlist);
            } else {
                currentPlaylist.setTitle(playlistTitleTextField.getText().strip());
                currentPlaylist.setSongs(new HashSet<>(playlistSongsModel));
                service.updatePlaylist(currentPlaylist);
                notifyPlaylistUpdated(currentPlaylist);
            }
            close();
        }
    }

    @FXML
    public void greenArrowClicked() {
        Song selectedSong = songsListView.getSelectionModel().getSelectedItem();
        if (selectedSong != null && !playlistSongsModel.contains(selectedSong)) {
            playlistSongsModel.add(selectedSong);
        }
    }

    @FXML
    public void redArrowClicked() {
        Song selectedSong = playlistSongsListView.getSelectionModel().getSelectedItem();
        if (selectedSong != null) {
            playlistSongsModel.remove(selectedSong);
        }
    }

    @FXML
    public void handleExitButtonClicked() {
        this.currentStage.close();
    }

    @Override
    public void addObserver(SongPlaylistObserver observer) {
        observersList.add(observer);
    }

    @Override
    public void removeObserver(SongPlaylistObserver observer) {
        observersList.remove(observer);
    }

    @Override
    public void notifyPlaylistAdded(Playlist playlist) {
        for (SongPlaylistObserver observer : observersList){
            observer.playlistAdded(playlist);
        }
    }

    @Override
    public void notifyPlaylistUpdated(Playlist playlist) {
        for (SongPlaylistObserver observer : observersList){
            observer.playlistUpdated(playlist);
        }
    }

    @Override
    public void notifyPlaylistDeleted(Playlist playlist) {
        // it is not needed
    }

    private void close(){
        this.currentStage.close();
    }
}
