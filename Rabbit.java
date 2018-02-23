import java.util.List;
import java.util.Random;
import java.util.Iterator;
/**
 * A simple model of a rabbit.
 * Rabbits age, move, breed, and die.
 * 
 * @author Yassine Lutumba and Miona Milenkovic
 * @version 2018.02.22
 */
public class Rabbit extends TheHunted
 
{
    private static final int OAKTREE_FOOD_VALUE = 1000;
    protected boolean infected;
    /**
     * Create a new rabbit. A rabbit may be created with age
     * zero (a new born) or with a random age.
     * 
     * @param randomAge If true, the rabbit will have a random age.
     * @param field The field currently occupied.
     * @param location The location within the field.
     */
    public Rabbit(boolean randomAge, Field field, Location location, boolean gender, boolean infected)
    {
        super(field, location, gender, infected);
        setBreedingAge(5); 
        setMaxAge(1000);
        setBreedingProbability(0.07);
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
     * Create a new rabbit if the genders of the two mating rabbits is opposite. 
     * A rabbit is created with age zero (a new born) 
     * 
     * @param newRabbits New rabbits of age 0.
     */
    private void giveBirth(List<Living> newRabbits) 
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
                if (surroundingAnimal != null && surroundingAnimal instanceof Rabbit) {
                    Rabbit mate = (Rabbit) surroundingAnimal;
                    if(this.getGender() != mate.getGender()) {
                        int births = breed();
                        for(int b = 0; b < births && adjacent.size() > 0; b++) {
                            if (free.size() == 0) {
                                break;
                            }
                            Location loc = free.remove(0);
                            Rabbit young = new Rabbit(false, field, loc, setGender(generateRandomGender()), infected);
                            newRabbits.add(young); 
                        }  
                    }
                }
            }     
        }
    }
    
    /**
     * This is what the rabbit does most of the time: it eats oaktrees. 
     * In the process, it might breed, die of hunger, or die of old age.
     * @param field The field currently occupied.
     * @param newRabbits A list to return newly born foxes.
     * @param currentTimeOfDay The current time of the day
     * @param weather The current weather conditions
     */
    public void act(List<Living> newRabbits, String currentTimeOfDay, String weather)
    {
        incrementAge();
        incrementHunger(); 
        infect();
        if(isAlive() && currentTimeOfDay.equals("Day Time")) {
            
                giveBirth(newRabbits);            
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
     * Look for OakTrees adjacent to the current location.
     * Only the first tree is eaten.
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
}
