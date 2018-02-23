import java.util.List;
import java.util.Iterator;
import java.util.Random;

/**
 * A simple model of an Oak Tree.
 * 
 * @author Yassine Lutumba and Miona Milenkovic
 * @version 2018.02.22
 */
public class OakTree extends Plant
{
    // The Oak Tree's age.
    private int age;
    /**
     * Create an Oaktree. An Oaktree can be created as a new born (age zero
     * and not hungry) or with a random age and food level.
     * 
     * @param randomAge If true, the Oak Tree will have random age.
     * @param field The field currently occupied.
     * @param location The location within the field.
     */
    public OakTree(boolean randomAge, Field field, Location location)
    {
        super(field, location);
        setMaxAge(500);
        setGrowingProbability(0.005);
        
        if(randomAge) {
            age = rand.nextInt(getMaxAge());
        }
        else {
            age = 0;
        }
    }
    
    /**
     * This is what the Oak Tree does most of the time
     * it will look for new space to spread seeds to or die of old age.
     * @param field The field currently occupied.
     * @param newOak Treees A list to return newly born Oak Treees.
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
            //trees grow better in the rain
            if(isAlive() && weather.equals("Raining")){
                double prob = getGrowingProbability();
                prob = 2 * getGrowingProbability();
            }
        }
    }
    
    /**
     * Check whether or not this Tree is to give birth at this step.
     * New births will be made into free adjacent locations.
     * @param newOakTree A list to return newly born trees.
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
    /**
     * Generate a number representing the number of births,
     * if it can breed.
     */
    private int plantTree()
    {
        int births = 0;
        if(rand.nextDouble() <= getGrowingProbability()) {
            births = 2;
        }
        return births;
    }
     
    /**
     * Increase the age. This could result in the Oak Tree's death.
     */
    private void incrementAge()
    {
        age++;
        if(age > getMaxAge()) {
            setDead();
        }
    }
    
}
