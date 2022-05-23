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
import observer.Observable;
import observer.Observer;
import repository.IPlaylistsRepository;
import repository.ISongsRepository;
import repository.PlaylistsRepository;
import repository.SongsRepository;
import service.LyrAppService;
import utils.Constants;
import utils.UpdateType;

import java.io.IOException;
import java.net.URL;
import java.net.URLConnection;
import java.text.SimpleDateFormat;
import java.util.*;

public class LyrAppController implements Initializable, Observable {
    private static final LyrAppService lyrAppService;
    private volatile boolean stopClock = false;
    private final List<Observer> observersList = new ArrayList<>();
    private boolean liveButtonClicked = false;
    private static final double FONT = Constants.PREVIEW_TEXT_FONT;
    private Stage currentStage;

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

    public void configure(Stage mainStage) {
        this.currentStage = mainStage;
        configureScreens();
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
                        setMinWidth(param.getWidth() - 2);
                        setMaxWidth(param.getWidth() - 2);
                        setPrefWidth(param.getWidth() - 2);
                        setWrapText(true);
                        setTextAlignment(TextAlignment.JUSTIFY);
                        setText(item.getTitle());
                    }
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
                    setMinWidth(param.getWidth() - 16);
                    setMaxWidth(param.getWidth() - 16);
                    setPrefWidth(param.getWidth() - 16);
                    setWrapText(true);
                    setTextAlignment(TextAlignment.JUSTIFY);
                    setText(item.getText());
                }
            }
        });
        strophesListView.setOnMouseClicked(event -> {
            Strophe selectedStrophe = strophesListView.getSelectionModel().getSelectedItem();
            setPreviewText(selectedStrophe.getText());
            if (liveButtonClicked) {
                notifyObservers(UpdateType.SET_TEXT, selectedStrophe.getText());
            }
        });
    }

    private void setPreviewText(String text) {
        textLabel.setText(text);
        Constants.autoresizeText(text, textLabel, FONT);
    }

    public void configureScreens() {
        ObservableList<Screen> allScreens = Screen.getScreens();
        Screen currentScreen = Screen.getPrimary();
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
                            } catch (IOException ignored) {
                            }
                        }
                    }
                } else if (c.wasRemoved()) {
                    for (Screen removedScreen : c.getRemoved()) {
                        for (Observer liveController : observersList) {
                            if (liveController.getCurrentScreen().equals(removedScreen)) {
                                liveController.closeWindow();
                                observersList.remove(liveController);
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
        observersList.add(liveController);
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
    public void handleLiveButtonClicked() {
        liveButtonClicked = !liveButtonClicked;
        if (liveButtonClicked) {
            notifyObservers(UpdateType.SET_TEXT, textLabel.getText());
            notifyObserversTextAlignment(textLabel.getAlignment(), textLabel.getTextAlignment());
        } else {
            notifyObservers(UpdateType.SET_TEXT, "");
        }
    }

    @FXML
    public void searchKeyPressed(KeyEvent key) {
        String keyWords = songSearchTextField.getText().strip();
        if (!key.getCode().equals(KeyCode.ENTER))
            return;
        songsModel.setAll(lyrAppService.getFilteredSongs(keyWords));
    }

    @FXML
    public void clockButtonClicked() {
        hourLabel.setVisible(!hourLabel.isVisible());
        notifyObserversHourVisibility(hourLabel.isVisible());
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
            confirmationController.configure(selectedSong, lyrAppService, confirmationStage);
            confirmationStage.show();
        } catch (Exception ignored) {
        }
    }

    @FXML
    public void handleUpdateButtonClicked() {
        // in the future
    }

    public void close() {
        stopClock = true;
    }

    @Override
    public void addObserver(Observer observer) {
        observersList.add(observer);
    }

    @Override
    public void removeObserver(Observer observer) {
        observersList.remove(observer);
    }

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

    public void handleSearchSongOnlineButtonClicked() {
        try {
            URL u = new URL(Constants.URL_TO_CHECK_INTERNET_CONNECTION);
            URLConnection conn = u.openConnection();
            conn.connect();
            Constants.runSearchSongRobot(songSearchTextField.getText().strip());
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
            warningController.configure(loadingRoot, stage, currentStage, warningText);
            stage.show();
        } catch (Exception exception) {
            exception.printStackTrace();
        }
    }
}