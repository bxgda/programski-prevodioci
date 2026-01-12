import java_cup.runtime.*;

%%

%class MPLexer
%cup
%line
%column

%{
    // Metoda za kreiranje Symbol objekta koji CUP razume
    private Symbol symbol(int type) {
        return new Symbol(type, yyline, yycolumn);
    }
    private Symbol symbol(int type, Object value) {
        return new Symbol(type, yyline, yycolumn, value);
    }
%}

// Makroi
digit      = [0-9]
hexDigit   = [0-9a-fA-F]
letter     = [a-zA-Z]

// Konstante po specifikaciji
// 1. Integer: [[osnova]#]cifre. Ako je osnova prazna a # tu -> osnova 16. Ako nema # -> osnova 10.
intConst = (({digit}+)?#)?{hexDigit}+ | {digit}+

// 2. Real: cifre.[cifre][E[+-]cifre]
realConst = {digit}+ \. {digit}* ( [eE] [+-]? {digit}+ )?

// 3. Identifikatori
identifier = {letter}({letter}|{digit})*

// Komentari
comment = "/**" ([^*]|"*"[^/])* "*/"

%%

// Pravila
[\t\n\r ]+ { ; }
{comment}   { ; }

"main"      { return symbol(sym.MAIN); }
"int"       { return symbol(sym.INT); }
"real"      { return symbol(sym.REAL); }
"boolean"   { return symbol(sym.BOOLEAN); }
"if"        { return symbol(sym.IF); }
"else"      { return symbol(sym.ELSE); }
"elif"      { return symbol(sym.ELIF); }
"true"      { return symbol(sym.CONST, "true"); }
"false"     { return symbol(sym.CONST, "false"); }

"("         { return symbol(sym.LEFTPAR); }
")"         { return symbol(sym.RIGHTPAR); }
"{"         { return symbol(sym.BLOCK_BEGIN); }
"}"         { return symbol(sym.BLOCK_END); }
";"         { return symbol(sym.SEMICOLON); }
":"         { return symbol(sym.COLON); }
":="        { return symbol(sym.ASSIGN); }

// Relacioni operatori
"<"         { return symbol(sym.LESS); }
"<="        { return symbol(sym.LESSEQ); }
"=="        { return symbol(sym.EQ); }
"<>"        { return symbol(sym.NEQ); }
">"         { return symbol(sym.GREATER); }
">="        { return symbol(sym.GREATEREQ); }

{realConst}    { return symbol(sym.CONST, yytext()); }
{intConst}     { return symbol(sym.CONST, yytext()); }
{identifier}   { return symbol(sym.ID, yytext()); }

. { System.out.println("Leksička greška: " + yytext() + " na liniji " + (yyline+1)); }