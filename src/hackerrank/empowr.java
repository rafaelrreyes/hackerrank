package hackerrank;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Scanner;

public class empowr {
    
    //output words
    public static ArrayList<String> outputList = new ArrayList<String>();
    
    public static int numberOfMatches = 0;
    
    //Initialize Puzzle Dimensions
    public static int puzzleHeight = 0;
    public static int puzzleWidth = 0;
    
    public static boolean solvePuzzle = true;
    
    //Temporary Word Containers
    public static ArrayList<String> horizontalWords = new ArrayList<String>();
    public static ArrayList<String> verticalWords = new ArrayList<String>();
    public static ArrayList<String> diagonalWords = new ArrayList<String>();
    
    //user input words
    public static ArrayList<String> wordsList = new ArrayList<String>();
    
    /**
     * Main function.
     * @param args
     */
    public static void main (String[] args) {
        
        //allow for variable word entry
        String puzzleWord = "";
        
        // Gets the dimensions of the puzzle based on user input
        createPuzzleDimensions();

        puzzleHeight = wordsList.size();
        puzzleWidth = wordsList.get(0).length();
        
        // Used for Test ----------------------
        
        //Solve the puzzle if input is valid
        if(solvePuzzle) {
            
            // Create input parameter to findWords method
            for(String word : wordsList) {
                puzzleWord = puzzleWord + word.toString();
            }
        
            //attempt to find new words at each character by searching horizontally, vertically or diagonally
            numberOfMatches = findWords(puzzleWord.toCharArray());
            
            //Print expected output
            System.out.println(numberOfMatches + "\n" + outputList.toString());
        } else
            System.out.println("You must enter words that all have the same length.");
    }
    
    /**
     * Gets the user input to determine the sizing of the puzzle.
     * Ensures that all inputted words are of equal length.
     * Prompts the user how many lines of words will make the puzzle.
     * 
     */
    public static void createPuzzleDimensions() {
        
        Scanner scan = new Scanner(System.in);
        System.out.print("Enter number of words: ");
        // Height of Puzzle
        puzzleHeight = scan.nextInt();
        
        //store words into an array
        
        //add first user input word to decide the width of the puzzle
        wordsList.add(new String(scan.next()));
        
        // Width of Puzzle
        puzzleWidth = wordsList.get(0).length();
        
        //scan remaining words and store into word list
        for (int inputWordCount = 1; inputWordCount < puzzleHeight; inputWordCount++) {
            String word = scan.next();
            
            //make sure each word is equal length based on first word entered
            if(word.length() != puzzleWidth) {
                System.out.println("That word does not match the width of the puzzle.");
                solvePuzzle = false;
                break;
            } else
                wordsList.add(new String(word));
        }
        
        scan.close();
    }
    
    /**
     * Finds the words in the Empowr crossword puzzle.
     * 
     * @param puzzle
     * @return matches
     */
    public static int findWords(char[] puzzle) {
    
        int matches = 0;
        //iterate through 'puzzle' input parameter
        int puzzleCounter = 0;
        
        //transposed puzzle (if necessary)
        char[][] transposedPuzzle = new char[puzzleWidth][puzzleHeight];
        
        //create an overall puzzle of the user inputed words for traversal    
        char[][] puzzleWords = new char[puzzleHeight][puzzleWidth];
        
        //reverse version of puzzle
        char[][] reversePuzzle = new char[puzzleHeight][puzzleWidth];
        char[][] reverseTransposedPuzzle = new char[puzzleWidth][puzzleHeight];
        
        //create an overall puzzle of the user inputed words for traversal
        for ( int i = 0; i < puzzleHeight; i++) {
            for (int j = 0; j < puzzleWidth; j++) {
                puzzleWords[i][j] = puzzle[puzzleCounter];
                puzzleCounter++;
            }
        }
        
        //add all matches and return for outputting to console for the solution (non-diagonal matches)
        matches = searchSingleLetters(puzzleWords) + searchHorizontalWords(puzzleWords) 
                    + searchVerticalWords(puzzleWords);

        //if the height of the puzzle is larger than the width, transpose and perform diagonal search
        if(puzzleHeight > puzzleWidth) {
        	
        	transposedPuzzle = transposePuzzle(puzzleWords);
        	
        	//create a reversed transposed version of the puzzle and do a search
        	for (int i = 0; i < puzzleWidth; i ++) {
                String temp = String.valueOf(transposedPuzzle[i]);
                reverseTransposedPuzzle[i] = new StringBuilder(temp).reverse().toString().toCharArray();
            }
        	
        	//search for diagonal matches and add to overall match count
        	matches += searchDiagonalWords(transposedPuzzle) + searchDiagonalWords(reverseTransposedPuzzle);

        } else {
        
        	//create a reversed version of the puzzle and do a search
        	for (int i = 0; i < puzzleHeight; i ++) {
        		String temp = String.valueOf(puzzleWords[i]);
        		reversePuzzle[i] = new StringBuilder(temp).reverse().toString().toCharArray();
        	}
        	
        	//search for diagonal matches and add to overall match count
        	matches += searchDiagonalWords(puzzleWords) + searchDiagonalWords(reversePuzzle);
        }
        
        return matches;
    }
    
    /**
     * Transposes the puzzle if height is greater than width.
     * Used for diagonal search algorithm to work.
     * 
     * @param puzzleWords
     * @return
     */
    private static char[][] transposePuzzle(char[][] puzzleWords) {
		char[][] transposedPuzzle = new char[puzzleWidth][puzzleHeight];
		for (int i = 0; i < puzzleWidth; i++) {
			for (int j = 0; j < puzzleHeight; j++) {
				transposedPuzzle[i][j] = puzzleWords[j][i];
			}
		}
		return transposedPuzzle;
	}

	/**
     * Search the puzzle for single letters that are included in the Empowr dictionary.
     * 
     * @param puzzleWords
     * @return matches
     */
    private static int searchSingleLetters(char[][] puzzleWords) {
        
        //used to track the number of dictionary hits found by single hits
        int matchCount = 0;
        
        for (int i = 0; i < puzzleHeight; i++) {
            for (int j = 0; j < puzzleWidth; j++) {
                if(PuzzleSolver.IsWord(String.valueOf(puzzleWords[i][j]))) {
                    outputList.add(String.valueOf(puzzleWords[i][j]));
                    matchCount++;
                }
            }
         }
        
        return matchCount;
    }

    /**
     * Search the puzzle along all diagonals.
     * 
     * @param puzzleWords
     * return matches
     * 
     */
    private static int searchDiagonalWords(char[][] puzzleWords) {
        
    	//clear the diagonal temporary possible words list
        diagonalWords.clear();
        
        String currWord = "";
        
        //used to track the number of dictionary hits found in diagonal search
        int matchCount = 0;
        
        //counters used to create diagonal possible words
        int diagDownCount = 0;
        int diagRightCount = 0;
        
        //used to determine when searching above or below the middle diagonal
        int endRightDiagonal = 0;
        int endBottomDiagonal = 0;
        
        //used to determine when to start searching on the bottom of the middle of the diagonals
        boolean startBottomDiag = false;
        
        //bottom and right border maximums
        int bottom;
        int right;
        
        //used to track when to start 
        int k = 2;
        int x = 2;
        
        //determines the border sizes based on transpose
        if (puzzleHeight > puzzleWidth) {
        	bottom = puzzleWidth;
        	right = puzzleHeight;
        } else {
        	bottom = puzzleHeight;
        	right = puzzleWidth;
        }
        
        for (int i = 0; i < puzzleHeight; i++) {

            for (int j = 0; j < puzzleWidth; j++) {

            	//begin top left to bottom right diagonal searches
                if(diagDownCount == bottom && !startBottomDiag) {
                	findSubStrings(currWord, diagonalWords);
                	diagDownCount = 0;           
                    diagRightCount = diagRightCount - bottom + 1;
                    endRightDiagonal++;
                    currWord = "";
                } 
                
                //if the traversal below the middle diagonal goes beyond the bottom border of puzzle, begin a recursive combination on current word
                if(diagDownCount == bottom && startBottomDiag) {
                	findSubStrings(currWord, diagonalWords);
                	diagDownCount = diagDownCount - bottom + k;
                	diagRightCount = 0;
                	k++;
                	currWord="";
                	endBottomDiagonal++;
                }
                
                //if the traversal goes beyond the right border of puzzle, begin a recursive combination on current word
                if(diagRightCount == right) {
                    findSubStrings(currWord, diagonalWords);
                    currWord="";
                    diagDownCount = 0;
                    diagRightCount = diagRightCount - bottom + x;
                    x++;
                    endRightDiagonal++;
                }

                //reset after searching above middle diagonal
                if(endRightDiagonal == right - 1) {
                    i = 1;
                    diagRightCount = 0;
                    diagDownCount = i;
                    endRightDiagonal = 0;
                    startBottomDiag = true;
                }

                //if bottom left diagonal is reached, end loop
                if(endBottomDiagonal == bottom - 2) {
                	break;
                }
                
                //create diagonal words by appending chars
                currWord = String.valueOf(new StringBuilder(currWord).append(puzzleWords[diagDownCount][diagRightCount]));
                
                diagDownCount++;
                diagRightCount++;
            }
            
        }
        
        //iterate through each word in diagonal words and check for dictionary matches
        for (String word : diagonalWords) {
            
            //non-reversed diagonals
            if (PuzzleSolver.IsWord(word.toLowerCase()) && !(word.length() == 1) && !(word.equals(""))) {
                outputList.add(word.toUpperCase());
                matchCount++;
            }
            
            //reversed diagonals
            if (PuzzleSolver.IsWord(new StringBuilder(word.toLowerCase()).reverse().toString())  && !(word.length() == 1) && !(word.equals(""))) {
                //add the reversed version of the word
                outputList.add(new StringBuilder(word.toUpperCase()).reverse().toString());
                matchCount++;
            }
        }
        
        return matchCount;
    }

    /**
     * Search the puzzle on each column.
     * 
     * @param puzzleWords
     * return matches
     */
    private static int searchVerticalWords(char[][] puzzleWords) {
        
        String currWord = "";
        
        //used to track the number of dictionary hits found in vertical search
        int matchCount = 0;
        
        //iterate through the puzzle vertically
        for (int j = 0; j < puzzleWidth; j++) {
            //reset currWord
            currWord = "";
            //build vertical strings by appending each character of the input parameter 2d puzzle 'puzzleWords'
            for (int i = 0; i < puzzleHeight; i++) {
                currWord = String.valueOf(new StringBuilder(currWord).append(puzzleWords[i][j]));
            }
            
            //clear the vertical words list for current word
            verticalWords.clear();
            
            //search current words substrings for possible dictionary matches and add to vertical words list
            findSubStrings(currWord.toLowerCase(), verticalWords);
            
            //iterate through each word in vertical words and check for dictionary matches
            for (String word : verticalWords) {
                
                //non-reversed verticals
                if (PuzzleSolver.IsWord(word.toLowerCase()) && !(word.length() == 1) && !(word.equals(""))) {
                    outputList.add(word.toUpperCase());
                    matchCount++;
                }
                
                //reversed verticals
                if (PuzzleSolver.IsWord(new StringBuilder(word.toLowerCase()).reverse().toString())  && !(word.length() == 1) && !(word.equals(""))) {
                    //add the reversed version of the word
                    outputList.add(new StringBuilder(word.toUpperCase()).reverse().toString());
                    matchCount++;
                }
            }
        }
        
        return matchCount;
    }

    /**
     * Searches the puzzle for horizontal words both in normal convention as well as reverse order.
     * 
     * @param puzzleWords
     */
    private static int searchHorizontalWords(char[][] puzzleWords) {
        
        String currWord = "";
        
        //used to track the number of dictionary hits found in vertical search
        int matchCount = 0;
        
        //iterate through the puzzle horizontally
        for (int i = 0; i < puzzleHeight; i++) {
            
            //clear current word
            currWord = "";
            
            currWord = String.valueOf(puzzleWords[i]);
            
            //clear the horizontal words list for current word
            horizontalWords.clear();
            
            //search current words substrings for possible dictionary matches and add to horizontal words list
            findSubStrings(currWord.toLowerCase(), horizontalWords);
            
            for (String word : horizontalWords) {
                
                //non-reversed horizontals
                if (PuzzleSolver.IsWord(word.toLowerCase()) && !(word.length() == 1) && !(word.equals(""))) {
                    outputList.add(word.toUpperCase());
                    matchCount++;
                }
                
                //reversed horizontals
                if (PuzzleSolver.IsWord(new StringBuilder(word.toLowerCase()).reverse().toString()) && !(word.length() == 1) && !(word.equals(""))) {
                    outputList.add(new StringBuilder(word.toUpperCase()).reverse().toString());
                    matchCount++;
                }
            }
        }
        return matchCount;
    }
    
    /**
     * Starts the recursive search for substring combinations of current input word.
     * @param currWord
     * @param currList
     */
    private static void findSubStrings(String currWord, ArrayList<String> currList) {
        //start getting substrings of the current word starting with first and second letters
        getSubstrings(currWord, 0, 1, currList);
    }
    
    /**
     * Recursively gets the different combinations of the input word.
     * i.e [ Word: CAT | 
     * Combinations: C, A, T, CA, AT, CAT ]
     * 
     * @param inputWord
     * @param beginIndex
     * @param endIndex
     * @param currList
     */
    private static void getSubstrings(String inputWord, int beginIndex, int endIndex, ArrayList<String> currList) {
        //if the beginning and ending index is equal to the end of the input word, return and end recursion
        if(beginIndex == inputWord.length() && endIndex == inputWord.length()) {
            return;
        } else {
            
            //if the endIndex is equal to the length of the inputWord + 1, increment the next recursive call to start from 1 ahead
            if(endIndex == inputWord.length() + 1) {
                getSubstrings(inputWord, beginIndex + 1, beginIndex + 1,  currList);
                
            //start a new recursive call that implements the endIndex + 1
            } else {
                //Add substring of inputWord to current word Array List
                currList.add(inputWord.substring(beginIndex, endIndex));
                getSubstrings(inputWord, beginIndex, endIndex + 1,  currList);
            }
        }
    }
    
    /**
     * Pre-determined dictionary of words by Empowr.
     *
     */
    public static class PuzzleSolver {
        public static String[] DICTIONARY = {"OX", "CAT", "TOY", "AT", "DOG", "CATAPULT", "T"};
        
        /**
         * Checks if the input String is a word contained in the pre-determined dictionary.
         * @param testWord
         * @return boolean
         */
        static boolean IsWord(String testWord) {
            
            if(Arrays.asList(DICTIONARY).contains(testWord.toUpperCase())) {
                return true;
            }
            return false;
        
        }
    }
}