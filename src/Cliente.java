import java.net.Socket;

public class Cliente {
	
	int puertoServidor;
	String ipServidor;
	
	public static void main(String[] args) {
		System.out.println("asd");
	}
	
	
	private void Login(String ipServidor, int puertoServidor){
		Socket s = new Socket(ipServidor,puertoServidor);
		
	}
}
