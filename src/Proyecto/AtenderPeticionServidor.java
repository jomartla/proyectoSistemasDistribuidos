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
//AQUÍ JUANJO			
			else if (peticion.startsWith("Get")){
				peticionGet(peticion, escribirRespuesta);
			} else if (peticion.startsWith("Add")){
				peticionAdd(peticion, escribirRespuesta);
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
//TO STRING E INETADRESS PUEDEN DAR PROBLEMAS
					usuarios.get(partes[1]).setDireccion(socketCliente.getInetAddress().toString());
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
	public void peticionGet(String linea, BufferedWriter escribirRespuesta) {
		try{
			String[] partes = linea.split(" ");
			
			if(usuarios.containsKey(partes[1])){	
					escribirRespuesta.write("ok " + usuarios.get(partes[1]).getNombreReal() + " " + usuarios.get(partes[1]).getDireccion());
			} else {
				escribirRespuesta.write("error 406");
			}
		} catch (IOException e){
			e.printStackTrace();
		}
	}
	public void peticionAdd(String linea, BufferedWriter escribirRespuesta) {
		try{
			String[] partes = linea.split(" ");
			
			if(usuarios.containsKey(partes[1])){	
					escribirRespuesta.write("error 411");
			} else {
				if(partes[2].length()<4){
					escribirRespuesta.write("error 412");
				}else {
//POSIBLE PROBLEMA CON INETADRESS
					Usuario nuevoUsuario = new Usuario(partes[3],partes[1], partes[2], socketCliente.getInetAddress().toString());
					usuarios.put(partes[1], nuevoUsuario);
					escribirRespuesta.write("ok");
				}
			}
		} catch (IOException e){
			e.printStackTrace();
		}
	}
}
