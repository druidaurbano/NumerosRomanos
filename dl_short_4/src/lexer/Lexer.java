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
				int somaromano = 0;
				char peekanterior = ' ';
				if( peek == '0') {
					num += peek;
					nextChar();
					if( peek == 'r') {
						num += peek;
						nextChar();
						do{
							switch(peek) {
							case 'I': case 'i':
								int counti = 1;
								peekanterior = peek;
								while(peek == 'I' || peek == 'i') {
									if(counti <= 3) {
										num += peek;
										somaromano += 1;
										nextChar();
										counti++;
									}
									else {
										num += peek;
										error("Erro Léxico: " + num + " não é um literal romano válido");
									}
								}
							break;
							case 'V': case 'v':
								num += peek;
								if(peekanterior == 'I' || peekanterior == 'i') {
									somaromano += 3;
									nextChar();
								}
								else {
									somaromano += 5;
									nextChar();
								}
							break;
							case 'X': case 'x':
								if(peekanterior == 'I' || peekanterior == 'i') {
									num += peek;
									somaromano += 8;
									nextChar();
								}
								else {
									int countx = 1;
									peekanterior = peek;
									while(peek == 'X' || peek == 'x') {
										if(countx <= 3) {
											num += peek;
											somaromano += 10;
											nextChar();
											countx++;
										}
										else {
											num += peek;
											error("Erro Léxico: " + num + " não é um literal romano válido");
										}
									}
								break;
								}
							break;
							case 'L': case 'l':
								num += peek;
								if(peekanterior == 'X' || peekanterior == 'x') {
									somaromano += 30;
									nextChar();
								}
								else {
									somaromano += 50;
									nextChar();
								}
							break;
							case 'C': case 'c':
								if(peekanterior == 'X' || peekanterior == 'x') {
									num += peek;
									somaromano += 80;
									nextChar();
								}
								else {
									int countx = 1;
									peekanterior = peek;
									while(peek == 'C' || peek == 'c') {
										if(countx <= 3) {
											num += peek;
											somaromano += 100;
											nextChar();
											countx++;
										}
										else {
											num += peek;
											error("Erro Léxico: " + num + " não é um literal romano válido");
										}
									}
								break;
								}
							break;
							case 'D': case 'd':
								num += peek;
								if(peekanterior == 'C' || peekanterior == 'c') {
									somaromano += 30;
									nextChar();
								}
								else {
									somaromano += 50;
									nextChar();
								}
							break;
							case 'M': case 'm':
								if(peekanterior == 'C' || peekanterior == 'c') {
									num += peek;
									somaromano += 80;
									nextChar();
								}
								else {
									int countx = 1;
									peekanterior = peek;
									while(peek == 'M' || peek == 'm') {
										if(countx <= 3) {
											num += peek;
											somaromano += 1000;
											nextChar();
											countx++;
										}
										else {
											num += peek;
											error("Erro Léxico: " + num + " não é um literal romano válido");
										}
									}
								break;
								}
							break;
							}
						}while(isRomano(peek));	//se pickar um romano entra no laço
						if(!Character.isAlphabetic(peek) || !Character.isDigit(peek))
							num = Integer.toString(somaromano);
						else {
							num += peek;
							error("Erro Léxico: " + num + " não é um literal romano válido");
						}
						return new Token(Tag.LIT_INT_ROM, num);
					}
					num += peek;
					error("Erro Léxico: '" + num + "' não é um número romano!");
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





