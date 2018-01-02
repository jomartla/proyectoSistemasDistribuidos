package InterfacesGraficas;

import Proyecto.AtenderChat;

import java.awt.*;

import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;
import javax.swing.*;
import javax.swing.border.*;
import java.awt.event.*;
import java.io.*;
import java.net.*;
import java.util.concurrent.*;


public class RecibirLlamada extends JFrame {

	private JPanel contentPane;
	private Socket socketLlamada;
	private Clip sonido;
	

	public RecibirLlamada(String nomUsuarioEntrante, Socket socketLlamada,  PrintWriter escribirRespuesta, String nombreUsuarioPrincipal) {
		
		 
		try {
			sonido = AudioSystem.getClip();
			sonido.open(AudioSystem.getAudioInputStream(new File("recibirllamada.wav")));
	        sonido.start();
		} catch (LineUnavailableException | IOException | UnsupportedAudioFileException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
        
		this.socketLlamada=socketLlamada;

		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 358, 156);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(new GridLayout(2, 0, 0, 0));
		

		JPanel panel_1 = new JPanel();
		contentPane.add(panel_1);
		
		JLabel lblNewLabel = new JLabel("Peticion de chat: ");

		panel_1.add(lblNewLabel);
		
		JLabel labelUsuarioLlamando = new JLabel(nomUsuarioEntrante);
		panel_1.add(labelUsuarioLlamando);
		
		JPanel panel = new JPanel();
		contentPane.add(panel);

		
		JButton btnColgar = new JButton("Rechazar");
		btnColgar.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				colgarLlamada(escribirRespuesta, sonido);
				
			}
		});
		panel.add(btnColgar);
		
		JButton btnRecibir = new JButton("Aceptar");

		btnRecibir.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				recibirLlamada(nomUsuarioEntrante,escribirRespuesta, nombreUsuarioPrincipal, sonido);
			}
		});
		panel.add(btnRecibir);
	}


	protected void colgarLlamada( PrintWriter escribirRespuesta, Clip sonido) {
		sonido.close();
		escribirRespuesta.println("error 501");	
		escribirRespuesta.flush();
		this.dispose();
		contentPane.setVisible(false);
	}


	protected void recibirLlamada(String nomUsuarioEntrante,  PrintWriter escribirRespuesta, String nomUsuarioPrincipal, Clip sonido) {
		sonido.close();
		escribirRespuesta.println("ok");
		escribirRespuesta.flush();
		ExecutorService pool = Executors.newCachedThreadPool();	
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					Chat frame = new Chat(socketLlamada, nomUsuarioPrincipal);
					AtenderChat atenderChat = new AtenderChat(frame, nomUsuarioEntrante);
					pool.execute(atenderChat);
					frame.setVisible(true);
					frame.setDefaultCloseOperation(frame.DISPOSE_ON_CLOSE);

					contentPane.setVisible(false);
					
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
		this.dispose();
		
	}

}
