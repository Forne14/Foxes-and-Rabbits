import java.util.List;
import java.util.Random;
import java.util.Iterator;
/**
 * A simple model of a rabbit.
 * Rabbits age, move, breed, and die.
 * 
 * @author David J. Barnes and Michael Kölling
 * @version 2016.02.29 (2)
 */
public class Rabbit extends TheHunted

{

    /**
     * Create a new rabbit. A rabbit may be created with age
     * zero (a new born) or with a random age.
     * 
     * @param randomAge If true, the rabbit will have a random age.
     * @param field The field currently occupied.
     * @param location The location within the field.
     */
    public Rabbit(boolean randomAge, Field field, Location location, boolean gender)
    {
        super(field, location, gender);
        BREEDING_AGE = 5;
        MAX_AGE = 80;
        BREEDING_PROBABILITY = 0.04;
        MAX_LITTER_SIZE = 6;
        if(randomAge) {
            age = rand.nextInt(MAX_AGE);
        }
    }
    
    /**
     * This is what the rabbit does most of the time - it runs 
     * around. Sometimes it will breed or die of old age.
     * @param newRabbits A list to return newly born rabbits.
     */
    public void act(List<Living> newRabbits, String currentTimeOfDay, String weather)
    {
        incrementAge();
        if(isAlive() && currentTimeOfDay.equals("Day Time")) {
            giveBirth(newRabbits);            
            // Try to move into a free location.
            Location newLocation = getField().freeAdjacentLocation(getLocation());
            if(newLocation != null) {
                setLocation(newLocation);
            }
            else {
                // Overcrowding.
                setDead();
            }
        }
        if(isAlive() && currentTimeOfDay.equals("Night Time")) {
            System.out.println("all the rabbits are sleeping shhhhh");
            
        }
    }  
    
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
                            Rabbit young = new Rabbit(false, field, loc, setGender(generateRandomGender()));
                            newRabbits.add(young); 
                            System.out.println("Rabbit has given birth");
                        }  
                    }
                }
            }     
        }
    }

}