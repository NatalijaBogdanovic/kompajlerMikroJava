package rs.ac.bg.etf.pp1;

import java_cup.runtime.Symbol;

%%

%{

	// ukljucivanje informacije o poziciji tokena
	private Symbol new_symbol(int type) {
		return new Symbol(type, yyline+1, yycolumn);
	}
	
	// ukljucivanje informacije o poziciji tokena
	private Symbol new_symbol(int type, Object value) {
		return new Symbol(type, yyline+1, yycolumn, value);
	}

%}

%cup
%line
%column

%xstate COMMENT

%eofval{
	return new_symbol(sym.EOF);
%eofval}

%%

" " 	{ }
"\b" 	{ }
"\t" 	{ }
"\r\n" 	{ }
"\f" 	{ }

"program" {return new_symbol(sym.PROG,yytext());}
"break" {return new_symbol(sym.BREAK,yytext());}
"class" {return new_symbol(sym.CLASS,yytext());}
"else" {return new_symbol(sym.ELSE,yytext());}
"const" {return new_symbol(sym.CONST,yytext());}
"if" {return new_symbol(sym.IF,yytext());}
"new" {return new_symbol(sym.NEW,yytext());}
"print" {return new_symbol(sym.PRINT,yytext());}
"read" {return new_symbol(sym.READ,yytext());}
"return" {return new_symbol(sym.RETURN,yytext());}
"void" {return new_symbol(sym.VOID,yytext());}
"extends" {return new_symbol(sym.EXTENDS,yytext());}
"continue" {return new_symbol(sym.CONTINUE,yytext());}
"union" {return new_symbol(sym.UNION,yytext());}
"do" {return new_symbol(sym.DO,yytext());}
"while" {return new_symbol(sym.WHILE,yytext());}
"map" {return new_symbol(sym.MAP,yytext());}
"interface" {return new_symbol(sym.INTERFACE,yytext());}

"+" 		{ return new_symbol(sym.PLUS, yytext()); }
"-" 		{ return new_symbol(sym.MINUS, yytext()); }
"*" 		{ return new_symbol(sym.MUL, yytext()); }
"/" 		{ return new_symbol(sym.DIV, yytext()); }
"%" 		{ return new_symbol(sym.PERCENT, yytext()); }
"==" 		{ return new_symbol(sym.EQUALS_EQUALS, yytext()); }
"!=" 		{ return new_symbol(sym.NOT_EQUALS, yytext()); }
">" 		{ return new_symbol(sym.GT, yytext()); }
">=" 		{ return new_symbol(sym.GTE, yytext()); }
"<" 		{ return new_symbol(sym.LT, yytext()); }
"<=" 		{ return new_symbol(sym.LTE, yytext()); }
"&&" 		{ return new_symbol(sym.AND, yytext()); }
"||" 		{ return new_symbol(sym.OR, yytext()); }
"=" 		{ return new_symbol(sym.EQUALS, yytext()); }
"++" 		{ return new_symbol(sym.INCREMENT, yytext()); }
"--" 		{ return new_symbol(sym.DECREMENT, yytext()); }
";" 		{ return new_symbol(sym.SEMICOLON, yytext()); }
":" 		{ return new_symbol(sym.COLON, yytext()); }
"," 		{ return new_symbol(sym.COMMA, yytext()); }
"." 		{ return new_symbol(sym.DOT, yytext()); }
"(" 		{ return new_symbol(sym.LEFT_ROUND_PARENTHESES, yytext()); }
")" 		{ return new_symbol(sym.RIGHT_ROUND_PARENTHESES, yytext()); }
"[" 		{ return new_symbol(sym.LEFT_SQUARE_PARENTHESES, yytext()); }
"]" 		{ return new_symbol(sym.RIGHT_SQUARE_PARENTHESES, yytext()); }
"{" 		{ return new_symbol(sym.LEFT_WIGGLY_PARENTHESES, yytext()); }
"}" 		{ return new_symbol(sym.RIGHT_WIGGLY_PARENTHESES, yytext()); }

"//" 		     { yybegin(COMMENT); }
<COMMENT> .      { yybegin(COMMENT); }
<COMMENT> "\r\n" { yybegin(YYINITIAL); }

[0-9]+  						{ return new_symbol(sym.NUM_CONST, new Integer (yytext())); }
("true"|"false") 				{return new_symbol(sym.BOOL_CONST, new Boolean(yytext())); }
"'"[ -~]"'"	 					{return new_symbol(sym.CHAR_CONST, new Character(yytext().charAt(1))); } 
[a-zA-Z][a-zA-Z0-9_]* 			{return new_symbol (sym.IDENT, yytext()); }



. { System.err.println("Leksicka greska ("+yytext()+") u liniji "+(yyline+1) + " na poziciji " + yycolumn); }






