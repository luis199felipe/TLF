package lexico;

import java.util.ArrayList;

public class AnalizadorLexico {
	private String codigoFuente;
	private ArrayList<Token> listaTokens;
	private char caracterActual;
	private char finCodigo;
	private int posicionActual, filaActual, colActual, posicionInicioPalabra;

	public AnalizadorLexico(String codigoFuente) {
		this.posicionInicioPalabra = 0;
		this.codigoFuente = codigoFuente;
		this.listaTokens = new ArrayList<>();
		this.caracterActual = codigoFuente.charAt(0);
		this.finCodigo = '#';
	}

	public void analizar() {
		while (caracterActual != finCodigo) {
			if (caracterActual == ' ') {
				obtenerSiguienteCaracter();
				posicionInicioPalabra++;

				continue;
			}
			if (caracterActual == '\n' || caracterActual == '\t') {
				obtenerSiguienteCaracter();
				posicionInicioPalabra += 2;
				continue;
			}

			System.out.println(posicionInicioPalabra);

			if (esLogico())
				continue;

//			if (esEntero())
//				continue;
//			if (esIdentificador())
//				continue;
//			if (esSeparador())
//				continue;
//			if (esHexadecimal())
//				continue;
//			if (esCadenaCaracteres())
//				continue;
//			if (esComentario())
//				continue;

			listaTokens.add(new Token(Categoria.DESCONOCIDO, "" + caracterActual, filaActual, colActual));
			obtenerSiguienteCaracter();

		}
	}

	private boolean esLogico() {
		int filaAct = filaActual;
		int colAct = colActual;
		int posActual = posicionActual;

		if (caracterActual == '^' || caracterActual == 'v') {

			listaTokens.add(new Token(Categoria.LOGICO, caracterActual + "", filaActual, colActual));
			obtenerSiguienteCaracter();
			return true;
		}

		if (caracterActual == '~') {
			obtenerSiguienteCaracter();
			if (caracterActual == '(') {
				obtenerSiguienteCaracter();
				if (esIdentificador()) {
					if (caracterActual == ')') {
						listaTokens.add(new Token(Categoria.LOGICO, "~()", filaActual, colActual));
						obtenerSiguienteCaracter();
						return true;
					} else {
						backtracking(posActual, filaAct, colAct);
						return false;
					}
				} else {
					backtracking(posActual, filaAct, colAct);
					return false;

				}
			} else {
				backtracking(posActual, filaAct, colAct);
				return false;
			}
		}

		if (caracterActual == '<') {
			obtenerSiguienteCaracter();
			if (caracterActual == '-') {
				obtenerSiguienteCaracter();
				if (caracterActual == '>') {
					listaTokens.add(new Token(Categoria.LOGICO, "<->", filaActual, colActual));
					obtenerSiguienteCaracter();
					return true;
				} else {
					backtracking(posActual, filaAct, colAct);
					return false;
				}
			} else {
				backtracking(posActual, filaAct, colAct);
				return false;
			}
		}
		return false;
	}

	public boolean esAsignacion() {
		int filaAct = filaActual;
		int colAct = colActual;
		int posActual = posicionActual;

		if (caracterActual == ':') {
			obtenerSiguienteCaracter();
			if (caracterActual == '>') {
				listaTokens.add(new Token(Categoria.ASIGNACION, ":>", filaActual, colActual));
				return true;
			} else if (caracterActual == ':') {
				obtenerSiguienteCaracter();
				if (caracterActual == '>') {
					listaTokens.add(new Token(Categoria.ASIGNACION, "::>", filaActual, colActual));
					return true;
				} else {
					backtracking(posActual, filaAct, colAct);
					return false;

				}
			} else {
				backtracking(posActual, filaAct, colAct);
				return false;
			}

		} else if (caracterActual == '>' || caracterActual == '<' || caracterActual == '|') {
			obtenerSiguienteCaracter();
			if (caracterActual == ':') {
				obtenerSiguienteCaracter();
				if (caracterActual == '>') {
					listaTokens.add(new Token(Categoria.ASIGNACION, caracterActual + ":>", filaActual, colActual));
					return true;
				} else {
					backtracking(posActual, filaAct, colAct);
					return false;
				}
			} else {
				backtracking(posActual, filaAct, colAct);
				return false;
			}
		}

		if (caracterActual == '|') {
			obtenerSiguienteCaracter();
			if (caracterActual == '|') {
				obtenerSiguienteCaracter();
				if (caracterActual == ':') {
					obtenerSiguienteCaracter();
					if (caracterActual == '>') {
						listaTokens.add(new Token(Categoria.ASIGNACION, ":>", filaActual, colActual));
						return true;
					} else {
						backtracking(posActual, filaAct, colAct);
						return false;
					}
				}
			} else {
				backtracking(posActual, filaAct, colAct);
				return false;
			}
		}
		return false;
	}

	public boolean IncrementoDecremento() {
		int filaAct = filaActual;
		int colAct = colActual;
		int posActual = posicionActual;

		if (caracterActual == '>') {
			obtenerSiguienteCaracter();
			if (caracterActual == '>') {
				listaTokens.add(new Token(Categoria.ASIGNACION, ">>", filaActual, colActual));
				return true;
			} else {
				backtracking(posActual, filaAct, colAct);
				return false;
			}
		} else if (caracterActual == '<') {
			obtenerSiguienteCaracter();
			if (caracterActual == '<') {
				listaTokens.add(new Token(Categoria.ASIGNACION, "<<", filaActual, colActual));
				return true;
			} else {
				backtracking(posActual, filaAct, colAct);
				return false;
			}
		}

		return false;
	}

	/**
	 * Método que identifica si una cadena de caracteres hace parte de la categoria
	 * NUMERO_NATURAL definida dentro de los tokens de un lenguaje de programación.
	 * 
	 * @return true || false
	 */
	public boolean esNumeroNatural() {
		if (caracterActual == 'n') {
			String numero = "";
			int fila = filaActual;
			int columna = colActual;

			numero += caracterActual;
			obtenerSiguienteCaracter();

			// Transición
			while (Character.isDigit(caracterActual)) {
				numero += caracterActual;
				obtenerSiguienteCaracter();
			}

			listaTokens.add(new Token(Categoria.NUMERO_NATURAL, numero, fila, columna));
			posicionInicioPalabra = posicionActual;
			return true;
		}
		return false;
	}

	public boolean esNumeroReal(String real, int contadorPuntos) {

		if (contadorPuntos == 1) {
			// Transición si siguen más digitos despues del punto
			if (Character.isDigit(caracterActual)) {

				real += caracterActual;
				obtenerSiguienteCaracter();
				esNumeroReal(real, 0);

			} else if (caracterActual == 'd') { // terminación del token

				real += caracterActual;
//				listaTokens.add(new Token(Categoria.NUMERO_REAL, real, fila, columna));
				posicionInicioPalabra = posicionActual;
				return true;

			} else {
				// rechazo inmediato
				return false;
			}
		}

		if (Character.isDigit(caracterActual)) { // Transición si siguen digitos

			real += caracterActual;
			obtenerSiguienteCaracter();
			esNumeroReal(real, 0);

		} else if (caracterActual == '.') { // Transición si sigue un punto

			contadorPuntos = contadorPuntos + 1;
			real += caracterActual;
			obtenerSiguienteCaracter();
			esNumeroReal(real, contadorPuntos);

		} else if (!Character.isDigit(caracterActual) || caracterActual != '.') {
			// rechazo inmediato
			return false;
		}
		return false;
	}

	public boolean esIdentificador() {
		if (Character.isLetter(caracterActual) || caracterActual == '_' || caracterActual == '$') {
			String palabra = "";
			int fila = filaActual;
			int columna = colActual;

			// Transición
			palabra += caracterActual;
			obtenerSiguienteCaracter();

			while (Character.isDigit(caracterActual) || Character.isLetter(caracterActual) || caracterActual == '_'
					|| caracterActual == '$') {
				palabra += caracterActual;
				obtenerSiguienteCaracter();
			}

			listaTokens.add(new Token(Categoria.IDENTIFICADOR, palabra, fila, columna));

			posicionInicioPalabra = posicionActual;
			return true;
		}
		return false;
	}

	public boolean esSeparador() {
		if (caracterActual == '-') {
			String palabra = "";
			int fila = filaActual;
			int columna = colActual;

			// Transición
			palabra += caracterActual;
			obtenerSiguienteCaracter();

			// Ambiguedad con operador relacional <->
			if (caracterActual == '>') {
				return false;

				// volver al principio de la palabra

			}

			// Ambiguedad con comentario --> <--
			if (caracterActual == '-') {
				return false;

				// volver al principio de la palabra

			}

			listaTokens.add(new Token(Categoria.IDENTIFICADOR, palabra, fila, columna));
			return true;
		}
		return false;
	}

	public boolean esHexadecimal() {
		if (caracterActual == '0') {
			String palabra = "";
			int fila = filaActual;
			int columna = colActual;

			// Transición
			palabra += caracterActual;
			obtenerSiguienteCaracter();
			listaTokens.add(new Token(Categoria.IDENTIFICADOR, palabra, fila, columna));
			return true;
		}
		return false;
	}

	public boolean esCadenaCaracteres() {
		if (caracterActual == '!') {
			String palabra = "";
			int fila = filaActual;
			int columna = colActual;

			// Transición
			palabra += caracterActual;
			obtenerSiguienteCaracter();
			listaTokens.add(new Token(Categoria.IDENTIFICADOR, palabra, fila, columna));
			return true;
		}
		return false;
	}

	public boolean esComentario() {
		if (caracterActual == '-') {
			String palabra = "";
			int fila = filaActual;
			int columna = colActual;

			// Transición
			palabra += caracterActual;
			obtenerSiguienteCaracter();
			listaTokens.add(new Token(Categoria.IDENTIFICADOR, palabra, fila, columna));
			return true;
		}
		return false;
	}

	public void backtracking(int posActual, int filaAct, int colAct) {
		posicionActual = posActual;
		filaActual = filaAct;
		colActual = colAct;
	}

	public void obtenerSiguienteCaracter() {
		posicionActual++;
		if (posicionActual < codigoFuente.length()) {

			if (caracterActual == '\n') {
				filaActual++;
				colActual = 0;
			} else {
				colActual++;
			}
			caracterActual = codigoFuente.charAt(posicionActual);
		} else {
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
