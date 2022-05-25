package controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import model.Song;
import model.Strophe;
import service.ILyrAppService;
import utils.SongWindowType;

import java.net.URL;
import java.util.HashSet;
import java.util.ResourceBundle;
import java.util.Set;

public class SongController implements Initializable {
    private Song currentSong;
    private SongWindowType songWindowType = SongWindowType.ADD;
    private ILyrAppService service;
    private Stage currentStage;
    private Parent currentRoot;
    private Stage previousStage;
    private double posX = 0;
    private double posY = 0;

    @FXML
    private TextField titleTextField;
    @FXML
    private TextArea textTextArea;
    @FXML
    private Button actionButton;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        //we don't have to initialize anything
    }

    public void configure(Song song, SongWindowType songWindowType, ILyrAppService service, Stage currentStage, Stage previousStage) {
        this.currentSong = song;
        this.currentStage = currentStage;
        this.currentRoot = currentStage.getScene().getRoot();
        this.service = service;
        this.previousStage = previousStage;
        this.songWindowType = songWindowType;
        if (songWindowType.equals(SongWindowType.ADD)) {
            actionButton.setText("Salveaza melodia");
        } else {
            actionButton.setText("Salveaza modificarile");
            titleTextField.setText(song.getTitle());
            textTextArea.setText(song.getText());
        }
        configureWindow();
    }

    private void configureWindow(){
        currentStage.initStyle(StageStyle.UNDECORATED);
        currentStage.initOwner(previousStage);
        currentStage.initModality(Modality.WINDOW_MODAL);
        currentStage.requestFocus();
        currentStage.centerOnScreen();

        currentRoot.setOnMousePressed(e -> {
            posX = currentStage.getX() - e.getScreenX();
            posY = currentStage.getY() - e.getScreenY();
        });
        currentRoot.setOnMouseDragged(e -> {
            currentStage.setX(e.getScreenX() + posX);
            currentStage.setY(e.getScreenY() + posY);
        });
    }
    @FXML
    public void handleActionButtonClicked() {
        switch (songWindowType) {
            case ADD -> addSong();
            case UPDATE -> updateSong();
        }
    }

    private void addSong() {
        String title = titleTextField.getText().strip();
        if (title.equals("")) {
            Thread borderColorFades = new Thread(new Runnable() {
                @Override
                public void run() {
                    titleTextField.setStyle("-fx-border-color: red");
                    try {
                        synchronized (this) {
                            wait(3000);
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    titleTextField.setStyle("-fx-border-color: transparent");
                }
            });
            borderColorFades.start();
            return;
        }
        if (!textTextArea.getText().replace("\n", "").strip().equals("")) {
            String[] texts = textTextArea.getText().split("\n[ \n]*\n");
            Long position = 0L;
            Set<Strophe> strophes = new HashSet<>();
            for (String text : texts) {
                if (!text.strip().equals("")) {
                    Strophe strophe = new Strophe(position, text.strip());
                    position++;
                    strophes.add(strophe);
                }
            }
            Song song = new Song(title);
            long id = service.addSong(song);
            if (id != -1){
                for(Strophe strophe : strophes){
                    strophe.setSongId(id);
                    service.addStrophe(strophe);
                }
            }
            currentStage.close();
        } else {
            Thread borderColorFades = new Thread(new Runnable() {
                @Override
                public void run() {
                    textTextArea.setStyle("-fx-border-color: red");
                    try {
                        synchronized (this) {
                            wait(3000);
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    textTextArea.setStyle("-fx-border-color: transparent");
                }
            });
            borderColorFades.start();
        }
    }

    private void updateSong() {
        if (titleTextField.getText().strip().equals("")) {
            Thread borderColorFades = new Thread(new Runnable() {
                @Override
                public void run() {
                    titleTextField.setStyle("-fx-border-color: red");
                    try {
                        synchronized (this) {
                            wait(3000);
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    titleTextField.setStyle("-fx-border-color: transparent");
                }
            });
            borderColorFades.start();
        } else {
            if (textTextArea.getText().replace("\n", "").strip().equals("")) {
                Thread borderColorFades = new Thread(new Runnable() {
                    @Override
                    public void run() {
                        textTextArea.setStyle("-fx-border-color: red");
                        try {
                            synchronized (this) {
                                wait(3000);
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                        textTextArea.setStyle("-fx-border-color: transparent");
                    }
                });
                borderColorFades.start();
            } else {
                currentSong.setTitle(titleTextField.getText().strip());
                service.updateSong(currentSong);
                String[] texts = textTextArea.getText().split("\n[ \n]*\n");
                Long position = 0L;
                Set<Strophe> strophes = new HashSet<>();
                for (String text : texts) {
                    if (!text.strip().equals("")) {
                        Strophe strophe = new Strophe(position, text);
                        position++;
                        strophes.add(strophe);
                    }
                }
                service.deleteStrophesForSong(currentSong.getId());
                currentSong.setLyrics(strophes);
                for(Strophe strophe : strophes){
                    strophe.setSongId(currentSong.getId());
                    service.addStrophe(strophe);
                }
                currentStage.close();
            }
        }
    }

    @FXML
    public void handleExitButtonClicked(){
        this.currentStage.close();
    }
}