public class Yytoken {

    // tip tokena
    public int type;

    // Tekst tokena (yytext iz JFlex-a)
    public String lexeme;

    // vrednost tokena 
    public Object value;

    // linija i kolona (korisno za debug i greske) 
    public int line;
    public int column;

    
    //konstruktori:
    
    // token bez dodatne vrednosti (npr. +, while, begin, ;)
    public Yytoken(int type, String lexeme, int line, int column) {
        this.type = type;
        this.lexeme = lexeme;
        this.line = line;
        this.column = column;
        this.value = null;
    }

    // token sa vrednoscu (ID, CONST)
    public Yytoken(int type, String lexeme, Object value, int line, int column) {
        this.type = type;
        this.lexeme = lexeme;
        this.value = value;
        this.line = line;
        this.column = column;
    }

    @Override
    public String toString() {
        return "TOKEN [type=" + type +
               ", lexeme=\"" + lexeme + "\"" +
               (value != null ? ", value=" + value : "") +
               ", line=" + line +
               ", column=" + column + "]";
    }
}
