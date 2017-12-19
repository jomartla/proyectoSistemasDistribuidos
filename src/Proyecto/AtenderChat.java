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
	private DataInputStream leerPeticion = null;
	private PrintWriter escribirRespuesta = null;
	
	
	public AtenderChat(Chat chat, String nomUsuario){
		this.cliente=chat.getSocketLlamada();
		this.chat=chat;
		this.nomUsuario=nomUsuario;
		this.leerPeticion = chat.getDataInputStream();
		this.escribirRespuesta = chat.getPrintWriter();
	}
	public void run() {
		
		DataOutputStream escribirFichero = null;
		
		try {		
			
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
					
					escribirFichero = new DataOutputStream(new FileOutputStream(f));
							
					int tamanoFichero = Integer.parseInt(partes[3]);
					
					
					byte[] buff = new byte[100];
					
					int partesEnteras = tamanoFichero/100;
					
					int leidos = leerPeticion.read(buff);
					
					while(partesEnteras>0){
						escribirFichero.write(buff,0,leidos);
						leidos=leerPeticion.read(buff);
						
     					partesEnteras--;
					}
					
					
      				escribirFichero.write(buff,0,leidos);

					Cerrar.cerrar(escribirFichero);
					
					escribir("Me","Archivo recibido con éxito");
				}
		
			}
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			Cerrar.cerrar(escribirFichero);
		}
		
	}
	
	private void escribir(String nomUsuario, String mensaje) {
		chat.escribir(nomUsuario, mensaje);
		
	}
	
}
