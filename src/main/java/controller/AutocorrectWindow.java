package controller;

import io.github.geniot.jortho.FileUserDictionary;
import io.github.geniot.jortho.SpellChecker;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class AutocorrectWindow extends JFrame {

    public AutocorrectWindow(String textToAutocorrect, SongController songController) {
        super("Autocorrect");
        JEditorPane text = new JTextPane();
        text.setText(textToAutocorrect);
        setLocationRelativeTo(null);

        SpellChecker.setUserDictionaryProvider(new FileUserDictionary());
        SpellChecker.registerDictionaries(null, null);
        SpellChecker.register(text);

        JScrollPane scrollBar = new JScrollPane(text, ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS, ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
        setSize(300, 400);
        setVisible(true);

        Dimension dim = Toolkit.getDefaultToolkit().getScreenSize();
        this.setLocation(dim.width / 2 - this.getSize().width / 2, dim.height / 2 - this.getSize().height / 2);

        JButton button = new JButton("Modifica");
        button.addActionListener(e -> {
            songController.setCorrectedText(text.getText());
            dispose();
        });
        button.setAlignmentX(Component.CENTER_ALIGNMENT);

        Box box = Box.createVerticalBox();
        box.add(scrollBar);
        box.add(button);
        add(box);
    }
}