package Proyecto;

import java.io.Serializable;

public class Usuario implements Serializable {
	private String nombreReal;
	private String nombreUsuario;
	private String contrasena;
	private String direccion;
	private int puerto;

	public Usuario(String nombreReal, String nombreUsuario, String contrasena, String direccion){
		this.nombreReal=nombreReal;
		this.nombreUsuario=nombreUsuario;
		this.contrasena=contrasena;
		this.direccion=direccion;
		this.puerto = 12000;
	}
	
	public String getNombreReal() {
		return nombreReal;
	}

	public String getNombreUsuario() {
		return nombreUsuario;
	}

	public String getContrasena() {
		return contrasena;
	}

	public String getDireccion() {
		return direccion;
	}

	public void setDireccion(String d) {
		direccion = d;
	}
	
	public void setPuerto(int p){
		puerto = p;
	}
	
	public int getPuerto(){
		direccion.substring(1);
		return(puerto);
	}

}
