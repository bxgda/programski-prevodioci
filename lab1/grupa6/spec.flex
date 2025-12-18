// IMPORT SEKCIJA:
import java.io.*;
%%

// OPCIJE I DEKLARACIJE:
%class MPLexer
%function next_token
%type Yytoken
%line
%column

%eofval{
    return new Yytoken(sym.EOF, null, yyline, yycolumn);
%eofval}

// DODATNI ČLANOVI:
%{
    KWTable kwTable = new KWTable();
%}

// STANJA:
%x KOMENTAR

// MAKROI:
slovo   = [a-zA-Z]
cifra   = [0-9]
id      = {slovo}({slovo}|{cifra}|\$)*

/* integer konstante */
int10   = {cifra}+
int8    = 0[0-7]+
int16   = 0x[0-9a-fA-F]+

/* real konstante */
real1   = {cifra}+\.{cifra}*([eE][+-]?{cifra}+)?
real2   = \.{cifra}+([eE][+-]?{cifra}+)?

/* char konstanta */
charc   = \'[^\']\'

%%

// KOMENTARI:
"**"            { yybegin(KOMENTAR); }
<KOMENTAR>"**"  { yybegin(YYINITIAL); }
<KOMENTAR>.|\n  { /* ignorisi */ }

// WHITESPACE:
[ \t\r\n]+      { /* preskoci */ }

// OPERATORI:
":="            { return new Yytoken(sym.ASSIGN, yytext(), yyline, yycolumn); }

"<="            { return new Yytoken(sym.LE, yytext(), yyline, yycolumn); }
">="            { return new Yytoken(sym.GE, yytext(), yyline, yycolumn); }
"=="            { return new Yytoken(sym.EQ, yytext(), yyline, yycolumn); }
"<>"            { return new Yytoken(sym.NE, yytext(), yyline, yycolumn); }
"<"             { return new Yytoken(sym.LT, yytext(), yyline, yycolumn); }
">"             { return new Yytoken(sym.GT, yytext(), yyline, yycolumn); }

// SEPARATORI:
","             { return new Yytoken(sym.COMMA, yytext(), yyline, yycolumn); }
":"             { return new Yytoken(sym.COLON, yytext(), yyline, yycolumn); }
";"             { return new Yytoken(sym.SEMICOLON, yytext(), yyline, yycolumn); }
"("             { return new Yytoken(sym.LEFTPAR, yytext(), yyline, yycolumn); }
")"             { return new Yytoken(sym.RIGHTPAR, yytext(), yyline, yycolumn); }
"."             { return new Yytoken(sym.DOT, yytext(), yyline, yycolumn); }

// KONSTANTE:
{int16}         { return new Yytoken(sym.CONST, yytext(), Integer.decode(yytext()), yyline, yycolumn); }
{int8}          { return new Yytoken(sym.CONST, yytext(), Integer.decode(yytext()), yyline, yycolumn); }
{int10}         { return new Yytoken(sym.CONST, yytext(), Integer.parseInt(yytext()), yyline, yycolumn); }

{real1}         { return new Yytoken(sym.CONST, yytext(), Double.parseDouble(yytext()), yyline, yycolumn); }
{real2}         { return new Yytoken(sym.CONST, yytext(), Double.parseDouble(yytext()), yyline, yycolumn); }

{charc}         { return new Yytoken(sym.CONST, yytext(), yytext().charAt(1), yyline, yycolumn); }

// IDENTIFIKATORI I KLJUČNE REČI:
{id} {
    int token = kwTable.find(yytext());
    Object val = null;

    if (token == sym.TRUE)  val = Boolean.TRUE;
    if (token == sym.FALSE) val = Boolean.FALSE;

    return new Yytoken(token, yytext(), val, yyline, yycolumn);
}

// GREŠKE:
. {
    System.out.println("LEXICAL ERROR at line " + yyline +
                       ", column " + yycolumn +
                       " : " + yytext());
}
