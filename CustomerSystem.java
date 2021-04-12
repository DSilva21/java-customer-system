package ���ð������ý��۱���;
/*
 * ������ �ý��� ���� ���� �̸����� �߰� ���� ���� ��ɱ���
 * 
 * <<�����̷� Modification information>>
 * 
 * ������   		������      ��������
 * 20210408 	�����	��
 * */

import java.awt.BorderLayout;
import java.awt.CardLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FileDialog;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Locale;
import java.util.StringTokenizer;
import java.util.Vector;
import java.util.regex.Pattern;

import javax.swing.ButtonGroup;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JCheckBoxMenuItem;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.RowSorter;
import javax.swing.SortOrder;
import javax.swing.SwingConstants;
import javax.swing.border.LineBorder;
import javax.swing.border.TitledBorder;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableModel;
import javax.swing.table.TableRowSorter;

public class CustomerSystem extends JFrame {

	// ����Ŭ���� ��ü ���� ��������
	MenuMain menuMain = new MenuMain();
	West west = new West();
	ShowTable showTable = new ShowTable();
	
	
	
	Buttons buttons = new Buttons();

	int updateRow; // ���� ��ư���� �̺�Ʈ �߻���.
	// private String id;

	// ������ �ܺ�Ŭ����
	public CustomerSystem() {
		// �߰� �κ�
		OUTTER: while (true) {

			// �߰� �κ� >�̹��� jpg ���� ��Ʈ�� ȭ������ ����
			ImageIcon icon = new ImageIcon("images/����������_�̹���.JPG");
			JOptionPane.showMessageDialog(null, null, "�����������ý���", JOptionPane.NO_OPTION, icon);

			// �н����� ����â
			String password = JOptionPane.showInputDialog("�������ý���" + "\n" + "�н����� �Է�");
			String passwd = "1234";

			if (password == null) {
				break OUTTER;
			}

			else if (password.equals(passwd)) {
				setTitle("�������ý���");
				setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
				add(menuMain.mb, BorderLayout.NORTH);
				add(west, BorderLayout.WEST);
				add(buttons, BorderLayout.SOUTH);
				add(showTable.scroll, BorderLayout.CENTER);

				setSize(1200, 800);
				setLocation(500, 50);
				setVisible(true);
				break OUTTER;
			} else {
				JOptionPane.showMessageDialog(null, "�н����� Ʋ��" + "\n" + "'Ȯ��' ��ư�� ��������", "�н����� ��������",
						JOptionPane.ERROR_MESSAGE);
				continue OUTTER;
			}

		} // endwhile

	}// end customersystem

	// ����Ŭ���� ���� �޴� ����ϴ� Ŭ����
	class MenuMain extends JPanel implements ActionListener, ItemListener {

		JMenuBar mb;
		JMenu file, sort, help;
		JMenuItem fopen, fsave, exit, proinfo;
		JCheckBoxMenuItem sname, snum, slocation, sjob;

		FileDialog readOpen, saveOpen;
		String fileDir, fileName, saveFileName, readFileName;

		/// ������ ���� Ŭ����
		public MenuMain() {
			mb = new JMenuBar();

			file = new JMenu("����");
			sort = new JMenu("����");
			help = new JMenu("����");

			fopen = new JMenuItem("����");
			fsave = new JMenuItem("����");
			exit = new JMenuItem("�ݱ�");

			sname = new JCheckBoxMenuItem("�̸�����");
			snum = new JCheckBoxMenuItem("��ȣ����");
			slocation = new JCheckBoxMenuItem("������� ����");
			sjob = new JCheckBoxMenuItem("���� ����");

			proinfo = new JMenuItem("���α׷� ����");

			file.add(fopen);
			file.add(fsave);
			file.addSeparator();
			file.add(exit);
			sort.add(sname);
			sort.add(snum);
			sort.add(slocation);
			sort.add(sjob);
			help.add(proinfo);
			mb.add(file);
			mb.add(sort);
			mb.add(help);

			// �̺�Ʈ ����
			fopen.addActionListener(this);
			fsave.addActionListener(this);
			exit.addActionListener(this);
			sname.addItemListener(this);
			snum.addItemListener(this);
			slocation.addItemListener(this);
			sjob.addItemListener(this);
		}// end menumain

		@Override
		public void actionPerformed(ActionEvent e) {
			// TODO Auto-generated method stub
			if (e.getActionCommand().equals("����")) {
				save();
			}
			if (e.getActionCommand().equals("����")) {
				open();
			}
			if (e.getActionCommand().equals("�ݱ�")) {
				exit();
			}

		}// end actionperform

		public void save() {
			saveOpen = new FileDialog(CustomerSystem.this, "��������", FileDialog.SAVE);
			saveOpen.setVisible(true);
			// c: c�ؿ� �������ý��۵��������� ���� ����
			fileDir = saveOpen.getDirectory();
			fileName = saveOpen.getFile();
			saveFileName = fileDir + "//" + fileName;

			String str = "";
			String temp = "";

			try {
				BufferedWriter save = new BufferedWriter(new FileWriter(saveFileName));

				for (int i = 0; i < showTable.table.getRowCount(); i++) {
					temp = showTable.data.elementAt(i).toString();
					str += temp.substring(1, temp.length() - 1) + "\n";
				}
				save.write(str);
				save.close();
			} catch (IOException ex) {
				System.out.println(ex);
			}

		}// end save

		public void open() {
			StringTokenizer st;
			Vector<String> vec;

			readOpen = new FileDialog(CustomerSystem.this, "��������", FileDialog.LOAD);
			readOpen.setVisible(true);
			fileDir = readOpen.getDirectory();
			fileName = readOpen.getFile();
			readFileName = fileDir + "//" + fileName;

			try {
				BufferedReader read = new BufferedReader(new FileReader(readFileName));
				String line = null;

				while ((line = read.readLine()) != null) {
					st = new StringTokenizer(line, ", ");
					vec = new Vector<String>();

					while (st.hasMoreTokens()) {
						vec.add(st.nextToken());
					}
					showTable.data.addElement(vec);
				}
				showTable.datamodel.fireTableDataChanged();
				read.close();

			} catch (IOException ex) {
				System.out.println(ex);
			}
		}// endopen

		public void exit() {
			System.exit(0);
		}// end exit

		@Override
		public void itemStateChanged(ItemEvent e) {
			int index=0;
			if (e.getSource().equals(sname)) 
				index=1;
			if (e.getSource().equals(snum)) 
				index=0;
			if (e.getSource().equals(slocation)) 
				index=7;
			if (e.getSource().equals(sjob)) 
				index=9;
			int row=showTable.table.getRowCount();
			int col=showTable.table.getColumnCount();
			String [][] arr=new String[row][col];
				
			 //JTable�� ������ ��ü��  2���� �迭�� ���
	         for(int i = 0; i < row; i++) {
	            for(int j = 0; j < col; j++) {
	               arr[i][j] = (String) showTable.table.getValueAt(i, j);
	            }
	         }
	         int sindex=index;
	         //���Ĺ�� - Arrays sort()�ż��� lambda ���ٽĿ����� final ����������(������ ����� ������ �Ұ���)
	         Arrays.sort(arr, (a, b) -> a[sindex].compareTo(b[sindex])); 
	         
	         //Jtable�� ���ĵ� ����Ÿ ���
	         for(int i = 0; i < row; i++) {
	            for(int j = 0; j < col; j++) {
	               showTable.table.setValueAt(arr[i][j], i, j);
	            }
	         }
	         //üũ�ڽ� ��������
	         snum.setSelected(false);
	         sname.setSelected(false);
	         slocation.setSelected(false);
	         sjob.setSelected(false);
		}
		
	}// end class menumain

	// ����Ŭ���� ������ �Է�, �Ż�������� Ŭ����
	class West extends JPanel {

		// �Է¹޴� Ŭ������ �Ż�������� �ϴ� Ŭ������ �����.

		Input in = new Input();
		Info info = new Info();

		public West() {
			setLayout(new BorderLayout());
			add(in, BorderLayout.CENTER);
			add(info, BorderLayout.SOUTH);
		}// endwest

		// ���� �Է��ϴ� Ŭ����
		class Input extends JPanel {
			JLabel la;
			JTextField tf[];
			JComboBox<String> cbx;
			String job[] = { "�л�", "����", "������" };

			public Input() {
				cbx = new JComboBox<String>(job);
				JLabel lb = new JLabel("����");
				LineBorder line = new LineBorder(Color.blue, 2);
				setBorder(new TitledBorder(line, "�Է�"));
				String[] text = { "��ȣ", "�̸�", "�ڵ�����ȣ", "�̸���", "�ֹι�ȣ" };
				tf = new JTextField[6];
				setLayout(new GridLayout(6, 2, 5, 30));
				for (int i = 0; i < text.length; i++) {
					la = new JLabel(text[i]);
					tf[i] = new JTextField(20);
					la.setHorizontalAlignment(JLabel.CENTER);
					add(la);
					add(tf[i]);
				}
				lb.setHorizontalAlignment(JLabel.CENTER);
				add(lb);
				add(cbx);
				setPreferredSize(new Dimension(250, 300));
			}

		}// end input

		class Info extends JPanel implements ActionListener {
			JLabel la;
			JPanel info_panel = new JPanel(); // �Ż����� ī��
			JPanel search = new JPanel(); // �˻� ī��
			JTextField txt = new JTextField(10); // �˻��Ҷ� �Է��� �ʵ�
			CardLayout card;
			JButton searchbtn, exitbtn;
			Buttons button = new Buttons();
			JRadioButton[] radio = new JRadioButton[4];

			JLabel[] label = new JLabel[4];

			public Info() {
				card = new CardLayout();
				setLayout(card);
				LineBorder line = new LineBorder(Color.blue, 2);
				String[] text = { "����:", "����:", "�������:", "����:" };

				label = new JLabel[4];

				info_panel.setBorder(new TitledBorder(line, "�Ż�����"));
				info_panel.setLayout(new GridLayout(4, 2, 5, 10));

				for (int i = 0; i < text.length; i++) {
					la = new JLabel(text[i]);
					info_panel.add(la);
					info_panel.add(label[i] = new JLabel());
				}
				info_panel.setPreferredSize(new Dimension(250, 300));
				add(info_panel, "1");
				// ������� �Ż����� ī��

				search.setBorder(new TitledBorder(line, "�����˻�"));
				ButtonGroup group = new ButtonGroup();
				String slist[] = { "�̸�", "����", "�������", "�������" };

				searchbtn = new JButton("ã��");
				exitbtn = new JButton("������");

				searchbtn.addActionListener(this);
				exitbtn.addActionListener(this);

				search.setPreferredSize(new Dimension(250, 300));
				for (int i = 0; i < radio.length; i++) {
					radio[i] = new JRadioButton(slist[i]);
					radio[i].addActionListener(this);
					group.add(radio[i]);
					search.add(radio[i]);
				}

				txt.setBounds(25, 100, 50, 30);
				search.add(txt);
				search.add(searchbtn);
				search.add(exitbtn);
				add(search, "2");
			}

			@Override
			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub
				String find = txt.getText(); // �˻��Ҷ� �Է��� �� ����

				if (e.getActionCommand().equals("������")) {
					card.next(this);
					showTable.datamodel.setDataVector(showTable.data, showTable.column_name);
					buttons.searchBtn.setEnabled(true);
					txt.setText(null);
				}

				if (e.getActionCommand().equals("ã��")) {
					int flag = 1;
					for (int q = 0; q < radio.length; q++) {
						if (radio[q].isSelected()) {
							flag = 0;
							break;
						}
					}

					if (flag > 0) {
						JOptionPane.showMessageDialog(null, "�˻��� ������ �����ϼ���", "���޽���", JOptionPane.ERROR_MESSAGE);

					} else if (txt.getText().length() == 0) {
						JOptionPane.showMessageDialog(null, "�˻� ������ �Է��ϼ���", "���޽���", JOptionPane.ERROR_MESSAGE);
					} else {
						Vector<Vector<String>> findData = new Vector<Vector<String>>();
						int num = 0;

						if (radio[0].isSelected())
							num = 1; // �̸�
						if (radio[1].isSelected())
							num = 9; // ����
						if (radio[2].isSelected())
							num = 7; // �����
						if (radio[3].isSelected())
							num = 8; // �������

						for (int i = 0; i < showTable.data.size(); i++) {
							if (showTable.data.elementAt(i).get(num).contains(find)) {
								findData.addElement(showTable.data.elementAt(i));
							}
						}

						if (findData.isEmpty()) {
							JOptionPane.showMessageDialog(null, "�˻���� ����", "���޽���", JOptionPane.ERROR_MESSAGE);
							txt.setText(null);
						} else {
							showTable.datamodel.setDataVector(findData, showTable.column_name);
							showTable.datamodel.fireTableDataChanged();
							showTable.setTable();
						}
					}
				}
			}
		} // end classinfo

	}// end west class

	// jtable�� �Է��� ������ �����ִ� Ŭ����
	class ShowTable extends MouseAdapter {
		DefaultTableModel datamodel;
		JTable table;
		JScrollPane scroll;

		String[] colName = { "��ȣ", "�̸�", "�ڵ�����ȣ", "EMAIL", "�ֹε�Ϲ�ȣ", "����", "����", "�������", "����", "����" };

		// �߿�
		Vector<Vector<String>> data;
		Vector<String> column_name;

		// DefaultTableModel(Vector data,Vector columnNames)
		public ShowTable() {
			data = new Vector<Vector<String>>(10); // �⺻10���� �����迭 ����
			column_name = new Vector<String>(10);

			for (int i = 0; i < colName.length; i++) {
				column_name.add(colName[i]);
			}

			// [�߿�]
			datamodel = new DefaultTableModel(data, column_name);
			table = new JTable(datamodel);
			scroll = new JScrollPane(table);
			setTable();

			// �̺�Ʈó��
			table.addMouseListener(this);

		}

		// ���� �������̵�
		@Override
		public void mouseClicked(MouseEvent e) {
			updateRow = table.getSelectedRow();
			west.in.tf[0].setText((String) showTable.table.getValueAt(updateRow, 0));
			west.in.tf[1].setText((String) showTable.table.getValueAt(updateRow, 1));
			west.in.tf[2].setText((String) showTable.table.getValueAt(updateRow, 2));
			west.in.tf[3].setText((String) showTable.table.getValueAt(updateRow, 3));
			west.in.tf[4].setText((String) showTable.table.getValueAt(updateRow, 4));
			west.in.cbx.setSelectedItem((String) showTable.table.getValueAt(updateRow, 9)); // ����
			west.info.label[0].setText((String) showTable.table.getValueAt(updateRow, 5));// ����
			west.info.label[1].setText((String) showTable.table.getValueAt(updateRow, 6));// ����
			west.info.label[2].setText((String) showTable.table.getValueAt(updateRow, 7));// �����
			west.info.label[3].setText((String) showTable.table.getValueAt(updateRow, 8));// ����
			super.mouseClicked(e);
			// �ֹι�ȣ�� ���� ���ϰ� ��Ȱ��ȭ
			west.in.tf[4].setEditable(false);
		}

		/////// ���̺� ���� �� �÷��� Sort���
		private void setTable() {
			table.getColumnModel().getColumn(0).setPreferredWidth(50);
			table.getColumnModel().getColumn(1).setPreferredWidth(100);
			table.getColumnModel().getColumn(2).setPreferredWidth(120);
			table.getColumnModel().getColumn(3).setPreferredWidth(120);
			table.getColumnModel().getColumn(4).setPreferredWidth(150);
			table.getColumnModel().getColumn(5).setPreferredWidth(50);
			table.getColumnModel().getColumn(6).setPreferredWidth(50);
			table.getColumnModel().getColumn(7).setPreferredWidth(100);
			table.getColumnModel().getColumn(8).setPreferredWidth(70);
			table.getColumnModel().getColumn(9).setPreferredWidth(50);

			// ���� ����Ÿ�� ��� ���Ľ�Ű��
			DefaultTableCellRenderer cell = new DefaultTableCellRenderer();
			cell.setHorizontalAlignment(SwingConstants.CENTER);

			for (int i = 0; i < table.getColumnCount(); i++) {
				table.getColumnModel().getColumn(i).setCellRenderer(cell);
			}

			// �÷�����ư ������ �����ϱ�
			TableRowSorter<TableModel> sorter = new TableRowSorter<TableModel>(table.getModel());
			table.setRowSorter(sorter);
			ArrayList<RowSorter.SortKey> sortKeys = new ArrayList<>(10);
			sortKeys.add(new RowSorter.SortKey(0, SortOrder.ASCENDING)); // SortKey(int column, SortOrder sortOrder)
			sorter.setSortKeys(sortKeys);
		}
	}

// �ϴܿ� ��ư ���ó�����
	class Buttons extends JPanel implements ActionListener {
		// ������� ����
		Vector<String> vector;
		JButton addBtn, updateBtn, delBtn, prevBtn, nextBtn, searchBtn, initBtn;
		String id;

		// ������ ����Ŭ����
		public Buttons() {
			setLayout(new GridLayout(1, 3, 5, 0));
			addBtn = new JButton("�߰�");
			updateBtn = new JButton("����");
			delBtn = new JButton("����");
			prevBtn = new JButton("����");
			nextBtn = new JButton("����");
			searchBtn = new JButton("�˻�");
			initBtn = new JButton("�Է��ʱ�ȭ");

			addBtn.setBackground(Color.GREEN);
			updateBtn.setBackground(Color.yellow);
			delBtn.setBackground(Color.LIGHT_GRAY);

			add(addBtn);
			add(delBtn);
			add(prevBtn);
			add(nextBtn);
			add(updateBtn);
			add(searchBtn);
			add(initBtn);
			// �̺�Ʈ ����

			addBtn.addActionListener(this);
			updateBtn.addActionListener(this);
			delBtn.addActionListener(this);
			searchBtn.addActionListener(this);
			prevBtn.addActionListener(this);
			nextBtn.addActionListener(this);
			initBtn.addActionListener(this);
		}

		@Override
		public void actionPerformed(ActionEvent e) {
			// TODO Auto-generated method stub
			if (e.getActionCommand().equals("�߰�"))
				addData(); // ����� ���� ȣ��
			if (e.getActionCommand().equals("����"))
				updateData();// ����� ���� ȣ��
			if (e.getActionCommand().equals("����"))
				deleteData();// ����� ���� ȣ��

			if (e.getActionCommand().equals("�˻�")) {
				west.info.card.next(west.info); // ����ī��� �Ѿ
				searchBtn.setEnabled(false);
				west.in.tf[0].requestFocus();

				west.in.tf[0].setText(null);
				west.in.tf[1].setText(null);
				west.in.tf[2].setText(null);
				west.in.tf[3].setText(null);
				west.in.tf[4].setText(null);
				west.in.cbx.setSelectedIndex(0);

				west.info.label[0].setText(null);
				west.info.label[1].setText(null);
				west.info.label[2].setText(null);
				west.info.label[3].setText(null);
			}

			if (e.getActionCommand().equals("����"))
				prevData();// ����� ���� ȣ��

			if (e.getActionCommand().equals("����"))
				nextData();// ����� ���� ȣ��

			if (e.getActionCommand().equals("�Է��ʱ�ȭ"))
				initData();// ����� ���� ȣ��

		}// end actionperformed

		public void initData() {
			west.in.tf[0].setText(null);
			west.in.tf[1].setText(null);
			west.in.tf[2].setText(null);
			west.in.tf[3].setText(null);
			west.in.tf[4].setText(null);
			west.in.cbx.setSelectedIndex(0);
			west.info.label[0].setText(null);
			west.info.label[1].setText(null);
			west.info.label[2].setText(null);
			west.info.label[3].setText(null);
			west.in.tf[0].requestFocus();
			west.in.tf[4].setEditable(true);
		}

		public void prevData() {
			if (showTable.table.getRowCount() > 0) {// ���̺� �����Ͱ� �����Ұ��
				if (showTable.table.getSelectedRow() == -1) {
					JOptionPane.showMessageDialog(null, "���õ� ���̾���", "���", JOptionPane.ERROR_MESSAGE);
					return;
				}

				if (showTable.table.getSelectedRow() == 0) { // ���õ� ���� ù���̶��
					JOptionPane.showMessageDialog(null, "ù ���Դϴ�", "���", JOptionPane.ERROR_MESSAGE);
				} else {// setrowselectoninterval�� ���õ� ����� ����� ���ù��� ���Ѵ�Selects the rows from index0 to
						// index1
					showTable.table.setRowSelectionInterval(showTable.table.getSelectedRow() - 1,
							showTable.table.getSelectedRow() - 1);
					updateRow = showTable.table.getSelectedRow();

					west.in.tf[0].setText((String) showTable.table.getValueAt(updateRow, 0));
					west.in.tf[1].setText((String) showTable.table.getValueAt(updateRow, 1));
					west.in.tf[2].setText((String) showTable.table.getValueAt(updateRow, 2));
					west.in.tf[3].setText((String) showTable.table.getValueAt(updateRow, 3));
					west.in.tf[4].setText((String) showTable.table.getValueAt(updateRow, 4));
					west.in.cbx.setSelectedItem((String) showTable.table.getValueAt(updateRow, 9)); // ����
					west.info.label[0].setText((String) showTable.table.getValueAt(updateRow, 5));// ����
					west.info.label[1].setText((String) showTable.table.getValueAt(updateRow, 6));// ����
					west.info.label[2].setText((String) showTable.table.getValueAt(updateRow, 7));// �����
					west.info.label[3].setText((String) showTable.table.getValueAt(updateRow, 8));// ����
					west.in.tf[4].setEditable(false);
				}
			}

		}// end prev

		public void nextData() {
			if (showTable.table.getRowCount() > 0) {// ���̺� �����Ͱ� �����Ұ��
				if (showTable.table.getSelectedRow() == -1) {
					JOptionPane.showMessageDialog(null, "���õ� ���̾���", "���", JOptionPane.ERROR_MESSAGE);
					return;
				}

				else if (showTable.table.getSelectedRow() == (showTable.table.getRowCount() - 1)) { // ���õ� ���� �����̶��
					JOptionPane.showMessageDialog(null, "�� ���Դϴ�", "���", JOptionPane.ERROR_MESSAGE);
				} else {
					showTable.table.setRowSelectionInterval(showTable.table.getSelectedRow() + 1,
							showTable.table.getSelectedRow() + 1);
					updateRow = showTable.table.getSelectedRow();

					west.in.tf[0].setText((String) showTable.table.getValueAt(updateRow, 0));
					west.in.tf[1].setText((String) showTable.table.getValueAt(updateRow, 1));
					west.in.tf[2].setText((String) showTable.table.getValueAt(updateRow, 2));
					west.in.tf[3].setText((String) showTable.table.getValueAt(updateRow, 3));
					west.in.tf[4].setText((String) showTable.table.getValueAt(updateRow, 4));
					west.in.cbx.setSelectedItem((String) showTable.table.getValueAt(updateRow, 9)); // ����
					west.info.label[0].setText((String) showTable.table.getValueAt(updateRow, 5));// ����
					west.info.label[1].setText((String) showTable.table.getValueAt(updateRow, 6));// ����
					west.info.label[2].setText((String) showTable.table.getValueAt(updateRow, 7));// �����
					west.info.label[3].setText((String) showTable.table.getValueAt(updateRow, 8));// ����
					west.in.tf[4].setEditable(false);
				}
			}
		}// end next

		public void addData() {
			Vector<String> vector = new Vector<String>(10);
			id = west.in.tf[4].getText(); // �ֹι�ȣ �Է°� ����
			/*
			 * �Է¹��� �ֹι�ȣ�� ��ȿ�� üũ�� ������ �����Ͽ� üũ�� �� ������ �ֹι�ȣ�϶��� vector ��ü�� ����
			 */
			String regex = "^[0-9]{6}-[1234][0-9]{6}$";
			// "^[0-9]{6}-[1234][0-9]{6}$"
			boolean check = Pattern.matches(regex, id);
			if (check == false) {
				JOptionPane.showMessageDialog(null, "��ȿ��  �ֹι�ȣ�� �ƴմϴ�.", "���޽���", JOptionPane.ERROR_MESSAGE);
				west.in.tf[4].setText(null);
				west.in.tf[4].requestFocus();
				return;// �� ���¸� �����ض�
			}

			// ��ȿ�� üũ�� �Ǹ� üũ ���������
			int sum = 0;
			int[] weight = { 2, 3, 4, 5, 6, 7, 0, 8, 9, 2, 3, 4, 5 }; // ����ġ
			for (int i = 0; i < 13; i++) {
				if (id.charAt(i) == '-')
					continue;
				else {
					sum += (id.charAt(i) - 48) * weight[i];
				}
			}

			int temp = 11 - (sum % 11);
			int result = temp % 10;

			if (result == id.charAt(13) - 48) {
				JOptionPane.showMessageDialog(null, "�ֹι�ȣ üũ ��� ����");

				// �Է¹��� �ֹι�ȣ�� �����϶��� vector ��ü�� �߰�
				vector.add(west.in.tf[0].getText());// ��ȣ
				vector.add(west.in.tf[1].getText());// �̸�
				vector.add(west.in.tf[2].getText());// �ڵ���
				vector.add(west.in.tf[3].getText());// �̸���
				vector.add(west.in.tf[4].getText());// �ֹι�ȣ
				// ������� �ؽ�Ʈ�ʵ忡�� �Է¹������� �����Ѵ�.

				// �Է¹��� �ֹι�ȣ�� ���� ���� ������ ���Ϳ� ����.

				String gender = " ";
				Calendar cal = Calendar.getInstance(Locale.KOREA);
				int year = cal.get(Calendar.YEAR);
				;// ���� �⵵
				int age = 0;
				int w = Integer.parseInt(id.substring(0, 2));
				// id.charAt[7]//�ֹι�ȣ �޹�ȣ ù�ڸ�.

				if ((id.charAt(7) == '1') || (id.charAt(7) == '2')) {
					age = year - (w + 1900);
				}
				if ((id.charAt(7) == '3') || (id.charAt(7) == '4')) {
					age = year - (w + 2000);
				} // ���̱��ϱ�

				if ((id.charAt(7) - '0') % 2 == 0) {
					gender = "����";
				} else {
					gender = "����";
				} // �������ϱ�

				String born = " ";
				String[][] reg = { { "����Ư����", "00", "08" }, { "�λ걤����", "09", "12" }, { "��õ������", "13", "15" },
						{ "��⵵���ֿ䵵��", "16", "25" }, { "������", "26", "34" }, { "��û�ϵ�", "35", "39" },
						{ "����������", "40", "40" }, { "��û����", "41", "43" }, { "��û����", "45", "47" },
						{ "����Ư����ġ��", "44", "44" }, { "����Ư����ġ��", "96", "96" }, { "����ϵ�", "48", "54" },
						{ "���󳲵�", "55", "64" }, { "���ֱ�����", "65", "66" }, { "�뱸������", "67", "70" },
						{ "���ϵ�", "71", "80" }, { "��󳲵�", "81", "84" }, { "��󳲵�", "86", "90" }, { "�λ걤����", "85", "85" },
						{ "����Ư����ġ��", "91", "95" } };

				int subid = Integer.parseInt(id.substring(8, 10));

				for (int i = 0; i < reg.length; i++) {
					int start = Integer.parseInt(reg[i][1]); // �ڵ� ù��°��
					int end = Integer.parseInt(reg[i][2]);// �ڵ� �ι�°��
					if (subid >= start && subid <= end) {
						born = reg[i][0];
					}
				} // ��������ϱ�

				String birth = id.substring(0, 6);

				vector.add(Integer.toString(age));// ����
				vector.add(gender);// ����
				vector.add(born);// �������
				vector.add(birth);// ����
				vector.add((String) west.in.cbx.getSelectedItem()); // ����

				west.in.tf[0].setText(null);
				west.in.tf[1].setText(null);
				west.in.tf[2].setText(null);
				west.in.tf[3].setText(null);
				west.in.tf[4].setText(null);
				west.in.cbx.setSelectedIndex(0);
				west.info.label[0].setText(null);
				west.info.label[1].setText(null);
				west.info.label[2].setText(null);
				west.info.label[3].setText(null);
				west.in.tf[0].requestFocus();
				west.in.tf[4].setEditable(true);
				showTable.data.addElement(vector);

				// �����Ͱ������ �� ������פ��� JTABLE�� �����Ϸ��� firetabledatachanged�޼ҵ带 ȣ���ؾ��Ѵ�.
				showTable.datamodel.fireTableDataChanged();

			} else {
				JOptionPane.showMessageDialog(null, "�ֹι�ȣ Ʋ��", "���޽���", JOptionPane.ERROR_MESSAGE);
				west.in.tf[4].setText(null);
				west.in.tf[4].requestFocus();
			}

		}// end addData

		// updateData()
		public void updateData() {

			for (int i = 0; i < 4; i++) {
				if (west.in.tf[i].getText().length() == 0) {
					JOptionPane.showMessageDialog(null, "�����ɰ�����", "���޽���", JOptionPane.ERROR_MESSAGE);
					return;
				}
			}

			int updateRow = showTable.table.getSelectedRow();
			// �����ϸ� ���ð��� �Ѱ��ְ�, �ƴϸ� -1
			if (showTable.table.getSelectedRow() != -1) {// ������ ���̶��?
				int yes_no_select = JOptionPane.showConfirmDialog(null, "�����Ͻðڽ��ϱ�?", "�������� ����",
						JOptionPane.YES_NO_OPTION);

				if (yes_no_select == JOptionPane.YES_OPTION) {

					updateRow = showTable.table.getSelectedRow();
					// JOptionPane.showMessageDialog
					showTable.table.setValueAt(west.in.tf[0].getText(), updateRow, 0); //
					showTable.table.setValueAt(west.in.tf[1].getText(), updateRow, 1);// ����
					showTable.table.setValueAt(west.in.tf[2].getText(), updateRow, 2);
					showTable.table.setValueAt(west.in.tf[3].getText(), updateRow, 3);
					showTable.table.setValueAt(west.in.tf[4].getText(), updateRow, 4);
					showTable.table.setValueAt(west.info.label[0].getText(), updateRow, 5);
					showTable.table.setValueAt(west.info.label[1].getText(), updateRow, 6);
					showTable.table.setValueAt(west.info.label[2].getText(), updateRow, 7);
					showTable.table.setValueAt(west.info.label[3].getText(), updateRow, 8);
					showTable.table.setValueAt(west.in.cbx.getSelectedItem(), updateRow, 9);// ����
					// �����Ǵ��۾�
					west.in.tf[4].setEditable(true);
					// �ֹι�ȣ �Է¶� Ȱ��ȭ
					west.in.tf[0].requestFocus();

					west.in.tf[0].setText(null);
					west.in.tf[1].setText(null);
					west.in.tf[2].setText(null);
					west.in.tf[3].setText(null);
					west.in.tf[4].setText(null);
					west.in.cbx.setSelectedIndex(0);

					west.info.label[0].setText(null);
					west.info.label[1].setText(null);
					west.info.label[2].setText(null);
					west.info.label[3].setText(null);

					JOptionPane.showMessageDialog(null, "���� �Ϸ�");
				} else
					return;
			} else
				JOptionPane.showMessageDialog(null, "������ ���� �����ϼ���", "���޽���", JOptionPane.ERROR_MESSAGE);
		}// end updatedata

		// deletedata
		public void deleteData() {
			int row = showTable.table.getSelectedRow();
			// �����ϸ� ���ð��� �Ѱ��ְ�, �ƴϸ� -1
			if (showTable.table.getSelectedRow() != -1) {// ������ ���̶��?
				int yes_no_select = JOptionPane.showConfirmDialog(null, "�����Ҳ���?", "�������� ����",
						JOptionPane.YES_NO_OPTION);

				if (yes_no_select == JOptionPane.YES_OPTION) {
					if (row == -1)
						return;
					DefaultTableModel model = (DefaultTableModel) showTable.table.getModel();
					showTable.data.removeElementAt(row);
					showTable.datamodel.fireTableDataChanged();
					west.in.tf[0].setText(null);
					west.in.tf[1].setText(null);
					west.in.tf[2].setText(null);
					west.in.tf[3].setText(null);
					west.in.tf[4].setText(null);
					west.in.cbx.setSelectedIndex(0);
					west.info.label[0].setText(null);
					west.info.label[1].setText(null);
					west.info.label[2].setText(null);
					west.info.label[3].setText(null);

					west.in.tf[4].setEditable(true);
				} else
					return;
			} else
				JOptionPane.showMessageDialog(null, "������ �����Ͻÿ�", "���޽���", JOptionPane.ERROR_MESSAGE);
		}

	}// end del

		public static void main(String[] args) {
			// TODO Auto-generated method stub
			CustomerSystem cs = new CustomerSystem();
		}

}
