package pl.erservice.zgloszenie;

import java.awt.Component;
import java.awt.EventQueue;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.Font;
import java.text.ParseException;

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
		setTitle("HelpDesk SRKK v0.26 bulid 0680 by Jełop");
		setBounds(100, 100, 700, 650);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		getContentPane().setLayout(new GridLayout(2, 1, 500, 0));
		
		JTabbedPane tabbedPane = new JTabbedPane(JTabbedPane.TOP);
		getContentPane().add(tabbedPane);
		
		
		JPanel panelZgloszenie = new JPanel();
		panelZgloszenie.setBackground(new Color(70, 130, 180));
		tabbedPane.addTab("Tabela Zgłoszenie", panelZgloszenie);
		tabbedPane.setEnabledAt(0, true);
		
		textAreaNazwaOpisu2 = new JTextArea();
		textAreaNazwaOpisu2.setToolTipText("Wprowadź opis zgłoszenia.");
		textAreaNazwaOpisu2.setFont(new Font("Monospaced", Font.PLAIN, 12));
		textAreaNazwaOpisu2.setBorder(new TitledBorder(UIManager.getBorder("TitledBorder.border"),
				"Opis zg\u0142oszenia", TitledBorder.CENTER, TitledBorder.TOP, null, null));
		textAreaNazwaOpisu2.setDragEnabled(true);
		textAreaNazwaOpisu2.setLineWrap(true);
		
		Komorka kom = new Komorka();
		final JComboBox<String> comboBoxKomorka = new JComboBox<String>(kom.tKomorka()); //usunąć komentarz po kompilacji !!!!!!!!!!!!!!!!!!!!!!!!!
		comboBoxKomorka.setBorder(new TitledBorder(null, "Wybierz kom\u00F3rk\u0119", TitledBorder.LEADING, TitledBorder.TOP, null, null));
		comboBoxKomorka.setName("");
		comboBoxKomorka.setToolTipText("Wybierz komórkę organizacyjną");
		comboBoxKomorka.setFont(new Font("Tahoma", Font.PLAIN, 11));
		comboBoxKomorka.setSelectedIndex(-1);
		kom.closeConn();
		
		Status stat = new Status();
		final JComboBox<String> comboBoxStatus = new JComboBox<String>(stat.tStatus()); //usunąć komentarz po kompilacji !!!!!!!!!!!!!!!!!!!!!!!!!
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
				JOptionPane.showMessageDialog(null, id_komorki);
				JOptionPane.showMessageDialog(null, nazwa_komorki);
				
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
				JOptionPane.showMessageDialog(null, id_statusu);
				JOptionPane.showMessageDialog(null, nazwa_statusu);
				
				String nazwa_opisu = textAreaNazwaOpisu2.getText();
				String data_zgl = fTextFieldTerminZgl.getText();
				String data_wymag = fTextFieldTerminWymag.getText();
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
				
				//teraz dla TerminWymag
				TerminWymag tw1 = new TerminWymag();
				int max_id_termWymag = 0;
				max_id_termWymag = tw1.tTerminWymag();
				JOptionPane.showMessageDialog(null, "max_id_termWymag = "+max_id_termWymag);
				tw1.closeConn();
				
				Zgloszenie zgl1 = new Zgloszenie();
				zgl1.insertZgloszenie(Integer.parseInt(id_komorki), max_id_termZgl, max_id_termWymag, max_id_opisu, Integer.parseInt(id_statusu), nazwa_komorki, nazwa_opisu, nazwa_statusu, data_zgl, data_wymag);
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
				
			}
		});
		btnEdytujZgloszenie.setToolTipText("Edytuj zgłoszenie i zapisz zmiany w bazie.");
		GroupLayout gl_panelZgloszenie = new GroupLayout(panelZgloszenie);
		gl_panelZgloszenie.setHorizontalGroup(
			gl_panelZgloszenie.createParallelGroup(Alignment.LEADING)
				.addGroup(Alignment.TRAILING, gl_panelZgloszenie.createSequentialGroup()
					.addContainerGap()
					.addGroup(gl_panelZgloszenie.createParallelGroup(Alignment.LEADING, false)
						.addComponent(btnInitialRecords, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
						.addComponent(comboBoxStatus, 0, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
						.addComponent(comboBoxKomorka, 0, 133, Short.MAX_VALUE))
					.addPreferredGap(ComponentPlacement.UNRELATED)
					.addComponent(textAreaNazwaOpisu2, GroupLayout.DEFAULT_SIZE, 346, Short.MAX_VALUE)
					.addPreferredGap(ComponentPlacement.UNRELATED)
					.addGroup(gl_panelZgloszenie.createParallelGroup(Alignment.LEADING)
						.addComponent(btnEdytujZgloszenie, GroupLayout.DEFAULT_SIZE, 152, Short.MAX_VALUE)
						.addGroup(Alignment.TRAILING, gl_panelZgloszenie.createParallelGroup(Alignment.LEADING)
							.addGroup(gl_panelZgloszenie.createParallelGroup(Alignment.TRAILING, false)
								.addComponent(fTextFieldTerminZgl, GroupLayout.PREFERRED_SIZE, 152, GroupLayout.PREFERRED_SIZE)
								.addComponent(btnShowZgloszenia, GroupLayout.PREFERRED_SIZE, 152, GroupLayout.PREFERRED_SIZE)
								.addComponent(btnAddZgloszenie, GroupLayout.PREFERRED_SIZE, 152, GroupLayout.PREFERRED_SIZE))
							.addComponent(fTextFieldTerminWymag, GroupLayout.PREFERRED_SIZE, 152, GroupLayout.PREFERRED_SIZE)))
					.addContainerGap())
		);
		gl_panelZgloszenie.setVerticalGroup(
			gl_panelZgloszenie.createParallelGroup(Alignment.TRAILING)
				.addGroup(gl_panelZgloszenie.createSequentialGroup()
					.addContainerGap()
					.addGroup(gl_panelZgloszenie.createParallelGroup(Alignment.LEADING)
						.addComponent(textAreaNazwaOpisu2, GroupLayout.DEFAULT_SIZE, 159, Short.MAX_VALUE)
						.addGroup(gl_panelZgloszenie.createSequentialGroup()
							.addComponent(comboBoxKomorka, GroupLayout.PREFERRED_SIZE, 43, GroupLayout.PREFERRED_SIZE)
							.addGap(11)
							.addComponent(comboBoxStatus, GroupLayout.PREFERRED_SIZE, 43, GroupLayout.PREFERRED_SIZE)
							.addGap(39)
							.addComponent(btnInitialRecords, GroupLayout.PREFERRED_SIZE, 23, GroupLayout.PREFERRED_SIZE))
						.addGroup(gl_panelZgloszenie.createSequentialGroup()
							.addComponent(fTextFieldTerminZgl, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
							.addPreferredGap(ComponentPlacement.RELATED)
							.addComponent(fTextFieldTerminWymag, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
							.addPreferredGap(ComponentPlacement.RELATED, 17, Short.MAX_VALUE)
							.addComponent(btnShowZgloszenia, GroupLayout.PREFERRED_SIZE, 23, GroupLayout.PREFERRED_SIZE)
							.addPreferredGap(ComponentPlacement.UNRELATED)
							.addComponent(btnAddZgloszenie, GroupLayout.PREFERRED_SIZE, 23, GroupLayout.PREFERRED_SIZE)))
					.addPreferredGap(ComponentPlacement.RELATED)
					.addComponent(btnEdytujZgloszenie)
					.addGap(74))
		);
		panelZgloszenie.setLayout(gl_panelZgloszenie);
		
		
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
		textFieldnNazwaKomorki.setToolTipText("Wprowadź nazwę komórki org.");
		textFieldnNazwaKomorki.setBorder(new TitledBorder(null, "Nazwa kom\u00F3rki", TitledBorder.LEADING, TitledBorder.TOP, null, null));
		textFieldnNazwaKomorki.setBounds(140, 11, 160, 35);
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
		
		
		
		JPanel panelStatus = new JPanel();
		panelStatus.setFont(new Font("Tahoma", Font.PLAIN, 11));		
		tabbedPane.addTab("Tabela Status", panelStatus);
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
		btnShowStatus.setBounds(10, 11, 120, 23);
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
		btnAddStatus.setBounds(10, 45, 120, 23);
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
		btnRemoveStatus.setBounds(10, 115, 120, 23);
		panelStatus.add(btnRemoveStatus);
		
		JButton btnModStatus = new JButton("Edytuj");
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
		btnModStatus.setBounds(10, 80, 120, 23);
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
					//int col = table.getSelectedColumn();
					String id = String.valueOf(table.getValueAt(row, 0));
					//textAreaNazwaOpisu2.setText(test);
					JOptionPane.showMessageDialog(null, id);
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
					//textAreaNazwaOpisu2.setText(test);
					JOptionPane.showMessageDialog(null, nazwa_komorki);
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
					JOptionPane.showMessageDialog(null, nazwa_statusu);
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
					fTextFieldTerminZgl.setText(data_zgl);;
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
					fTextFieldTerminWymag.setText(data_wymag);;
				}
			}
		});

		
		
		
		
		JScrollPane scrollPane = new JScrollPane(table);
		getContentPane().add(scrollPane);
		table.setFillsViewportHeight(true);	

	}
}
