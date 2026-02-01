import java.util.Stack;

public class LRParser {
    private MPLexer lexer;
    private Yytoken nextToken;
    private int errorCount = 0;

    // Sintaksna tabela akcija [stanje][terminal_index]
    private String[][] actionTable = new String[20][20]; 
    // Sintaksna tabela prelaza [stanje][neterminal_index]
    private int[][] gotoTable = new int[20][20];

    // Indeksi neterminala za GOTO tabelu
    private final int S = 0, P = 1, p = 2, E = 3, T = 4;

    // Podaci o smenama (Leva strana i broj simbola na desnoj strani)
    private int[] ruleLHS = { -1, S, P, P, p, p, E, E, T, T }; // -1 je za S'
    private int[] ruleSize = { 1, 7, 3, 1, 1, 3, 3, 1, 1, 1 };

    public LRParser(MPLexer lexer) {
        this.lexer = lexer;
        initTables();
    }

    private void initTables() {
        // Popunjavanje ACTION tabele prema tvojoj slici
        actionTable[0][sym.ID] = "s2";
        actionTable[1][sym.EOF] = "acc";
        actionTable[2][sym.LPAREN] = "s3";
        actionTable[3][sym.ID] = "s6";
        actionTable[4][sym.RPAREN] = "s7";
        actionTable[4][sym.COMMA] = "s8";
        actionTable[5][sym.RPAREN] = "r3";
        actionTable[5][sym.COMMA] = "r3";
        actionTable[6][sym.RPAREN] = "r4";
        actionTable[6][sym.COMMA] = "r4";
        actionTable[6][sym.ASSIGN] = "s9";
        actionTable[7][sym.ARROW] = "s11";
        actionTable[8][sym.ID] = "s6";
        actionTable[9][sym.CONST] = "s10";
        actionTable[10][sym.RPAREN] = "r5";
        actionTable[10][sym.COMMA] = "r5";
        actionTable[11][sym.ID] = "s14";
        actionTable[11][sym.CONST] = "s15";
        actionTable[12][sym.MULT] = "s18";
        actionTable[12][sym.SEMICOLON] = "s17";
        actionTable[13][sym.MULT] = "r7";
        actionTable[13][sym.SEMICOLON] = "r7";
        actionTable[14][sym.MULT] = "r8";
        actionTable[14][sym.SEMICOLON] = "r8";
        actionTable[15][sym.MULT] = "r9";
        actionTable[15][sym.SEMICOLON] = "r9";
        actionTable[16][sym.RPAREN] = "r2";
        actionTable[16][sym.COMMA] = "r2";
        actionTable[17][sym.EOF] = "r1";
        actionTable[18][sym.ID] = "s14";
        actionTable[18][sym.CONST] = "s15";
        actionTable[19][sym.MULT] = "r6";
        actionTable[19][sym.SEMICOLON] = "r6";

        // GOTO tabela
        gotoTable[0][S] = 1;
        gotoTable[3][P] = 4;
        gotoTable[3][p] = 5;
        gotoTable[8][p] = 16;
        gotoTable[11][E] = 12;
        gotoTable[11][T] = 13;
        gotoTable[18][T] = 19;
    }

    public boolean parse() {
        Stack<Integer> stack = new Stack<>();
        stack.push(0); // Pocetno stanje l0

        try {
            nextToken = lexer.next_token();

            while (true) {
                int currentState = stack.peek();
                int symbolIdx = nextToken.m_index;
                String action = actionTable[currentState][symbolIdx];

                if (action == null) {
                    reportError(currentState, nextToken);
                    return false;
                }

                if (action.equals("acc")) {
                    System.out.println("SINTAKSNA ANALIZA ZAVRSENA: Ulaz je ISPRAVAN.");
                    return true;
                } 
                else if (action.startsWith("s")) {
                    // SHIFT akcija
                    int nextState = Integer.parseInt(action.substring(1));
                    stack.push(symbolIdx); // stavljamo simbol (nije obavezno ali pomaze vizuelno)
                    stack.push(nextState); // stavljamo novo stanje
                    System.out.println("Shift: " + nextToken.m_text + ", prelazi u stanje I" + nextState);
                    nextToken = lexer.next_token();
                } 
                else if (action.startsWith("r")) {
                    // REDUCE akcija
                    int ruleIdx = Integer.parseInt(action.substring(1));
                    System.out.println("Reduce: primena smene (" + ruleIdx + ")");
                    
                    // Izbacujemo sa steka 2 * broj simbola desne strane (simbol + stanje)
                    for (int i = 0; i < 2 * ruleSize[ruleIdx]; i++) {
                        stack.pop();
                    }

                    int stateAfterPop = stack.peek();
                    int lhs = ruleLHS[ruleIdx];
                    int gotoState = gotoTable[stateAfterPop][lhs];
                    
                    stack.push(lhs); // Stavljamo neterminal
                    stack.push(gotoState); // Stavljamo stanje iz GOTO tabele
                }
            }
        } catch (Exception e) {
            System.err.println("Greska prilikom citanja tokena: " + e.getMessage());
            return false;
        }
    }


    private String getSymbolName(int index) {
        switch (index) {
            case sym.ID: return "identifikator (ID)";
            case sym.CONST: return "konstantu (CONST)";
            case sym.LPAREN: return "(";
            case sym.RPAREN: return ")";
            case sym.COMMA: return ",";
            case sym.ASSIGN: return "=";
            case sym.ARROW: return "=>";
            case sym.MULT: return "*";
            case sym.SEMICOLON: return ";";
            case sym.EOF: return "kraj fajla (EOF)";
            default: return "nepoznat simbol (" + index + ")";
        }
    }

    private void reportError(int state, Yytoken found) {
        StringBuilder expected = new StringBuilder();
        boolean first = true;

        // Prolazimo kroz actionTable za trenutno stanje i trazimo dozvoljene terminale
        for (int i = 0; i < actionTable[state].length; i++) {
            if (actionTable[state][i] != null) {
                if (!first) expected.append(", ");
                expected.append("'").append(getSymbolName(i)).append("'");
                first = false;
            }
        }

        System.err.println("--------------------------------------------------");
        System.err.println("SINTAKSNA GRESKA na liniji " + (found.m_line + 1));
        System.err.println("Pronadjeno: '" + found.m_text + "' [" + getSymbolName(found.m_index) + "]");
        System.err.println("Ocekivano: " + expected.toString());
        System.err.println("--------------------------------------------------");
    }
} 
