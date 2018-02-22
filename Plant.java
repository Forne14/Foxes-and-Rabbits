import java.util.List;
import java.util.Iterator;
import java.util.Random;
/**
 * Write a description of class Plant here.
 *
 * @author (your name)
 * @version (a version number or a date)
 */
public abstract class Plant extends Living
{
    // The age to which a fox can live
    private static int MAX_AGE;
    // A shared random number generator to control breeding.
    protected static Random rand = Randomizer.getRandom();
    //the likelihood of a tree growing in an unocupied field.
    private static double GROWING_PROBABILITY;

    /**
     * Constructor for objects of class Plant
     */
    public Plant(Field field, Location location)
    
    {
        super(field, location);
    }
    
    public int getMaxAge()
    {
        return MAX_AGE;
    }
    
    public double getGrowingProbability()
    {
        return GROWING_PROBABILITY;
    }
    
     public void setMaxAge(int age)
    {
        MAX_AGE = age;
    }
    
    public void setGrowingProbability(double prob)
    {
        GROWING_PROBABILITY = prob;
    }
    
        /**
     * Return the animal's location.
     * @return The animal's location.
     */
    protected Location getLocation()
    {
        return location;
    }
    
    /**
     * Return the animal's field.
     * @return The animal's field.
     */
    protected Field getField()
    {
        return field;
    }
    
    
}
