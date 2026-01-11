import java.util.Stack;

public class SyntaxAnalyzer {
    private MPLexer lexer;
    
    private static final int ERR = 0;
    private static final int POP = -1;
    private static final int ACC = -2;
    
    private static final int CS = 20;
    private static final int WSL = 21;
    private static final int W1 = 22;
    private static final int WS = 23;
    private static final int S = 24;
    private static final int V1 = 25;
    
    private int[][] M = new int[26][20];
    private int[][] pravila = new int [10][];
    
    SyntaxAnalyzer(MPLexer lexer){
        this.lexer = lexer;
        initPravila();
        initTable();
    }
    
    private void initPravila(){
        pravila[1] = new int[]{sym.RIGHTBRACE, WSL, sym.LEFTBRACE, sym.RIGHTPAR, sym.ID, sym.LEFTPAR, sym.CASE};
        pravila[2] = new int[]{W1, WS};
        pravila[3] = new int[]{W1, WS};
        pravila[4] = new int[]{};
        pravila[5] = new int[]{S, sym.COLON, sym.CONST, sym.WHEN};
        pravila[6] = new int[]{CS};
        pravila[7] = new int[]{V1, sym.ASSIGN, sym.ID};
        pravila[8] = new int[]{sym.SEMICOLON, sym.ID};
        pravila[9] = new int[]{sym.SEMICOLON, sym.CONST};
    }
    
    private void initTable(){
        int[] terminals = {sym.CASE, sym.LEFTPAR, sym.RIGHTPAR, sym.LEFTBRACE, sym.RIGHTBRACE, sym.WHEN, sym.CONST, sym.COLON, sym.ID, sym.ASSIGN, sym.SEMICOLON};
        
        for(int t: terminals)
            M[t][t] = POP;
        M[sym.EOF][sym.EOF] = ACC;
        
        M[CS][sym.CASE] = 1;
        M[WSL][sym.WHEN] = 2;
        M[W1][sym.WHEN] = 3;
        M[W1][sym.RIGHTBRACE] = 4;
        M[WS][sym.WHEN] = 5;
        M[S][sym.CASE] = 6;
        M[S][sym.ID] = 7;
        M[V1][sym.ID] = 8;
        M[V1][sym.CONST] = 9;
    }
    
    public boolean analiza(){
        Stack<Integer> stack = new Stack<>();
        stack.push(sym.EOF);
        stack.push(CS);
        
        boolean prepoznat = false;
        boolean greska = false;
        
        try{
            Yytoken currentToken = lexer.next_token();
            do 
            {
                int top = stack.peek();
                int next = (currentToken == null) ? sym.EOF : currentToken.m_index;
                
                int action = M[top][next];
                
                if(action == POP){
                    stack.pop();
                    currentToken = lexer.next_token();
                }
                else if(action>0){
                    int k = action;
                    stack.pop();
                    for(int i = 0; i<pravila[k].length; i++)
                        stack.push(pravila[k][i]);
                    System.out.println("Primenjeno pravilo "+k);
                }
                else if(action == ACC){
                    prepoznat = true;
                    break;
                }
                else if(action == ERR){
                    greska = true;
                    System.err.println("Sintaksna greska linija: " + currentToken.m_line + 1 + ", pozicija: " + currentToken.m_index);
                    break;
                }
            } while(!(prepoznat || greska));
        }
        catch(Exception e){
            e.printStackTrace();
            return false;
        }

        return prepoznat;
    }
}
