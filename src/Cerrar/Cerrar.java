package Cerrar;

import java.io.Closeable;
import java.io.IOException;

public class Cerrar {
	public static void cerrar(Closeable o){
		try{
			if(o!=null){
				o.close();
			}
			
		}catch(IOException e){
			e.printStackTrace();
		}
	}
}
