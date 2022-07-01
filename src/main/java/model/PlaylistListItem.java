package model;

import controller.ConfirmationController;
import controller.LyrAppController;
import controller.PlaylistController;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.scene.Cursor;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.Priority;
import javafx.scene.text.Font;
import javafx.stage.Stage;

public class PlaylistListItem extends ListCell<Playlist> {
    HBox hbox = new HBox();
    Label label = new Label("(empty)");
    Pane pane = new Pane();
    ImageView updatePlaylist = new ImageView(new Image("file:src/main/resources/pictures/Icons/edit.png"));
    ImageView deletePlaylist = new ImageView(new Image("file:src/main/resources/pictures/Icons/delete.png"));
    ListView<Playlist> playlistListView;
    Playlist currentPlaylist;
    Button updateButton = new Button("");
    Button deleteButton = new Button("");
    LyrAppController lyrAppController;
    Pane spacePane = new Pane();

    public PlaylistListItem(ListView<Playlist> listView, LyrAppController lyrAppController) {
        super();
        this.playlistListView = listView;
        this.lyrAppController = lyrAppController;
        hbox.getChildren().addAll(label, pane, updateButton, spacePane, deleteButton);
        HBox.setHgrow(pane, Priority.ALWAYS);

        spacePane.setPrefSize(5, 5);

        updateButton.setGraphic(updatePlaylist);
        updateButton.setOnMouseClicked(event -> openUpdateWindow());
        updateButton.setStyle("-fx-background-color: white");
        updatePlaylist.setFitHeight(17);
        updatePlaylist.setFitWidth(15);
        updateButton.setPadding(new Insets(2, 2, 2, 2));

        deleteButton.setGraphic(deletePlaylist);
        deleteButton.setOnMouseClicked(event -> openConfirmationWindow());
        deleteButton.setStyle("-fx-background-color: white");
        deletePlaylist.setFitHeight(17);
        deletePlaylist.setFitWidth(15);
        deleteButton.setPadding(new Insets(2, 2, 2, 2));

        updateButton.setVisible(false);
        deleteButton.setVisible(false);

        this.setOnMouseEntered(event -> {
            updateButton.setVisible(true);
            deleteButton.setVisible(true);
        });

        this.setOnMouseExited(event -> {
            updateButton.setVisible(false);
            deleteButton.setVisible(false);
        });

        deleteButton.setCursor(Cursor.HAND);
        updateButton.setCursor(Cursor.HAND);
    }

    @Override
    protected void updateItem(Playlist item, boolean empty) {
        super.updateItem(item, empty);
        if (empty || item == null) {
            hbox.getChildren().clear();
        } else {
            hbox.getChildren().clear();
            hbox.getChildren().addAll(label, pane, updateButton, spacePane, deleteButton);
            setMinWidth(playlistListView.getWidth() - 17);
            setMaxWidth(playlistListView.getWidth() - 17);
            setPrefWidth(playlistListView.getWidth() - 17);
            label.setWrapText(true);
            label.setFont(Font.font(15));
            currentPlaylist = item;
            label.setText(item.getTitle());
            setGraphic(hbox);
        }
    }

    private void openConfirmationWindow() {
        try {
            FXMLLoader confirmationLoader = new FXMLLoader(getClass().getClassLoader().getResource("user_interface\\ConfirmationWindow.fxml"));
            Parent confirmationRoot = confirmationLoader.load();
            Scene confirmationScene = new Scene(confirmationRoot);
            ConfirmationController confirmationController = confirmationLoader.getController();

            Stage confirmationStage = new Stage();
            confirmationStage.centerOnScreen();
            confirmationStage.setScene(confirmationScene);
            confirmationController.configureForPlaylist(currentPlaylist, LyrAppController.getLyrAppService(), confirmationStage, lyrAppController.getCurrentStage(), lyrAppController);
            confirmationStage.show();
        } catch (Exception ignored) {
        }
    }

    private void openUpdateWindow() {
        try {
            FXMLLoader confirmationLoader = new FXMLLoader(getClass().getClassLoader().getResource("user_interface\\PlaylistWindow.fxml"));
            Parent root = confirmationLoader.load();
            Scene scene = new Scene(root);
            PlaylistController playlistController = confirmationLoader.getController();

            Stage stage = new Stage();
            stage.centerOnScreen();
            stage.setScene(scene);
            playlistController.configure(LyrAppController.getLyrAppService(), stage, lyrAppController.getCurrentStage(), lyrAppController, currentPlaylist);
            stage.show();
        } catch (Exception ignored) {
        }
    }
}
