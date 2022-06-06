package controller;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import model.Song;
import model.Strophe;
import observer.SongObservable;
import observer.SongPlaylistObserver;
import service.ILyrAppService;
import utils.Constants;
import utils.SongWindowType;

import java.net.URL;
import java.util.*;

public class SongController extends AbstractUndecoratedController implements Initializable, SongObservable {
    private final List<SongPlaylistObserver> observersList = new ArrayList<>();
    private Song currentSong;
    private SongWindowType songWindowType = SongWindowType.ADD;
    private ILyrAppService service;
    private Stage currentStage;

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

    public void configure(Song song, SongWindowType songWindowType, ILyrAppService service, Stage currentStage, Stage previousStage, LyrAppController lyrAppController) {
        this.currentSong = song;
        this.currentStage = currentStage;
        this.service = service;
        this.songWindowType = songWindowType;
        if (songWindowType.equals(SongWindowType.ADD)) {
            actionButton.setText("Salveaza melodia");
        } else {
            actionButton.setText("Salveaza modificarile");
            titleTextField.setText(song.getTitle());
            textTextArea.setText(song.getText());
        }
        configureUndecoratedWindow(currentStage, previousStage);
        addObserver(lyrAppController);
    }

    @FXML
    public void handleActionButtonClicked() {
        switch (songWindowType) {
            case ADD -> addSong();
            case UPDATE -> updateSong();
        }
    }

    private void addSong() {
        boolean isTextAreaEmpty = textTextArea.getText().replace("\n", "").strip().equals("");
        String title = titleTextField.getText().strip();
        if (title.equals("")) {
            Constants.makeBorderRedForAWhile(titleTextField);
            if (isTextAreaEmpty)
                Constants.makeBorderRedForAWhile(textTextArea);
            return;
        }
        if (!isTextAreaEmpty) {
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
                    strophe.setSongId(song.getId());
                    service.addStrophe(strophe);
                }
            }
            song.setLyrics(strophes);
            song.setId(id);
            notifySongAdded(song);
            currentStage.close();
        } else {
            Constants.makeBorderRedForAWhile(textTextArea);
        }
    }

    private void updateSong() {
        if (titleTextField.getText().strip().equals("")) {
            Constants.makeBorderRedForAWhile(titleTextField);
        } else {
            if (textTextArea.getText().replace("\n", "").strip().equals("")) {
                Constants.makeBorderRedForAWhile(textTextArea);
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
                notifySongUpdated(currentSong);
                currentStage.close();
            }
        }
    }

    public void setCorrectedText(String correctText){
        textTextArea.setText(correctText);
    }

    @FXML
    public void handleAutocorrect() { new AutocorrectWindow(textTextArea.getText(), this).setVisible( true ); }

    @FXML
    public void handleExitButtonClicked(){
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
    public void notifySongAdded(Song song) {
        for (SongPlaylistObserver observer : observersList){
            observer.songAdded(song);
        }
    }

    @Override
    public void notifySongUpdated(Song song) {
        for (SongPlaylistObserver observer : observersList){
            observer.songUpdated(song);
        }
    }

    @Override
    public void notifySongDeleted(Song song) {
        // it is not needed
    }
}