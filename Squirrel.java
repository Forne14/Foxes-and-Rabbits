import java.util.List;
import java.util.Random;
import java.util.Iterator;
/**
 * A simple model of a squirrel.
 * Squirrels age, move, breed, and die.
 * 
 * @author David J. Barnes and Michael Kölling
 * @version 2016.02.29 (2)
 */
public class Squirrel extends TheHunted
{
    
    private static final int OAKTREE_FOOD_VALUE = 7;
   

    /**
     * Create a new squirrel. A squirrel may be created with age
     * zero (a new born) or with a random age.
     * 
     * @param randomAge If true, the squirrel will have a random age.
     * @param field The field currently occupied.
     * @param location The location within the field.
     */
    public Squirrel(boolean randomAge, Field field, Location location)
    {
        super(field, location);
        MAX_AGE = 120;
        BREEDING_AGE = 15;
        BREEDING_PROBABILITY = 0.40;
        MAX_LITTER_SIZE = 6;
        if(randomAge) {
            age = rand.nextInt(MAX_AGE);
        }
    }
    
    public void act(List<Animal> newSquirrels)
    {
        incrementAge();
        incrementHunger();
        if(isAlive()) {
            giveBirth(newSquirrels);            
            // Move towards a source of food if found.
            Location newLocation = findFood();
            if(newLocation == null) { 
                // No food found - try to move to a free location.
                newLocation = getField().freeAdjacentLocation(getLocation());
            }
            // See if it was possible to move.
            if(newLocation != null) {
                setLocation(newLocation);
            }
            else {
                // Overcrowding.
                setDead();
            }
        }
    }
    
    /**
     * Look for Squirrels adjacent to the current location.
     * Only the first live prey is eaten.
     * @return Where food was found, or null if it wasn't.
     */
    private Location findFood()
    {
        Field field = getField();
        List<Location> adjacent = field.adjacentLocations(getLocation());
        Iterator<Location> it = adjacent.iterator();
        while(it.hasNext()) {
            Location where = it.next();
            Object animal = field.getObjectAt(where);
            if(animal instanceof Squirrel) {
                Squirrel squirrel = (Squirrel) animal;
                if(squirrel.isAlive()) { 
                    squirrel.setDead();
                    foodLevel += OAKTREE_FOOD_VALUE;
                                        System.out.println("Squirrel has eaten");
                    return where;
                }
            }
        }
        return null;
    }
    
    /**
     * Check whether or not this Squirrel is to give birth at this step.
     * New births will be made into free adjacent locations.
     * @param newSquirreles A list to return newly born Squirreles.
     */
    private void giveBirth(List<Animal> newSquirrels)
    {
        // New Squirreles are born into adjacent locations.
        // Get a list of adjacent free locations.
        Field field = getField();
        List<Location> free = field.getFreeAdjacentLocations(getLocation());
        int births = breed();
        for(int b = 0; b < births && free.size() > 0; b++) {
            Location loc = free.remove(0);
            Squirrel young = new Squirrel(false, field, loc);
            newSquirrels.add(young);
                                System.out.println("New Squirrels");
        }
    }
    
}

