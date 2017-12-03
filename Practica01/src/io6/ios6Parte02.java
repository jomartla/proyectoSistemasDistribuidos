package io6;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
//Lo que ocurre al añadir el método tamaño y volver a iniciar esta clase es que en la deserializacion los identificadores ya no
//coinciden y por lo tanto no se puede. Esto ocurre porque hemos modificado la clase contactos, podríamos forzar a la aplicación
//a deserialiar poniendo el mismo ID pero no seria conveniente ni el resultado seguramente fuera bueno.
public class ios6Parte02 implements Serializable {

	public static void main(String[] args) {
		Contactos c;
		ObjectInputStream ois=null;
		try {
		
			FileInputStream fi = new FileInputStream("fichero.dat");
			ois = new ObjectInputStream(fi);
			
			c= (Contactos) ois.readObject();
			
			ois.close();
			System.out.println(c.getEmail("Jorge"));
			
			System.out.println(c.getTfno("Juanjo"));
//Si que funciona, pero nos da 0; valor que da por defecto a un atributo que no tiene. Lo que ha pasado es que 
//no hemos creado el atributo antes de seralizarlo, así que se lo ha inventado literalmente. Si volvemos al punto b
//y lo ejecutamos y despues ejecutamos el c, ya tendremos el atributo actualizado.
			System.out.println(c.tamanoMaximoAgenda);
		
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
