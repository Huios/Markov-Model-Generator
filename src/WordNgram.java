/*
 * This class encapsulates N words/strings so that the
 * group of N words can be treated as a key in a map or an
 * element in a set, or an item to be searched for in an array.
 * <P>
 * @author YOU,COMPSCI 201 STUDENT
 */

public class WordNgram implements Comparable<WordNgram>{
    
	// mywords
    private String[] myWords;
    // hash code
    private int hashCode;
    
    /*
     * Store the n words that begin at index start of array list as
     * the N words of this N-gram.
     * @param list contains at least n words beginning at index start
     * @start is the first of the N worsd to be stored in this N-gram
     * @n is the number of words to be stored (the n in this N-gram)
     */
    public WordNgram(String[] list, int start, int n) {
    	// create mywords
        myWords = new String[n];
        // copy array
        System.arraycopy(list, start, myWords, 0, n);
        
        // init
        init();
    }
    
    public WordNgram(String[] start, String newString) {
    	// create temp array
    	String[] temp = new String[start.length];
    	
    	// add values except final
    	for(int i=1; i<start.length; i++) {
    		temp[i-1] = start[i];
    	}
    	// set final string
    	temp[start.length-1] = newString;
    	
    	// set mywords
    	myWords = temp;
    	
    	// init
    	init();
    }
    
    private void init() {
    	// create hashcode
    	int prime = 29;
    	int hashSum = 1;
    	for(int i=0; i < myWords.length; i++) {
    		hashSum = hashSum*prime + myWords[i].hashCode();
    	}
    	
    	hashCode = hashSum;
    }
    
    /**
     * Return value that meets criteria of compareTo conventions.
     * @param wg is the WordNgram to which this is compared
     * @return appropriate value less than zero, zero, or greater than zero
     */
    @Override
	public int compareTo(WordNgram wg) {
    	// curr
    	int curr = 0;
    	
    	// for length of my words
    	for(int i=0; i<myWords.length; i++) {
    		// curr = compareto
    		curr = myWords[i].compareTo(wg.myWords[i]);
    		
    		// if it's not 0
    		if(curr != 0) {
    			// if it's greater than 0 return 1
    			if(curr > 0)
    				return 1;
    			// else return -1
    			else
    				return -1;
    		}
    		// keep going otherwise
    	}
    	
    	// return curr
        return curr;
    }
    
    /**
     * Return true if this N-gram is the same as the parameter: all words the same.
     * @param o is the WordNgram to which this one is compared
     * @return true if o is equal to this N-gram
     */
    @Override
	public boolean equals(Object o){
        WordNgram wg = (WordNgram) o;
        
        // if anything isn't equal, return false
        for(int i=0; i < wg.myWords.length; i++) {
        	if(!wg.myWords[i].equals(myWords[i]))
        		return false;
        }
        return true;
    }
    
    /**
     * Returns a good value for this N-gram to be used in hashing.
     * @return value constructed from all N words in this N-gram
     */
    @Override
	public int hashCode(){
    	// return hashcode
        return hashCode;
    }
    
    public String[] getWords() {
    	// return words
    	return myWords;
    }
    
    public String getWordsAppended() {
    	// return all words appended delimited by space
    	StringBuilder b = new StringBuilder();
    	for(String s : myWords) {
    		b.append(s);
    		b.append(" ");
    	}
    	return b.toString();
    }
}
