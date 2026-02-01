import java.util.Stack;

public class LLParser {
    private static final int NT_BASE = 1000;

    private static final int NT_APPLY_EXPRESSION = 0;
    private static final int NT_NAME_LIST = 1;
    private static final int NT_NAME_LIST_PRIME = 2;
    private static final int NT_EXPRESSION = 3;
    private static final int NT_EXPRESSION_PRIME = 4;
    private static final int NT_TERM = 5;

    private static final int NONTERM_COUNT = 6;
    private static final int TERM_COUNT = 10;

    private final MPLexer lexer;
    private Yytoken nextToken;

    // parseTable[nonterminal][terminal] = production number, 0 means error
    private final int[][] parseTable = new int[NONTERM_COUNT][TERM_COUNT];

    // productions indexed from 1
    private final int[][] productions = new int[10][];

    public LLParser(MPLexer lexer) {
        this.lexer = lexer;
        initProductions();
        initParseTable();
    }

    private void initProductions() {
        productions[1] = new int[] { sym.FOR, sym.ID, sym.IN, sym.LBRACKET, nt(NT_NAME_LIST), sym.RBRACKET, sym.APPLY, nt(NT_EXPRESSION) };
        productions[2] = new int[] { sym.ID, nt(NT_NAME_LIST_PRIME) };
        productions[3] = new int[] { sym.COMMA, sym.ID, nt(NT_NAME_LIST_PRIME) };
        productions[4] = new int[] { };
        productions[5] = new int[] { nt(NT_TERM), nt(NT_EXPRESSION_PRIME) };
        productions[6] = new int[] { sym.PLUS, nt(NT_TERM), nt(NT_EXPRESSION_PRIME) };
        productions[7] = new int[] { };
        productions[8] = new int[] { sym.ID };
        productions[9] = new int[] { sym.CONST };
    }

    private void initParseTable() {
        // ApplyExpression
        parseTable[NT_APPLY_EXPRESSION][sym.FOR] = 1;

        // NameList
        parseTable[NT_NAME_LIST][sym.ID] = 2;

        // NameList'
        parseTable[NT_NAME_LIST_PRIME][sym.COMMA] = 3;
        parseTable[NT_NAME_LIST_PRIME][sym.RBRACKET] = 4;

        // Expression
        parseTable[NT_EXPRESSION][sym.ID] = 5;
        parseTable[NT_EXPRESSION][sym.CONST] = 5;

        // Expression'
        parseTable[NT_EXPRESSION_PRIME][sym.PLUS] = 6;
        parseTable[NT_EXPRESSION_PRIME][sym.RBRACKET] = 7;
        parseTable[NT_EXPRESSION_PRIME][sym.EOF] = 7;

        // Term
        parseTable[NT_TERM][sym.ID] = 8;
        parseTable[NT_TERM][sym.CONST] = 9;
    }

    public boolean parse() {
        Stack<Integer> stack = new Stack<>();
        stack.push(sym.EOF);
        stack.push(nt(NT_APPLY_EXPRESSION));

        try {
            nextToken = lexer.next_token();

            while (!stack.isEmpty()) {
                int top = stack.pop();

                if (isTerminal(top)) {
                    if (top == nextToken.m_index) {
                        if (top == sym.EOF) {
                            System.out.println("SINTAKSNA ANALIZA ZAVRSENA: Ulaz je ISPRAVAN.");
                            return true;
                        }
                        nextToken = lexer.next_token();
                    } else {
                        reportTerminalError(top, nextToken);
                        return false;
                    }
                } else {
                    int ntIndex = top - NT_BASE;
                    int lookahead = nextToken.m_index;
                    int prod = (lookahead >= 0 && lookahead < TERM_COUNT) ? parseTable[ntIndex][lookahead] : 0;
                    if (prod == 0) {
                        reportNonterminalError(ntIndex, nextToken);
                        return false;
                    }
                    int[] rhs = productions[prod];
                    for (int i = rhs.length - 1; i >= 0; i--) {
                        stack.push(rhs[i]);
                    }
                }
            }
        } catch (Exception e) {
            System.err.println("Greska prilikom citanja tokena: " + e.getMessage());
            return false;
        }

        return false;
    }

    private boolean isTerminal(int symbol) {
        return symbol < NT_BASE;
    }

    private int nt(int ntIndex) {
        return NT_BASE + ntIndex;
    }

    private String getSymbolName(int index) {
        switch (index) {
            case sym.ID: return "identifikator (ID)";
            case sym.CONST: return "konstanta (CONST)";
            case sym.FOR: return "for";
            case sym.IN: return "in";
            case sym.APPLY: return "apply";
            case sym.PLUS: return "+";
            case sym.COMMA: return ",";
            case sym.LBRACKET: return "[";
            case sym.RBRACKET: return "]";
            case sym.EOF: return "kraj fajla (EOF)";
            default: return "nepoznat simbol (" + index + ")";
        }
    }

    private String getNonterminalName(int ntIndex) {
        switch (ntIndex) {
            case NT_APPLY_EXPRESSION: return "ApplyExpression";
            case NT_NAME_LIST: return "NameList";
            case NT_NAME_LIST_PRIME: return "NameList'";
            case NT_EXPRESSION: return "Expression";
            case NT_EXPRESSION_PRIME: return "Expression'";
            case NT_TERM: return "Term";
            default: return "<NT>";
        }
    }

    private void reportTerminalError(int expectedTerminal, Yytoken found) {
        System.err.println("--------------------------------------------------");
        System.err.println("SINTAKSNA GRESKA na liniji " + (found.m_line + 1));
        System.err.println("Pronadjeno: '" + found.m_text + "' [" + getSymbolName(found.m_index) + "]");
        System.err.println("Ocekivano: '" + getSymbolName(expectedTerminal) + "'");
        System.err.println("--------------------------------------------------");
    }

    private void reportNonterminalError(int ntIndex, Yytoken found) {
        StringBuilder expected = new StringBuilder();
        boolean first = true;
        for (int t = 0; t < TERM_COUNT; t++) {
            if (parseTable[ntIndex][t] != 0) {
                if (!first) expected.append(", ");
                expected.append("'").append(getSymbolName(t)).append("'");
                first = false;
            }
        }

        System.err.println("--------------------------------------------------");
        System.err.println("SINTAKSNA GRESKA na liniji " + (found.m_line + 1));
        System.err.println("Neterminal: " + getNonterminalName(ntIndex));
        System.err.println("Pronadjeno: '" + found.m_text + "' [" + getSymbolName(found.m_index) + "]");
        System.err.println("Ocekivano: " + expected);
        System.err.println("--------------------------------------------------");
    }
}
