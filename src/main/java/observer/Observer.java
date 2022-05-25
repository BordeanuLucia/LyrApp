package observer;

import javafx.geometry.Pos;
import javafx.scene.text.TextAlignment;
import javafx.stage.Screen;

public interface Observer {
    void setClockVisibility(boolean visibility);
    void setText(String text);
    void setTextAlignment(Pos textAlignment, TextAlignment alignment);
    void formatText(boolean isBold, boolean isItalic, boolean isUnderlined);
    void setHours(String timeNow);
    void closeWindow();
    Screen getCurrentScreen();
}
