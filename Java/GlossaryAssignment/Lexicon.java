import java.util.ArrayList;
import java.util.Iterator;

/**
 * This class implements a lexicon with functionality to support the
 * search for words. If a word is not in the lexicon alternatives
 * (similar words) can be suggested. The identification of similar 
 * words is done with the Levenshtein algorithm.
 * 
 * @author Pablo Romero (Levenshtein algorithm implementation by
 * Michael Gilleland)
 * @version 2008.09.09
 */
public class Lexicon
{

	private ArrayList<String> wordList;

 	/**
	 * Create a new lexicon
    **/
  	public Lexicon() 
	  {
        wordList = new ArrayList<String>();
    }

    /**
     * This method returns true if the parameters exists in the list of
     * words, false otherwise.
     * @param word  word to look for in the wordList ArrayList
     * @return true if the word already exists, false otherwise
     */
    public boolean wordExists (String word) 
    {
        boolean found = false;
        Iterator i = wordList.iterator();
        while (!found && i.hasNext()) {
            if (word.equals(i.next()))
                found = true;
        }
        return found;
    }

    /**
     * This method computes the Levenshtein distance
     * @param from  first of the two strings to be compared
     * @param to  second string to be compared
     * @return the "distance" between the two parameters
     */
    private WordDistance getEditDistance (String from, String to)
    {
         int distance;
         WordDistance wordDistance;

         distance = editDistance(from,to);
         wordDistance = new WordDistance(from, to, distance);
         return wordDistance;
    }

    /**
     * This method  returns an ArrayList of WordDistance objects, one for
     * each of the words in the WordsList field of the Lexicon class
     * @param word  word to be compared to the lexicon items
     * @return the "distances" between the parameter and all the words
     *         in the lexicon
     */
    private ArrayList<WordDistance> getEditDistances (String word)
    {
         ArrayList<WordDistance> wordDistances = new ArrayList<WordDistance>();

         for (String compareWord : wordList)
             wordDistances.add(getEditDistance(word, compareWord));

         return wordDistances;
    }

    /**
     * This method  receives an ArrayList of WordDistance objects as
     * parameter and returns the WordDistance object with the minimum 
     * edit distance. If there are several matches, it returns the one
     * with the lowest index
     * @param wordDistances  ArrayList of WordDistance to be inspected
     * @return the minimum distance in the parameter
     */
    private WordDistance getMinDistance (ArrayList<WordDistance> wordDistances)
    {
         WordDistance minWordDistance = wordDistances.iterator().next();

         for (WordDistance wordDistance : wordDistances)
		 {
             if (minWordDistance.getDistance() > wordDistance.getDistance())
             {
                 minWordDistance = wordDistance;
             }
         }

         return minWordDistance;
    }

    /**
     * This method retrieves words from the wordList which are similar
     * to the one specified as first parameter
     * @param word  the word taken as a base in the search for 
     *              alternatives
     * @param n  the number of alternatives to be retrieved
     * @return an ArrayList containing the alternative words
     */
    public ArrayList<String> getSimilarWords (String word, int n) 
    {
        ArrayList<String> similarWords = new ArrayList<String>();
        ArrayList<WordDistance> editDistances = getEditDistances(word);

        // In case the number of alternatives requested is greater than the
        // actual number of words in the lexicon
        if (n>wordList.size())
            n= wordList.size();

        for (int i=0;i<n;i++)
        {
            WordDistance minWordDistance = getMinDistance(editDistances);
            similarWords.add(minWordDistance.to());
            editDistances.remove(minWordDistance);
        }

        return similarWords;
    }

    /**
     * This method adds a new word to the wordList field
     * @param word  the new term to be added
     * @return true if the word didn't exist and could be added,
     *         false otherwise
     */
    public boolean addWord (String word) 
    {
        boolean added = false;
        if (!wordExists(word)) {
            wordList.add(word);
            added = true;
        }

        return added;
    }

    /**
     * This method returns the minimum of three values
     * @param a  one of the three numbers to be compared
     * @param b  one of the three numbers to be compared
     * @param c  one of the three numbers to be compared
     * @return the minimum value of the three parameters
     */
    private int Minimum (int a, int b, int c) 
    {
       int mi;

        mi = a;
        if (b < mi) {
            mi = b;
        }
        if (c < mi) {
            mi = c;
        }
        return mi;
    }

    /**
     * This method computes the Levenshtein distance. The Levenshtein 
     * distance is the amount of difference between two strings and is 
     * given by the minimum number of operations needed to transform one 
     * string into the other, where an operation is an insertion, 
     * deletion, or substitution of a single character. This method should
     * be used to compare words, not phrases. If any of the arguments is
     * a phrase an exception is thrown.
     * @param s  one of the two strings to be compared
     * @param t  one of the two strings to be compared
     * @return an integer representing the distance between the two 
     *         parameters
     */
    public int editDistance (String s, String t) throws IllegalArgumentException
    {
        int d[][]; // matrix of costs
        int n; // length of s
        int m; // length of t
        int i; // iterates through s
        int j; // iterates through t
        char s_i; // ith character of s
        char t_j; // jth character of t
        int cost; // cost

        // The algorithm should be used for words, not phrases
        if (s.contains(" ") || t.contains(" ")) 
              throw new IllegalArgumentException("The Levenshtien algorithm should work on words, not phrases");
        
          // If any of the parameters is the empty string then the distance
          // is the length of the other

          n = s.length ();
          m = t.length ();
          if (n == 0) {
              return m;
          }
          if (m == 0) {
              return n;
          }
          d = new int[n+1][m+1];

          // Initialising the first row and column of the matrix to the
          // maximum possible transformations for each case

          for (i = 0; i <= n; i++) {
              d[i][0] = i;
          }

          for (j = 0; j <= m; j++) {
              d[0][j] = j;
          }

          // For each character in the first string

          for (i = 1; i <= n; i++) {

              s_i = s.charAt (i - 1);

             // For each character in the second string

             for (j = 1; j <= m; j++) {

                 t_j = t.charAt (j - 1);

                 // Calculate the transformation cost

                 if (s_i == t_j) {
                     cost = 0;
                 }
                 else {
                     cost = 1;
                 }

                // Choosing the minimum of the three possible transformations

                d[i][j] = Minimum (d[i-1][j]+1, d[i][j-1]+1, d[i-1][j-1] + cost);

             }

          }

          // The final cost will be found in the (n,m) cell of the table

          return d[n][m];

    }

}