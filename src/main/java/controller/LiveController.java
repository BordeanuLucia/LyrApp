package controller;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Pos;
import javafx.geometry.Rectangle2D;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;
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
        double height = bounds.getHeight();
        double width = bounds.getWidth();
        mainAnchorPane.setPrefSize(width, height);
        hourLabel.setPrefWidth(width);
        hourLabel.setLayoutX(0);
        double aux = hourLabel.getFont().getSize();
        hourLabel.setLayoutY(height - aux * 4 / 5);

        textLabel.setLayoutX(0);
        textLabel.setLayoutY(0);
        textLabel.setPrefSize(width, height - aux * 3 / 5);
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
        Constants.autoresizeText(text, textLabel, FONT);
    }

    @Override
    public void setHours(String timeNow) {
        hourLabel.setText(timeNow);
    }
}
