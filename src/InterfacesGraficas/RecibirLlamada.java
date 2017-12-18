package InterfacesGraficas;

import java.awt.BorderLayout;
import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

import Proyecto.AtenderChat;

import java.awt.GridLayout;
import javax.swing.JLabel;
import javax.swing.JButton;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.awt.event.ActionEvent;

public class RecibirLlamada extends JFrame {

	private JPanel contentPane;
	private Socket socketLlamada;
	

	public RecibirLlamada(String nomUsuarioEntrante, Socket socketLlamada,  PrintWriter escribirRespuesta) {
		
		
		this.socketLlamada=socketLlamada;
		

		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 358, 156);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(new GridLayout(2, 0, 0, 0));
		
		JPanel panel_1 = new JPanel();
		contentPane.add(panel_1);
		
		JLabel lblNewLabel = new JLabel("Llamada entrante de: ");
		panel_1.add(lblNewLabel);
		
		JLabel labelUsuarioLlamando = new JLabel(nomUsuarioEntrante);
		panel_1.add(labelUsuarioLlamando);
		
		JPanel panel = new JPanel();
		contentPane.add(panel);
		
		JButton btnColgar = new JButton("Colgar");
		btnColgar.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				colgarLlamada(escribirRespuesta);
			}
		});
		panel.add(btnColgar);
		
		JButton btnRecibir = new JButton("Recibir");
		btnRecibir.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				recibirLlamada(nomUsuarioEntrante,escribirRespuesta);
			}
		});
		panel.add(btnRecibir);
		
		
	}


	protected void colgarLlamada( PrintWriter escribirRespuesta) {
		escribirRespuesta.println("error 501");	
		escribirRespuesta.flush();
		this.dispose();
	}


	protected void recibirLlamada(String nomUsuarioEntrante,  PrintWriter escribirRespuesta) {
		escribirRespuesta.println("ok");
		escribirRespuesta.flush();
		ExecutorService pool = Executors.newCachedThreadPool();	
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					Chat frame = new Chat(socketLlamada, nomUsuarioEntrante);
					AtenderChat atenderChat = new AtenderChat(socketLlamada, frame, nomUsuarioEntrante);
					pool.execute(atenderChat);
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
		this.dispose();
		
	}

}
