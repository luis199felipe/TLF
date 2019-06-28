package lexico;

public class App {

	public static void main(String[] args) {
		
		
		String codigoFuente = "1234abc567 ppp000";
		AnalizadorLexico al = new AnalizadorLexico(codigoFuente);
		al.analizar();
		System.out.println(al.getListaTokens());
		
		
	}

}
