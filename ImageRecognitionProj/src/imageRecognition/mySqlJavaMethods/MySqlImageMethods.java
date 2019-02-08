// Credit: StackOverflow - https://stackoverflow.com/questions/35410171/how-to-insert-image-to-mysql-database-in-netbeans-java

import java.sql.*;
import java.io.*;

public class MySqlImageMethods 
{
	private String driverName;
	private String url;
	private String dbName;
	private String userName;
	private String password;
	private Connection con;
	
	
	public MySqlImageMethods()
	{
		driverName = "com.mysql.jdbc.Driver";
		url = "jdbc:mysql://mysql.csc.liv.ac.uk:3306/";
		dbName = "u5af";
		userName = "u5af";
		password = "";
		con = null;
	}
	
	public void InsertImage()
	{
		try{
			Class.forName(driverName);
			con = DriverManager.getConnection(url+dbName,userName,password);
			Statement st = con.createStatement();
			File imgfile = new File("H:/Pictures/maxresdefault.jpg");

			FileInputStream fin = new FileInputStream(imgfile);

			PreparedStatement pre =
			con.prepareStatement("insert into images values(?,?,?,?,?)");

			pre.setString(1,"369");
			pre.setInt(2,3);
			pre.setBinaryStream(3,(InputStream)fin,(int)imgfile.length());
			pre.setInt(4,69);
			pre.setString(5,"Y");
			pre.executeUpdate();
			System.out.println("Successfully inserted the file into the database!");

			pre.close();
			con.close(); 
		}catch (Exception e1){
			System.out.println(e1.getMessage());
		}
	}
	
	public void RetriveImage()
	{
		try{
			Class.forName(driverName);
			con = DriverManager.getConnection(url+dbName,userName,password);
			Statement stmt = con.createStatement();
			ResultSet rs = stmt.executeQuery("select image from images");
			int i = 0;
			while (rs.next()) {
				InputStream in = rs.getBinaryStream(1);
				OutputStream f = new FileOutputStream(new File("test"+i+".jpg"));
				i++;
				int c = 0;
				while ((c = in.read()) > -1) {
					f.write(c);
				}
				f.close();
				in.close();
			}
		}catch(Exception ex){
			System.out.println(ex.getMessage());
		}
	}
}
