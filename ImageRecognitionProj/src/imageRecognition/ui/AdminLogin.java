package imageRecognition.ui;

import java.awt.Color;
import java.awt.EventQueue;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.border.EmptyBorder;
import imageRecognition.ui.Home;

public class AdminLogin extends JFrame {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private JPanel contentPane;
	private JTextField txtUsername;
	private JTextField txtPassword;
	public static AdminPage admin;
	
	private static JFrame frame;
	
	public static AdminPage getAdmin() {
		return admin;
	}

	/**
	 * Launch the application.
	 */	
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					frame = new AdminLogin();
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}
	
	public void ErrorMessage()
	{
		JLabel errorLabel = new JLabel("Error");
		errorLabel.setText("Incorrect username and/or password.");
		errorLabel.setForeground(Color.RED);
		errorLabel.setBounds(110, 220, 400, 20);
		add(errorLabel);
	}

	/**
	 * Create the frame.
	 */
	public AdminLogin() {
		setTitle("Admin Login Page");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 450, 453);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);
		
		JButton btnBack = new JButton("Back");
		btnBack.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				try {
					Home.getHome().setVisible(true);
					Home.getLogin().dispose();
				}catch(Exception e1) {
					JOptionPane.showMessageDialog(null, e1);
				}
			}
		});
		btnBack.setBounds(23, 25, 117, 29);
		contentPane.add(btnBack);
		
		JButton btnLogin = new JButton("Login");
		btnLogin.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				try
				{
					if (txtUsername.getText().trim().equals("admin") && txtPassword.getText().trim().equals("password"))
					{
						admin = new AdminPage();
						Home.getLogin().setVisible(false);
					} else 
					{
						ErrorMessage();
						revalidate();
						repaint();
					}
				} catch (Exception e1)
				{
					JOptionPane.showMessageDialog(null, e1);
				}
			}
		});
		btnLogin.setBounds(169, 317, 117, 29);
		contentPane.add(btnLogin);
		
		JLabel lblUsername = new JLabel("Username:");
		lblUsername.setBounds(123, 149, 66, 16);
		contentPane.add(lblUsername);
		
		JLabel lblPassword = new JLabel("Password:");
		lblPassword.setBounds(123, 177, 66, 16);
		contentPane.add(lblPassword);
		
		txtUsername = new JTextField();
		txtUsername.setBounds(201, 144, 130, 26);
		contentPane.add(txtUsername);
		txtUsername.setColumns(10);
		
		txtPassword = new JTextField();
		txtPassword.setColumns(10);
		txtPassword.setBounds(201, 172, 130, 26);
		contentPane.add(txtPassword);
		
		
	}
}
