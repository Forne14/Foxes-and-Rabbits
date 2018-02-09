import java.util.List;
import java.util.Iterator;
import java.util.Random;
/**
 * Write a description of class Owl here.
 *
 * @author (your name)
 * @version (a version number or a date)
 */
public class Owl extends TheHunter
{
    // Characteristics shared by all owl (class variables).
    
    // number of steps a owl can go before it has to eat again.
    private static final int SQUIRREL_FOOD_VALUE = 12;

    /**
     * Constructor for objects of class owl
     */
    public Owl(boolean randomAge, Field field, Location location)
    {
        super(field,location);
        BREEDING_AGE =30;
        MAX_AGE = 300;
        BREEDING_PROBABILITY = 0.8;
        MAX_LITTER_SIZE = 5;
        if(randomAge) {
            age = rand.nextInt(MAX_AGE);
            foodLevel = rand.nextInt(SQUIRREL_FOOD_VALUE) ;
        }
        else {
            age = 0;
            foodLevel = SQUIRREL_FOOD_VALUE;
        }
        
    }
    
    public void act(List<Animal> newOwls)
    {
        incrementAge();
        incrementHunger();
        System.out.println("owl class check");
        if(isAlive()) {
            giveBirth(newOwls);            
            // Move towards a source of food if found.
            System.out.println("find owl food");
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
                    foodLevel += SQUIRREL_FOOD_VALUE;
                                        System.out.println("owl has eaten");
                    return where;
                }
            }
        }
        return null; 
    }
    
    
    /**
     * Check whether or not this fox is to give birth at this step.
     * New births will be made into free adjacent locations.
     * @param newFoxes A list to return newly born foxes.
     */
    private void giveBirth(List<Animal> newOwls)
    {
        // New foxes are born into adjacent locations.
        // Get a list of adjacent free locations.
        Field field = getField();
        List<Location> free = field.getFreeAdjacentLocations(getLocation());
        int births = breed();
        for(int b = 0; b < births && free.size() > 0; b++) {
            Location loc = free.remove(0);
            Owl young = new Owl(false, field, loc);
            newOwls.add(young);
                                System.out.println("New owls");
        }
    } 
}

