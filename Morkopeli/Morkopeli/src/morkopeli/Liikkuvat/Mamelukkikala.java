package morkopeli.Liikkuvat;

import morkopeli.Logiikka.Luola;

public class Mamelukkikala implements Liikkuva {

    private int x;
    private int y;
    private Luola luola;
    private Suunta suunta; // Mamelukkikala ei voi mennä > < tyyliin

    public Mamelukkikala(int xkoord, int ykoord, Luola luola) {
        this.x = xkoord;
        this.y = ykoord;
        this.luola = luola;
        int r = (int) (Math.random() * 4) + 1;
        if (r == 1) {
            setSuunta(Suunta.OIKEA);
        }
        if (r == 2) {
            setSuunta(Suunta.VASEN);
        }
        if (r == 3) {
            setSuunta(Suunta.ALAS);
        }
        if (r == 4) {
            setSuunta(Suunta.YLOS);
        }
    }

    public void liiku(int korkeus, int leveys) {
        int r = 1;
        // Jotta ei mentäisi suuntaan josta tultiin
        String[][] kentta = luola.getKentta();
        while (true) {
            r = (int) (Math.random() * 4) + 1;
            if (r == 1 && suunta != Suunta.YLOS && !kentta[y + 1][x].equals("S") && !kentta[y + 1][x].equals("E")) {
                break;
            }
            if (r == 2 && suunta != Suunta.ALAS && !kentta[y - 1][x].equals("S") && !kentta[y - 1][x].equals("E")) {
                break;
            }
            if (r == 3 && suunta != Suunta.VASEN && !kentta[y][x + 1].equals("S") && !kentta[y][x + 1].equals("E")) {
                break;
            }
            if (r == 4 && suunta != Suunta.OIKEA && !kentta[y][x - 1].equals("S") && !kentta[y][x - 1].equals("E")) {
                break;
            }
        }

        if (r == 1 && this.y + 1 < korkeus) {
            this.y++;
            setSuunta(Suunta.ALAS);
        }
        if (r == 2 && this.y - 1 > 0) {
            this.y--;
            setSuunta(Suunta.YLOS);
        }
        if (r == 3 && this.x + 1 < leveys) {
            this.x++;
            setSuunta(Suunta.OIKEA);
        }
        if (r == 4 && this.x - 1 > 0) {
            this.x--;
            setSuunta(Suunta.VASEN);
        }
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public void setSuunta(Suunta suunta) {
        this.suunta = suunta;
    }

    @Override
    public void liiku() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
}