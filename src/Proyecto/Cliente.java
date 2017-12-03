package Proyecto;
import java.io.IOException;
import java.net.Socket;

public class Cliente {
	
	int puertoServidor;
	String ipServidor;
	
	public static void main(String[] args) {
		System.out.println("asd");
	}
	
	
	private void Login(String ipServidor, int puertoServidor){
		try {
			Socket s = new Socket(ipServidor,puertoServidor);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
}
