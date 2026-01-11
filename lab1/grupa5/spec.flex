// import sekcija

%%

// sekcija opcija i deklaracija
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
//dodatni clanovi generisane klase
KWTable kwTable = new KWTable();
// Funkcija za prepoznavanje KW ili ID
Yytoken getKW()
{
	return new Yytoken( kwTable.find( yytext() ),
	yytext(), yyline, yycolumn );
}
%}

//stanja
%xstate KOMENTAR
//makroi
slovo = [a-zA-Z]
cifra = [0-9]
donjacrta = [_]
//ID ne krece cifrom
ID = ({slovo}|{donjacrta})({slovo}|{cifra}|{donjacrta})*
//konstante
INT8 = 0oct[0-7]+
INT16 = 0hex[0-9a-fA-F]+
INT10 = (0dec)?{cifra}+
FLOAT = ({cifra}+\.{cifra}*)([Ee][+-]?{cifra}+)?
CHAR = "'" [^\n\r] "'"
%%

// pravila
// komentari
"%%" { yybegin( KOMENTAR ); }
<KOMENTAR>[^%]+ { ; }
<KOMENTAR>"%" { ; }
<KOMENTAR>"%%" { yybegin( YYINITIAL ); }

// whitespace
[\t\n\r ] { ; }

// konstante
{FLOAT}|{CHAR}|{INT8}|{INT16}|{INT10} { return new Yytoken( sym.CONST, yytext(), yyline, yycolumn ); }

// ključne Reči i identifikatori
{ID} { return getKW(); }

// 5. Operatori i Separatori
\+ { return new Yytoken( sym.PLUS,yytext(), yyline, yycolumn ); }
\- { return new Yytoken( sym.MINUS,yytext(), yyline, yycolumn ); }
\( { return new Yytoken( sym.LEFTPAR, yytext(), yyline, yycolumn ); }
\) { return new Yytoken( sym.RIGHTPAR, yytext(), yyline, yycolumn ); }
\{ { return new Yytoken( sym.LEFTBRACE, yytext(), yyline, yycolumn ); }
\} { return new Yytoken( sym.RIGHTBRACE, yytext(), yyline, yycolumn ); }
; { return new Yytoken( sym.SEMICOLON, yytext(), yyline, yycolumn ); }
: { return new Yytoken( sym.COLON, yytext(), yyline, yycolumn ); }
, { return new Yytoken( sym.COMMA, yytext(), yyline, yycolumn ); }
= { return new Yytoken( sym.ASSIGN, yytext(), yyline, yycolumn ); }

// 6. Obrada grešaka (Ispravljena sintaksička greška u println pozivu)
. { 
    if (yytext() != null && yytext().length() > 0) 
        System.out.println( "ERROR: " + yytext() + " line: " + (yyline+1) + " column: " + (yycolumn+1) ); 
}