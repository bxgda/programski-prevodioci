import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.Stack;

public class MPParser {
	// Konstante za neterminale (negativni brojevi)
	private static final int REDOLOOP = -1;
	private static final int EXPRESSION = -2;
	private static final int EXPRESSION_PRIME = -3;
	private static final int ANDEXPRESSION = -4;
	private static final int ANDEXPRESSION_PRIME = -5;
	private static final int TERM = -6;
	private static final int STATEMENT = -7;

	// Specijalni simboli
	private static final int EPSILON = -100;
	private static final int END_MARKER = 0; // sym.EOF

	// Sintaksna tabela M[neterminal][terminal]
	// Redovi: neterminali (indeksirani po redosledu)
	// Kolone: terminali (indeksirani po sym konstantama)
	private int[][] syntaxTable;

	// Magacin za parsiranje
	private Stack<Integer> stack;

	// Leksicki analizator
	private MPLexer lexer;

	// Trenutni token
	private Yytoken currentToken;

	// Flag za uspesno prepoznavanje
	private boolean recognized;
	private boolean error;

	// Sve produkcije gramatike
	private Production[] productions;

	public MPParser(MPLexer lexer) {
		this.lexer = lexer;
		this.stack = new Stack<>();
		initializeProductions();
		initializeSyntaxTable();
	}

	private void initializeProductions() {
		productions = new Production[15];

		// (1) RedoLoop → loop ( Expression ) { Statement redo ( Expression ) ;
		// Statement }
		productions[1] = new Production(REDOLOOP, new int[] { sym.LOOP, sym.LPAREN, EXPRESSION, sym.RPAREN, sym.LBRACE,
				STATEMENT, sym.REDO, sym.LPAREN, EXPRESSION, sym.RPAREN, sym.SEMICOLON, STATEMENT, sym.RBRACE });

		// (2) Expression → AndExpression Expression'
		productions[2] = new Production(EXPRESSION, new int[] { ANDEXPRESSION, EXPRESSION_PRIME });

		// (3) Expression' → || AndExpression Expression'
		productions[3] = new Production(EXPRESSION_PRIME, new int[] { sym.OR, ANDEXPRESSION, EXPRESSION_PRIME });

		// (3.1) Expression' → ε
		productions[4] = new Production(EXPRESSION_PRIME, new int[] { EPSILON });

		// (4) AndExpression → Term AndExpression'
		productions[5] = new Production(ANDEXPRESSION, new int[] { TERM, ANDEXPRESSION_PRIME });

		// (5) AndExpression' → && Term AndExpression'
		productions[6] = new Production(ANDEXPRESSION_PRIME, new int[] { sym.AND, TERM, ANDEXPRESSION_PRIME });

		// (5.1) AndExpression' → ε
		productions[7] = new Production(ANDEXPRESSION_PRIME, new int[] { EPSILON });

		// (6) Term → ID
		productions[8] = new Production(TERM, new int[] { sym.ID });

		// (6) Term → CONST
		productions[9] = new Production(TERM, new int[] { sym.CONST });

		// (7) Statement → RedoLoop
		productions[10] = new Production(STATEMENT, new int[] { REDOLOOP });

		// (7) Statement → ID = Expression ;
		productions[11] = new Production(STATEMENT, new int[] { sym.ID, sym.ASSIGN, EXPRESSION, sym.SEMICOLON });
	}

	private void initializeSyntaxTable() {
		// Sintaksna tabela: [neterminal_index][terminal_value]
		// Neterminali: 0=RedoLoop, 1=Expression, 2=Expression', 3=AndExpression,
		// 4=AndExpression', 5=Term, 6=Statement
		// Max terminal value iz sym.java je 24, pa koristimo 25
		syntaxTable = new int[7][25];

		for (int i = 0; i < 7; i++) {
			for (int j = 0; j < 25; j++) {
				syntaxTable[i][j] = -1;
			}
		}

		// RedoLoop (index 0)
		syntaxTable[0][sym.LOOP] = 1;

		// Expression (index 1)
		syntaxTable[1][sym.ID] = 2;
		syntaxTable[1][sym.CONST] = 2;

		// Expression' (index 2)
		syntaxTable[2][sym.RPAREN] = 4; // epsilon
		syntaxTable[2][sym.SEMICOLON] = 4; // epsilon
		syntaxTable[2][sym.OR] = 3;

		// AndExpression (index 3)
		syntaxTable[3][sym.ID] = 5;
		syntaxTable[3][sym.CONST] = 5;

		// AndExpression' (index 4)
		syntaxTable[4][sym.RPAREN] = 7; // epsilon
		syntaxTable[4][sym.SEMICOLON] = 7; // epsilon
		syntaxTable[4][sym.OR] = 7; // epsilon
		syntaxTable[4][sym.AND] = 6;

		// Term (index 5)
		syntaxTable[5][sym.ID] = 8;
		syntaxTable[5][sym.CONST] = 9;

		// Statement (index 6)
		syntaxTable[6][sym.LOOP] = 10;
		syntaxTable[6][sym.ID] = 11;
	}

	private int nonterminalToIndex(int nonterminal) {
		switch (nonterminal) {
		case REDOLOOP:
			return 0;
		case EXPRESSION:
			return 1;
		case EXPRESSION_PRIME:
			return 2;
		case ANDEXPRESSION:
			return 3;
		case ANDEXPRESSION_PRIME:
			return 4;
		case TERM:
			return 5;
		case STATEMENT:
			return 6;
		default:
			return -1;
		}
	}

	private String symbolToString(int symbol) {
		if (symbol == REDOLOOP)
			return "RedoLoop";
		if (symbol == EXPRESSION)
			return "Expression";
		if (symbol == EXPRESSION_PRIME)
			return "Expression'";
		if (symbol == ANDEXPRESSION)
			return "AndExpression";
		if (symbol == ANDEXPRESSION_PRIME)
			return "AndExpression'";
		if (symbol == TERM)
			return "Term";
		if (symbol == STATEMENT)
			return "Statement";
		if (symbol == EPSILON)
			return "ε";
		if (symbol == END_MARKER)
			return "#";

		// Terminali
		switch (symbol) {
		case sym.LOOP:
			return "loop";
		case sym.REDO:
			return "redo";
		case sym.LPAREN:
			return "(";
		case sym.RPAREN:
			return ")";
		case sym.LBRACE:
			return "{";
		case sym.RBRACE:
			return "}";
		case sym.SEMICOLON:
			return ";";
		case sym.OR:
			return "||";
		case sym.AND:
			return "&&";
		case sym.ASSIGN:
			return "=";
		case sym.ID:
			return "ID";
		case sym.CONST:
			return "CONST";
		default:
			return "UNKNOWN(" + symbol + ")";
		}
	}

	private boolean isNonterminal(int symbol) {
		return symbol < 0 && symbol != EPSILON;
	}

	public boolean parse() throws IOException {
		// Inicijalizacija
		stack.clear();
		stack.push(END_MARKER);
		stack.push(REDOLOOP); // Startni simbol

		recognized = false;
		error = false;

		currentToken = lexer.next_token();

		System.out.println("=== Pocetak parsiranja ===\n");

		// LL(1) algoritam
		while (!recognized && !error) {
			int top = stack.peek();
			int input = currentToken.m_index;

			System.out.println("Top: " + symbolToString(top) + ", Input: " + symbolToString(input) + " ["
					+ currentToken.m_text + "]");

			if (top == END_MARKER && input == END_MARKER) {
				// accept
				recognized = true;
				System.out.println(">>> ACCEPT <<<");

			} else if (top == input) {
				// pop and advance
				stack.pop();
				System.out.println(">>> POP i citaj sledeci token <<<");
				currentToken = lexer.next_token();

			} else if (isNonterminal(top)) {
				// Uzmi iz sintaksne tabele
				int ntIndex = nonterminalToIndex(top);

				if (ntIndex >= 0 && input < 25 && syntaxTable[ntIndex][input] != -1) {
					int productionNum = syntaxTable[ntIndex][input];
					Production prod = productions[productionNum];

					stack.pop();
					System.out.println(">>> Primeni produkciju " + productionNum + " <<<");

					// Push desnu stranu u obrnutom redosledu (osim epsilon)
					if (prod.right[0] != EPSILON) {
						for (int i = prod.right.length - 1; i >= 0; i--) {
							stack.push(prod.right[i]);
						}
					}
				} else {
					// Greska
					error = true;
					System.out.println(">>> GRESKA: Neocekivan simbol <<<");
					System.err.println("SINTAKSNA GRESKA na liniji " + currentToken.m_line + ", kolona "
							+ currentToken.m_charBegin);
					System.err.println("Neocekivan token: " + currentToken.m_text);
				}
			} else {
				// Greska - ocekivan terminal ne odgovara
				error = true;
				System.out.println(
						">>> GRESKA: Ocekivan " + symbolToString(top) + ", dobijen " + symbolToString(input) + " <<<");
				System.err.println(
						"SINTAKSNA GRESKA na liniji " + currentToken.m_line + ", kolona " + currentToken.m_charBegin);
				System.err.println("Ocekivan: " + symbolToString(top) + ", dobijen: " + currentToken.m_text);
			}

			System.out.println();
		}

		if (recognized) {
			System.out.println("=== Parsiranje uspesno zavrseno ===");
		} else {
			System.out.println("=== Parsiranje neuspesno ===");
		}

		return recognized;
	}

	public static void main(String[] args) {
		if (args.length < 1) {
			System.out.println("Upotreba: java MPParser <ulazni_fajl>");
			return;
		}

		try {
			FileReader fr = new FileReader(args[0]);
			MPLexer lexer = new MPLexer(fr);
			MPParser parser = new MPParser(lexer);

			boolean success = parser.parse();
			System.exit(success ? 0 : 1);

		} catch (FileNotFoundException e) {
			System.err.println("Fajl nije pronadjen: " + args[0]);
			System.exit(1);
		} catch (IOException e) {
			System.err.println("Greska pri citanju: " + e.getMessage());
			System.exit(1);
		}
	}
}
