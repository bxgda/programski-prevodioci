// import sekcija
import java_cup.runtime.*;

%%
// sekcija deklaracija
%class MPLexer

%cup

%line
%column

%eofval{
	return new Symbol( sym.EOF );
%eofval}

%{
   public int getLine()
   {
      return yyline;
   }
%}


//stanja
%xstate KOMENTAR
//macros
slovo = [a-zA-Z]
cifra = [0-9]

%%
// rules section
"|*" { yybegin( KOMENTAR ); }
<KOMENTAR>"*|" { yybegin( YYINITIAL ); }
<KOMENTAR>.|\n  { ; }


[\t\r\n ]		{ ; }
"(" { return new Symbol( sym.LEFTPAR ); }
")" { return new Symbol( sym.RIGHTPAR ); }

":=" { return new Symbol(sym.ASSIGN); }
"=>" { return new Symbol(sym.ARROW); }
"<=" { return new Symbol(sym.LE); }
">=" { return new Symbol(sym.GE); }
"==" { return new Symbol(sym.EQ); }
"<>" { return new Symbol(sym.NE); }
"<" { return new Symbol(sym.LT); }
">" { return new Symbol(sym.GT); }


//separatori
; { return new Symbol( sym.SEMICOLON ); }
: { return new Symbol( sym.COLON ); }
, { return new Symbol( sym.COMMA ); }
\. { return new Symbol( sym.DOT ); }

//kljucne reci
"program"		{ return new Symbol( sym.PROGRAM );	}	
"begin"			{ return new Symbol( sym.BEGIN );	}
"end"			{ return new Symbol( sym.END );	}
"integer"		{ return new Symbol( sym.INTEGER );	}
"char"			{ return new Symbol( sym.CHAR );	}
"real"			{ return new Symbol( sym.REAL );	}
"boolean"		{ return new Symbol( sym.BOOLEAN );	}
"select"		{ return new Symbol( sym.SELECT );	}
"case"			{ return new Symbol( sym.CASE );	}
"or"			{ return new Symbol( sym.OR );	}
"and"			{ return new Symbol( sym.AND );	}
"true"			{ return new Symbol( sym.CONST );	}
"false"			{ return new Symbol( sym.CONST );	}

//identifikatori
{slovo}({slovo}|{cifra}|\$)*	{ return new Symbol( sym.ID ); }

//konstante
// real
{cifra}+\.{cifra}*(E[+-]?{cifra}+)? { return new Symbol(sym.CONST); }
\.{cifra}+(E[+-]?{cifra}+)?        { return new Symbol(sym.CONST); }

// int
(0[0-7]+)|(0x[0-9a-fA-F]+)|({cifra}+) { return new Symbol(sym.CONST); }

//char
'[^]' { return new Symbol(sym.CONST); }


//obrada greske
.		{ System.out.println( "ERROR: " + yytext() ); }

