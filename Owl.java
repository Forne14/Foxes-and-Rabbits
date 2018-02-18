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
    private static final int SQUIRREL_FOOD_VALUE = 20;

    /**
     * Constructor for objects of class owl
     */
    public Owl(boolean randomAge, Field field, Location location, boolean gender)
    {
        super(field,location, gender);
        BREEDING_AGE =7;
        MAX_AGE = 235;
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
    
    public void act(List<Living> newOwls, String currentTimeOfDay, String weather)
    {
        incrementAge();
        incrementHunger();
        if(isAlive() && currentTimeOfDay.equals("Night Time")) {
               
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
        
        if(isAlive() && currentTimeOfDay.equals("Day Time")) {
            System.out.println("all the Owls are sleeping shhhhh");
            giveBirth(newOwls);
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
                                        System.out.println("owl has eaten a squirrel");
                    return where;
                }
            }
        }
        return null; 
    }
    
    private void giveBirth(List<Living> newOwls) 
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
                if (surroundingAnimal != null && surroundingAnimal instanceof Owl) {
                    Owl mate = (Owl) surroundingAnimal;
                    if(this.getGender() != mate.getGender()) {
                        int births = breed();
                        for(int b = 0; b < births && adjacent.size() > 0; b++) {
                            if (free.size() == 0) {
                                break;
                            }
                            Location loc = free.remove(0);
                            Owl young = new Owl(false, field, loc, setGender(generateRandomGender()));
                            newOwls.add(young); 
                            System.out.println("Owl has given birth");
                        }  
                    }
                }
            }     
        }
    }
}

