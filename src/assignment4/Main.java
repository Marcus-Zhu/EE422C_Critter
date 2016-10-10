/* CRITTERS Main.java
 * EE422C Project 4 submission by
 * Replace <...> with your actual data.
 * <Student1 Name>
 * <Student1 EID>
 * <Student1 5-digit Unique No.>
 * <Student2 Name>
 * <Student2 EID>
 * <Student2 5-digit Unique No.>
 * Slip days used: <0>
 * Fall 2016
 */
package assignment4; // cannot be in default package
import java.util.Scanner;
import java.io.*;


/*
 * Usage: java <pkgname>.Main <input file> test
 * input file is optional.  If input file is specified, the word 'test' is optional.
 * May not use 'test' argument without specifying input file.
 */
public class Main {

    static Scanner kb;	// scanner connected to keyboard input, or input file
    private static String inputFile;	// input file, used instead of keyboard input if specified
    static ByteArrayOutputStream testOutputString;	// if test specified, holds all console output
    private static String myPackage;	// package of Critter file.  Critter cannot be in default pkg.
    private static boolean DEBUG = false; // Use it or not, as you wish!
    static PrintStream old = System.out;	// if you want to restore output to console


    // Gets the package name.  The usage assumes that Critter and its subclasses are all in the same package.
    static {
        myPackage = Critter.class.getPackage().toString().split(" ")[1];
    }

    /**
     * Main method.
     * @param args args can be empty.  If not empty, provide two parameters -- the first is a file name, 
     * and the second is test (for test output, where all output to be directed to a String), or nothing.
     */
    public static void main(String[] args) { 
        if (args.length != 0) {
            try {
                inputFile = args[0];
                kb = new Scanner(new File(inputFile));			
            } catch (FileNotFoundException e) {
                System.out.println("USAGE: java Main OR java Main <input file> <test output>");
                e.printStackTrace();
            } catch (NullPointerException e) {
                System.out.println("USAGE: java Main OR java Main <input file>  <test output>");
            }
            if (args.length >= 2) {
                if (args[1].equals("test")) { // if the word "test" is the second argument to java
                    // Create a stream to hold the output
                    testOutputString = new ByteArrayOutputStream();
                    PrintStream ps = new PrintStream(testOutputString);
                    // Save the old System.out.
                    old = System.out;
                    // Tell Java to use the special stream; all console output will be redirected here from now
                    System.setOut(ps);
                }
            }
        } else { // if no arguments to main
            kb = new Scanner(System.in); // use keyboard and console
        }

        /* Do not alter the code above for your submission. */
        /* Write your code below. */
        
		//System.out.println("GLHF");
        try {
        	for (int a = 0; a < 100; a++)
        		Critter.makeCritter("Algae");
        	
        	for (int a = 0; a < 25; a++)	
        		Critter.makeCritter("Craig");
		} catch (InvalidCritterException e) {
			// TODO Auto-generated catch block
			System.out.println("Invalid Critter exception!");
			//e.printStackTrace();
		}
		String input = "";
		// loop until get a valid input
		do {
			System.out.print("critters>");
			input = kb.nextLine().trim(); // get rid of leading/trailing spaces

			/*if (input.equals("quit")) {
				// let program execution "fall through" to end
				System.out.println("Quitting");
			} */
			try{
				if (input != null && input.length() > 0){
					String[] words = input.split("\\s+");
					if (words[0].equals("show")) {
						Critter.displayWorld();
					} 
					else if (words[0].equals("step")) {
						// expecting space betw "step" and number of steps
						int steps = 1;
						if (words.length > 1)
							steps = Integer.parseInt(words[1]); 
						for (int i = 0; i < steps; i++)
							Critter.worldTimeStep();
					}
					else if (words[0].equals("seed")){
						if (words.length > 1){
							long seed = Integer.parseInt(words[1]);
							Critter.setSeed(seed);
						}
						
					}
					else if (words[0].equals("make")){
						if (words.length > 1){
							String critName = words[1];
							int numCrits = 1;
							if (words.length > 2)
								try{
									numCrits = Integer.parseInt(words[2]);
								
									for (int k = 0; k < numCrits; k++)
										Critter.makeCritter(critName);
								} 
								catch (InvalidCritterException | NumberFormatException e) {
									// TODO Auto-generated catch block
									//e.printStackTrace();
									System.out.println("error processing: " + input);
								}
						}
					}
					else{
						System.out.println("invalid command: " + input);	
					}
				}
			}
			catch(NumberFormatException e){
				System.out.println("invalid command: " + input);
			}

		} while (!input.equals("quit"));
        /* Write your code above */
        System.out.flush();

    }
}
