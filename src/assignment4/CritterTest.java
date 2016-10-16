/* CRITTERS Main.java
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

import static org.junit.Assert.*;

import org.junit.Test;

public class CritterTest {
	
	@Test
	public void testWalk() throws InvalidCritterException {
		Critter.makeCritter("MyCritter1");
		MyCritter1 m1 = (MyCritter1) Critter.TestCritter.getPopulation().get(0);
		int x1a = m1.getX_coord(); int  y1a = m1.getY_coord();
		m1.doTimeStep();
		int x1b = m1.getX_coord(); int  y1b = m1.getY_coord();
		assertTrue((x1b - x1a == 1) || (x1b + Params.world_width - x1a) == 1);
		assertTrue(Math.abs(y1b - y1a) == 0);
	}
}
