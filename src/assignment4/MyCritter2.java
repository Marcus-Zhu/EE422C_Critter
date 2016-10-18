/* CRITTERS MyCritter2.java
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

package assignment4;

import java.util.List;

/**
 * This Critter tends to move along a certain direction and reluctant to reproduce.
 * @author Yilin Zhu
 *
 */
public class MyCritter2 extends Critter {

	private int mainDir = 0;

	public MyCritter2(){
		mainDir = Critter.getRandomInt(8);
	}
	
	public MyCritter2(int dir){
		mainDir = dir%8;
	}

	@Override
	public void doTimeStep() {
		int dir = (mainDir + Critter.getRandomInt(3) - 1) % 8;
		walk(dir);
		if (getEnergy() > Params.min_reproduce_energy * 2){
			MyCritter2 child = new MyCritter2(dir);
			reproduce(child, (mainDir + 4) % 8);			
		}
	}

	@Override
	public boolean fight(String opp) {
		run(mainDir);
		return false;
	}
	
	public static void runStats(List<Critter> mycritter2s) {
		int total = mycritter2s.size();
		int left = 0;
		int right = 0;
		for (Object obj : mycritter2s) {
			MyCritter2 c = (MyCritter2) obj;
			int d = c.mainDir;
			if (d == 0 || d == 1 || d == 7)
				right++;
			else if (d == 3 || d == 4 || d == 5)
				left++;
		}
		System.out.print("" + total + " total MyCritter2    ");
		if (total != 0) {
			System.out.print("" + left * 100.0 / total + "% left   ");
			System.out.print("" + right *100.0 / total + "% right.");
		}
		System.out.println();
	}
	
	public String toString() {
		return "2";
	}

}
