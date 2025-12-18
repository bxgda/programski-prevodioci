public class sym {
    
    public static final int EOF = 0;
    
    // identifikatori i konstante
    public static final int ID = 1;
    public static final int CONST = 2;
    
    // kljucne reci
    public static final int PROGRAM = 3;
    public static final int BEGIN = 4;
    public static final int END = 5;
    public static final int INTEGER = 6;
    public static final int CHAR = 7;
    public static final int REAL = 8;
    public static final int BOOLEAN = 9;
    public static final int WHILE = 10;
    public static final int ELSE = 11;
    public static final int OR = 12;
    public static final int AND = 13;
    public static final int TRUE = 14;
    public static final int FALSE = 15;
    
    // operatori
    public static final int ASSIGN = 16; // :=
    public static final int LT = 17; // <
    public static final int LE = 18; // <=
    public static final int EQ = 19; // ==
    public static final int NE = 20; // <>
    public static final int GT = 21; // >
    public static final int GE = 22; // >=
    
    // separatori
    public static final int COMMA = 23;     // ,
    public static final int COLON = 24;     // :
    public static final int SEMICOLON = 25; // ;
    public static final int LEFTPAR = 26;   // (
    public static final int RIGHTPAR = 27;  // )
    public static final int DOT = 28;       // .
    
}
