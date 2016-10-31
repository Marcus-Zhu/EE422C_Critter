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

import java.util.*;

/**
 * This critter tends to keep away from fighting and reluctant from moving, but
 * likes reproducing.
 * 
 * @author Yilin Zhu
 *
 */
public class MyCritter1 extends Critter {
	private int lastFightTime = -1;
	private int lastDir = 0;

	@Override
	public void doTimeStep() {
		int dir = Critter.getRandomInt(8);
		if (lastFightTime == Critter.timeStep - 1) {
			run((lastDir + 4) % 8);
		} else {
			if (Critter.getRandomInt(4) == 0 || getEnergy() < 50)
				walk(dir);
			else if (getEnergy() > Params.min_reproduce_energy) {
				MyCritter1 child = new MyCritter1();
				reproduce(child, (lastDir + 4) % 8);
			}
		}
		lastDir = dir;
	}

	@Override
	public boolean fight(String opponent) {
		if (getEnergy() > Params.min_reproduce_energy && Critter.timeStep - lastFightTime > 10) {
			return true;
		}
		lastFightTime = Critter.timeStep;
		int dir = (lastDir + 4) % 8;
		run(dir);
		lastDir = dir;
		return false;
	}

	public String toString() {
		return "0";
	}

	public static void runStats(List<Critter> mycritter1s) {
		int total = mycritter1s.size();
		int total_happy = 0;
		int total_sad = 0;
		for (Object obj : mycritter1s) {
			MyCritter1 c = (MyCritter1) obj;
			if (Critter.timeStep - 1 == c.lastFightTime)
				total_sad++;
			else
				total_happy++;
		}
		System.out.print("" + total + " total MyCritter1    ");
		if (total != 0) {
			System.out.print("" + total_happy * 100.0 / total + "% happy   ");
			System.out.print("" + total_sad * 100.0 / total + "% sad.");
		}
		System.out.println();
	}
}
