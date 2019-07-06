package lexico;

import java.awt.EventQueue;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;

import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JLabel;
import javax.swing.SwingConstants;
import java.awt.Font;

public class App implements ActionListener {

	private JFrame frame;
	private JTextArea txtAnalisis;
	private JButton btnAnalizarArchivo;
	private JButton btnLenguajesR;
	private JButton btnAnalizar;
	private AnalizadorLexico al;

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
		sp1.setBounds(20, 124, 591, 90);
		frame.getContentPane().add(sp1);

		btnAnalizar = new JButton("Obtener Tokens");
		btnAnalizar.setBounds(20, 281, 205, 23);
		btnAnalizar.addActionListener(this);
		frame.getContentPane().add(btnAnalizar);

		btnAnalizarArchivo = new JButton("Analizar archivo");
		btnAnalizarArchivo.setBounds(235, 242, 189, 23);
		btnAnalizarArchivo.addActionListener(this);
		frame.getContentPane().add(btnAnalizarArchivo);

		btnLenguajesR = new JButton("Lenguajes regulares");
		btnLenguajesR.setBounds(434, 242, 177, 23);
		btnLenguajesR.addActionListener(this);
		frame.getContentPane().add(btnLenguajesR);

		JLabel lblNewLabel = new JLabel("LENGUAJE DE PROGRAMACI\u00D3N - MAF");
		lblNewLabel.setFont(new Font("Noto Serif", Font.PLAIN, 20));
		lblNewLabel.setHorizontalAlignment(SwingConstants.CENTER);
		lblNewLabel.setBounds(10, 11, 591, 62);
		frame.getContentPane().add(lblNewLabel);

		txtAnalisis = new JTextArea();
		txtAnalisis.setBounds(10, 84, 589, 88);
		frame.getContentPane().add(txtAnalisis);
		txtAnalisis.setColumns(10);

	}

	@Override
	public void actionPerformed(ActionEvent e) {
		if (e.getSource() == btnAnalizar) {
			String texto = txtAnalisis.getText();
			al.setCodigoFuente(texto);
			al.analizar();
//			txtResultado.setText(al.getListaTokens().toString());

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
				}
			}
		}

	}
}
