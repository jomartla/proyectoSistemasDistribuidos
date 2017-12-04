package Proyecto;

import java.io.Serializable;

public class Usuario implements Serializable {
	private String nombreReal;
	private String nombreUsuario;
	private String contrasena;
	private String direccion;

	public Usuario(String nombreReal, String nombreUsuario, String contrasena, String direccion){
		this.nombreReal=nombreReal;
		this.nombreUsuario=nombreUsuario;
		this.contrasena=contrasena;
		this.direccion=direccion;
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

}
