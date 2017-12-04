package Proyecto;

import java.net.*;
import java.util.*;

public class AtenderPeticionServidor implements Runnable {

	private Socket socketCliente;
	private ArrayList<Usuario> usuarios;
	
	public AtenderPeticionServidor(Socket socketCliente, ArrayList<Usuario> usuarios){
		this.socketCliente = socketCliente;	
		this.usuarios=usuarios;
	}
	public void run(){
		
	}
}
