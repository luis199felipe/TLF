package lexico;

import java.awt.EventQueue;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.SwingConstants;
import javax.swing.table.DefaultTableModel;

import java.awt.Font;
import javax.swing.JTable;

public class App implements ActionListener {

	private JFrame frame;
	private JTextArea txtAnalisis;
	private JButton btnAnalizarArchivo;
	private JButton btnLenguajesR;
	private JButton btnAnalizar;
	private AnalizadorLexico al;
	private JScrollPane scrolltabla;
	private JTable table;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					App window = new App();
					window.frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the application.
	 */
	public App() {
		initialize();
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		frame = new JFrame();
		frame.setBounds(100, 100, 627, 392);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.getContentPane().setLayout(null);

		al = new AnalizadorLexico();
		JScrollPane sp1 = new JScrollPane();
		sp1.setBounds(10, 79, 591, 99);
		frame.getContentPane().add(sp1);

		txtAnalisis = new JTextArea();
		sp1.setViewportView(txtAnalisis);
		txtAnalisis.setColumns(10);

		btnAnalizar = new JButton("Obtener Tokens");
		btnAnalizar.setBounds(10, 189, 205, 23);
		btnAnalizar.addActionListener(this);
		frame.getContentPane().add(btnAnalizar);

		btnAnalizarArchivo = new JButton("Analizar archivo");
		btnAnalizarArchivo.setBounds(225, 189, 189, 23);
		btnAnalizarArchivo.addActionListener(this);
		frame.getContentPane().add(btnAnalizarArchivo);

		btnLenguajesR = new JButton("Lenguajes regulares");
		btnLenguajesR.setBounds(424, 189, 177, 23);
		btnLenguajesR.addActionListener(this);
		frame.getContentPane().add(btnLenguajesR);

		JLabel lblNewLabel = new JLabel("LENGUAJE DE PROGRAMACI\u00D3N - MAF");
		lblNewLabel.setFont(new Font("Noto Serif", Font.PLAIN, 20));
		lblNewLabel.setHorizontalAlignment(SwingConstants.CENTER);
		lblNewLabel.setBounds(10, 11, 591, 62);
		frame.getContentPane().add(lblNewLabel);

		scrolltabla = new JScrollPane();
		scrolltabla.setBounds(10, 222, 591, 120);
		frame.getContentPane().add(scrolltabla);

		table = new JTable();
		scrolltabla.setViewportView(table);

		frame.setResizable(false);
		frame.setLocationRelativeTo(null);

	}

	public String[][] setModelo() {

		ArrayList<Token> listaTokens = al.getListaTokens();
		String[][] salida = new String[listaTokens.size()][4];
		for (int i = 0; i < salida.length; i++) {
			Token t = listaTokens.get(i);
			salida[i][0] = t.getCategoria().name();
			salida[i][1] = t.getPalabra();
			salida[i][2] = "" + t.getFila();
			salida[i][3] = "" + t.getColumna();
		}
		return salida;
	}

	public void crearTabla(JTable tabla) {
		String[] columnas = { "Categoría", "Palabra", "Fila", "Columna" };
		String[][] data = setModelo();
		DefaultTableModel modelo = new DefaultTableModel(data, columnas);
		table.setModel(modelo);
		scrolltabla.setViewportView(table);
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		if (e.getSource() == btnAnalizar) {
			try {
				String texto = txtAnalisis.getText();
				al.setCodigoFuente(texto);
				al.analizar();
//			txtResultado.setText(al.getListaTokens().toString());
				crearTabla(table);

			} catch (StringIndexOutOfBoundsException e1) {
				JOptionPane.showMessageDialog(null, "Antes de analizar debe de escribir o anexar un código fuente",
						"Código fuente vacio", JOptionPane.WARNING_MESSAGE);
			}

		}

		if (e.getSource() == btnAnalizarArchivo) {
			// Creamos el objeto JFileChooser
			JFileChooser fc = new JFileChooser();

			// Abrimos la ventana, guardamos la opcion seleccionada por el usuario
			int seleccion = fc.showOpenDialog(frame.getContentPane());

			// Si el usuario, pincha en aceptar
			if (seleccion == JFileChooser.APPROVE_OPTION) {

				// Seleccionamos el fichero
				File fichero = fc.getSelectedFile();

				// Ecribe la ruta del fichero seleccionado en el campo de texto
				txtAnalisis.setText(fichero.getAbsolutePath());
				String codigoFuente = "";
				try (FileReader fr = new FileReader(fichero)) {
					String cadena = "";
					int valor = fr.read();
					while (valor != -1) {
						cadena = cadena + (char) valor;
						valor = fr.read();
					}
					codigoFuente += cadena;
				} catch (IOException e1) {
					e1.printStackTrace();
				}
				if (!codigoFuente.equals("")) {
					al.setCodigoFuente(codigoFuente);
					al.analizar();
//					txtResultado.setText(al.getListaTokens().toString());
					crearTabla(table);
				}
			}
		}
		
		
		if(e.getSource() == btnLenguajesR) {
			String[] columnas = { "Categoría", "Lenguaje Regular", "equivalente java"};
			String[][] data = setModelo();
			DefaultTableModel modelo = new DefaultTableModel(data, columnas);
			table.setModel(modelo);
			scrolltabla.setViewportView(table);
		}

	}
}
