package lab08;

import static org.junit.Assert.assertEquals;

import java.io.File;

import org.junit.Test;

import testbase.StdioTestBase;

public class HuffmanCodesTest extends StdioTestBase {
		
	@Test
	public void testSampleInput1() {
		String input = "F\nAABBBCDDDDEE";
		String output = "A:2\nB:3\nC:1\nD:4\nE:2\n";

		runTest(HuffmanCodes.class, input, output,
				"Incorrect result for sample input 1.");
	}
	
	@Test
	public void testSampleInput2() {
		String input = "T\nAABBBCDDDDEE";
		String output = "A:12\nB:7\nA:5\nD:4\nB:3\nA:3\nE:2\nA:2\nC:1";

		runTest(HuffmanCodes.class, input, output,
				"Incorrect result for sample input 2.");
	}
	
	@Test
	public void testSampleInput3() {
		String input = "H\nAABBBCDDDDEE";
		String output = "A:100\nB:01\nC:101\nD:00\nE:11";

		runTest(HuffmanCodes.class, input, output,
				"Incorrect result for sample input 3.");
	}
	
	@Test
	public void testSampleInput4() {
		String input = "M\nAABBBCDDDDEE";
		String output = "100100010101101000000001111";

		runTest(HuffmanCodes.class, input, output,
				"Incorrect result for sample input 4.");
	}
	
	@Test
	public void testLoadInitQueue1(){
		HuffmanCodes test = new HuffmanCodes();
		test.frequencyCount[50]=50;
		test.frequencyCount[75]=75;
		test.frequencyCount[100]=75;
		test.frequencyCount[125]=125;
		test.loadInitQueue();
		String output = "";
		while(!test.huffBuilder.isEmpty()){
			CS232LinkedBinaryTree<Integer,Character> curRootNode = test.huffBuilder.remove();
			char val = curRootNode.root.value;
			output+= val;
			output+= ":" + curRootNode.root.key + "\n";
		}
		/*
		 * 2 comes first because is has the lowest frequency
		 * K is second over d because it's ascii val is lower
		 * d next
		 * } because is has the highest frequency
		 */
		assertEquals("Incorrect # of row's", output,"2:50\nK:75\nd:75\n}:125\n"); 	
	}
	
	
	@Test
	public void testgetPriorityCharacter(){
		HuffmanCodes test = new HuffmanCodes();
		
		char correctChar = test.getPriorityCharacter('A', 'B');
		assertEquals("Incorrect char returned", correctChar,'A');
		
		correctChar = test.getPriorityCharacter('B', 'A');
		assertEquals("Incorrect char returned", correctChar,'A');
		
		correctChar = test.getPriorityCharacter('C', 'C');
		assertEquals("Incorrect char returned", correctChar,'C');
	}
	
	@Test
	public void checkSpecialCharOutputForFreqency(){
		/*
		 * Tabs and newlines have are kind of a special case for character. We don't print them out directly. We simply have their symbols (\t and \n)
		 */
		String input="F\n	\tAA\nA   ";
		String output = "\\t:2\n\\n:1\n :3\nA:3";
		runTest(HuffmanCodes.class, input, output,
				"Incorrect result for checkSpecialCharOutputForFreqency()");
	}
	
	
	@Test 
	public void checkSpecialCharOutputForLevelTree(){
		String input="T\n	\tAA\nA   ";
		String output= "\\t:9\n\\t:6\nA:3\n :3\n\\t:3\n\\t:2\n\\n:1";
		runTest(HuffmanCodes.class, input, output,
				"Incorrect result for checkSpecialCharOutputForLevelTree()");
	}
	
	@Test
	public void checkSpecialCharOutputForCodeTable(){
		String input="H\n	\tAA\nA   ";
		String output="\\t:010\n\\n:011\n :00\nA:1";
		runTest(HuffmanCodes.class, input, output,
				"Incorrect result for checkSpecialCharOutputForCodeTable()");
	}
	
	@Test
	public void checkSpecialCharOutputForHuffmanCode(){
		String input="M\n	\tAA\nA   ";
		String output = "010010110111000000";
		runTest(HuffmanCodes.class, input, output,
				"Incorrect result for  checkSpecialCharOutputForHuffmanCode()");
	}
	
	@Test
	public void testSmallFileForFrequency(){
		String input = "F\nABA";
		String output= "A:2\nB:1";
		runTest(HuffmanCodes.class, input, output,
				"Incorrect result for testSmallFileForFrequency()");
		
	}
	
	@Test
	public void testSmallFileForLevelTree(){
		String input = "T\nABA";
		String output= "A:3\nA:2\nB:1";
		runTest(HuffmanCodes.class, input, output,
				"Incorrect result for testSmallFileForLevelTree()");
		
	}
	
	@Test
	public void testSmallFileForCodeTable(){
		String input = "H\nABA";
		String output= "A:0\nB:1";
		runTest(HuffmanCodes.class, input, output,
				"Incorrect result for testSmallFileForCodeTable()");	
	}
	
	@Test
	public void testSmallFileForHuffmanCode(){
		String input = "M\nABA";
		String output= "010";
		runTest(HuffmanCodes.class, input, output,
				"Incorrect result for testSmallFileForHuffmanCode()");	
	}
	
	@Test
	public void testOnlyNewlinesAForFrequency(){
		String input = "F\n\n\n\n\nA";
		String output = "\\n:4\nA:1";
		runTest(HuffmanCodes.class, input, output,
				"Incorrect result for testOnlyNewlinesAForFrequency()");
	}
	
	@Test
	public void testOnlyNewlinesAForLevelTree(){
		String input = "T\n\n\n\n\nA";
		String output = "\\n:5\n\\n:4\nA:1";
		runTest(HuffmanCodes.class, input, output,
				"Incorrect result for testOnlyNewlinesAForLevelTree()");
	}
	
	@Test
	public void testOnlyNewlinesAForLevelTable(){
		String input = "H\n\n\n\n\nA";
		String output = "\\n:0\nA:1";
		runTest(HuffmanCodes.class, input, output,
				"Incorrect result for testOnlyNewlinesAForLevelTable()");
	}
	
	@Test
	public void testOnlyNewlinesAForHuffmanCode(){
		String input = "M\n\n\n\n\nA";
		String output = "00001";
		runTest(HuffmanCodes.class, input, output,
				"Incorrect result for testOnlyNewlinesAForHuffmanCode()");
	}
	
	@Test
	public void testTabAtEndForFrequency(){
		String input = "F\n\nA 	";
		String output = "\\t:1\n\\n:1\n :1\nA:1";
		runTest(HuffmanCodes.class, input, output,
				"Incorrect result for testTabAtEndForFrequency()");
	}
	
	@Test
	public void testTabAtEndLevelTree(){
		String input = "T\n\nA 	";
		String output = "\\t:4\n :2\n\\t:2\nA:1\n :1\n\\n:1\n\\t:1";
		runTest(HuffmanCodes.class, input, output,
				"Incorrect result for testTabAtEndLevelTable()");
	}
	
	@Test
	public void testTabAtEndForCodeTable(){
		String input = "H\n\nA 	";
		String output = "\\t:11\n\\n:10\n :01\nA:00";
		runTest(HuffmanCodes.class, input, output,
				"Incorrect result for testTabAtEndForCodeTable()");
	}
	
	@Test
	public void testTabAtEndForHuffmanCode(){
		String input = "M\n\nA 	";
		String output = "10000111";
		runTest(HuffmanCodes.class, input, output,
				"Incorrect result for testTabAtEndForHuffmanCode()");
	}
	
	@Test
	public void testASCIIvalBoundsForFrequency(){
		/*
		 * This file includes the bounds of the usable ACSII table: 9,10,32, and 126 (meaning 32-126 should work too).
		 */
		String input = "F\n	\n ~";
		String output = "\\t:1\n\\n:1\n :1\n~:1";
		runTest(HuffmanCodes.class, input, output,
				"Incorrect result for testASCIIvalBoundsForFrequency()");
	}
	
	@Test
	public void testASCIIvalBoundsForLevelTree(){
		
		String input = "T\n	\n ~";
		String output = "\\t:4\n :2\n\\t:2\n~:1\n :1\n\\n:1\n\\t:1";
		runTest(HuffmanCodes.class, input, output,
				"Incorrect result for testASCIIvalBoundsForLevelTree()");
	}
	
	@Test
	public void testASCIIvalBoundsForCodeTable(){
		
		String input = "H\n	\n ~";
		String output = "\\t:11\n\\n:10\n :01\n~:00";
		runTest(HuffmanCodes.class, input, output,
				"Incorrect result for testASCIIvalBoundsForCodeTable()");
	}
	
	@Test
	public void testASCIIvalBoundsForHuffmanCode(){
		
		String input = "M\n	\n ~";
		String output = "11100100";
		runTest(HuffmanCodes.class, input, output,
				"Incorrect result for testASCIIvalBoundsForHuffmanCode()");
	}
	
	@Test
	public void testBoundForFrequency(){
		File input = new File("src/lab08/testBoundsFrequency.txt");
		File output = new File("src/lab08/testBoundsFrequencyOutput.txt");
		runTest(HuffmanCodes.class, input, output,
				"Incorrect result for testBoundForFrequency()");
	}
	
	@Test
	public void testLargeFile(){
		File input = new File("src/lab08/testLargeFile.txt");
		File output = new File("src/lab08/testLargeFileOutput.txt");
		runTest(HuffmanCodes.class, input, output,
				"Incorrect result for testLargeFile()");
	}
	
	
	@Test public void testSampleFromHackerRankForHuffmanCode(){
		File input = new File("src/lab08/testSampleFromHackerRankForHuffmanCode.txt");
		File output = new File("src/lab08/testSampleFromHackerRankForHuffmanCodeOutput.txt");
		runTest(HuffmanCodes.class, input, output,
				"Incorrect result for testLargeFile()");
	}
	
}