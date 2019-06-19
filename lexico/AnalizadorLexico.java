package TLF.lexico;
import java.util.ArrayList;

public class AnalizadorLexico {
	private String codigoFuente;
	private ArrayList<Token> listaTokens;
	private char caracterActual;
	private char finCodigo;
	private int posicionActual,filaActual,colActual;
	
	public AnalizadorLexico(String codigoFuente) {
		this.codigoFuente  = codigoFuente;
		this.listaTokens = new ArrayList<>();
		this.caracterActual = codigoFuente.charAt(0);
		this.finCodigo = '¡';
	}
	
	public void analizar() {
		while(caracterActual!=finCodigo) {
			if (caracterActual==' ' || caracterActual=='\n' || caracterActual == '\t') {
				obtenerSiguienteCaracter();
				continue;
			}
			if (esEntero()) continue;
			if (esIdentificador()) continue;
			
			listaTokens.add(new Token(Categoria.DESCONOCIDO, ""+caracterActual, filaActual, colActual));
			obtenerSiguienteCaracter();
			
		}
	}
	
	public boolean esEntero() {
		
		
		if (Character.isDigit(caracterActual)) {
		
			String palabra = "";
			int fila =  filaActual;
			int columna  = colActual;
			
			//Transición
			palabra+=caracterActual;
			obtenerSiguienteCaracter();

			
			while (Character.isDigit(caracterActual)) {
				palabra += caracterActual;
				obtenerSiguienteCaracter();
			}
			
			listaTokens.add(new Token(Categoria.ENTERO, palabra, fila, columna));
			return true;
		}
		return false;
	}
	
public boolean esIdentificador() {
		
		
		if (Character.isLetter(caracterActual) || caracterActual=='_' || caracterActual=='$') {
			String palabra = "";
			int fila =  filaActual;
			int columna  = colActual;
			
			//Transición
			palabra+=caracterActual;
			obtenerSiguienteCaracter();

			
			while (Character.isDigit(caracterActual) || Character.isLetter(caracterActual) || caracterActual=='_' || caracterActual=='$') {
				palabra += caracterActual;
				obtenerSiguienteCaracter();
			}
			
			listaTokens.add(new Token(Categoria.IDENTIFICADOR, palabra, fila, columna));
			return true;
		}
		return false;
	}
	
	
	
	public void obtenerSiguienteCaracter() {
		
		
		posicionActual++;
		if (posicionActual<codigoFuente.length()) {
			
			if (caracterActual=='\n') {
				filaActual++;
				colActual = 0;
			}else {
				colActual++;
			}
			caracterActual = codigoFuente.charAt(posicionActual);	
		}else {
			caracterActual = finCodigo;
		}
		
		
	}
	
	
	
	
	
	
	
	
	
	
	
	

	public String getCodigoFuente() {
		return codigoFuente;
	}

	public void setCodigoFuente(String codigoFuente) {
		this.codigoFuente = codigoFuente;
	}

	public ArrayList<Token> getListaTokens() {
		return listaTokens;
	}

	public void setListaTokens(ArrayList<Token> listaTokens) {
		this.listaTokens = listaTokens;
	}

	public char getCaracterActual() {
		return caracterActual;
	}

	public void setCaracterActual(char caracterActual) {
		this.caracterActual = caracterActual;
	}

	public char getFinCodigo() {
		return finCodigo;
	}

	public void setFinCodigo(char finCodigo) {
		this.finCodigo = finCodigo;
	}

	public int getPosicionActual() {
		return posicionActual;
	}

	public void setPosicionActual(int posicionActual) {
		this.posicionActual = posicionActual;
	}

	public int getFilaActual() {
		return filaActual;
	}

	public void setFilaActual(int filaActual) {
		this.filaActual = filaActual;
	}

	public int getColActual() {
		return colActual;
	}

	public void setColActual(int colActual) {
		this.colActual = colActual;
	}

	
}
