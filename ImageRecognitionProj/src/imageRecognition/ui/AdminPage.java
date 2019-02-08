package imageRecognition.ui;

import java.awt.Color;
import java.awt.EventQueue;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.Statement;
import java.util.Vector;

import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JButton;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableColumn;
import javax.swing.SwingConstants;
import javax.swing.JLabel;

import java.awt.Font;
import javax.swing.JTextField;

public class AdminPage extends JFrame {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private JPanel contentPane;
	public JTable imagesTable;
	private JTable analysisTable;
	private JTextField viewTextBox;
	private static ImageView viewImage;
	
	public static ImageView getViewer() {
		return viewImage;
	} 

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					new AdminPage();
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the frame.
	 */
	public AdminPage() {
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		/* *************** DATABASE TABLE ******************* */
	
		Vector<String> columnNamesDB = new Vector<String>();
        Vector<Vector> dataDB = new Vector<Vector>();
        contentPane = new JPanel();   //
        try {
            Class.forName("com.mysql.jdbc.Driver").newInstance();
            Connection con = DriverManager.getConnection(
                    "jdbc:mysql://mysql.csc.liv.ac.uk:3306/u5af", "u5af", "");
            String sql = "Select label, tag, confidence, correct from images";
            Statement statement = con.createStatement();
            ResultSet resultSet = statement.executeQuery(sql);
            ResultSetMetaData metaData = resultSet.getMetaData();
            int columns = metaData.getColumnCount();
            for (int i = 1; i <= columns; i++) {
                columnNamesDB.addElement(metaData.getColumnName(i));
            }
            while (resultSet.next()) {
                Vector<Object> row = new Vector<Object>(columns);
                for (int i = 1; i <= columns; i++) {
                    row.addElement(resultSet.getObject(i));
                }
                dataDB.addElement(row);
            }
            resultSet.close();
            statement.close();
        } catch (Exception e) {
            System.out.println(e);
        }
        JTable databaseTable = new JTable(dataDB, columnNamesDB);
        TableColumn column;
        for (int i = 0; i < databaseTable.getColumnCount(); i++) {
            column = databaseTable.getColumnModel().getColumn(i);
            column.setMaxWidth(250);
        }
        contentPane.setLayout(null);
        JScrollPane tablePane = new JScrollPane(databaseTable);        
        contentPane.add(tablePane);
        tablePane.setBounds(50, 70, 300, 350);
        JFrame frame = new JFrame();
        frame.setTitle("Admin Page");
        frame.getContentPane().add(contentPane);         				//adding panel to the frame
        frame.setBounds(300, 300, 642, 580); 							//setting frame size
        frame.setVisible(true);  										//setting visibility true
        
        /* ***************** END OF TABLE ******************* */
        
        // Log out button
        JButton btnLogOut = new JButton("Log out");
        btnLogOut.addActionListener(new ActionListener() {
        	public void actionPerformed(ActionEvent e) {
        		try {
        			Home.getLogin().setVisible(true);
        			frame.dispose();
        		}catch(Exception e1) {
        			JOptionPane.showMessageDialog(null, e1);
        		}
        	}
        });
        btnLogOut.setBounds(26, 12, 98, 23);
        contentPane.add(btnLogOut);
        
        /* *************** ANALYSIS TABLE ******************* */
        
        JScrollPane analysisPane = new JScrollPane();
        analysisPane.setBounds(402, 124, 202, 296);
        contentPane.add(analysisPane);
        
        String[]  rowNames = {"No. of images", "No. correct", "Average Confidence", "Av. conf. of correct", 
        		"Av. conf. of incorrect", "Percentage correct"};
        
        int row1 = 0;
        int row2 = 0;
        int row3 = 0;
        int row4 = 0;
        int row5 = 0;
        int row6 = 0;
        
        int numImages = 0;
        int numCorrect = 0;
        int totalConfidence = 0;
        
        try {
            Class.forName("com.mysql.jdbc.Driver").newInstance();
            Connection con = DriverManager.getConnection(
                    "jdbc:mysql://mysql.csc.liv.ac.uk:3306/u5af", "u5af", "");
            
            String sqlRow1 = "SELECT * FROM images";
            String sqlRow2 = "SELECT * FROM images WHERE correct = 'Y'";
            String sqlRow3 = "SELECT confidence FROM images";
            String sqlRow4 = "SELECT confidence FROM images WHERE correct = 'Y'";
            String sqlRow5 = "SELECT confidence FROM images WHERE correct = 'N'";
            
            
            Statement statement = con.createStatement();
            ResultSet resultSet;
            
            // No. of images
            resultSet = statement.executeQuery(sqlRow1);
            while (resultSet.next()) {
               numImages++;
            }
            row1 = numImages;
            
            // No. correct
            resultSet = statement.executeQuery(sqlRow2);
            while (resultSet.next()) {
               numCorrect++;
            }
            row2 = numCorrect;
            
            // Average Confidence
            resultSet = statement.executeQuery(sqlRow3);
            while (resultSet.next()) {
            	totalConfidence += resultSet.getInt(1);
            }
            if (numImages == 0) {
            	row3 = 0;
            } else {
            	row3 = totalConfidence / numImages;
            }
            
            // Av. conf. of correct
            resultSet = statement.executeQuery(sqlRow4);
            numImages = 0;
            totalConfidence = 0;
            while (resultSet.next()) {
                numImages++;
            	totalConfidence += resultSet.getInt(1);
            }
            if (numImages == 0) {
            	row4 = 0;
            } else {
            	row4 = totalConfidence / numImages;
            }
            
            // Av. conf. of incorrect
            resultSet = statement.executeQuery(sqlRow5);
            numImages = 0;
            totalConfidence = 0;
            while (resultSet.next()) {
                numImages++;
            	totalConfidence += resultSet.getInt(1);
            }
            if (numImages == 0) {
            	row5 = 0;
            } else {
            	row5 = totalConfidence / numImages;
            }
            
            // Percentage correct
            resultSet = statement.executeQuery(sqlRow1);
            numImages = 0;
            while (resultSet.next()) {
                numImages++;
            }
            if (numImages == 0) {
            	row6 = 0;
            } else {
            	row6 = (numCorrect / numImages) * 100;
            }
            
            resultSet.close();
            statement.close();
        } catch (Exception e) {
            System.out.println(e);
        }
        analysisTable = new JTable();
        analysisTable.setModel(new DefaultTableModel(
        	new Object[][] {
        		{rowNames[0], row1},
        		{rowNames[1], row2},
        		{rowNames[2], row3},
        		{rowNames[3], row4},
        		{rowNames[4], row5},
        		{rowNames[5], row6},
        	},
        	new String[] {
        		"Descriptives", "Data"
        	}
        ));
        analysisTable.getColumnModel().getColumn(0).setPreferredWidth(118);
        analysisTable.getColumnModel().getColumn(0).setMinWidth(120);
        analysisTable.setRowHeight(0, 50);
        analysisTable.setRowHeight(1, 50);
        analysisTable.setRowHeight(2, 50);
        analysisTable.setRowHeight(3, 50);
        analysisTable.setRowHeight(4, 50);
        analysisTable.setRowHeight(5, 50);
        
        analysisPane.setColumnHeaderView(analysisTable);
       
        /* *************** END OF TABLE ******************* */
        
        // Analysis Label
        JLabel lblAnalysisBox = new JLabel("Analysis of images");
        lblAnalysisBox.setFont(new Font("Tahoma", Font.BOLD, 14));
        lblAnalysisBox.setHorizontalAlignment(SwingConstants.CENTER);
        lblAnalysisBox.setBounds(392, 87, 212, 26);
        contentPane.add(lblAnalysisBox);
        
        // View Image Label
        JLabel lblSearchImage = new JLabel("View image:");
        lblSearchImage.setFont(new Font("Tahoma", Font.BOLD, 14));
        lblSearchImage.setBounds(41, 431, 98, 23);
        contentPane.add(lblSearchImage);
        
        // Text Bow for image view
        viewTextBox = new JTextField();
        viewTextBox.setBounds(131, 434, 106, 20);
        contentPane.add(viewTextBox);
        viewTextBox.setColumns(10);
        
        // Button to view image
        JButton btnView = new JButton("View");
        btnView.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				try {
					JOptionPane.showMessageDialog(null, "Wait for image to load. Please click okay.");
					Class.forName("com.mysql.jdbc.Driver").newInstance();
					Connection con = DriverManager.getConnection(
							"jdbc:mysql://mysql.csc.liv.ac.uk:3306/u5af", "u5af", "");
			        
					Statement statement = con.createStatement();
		            ResultSet resultSet;
		            ResultSet testStatement;
		            testStatement = statement.executeQuery("SELECT COUNT(*) FROM images WHERE label = '"+viewTextBox.getText().trim()+"'");
		            while (testStatement.next()) {
		            	if (testStatement.getInt(1) == 0) {
			            	ErrorMessage();
			            	revalidate();
			            	repaint();
			            	return;
		            	}
		            }
		            resultSet = statement.executeQuery("SELECT image FROM images WHERE label = '"+viewTextBox.getText().trim()+"'");
		            int i = 0;
					while (resultSet.next()) {
						InputStream in = resultSet.getBinaryStream(1);
						OutputStream f = new FileOutputStream(new File("tempImageFile.jpg"));
						i++;
						int c = 0;
						while ((c = in.read()) > -1) {
							f.write(c);
						}
						f.close();
						in.close();
					}
					
					resultSet.close();
					viewImage = new ImageView();
					viewImage.setVisible(true);
				}catch(Exception e1) {
					JOptionPane.showMessageDialog(null, e1);
				}
			}
		});
        btnView.setBounds(261, 433, 89, 23);
        contentPane.add(btnView);
        
	}
	
	public void ErrorMessage() {
		JLabel lblErrorMessage = new JLabel("Please enter a valid label name");
        lblErrorMessage.setFont(new Font("Tahoma", Font.BOLD, 12));
        lblErrorMessage.setForeground(Color.RED);
        lblErrorMessage.setBounds(96, 465, 202, 14);
        contentPane.add(lblErrorMessage);
	}
}
