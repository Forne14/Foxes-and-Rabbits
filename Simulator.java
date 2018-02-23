import java.util.Random;
import java.util.List;
import java.util.ArrayList;
import java.util.Iterator;
import java.awt.Color;

/**
 * A simple predator-prey simulator, based on a rectangular field
 * containing rabbits, foxes, bears, owls and squirrels
 * @author Yassine Lutumba and Miona Milenkovic
 * @version 2018.02.22
 */
public class Simulator 
{
    // Constants representing configuration information for the simulation.
    // The default width for the grid.
    private static final int DEFAULT_WIDTH = 120;
    // The default depth of the grid.
    private static final int DEFAULT_DEPTH = 80;
    // The probability that a fox will be created in any given grid position.
    private static final double FOX_CREATION_PROBABILITY = 0.08;
    // The probability that a rabbit will be created in any given grid position.
    private static final double RABBIT_CREATION_PROBABILITY = 0.4;  
    // The probability that a Squirrel will be created in any given grid position.
    private static final double SQUIRREL_CREATION_PROBABILITY = 0.09;
    // The probability that a Bear will be created in any given grid position.
    private static final double BEAR_CREATION_PROBABILITY = 0.06;
    // The probability that a Owl will be created in any given grid position.
    private static final double OWL_CREATION_PROBABILITY = 0.05;
    // The probability that a OakTree will be created in any given grid position.
    private static final double OAKTREE_CREATION_PROBABILITY = 0.075;
    
    private static final String DAY = "Day Time";
    private static final String NIGHT = "Night Time";
    
    private static String currentTimeOfDay; 
    private static String weather;
    
    // List of animals in the field.
    private List<Living> animals;
    // The current state of the field.
    private Field field;
    // The current step of the simulation.
    private int step;
    // A graphical view of the simulation.
    private SimulatorView view;
    protected static Random rand = new Random();
    
    ArrayList<String> weathersPossible;
    
    /**
     * Construct a simulation field with default size.
     */
    public Simulator()
    {
        this(DEFAULT_DEPTH, DEFAULT_WIDTH);

    }
    
    /**
     * Create a simulation field with the given size.
     * @param depth Depth of the field. Must be greater than zero.
     * @param width Width of the field. Must be greater than zero.
     */
    public Simulator(int depth, int width)
    {
        if(width <= 0 || depth <= 0) {
            System.out.println("The dimensions must be greater than zero.");
            System.out.println("Using default values.");
            depth = DEFAULT_DEPTH;
            width = DEFAULT_WIDTH;
        }
        
        animals = new ArrayList<>(); //create a new array list for animals
        field = new Field(depth, width); //create a field
        weathersPossible = new ArrayList<>(); //create a new array list for the types of weather possible
        view = new SimulatorView(depth, width); //create a new simulator view

        // Create a view of the state of each location in the field.
        createViews();
        // Setup a valid starting point.
        reset();
        fillWeatherList(); //fills array list with weather
    }
    /***
     * this meathod creates the views
     */
    public void createViews()
    {
        //self explanatory code
        view.setColor(Rabbit.class, Color.BLUE);
        view.setColor(Fox.class, Color.ORANGE);
        view.setColor(Bear.class, Color.BLACK);
        view.setColor(Owl.class, Color.GRAY);
        view.setColor(Squirrel.class, Color.RED);
        view.setColor(OakTree.class, Color.GREEN);
    }
    
    /***
     * this meathod generates a random weather from the list 
     * and returns it as a String w
     */
    public String generateRandomWeather()
    {
         int weatherIndex = (rand.nextInt(weathersPossible.size())); //get a random number from 0 up to the max size of the index
         String w = weathersPossible.get(weatherIndex); //return a String w from the list
         return w;
    }
        
    /***
     *  this meathod sets the weather
     *  @params w the weather to be added
     */
    public String setWeatherAs(String w)
    {
        return weather = w;
    }
    
    /***
     * fills weathersPossible list with weathers
     */
    public void fillWeatherList()
    {
        //self explanatory code
        weathersPossible.add("Rainy");
        weathersPossible.add("Foggy");
        weathersPossible.add("Windy");
        weathersPossible.add("Sunny");
        
    }
    
    /**
     * Run the simulation from its current state for a reasonably long period,
     * (4000 steps).
     */
    public void runLongSimulation()
    {
        simulate(4000);
    }
    
    /**
     * Run the simulation from its current state for the given number of steps.
     * Stop before the given number of steps if it ceases to be viable.
     * @param numSteps The number of steps to run for.
     */
    public void simulate(int numSteps)
    {
        for(int step = 1; step <= numSteps && view.isViable(field); step++) {
            simulateOneStep();
            //delay(60);   // uncomment this to run more slowly
        }
    }
    /**
     * this meathod changes the weather field
     */
    private void simulateChangeInWeather()
    {
        int n= rand.nextInt(60); // let n be a random number from 0 to 60
        int m = rand.nextInt(60); // let m be a random number from 0 to 60
        if(!((n == 0)||(m == 0))) //n or m cannot be 0
        {
            if(step % n < m){
                setWeatherAs(generateRandomWeather()); //change the weather
            }
        }
        else
        {
            setWeatherAs(generateRandomWeather());
        }
    }
    
    /**
     * this meathod cycles between day and night every 12 steps
     */
    private void simulateDayCycle()
    {
        if (step % 24 < 12) {
            currentTimeOfDay = DAY;
        }
        else { 
            currentTimeOfDay = NIGHT;
        }
    }
    
    /**
     * Run the simulation from its current state for a single step.
     * Iterate over the whole field updating the state of each
     * fox and rabbit.
     */
    public void simulateOneStep()
    {
        step++; //increment step
        simulateDayCycle(); //day cycle
        simulateChangeInWeather(); // change weather
        // Provide space for newborn animals.
        List<Living> newAnimals = new ArrayList<>();        
        // Let all rabbits act.
        for(Iterator<Living> it = animals.iterator(); it.hasNext(); ) {
            Living animal = it.next(); 
            if(! animal.isAlive()) {
                it.remove();
            }
            else{
                animal.act(newAnimals, currentTimeOfDay, weather);
            }
        }
               
        // Add the newly born foxes and rabbits to the main lists.
        animals.addAll(newAnimals);
        view.showStatus(step, field, currentTimeOfDay, weather);
    }
        
    /**
     * Reset the simulation to a starting position.
     */
    public void reset()
    {
        //self explanatory code
        step = 0;
        animals.clear();
        populate();
        // Show the starting state in the view.
        view.showStatus(step, field, currentTimeOfDay, weather); 
    }
    
    /**
     * Randomly populate the field with foxes and rabbits.
     */
    private void populate()
    {
        Random rand = Randomizer.getRandom();
        field.clear();
        for(int row = 0; row < field.getDepth(); row++) {
            for(int col = 0; col < field.getWidth(); col++) {
                if(rand.nextDouble() <= FOX_CREATION_PROBABILITY) {
                    Location location = new Location(row, col);
                    Fox fox = new Fox(true, field, location, rand.nextBoolean());
                    animals.add(fox);
                }
                else if(rand.nextDouble() <= RABBIT_CREATION_PROBABILITY) {
                   Location location = new Location(row, col);
                   Rabbit rabbit = new Rabbit(true, field, location, rand.nextBoolean(), rand.nextBoolean());
                   animals.add(rabbit);
                }
                else if(rand.nextDouble()<= SQUIRREL_CREATION_PROBABILITY)
                {
                    Location location = new Location(row, col);
                    Squirrel squirrel = new Squirrel(true, field, location, rand.nextBoolean(), rand.nextBoolean());
                    animals.add(squirrel);
                }
                else if(rand.nextDouble()<= OWL_CREATION_PROBABILITY)
                {
                    Location location = new Location(row, col);
                    Owl owl= new Owl(true, field, location, rand.nextBoolean());
                    animals.add(owl);
                }
                else if(rand.nextDouble()<= BEAR_CREATION_PROBABILITY)
                {
                    Location location = new Location(row, col);
                    Bear bear= new Bear(true, field, location, rand.nextBoolean());
                    animals.add(bear);
                }
                else if(rand.nextDouble()<= OAKTREE_CREATION_PROBABILITY)
                {
                    Location location = new Location(row, col);
                    OakTree oakTree= new OakTree(true, field, location);
                    animals.add(oakTree);
                }
            }
        }
    }
    
    /**
     * Pause for a given time.
     * @param millisec  The time to pause for, in milliseconds
     */
    private void delay(int millisec)
    {
        try {
            Thread.sleep(millisec);
        }
        catch (InterruptedException ie) {
            // wake up
        }
    }
    
    /**
     * this meathod returns the current number of steps elapsed
     */
    public int getStep()
    {
        return step; 
    }
}
