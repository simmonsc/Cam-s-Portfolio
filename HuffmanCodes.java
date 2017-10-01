package lab08;

import java.util.ArrayList;

import java.util.PriorityQueue;
import java.util.Scanner;
import hw04.CS232Visitor;
import lab08.CS232LinkedBinaryTree.BTNode;
/**
 * This Class solves the Huffman code problems found in the URL below.
 * Problem URL: http://users.dickinson.edu/~hugginsk/courses/2017_spring/cs232s17/labs/lab08.html
 * 
 * @author Cameron Simmons
 * @author Xuanhe Xu
 * @version 04/11/2017
 *
 */

public class HuffmanCodes {

	public int[] frequencyCount; // A frequency count of the possible
									// characters
	public ArrayList<char[]> fileData; // The file but in char array form
	public String type; // Specifies which kind of output we want

	/*
	 * The huffBuilder is the Queue that is used to construct the main tree that
	 * is used to find the Huffman code values.
	 */
	public PriorityQueue<CS232LinkedBinaryTree<Integer, Character>> huffBuilder;

	/*
	 * The finalHuffmanTree is the tree that is used to assign the 'direction
	 * values' to the characters. It's the final product of the huffBuilder
	 */
	public CS232LinkedBinaryTree<Integer, Character> finalHuffmanTree;

	public String[] huffCodeVals; // stores the Huffman codes for all
									// characters

	/**
	 * Initialize important pieces. For the most part, what is initialized in
	 * here is the important info that we want. (Except the final tree.. that
	 * comes later)
	 */
	public HuffmanCodes() {
		huffBuilder = new PriorityQueue<CS232LinkedBinaryTree<Integer, Character>>();
		fileData = new ArrayList<char[]>();
		frequencyCount = new int[127];
		huffCodeVals = new String[127];
	}

	/**
	 * This method is the 'basics' of the entire project. It's the one that
	 * calls all of the shots.
	 */
	public void run() {

		Scanner s = new Scanner(System.in); // A scanner to scan the file per
											// character
		type = s.next(); // The type specifies our wanted output
		s.nextLine(); 

		// STEP 1: Count how many times each character appears.
		this.charFrequencyCount(s);

		/*
		 * Type F only wants the frequency. Display it, and we're done (so
		 * return)
		 */
		if (type.equals("F")) {
			displayFrequency(frequencyCount);
			return;
		}

		/*
		 * STEP 2: we want to make our final tree (used to get the direction
		 * values) This entails loading up the priority queue and using it to
		 * build the tree.
		 */
		loadInitQueue();
		this.finalHuffmanTree = this.buildHuffTree();
		
		/*
		 * Type T wants a level order display of the Tree. Do so, and we're done
		 * (so return)
		 */
		if (type.equals("T")) {
			this.finalHuffmanTree.visitLevelOrder(new HuffmanVisitorForTypeT());
			return;
		}

		/*
		 * STEP 3: We want the Huffman code values for the characters.
		 * The Huffman codes are specified by the path (directions) from the root.
		 * 
		 * Each node in the tree has a path to get to it from the root. The
		 * leaves are especially important because they are the original
		 * characters scanned. Load every node with it's special direction value
		 * (the method saves the original character Huffman codes, the direction
		 * vals).
		 */
		this.loadDirectionValues(this.finalHuffmanTree.root);
		// Type H simply wants the codes... display them and we're done (so
		// return)
		if (type.equals("H")) {
			this.displayHuffCodesTable();
			return;
		}

		/*
		 * STEP 4: The Type must be M (unless there was invalid input) since all other cases were exhausted, so now we simply display the file using
		 * the Huffman codes.
		 */
		this.displayHuffCodefile();
	}

	/**
	 * Scan the file and count how many time each character appears. Save those
	 * values into the 'frequency count'. This method does just that by scanning
	 * each line as a char array, and after each line is adds a newline
	 * character (as there would be in the file).
	 * 
	 * @param s,
	 *            scanner that scans the file
	 */
	public void charFrequencyCount(Scanner s) {
		while (s.hasNextLine()) { /* keep scanning while there is info in the
								   * file
								   * */ 
			char[] curLine = s.nextLine().toCharArray();
			fileData.add(curLine); // save the data for later

			/*
			 * Go through the line curRent line and count the characters
			 */
			for (int i = 0; i < curLine.length; i++) {
				frequencyCount[curLine[i]]++;
			}
			frequencyCount[10]++; // add a new line!
		}
		/*
		 * There is always one extra newline so take one away to re-balance it.
		 */
		frequencyCount[10]--;
	}

	/**
	 * This method creates binary trees for every character that appeared at
	 * least once in the file. The root of each tree (because it only has one
	 * node) has the character's frequency as the key and the character as the
	 * value. In our world, binary trees are comparable so they can be put in
	 * the priority queue.
	 * 
	 * Once the tree in made, it gets put in the priority queue to await further
	 * processing by other methods.
	 * 
	 */
	public void loadInitQueue() {
		// go through every character
		for (int i = 0; i < frequencyCount.length; i++) { 
			// check to see if it has appeared at least once.
			if (frequencyCount[i] != 0) { 
				// if so, make a tree for it and add it to the priority queue.
				CS232LinkedBinaryTree<Integer, Character> curTree = new CS232LinkedBinaryTree<Integer, Character>();
				curTree.add(frequencyCount[i], (char) i);
				huffBuilder.add(curTree);
			}
		}
	}

	/**
	 * @param c1,
	 *            the first character
	 * @param c2,
	 *            the second character
	 * @return the character with higher priority
	 */
	public Character getPriorityCharacter(Character c1, Character c2) {
		if (c1 < c2) {
			return c1;
		} else {
			return c2;
		}
	}

	/**
	 * Until there is only one tree left in the huffBuilder priority queue, the
	 * first two trees are removed from the queue, joined into a single tree and
	 * replaced into the queue. They are joined by a new root who's key is the
	 * sum of the root keys of the removed trees and the value is the character
	 * of 'higher priority' from the values of the removed trees. The new root's
	 * left child is now the second tree removed while the right child is the
	 * first tree removed. At the end of this process, one tree will remain.
	 * That true will be removed and returned (into the finalHuffmanTree)
	 * 
	 * @return the final Huffman tree! It will be used for the rest of the
	 *         'Types'.
	 */
	public CS232LinkedBinaryTree<Integer, Character> buildHuffTree() {

		while (huffBuilder.size() > 1) {// Until one tree is left...

			// Make references to the important trees
			CS232LinkedBinaryTree<Integer, Character> newTree = new CS232LinkedBinaryTree<Integer, Character>();
			CS232LinkedBinaryTree<Integer, Character> firstSubTree = huffBuilder.remove();
			CS232LinkedBinaryTree<Integer, Character> secondSubTree = huffBuilder.remove();

			// Create the new root on the new joined tree. Use the character of higher priority.
			Character newTreeRootValue = this.getPriorityCharacter(firstSubTree.root.value, secondSubTree.root.value);
			newTree.add(firstSubTree.root.key + secondSubTree.root.key, newTreeRootValue);

			// Join the new root's right side with the first tree
			newTree.root.right = firstSubTree.root;
			firstSubTree.root.parent = newTree.root;

			// Join the new root's left side with the second tree
			newTree.root.left = secondSubTree.root;
			secondSubTree.root.parent = newTree.root;

			// put the newly created tree back into the priority queue
			huffBuilder.add(newTree); 
		}
		/*
		 * At the end of the whole process, we have one final mega-tree. return
		 * it and finally empty the queue!
		 */
		return huffBuilder.remove();
	}

	/**
	 * A recursive method that assigns directions values from the root to each
	 * node. We'll start at the root node (where there are no directions). If the
	 * current node being recured upon is a leaf, we've reached that end of that
	 * direction and that leaf's direction value is the Huffman code for the
	 * value that resides there. Save that 'code' into the 'huffCodeVals'
	 * 
	 * However, if i'm (speaking first person as the current node) not a leaf, my
	 * left child has every direction that I have plus an additional left (0).
	 * Load that child with my information and the additional left. Recur with
	 * that child (it's now me and repeat). The right child has all of my information plus
	 * an additional right (1). Load my information and the right, and recur with
	 * my right child as well. (This is assuming I have those children. If I
	 * don't, then nothing happens.... but I know that I have at least one of them)
	 * 
	 * @param curNode,
	 *            me ... I currently have a direction to get to me from the
	 *            root. I'll either load my information into the huffCodeVals or
	 *            pass my information on to my children.
	 */
	public void loadDirectionValues(BTNode<Integer, Character> curNode) {
		/*
		 * (Speaking first person as the curNode) If i'm a leaf, then I'm an
		 * original copy of the value and frequency pair. I'm important, so i'll
		 * share my directions with the huffCodeVals. My directions are the
		 * Huffman code for my char value.
		 */
		if (curNode.isLeaf()) {
			/*
			 * Converts the ArrayList of direction values (for every leaf node)
			 * into a string using a StringBuffer and it's toString() (Note: The
			 * leaf nodes contain the original character values and frequencies)
			 */
			StringBuffer codeBuilder = new StringBuffer();
			// for each direction in the directions ...
			for (int i = 0; i < curNode.dirVal.size(); i++) {
				codeBuilder.append(curNode.dirVal.get(i));
			}
			this.huffCodeVals[curNode.value] = codeBuilder.toString();

		} else { // I have children
			// If I have a left child, I'll pass my information and their added
			// left (0)
			if (curNode.left != null) {
				curNode.left.dirVal.addAll(curNode.dirVal);
				curNode.left.dirVal.add(0);
				
				// I'm now the left child and repeat
				this.loadDirectionValues(curNode.left);
			}
			// If I have a right child, I'll pass my information and their added
			// right (1)
			if (curNode.right != null) {
				curNode.right.dirVal.addAll(curNode.dirVal);
				curNode.right.dirVal.add(1);
				// I'm now the right child and repeat
				this.loadDirectionValues(curNode.right); 
			}
		}
	}

	/**
	 * Goes through every character and displays the frequency of those who have
	 * appeared at least once. For those displayed, print the character, ':',
	 * and the frequency. tab and new line are special. Print "\t" for tab and
	 * "\n" for new line
	 * 
	 * @param frequencyCount,
	 *            this contains the frequencies.
	 */
	public void displayFrequency(int[] frequencyCount) {
		StringBuffer toPrint = new StringBuffer();
		// For every character...
		for (int i = 0; i < frequencyCount.length; i++) {
			if (frequencyCount[i] > 0) { // if it appears at least once..
				// tabs have a char value of 9
				if (i == 9) {
					toPrint.append("\\t");
					// new lines have a char value of 10
				} else if (i == 10) {
					toPrint.append("\\n");
				} else {
					// everything else is simply the same but as a char.
					// the index i is the char values
					toPrint.append((char) i);
				}
				// the values at i is (char)i's frequency.
				toPrint.append(":" + frequencyCount[i] + "\n");
			}
		}
		System.out.println(toPrint.toString());
	}

	/**
	 * Goes though every character and displays the Huffman code for that value.
	 * It displays the character,':', and then the code. All of the codes are
	 * held in huffCodeVals where the index (as a char) is the character and the
	 * value at that index is the code for that character. The special cases of \t and \n apply
	 * here too.
	 */
	public void displayHuffCodesTable() {
		StringBuffer toPrint = new StringBuffer();
		
		for (int i = 0; i < this.huffCodeVals.length; i++) {
			
			if (this.huffCodeVals[i] != null) {
				if (i == 9) {
					toPrint.append("\\t");
					
				} else if (i == 10) {
					toPrint.append("\\n");
					
				} else {
					toPrint.append((char) i);
				}
				
				toPrint.append(":" + this.huffCodeVals[i] + "\n");
			}
		}
		
		System.out.println(toPrint.toString());
	}

	/**
	 * Goes through the file's data and displays the file with the codes
	 * replacing the characters. The data is stored as an ArrayList of char
	 * arrays where each char array is a line. This method goes through each
	 * line (char array) and display each characters code. Thus, after each
	 * line, there must be a newLine, so the new line code is displays (except
	 * after the last line)
	 */
	public void displayHuffCodefile() {
		StringBuffer toPrint = new StringBuffer();
		/*
		 * Go through every line and print them with the codes
		 */
		for (char[] curLine : this.fileData) {
			for (char c : curLine) {
				toPrint.append(this.huffCodeVals[c]);
			}
			
			/*
			 * Keep taps on the number of newLines displayed. This ensures that we don't print a new
			 * line code for the last line
			 */
			if (this.frequencyCount[10] > 0) {
				toPrint.append(huffCodeVals[10]);
				this.frequencyCount[10]--;
			}
		}

		System.out.println(toPrint.toString());
	}

	/**
	 * This class is a visitor for the level order display of the
	 * finalHuffmanTree.
	 */
	private class HuffmanVisitorForTypeT implements CS232Visitor<Integer, Character> {
		/**
		 * When visiting each node, display that node's (character) value first
		 * and then is key (related to the frequency) The special case for \t
		 * and \n apply here too.
		 */
		@Override
		public void visit(Integer key, Character value) {
			StringBuffer toPrint = new StringBuffer();

			if ((int) value == 9) {
				toPrint.append("\\t");

			} else if ((int) value == 10) {
				toPrint.append("\\n");

			} else {
				toPrint.append(value);
			}

			toPrint.append(":" + key);
			System.out.println(toPrint.toString());

		}
	}

	public static void main(String[] args) {
		/*
		 * Keeping the main short... that's a first! Create a HuffmanCodes
		 * object and runs it.
		 */
		HuffmanCodes MyHuffmanCodes = new HuffmanCodes();
		MyHuffmanCodes.run();
	}
	
}