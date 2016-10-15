package assignment4;

/*
 * This Critter is good for testing running away while fighting, and for testing 
 * movement in different directions.
 */
public class MyCritter4 extends MyCritter1{
	
	private int myDir = 0;
	
	@Override
	public void doTimeStep () {
		run (myDir);
		//walk(myDir);
		//myDir = (myDir+1)%8; // change direction each walk call, CCW.
	}
	
	@Override
	/**
	 * Always fights
	 */
	public boolean fight(String opp) {
		//run(myDir);
		return true;
	}
	
	public String toString() {
		return "4";
	}

}
