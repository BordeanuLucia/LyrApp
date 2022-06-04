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
import service.ILyrAppService;
import utils.Constants;

import java.util.HashSet;
import java.util.List;
import java.net.URL;
import java.util.ResourceBundle;


public class PlaylistController extends AbstractUndecoratedController implements Initializable {
    private ILyrAppService service;
    private Stage currentStage;

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

    public void configure(ILyrAppService service, Stage currentStage, Stage previousStage, List<Song> allSongs){
        this.service = service;
        this.currentStage = currentStage;
        songsModel.setAll(allSongs);
        songsListView.setItems(songsModel);
        playlistSongsListView.setItems(playlistSongsModel);
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
        if (playlistTitleTextField.getText().strip().equals("")){
            Constants.makeBorderRedForAWhile(playlistTitleTextField);
        } else {
            Playlist playlist = new Playlist(playlistTitleTextField.getText().strip(), new HashSet<>(playlistSongsModel));
            service.addPlaylist(playlist);
            playlistTitleTextField.setText("");
            playlistSongsModel.removeAll();
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
}
