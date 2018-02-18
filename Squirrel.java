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
    private static final int OAKTREE_FOOD_VALUE = 20;
    /**
     * Create a new squirrel. A squirrel may be created with age
     * zero (a new born) or with a random age.
     * 
     * @param randomAge If true, the squirrel will have a random age.
     * @param field The field currently occupied.
     * @param location The location within the field.
     */
    public Squirrel(boolean randomAge, Field field, Location location, boolean gender)
    {
        super(field, location, gender);
        MAX_AGE = 120;
        BREEDING_AGE = 12;
        BREEDING_PROBABILITY = 0.4;
        MAX_LITTER_SIZE = 6;
        if(randomAge) {
            age = rand.nextInt(MAX_AGE);
        }
    }
    
    public void act(List<Living> newSquirrels, String currentTimeOfDay, String weather)
    {
        incrementAge();
        incrementHunger();
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
        if(isAlive() && currentTimeOfDay.equals("Night Time")) {
                 System.out.println("all the squirrels are sleeping shhhhh");
            
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
                    System.out.println("Squirrel has eaten");
                    return where;
                }
            }
        }
        return null;
    }
    
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
                            Squirrel young = new Squirrel(false, field, loc, setGender(generateRandomGender()));
                            newSquirrels.add(young); 
                            System.out.println("Squirrel has given birth");
                        }  
                    } 
                }
            }     
        }
    }
    
}