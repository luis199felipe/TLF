package lexico;

import java.util.ArrayList;
import java.util.Observable;

public class AnalizadorLexico {
	private String codigoFuente;
	private ArrayList<Token> listaTokens;
	private ArrayList<String> palabrasReservadas;
	private char caracterActual;
	private char finCodigo;
	private int posicionActual, filaActual, colActual, posicionInicioPalabra;

	public AnalizadorLexico(String codigoFuente) {
		this.posicionInicioPalabra = 0;
		this.codigoFuente = codigoFuente;
		this.listaTokens = new ArrayList<>();
		this.palabrasReservadas = new ArrayList<String>();
		crearPalabraReservadas();
		// this.caracterActual = codigoFuente.charAt(0);
		this.finCodigo = '#';
	}

	public AnalizadorLexico() {
		this.posicionInicioPalabra = 0;
		this.listaTokens = new ArrayList<>();
		this.palabrasReservadas = new ArrayList<String>();
		crearPalabraReservadas();
		// this.caracterActual = codigoFuente.charAt(0);
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

			if (esNumeroNatural())
				continue;

			if (esOperadorRelacional())
				continue;

			if (IncrementoDecremento())
				continue;

			if (esAsignacion())
				continue;

			if (esOperadorAritmetico())
				continue;

			if (esPalabraReservada())
				continue;

			if (esIdentificador())
				continue;

			if (esLogico())
				continue;

			if (esParentesis())
				continue;

			if (esSeparador())
				continue;
			if (esHexadecimal())
				continue;
			if (esCadenaCaracteres())
				continue;
			if (esComentario())
				continue;
			if (esNumeroReal())
				continue;
			listaTokens.add(new Token(Categoria.DESCONOCIDO, "" + caracterActual, filaActual, colActual));
			obtenerSiguienteCaracter();

		}
	}

	private boolean esParentesis() {
		int filaAct = filaActual;
		int colAct = colActual;
		int posActual = posicionActual;

		if (caracterActual == '(' || caracterActual == ')') {

			listaTokens.add(new Token(Categoria.PARENTESIS, caracterActual + "", filaActual, colActual));
			obtenerSiguienteCaracter();
			return true;
		}
		return false;
	}

	private boolean esLlave() {
		int filaAct = filaActual;
		int colAct = colActual;
		int posActual = posicionActual;

		if (caracterActual == '{' || caracterActual == '}') {

			listaTokens.add(new Token(Categoria.PARENTESIS, caracterActual + "", filaActual, colActual));
			obtenerSiguienteCaracter();
			return true;
		}
		return false;
	}

	private boolean esLogico() {
		int filaAct = filaActual;
		int colAct = colActual;
		int posActual = posicionActual;

		if (caracterActual == '^' || caracterActual == 'v') {

			listaTokens.add(new Token(Categoria.OPERADOR_LOGICO, caracterActual + "", filaActual, colActual));
			obtenerSiguienteCaracter();
			return true;
		}

		if (caracterActual == '~') {
			obtenerSiguienteCaracter();
			if (caracterActual == '(') {
				obtenerSiguienteCaracter();
				if (esIdentificador()) {
					if (caracterActual == ')') {
						listaTokens.add(new Token(Categoria.OPERADOR_LOGICO, "~()", filaActual, colActual));
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
					listaTokens.add(new Token(Categoria.OPERADOR_LOGICO, "<->", filaActual, colActual));
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
				listaTokens.add(new Token(Categoria.OPERADOR_ASIGNACION, ":>", filaActual, colActual));
				obtenerSiguienteCaracter();
				return true;
			} else if (caracterActual == ':') {
				obtenerSiguienteCaracter();
				if (caracterActual == '>') {
					listaTokens.add(new Token(Categoria.OPERADOR_ASIGNACION, "::>", filaActual, colActual));
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

		} else if (caracterActual == '>' || caracterActual == '<' || caracterActual == '|') {
			char primerC = caracterActual;

			obtenerSiguienteCaracter();
			if (caracterActual == ':') {
				obtenerSiguienteCaracter();
				if (caracterActual == '>') {

					listaTokens.add(new Token(Categoria.OPERADOR_ASIGNACION, primerC + ":>", filaActual, colActual));
					obtenerSiguienteCaracter();
					return true;
				} else {
					backtracking(posActual, filaAct, colAct);
					return false;
				}
			} else {
				backtracking(posActual, filaAct, colAct);

			}
		}

		if (caracterActual == '|') {
			obtenerSiguienteCaracter();
			if (caracterActual == '|') {
				obtenerSiguienteCaracter();
				if (caracterActual == ':') {
					obtenerSiguienteCaracter();
					if (caracterActual == '>') {
						listaTokens.add(new Token(Categoria.OPERADOR_ASIGNACION, "||:>", filaActual, colActual));
						obtenerSiguienteCaracter();
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
				listaTokens.add(new Token(Categoria.OPERADOR_INCREMENTO, ">>", filaActual, colActual));
				obtenerSiguienteCaracter();
				return true;
			} else {
				backtracking(posActual, filaAct, colAct);
				return false;
			}
		} else if (caracterActual == '<') {
			obtenerSiguienteCaracter();
			if (caracterActual == '<') {
				listaTokens.add(new Token(Categoria.OPERADOR_DECREMENTO, "<<", filaActual, colActual));
				obtenerSiguienteCaracter();
				return true;
			} else {
				backtracking(posActual, filaAct, colAct);
				return false;
			}
		}

		return false;
	}

	public boolean esOperadorAsignacion() {
		int filaAct = filaActual;
		int colAct = colActual;
		int posActual = posicionActual;

		if (caracterActual == ':') {
			obtenerSiguienteCaracter();
			if (caracterActual == '>') {
				listaTokens.add(new Token(Categoria.OPERADOR_ASIGNACION, ":>", filaActual, colActual));
				return true;
			} else if (caracterActual == ':') {
				obtenerSiguienteCaracter();
				if (caracterActual == '>') {
					listaTokens.add(new Token(Categoria.OPERADOR_ASIGNACION, "::>", filaActual, colActual));
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
					listaTokens.add(
							new Token(Categoria.OPERADOR_ASIGNACION, caracterActual + ":>", filaActual, colActual));
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
						listaTokens.add(new Token(Categoria.OPERADOR_ASIGNACION, ":>", filaActual, colActual));
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

	/**
	 * Metodo que identifica si dentro de una cadena de caracteres existe un
	 * operador aritmetico definido dentro de los tokens de un lenguaje de
	 * programación.
	 * 
	 * @return true || false
	 */
	public boolean esOperadorAritmetico() {
		int fila = filaActual;
		int columna = colActual;
		int posActual = posicionActual;

		// operador de suma
		if (caracterActual == '>' || caracterActual == '<' || caracterActual == ':') {
			// transición
			listaTokens.add(new Token(Categoria.OPERADOR_ARITMETICO, caracterActual + "", fila, columna));
			obtenerSiguienteCaracter();
			return true;
		} else if (caracterActual == '|') {
			String operador = "";

			operador += caracterActual;
			// transición
			obtenerSiguienteCaracter();

			// operador de modulo
			if (caracterActual == '|') {
				operador += caracterActual;
				// siguiente transición
				obtenerSiguienteCaracter();

				listaTokens.add(new Token(Categoria.OPERADOR_ARITMETICO, operador, fila, columna));
				return true;
			}

			listaTokens.add(new Token(Categoria.OPERADOR_ARITMETICO, operador, fila, columna));
			return true;
		}
		return false;
	}

	/**
	 * Método que identifica si una cadena de caracteres hace parte de la categoría
	 * OPERADOR_RELACIONAL definida dentro de los tokens de una lenguaje de
	 * programación
	 * 
	 * @return true || false>
	 */
	public boolean esOperadorRelacional() {
		int fila = filaActual;
		int columna = colActual;
		int posActual = posicionActual;

		// operador mayor que >
		if (caracterActual == '+') {

			// primera transición
			obtenerSiguienteCaracter();

			// operador mayor igual >=
			if (caracterActual == ':') {
				obtenerSiguienteCaracter();
				if (caracterActual == '>') {
					listaTokens.add(new Token(Categoria.OPERADOR_RELACIONAL, "+:>", fila, columna));
					obtenerSiguienteCaracter();
					return true;
				}
			} else {
				listaTokens.add(new Token(Categoria.OPERADOR_RELACIONAL, "+", fila, columna));
				return true;
			}

		} else if (caracterActual == '-') { // operador menor que <

			// primera transición
			obtenerSiguienteCaracter();

			// operador menor igual <=
			if (caracterActual == ':') {
				obtenerSiguienteCaracter();
				if (caracterActual == '>') {
					listaTokens.add(new Token(Categoria.OPERADOR_RELACIONAL, "-:>", fila, columna));
					obtenerSiguienteCaracter();
					return true;
				}
			}
			listaTokens.add(new Token(Categoria.OPERADOR_RELACIONAL, "-", fila, columna));
			return true;

		} else if (caracterActual == '~') { // operador diferente !=

			// primera transición
			obtenerSiguienteCaracter();
			if (caracterActual == ':') {
				obtenerSiguienteCaracter();
				if (caracterActual == '>') {
					listaTokens.add(new Token(Categoria.OPERADOR_RELACIONAL, "~:>", fila, columna));
					obtenerSiguienteCaracter();
					return true;
				}
			}
		} else if (caracterActual == '<') {// operador ==
			// primera transición
			obtenerSiguienteCaracter();
			if (caracterActual == '-') {
				// segunda trasición
				obtenerSiguienteCaracter();
				if (caracterActual == '>') {
					listaTokens.add(new Token(Categoria.OPERADOR_RELACIONAL, "<->", fila, columna));
					obtenerSiguienteCaracter();
					return true;
				} else {
					backtracking(posActual, fila, columna);
					return false;
				}
			} else {
				backtracking(posActual, fila, columna);
				return false;
			}
		}
		// rechazo inmediato
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

	/**
	 * Método recursivo que verifica si una cadena de caracteres hace parte de la
	 * categoria NUMERO_REAL definida dentro de los tokens de un lenguaje de
	 * programación.
	 * 
	 * @param real, cadena donde se va concatenando los caracteres válidos del
	 *        número real.
	 * @param contadorPuntos, entero que cuenta las veces que aparece un punto,
	 * @param fila, fila desde donde se empieza a identificar el token.
	 * @param columna, columna desde donde se empieza a identificar el token.
	 * @return true || false
	 */

	public boolean esNumeroReal() {
		String real = "";
		int fila = filaActual;
		int columna = colActual;
		int posActual = posicionActual;
		return esNumeroReal(real, 0, fila, columna, posActual, false);
	}

	public boolean esNumeroReal(String real, int contadorPuntos, int fila, int columna, int posActual, boolean result) {

		if (contadorPuntos == 1) {
			// Transición si siguen más digitos despues del punto
			if (Character.isDigit(caracterActual)) {

				real += caracterActual;
				obtenerSiguienteCaracter();
				result = esNumeroReal(real, contadorPuntos, fila, columna, posActual, result);

			} else if (caracterActual == 'd') { // terminación del token

				real += caracterActual;
				posicionInicioPalabra = posicionActual;
				listaTokens.add(new Token(Categoria.NUMERO_REAL, real, fila, columna));
				obtenerSiguienteCaracter();
				return true;

			} else if (!Character.isDigit(caracterActual) || caracterActual != 'd' || caracterActual == '.') {
				backtracking(posActual, fila, columna);
				return false;
			}
		} else {

			if (Character.isDigit(caracterActual)) { // Transición si siguen digitos

				real += caracterActual;
				obtenerSiguienteCaracter();
				result = esNumeroReal(real, contadorPuntos, fila, columna, posActual, result);

			} else if (caracterActual == '.') { // Transición si sigue un punto

				contadorPuntos = contadorPuntos + 1;
				real += caracterActual;
				obtenerSiguienteCaracter();
				result = esNumeroReal(real, contadorPuntos, fila, columna, posActual, result);

			} else if (!Character.isDigit(caracterActual) || caracterActual != '.') {
				backtracking(posActual, fila, columna);
				return false;
			}
		}
		return result;
	}

	/**
	 * public boolean esIdentificador() { String palabra = ""; int fila =
	 * filaActual; int columna = colActual; int posActual = posicionActual;
	 * 
	 * if (Character.isLetter(caracterActual)) { palabra += caracterActual;
	 * 
	 * // primera transición obtenerSiguienteCaracter();
	 * 
	 * if (Character.isLetter(caracterActual)) { palabra += caracterActual;
	 * 
	 * // segunda transición obtenerSiguienteCaracter();
	 * 
	 * if (Character.isDigit(caracterActual)) { palabra += caracterActual;
	 * 
	 * // tercera transición obtenerSiguienteCaracter();
	 * 
	 * if (Character.isDigit(caracterActual)) { palabra += caracterActual;
	 * 
	 * // cuarta transición obtenerSiguienteCaracter();
	 * 
	 * if (Character.isDigit(caracterActual)) { palabra += caracterActual;
	 * 
	 * // quinta transición obtenerSiguienteCaracter();
	 * 
	 * if (caracterActual == '_') { palabra += caracterActual;
	 * 
	 * // sexta transición obtenerSiguienteCaracter();
	 * 
	 * if (Character.isLetter(caracterActual)) { palabra += caracterActual;
	 * 
	 * // ultima transición obtenerSiguienteCaracter();
	 * 
	 * while (Character.isLetter(caracterActual)) { palabra += caracterActual; //
	 * ultima transición obtenerSiguienteCaracter(); }
	 * 
	 * posicionInicioPalabra = posicionActual; listaTokens.add(new
	 * Token(Categoria.IDENTIFICADOR, palabra, fila, columna));
	 * obtenerSiguienteCaracter(); return true; } else { // rechazo inmediato
	 * backtracking(posActual, fila, columna); return false; } } else { // rechazo
	 * inmediato backtracking(posActual, fila, columna); return false; } } else { //
	 * rechazo inmediato backtracking(posActual, fila, columna); return false; } }
	 * else { // rechazo inmediato backtracking(posActual, fila, columna); return
	 * false; } } else { // rechazo inmediato backtracking(posActual, fila,
	 * columna); return false; } } else { // rechazo inmediato
	 * backtracking(posActual, fila, columna); return false; } } else { // rechazo
	 * inmediato backtracking(posActual, fila, columna); return false; } }
	 */

	/**
	 * Método que identifica si una cadena de caracteres hace parte de la categoria
	 * IDENTIFICADOR definida dentro de los tokens de una lenguaje de programación.
	 * 
	 * @return true || false
	 */
	public boolean esIdentificador() {
		String palabra = "";
		int fila = filaActual;
		int columna = colActual;
		int posActual = posicionActual;

		if (caracterActual == '_') {
			palabra += caracterActual;

			// primera transición
			obtenerSiguienteCaracter();

			while (Character.isLetter(caracterActual)) {
				palabra += caracterActual;
				// demas transiciones
				obtenerSiguienteCaracter();
			}

			if (caracterActual == '_') {
				palabra += caracterActual;

				listaTokens.add(new Token(Categoria.IDENTIFICADOR, palabra, fila, columna));
				// transición al siguiente token
				obtenerSiguienteCaracter();
				return true;

			} else {
				backtracking(posActual, fila, columna);
				return false;
			}
		}
		return false;
	}

	/**
	 * Método que identifica si una cadena de caracteres pertenece al conjunto de
	 * palabras reservadas definidas dentro de los tokens de un lenguaje de
	 * programación.
	 * 
	 * @return true || false
	 */
	public boolean esPalabraReservada() {
		String palabra = "";
		int fila = filaActual;
		int columna = colActual;
		int posActual = posicionActual;

		// System.out.println("Entro a reserva "+caracterActual+"
		// "+Character.isLetter(caracterActual));
//		obtenerSiguienteCaracter();
		if (Character.isLetter(caracterActual)) {
			palabra += caracterActual;
			// primera transición
			obtenerSiguienteCaracter();

			while (Character.isLetter(caracterActual)) {
				palabra += caracterActual;
				// demás transiciones
				obtenerSiguienteCaracter();
			}
			// System.out.println("La pal es "+palabra+"
			// "+palabrasReservadas.contains(palabra));

			if (palabrasReservadas.contains(palabra)) {

				listaTokens.add(new Token(Categoria.PALABRA_RESERVADA, palabra, fila, columna));
				return true;
			} else {
				backtracking(posActual, fila, columna);
				return false;
			}
		} else {
			backtracking(posActual, fila, columna);
			return false;
		}

	}

	public boolean esSeparador() {

		if (caracterActual == '¬') {
			String palabra = "";
			obtenerSiguienteCaracter();

			listaTokens.add(new Token(Categoria.SEPARADOR, palabra, filaActual, colActual));
			return true;
		}
		return false;
	}

	/**
	 * 0x(LuD)* L: ABCDEF D: 0123456789 Hexadecimal Ejemplo Hexadecimal 0xFF1A
	 * 
	 * @return
	 */
	public boolean esHexadecimal() {
		String palabra = "";
		int fila = filaActual;
		int columna = colActual;
		int posActual = 0;

		if (caracterActual == '0') {
			posActual++;
			palabra += caracterActual;
			obtenerSiguienteCaracter();
			if (caracterActual == 'x') {
				posActual++;
				palabra += caracterActual;
				obtenerSiguienteCaracter();
				while (Character.isDigit(caracterActual) || letraAceptada()) {
					posActual++;
					palabra += caracterActual;
					obtenerSiguienteCaracter();
				}

				listaTokens.add(new Token(Categoria.HEXADECIMAL, palabra, fila, columna));
				return false;

			} else {
				backtracking(posActual, fila, columna);
				return false;
			}

		}
		return false;

	}

	public boolean letraAceptada() {
		switch (caracterActual) {
		case 'A':
			return true;
		case 'B':
			return true;
		case 'C':
			return true;
		case 'D':
			return true;
		case 'E':
			return true;
		case 'F':
			return true;
		default:
			return false;
		}
	}

	public boolean esCadenaCaracteres() {
		String palabra = "";
		int fila = filaActual;
		int columna = colActual;
		int posActual = 0;
		if (caracterActual == '!') {
			posActual++;
			palabra += caracterActual;
			obtenerSiguienteCaracter();

			while (caracterActual != '¡') {
				if (caracterActual == finCodigo) {
					backtracking(posActual, fila, columna);
					return false;
				} else {
					posActual++;
					palabra += caracterActual;
					obtenerSiguienteCaracter();
				}
			}
			palabra += caracterActual;
			obtenerSiguienteCaracter();
			listaTokens.add(new Token(Categoria.CADENA_CARACTERES, palabra, fila, columna));
			return true;
		}
		return false;

	}

	/**
	 * Ejemplo comentario: $es un comentario$
	 * 
	 * @return
	 */
	public boolean esComentario() {
		String palabra = "";
		int fila = filaActual;
		int columna = colActual;
		int posActual = 0;
		if (caracterActual == '$') {
			posActual++;
			palabra += caracterActual;
			obtenerSiguienteCaracter();

			while (caracterActual != '$') {
				if (caracterActual == finCodigo) {
					backtracking(posActual, fila, columna);
					return false;
				} else {
					posActual++;
					palabra += caracterActual;
					obtenerSiguienteCaracter();
				}
			}
			palabra += caracterActual;
			obtenerSiguienteCaracter();
			listaTokens.add(new Token(Categoria.COMENTARIO, palabra, fila, columna));
			return true;
		}
		return false;

	}

	public void backtracking(int posActual, int filaAct, int colAct) {
		posicionActual = posActual;
		filaActual = filaAct;
		colActual = colAct;
		caracterActual = codigoFuente.charAt(posicionActual);
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

	public void crearPalabraReservadas() {
		palabrasReservadas.add("metodo");
		palabrasReservadas.add("heredar");
		palabrasReservadas.add("salir");
		palabrasReservadas.add("continuar");
		palabrasReservadas.add("usuario");
		palabrasReservadas.add("hacer");
		palabrasReservadas.add("accion");
	}

	public String getCodigoFuente() {
		return codigoFuente;
	}

	public void setCodigoFuente(String codigoFuente) {
		this.codigoFuente = codigoFuente;
		this.posicionActual = 0;
		this.filaActual = 0;
		this.colActual = 0;
		this.posicionInicioPalabra = 0;

		this.caracterActual = codigoFuente.charAt(0);

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
