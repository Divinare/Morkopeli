package morkopeli.Logiikka;

import morkopeli.Liikkuvat.*;
import morkopeli.Kuuntelija.*;
import morkopeli.GUI.*;




import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JFileChooser;
import javax.swing.Timer;
import javazoom.jl.decoder.JavaLayerException;
import morkopeli.Game;
import morkopeli.Liikkuvat.Hirvio;
import morkopeli.Lukija.Tekstinlukija;
import morkopeli.soittaja.Soittaja;

public class Luola extends Timer implements ActionListener {

    private int pisteet, taso, biisi, ovenKorkeus, ovenLeveys;
    private long alkuaika, pauseihinKulunutAika;
    private long morkoSpawnAlkuAika, mamelukkiSpawnAika, mamelukkiLiikkumaAika;
    private ArrayList<Hirvio> hirviot;
    private Pelaaja pelaaja;
    private boolean jatkuu, paused, havitty, tasoValmis, voidaankoLiikkua;
    private String[][] kentta;
    private Kentat tasot;
    private Tekstinlukija lukija;
    public Paivitettava paivitettava;
    private Soittaja musiikinSoittaja;
    private String[] randomMusic;
    private Game game;
    private Mamelukkikala mamelukkikala;

    public Luola(int morkoM, Soittaja musiikinSoittaja, String[] randomMusic, Game game) {
        super(1000, null);
        this.musiikinSoittaja = musiikinSoittaja;
        this.randomMusic = randomMusic;
        this.game = game;
        this.pisteet = 0;
        this.jatkuu = true;
        this.havitty = false;
        this.pauseihinKulunutAika = 0;
        hirviot = new ArrayList<>();
        paused = false;
        this.lukija = new Tekstinlukija();
        this.tasot = new Kentat();
        this.kentta = tasot.taso1;
        this.taso = 1;
        this.biisi = 1;
        this.voidaankoLiikkua = true;
        pelaaja = new Pelaaja(Suunta.ALAS, getLeveys(), getKorkeus(), this);
        alustaKentta(morkoM); // morko M montako mörköä per "M"
        addActionListener(this);
        setInitialDelay(2000);
    }

    @Override
    public void actionPerformed(ActionEvent ae) {
        System.out.println("MORKOSPAWN " + ((System.currentTimeMillis() - morkoSpawnAlkuAika) / 1000));
        if (!jatkuu) {
            try {
                peliHavitty();
            } catch (JavaLayerException ex) {
                Logger.getLogger(Luola.class.getName()).log(Level.SEVERE, null, ex);
            } catch (FileNotFoundException ex) {
                Logger.getLogger(Luola.class.getName()).log(Level.SEVERE, null, ex);
            }
            return;
        }
        if (tasoValmis == false) {
            checkTasonVaihtuminen();
        }
        if (!musiikinSoittaja.soitetaankoTallaHetkellaMitaan()) {
            biisi++;
            if (biisi > randomMusic.length - 1) { // Jos playlist loppuu
                biisi = 0;
                game.sekoitaPlaylist();
            }
//            try {
//                musiikinSoittaja.play(randomMusic[biisi]);
//            } catch (JavaLayerException ex) {
//                Logger.getLogger(Luola.class.getName()).log(Level.SEVERE, null, ex);
//            } catch (FileNotFoundException ex) {
//                Logger.getLogger(Luola.class.getName()).log(Level.SEVERE, null, ex);
//            }
        }
        if (paused) {
            piirra();
            return;
        }
        if (voidaankoLiikkua == true) {
            liiku();
            checkSynnytaMorko();
        }
        try {
            checkMamelukkikala();
        } catch (JavaLayerException ex) {
            Logger.getLogger(Luola.class.getName()).log(Level.SEVERE, null, ex);
        } catch (FileNotFoundException ex) {
            Logger.getLogger(Luola.class.getName()).log(Level.SEVERE, null, ex);
        }
        if (tasoValmis == true) {
            checkOnkoTasoLapi();
        }
        if (onkoMorkoSamassaPaikassa(pelaaja.getX(), pelaaja.getY())) {
            havio();
            piirra();
            return;
        }
        if (voidaankoLiikkua == true) {
            for (int i = 0; i < hirviot.size(); i++) {
                hirviot.get(i).liiku(getKorkeus(), getLeveys());
            }
        }
        if (onkoMorkoSamassaPaikassa(pelaaja.getX(), pelaaja.getY())) {
            havio();
            piirra();
            return;
        }
        if (onkoMamelukkikalaLahella(pelaaja.getX(), pelaaja.getY())) {
            havio();
            piirra();
            return;
        }

        piirra();
        if (taso == 1) {
            setDelay(800 - (getAika() * 5));
            System.out.println(500 - (getAika() * 5));
        }
//        if (taso == 2 && getMamelukkikala() != null) {
//            setDelay(getDelay() / 2);
//        }
    }
    // Custom gamea varten

    private void lueKentta() {
        try {
            JFileChooser jfc = new JFileChooser();
            int valinta = jfc.showOpenDialog(null);
            //Nyt tää siis kysyy et mikä filu avataan. jolloin .txt filu varmasti löytyy .
            //Tässä tavallaan turhaa toistoa, nyt luodaan Fileolio, jolta kysytään polku, jotta voidaan 
            //luoda uus File olio. mut katotaan jos saadaan tällee toimii
            File valittu = jfc.getSelectedFile();
            System.out.println(valittu.getAbsolutePath() + "asdasdasdad");
            kentta = lukija.read(valittu.getAbsolutePath()/*"level.txt"*/);
        } catch (IOException e) {
            System.out.println("lol");
        }
    }

    private void alustaKentta(int maara) {
        System.out.println("KORK " + getKorkeus());
        System.out.println("LEVE " + getLeveys());
//        this.leveys = kentta.length;
//        this.korkeus = kentta[0].length;
        randomaaPaikkaEiReunoille("D"); // Laitetaan kenttään ovi
        for (int i = 0; i < getKorkeus(); i++) {
            for (int j = 0; j < getLeveys(); j++) {
                if ("M".equalsIgnoreCase(kentta[i][j])) {
                    int lisatty = 0;
                    while (lisatty < maara) {
                        Hirvio hirvio = new Hirvio(j, i, this);
                        hirviot.add(hirvio);
                        System.out.println("laitettiin hirviö " + j + " " + i);
                        lisatty++;
                    }
                }
                if (kentta[i][j].equals("P")) {
                    pelaaja.setX(j);
                    pelaaja.setY(i);
                }
            }
        }
    }

    public void peliHavitty() throws JavaLayerException, FileNotFoundException {
        if (getMamelukkikala() != null && havitty == false) {
            musiikinSoittaja.stop();
            musiikinSoittaja.play("mamelukkikalaend");
            havitty = true;
        }
        int luku = (int) Math.floor((Math.random() * 2) + 1); // randomaa 1 tai 2
        if (luku == 1 && havitty == false) {
            musiikinSoittaja.stop();
            musiikinSoittaja.play("aww");
            havitty = true;
        }
        if (luku == 2 && havitty == false) {
            musiikinSoittaja.stop();
            musiikinSoittaja.play("aaa");
            havitty = true;
        }
        System.out.println("hävisit hähää!");
    }

    public void liiku() {
        pelaaja.liiku();
    }

    private void piirra() {
        paivitettava.paivita();
    }

    public Pelaaja getPelaaja() {
        return pelaaja;
    }

    public int getAika() {
        return (int) ((System.currentTimeMillis() - (alkuaika + 2000)) / 1000 - (pauseihinKulunutAika / 1000));
    }

    public void havio() {
        jatkuu = false;
    }

    public boolean getJatkuu() {
        return jatkuu;
    }

    public int getPisteet() {
        return pisteet;
    }

    public void pause() {
        paused = !paused;
    }

    public boolean getPause() {
        return paused;
    }

    public void lisaaAikaaPauseenKuluneeseenAikaan(long maara) {
        this.pauseihinKulunutAika = pauseihinKulunutAika + maara;
    }

    public void asetaAlkuAika(long maara) {
        this.alkuaika = maara;
    }

    public void asetaMamelukkiSpawnAika(long maara) {
        this.mamelukkiSpawnAika = maara;
    }

    public void setPaivitettava(Paivitettava paivitettava) {
        this.paivitettava = paivitettava;
    }

    public boolean onkoMorkoSamassaPaikassa(int xkoord, int ykoord) {
        boolean onko = false;
        if (hirviot == null) {
            return false;
        }
        for (int i = 0; i < hirviot.size(); i++) {
            if (xkoord == hirviot.get(i).getX() && ykoord == hirviot.get(i).getY()) {
                onko = true;
            }
        }
        return onko;
    }

    public boolean onkoMamelukkikalaSamassaPaikassa(int xkoord, int ykoord) {
        boolean onko = false;
        if (mamelukkikala == null) {
            return false;
        }
        if (xkoord == mamelukkikala.getX() && ykoord == mamelukkikala.getY()) {
            onko = true;
        }
        return onko;
    }

    public boolean onkoMamelukkikalaLahella(int xkoord, int ykoord) {
        boolean onko = false;
        if (mamelukkikala == null) {
            return false;
        }
        int x = mamelukkikala.getX();
        int y = mamelukkikala.getY();
        if (x - 1 == xkoord && y == ykoord) {
            return true;
        }
        if (x == xkoord && y == ykoord) {
            return true;
        }
        if (x + 1 == xkoord && y == ykoord) {
            return true;
        }
        if (x == xkoord && y - 1 == ykoord) {
            return true;
        }
        if (x == xkoord && y == ykoord) {
            return true;
        }
        if (x == xkoord && y + 1 == ykoord) {
            return true;
        }
        return onko;
    }

    public int getKorkeus() {
        return kentta[0].length;
    }

    public int getLeveys() {
        return kentta.length;
    }

    public boolean onkoPelaajaSamassaPaikassa(int xkoord, int ykoord) {
        boolean onko = false;
        if (hirviot == null) {
            return false;
        }
        if (xkoord == pelaaja.getX() && ykoord == pelaaja.getY()) {
            onko = true;
        }
        return onko;
    }
    // X = Morko, O = tyhja, P = pelaaja, S = seinä, E = end

    public String[][] getKentta() {
        for (int i = 0; i < getLeveys(); i++) {
            for (int j = 0; j < getKorkeus(); j++) {
                if (!kentta[i][j].equals("S") && !kentta[i][j].equals("E") && !kentta[i][j].contains("D")) {
                    if (onkoMorkoSamassaPaikassa(j, i)) {
                        kentta[i][j] = "M";
                    } else if (onkoMamelukkikalaSamassaPaikassa(j, i)) {
                        kentta[i][j] = "mamelukkikala";
                    } else {
                        kentta[i][j] = "O";
                    }
                }
            }
        }
        kentta[pelaaja.getY()][pelaaja.getX()] = "P";
        return kentta;
    }

    public void kasvataTasoa() {
        this.hirviot.clear();
        taso++;
        if (taso == 2) {
            this.kentta = this.tasot.taso2;
        }
        if (taso == 3) {
            this.kentta = this.tasot.taso3;
        }
        alustaKentta(2);
        pelaaja.setPelaajanKentanKorkeus(getKorkeus());
        pelaaja.setPelaajanKentanLeveys(getLeveys());
    }

    private void checkTasonVaihtuminen() {
        if (taso == 1) {
            if (getAika() > 5) {
                this.tasoValmis = true;
                randomaaPaikka("E");
            }
        }
        if (taso == 2) {
            if (getAika() > 5) {
                this.tasoValmis = true;
                randomaaPaikka("E");
            }
        }
        if (taso == 3) {
            if (getAika() > 5) {
                this.tasoValmis = true;
                randomaaPaikka("E");
            }
        }
    }

    private void checkOnkoTasoLapi() {
        if (kentta[pelaaja.getY()][pelaaja.getX()].equals("E")) {
            System.out.println("siirrytään seuraavaan tasoon!");
            tasoValmis = false;
            game.siirrySeuraavaanTasoon();
        }
    }

    // Etsitään seinä S, jonka vieressä on tyhjää eli O
    private void randomaaPaikka(String kirjain) {
        System.out.println("randomataan " + kirjain);
        while (true) {
            int k = (int) Math.floor((Math.random() * getKorkeus()));
            int l = (int) Math.floor((Math.random() * getLeveys()));
            if (kentta[k][l].equals("S")) {
                if (l - 1 >= 0) {
                    if (kentta[k][l - 1].equals("O")) {
                        kentta[k][l] = kirjain;
                        break;
                    }
                }
                if (l + 1 < getLeveys()) {
                    if (kentta[k][l + 1].equals("O")) {
                        kentta[k][l] = kirjain;
                        break;
                    }
                }
                if (k - 1 >= 0) {
                    if (kentta[k - 1][l].equals("O")) {
                        kentta[k][l] = kirjain;
                        break;
                    }
                }
                if (k + 1 < getKorkeus()) {
                    if (kentta[k + 1][l].equals("O")) {
                        kentta[k][l] = kirjain;
                        break;
                    }
                }
            }
        }
    }

    private void randomaaPaikkaEiReunoille(String kirjain) {
        System.out.println("randomataan " + kirjain);
        while (true) {
            int k = (int) Math.floor((Math.random() * getKorkeus()));
            int l = (int) Math.floor((Math.random() * getLeveys()));
            if (kirjain.equals("D")) {
                this.ovenKorkeus = k;
                this.ovenLeveys = l;
            }
            if (kentta[k][l].equals("S")) {
                if (l - 1 >= 0) {
                    if (kentta[k][l - 1].equals("O") && l + 1 < getLeveys()) {
                        kentta[k][l] = kirjain;
                        break;
                    }
                }
                if (l + 1 < getLeveys()) {
                    if (kentta[k][l + 1].equals("O") && l - 1 >= 0) {
                        kentta[k][l] = kirjain;
                        break;
                    }
                }
                if (k - 1 >= 0) {
                    if (kentta[k - 1][l].equals("O") && k + 1 < getKorkeus()) {
                        kentta[k][l] = kirjain;
                        break;
                    }
                }
                if (k + 1 < getKorkeus()) {
                    if (kentta[k + 1][l].equals("O") && k - 1 >= 0) {
                        kentta[k][l] = kirjain;
                        break;
                    }
                }
            }
        }
    }

    private void checkSynnytaMorko() {
        int aika = 100;
        if (taso == 1) {
            aika = 15;
        }
        if (taso == 2) {
            aika = 10;
        }
        if (taso == 3) {
            aika = 5;
        }
        // "Jos on kulunut se väli millä mörköjä halutaan spawnata"
        if (((System.currentTimeMillis() / 1000) - aika) > (morkoSpawnAlkuAika / 1000)) {
            synnytaMorko();
            morkoSpawnAlkuAika = System.currentTimeMillis();
        }
    }

    private void synnytaMorko() {
        Hirvio hirvio = new Hirvio(ovenLeveys, ovenKorkeus, this);
        hirviot.add(hirvio);
    }

    private void checkMamelukkikala() throws JavaLayerException, FileNotFoundException {
        if (getMamelukkikala() == null) {
            if (taso == 2) {
                System.out.println("tarkistetaan mamelukkikala");
                if (((System.currentTimeMillis() / 1000) - 5) > (mamelukkiSpawnAika / 1000)) {
                    System.out.println("laitetaan mamelukkikala");
                    laitaMamelukkikala();
                    musiikinSoittaja.stop();
                    musiikinSoittaja.play("mamelukkikala");
                    this.mamelukkiLiikkumaAika = System.currentTimeMillis();
                    setDelay(getDelay() / 2);
                }
            }
        } else {
            if (((System.currentTimeMillis() / 1000) - 30) > (mamelukkiLiikkumaAika / 1000)) {
                voidaankoLiikkua = !voidaankoLiikkua; // Jotta mamelukkikalasta tulisi 2x pelaajaa nopeampi
                mamelukkikala.liiku(getKorkeus(), getLeveys());
            }
        }
    }

    private void laitaMamelukkikala() {
        System.out.println("randomataan mamelukkikalan sijainti");
        while (true) {
            int k = (int) Math.floor((Math.random() * getKorkeus()));
            int l = (int) Math.floor((Math.random() * getLeveys()));
//            if (kirjain.equals("D")) {
//                this.ovenKorkeus = k;
//                this.ovenLeveys = l;
//            }
            if (kentta[k][l].equals("O")) {
                if (l + 10 < pelaaja.getX()) {
                    this.mamelukkikala = new Mamelukkikala(l, k, this);
                    break;
                }

                if (l - 10 > pelaaja.getX()) {
                    this.mamelukkikala = new Mamelukkikala(l, k, this);
                    break;
                }

                if (k + 10 < pelaaja.getY()) {
                    this.mamelukkikala = new Mamelukkikala(l, k, this);
                    break;
                }

                if (k - 10 < pelaaja.getY()) {
                    this.mamelukkikala = new Mamelukkikala(l, k, this);
                    break;
                }
            }
        }
    }

    public Mamelukkikala getMamelukkikala() {
        return mamelukkikala;
    }

    public ArrayList<Hirvio> getHirvio() {
        return hirviot;
    }

    public void asetaMorkoSpawnAlkuAika() {
        this.morkoSpawnAlkuAika = System.currentTimeMillis();
    }
}
