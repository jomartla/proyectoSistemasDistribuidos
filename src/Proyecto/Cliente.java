package Proyecto;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Scanner;

public class Cliente {

	static int puertoServidor;
	static String ipServidor;

	// El nombreUsuario nos indica el nombre del usuario que esta ejecutando el
	// cliente
	// se inicializa en null, debido hasta que no hagamos el proceso de login no
	// se habra accedido al usuario
	static String nombreUsuario = null;

	public static void main(String[] args) {
		Socket servidor;
		try {
			
			servidor = new Socket(ipServidor, puertoServidor);
			login(servidor);
		} catch (IOException e) {
			e.printStackTrace();
		}
		
	}
		
	//El metodo se conecta mediante su usuario y contraseña al servidor
	//Este le devuelve un mensaje de confirmacion si se ha podido logearse, o un mensaje de error
	//si no ha sido posible el logeo, en este caso volvera a pedir los datos para hacer un nuevo intento
	private static void login(Socket s) {
		PrintWriter pw = null;
		DataInputStream di = null;
		try {
			di = new DataInputStream(s.getInputStream());
			Scanner sc = new Scanner(System.in);
			String respuesta;

			do {

				System.out.println("Introduce tu nombre de usuario:");
				String nombre = sc.nextLine();
				System.out.println("Introduce tu contraseña");
				String contrasena = sc.nextLine();
				pw = new PrintWriter(s.getOutputStream());

				pw.println("Login " + nombre + " " + contrasena);

				respuesta = di.readLine();

				if (respuesta.startsWith("ok")) {
					nombreUsuario = nombre;
				}

			} while (respuesta.startsWith("error"));

		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}
}
