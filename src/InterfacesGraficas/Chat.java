package InterfacesGraficas;

import java.awt.BorderLayout;
import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

import Cerrar.Cerrar;
import Proyecto.AtenderChat;
import Proyecto.AtenderPeticionCliente;

import javax.swing.JScrollPane;
import javax.swing.GroupLayout;
import javax.swing.GroupLayout.Alignment;
import javax.swing.JTextArea;
import javax.swing.DropMode;
import javax.swing.LayoutStyle.ComponentPlacement;
import javax.swing.JTextField;
import javax.swing.JButton;
import java.awt.event.ActionListener;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.CyclicBarrier;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.awt.event.ActionEvent;

public class Chat extends JFrame {

	private JPanel contentPane;
	private JTextField textField;
	private JTextArea textArea;
	private Socket socketLlamada;
	private ServerSocket recibirMensajes;
	private PrintWriter escribirLineaSocket;
	private ServerSocket servidorRecibirChat;
	private String nomUsuario;
	private DataInputStream recibirRespuesta;
	private DataOutputStream escribirArchivo;

	public Chat(Socket socketLlamada, String nomUsuario) {
		setResizable(false);
		this.nomUsuario = nomUsuario;
		this.socketLlamada = socketLlamada;

		try {

			escribirLineaSocket = new PrintWriter(new OutputStreamWriter(socketLlamada.getOutputStream()));
			recibirRespuesta = new DataInputStream(socketLlamada.getInputStream());
	
			setTitle("Chat - "+nomUsuario);
			setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
			setBounds(100, 100, 443, 241);
			contentPane = new JPanel();
			contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
			setContentPane(contentPane);
			
			JScrollPane scrollPane = new JScrollPane();
			
			textField = new JTextField();
			textField.setColumns(10);
			
			JButton btnEnviar = new JButton("Enviar");
			btnEnviar.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					enviar();
				}
			});
			
			JButton btnNewButton = new JButton("Adjuntar Archivo");
			btnNewButton.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent arg0) {
					enviarArchivo();
				}
			});
			textArea = new JTextArea();
			scrollPane.setViewportView(textArea);
			GroupLayout gl_contentPane = new GroupLayout(contentPane);
			gl_contentPane.setHorizontalGroup(
				gl_contentPane.createParallelGroup(Alignment.LEADING)
					.addGroup(gl_contentPane.createSequentialGroup()
						.addGap(10)
						.addGroup(gl_contentPane.createParallelGroup(Alignment.LEADING)
							.addComponent(scrollPane, GroupLayout.PREFERRED_SIZE, 400, GroupLayout.PREFERRED_SIZE)
							.addGroup(gl_contentPane.createSequentialGroup()
								.addComponent(textField, GroupLayout.PREFERRED_SIZE, 200, GroupLayout.PREFERRED_SIZE)
								.addGap(10)
								.addComponent(btnEnviar)
								.addGap(6)
								.addComponent(btnNewButton, GroupLayout.PREFERRED_SIZE, 121, GroupLayout.PREFERRED_SIZE))))
			);
			gl_contentPane.setVerticalGroup(
				gl_contentPane.createParallelGroup(Alignment.LEADING)
					.addGroup(gl_contentPane.createSequentialGroup()
						.addGap(11)
						.addComponent(scrollPane, GroupLayout.PREFERRED_SIZE, 145, GroupLayout.PREFERRED_SIZE)
						.addGap(6)
						.addGroup(gl_contentPane.createParallelGroup(Alignment.LEADING)
							.addGroup(gl_contentPane.createSequentialGroup()
								.addGap(1)
								.addComponent(textField, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
							.addComponent(btnEnviar)
							.addComponent(btnNewButton)))
			);
			contentPane.setLayout(gl_contentPane);

		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		
	}
	protected void enviarArchivo() {
		
		DataInputStream leerArchivo = null;
		try {
		String nomArchivo = textField.getText().replaceAll("//s", "");
		File f = new File(nomArchivo);
		if(f.exists()){
			if(f.isFile()){
				
				escribir("Me","Enviando archivo");
				escribirLineaSocket.println("Archivo "+ nomUsuario + " " + f.getName() +" "+ f.length());
				escribirLineaSocket.flush();
				
				
					String respuesta = recibirRespuesta.readLine();
					
					if(respuesta.startsWith("ok")){
						
						leerArchivo = new DataInputStream(new FileInputStream(f));
						escribirArchivo = new DataOutputStream(socketLlamada.getOutputStream());
						
						byte[] buff = new byte[100];
						int leidos = leerArchivo.read(buff);
						while(leidos!=-1){
							escribirArchivo.write(buff,0,leidos);
							leidos=leerArchivo.read(buff);
						}
						escribirArchivo.flush();
						escribir("Me","Archivo enviado con éxito");
					} else {
						JOptionPane.showMessageDialog(null, "El archivo no puede ser enviado");
					}
				
				
			}else{
				JOptionPane.showMessageDialog(null, "El elemento a enviar no es un archivo válido");
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
	protected void enviar() {
		escribir("Me", textField.getText());
		escribirLineaSocket.println("Escribir " +  nomUsuario + " " + textField.getText());
		escribirLineaSocket.flush();
		textField.setText("");		
	}
	public Socket getSocketLlamada(){
		return this.socketLlamada;
	}
	
	public void escribir(String nomUsuario, String mensaje){
		textArea.append(nomUsuario+": " + mensaje +"\n");
	}
}
