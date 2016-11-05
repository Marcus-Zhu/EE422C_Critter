package assignment5;

import assignment5.Critter.TestCritter;
import javafx.scene.paint.Color;

public class Algae extends TestCritter {

	public String toString() { return "@"; }

	@Override
	public Color viewColor() {
		return javafx.scene.paint.Color.web("#72c7c4");
	}
	@Override
	public CritterShape viewShape(){
		return CritterShape.CIRCLE;
	}
	public boolean fight(String not_used) { return false; }

	public void doTimeStep() {
		setEnergy(getEnergy() + Params.photosynthesis_energy_amount);
	}
}
