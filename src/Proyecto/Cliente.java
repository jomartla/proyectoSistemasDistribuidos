package Proyecto;

import Cerrar.Cerrar;
import InterfacesGraficas.Llamar;
import InterfacesGraficas.Login;

import java.awt.*;
import java.io.*;
import java.net.*;
import java.util.concurrent.*;
import javax.swing.*;

public class Cliente {

	static int puertoServidor = 10000;
	static String ipServidor = "188.79.157.154";

	// El nombreUsuario nos indica el nombre del usuario que esta ejecutando el
	// cliente
	// se inicializa en null, debido hasta que no hagamos el proceso de login no
	// se habra accedido al usuario

	public static void main(String[] args) {
		Socket socketServer = null;
		ServerSocket servidorCliente = null;
		PrintWriter escritura = null;
		DataInputStream lectura = null;
		StringBuilder nombreUsuario = new StringBuilder();
		String estado = new String();
		StringBuilder puertoCliente = new StringBuilder();
		ExecutorService pool = null;

		try {
			socketServer = new Socket(ipServidor, puertoServidor);
			escritura = new PrintWriter(socketServer.getOutputStream());
			lectura = new DataInputStream(socketServer.getInputStream());
			pool = Executors.newCachedThreadPool();

			comenzarLogin(escritura, lectura, nombreUsuario, puertoCliente);

			if (!nombreUsuario.toString().equals("")) {

				servidorCliente = new ServerSocket(Integer.parseInt(puertoCliente.toString()));
				interfazLlamada(servidorCliente, escritura, lectura, nombreUsuario);
				while (true) {
					final Socket cliente = servidorCliente.accept();

					AtenderPeticionCliente atenderLlamadas = new AtenderPeticionCliente(cliente, estado, nombreUsuario);
					
					Thread demonioAtenderLlamadas = new Thread(atenderLlamadas);
					demonioAtenderLlamadas.setDaemon(true);
					pool.execute(demonioAtenderLlamadas);

				}
			}

		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			escritura.println("Disconnect " + nombreUsuario.toString());
			escritura.flush();
			try {
				if (lectura.readLine().equals("ok")) {
					JOptionPane.showMessageDialog(null, "Se ha desconectado...");
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
			Cerrar.cerrar(socketServer);
			pool.shutdown();
		}
	}

	// El metodo se conecta mediante su usuario y contraseña al servidor
	// para ello despliega una interfaz grafica, en la cual se nos dara dos
	// opciones, o acceder, lo que equivaldra a logearse,
	// introduciendo su nombre y contraseña, o registrarse, como nuevo usuario,
	// desplegando para ello una nueva ventana
	private static void comenzarLogin(PrintWriter esc, DataInputStream lec, StringBuilder nombreUsuario, StringBuilder puertoCliente) {
		CyclicBarrier cb = new CyclicBarrier(2);
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					Login frame = new Login(esc, lec, nombreUsuario, cb, puertoCliente);
					frame.setVisible(true);
					frame.setDefaultCloseOperation(frame.DISPOSE_ON_CLOSE);
					frame.addWindowListener(new java.awt.event.WindowAdapter() {
						@Override
						public void windowClosing(java.awt.event.WindowEvent windowEvent) {
							try {
								cb.await();
							} catch (InterruptedException | BrokenBarrierException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}
						}
					});

				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});

		try {
			cb.await();
		} catch (InterruptedException | BrokenBarrierException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	public static void interfazLlamada(ServerSocket servidor, PrintWriter esc, DataInputStream lec,
			StringBuilder nombreUsuario) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					Llamar frame = new Llamar(esc, lec, nombreUsuario);
					frame.setVisible(true);
					frame.setDefaultCloseOperation(frame.DISPOSE_ON_CLOSE);
					frame.addWindowListener(new java.awt.event.WindowAdapter() {
						@Override
						public void windowClosing(java.awt.event.WindowEvent windowEvent) {
							Cerrar.cerrar(servidor);
						}
					});
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}
}
