import java.util.List;
import java.util.Iterator;
import java.util.Random;
/**
 * this is a subclass of animal and is a superclass for all predatory animals
 *
 * @Author Yassine Lutumba and Miona Milenkovic
 * @2018.02.22
 */
public abstract class TheHunter extends Animal
{

    /**
     * Constructor for objects of class TheHunter
     */
    public TheHunter(Field field, Location location, boolean gender)
    {
        super(field, location, gender);
    }
    
    
}
