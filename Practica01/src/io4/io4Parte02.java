package io4;


import java.io.*;


public class io4Parte02 {
	
	public static void main(String[] args){
		
		DataInputStream dis=null;
		BufferedReader br=null;
		Reader ru= null;
		Reader rw=null; 
		
		try{
//¿ES ESTO UN INPUT STREAM O SE PIDE OTRA COSA?	-- mejor datainputstream		
			String resultadoInput="";
			String linea;
			//InputStream
			dis=new DataInputStream(new FileInputStream("ficheroUTF-8.txt"));
			
			while ((linea=dis.readLine())!=null){
				resultadoInput=resultadoInput+linea;
			}
			//UTF-8
			ru = new InputStreamReader(new FileInputStream("ficheroUTF-8.txt"),"UTF-8");
			br=new BufferedReader(ru);
			String resultadoUTF8="";
			
			while ((linea = br.readLine()) != null) {
			    resultadoUTF8=resultadoUTF8+linea;
			}
			//Windows-1252
			rw = new InputStreamReader(new FileInputStream("ficheroUTF-8.txt"),"Windows-1252");
			br=new BufferedReader(rw);
	
			String resultadoWindows1252="";
			while ((linea = br.readLine()) != null) {
			    resultadoWindows1252=resultadoWindows1252+linea;
			}
			//Resultados: sólo con la lectura en UTF-8 podremos ver bien el contenido del archivo
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
