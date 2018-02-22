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
     * Constructor for objects of class TheHunted
     */
    public TheHunted(Field field, Location location, boolean gender, boolean infected)
    {
        super(field, location, gender); 
        this.infected = infected;
        this.gender = gender;
    }
   
    /**
     * Set the boolean infected field of an animal to true or false 
     * @param       boolean
     */
    protected void setInfected(boolean infection)
    {
         infected = infection;
    }
    
    /**
     * Retrieve whether an animal is infected or not.
     * @return       boolean
     */
    protected boolean getInfected()
    {
        return infected;
    }
    
    /**
     * An infected animal searches for the animals in the surrounding fields and infects the
     * animals that are not infected.
     */
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
        }
    }
    
    
}
