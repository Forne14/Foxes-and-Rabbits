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
    protected String gender;
     // A shared random number generator to control breeding.
    protected static Random rand = new Random();
    /**
     * Create a new animal at location in field.
     * 
     * @param field The field currently occupied.
     * @param location The location within the field.
     */
    public Animal(Field field, Location location)
    {
        super(field, location);
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
    
    protected void setGender(boolean sex)
    {
        if(sex)
        {
            gender = "f";
        }
        else
        {
            gender = "m";
        }
    }
    
    private Location findMate()
    {
        Field field = getField();
        List<Location> adjacent = field.adjacentLocations(getLocation());
        Iterator<Location> it = adjacent.iterator();
        while(it.hasNext()) {
            Location where = it.next();
            Object animal = field.getObjectAt(where);
            if(animal.getGender().equals()) {
                Rabbit rabbit = (Rabbit) animal;
                if(rabbit.isAlive()) { 
                    rabbit.setDead();
                    foodLevel = RABBIT_FOOD_VALUE;
                    return where;
                }
            }
        }
        return null;
    }
    
    protected String getGender()
    {
        return gender;
    }
}
