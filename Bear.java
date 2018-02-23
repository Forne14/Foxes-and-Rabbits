import java.util.List;
import java.util.Iterator;
import java.util.Random;
/**
 * this class represents a model of a bear.
 * a bear is a predatory animal which is born, eats, gives birth and dies
 * it is a subclass of animal, theHunter, and Living
 *
 * @author Yassine Lutumba and Miona Milenkovic
 * @version 2018.02.22
 */
public class Bear extends TheHunter
{
    // The food value of a single fox. In effect, this is the
    // number of steps a bear can go before it has to eat again.
    private static final int RABBIT_FOOD_VALUE = 100;

    /**
     * Constructor for objects of class Bear
     */
    public Bear(boolean randomAge, Field field, Location location, boolean gender)
    {
        super(field,location, gender);
        setBreedingAge(5); 
        setMaxAge(1000);
        setBreedingProbability(0.09);
        setMaxLitterSize(7);
        if(randomAge) {
            setAge(rand.nextInt(getMaxAge()));
            foodLevel = rand.nextInt(RABBIT_FOOD_VALUE) ;
        }
        else {
            setAge(0);
            foodLevel = RABBIT_FOOD_VALUE;
        }
        
    }
    /**
     * lets a bear act
     * it will age, get hungry and give birth in the day time
     * a bear will not act in the rain
     * @param newBears the list of newBears to be added
     * @param currentTimeOfDay the current time of the day
     * @weather the current weather being simulated
     */
    public void act(List<Living> newBears, String currentTimeOfDay, String weather)
    {
        incrementAge();
        incrementHunger();
        if(isAlive() && currentTimeOfDay.equals("Day Time") && !(weather.equals("Rainy"))) {
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
                        }   
                    }
                }
            }     
        }
    }  
}
