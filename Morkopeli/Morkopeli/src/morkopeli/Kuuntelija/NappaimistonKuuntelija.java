package morkopeli.Kuuntelija;

import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import morkopeli.GUI.GUI;
import morkopeli.GUI.Paivitettava;
import morkopeli.Game;
import morkopeli.Liikkuvat.Pelaaja;
import morkopeli.Liikkuvat.Suunta;
import morkopeli.Logiikka.Luola;

public class NappaimistonKuuntelija extends KeyAdapter {

    private Pelaaja pelaaja;
    private Luola luola;
    private Game game;
    private long pausenAlkuAika;
    private Paivitettava paivitettava;
    private GUI gui;
    boolean pausePaalla;

    public NappaimistonKuuntelija(Game game, Paivitettava paivitettava, GUI gui) {
        this.game = game;
        this.pausePaalla = false;
        this.paivitettava = paivitettava;
        this.gui = gui;
    }

    @Override
    public void keyPressed(KeyEvent e) {
        if (game.getOnkoPeliPaalla() == true) {
            if (e.getKeyCode() == KeyEvent.VK_UP) {
                pelaaja.setSuunta(Suunta.YLOS);
            } else if (e.getKeyCode() == KeyEvent.VK_RIGHT) {
                pelaaja.setSuunta(Suunta.OIKEA);
            } else if (e.getKeyCode() == KeyEvent.VK_DOWN) {
                pelaaja.setSuunta(Suunta.ALAS);
            } else if (e.getKeyCode() == KeyEvent.VK_LEFT) {
                pelaaja.setSuunta(Suunta.VASEN);
            } else if (e.getKeyCode() == KeyEvent.VK_P) {
                if (pausePaalla == false) {
                    pausePaalla = true;
                    game.vaihdaPausePaalleTaiPois(true);
                } else {
                    pausePaalla = false;
                    game.vaihdaPausePaalleTaiPois(false);
                }
                if (pausePaalla) {
                    pausenAlkuAika = System.currentTimeMillis();
                }
                long pausenLopetusAika = System.currentTimeMillis();
                luola.lisaaAikaaPauseenKuluneeseenAikaan(pausenLopetusAika - pausenAlkuAika);
                luola.pause();

            }
        }

        if (game.getOnkoPeliPaalla() == false) {
            int menuValinta = game.getMenuValinta();
            if (e.getKeyCode() == KeyEvent.VK_ENTER) {
                if (menuValinta == 1) {
                    game.aloitaPeli();
                }
                if (menuValinta == 5) {
                    gui.dispose();
                }
            }
            if (e.getKeyCode() == KeyEvent.VK_UP) {
                if (menuValinta == 1) {
                    game.setMenuValinta(5);
                } else if (menuValinta == 2) {
                    game.setMenuValinta(1);
                } else if (menuValinta == 3) {
                    game.setMenuValinta(2);
                } else if (menuValinta == 4) {
                    game.setMenuValinta(3);
                } else if (menuValinta == 5) {
                    game.setMenuValinta(4);
                }
                paivitettava.paivita();
            }
            if (e.getKeyCode() == KeyEvent.VK_DOWN) {
                if (menuValinta == 1) {
                    game.setMenuValinta(2);
                } else if (menuValinta == 2) {
                    game.setMenuValinta(3);
                } else if (menuValinta == 3) {
                    game.setMenuValinta(4);
                } else if (menuValinta == 4) {
                    game.setMenuValinta(5);
                } else if (menuValinta == 5) {
                    game.setMenuValinta(1);
                }
                paivitettava.paivita();
            }

        }
    }

    public void asetaUusiLuola(Luola luola) {
        this.luola = luola;
        pelaaja = luola.getPelaaja();
    }
}