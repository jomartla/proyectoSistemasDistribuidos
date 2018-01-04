package Proyecto;

import Cerrar.Cerrar;
import InterfacesGraficas.Chat;

import java.io.*;
import java.net.*;


public class AtenderChat implements Runnable {

	private Socket cliente;
	private Chat chat;
	private DataInputStream leerPeticion = null;

	public AtenderChat(Chat chat){
		this.cliente=chat.getSocketLlamada();
		this.chat=chat;
		this.leerPeticion = chat.getDataInputStream();
	}
	public void run() {
		try {		
			while (!cliente.isClosed()) {
				String peticion = leerPeticion.readLine();
				
				if (peticion.startsWith("Escribir")){
					reciboEscribir(peticion);
				}else if (peticion.startsWith("Desconectar")){
					reciboDesconectar();
				}else if (peticion.startsWith("Archivo")){
					reciboArchivo(peticion);
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
		} 
	}
	private void escribir(String nomUsuario, String mensaje) {
		chat.escribir(nomUsuario, mensaje);	
	}
	private void reciboEscribir(String peticion){
		String aux = peticion.substring(peticion.indexOf(" ")+1);
		String mensaje = aux.substring(aux.indexOf(" ")+1);
		String nomUsuario = aux.substring(0, aux.indexOf(" "));
		escribir(nomUsuario,mensaje);
	}
	private void reciboDesconectar(){
		this.chat.desactivar();
	}
	private void reciboArchivo(String peticion){
		
		DataOutputStream escribirFichero = null;
		
		try {
			
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
			
			escribir("Me","Archivo recibido con Exito");
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}

				
		
	}
	
	
}
