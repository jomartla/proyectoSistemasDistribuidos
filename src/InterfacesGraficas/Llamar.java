package InterfacesGraficas;

import java.awt.EventQueue;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.DataInputStream;
import java.io.File;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.border.EmptyBorder;

import Proyecto.AtenderChat;


public class Llamar extends JFrame {

	private JPanel contentPane;
	private JTextField tdUsuarioLlamar;
	private JPanel panel_1;
	private JButton btnLlamar;
	private StringBuilder nomUsuario;

	private PrintWriter escritura;
	private DataInputStream lectura;
	private Socket socketLlamada;
	
	private Clip sonido;

	public Llamar(PrintWriter esc, DataInputStream lec, StringBuilder nomUsuario) {
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

		tdUsuarioLlamar = new JTextField();
		panel.add(tdUsuarioLlamar);
		tdUsuarioLlamar.setColumns(10);

		panel_1 = new JPanel();
		contentPane.add(panel_1);

    
		btnLlamar = new JButton("Conectar");

		btnLlamar.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				llamar(nomUsuario.toString());
			}
		});
		panel_1.add(btnLlamar);
	}

	protected void llamar(String nomUsuarioPrincipal) {
		if (!tdUsuarioLlamar.getText().replaceAll("\\s", "").isEmpty()) {
			escritura.println("ConnectTo " + tdUsuarioLlamar.getText().replaceAll("\\s", ""));
			escritura.flush();

			socketLlamada = null;
			PrintWriter mensajeLlamada = null;
			DataInputStream contestacionLlamada = null;

			try {
				String respuesta = lectura.readLine();
				System.out.println(respuesta);
				if (respuesta.startsWith("ok")) {
					String[] partes = respuesta.split(" ");


					//UTILIZAR AL QUITAR LOCALHOST
					
					String direccion = partes[1];


					socketLlamada = new Socket(direccion, Integer.parseInt(partes[2]));
					mensajeLlamada = new PrintWriter(new OutputStreamWriter(socketLlamada.getOutputStream()));
					contestacionLlamada = new DataInputStream(socketLlamada.getInputStream());

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
						//Socket socketChat = new Socket("localhost", Integer.parseInt((respuesta.split(" ")[1])));
						respuesta = contestacionLlamada.readLine();
						
						sonido.close();
						if (respuesta.startsWith("ok")) {
							ExecutorService pool = Executors.newCachedThreadPool();
							
							EventQueue.invokeLater(new Runnable() {
								public void run() {
									try {
										Chat frame = new Chat(socketLlamada,nomUsuarioPrincipal);
										AtenderChat atenderChat = new AtenderChat(frame,nomUsuario.toString());


										pool.execute(atenderChat);
										frame.setVisible(true);
									} catch (Exception e) {
										e.printStackTrace();
									}
								}
							});

						}
						else{
							if (respuesta.split(" ")[1].equals("501")) {
								JOptionPane.showMessageDialog(null, "Error: El usuario a conectar ha rechazado la peticion");

							}
						}
					} else {
						JOptionPane.showMessageDialog(null, "Error: Fallo al establecer la conexion");
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
