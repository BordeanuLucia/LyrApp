package controller;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Pos;
import javafx.geometry.Rectangle2D;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;
import javafx.scene.text.Font;
import javafx.scene.text.FontPosture;
import javafx.scene.text.FontWeight;
import javafx.scene.text.TextAlignment;
import javafx.stage.Screen;
import javafx.stage.Stage;
import observer.Observer;
import utils.Constants;

import java.net.URL;
import java.util.ResourceBundle;

public class LiveController implements Initializable, Observer {
    private Screen currentScreen;
    private static final double FONT = Constants.LIVE_TEXT_FONT;
    private double screenHeight = 0;
    private double screenWidth = 0;
    private FontWeight bold = FontWeight.BOLD;
    private FontPosture italic = FontPosture.REGULAR;
    private boolean underlined = false;

    @FXML
    private Label hourLabel;
    @FXML
    private Label textLabel;
    @FXML
    private AnchorPane mainAnchorPane;


    @Override
    public void initialize(URL location, ResourceBundle resources) {
        // We don't have anything to initialize
    }

    public void configure(Screen screen) {
        currentScreen = screen;
        Rectangle2D bounds = screen.getVisualBounds();
        screenHeight = bounds.getHeight();
        screenWidth = bounds.getWidth();

        mainAnchorPane.setPrefSize(screenWidth, screenHeight);

        hourLabel.setPrefWidth(screenWidth);
        hourLabel.setLayoutX(0);
        double clockFont = FONT * screenWidth / Constants.DISPLAY_WIDTH;
        hourLabel.setFont(Font.font("System", FontWeight.BOLD, clockFont));
        double clockHeight = clockFont * Constants.CLOCK_HEIGHT / FONT;
        hourLabel.setLayoutY(screenHeight - clockHeight);

        textLabel.setLayoutX(2 * screenWidth / 100);
        textLabel.setLayoutY(2 * screenHeight / 100);
        textLabel.setPrefSize(screenWidth - 4 * screenWidth / 100, screenHeight - clockHeight);
    }

    @Override
    public Screen getCurrentScreen() {
        return currentScreen;
    }

    @Override
    public void closeWindow() {
        Stage stage = (Stage) hourLabel.getScene().getWindow();
        stage.close();
    }

    @Override
    public void setTextAlignment(Pos alignment, TextAlignment textAlignment) {
        textLabel.setAlignment(alignment);
        textLabel.setTextAlignment(textAlignment);
    }

    @Override
    public void setClockVisibility(boolean visible) {
        hourLabel.setVisible(visible);
    }

    @Override
    public void setText(String text) {
        int numberLines = (int) Math.round(screenHeight * Constants.MAX_NUMBER_OF_LINES_ON_SCREEN / Constants.DISPLAY_HEIGHT);
        int numberOfCharacters = (int) Math.round(screenWidth * Constants.MAX_NUMBER_OF_CHARACTERS_ON_LINE_ON_SCREEN / Constants.DISPLAY_WIDTH);
        Constants.autoresizeText(text, textLabel, numberLines, numberOfCharacters);
        textLabel.setFont(Font.font(textLabel.getFont().getFamily(), bold, italic, textLabel.getFont().getSize()));
        textLabel.setUnderline(underlined);
    }

    @Override
    public void setHours(String timeNow) {
        hourLabel.setText(timeNow);
    }

    @Override
    public void formatText(boolean isBold, boolean isItalic, boolean isUnderlined) {
        if (isBold) {
            bold = FontWeight.BOLD;
        } else {
            bold = FontWeight.NORMAL;
        }

        if (isItalic) {
            italic = FontPosture.ITALIC;
        } else {
            italic = FontPosture.REGULAR;
        }

        underlined = isUnderlined;
        textLabel.setFont(Font.font(textLabel.getFont().getFamily(), bold, italic, textLabel.getFont().getSize()));
        textLabel.setUnderline(underlined);
    }
}