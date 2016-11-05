package assignment5;

import assignment5.Critter.CritterShape;
import assignment5.Critter.TestCritter;
import javafx.scene.paint.Color;

public class MyCritter5 extends TestCritter {

	boolean willFight;


	@Override
	public Color viewColor() {
		return javafx.scene.paint.Color.web("#39425b");
	}

	@Override
	public CritterShape viewShape(){
		return CritterShape.CIRCLE;
	}

	@Override
	public void doTimeStep() {
		// TODO Auto-generated method stub

	}

	@Override
	public boolean fight(String opponent) {

		if (opponent.equals("@"))
			return true;
		willFight = getRandomInt(1) == 1;
		return willFight;
	}

	@Override
	public String toString () {
		return "5";
	}
}
