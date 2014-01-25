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

class Zgloszenie {
	private ArrayList<Integer> id = new ArrayList<Integer>();
	private ArrayList<String> nazwa_komorki = new ArrayList<String>();
	private ArrayList<String> nazwa_opisu = new ArrayList<String>();
	private ArrayList<String> data_zgl = new ArrayList<String>();
	private static final String DRIVER = "org.sqlite.JDBC";
	private static final String url = "jdbc:sqlite:HD.db";
	private Connection conn;
	private Statement st;
	
	public ArrayList<Integer> getId() {
		return this.id;
	}
	
	public ArrayList<String> getNazwaKomorki() {
		return this.nazwa_komorki;
	}
	
	public ArrayList<String> getNazwaOpisu() {
		return this.nazwa_opisu;
	}
	
	public ArrayList<String> getDataZgl() {
		return this.data_zgl;
	}
	
	public void setId(int id) {
		this.id.add(id);
	}
	
	public void setNazwaKomorki(String nazwa_komorki) {
		this.nazwa_komorki.add(nazwa_komorki);
	}
	
	public void setNazwaOpisu(String nazwa_opisu) {
		this.nazwa_opisu.add(nazwa_opisu);
	}
	
	public void setDataZgl(String data_zgl) {
		this.data_zgl.add(data_zgl);
	}
	
	public Zgloszenie() {
		//konstruktor domyślny
		this.poplaczZbaza();
	}
	
	public Zgloszenie(int id,String nazwa_komorki, String nazwa_opisu, String data_zgl) {
		this.id.add(id);
		this.nazwa_komorki.add(nazwa_komorki);
		this.nazwa_opisu.add(nazwa_opisu);
		this.data_zgl.add(data_zgl);
		this.poplaczZbaza();
	}
	
	public void poplaczZbaza() {	// Tworzy lub łączy z bazą danych 'HD.db' i wywoluje metode 'createDBtables'
		try{
			Class.forName(Zgloszenie.DRIVER);
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
		String createKomorka = "CREATE TABLE IF NOT EXISTS komorka(id_komorki INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT,"
				+ "nazwa_komorki varchar(20) NOT NULL)";
		String createOpis = "CREATE TABLE IF NOT EXISTS opis(id_opisu INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT,"
				+ "nazwa_opisu varchar(20) NOT NULL)";
		String createTerminZgl = "CREATE TABLE IF NOT EXISTS termin_zgl(id_term_zgl INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT,"
				+ "data_zgl varchar(20) NOT NULL)";
		String createZgloszenie = "CREATE TABLE IF NOT EXISTS zgloszenie( id INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT,"
				+ "id_komorki int NOT NULL, id_opisu int NOT NULL, id_term_zgl int NOT NULL, FOREIGN KEY(id_komorki) REFERENCES komorka(id_komorki),"
				+ " FOREIGN KEY(id_opisu) REFERENCES opis(id_opisu), FOREIGN KEY(id_term_zgl) REFERENCES termin_zgl (id_term_zgl) )";
		try{
			st.execute(createKomorka);
			st.execute(createOpis);
			st.execute(createTerminZgl);
			st.execute(createZgloszenie);
		}
		catch(SQLException e2) {
			e2.printStackTrace();
			return false;
		}
		return true;
	}
	
	public boolean insertInitialRecords() {
		try {
			PreparedStatement ps_initial_opis = conn.prepareStatement("INSERT INTO opis values(null, ?);");
			ps_initial_opis.setString(1, "initial record");
			ps_initial_opis.execute();
			
			PreparedStatement ps_initial_termin_zgl = conn.prepareStatement("INSERT INTO termin_zgl values(null, ?)");
			ps_initial_termin_zgl.setString(1, "2000-10-10");
			ps_initial_termin_zgl.execute();
		}
		catch (SQLException e) {
			e.printStackTrace();
			return false;
		}
		return true;
	}
	
	public boolean insertZgloszenie(int id_komorki, int id_term_zgl, int id_opisu,
			String nazwa_komorki, String nazwa_opisu, String data_zgl) {	// wstawia dane do tabeli 'zgloszenie' w bazie 'HD.db'
		try{
			//PreparedStatement ps_komorka = conn.prepareStatement("INSERT INTO komorka values(null, ?);");
			//ps_komorka.setString(1, nazwa_komorki);
			//ps_komorka.execute();
			
			PreparedStatement ps_zgloszenie = conn.prepareStatement("INSERT INTO zgloszenie values(null, ?, ?, ?);");
			ps_zgloszenie.setInt(1, id_komorki);
			ps_zgloszenie.setInt(2, id_opisu);
			ps_zgloszenie.setInt(3, id_term_zgl);
			ps_zgloszenie.execute();
			
			PreparedStatement ps_opis = conn.prepareStatement("INSERT INTO opis values(null, ?);");
			ps_opis.setString(1, nazwa_opisu);
			ps_opis.execute();
			
			PreparedStatement ps_termin_zgl = conn.prepareStatement("INSERT INTO termin_zgl values(null, ?);");
			ps_termin_zgl.setString(1, data_zgl);
			ps_termin_zgl.execute();
		}
		catch(SQLException e3) {
			e3.printStackTrace();
			return false;
		}
		return true;
	}
	
	public boolean deleteZgloszenie(int id) {			// usuwa wybrany wiersz z tabeli'komorka'
		try {
			PreparedStatement ps = conn.prepareStatement("DELETE FROM zgloszenie WHERE id = ?;");
			ps.setInt(1, id);
			ps.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
			return false;
		}
		return true;
	}
	
	public boolean modifyZgloszenie(int id, int id_komorki, int id_opisu, int id_term_zgl)	{
		try {
			PreparedStatement ps = conn.prepareStatement("UPDATE zgloszenie SET id_komorki = ?, SET id_opisu = ?, SET id_term_zgl = ? WHERE id = ?;");
			ps.setInt(1, id);
			ps.setInt(2, id_komorki);
			ps.setInt(3, id_opisu);
			ps.setInt(4, id_term_zgl);
			ps.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
			return false;
		}
		return true;
	}
	
	public void selectZgloszenie() {					// podstawia wynik zapytania SQL do metody set i przekazuje pól klasy 'Zgloszenie'.
		try{
			ResultSet wynik = st.executeQuery("SELECT z.id, k.nazwa_komorki, t.data_zgl, o.nazwa_opisu FROM zgloszenie AS z LEFT JOIN komorka AS k ON z.id_komorki = k.id_komorki LEFT JOIN opis AS o ON z.id_opisu = o.id_opisu LEFT JOIN termin_zgl AS t ON z.id_term_zgl = t.id_term_zgl ORDER BY z.id;");
			while(wynik.next()) {
				this.setId(wynik.getInt("id"));
				this.setNazwaKomorki(wynik.getString("nazwa_komorki"));
				this.setNazwaOpisu(wynik.getString("nazwa_opisu"));
				this.setDataZgl(wynik.getString("data_zgl"));
			}
		}
		catch(SQLException e5) {
			e5.printStackTrace();
		}
	}
	
	public void showZgloszenie(JTable table1 , DefaultTableModel model)	{				// wywołuje metode 'selectZgloszenie'; pobiera dane z bazy;
		this.selectZgloszenie();							// za pomoca metod klasy 'KZgloszenie' podstawia dane do pól i konfiguruje 'DefaultTableModel';
		Integer konwersjaI[] = new Integer[this.id.size()]; // podstawia do JTable 'tabela' model 'DefaultTableModel' 
		String konwersjaS[] = new String[this.nazwa_komorki.size()];
		String konwersjaOpisu[] = new String[this.nazwa_opisu.size()];
		String konwersjaTermZgl[] = new String[this.data_zgl.size()];
		
		@SuppressWarnings("serial")	//krzykacz dot. serializacji obiektu
		DefaultTableModel mojModel = new DefaultTableModel() {
			@Override
			public boolean isCellEditable(int row, int column)	{
				return false;
			}
		};
		
		Object[] columnNames = new Object[4];
		columnNames[0] = "id";
		columnNames[1] = "nazwa_komorki";
		columnNames[2] = "nazwa_opisu";
		columnNames[3] = "data_zgl";
		mojModel.setColumnIdentifiers(columnNames);
		
		JTable tabela = new JTable();
		tabela = table1;
		tabela.setModel(mojModel);
		
		
		konwersjaI = this.id.toArray(konwersjaI);
		konwersjaS = this.nazwa_komorki.toArray(konwersjaS);
		konwersjaOpisu = this.nazwa_opisu.toArray(konwersjaOpisu);
		konwersjaTermZgl = this.data_zgl.toArray(konwersjaTermZgl);
		
		Object[] obj = new Object[4];
		for(int i = 0; i < konwersjaI.length; i++) {
			obj[0] = konwersjaI[i];
			obj[1] = konwersjaS[i];
			obj[2] = konwersjaOpisu[i];
			obj[3] = konwersjaTermZgl[i];
			mojModel.addRow(obj);
		}
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
