package observer;

import javafx.geometry.Pos;
import javafx.scene.text.TextAlignment;
import utils.UpdateType;

import java.util.ArrayList;
import java.util.List;

public interface Observable {
    List<Observer> observersList = new ArrayList<>();

    void addObserver(Observer observer);
    void removeObserver(Observer observer);
    void notifyObservers(UpdateType updateType, String text);
    void notifyObserversTextAlignment(Pos textAlignment, TextAlignment alignment);
    void notifyObserversHourVisibility(boolean visibility);
    void notifyTextFormat(boolean isBold, boolean isItalic, boolean isUnderlined);
    void notifyObserversToClose();
}
