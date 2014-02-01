package pl.erservice.zgloszenie;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

class PoplaczZbaza {
	public static Connection getConnection() throws SQLException, IOException {
		Properties prop = new Properties();
		try (
				FileInputStream in = new FileInputStream( new File("src/hd.properties")))

		{
			prop.load(in);
			in.close();
		}
		String sterownik = prop.getProperty("jdbc.drivers");
		if(sterownik != null) {
			System.setProperty("jdbc.drivers", sterownik);
		}
		String url = prop.getProperty("jdbc.url");
		
		return DriverManager.getConnection(url);
	}
}
