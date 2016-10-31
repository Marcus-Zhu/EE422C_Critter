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
 * This Critter only runs in north, east, south, or west direction
 * @author Andrew Wong
 */
public class MyCritter3 extends Critter{
	
	private int myDir = 0;
	
	@Override
	public void doTimeStep () {
		run(myDir);
		myDir = (myDir + 2) % 8; // change cardinal direction each walk call, CCW.
	}
	
	@Override
	/**
	 * Critter only fights when facing north or south
	 */
	public boolean fight(String opp) {
		return (myDir == 2 || myDir == 6);
	}
	
	public String toString() {
		return "3";
	}

}
