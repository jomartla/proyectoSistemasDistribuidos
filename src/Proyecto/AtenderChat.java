package Proyecto;

import java.io.DataInputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.Socket;

import javax.swing.JTextArea;

import Cerrar.Cerrar;
import InterfacesGraficas.Chat;

public class AtenderChat implements Runnable {

	private Socket cliente;
	private Chat chat;
	private String nomUsuario;
	
	
	public AtenderChat(Socket cliente, Chat chat, String nomUsuario){
		this.cliente=cliente;
		this.chat=chat;
		this.nomUsuario=nomUsuario;
	}
	public void run() {
		DataInputStream leerPeticion = null;
		
		try {		
			leerPeticion = new DataInputStream(cliente.getInputStream());
			
			while (!cliente.isClosed()) {
				String peticion = leerPeticion.readLine();
				
				if (peticion.startsWith("Escribir")){
					String[] partes = peticion.split(" ");
					escribir(partes[1],partes[2]);
				}
				
				
				
				
			}
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			Cerrar.cerrar(leerPeticion);
		}
		
	}
	
	private void escribir(String nomUsuario, String mensaje) {
		chat.escribir(nomUsuario, mensaje);
		
	}
	
}
