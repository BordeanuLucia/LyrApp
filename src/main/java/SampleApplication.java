import io.github.geniot.jortho.FileUserDictionary;
import io.github.geniot.jortho.SpellChecker;

import javax.swing.JEditorPane;
import javax.swing.JFrame;
import javax.swing.JTextPane;


/*
 *  JOrtho
 *
 *  Copyright (C) 2005-2008 by i-net software
 *
 *  This program is free software; you can redistribute it and/or
 *  modify it under the terms of the GNU General Public License as 
 *  published by the Free Software Foundation; either version 2 of the
 *  License, or (at your option) any later version. 
 *
 *  This program is distributed in the hope that it will be useful, but
 *  WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 *  General Public License for more details.
 *
 *  You should have received a copy of the GNU General Public License
 *  along with this program; if not, write to the Free Software
 *  Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA 02111-1307
 *  USA.
 *  
 *  Created on 13.02.2008
 */

public class SampleApplication extends JFrame{

    public static void main(String[] args){
        new SampleApplication().setVisible( true );
    }
    
    private SampleApplication(){
        // Build the test frame for the sample
        super("JOrtho Sample");
        JEditorPane text = new JTextPane();
        text.setText( "Nowadays technology has infiltrated in every aspect of our lives, in some it made them better and in others worse, but regarding the spiritual part of out lives it seems to have been a change for the better. In this paper we discuss the development of a software application intended to help religious organisations to carry out their programs in an easier manner by offering a smart solution for the management of songs. The software allows users to manage songs, display them on external devices, linked by a HDMI cables, and to search song online through a software robot.\n" +
                " In the first chapters we talk about the importance and the motive behind the application and about some similar softwares, along with a comparison between them. We continue by discussing the tehnologies, tools and frameworks used as well as the overall environment in which the application was developed. In the last chapters we write about the process which led to the actual software and in the end we mention to which conclusions we came to and which enhancements could be made to obtain the best version of this type of application." );
        add( text );
        setSize(200, 160);
        setDefaultCloseOperation( EXIT_ON_CLOSE );
        setLocationRelativeTo( null );

        // Create user dictionary in the current working directory of your application
        SpellChecker.setUserDictionaryProvider( new FileUserDictionary() );
        
        // Load the configuration from the file dictionaries.cnf and 
        // use the current locale or the first language as default
        // You can download the dictionary files from http://sourceforge.net/projects/jortho/files/Dictionaries/
        SpellChecker.registerDictionaries( null, null );

        // enable the spell checking on the text component with all features
        SpellChecker.register( text );
    }
}
