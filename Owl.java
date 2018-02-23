import java.util.List;
import java.util.Iterator;
import java.util.Random; 
/**
 * This class is a model representation of an Owl. Owls are nocturnal predators which eat squirrels or rabbits
 * owls are born, age, give birth, eat and die.
 *
 * @Author Yassine Lutumba and Miona Milenkovic
 * @version 2018.02.22
 */
public class Owl extends TheHunter 
{
    // Characteristics shared by all owl (class variables).
    
    // number of steps an owl can go before it has to eat again.
    private static final int SQUIRREL_FOOD_VALUE = 50;

    /**
     * Constructor for objects of class owl
     */
    public Owl(boolean randomAge, Field field, Location location, boolean gender)
    {
        super(field,location, gender);
        setBreedingAge(2); 
        setMaxAge(1000);
        setBreedingProbability(0.08);
        setMaxLitterSize(5);
        if(randomAge) {
            setAge(rand.nextInt(getMaxAge()));
            foodLevel = rand.nextInt(SQUIRREL_FOOD_VALUE) ;
        }
        else {
            setAge(0);
            foodLevel = SQUIRREL_FOOD_VALUE;
        }
    }
    
    /**
     * this owl acts. its age increases
     * its hunger increases
     * if the weather is windy it will not do anything.
     * if its night time it will act.
     */
    public void act(List<Living> newOwls, String currentTimeOfDay, String weather)
    {
        incrementAge();
        incrementHunger();
        if(isAlive() && currentTimeOfDay.equals("Night Time") && !(weather.equals("Windy"))) {
               giveBirth(newOwls);            
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
     * Look for squirrels adjacent to the current location.
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
                    return where;
                }
            }
        }
        return null; 
    }
    
    /**
     * lets the owl give birth
     * owls can only give birth if they are of opposite genders
     * @param newOwls a list containing newly born owls
     */
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
                            System.out.println("Owl Birth");
                        }  
                    }
                }
            }     
        }
    }
    
    
}

