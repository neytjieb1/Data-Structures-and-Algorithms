public class Main {


	public static void main(String[] args)
	{
		char[] letters = {'E', 'Q', 'R', 'T', 'W', 'Y'};
		Trie trie = new Trie(letters);
			
		trie.insert("TERRY");
		trie.insert("WERE");
		trie.insert("RYE");
		trie.insert("TYRE");
		trie.insert("YET");
		trie.insert("EYE");
		trie.print(false);
		System.out.println();

		trie.insert("YEW");
		trie.insert("WRY");
		trie.insert("ERR");
		trie.insert("EWE");
		trie.insert("EWER");
		trie.print(false);

		/*
	System.out.println(trie.contains("WHERE"));
		System.out.println(trie.contains("TERRY"));
		System.out.println(trie.contains("JEW"));
		System.out.println(trie.contains("EWE"));

		trie.printKeyList();		*/

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
