package morkopeli.Kuuntelija;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JMenuItem;


public class MenubarKuuntelija implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent tapahtuma) {
            JMenuItem item = (JMenuItem) tapahtuma.getSource();
            String nimi = item.getText();

//            if (nimi.equals("Rules")) {
//                runRules();
//            } else if (nimi.equals("Settings")) {
//                runSettings();
//            } else if (nimi.equals("New Game")) {
//                runNewGame();
//            } else if (nimi.equals("Scores")) {
//                runScores();
//            }
        }
}