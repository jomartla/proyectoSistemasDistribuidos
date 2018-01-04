package InterfacesGraficas;

import Cerrar.Cerrar;

import javax.swing.*;
import javax.swing.GroupLayout.*;
import javax.swing.border.*;
import java.awt.event.*;
import java.io.*;
import java.net.Socket;

public class Chat extends JFrame {

	private JPanel contentPane;
	private JTextField textField;
	private JTextArea textArea;
	private Socket socketConexionChat;
	private PrintWriter escribirLineaSocket;
	private String nomUsuario;
	private DataInputStream recibirRespuesta;
	private DataOutputStream escribirArchivo;
	private JButton btnEnviar;
	private JButton btnAdjuntar;

	/*Una vez aceptada la llamada. Se lanza la interfaz del chat (esta). 
	 A esta clase se le pasa el nombre de usuario del cliente con el que nos estamos conectando y el propio socket de dicha conexion.
	 
	 En el constructor creamos la interfaz.
	*/
	public Chat(Socket socketConexionChat, String nomUsuario) {

		setResizable(false);
		this.nomUsuario = nomUsuario;
		this.socketConexionChat = socketConexionChat;

		try {
			escribirLineaSocket = new PrintWriter(new OutputStreamWriter(socketConexionChat.getOutputStream()));
			recibirRespuesta = new DataInputStream(socketConexionChat.getInputStream());

			setTitle("Chat - " + nomUsuario);
			setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

			setBounds(100, 100, 443, 241);
			contentPane = new JPanel();
			contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
			
			setContentPane(contentPane);

			JScrollPane scrollPane = new JScrollPane();

			textField = new JTextField();
			textField.addKeyListener(new PresionarEnter());
			textField.setColumns(10);
			
			this.addWindowListener(new java.awt.event.WindowAdapter() {
			    public void windowClosing(java.awt.event.WindowEvent windowEvent) {
			       desconectar(); 
			    }
			});


			btnEnviar = new JButton("Enviar");
			btnEnviar.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					enviar();
				}
			});
			

			btnAdjuntar = new JButton("Adjuntar Archivo");
			btnAdjuntar.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent arg0) {
					enviarArchivo();
				}
			});
			
			
			textArea = new JTextArea();
			textArea.append("---------- CHAT INICIADO: "+nomUsuario+ " ------------\n");

			textArea.setEditable(false);
			scrollPane.setViewportView(textArea);
			GroupLayout gl_contentPane = new GroupLayout(contentPane);
			gl_contentPane.setHorizontalGroup(gl_contentPane.createParallelGroup(Alignment.LEADING)
					.addGroup(gl_contentPane.createSequentialGroup().addGap(10).addGroup(gl_contentPane
							.createParallelGroup(Alignment.LEADING)
							.addComponent(scrollPane, GroupLayout.PREFERRED_SIZE, 400, GroupLayout.PREFERRED_SIZE)
							.addGroup(gl_contentPane.createSequentialGroup()
									.addComponent(textField, GroupLayout.PREFERRED_SIZE, 200,
											GroupLayout.PREFERRED_SIZE)
									.addGap(10).addComponent(btnEnviar).addGap(6).addComponent(btnAdjuntar,
											GroupLayout.PREFERRED_SIZE, 121, GroupLayout.PREFERRED_SIZE)))));
			gl_contentPane.setVerticalGroup(gl_contentPane.createParallelGroup(Alignment.LEADING)
					.addGroup(gl_contentPane.createSequentialGroup().addGap(11)
							.addComponent(scrollPane, GroupLayout.PREFERRED_SIZE, 145, GroupLayout.PREFERRED_SIZE)
							.addGap(6)
							.addGroup(gl_contentPane.createParallelGroup(Alignment.LEADING)
									.addGroup(gl_contentPane.createSequentialGroup().addGap(1).addComponent(textField,
											GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE,
											GroupLayout.PREFERRED_SIZE))
									.addComponent(btnEnviar).addComponent(btnAdjuntar))));

			contentPane.setLayout(gl_contentPane);

		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		
	}
	//Mediante este método enviamos automáticamente lo que esté en el TextField al otro usuario.
	public class PresionarEnter extends KeyAdapter {
	      public void keyPressed(KeyEvent ke) {
	          if (ke.getKeyCode() == KeyEvent.VK_ENTER) {
	              enviar();
	          }
	      }
	}
	/*
	 * En este método enviamos un archivo siguiendo los siguientes pasos:
	 * 1. Recogemos la ruta del archivo desde el campo donde escribe el usuario.
	 * 2. Comprobamos que la ruta es valida y que el elemento a enviar lo es tambien.
	 * 3. Si todo es correcto, enviamos la petición Archivo junto al nombre de usuario, el nombre del fichero y el tamaño.
	 * 4. Acto seguido enviamos el fichero byte a byte.
	 */
	protected void enviarArchivo() {

		DataInputStream leerArchivo = null;
		try {
			String nomArchivo = textField.getText().replaceAll("//s", "");
			File f = new File(nomArchivo);
			if (f.exists()) {
				if (f.isFile()) {

					escribir("Me", "Enviando archivo");
					escribirLineaSocket.println("Archivo " + nomUsuario + " " + f.getName() + " " + f.length());
					escribirLineaSocket.flush();

					leerArchivo = new DataInputStream(new FileInputStream(f));
					escribirArchivo = new DataOutputStream(socketConexionChat.getOutputStream());

					byte[] buff = new byte[100];
					int leidos = leerArchivo.read(buff);
					while (leidos != -1) {
						escribirArchivo.write(buff, 0, leidos);
						leidos = leerArchivo.read(buff);
					}
					escribirArchivo.flush();

					Cerrar.cerrar(leerArchivo);

					escribir("Me", "Archivo enviado con Exito");

				} else {
					JOptionPane.showMessageDialog(null, "El elemento a enviar no es un archivo valido");
				}
			} else {
				JOptionPane.showMessageDialog(null, "Ruta no especificada");
			}
			textField.setText("");

		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			Cerrar.cerrar(leerArchivo);
		}
	}
	//Este método comprueba que el campo donde escribe el usuario no está vacío.
	//Y si no lo está, escribe el contenido en su propio chat, y envía una petición de escribir al otro usuario.
	protected void enviar() {
		if(textField.getText().equals("")){
			
		}else{
			escribir("Me", textField.getText());
			escribirLineaSocket.println("Escribir " + nomUsuario + " " + textField.getText());
			escribirLineaSocket.flush();
			textField.setText("");
		}
	}
	//Este método escribe que el chat ha finalizado al otro usuario y después envía la petición de desconectar.
	protected void desconectar() {
		escribirLineaSocket.println("Escribir " + "---------- CHAT FINALIZADO: "+nomUsuario+ " ------------ \n");
		escribirLineaSocket.println("Desconectar "+ nomUsuario);
		escribirLineaSocket.flush();
		
		Cerrar.cerrar(socketConexionChat);
	}
	//Cuando recibimos una petición de desconexión, se llama a este método para que no se pueda seguir utilizando el chat.
	public void desactivar(){
		textField.setEditable(false);
		btnAdjuntar.setEnabled(false);
		btnEnviar.setEnabled(false);
	}
	//A continuación tenemos diversos gets utilizados en otras clases.
	public Socket getSocketLlamada() {
		return this.socketConexionChat;
	}

	public void escribir(String nomUsuario, String mensaje) {
		textArea.append(nomUsuario + ": " + mensaje + "\n");
	}

	public DataInputStream getDataInputStream() {
		return recibirRespuesta;
	}

	public PrintWriter getPrintWriter() {
		return escribirLineaSocket;

	}

}
