package controller;

import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Rectangle2D;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.stage.Screen;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import model.Song;
import repository.IPlaylistsRepository;
import repository.ISongsRepository;
import repository.PlaylistsRepository;
import repository.SongsRepository;
import service.LyrAppService;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;
import java.util.List;

public class LyrAppController implements Initializable {
    private static final LyrAppService lyrAppService;
    private Screen currentScreen;
    private ObservableList<Screen> allScreens;
    private final List<LiveController> liveControllers = new ArrayList<>();

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

    public LyrAppController() { }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        songsListView.setItems(songsModel);
        // to make only the title appear as a list item
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

    public void configure(){
        allScreens = Screen.getScreens();
        allScreens.addListener((ListChangeListener<Screen>) c -> {
            while (c.next()) {
                if (c.wasAdded()) {
                    int MAX_RETRY = 3;
                    for (Screen addedScreen : c.getAddedSubList()) {
                        int retryCount = 0;
                        while (retryCount < MAX_RETRY) {
                            retryCount++;
                            try {
                                createLiveSceneForScreen(addedScreen);
                                break;
                            } catch (IOException ignored) { }
                        }
                    }
                } else if (c.wasRemoved()) {
                    for (Screen removedScreen : c.getRemoved()) {
                        for (LiveController liveController : liveControllers){
                            if (liveController.getCurrentScreen().equals(removedScreen)){
                                liveController.closeWindow();
                                liveControllers.remove(liveController);
                            }
                        }
                    }
                }
            }
        });
        currentScreen = Screen.getPrimary();

        try {
            for (Screen screen : Screen.getScreens()) {
                createLiveSceneForScreen(screen);
            }
        } catch (IOException exception) {
            exception.printStackTrace();
        }
    }

    private void createLiveSceneForScreen(Screen screen) throws IOException {
        FXMLLoader liveLoader = new FXMLLoader(getClass().getClassLoader().getResource("user_interface\\LiveWindow.fxml"));
        Parent liveRoot = liveLoader.load();
        Scene liveScene = new Scene(liveRoot);
        LiveController liveController = liveLoader.getController();
        liveControllers.add(liveController);
        liveController.configure(screen);

        Rectangle2D bounds = screen.getVisualBounds();
        Stage liveStage = new Stage();
        liveStage.initStyle(StageStyle.UNDECORATED);
        liveStage.setScene(liveScene);

        liveStage.setX(bounds.getMinX());
        liveStage.setY(bounds.getMinY());

        liveStage.show();
    }

    @FXML
    public void handleLiveButtonClicked(){
    }

    @FXML
    public void searchKeyPressed(KeyEvent key){
        String keyWords = songSearchTextField.getText().strip();
        if (!key.getCode().equals(KeyCode.ENTER) || keyWords.equals(""))
            return;
        songsModel.setAll(lyrAppService.getFilteredSongs(keyWords));
    }
}