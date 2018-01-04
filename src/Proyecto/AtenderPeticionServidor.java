package Proyecto;

import java.io.*;
import java.net.*;
import java.util.*;

import Cerrar.Cerrar;

public class AtenderPeticionServidor implements Runnable {

	private Socket socketCliente;
	private static HashMap<String, Usuario> usuarios;

	public AtenderPeticionServidor(Socket socketCliente, HashMap<String, Usuario> usuarios) {
		this.socketCliente = socketCliente;
		this.usuarios = usuarios;
	}
	
	 /* Este hilo se ejectuta de forma continua recibiendo 6 tipos de peticiones:
	 * 1: Logearse
	 * 2: Obtener un usuario
	 * 3: Añadir un usuario
	 * 4: Conectarse a un usuario
	 * 5: Desconectarse
	 * 6: Conectarse
	 */

	public void run() {

		DataInputStream leerPeticion = null;
		PrintWriter escribirRespuesta = null;

		
		try {
			leerPeticion = new DataInputStream(socketCliente.getInputStream());
			escribirRespuesta = new PrintWriter(new OutputStreamWriter(socketCliente.getOutputStream()));

			while (!socketCliente.isClosed()) {
				String peticion = leerPeticion.readLine();

				if (peticion.startsWith("Login")) {
					peticionLogin(peticion, escribirRespuesta);
				} else if (peticion.startsWith("Get")) {
					peticionGet(peticion, escribirRespuesta);
				} else if (peticion.startsWith("Add")) {
					peticionAdd(peticion, escribirRespuesta);
				} else if (peticion.startsWith("ConnectTo")) {
					peticionConnectToSomeone(peticion, escribirRespuesta);
				} else if (peticion.startsWith("Disconnect")) {
					peticionDisconnect(peticion, escribirRespuesta);
				} else if (peticion.startsWith("Connect")) {
					peticionConnect(peticion, escribirRespuesta);
				}
			}
		} catch (SocketException e) {
			System.out.println("Se ha desconectado un cliente");

		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			Cerrar.cerrar(leerPeticion);
		}

	}

	//El metodo gestiona la peticion de login, donde se comprueba que la contraseña corresponde con la del usuario registrado
	//admas fija la direccion y el puerto que usa ese usuario.
	//En caso de que no coincida enviara un error.
	public void peticionLogin(String linea, PrintWriter escribirRespuesta) {

		String[] partes = linea.split(" ");

		if (usuarios.containsKey(partes[1])) {
			if (usuarios.get(partes[1]).getContrasena().equals(partes[2])) {
				usuarios.get(partes[1]).setDireccion(socketCliente.getInetAddress().toString().substring(1));
				usuarios.get(partes[1]).setPuerto(Integer.parseInt(partes[3]));

				escribirRespuesta.println("ok");
			} else {
				escribirRespuesta.println("error 402");
			}
		} else {
			escribirRespuesta.println("error 401");
		}
		escribirRespuesta.flush();

	}

	//El metodo gestiona la peticion Get, en la cual se devolvera el nombre real y la direccion del usuario a buscar
	public void peticionGet(String linea, PrintWriter escribirRespuesta) {

		String[] partes = linea.split(" ");

		if (usuarios.containsKey(partes[1])) {
			escribirRespuesta.println(
					"ok " + usuarios.get(partes[1]).getNombreReal() + " " + usuarios.get(partes[1]).getDireccion());
		} else {
			escribirRespuesta.println("error 406");
		}
		escribirRespuesta.flush();
	}
	
	
	//El metodo gestiona la peticion add, en la cual se añadira con los datos especificados en el mesnaje un nuevo usuario a la lista
	public void peticionAdd(String linea, PrintWriter escribirRespuesta) {

		String[] partes = linea.split(" ");

		if (usuarios.containsKey(partes[1])) {
			escribirRespuesta.println("error 411");
		} else {
			if (partes[2].length() < 4) {
				escribirRespuesta.println("error 412");
			} else {
				Usuario nuevoUsuario = new Usuario(partes[3], partes[1], partes[2],
						socketCliente.getInetAddress().toString());
				usuarios.put(partes[1], nuevoUsuario);
				escribirRespuesta.println("ok");
				save();
			}
		}
		escribirRespuesta.flush();
	}

	
	//En la peticion ConnectTo, se devuelve la direccion y el puerto en el que esta funcionando el otro usuario a conectarse
	public void peticionConnectToSomeone(String linea, PrintWriter escribirRespuesta) {

		String[] partes = linea.split(" ");

		if (usuarios.containsKey(partes[1])) {
			if (usuarios.get(partes[1]).getDireccion().equals("")) {
				escribirRespuesta.println("error 416");

			} else {
				escribirRespuesta.println(
						"ok " + usuarios.get(partes[1]).getDireccion() + " " + usuarios.get(partes[1]).getPuerto());
			}
		} else {
			escribirRespuesta.println("error 417");
		}
		escribirRespuesta.flush();
	}

	
	//El metodo marca que un usuario se ha desconectado, eliminado asi su direccion en la cual estaba funcionando
	public void peticionDisconnect(String linea, PrintWriter escribirRespuesta) {

		String[] partes = linea.split(" ");

		if (usuarios.containsKey(partes[1])) {

			usuarios.get(partes[1]).setDireccion("");
			escribirRespuesta.println("ok");

		} else {
			escribirRespuesta.println("error 422");
		}
		escribirRespuesta.flush();
		
		Cerrar.cerrar(socketCliente);
	}

	//El metodo marca que el usaurio se ha conectado, actuializando asi la direccion y el puerto desde la cual esta trabajando
	public void peticionConnect(String linea, PrintWriter escribirRespuesta) {

		String[] partes = linea.split(" ");

		if (usuarios.containsKey(partes[1])) {
			if (usuarios.get(partes[1]).getDireccion().equals(socketCliente.getInetAddress().toString())) {
				usuarios.get(partes[1]).setDireccion(socketCliente.getInetAddress().toString());
				usuarios.get(partes[1]).setPuerto(Integer.parseInt(partes[2]));
				escribirRespuesta.println("ok");
			} else {
				escribirRespuesta.println("error 426");
			}
		} else {
			escribirRespuesta.println("error 427");
		}
		escribirRespuesta.flush();

	}

	//El metodo guarda la lista de usuarios en un fichero, para asi no perder los usuarios que se han registrado previamente
	public static void save() {

		FileOutputStream fos = null;
		ObjectOutputStream oos = null;

		try {
			fos = new FileOutputStream("usuarios.dat");
			oos = new ObjectOutputStream(fos);
			oos.writeObject(usuarios);
			oos.flush();
			oos.close();
		} catch (FileNotFoundException e) {
			System.out.println("1" + e.getMessage());
		} catch (IOException e) {
			System.out.println("2" + e.getMessage());
		} finally {
			Cerrar.cerrar(fos);
			Cerrar.cerrar(oos);
		}
	}

}
