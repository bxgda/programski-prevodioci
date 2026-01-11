// =======================
// import sekcija
// =======================

import java.util.*;
import java_cup.runtime.Symbol;

%%

// =======================
// sekcija opcija i deklaracija
// =======================

%class MPLexer
%cup
%line
%column
%debug

%eofval{
    return new Symbol(sym.EOF, yyline + 1, yycolumn + 1);
%eofval}

%{
// =======================
// dodatni clanovi generisane klase
// =======================

/** Symbol bez semantičke vrednosti */
private Symbol symTok(int type) {
    return new Symbol(type, yyline + 1, yycolumn + 1);
}

/** Symbol sa semantičkom vrednošću (npr. yytext()) */
private Symbol symTok(int type, Object value) {
    return new Symbol(type, yyline + 1, yycolumn + 1, value);
}

/** Obični tokeni: kao value stavljamo yytext() (možeš i bez value ako ne treba) */
Symbol token(int type) {
    return symTok(type, yytext());
}
%}

%x COMMENT


// =======================
// makroi
// =======================

slovo = [a-zA-Z_]
cifra = [0-9]

ID = {slovo}({slovo}|{cifra})*

INT_DEC   = {cifra}+
INT_OCT   = 0#o[0-7]+
INT_HEX   = 0#x[0-9A-Fa-f]+
INT_DEC2  = 0#d{cifra}+

INT_CONST = ({INT_OCT}|{INT_HEX}|{INT_DEC2}|{INT_DEC})

FLOAT_CONST = 0\.{cifra}*(E[+\-]?{cifra}+)?

CHAR_CONST = \'([^\\\']|\\.)\'

%%

// =======================
// whitespace
// =======================

[ \t\r\n]+      { /* ignore */ }

// =======================
// komentari
// =======================

"%"            { yybegin(COMMENT); }
<COMMENT>"%"   { yybegin(YYINITIAL); }
<COMMENT>.|\n  { /* ignore */ }

// =======================
// zagrade i blokovi
// =======================

"("             { return symTok(sym.LPAREN); }
")"             { return symTok(sym.RPAREN); }
"{"             { return symTok(sym.LBRACE); }
"}"             { return symTok(sym.RBRACE); }

// =======================
// separatori
// =======================

";"             { return symTok(sym.SEMICOLON); }
","             { return symTok(sym.COMMA); }

// =======================
// operatori
// =======================

"="             { return symTok(sym.ASSIGN); }

"||"            { return symTok(sym.OR); }
"&&"            { return symTok(sym.AND); }

"<="            { return symTok(sym.LE); }
">="            { return symTok(sym.GE); }
"=="            { return symTok(sym.EQ); }
"!="            { return symTok(sym.NE); }
"<"             { return symTok(sym.LT); }
">"             { return symTok(sym.GT); }

// =======================
// bool konstante
// =======================
// Ako želiš da parser dobije pravu boolean vrednost, može ovako:
// "true"  { return symTok(sym.CONST, Boolean.TRUE); }
// "false" { return symTok(sym.CONST, Boolean.FALSE); }
// Ali pošto si ranije sve vraćao kao CONST, ostavljam kompatibilno + yytext().

"true"          { return symTok(sym.CONST, yytext()); }
"false"         { return symTok(sym.CONST, yytext()); }

// =======================
// konstante
// =======================

{FLOAT_CONST}   { return symTok(sym.CONST, yytext()); }
{INT_CONST}     { return symTok(sym.CONST, yytext()); }
{CHAR_CONST}    { return symTok(sym.CONST, yytext()); }

// =======================
// kljucne reci i ID
// =======================

"main"  { return symTok(sym.MAIN); }
"int"   { return symTok(sym.INT); }
"char"  { return symTok(sym.CHAR); }
"float" { return symTok(sym.FLOAT); }
"bool"  { return symTok(sym.BOOL); }
"loop"  { return symTok(sym.LOOP); }
"redo"  { return symTok(sym.REDO); }

{ID}    { return symTok(sym.ID, yytext()); }

// =======================
// obrada gresaka
// =======================

. {
    System.out.println(
        "LEXICAL ERROR at line " + (yyline + 1) +
        ", column " + (yycolumn + 1) +
        " : " + yytext()
    );
    // U CUP svetu, tipično je bolje baciti exception nego samo ispisati,
    // da parser ne nastavi sa pogrešnim stream-om tokena.
    throw new Error("Lexical error at " + (yyline + 1) + ":" + (yycolumn + 1) + " -> " + yytext());
}

