package src;
public class SpelerBuilder {
    private String naam;
    private int aantalStenen;
    private boolean laatsteZet;

    public SpelerBuilder setNaam(String naam) {
        this.naam = naam;
        return this;
    }

    public SpelerBuilder setAantalStenen(int aantalStenen) {
        this.aantalStenen = aantalStenen;
        return this;
    }

    public SpelerBuilder setLaatsteZet(boolean laatsteZet) {
        this.laatsteZet = laatsteZet;
        return this;
    }

    public Speler createSpeler() {
        return Speler.createSpeler(naam, aantalStenen, laatsteZet);
    }
}