package utils;

import javafx.scene.control.Label;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.Optional;

public class Constants {
    private Constants() {
    }
    public static final int DISPLAY_WIDTH = 1536;
    public static final int DISPLAY_HEIGHT = 824;
    public static final int LIVE_TEXT_FONT = 72;
    public static final int CLOCK_HEIGHT = 50;

    public static final int MAX_NUMBER_OF_LINES_ON_SCREEN = 7;
    public static final int MAX_NUMBER_OF_CHARACTERS_ON_LINE_ON_SCREEN = 44;

    public static final String ROBOT_RUN_COMMAND = "UiRobot.exe -file \"E:\\Licenta\\SongSearcherRobot\\SearchSongProcess\\SearchSongs.xaml\"";
    public static final String ROBOT_EXEC_DIR = "C:\\Program Files (x86)\\UiPath\\Studio";

    public static final String NO_INTERNET_CONNECTION_STRING = "Ups...\nnu exista conexiune la internet";
    public static final String URL_TO_CHECK_INTERNET_CONNECTION = "https://www.google.com";


    public static void autoresizeText(String text, Label textLabel, int maxNumberOfLines, int maxNumberOfCharacters) {
        double font = LIVE_TEXT_FONT;
        textLabel.setFont(Font.font("System", FontWeight.BOLD, font));
        textLabel.setText(text);
        int nrLines = text.split("\n").length;
        if (nrLines > maxNumberOfLines) {
            double newFont = (textLabel.getFont().getSize() * maxNumberOfLines) / nrLines;
            textLabel.setFont(Font.font("System", FontWeight.BOLD, newFont));
        }

        double actualFont = textLabel.getFont().getSize();
        double maxCharacters = (maxNumberOfCharacters * font) / actualFont;
        int maxNrCharacters = 1;
        Optional<Integer> optional = Arrays.stream(text.split("\n")).map(s -> s.toCharArray().length)
                .reduce((integer1, integer2) -> {
                    if (integer1 > integer2)
                        return integer1;
                    return integer2;
                });
        if (optional.isPresent())
            maxNrCharacters = optional.get();
        if (maxNrCharacters > maxCharacters) {
            double newFont = (actualFont * maxCharacters) / maxNrCharacters;
            textLabel.setFont(Font.font("System", FontWeight.BOLD, newFont));
        }
    }

    public static void runSearchSongRobot(String songTitle){
        String inputArguments = " --input \"{'inArg' : 'value' , 'String' : '"+ songTitle +"'}\"";
        ProcessBuilder builder = new ProcessBuilder(
                "cmd.exe", "/c", ROBOT_RUN_COMMAND + inputArguments);
        builder.redirectErrorStream(true);
        builder.directory(new File(ROBOT_EXEC_DIR));
        try {
            builder.start();
        } catch (IOException exception) {
            exception.printStackTrace();
        }
    }
}
