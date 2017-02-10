import java.util.ArrayList;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;


public class Damspel {

	private Speler wit;
	private Speler zwart;
	private final String witnaam = "WIT";
	private final String zwartnaam = "ZWART";
	private final int stenen_per_speler = 20;
	private ArrayList<String> status = new ArrayList<String>(100);
	private boolean doFocus, confirm, forcelock;
	
	// Define a variable to store the property
    private StringProperty foutmelding = new SimpleStringProperty();
	
	public Damspel()
	{
		 wit = new Speler(witnaam, stenen_per_speler, false);
		 zwart = new Speler(zwartnaam, stenen_per_speler, true);
	}
	
	public void reset()
	{
		wit.spelerReset(witnaam, stenen_per_speler, false);
		zwart.spelerReset(zwartnaam, stenen_per_speler, true);
	}
	
	public String getVeldStatus(int veldId)
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
		int selectie1 = getSpelerObject().getSelectie(0);
		boolean isWhitePlayer = getSpeler().equals("WIT");
		//if(isWhitePlayer) String tegenstander = 
		String tegenstander = isWhitePlayer ? "ZWART" : "WIT";	
		if(selectie1 != -1)
		{
			if(status.get(veldId).equals("LEEG")) 
			{ 
				//speler probeert 1 stap vooruit te gaan...
				//de witte mag alleen achteruit gaan qua button-telling, de zwarte alleen vooruit...
				if((isWhitePlayer && (selectie1-11 == veldId || selectie1-9 == veldId)) || (!isWhitePlayer && (selectie1+11 == veldId || selectie1+9 == veldId)))
				{
					if(!kanSteenAfpakken())
					{
						setfoutmelding("U heeft een gewone zet gedaan.");
						getSpelerObject().setSelectie(1, veldId);
						doeZet(selectie1, veldId, true, false); 
						return true; 
					}
					else
					{
						setfoutmelding("U kunt een steen van de tegenstander afpakken, daarom mag u geen gewone zet doen.");
					}
				}
			
				//speler probeert over tegenstanders dam heen te gaan (2 stappen vooruit/achteruit).
				if((status.get(selectie1-11).equals(tegenstander) && selectie1-22 == veldId)  
				|| (status.get(selectie1+11).equals(tegenstander) && selectie1+22 == veldId) 
				|| (status.get(selectie1-9).equals(tegenstander) && selectie1-18 == veldId) 
				|| (status.get(selectie1+9).equals(tegenstander) && selectie1+18 == veldId))
				{
					setfoutmelding("Tegenstanders steen succesvol weggespeeld.");
					//je moet de tussendam verwijderen van het spelbord. de code hieronder bepaald de locatie ervan.
					int index = (selectie1 > veldId) ? veldId+((selectie1-veldId)/2) : selectie1+((veldId-selectie1)/2);
					System.out.println(index+"");
					status.set(index, "LEEG");
					getTegenStanderObject().verliesSteen();
					
					getSpelerObject().setSelectie(1, veldId);
					doeZet(selectie1, veldId, false, true);
					doFocus = true;
					return true;
				}
			}
			//als de speler weer op hetzelfde knopje drukt (waar al focus op zat)
			if(selectie1 == veldId) 
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
			//System.out.println(getSpeler().equals(getVeldStatus(veldId)));
			if(getSpeler().equals(getVeldStatus(veldId)))
			{
				getSpelerObject().setSelectie(0, veldId);
				doFocus = true;
			}
		}
		return false;
	}
	
	//flipzet is false wanneer de speler een dam afpakt... hij blijft dan aan de beurt.
	public boolean doeZet(int vanVeldId, int naarVeldId, boolean flipZet, boolean lock)
	{
		forcelock = lock;
		
		//update de arraylist... als deze functie true returned zal DamspelApp de array uitlezen.
		status.set(vanVeldId, "LEEG");
		status.set(naarVeldId, getSpeler());
		
		if(!lock)
		{
			getSpelerObject().setSelectie(0, -1);
			getSpelerObject().setSelectie(1, -1);
		}
		else
		{
			getSpelerObject().setSelectie(0, naarVeldId);
			getSpelerObject().setSelectie(1, -1);
		}
		
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
		if(index >= 0 && index < 2)
		{
		 if(index == 0) return wit;
		 if(index == 1) return zwart;
		} 
		return null;
	}
	
	public String getSpeler()
	{
		return wit.getLaatsteZet() ? "ZWART" : "WIT";
	}
	
	public Speler getSpelerObject()
	{
		return wit.getLaatsteZet() ? zwart : wit;
	}
	
	public Speler getTegenStanderObject()
	{
		return getSpeler().equals("WIT") ? zwart : wit;
	}
	
	public void addArray(String element)
	{
		status.add(element);
	}
	
	public String getArray(int index)
	{
		return status.get(index);
	}
	
	public int getLength()
	{
		return status.size();
	}
	public void setArray(int index, String element)
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
		String player = getSpeler();
		boolean isWhitePlayer = player.equals("WIT");
		String tegenstander = isWhitePlayer ? "ZWART" : "WIT";	
		int selectie = getSpelerObject().getSelectie(0);
		
		for(int i = 0; i < 100; i++)
		{
			if(status.get(i).equals(player))
			{
				//speler probeert over tegenstanders dam heen te gaan (2 stappen vooruit/achteruit).
				if((i-22 > -1 && status.get(i-11).equals(tegenstander) && status.get(i-22) == "LEEG")  
				|| (i+22 < 100 && status.get(i+11).equals(tegenstander) && status.get(i+22) == "LEEG") 
				|| (i-18 > -1 && status.get(i-9).equals(tegenstander) && status.get(i-18) == "LEEG") 
				|| (i+18 < 100 && status.get(i+9).equals(tegenstander) && status.get(i+18) == "LEEG"))
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
		boolean isWhitePlayer = getSpeler().equals("WIT");
		boolean kanGewoneZetDoen = (isWhitePlayer && (status.get(selectie-11) == "LEEG" || status.get(selectie-9) == "LEEG")) || (!isWhitePlayer && (status.get(selectie+11) == "LEEG" || status.get(selectie+9) == "LEEG"));
		
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
		String s = "";
		for(String element : status)
		{
			s += element + ", ";
		}
		return s;
	}
}
