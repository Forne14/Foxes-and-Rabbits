import java.util.List;
import java.util.Iterator;
import java.util.Random;
/**
 * A class representing shared characteristics of animals.
 * 
 * @author David J. Barnes and Michael Kölling
 * @version 2016.02.29 (2)
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
     * 
     * @param field The field currently occupied.
     * @param location The location within the field.
     */
    public Animal(Field field, Location location, boolean gender)
    {
        super(field, location);
        this.gender = gender;
    }
    
    public int getBreedingAge()
    {
        return BREEDING_AGE;
    }
    
    public int getMaxAge()
    {
        return MAX_AGE;
    }
    
    public double getBreedingProbability()
    {
        return BREEDING_PROBABILITY;
    }
    
     public int getMaxLitterSize()
    {
        return MAX_LITTER_SIZE;
    }
    
    public int getAge()
    {
        return age;
    }
    
    public void setAge(int age)
    {
        age = age;
    }
    
    public int getFoodLevel()
    {
        return foodLevel;
    }
    
    public void setFoodLevel(int level)
    {
        foodLevel = level;
    }
    
    public void setBreedingAge(int breeding)
    {
        BREEDING_AGE = age;
    }
    
    public void setMaxAge(int max)
    {
        MAX_AGE = max;
    }
    
    public void setBreedingProbability(double probability)
    {
        BREEDING_PROBABILITY = probability;
    }
    
     public void setMaxLitterSize(int litter)
    {
        MAX_LITTER_SIZE = litter;
    }
    
    protected boolean generateRandomGender()
    {
        return rand.nextBoolean();
    }
    
    protected boolean setGender(boolean female)
    {
         return gender = female;
    }
    
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
     * Make this fox more hungry. This could result in the fox's death.
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
     * A fox can breed if it has reached the breeding age.
     */
    protected boolean canBreed()
    {
        return age >= BREEDING_AGE;
    }
    
   
}
