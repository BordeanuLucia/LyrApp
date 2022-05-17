package observer;

import javafx.geometry.Pos;
import javafx.scene.text.TextAlignment;
import utils.UpdateType;

public interface Observable {
    void addObserver(Observer observer);
    void removeObserver(Observer observer);
    void notifyObservers(UpdateType updateType, String text);
    void notifyObserversTextAlignment(Pos textAlignment, TextAlignment alignment);
    void notifyObserversHourVisibility(boolean visibility);
    void notifyObserversToClose();
}
