package InterfacesGraficas;

import java.io.DataInputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.concurrent.BrokenBarrierException;
import java.util.concurrent.CyclicBarrier;

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
	private CyclicBarrier cb;
	private JTextField textField;
	private JTextField textField_1;

	public Login(PrintWriter esc, DataInputStream lec, StringBuilder nombreUsuario, CyclicBarrier barrera, StringBuilder puertoCliente, StringBuilder puertoChat) {
		setTitle("Login");
		escritura = esc;
		lectura = lec;
		cb = barrera;

		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 450, 420);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(new GridLayout(5, 0, 0, 0));

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
		
		JLabel lblPuertoAUsar = new JLabel("Puerto a usar (Cliente)");
		panel_3.add(lblPuertoAUsar);
		
		textField = new JTextField();
		panel_3.add(textField);
		textField.setColumns(10);
		
		JPanel panel_4 = new JPanel();
		contentPane.add(panel_4);
		
		JLabel lblPuertoAUsar_1 = new JLabel("Puerto a usar (Chat)");
		panel_4.add(lblPuertoAUsar_1);
		
		textField_1 = new JTextField();
		panel_4.add(textField_1);
		textField_1.setColumns(10);

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
				acceder(nombreUsuario, textField.getText(), textField_1.getText(), puertoCliente, puertoChat);
			}
		});

		panel.add(btnAcceder);
	}

	// Al presionar el boton registrarse, se despliega una nueva ventana, en la
	// cual se le pediran los datos correspondientes
	// para registrarse en la aplicacion
	protected void registrarse() {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					Registrarse frame = new Registrarse(escritura, lectura);
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	// Al presionar el boton acceder, se envia un mensaje al servidor con los
	// datos introducidos (no se permiten campos vacios)
	// y este respondera con diferentes respuestas en funcion de si se ha podido
	// completar la opcion (Especificado en README)
	protected void acceder(StringBuilder nombreUs, String PuertoAUsarCliente, String puertoAUsarChat, StringBuilder puertoCliente, StringBuilder puertoChat) {
		if (!tfContrasena.getText().isEmpty() || !tfContrasena.getText().contains(" ") || !tfUsuario.getText().isEmpty()
				|| tfUsuario.getText().contains(" ")) {
			escritura.println("Login " + tfUsuario.getText() + " " + tfContrasena.getText());
			escritura.flush();

			try {
				String respuesta = lectura.readLine();
				if (respuesta.startsWith("ok")) {
					nombreUs.delete(0, nombreUs.length());
					nombreUs.insert(0,tfUsuario.getText());
					JOptionPane.showMessageDialog(null, "Se ha logeado correctamente");
					escritura.println("Connect " + tfUsuario.getText().replaceAll("\\s", "") + " " + PuertoAUsarCliente);
					puertoCliente.delete(0, puertoCliente.length());
					puertoCliente.append(PuertoAUsarCliente);
					
					puertoChat.delete(0, puertoChat.length());
					puertoChat.append(puertoAUsarChat);
					escritura.flush();
					
					respuesta = lectura.readLine();
					
					cb.await();
					
					this.dispose();
				} else if (respuesta.startsWith("error")) {
					if (respuesta.split(" ")[1].equals("401")) {
						JOptionPane.showMessageDialog(null, "Error: Usuario no encontrado");
					} else if (respuesta.split(" ")[1].equals("402")) {
						JOptionPane.showMessageDialog(null, "Error: Contraseña incorrecta");
					}
				}
			} catch (IOException e) {
				e.printStackTrace();
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (BrokenBarrierException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		} else {
			if (!tfContrasena.getText().isEmpty() || !tfUsuario.getText().isEmpty()){
				JOptionPane.showMessageDialog(null, "Error: Hay campos vacios");
			}
			else{
				JOptionPane.showMessageDialog(null, "Error: Hay espacios en blanco");
			}
		}

	}

}
