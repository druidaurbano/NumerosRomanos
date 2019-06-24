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
						while(Character.isAlphabetic(peek)){
							switch(peek) {
							
							case 'I': case 'i':
								int counti = 0;
								peekanterior = peek;
								
								while(peek == 'I' || peek == 'i') {
									if(counti < 3) {	//if serve apenas para verificar a quantidade m�xima de I, ou seja 3
										counti++;
										num += peek;
										somaromano += 1;
										nextChar();
									}
									else {
										num += peek;
										error("Erro L�xico: " + num + " n�o � um literal romano v�lido, s�o permitidos apenas tr�s letras 'I' !");
									}
								}
								
								if((peek == 'V' || peek =='v' ||
									peek == 'X' || peek == 'x') && counti < 2)	//if que verifica se o pr�ximo caractere � v�lido
									break;
								else if (Character.isAlphabetic(peek) || Character.isDigit(peek)) {
									num += peek;
									error("Erro l�xico: " + num + " n�o � um n�mero romano v�lido!");
								} else
									break;
							break;
							
							case 'V': case 'v':
								num += peek;
								if(peekanterior == 'I' || peekanterior == 'i') { 	//se for o n�mero IV =4
									somaromano += 3;
									nextChar();
									if (Character.isAlphabetic(peek) || Character.isDigit(peek)) {		//se houver algo depois do v d� erro
										num += peek;
										error("Erro l�xico: " + num + " n�o � um n�mero romano v�lido!");
									}
									else	//se n�o ele retorna tudo certinho
										break;
								}
								else {	//se for n�mero 5 entra aqui
									somaromano += 5;
									nextChar();
									if(peek == 'I' || peek == 'i')	//se o pr�ximo for menor que 5 ele sai do la�o switch
										break;
									else if (Character.isAlphabetic(peek) || Character.isDigit(peek)) {	//se houver algo maior que 5 = v ele d� erro
										num += peek;
										error("Erro l�xico: " + num + " n�o � um n�mero romano v�lido!");
									} else	// se for apenas o n�mero 5 = v sai do la�o
										break;
								}
							
							case 'X': case 'x':
								if(peekanterior == 'I' || peekanterior == 'i') {	//verifica se � um n�mero 9
									num += peek;
									somaromano += 8;
									nextChar();
									if (Character.isAlphabetic(peek) || Character.isDigit(peek)) {		//se houver algum d�gito ou letra depois do x d� erro
										num += peek;
										error("Erro l�xico: " + num + " n�o � um n�mero romano v�lido!");
									}
									else	//se tiver tudo certinho d� break
										break;
								}
								else {		//se x n�o anteceder i entra aqui para formar valores m�ltiplos de 10
									int countx = 0;
									peekanterior = peek;
									while(peek == 'X' || peek == 'x') {
										if(countx < 3) {
											countx++;
											num += peek;
											somaromano += 10;
											nextChar();						
										}
										else {
											num += peek;
											error("Erro L�xico: " + num + " n�o � um literal romano v�lido");
										}
									}
									if((peek == 'I' || peek == 'i' ||
										peek == 'V' || peek == 'v')
										||
										((peek == 'L' || peek == 'l' ||
										 peek == 'C' || peek == 'c') && countx < 2))
										break;
									else if (Character.isAlphabetic(peek) || Character.isDigit(peek)) {
										num += peek;
										error("Erro l�xico: " + num + " n�o � um n�mero romano v�lido!");
									} else
										break;
								}
								
							case 'L': case 'l':	//finalizado
								num += peek;
								if(peekanterior == 'X' || peekanterior == 'x') {
									somaromano += 30;
									nextChar();
									if( peek == 'I' || peek == 'i' ||
										peek == 'V' || peek == 'v')
										break;
									else if (Character.isAlphabetic(peek) || Character.isDigit(peek)) {
										num += peek;
										error("Erro l�xico: " + num + " n�o � um n�mero romano v�lido!");
									}
									else
										break;
								}
								else {
									somaromano += 50;
									nextChar();
									if( peek == 'I' || peek == 'i' ||
										peek == 'V' || peek == 'v' ||
										peek == 'X' || peek == 'x')
										break;
									else if (Character.isAlphabetic(peek) || Character.isDigit(peek)) {
										num += peek;
										error("Erro l�xico: " + num + " n�o � um n�mero romano v�lido!");
									}
									else
										break;
								}
							
							case 'C': case 'c':	//finalizado
								if(peekanterior == 'X' || peekanterior == 'x') {
									num += peek;
									somaromano += 80;
									nextChar();
									if( peek == 'I' || peek == 'i' ||
										peek == 'V' || peek == 'v')
										break;
									else if (Character.isAlphabetic(peek) || Character.isDigit(peek)) {		//se houver algum d�gito ou letra depois do x d� erro
										num += peek;
										error("Erro l�xico: " + num + " n�o � um n�mero romano v�lido!");
									}
									else	//se tiver tudo certinho d� break
										break;
								}
								else {
									int countc = 0;
									peekanterior = peek;
									while(peek == 'C' || peek == 'c') {
										if(countc < 3) {
											countc++;
											num += peek;
											somaromano += 100;
											nextChar();
										}
										else {
											num += peek;
											error("Erro l�xico: " + num + " n�o � um literal romano v�lido");
										}
									}
									if ((peek == 'I' || peek == 'i' ||
										 peek == 'V' || peek == 'v' ||
										 peek == 'X' || peek == 'x' ||
										 peek == 'L' || peek == 'l')
										 ||
										 ((peek == 'D' || peek == 'd' ||
										   peek == 'M' || peek == 'm') && countc < 2))
										break;
									else if (Character.isAlphabetic(peek) || Character.isDigit(peek)) {
										num += peek;
										error("Erro l�xico: " + num + " n�o � um n�mero romano v�lido!");
									} else
										break;
								}
							
							case 'D': case 'd':		//finalizado
								num += peek;
								if(peekanterior == 'C' || peekanterior == 'c') {
									somaromano += 300;
									nextChar();
									if( peek == 'I' || peek == 'i' ||
										peek == 'V' || peek == 'v' ||
										peek == 'X' || peek == 'x' ||
										peek == 'L' || peek == 'l')
											break;
									else if (Character.isAlphabetic(peek) || Character.isDigit(peek)) {
										num += peek;
										error("Erro l�xico: " + num + " n�o � um n�mero romano v�lido!");
									} else
										break;
								}
								else {
									somaromano += 500;
									nextChar();
									if( peek == 'I' || peek == 'i' ||
										peek == 'V' || peek == 'v' ||
										peek == 'X' || peek == 'x' ||
										peek == 'L' || peek == 'l' ||
										peek == 'C' || peek == 'c')
										break;
									else if (Character.isAlphabetic(peek) || Character.isDigit(peek)) {
										num += peek;
										error("Erro l�xico: " + num + " n�o � um n�mero romano v�lido!");
									} else
										break;
								}
							
							case 'M': case 'm':		//finalizado
								if(peekanterior == 'C' || peekanterior == 'c') {	//900
									num += peek;
									somaromano += 800;
									nextChar();
									if( peek == 'I' || peek == 'i' ||
										peek == 'V' || peek == 'v' ||
										peek == 'X' || peek == 'x' ||
										peek == 'L' || peek == 'l')
										break;
									else if (Character.isAlphabetic(peek) || Character.isDigit(peek)) {
										num += peek;
										error("Erro l�xico: " + num + " n�o � um n�mero romano v�lido!");
									} else
										break;								
								}
								else {
									int countm = 0;
									peekanterior = peek;
									while(peek == 'M' || peek == 'm') {
										if(countm < 4) {
											countm++;
											num += peek;
											somaromano += 1000;
											nextChar();
										}
										else {
											num += peek;
											error("Erro l�xico: " + num + " n�o � um literal romano v�lido");
										}
									}
									if ((peek == 'I' || peek == 'i' ||
										 peek == 'V' || peek == 'v' ||
										 peek == 'X' || peek == 'x' ||
										 peek == 'L' || peek == 'l' ||
										 peek == 'C' || peek == 'c' ||
										 peek == 'D' || peek == 'd' )
										 ||
										 ((peek == 'M' || peek == 'm') && countm < 3))
										break;
									else if (Character.isAlphabetic(peek) || Character.isDigit(peek)) {
										num += peek;
										error("Erro l�xico: " + num + " n�o � um n�mero romano v�lido!");
									} else
										break;
								}
							
							default:
								num += peek;
								error("Erro l�xico: '" + num + "' n�o � um n�mero romano!");
							break;
							}
						}	//se pickar um romano entra no la�o ----  Fim while(isRomano(peek))
						if(Character.isDigit(peek)) {
							num += peek;
							error("Erro l�xico: " + num + " -> n�meros romanos n�o podem ser concatenado com d�gitos");
						}
						else
							num = Integer.toString(somaromano);
						return new Token(Tag.LIT_INT_ROM, num);
					}
					return new Token(Tag.LIT_INT, num);
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





