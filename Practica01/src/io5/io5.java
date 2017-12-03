package io5;

import java.io.Closeable;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;


public class io5 {
	
	public static void main(String[] args) {
	
		
		DataInputStream is=null;

		File f=new File(args[0]);

		boolean esAnsi=true;
		boolean esUTF8=false;
		boolean aparece110=false;
		boolean aparece10=false;
		boolean aparecen10y10seguidos=false;
		
		if(args.length!=1){
			System.out.println("El número de argumentos es erróneo");
		}else{
			try{
				is=new DataInputStream(new FileInputStream(args[0]));
				for(int i=0;i<f.length();i++){
					
					switch (tipoByte(is.readByte())){
					
					case 1: 
						//0xxxxxx			
						aparece110=false;
						aparece10=false;
						aparecen10y10seguidos=false;
						break;
					case 2:
						//110xxxxx
						
						aparece110=true;
						aparece10=false;
						aparecen10y10seguidos=false;
						esAnsi=false;
						break;
					case 3:
						//10xxxxxx
						if(aparece110){
							esUTF8=true;
							aparece110=false;
							esAnsi=false;
						}else if(aparece10 && aparecen10y10seguidos){
							esUTF8=true;
							aparece10=false;
							aparecen10y10seguidos=false;
							aparece110=false;
							esAnsi=false;
						}else if(aparece10 && !aparecen10y10seguidos){
							aparecen10y10seguidos=true;
							aparece110=false;
							esAnsi=false;
						}		
						break;
					case 4:
						//1110xxxx
						aparece10=true;
						
						aparecen10y10seguidos=false;
						aparece110=false;
						esAnsi=false;
						break;
					case 5:
						//1xxxxxxx
						aparece110=false;
						aparece10=false;
						aparecen10y10seguidos=false;
						esAnsi=false;
						break;
					}
					
				}		
				if(esUTF8){
					System.out.println("UTF-8");
				}else if(esAnsi){
					System.out.println("ANSI");
				}else{
						System.out.println("Otra codificacion no UTF-8");
				}
			}catch(FileNotFoundException e){
				e.printStackTrace();
			}catch(IOException e){
				e.printStackTrace();
			}finally{
				cerrar(is);
			
			}
		}
	}
	public static int tipoByte(byte b){	
		int devuelto=0;
		if (((byte) (b | 0x1F)) == ((byte) 0xDF)) {
			//Empieza por 110
			devuelto=2;
		}else if (((byte) (b | 0x3F)) == (byte) 0xBF) {
			//Empieza por 10
			devuelto=3;
		}else if (((byte) (b | 0x0F)) == (byte) 0xEF){
			//Empieza por 1110
			devuelto=4;
		}else if (((byte) (b | 0x7F)) == (byte) 0xFF) {
			//Empieza por 1xxxxxxx
			devuelto=5;
		}else if (((byte) (b | 0x7F)) == (byte) 0x7F) {
			//Empieza por 0xxxxxxx
			devuelto=1;
		}
		return (devuelto);
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
