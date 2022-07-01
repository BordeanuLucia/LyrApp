package controller;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.stage.Stage;
import model.Playlist;
import model.Song;
import observer.PlaylistObservable;
import observer.SongObservable;
import observer.SongPlaylistObserver;
import service.ILyrAppService;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

public class ConfirmationController extends AbstractUndecoratedController implements Initializable, SongObservable, PlaylistObservable {
    private final List<SongPlaylistObserver> observersList = new ArrayList<>();
    private Song currentSong;
    private Playlist currentPlaylist;
    private Stage currentStage;
    private ILyrAppService service;
    private boolean forSong;

    @FXML
    private Label titleLabel;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        //we have nothing to initialize
    }

    public void configureForSong(Song song, ILyrAppService service, Stage currentStage, Stage previousStage, LyrAppController lyrAppController) {
        addObserver(lyrAppController);
        this.currentSong = song;
        this.service = service;
        this.currentStage = currentStage;

        titleLabel.setText(song.getTitle());
        configureUndecoratedWindow(currentStage, previousStage);
        forSong = true;
    }

    public void configureForPlaylist(Playlist playlist, ILyrAppService service, Stage currentStage, Stage previousStage, LyrAppController lyrAppController) {
        addObserver(lyrAppController);
        this.currentPlaylist = playlist;
        this.service = service;
        this.currentStage = currentStage;

        titleLabel.setText(playlist.getTitle());
        configureUndecoratedWindow(currentStage, previousStage);
        forSong = false;
    }

    @FXML
    public void handleDeleteSong() {
        if (forSong) {
            service.deleteSong(currentSong);
            notifySongDeleted(currentSong);
            currentStage.close();
        } else {
            service.deletePlaylist(currentPlaylist);
            notifyPlaylistDeleted(currentPlaylist);
            currentStage.close();
        }
    }

    @FXML
    public void handleNotDeleteSong() {
        currentStage.close();
    }

    @FXML
    public void handleExitButtonClicked(){
        currentStage.close();
    }

    @Override
    public void addObserver(SongPlaylistObserver observer) {
        if(!observersList.contains(observer))
            observersList.add(observer);
    }

    @Override
    public void removeObserver(SongPlaylistObserver observer) {
        observersList.remove(observer);
    }

    @Override
    public void notifySongAdded(Song song) {
        // it is not needed
    }

    @Override
    public void notifySongUpdated(Song song) {
        // it is not needed
    }

    @Override
    public void notifySongDeleted(Song song) {
        for (SongPlaylistObserver observer : observersList){
            observer.songDeleted(song);
        }
    }

    @Override
    public void notifyPlaylistAdded(Playlist playlist) {
        // it is not needed
    }

    @Override
    public void notifyPlaylistUpdated(Playlist playlist) {
        // it is not needed
    }

    @Override
    public void notifyPlaylistDeleted(Playlist playlist) {
        for (SongPlaylistObserver observer : observersList){
            observer.playlistDeleted(currentPlaylist);
        }
    }
}