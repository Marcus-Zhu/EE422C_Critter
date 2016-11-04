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
package assignment5; // cannot be in default package

import java.util.Scanner;
import java.awt.Point;
import java.io.*;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/*
 * Usage: java <pkgname>.Main <input file> test
 * input file is optional.  If input file is specified, the word 'test' is optional.
 * May not use 'test' argument without specifying input file.
 */
public class Controller {

	static Scanner kb; // scanner connected to keyboard input, or input file
	static ByteArrayOutputStream testOutputString; // if test specified, holds
													// all console output
	private static String myPackage; // package of Critter file. Critter cannot
										// be in default pkg.
	static PrintStream old = System.out; // if you want to restore output to
											// console
	static ByteArrayOutputStream buffer = new ByteArrayOutputStream();
	static PrintStream ps = new PrintStream(buffer);
	// Gets the package name. The usage assumes that Critter and its subclasses
	// are all in the same package.
	static {
		myPackage = Critter.class.getPackage().toString().split(" ")[1];
	}

	// try {
	// for (int a = 0; a < 5; a++)
	// Critter.makeCritter("Algae");
	// for (int a = 0; a < 2; a++)
	// Critter.makeCritter("Craig");
	// for (int a = 0; a < 2; a++)
	// Critter.makeCritter("MyCritter1");
	// for (int a = 0; a < 2; a++)
	// Critter.makeCritter("MyCritter2");
	// for (int a = 0; a < 2; a++)
	// Critter.makeCritter("MyCritter3");
	// for (int a = 0; a < 2; a++)
	// Critter.makeCritter("MyCritter4");
	// for (int a = 0; a < 2; a++)
	// Critter.makeCritter("MyCritter5");
	// } catch (InvalidCritterException e) {
	// System.out.println("Invalid Critter exception!");
	// }

	// public static Map<Point, ArrayList<Critter>> run(int n){
	// needStop = false;
	// Timer timer = new Timer();
	// timer.scheduleAtFixedRate( new TimerTask() {
	// public void run() {
	// Critter.worldTimeStep();
	// Critter.displayWorld();
	// }
	// }, 0, 1000/n);
	// while(!needStop);
	// timer.cancel();
	// }

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
			return "seed " + n + " set.";
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
