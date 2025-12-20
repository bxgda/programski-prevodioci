// =======================
// import sekcija
// =======================

import java.util.*;


%%

// =======================
// sekcija opcija i deklaracija
// =======================

%class MPLexer
%function next_token
%type Yytoken
%line
%column
%debug

%eofval{
    return new Yytoken(sym.EOF, "EOF", yyline + 1, yycolumn + 1);
%eofval}

%{
// =======================
// dodatni clanovi generisane klase
// =======================

KWTable kwTable = new KWTable();

Yytoken getKW() {
    return new Yytoken(
        kwTable.find(yytext()),
        yytext(),
        yyline + 1,
        yycolumn + 1
    );
}

Yytoken token(int type) {
    return new Yytoken(type, yytext(), yyline + 1, yycolumn + 1);
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

[ \t\r\n]+      { ; }

// =======================
// komentari
// =======================

"%"            { yybegin(COMMENT); }
<COMMENT>"%"   { yybegin(YYINITIAL); }
<COMMENT>.|\n  { /* ignore */ }

// =======================
// zagrade i blokovi
// =======================

"("             { return token(sym.LPAREN); }
")"             { return token(sym.RPAREN); }
"{"             { return token(sym.LBRACE); }
"}"             { return token(sym.RBRACE); }

// =======================
// separatori
// =======================

";"             { return token(sym.SEMICOLON); }
","             { return token(sym.COMMA); }

// =======================
// operatori
// =======================

"="             { return token(sym.ASSIGN); }

"||"            { return token(sym.OR); }
"&&"            { return token(sym.AND); }

"<="            { return token(sym.LE); }
">="            { return token(sym.GE); }
"=="            { return token(sym.EQ); }
"!="            { return token(sym.NE); }
"<"             { return token(sym.LT); }
">"             { return token(sym.GT); }

// =======================
// bool konstante
// =======================

"true"          { return token(sym.CONST); }
"false"         { return token(sym.CONST); }

// =======================
// konstante
// =======================

{FLOAT_CONST}   { return token(sym.CONST); }
{INT_CONST}     { return token(sym.CONST); }
{CHAR_CONST}    { return token(sym.CONST); }

// =======================
// kljucne reci i ID
// =======================

{ID}            { return getKW(); }

// =======================
// obrada gresaka
// =======================

. {
    System.out.println(
        "LEXICAL ERROR at line " + (yyline + 1) +
        ", column " + (yycolumn + 1) +
        " : " + yytext()
    );
}
