package controller;

import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Pos;
import javafx.geometry.Rectangle2D;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.text.TextAlignment;
import javafx.stage.Screen;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import model.Song;
import model.Strophe;
import repository.IPlaylistsRepository;
import repository.ISongsRepository;
import repository.PlaylistsRepository;
import repository.SongsRepository;
import service.LyrAppService;

import java.io.IOException;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.ResourceBundle;
import java.util.List;

public class LyrAppController implements Initializable {
    private static final LyrAppService lyrAppService;
    private Screen currentScreen;
    private ObservableList<Screen> allScreens;
    private final List<LiveController> liveControllers = new ArrayList<>();
    private volatile boolean stopClock = false;

    @FXML
    private TextField songSearchTextField;
    @FXML
    private ListView<Song> songsListView;
    ObservableList<Song> songsModel = FXCollections.observableArrayList();
    @FXML
    private ListView<Strophe> strophesListView;
    ObservableList<Strophe> strophesModel = FXCollections.observableArrayList();
    @FXML
    private Label titleLabel;
    @FXML
    private Label textLabel;
    @FXML
    private RadioButton upLeftButton;
    @FXML
    private Label hourLabel;

    static {
        ISongsRepository songsRepository = new SongsRepository();
        IPlaylistsRepository playlistRepository = new PlaylistsRepository();
        lyrAppService = new LyrAppService(songsRepository, playlistRepository);
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        initializeListViews();
        initializeRadioButtons();
        initializeClock();
    }

    private void initializeClock() {
        Thread thread = new Thread(() -> {
            SimpleDateFormat sdf = new SimpleDateFormat("HH:mm");
            while (!stopClock) {
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException ignored) { }
                String timeNow = sdf.format(new Date());
                Platform.runLater(() -> {
                    hourLabel.setText(timeNow);
                    for (LiveController liveController : liveControllers){
                        liveController.setClockLabel(timeNow);
                    }
                });
            }
        });
        thread.start();
    }

    private void initializeRadioButtons() {
        ToggleGroup group = upLeftButton.getToggleGroup();
        group.selectedToggleProperty().addListener((observableValue, oldToggle, newToggle) -> {
            if (group.getSelectedToggle() != null) {
                RadioButton selectedAlignment = (RadioButton) group.getSelectedToggle();
                Pos alignment = Pos.BASELINE_CENTER;
                TextAlignment textAlignment = TextAlignment.CENTER;
                switch (selectedAlignment.getId()){
                    case "upLeftButton":
                        alignment = Pos.TOP_LEFT;
                        textAlignment = TextAlignment.LEFT;
                        break;
                    case "upCenterButton":
                        alignment = Pos.TOP_CENTER;
                        textAlignment = TextAlignment.CENTER;
                        break;
                    case "upRightButton":
                        alignment = Pos.TOP_RIGHT;
                        textAlignment = TextAlignment.RIGHT;
                        break;
                    case "centerLeftButton":
                        alignment = Pos.CENTER_LEFT;
                        textAlignment = TextAlignment.LEFT;
                        break;
                    case "centerCenterButton":
                        alignment = Pos.CENTER;
                        textAlignment = TextAlignment.CENTER;
                        break;
                    case "centerRightButton":
                        alignment = Pos.CENTER_RIGHT;
                        textAlignment = TextAlignment.RIGHT;
                        break;
                    case "downLeftButton":
                        alignment = Pos.BOTTOM_LEFT;
                        textAlignment = TextAlignment.LEFT;
                        break;
                    case "downCenterButton":
                        alignment = Pos.BOTTOM_CENTER;
                        textAlignment = TextAlignment.CENTER;
                        break;
                    case "downRightButton":
                        alignment = Pos.BOTTOM_RIGHT;
                        textAlignment = TextAlignment.RIGHT;
                        break;
                }
                textLabel.setAlignment(alignment);
                textLabel.setTextAlignment(textAlignment);
                for (LiveController liveController : liveControllers){
                    liveController.setTextAlignment(alignment, textAlignment);
                }
            }
        });
    }

    private void initializeListViews() {
        songsModel.setAll(lyrAppService.getAllSongs());
        songsListView.setItems(songsModel);
        songsListView.getSelectionModel().select(-1);
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
        songsListView.setOnMouseClicked(event -> {
            Song selectedSong = songsListView.getSelectionModel().getSelectedItem();
            titleLabel.setText(selectedSong.getTitle());
            strophesModel.setAll(selectedSong.getOrderedLyrics());
        });

        strophesListView.setItems(strophesModel);
        strophesListView.getSelectionModel().select(-1);
        strophesListView.setCellFactory(param -> new ListCell<>() {
            @Override
            protected void updateItem(Strophe item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || item == null || item.getText() == null || item.getText().strip().equals("")) {
                    setText(null);
                } else {
                    setText(item.getText());
                }
            }
        });
        strophesListView.setOnMouseClicked(event -> {
            Strophe selectedStrophe = strophesListView.getSelectionModel().getSelectedItem();
            textLabel.setText(selectedStrophe.getText());
            for (LiveController liveController : liveControllers){
                liveController.setTextLabel(selectedStrophe.getText());
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

        if (allScreens.size() > 1) {
            try {
                for (Screen screen : allScreens) {
                    if (!screen.equals(currentScreen))
                        createLiveSceneForScreen(screen);
                }
            } catch (IOException exception) {
                exception.printStackTrace();
            }
        } else {
            try {
                createLiveSceneForScreen(currentScreen);
            } catch (IOException exception) {
                exception.printStackTrace();
            }
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

    @FXML
    public void clockButtonClicked() {
        hourLabel.setVisible(!hourLabel.isVisible());
        for (LiveController liveController : liveControllers){
            liveController.setClockVisibility(hourLabel.isVisible());
        }
    }

    public void close() { stopClock = true; }
}