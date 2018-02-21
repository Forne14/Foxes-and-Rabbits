import java.util.List;
import java.util.Iterator;
import java.util.Random;
/**
 * Write a description of class Hunted here.
 *
 * @author (your name)
 * @version (a version number or a date)
 */
public abstract class TheHunted extends Animal
{
    boolean infected;
    /**
     * Constructor for objects of class Hunted
     */
    public TheHunted(Field field, Location location, boolean gender, boolean infected)
    {
        super(field, location, gender); 
        this.infected = infected;
        this.gender = gender;
    }
   
    protected void setInfected(boolean infection)
    {
         infected = infection;
    }
    
    protected boolean getInfected()
    {
        return infected;
    }
    
    protected void infect()
    {
        Field field = getField();
        if(field == null){
            return;
        }
        else{
            List<Location> adjacent = field.adjacentLocations(getLocation());
            Iterator<Location> it = adjacent.iterator();
            while(it.hasNext()) {
               Location where = it.next();
               Object theHunted = field.getObjectAt(where);
               if(theHunted != null && theHunted instanceof TheHunted){
                    TheHunted poorSoul = (TheHunted) theHunted;
                    if(poorSoul.isAlive() && poorSoul.getInfected()==false){
                         poorSoul.setInfected(true);
                         foodLevel -= 2;
                    }
               }
            }
            System.out.println("animal has infected another animal");
        }
    }
    
    
}
