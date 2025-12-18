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
Yytoken getKW()
{
	return new Yytoken( kwTable.find( yytext() ),
	yytext(), yyline, yycolumn );
}
%}

// --- MAKROI ---

	// OSNOVNI
praznina = [\t\n\r ]
slovo = [a-zA-Z]
cifra = [0-9]
hex_cifra = [0-9a-fA-F]
oct_cifra = [0-7]

	// 1. identifikator: niz slova i cifara, prvi znak ne moze da bude cifra
identifikator = {slovo}({slovo}|{cifra})*

	// 2. int konstante
	// hex: pocinje sa 0x
int_hex = 0x{hex_cifra}+
	// Octal: pocinje sa 0
int_oct = 0{oct_cifra}*
	// Decimal: ne pocinje nulom
int_dec = [1-9]{cifra}*

	// 3. Float konstante: 0. <niz_cifara>[E[±]<niz_cifara>]
float_const = 0\.{cifra}+(E[+-]?{cifra}+)?

	// 4. Boolean konstante
bool_const = true|false


%%

// --- PRAVILA ---

	// 1. Komentari (// do kraja reda)
\/\/.* { ; }

	// 2. Praznine
{praznina} { ; }

	// 3. Operatori i Separatori
\+ { return new Yytoken( sym.PLUS, yytext(), yyline, yycolumn ); }
\- { return new Yytoken( sym.MINUS, yytext(), yyline, yycolumn ); }
:= { return new Yytoken( sym.ASSIGN, yytext(), yyline, yycolumn ); }
;  { return new Yytoken( sym.SEMICOLON, yytext(), yyline, yycolumn ); }
,  { return new Yytoken( sym.COMMA, yytext(), yyline, yycolumn ); }
\[ { return new Yytoken( sym.LBRACKET, yytext(), yyline, yycolumn ); }
\] { return new Yytoken( sym.RBRACKET, yytext(), yyline, yycolumn ); }

	// 4. Konstante 
	// prvo hex i float da se ne bi prepoznao octal odmah
{float_const} { return new Yytoken( sym.CONST, yytext(), yyline, yycolumn ); }
{int_hex}     { return new Yytoken( sym.CONST, yytext(), yyline, yycolumn ); }
{int_oct}     { return new Yytoken( sym.CONST, yytext(), yyline, yycolumn ); }
{int_dec}     { return new Yytoken( sym.CONST, yytext(), yyline, yycolumn ); }
{bool_const}  { return new Yytoken( sym.CONST, yytext(), yyline, yycolumn ); }

	// 5. Kljucne reci i identifikatori
{identifikator} { return getKW(); }

	// 6. Greske
. { if (yytext() != null && yytext().length() > 0) System.out.println( "ERROR: " + yytext() ); }