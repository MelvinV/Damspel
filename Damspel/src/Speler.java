
public class Speler {
	private String naam;
	private int aantal_stenen;
	private boolean laatste_zet;
	private int[] selectie = new int[2];
	
	public Speler(String nm, int as, boolean lz)
	{
		naam = nm;
		aantal_stenen = as;
		laatste_zet = lz;
		selectie[0] = -1;
		selectie[1] = -1;
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

}