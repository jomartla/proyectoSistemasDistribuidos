package InterfacesGraficas;

import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.net.Socket;
import java.util.concurrent.*;
import javax.sound.sampled.*;
import javax.swing.*;
import javax.swing.border.EmptyBorder;

import Cerrar.Cerrar;
import Proyecto.AtenderPeticionChat;

public class Llamar extends JFrame {

	private JPanel contentPane;
	private JTextField tdUsuarioConectar;
	private JPanel panel_1;
	private JButton btnConectar;
	private StringBuilder nomUsuario;

	private PrintWriter escritura;
	private DataInputStream lectura;
	private Socket socketConexionChat;
	
	private Clip sonido;
	
	//Esta interfaz se abre cuando conseguimos loguearnos correctamente.
	//En el constructor generamos la interfaz.
	public Llamar(PrintWriter esc, DataInputStream lec, StringBuilder nomUsuario) {
		
		ExecutorService pool = Executors.newCachedThreadPool();
		
		escritura = esc;
		lectura = lec;
		this.nomUsuario = nomUsuario;
		
		this.setTitle(nomUsuario.toString());

		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 390, 155);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(new GridLayout(2, 0, 0, 0));

		JPanel panel = new JPanel();
		contentPane.add(panel);


		JLabel lblUsuarioALlamar = new JLabel("Usuario a conectar:");

		panel.add(lblUsuarioALlamar);

		tdUsuarioConectar = new JTextField();
		panel.add(tdUsuarioConectar);
		tdUsuarioConectar.setColumns(10);

		panel_1 = new JPanel();
		contentPane.add(panel_1);

		
    
		btnConectar = new JButton("Conectar");

		btnConectar.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				llamar(nomUsuario.toString(), pool);
			}
		});
		panel_1.add(btnConectar);
	}
	/*
	 * El m�todo llamar env�a una petici�n al servidor junto al nombre de usuario al que se quiere conectar.
	 * Existen varias respuestas posibles:
	 * 1: El usuario est� conectado y el servidor nos manda la direcci�n ip actual, a trav�s de la cual le llamamos.
	 * Si el otro usuario no nos cuelga iniciamos una interfaz chat y un atender chat que reciba peticiones del otro cliente.
	 * 2: El usuario no existe, y por tanto no se realiza la conexi�n.
	 * 3: Ha habido un fallo al realizar la conexi�n, y por tanto no podemos abrir el chat.
	 */
	protected void llamar(String nomUsuarioPrincipal, ExecutorService pool) {
		if (!tdUsuarioConectar.getText().replaceAll("\\s", "").isEmpty()) {
			escritura.println("ConnectTo " + tdUsuarioConectar.getText().replaceAll("\\s", ""));
			escritura.flush();

			socketConexionChat = null;
			PrintWriter mensajeLlamada = null;
			DataInputStream contestacionLlamada = null;

			try {
				String respuesta = lectura.readLine();
				System.out.println(respuesta);
				if (respuesta.startsWith("ok")) {
					String[] partes = respuesta.split(" ");
					String direccion = partes[1];


					socketConexionChat = new Socket(direccion, Integer.parseInt(partes[2]));
					mensajeLlamada = new PrintWriter(new OutputStreamWriter(socketConexionChat.getOutputStream()));
					contestacionLlamada = new DataInputStream(socketConexionChat.getInputStream());

					mensajeLlamada.println("Llamada " + nomUsuario);
					mensajeLlamada.flush();
					
				
					try {
						sonido = AudioSystem.getClip();
						sonido.open(AudioSystem.getAudioInputStream(new File("llamar.wav")));
				        sonido.start();
					} catch (LineUnavailableException | UnsupportedAudioFileException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}

					respuesta = contestacionLlamada.readLine();
					
					if (respuesta.startsWith("ok")) {
						respuesta = contestacionLlamada.readLine();
								
						if (respuesta.startsWith("ok")) {
							Chat frame = new Chat(socketConexionChat,nomUsuarioPrincipal);
							EventQueue.invokeLater(new Runnable() {
								public void run() {
									try {
										
										AtenderPeticionChat atenderChat = new AtenderPeticionChat(frame);
										frame.dispose();
										
										
										pool.execute(atenderChat);
										frame.setVisible(true);
									} catch (Exception e) {
										e.printStackTrace();
									}
								}
							});
							
							

							this.addWindowListener(new java.awt.event.WindowAdapter() {
								@Override
								public void windowClosing(java.awt.event.WindowEvent windowEvent) {
									pool.shutdownNow();
									frame.dispose();
								}
							});
							sonido.close();
						}
						else{
							if (respuesta.split(" ")[1].equals("501")) {
							;
								sonido.close();
								JOptionPane.showMessageDialog(null, "Error: El usuario a conectar ha rechazado la peticion");
								Cerrar.cerrar(socketConexionChat);
							}
						}
					} else {
						sonido.close();
					
						JOptionPane.showMessageDialog(null, "Error: Fallo al establecer la conexion");
						Cerrar.cerrar(socketConexionChat);
					}
				} else if (respuesta.startsWith("error")) {
					
					if (respuesta.split(" ")[1].equals("417")) {
						JOptionPane.showMessageDialog(null, "Error: El usuario a conectar no existe");
					} else if (respuesta.split(" ")[1].equals("416")) {
						JOptionPane.showMessageDialog(null, "Error: El usuario a conectar no esta disponible");

					}
				}
			} catch (IOException e) {
				e.printStackTrace();
			} finally {
				
			}

		} else {
			
			JOptionPane.showMessageDialog(null, "Error: El campo esta vacio");
			
		}

	}

}
