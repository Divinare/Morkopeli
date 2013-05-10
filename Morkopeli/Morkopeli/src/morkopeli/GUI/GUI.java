/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package morkopeli.GUI;

import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.Dimension;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.WindowConstants;
import morkopeli.Game;
import morkopeli.Kuuntelija.MenubarKuuntelija;
import morkopeli.Kuuntelija.NappaimistonKuuntelija;
import morkopeli.Logiikka.Luola;

public class GUI extends JFrame {

    private JFrame frame;
    private int SivunPituus;
    private Luola luola;
    private JLabel siirrot;
    private Piirtoalusta piirtoalusta; // "pelikenttä"
    private boolean onkoPaalla;
    private Game game;

    public GUI(int SivunPituus, Game game) {
        this.SivunPituus = SivunPituus;
        this.luola = luola;
        this.game = game;
        this.onkoPaalla = false;
    }

//    @Override
    public void run() {
        frame = new JFrame("Luolapeli");
        // 700 700
        frame.setPreferredSize(new Dimension(1000, 1000));
        frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        luoKomponentit(frame.getContentPane());
        frame.pack();
        frame.setVisible(true);
        if (onkoPaalla == false) {
            frame.setSize(1000, 1000);
            System.out.println("noni menu oli pääl");
        }
    }

    public void luoKomponentit(Container container) {
        BorderLayout layout = new BorderLayout();
        container.setLayout(layout);

        this.siirrot = new JLabel("Level: - Time: - Boogies: - Score: - ");
        container.add(siirrot, BorderLayout.SOUTH);

        System.out.println("luotiin piirtoalusta");
        piirtoalusta = new Piirtoalusta(SivunPituus, siirrot, game);
        container.add(piirtoalusta);
        container.add(luoMenubar(), BorderLayout.NORTH);

        NappaimistonKuuntelija kuuntelija = new NappaimistonKuuntelija(game, piirtoalusta, this);
        game.setNappaimistonKuuntelija(kuuntelija);
        frame.addKeyListener(kuuntelija);
        if (getPaivitettava() == null) {
            System.out.println("wtf?????????");
        }
    }

    private JMenuBar luoMenubar() {
        JMenuBar mb = new JMenuBar();
        JMenu file = new JMenu("File");
        JMenu help = new JMenu("Help");
        JMenu sound = new JMenu("Sound");

        JMenuItem newGame = new JMenuItem("New Game");
        JMenuItem settings = new JMenuItem("Settings");
        JMenuItem tilastot = new JMenuItem("Scores");
        JMenuItem help2 = new JMenuItem("Rules");
        JMenuItem soundOn = new JMenuItem("On");
        JMenuItem soundOff = new JMenuItem("Off");

        file.add(newGame);
        file.add(settings);
        file.add(tilastot);
        help.add(help2);
        sound.add(soundOn);
        sound.add(soundOff);

        mb.add(file);
        mb.add(help);
        mb.add(sound);

        newGame.addActionListener(new MenubarKuuntelija());
        help2.addActionListener(new MenubarKuuntelija());
        settings.addActionListener(new MenubarKuuntelija());
        tilastot.addActionListener(new MenubarKuuntelija());
        return mb;
    }

    public Paivitettava getPaivitettava() {
        return piirtoalusta;
    }

    public boolean onkoPaalla() {
        return onkoPaalla;
    }

    public JFrame getFrame() {
        return frame;
    }

    public void setGame(Luola luola) {
        this.luola = luola;
    }
    
    public void muutaIkkunanKokoa(int kentanKoko) {
        System.out.println("muutetaan kokoa " + (kentanKoko * 35 + 17));
        System.out.println("muutetaan kokoa " + (kentanKoko * 35 + 78));
//        frame.setSize(1000,1000);
        frame.setSize((kentanKoko * 35 + 17), (kentanKoko * 35 + 78));
    }
    
    public void LuoUusiLuola(Luola luola) {
        this.luola = luola;
        piirtoalusta.setUusiLuola(luola);
    }
}