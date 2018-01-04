package Proyecto;

import Cerrar.Cerrar;
import InterfacesGraficas.Chat;

import java.io.*;
import java.net.*;


public class AtenderPeticionChat implements Runnable {

	private Socket cliente;
	private Chat chat;
	private DataInputStream leerPeticion = null;

	public AtenderPeticionChat(Chat chat){
		this.cliente=chat.getSocketLlamada();
		this.chat=chat;
		this.leerPeticion = chat.getDataInputStream();
	}
	
	/*
	 * Este hilo se ejectuta de forma continua recibiendo 3 tipos de peticiones:
	 * 1: Escribir un mensaje en el chat
	 * 2: El otro usuario se ha desconectado del chat
	 * 3: El otro usuario ha enviado un archivo
	 */
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
	
	//El metodo escribe en el chat el mensaje, indicando cual es el usuario
	private void escribir(String nomUsuario, String mensaje) {
		chat.escribir(nomUsuario, mensaje);	
	}
	
	//El metodo trata la peticion de escribir, sacando el nombre de usuario y el mensaje que han enviado
	private void reciboEscribir(String peticion){
		String aux = peticion.substring(peticion.indexOf(" ")+1);
		String mensaje = aux.substring(aux.indexOf(" ")+1);
		String nomUsuario = aux.substring(0, aux.indexOf(" "));
		escribir(nomUsuario,mensaje);
	}
	
	//El metodo trata la peticion de desconectar, y deshabilita el cuadro para escribir y los botones
	private void reciboDesconectar(){
		this.chat.desactivar();
	}
	
	
	//El metodo tarta la peticion de recibir archivo, y comienza el proceso para recibirlo, se guarda por defecto en la ruta C:\
	private void reciboArchivo(String peticion){
		
		DataOutputStream escribirFichero = null;
		
		try {
			
			String[] partes = peticion.split(" ");
			escribir(partes[1],"Recibiendo "+partes[2]);
			
			File f = new File("C:/"+partes[2]);
			escribirFichero = new DataOutputStream(new FileOutputStream(f));
			
			int tamanoFichero = Integer.parseInt(partes[3]);
			
			
			byte[] buff = new byte[100];
					
			int leidos = leerPeticion.read(buff);
			tamanoFichero = tamanoFichero - leidos;
			
			while(tamanoFichero>0){
				escribirFichero.write(buff,0,leidos);
				leidos=leerPeticion.read(buff);
				tamanoFichero = tamanoFichero - leidos;
			}

			


			Cerrar.cerrar(escribirFichero);
			
			escribir("Me","Archivo recibido con Exito");
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}

				
		
	}
	
	
}
