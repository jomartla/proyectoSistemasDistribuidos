package io3;
import java.io.*;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Calendar;

public class io3 {
	
	public static void main(String[] args){
		File f= null;
		if(args.length!=1){
			System.out.println("El número de argumentos no es válido");
		}else{
				//Inicializamos el fichero con el nombre pasado como parámetro.
				f=new File(args[0]);
				//Comprobamos si se ha creado y si es un directorio.
				if(f.exists()&&f.isDirectory()){
					//Recorremos los ficheros del directorio y, si son directorios, mostramos su nombre, mientas que si son ficheros mostramos ciertas propiedades.
					
					//cambiar listFiles()
					for(File file : f.listFiles()){
						boolean isFolder= file.isDirectory();
						if (isFolder){
							System.out.println(file.getName()+"\t"+"<DIR>");
						}else{
							System.out.println(file.getName()+"\t"+file.length()+"\t"+longADate(file.lastModified()));
						}
					}

					System.out.println("\t"+f.getFreeSpace()+" bytes libres");
				}else{
					if(!f.exists()){
						System.out.println("El fichero no existe");
					}else if(!f.isDirectory()){
						System.out.println("El fichero no es un directorio ");
					}	
				}				
		}
	}
	//Convierte la fecha de modificación dada en un long a una fecha que podemos mostrar por pantalla.
	public static String longADate(long date){
		Date d=new Date(date);
		Calendar c= new GregorianCalendar();
		c.setTime(d);
		return(c.getTime().toString());
	}
}
