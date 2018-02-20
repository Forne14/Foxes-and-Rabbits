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
    
    protected boolean infected;

    /**
     * Constructor for objects of class Plant
     */
    public Living(Field field, Location location, boolean infected)
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
 
        protected void setInfected(boolean infection)
    {
         infected = infection;
    }
    
     protected boolean getInfected()
    {
        return infected;
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
    
    protected Location infect()
    {
        Field field = getField();
        List<Location> adjacent = field.adjacentLocations(getLocation());
        Iterator<Location> it = adjacent.iterator();
        while(it.hasNext()) {
           Location where = it.next();
           Object animal = field.getObjectAt(where);
           Living poorSoul = (Living) animal;
           if(poorSoul != null && poorSoul instanceof Animal)
             {
               if(this.getInfected()==true){
                    poorSoul.setInfected(true);  
                    return where;
               }
             }
        }
        System.out.println("animal has infected another animal");
        return null;
    }
}
