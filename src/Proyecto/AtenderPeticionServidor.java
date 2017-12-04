package Proyecto;

import java.io.*;
import java.net.*;
import java.util.*;
import Cerrar.Cerrar;

public class AtenderPeticionServidor implements Runnable {

	private Socket socketCliente;
	private HashMap<String,Usuario> usuarios;
	
	public AtenderPeticionServidor(Socket socketCliente, HashMap<String,Usuario> usuarios){
		this.socketCliente = socketCliente;	
		this.usuarios=usuarios;
	}
	public void run(){
		
		DataInputStream leerPeticion = null;
		BufferedWriter escribirRespuesta = null;
		
		try{
			leerPeticion = new DataInputStream(socketCliente.getInputStream());
			escribirRespuesta = new BufferedWriter(new OutputStreamWriter(socketCliente.getOutputStream()));
			
			String peticion = leerPeticion.readLine();
			
			if (peticion.startsWith("Login")){
				peticionLogin(peticion, escribirRespuesta);
			}
			
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			Cerrar.cerrar(leerPeticion);
		}
		
	}
	public void peticionLogin(String linea, BufferedWriter escribirRespuesta) {
		try{
			String[] partes = linea.split(" ");
			
			if(usuarios.containsKey(partes[1])){
				if(usuarios.get(partes[1]).getContrasena().equals(partes[2])){
					escribirRespuesta.write("ok");
				}else{
					escribirRespuesta.write("error 402");
				}
			} else {
				escribirRespuesta.write("error 401");
			}
		} catch (IOException e){
			e.printStackTrace();
		}
	}
}
