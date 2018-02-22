import java.util.List;
import java.util.Iterator;
import java.util.Random;
/**
 * A class representing shared characteristics of animals.
 * animals have a gender and constants for their maximum age, breeding age, litter size and breeding probability
 * 
 * @author Yassine Lutumba and Miona Milenkovic
 * @version 2018.02.22
 */
public abstract class Animal extends Living
{
    //protected static final double GENDER_PROBABILITY = 0.5;
    protected boolean gender;
    
    // instance variables - replace the example below with your own
    // The age at which a fox can start to breed.
    private static  int BREEDING_AGE;
    // The age to which a fox can live.
    private static  int MAX_AGE;
    // The likelihood of a fox breeding.
    private static  double BREEDING_PROBABILITY;
    // The maximum number of births.
    private static int MAX_LITTER_SIZE;
    // The food value of a single rabbit. In effect, this is the
    // number of steps a fox can go before it has to eat again.
    // A shared random number generator to control breeding.
    protected static final Random rand = Randomizer.getRandom();
    // The fox's age.
    private int age;
    // The fox's food level, which is increased by eating rabbits.
    protected int foodLevel;
    
    /**
     * Create a new animal at location in field.
     * subclass of living
     * superclass of all animals
     * 
     * @param field The field currently occupied.
     * @param location The location within the field.
     */
    public Animal(Field field, Location location, boolean gender)
    {
        super(field, location);
        this.gender = gender;
    }
    
    /***
     * @return the breeding age of this animal
     */
    public int getBreedingAge()
    {
        return BREEDING_AGE;
    }
    /**
     * @return the maximum age of this animal
     */
    public int getMaxAge()
    {
        return MAX_AGE;
    }
    /***
     * @return the breeding probability of this animal
     */
    public double getBreedingProbability()
    {
        return BREEDING_PROBABILITY;
    }
    /**
     * @return the maximum litter size of this object
     */
     public int getMaxLitterSize()
    {
        return MAX_LITTER_SIZE;
    }
    /**
     * @return the age of this animal
     */
    public int getAge()
    {
        return age;
    }
    /**
     * sets the age of this animal to a given int
     * @param a the age to be set
     */
    public void setAge(int a)
    {
        age = a;
    }
    
    /**
     * @return the foodlevel of this animal
     */
    public int getFoodLevel()
    {
        return foodLevel;
    }
    /**
     * sets the food level of this animal to the given int level
     * @param level the food level to be set
     */
    public void setFoodLevel(int level)
    {
        foodLevel = level;
    }
    /**
     * sets the breeding age of the animal to whatever is passed through
     * @param breeding the breeding age to be set
     */
    public void setBreedingAge(int breeding)
    {
        BREEDING_AGE = age;
    }
    
    /**
     * sets the maximum age to the given parameter
     * @param max the max age to be set
     */
    public void setMaxAge(int max)
    {
        MAX_AGE = max;
    }
    
    /**
     * sets the breeding probability of the animal
     * @param probability the probability to be set
     */
    public void setBreedingProbability(double probability)
    {
        BREEDING_PROBABILITY = probability;
    }
    
    /***
     * sets the max litter size 
     * @param litter the maximum litter 
     */
     public void setMaxLitterSize(int litter)
    {
        MAX_LITTER_SIZE = litter;
    }
    
    /**
     * generates a random boolean
     */
    protected boolean generateRandomGender()
    {
        return rand.nextBoolean();
    }
    
    /**
     * sets gender to the given parameter
     * @param female the gender to be given. true = female, false = male
     */
    protected boolean setGender(boolean female)
    {
         return gender = female;
    }
    
    /**
     * @return the gender of the animal
     */
    protected boolean getGender()
    {
        return gender;
    }
    
    /**
     * Increase the age. This could result in the fox's death.
     */
    protected void incrementAge()
    {
        age++;
        if(age > MAX_AGE) {
            setDead();
        }
    }
    
    /**
     * Make this animal more hungry. This could result in the animal's death.
     */
    protected void incrementHunger()
    {
        foodLevel--;
        if(foodLevel <= 0) {
            setDead();
        }
    }

    /**
     * Generate a number representing the number of births,
     * if it can breed.
     * @return The number of births (may be zero).
     */
    protected int breed()
    {
        int births = 0;
        if(canBreed()  && rand.nextDouble() <= BREEDING_PROBABILITY) { 
            births = rand.nextInt(MAX_LITTER_SIZE) + 1;
        }
        return births;
    }

    /**
     * An animal can breed if it has reached the breeding age.
     */
    protected boolean canBreed()
    {
        return age >= BREEDING_AGE;
    }
    
   
}
