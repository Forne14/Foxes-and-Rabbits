import java.util.List;
import java.util.Iterator;
import java.util.Random; 

/**
 * A simple model of a fox.
 * Foxes age, move, eat rabbits, and die.
 * 
 * @author Yassine Lutumba and Miona Milenkovic
 * @version 2018.02.22
 */
public class Fox extends TheHunter

{
    // Characteristics shared by all foxes (class variables).
    
    private static final int RABBIT_FOOD_VALUE = 20;
    // Individual characteristics (instance fields).

    /**
     * Create a fox. A fox can be created as a new born (age zero
     * and not hungry) or with a random age and food level.
     * 
     * @param randomAge If true, the fox will have random age and hunger level.
     * @param field The field currently occupied.
     * @param location The location within the field.
     */
    public Fox(boolean randomAge, Field field, Location location, boolean gender)
    {
        super(field, location, gender);   
        setBreedingAge(6); 
        setMaxAge(150);
        setBreedingProbability(0.3);
        setMaxLitterSize(5);
        if(randomAge) {
            setAge(rand.nextInt(getMaxAge()));
            foodLevel = rand.nextInt(RABBIT_FOOD_VALUE);
        }
        else {
            setAge(0);
            foodLevel = RABBIT_FOOD_VALUE;
        }
        
    }
    
    /**
     * This is what the fox does most of the time: it hunts for
     * rabbits. In the process, it might breed, die of hunger,
     * or die of old age.
     * @param field The field currently occupied.
     * @param newFoxes A list to return newly born foxes.
     */
    public void act(List<Living> newFoxes, String currentTimeOfDay, String weather)
    {
        incrementAge();
        incrementHunger();
        if(!(weather.equals("Foggy"))){
            if(isAlive() && currentTimeOfDay.equals("Night Time")){
                if(isAlive()) {
                    giveBirth(newFoxes);            
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
        }
       
    }

    /**
     * Look for rabbits adjacent to the current location.
     * Only the first live rabbit is eaten.
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
            if(animal != null && animal instanceof Rabbit) {
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
    /**
     * lets the fox give birth
     * fox can only give birth if they are of opposite genders
     * @param newFoxes a list containing newly born owls
     */
    private void giveBirth(List<Living> newFoxes) 
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
                if (surroundingAnimal != null && surroundingAnimal instanceof Fox) {
                    Fox mate = (Fox) surroundingAnimal;
                    if(this.getGender() != mate.getGender()) {
                        int births = breed();
                        for(int b = 0; b < births && adjacent.size() > 0; b++) {
                            if (free.size() == 0) {
                                break;
                            }
                            Location loc = free.remove(0);
                            Fox young = new Fox(false, field, loc, setGender(generateRandomGender()));
                            newFoxes.add(young);
                        }  
                    }
                }
            }     
        }
    }
}
