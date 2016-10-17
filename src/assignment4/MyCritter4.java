package assignment4;

/**
 * This Critter runs in random directions
 * @author Andrew Wong
 */
public class MyCritter4 extends MyCritter1{
	
	private int myDir = 0;
	
	@Override
	public void doTimeStep () {
		myDir = Critter.getRandomInt(7);
		run (myDir);
	}
	
	@Override
	/**
	 * Always fights
	 */
	public boolean fight(String opp) {
		return true;
	}
	
	public String toString() {
		return "4";
	}

}
