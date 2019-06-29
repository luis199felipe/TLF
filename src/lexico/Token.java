package lexico;

public class Token {

	private Categoria categoria;
	private String palabra;
	private int fila;
	private int columna;

	public Token(Categoria categoria, String palabra, int fila, int columna) {
		this.categoria = categoria;
		this.palabra = palabra;
		this.fila = fila;
		this.columna = columna;
	}

	@Override
	public String toString() {
		return "Token [categoria=" + categoria + ", palabra=" + palabra + ", fila=" + fila + ", columna=" + columna
				+ "]\n";
	}

}
