package InterfacesGraficas;

import java.awt.BorderLayout;
import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import java.awt.GridLayout;
import javax.swing.JLabel;
import javax.swing.JButton;

public class RecibirLlamada extends JFrame {

	private JPanel contentPane;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					RecibirLlamada frame = new RecibirLlamada();
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
	public RecibirLlamada() {
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 358, 156);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(new GridLayout(2, 0, 0, 0));
		
		JPanel panel_1 = new JPanel();
		contentPane.add(panel_1);
		
		JLabel lblNewLabel = new JLabel("Llamada entrante de: ");
		panel_1.add(lblNewLabel);
		
		JLabel labelUsuarioLlamando = new JLabel("Esta llamando");
		panel_1.add(labelUsuarioLlamando);
		
		JPanel panel = new JPanel();
		contentPane.add(panel);
		
		JButton btnColgar = new JButton("Colgar");
		panel.add(btnColgar);
		
		JButton btnRecibir = new JButton("Recibir");
		panel.add(btnRecibir);
	}

}
