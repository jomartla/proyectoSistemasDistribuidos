package Proyecto;

import java.awt.EventQueue;
import java.io.DataInputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.BrokenBarrierException;
import java.util.concurrent.CyclicBarrier;

import Cerrar.Cerrar;
import InterfacesGraficas.RecibirLlamada;

public class AtenderPeticionCliente implements Runnable {

	Socket socketCliente;
	String estado;
	StringBuilder nomUsuario;
	static  int puertoChat=13000;

	public AtenderPeticionCliente(Socket s, String est, StringBuilder nomUs) {
		socketCliente = s;
		estado = est;
		nomUsuario = nomUs;
	}

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
					peticionLlamada(peticion, escribirRespuesta);
				}
				
			}
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			Cerrar.cerrar(leerPeticion);
		}
		
	}

	private void peticionLlamada(String peticion, PrintWriter escribirRespuesta) {
		
		if(estado.equals("ocupado")){
			escribirRespuesta.println("error 501");
			escribirRespuesta.flush();
		}
		else{
			ServerSocket servidorChat = null;
			
			try {
				servidorChat = new ServerSocket(puertoChat);
				escribirRespuesta.println("ok " + puertoChat);
				escribirRespuesta.flush();
				puertoChat++;
				
				Socket socketChat = servidorChat.accept();
				EventQueue.invokeLater(new Runnable() {
					public void run() {
						try {
							RecibirLlamada frame = new RecibirLlamada(peticion.split(" ")[1], socketChat);
							frame.setVisible(true);
						} catch (Exception e) {
							e.printStackTrace();
						}
					}
				});
			} catch (IOException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			
		}
		
	}
}
