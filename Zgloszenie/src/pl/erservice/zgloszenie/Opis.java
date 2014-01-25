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

class Opis {
	private ArrayList<Integer> id = new ArrayList<Integer>();
	private ArrayList<String> nazwa_opisu = new ArrayList<String>();
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
	
	public ArrayList<String> getNazwa() {
		return this.nazwa_opisu;
	}
	
	public void setNazwa(String nazwa_komorki) {
		this.nazwa_opisu.add(nazwa_komorki);
	}
	
	public Opis() {
		//konstruktor domyślny
		this.poplaczZbaza();
	}
	
	public Opis(int id, String nazwa_opisu) {
		this.id.add(id);
		this.nazwa_opisu.add(nazwa_opisu);
		this.poplaczZbaza();
	}
	
	public void poplaczZbaza() {	// Tworzy lub łączy z bazą danych 'HD.db' i wywoluje metode 'createDBtables'
		try{
			Class.forName(Opis.DRIVER);
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
		String createOpis = "CREATE TABLE IF NOT EXISTS opis(id_opisu INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT,"
				+ "nazwa_opisu varchar(20) NOT NULL)";
		try{
			st.execute(createOpis);
		}
		catch(SQLException e2) {
			e2.printStackTrace();
			return false;
		}
		return true;
	}
	
	public boolean insertOpis(String nazwa_opisu) {	// wstawia dane do tabeli 'komorka' w bazie 'HD.db'
		try{
			PreparedStatement ps = conn.prepareStatement("INSERT INTO opis values(null, ?, ?);");
			ps.setString(1, nazwa_opisu);
			ps.execute();
		}
		catch(SQLException e3) {
			e3.printStackTrace();
			return false;
		}
		return true;
	}
	
	public boolean deleteOpis(int id_opisu) {			// usuwa wybrany wiersz z tabeli'komorka'
		try {
			PreparedStatement ps = conn.prepareStatement("DELETE FROM opis WHERE id_opisu = ?;");
			ps.setInt(1, id_opisu);
			ps.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
			return false;
		}
		return true;
	}
	
	public boolean modifyOpis(int id_opisu, String nazwa_opisu)	{
		try {
			PreparedStatement ps = conn.prepareStatement("UPDATE opis SET nazwa_opisu = ? WHERE id_opisu = ?;");
			ps.setString(1, nazwa_opisu);
			ps.setInt(2, id_opisu);
			ps.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
			return false;
		}
		return true;
	}
	
	public void selectOpis() {							// podstawia wynik zapytania SQL do metody set i przekazuje pól klasy 'Komorka'.
		try{
			ResultSet wynik = st.executeQuery("SELECT * FROM opis");
			while(wynik.next()) {
				this.setId(wynik.getInt("id_opisu"));
				this.setNazwa(wynik.getString("nazwa_opisu"));
			}
		}
		catch(SQLException e5) {
			e5.printStackTrace();
		}
	}
	
	public void showOpis(JTable table1 , DefaultTableModel model)	{				// wywołuje metode 'selectKomorka'; pobiera dane z bazy;
		this.selectOpis();								// za pomoca metod klasy 'Komorka' podstawia dane do pól i konfiguruje 'DefaultTableModel';
		Integer konwersjaI[] = new Integer[this.id.size()]; // podstawia do JTable 'tabela' model 'DefaultTableModel' 
		String konwersjaS[] = new String[this.nazwa_opisu.size()];
		
		@SuppressWarnings("serial")	//krzykacz dot. serializacji obiektu
		DefaultTableModel mojModel = new DefaultTableModel() {
			@Override
			public boolean isCellEditable(int row, int column)	{
				return false;
			}
		};
		
		Object[] columnNames = new Object[2];
		columnNames[0] = "id_opisu";
		columnNames[1] = "nazwa_opisu";
		mojModel.setColumnIdentifiers(columnNames);
		
		JTable tabela = new JTable();
		tabela = table1;
		tabela.setModel(mojModel);
		
		
		konwersjaI = this.id.toArray(konwersjaI);
		konwersjaS = this.nazwa_opisu.toArray(konwersjaS);
		
		Object[] obj = new Object[2];
		for(int i = 0; i < konwersjaI.length; i++) {
			obj[0] = konwersjaI[i];
			obj[1] = konwersjaS[i];
			mojModel.addRow(obj);
		}
	}
	
	public int tOpis_id() {
		this.selectOpis();
		try{
			ResultSet wynik = st.executeQuery("SELECT seq FROM sqlite_sequence WHERE name = 'opis'");
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
