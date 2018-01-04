package Proyecto;


import Cerrar.Cerrar;
import InterfacesGraficas.RecibirLlamada;

import java.awt.*;
import java.io.*;
import java.net.*;
import java.util.concurrent.BrokenBarrierException;
import java.util.concurrent.CyclicBarrier;

public class AtenderPeticionCliente implements Runnable {

	Socket socketCliente;
	String estado;
	StringBuilder nomUsuario;
	int puertoChat;
	ServerSocket servidorChat;

	public AtenderPeticionCliente(Socket s, String est, StringBuilder nomUs) {
		socketCliente = s;
		estado = est;
		nomUsuario = nomUs;
	}

	/*
	 * Este hilo se ejectuta de forma continua recibiendo un tipo de peticion:
	 * 1: LLamada
	 */
	
	@Override
	public void run() {
		DataInputStream leerPeticion = null;
		PrintWriter escribirRespuesta = null;
		
		try {
			leerPeticion = new DataInputStream(socketCliente.getInputStream());
			escribirRespuesta = new PrintWriter(new OutputStreamWriter(socketCliente.getOutputStream()));
			
			while (!socketCliente.isClosed()) {
				String peticion = leerPeticion.readLine();
				
				if (peticion.startsWith("Llamada")) {
					peticionLlamada(peticion, escribirRespuesta, nomUsuario.toString());
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			Cerrar.cerrar(leerPeticion);
		}
	}
	
	//El metodo gestiona la peticion llamada, en la cual se ejecuta la interfaz grafica RecibirLlamada, la cual bloqueara la ejecucion hasta que se cierre esa ventana
	//Se bloque mediante el uso de CyclicBarrier
	private void peticionLlamada(String peticion, PrintWriter escribirRespuesta, String nomUsuario) {
		
		escribirRespuesta.println("ok " + puertoChat);
		escribirRespuesta.flush();
			
		CyclicBarrier cb = new CyclicBarrier(2);
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					RecibirLlamada frame = new RecibirLlamada(peticion.split(" ")[1], socketCliente, escribirRespuesta,nomUsuario);
					frame.setDefaultCloseOperation(RecibirLlamada.DISPOSE_ON_CLOSE);
					frame.setVisible(true);
					frame.addWindowListener(new java.awt.event.WindowAdapter() {

					    public void windowClosing(java.awt.event.WindowEvent windowEvent) {
					       try {
							cb.await();
						} catch (InterruptedException | BrokenBarrierException e) {

							e.printStackTrace();
						}
					    }
					});
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
		try {
			cb.await();
		} catch (InterruptedException | BrokenBarrierException e) {
			e.printStackTrace();
		}
			
		
		
	}
}
