package io1;

import java.io.BufferedReader;
import java.io.Closeable;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

public class io1 {

	public static void main(String []args){
		
		BufferedReader br=null;
		if(args.length!=2){
			System.out.println("El número de argumentos no es el correcto");
		}else{
			try{
				int cont=0;
				br =new BufferedReader(new FileReader(args[0]));
				String linea=br.readLine();
				while(linea!=null){
					cont=cont+estaEnLaLinea(linea,args[1]);
					linea=br.readLine();
				}
				System.out.println("El numero de veces que aparece es: "+ cont);
				}catch(FileNotFoundException e){
					System.out.println("Fichero no encontrado: " + e);
				}catch(IOException e){
					System.out.println("Error de entrada o salida de datos: "+ e);
				}finally{
					cerrar(br);
				}
			}
		}
		
	//Devuelve el número de veces que el String iniciales está en el String lineaLeida
	public static int estaEnLaLinea(String lineaLeida, String iniciales){
		int puntero=0;
		int contador=0;
		while(lineaLeida.indexOf(iniciales,puntero)!=-1){
				puntero=lineaLeida.indexOf(iniciales,puntero) + iniciales.length();
				contador++;
		}
		return contador;
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
