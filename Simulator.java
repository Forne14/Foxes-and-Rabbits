import java.util.Random;
import java.util.List;
import java.util.ArrayList;
import java.util.Iterator;
import java.awt.Color;

/**
 * A simple predator-prey simulator, based on a rectangular field
 * containing rabbits and foxes.
 * 
 * @author David J. Barnes and Michael Kölling
 * @version 2016.02.29 (2)
 */
public class Simulator 
{
    // Constants representing configuration information for the simulation.
    // The default width for the grid.
    private static final int DEFAULT_WIDTH = 120;
    // The default depth of the grid.
    private static final int DEFAULT_DEPTH = 80;
    // The probability that a fox will be created in any given grid position.
    private static final double FOX_CREATION_PROBABILITY = 0.04;
    // The probability that a rabbit will be created in any given grid position.
    private static final double RABBIT_CREATION_PROBABILITY = 0.05;  
    private static final double SQUIRREL_CREATION_PROBABILITY = 0.08;
    private static final double BEAR_CREATION_PROBABILITY = 0.04;
    private static final double OWL_CREATION_PROBABILITY = 0.06;
    private static final double OAKTREE_CREATION_PROBABILITY = 0.04;
    private static final String DAY = "Day Time";
    private static final String NIGHT = "Night Time";
    private static final String RAINING = "It is raining";
    private static final String SUNNY = "It is sunny";
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
        
        animals = new ArrayList<>();
        field = new Field(depth, width);

        // Create a view of the state of each location in the field.
        view = new SimulatorView(depth, width);
        view.setColor(Rabbit.class, Color.BLUE);
        view.setColor(Fox.class, Color.ORANGE);
        view.setColor(Bear.class, Color.BLACK);
        view.setColor(Owl.class, Color.GRAY);
        view.setColor(Squirrel.class, Color.RED);
        view.setColor(OakTree.class, Color.GREEN);
        // Setup a valid starting point.
        reset();
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
             delay(100);   // uncomment this to run more slowly
        }
    }
    
    /**
     * Run the simulation from its current state for a single step.
     * Iterate over the whole field updating the state of each
     * fox and rabbit.
     */
    public void simulateOneStep()
    {
        step++;
        if (step % 24 < 12) {
            currentTimeOfDay = DAY;
        }
        else { 
            currentTimeOfDay = NIGHT;
        }
        if(step % 60 < 30){
            weather = RAINING;
        }
        else{
            weather = SUNNY;
        }

        // Provide space for newborn animals.
        List<Living> newAnimals = new ArrayList<>();        
        // Let all rabbits act.
        System.out.println(animals.size());
        for(Iterator<Living> it = animals.iterator(); it.hasNext(); ) {
            Living animal = it.next(); 
            animal.act(newAnimals, currentTimeOfDay, weather);  
            animal.infect();
            if(! animal.isAlive()) {
                it.remove();
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
                    Fox fox = new Fox(true, field, location, rand.nextBoolean(), true);
                    animals.add(fox);
                }
                else if(rand.nextDouble() <= RABBIT_CREATION_PROBABILITY) {
                   Location location = new Location(row, col);
                   Rabbit rabbit = new Rabbit(true, field, location, rand.nextBoolean(), true);
                   animals.add(rabbit);
                }
                else if(rand.nextDouble()<= SQUIRREL_CREATION_PROBABILITY)
                {
                    Location location = new Location(row, col);
                    Squirrel squirrel = new Squirrel(true, field, location, rand.nextBoolean(), true);
                    animals.add(squirrel);
                }
                else if(rand.nextDouble()<= OWL_CREATION_PROBABILITY)
                {
                    Location location = new Location(row, col);
                    Owl owl= new Owl(true, field, location, rand.nextBoolean(), rand.nextBoolean());
                    animals.add(owl);
                }
                else if(rand.nextDouble()<= BEAR_CREATION_PROBABILITY)
                {
                    Location location = new Location(row, col);
                    Bear bear= new Bear(true, field, location, rand.nextBoolean(), rand.nextBoolean());
                    animals.add(bear);
                }
                else if(rand.nextDouble()<= OAKTREE_CREATION_PROBABILITY)
                {
                    Location location = new Location(row, col);
                    OakTree oakTree= new OakTree(true, field, location,false);
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
    
    public int getStep()
    {
        return step; 
    }
    
    
    
}
