package morkopeli;

import java.io.FileNotFoundException;
import javazoom.jl.decoder.JavaLayerException;
import morkopeli.GUI.GUI;
import morkopeli.Kuuntelija.NappaimistonKuuntelija;
import morkopeli.Logiikka.Luola;
import morkopeli.soittaja.Soittaja;

public class Game {

    private String[] randomMusic;
    private String menuMusic;
    private int[] peliTilanne;
    private boolean onkoPeliPaalla, onkoPausePaalla, onkoMusiikkiPaalla;
    private int menuValinta;     // missä kohtaa on "focus" menuruudussa
    private Luola luola;
    private Soittaja musiikinSoittaja;
    private GUI gui;
    private NappaimistonKuuntelija nappaimistonKuuntelija;

    public Game() {
        this.peliTilanne = new int[4];
        peliTilanne[0] = 1; // taso
        peliTilanne[1] = 0; // peliin kulunut aika sekuntteina
        peliTilanne[2] = 0; // pisteet
        peliTilanne[3] = 0; // 0 = hävittiinkö, 1 = voitettiinko
        this.onkoPeliPaalla = false;
        this.onkoPausePaalla = false;
        this.onkoMusiikkiPaalla = true;
        this.menuValinta = 1;

        // Musat:
        this.musiikinSoittaja = new Soittaja();
        this.randomMusic = new String[3];
        this.randomMusic[0] = "muumimusaa1";
        this.randomMusic[1] = "muumimusaa2";
        this.randomMusic[2] = "patmat";
        this.menuMusic = "tristram";

    }

    public void run() {

//        musiikinSoittaja.play(menuMusic);
//        sekoitaPlaylist();
//        for (int i = 0; i < randomMusic.length; i++) {
//            System.out.println(randomMusic[i]);
//        }
        this.gui = new GUI(35, this);
        gui.run();
    }

    public void aloitaPeli() {
        this.luola = new Luola(2, musiikinSoittaja, randomMusic, this);
        luola.setPaivitettava(gui.getPaivitettava());
        gui.LuoUusiLuola(luola);
        nappaimistonKuuntelija.asetaUusiLuola(luola);
        onkoPeliPaalla = true;
        luola.asetaAlkuAika(System.currentTimeMillis());
//        musiikinSoittaja.stop();
        System.out.println("KENTÄN KOKO LOL " + luola.getKentta().length);
//        this.kayttoliittyma.muutaIkkunanKokoa(luola.getKentta().length);
        this.luola.start();
        luola.asetaMorkoSpawnAlkuAika();

    }

    public void siirrySeuraavaanTasoon() {
        this.peliTilanne[0]++; // Level kasvaa
        luola.kasvataTasoa();
        this.gui.muutaIkkunanKokoa(luola.getKentta().length);
        luola.asetaAlkuAika(System.currentTimeMillis());
        luola.asetaMorkoSpawnAlkuAika();
        luola.asetaMamelukkiSpawnAika(System.currentTimeMillis());
    }

    public void siirryMenuun() {
        this.luola.stop();
    }

//        while (kayttoliittyma.getPaivitettava() == null) {
//            System.out.println("haha");
//            try {
//                Thread.sleep(100);
//            } catch (InterruptedException ex) {
//                System.out.println("Piirtoalustaa ei ole vielä luotu.");
//            }
//        }
    public void muutaPeliTilannetta(int[] uusiPeliTilanne) {
        this.peliTilanne = uusiPeliTilanne;
    }

    public boolean getOnkoPeliPaalla() {
        return onkoPeliPaalla;
    }

    public void vaihaPeliPaalleTaiPois(boolean paalleTaiPois) {
        this.onkoPeliPaalla = paalleTaiPois;
    }

    public boolean getOnkoPausePaalla() {
        return onkoPausePaalla;
    }

    public void vaihdaPausePaalleTaiPois(boolean paalleTaiPois) {
        this.onkoPausePaalla = paalleTaiPois;
    }

    public int getMenuValinta() {
        return menuValinta;
    }

    public void setMenuValinta(int kohta) {
        this.menuValinta = kohta;
    }

    public int[] getPeliTilanne() {
        return peliTilanne;
    }

    public void sekoitaPlaylist() {
        for (int i = 0; i < randomMusic.length; i++) {
            int j = 3;
            int luku1 = (int) Math.floor((Math.random() * j));
            int luku2 = (int) Math.floor((Math.random() * j));
            while (luku1 == luku2) {
                luku2 = (int) Math.floor((Math.random() * j));
            }
            String biisi = randomMusic[luku1];
            randomMusic[luku1] = randomMusic[luku2];
            randomMusic[luku2] = biisi;
        }
    }
    
    public void setNappaimistonKuuntelija(NappaimistonKuuntelija nappaimistonKuuntelija) {
        this.nappaimistonKuuntelija = nappaimistonKuuntelija;
    }
//    public void lopetaPeli() {
//       
//    }
}
