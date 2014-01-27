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

class Status {
	private ArrayList<Integer> id = new ArrayList<Integer>();
	private ArrayList<String> nazwa_statusu = new ArrayList<String>();
	private static final String DRIVER = "org.sqlite.JDBC";
	private static final String url = "jdbc:sqlite:HD.db";
	private Connection conn;
	private Statement st;
	
	public ArrayList<Integer> getId() {
		return this.id;
	}
	
	public void setId(int id) {
		this.id.add(id);
	}
	
	public ArrayList<String> getNazwa() {
		return this.nazwa_statusu;
	}
	
	public void setNazwa(String nazwa_statusu) {
		this.nazwa_statusu.add(nazwa_statusu);
	}
	
	public Status() {
		//konstruktor domyślny
		this.poplaczZbaza();
	}
	
	public Status(int id, String nazwa_statusu) {
		this.id.add(id);
		this.nazwa_statusu.add(nazwa_statusu);
		this.poplaczZbaza();
	}
	
	public void poplaczZbaza() {	// Tworzy lub łączy z bazą danych 'HD.db' i wywoluje metode 'createDBtables'
		try{
			Class.forName(Status.DRIVER);
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
		String createStatus = "CREATE TABLE IF NOT EXISTS status(id_statusu INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT,"
				+ "nazwa_statusu varchar(20) NOT NULL)";
		try{
			st.execute(createStatus);
		}
		catch(SQLException e2) {
			e2.printStackTrace();
			return false;
		}
		return true;
	}
	
	public boolean insertStatus(String nazwa_statusu) {	// wstawia dane do tabeli 'komorka' w bazie 'HD.db'
		try{
			PreparedStatement ps = conn.prepareStatement("INSERT INTO status values(null, ?);");
			ps.setString(1, nazwa_statusu);
			ps.execute();
		}
		catch(SQLException e3) {
			e3.printStackTrace();
			return false;
		}
		return true;
	}
	
	public boolean deleteStatus(int id_statusu) {			// usuwa wybrany wiersz z tabeli'komorka'
		try {
			PreparedStatement ps = conn.prepareStatement("DELETE FROM status WHERE id_statusu = ?;");
			ps.setInt(1, id_statusu);
			ps.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
			return false;
		}
		return true;
	}
	
	public boolean modifyStatus(int id_statusu, String nazwa_statusu)	{
		try {
			PreparedStatement ps = conn.prepareStatement("UPDATE status SET nazwa_statusu = ? WHERE id_statusu = ?;");
			ps.setString(1, nazwa_statusu);
			ps.setInt(2, id_statusu);
			ps.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
			return false;
		}
		return true;
	}
	
	public void selectStatus() {							// podstawia wynik zapytania SQL do metody set i przekazuje pól klasy 'Komorka'.
		try{
			ResultSet wynik = st.executeQuery("SELECT * FROM status");
			while(wynik.next()) {
				this.setId(wynik.getInt("id_statusu"));
				this.setNazwa(wynik.getString("nazwa_statusu"));
			}
		}
		catch(SQLException e5) {
			e5.printStackTrace();
		}
	}
	
	public void showStatus(JTable table1 , DefaultTableModel model)	{				// wywołuje metode 'selectStatus'; pobiera dane z bazy;
		this.selectStatus();								// za pomoca metod klasy 'Status' podstawia dane do pól i konfiguruje 'DefaultTableModel';
		Integer konwersjaI[] = new Integer[this.id.size()]; // podstawia do JTable 'tabela' model 'DefaultTableModel' 
		String konwersjaS[] = new String[this.nazwa_statusu.size()];
		
		@SuppressWarnings("serial")	//krzykacz dot. serializacji obiektu
		DefaultTableModel mojModel = new DefaultTableModel() {
			@Override
			public boolean isCellEditable(int row, int column)	{
				return false;
			}
		};
		
		Object[] columnNames = new Object[2];
		columnNames[0] = "id_statusu";
		columnNames[1] = "nazwa_statusu";
		mojModel.setColumnIdentifiers(columnNames);
		
		JTable tabela = new JTable();
		tabela = table1;
		tabela.setModel(mojModel);
		
		
		konwersjaI = this.id.toArray(konwersjaI);
		konwersjaS = this.nazwa_statusu.toArray(konwersjaS);
		
		Object[] obj = new Object[2];
		for(int i = 0; i < konwersjaI.length; i++) {
			obj[0] = konwersjaI[i];
			obj[1] = konwersjaS[i];
			mojModel.addRow(obj);
		}
	}
	
	public String[] tStatus() {
		this.selectStatus();
		ArrayList<String> nazwa_statusuT = new ArrayList<String>();
		String konwersjaS[] = new String[nazwa_statusuT.size()];
		try{
			ResultSet wynik = st.executeQuery("SELECT * FROM status");
			while(wynik.next()) {
				nazwa_statusuT.add(wynik.getInt("id_statusu")+ " " + wynik.getString("nazwa_statusu") );
				//nazwa_komorkiT.add(wynik.getString("nazwa_komorki"));
			}
			konwersjaS = nazwa_statusuT.toArray(konwersjaS);
		}
			catch(SQLException e) {
				e.printStackTrace();
			}
		return konwersjaS;
	}
	
	public String[] tStatus_id() {		//to tylko testowo - do usunięcia!!!!
		this.selectStatus();
		ArrayList<Integer> id_statusuT = new ArrayList<Integer>();
		String konwersjaI[] = new String[id_statusuT.size()];
		try{
			ResultSet wynik = st.executeQuery("SELECT id_statusu FROM status");
			while(wynik.next()) {
				id_statusuT.add(wynik.getInt("id_statusu"));
			}
			konwersjaI = id_statusuT.toArray(konwersjaI);
		}
			catch(SQLException e) {
				e.printStackTrace();
			}
		return konwersjaI;
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
