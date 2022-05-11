package controller;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Pos;
import javafx.geometry.Rectangle2D;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;
import javafx.scene.text.TextAlignment;
import javafx.stage.Screen;
import javafx.stage.Stage;

import java.net.URL;
import java.util.ResourceBundle;

public class LiveController implements Initializable {
    private Screen currentScreen;

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

        hourLabel.setPrefWidth(width);
        hourLabel.setLayoutX(0);
        double aux = hourLabel.getFont().getSize();
        hourLabel.setLayoutY(height - aux*4/5);

        textLabel.setLayoutX(0);
        textLabel.setLayoutY(0);
        textLabel.setPrefSize(width, height - aux*3/5);
    }

    public Screen getCurrentScreen(){
        return currentScreen;
    }

    public void closeWindow(){
        Stage stage = (Stage) hourLabel.getScene().getWindow();
        stage.close();
    }

    public void setClockLabel(String timeNow) {
        hourLabel.setText(timeNow);
    }

    public void setTextLabel(String text) {
        textLabel.setText(text);
    }

    public void setTextAlignment(Pos alignment, TextAlignment textAlignment){
        textLabel.setAlignment(alignment);
        textLabel.setTextAlignment(textAlignment);
    }

    public void setClockVisibility(boolean visible) {
        hourLabel.setVisible(visible);
    }
}
