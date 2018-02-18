import java.util.List;
import java.util.Iterator;
import java.util.Random;

/**
 * A simple model of a fox.
 * Foxes age, move, eat rabbits, and die.
 * 
 * @author David J. Barnes and Michael Kölling
 * @version 2016.02.29 (2)
 */
public class OakTree extends Plant
{
    // Characteristics shared by all foxes (class variables).
    

    // The fox's age.
    private int age;

    /**
     * Create a fox. A fox can be created as a new born (age zero
     * and not hungry) or with a random age and food level.
     * 
     * @param randomAge If true, the fox will have random age and hunger level.
     * @param field The field currently occupied.
     * @param location The location within the field.
     */
    public OakTree(boolean randomAge, Field field, Location location)
    {
        super(field, location);
        MAX_AGE = 500;
        GROWING_PROBABILITY = 0.001; 
        if(randomAge) {
            age = rand.nextInt(MAX_AGE);
        }
        else {
            age = 0;
        }
    }
    
    /**
     * This is what the fox does most of the time: it hunts for
     * rabbits. In the process, it might breed, die of hunger,
     * or die of old age.
     * @param field The field currently occupied.
     * @param newFoxes A list to return newly born foxes.
     */
    public void act(List<Living> newOakTrees, String currentTimeOfDay, String weather)
    {
        incrementAge();
        if(isAlive()) {
            treeGrow(newOakTrees);       
            Location newLocation = getField().freeAdjacentLocation(getLocation());
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
            if(isAlive() && weather.equals("Raining")){
                GROWING_PROBABILITY = 2 * GROWING_PROBABILITY;
            }
        }
    }
    
    /**
     * Check whether or not this rabbit is to give birth at this step.
     * New births will be made into free adjacent locations.
     * @param newRabbits A list to return newly born rabbits.
     */
    private void treeGrow(List<Living> newOakTree)
    {
        // New rabbits are born into adjacent locations.
        // Get a list of adjacent free locations.
        Field field = getField();
        List<Location> free = field.getFreeAdjacentLocations(getLocation());
        int sappling = plantTree();
        for(int b = 0; b < sappling && free.size() > 0; b++) {
            Location loc = free.remove(0);
            OakTree young = new OakTree(false, field, loc);
            newOakTree.add(young);
        }
    }
    
    private int plantTree()
    {
        int births = 0;
        if(rand.nextDouble() <= GROWING_PROBABILITY) {
            births = 1;
        }
        return births;
    }
     
    /**
     * Increase the age. This could result in the fox's death.
     */
    private void incrementAge()
    {
        age++;
        if(age > MAX_AGE) {
            setDead();
        }
    }
    
}
