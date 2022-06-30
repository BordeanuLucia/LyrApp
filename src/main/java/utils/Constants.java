package utils;

import javafx.scene.control.Label;
import javafx.scene.control.TextInputControl;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;

import java.io.*;
import java.net.URISyntaxException;
import java.nio.file.Paths;
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
    public static final int MAX_NUMBER_OF_CHARACTERS_ON_LINE_ON_SCREEN = 42;

    public static String bot_path = "";
    public static String studio_path = "";

    static {
        try {
            bot_path = Paths.get(Constants.class.getClassLoader().getResource("rpa_utils/SearchSongProcess/SearchSongs.xaml").toURI()).toFile().getAbsolutePath();
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }
    }

    public static final String ROBOT_RUN_COMMAND = "UiRobot.exe -file " + bot_path;
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

    public static String runSearchSongRobot(String songTitle){
        String lyrics = "";
        String inputArguments = " --input \"{'song_title' : '"+ songTitle +"'}\"";
        ProcessBuilder builder = new ProcessBuilder(
                "cmd.exe", "/c", ROBOT_RUN_COMMAND + inputArguments);
        builder.redirectErrorStream(true);
        builder.directory(new File(ROBOT_EXEC_DIR));
        try {
            Process rez = builder.start();
            BufferedReader r = new BufferedReader(new InputStreamReader(rez.getInputStream()));
            String line;
                line = r.readLine();
                if (line != null) {
                    line = line.replaceFirst("\\{\"song_lyrics\":\"", "");
                    lyrics = line.substring(0, line.length() - 2);
                    lyrics = lyrics.replace("\\n","\n");
                }
        } catch (IOException exception) {
            exception.printStackTrace();
        }
        return lyrics;
    }

    public static void makeBorderRedForAWhile(TextInputControl textField){
        Thread borderColorFades = new Thread(new Runnable() {
            @Override
            public void run() {
                textField.setStyle("-fx-border-color: red");
                try {
                    synchronized (this) {
                        wait(3000);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
                textField.setStyle("-fx-border-color: transparent");
            }
        });
        borderColorFades.start();
    }

    public static String removeDiacritics(String text){
        StringBuilder result = new StringBuilder();
        for (String s : text.split("")){
            boolean added = false;
            if (s.hashCode() == 259 || s.hashCode() == 226) {
                result.append("a");
                added = true;
            }
            if (s.hashCode() == 238) {
                result.append("i");
                added = true;
            }
            if (s.hashCode() == 537) {
                result.append("s");
                added = true;
            }
            if (s.hashCode() == 539) {
                result.append("t");
                added = true;
            }
            if (s.hashCode() == 258 || s.hashCode() == 194) {
                result.append("A");
                added = true;
            }
            if (s.hashCode() == 206) {
                result.append("I");
                added = true;
            }
            if (s.hashCode() == 536) {
                result.append("S");
                added = true;
            }
            if (s.hashCode() == 538) {
                result.append("T");
                added = true;
            }
            if (!added)
                result.append(s);
        }
        return result.toString();
    }
}
