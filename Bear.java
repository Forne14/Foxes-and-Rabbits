import java.util.List;
import java.util.Iterator;
import java.util.Random;
/**
 * Write a description of class Bear here.
 *
 * @author (your name)
 * @version (a version number or a date)
 */
public class Bear extends Animal
{
    // Characteristics shared by all bear (class variables).
    
    // The age at which a bear can start to breed.
    private static final int BREEDING_AGE = 60;
    // The age to which a fox can live.
    private static final int MAX_AGE = 600;
    // The likelihood of a bear breeding.
    private static final double BREEDING_PROBABILITY = 0.04;
    // The maximum number of births.
    private static final int MAX_LITTER_SIZE = 4;
    // The food value of a single rabbit. In effect, this is the
    // number of steps a bear can go before it has to eat again.
    private static final int RABBIT_FOOD_VALUE = 9;
    // The food value of a single fox. In effect, this is the
    // number of steps a bear can go before it has to eat again.
    private static final int FOX_FOOD_VALUE = 15;
    // A shared random number generator to control breeding.
    private static final Random rand = Randomizer.getRandom();
    
    // Individual characteristics (instance fields).
    // The bear age.
    private int age;
    // The bear's food level, which is increased by eating everything.
    private int foodLevel;

    /**
     * Constructor for objects of class Bear
     */
    public Bear(boolean randomAge, Field field, Location location)
    {
        super(field,location);
        if(randomAge) {
            age = rand.nextInt(MAX_AGE);
            foodLevel = rand.nextInt(RABBIT_FOOD_VALUE+FOX_FOOD_VALUE) ;
        }
        else {
            age = 0;
            foodLevel = RABBIT_FOOD_VALUE+FOX_FOOD_VALUE;
        }
        
    }
    
    public void act(List<Animal> newBears)
    {
        incrementAge();
        incrementHunger();
        if(isAlive()) {
            giveBirth(newBears);            
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
     * Look for Foxes or Rabbits adjacent to the current location.
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
            if(animal instanceof Rabbit || animal instanceof Fox) {
                Rabbit rabbit = (Rabbit) animal;
                Fox fox = (Fox) animal;
                if(rabbit.isAlive() || fox.isAlive()) { 
                    rabbit.setDead();
                    fox.setDead();
                    foodLevel = RABBIT_FOOD_VALUE+FOX_FOOD_VALUE;
                    return where;
                }
            }
        }
        return null;
    }
    
    /**
     * Increase the age. This could result in the bear's death.
     */
    private void incrementAge()
    {
        age++;
        if(age > MAX_AGE) {
            setDead();
        }
    }
    
    /**
     * Make this fox more hungry. This could result in the bear's death.
     */
    private void incrementHunger()
    {
        foodLevel--;
        if(foodLevel <= 0) {
            setDead();
        }
    }
    
    /**
     * Check whether or not this fox is to give birth at this step.
     * New births will be made into free adjacent locations.
     * @param newFoxes A list to return newly born foxes.
     */
    private void giveBirth(List<Animal> newBears)
    {
        // New foxes are born into adjacent locations.
        // Get a list of adjacent free locations.
        Field field = getField();
        List<Location> free = field.getFreeAdjacentLocations(getLocation());
        int births = breed();
        for(int b = 0; b < births && free.size() > 0; b++) {
            Location loc = free.remove(0);
            Fox young = new Fox(false, field, loc);
            newBears.add(young);
        }
    }
        
    /**
     * Generate a number representing the number of births,
     * if it can breed.
     * @return The number of births (may be zero).
     */
    private int breed()
    {
        int births = 0;
        if(canBreed() && rand.nextDouble() <= BREEDING_PROBABILITY) {
            births = rand.nextInt(MAX_LITTER_SIZE) + 1;
        }
        return births;
    }

    /**
     * A fox can breed if it has reached the breeding age.
     */
    private boolean canBreed()
    {
        return age >= BREEDING_AGE;
    }
}
