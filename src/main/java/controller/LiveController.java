package controller;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Rectangle2D;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;
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
        hourLabel.setLayoutY(height - hourLabel.getHeight() - 60);
    }

    public Screen getCurrentScreen(){
        return currentScreen;
    }

    public void closeWindow(){
        Stage stage = (Stage) hourLabel.getScene().getWindow();
        stage.close();
    }
}
