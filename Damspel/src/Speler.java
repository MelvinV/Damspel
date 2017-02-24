
public class Speler {
	private final Person person = new Person();
	private String naam;
	private int aantal_stenen;
	private boolean laatste_zet;
	private int[] selectie = new int[2];
	
	private Speler(String naam, int aantal_stenen, boolean laatste_zet)
	{
		spelerReset(naam, aantal_stenen, laatste_zet);
	}

	public static Speler createSpeler(String naam, int aantal_stenen, boolean laatste_zet) {
		return new Speler(naam, aantal_stenen, laatste_zet);
	}

	public void verliesSteen()
	{
		aantal_stenen--;
	}
	
	public boolean getLaatsteZet()
	{
		return laatste_zet;
	}
	
	public void flipLaatsteZet()
	{
		laatste_zet = !laatste_zet;
	}
	
	public void setSelectie(int index, int value)
	{
		if(index >= 0 && index < 2) selectie[index] = value;
	}
	
	public int getSelectie(int index)
	{
		return (index >= 0 && index < 2) ? selectie[index] : -1;
	}
	
	public int getSteenHoeveelheid()
	{
		return aantal_stenen;
	}
	
	public String getNaam()
	{
		return naam;
	}

	public void spelerReset(String nm, int as, boolean lz)
	{
		naam = nm;
		aantal_stenen = as;
		laatste_zet = lz;
		selectie[0] = -1;
		selectie[1] = -1;
	}

	public int getAantal_ogen() {
		return PersonImp.getAantal_ogen(person.aantal_ogen);
	}

	public void setAantal_ogen(int aantal_ogen) {
		person.setAantal_ogen(aantal_ogen);
	}
}
