package io4;

import java.io.BufferedWriter;
import java.io.Closeable;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.io.Writer;

public class io4Parte03 {
	public static void main(String[] args){
		Writer w=null;
		BufferedWriter out = null;
		try{
			w = new OutputStreamWriter(new FileOutputStream("ficheroWindows-1252.txt"),"Windows-1252");
			out = new BufferedWriter(w);
			out.write("Lápiz 1€");
			out.flush();
//Bytes por caracter
//1.Todos los caracteres
		}catch(FileNotFoundException e){
			e.printStackTrace();
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}catch(IOException e){
			e.printStackTrace();
		}finally{	
			cerrar(out);
			cerrar(w);
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
