package Proyecto;

import java.io.DataInputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.Socket;

import javax.swing.JTextArea;

import Cerrar.Cerrar;

public class AtenderChat implements Runnable {

	private Socket cliente;
	private JTextArea textArea;
	private String nomUsuario;
	
	
	public AtenderChat(Socket cliente, JTextArea textArea, String nomUsuario){
		this.cliente=cliente;
		this.textArea=textArea;
		this.nomUsuario=nomUsuario;
	}
	public void run() {
		DataInputStream leerPeticion = null;

		try {
			leerPeticion = new DataInputStream(cliente.getInputStream());
		
			while (!cliente.isClosed()) {
				textArea.append(nomUsuario+": "+leerPeticion.readLine());
			}
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			Cerrar.cerrar(leerPeticion);
		}
		
	}
	
}
