import java.util.List;
import java.util.Iterator;
import java.util.Random;
/**
 * A class representing shared characteristics of animals.
 * 
 * @author David J. Barnes and Michael Kölling
 * @version 2016.02.29 (2)
 */
public abstract class Animal extends Living
{
    protected static final double GENDER_PROBABILITY = 0.5;
    protected boolean gender;
     // A shared random number generator to control breeding.
    protected static Random rand = new Random();
    /**
     * Create a new animal at location in field.
     * 
     * @param field The field currently occupied.
     * @param location The location within the field.
     */
    public Animal(Field field, Location location, boolean gender)
    {
        super(field, location);
        this.gender = gender;
    }

    /**
     * Return the animal's location.
     * @return The animal's location.
     */
    protected Location getLocation()
    {
        return location;
    }
    
    /**
     * Return the animal's field.
     * @return The animal's field.
     */
    protected Field getField()
    {
         return field;
    }
    
    protected boolean generateRandomGender()
    {
        return rand.nextBoolean();
    }
    
    protected boolean setGender(boolean female)
    {
            return gender = female;
    }
    
    /*protected boolean findMate()
    {
        Field field = getField();
        List<Location> adjacent = field.adjacentLocations(getLocation());
        Iterator<Location> it = adjacent.iterator();
        while(it.hasNext()) {
            Location where = it.next();
            Object surroundingAnimal = field.getObjectAt(where);
            Animal mate = (Animal) surroundingAnimal;
            boolean currentAnimalGender = getGender(); 
            if(this.getGender() != mate.getGender()) {   
                 return true;
            }
            else{ 
                return false;
            }
        }
        return false; 
    }
    */

    
    protected boolean getGender()
    {
        return gender;
    }
}
