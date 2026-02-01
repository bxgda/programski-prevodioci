// IMPORT SEKCIJA ----------------------------------------------------------------------------------------------------

%%

// OPCIJE I DEKLARACIJE -------------------------------------------------------------------------------------------------------------
%class MPLexer
%function next_token // ime funkcije za dobijanje sledeceg tokena
%line 
%column
%debug 
%type Yytoken 


%eofval{ // Kad dodjes do kraja fajla nemoj da puknes nego vrati EOF
return new Yytoken( sym.EOF, null, yyline, yycolumn);
%eofval}

//Ugradjeni java kod
%{ 
KWTable kwTable = new KWTable(); // Pravimo instancu nase tabele da bismo mogli da je koristimo za prepoznavanje kljucnih reci

Yytoken getKW(){ // funkcija koju cemo koristitu da u okviru KWTable pokusamo da nadjemo kljucnu rec i da rezultat Wrapujemo u Yytoken
    return new Yytoken( kwTable.find(yytext()), yytext(), yyline, yycolumn );
}
%}

// Stanje komentar ce nam pomoci da kazemo da dok smo u ovom stanju ne radimo nista odnosno ignorisemo sve sto je u ovom stanju (Lekser inace radi u stanju YYINITIAL)
%xstate KOMENTAR

// Makroi - definicije regexa 
slovo = [a-zA-Z]
cifra = [0-9]
hex_cifra = [0-9a-fA-F]
oct_cifra = [0-7]

identifikator = {slovo}({slovo}|{cifra})* // Ovde kazemod a promenljiva mora da ima ime koje pocinje sa slovom a nakon toga  slede ili slova ili cifre

%%

// PRAVILA ---------------------------------------------------------------------------------------------------------------------------------------

// 1. Komentari -- ... -- 
"--" { yybegin( KOMENTAR ); }  // Kazemo kad naidje -- onda zapocni stanje KOMENTAR a kad posle opet naijde -- zapocne stanje YYINITIAL
<KOMENTAR>~"--" { yybegin( YYINITIAL ); }  // Koristimo ~"--" sto znaci: gutaj sve (ukljucujuci \r i \n) dok ne vidis string "--"

// 2. Beline
[\t\n\r ] { ; } // kazemo kad nadijes na nesto od ovih zankova tab, novi red, space itd. onda tu stavi ; a to ; znaci prazna naredba odnosno ne radi nista 

// 3. Operatori / Svi operatori da kad naidje na neki vrati Wraper Yytoken tog operatora (najbolje je operatore poredjati po broju karaktera pa je tako ovde => ispred = )
"=>" { return new Yytoken( sym.ARROW, yytext(), yyline, yycolumn ); }
"="  { return new Yytoken( sym.ASSIGN, yytext(), yyline, yycolumn ); }

":" { return new Yytoken( sym.COLON, yytext(), yyline, yycolumn ); }
";" { return new Yytoken( sym.SEMICOLON, yytext(), yyline, yycolumn ); }
"," { return new Yytoken( sym.COMMA, yytext(), yyline, yycolumn ); }
"(" { return new Yytoken( sym.LPAREN, yytext(), yyline, yycolumn ); }
")" { return new Yytoken( sym.RPAREN, yytext(), yyline, yycolumn ); }
"{" { return new Yytoken( sym.LBRACE, yytext(), yyline, yycolumn ); }
"}" { return new Yytoken( sym.RBRACE, yytext(), yyline, yycolumn ); }
"*" { return new Yytoken( sym.MULT, yytext(), yyline, yycolumn ); }
// 4. Kljucne reci i Identifikatori
{identifikator} { return getKW(); } // Ako tekst odnosno token koji je uhvacen podrzava pravila identifikatora/imena promenljivih moramo da proverimo da li je kljucna rec ili promenljiva
                                    //Npr. ako dam ime promenljive nesto = 5 on mora da zna da razlikuje to nesto od int, char, float itd. 

// 5. Konstante

// 5a. Float konstante
{cifra}+\.{cifra}*(E[+-]?{cifra}+)? { return new Yytoken( sym.CONST, yytext(), yyline, yycolumn ); }
\.{cifra}+(E[+-]?{cifra}+)? { return new Yytoken( sym.CONST, yytext(), yyline, yycolumn ); }


// 5b. Integer konstante
0x{hex_cifra}+ { return new Yytoken( sym.CONST, yytext(), yyline, yycolumn ); }
0{oct_cifra}+  { return new Yytoken( sym.CONST, yytext(), yyline, yycolumn ); }
(0|([1-9]{cifra}*)) { return new Yytoken( sym.CONST, yytext(), yyline, yycolumn ); }

// 5c. Char konstante
'[^]' { return new Yytoken( sym.CONST, yytext(), yyline, yycolumn ); } // Kaze mora da bude jednan znak izmedju ' ' ne sme da stoje prazne

// obrada gresaka
. { if (yytext() != null && yytext().length() > 0) System.out.println( "ERROR: " + yytext() ); } // Kaze ako lekser nadje neki znak koji nismo obuhvatili u nasoj specifikaciji
                                                                                                // vrati error i onda znak koji je napravio error