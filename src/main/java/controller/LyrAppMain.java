package controller;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Cursor;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.io.IOException;

public class LyrAppMain extends Application {
    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        primaryStage.centerOnScreen();

        FXMLLoader loadingLoader = new FXMLLoader(getClass().getClassLoader().getResource("user_interface\\LoadingWindow.fxml"));
        FXMLLoader mainLoader = new FXMLLoader(getClass().getClassLoader().getResource("user_interface\\LyrAppInterface.fxml"));

        Parent loadingRoot = loadingLoader.load();
//        LoadingController loadingController = loadingLoader.getController();
        Scene loadingScene = new Scene(loadingRoot);
        loadingScene.setCursor(Cursor.DEFAULT);
        primaryStage.setScene(loadingScene);
        primaryStage.initStyle(StageStyle.UNDECORATED);
        primaryStage.show();

        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                try {
                    Parent mainRoot = mainLoader.load();
                    primaryStage.close();

                    Stage mainStage = new Stage();
                    mainStage.setTitle("LyrApp");
                    mainStage.setScene(new Scene(mainRoot));
                    mainStage.show();
                } catch (IOException exception) {
                    exception.printStackTrace();
                }
            }
        });
    }
}
