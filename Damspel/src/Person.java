/**
 * Created by melvin on 24-2-2017.
 */
public class Person extends BreathingSomething implements PersonImp {

    public int aantal_ogen;

    @Override
    public void setAantal_ogen(int aantal_ogen) {
        this.aantal_ogen = aantal_ogen;
    }

    public Person()
    {
        setAantal_ogen(2);
    }
}
