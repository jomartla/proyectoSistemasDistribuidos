package io2;
import java.io.*;
public class io2 {
	
	public static void main(String []args){
		
		InputStream is=null;
		OutputStream os=null;
		
		if(args.length!=2){
			System.out.println("El número de argumentos es erróneo");
		}else{
			try{
				
				//El primer argumento será el fichero que queremos copiar. 
				is= new FileInputStream(args[0]);
				//El segundo argumento será el fichero sobre el cual queremos hacer la copia.
				os=new FileOutputStream(args[1]);
				
				//Creamos un buffer para leer los datos y escribirlos directamente sobre el fichero donde queremos copiar los archivos.
				byte buff[] = new byte[50];
				int leidos =is.read(buff);
				while (leidos!=-1){
					os.write(buff, 0, leidos);
					leidos=is.read(buff);
				}
				//FLUSH AQUÍ
				os.flush();
				
			}catch(FileNotFoundException e){
				e.printStackTrace();
			}catch(IOException e){
				e.printStackTrace();
			}finally{
				cerrar(is);
				cerrar(os);
			}
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
