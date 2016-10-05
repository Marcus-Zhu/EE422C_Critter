/* CRITTERS Critter.java
 * EE422C Project 4 submission by
 * Replace <...> with your actual data.
 * <Student1 Name>
 * <Student1 EID>
 * <Student1 5-digit Unique No.>
 * <Student2 Name>
 * <Student2 EID>
 * <Student2 5-digit Unique No.>
 * Slip days used: <0>
 * Fall 2016
 */
package assignment4;

import java.awt.Point;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/* see the PDF for descriptions of the methods and fields in this class
 * you may add fields, methods or inner classes to Critter ONLY if you make your additions private
 * no new public, protected or default-package code or data can be added to Critter
 */


public abstract class Critter {
	private static String myPackage;
	private	static List<Critter> population = new ArrayList<Critter>();
	private static List<Critter> babies = new ArrayList<Critter>();
	private static Map<Point, ArrayList<Critter>> map = new HashMap<Point, ArrayList<Critter>>();

	// Gets the package name.  This assumes that Critter and its subclasses are all in the same package.
	static {
		myPackage = Critter.class.getPackage().toString().split(" ")[1];
	}
	
	private static java.util.Random rand = new java.util.Random();
	public static int getRandomInt(int max) {
		return rand.nextInt(max);
	}
	
	public static void setSeed(long new_seed) {
		rand = new java.util.Random(new_seed);
	}
	
	
	/* a one-character long string that visually depicts your critter in the ASCII interface */
	public String toString() { return ""; }
	
	private int energy = 0;
	protected void setEnergy(int new_energy) { energy = new_energy; }
	protected int getEnergy() { return energy; }
	
	private int x_coord;
	private int y_coord;
	private int lastMovedTimeStep = -1;
	protected int getX_coord() { return x_coord; }
	protected void setX_coord(int x_coord) { this.x_coord = x_coord; }
	protected int getY_coord() { return y_coord; }
	protected void setY_coord(int y_coord) { this.y_coord = y_coord; }
	protected void setCoord(Point p){ this.x_coord = p.x; this.y_coord = p.y; }
	protected Point getCoord(){	return new Point(x_coord, y_coord); }

	private Point moveDirection(Point p, int direction, int step){
		int w = Params.world_width;
		int h = Params.world_height;
		int x = p.x;
		int y = p.y;
		Point pn = new Point();
		switch(direction) {
		case 0:	pn.x = (x+step)%w; break;
		case 1:	pn.x = (x+step)%w; pn.y = (y-step)%h; break;
		case 2:	pn.y = (y-step)%h; break;
		case 3:	pn.x = (x-step)%w; pn.y = (y-step)%h; break;
		case 4:	pn.x = (x-step)%w; break;
		case 5:	pn.x = (x-step)%w; pn.y = (y+step)%h; break;
		case 6:	pn.y = (y+step)%h; break;
		case 7:	pn.x = (x+step)%w; pn.y = (y+step)%h; break;
		default: break;		
		}
		return pn;
	}
	protected final void walk(int direction) {
		if (lastMovedTimeStep < timeStep){
			setCoord(moveDirection(getCoord(),direction,1));
			lastMovedTimeStep = timeStep;
		}
		setEnergy(getEnergy()-Params.walk_energy_cost);
	}	
	protected final void run(int direction) {
		if (lastMovedTimeStep < timeStep){
			setCoord(moveDirection(getCoord(),direction,2));
			lastMovedTimeStep = timeStep;
		}
		setEnergy(getEnergy()-Params.run_energy_cost);
	}
	
	protected final void reproduce(Critter offspring, int direction) {
		if (energy < Params.min_reproduce_energy)
			return;
		offspring.setCoord(moveDirection(getCoord(), direction, 1));
		babies.add(offspring);
	}

	public static int timeStep = 0;
	public abstract void doTimeStep();
	public abstract boolean fight(String oponent);

	protected void updateRestEnergy() {
		setEnergy(getEnergy() - Params.rest_energy_cost);
	};
	
	/**
	 * create and initialize a Critter subclass.
	 * critter_class_name must be the unqualified name of a concrete subclass of Critter, if not,
	 * an InvalidCritterException must be thrown.
	 * (Java weirdness: Exception throwing does not work properly if the parameter has lower-case instead of
	 * upper. For example, if craig is supplied instead of Craig, an error is thrown instead of
	 * an Exception.)
	 * @param critter_class_name
	 * @throws InvalidCritterException
	 */
	public static void makeCritter(String critter_class_name) throws InvalidCritterException {
		try {
			Critter new_critter = (Critter) Class.forName(critter_class_name).newInstance();
			new_critter.setEnergy(Params.start_energy);
			new_critter.setX_coord(getRandomInt(Params.world_width)); 
			new_critter.setY_coord(getRandomInt(Params.world_height)); 
		}
		catch(Exception e){
			throw new InvalidCritterException(critter_class_name);
		}				
	}
	
	/**
	 * Gets a list of critters of a specific type.
	 * @param critter_class_name What kind of Critter is to be listed.  Unqualified class name.
	 * @return List of Critters.
	 * @throws InvalidCritterException
	 */
	public static List<Critter> getInstances(String critter_class_name) throws InvalidCritterException {
		List<Critter> result = new java.util.ArrayList<Critter>();
	
		return result;
	}
	
	/**
	 * Prints out how many Critters of each type there are on the board.
	 * @param critters List of Critters.
	 */
	public static void runStats(List<Critter> critters) {
		System.out.print("" + critters.size() + " critters as follows -- ");
		java.util.Map<String, Integer> critter_count = new java.util.HashMap<String, Integer>();
		for (Critter crit : critters) {
			String crit_string = crit.toString();
			Integer old_count = critter_count.get(crit_string);
			if (old_count == null) {
				critter_count.put(crit_string,  1);
			} else {
				critter_count.put(crit_string, old_count.intValue() + 1);
			}
		}
		String prefix = "";
		for (String s : critter_count.keySet()) {
			System.out.print(prefix + s + ":" + critter_count.get(s));
			prefix = ", ";
		}
		System.out.println();		
	}
	
	/* the TestCritter class allows some critters to "cheat". If you want to 
	 * create tests of your Critter model, you can create subclasses of this class
	 * and then use the setter functions contained here. 
	 * 
	 * NOTE: you must make sure that the setter functions work with your implementation
	 * of Critter. That means, if you're recording the positions of your critters
	 * using some sort of external grid or some other data structure in addition
	 * to the x_coord and y_coord functions, then you MUST update these setter functions
	 * so that they correctly update your grid/data structure.
	 */
	static abstract class TestCritter extends Critter {
		protected void setEnergy(int new_energy_value) {
			super.energy = new_energy_value;
		}
		
		protected void setX_coord(int new_x_coord) {
			super.x_coord = new_x_coord;
		}
		
		protected void setY_coord(int new_y_coord) {
			super.y_coord = new_y_coord;
		}
		
		protected int getX_coord() {
			return super.x_coord;
		}
		
		protected int getY_coord() {
			return super.y_coord;
		}
		

		/*
		 * This method getPopulation has to be modified by you if you are not using the population
		 * ArrayList that has been provided in the starter code.  In any case, it has to be
		 * implemented for grading tests to work.
		 */
		protected static List<Critter> getPopulation() {
			return population;
		}
		
		/*
		 * This method getBabies has to be modified by you if you are not using the babies
		 * ArrayList that has been provided in the starter code.  In any case, it has to be
		 * implemented for grading tests to work.  Babies should be added to the general population 
		 * at either the beginning OR the end of every timestep.
		 */
		protected static List<Critter> getBabies() {
			return babies;
		}
	}

	/**
	 * Clear the world of all critters, dead and alive
	 */
	public static void clearWorld() {
		population = new ArrayList<Critter>();
		babies = new ArrayList<Critter>();
		map = new HashMap<Point, ArrayList<Critter>>();
		timeStep = 0;		
	}
	
	public static void worldTimeStep() {
		timeStep++;

		for (Critter c: population){
			c.doTimeStep();
		}

		map.clear();
        for (int i = 0; i < population.size(); i++){
        	Critter c = population.get(i);
        	if (map.containsKey(c.getCoord())){
				map.get(c.getCoord()).add(0, c);
        	}
        	else {
        		ArrayList<Critter> l = new ArrayList<Critter>();
        		l.add(c);
        		map.put(c.getCoord(), l);
        	}
        }
        
        for (Point p : map.keySet()){
			ArrayList<Critter> mapPop = map.get(p);
			if (mapPop.size() > 1) {
				for (int i = 0; i < mapPop.size(); i++) {
					Critter c1 = mapPop.get(i);
					Critter c2 = mapPop.get(i + 1);
					boolean f1 = c1.fight(c2.toString());
					boolean f2 = c2.fight(c1.toString());
					if (c1.getEnergy() <= 0 || c2.getEnergy() <= 0) {
						continue;
					}
					int r1 = f1 ? getRandomInt(c1.getEnergy()) : 0;
					int r2 = f2 ? getRandomInt(c2.getEnergy()) : 0;
					if (r1 > r2) {
						c1.setEnergy(c1.getEnergy() + c2.getEnergy() / 2);
						c2.setEnergy(0);
						mapPop.remove(i + 1);
						population.remove(c2);
					} else {
						c2.setEnergy(c2.getEnergy() + c1.getEnergy() / 2);
						c1.setEnergy(0);
						mapPop.remove(i);
						population.remove(c1);
					}
        		}
        	}
        }
	
        
		for (int i = 0; i < population.size(); i++){
			Critter c = population.get(i);
			c.updateRestEnergy();
			if (c.getEnergy() <= 0) {
				population.remove(i);
				i--;
			}
		}
        
        generateAlgae();

		population.addAll(babies);
		babies.clear();
	}
	
	public static void displayWorld() {}

	public static final void generateAlgae() {
		for (int i = 0; i < Params.refresh_algae_count; i++) {
			Algae a = new Algae();
			a.setEnergy(Params.start_energy);
			a.setCoord(new Point(getRandomInt(Params.world_width), getRandomInt(Params.world_height)));
			population.add(a);
		}
	}

	public static final void printWorld() {
		PrintStream o = System.out;
		o.print('+');
		for (int i = 0; i < Params.world_width; i++)
			o.print('-');
		o.print('+');

		for (int i = 0; i < Params.world_height; i++) {
			o.print('|');
			for (int j = 0; j < Params.world_width; i++)
				if (map.containsKey(new Point(j, i)))
					o.print(map.get(new Point(j, i)).get(0).toString());
				else
					o.print(' ');
			o.print('|');
		}

		o.print('+');
		for (int i = 0; i < Params.world_width; i++)
			o.print('-');
		o.print('+');
	}
}
