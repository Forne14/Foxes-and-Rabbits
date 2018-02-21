import java.util.List;
import java.util.Iterator;
/**
 * Write a description of class Living here.
 *
 * @author (your name)
 * @version (a version number or a date)
 */
public abstract class Living
{
    // Whether the plant is living or not.
    protected boolean alive;
    // The plant's field.
    protected Field field;
    // The plant's position in the field.
    protected Location location;

    /**
     * Constructor for objects of class Plant
     */
    public Living(Field field, Location location)
    {
        alive = true;
        this.field = field;
        setLocation(location);
    }
    
    /**
     * Check whether the living is living or not.
     * @return true if the animal is still living.
     */
    protected boolean isAlive()
    {
        return alive;
    }
    
    /**
     * Make this animal act - that is: make it do
     * whatever it wants/needs to do.
     * @param newAnimals A list to receive newly born animals.
     */
    abstract public void act(List<Living> newLiving, String currentTimeOfDay, String weather); 

    /**
     * Indicate that the living is no longer living.
     * It is removed from the field.
     */
    protected void setDead()
    {
        alive = false;
        if(location != null) {
            field.clear(location);
            location = null;
            field = null;
        }
    }
    
    /**
     * Place the living at the new location in the given field.
     * @param newLocation The animal's new location.
     */
    protected void setLocation(Location newLocation)
    {
        if(location != null) {
            field.clear(location);
        }
        location = newLocation;
        field.place(this, newLocation);
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
    
    
}
