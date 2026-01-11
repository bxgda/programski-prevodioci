import java_cup.runtime.Symbol;

%%

// OPCIJE -------------------------------------------------------------------------
%class MPLexer
%cup
%line 
%column
%debug 

//Ugradjeni java kod
%{ 
    KWTable kwTable = new KWTable();

    // Pomoćna funkcija za pravljenje Symbol objekta (bez vrednosti)
    private Symbol symbol(int type) {
        return new Symbol(type, yyline, yycolumn);
    }

    // Pomoćna funkcija za pravljenje Symbol objekta (sa vrednošću tokena)
    private Symbol symbol(int type, Object value) {
        return new Symbol(type, yyline, yycolumn, value);
    }
%}

%eofval{
    return symbol(sym.EOF);
%eofval}

%xstate KOMENTAR

// Makroi
slovo = [a-zA-Z]
cifra = [0-9]
hex_cifra = [0-9a-fA-F]
oct_cifra = [0-7]
identifikator = {slovo}({slovo}|{cifra})*

%%

// PRAVILA ------------------------------------------------------------------------

// Komentari
"--" { yybegin( KOMENTAR ); }
<KOMENTAR>~"--" { yybegin( YYINITIAL ); }

// Beline
[\t\n\r ] { ; }

// Operatori i separatori
"=>" { return symbol( sym.ARROW ); }
"="  { return symbol( sym.ASSIGN ); }
":"  { return symbol( sym.COLON ); }
";"  { return symbol( sym.SEMICOLON ); }
","  { return symbol( sym.COMMA ); }
"("  { return symbol( sym.LPAREN ); }
")"  { return symbol( sym.RPAREN ); }
"{"  { return symbol( sym.LBRACE ); }
"}"  { return symbol( sym.RBRACE ); }

// Kljucne reci i Identifikatori
{identifikator} { 
    int tip = kwTable.find(yytext());
    return symbol(tip, yytext()); 
}

// Konstante
{cifra}+\.{cifra}*(E[+-]?{cifra}+)? { return symbol( sym.CONST, yytext() ); }
\.{cifra}+(E[+-]?{cifra}+)?         { return symbol( sym.CONST, yytext() ); }
0x{hex_cifra}+                      { return symbol( sym.CONST, yytext() ); }
0{oct_cifra}+                       { return symbol( sym.CONST, yytext() ); }
(0|([1-9]{cifra}*))                 { return symbol( sym.CONST, yytext() ); }
'[^]'                               { return symbol( sym.CONST, yytext() ); }

// Obrada gresaka
. { System.out.println( "Leksicka greska: " + yytext() + " na liniji " + yyline ); }