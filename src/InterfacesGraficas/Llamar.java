package InterfacesGraficas;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import java.awt.GridLayout;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JTextField;
import javax.swing.JButton;
import java.awt.event.ActionListener;
import java.io.DataInputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.awt.event.ActionEvent;

public class Llamar extends JFrame {

	private JPanel contentPane;
	private JTextField tdUsuarioLlamar;
	private JPanel panel_1;
	private JButton btnLlamar;
	
	private PrintWriter escritura;
	private DataInputStream lectura;


	public Llamar(PrintWriter esc, DataInputStream lec) {
		escritura = esc;
		lectura = lec;
		
		
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 390, 155);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(new GridLayout(2, 0, 0, 0));
		
		JPanel panel = new JPanel();
		contentPane.add(panel);
		
		JLabel lblUsuarioALlamar = new JLabel("Usuario a Llamar:");
		panel.add(lblUsuarioALlamar);
		
		tdUsuarioLlamar = new JTextField();
		panel.add(tdUsuarioLlamar);
		tdUsuarioLlamar.setColumns(10);
		
		panel_1 = new JPanel();
		contentPane.add(panel_1);
		
		btnLlamar = new JButton("Llamar");
		btnLlamar.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				llamar();
			}
		});
		panel_1.add(btnLlamar);
	}


	protected void llamar() {
		if(!tdUsuarioLlamar.getText().replaceAll("\\s","").isEmpty()){
			escritura.println("ConnectTo " + tdUsuarioLlamar.getText().replaceAll("\\s",""));
			escritura.flush();
			
			try {
				String respuesta = lectura.readLine();
				if (respuesta.startsWith("ok")){
					String direccion = respuesta.split(" ")[1];
					
					
					////TRATAR LA LLAMADA!!!!!!!!!!!!!!!!
					System.out.println(direccion);
				}
				else if (respuesta.startsWith("error")){
					if (respuesta.split(" ")[1].equals("416")){
						JOptionPane.showMessageDialog(null,"Error: El usuario a llamar no existe"); 
					}
					else if (respuesta.split(" ")[1].equals("417")){
						JOptionPane.showMessageDialog(null,"Error: El usuario a llamar no esta conectado"); 
					}
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
			
		}
		else{
			JOptionPane.showMessageDialog(null,"Error: El campo esta vacio"); 
		}
		
	}
	
	

}
