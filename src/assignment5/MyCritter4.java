/* CRITTERS MyCritter1.java
 * EE422C Project 5 submission by
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

import javafx.scene.paint.Color;

/**
 * This Critter runs in random directions
 * @author Andrew Wong
 */
public class MyCritter4 extends Critter{

	private int myDir = 0;


	@Override
	public Color viewColor() {
		return javafx.scene.paint.Color.web("#e6659a");
	}

	@Override
	public CritterShape viewShape(){
		return CritterShape.CIRCLE;
	}

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
