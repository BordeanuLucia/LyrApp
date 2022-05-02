package controller;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class LyrAppMain extends Application {
    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        FXMLLoader loader = new FXMLLoader(getClass().getClassLoader().getResource("user_interface\\LyrAppInterface.fxml"));
        Parent root = loader.load();

        LyrAppController lyrAppController = loader.getController();

        primaryStage.setTitle("LyrApp");
        primaryStage.setScene(new Scene(root));
        primaryStage.show();
    }
}
