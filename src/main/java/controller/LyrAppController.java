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
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.text.Font;
import javafx.scene.text.FontPosture;
import javafx.scene.text.FontWeight;
import javafx.scene.text.TextAlignment;
import javafx.stage.Screen;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import model.Playlist;
import model.PlaylistListItem;
import model.Song;
import model.Strophe;
import observer.Observable;
import observer.Observer;
import observer.SongPlaylistObserver;
import repository.*;
import service.LyrAppService;
import utils.Constants;
import utils.SongWindowType;
import utils.UpdateType;

import java.io.IOException;
import java.net.URL;
import java.net.URLConnection;
import java.text.SimpleDateFormat;
import java.util.*;

public class LyrAppController extends AbstractUndecoratedController implements Initializable, Observable, SongPlaylistObserver {
    private static final LyrAppService lyrAppService;
    private volatile boolean stopClock = false;
    private boolean liveButtonClicked = false;
    private Stage currentStage;
    private static final String MENU_BUTTON_CLICKED_STYLE = "-fx-border-color: WHITE; -fx-border-width: 0px 0px 0px 5px; -fx-base: #34526c";

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
    @FXML
    private ImageView runRobotImageView;
    @FXML
    private Button liveButton;
    @FXML
    private Button clockButton;
    @FXML
    private Button exitButton;
    @FXML
    private TextField playlistSearchTextField;
    @FXML
    private ListView<Playlist> playlistListView;
    ObservableList<Playlist> playlistModel = FXCollections.observableArrayList();
    @FXML
    private ImageView updateImageView;
    @FXML
    private ImageView deleteImageView;
    @FXML
    private Label updateLabel;
    @FXML
    private Label deleteLabel;
    @FXML
    private Label browseLabel;
    @FXML
    private ToggleButton boldButton;
    @FXML
    private ToggleButton italicButton;
    @FXML
    private ToggleButton underlineButton;
    @FXML
    private ToggleButton minimizeButton;

    static {
        ISongsRepository songsRepository = new SongsRepository();
        IPlaylistsRepository playlistRepository = new PlaylistsRepository();
        IStrophesRepository strophesRepository = new StrophesRepository();
        lyrAppService = new LyrAppService(songsRepository, playlistRepository, strophesRepository);
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        initializeListViews();
        initializePlaylistListView();
        initializeRadioButtons();
        initializeClock();

        boldButton.setSelected(true);
        clockButton.setStyle(MENU_BUTTON_CLICKED_STYLE);

        Image image1 = new Image("file:src/main/resources/pictures/Icons/delete.png");
        deleteImageView.setImage(image1);
        deleteImageView.setVisible(false);
        deleteImageView.setOnMouseEntered(event -> deleteLabel.setVisible(true));
        deleteImageView.setOnMouseExited(event -> deleteLabel.setVisible(false));
        Image image2 = new Image("file:src/main/resources/pictures/Icons/edit.png");
        updateImageView.setImage(image2);
        updateImageView.setVisible(false);
        updateImageView.setOnMouseEntered(event -> updateLabel.setVisible(true));
        updateImageView.setOnMouseExited(event -> updateLabel.setVisible(false));

        Image image3 = new Image("file:src/main/resources/pictures/Icons/search-engine.png");
        runRobotImageView.setImage(image3);
        runRobotImageView.setOnMouseClicked(event -> {
            String songTitle = songSearchTextField.getText().strip();
            if (songTitle.equals("")) {
                Constants.makeBorderRedForAWhile(songSearchTextField);
            } else {
                handleSearchSongOnlineButtonClicked();
            }
        });
        runRobotImageView.setOnMouseEntered(event -> browseLabel.setVisible(true));
        runRobotImageView.setOnMouseExited(event -> browseLabel.setVisible(false));

        exitButton.setOnMouseClicked(event -> {
            close();
            Platform.exit();
            System.exit(0);
        });

        songSearchTextField.textProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue.strip().equals(""))
                songsModel.setAll(lyrAppService.getAllSongs());
        });
        playlistSearchTextField.textProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue.strip().equals(""))
                playlistModel.setAll(lyrAppService.getAllPlaylists());
        });
    }

    public void configure(Stage mainStage) {
        this.currentStage = mainStage;
        configureScreens();
        configureUndecoratedWindow(currentStage, null);
    }

    private Playlist currentSelectedPlaylist = null;
    private void initializePlaylistListView() {
        playlistModel.setAll(lyrAppService.getAllPlaylists());
        playlistListView.setItems(playlistModel);
        playlistListView.getSelectionModel().select(-1);
        playlistListView.setCellFactory(param -> new PlaylistListItem(param, this));

        playlistListView.setOnMouseClicked(event -> {
            Playlist selectedPlaylist = playlistListView.getSelectionModel().getSelectedItem();
            if (selectedPlaylist != null) {
                currentSelectedPlaylist = lyrAppService.findPlaylist(selectedPlaylist.getId());
                songsModel.setAll(currentSelectedPlaylist.getSongs());
            }
        });
    }

    private void initializeClock() {
        Thread thread = new Thread(() -> {
            SimpleDateFormat sdf = new SimpleDateFormat("HH:mm");
            while (!stopClock) {
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException ignored) {
                }
                String timeNow = sdf.format(new Date());
                Platform.runLater(() -> {
                    hourLabel.setText(timeNow);
                    notifyObservers(UpdateType.SET_HOUR, timeNow);
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
                Pos alignment;
                TextAlignment textAlignment;
                switch (selectedAlignment.getId()) {
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
                    default:
                        alignment = Pos.CENTER;
                        textAlignment = TextAlignment.CENTER;
                        break;
                }
                textLabel.setAlignment(alignment);
                textLabel.setTextAlignment(textAlignment);
                if (liveButtonClicked) {
                    notifyObserversTextAlignment(alignment, textAlignment);
                }
            }
        });
    }

    private long selectedStropheIndex = -1;
    private Song currentSelectedSong = null;
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
                    else {
                        setMinWidth(param.getWidth() - 17);
                        setMaxWidth(param.getWidth() - 17);
                        setPrefWidth(param.getWidth() - 17);
                        setWrapText(true);
                        setText(item.getTitle());
                        setFont(Font.font(15));
                    }
                }
            }
        });

        songsListView.setOnMouseClicked(event -> {
            Song selectedSong = songsListView.getSelectionModel().getSelectedItem();
            if (selectedSong != null) {
                currentSelectedSong = selectedSong;
                if (!updateImageView.isVisible()) {
                    updateImageView.setVisible(true);
                    deleteImageView.setVisible(true);
                }
                titleLabel.setText(selectedSong.getTitle());
                strophesModel.setAll(selectedSong.getOrderedLyrics());
            }
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
                    setMinWidth(param.getWidth() - 16);
                    setMaxWidth(param.getWidth() - 16);
                    setPrefWidth(param.getWidth() - 16);
                    setWrapText(true);
                    setText(item.getText());
                    setFont(Font.font(13));
                }
            }
        });
        strophesListView.setOnMouseClicked(event -> {
            Strophe selectedStrophe = strophesListView.getSelectionModel().getSelectedItem();
            if (selectedStrophe != null) {
                selectedStropheIndex = selectedStrophe.getPosition();
                setPreviewText(selectedStrophe.getText());
                if (liveButtonClicked) {
                    notifyObservers(UpdateType.SET_TEXT, selectedStrophe.getText());
                }
            }
        });
    }

    private void setPreviewText(String text) {
        textLabel.setText(text);
        int numberLines = (307 * Constants.MAX_NUMBER_OF_LINES_ON_SCREEN / Constants.DISPLAY_HEIGHT);
        int numberOfCharacters = (503 * Constants.MAX_NUMBER_OF_CHARACTERS_ON_LINE_ON_SCREEN / Constants.DISPLAY_WIDTH);
        Constants.autoresizeText(text, textLabel, numberLines, numberOfCharacters);
    }

    public void configureScreens() {
        ObservableList<Screen> allScreens = Screen.getScreens();
        Screen currentScreen = Screen.getPrimary();
        allScreens.addListener((ListChangeListener<Screen>) c -> {
            while (c.next()) {
                if (c.wasAdded()) {
                    final int MAX_RETRY = 3;
                    for (Screen addedScreen : c.getAddedSubList()) {
                        int retryCount = 0;
                        while (retryCount < MAX_RETRY) {
                            retryCount++;
                            try {
                                createLiveSceneForScreen(addedScreen);
                                break;
                            } catch (IOException ignored) {
                            }
                        }
                    }
                } else if (c.wasRemoved()) {
                    for (Screen removedScreen : c.getRemoved()) {
                        for (Observer liveController : observersList) {
                            if (liveController.getCurrentScreen().equals(removedScreen)) {
                                liveController.closeWindow();
                                removeObserver(liveController);
                            }
                        }
                    }
                }
            }
        });

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
        addObserver(liveController);
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
    public void handleSettingsButtonClicked() { }

    @FXML
    public void handleLiveButtonClicked() {
        liveButtonClicked = !liveButtonClicked;
        if (liveButtonClicked) {
            notifyObservers(UpdateType.SET_TEXT, textLabel.getText());
            notifyObserversTextAlignment(textLabel.getAlignment(), textLabel.getTextAlignment());
            liveButton.setStyle(MENU_BUTTON_CLICKED_STYLE);
        } else {
            notifyObservers(UpdateType.SET_TEXT, "");
            liveButton.setStyle("-fx-border-width: 0px 0px 0px 0px; -fx-base:  #2c4358");
        }
    }

    @FXML
    public void handleAddButtonClicked() {
        try {
            FXMLLoader confirmationLoader = new FXMLLoader(getClass().getClassLoader().getResource("user_interface\\SongWindow.fxml"));
            Parent songRoot = confirmationLoader.load();
            Scene songScene = new Scene(songRoot);
            SongController songController = confirmationLoader.getController();

            Stage songStage = new Stage();
            songStage.centerOnScreen();
            songStage.setScene(songScene);
            songController.configure(null, SongWindowType.ADD, lyrAppService, songStage, currentStage, this, onlineLyrics);
            songStage.show();
            onlineLyrics = "";
        } catch (Exception ignored) {
        }
    }

    @FXML
    public void handleCreatePlaylistButtonClicked() {
        try {
            FXMLLoader confirmationLoader = new FXMLLoader(getClass().getClassLoader().getResource("user_interface\\PlaylistWindow.fxml"));
            Parent root = confirmationLoader.load();
            Scene scene = new Scene(root);
            PlaylistController playlistController = confirmationLoader.getController();

            Stage stage = new Stage();
            stage.centerOnScreen();
            stage.setScene(scene);
            playlistController.configure(lyrAppService, stage, currentStage,this, null);
            stage.show();
        } catch (Exception ignored) {
        }
    }

    @FXML
    public void searchKeyPressedForSong(KeyEvent key) {
        String keyWords = songSearchTextField.getText().strip();
        if (!key.getCode().equals(KeyCode.ENTER))
            return;
        currentSelectedPlaylist = null;
        if (!keyWords.equals("")) {
            songsModel.setAll(lyrAppService.getFilteredSongs(keyWords));
        }else{
            songsModel.setAll(lyrAppService.getAllSongs());
        }
    }

    @FXML
    public void searchKeyPressedForPlaylist(KeyEvent key) {
        String keyWords = playlistSearchTextField.getText().strip();
        if (!key.getCode().equals(KeyCode.ENTER))
            return;
        playlistModel.setAll(lyrAppService.getFilteredPlaylists(keyWords));
    }

    @FXML
    public void clockButtonClicked() {
        hourLabel.setVisible(!hourLabel.isVisible());
        notifyObserversHourVisibility(hourLabel.isVisible());
        if (hourLabel.isVisible()) {
            clockButton.setStyle(MENU_BUTTON_CLICKED_STYLE);
        } else {
            clockButton.setStyle("-fx-border-width: 0px 0px 0px 0px; -fx-base:  #2c4358");
        }
    }

    @FXML
    public void handleDeleteButtonClicked() {
        Song selectedSong = songsListView.getSelectionModel().getSelectedItem();
        if (selectedSong != null) {
            openConfirmationWindow(selectedSong);
        }
    }

    private void openConfirmationWindow(Song selectedSong) {
        try {
            FXMLLoader confirmationLoader = new FXMLLoader(getClass().getClassLoader().getResource("user_interface\\ConfirmationWindow.fxml"));
            Parent confirmationRoot = confirmationLoader.load();
            Scene confirmationScene = new Scene(confirmationRoot);
            ConfirmationController confirmationController = confirmationLoader.getController();

            Stage confirmationStage = new Stage();
            confirmationStage.centerOnScreen();
            confirmationStage.setScene(confirmationScene);
            confirmationController.configureForSong(selectedSong, lyrAppService, confirmationStage, currentStage, this);
            confirmationStage.show();
        } catch (Exception ignored) {
        }
    }

    @FXML
    public void handleUpdateButtonClicked() {
        Song selectedSong = currentSelectedSong;
        if (selectedSong != null) {
            try {
                FXMLLoader confirmationLoader = new FXMLLoader(getClass().getClassLoader().getResource("user_interface\\SongWindow.fxml"));
                Parent songRoot = confirmationLoader.load();
                Scene songScene = new Scene(songRoot);
                SongController songController = confirmationLoader.getController();

                Stage songStage = new Stage();
                songStage.centerOnScreen();
                songStage.setScene(songScene);
                songController.configure(selectedSong, SongWindowType.UPDATE, lyrAppService, songStage, currentStage, this, "");
                songStage.show();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    String onlineLyrics = "";
    public void handleSearchSongOnlineButtonClicked() {
        try {
            URL u = new URL(Constants.URL_TO_CHECK_INTERNET_CONNECTION);
            URLConnection conn = u.openConnection();
            conn.connect();
            onlineLyrics = Constants.runSearchSongRobot(songSearchTextField.getText().strip());
            if (!onlineLyrics.equals(""))
                handleAddButtonClicked();
        } catch (Exception e) {
            this.showSomethingWentWrongWindow(Constants.NO_INTERNET_CONNECTION_STRING);
            e.printStackTrace();
        }
    }

    private void showSomethingWentWrongWindow(String warningText) {
        try {
            Stage stage = new Stage();
            stage.centerOnScreen();
            FXMLLoader loadingLoader = new FXMLLoader(getClass().getClassLoader().getResource("user_interface\\WarningWindow.fxml"));
            Parent loadingRoot = loadingLoader.load();
            Scene loadingScene = new Scene(loadingRoot);
            stage.setScene(loadingScene);
            WarningController warningController = loadingLoader.getController();
            warningController.configure(stage, currentStage, warningText);
            stage.show();
        } catch (Exception exception) {
            exception.printStackTrace();
        }
    }

    @FXML
    public void boldButtonClicked() {
        if (boldButton.isSelected()) {
            if (italicButton.isSelected()) {
                textLabel.setFont(Font.font(textLabel.getFont().getFamily(), FontWeight.BOLD, FontPosture.ITALIC, textLabel.getFont().getSize()));
            } else {
                textLabel.setFont(Font.font(textLabel.getFont().getFamily(), FontWeight.BOLD, FontPosture.REGULAR, textLabel.getFont().getSize()));
            }
        } else {
            if (italicButton.isSelected()) {
                textLabel.setFont(Font.font(textLabel.getFont().getFamily(), FontWeight.NORMAL, FontPosture.ITALIC, textLabel.getFont().getSize()));
            } else {
                textLabel.setFont(Font.font(textLabel.getFont().getFamily(), FontWeight.NORMAL, FontPosture.REGULAR, textLabel.getFont().getSize()));
            }
        }
        notifyTextFormat(boldButton.isSelected(), italicButton.isSelected(), underlineButton.isSelected());
    }

    @FXML
    public void italicButtonClicked() {
        if (italicButton.isSelected()) {
            if (boldButton.isSelected()) {
                textLabel.setFont(Font.font(textLabel.getFont().getFamily(), FontWeight.BOLD, FontPosture.ITALIC, textLabel.getFont().getSize()));
            } else {
                textLabel.setFont(Font.font(textLabel.getFont().getFamily(), FontWeight.NORMAL, FontPosture.ITALIC, textLabel.getFont().getSize()));
            }
        } else {
            if (boldButton.isSelected()) {
                textLabel.setFont(Font.font(textLabel.getFont().getFamily(), FontWeight.BOLD, FontPosture.REGULAR, textLabel.getFont().getSize()));
            } else {
                textLabel.setFont(Font.font(textLabel.getFont().getFamily(), FontWeight.NORMAL, FontPosture.REGULAR, textLabel.getFont().getSize()));
            }
        }
        notifyTextFormat(boldButton.isSelected(), italicButton.isSelected(), underlineButton.isSelected());
    }

    @FXML
    public void underlineButtonClicked() {
        textLabel.setUnderline(underlineButton.isSelected());
        notifyTextFormat(boldButton.isSelected(), italicButton.isSelected(), underlineButton.isSelected());
    }

    @FXML
    public void handleMinimizeWindow() {
        currentStage.setIconified(true);
        minimizeButton.setSelected(false);
    }

    @FXML
    public void handleExitButtonClicked() {
        this.close();
        Platform.exit();
        System.exit(0);
    }

    @Override
    public void songAdded(Song song) {
        if (currentSelectedPlaylist == null)
            songsModel.add(song);
    }

    @Override
    public void songUpdated(Song song) {
        int index = 0;
        for (Song s : songsModel) {
            if (s.getId().equals(song.getId())) {
                songsModel.remove(index);
                songsModel.add(index, song);
                songsListView.getSelectionModel().select(index);
                break;
            }
            index++;
        }

        strophesModel.setAll(song.getOrderedLyrics());
        titleLabel.setText(song.getTitle());
        if (selectedStropheIndex != -1) {
            Strophe strophe = song.getStrophe(selectedStropheIndex);
            if (strophe != null) {
                setPreviewText(strophe.getText());
                if (liveButtonClicked) {
                    notifyObservers(UpdateType.SET_TEXT, song.getStrophe(selectedStropheIndex).getText());
                }
            }
        }
    }

    @Override
    public void songDeleted(Song song) {
        if (selectedStropheIndex != -1) {
            setPreviewText("");
            if (liveButtonClicked) {
                notifyObservers(UpdateType.SET_TEXT, "");
            }
        }
        int index = 0;
        for (Song s : songsModel) {
            if (s.getId().equals(song.getId())) {
                songsModel.remove(index);
                if (currentSelectedSong.equals(song)) {
                    selectedStropheIndex = -1;
                    currentSelectedSong = null;
                    strophesModel.removeAll(strophesModel);
                    titleLabel.setText("");
                    deleteImageView.setVisible(false);
                    updateImageView.setVisible(false);
                }
                break;
            }
            index++;
        }
    }

    @Override
    public void playlistAdded(Playlist playlist) { playlistModel.add(playlist); }

    @Override
    public void playlistUpdated(Playlist playlist) {
        int index = 0;
        for (Playlist p : playlistModel) {
            if (p.getId().equals(playlist.getId())) {
                playlistModel.remove(index);
                playlistModel.add(index, playlist);
                break;
            }
            index++;
        }
        if (currentSelectedPlaylist!= null && currentSelectedPlaylist.getId().equals(playlist.getId())){
            songsModel.setAll(playlist.getSongs());
            currentSelectedPlaylist = playlist;
        }
    }

    @Override
    public void playlistDeleted(Playlist playlist) {
        int index = 0;
        for (Playlist p : playlistModel) {
            if (p.getId().equals(playlist.getId())) {
                playlistModel.remove(index);
                break;
            }
            index++;
        }
        if (currentSelectedPlaylist != null && currentSelectedPlaylist.getId().equals(playlist.getId())){
            songsModel.setAll(lyrAppService.getAllSongs());
        }
    }

    @Override
    public void addObserver(Observer observer) {
        if(!observersList.contains(observer))
            observersList.add(observer);
    }

    @Override
    public void removeObserver(Observer observer) { observersList.remove(observer); }

    @Override
    public void notifyObservers(UpdateType updateType, String text) {
        for (Observer observer : observersList) {
            switch (updateType) {
                case SET_HOUR -> observer.setHours(text);
                case SET_TEXT -> observer.setText(text);
            }
        }
    }

    @Override
    public void notifyObserversTextAlignment(Pos textAlignment, TextAlignment alignment) {
        for (Observer observer : observersList) {
            observer.setTextAlignment(textAlignment, alignment);
        }
    }

    @Override
    public void notifyObserversHourVisibility(boolean visibility) {
        for (Observer observer : observersList) {
            observer.setClockVisibility(visibility);
        }
    }

    @Override
    public void notifyObserversToClose() {
        for (Observer observer : observersList) {
            observer.closeWindow();
        }
    }

    @Override
    public void notifyTextFormat(boolean isBold, boolean isItalic, boolean isUnderlined) {
        for (Observer observer : observersList) {
            observer.formatText(isBold, isItalic, isUnderlined);
        }
    }

    public void close() { stopClock = true; }

    public static LyrAppService getLyrAppService() { return lyrAppService; }

    public Stage getCurrentStage() { return currentStage; }

    public ObservableList<Song> getSongsModel() { return songsModel; }
}