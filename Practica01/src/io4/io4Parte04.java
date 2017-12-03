package io4;

import java.io.BufferedReader;
import java.io.Closeable;
import java.io.DataInputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;

public class io4Parte04 {
public static void main(String[] args){
		

		DataInputStream dis;
		BufferedReader br=null;
		Reader ru= null;
		Reader rw=null; 
		
		try{
			String resultadoInput="";
			String linea;
			//InputStream
			dis=new DataInputStream(new FileInputStream("ficheroWindows-1252.txt"));
			
			while ((linea=dis.readLine())!=null){
				resultadoInput=resultadoInput+linea;
			}
			//UTF-8
			ru = new InputStreamReader(new FileInputStream("ficheroWindows-1252.txt"),"UTF-8");
			br=new BufferedReader(ru);
			String resultadoUTF8="";
			
			while ((linea = br.readLine()) != null) {
			    resultadoUTF8=resultadoUTF8+linea;
			}
			//Windows-1252
			rw = new InputStreamReader(new FileInputStream("ficheroWindows-1252.txt"),"Windows-1252");
			br=new BufferedReader(rw);
	
			String resultadoWindows1252="";
			while ((linea = br.readLine()) != null) {
			    resultadoWindows1252=resultadoWindows1252+linea;
			}
			//Sólo con la lectura de Windows-1252 vemos bien el contenido.
			//Con input stream falla el euro.
			System.out.println("Con InputStream: "+resultadoInput);
			System.out.println("Con lectura en UTF-8: "+resultadoUTF8);
			System.out.println("Con lectura en Windows-1252: "+resultadoWindows1252);
			
		}catch(FileNotFoundException e){
			e.printStackTrace();
		}catch(IOException e){
			e.printStackTrace();
		}finally{
			cerrar(br);
			cerrar(ru);
			cerrar(rw);
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
