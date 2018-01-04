
package Proyecto;

import java.io.*;
import java.net.*;
import java.util.*;
import java.util.concurrent.*;
import Cerrar.Cerrar;

public class Servidor {
	
	private static HashMap<String,Usuario> usuarios = new HashMap<String,Usuario>();
	
	public static void main(String[] args) {
		//El servidor recibe peticiones de clientes ejecutando para cada una de ellas el hilo AtenderPeticionServidor
		ServerSocket servidor=null;
		CyclicBarrier barrera;
		try {
			servidor = new ServerSocket(10000);
			ExecutorService pool = Executors.newCachedThreadPool();	
			leerFichero();
			
			while (true){
				
				final Socket cliente = servidor.accept();

				AtenderPeticionServidor atenderCliente = new AtenderPeticionServidor(cliente,usuarios);
				
				pool.execute(atenderCliente);
			}
	
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}finally{
			Cerrar.cerrar(servidor);
		}
	}
	
	//Este metodo carga el fichero en el cual estan los usuarios, asi cada vez que arranquemos el servidor, podemos disponer de 
	//los usuarios que se habian registrado previamente
	public static void leerFichero() {
		
		FileInputStream fis = null;
		ObjectInputStream ois = null;
		
		try{
		fis = new FileInputStream("usuarios.dat");
		ois = new ObjectInputStream(fis);
		
		usuarios=(HashMap<String,Usuario>) ois.readObject();
		
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
	
}