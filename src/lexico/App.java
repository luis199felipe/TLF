package lexico;

public class App {

	public static void main(String[] args) {

		String codigoFuente = "||AA1|:-->:";
		AnalizadorLexico al = new AnalizadorLexico(codigoFuente);
		al.analizar();
		System.out.println(codigoFuente);
		System.out.println(al.getListaTokens());

	}

}
