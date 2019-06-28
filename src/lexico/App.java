package lexico;

public class App {

	public static void main(String[] args) {

		String codigoFuente = "1234abc567ppp000 12men";
		AnalizadorLexico al = new AnalizadorLexico(codigoFuente);
		al.analizar();
		System.out.println(al.getListaTokens());

	}

}
