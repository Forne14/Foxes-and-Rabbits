import java.util.List;
import java.util.Iterator;
import java.util.Random;
/**
 * this class is a model of plants. it is a subclass of living
 *
 * @author Yassine Lutumba and Miona Milenkovic
 * @version 2018.02.22
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
    /**
     * @return the maximum age of this plant
     */
    public int getMaxAge()
    {
        return MAX_AGE;
    }
    /**
     * @return the growing probability of this plant
     */
    public double getGrowingProbability()
    {
        return GROWING_PROBABILITY;
    }
    /***
     *  sets the maximum age of this plant
     *  @param age the age to be set
     */
     public void setMaxAge(int age)
    {
        MAX_AGE = age;
    }
    /**
     * sets the growing probability of this plant
     * @param prob the probability to be set to
     */
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
