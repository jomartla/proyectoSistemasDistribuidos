package InterfacesGraficas;

import java.awt.BorderLayout;
import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

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
	

	public Chat(Socket socketLlamada, String nomUsuario) {
		

		//AQUÍ INICIAMOS UN ATENDERCHAT 
		try {
			//Mismo puerto que el del socketLlamada
			servidorRecibirChat=new ServerSocket(11000);
			
			this.socketLlamada = socketLlamada;
			escribirLineaSocket = new PrintWriter(new OutputStreamWriter(socketLlamada.getOutputStream()));
	
			setTitle("Chat");
			setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
			setBounds(100, 100, 456, 338);
			contentPane = new JPanel();
			contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
			setContentPane(contentPane);
			
			JScrollPane scrollPane = new JScrollPane();
			
			textField = new JTextField();
			textField.setColumns(10);
			
			JButton btnEnviar = new JButton("Enviar");
			btnEnviar.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					textArea.append("Me: " + textField.getText()+"\n");
					escribirLineaSocket.println(textField.getText());
					textField.setText("");
				}
			});
			GroupLayout gl_contentPane = new GroupLayout(contentPane);
			gl_contentPane.setHorizontalGroup(
				gl_contentPane.createParallelGroup(Alignment.LEADING)
					.addGroup(gl_contentPane.createSequentialGroup()
						.addContainerGap()
						.addGroup(gl_contentPane.createParallelGroup(Alignment.LEADING)
							.addComponent(scrollPane, GroupLayout.DEFAULT_SIZE, 415, Short.MAX_VALUE)
							.addGroup(gl_contentPane.createSequentialGroup()
								.addComponent(textField, GroupLayout.DEFAULT_SIZE, 329, Short.MAX_VALUE)
								.addPreferredGap(ComponentPlacement.UNRELATED)
								.addComponent(btnEnviar)
								.addGap(13)))
						.addGap(5))
			);
			gl_contentPane.setVerticalGroup(
				gl_contentPane.createParallelGroup(Alignment.LEADING)
					.addGroup(Alignment.TRAILING, gl_contentPane.createSequentialGroup()
						.addContainerGap()
						.addComponent(scrollPane, GroupLayout.DEFAULT_SIZE, 162, Short.MAX_VALUE)
						.addPreferredGap(ComponentPlacement.UNRELATED)
						.addGroup(gl_contentPane.createParallelGroup(Alignment.BASELINE)
							.addComponent(textField, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
							.addComponent(btnEnviar))
						.addGap(82))
			);
			textArea = new JTextArea();
			scrollPane.setViewportView(textArea);
			contentPane.setLayout(gl_contentPane);
			
			while (true){
				
				final Socket cliente = servidorRecibirChat.accept();
				
				ExecutorService pool = Executors.newCachedThreadPool();	

				AtenderChat atenderChat = new AtenderChat(cliente, textArea, nomUsuario);

				pool.execute(atenderChat);
				
			}
			
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		
		
		
	}
}
