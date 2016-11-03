/* CRITTERS Critter.java
 * EE422C Project 4 submission by
 * Yilin Zhu
 * yz22778
 * 16450
 * Andrew Wong
 * aw27772
 * 16450
 * Slip days used: <0>
 * Fall 2016
 */
package assignment5;

import java.awt.Point;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javafx.scene.paint.Color;
/* see the PDF for descriptions of the methods and fields in this class
 * you may add fields, methods or inner classes to Critter ONLY if you make your additions private
 * no new public, protected or default-package code or data can be added to Critter
 */


public abstract class Critter {
	private static String myPackage;
	private static boolean DEBUG = false;
	private	static List<Critter> population = new ArrayList<Critter>();
	private static List<Critter> babies = new ArrayList<Critter>();
	private static Map<Point, ArrayList<Critter>> map = new HashMap<Point, ArrayList<Critter>>();
	
	/* NEW FOR PROJECT 5 */
	public enum CritterShape {
		CIRCLE,
		SQUARE,
		TRIANGLE,
		DIAMOND,
		STAR
	}
	
	/* the default color is white, which I hope makes critters invisible by default
	 * If you change the background color of your View component, then update the default
	 * color to be the same as you background 
	 * 
	 * critters must override at least one of the following three methods, it is not 
	 * proper for critters to remain invisible in the view
	 * 
	 * If a critter only overrides the outline color, then it will look like a non-filled 
	 * shape, at least, that's the intent. You can edit these default methods however you 
	 * need to, but please preserve that intent as you implement them. 
	 */
	public Color viewColor() { 
		return javafx.scene.paint.Color.WHITE; 
	}
	
	public javafx.scene.paint.Color viewOutlineColor() { return viewColor(); }
	public javafx.scene.paint.Color viewFillColor() { return viewColor(); }
	
	//TODO: OVERRIDE VIEWSHAPE IN CRITTER METHODS!!!
	//public abstract CritterShape viewShape(); 
	public CritterShape viewShape(){
		return null;
	}
	
	
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
	protected int getEnergy() { return energy; }

	private int x_coord;
	private int y_coord;
	private int lastMovedTimeStep = -1;
	private static boolean in_fight_mode = false;

	protected int getX_coord() { return x_coord; }
	private void setX_coord(int x_coord) { this.x_coord = x_coord; }
	protected int getY_coord() { return y_coord; }
	private void setY_coord(int y_coord) { this.y_coord = y_coord; }
	protected Point getCoord(){	return new Point(x_coord, y_coord); }
	private void setCoord(Point p){ this.x_coord = p.x; this.y_coord = p.y; }

	/**
	 * Calculate a world position by current position, direction and distance.
	 * @param p Current position
	 * @param direction [0, 8] , from right to right-down, ccw
	 * @param step 1 for walk, 2 for run
	 * @return result
	 */
	private Point calcDirection(Point p, int direction, int step) {
		if (direction < 0 || direction > 8)
			return (Point) p.clone();
		int[] x_dir = { 1, 1, 0, -1, -1, -1, 0, 1 };
		int[] y_dir = { 0, -1, -1, -1, 0, 1, 1, 1 };
		int x = (p.x + x_dir[direction] * step);
		x = x > 0 ? x % Params.world_width : (x + Params.world_width) % Params.world_width;
		int y = (p.y + y_dir[direction] * step);
		y = y > 0 ? y % Params.world_height : (y + Params.world_height) % Params.world_height;
		return new Point(x, y);
	}

	/**
	 * Move to the instructed position. Will return original if:
	 * 	1. already moved in this timestep.
	 * 	2. position already occupied during fight mode.
	 * @param dir [0, 8] , from right to right-down, ccw
	 * @param step 1 for walk, 2 for run
	 * @return result
	 */
	private boolean moveDirection(int dir, int step) {
		if (lastMovedTimeStep < timeStep){
			Point dest = calcDirection(getCoord(), dir, step);
			boolean pos_conflict = false;
			if (in_fight_mode)
				for (Critter c : population)
					if (c.getCoord().equals(dest)) {
						pos_conflict = true;
						break;
					}
			if (!pos_conflict) {
				setCoord(dest);
				lastMovedTimeStep = timeStep;
				return true;
			}
		}
		if (DEBUG) System.out.println("Cannot move in fight:" + getCoord());
		return false;
	}

	protected String look(int direction, boolean steps){
		this.energy -= Params.look_energy_cost;
		// steps = true means move 2 steps
		Point nextPt;
		if (steps){
			nextPt = calcDirection(new Point(x_coord, y_coord), direction, 2);
		}
		// otherwise move 1 step
		else{
			nextPt = calcDirection(new Point(x_coord, y_coord), direction, 1);	
		}
		// test if another critter in same coordinates as where critter will move
		for (Critter c: population){
			if (c.getCoord().equals(nextPt)){
				return c.toString();
			}
		}
		return null;
	}

	/**
	 * Walk in direction for 1 step.
	 * @param direction
	 */
	protected final void walk(int direction) {
		moveDirection(direction, 1);
		energy -= Params.walk_energy_cost;
	}

	/**
	 * Run in direction for 2 steps.
	 * @param direction
	 */
	protected final void run(int direction) {
		moveDirection(direction, 2);
		energy -= Params.run_energy_cost;
	}

	/**
	 * Concrete subclasses of Critter may invoke this function.
	 * @param offspring
	 * @param direction
	 */
	protected final void reproduce(Critter offspring, int direction) {
		if (energy < Params.min_reproduce_energy){
			if (DEBUG) System.out.println("Energy not enough");
			return;
		}
		offspring.energy = energy/2;
		energy -= offspring.energy;
		offspring.setCoord(calcDirection(getCoord(), direction, 1));
		babies.add(offspring);
	}

	public static int timeStep = 0;
	public abstract void doTimeStep();
	public abstract boolean fight(String oponent);

	/**
	 * Subtract rest energy from critter's energy. Called by WorldTimeStep().
	 */
	protected void updateRestEnergy() {
		energy -= Params.rest_energy_cost;
	};

	/**
	 * create and initialize a Critter subclass.
	 * critter_class_name must be the unqualified name of a concrete subclass of Critter, if not,
	 * an InvalidCritterException must be thrown.
	 * (Java weirdness: Exception throwing does not work properly if the parameter has lower-case instead of
	 * upper. For example, if craig is supplied instead of Craig, an error is thrown instead of
	 * an Exception.)
	 * @param critter_class_name critter to be made
	 * @throws InvalidCritterException exception
	 */
	public static void makeCritter(String critter_class_name) throws InvalidCritterException {
		try {
			Critter new_critter = (Critter) Class.forName(critter_class_name).newInstance();
			new_critter.energy = Params.start_energy;
			new_critter.setX_coord(getRandomInt(Params.world_width));
			new_critter.setY_coord(getRandomInt(Params.world_height));
			population.add(new_critter);
		}
		catch (Exception e) {
//		catch (InstantiationException|IllegalAccessException|NoClassDefFoundError e) {
			throw new InvalidCritterException(critter_class_name);
		}
	}

	/**
	 * Gets a list of critters of a specific type.
	 * @param critter_class_name What kind of Critter is to be listed.  Unqualified class name.
	 * @return List of Critters.
	 * @throws InvalidCritterException exception
	 */
	public static List<Critter> getInstances(String critter_class_name) throws InvalidCritterException {
		List<Critter> result = new java.util.ArrayList<Critter>();
		try {
			for (Critter c : population) {
				if (Class.forName(critter_class_name).isInstance(c))
					result.add(c);
			}
		} catch (ClassNotFoundException e) {
			throw new InvalidCritterException(critter_class_name);
		}
		return result;
	}

	/**
	 * Prints out how many Critters of each type there are on the board.
	 * @param critters List of Critters.
	 */
	public static void runStats(List<Critter> critters) {
		System.out.print("" + critters.size() + " critters as follows -- ");
		Map<String, Integer> critter_count = new HashMap<String, Integer>();
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
		protected void setEnergy(int new_energy_value) { super.energy = new_energy_value; }
		protected void setX_coord(int new_x_coord) { super.x_coord = new_x_coord; }
		protected void setY_coord(int new_y_coord) { super.y_coord = new_y_coord; }
		protected int getX_coord() { return super.x_coord; }
		protected int getY_coord() { return super.y_coord; }
		protected static List<Critter> getPopulation() { return population; }
		protected static List<Critter> getBabies() { return babies;	}
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

	/**
	 * The main loop for critter world. Excecuted for every timestep.
	 */
	public static void worldTimeStep() {
		timeStep++;

		for (Critter c: population){
			c.doTimeStep();
		}

		updateMap();

		in_fight_mode = true;
        for (Point p : map.keySet()){
			ArrayList<Critter> mapPop = map.get(p);
			while (mapPop.size() > 1) {
				Critter c1 = mapPop.get(0);
				Critter c2 = mapPop.get(1);
				boolean f1 = c1.fight(c2.toString());
				boolean f2 = c2.fight(c1.toString());
				if (c1.energy <= 0) {
					mapPop.remove(0);
					population.remove(c1);
					continue;
				}
				if (c2.energy <= 0) {
					mapPop.remove(1);
					population.remove(c2);
					continue;
				}
				if (!p.equals(c1.getCoord())){
					mapPop.remove(c1);
					continue;
				}
				if (!p.equals(c2.getCoord())){
					mapPop.remove(c2);
					continue;
				}
				if (DEBUG){
					if (!(c1 instanceof Algae) || !(c2 instanceof Algae)) {
						System.out.print("Fight between: ");
						System.out.println(c1.toString() + ' ' + c2.toString() + ' ' + c2.getCoord());
					}
				}
				int r1 = f1 ? getRandomInt(c1.energy) : 0;
				int r2 = f2 ? getRandomInt(c2.energy) : 0;
				if (r1 > r2 || (r1 == r2 && (c2 instanceof Algae))) {
					c1.energy += c2.energy / 2;
					c2.energy = 0;
					mapPop.remove(1);
					population.remove(c2);
				} else {
					c2.energy += c1.energy / 2;
					c1.energy = 0;
					mapPop.remove(0);
					population.remove(c1);
				}
    		}
		}
		in_fight_mode = false;

		for (int i = 0; i < population.size(); i++){
			Critter c = population.get(i);
			c.updateRestEnergy();
			if (c.energy <= 0) {
				population.remove(i);
				i--;
			}
		}

        generateAlgae();

		population.addAll(babies);
		babies.clear();
	}

	/**
	 * Update 2d map according to population
	 */
	private static void updateMap() {
		map.clear();
		for (int i = 0; i < population.size(); i++) {
			Critter c = population.get(i);
			if (map.containsKey(c.getCoord())) {
				map.get(c.getCoord()).add(0, c);
			} else {
				ArrayList<Critter> l = new ArrayList<Critter>();
				l.add(c);
				map.put(c.getCoord(), l);
			}
		}
	}
	/**
	 * Print the critter world with each critter as a character. Border of the
	 * world is also printed.
	 */
	public static void displayWorld() {
		updateMap();
		PrintStream o = System.out;

		o.print('+');
		for (int i = 0; i < Params.world_width; i++)
			o.print('-');
		o.println('+');

		for (int i = 0; i < Params.world_height; i++) {
			o.print('|');
			for (int j = 0; j < Params.world_width; j++)
				if (map.containsKey(new Point(j, i))) {
					ArrayList<Critter> l = map.get(new Point(j, i));
					// not let other critters covered by algae
					int s = 0;
					while (l.get(s) instanceof Algae && s < l.size() - 1)
						s++;

					o.print(l.get(s).toString());
				}
				else
					o.print(' ');
			o.println('|');
		}

		o.print('+');
		for (int i = 0; i < Params.world_width; i++)
			o.print('-');
		o.println('+');

		if (DEBUG) {
			for (Critter c : population){
				if (!(c instanceof Algae))
				System.out.println(c.toString()+' '+c.getCoord()+' '+c.energy);
			}
		}
	}

	public static Map<Point, ArrayList<Critter>> getMap(){
		updateMap();
		return map;
	}
	/**
	 * Generate random positioned algae of refresh_algae_count number.
	 */
	public static final void generateAlgae() {
		for (int i = 0; i < Params.refresh_algae_count; i++) {
			Algae a = new Algae();
			a.setEnergy(Params.start_energy);
			a.setX_coord(getRandomInt(Params.world_width));
			a.setY_coord(getRandomInt(Params.world_height));
			population.add(a);
		}
	}
}
