import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;
import java.util.Scanner;


public class MapMarkovModel extends AbstractModel {

	private String myString;
	private Random myRandom;
	public static final int DEFAULT_COUNT = 100; // default # random letters generated

	public MapMarkovModel() {

		myRandom = new Random(1234);
	}

	/**
	 * Create a new training text for this model based on the information read
	 * from the scanner.
	 * @param s is the source of information
	 */
	@Override
	public void initialize(Scanner s) {
		double start = System.currentTimeMillis();
		int count = readChars(s);
		double end = System.currentTimeMillis();
		double time = (end - start) / 1000.0;

		// create map
		map = new HashMap<String, ArrayList<Character>>();

		super.messageViews("#read: " + count + " chars in: " + time + " secs");
	}

	protected int readChars(Scanner s) {
		myString = s.useDelimiter("\\Z").next();
		s.close();
		return myString.length();
	}

	/**
	 * Generate N letters using an order-K markov process where
	 * the parameter is a String containing K and N separated by
	 * whitespace with K first. If N is missing it defaults to some
	 * value.
	 */
	@Override
	public void process(Object o) {
		String temp = (String) o;
		String[] nums = temp.split("\\s+");
		int k = Integer.parseInt(nums[0]);
		int numLetters = DEFAULT_COUNT;
		if (nums.length > 1) {
			numLetters = Integer.parseInt(nums[1]);
		}
		nonBrute(k, numLetters);
	}

	// map
	HashMap<String, ArrayList<Character>> map;

	public void nonBrute(int k, int numLetters) {
		// get start position
		int start = myRandom.nextInt(myString.length() - k + 1);
		// create initial seed
		String str = myString.substring(start, start + k);
		// create wraparoundstring
		String wrapAroundString = myString + myString.substring(0,k);   
		// create stringbulder
		StringBuilder build = new StringBuilder();

		// start time
		double stime = System.currentTimeMillis();

		// for 0 to numletters
		for(int i=0; i < numLetters; i++) {
			// list of characters
			ArrayList<Character> list;

			// if the list is null in the map for seed
			if((list = map.get(str)) == null) {
				// create it
				list = new ArrayList<Character>();

				// pos 0
				int pos = 0;
				// while the seed exists
				while ((pos = wrapAroundString.indexOf(str, pos)) != -1 && pos < myString.length()) {
					// get a seed, at it to the list, and increment pos
					char ch = wrapAroundString.charAt(pos + k);
					list.add(ch);
					pos++;
				}
				// put the seed and list in the map
				map.put(str, list);

			} 
			
			// pick a random character from the list
			int pick = myRandom.nextInt(list.size());
			char ch = list.get(pick);
			// add it to the builder plus space
			build.append(ch);
			// get a new seed
			str = str.substring(1) + ch;

		}

		// finish up with timing and notify view
		double etime = System.currentTimeMillis();
		double time = (etime - stime) / 1000.0;
		this.messageViews("Time to generate: " + time);
		this.notifyViews(build.toString());
	}
}
