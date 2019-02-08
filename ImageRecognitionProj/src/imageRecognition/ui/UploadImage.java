package imageRecognition.ui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.EventQueue;
import java.awt.Image;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.border.Border;
import javax.swing.border.EmptyBorder;
import javax.imageio.ImageIO;
import javax.swing.BorderFactory;
import javax.swing.ButtonGroup;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFileChooser;

import java.awt.event.ActionListener;
import java.io.ByteArrayOutputStream;
package imageRecognition.ui;

import java.awt.Color;
import java.awt.EventQueue;
import java.awt.Image;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.border.Border;
import javax.swing.border.EmptyBorder;
import javax.swing.BorderFactory;
import javax.swing.ButtonGroup;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFileChooser;

import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.awt.event.ActionEvent;

import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.JRadioButton;

import ml.ImageAnalyzer9000;

public class UploadImage extends JFrame {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private JPanel contentPane;
	private boolean analysed = false;
	private boolean imageChosen = false;
	private File selectedFile;
	private JRadioButton rdbtnYes;
	private JRadioButton rdbtnNo;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					UploadImage frame = new UploadImage();
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
	public UploadImage() {
		setTitle("Image Recognition Page");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 724, 484);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);
		
		JLabel lblImage = new JLabel();
		Border border = BorderFactory.createLineBorder(Color.BLACK, 5);
		lblImage.setBorder(border);
		lblImage.setBounds(26, 47, 378, 329);
		contentPane.add(lblImage);
		
		JButton btnChooseImage = new JButton("Choose Image");
		btnChooseImage.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				JFileChooser file = new JFileChooser();
				file.setCurrentDirectory(new File(System.getProperty("user.home")));
				FileNameExtensionFilter filter = new FileNameExtensionFilter("*.Images','jpg','gif","png");
				file.addChoosableFileFilter(filter);
				int result = file.showSaveDialog(null);
				if(result == JFileChooser.APPROVE_OPTION) {
					selectedFile = file.getSelectedFile();
					String path = selectedFile.getAbsolutePath();
					lblImage.setIcon(ResizeImage(path));
					analysed = false;
					imageChosen = true;
				} else if(result == JFileChooser.CANCEL_OPTION) {
					JOptionPane.showMessageDialog(null, "No File Selected");
				}
			}

			private ImageIcon ResizeImage(String ImagePath) {
				ImageIcon MyImage = new ImageIcon(ImagePath);
				Image img = MyImage.getImage();
				Image newImg = img.getScaledInstance(lblImage.getWidth(), lblImage.getHeight(), Image.SCALE_SMOOTH);
				ImageIcon image = new ImageIcon(newImg);
				return image;
			}
		});
		btnChooseImage.setBounds(60, 404, 117, 42);
		contentPane.add(btnChooseImage);
		
		JLabel lblTheImageIs = new JLabel("The image is:");
		lblTheImageIs.setBounds(439, 125, 84, 16);
		contentPane.add(lblTheImageIs);
		
		JLabel lblConfidence = new JLabel("Confidence:");
		lblConfidence.setBounds(448, 153, 75, 16);
		contentPane.add(lblConfidence);
		
		JLabel lblImageGuess = new JLabel("");
		lblImageGuess.setBounds(535, 125, 100, 16);
		contentPane.add(lblImageGuess);
		
		JLabel lblConfidenceOutput = new JLabel("");
		lblConfidenceOutput.setBounds(535, 153, 84, 16);
		contentPane.add(lblConfidenceOutput);
		
		JLabel lblIsThisCorrect = new JLabel("Is this correct?");
		lblIsThisCorrect.setBounds(425, 204, 98, 16);
		contentPane.add(lblIsThisCorrect);
		
		rdbtnYes = new JRadioButton("Yes");
		rdbtnYes.setBounds(447, 250, 141, 23);
		contentPane.add(rdbtnYes);
		
		rdbtnNo = new JRadioButton("No");
		rdbtnNo.setBounds(448, 285, 141, 23);
		contentPane.add(rdbtnNo);
		
		ButtonGroup yesNoGroup = new ButtonGroup();
		yesNoGroup.add(rdbtnYes);
		yesNoGroup.add(rdbtnNo);
		
		JButton btnAnalyse = new JButton("Analyse");
		btnAnalyse.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				ImageAnalyzer9000 ia9000 = new ImageAnalyzer9000(selectedFile.getAbsolutePath());
				String ko [] = ia9000.getLabel();
				lblImageGuess.setText(ko[0]);
				lblConfidenceOutput.setText(ko[1]);
				analysed = true;
			}
		});
		btnAnalyse.setBounds(261, 404, 117, 42);
		contentPane.add(btnAnalyse);
		
		JButton btnSubmit = new JButton("Submit");
		btnSubmit.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				try {
					Class.forName("com.mysql.jdbc.Driver").newInstance();
					Connection con = DriverManager.getConnection(
							"jdbc:mysql://mysql.csc.liv.ac.uk:3306/u5af", "u5af", "");
			        
					
					File imgfile = new File(selectedFile.getPath());

					FileInputStream fin = new FileInputStream(imgfile);

					PreparedStatement pre =
					con.prepareStatement("INSERT INTO images VALUES(?,?,?,?,?)");

					pre.setString(1, setLabelName("1"));
					
					pre.setString(2, lblImageGuess.getText());
					
					pre.setBinaryStream(3,(InputStream)fin,(int)imgfile.length());
					
					pre.setInt(4, Integer.parseInt(lblConfidenceOutput.getText()));
					
					pre.setString(5,checkGuess());
					
					pre.executeUpdate();
					System.out.println("Successfully inserted the file into the database!");

					pre.close();
					con.close(); 
				}catch (Exception e1){
					e1.printStackTrace();
				}
				if(!imageChosen) {
					JOptionPane.showMessageDialog(null, "You must choose an image and analyse it first");
				}
				else if(!analysed) {
					JOptionPane.showMessageDialog(null, "You must analyse the image first");
				}
				else if(rdbtnYes.isSelected() == false && rdbtnNo.isSelected() == false) {
					JOptionPane.showMessageDialog(null, "Select whether the guess is correct");
				}
			}
		});
		btnSubmit.setBounds(416, 320, 117, 42);
		contentPane.add(btnSubmit);
		
		JButton btnBack = new JButton("Back");
		btnBack.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				try {
					
					Home.getHome().setVisible(true);
					Home.getImageUp().dispose();
				}catch(Exception e3) {
					JOptionPane.showMessageDialog(null, e3);
				}
			}
		});
		btnBack.setBounds(26, 12, 98, 23);
		contentPane.add(btnBack);
	}
	
	public String setLabelName(String label) {
		String labelCheck = "";
		try {
			Class.forName("com.mysql.jdbc.Driver").newInstance();
			Connection con = DriverManager.getConnection(
					"jdbc:mysql://mysql.csc.liv.ac.uk:3306/u5af", "u5af", "");
			
			Statement statement = con.createStatement();
            ResultSet resultSet;
            resultSet = statement.executeQuery("SELECT label FROM images WHERE label = '"+label.trim()+"'");
            while (resultSet.next()) {
            	labelCheck = resultSet.getString(1);
            }
            if (labelCheck.equals(label)) {
            	int change = Integer.parseInt(label);
            	change++;
            	label = Integer.toString(change);
            	setLabelName(label);
            }
		}catch (Exception e) {
			e.printStackTrace();
		}
		return label;
	}
	
	public String checkGuess() {
		String answer = "";
		if (rdbtnYes.isSelected() == true) {
			answer = "Y";
		} else if (rdbtnNo.isSelected() == true) {
			answer = "N";
		}
		return answer;
	}
}
