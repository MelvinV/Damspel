package src;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

import java.util.ArrayList;


public class Damspel {

	private Speler wit;
	private Speler zwart;
	private final String witnaam = "WIT";
	private final String zwartnaam = "ZWART";
	private final int stenen_per_speler = 20;
	private ArrayList<BoardTileStatus> status = new ArrayList<BoardTileStatus>(100);
	private boolean doFocus, confirm, forcelock;
	
	// Define a variable to store the property
    private StringProperty foutmelding = new SimpleStringProperty();
	
	public Damspel()
	{
		 wit = new SpelerBuilder().setNaam(witnaam).setAantal_stenen(stenen_per_speler).setLaatste_zet(false).createSpeler();
		 zwart = new SpelerBuilder().setNaam(zwartnaam).setAantal_stenen(stenen_per_speler).setLaatste_zet(true).createSpeler();
	}
	
	public void reset()
	{
		wit.spelerReset(witnaam, stenen_per_speler, false);
		zwart.spelerReset(zwartnaam, stenen_per_speler, true);
	}
	
	public BoardTileStatus getVeldStatus(int veldId)
	{
		return status.get(veldId);
	}
	
	 // Define a getter for the property's value
    public final String getfoutmelding(){return foutmelding.get();}
 
    // Define a setter for the property's value
    public final void setfoutmelding(String value){foutmelding.set(value);}
 
     // Define a getter for the property itself
    public StringProperty foutmeldingProperty() {return foutmelding;}
	
	public boolean isVeldSpeelbaar(int veldId)
	{
		int selectie = getSpelerObject().getSelectie(0);
		boolean isWhitePlayer = getSpeler() == BoardTileStatus.WIT;
		if(selectie != -1)
		{
			if(status.get(veldId) == BoardTileStatus.LEEG)
			{ 
				//speler probeert 1 stap vooruit te gaan...
				//de witte mag alleen achteruit gaan qua button-telling, de zwarte alleen vooruit...
				if((isWhitePlayer && (selectie-11 == veldId || selectie-9 == veldId)) || (!isWhitePlayer && (selectie+11 == veldId || selectie+9 == veldId)))
				{
					if(!kanSteenAfpakken())
					{
						setfoutmelding("U heeft een gewone zet gedaan.");
						getSpelerObject().setSelectie(1, veldId);
						doeZet(selectie, veldId, true, false);
						return true; 
					}
					else
					{
						setfoutmelding("U kunt een steen van de tegenstander afpakken, daarom mag u geen gewone zet doen.");
					}
				}

				if(spelerProbeertTeSlaan(veldId))
				{
					setfoutmelding("Tegenstanders steen succesvol weggespeeld.");
					//je moet de tussendam verwijderen van het spelbord. de code hieronder bepaald de locatie ervan.
					int index = (selectie > veldId) ? veldId+0((selectie-veldId)/2) : selectie+((veldId-selectie)/2);
					System.out.println(Integer.toString(index);
					status.set(index, BoardTileStatus.LEEG);
					getTegenStanderObject().verliesSteen();
					
					getSpelerObject().setSelectie(1, veldId);
					doeZet(selectie, veldId, false, true);
					doFocus = true;
					return true;
				}
			}
			//als de speler weer op hetzelfde knopje drukt (waar al focus op zat)
			if(selectie == veldId)
			{ 
				if(forcelock)
				{
					setfoutmelding("U moet met deze steen doorspelen, druk op ESC om uw beurt te vergeven.");
				}
				else
				{
					setfoutmelding("U heeft uw eerder geselecteerde zet gecanceld");
					getSpelerObject().setSelectie(0, -1); 
					System.out.println(getSpelerObject().getNaam()); 
					doFocus = true; 
				}
			}
		}
		else
		{
			//System.out.println(getSpeler() == getVeldStatus(veldId));
			if(getSpeler() == getVeldStatus(veldId))
			{
				getSpelerObject().setSelectie(0, veldId);
				doFocus = true;
			}
		}
		return false;
	}

	public boolean spelerProbeertTeSlaan(int veldId)
	{
		int selectie = getSpelerObject().getSelectie(0);
        boolean isWhitePlayer = getSpeler() == BoardTileStatus.WIT;
        BoardTileStatus tegenstander = isWhitePlayer ? BoardTileStatus.ZWART : BoardTileStatus.WIT;

		//speler probeert over tegenstanders dam heen te gaan (2 stappen vooruit/achteruit).
		return (status.get(selectie-11) == tegenstander && selectie-22 == veldId)
				|| (status.get(selectie+11) == tegenstander && selectie+22 == veldId)
				|| (status.get(selectie-9) == tegenstander && selectie-18 == veldId)
				|| (status.get(selectie+9) == tegenstander && selectie+18 == veldId);
	}
	
	//flipzet is false wanneer de speler een dam afpakt... hij blijft dan aan de beurt.
	public boolean doeZet(int vanVeldId, int naarVeldId, boolean flipZet, boolean lock)
	{
		forcelock = lock;
		
		//update de arraylist... als deze functie true returned zal DamspelApp de array uitlezen.
		status.set(vanVeldId, BoardTileStatus.LEEG);
		status.set(naarVeldId, getSpeler());

		getSpelerObject().setSelectie(0, lock ? naarVeldId : -1);
		getSpelerObject().setSelectie(1, -1);
		
		if(flipZet)
		{
			wit.flipLaatsteZet();
			zwart.flipLaatsteZet();
		}
		return true;
	}
	
	public void flipConfirm()
	{
		confirm = !confirm;
	}
	
	public Speler getSpecifiekeSpeler(int index)
	{
		if(index == 0 || index == 1)
		{
			return index == 0 ? wit : zwart;
		} 
		return null;
	}
	
	public BoardTileStatus getSpeler()
	{
		return wit.getLaatsteZet() ? BoardTileStatus.ZWART : BoardTileStatus.WIT;
	}
	
	public Speler getSpelerObject()
	{
		return wit.getLaatsteZet() ? zwart : wit;
	}
	
	public Speler getTegenStanderObject()
	{
		return getSpeler() == BoardTileStatus.WIT ? zwart : wit;
	}
	
	public void addArray(BoardTileStatus element)
	{
		status.add(element);
	}
	
	public BoardTileStatus getArray(int index)
	{
		return status.get(index);
	}
	
	public int getLength()
	{
		return status.size();
	}
	public void setArray(int index, BoardTileStatus element)
	{
		status.set(index, element);
	}
	
	public boolean getDoFocus()
	{
		return doFocus;
	}
	
	public void setDoFocus(boolean setter)
	{
		doFocus = setter;
	}
	
	public boolean kanSteenAfpakken()
	{
		BoardTileStatus player = getSpeler();
		boolean isWhitePlayer = player == BoardTileStatus.WIT;
		BoardTileStatus tegenstander = isWhitePlayer ? BoardTileStatus.ZWART : BoardTileStatus.WIT;
		int selectie = getSpelerObject().getSelectie(0);
		
		for(int i = 0; i < 100; i++)
		{
			if(status.get(i) == player)
			{
				//speler probeert over tegenstanders dam heen te gaan (2 stappen vooruit/achteruit).
				if((i-22 > -1 && status.get(i-11) == tegenstander && status.get(i-22) == BoardTileStatus.LEEG)
				|| (i+22 < 100 && status.get(i+11) == tegenstander && status.get(i+22) == BoardTileStatus.LEEG)
				|| (i-18 > -1 && status.get(i-9) == tegenstander && status.get(i-18) == BoardTileStatus.LEEG)
				|| (i+18 < 100 && status.get(i+9) == tegenstander && status.get(i+18) == BoardTileStatus.LEEG))
				{
					if(forcelock && i == selectie) return true;
					if(!forcelock) return true;
				}
			}
		}
		return false;
		
	}
	
	public boolean vergeefBeurt()
	{
		int selectie = getSpelerObject().getSelectie(0);
		boolean isWhitePlayer = getSpeler() == BoardTileStatus.WIT;
		boolean kanGewoneZetDoen = (isWhitePlayer && (status.get(selectie-11) == BoardTileStatus.LEEG || status.get(selectie-9) == BoardTileStatus.LEEG)) || (!isWhitePlayer && (status.get(selectie+11) == BoardTileStatus.LEEG || status.get(selectie+9) == BoardTileStatus.LEEG));
		
		if(forcelock && !kanSteenAfpakken() && !kanGewoneZetDoen)
		{
				getSpelerObject().setSelectie(0, -1);
				getSpelerObject().setSelectie(1, -1);
				wit.flipLaatsteZet();
				zwart.flipLaatsteZet();
				forcelock = false;
				return true;
		}
		else
		{
			setfoutmelding("U mag uw beurt niet vergeven als u nog een zet kan doen...");
		}
		return false;
	}
	
	public String toString()
	{
		StringBuilder s = new StringBuilder();

		for(BoardTileStatus element : status)
		{
			s.append(element.name() + ", ");
		}
		return s.toString();
	}
}
