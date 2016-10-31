/* CRITTERS MyCritter1.java
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

/**
 * This Critter runs in random directions
 * @author Andrew Wong
 */
public class MyCritter4 extends Critter{
	
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
