package Proyecto;

import java.awt.EventQueue;
import java.io.DataInputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.BrokenBarrierException;
import java.util.concurrent.CyclicBarrier;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import javax.swing.JFrame;
import javax.swing.JOptionPane;

import Cerrar.Cerrar;
import InterfacesGraficas.Llamar;
import InterfacesGraficas.Login;

public class Cliente {

	static int puertoServidor = 10000;
	static String ipServidor =  "localhost";

	// El nombreUsuario nos indica el nombre del usuario que esta ejecutando el
	// cliente
	// se inicializa en null, debido hasta que no hagamos el proceso de login no
	// se habra accedido al usuario
	
	
	

	public static void main(String[] args) {
		Socket socketServer = null;
		Socket socketCliente = null;
		ServerSocket servidorCliente = null;
		PrintWriter escritura = null;
		DataInputStream lectura = null;
		StringBuilder nombreUsuario = new StringBuilder();
		String estado = new String();
		StringBuilder puertoCliente = new StringBuilder();
		StringBuilder puertoChat = new StringBuilder();
		
		
		try {
			socketServer = new Socket(ipServidor, puertoServidor);
			escritura = new PrintWriter(socketServer.getOutputStream());
			lectura = new DataInputStream(socketServer.getInputStream());
			
			login(escritura, lectura, nombreUsuario, puertoCliente, puertoChat);
			ServerSocket servidorChat = new ServerSocket(Integer.parseInt(puertoChat.toString()));
			
			if(!nombreUsuario.toString().equals("")){
				try {
					ExecutorService pool = Executors.newCachedThreadPool();	
					servidorCliente = new ServerSocket(Integer.parseInt(puertoCliente.toString()));
					interfazLlamada(servidorCliente, escritura, lectura, nombreUsuario);
					while (true){
						final Socket cliente = servidorCliente.accept();

						AtenderPeticionCliente atenderLlamadas = new AtenderPeticionCliente(cliente,estado,nombreUsuario, servidorChat);

						pool.execute(atenderLlamadas);
						
					}
				}finally{
					escritura.println("Disconnect " +nombreUsuario.toString());
					escritura.flush();
					try {
						if(lectura.readLine().equals("ok")){
							JOptionPane.showMessageDialog(null, "Se ha desconectado...");
						}
					} catch (IOException e) {
						e.printStackTrace();
					}
					Cerrar.cerrar(servidorCliente);
					
				}
			}
			
			System.out.println("terminado");
		} catch (IOException e) {
			e.printStackTrace();
		}
		finally {
		
		}
		
		
		
	}
		
	//El metodo se conecta mediante su usuario y contraseņa al servidor
	//para ello despliega una interfaz grafica, en la cual se nos dara dos opciones, o acceder, lo que equivaldra a logearse,
	// introduciendo su nombre y contraseņa, o registrarse, como nuevo usuario, desplegando para ello una nueva ventana
	private static void login(PrintWriter esc, DataInputStream lec,StringBuilder nombreUsuario, StringBuilder puertoCliente, StringBuilder puertoChat) {
		CyclicBarrier cb = new CyclicBarrier(2);
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					Login frame = new Login(esc,lec,nombreUsuario,cb,puertoCliente,puertoChat);
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
	
	
	public static void interfazLlamada(ServerSocket servidor, PrintWriter esc, DataInputStream lec, StringBuilder nombreUsuario){
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					Llamar frame = new Llamar(esc,lec, nombreUsuario);
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
