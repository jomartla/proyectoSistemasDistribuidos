package io4;

import java.io.Closeable;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.io.Writer;

public class io4Parte01 {

	public static void main(String[] args){
		
		Writer w=null;

		try{
			w = new OutputStreamWriter(new FileOutputStream("ficheroUTF-8.txt"),"UTF-8");
			w.write("Lápiz 1€");
			w.flush();
//Bytes por caracter
//1.(normales) One byte is needed to encode the 128 US-ASCII characters (Unicode range U+0000 to U+007F).
//2.(a con tílde) Two bytes are needed for Latin letters with diacritics and for characters from Greek, Cyrillic, Armenian, Hebrew, Arabic, Syriac and Thaana alphabets (Unicode range U+0080 to U+07FF).
//3.(símbolo euro) Three bytes are needed for the rest of the Basic Multilingual Plane (which contains virtually all characters in common use).
		}catch(FileNotFoundException e){
			e.printStackTrace();
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}catch(IOException e){
			e.printStackTrace();
		}finally{
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

