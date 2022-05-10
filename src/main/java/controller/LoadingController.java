package controller;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

import java.net.URL;
import java.util.ResourceBundle;

public class LoadingController  implements Initializable {
    @FXML
    private ImageView imageView;

    public LoadingController() {
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        Image image = new Image("file:src/main/resources/pictures/LoadingImage.jpeg");
        imageView.setImage(image);
    }
}