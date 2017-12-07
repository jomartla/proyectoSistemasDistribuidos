package InterfacesGraficas;

import java.awt.BorderLayout;
import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import java.awt.GridLayout;
import javax.swing.JLabel;
import javax.swing.JTextField;
import javax.swing.JButton;

public class Llamar extends JFrame {

	private JPanel contentPane;
	private JTextField tdUsuarioLlamar;
	private JPanel panel_1;
	private JButton btnLlamar;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					Llamar frame = new Llamar();
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the frame.
	 */
	public Llamar() {
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
		panel_1.add(btnLlamar);
	}

}
