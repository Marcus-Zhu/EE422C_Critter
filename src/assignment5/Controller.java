/* CRITTERS Main.java
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
package assignment5; // cannot be in default package

import java.awt.Point;
import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class Controller {

	static PrintStream old = System.out;
	static ByteArrayOutputStream buffer = new ByteArrayOutputStream();
	static PrintStream ps = new PrintStream(buffer);

	public static Map<Point, ArrayList<Critter>> show() {
		return Critter.getMap();
	}

	public static Map<Point, ArrayList<Critter>> step(int n) {
		int steps = n;
		for (int i = 0; i < steps; i++) {
			Critter.worldTimeStep();
		}
		return Critter.getMap();
	}

	public static String seed(long n) {
		try {
			Critter.setSeed(n);
			return "seed " + n + " set.\n";
		} catch (NumberFormatException e) {
			return "error processing: seed " + n + '\n';
		}
	}

	public static boolean isCritter(String critName) {
		try {
			Critter.makeCritter(critName);
			return true;
		} catch (InvalidCritterException | NumberFormatException e) {
			return false;
		}
	}

	public static String make(String critName, int n) {
		try {
			for (int k = 0; k < n; k++)
				Critter.makeCritter(critName);
			return critName + " made.\n";
		} catch (InvalidCritterException | NumberFormatException e) {
			return "error processing: make " + critName + " " + n + '\n';
		}
	}

	public static String stats(String critName) {
		buffer.reset();
		System.setOut(ps);
		try {
			List<Critter> critList = Critter.getInstances(critName);
			Method m = Class.forName(critName).getMethod("runStats", List.class);
			m.invoke(null, critList);

		} catch (Exception e) {
			System.out.println("error processing: stats " + critName);
		}
		System.out.flush();
		System.setOut(old);
		return buffer.toString();
	}

	public static Map<Point, ArrayList<Critter>> clear() {
		Critter.clearWorld();
		return Critter.getMap();
	}
}
