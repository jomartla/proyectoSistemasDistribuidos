package io7;

import java.io.*;

public class io7 {

	public static void main(String[] args) {
		
		File fichero = new File("miConfiguracion.txt");
		BufferedReader br=null;
		BufferedWriter bw=null;
		
		try {
				
			br=new BufferedReader(new InputStreamReader(new FileInputStream(fichero)));
			String linea;
			bw=new BufferedWriter(new OutputStreamWriter(new FileOutputStream("miConfiguracionC.txt")));
			String nuevaCadena="";
				
			while((linea=br.readLine())!=null){
				nuevaCadena="";
				if(linea.contains("<") && linea.contains(">")){
					int iniciolinea=linea.indexOf("<");
					int finLinea=linea.indexOf(">");

					//metodo split
					if(linea.substring(iniciolinea+1,finLinea).equals("iniciales") && iniciolinea==0){
						nuevaCadena=nuevaCadena+"<iniciales>:<"+args[0]+">";
						bw.write(nuevaCadena+"\r\n");
					}else{
						bw.write(linea+"\r\n");
					}
				}else{
					bw.write(linea+"\r\n");
				}				
				bw.flush();
			}
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			} finally{
				cerrar(br);
				cerrar(bw);
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
