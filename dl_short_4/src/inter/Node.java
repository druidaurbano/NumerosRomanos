package inter;

import java.util.LinkedList;

import lexer.Lexer;


//Visualiza��o da �rvore com os n�s
public abstract class Node {
	protected static Emitter code = new Emitter();
	protected LinkedList<Node> children = new LinkedList<Node>();
	
	
	//pega o n�mero da linha para quando ocorrerem erros
	public static void error(String s) {
		System.err.println("linha " 
				+ Lexer.line() + ": " + s);
		System.exit(0);
	}

	public static String code() {
		return code.toString();
	}

	public String strTree() {
		return strTree("");
	}
	
	//constr�i a identa��o da �rvore
	private String strTree(String ident) {
		StringBuffer sb = new StringBuffer();
		sb.append(toString());
		for( Node n: children() ) {
			sb.append("\n" + ident + "|--> ");
			sb.append(n.strTree(ident + "     ")); //5x espa�o
		}
		return sb.toString();
	}

	//adiciona o n� filho
	protected void addChild( Node n ) {
		children.add(n);
	}
	
	//
	protected LinkedList<Node> children() {
		return children;
	}
}
