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

class TerminZgl {
	private ArrayList<Integer> id = new ArrayList<Integer>();
	private ArrayList<String> data_zgl = new ArrayList<String>();
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
		return this.data_zgl;
	}
	
	public void setData(String data_zgl) {
		this.data_zgl.add(data_zgl);
	}
	
	public TerminZgl() {
		//konstruktor domyślny
		this.poplaczZbaza();
	}
	
	public TerminZgl(int id, String data_zgl) {
		this.id.add(id);
		this.data_zgl.add(data_zgl);
		this.poplaczZbaza();
	}
	
	public void poplaczZbaza() {	// Tworzy lub łączy z bazą danych 'HD.db' i wywoluje metode 'createDBtables'
		try{
			Class.forName(TerminZgl.DRIVER);
		}
		catch(ClassNotFoundException e) {
			e.printStackTrace();
		}
		try{
			conn = DriverManager.getConnection(url);
			st = conn.createStatement();
		}
		catch(SQLException e1) {
			e1.printStackTrace();
		}
		createDBtables();
	}
	
	public boolean createDBtables() {	// tworzy tabele w bazie jeśli tabela nie istnieje
		String createTerminZgl = "CREATE TABLE IF NOT EXISTS termin_zgl(id_term_zgl INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT,"
				+ "data_zgl varchar(23) NOT NULL)";
		try{
			st.execute(createTerminZgl);
		}
		catch(SQLException e2) {
			e2.printStackTrace();
			return false;
		}
		return true;
	}
	
	public boolean insertTerminZgl(String data_zgl) {	// wstawia dane do tabeli 'komorka' w bazie 'HD.db'
		try{
			PreparedStatement ps = conn.prepareStatement("INSERT INTO termin_zgl values(null, ?);");
			ps.setString(1, data_zgl);
			ps.execute();
		}
		catch(SQLException e3) {
			e3.printStackTrace();
			return false;
		}
		return true;
	}
	
	public boolean deleteTerminZgl(int id_term_zgl) {			// usuwa wybrany wiersz z tabeli'komorka'
		try {
			PreparedStatement ps = conn.prepareStatement("DELETE FROM termin_zgl WHERE id_term_zgl = ?;");
			ps.setInt(1, id_term_zgl);
			ps.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
			return false;
		}
		return true;
	}
	
	public boolean modifyTerminZgl(int id_term_zgl, String data_zgl)	{
		try {
			PreparedStatement ps = conn.prepareStatement("UPDATE termin_zgl SET data_zgl = ? WHERE id_term_zgl = ?;");
			ps.setString(1, data_zgl);
			ps.setInt(2, id_term_zgl);
			ps.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
			return false;
		}
		return true;
	}
	
	public void selectTerminZgl() {							// podstawia wynik zapytania SQL do metody set i przekazuje pól klasy 'Komorka'.
		try{
			ResultSet wynik = st.executeQuery("SELECT * FROM termin_zgl");
			while(wynik.next()) {
				this.setId(wynik.getInt("id_term_zgl"));
				this.setData(wynik.getString("data_zgl"));
			}
		}
		catch(SQLException e5) {
			e5.printStackTrace();
		}
	}
	
	public void showTerminZgl(JTable table1 , DefaultTableModel model)	{				// wywołuje metode 'selectKomorka'; pobiera dane z bazy;
		this.selectTerminZgl();								// za pomoca metod klasy 'Komorka' podstawia dane do pól i konfiguruje 'DefaultTableModel';
		Integer konwersjaI[] = new Integer[this.id.size()]; // podstawia do JTable 'tabela' model 'DefaultTableModel' 
		String konwersjaS[] = new String[this.data_zgl.size()];
		
		@SuppressWarnings("serial")	//krzykacz dot. serializacji obiektu
		DefaultTableModel mojModel = new DefaultTableModel() {
			@Override
			public boolean isCellEditable(int row, int column)	{
				return false;
			}
		};
		
		Object[] columnNames = new Object[2];
		columnNames[0] = "id_term_zgl";
		columnNames[1] = "data_zgl";
		mojModel.setColumnIdentifiers(columnNames);
		
		JTable tabela = new JTable();
		tabela = table1;
		tabela.setModel(mojModel);
		
		
		konwersjaI = this.id.toArray(konwersjaI);
		konwersjaS = this.data_zgl.toArray(konwersjaS);
		
		Object[] obj = new Object[2];
		for(int i = 0; i < konwersjaI.length; i++) {
			obj[0] = konwersjaI[i];
			obj[1] = konwersjaS[i];
			mojModel.addRow(obj);
		}
	}
	
	public int tTerminZgl() {
		this.selectTerminZgl();
		try{
			ResultSet wynik = st.executeQuery("SELECT seq FROM sqlite_sequence WHERE name = 'termin_zgl'");
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
