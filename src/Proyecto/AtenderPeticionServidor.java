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
		PrintWriter escribirRespuesta = null;
		
		try{
			leerPeticion = new DataInputStream(socketCliente.getInputStream());
			escribirRespuesta = new PrintWriter(new OutputStreamWriter(socketCliente.getOutputStream()));
			
			String peticion = leerPeticion.readLine();
			
			if (peticion.startsWith("Login")){
				peticionLogin(peticion, escribirRespuesta);
			} else if (peticion.startsWith("Get")){
				peticionGet(peticion, escribirRespuesta);
			} else if (peticion.startsWith("Add")){
				peticionAdd(peticion, escribirRespuesta);
			} else if (peticion.startsWith("ConnectTo")){
				peticionConnectToSomeone(peticion, escribirRespuesta);
			} else if (peticion.startsWith("Disconnect")){
				peticionDisconnect(peticion,escribirRespuesta);
			} else if (peticion.startsWith("Connect")){
				peticionConnect(peticion,escribirRespuesta);
			}
			
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			Cerrar.cerrar(leerPeticion);
		}
		
	}
	public void peticionLogin(String linea, PrintWriter escribirRespuesta) {
		
			String[] partes = linea.split(" ");
			
			if(usuarios.containsKey(partes[1])){
				if(usuarios.get(partes[1]).getContrasena().equals(partes[2])){
//TO STRING E INETADRESS PUEDEN DAR PROBLEMAS
					usuarios.get(partes[1]).setDireccion(socketCliente.getInetAddress().toString());
					escribirRespuesta.println("ok");						
				}else{
					escribirRespuesta.println("error 402");
				}
			} else {
				escribirRespuesta.println("error 401");
			}
			escribirRespuesta.flush();
		
	}
	public void peticionGet(String linea, PrintWriter escribirRespuesta) {
	
			String[] partes = linea.split(" ");
			
			if(usuarios.containsKey(partes[1])){	
					escribirRespuesta.println("ok " + usuarios.get(partes[1]).getNombreReal() + " " + usuarios.get(partes[1]).getDireccion());
			} else {
				escribirRespuesta.println("error 406");
			}
			escribirRespuesta.flush();
	}
	public void peticionAdd(String linea, PrintWriter escribirRespuesta) {
		
			String[] partes = linea.split(" ");
			
			if(usuarios.containsKey(partes[1])){	
					escribirRespuesta.println("error 411");
			} else {
				if(partes[2].length()<4){
					escribirRespuesta.println("error 412");
				}else {
//POSIBLE PROBLEMA CON INETADRESS
					Usuario nuevoUsuario = new Usuario(partes[3],partes[1], partes[2], socketCliente.getInetAddress().toString());
					usuarios.put(partes[1], nuevoUsuario);
					escribirRespuesta.println("ok");
				}
			}
			escribirRespuesta.flush();
	}
	public void peticionConnectToSomeone(String linea, PrintWriter escribirRespuesta) {
		
		String[] partes = linea.split(" ");
		
		if(usuarios.containsKey(partes[1])){	
				if (usuarios.get(partes[1]).getDireccion().equals("")){
					escribirRespuesta.println("error 416");
				} else{
					escribirRespuesta.println("ok "+usuarios.get(partes[1]).getDireccion());
				}
		} else {
			escribirRespuesta.println("error 417");
		}
		escribirRespuesta.flush();
	}
	public void peticionDisconnect(String linea, PrintWriter escribirRespuesta) {
		
		String[] partes = linea.split(" ");
		
		if(usuarios.containsKey(partes[1])){	
				if (usuarios.get(partes[1]).getDireccion().equals(socketCliente.getInetAddress().toString())){
					usuarios.get(partes[1]).setDireccion("");
					escribirRespuesta.println("ok");
				} else{
					escribirRespuesta.println("error 421");
				}
		} else {
			escribirRespuesta.println("error 422");
		}
		escribirRespuesta.flush();
	}
	public void peticionConnect(String linea, PrintWriter escribirRespuesta) {
		
		String[] partes = linea.split(" ");
		
		if(usuarios.containsKey(partes[1])){	
				if (usuarios.get(partes[1]).getDireccion().equals(socketCliente.getInetAddress().toString())){
					usuarios.get(partes[1]).setDireccion(socketCliente.getInetAddress().toString());
					escribirRespuesta.println("ok");
				} else{
					escribirRespuesta.println("error 426");
				}
		} else {
			escribirRespuesta.println("error 427");
		}
		escribirRespuesta.flush();
	
	}
}
