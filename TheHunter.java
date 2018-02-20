import java.util.List;
import java.util.Iterator;
import java.util.Random;
/**
 * Write a description of class Predator here.
 *
 * @author (your name)
 * @version (a version number or a date)
 */
public abstract class TheHunter extends Animal
{

    /**
     * Constructor for objects of class Predator
     */
    public TheHunter(Field field, Location location, boolean gender, boolean infected)
    {
        super(field, location, gender, infected);
    }
    
    
}
