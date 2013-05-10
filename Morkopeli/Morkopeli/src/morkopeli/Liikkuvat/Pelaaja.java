package morkopeli.Liikkuvat;

import morkopeli.Logiikka.Luola;



public class Pelaaja implements Liikkuva {

    private int x;
    private int y;
    private int leveys;
    private int korkeus;
    private Luola luola;
    private Suunta suunta;

    public Pelaaja(Suunta alkusuunta, int kentanLeveys, int kentanKorkeus, Luola luola) {
        this.x = 0;
        this.y = 0;
        this.korkeus = kentanKorkeus;
        this.leveys = kentanLeveys;
        this.suunta = alkusuunta;
        this.luola = luola;
    }

    @Override
    public void liiku() {
        String[][] kentta = luola.getKentta();

        if (this.suunta.equals(Suunta.YLOS)) {
            if (!kentta[y - 1][x].equals("S") && y - 1 >= 0) {
                setY(this.y - 1);
            }
        } else if (this.suunta.equals(Suunta.ALAS)) {
            if (!kentta[y + 1][x].equals("S") && y + 1 < korkeus) {
                setY(this.y + 1);
            }
        } else if (this.suunta.equals(Suunta.OIKEA)) {
            if (!kentta[y][x + 1].equals("S") && x + 1 < leveys) {
                setX(this.x + 1);
            }
        } else if (this.suunta.equals(Suunta.VASEN)) {
            if (!kentta[y][x - 1].equals("S") && x - 1 >= 0) {
                setX(this.x - 1);
            }
        }
    }

    public Suunta getSuunta() {
        return suunta;
    }

    public void setSuunta(Suunta suunta) {
        this.suunta = suunta;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public void setX(int x) {
        this.x = x;
    }

    public void setY(int y) {
        this.y = y;
    }

    public void setPelaajanKentanKorkeus(int k) {
        this.korkeus = k;
    }

    public void setPelaajanKentanLeveys(int l) {
        this.leveys = l;
    }
}
