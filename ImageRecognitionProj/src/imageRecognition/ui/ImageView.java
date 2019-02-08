package imageRecognition.ui;

import java.awt.Color;
import java.awt.EventQueue;
import java.awt.Image;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.Border;
import javax.swing.border.EmptyBorder;
import javax.swing.JLabel;
import javax.imageio.ImageIO;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.ImageIcon;

import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.awt.event.ActionEvent;

public class ImageView extends JFrame {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private JPanel contentPane;
	private JLabel lblImage;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					new ImageView();
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the frame.
	 */
	public ImageView() {
		setTitle("View Image");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 545, 430);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);
		
		lblImage = new JLabel("");
		lblImage.setBounds(30, 24, 470, 326);
		BufferedImage picture = null;
		try {
			picture = ImageIO.read(new File("tempImageFile.jpg"));
		} catch (IOException ioe) {
			ioe.printStackTrace();
		}
		lblImage.setIcon(ResizeImage(picture));
		Border border = BorderFactory.createLineBorder(Color.BLACK, 5);
		lblImage.setBorder(border);
		contentPane.add(lblImage);
		
		JButton btnBack = new JButton("Back");
		btnBack.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				try {
					Files.deleteIfExists(Paths.get("H:/eclipse17/imageRec/tempImageFile.jpg"));
				} catch( IOException ioe) {
					ioe.printStackTrace();
				}
				AdminPage.getViewer().dispose();
			}
		});
		btnBack.setBounds(209, 362, 117, 29);
		contentPane.add(btnBack);
	}
	
	public ImageIcon ResizeImage(BufferedImage pic) {
		Image newImg = pic.getScaledInstance(lblImage.getWidth(), lblImage.getHeight(), Image.SCALE_SMOOTH);
		ImageIcon image = new ImageIcon(newImg);
		return image;
	}

}
