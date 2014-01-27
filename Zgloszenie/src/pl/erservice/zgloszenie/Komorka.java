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

class Komorka  {
	private ArrayList<Integer> id = new ArrayList<Integer>();
	private ArrayList<String> nazwa_komorki = new ArrayList<String>();
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
		return this.nazwa_komorki;
	}
	
	public void setNazwa(String nazwa_komorki) {
		this.nazwa_komorki.add(nazwa_komorki);
	}
	
	public Komorka() {
		//konstruktor domyślny
		this.poplaczZbaza();
	}
	
	public Komorka(int id, String nazwa_komorki) {
		this.id.add(id);
		this.nazwa_komorki.add(nazwa_komorki);
		this.poplaczZbaza();
	}
	
	public void poplaczZbaza() {	// Tworzy lub łączy z bazą danych 'HD.db' i wywoluje metode 'createDBtables'
		try{
			Class.forName(Komorka.DRIVER);
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
		String createKomorka = "CREATE TABLE IF NOT EXISTS komorka(id_komorki INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT,"
				+ "nazwa_komorki varchar(20) NOT NULL)";
		try{
			st.execute(createKomorka);
		}
		catch(SQLException e2) {
			e2.printStackTrace();
			return false;
		}
		return true;
	}
	
	public boolean insertKomorka(String nazwa_komorki) {	// wstawia dane do tabeli 'komorka' w bazie 'HD.db'
		try{
			PreparedStatement ps = conn.prepareStatement("INSERT INTO komorka values(null, ?);");
			ps.setString(1, nazwa_komorki);
			ps.execute();
		}
		catch(SQLException e3) {
			e3.printStackTrace();
			return false;
		}
		return true;
	}
	
	public boolean deleteKomorka(int id_komorki) {			// usuwa wybrany wiersz z tabeli'komorka'
		try {
			PreparedStatement ps = conn.prepareStatement("DELETE FROM komorka WHERE id_komorki = ?;");
			ps.setInt(1, id_komorki);
			ps.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
			return false;
		}
		return true;
	}
	
	public boolean modifyKomorka(int id_komorki, String nazwa_komorki)	{
		try {
			PreparedStatement ps = conn.prepareStatement("UPDATE komorka SET nazwa_komorki = ? WHERE id_komorki = ?;");
			ps.setString(1, nazwa_komorki);
			ps.setInt(2, id_komorki);
			ps.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
			return false;
		}
		return true;
	}
	
	public void selectKomorka() {							// podstawia wynik zapytania SQL do metody set i przekazuje pól klasy 'Komorka'.
		try{
			ResultSet wynik = st.executeQuery("SELECT * FROM komorka");
			while(wynik.next()) {
				this.setId(wynik.getInt("id_komorki"));
				this.setNazwa(wynik.getString("nazwa_komorki"));
			}
		}
		catch(SQLException e5) {
			e5.printStackTrace();
		}
	}
	
	public void showKomorka(JTable table1 , DefaultTableModel model)	{				// wywołuje metode 'selectKomorka'; pobiera dane z bazy;
		this.selectKomorka();								// za pomoca metod klasy 'Komorka' podstawia dane do pól i konfiguruje 'DefaultTableModel';
		Integer konwersjaI[] = new Integer[this.id.size()]; // podstawia do JTable 'tabela' model 'DefaultTableModel' 
		String konwersjaS[] = new String[this.nazwa_komorki.size()];
		
		@SuppressWarnings("serial")	//krzykacz dot. serializacji obiektu
		DefaultTableModel mojModel = new DefaultTableModel() {
			@Override
			public boolean isCellEditable(int row, int column)	{
				return false;
			}
		};
		
		Object[] columnNames = new Object[2];
		columnNames[0] = "id_komorki";
		columnNames[1] = "nazwa_komorki";
		mojModel.setColumnIdentifiers(columnNames);
		
		JTable tabela = new JTable();
		tabela = table1;
		tabela.setModel(mojModel);
		
		
		konwersjaI = this.id.toArray(konwersjaI);
		konwersjaS = this.nazwa_komorki.toArray(konwersjaS);
		
		Object[] obj = new Object[2];
		for(int i = 0; i < konwersjaI.length; i++) {
			obj[0] = konwersjaI[i];
			obj[1] = konwersjaS[i];
			mojModel.addRow(obj);
		}
	}
	
	public String[] tKomorka() {
		this.selectKomorka();
		ArrayList<String> nazwa_komorkiT = new ArrayList<String>();
		String konwersjaS[] = new String[nazwa_komorkiT.size()];
		try{
			ResultSet wynik = st.executeQuery("SELECT * FROM komorka");
			while(wynik.next()) {
				nazwa_komorkiT.add(wynik.getInt("id_komorki")+ " " + wynik.getString("nazwa_komorki") );
			}
			konwersjaS = nazwa_komorkiT.toArray(konwersjaS);
		}
			catch(SQLException e) {
				e.printStackTrace();
			}
		return konwersjaS;
	}
	
	public String[] tKomorka_id() {		//to tylko testowo - do usunięcia!!!!
		this.selectKomorka();
		ArrayList<Integer> id_komorkiT = new ArrayList<Integer>();
		String konwersjaI[] = new String[id_komorkiT.size()];
		try{
			ResultSet wynik = st.executeQuery("SELECT id_komorki FROM komorka");
			while(wynik.next()) {
				id_komorkiT.add(wynik.getInt("id_komorki"));
			}
			konwersjaI = id_komorkiT.toArray(konwersjaI);
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
