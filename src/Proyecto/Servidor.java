package Proyecto;

import java.io.*;
import java.net.*;
import java.util.*;
import java.util.concurrent.*;
import Cerrar.Cerrar;

public class Servidor {
	
	private static ArrayList<Usuario> usuarios = new ArrayList<Usuario>();
	
	public static void main(String[] args) {
		
		ServerSocket servidor=null;
		
		leerFichero();
		try {
			servidor = new ServerSocket(10000);
			ExecutorService pool = Executors.newCachedThreadPool();	

			while (true){
				final Socket cliente = servidor.accept();

				AtenderPeticionServidor atenderCliente = new AtenderPeticionServidor(cliente,usuarios);

				pool.execute(atenderCliente);
				
				save();
			}
		} catch (IOException e) {
			e.printStackTrace();
		}finally{
			Cerrar.cerrar(servidor);
		}
	}
	public static void leerFichero() {
		
		FileInputStream fis = null;
		ObjectInputStream ois = null;
		
		try{
		fis = new FileInputStream("usuarios.dat");
		ois = new ObjectInputStream(fis);
		
		usuarios=(ArrayList<Usuario>) ois.readObject();
		
		}catch(FileNotFoundException e){
			System.out.println("1"+e.getMessage());
		} catch(IOException e){
			System.out.println("2"+e.getMessage());
		}catch(ClassNotFoundException e){
			System.out.println("3"+e.getMessage());
		} finally {
			Cerrar.cerrar(fis);
			Cerrar.cerrar(ois);
		}
	}
	public static void save() {
		
		FileOutputStream fos = null;
		ObjectOutputStream oos = null;
		
		try{
		fos=new FileOutputStream("usuarios.dat");
		oos = new ObjectOutputStream(fos) ;
		oos.writeObject(usuarios);
		oos.flush();
		oos.close();
		} catch(FileNotFoundException e){
			System.out.println("1"+e.getMessage());
		} catch(IOException e){
			System.out.println("2"+e.getMessage());
		} finally {
			Cerrar.cerrar(fos);
			Cerrar.cerrar(oos);
		}
	}
	
}
