package pl.erservice.zgloszenie;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;

class TerminWymag {
	private ArrayList<Integer> id = new ArrayList<Integer>();
	private ArrayList<String> data_wymag = new ArrayList<String>();
	private static final String DRIVER = "org.sqlite.JDBC";
	private static final String url = "jdbc:sqlite:HD.db";
	private Connection conn;
	private Statement st;
	private int max_id;
	
	public ArrayList<Integer> getId() {
		return this.id;
	}
	
	public void setId(int id) {
		this.id.add(id);
	}
	
	public ArrayList<String> getData() {
		return this.data_wymag;
	}
	
	public void setData(String data_wymag) {
		this.data_wymag.add(data_wymag);
	}
	
	public TerminWymag() {
		//konstruktor domyślny
		this.poplaczZbaza();
	}
	
	public TerminWymag(int id, String data_wymag) {
		this.id.add(id);
		this.data_wymag.add(data_wymag);
		this.poplaczZbaza();
	}
	
	public void poplaczZbaza() {	// Tworzy lub łączy z bazą danych 'HD.db' i wywoluje metode 'createDBtables'
		try{
			Class.forName(TerminWymag.DRIVER);
		}
		catch(ClassNotFoundException e) {
			e.printStackTrace();
		}
		try{
			conn = DriverManager.getConnection(url);
			st = conn.createStatement();
			st.execute("PRAGMA foreign_keys = ON");
		}
		catch(SQLException e1) {
			e1.printStackTrace();
		}
		createDBtables();
	}
	
	public boolean createDBtables() {	// tworzy tabele w bazie jeśli tabela nie istnieje
		String createTerminWymag = "CREATE TABLE IF NOT EXISTS termin_wymag(id_term_wymag INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT,"
				+ "data_wymag varchar(23) NOT NULL)";
		try{
			st.execute(createTerminWymag);
		}
		catch(SQLException e2) {
			e2.printStackTrace();
			return false;
		}
		return true;
	}
	
	public boolean insertTerminWymag(String data_wymag) {	// wstawia dane do tabeli 'komorka' w bazie 'HD.db'
		try{
			PreparedStatement ps = conn.prepareStatement("INSERT INTO termin_wymag values(null, ?);");
			ps.setString(1, data_wymag);
			ps.execute();
		}
		catch(SQLException e3) {
			e3.printStackTrace();
			return false;
		}
		return true;
	}
	
	public boolean deleteTerminWymag(int id_term_wymag) {			// usuwa wybrany wiersz z tabeli'komorka'
		try {
			PreparedStatement ps = conn.prepareStatement("DELETE FROM termin_wymag WHERE id_term_wymag = ?;");
			ps.setInt(1, id_term_wymag);
			ps.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
			return false;
		}
		return true;
	}
	
	public boolean modifyTerminWymag(int id_term_wymag, String data_wymag)	{
		try {
			PreparedStatement ps = conn.prepareStatement("UPDATE termin_wymag SET data_wymag = ? WHERE id_term_wymag = ?;");
			ps.setString(1, data_wymag);
			ps.setInt(2, id_term_wymag);
			ps.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
			return false;
		}
		return true;
	}
	
	public void selectTerminWymag() {							// podstawia wynik zapytania SQL do metody set i przekazuje pól klasy 'Komorka'.
		try{
			ResultSet wynik = st.executeQuery("SELECT * FROM termin_wymag");
			while(wynik.next()) {
				this.setId(wynik.getInt("id_term_wymag"));
				this.setData(wynik.getString("data_wymag"));
			}
		}
		catch(SQLException e5) {
			e5.printStackTrace();
		}
	}
	
	public void showTerminWymag(JTable table1 , DefaultTableModel model)	{				// wywołuje metode 'selectKomorka'; pobiera dane z bazy;
		this.selectTerminWymag();								// za pomoca metod klasy 'Komorka' podstawia dane do pól i konfiguruje 'DefaultTableModel';
		Integer konwersjaI[] = new Integer[this.id.size()]; // podstawia do JTable 'tabela' model 'DefaultTableModel' 
		String konwersjaS[] = new String[this.data_wymag.size()];
		
		@SuppressWarnings("serial")	//krzykacz dot. serializacji obiektu
		DefaultTableModel mojModel = new DefaultTableModel() {
			@Override
			public boolean isCellEditable(int row, int column)	{
				return false;
			}
		};
		
		Object[] columnNames = new Object[2];
		columnNames[0] = "id_term_wymag";
		columnNames[1] = "data_wymag";
		mojModel.setColumnIdentifiers(columnNames);
		
		JTable tabela = new JTable();
		tabela = table1;
		tabela.setModel(mojModel);
		
		
		konwersjaI = this.id.toArray(konwersjaI);
		konwersjaS = this.data_wymag.toArray(konwersjaS);
		
		Object[] obj = new Object[2];
		for(int i = 0; i < konwersjaI.length; i++) {
			obj[0] = konwersjaI[i];
			obj[1] = konwersjaS[i];
			mojModel.addRow(obj);
		}
	}
	
	public int tTerminWymag() {
		this.selectTerminWymag();
		try{
			ResultSet wynik = st.executeQuery("SELECT seq FROM sqlite_sequence WHERE name = 'termin_wymag'");
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
