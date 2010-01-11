/**
 * This class implements a container which represents the distance
 * between two words. It is used to record the edit distance between
 * a main and a secondary word.
 * 
 * @author Pablo Romero
 * @version 2004.08.10
 */
public class WordDistance
{
	// The two words to be compared
	private String word1;
	private String word2;

	// Their edit distance
  private int distance;

 	/**
	 * Create a new WordDistance
   * @param a  main word
   * @param b  word to be compared
   * @param ed  their edit distance
   **/
  	public WordDistance(String a, String b, int ed) 
	  {
        word1 = a;
        word2 = b;
        distance = ed;
    }

    /**
     * Accessor for the main word
     * @return a string representing the word's label
     */
    public String from () 
    {
        return word1;
    }

    /**
     * Accessor for the secondary word
     * @return a string representing the word's label
     */
    public String to () 
    {
        return word2;
    }

    /**
     * Accessor for the edit distance
     * @return an integer representing the distance
     */
    public int getDistance () 
    {
        return distance;
    }

}
