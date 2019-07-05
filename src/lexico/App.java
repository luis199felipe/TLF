package lexico;

public class App {

	public static void main(String[] args) {

		String codigoFuente = "0xADEEF12-$HFHD!nbm¡";
		AnalizadorLexico al = new AnalizadorLexico(codigoFuente);
		al.analizar();
		System.out.println(codigoFuente);
		System.out.println(al.getListaTokens());

	}

}
