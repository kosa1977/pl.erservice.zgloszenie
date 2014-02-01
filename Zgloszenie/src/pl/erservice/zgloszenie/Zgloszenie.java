package pl.erservice.zgloszenie;

import java.io.IOException;
import java.sql.Connection;
//import java.sql.DriverManager;
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
	private ArrayList<String> nazwa_statusu = new ArrayList<String>();
	private ArrayList<String> data_zgl = new ArrayList<String>();
	private ArrayList<String> data_wymag = new ArrayList<String>();
	//private static final String DRIVER = "org.sqlite.JDBC";
	//private static final String url = "jdbc:sqlite:HD.db";
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
	
	public ArrayList<String> getNazwaStatusu() {
		return this.nazwa_statusu;
	}
	
	public ArrayList<String> getDataZgl() {
		return this.data_zgl;
	}
	
	public ArrayList<String> getDataWymag() {
		return this.data_wymag;
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
	
	public void setNazwaStatusu(String nazwa_statusu) {
		this.nazwa_statusu.add(nazwa_statusu);
	}
	
	public void setDataZgl(String data_zgl) {
		this.data_zgl.add(data_zgl);
	}
	
	public void setDataWymag(String data_wymag) {
		this.data_wymag.add(data_wymag);
	}
	
	public Zgloszenie() {
		//konstruktor domyślny
		//this.poplaczZbaza();
		this.createDBtables();
	}
	
	public Zgloszenie(int id,String nazwa_komorki, String nazwa_opisu, String nazwa_statusu, String data_zgl, String data_wymag) {
		this.id.add(id);
		this.nazwa_komorki.add(nazwa_komorki);
		this.nazwa_opisu.add(nazwa_opisu);
		this.nazwa_statusu.add(nazwa_statusu);
		this.data_zgl.add(data_zgl);
		this.data_wymag.add(data_wymag);
		//this.poplaczZbaza();
		this.createDBtables();
	}
	
	/*public void poplaczZbaza() {	// Tworzy lub łączy z bazą danych 'HD.db' i wywoluje metode 'createDBtables'
		try{
			Class.forName(Zgloszenie.DRIVER);
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
	}*/
	
	public boolean createDBtables() {	// tworzy tabele w bazie jeśli tabela nie istnieje
		// tu można dać nowy obiekt klasy 'Connection' i: conn = obj; potem st=conn; potem st.execute("PRAGMA foreign_keys = ON");
		// pamętać, że dla każdej z klas po wywołaniu (poplaczZbaza / Connection) trzeba je 
		try {
			conn = PoplaczZbaza.getConnection();
			st = conn.createStatement();
			st.execute("PRAGMA foreign_keys = ON");
		} catch (SQLException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		String createKomorka = "CREATE TABLE IF NOT EXISTS komorka(id_komorki INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT,"
				+ "nazwa_komorki varchar(20) NOT NULL)";
		String createOpis = "CREATE TABLE IF NOT EXISTS opis(id_opisu INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT,"
				+ "nazwa_opisu varchar(20) NOT NULL)";
		String createStatus = "CREATE TABLE IF NOT EXISTS status(id_statusu INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT,"
				+ "nazwa_statusu varchar(20) NOT NULL)";
		String createTerminZgl = "CREATE TABLE IF NOT EXISTS termin_zgl(id_term_zgl INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT,"
				+ "data_zgl varchar(20) NOT NULL)";
		String createTerminWymag = "CREATE TABLE IF NOT EXISTS termin_wymag(id_term_wymag INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT,"
				+ "data_wymag varchar(23) NOT NULL)";
		String createZgloszenie = "CREATE TABLE IF NOT EXISTS zgloszenie( id INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT,"
				+ "id_komorki int NOT NULL, id_opisu int NOT NULL, id_statusu int NOT NULL, id_term_zgl int NOT NULL, id_term_wymag int NOT NULL,"
				+ "CONSTRAINT k_komorka_id_komorki FOREIGN KEY(id_komorki) REFERENCES komorka(id_komorki) ON UPDATE CASCADE ON DELETE CASCADE,"
				+ "CONSTRAINT fk_opis_id_opisu FOREIGN KEY(id_opisu) REFERENCES opis(id_opisu) ON UPDATE CASCADE ON DELETE CASCADE,"
				+ "CONSTRAINT fk_status_id_statusu FOREIGN KEY(id_statusu) REFERENCES status(id_statusu) ON UPDATE CASCADE ON DELETE CASCADE,"
				+ "CONSTRAINT fk_termin_zgl_id_term_zgl FOREIGN KEY(id_term_zgl) REFERENCES termin_zgl (id_term_zgl) ON UPDATE CASCADE ON DELETE CASCADE,"
				+ "CONSTRAINT fk_termin_wymag_id_term_wymag FOREIGN KEY(id_term_wymag) REFERENCES termin_wymag (id_term_wymag) ON UPDATE CASCADE ON DELETE CASCADE)";
		try{
			st.execute(createKomorka);
			st.execute(createOpis);
			st.execute(createStatus);
			st.execute(createTerminZgl);
			st.execute(createTerminWymag);
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
			ps_initial_termin_zgl.setString(1, "2000-10-10 15:01");
			ps_initial_termin_zgl.execute();
			
			PreparedStatement ps_initial_termin_wymag = conn.prepareStatement("INSERT INTO termin_wymag values(null, ?)");
			ps_initial_termin_wymag.setString(1, "2001-10-11 15:30");
			ps_initial_termin_wymag.execute();
		}
		catch (SQLException e) {
			e.printStackTrace();
			return false;
		}
		return true;
	}
	
	public boolean insertZgloszenie(int id_komorki, int id_term_zgl, int id_term_wymag, int id_opisu, int id_statusu,
			String nazwa_komorki, String nazwa_opisu, String nazwa_statusu, String data_zgl, String data_wymag) {	// wstawia dane do tabeli 'zgloszenie' w bazie 'HD.db'
		try{
			//PreparedStatement ps_komorka = conn.prepareStatement("INSERT INTO komorka values(null, ?);");
			//ps_komorka.setString(1, nazwa_komorki);
			//ps_komorka.execute();
			
			PreparedStatement ps_opis = conn.prepareStatement("INSERT INTO opis values(null, ?);");
			ps_opis.setString(1, nazwa_opisu);
			ps_opis.execute();
			
			PreparedStatement ps_termin_zgl = conn.prepareStatement("INSERT INTO termin_zgl values(null, ?);");
			ps_termin_zgl.setString(1, data_zgl);
			ps_termin_zgl.execute();
			
			PreparedStatement ps_termin_wymag = conn.prepareStatement("INSERT INTO termin_wymag values(null, ?);");
			ps_termin_wymag.setString(1, data_wymag);
			ps_termin_wymag.execute();
			
			PreparedStatement ps_zgloszenie = conn.prepareStatement("INSERT INTO zgloszenie values(null, ?, ?, ?, ?, ?);");
			ps_zgloszenie.setInt(1, id_komorki);
			ps_zgloszenie.setInt(2, id_opisu);
			ps_zgloszenie.setInt(3, id_statusu);
			ps_zgloszenie.setInt(4, id_term_zgl);
			ps_zgloszenie.setInt(5, id_term_wymag);
			ps_zgloszenie.execute();
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
	
	public boolean modifyZgloszenie(int id_komorki, int id_opisu, int id_statusu, int id_term_zgl, int id_term_wymag, int id,
			String nazwa_komorki, String nazwa_opisu, String nazwa_statusu, String data_zgl, String data_wymag)	{
		try {
			PreparedStatement ps_opis = conn.prepareStatement("UPDATE opis SET nazwa_opisu = ? WHERE id_opisu = ?;");
			ps_opis.setString(1, nazwa_opisu);
			ps_opis.setInt(2, id_opisu);
			ps_opis.executeUpdate();

			PreparedStatement ps_term_zgl = conn.prepareStatement("UPDATE termin_zgl SET data_zgl = ? WHERE id_term_zgl = ?;");
			ps_term_zgl.setString(1, data_zgl);
			ps_term_zgl.setInt(2, id_term_zgl);
			ps_term_zgl.executeUpdate();
			
			PreparedStatement ps_term_wymag = conn.prepareStatement("UPDATE termin_wymag SET data_wymag = ? WHERE id_term_wymag = ?;");
			ps_term_wymag.setString(1, data_wymag);
			ps_term_wymag.setInt(2, id_term_wymag);
			ps_term_wymag.executeUpdate();
			
			//najpierw modyfikujemy pozostałe tabele poza 'komorka' i 'status' (tym ewentualnie zmienia się id w 'zgloszenie') na końcu 'zgloszenie'
			PreparedStatement ps = conn.prepareStatement("UPDATE zgloszenie SET id_komorki = ?, id_opisu = ?, id_statusu = ?, id_term_zgl = ?, id_term_wymag = ? WHERE id = ?;");
			ps.setInt(1, id_komorki);
			ps.setInt(2, id_opisu);
			ps.setInt(3, id_statusu);
			ps.setInt(4, id_term_zgl);
			ps.setInt(5, id_term_wymag);
			ps.setInt(6, id);
			ps.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
			return false;
		}
		return true;
	}
	
	public void selectZgloszenie() {					// podstawia wynik zapytania SQL do metody set i przekazuje pól klasy 'Zgloszenie'.
		try{
			ResultSet wynik = st.executeQuery("SELECT z.id, k.nazwa_komorki, t.data_zgl, tw.data_wymag, o.nazwa_opisu, s.nazwa_statusu FROM zgloszenie AS z LEFT JOIN komorka AS k ON z.id_komorki = k.id_komorki LEFT JOIN opis AS o ON z.id_opisu = o.id_opisu LEFT JOIN status AS s ON z.id_statusu = s.id_statusu LEFT JOIN termin_zgl AS t ON z.id_term_zgl = t.id_term_zgl LEFT JOIN termin_wymag AS tw ON z.id_term_wymag = tw.id_term_wymag ORDER BY z.id;");
			while(wynik.next()) {
				this.setId(wynik.getInt("id"));
				this.setNazwaKomorki(wynik.getString("nazwa_komorki"));
				this.setNazwaOpisu(wynik.getString("nazwa_opisu"));
				this.setNazwaStatusu(wynik.getString("nazwa_statusu"));
				this.setDataZgl(wynik.getString("data_zgl"));
				this.setDataWymag(wynik.getString("data_wymag"));
			}
		}
		catch(SQLException e5) {
			e5.printStackTrace();
		}
	}
	
	public int[] selectID(int id) {
		int id_wynik[] = new int[6];
		try {
			String query = "SELECT id_komorki, id_opisu, id_statusu, id_term_zgl, id_term_wymag FROM zgloszenie WHERE id = " + id;
			ResultSet wynik = st.executeQuery(query);
			//int id_komorki = wynik.getInt("id_komorki");
			//int id_opisu = wynik.getInt("id_opisu");
			//int id_statusu = wynik.getInt("id_statusu");
			//int id_term_zgl = wynik.getInt("id_term_zgl");
			//int id_term_wymag = wynik.getInt("id_term_wymag");
			for(int i = 1 ; i < 6; i++) {
				id_wynik[i] = wynik.getInt(i);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return id_wynik;
	}
	
	public void showZgloszenie(JTable table1 , DefaultTableModel model)	{				// wywołuje metode 'selectZgloszenie'; pobiera dane z bazy;
		this.selectZgloszenie();							// za pomoca metod klasy 'KZgloszenie' podstawia dane do pól i konfiguruje 'DefaultTableModel';
		Integer konwersjaI[] = new Integer[this.id.size()]; // podstawia do JTable 'tabela' model 'DefaultTableModel' 
		String konwersjaS[] = new String[this.nazwa_komorki.size()];
		String konwersjaOpisu[] = new String[this.nazwa_opisu.size()];
		String konwersjaStatusu[] = new String[this.nazwa_statusu.size()];
		String konwersjaTermZgl[] = new String[this.data_zgl.size()];
		String konwersjaTermWymag[] = new String[this.data_wymag.size()];
		
		@SuppressWarnings("serial")	//krzykacz dot. serializacji obiektu
		DefaultTableModel mojModel = new DefaultTableModel() {
			@Override
			public boolean isCellEditable(int row, int column)	{
				return false;
			}
		};
		
		Object[] columnNames = new Object[6];
		columnNames[0] = "id";
		columnNames[1] = "Komórka org.";
		columnNames[2] = "Opis zgł.";
		columnNames[3] = "Status";
		columnNames[4] = "data zgł.";
		columnNames[5] = "data wymag.";
		mojModel.setColumnIdentifiers(columnNames);

		JTable tabela = new JTable();
		tabela = table1;
		table1.setAutoCreateRowSorter(true);
		tabela.setModel(mojModel);
		
		konwersjaI = this.id.toArray(konwersjaI);
		konwersjaS = this.nazwa_komorki.toArray(konwersjaS);
		konwersjaOpisu = this.nazwa_opisu.toArray(konwersjaOpisu);
		konwersjaStatusu = this.nazwa_statusu.toArray(konwersjaStatusu);
		konwersjaTermZgl = this.data_zgl.toArray(konwersjaTermZgl);
		konwersjaTermWymag = this.data_wymag.toArray(konwersjaTermWymag);
		
		Object obj[] = new Object[6];
		for(int i = 0; i < konwersjaI.length; i++) {
			obj[0] = konwersjaI[i];
			obj[1] = konwersjaS[i];
			obj[2] = konwersjaOpisu[i];
			obj[3] = konwersjaStatusu[i];
			obj[4] = konwersjaTermZgl[i];
			obj[5] = konwersjaTermWymag[i];
			mojModel.addRow(obj);
			table1.setRowHeight(i, 60);
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
