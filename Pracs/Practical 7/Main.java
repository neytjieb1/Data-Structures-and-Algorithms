import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.PrintStream;

public class Main {
	public static void testContains(Trie trie) {
		System.out.println("TESTING FALSE INPUTS");
		String[] arrFalse = {"WHERE","YER","YEQ","JEW","YUQ", "QQQQ", "QQQQQQ"};
		int i;
		for (i = 0; i < arrFalse.length; i++) {
			if (trie.contains(arrFalse[i])) {
				System.out.println("Failed. Must be false: " + arrFalse[i]);
				break;
			}
		}
		if (i==arrFalse.length) System.out.println("Success");
		System.out.println("TESTING TRUE INPUTS");
		String[] arrTrue = {"TERRY", "EWE", "WERE","TYRE", "YET", "EYE", "YEW", "ERR", "EWER", "QQQ", "QQE"};
		for (i = 0; i < arrTrue.length; i++) {
			if (!trie.contains(arrTrue[i])) {
				System.out.println("Failed. Must be true: " + arrTrue[i]);
			}
		}
		if (i==arrTrue.length) System.out.println("Success");


	}

	public static void main(String[] args) throws FileNotFoundException {
		/*PrintStream out = new PrintStream(new FileOutputStream("/home/jo/IdeaProjects/Practical 7/outputB.txt"));
		System.setOut(out);*/
		char[] letters = {'E', 'Q', 'R', 'T', 'W', 'Y'};
		Trie trie = new Trie(letters);
			
		trie.insert("TERRY");
		trie.insert("WERE");
		trie.insert("RYE");
		trie.insert("TYRE");
		trie.insert("YET");
		trie.insert("EYE");
		trie.print(true);
		System.out.println();

		trie.insert("YEW");
		trie.insert("WRY");
		trie.insert("ERR");
		trie.insert("EWE");
		trie.insert("EWER");
		trie.insert("QWERTY");
		trie.insert("ERE");
		trie.insert("RERE");
		trie.insert("RE");
		trie.insert("QQQ");
		trie.insert("QQE");
		trie.insert("QQWW");
		trie.print(false);

		testContains(trie);



		trie.printKeyList();


		/* Expected Output:
		Level 1 [ (#,0)  (E,1)  (Q,0)  (R,1)  (T,1)  (W,1)  (Y,1) ]
		Level 2 [EYE]
		Level 2 [RYE]
		Level 2 [ (#,0)  (E,1)  (Q,0)  (R,0)  (T,0)  (W,0)  (Y,1) ]
		Level 2 [WERE]
		Level 2 [YET]
		Level 3 [TERRY]
		Level 3 [TYRE]

		Level 1 [ (E,1)  (R,1)  (T,1)  (W,1)  (Y,1) ]
		Level 2 [ (R,1)  (W,1)  (Y,1) ]
		Level 2 [RYE]
		Level 2 [ (E,1)  (Y,1) ]
		Level 2 [ (E,1)  (R,1) ]
		Level 2 [ (E,1) ]
		Level 3 [ERR]
		Level 3 [ (E,1) ]
		Level 3 [EYE]
		Level 3 [TERRY]
		Level 3 [TYRE]
		Level 3 [WERE]
		Level 3 [WRY]
		Level 3 [ (T,1)  (W,1) ]
		Level 4 [ (#,1)  (R,1) ]
		Level 4 [YET]
		Level 4 [YEW]
		Level 5 [EWE]
		Level 5 [EWER]
		false
		true
		false
		true
		ERR EWE EWER EYE RYE TERRY TYRE WERE WRY YET YEW
		*/
	}
}
