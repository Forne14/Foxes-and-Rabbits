import java.util.List;
import java.util.Random;
import java.util.Iterator; 
/**
 * A simple model of a squirrel.
 * Squirrels age, move, breed, and die.
 * 
 * @author Yassine Lutumba and Yassine Lutumba
 * @version 2018.02.22
 */
public class Squirrel extends TheHunted
{
    private static final int OAKTREE_FOOD_VALUE = 1000;
    /**
     * Create a new squirrel. A squirrel may be created with age
     * zero (a new born) or with a random age.
     * 
     * @param randomAge If true, the squirrel will have a random age.
     * @param field The field currently occupied.
     * @param location The location within the field.
     */
    public Squirrel(boolean randomAge, Field field, Location location, boolean gender, boolean infected)
    {
        super(field, location, gender, infected);
        setMaxAge(1000);
        setBreedingAge(1); 
        setBreedingProbability(0.9);
        setMaxLitterSize(10);
        if(randomAge) {
            setAge(rand.nextInt(getMaxAge()));
                 foodLevel = rand.nextInt(OAKTREE_FOOD_VALUE);
        }
        else {
            setAge(0);
            foodLevel = OAKTREE_FOOD_VALUE;
        }
    }
    /**
     * lets this Squirrel act. a squirrel will age, grow in hunger,
     * infect other animals or will become more prone to breeding depending on the weather
     * @param newSquirrels the list of new squirrels to be added
     * @param currentTimeOfDay the current time of day
     * @param weather the weather
     */
    public void act(List<Living> newSquirrels, String currentTimeOfDay, String weather)
    {
        incrementAge();
        incrementHunger();
        infect();
        increaseRoast(weather);
        if(isAlive() && currentTimeOfDay.equals("Day Time")) {
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
     * this meathod changes the breeding probability of a squirrel based on the weather
     * @params w the weather which this is based off
     */
    private void increaseRoast(String w)
    {
        if(w.equals("Sunny")){
            double n = getBreedingProbability();
            setBreedingProbability(n * 0.5);
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
            Object plant = field.getObjectAt(where);
            if(plant instanceof OakTree) {
                OakTree tree = (OakTree) plant;
                if(tree.isAlive()) {    
                    foodLevel += OAKTREE_FOOD_VALUE;
                    return where;
                }
            }
        }
        return null;
    }
    /**
     * lets the squirrel give birth
     * squirrel can only give birth if they are of opposite genders
     * @param newSquirrel a list containing newly born owls
     */
    private void giveBirth(List<Living> newSquirrels) 
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
                if (surroundingAnimal != null && surroundingAnimal instanceof Squirrel) {
                    Squirrel mate = (Squirrel) surroundingAnimal;
                    if(this.getGender() != mate.getGender()) {
                        int births = breed();
                        for(int b = 0; b < births && adjacent.size() > 0; b++) {
                            if (free.size() == 0) {
                                break;
                            }
                            Location loc = free.remove(0);
                            Squirrel young = new Squirrel(false, field, loc, setGender(generateRandomGender()), infected);
                            newSquirrels.add(young); 
                        }  
                    } 
                }
            }     
        }
    }
    
}
