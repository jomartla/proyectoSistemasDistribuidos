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
	static StringBuilder nombreUsuario = new StringBuilder();
	static String estado = new String();

	public static void main(String[] args) {
		Socket socketServer = null;
		Socket socketCliente = null;
		ServerSocket servidorCliente = null;
		PrintWriter escritura = null;
		DataInputStream lectura = null;
		
		
		try {
			socketServer = new Socket(ipServidor, puertoServidor);
			escritura = new PrintWriter(socketServer.getOutputStream());
			lectura = new DataInputStream(socketServer.getInputStream());
			
			login(escritura, lectura);
			
			
			if(!nombreUsuario.toString().equals("")){
				
				try {
					ExecutorService pool = Executors.newCachedThreadPool();	
					CyclicBarrier barrera;
					servidorCliente = new ServerSocket(11000);
					interfazLlamada(servidorCliente, escritura, lectura);
					while (true){
						
						final Socket cliente = servidorCliente.accept();
						
						barrera = new CyclicBarrier(2);

						AtenderPeticionCliente atenderLlamadas = new AtenderPeticionCliente(cliente,estado,nombreUsuario,barrera);

						pool.execute(atenderLlamadas);
						
						barrera.await();
					}
					
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (BrokenBarrierException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
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
			
			System.out.println("termionado");
		} catch (IOException e) {
			e.printStackTrace();
		}
		finally {
		
		}
		
		
		
	}
		
	//El metodo se conecta mediante su usuario y contraseņa al servidor
	//para ello despliega una interfaz grafica, en la cual se nos dara dos opciones, o acceder, lo que equivaldra a logearse,
	// introduciendo su nombre y contraseņa, o registrarse, como nuevo usuario, desplegando para ello una nueva ventana
	private static void login(PrintWriter esc, DataInputStream lec) {
		CyclicBarrier cb = new CyclicBarrier(2);
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					Login frame = new Login(esc,lec,nombreUsuario,cb);
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
	
	
	public static void interfazLlamada(ServerSocket servidor, PrintWriter esc, DataInputStream lec){
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					Llamar frame = new Llamar(esc,lec);
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
