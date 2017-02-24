public class SpelerBuilder {
    private String naam;
    private int aantal_stenen;
    private boolean laatste_zet;

    public SpelerBuilder setNaam(String naam) {
        this.naam = naam;
        return this;
    }

    public SpelerBuilder setAantal_stenen(int aantal_stenen) {
        this.aantal_stenen = aantal_stenen;
        return this;
    }

    public SpelerBuilder setLaatste_zet(boolean laatste_zet) {
        this.laatste_zet = laatste_zet;
        return this;
    }

    public Speler createSpeler() {
        return Speler.createSpeler(naam, aantal_stenen, laatste_zet);
    }
}