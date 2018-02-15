import java.util.List;
import java.util.Iterator;
import java.util.Random;
/**
 * Write a description of class Bear here.
 *
 * @author (your name)
 * @version (a version number or a date)
 */
public class Bear extends TheHunter
{
    // The food value of a single fox. In effect, this is the
    // number of steps a bear can go before it has to eat again.
    private static final int RABBIT_FOOD_VALUE = 15;

    /**
     * Constructor for objects of class Bear
     */
    public Bear(boolean randomAge, Field field, Location location, boolean gender)
    {
        super(field,location, gender);
        BREEDING_AGE = 40;
        MAX_AGE = 600;
        BREEDING_PROBABILITY = 0.08;
        MAX_LITTER_SIZE = 4;
        if(randomAge) {
            age = rand.nextInt(MAX_AGE);
            foodLevel = rand.nextInt(RABBIT_FOOD_VALUE) ;
        }
        else {
            age = 0;
            foodLevel = RABBIT_FOOD_VALUE;
        }
        
    }
    
    public void act(List<Living> newBears)
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
                //setDead();
                System.out.println("no space");
            }
        } 
    }
    
    /**
     * Look for Beares or Rabbits adjacent to the current location.
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
            if(animal instanceof Rabbit) {
                Rabbit rabbit = (Rabbit) animal;
                if(rabbit.isAlive()) { 
                    rabbit.setDead();
                    foodLevel += RABBIT_FOOD_VALUE;
                    System.out.println("Bear has eaten a rabbit");
                    return where;
                }
            }
        }
        return null;
    }
    
    /**
     * Check whether or not this fox is to give birth at this step.
     * New births will be made into free adjacent locations.
     * @param newBears A list to return newly born foxes.
     */
    private void giveBirth(List<Living> newBears) 
    {
        Field field = getField();
        List<Location> adjacent = field.adjacentLocations(getLocation());
        List<Location> free = field.getFreeAdjacentLocations(getLocation());
        Iterator<Location> it = adjacent.iterator();
        while(it.hasNext()) {
            Location where = it.next();
            if(where != null) 
            {
                Object surroundingAnimal = field.getObjectAt(where);
                if (surroundingAnimal != null && surroundingAnimal instanceof Bear) {
                    Bear mate = (Bear) surroundingAnimal;
                    if(this.getGender() != mate.getGender()) {
                        int births = breed();
                        for(int b = 0; b < births && adjacent.size() > 0; b++) {
                            if (free.size() == 0) {
                                break;
                            }
                            Location loc = free.remove(0);
                            Bear young = new Bear(false, field, loc, setGender(generateRandomGender()));
                            newBears.add(young); 
                            System.out.println("Bear has given birth");
                        }   
                    }
                }
            }     
        }
    }
}
