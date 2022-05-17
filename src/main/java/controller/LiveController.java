package controller;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Pos;
import javafx.geometry.Rectangle2D;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;
import javafx.scene.text.Font;
import javafx.scene.text.TextAlignment;
import javafx.stage.Screen;
import javafx.stage.Stage;
import observer.Observer;
import utils.Constants;

import java.net.URL;
import java.util.Arrays;
import java.util.Optional;
import java.util.ResourceBundle;

public class LiveController implements Initializable, Observer {
    private Screen currentScreen;
    private double font = Constants.LIVE_TEXT_FONT;

    @FXML
    private Label hourLabel;
    @FXML
    private Label textLabel;
    @FXML
    private AnchorPane mainAnchorPane;


    @Override
    public void initialize(URL location, ResourceBundle resources) {
    }

    public void configure(Screen screen) {
        currentScreen = screen;
        Rectangle2D bounds = screen.getVisualBounds();
        double height = bounds.getHeight();
        double width = bounds.getWidth();
        mainAnchorPane.setPrefSize(width, height);
        System.out.println(width + " " + height);
        hourLabel.setPrefWidth(width);
        hourLabel.setLayoutX(0);
        double aux = hourLabel.getFont().getSize();
        hourLabel.setLayoutY(height - aux*4/5);

        textLabel.setLayoutX(0);
        textLabel.setLayoutY(0);
        textLabel.setPrefSize(width, height - aux*3/5);
    }

    @Override
    public Screen getCurrentScreen(){ return currentScreen; }

    @Override
    public void closeWindow(){
        Stage stage = (Stage) hourLabel.getScene().getWindow();
        stage.close();
    }

    @Override
    public void setTextAlignment(Pos alignment, TextAlignment textAlignment){
        textLabel.setAlignment(alignment);
        textLabel.setTextAlignment(textAlignment);
    }

    @Override
    public void setClockVisibility(boolean visible) { hourLabel.setVisible(visible); }

    @Override
    public void setText(String text) {
        textLabel.setFont(Font.font(font));
        textLabel.setText(text);
        int maxLines = Constants.MAX_NUMBER_OF_LINES_ON_SCREEN;
        int nrLines = text.split("\n").length;
        if (nrLines > maxLines) {
            double newFont = (textLabel.getFont().getSize() * maxLines) / nrLines;
            textLabel.setFont(Font.font(newFont));
        }

        double actualFont = textLabel.getFont().getSize();
        double maxCharacters = (Constants.MAX_NUMBER_OF_CHARACTERS_ON_LINE_ON_SCREEN * font) / actualFont;
        int maxNrCharacters = 1;
        Optional<Integer> optional = Arrays.stream(text.split("\n")).map(s -> s.toCharArray().length)
                .reduce((integer1, integer2) -> {
                    if (integer1 > integer2)
                        return integer1;
                    return integer2;
                });
        if (optional.isPresent())
            maxNrCharacters = optional.get();
        if (maxNrCharacters > maxCharacters) {
            double newFont = (actualFont * maxCharacters) / maxNrCharacters;
            textLabel.setFont(Font.font(newFont));
        }
    }

    @Override
    public void setHours(String timeNow) { hourLabel.setText(timeNow); }
}
