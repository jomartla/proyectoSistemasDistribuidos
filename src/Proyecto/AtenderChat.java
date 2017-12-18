package Proyecto;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileOutputStream;
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
		PrintWriter escribirRespuesta = null;
		
		try {		
			leerPeticion = new DataInputStream(cliente.getInputStream());
			DataInputStream leerFichero = new DataInputStream(cliente.getInputStream());
			escribirRespuesta = new PrintWriter(new OutputStreamWriter(cliente.getOutputStream()));
			
			while (!cliente.isClosed()) {
				String peticion = leerPeticion.readLine();
				
				if (peticion.startsWith("Escribir")){
					String aux = peticion.substring(peticion.indexOf(" ")+1);
					String mensaje = aux.substring(aux.indexOf(" ")+1);
					String nomUsuario = aux.substring(0, aux.indexOf(" "));
					escribir(nomUsuario,mensaje);
				}
				if (peticion.startsWith("Archivo")){
					String[] partes = peticion.split(" ");
					escribir(partes[1],"Recibiendo "+partes[2]);
					
					File f = new File("C:/"+partes[2]);
					
					
						escribirRespuesta.println("ok");
						escribirRespuesta.flush();
						
						DataOutputStream escribirFichero = new DataOutputStream(new FileOutputStream(f));
						
						byte[] buff = new byte[100];
						int leidos = leerFichero.read(buff);
						while(leidos!=-1){
							escribirFichero.write(buff,0,leidos);
							leidos=leerFichero.read(buff);
						}
						escribirFichero.flush();
					
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
