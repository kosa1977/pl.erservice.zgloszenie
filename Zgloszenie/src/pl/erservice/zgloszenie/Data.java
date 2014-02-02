package pl.erservice.zgloszenie;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;

class Data {
	private ArrayList<Integer> id = new ArrayList<Integer>();
	private ArrayList<String> data_zgl = new ArrayList<String>();
	private ArrayList<String> data_wymag = new ArrayList<String>();
	private ArrayList<String> data_wyk = new ArrayList<String>();
	private Connection conn;
	private Statement st;
	private int max_id;
	
	public ArrayList<Integer> getId() {
		return this.id;
	}
	
	public ArrayList<String> getDataZ() {
		return this.data_zgl;
	}
	
	public ArrayList<String> getDataWymag() {
		return this.data_wymag;
	}
	
	public ArrayList<String> getDataWyk() {
		return this.data_wyk;
	}
	
	public void setId(int id) {
		this.id.add(id);
	}
	
	public void setDataZ(String data_zgl) {
		this.data_zgl.add(data_zgl);
	}
	
	public void setDataWymag(String data_wymag) {
		this.data_wymag.add(data_wymag);
	}
	
	public void setDataWyk(String data_wyk) {
		this.data_wyk.add(data_wyk);
	}
	
	public Data() {
		//konstruktor domyślny
		this.createDBtables();
	}
	
	public Data(int id, String data_zgl, String data_wymag, String data_wyk) {
		this.id.add(id);
		this.data_zgl.add(data_zgl);
		this.data_wymag.add(data_zgl);
		this.data_wyk.add(data_wyk);
		this.createDBtables();
	}
	
	public boolean createDBtables() {	// tworzy tabele w bazie jeśli tabela nie istnieje
		try {
			conn = PoplaczZbaza.getConnection();
			st = conn.createStatement();
			st.execute("PRAGMA foreign_keys = ON");
		} catch (SQLException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		String createData = "CREATE TABLE IF NOT EXISTS data(id_data INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT,"
				+ "data_zgl varchar(23) NOT NULL,"
				+ "data_wymag varchar(23) NOT NULL,"
				+ "data_wyk varchar(23) )";
		try{
			st.execute(createData);
		}
		catch(SQLException e2) {
			e2.printStackTrace();
			return false;
		}
		return true;
	}
	
	public boolean insertTerminZgl(String data_zgl, String data_wymag, String data_wyk) {	// wstawia dane do tabeli 'komorka' w bazie 'HD.db'
		try{
			PreparedStatement ps = conn.prepareStatement("INSERT INTO data values(null, ?, ?, ?);");
			ps.setString(1, data_zgl);
			ps.setString(2, data_wymag);
			ps.setString(3, data_wyk);
			ps.execute();
		}
		catch(SQLException e3) {
			e3.printStackTrace();
			return false;
		}
		return true;
	}
	
	public boolean deleteTerminZgl(int id_data) {			// usuwa wybrany wiersz z tabeli'komorka'
		try {
			PreparedStatement ps = conn.prepareStatement("DELETE FROM data WHERE id_term_zgl = ?;");
			ps.setInt(1, id_data);
			ps.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
			return false;
		}
		return true;
	}
	
	public boolean modifyTerminZgl(int id_data, String data_zgl, String data_wymag, String data_wyk)	{
		try {
			PreparedStatement ps = conn.prepareStatement("UPDATE termin_zgl SET data_zgl = ?, data_wymag = ?, data_wyk = ? WHERE id_term_zgl = ?;");
			ps.setString(1, data_zgl);
			ps.setString(2, data_wymag);
			ps.setString(3, data_wyk);
			ps.setInt(4, id_data);
			ps.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
			return false;
		}
		return true;
	}
	
	public void selectTerminZgl() {							// podstawia wynik zapytania SQL do metody set i przekazuje pól klasy 'Komorka'.
		try{
			ResultSet wynik = st.executeQuery("SELECT * FROM data");
			while(wynik.next()) {
				this.setId(wynik.getInt("id_data"));
				this.setDataZ(wynik.getString("data_zgl"));
				this.setDataWymag(wynik.getString("data_wymag"));
				this.setDataWyk(wynik.getString("data_wyk"));
			}
		}
		catch(SQLException e5) {
			e5.printStackTrace();
		}
	}
	
	public void showTerminZgl(JTable table1 , DefaultTableModel model)	{				// wywołuje metode 'selectKomorka'; pobiera dane z bazy;
		this.selectTerminZgl();								// za pomoca metod klasy 'Komorka' podstawia dane do pól i konfiguruje 'DefaultTableModel';
		Integer konwersjaI[] = new Integer[this.id.size()]; // podstawia do JTable 'tabela' model 'DefaultTableModel' 
		String konwersjaZgl[] = new String[this.data_zgl.size()];
		String konwersjaWymag[] = new String[this.data_wymag.size()];
		String konwersjaWyk[] = new String[this.data_wyk.size()];
		
		@SuppressWarnings("serial")	//krzykacz dot. serializacji obiektu
		DefaultTableModel mojModel = new DefaultTableModel() {
			@Override
			public boolean isCellEditable(int row, int column)	{
				return false;
			}
		};
		
		Object[] columnNames = new Object[4];
		columnNames[0] = "id_data";
		columnNames[1] = "data_zgl";
		columnNames[2] = "data_wymag";
		columnNames[3] = "data_wyk";
		mojModel.setColumnIdentifiers(columnNames);
		
		JTable tabela = new JTable();
		tabela = table1;
		tabela.setModel(mojModel);
		
		
		konwersjaI = this.id.toArray(konwersjaI);
		konwersjaZgl = this.data_zgl.toArray(konwersjaZgl);
		
		Object[] obj = new Object[4];
		for(int i = 0; i < konwersjaI.length; i++) {
			obj[0] = konwersjaI[i];
			obj[1] = konwersjaZgl[i];
			obj[2] = konwersjaWymag[i];
			obj[3] = konwersjaWyk[i];
			mojModel.addRow(obj);
		}
	}
	
	public int tData() {
		this.selectTerminZgl();
		try{
			ResultSet wynik = st.executeQuery("SELECT seq FROM sqlite_sequence WHERE name = 'data'");
			this.max_id = wynik.getInt("seq");
			max_id = max_id + 1;
		}
			catch(SQLException e) {
				e.printStackTrace();
			}
		return max_id;
	}
	
	public void closeConn() {		// kończy połączenie z bazą danych 'HD.db'
		try{
			//if(conn != null) {
				conn.close();
			//}
		}
		catch(SQLException e4) {
			e4.printStackTrace();
		}
	}
}
