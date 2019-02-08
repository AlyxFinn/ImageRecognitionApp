package imageRecognition.ui;


import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.JButton;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import javax.swing.JLabel;
import java.awt.Font;

public class Home extends JFrame {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private JPanel contentPane;
	
	private static AdminLogin login;
	private static UploadImage imageUp;
	private static Home home;
	
	public static AdminLogin getLogin() {
		return login;
	}
	
	public static UploadImage getImageUp() {
		return imageUp;
	}
	
	public static Home getHome() {
		return home;
	}
	
	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					home = new Home();
					home.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the frame.
	 */
	public Home() {
		setTitle("Image Recognition Program");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 450, 300);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);
		
		JButton btnAdmin = new JButton("Admin");
		btnAdmin.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				login = new AdminLogin();
				login.setVisible(true);
				home.setVisible(false);
			}
		});
		btnAdmin.setBounds(78, 178, 146, 32);
		contentPane.add(btnAdmin);
		
		JButton btnImageRecognition = new JButton("Image Recognition");
		btnImageRecognition.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				try {
					imageUp = new UploadImage();
					imageUp.setVisible(true);
					home.setVisible(false);
				}catch(Exception e1) {
					//JOptionPane.showMessageDialog(null, e1);
				}
			}
		});
		btnImageRecognition.setBounds(244, 178, 146, 32);
		contentPane.add(btnImageRecognition);
		
		JLabel lblNewLabel = new JLabel("Welcome to the Image Recognition System");
		lblNewLabel.setFont(new Font("Tahoma", Font.BOLD, 14));
		lblNewLabel.setBounds(85, 81, 305, 32);
		contentPane.add(lblNewLabel);
	}
}
