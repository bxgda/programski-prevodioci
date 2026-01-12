import java_cup.runtime.Symbol;

%%

%class MPLexer
%cup
%line
%column
%type Symbol

%eofval{
    return new Symbol(sym.EOF);
%eofval}

%{
    KWTable kwTable = new KWTable();
    
    private Symbol token(int type) {
        return new Symbol(type, yyline, yycolumn);
    }
    
    private Symbol token(int type, Object value) {
        return new Symbol(type, yyline, yycolumn, value);
    }
%}

praznina = [\t\n\r ]
slovo = [a-zA-Z]
cifra = [0-9]
identifikator = {slovo}({slovo}|{cifra})*
int_hex = 0x[0-9a-fA-F]+
int_oct = 0[0-7]*
int_dec = [1-9][0-9]*
float_const = 0\.{cifra}+(E[+-]?{cifra}+)?
bool_const = true|false

%%

"//" .* { ; }
{praznina} { ; }

// KLJUCNE RECI (Stavljene pre identifikatora!)
"main"  { return token(sym.MAIN); }
"exit"  { return token(sym.EXIT); }
"int"   { return token(sym.INT); }
"float" { return token(sym.FLOAT); }
"bool"  { return token(sym.BOOL); }
"for"   { return token(sym.FOR); }
"in"    { return token(sym.IN); }
"apply" { return token(sym.APPLY); }

// OPERATORI
"+"  { return token(sym.PLUS); }
"-"  { return token(sym.MINUS); }
":=" { return token(sym.ASSIGN); }
";"  { return token(sym.SEMICOLON); }
","  { return token(sym.COMMA); }
"["  { return token(sym.LBRACKET); }
"]"  { return token(sym.RBRACKET); }

// IDENTIFIKATORI I KONSTANTE
{bool_const}    { return token(sym.CONST, yytext()); }
{float_const}   { return token(sym.CONST, yytext()); }
{int_hex}       { return token(sym.CONST, yytext()); }
{int_oct}       { return token(sym.CONST, yytext()); }
{int_dec}       { return token(sym.CONST, yytext()); }
{identifikator} { 
    int tip = kwTable.find(yytext());
    // Ako je CONST (za true/false), prosledi tekst kao vrednost, inace samo tip
    if (tip == sym.CONST) return token(tip, yytext());
    return token(tip, yytext()); 
}

. { System.err.println("Leksicka greska: " + yytext() + " na liniji " + (yyline+1)); }