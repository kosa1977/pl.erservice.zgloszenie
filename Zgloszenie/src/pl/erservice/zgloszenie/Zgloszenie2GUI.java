package pl.erservice.zgloszenie;

import java.awt.Component;
import java.awt.EventQueue;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.Font;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import javax.swing.ImageIcon;
import javax.swing.Timer;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.table.DefaultTableModel;
import javax.swing.text.MaskFormatter;
import javax.swing.JTextArea;
import javax.swing.border.TitledBorder;
import javax.swing.UIManager;
import javax.swing.JComboBox;
import javax.swing.JFormattedTextField;

import java.awt.Color;
import java.awt.GridLayout;

import javax.swing.GroupLayout;
import javax.swing.GroupLayout.Alignment;
import javax.swing.LayoutStyle.ComponentPlacement;
import javax.swing.JLabel;

public class Zgloszenie2GUI extends JFrame {

	/**
	 * 
	 */
	private static final long serialVersionUID = -9007557187115999340L;
	private JTextField textFieldnNazwaKomorki;
	private JTable table = new JTable();
	private DefaultTableModel model;
	private JTextField textFieldNazwaStatusu;
	private JTextArea textAreaNazwaOpisu2;
	private JFormattedTextField fTextFieldTerminZgl;
	private JFormattedTextField fTextFieldTerminWymag;
	private JFormattedTextField fTextFieldTerminWyk;
	private JTextField txtIdZgoszenia;
	private JTextField textNazwaKomorki;
	private JTextField textNazwaStatusu;
	
	private SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm");
	private JLabel lblDateTime = new JLabel();

	
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
		setResizable(false);
		setTitle("HelpDesk SRKK v0.280alpha; bulid 0720 by Jełop");
		setBounds(100, 100, 800, 650);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		getContentPane().setLayout(new GridLayout(2, 1, 500, 0));
		
		JTabbedPane tabbedPane = new JTabbedPane(JTabbedPane.TOP);
		getContentPane().add(tabbedPane);
		
		JPanel panelZgloszenie = new JPanel();
		panelZgloszenie.setBackground(new Color(70, 130, 180));
		tabbedPane.addTab("Tabela Zgłoszenie", new ImageIcon("src/png/clip.png"), panelZgloszenie);
		tabbedPane.setEnabledAt(0, true);
		
		textAreaNazwaOpisu2 = new JTextArea();
		textAreaNazwaOpisu2.setWrapStyleWord(true);
		textAreaNazwaOpisu2.setRows(2);
		textAreaNazwaOpisu2.setToolTipText("Wprowadź opis zgłoszenia.");
		textAreaNazwaOpisu2.setFont(new Font("Monospaced", Font.PLAIN, 12));
		textAreaNazwaOpisu2.setBorder(new TitledBorder(UIManager.getBorder("TitledBorder.border"),
				"Opis zg\u0142oszenia", TitledBorder.CENTER, TitledBorder.TOP, null, null));
		textAreaNazwaOpisu2.setDragEnabled(true);
		textAreaNazwaOpisu2.setLineWrap(true);
		
		Komorka kom = new Komorka();
		final JComboBox<String> comboBoxKomorka = new JComboBox<String>(/*kom.tKomorka()*/); //usunąć komentarz po kompilacji !!!!!!!!!!!!!!!!!!!!!!!!!
		comboBoxKomorka.setBorder(new TitledBorder(null, "Wybierz kom\u00F3rk\u0119", TitledBorder.LEADING, TitledBorder.TOP, null, null));
		comboBoxKomorka.setName("");
		comboBoxKomorka.setToolTipText("Wybierz komórkę organizacyjną");
		comboBoxKomorka.setFont(new Font("Tahoma", Font.PLAIN, 11));
		comboBoxKomorka.setSelectedIndex(-1);
		kom.closeConn();
		
		Status stat = new Status();
		final JComboBox<String> comboBoxStatus = new JComboBox<String>(/*stat.tStatus()*/); //usunąć komentarz po kompilacji !!!!!!!!!!!!!!!!!!!!!!!!!
		comboBoxStatus.setBorder(new TitledBorder(null, "Wybierz status", TitledBorder.LEADING, TitledBorder.TOP, null, null));
		comboBoxStatus.setToolTipText("Wybierz status zgłoszenia");
		comboBoxStatus.setFont(new Font("Tahoma", Font.PLAIN, 11));
		comboBoxStatus.setSelectedIndex(-1);
		stat.closeConn();
		
		MaskFormatter formatter = null;
		try {
			formatter = new MaskFormatter("####-##-## ##:##");
			formatter.setAllowsInvalid(false);
			formatter.setPlaceholderCharacter('0');
		} 
		catch (ParseException e1) {
			e1.printStackTrace();
		}
		
		fTextFieldTerminZgl = new JFormattedTextField(formatter);
		fTextFieldTerminZgl.setBorder(new TitledBorder(null, "Data zg\u0142oszenia", TitledBorder.LEADING, TitledBorder.TOP, null, null));
		fTextFieldTerminZgl.setToolTipText("Wprowadź datę i godzinę zgłoszenia");
		
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
				//JOptionPane.showMessageDialog(null, id_komorki);
				//JOptionPane.showMessageDialog(null, nazwa_komorki);
				
				String tmp2 = (String)comboBoxStatus.getSelectedItem();
				String id_statusu = "";
				String nazwa_statusu = "";
				int j = tmp2.length();
				while(j > 0) {
					if(tmp2.matches("^(\\d).*")) {
						id_statusu = tmp2.replaceAll("[^0-9]", "");
						nazwa_statusu = tmp2.replaceAll("[0-9]\\s", "");
						j--;
					}
				}
				//JOptionPane.showMessageDialog(null, id_statusu);
				//JOptionPane.showMessageDialog(null, nazwa_statusu);
				
				String nazwa_opisu = textAreaNazwaOpisu2.getText();
				String data_zgl = fTextFieldTerminZgl.getText();
				String data_wymag = fTextFieldTerminWymag.getText();
				String data_wyk = fTextFieldTerminWyk.getText();
				Data data1 = new Data();
				data1.tData();
				int max_id_data = 0;
				max_id_data = data1.tData();
				data1.closeConn();
				
				// koniec tego samego co poniżej dla TerminZgl
				
				Opis tOpis1 = new Opis();
				int max_id_opisu = 0;
				max_id_opisu = tOpis1.tOpis_id();
				tOpis1.closeConn();
				
				Zgloszenie zgl1 = new Zgloszenie();
				zgl1.insertZgloszenie(Integer.parseInt(id_komorki), max_id_data, max_id_opisu, Integer.parseInt(id_statusu),
						nazwa_komorki, nazwa_opisu, nazwa_statusu, data_zgl, data_wymag, data_wyk);
				zgl1.closeConn();
			}
		});
		btnAddZgloszenie.setToolTipText("Dodaje kolejne zgłoszenie do bazy");
		
		JButton btnShowZgloszenia = new JButton("Zgłoszenia:");
		btnShowZgloszenia.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				// tu pokaż zgłoszenia //
				Zgloszenie zgl = new Zgloszenie();
				zgl.showZgloszenie(table, model);
				zgl.closeConn();
			}
		});
		btnShowZgloszenia.setToolTipText("Pokaż wszystkie zgłoszenia");
		
		fTextFieldTerminWymag = new JFormattedTextField(formatter);
		fTextFieldTerminWymag.setBorder(new TitledBorder(null, "Data Wymagalno\u015Bci", TitledBorder.LEADING, TitledBorder.TOP, null, null));
		
		JButton btnEdytujZgloszenie = new JButton("Edytuj zgłoszenie");
		btnEdytujZgloszenie.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				//Edycja zgłoszenia w bazie//
				Zgloszenie z1 = new Zgloszenie();
				int id;
				id = Integer.parseInt(txtIdZgoszenia.getText());
				z1.selectID(id);
				
				int id_komorki2 = z1.selectID(id)[1];
				int id_opisu = z1.selectID(id)[2];
				int id_statusu2 = z1.selectID(id)[3];
				int id_data = z1.selectID(id)[4];
				z1.closeConn();
				
				String nazwa_opisu = textAreaNazwaOpisu2.getText();
				String data_zgl = fTextFieldTerminZgl.getText();
				String data_wymag = fTextFieldTerminWymag.getText();
				//String data_wyk = fTextFieldTerminWyk.getText(); przeniesiono poniżej comboBox'ów
				String id_komorki = "";
				String nazwa_komorki = "";
				String id_statusu = "";
				String nazwa_statusu = "";
				
				if(comboBoxKomorka.getSelectedIndex() != -1) {
					String tmp = (String) comboBoxKomorka.getSelectedItem();
					int i = tmp.length();
					while(i > 0) {
						if (tmp.matches("^(\\d).*")){
							id_komorki = tmp.replaceAll("[^0-9]", "");
							nazwa_komorki = tmp.replaceAll("[0-9]\\s", "");
							i--;
						}
					}
				}
				else {
						id_komorki = String.valueOf(id_komorki2);
						JOptionPane.showMessageDialog(null, "id_komorki 'else': "+id_komorki2);
						nazwa_komorki = textNazwaKomorki.getText();
				}
				if(comboBoxStatus.getSelectedIndex() != -1)	{
					String tmp2 = (String)comboBoxStatus.getSelectedItem();
					int j = tmp2.length();
					while(j > 0) {
						if(tmp2.matches("^(\\d).*")) {
							id_statusu = tmp2.replaceAll("[^0-9]", "");
							nazwa_statusu = tmp2.replaceAll("[0-9]\\s", "");
							j--;
							if(Integer.parseInt(id_statusu) == 3) {
								fTextFieldTerminWyk.setText(sdf.format(new Date()));
							}
						}
					}
				}
				else {
					id_statusu = String.valueOf(id_statusu2);
					nazwa_statusu = textNazwaStatusu.getText();
				}
				String data_wyk = fTextFieldTerminWyk.getText();
				Zgloszenie zgl2 = new Zgloszenie();
				zgl2.modifyZgloszenie(Integer.parseInt(id_komorki), id_opisu, Integer.parseInt(id_statusu), id_data, id,
						nazwa_komorki, nazwa_opisu, nazwa_statusu, data_zgl, data_wymag, data_wyk);
				zgl2.closeConn();
			}
		});
		btnEdytujZgloszenie.setToolTipText("Edytuj zgłoszenie i zapisz zmiany w bazie.");
		
		txtIdZgoszenia = new JTextField();
		txtIdZgoszenia.setEditable(false);
		txtIdZgoszenia.setBorder(new TitledBorder(null, "ID zg\u0142oszenia:", TitledBorder.LEADING, TitledBorder.TOP, null, null));
		txtIdZgoszenia.setToolTipText("ID wybranego do edycji zgłoszenia");
		txtIdZgoszenia.setColumns(10);
		
		textNazwaKomorki = new JTextField();
		textNazwaKomorki.setVisible(false);
		textNazwaKomorki.setBorder(new TitledBorder(null, "Nazwa kom\u00F3rki", TitledBorder.LEADING, TitledBorder.TOP, null, null));
		textNazwaKomorki.setColumns(10);
		
		textNazwaStatusu = new JTextField();
		textNazwaStatusu.setVisible(false);
		textNazwaStatusu.setBorder(new TitledBorder(null, "Nazwa Statusu", TitledBorder.LEADING, TitledBorder.TOP, null, null));
		textNazwaStatusu.setColumns(10);
		
		// tu jest datatime
		int interval = 1000;
		new Timer(interval, new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				Calendar teraz = Calendar.getInstance();
				lblDateTime.setText(sdf.format(teraz.getTime()));
			}
		}).start();
		lblDateTime.setText("Data i czas");
		lblDateTime.setToolTipText("Aktualna data i czas.");
		panelZgloszenie.add(lblDateTime);
		
		fTextFieldTerminWyk = new JFormattedTextField(formatter);
		fTextFieldTerminWyk.setToolTipText("Data wykonania Zgłoszenia");
		fTextFieldTerminWyk.setBorder(new TitledBorder(null, "Data Wykonania", TitledBorder.LEADING, TitledBorder.TOP, null, null));
		
		JButton btnDelZgloszenie = new JButton("Usuń zgłoszenie");
		btnDelZgloszenie.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				//tu kod usuwający wybrane zgłoszenie
				Zgloszenie zgl3 = new Zgloszenie();
				int id;
				id = Integer.parseInt(txtIdZgoszenia.getText());
				zgl3.deleteZgloszenie(id);
			}
		});
		btnDelZgloszenie.setToolTipText("Usuń wybrane zgłoszenie");

		
		GroupLayout gl_panelZgloszenie = new GroupLayout(panelZgloszenie);
		gl_panelZgloszenie.setHorizontalGroup(
			gl_panelZgloszenie.createParallelGroup(Alignment.TRAILING)
				.addGroup(gl_panelZgloszenie.createSequentialGroup()
					.addContainerGap()
					.addGroup(gl_panelZgloszenie.createParallelGroup(Alignment.LEADING)
						.addGroup(gl_panelZgloszenie.createParallelGroup(Alignment.LEADING, false)
							.addComponent(comboBoxStatus, 0, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
							.addComponent(comboBoxKomorka, 0, 174, Short.MAX_VALUE))
						.addComponent(btnInitialRecords, GroupLayout.PREFERRED_SIZE, 174, GroupLayout.PREFERRED_SIZE)
						.addComponent(lblDateTime, GroupLayout.PREFERRED_SIZE, 133, GroupLayout.PREFERRED_SIZE))
					.addGap(18)
					.addGroup(gl_panelZgloszenie.createParallelGroup(Alignment.LEADING, false)
						.addComponent(textNazwaStatusu, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
						.addGroup(gl_panelZgloszenie.createSequentialGroup()
							.addComponent(textNazwaKomorki, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
							.addPreferredGap(ComponentPlacement.RELATED, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
							.addComponent(txtIdZgoszenia, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
						.addComponent(textAreaNazwaOpisu2, GroupLayout.PREFERRED_SIZE, 403, GroupLayout.PREFERRED_SIZE))
					.addGap(18)
					.addGroup(gl_panelZgloszenie.createParallelGroup(Alignment.LEADING, false)
						.addComponent(btnAddZgloszenie, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
						.addComponent(btnDelZgloszenie, Alignment.TRAILING, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
						.addComponent(btnEdytujZgloszenie, Alignment.TRAILING, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
						.addComponent(fTextFieldTerminWymag, Alignment.TRAILING)
						.addComponent(btnShowZgloszenia, Alignment.TRAILING, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
						.addComponent(fTextFieldTerminWyk, Alignment.TRAILING)
						.addComponent(fTextFieldTerminZgl, Alignment.TRAILING, GroupLayout.DEFAULT_SIZE, 152, Short.MAX_VALUE))
					.addContainerGap())
		);
		gl_panelZgloszenie.setVerticalGroup(
			gl_panelZgloszenie.createParallelGroup(Alignment.TRAILING)
				.addGroup(gl_panelZgloszenie.createSequentialGroup()
					.addContainerGap()
					.addGroup(gl_panelZgloszenie.createParallelGroup(Alignment.BASELINE)
						.addComponent(textAreaNazwaOpisu2, GroupLayout.PREFERRED_SIZE, 160, GroupLayout.PREFERRED_SIZE)
						.addGroup(gl_panelZgloszenie.createSequentialGroup()
							.addComponent(comboBoxKomorka, GroupLayout.PREFERRED_SIZE, 43, GroupLayout.PREFERRED_SIZE)
							.addGap(11)
							.addComponent(comboBoxStatus, GroupLayout.PREFERRED_SIZE, 43, GroupLayout.PREFERRED_SIZE)
							.addGap(40)
							.addComponent(btnInitialRecords, GroupLayout.PREFERRED_SIZE, 23, GroupLayout.PREFERRED_SIZE))
						.addGroup(gl_panelZgloszenie.createSequentialGroup()
							.addComponent(fTextFieldTerminWymag, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
							.addPreferredGap(ComponentPlacement.RELATED)
							.addComponent(fTextFieldTerminZgl, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
							.addPreferredGap(ComponentPlacement.RELATED)
							.addComponent(fTextFieldTerminWyk, GroupLayout.PREFERRED_SIZE, 37, GroupLayout.PREFERRED_SIZE)
							.addPreferredGap(ComponentPlacement.RELATED)
							.addComponent(btnShowZgloszenia, GroupLayout.PREFERRED_SIZE, 23, GroupLayout.PREFERRED_SIZE)
							.addPreferredGap(ComponentPlacement.RELATED)
							.addComponent(btnAddZgloszenie, GroupLayout.PREFERRED_SIZE, 23, GroupLayout.PREFERRED_SIZE)))
					.addPreferredGap(ComponentPlacement.RELATED)
					.addGroup(gl_panelZgloszenie.createParallelGroup(Alignment.LEADING)
						.addGroup(gl_panelZgloszenie.createSequentialGroup()
							.addGap(6)
							.addComponent(btnEdytujZgloszenie)
							.addPreferredGap(ComponentPlacement.RELATED)
							.addComponent(btnDelZgloszenie, GroupLayout.PREFERRED_SIZE, 25, GroupLayout.PREFERRED_SIZE))
						.addGroup(gl_panelZgloszenie.createSequentialGroup()
							.addGroup(gl_panelZgloszenie.createParallelGroup(Alignment.LEADING)
								.addGroup(gl_panelZgloszenie.createSequentialGroup()
									.addGap(12)
									.addComponent(textNazwaKomorki, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
								.addGroup(gl_panelZgloszenie.createSequentialGroup()
									.addGap(2)
									.addComponent(txtIdZgoszenia, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)))
							.addPreferredGap(ComponentPlacement.RELATED)
							.addGroup(gl_panelZgloszenie.createParallelGroup(Alignment.LEADING)
								.addComponent(lblDateTime)
								.addComponent(textNazwaStatusu, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))))
					.addContainerGap())
		);
		panelZgloszenie.setLayout(gl_panelZgloszenie);
		
		
		JPanel panelKomorka = new JPanel();
		panelKomorka.setBackground(new Color(0, 128, 128));
		panelKomorka.setLayout(null);
		tabbedPane.addTab("Tabela Komórka",new ImageIcon("src/png/komorka.png"), panelKomorka);	//dodaje panelKomorka do zakładki
		tabbedPane.setEnabledAt(1, true);
		
		JButton btnKomorka = new JButton("Dodaj komórkę");
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
		btnKomorka.setBounds(8, 46, 120, 23);
		panelKomorka.add(btnKomorka);
		
		textFieldnNazwaKomorki = new JTextField();
		textFieldnNazwaKomorki.setToolTipText("Wprowadź nazwę komórki org.");
		textFieldnNazwaKomorki.setBorder(new TitledBorder(null, "Nazwa kom\u00F3rki", TitledBorder.LEADING, TitledBorder.TOP, null, null));
		textFieldnNazwaKomorki.setBounds(140, 11, 160, 39);
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
		btnShowKomorka.setBounds(8, 11, 120, 23);
		panelKomorka.add(btnShowKomorka);
		
		JButton btnRemoveSelected = new JButton("Usuń komórkę");
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
		btnRemoveSelected.setBounds(8, 116, 120, 23);
		panelKomorka.add(btnRemoveSelected);
		
		JButton btnModyfikuj = new JButton("Edytuj komorkę");
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
		btnModyfikuj.setBounds(8, 81, 120, 23);
		panelKomorka.add(btnModyfikuj);
		
		
		
		JPanel panelStatus = new JPanel();
		panelStatus.setBackground(new Color(46, 139, 87));
		panelStatus.setFont(new Font("Tahoma", Font.PLAIN, 11));		
		tabbedPane.addTab("Tabela Status", new ImageIcon("src/png/mainicon.png"), panelStatus);
		tabbedPane.setEnabledAt(2, true);
		
		JButton btnShowStatus = new JButton("Show Status");
		btnShowStatus.setFont(new Font("Tahoma", Font.PLAIN, 11));
		btnShowStatus.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				Status status = new Status();
				status.showStatus(table, model);
				status.closeConn();
			}
		});
		panelStatus.setLayout(null);
		btnShowStatus.setAlignmentX(Component.CENTER_ALIGNMENT);
		btnShowStatus.setBounds(8, 11, 120, 23);
		panelStatus.add(btnShowStatus);
		
		JButton btnAddStatus = new JButton("Dodaj Status");
		btnAddStatus.setFont(new Font("Tahoma", Font.PLAIN, 11));
		btnAddStatus.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				Status stat = new Status();
				if(stat.insertStatus(textFieldNazwaStatusu.getText()) ) {	//dodaje kolejny Status do bazy
					JOptionPane.showMessageDialog(null, "Nowy status został dodany do bazy:\n" + textFieldNazwaStatusu.getText());
				};
				stat.closeConn();
			}
		});
		btnAddStatus.setBounds(8, 46, 120, 23);
		panelStatus.add(btnAddStatus);
		
		JButton btnRemoveStatus = new JButton("Usuń status");
		btnRemoveStatus.setFont(new Font("Tahoma", Font.PLAIN, 11));
		btnRemoveStatus.setToolTipText("usuń status z tabeli 'status'");
		btnRemoveStatus.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				Status stat = new Status();
				stat.showStatus(table, model);
				String input = JOptionPane.showInputDialog("Podaj numer wiersza do usunięcia:");
				if(input != null)	{
					stat.deleteStatus(Integer.parseInt(input)); // tu ma być wartość z wiersza poniżej
					//model.removeRow(Integer.parseInt(input));
				}
				//stat.showStatus(table, model);
				stat.closeConn();
			}
		});
		btnRemoveStatus.setBounds(8, 115, 120, 23);
		panelStatus.add(btnRemoveStatus);
		
		JButton btnModStatus = new JButton("Edytuj status");
		btnModStatus.setFont(new Font("Tahoma", Font.PLAIN, 11));
		btnModStatus.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				Status stat2 = new Status();
				String input = JOptionPane.showInputDialog("Podaj numer wiersza do modyfikacji:");
				String input2 = JOptionPane.showInputDialog("wprowadź nową wartość statusu. ...");
				if((input != null) && (input2 != null)) {
					stat2.modifyStatus(Integer.parseInt(input), input2);
				}
				stat2.showStatus(table, model);
				stat2.closeConn();
			}
		});
		btnModStatus.setToolTipText("Edytuj wiersz tabeli 'status'");
		btnModStatus.setBounds(8, 80, 120, 23);
		panelStatus.add(btnModStatus);
		
		textFieldNazwaStatusu = new JTextField();
		textFieldNazwaStatusu.setBorder(new TitledBorder(null, "Nazwa statusu", TitledBorder.LEADING, TitledBorder.TOP, null, null));
		textFieldNazwaStatusu.setToolTipText("Wprowadź nową nazwę statusu");
		textFieldNazwaStatusu.setBounds(140, 11, 160, 38);
		panelStatus.add(textFieldNazwaStatusu);
		textFieldNazwaStatusu.setColumns(20);
		table.setForeground(new Color(255, 255, 255));
		table.setBackground(new Color(119, 136, 153));
		table.setOpaque(false);
		table.setGridColor(new Color(0, 0, 205));
		
		table.addMouseListener(new MouseAdapter() {
			public void mouseClicked(final MouseEvent e) {
				if(e.getClickCount() == 1) {
					table = (JTable)e.getSource();
					int row = table.getSelectedRow();
					String id = String.valueOf(table.getValueAt(row, 0));
					txtIdZgoszenia.setText(id);
				}
			}
		});
		table.addMouseListener(new MouseAdapter() {
			public void mouseClicked(final MouseEvent e) {
				if(e.getClickCount() == 1) {
					table = (JTable)e.getSource();
					int row = table.getSelectedRow();
					//int col = table.getSelectedColumn();
					String nazwa_komorki = (String)table.getValueAt(row, 1);
					textNazwaKomorki.setText(nazwa_komorki);
				}
			}
		});
		table.addMouseListener(new MouseAdapter() {
			public void mouseClicked(final MouseEvent e) {
				if(e.getClickCount() == 1) {
					table = (JTable)e.getSource();
					int row = table.getSelectedRow();
					//int col = table.getSelectedColumn();
					String nazwa_opisu = (String)table.getValueAt(row, 2);
					textAreaNazwaOpisu2.setText(nazwa_opisu);
				}
			}
		});
		table.addMouseListener(new MouseAdapter() {
			public void mouseClicked(final MouseEvent e) {
				if(e.getClickCount() == 1) {
					table = (JTable)e.getSource();
					int row = table.getSelectedRow();
					//int col = table.getSelectedColumn();
					String nazwa_statusu = (String)table.getValueAt(row, 3);
					textNazwaStatusu.setText(nazwa_statusu);
				}
			}
		});
		table.addMouseListener(new MouseAdapter() {
			public void mouseClicked(final MouseEvent e) {
				if(e.getClickCount() == 1) {
					table = (JTable)e.getSource();
					int row = table.getSelectedRow();
					//int col = table.getSelectedColumn();
					String data_zgl = (String)table.getValueAt(row, 4);
					fTextFieldTerminZgl.setText(data_zgl);
				}
			}
		});
		table.addMouseListener(new MouseAdapter() {
			public void mouseClicked(final MouseEvent e) {
				if(e.getClickCount() == 1) {
					table = (JTable)e.getSource();
					int row = table.getSelectedRow();
					//int col = table.getSelectedColumn();
					String data_wymag = (String)table.getValueAt(row, 5);
					fTextFieldTerminWymag.setText(data_wymag);
				}
			}
		});
		table.addMouseListener(new MouseAdapter() {
			public void mouseClicked(final MouseEvent e) {
				if(e.getClickCount() == 1) {
					table = (JTable)e.getSource();
					int row = table.getSelectedRow();
					//int col = table.getSelectedColumn();
					String data_wyk = (String)table.getValueAt(row, 6);
					fTextFieldTerminWyk.setText(data_wyk);
				}
			}
		});	
		
		JScrollPane scrollPane = new JScrollPane(table);
		getContentPane().add(scrollPane);
		table.setFillsViewportHeight(true);	

	}
}