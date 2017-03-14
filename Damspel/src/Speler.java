package src;
public class Speler {
    private String naam;
    private int aantalStenen;
    private boolean laatsteZet;
    private int[] selectie = new int[2];

    private Speler(String naam, int aantalStenen, boolean laatsteZet) {
        spelerReset(naam, aantalStenen, laatsteZet);
    }

    public static Speler createSpeler(String naam, int aantalStenen, boolean laatsteZet) {
        return new Speler(naam, aantalStenen, laatsteZet);
    }

    public void verliesSteen() {
        aantalStenen--;
    }

    public boolean getLaatsteZet() {
        return laatsteZet;
    }

    public void flipLaatsteZet() {
        laatste_zet = !laatsteZet;
    }

    public void setSelectie(int index, int value) {
        if (index >= 0 && index < 2) {
            selectie[index] = value;
        }
    }

    public int getSelectie(int index) {
        return (index >= 0 && index < 2) ? selectie[index] : -1;
    }

    public int getSteenHoeveelheid() {
        return aantalStenen;
    }

    public String getNaam() {
        return naam;
    }

    public void spelerReset(String nm, int as, boolean lz) {
        naam = nm;
        aantalStenen = as;
        laatsteZet = lz;
        selectie[0] = -1;
        selectie[1] = -1;
    }

}