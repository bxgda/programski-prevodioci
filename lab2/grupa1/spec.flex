// IMPORT SEKCIJA ----------------------------------------------------------------------------------------------------

%%

// OPCIJE I DEKLARACIJE -------------------------------------------------------------------------------------------------------------
%class MPLexer
%function next_token
%line 
%column
%debug 
%type Yytoken 

%eofval{
return new Yytoken( sym.EOF, null, yyline, yycolumn);
%eofval}

%{
KWTable kwTable = new KWTable();

Yytoken getKW(){
    return new Yytoken( kwTable.find(yytext()), yytext(), yyline, yycolumn );
}
%}

// Makroi
slovo = [a-zA-Z]
cifra = [0-9]
hex_cifra = [0-9a-fA-F]
oct_cifra = [0-7]

identifikator = {slovo}({slovo}|{cifra})*

// konstante
int_hex = 0x{hex_cifra}+
int_oct = 0{oct_cifra}*
int_dec = [1-9]{cifra}*|0
float_const = 0\.{cifra}+(E[+-]?{cifra}+)?

%%

// PRAVILA ---------------------------------------------------------------------------------------------------------------------------------------

// 1. Komentari // ... \n
"//".* { ; }

// 2. Beline
[\t\n\r ] { ; }

// 3. Operatori i separatori
"+" { return new Yytoken( sym.PLUS, yytext(), yyline, yycolumn ); }
"," { return new Yytoken( sym.COMMA, yytext(), yyline, yycolumn ); }
"[" { return new Yytoken( sym.LBRACKET, yytext(), yyline, yycolumn ); }
"]" { return new Yytoken( sym.RBRACKET, yytext(), yyline, yycolumn ); }

// 4. Konstante
{float_const} { return new Yytoken( sym.CONST, yytext(), yyline, yycolumn ); }
{int_hex}     { return new Yytoken( sym.CONST, yytext(), yyline, yycolumn ); }
{int_oct}     { return new Yytoken( sym.CONST, yytext(), yyline, yycolumn ); }
{int_dec}     { return new Yytoken( sym.CONST, yytext(), yyline, yycolumn ); }

// 5. Kljucne reci i identifikatori
{identifikator} { return getKW(); }

// 6. Greske
. { if (yytext() != null && yytext().length() > 0) System.out.println( "ERROR: " + yytext() ); }