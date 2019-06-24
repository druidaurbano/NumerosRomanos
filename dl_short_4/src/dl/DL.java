package dl;

import java.io.File;
import java.io.PrintWriter;

import lexer.Lexer;
import parser.Parser;

public class DL {
	public static void main(String[] args) {
		//An�lise
		Lexer l = new Lexer(
				new File("prog.dl"));
		Parser p = new Parser(l);
		p.parse();

		//Imprimindo a �rvore sint�tica e c�digo intermedi�rio
		System.out.println(p.parseTree());
		System.out.println(p.code());
		System.out.println("finalizado");

		//Construindo arquivo
		try {
			PrintWriter pw = 
				new PrintWriter("prog.ll");
			pw.write(p.code());
			pw.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
