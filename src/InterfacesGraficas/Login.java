package InterfacesGraficas;

import java.io.DataInputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

import Cerrar.Cerrar;

import java.awt.EventQueue;
import java.awt.GridLayout;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JTextField;
import javax.swing.JButton;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

public class Login extends JFrame {

	private JPanel contentPane;
	
	private PrintWriter escritura = null;
	private DataInputStream lectura = null;
	private JTextField tfUsuario;
	private JTextField tfContrasena;



	public Login(Socket s, String nombreUsuario) {
		setTitle("Login");
		try {
			escritura = new PrintWriter(s.getOutputStream());
			lectura = new DataInputStream(s.getInputStream());
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 450, 300);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(new GridLayout(4, 0, 0, 0));
		
		JPanel panel_1 = new JPanel();
		contentPane.add(panel_1);
		
		JLabel lblNewLabel = new JLabel("Nombre de Usuario");
		panel_1.add(lblNewLabel);
		
		tfUsuario = new JTextField();
		panel_1.add(tfUsuario);
		tfUsuario.setColumns(10);
		
		JPanel panel_2 = new JPanel();
		contentPane.add(panel_2);
		
		JLabel lblNewLabel_1 = new JLabel("Contrase\u00F1a");
		panel_2.add(lblNewLabel_1);
		
		tfContrasena = new JTextField();
		panel_2.add(tfContrasena);
		tfContrasena.setColumns(10);
		
		JPanel panel_3 = new JPanel();
		contentPane.add(panel_3);
		
		JPanel panel = new JPanel();
		contentPane.add(panel);
		
		JButton btnRegistrarse = new JButton("Registrarse");
		btnRegistrarse.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				registrarse();
			}
		});
		panel.add(btnRegistrarse);
		
		JButton btnAcceder = new JButton("Acceder");
		btnAcceder.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				acceder(nombreUsuario,s);
			}
		});
		panel.add(btnAcceder);
	}


	//Al presionar el boton registrarse, se despliega una nueva ventana, en la cual se le pediran los datos correspondientes
	//para registrarse en la aplicacion
	protected void registrarse() {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					Registrarse frame = new Registrarse(escritura,lectura);
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});		
	}


	//Al presionar el boton acceder, se envia un mensaje al servidor con los datos introducidos (no se permiten campos vacios)
	//y este respondera con diferentes respuestas en funcion de si se ha podido completar la opcion (Especificado en README)
	protected void acceder(String nombreUs, Socket s) {
		if(!tfContrasena.getText().replaceAll("\\s","").isEmpty() || !tfUsuario.getText().replaceAll("\\s","").isEmpty()){
			escritura.println("Login " + tfUsuario.getText().replaceAll("\\s","") + " " + tfContrasena.getText().replaceAll("\\s",""));
			escritura.flush();
			
			try {
				String respuesta = lectura.readLine();
				if (respuesta.startsWith("ok")){
					nombreUs = tfUsuario.getText();
					JOptionPane.showMessageDialog(null,"Se ha logeado correctamente"); 
					escritura.println("Connect " + tfUsuario.getText().replaceAll("\\s",""));
					escritura.flush();
					Cerrar.cerrar(s);
					this.dispose();
				}
				else if (respuesta.startsWith("error")){
					if (respuesta.split(" ")[1].equals("401")){
						JOptionPane.showMessageDialog(null,"Error: Usuario no encontrado"); 
					}
					else if (respuesta.split(" ")[1].equals("402")){
						JOptionPane.showMessageDialog(null,"Error: Contraseņa incorrecta"); 
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
