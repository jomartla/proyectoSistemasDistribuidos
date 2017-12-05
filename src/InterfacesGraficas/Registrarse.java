package InterfacesGraficas;

import java.awt.BorderLayout;
import java.awt.EventQueue;
import java.io.DataInputStream;
import java.io.IOException;
import java.io.PrintWriter;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import java.awt.GridLayout;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JTextField;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

public class Registrarse extends JFrame {

	private JPanel contentPane;

	//Se declaran como atributos los streams, para asi evitar tener que abrirlos cada vez que hacedemos, haciendolo mas eficaz
	PrintWriter escritura;
	DataInputStream lectura;
	private JTextField tfNReal;
	private JTextField tfNUsuario;
	private JTextField tfContrasena;
	public Registrarse(PrintWriter esc, DataInputStream lec) {
		escritura = esc;
		lectura = lec;
		
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 285, 377);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(new GridLayout(5, 0, 0, 0));
		
		JPanel panel_1 = new JPanel();
		contentPane.add(panel_1);
		
		JLabel lblNewLabel = new JLabel("Nombre Real:");
		panel_1.add(lblNewLabel);
		
		tfNReal = new JTextField();
		panel_1.add(tfNReal);
		tfNReal.setColumns(10);
		
		JPanel panel = new JPanel();
		contentPane.add(panel);
		
		JLabel lblNewLabel_1 = new JLabel("Nombre de Usuario:");
		panel.add(lblNewLabel_1);
		
		tfNUsuario = new JTextField();
		panel.add(tfNUsuario);
		tfNUsuario.setColumns(10);
		
		JPanel panel_3 = new JPanel();
		contentPane.add(panel_3);
		
		JLabel lblNewLabel_2 = new JLabel("Contrase\u00F1a:");
		panel_3.add(lblNewLabel_2);
		
		tfContrasena = new JTextField();
		panel_3.add(tfContrasena);
		tfContrasena.setColumns(10);
		
		JPanel panel_4 = new JPanel();
		contentPane.add(panel_4);
		
		JPanel panel_2 = new JPanel();
		contentPane.add(panel_2);
		
		JButton btnRegistrarse = new JButton("Registrarse");
		btnRegistrarse.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				registrarse();
			}
		});
		panel_2.add(btnRegistrarse);
	}
	
	protected void registrarse() {
		if(!tfContrasena.getText().isEmpty() || !tfNUsuario.getText().isEmpty() || !tfNReal.getText().isEmpty()){
			escritura.println("Add " + tfNUsuario.getText() + " " + tfContrasena.getText() + " " + tfNReal.getText());
			escritura.flush();
			
			try {
				String respuesta = lectura.readLine();
				if (respuesta.startsWith("ok")){
					JOptionPane.showMessageDialog(null,"Usuario registrado correctamente");
					this.dispose();
				}
				else if (respuesta.startsWith("error")){
					if (respuesta.split(" ")[1].equals("411")){
						JOptionPane.showMessageDialog(null,"Error: Nombre de usuario ya existente"); 
					}
					else if (respuesta.split(" ")[1].equals("412")){
						JOptionPane.showMessageDialog(null,"Error: Contraseña demasiado corta (<4 caracteres)"); 
					}
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		else{
			JOptionPane.showMessageDialog(null,"Error: Hay campos vacios"); 
		}
		
	}

}
