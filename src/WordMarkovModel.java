import java.util.ArrayList;
import java.util.Arrays;
import java.util.TreeMap;
import java.util.Random;
import java.util.Scanner;
import java.util.StringTokenizer;
import java.util.TreeMap;


public class WordMarkovModel extends AbstractModel {

	private String myString;
	private String[] words;
	private ArrayList<String> wordList;

	private Random myRandom;
	public static final int DEFAULT_COUNT = 100; // default # random letters generated

	public WordMarkovModel() {

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
		map = new TreeMap<WordNgram, ArrayList<String>>();

		super.messageViews("#read: " + count + " chars in: " + time + " secs");
	}

	protected int readChars(Scanner s) {
		myString = s.useDelimiter("\\Z").next();
		// create word array
		words = myString.split("\\s+");
		// create word arraylist
		wordList = new ArrayList<String>(Arrays.asList(words));

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
	TreeMap<WordNgram, ArrayList<String>> map;

	public void nonBrute(int k, int numLetters) {
		// get start position
		int start = myRandom.nextInt(wordList.size() - k + 1);
		// create initial seed
		WordNgram seed = new WordNgram(words, start, k);
		// create stringbuilder
		StringBuilder sb = new StringBuilder();
		
		// add every word in word list into space delimited string
		for(String s : wordList) {
			if(s.isEmpty()) {
				
			} else {
				sb.append(s);
				sb.append(" ");
			}
		}
		// get the length of the string without wrap around words
		int wordListLength = sb.toString().length();
		int p = 0;
		int x = 0;
		// add the wrap around words
		while(p<k) {
			if(!wordList.get(x).isEmpty()) {
				sb.append(wordList.get(x));
				sb.append(" ");
				p++;
			}
			
			x++;
		}

		// stringfromwraparound
		String stringFromWrapAround = sb.toString().trim();
		StringBuilder build = new StringBuilder();

		// start time
		double stime = System.currentTimeMillis();

		// for 0 to numletters
		for(int i=0; i<numLetters; i++) {
			// list of strings
			ArrayList<String> list;
			
			// if the list is null in the map for seed
			if((list = map.get(seed)) == null || list.size() == 0) {
				// create it
				list = new ArrayList<String>();

				// pos 0
				int pos = 0;
				// while the seed exists
				while((pos = stringFromWrapAround.indexOf(seed.getWordsAppended(), pos)) != -1 && pos < wordListLength) {
					int startIndex = stringFromWrapAround.indexOf(" ", pos) + 1;
					int endIndex = startIndex;
					for(int n=0; n<k; n++) {
						endIndex = stringFromWrapAround.indexOf(" ", endIndex) + 1;
					}
					
					if(endIndex < startIndex) {
						endIndex = stringFromWrapAround.length();
					}
					
					// create delimitable string 
					String delimitableString = stringFromWrapAround.substring(startIndex, endIndex);

					// create string array
					String[] wordGramArray = new String[k];
					// put space delimited words into array
					wordGramArray = delimitableString.split(" ");

					// add the last word to the list
					list.add(wordGramArray[wordGramArray.length-1]);
					// increment the position by 1 word
					pos = pos + wordGramArray[0].length();
				}
				
				// put the seed in the list
				map.put(seed, list);
			}

			// pick a random word from the list
			int pick = myRandom.nextInt(list.size());
			String st = list.get(pick);
			// add it to the builder plus space
			build.append(st);
			build.append(" ");
			// get a new seed
			seed = new WordNgram(seed.getWords(), st);
		}
		
		// finish up with timing and notify view
		double etime = System.currentTimeMillis();
		double time = (etime - stime) / 1000.0;
		this.messageViews("Time to generate: " + time + " Seed size: " + map.size());
		this.notifyViews(build.toString());
	}

}
