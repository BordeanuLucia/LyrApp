package controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
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

    @FXML
    private TextField titleTextField;
    @FXML
    private TextArea textTextArea;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
    }

    public void configure(Song song, SongWindowType songWindowType, ILyrAppService service) {
        this.currentSong = song;
        this.service = service;
        this.songWindowType = songWindowType;
        titleTextField.setText(song.getTitle());
        textTextArea.setText(song.getText());
    }

    @FXML
    public void handleActionButtonClicked(ActionEvent actionEvent) {
        switch (songWindowType) {
            case ADD -> addSong();
            case UPDATE -> updateSong();
        }
    }

    private void addSong() {
        String title = titleTextField.getText().strip();
        if (title.equals("")) {
            titleTextField.setStyle("-fx-border-color: red;");
            return;
        }
        if (!textTextArea.getText().replace("\n", "").strip().equals("")) {
            String[] texts = textTextArea.getText().split("\n[ \n]*\n");
            Long position = 0L;
            Set<Strophe> strophes = new HashSet<>();
            for (String text : texts) {
                if (!text.strip().equals("")) {
                    Strophe strophe = new Strophe(position, text);
                    strophes.add(strophe);
                }
            }
            Song song = new Song(title, strophes);
            service.addSong(song);
        } else {
            textTextArea.setStyle("-fx-border-color: red;");
        }
    }

    private void updateSong() {

    }
}
