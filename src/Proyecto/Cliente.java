package Proyecto;

import java.awt.EventQueue;
import java.io.IOException;
import java.net.Socket;
import InterfacesGraficas.Login;

public class Cliente {

	static int puertoServidor = 10000;
	static String ipServidor =  "localhost";

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
	//para ello despliega una interfaz grafica, en la cual se nos dara dos opciones, o acceder, lo que equivaldra a logearse,
	// introduciendo su nombre y contraseña, o registrarse, como nuevo usuario, desplegando para ello una nueva ventana
	private static void login(Socket s) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					Login frame = new Login(s,nombreUsuario);
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});

	}
}
