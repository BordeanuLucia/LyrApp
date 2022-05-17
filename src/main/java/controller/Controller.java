package controller;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.Pane;

public class Controller {
    @FXML Label label;
    double font;
    int error = 5;//%
    @FXML Pane pane;
    @FXML TextField input;

    @FXML void initialize(){
        font = 15;
        label.setStyle("-fx-font-size: " + font);

        label.heightProperty().addListener(new ChangeListener<Number>() {
            //The changed(...) method is called every time a change in the height is detected
            @Override
            public void changed(ObservableValue<? extends Number> value, Number number, Number t1) {
                double tentativeFont = font * Math.sqrt(pane.getHeight()/ label.getHeight());
                if (tentativeFont < font*(100-error)/100 || tentativeFont > font*(100+error)/100) {
                    font = tentativeFont;
                    label.setStyle("-fx-font-size: " + font);
                }
            }
        });
    }
}
