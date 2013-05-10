package morkopeli.GUI;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Toolkit;
import java.net.URL;
import javax.swing.JLabel;
import javax.swing.JPanel;
import morkopeli.Game;
import morkopeli.Logiikka.Luola;

public class Piirtoalusta extends JPanel implements Paivitettava {

    private Luola luola;
    private int palaP;
    private Image morko;
    private Image pelaaja;
    private Image tyhja;
    private Image seina1;
    private Image seina2;
    private Image seina3;
    private Image loppu;
    private Image pause;
    private Image rip;
    private Image menu1;
    private Image menu2;
    private Image menu3;
    private Image menu4;
    private Image menu5;
    private Image door;
    private Image mamelukkikala;
    private Image morkopeli;
    private JLabel siirrot;
    private Game game;

    public Piirtoalusta(int palanSivunPituus, JLabel siirrot, Game game) {
        super.setBackground(Color.WHITE);
        this.palaP = palanSivunPituus;
        this.siirrot = siirrot;
        this.game = game;
        haeKuvat();
    }

    private void haeKuvat() {

        URL url1 = GUI.class.getResource("morko.png");
        this.morko = Toolkit.getDefaultToolkit().getImage(url1);
        URL url2 = GUI.class.getResource("pelaaja.png");
        this.pelaaja = Toolkit.getDefaultToolkit().getImage(url2);
        URL url3 = GUI.class.getResource("tyhja.png");
        this.tyhja = Toolkit.getDefaultToolkit().getImage(url3);
        URL url4 = GUI.class.getResource("morkopeli.png");
        this.morkopeli = Toolkit.getDefaultToolkit().getImage(url4);
        URL url5 = GUI.class.getResource("end.png");
        this.loppu = Toolkit.getDefaultToolkit().getImage(url5);
        URL url6 = GUI.class.getResource("pause.png");
        this.pause = Toolkit.getDefaultToolkit().getImage(url6);
        URL url7 = GUI.class.getResource("rip.png");
        this.rip = Toolkit.getDefaultToolkit().getImage(url7);
        URL url8 = GUI.class.getResource("menu1.png");
        this.menu1 = Toolkit.getDefaultToolkit().getImage(url8);
        URL url9 = GUI.class.getResource("menu2.png");
        this.menu2 = Toolkit.getDefaultToolkit().getImage(url9);
        URL url10 = GUI.class.getResource("menu3.png");
        this.menu3 = Toolkit.getDefaultToolkit().getImage(url10);
        URL url11 = GUI.class.getResource("menu4.png");
        this.menu4 = Toolkit.getDefaultToolkit().getImage(url11);
        URL url12 = GUI.class.getResource("menu5.png");
        this.menu5 = Toolkit.getDefaultToolkit().getImage(url12);
        URL url13 = GUI.class.getResource("door.png");
        this.door = Toolkit.getDefaultToolkit().getImage(url13);
        URL url14 = GUI.class.getResource("mamelukkikala.png");
        this.mamelukkikala = Toolkit.getDefaultToolkit().getImage(url14);
        // Seinät
        haeSeinat();
    }

    private void haeSeinat() {
        // Arvotaan 3 eri seinäsettiä:
        int s1 = (int) Math.floor((Math.random() * 12));
        int s2 = (int) Math.floor((Math.random() * 12));
        int s3 = (int) Math.floor((Math.random() * 12));
        while (true) {
            if (s1 != s2 && s2 != s3 && s1 != s3) {
                break;
            } else {
                s1 = (int) Math.floor((Math.random() * 12));
                s2 = (int) Math.floor((Math.random() * 12));
                s3 = (int) Math.floor((Math.random() * 12));
            }
        }
        String taulu[] = new String[]{"seina1.png", "seina2.png", "seina3.png",
            "seina4.png", "seina5.png", "seina6.png", "seina7.png", "seina8.png",
            "seina9.png", "seina10.png", "seina11.png", "seina12.png"};

        URL url30 = GUI.class.getResource(taulu[s1]);
        this.seina1 = Toolkit.getDefaultToolkit().getImage(url30);
        URL url31 = GUI.class.getResource(taulu[s2]);
        this.seina2 = Toolkit.getDefaultToolkit().getImage(url31);
        URL url32 = GUI.class.getResource(taulu[s3]);
        this.seina3 = Toolkit.getDefaultToolkit().getImage(url32);
    }

    @Override
    protected void paintComponent(Graphics graphics) {
        super.paintComponent(graphics);

        piirraKentta(graphics);
    }

    private void piirraKentta(Graphics graphics) {
        // Jos halutaan näyttää pausenäkymä
        if (luola != null) {
            if (luola.getPause()) {
                graphics.drawImage(pause, (int) ((luola.getLeveys() * 10 - 100) * 1.75), (int) ((luola.getKorkeus() * 10 - 100) * 1.75), 350, 200, this);
                return;
            }
        }
        // Jos halutaan näyttää pelinäkymä
        if (game.getOnkoPeliPaalla()) {
            String[][] taulu = luola.getKentta();
            for (int i = 0; i < taulu.length; i++) {
                for (int j = 0; j < taulu[0].length; j++) {
                    if (taulu[i][j].equals("M")) {
                        graphics.drawImage(morko, j * palaP, i * palaP, palaP, palaP, this);
                    }
                    if (taulu[i][j].equals("O")) {
                        graphics.drawImage(tyhja, j * palaP, i * palaP, palaP, palaP, this);
                    }
                    if (taulu[i][j].equals("P")) {
                        if (luola.getJatkuu()) {
                            graphics.drawImage(pelaaja, j * palaP, i * palaP, palaP, palaP, this);
                        } else {
                            graphics.drawImage(rip, j * palaP, i * palaP, palaP, palaP, this);
                        }
                    }
                    if (taulu[i][j].equals("S")) {
                        if (game.getPeliTilanne()[0] == 1) {
                            graphics.drawImage(seina1, j * palaP, i * palaP, palaP, palaP, this);
                        }
                        if (game.getPeliTilanne()[0] == 2) {
                            graphics.drawImage(seina2, j * palaP, i * palaP, palaP, palaP, this);
                        }
                        if (game.getPeliTilanne()[0] == 3) {
                            graphics.drawImage(seina3, j * palaP, i * palaP, palaP, palaP, this);
                        }
                    }
                    if (taulu[i][j].equals("E")) {
                        graphics.drawImage(loppu, j * palaP, i * palaP, palaP, palaP, this);
                    }
                    if (taulu[i][j].equals("D")) {
                        graphics.drawImage(door, j * palaP, i * palaP, palaP, palaP, this);
                    }
                }
            }
            if (luola.getMamelukkikala() != null) {
                graphics.drawImage(mamelukkikala, luola.getMamelukkikala().getX() * palaP - (105 / 3), luola.getMamelukkikala().getY() * palaP - (105 / 3), palaP * 3, palaP * 3, this);
                System.out.println("MAMELUKKIKALAN X KOORDINAATTI " + luola.getMamelukkikala().getX());
                System.out.println("MAMELUKKIKALAN X KOORDINAATTI " + luola.getMamelukkikala().getY());
            }

        }
        // Jos ollaan menussa
        if (!game.getOnkoPeliPaalla()) {
            if (game.getMenuValinta() == 1) {
                graphics.drawImage(menu1, 75, 0, 500, 500, this);
            } else if (game.getMenuValinta() == 2) {
                graphics.drawImage(menu2, 75, 0, 500, 500, this);
            } else if (game.getMenuValinta() == 3) {
                graphics.drawImage(menu3, 75, 0, 500, 500, this);
            } else if (game.getMenuValinta() == 4) {
                graphics.drawImage(menu4, 75, 0, 500, 500, this);
            } else if (game.getMenuValinta() == 5) {
                graphics.drawImage(menu5, 75, 0, 500, 500, this);
            }
            graphics.drawImage(morkopeli, 50, 500, this);
        }

    }

    public void paivita() {
        // Pelin alalaidassa oleva teksti:
        if (luola != null && game.getOnkoPausePaalla() == false && game.getOnkoPeliPaalla() == true) {
            siirrot.setText("Level: " + game.getPeliTilanne()[0] + " Time : " + luola.getAika()
                    + " Boogies: " + luola.getHirvio().size());
        }
        System.out.println("paivitettiin");
        repaint();
    }
    
    public void setUusiLuola(Luola luola) {
        this.luola = luola;
    }
}