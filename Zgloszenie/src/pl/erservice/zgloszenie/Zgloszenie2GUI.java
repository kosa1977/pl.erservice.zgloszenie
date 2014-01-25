package pl.erservice.zgloszenie;

import java.awt.Component;
import java.awt.EventQueue;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.Font;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.table.DefaultTableModel;
import javax.swing.JTextArea;
import javax.swing.border.TitledBorder;
import javax.swing.UIManager;
import javax.swing.JComboBox;
import javax.swing.JFormattedTextField;

public class Zgloszenie2GUI extends JFrame {

	/**
	 * 
	 */
	private static final long serialVersionUID = -9007557187115999340L;
	private JTextField textFieldnNazwaKomorki;
	private JTable table = new JTable();
	private DefaultTableModel model;
	private JTextArea textAreaNazwaOpisu;
	private JTextArea textAreaNazwaOpisu2;
	private JFormattedTextField fTextFieldTerminZgl;
	//private DefaultListModel listModel = new DefaultListModel();

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					JFrame.setDefaultLookAndFeelDecorated(true);
					Zgloszenie2GUI frame = new Zgloszenie2GUI();
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the frame.
	 */
	public Zgloszenie2GUI() {
		setBounds(100, 100, 700, 650);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		getContentPane().setLayout(null);
		
		JTabbedPane tabbedPane = new JTabbedPane(JTabbedPane.TOP);
		tabbedPane.setBounds(10, 11, 664, 209);
		getContentPane().add(tabbedPane);
		
		
		JPanel panelZgloszenie = new JPanel();
		tabbedPane.addTab("Tabela Zgłoszenie", panelZgloszenie);
		tabbedPane.setEnabledAt(0, true);
		panelZgloszenie.setLayout(null);
		
		textAreaNazwaOpisu2 = new JTextArea();
		textAreaNazwaOpisu2.setToolTipText("Wprowadź opis zgłoszenia.");
		textAreaNazwaOpisu2.setFont(new Font("Monospaced", Font.PLAIN, 10));
		textAreaNazwaOpisu2.setBorder(new TitledBorder(UIManager.getBorder("TitledBorder.border"),
				"Opis zg\u0142oszenia", TitledBorder.CENTER, TitledBorder.TOP, null, null));
		textAreaNazwaOpisu2.setBounds(184, 11, 319, 159);
		textAreaNazwaOpisu2.setDragEnabled(true);
		textAreaNazwaOpisu2.setLineWrap(true);
		panelZgloszenie.add(textAreaNazwaOpisu2);
		
		Komorka kom = new Komorka();
		final JComboBox<String> comboBoxKomorka = new JComboBox<String>(kom.tKomorka()); //usunąć komentarz po kompilacji !!!!!!!!!!!!!!!!!!!!!!!!!
		comboBoxKomorka.setToolTipText("Wybierz komórkę organizacyjną");
		comboBoxKomorka.setFont(new Font("Tahoma", Font.PLAIN, 11));
		comboBoxKomorka.setBounds(10, 11, 129, 20);
		comboBoxKomorka.setSelectedIndex(-1);		//po kompilacji zmienić na 4 !!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
		panelZgloszenie.add(comboBoxKomorka);
		kom.closeConn();
		
		fTextFieldTerminZgl = new JFormattedTextField();
		fTextFieldTerminZgl.setToolTipText("Wprowadź datę i godzinę zgłoszenia");
		fTextFieldTerminZgl.setBounds(513, 11, 122, 20);
		panelZgloszenie.add(fTextFieldTerminZgl);
		
		
		JButton btnInitialRecords = new JButton("Inicjalizacja");
		btnInitialRecords.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				//tu rekord inicjalizacyjny z klasy Zgloszenie.
				Zgloszenie zgl = new Zgloszenie();
				zgl.insertInitialRecords();	
				zgl.closeConn();
			}
		});
		btnInitialRecords.setToolTipText("Inicjalizacja bazy.");
		btnInitialRecords.setBounds(3, 147, 136, 23);
		panelZgloszenie.add(btnInitialRecords);
		
		JButton btnAddZgloszenie = new JButton("Dodaj Zgłoszenie");
		btnAddZgloszenie.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				// tu kod obsługujący dodanie wszystkich pól zgłoszenia do bazy
				String tmp = (String) comboBoxKomorka.getSelectedItem();
				String id_komorki = "";
				String nazwa_komorki = "";
				int i = tmp.length();
				while(i > 0) {
					if (tmp.matches("^(\\d).*")){
						id_komorki = tmp.replaceAll("[^0-9]", "");
						nazwa_komorki = tmp.replaceAll("[0-9]\\s", "");
						i--;
					}
				}
				JOptionPane.showMessageDialog(null, id_komorki);
				JOptionPane.showMessageDialog(null, nazwa_komorki);
				String nazwa_opisu = textAreaNazwaOpisu2.getText();
				String data_zgl = fTextFieldTerminZgl.getText();
				TerminZgl tZgl1 = new TerminZgl();
				tZgl1.tTerminZgl();
				int max_id_termZgl = 0;
				max_id_termZgl = tZgl1.tTerminZgl();
				JOptionPane.showMessageDialog(null, "max_id_termZgl = "+max_id_termZgl);
				tZgl1.closeConn();
				
				// koniec tego samego co poniżej dla TerminZgl
				
				Opis tOpis1 = new Opis();
				int max_id_opisu = 0;
				max_id_opisu = tOpis1.tOpis_id();
				JOptionPane.showMessageDialog(null, "max_id_opisu = "+max_id_opisu);
				tOpis1.closeConn();
				
				Zgloszenie zgl1 = new Zgloszenie();
				zgl1.insertZgloszenie(Integer.parseInt(id_komorki), max_id_termZgl, max_id_opisu, nazwa_komorki, nazwa_opisu, data_zgl);
				zgl1.closeConn();
			}
		});
		btnAddZgloszenie.setToolTipText("Dodaje kolejne zgłoszenie do bazy");
		btnAddZgloszenie.setBounds(513, 147, 136, 23);
		panelZgloszenie.add(btnAddZgloszenie);		
		
		JButton btnShowZgloszenia = new JButton("Zgłoszenia");
		btnShowZgloszenia.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				// tu pokaż zgłoszenia //
				Zgloszenie zgl = new Zgloszenie();
				zgl.showZgloszenie(table, model);
				zgl.closeConn();
			}
		});
		btnShowZgloszenia.setToolTipText("Pkaż wszystkie zgłoszenia");
		btnShowZgloszenia.setBounds(513, 117, 136, 23);
		panelZgloszenie.add(btnShowZgloszenia);
		
		
		JPanel panelKomorka = new JPanel();
		panelKomorka.setLayout(null);
		tabbedPane.addTab("Tabela Komórka", panelKomorka);	//dodaje panelKomorka do zakładki
		tabbedPane.setEnabledAt(1, true);
		
		JButton btnKomorka = new JButton("Add Komórka");
		btnKomorka.setFont(new Font("Tahoma", Font.PLAIN, 11));
		btnKomorka.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				Komorka kom = new Komorka();
				if(kom.insertKomorka(textFieldnNazwaKomorki.getText()) ) {	//dodaje kolejny wiersz do bazy
					JOptionPane.showMessageDialog(null, "Nowa jednostka org. została dodana do bazy:\n" + textFieldnNazwaKomorki.getText());
				};
				kom.closeConn();
			}
		});
		btnKomorka.setToolTipText("Dodaj nową komórkę org. do bazy.");
		btnKomorka.setAlignmentX(Component.CENTER_ALIGNMENT);
		btnKomorka.setBounds(10, 11, 120, 23);
		panelKomorka.add(btnKomorka);
		
		textFieldnNazwaKomorki = new JTextField();
		textFieldnNazwaKomorki.setBounds(140, 11, 160, 23);
		panelKomorka.add(textFieldnNazwaKomorki);
		textFieldnNazwaKomorki.setColumns(20);
		
		JButton btnShowKomorka = new JButton("Show Komórka"); // pokazuje zawartość tabeli 'komorka'
		btnShowKomorka.setFont(new Font("Tahoma", Font.PLAIN, 11));
		btnShowKomorka.setToolTipText("Pokaż zawartość tabeli 'komorka'");
		btnShowKomorka.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				Komorka kom = new Komorka();
				kom.showKomorka(table, model);
				kom.closeConn();
			}
		});
		btnShowKomorka.setBounds(10, 60, 120, 23);
		panelKomorka.add(btnShowKomorka);
		
		JButton btnRemoveSelected = new JButton("Usuń wiersz");
		btnRemoveSelected.setFont(new Font("Tahoma", Font.PLAIN, 11));
		btnRemoveSelected.setToolTipText("usuń wiersz z tabeli 'komorka'");
		btnRemoveSelected.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				Komorka kom = new Komorka();
				kom.showKomorka(table, model);
				String input = JOptionPane.showInputDialog("Podaj numer wiersza do usunięcia:");
				if(input != null)	{
					kom.deleteKomorka(Integer.parseInt(input)); // tu ma być wartość z wiersza poniżej
					model.removeRow(Integer.parseInt(input));
				}
				kom.showKomorka(table, model);
				kom.closeConn();
			}
		});
		btnRemoveSelected.setBounds(10, 110, 120, 23);
		panelKomorka.add(btnRemoveSelected);
		
		JButton btnModyfikuj = new JButton("Edytuj");
		btnModyfikuj.setFont(new Font("Tahoma", Font.PLAIN, 11));
		btnModyfikuj.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				Komorka kom = new Komorka();
				String input = JOptionPane.showInputDialog("Podaj numer wiersza do modyfikacji:");
				String input2 = JOptionPane.showInputDialog("wprowadź nową wartość komórki org. ...");
				if((input != null) && (input2 != null)) {
					kom.modifyKomorka(Integer.parseInt(input), input2);
				}
				kom.showKomorka(table, model);
				kom.closeConn();
			}
		});
		btnModyfikuj.setToolTipText("Edytuj wiersz tabeli 'komorka'");
		btnModyfikuj.setBounds(10, 158, 120, 23);
		panelKomorka.add(btnModyfikuj);
		
		
		
		JPanel panelOpis = new JPanel();
		panelOpis.setFont(new Font("Tahoma", Font.PLAIN, 11));		
		tabbedPane.addTab("Tabela Opis", panelOpis);
		tabbedPane.setEnabledAt(2, true);
		
		JButton btnShowOpis = new JButton("Show Opis");
		btnShowOpis.setFont(new Font("Tahoma", Font.PLAIN, 11));
		btnShowOpis.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				Opis opis = new Opis();
				opis.showOpis(table, model);
				opis.closeConn();
			}
		});
		panelOpis.setLayout(null);
		btnShowOpis.setAlignmentX(Component.CENTER_ALIGNMENT);
		btnShowOpis.setBounds(10, 11, 120, 23);
		panelOpis.add(btnShowOpis);
		
		JButton btnAddOpis = new JButton("Add Opis");
		btnAddOpis.setFont(new Font("Tahoma", Font.PLAIN, 11));
		btnAddOpis.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				Opis opis = new Opis();
				if(opis.insertOpis(textAreaNazwaOpisu.getText()) ) {	//dodaje kolejny wiersz do bazy
					JOptionPane.showMessageDialog(null, "Nowy opis zgłoszenia został dodany do bazy:\n" + textAreaNazwaOpisu.getText());
				};
				opis.closeConn();
			}
		});
		btnAddOpis.setBounds(529, 11, 120, 23);
		panelOpis.add(btnAddOpis);
		
		textAreaNazwaOpisu = new JTextArea();
		textAreaNazwaOpisu.setFont(new Font("Monospaced", Font.PLAIN, 10));
		textAreaNazwaOpisu.setBorder(new TitledBorder(UIManager.getBorder("TitledBorder.border"),
				"Opis zg\u0142oszenia", TitledBorder.CENTER, TitledBorder.TOP, null, null));
		textAreaNazwaOpisu.setBounds(184, 11, 319, 159);
		textAreaNazwaOpisu.setDragEnabled(true);
		textAreaNazwaOpisu.setLineWrap(true);
		panelOpis.add(textAreaNazwaOpisu);
		
		/*
		JPanel panelTerminZgl = new JPanel();
		panelTerminZgl.setFont(new Font("Tahoma", Font.PLAIN, 11));		
		tabbedPane.addTab("Tabela Data zgł.", panelTerminZgl);
		tabbedPane.setEnabledAt(3, true);
		*/
		
		
		
		
		JScrollPane scrollPane = new JScrollPane(table);
		scrollPane.setBounds(10, 220, 664, 382);
		getContentPane().add(scrollPane);
		table.setFillsViewportHeight(true);	

	}
}
