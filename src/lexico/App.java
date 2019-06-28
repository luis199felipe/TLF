package lexico;

public class App {

	public static void main(String[] args) {

		String codigoFuente = "^v<->#";
		AnalizadorLexico al = new AnalizadorLexico(codigoFuente);
		al.analizar();
		System.out.println(al.getListaTokens());

	}

}
