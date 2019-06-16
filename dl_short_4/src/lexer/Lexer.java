package lexer;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.Hashtable;

//import parser.Parser;

public class Lexer {
	private static final char EOF_CHAR = (char)-1;
	private static int line = 1;
	private BufferedReader reader;
	private char peek;
	private Hashtable<String, Tag> keywords;

	public Lexer(File file) {
		try {
			this.reader = 
				new BufferedReader(
				new FileReader(file));
		} catch (Exception e) {
			e.printStackTrace();
		}
		this.peek = ' ';
		keywords = new Hashtable<String, Tag>();
		keywords.put("programa", Tag.PROGRAM);
		keywords.put("inicio", Tag.BEGIN);
		keywords.put("fim", Tag.END);
		keywords.put("escreva", Tag.WRITE);
		keywords.put("se", Tag.IF);
		keywords.put("verdadeiro", Tag.TRUE);
		keywords.put("falso", Tag.FALSE);
		keywords.put("inteiro", Tag.INT);
		keywords.put("real", Tag.REAL);
		keywords.put("booleano", Tag.BOOL);
	}

	public static int line() {
		return line;
	}

	private char nextChar() {
		if ( peek == '\n' ) line++;
		try {
			peek = (char)reader.read();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return peek;
	}
	
	//checa se pertence ao conjunto dos números romanos
	private static boolean isRomano(char r) {
		if( r == 'i' || r == 'I' || 
			r == 'v' || r == 'V' || 
			r == 'x' || r == 'X' || 
			r == 'l' || r == 'L' || 
			r == 'c' || r == 'C' || 
			r == 'd' || r == 'D' || 
			r == 'm' || r == 'M')
				return true;
		return false;
	}

	private static boolean isWhitespace(int c) {
		switch (c) {
		case ' ': case '\t': case '\n':
			return true;
		default: 
			return false;
		}
	}

	private static boolean isIdStart(int c) {
		return ( Character.isAlphabetic(c) || c == '_' );
	}

	private static boolean isIdPart(int c) {
		return (isIdStart(c) || Character.isDigit(c));
	}

	public Token nextToken() {
		while (isWhitespace(peek)) nextChar();
		switch(peek) {
		case '=':
			nextChar();
			return new Token(Tag.ASSIGN, "=");
		case '+':
			nextChar();
			return new Token(Tag.SUM, "+");
		case '-':
			nextChar();
			return new Token(Tag.SUB, "-");
		case '*':
			nextChar();
			return new Token(Tag.MUL,"*");
		case '|':
			nextChar();
			return new Token(Tag.OR, "|");
		case '<':
			nextChar();
			if ( peek == '=' ) {
				nextChar();
				return new Token(Tag.LE, "<=");
			}
			return new Token(Tag.LT, "<");
		case '>':
			nextChar();
			return new Token(Tag.GT, ">");
		case ';':
			nextChar();
			return new Token(Tag.SEMI, ";");
		case '.':
			nextChar();
			return new Token(Tag.DOT, ".");
		case '(':
			nextChar();
			return new Token(Tag.LPAREN, "(");
		case ')':
			nextChar();
			return new Token(Tag.RPAREN, ")");
		case EOF_CHAR:
			return new Token(Tag.EOF, "");
		default:
			if (Character.isDigit(peek)) {
				String num = "";
				if( peek == '0') {
					num += peek;
					nextChar();
					if( peek == 'r') {
						num += peek;
						nextChar();
						if( isRomano(peek)){
							switch(peek) {
							case 'i' | 'I':
								num += peek;
								nextChar();
								if(peek == 'i' || peek == 'I') {
									num += peek;
									nextChar();
									if(peek == 'i' || peek == 'I' ) {	//número 3 -> III
										num += peek;
										nextChar();
										if(!isWhitespace(peek)) {
											num = "3";
											return new Token(Tag.LIT_INT, num);
										}
										else {
											num += peek;
											error("Erro Léxico: " + num + " não é um literal romano válido");
										}
									}
									else if(isWhitespace(peek)) {	//número 2 -> II
										num = "2";
										return new Token(Tag.LIT_INT_ROM, num);
									}
									else {
										num += peek;
										error("Erro Léxico: " + num + " não é um literal romano válido");
									}
								}
								else if ( peek == 'v' || peek == 'V') {
									num += peek;
									nextChar();
									if(isWhitespace(peek)) {
										num = "4";
										return new Token(Tag.LIT_INT_ROM, num);
									}
									else {
										num += peek;
										error("Erro Léxico: " + num + " não é um literal romano válido");
									}
								}
								else if(isWhitespace(peek)) {
									num = "1";
									return new Token(Tag.LIT_INT_ROM, num);
								}
								else
									error("Erro Léxico: " + num + " não é um literal romano válido");
							}
							//if(isRomano(peek))
								//error("O caractere" + peek + "não é um número romano!");
						}	//se pickar um romano entra no laço
					}
				}
				do {
					num += peek;
					nextChar();
				} while( Character.isDigit(peek) );
				if ( peek != '.' ) 
					return new Token(Tag.LIT_INT, num);
				do {
					num += peek;
					nextChar();
				} while ( Character.isDigit(peek) );
				return new Token(Tag.LIT_REAL, num);
			} else if ( isIdStart(peek)  ) {
				String id = "";
				do {
					id += peek;
					nextChar();
				} while ( isIdPart(peek) );
				if ( keywords.containsKey(id) )
					return new Token(keywords.get(id), id);
				return new Token(Tag.ID, id);
			}
		}
		String unk = String.valueOf(peek);
		nextChar();
		return new Token(Tag.UNK, unk);
	}

	private void error(String s) {
		System.err.println("linha " 
				+ Lexer.line() 
				+ ": " + s);
		System.exit(0);
	}
}