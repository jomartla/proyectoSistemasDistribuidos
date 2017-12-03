package io6;

import java.io.Closeable;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.io.Serializable;

public class io6Parte01 implements Serializable {
	
	public static void main(String[] args) {
	
		ObjectOutputStream oos=null;
		
		try {
			Contactos c= new Contactos();
			c.addDatos("J", "622333555", "j@gmail.com");
			c.addDatos("A", "632333555", "a@gmail.com");
			c.addDatos("G", "642333555", "g@gmail.com");
			c.addDatos("H", "653333555", "h@gmail.com");
			
			FileOutputStream f = new FileOutputStream("fichero.dat");
			oos = new ObjectOutputStream(f);
			oos.writeObject(c);
			
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally{
			cerrar(oos);
		}
	}
	public static void cerrar(Closeable o){
		try{
			if(o!=null);
			o.close();
		}catch(IOException e){
			e.printStackTrace();
		}
	}
}
