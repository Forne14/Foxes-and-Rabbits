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
    private static final int OAKTREE_FOOD_VALUE = 20;
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
        setMaxAge(80);
        setBreedingProbability(0.04);
        setMaxLitterSize(6); 
        if(randomAge) {
            setAge(rand.nextInt(getMaxAge()));
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
                            Rabbit young = new Rabbit(false, field, loc, setGender(generateRandomGender()), infected);
                            newRabbits.add(young); 
                            System.out.println("Rabbit has given birth");
                        }  
                    }
                }
            }     
        }
    }
    
    public void act(List<Living> newRabbits, String currentTimeOfDay, String weather)
    {
        incrementAge();
        incrementHunger(); 
        infect();
        if(isAlive() && currentTimeOfDay.equals("Day Time")) {
            if(isAlive()){
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
        if(isAlive() && currentTimeOfDay.equals("Night Time")) {
                 System.out.println("all the rabbits are sleeping shhhhh");
            
            }
            
    }
    
    private void infect()
    {
        Field field = getField();
        if(field == null){
            return;
        }
        else{
            List<Location> adjacent = field.adjacentLocations(getLocation());
            Iterator<Location> it = adjacent.iterator();
            while(it.hasNext()) {
               Location where = it.next();
               Object theHunted = field.getObjectAt(where);
               if(theHunted != null && theHunted instanceof TheHunted){
                    TheHunted poorSoul = (TheHunted) theHunted;
                    if(poorSoul.isAlive() && poorSoul.getInfected()==false){
                         poorSoul.setInfected(true);    
                    }
               }
            }
            System.out.println("animal has infected another animal");
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
                    System.out.println("Rabbit has eaten");
                    return where;
                }
            }
        }
        return null;
    }
}
