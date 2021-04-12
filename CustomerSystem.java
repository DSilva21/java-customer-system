package 샘플고객관리시스템구현;
/*
 * 고객관리 시스템 열기 저장 이름정렬 추가 수정 삭제 기능구현
 * 
 * <<개정이력 Modification information>>
 * 
 * 수정일   		수정자      수정내용
 * 20210408 	김웅기	뭐
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

	// 내부클래스 객체 생성 전역선언
	MenuMain menuMain = new MenuMain();
	West west = new West();
	ShowTable showTable = new ShowTable();
	
	
	
	Buttons buttons = new Buttons();

	int updateRow; // 수정 버튼에서 이벤트 발생시.
	// private String id;

	// 생성자 외부클래스
	public CustomerSystem() {
		// 추가 부분
		OUTTER: while (true) {

			// 추가 부분 >이미지 jpg 파일 인트로 화면으로 띄우기
			ImageIcon icon = new ImageIcon("images/고객정보관리_이미지.JPG");
			JOptionPane.showMessageDialog(null, null, "고객정보관리시스템", JOptionPane.NO_OPTION, icon);

			// 패스워드 인증창
			String password = JOptionPane.showInputDialog("고객관리시스템" + "\n" + "패스워드 입력");
			String passwd = "1234";

			if (password == null) {
				break OUTTER;
			}

			else if (password.equals(passwd)) {
				setTitle("고객관리시스템");
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
				JOptionPane.showMessageDialog(null, "패스워드 틀림" + "\n" + "'확인' 버튼을 누르세요", "패스워드 인증실패",
						JOptionPane.ERROR_MESSAGE);
				continue OUTTER;
			}

		} // endwhile

	}// end customersystem

	// 내부클래스 구현 메뉴 담당하는 클래스
	class MenuMain extends JPanel implements ActionListener, ItemListener {

		JMenuBar mb;
		JMenu file, sort, help;
		JMenuItem fopen, fsave, exit, proinfo;
		JCheckBoxMenuItem sname, snum, slocation, sjob;

		FileDialog readOpen, saveOpen;
		String fileDir, fileName, saveFileName, readFileName;

		/// 생성자 내부 클래스
		public MenuMain() {
			mb = new JMenuBar();

			file = new JMenu("파일");
			sort = new JMenu("정렬");
			help = new JMenu("도움말");

			fopen = new JMenuItem("열기");
			fsave = new JMenuItem("저장");
			exit = new JMenuItem("닫기");

			sname = new JCheckBoxMenuItem("이름정렬");
			snum = new JCheckBoxMenuItem("번호정렬");
			slocation = new JCheckBoxMenuItem("출생지역 정렬");
			sjob = new JCheckBoxMenuItem("직업 정렬");

			proinfo = new JMenuItem("프로그램 정보");

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

			// 이벤트 연결
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
			if (e.getActionCommand().equals("저장")) {
				save();
			}
			if (e.getActionCommand().equals("열기")) {
				open();
			}
			if (e.getActionCommand().equals("닫기")) {
				exit();
			}

		}// end actionperform

		public void save() {
			saveOpen = new FileDialog(CustomerSystem.this, "문서저장", FileDialog.SAVE);
			saveOpen.setVisible(true);
			// c: c밑에 고객관리시스템데이터파일 폴더 생성
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

			readOpen = new FileDialog(CustomerSystem.this, "문서열기", FileDialog.LOAD);
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
				
			 //JTable의 데이터 전체를  2차원 배열로 담기
	         for(int i = 0; i < row; i++) {
	            for(int j = 0; j < col; j++) {
	               arr[i][j] = (String) showTable.table.getValueAt(i, j);
	            }
	         }
	         int sindex=index;
	         //정렬방식 - Arrays sort()매서드 lambda 람다식에서는 final 변수만가능(변수값 변경된 변수는 불가능)
	         Arrays.sort(arr, (a, b) -> a[sindex].compareTo(b[sindex])); 
	         
	         //Jtable에 정렬된 데이타 출력
	         for(int i = 0; i < row; i++) {
	            for(int j = 0; j < col; j++) {
	               showTable.table.setValueAt(arr[i][j], i, j);
	            }
	         }
	         //체크박스 선택해제
	         snum.setSelected(false);
	         sname.setSelected(false);
	         slocation.setSelected(false);
	         sjob.setSelected(false);
		}
		
	}// end class menumain

	// 내부클래스 데이터 입력, 신상정보담당 클래스
	class West extends JPanel {

		// 입력받는 클래스와 신상정보담당 하는 클래스를 만든다.

		Input in = new Input();
		Info info = new Info();

		public West() {
			setLayout(new BorderLayout());
			add(in, BorderLayout.CENTER);
			add(info, BorderLayout.SOUTH);
		}// endwest

		// 정보 입력하는 클래스
		class Input extends JPanel {
			JLabel la;
			JTextField tf[];
			JComboBox<String> cbx;
			String job[] = { "학생", "선생", "개발자" };

			public Input() {
				cbx = new JComboBox<String>(job);
				JLabel lb = new JLabel("직업");
				LineBorder line = new LineBorder(Color.blue, 2);
				setBorder(new TitledBorder(line, "입력"));
				String[] text = { "번호", "이름", "핸드폰번호", "이메일", "주민번호" };
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
			JPanel info_panel = new JPanel(); // 신상정보 카드
			JPanel search = new JPanel(); // 검색 카드
			JTextField txt = new JTextField(10); // 검색할때 입력할 필드
			CardLayout card;
			JButton searchbtn, exitbtn;
			Buttons button = new Buttons();
			JRadioButton[] radio = new JRadioButton[4];

			JLabel[] label = new JLabel[4];

			public Info() {
				card = new CardLayout();
				setLayout(card);
				LineBorder line = new LineBorder(Color.blue, 2);
				String[] text = { "나이:", "성별:", "출생지역:", "생일:" };

				label = new JLabel[4];

				info_panel.setBorder(new TitledBorder(line, "신상정보"));
				info_panel.setLayout(new GridLayout(4, 2, 5, 10));

				for (int i = 0; i < text.length; i++) {
					la = new JLabel(text[i]);
					info_panel.add(la);
					info_panel.add(label[i] = new JLabel());
				}
				info_panel.setPreferredSize(new Dimension(250, 300));
				add(info_panel, "1");
				// 여기까지 신상정보 카드

				search.setBorder(new TitledBorder(line, "정보검색"));
				ButtonGroup group = new ButtonGroup();
				String slist[] = { "이름", "직업", "출생지역", "생년월일" };

				searchbtn = new JButton("찾기");
				exitbtn = new JButton("나가기");

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
				String find = txt.getText(); // 검색할때 입력한 값 저장

				if (e.getActionCommand().equals("나가기")) {
					card.next(this);
					showTable.datamodel.setDataVector(showTable.data, showTable.column_name);
					buttons.searchBtn.setEnabled(true);
					txt.setText(null);
				}

				if (e.getActionCommand().equals("찾기")) {
					int flag = 1;
					for (int q = 0; q < radio.length; q++) {
						if (radio[q].isSelected()) {
							flag = 0;
							break;
						}
					}

					if (flag > 0) {
						JOptionPane.showMessageDialog(null, "검색할 주제를 선택하세요", "경고메시지", JOptionPane.ERROR_MESSAGE);

					} else if (txt.getText().length() == 0) {
						JOptionPane.showMessageDialog(null, "검색 내용을 입력하세요", "경고메시지", JOptionPane.ERROR_MESSAGE);
					} else {
						Vector<Vector<String>> findData = new Vector<Vector<String>>();
						int num = 0;

						if (radio[0].isSelected())
							num = 1; // 이름
						if (radio[1].isSelected())
							num = 9; // 직업
						if (radio[2].isSelected())
							num = 7; // 출생지
						if (radio[3].isSelected())
							num = 8; // 생년월일

						for (int i = 0; i < showTable.data.size(); i++) {
							if (showTable.data.elementAt(i).get(num).contains(find)) {
								findData.addElement(showTable.data.elementAt(i));
							}
						}

						if (findData.isEmpty()) {
							JOptionPane.showMessageDialog(null, "검색결과 없음", "경고메시지", JOptionPane.ERROR_MESSAGE);
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

	// jtable에 입력한 정보를 보여주는 클래스
	class ShowTable extends MouseAdapter {
		DefaultTableModel datamodel;
		JTable table;
		JScrollPane scroll;

		String[] colName = { "번호", "이름", "핸드폰번호", "EMAIL", "주민등록번호", "나이", "성별", "출생지역", "생일", "직업" };

		// 중요
		Vector<Vector<String>> data;
		Vector<String> column_name;

		// DefaultTableModel(Vector data,Vector columnNames)
		public ShowTable() {
			data = new Vector<Vector<String>>(10); // 기본10개의 가변배열 생성
			column_name = new Vector<String>(10);

			for (int i = 0; i < colName.length; i++) {
				column_name.add(colName[i]);
			}

			// [중요]
			datamodel = new DefaultTableModel(data, column_name);
			table = new JTable(datamodel);
			scroll = new JScrollPane(table);
			setTable();

			// 이벤트처리
			table.addMouseListener(this);

		}

		// 수동 오버라이드
		@Override
		public void mouseClicked(MouseEvent e) {
			updateRow = table.getSelectedRow();
			west.in.tf[0].setText((String) showTable.table.getValueAt(updateRow, 0));
			west.in.tf[1].setText((String) showTable.table.getValueAt(updateRow, 1));
			west.in.tf[2].setText((String) showTable.table.getValueAt(updateRow, 2));
			west.in.tf[3].setText((String) showTable.table.getValueAt(updateRow, 3));
			west.in.tf[4].setText((String) showTable.table.getValueAt(updateRow, 4));
			west.in.cbx.setSelectedItem((String) showTable.table.getValueAt(updateRow, 9)); // 직업
			west.info.label[0].setText((String) showTable.table.getValueAt(updateRow, 5));// 나이
			west.info.label[1].setText((String) showTable.table.getValueAt(updateRow, 6));// 성별
			west.info.label[2].setText((String) showTable.table.getValueAt(updateRow, 7));// 출생지
			west.info.label[3].setText((String) showTable.table.getValueAt(updateRow, 8));// 생일
			super.mouseClicked(e);
			// 주민번호를 수정 못하게 비활성화
			west.in.tf[4].setEditable(false);
		}

		/////// 테이블 정렬 및 컬럼별 Sort기능
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

			// 셀의 데이타를 가운데 정렬시키기
			DefaultTableCellRenderer cell = new DefaultTableCellRenderer();
			cell.setHorizontalAlignment(SwingConstants.CENTER);

			for (int i = 0; i < table.getColumnCount(); i++) {
				table.getColumnModel().getColumn(i).setCellRenderer(cell);
			}

			// 컬럼별버튼 누르면 정렬하기
			TableRowSorter<TableModel> sorter = new TableRowSorter<TableModel>(table.getModel());
			table.setRowSorter(sorter);
			ArrayList<RowSorter.SortKey> sortKeys = new ArrayList<>(10);
			sortKeys.add(new RowSorter.SortKey(0, SortOrder.ASCENDING)); // SortKey(int column, SortOrder sortOrder)
			sorter.setSortKeys(sortKeys);
		}
	}

// 하단에 버튼 기능처리담당
	class Buttons extends JPanel implements ActionListener {
		// 멤버변수 선언
		Vector<String> vector;
		JButton addBtn, updateBtn, delBtn, prevBtn, nextBtn, searchBtn, initBtn;
		String id;

		// 생성자 내부클래스
		public Buttons() {
			setLayout(new GridLayout(1, 3, 5, 0));
			addBtn = new JButton("추가");
			updateBtn = new JButton("수정");
			delBtn = new JButton("삭제");
			prevBtn = new JButton("이전");
			nextBtn = new JButton("다음");
			searchBtn = new JButton("검색");
			initBtn = new JButton("입력초기화");

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
			// 이벤트 연결

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
			if (e.getActionCommand().equals("추가"))
				addData(); // 사용자 정의 호출
			if (e.getActionCommand().equals("수정"))
				updateData();// 사용자 정의 호출
			if (e.getActionCommand().equals("삭제"))
				deleteData();// 사용자 정의 호출

			if (e.getActionCommand().equals("검색")) {
				west.info.card.next(west.info); // 다음카드로 넘어감
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

			if (e.getActionCommand().equals("이전"))
				prevData();// 사용자 정의 호출

			if (e.getActionCommand().equals("다음"))
				nextData();// 사용자 정의 호출

			if (e.getActionCommand().equals("입력초기화"))
				initData();// 사용자 정의 호출

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
			if (showTable.table.getRowCount() > 0) {// 테이블에 데이터가 존재할경우
				if (showTable.table.getSelectedRow() == -1) {
					JOptionPane.showMessageDialog(null, "선택된 행이없다", "경고", JOptionPane.ERROR_MESSAGE);
					return;
				}

				if (showTable.table.getSelectedRow() == 0) { // 선택된 행이 첫행이라면
					JOptionPane.showMessageDialog(null, "첫 행입니다", "경고", JOptionPane.ERROR_MESSAGE);
				} else {// setrowselectoninterval은 선택된 행부터 행까지 선택범윌 뜻한다Selects the rows from index0 to
						// index1
					showTable.table.setRowSelectionInterval(showTable.table.getSelectedRow() - 1,
							showTable.table.getSelectedRow() - 1);
					updateRow = showTable.table.getSelectedRow();

					west.in.tf[0].setText((String) showTable.table.getValueAt(updateRow, 0));
					west.in.tf[1].setText((String) showTable.table.getValueAt(updateRow, 1));
					west.in.tf[2].setText((String) showTable.table.getValueAt(updateRow, 2));
					west.in.tf[3].setText((String) showTable.table.getValueAt(updateRow, 3));
					west.in.tf[4].setText((String) showTable.table.getValueAt(updateRow, 4));
					west.in.cbx.setSelectedItem((String) showTable.table.getValueAt(updateRow, 9)); // 직업
					west.info.label[0].setText((String) showTable.table.getValueAt(updateRow, 5));// 나이
					west.info.label[1].setText((String) showTable.table.getValueAt(updateRow, 6));// 성별
					west.info.label[2].setText((String) showTable.table.getValueAt(updateRow, 7));// 출생지
					west.info.label[3].setText((String) showTable.table.getValueAt(updateRow, 8));// 생일
					west.in.tf[4].setEditable(false);
				}
			}

		}// end prev

		public void nextData() {
			if (showTable.table.getRowCount() > 0) {// 테이블에 데이터가 존재할경우
				if (showTable.table.getSelectedRow() == -1) {
					JOptionPane.showMessageDialog(null, "선택된 행이없다", "경고", JOptionPane.ERROR_MESSAGE);
					return;
				}

				else if (showTable.table.getSelectedRow() == (showTable.table.getRowCount() - 1)) { // 선택된 행이 끝행이라면
					JOptionPane.showMessageDialog(null, "끝 행입니다", "경고", JOptionPane.ERROR_MESSAGE);
				} else {
					showTable.table.setRowSelectionInterval(showTable.table.getSelectedRow() + 1,
							showTable.table.getSelectedRow() + 1);
					updateRow = showTable.table.getSelectedRow();

					west.in.tf[0].setText((String) showTable.table.getValueAt(updateRow, 0));
					west.in.tf[1].setText((String) showTable.table.getValueAt(updateRow, 1));
					west.in.tf[2].setText((String) showTable.table.getValueAt(updateRow, 2));
					west.in.tf[3].setText((String) showTable.table.getValueAt(updateRow, 3));
					west.in.tf[4].setText((String) showTable.table.getValueAt(updateRow, 4));
					west.in.cbx.setSelectedItem((String) showTable.table.getValueAt(updateRow, 9)); // 직업
					west.info.label[0].setText((String) showTable.table.getValueAt(updateRow, 5));// 나이
					west.info.label[1].setText((String) showTable.table.getValueAt(updateRow, 6));// 성별
					west.info.label[2].setText((String) showTable.table.getValueAt(updateRow, 7));// 출생지
					west.info.label[3].setText((String) showTable.table.getValueAt(updateRow, 8));// 생일
					west.in.tf[4].setEditable(false);
				}
			}
		}// end next

		public void addData() {
			Vector<String> vector = new Vector<String>(10);
			id = west.in.tf[4].getText(); // 주민번호 입력값 얻어옴
			/*
			 * 입력받은 주민번호는 유효성 체크와 공식을 적용하여 체크한 후 정상인 주민번호일때만 vector 객체에 저장
			 */
			String regex = "^[0-9]{6}-[1234][0-9]{6}$";
			// "^[0-9]{6}-[1234][0-9]{6}$"
			boolean check = Pattern.matches(regex, id);
			if (check == false) {
				JOptionPane.showMessageDialog(null, "유효한  주민번호가 아닙니다.", "경고메시지", JOptionPane.ERROR_MESSAGE);
				west.in.tf[4].setText(null);
				west.in.tf[4].requestFocus();
				return;// 그 상태를 유지해라
			}

			// 유효성 체크가 되면 체크 공식적용됨
			int sum = 0;
			int[] weight = { 2, 3, 4, 5, 6, 7, 0, 8, 9, 2, 3, 4, 5 }; // 가중치
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
				JOptionPane.showMessageDialog(null, "주민번호 체크 결과 정상");

				// 입력받은 주민번호가 정상일때만 vector 객체에 추가
				vector.add(west.in.tf[0].getText());// 번호
				vector.add(west.in.tf[1].getText());// 이름
				vector.add(west.in.tf[2].getText());// 핸드폰
				vector.add(west.in.tf[3].getText());// 이메일
				vector.add(west.in.tf[4].getText());// 주민번호
				// 여기까진 텍스트필드에서 입력받은값을 저장한다.

				// 입력받은 주민번호를 통해 얻은 정보를 벡터에 저장.

				String gender = " ";
				Calendar cal = Calendar.getInstance(Locale.KOREA);
				int year = cal.get(Calendar.YEAR);
				;// 현재 년도
				int age = 0;
				int w = Integer.parseInt(id.substring(0, 2));
				// id.charAt[7]//주민번호 뒷번호 첫자리.

				if ((id.charAt(7) == '1') || (id.charAt(7) == '2')) {
					age = year - (w + 1900);
				}
				if ((id.charAt(7) == '3') || (id.charAt(7) == '4')) {
					age = year - (w + 2000);
				} // 나이구하기

				if ((id.charAt(7) - '0') % 2 == 0) {
					gender = "여자";
				} else {
					gender = "남자";
				} // 성별구하기

				String born = " ";
				String[][] reg = { { "서울특별시", "00", "08" }, { "부산광역시", "09", "12" }, { "인천광역시", "13", "15" },
						{ "경기도내주요도시", "16", "25" }, { "강원도", "26", "34" }, { "충청북도", "35", "39" },
						{ "대전광역시", "40", "40" }, { "충청남도", "41", "43" }, { "충청남도", "45", "47" },
						{ "세종특별자치시", "44", "44" }, { "세종특별자치시", "96", "96" }, { "전라북도", "48", "54" },
						{ "전라남도", "55", "64" }, { "광주광역시", "65", "66" }, { "대구광역시", "67", "70" },
						{ "경상북도", "71", "80" }, { "경상남도", "81", "84" }, { "경상남도", "86", "90" }, { "부산광역시", "85", "85" },
						{ "제주특별자치도", "91", "95" } };

				int subid = Integer.parseInt(id.substring(8, 10));

				for (int i = 0; i < reg.length; i++) {
					int start = Integer.parseInt(reg[i][1]); // 코드 첫번째값
					int end = Integer.parseInt(reg[i][2]);// 코드 두번째값
					if (subid >= start && subid <= end) {
						born = reg[i][0];
					}
				} // 출생지구하기

				String birth = id.substring(0, 6);

				vector.add(Integer.toString(age));// 나이
				vector.add(gender);// 성별
				vector.add(born);// 출생지역
				vector.add(birth);// 생일
				vector.add((String) west.in.cbx.getSelectedItem()); // 직업

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

				// 데이터가변경된 후 변경사항ㅇㄹ JTABLE에 적용하려면 firetabledatachanged메소드를 호출해야한다.
				showTable.datamodel.fireTableDataChanged();

			} else {
				JOptionPane.showMessageDialog(null, "주민번호 틀림", "경고메시지", JOptionPane.ERROR_MESSAGE);
				west.in.tf[4].setText(null);
				west.in.tf[4].requestFocus();
			}

		}// end addData

		// updateData()
		public void updateData() {

			for (int i = 0; i < 4; i++) {
				if (west.in.tf[i].getText().length() == 0) {
					JOptionPane.showMessageDialog(null, "수정될값없음", "경고메시지", JOptionPane.ERROR_MESSAGE);
					return;
				}
			}

			int updateRow = showTable.table.getSelectedRow();
			// 선택하면 선택값을 넘겨주고, 아니면 -1
			if (showTable.table.getSelectedRow() != -1) {// 선택한 값이라면?
				int yes_no_select = JOptionPane.showConfirmDialog(null, "수정하시겠습니까?", "고객데이터 수정",
						JOptionPane.YES_NO_OPTION);

				if (yes_no_select == JOptionPane.YES_OPTION) {

					updateRow = showTable.table.getSelectedRow();
					// JOptionPane.showMessageDialog
					showTable.table.setValueAt(west.in.tf[0].getText(), updateRow, 0); //
					showTable.table.setValueAt(west.in.tf[1].getText(), updateRow, 1);// 수정
					showTable.table.setValueAt(west.in.tf[2].getText(), updateRow, 2);
					showTable.table.setValueAt(west.in.tf[3].getText(), updateRow, 3);
					showTable.table.setValueAt(west.in.tf[4].getText(), updateRow, 4);
					showTable.table.setValueAt(west.info.label[0].getText(), updateRow, 5);
					showTable.table.setValueAt(west.info.label[1].getText(), updateRow, 6);
					showTable.table.setValueAt(west.info.label[2].getText(), updateRow, 7);
					showTable.table.setValueAt(west.info.label[3].getText(), updateRow, 8);
					showTable.table.setValueAt(west.in.cbx.getSelectedItem(), updateRow, 9);// 직업
					// 수정되는작업
					west.in.tf[4].setEditable(true);
					// 주민번호 입력란 활성화
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

					JOptionPane.showMessageDialog(null, "수정 완료");
				} else
					return;
			} else
				JOptionPane.showMessageDialog(null, "수정할 행을 선택하세요", "경고메시지", JOptionPane.ERROR_MESSAGE);
		}// end updatedata

		// deletedata
		public void deleteData() {
			int row = showTable.table.getSelectedRow();
			// 선택하면 선택값을 넘겨주고, 아니면 -1
			if (showTable.table.getSelectedRow() != -1) {// 선택한 값이라면?
				int yes_no_select = JOptionPane.showConfirmDialog(null, "삭제할꺼야?", "고객데이터 삭제",
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
				JOptionPane.showMessageDialog(null, "삭제행 선택하시오", "경고메시지", JOptionPane.ERROR_MESSAGE);
		}

	}// end del

		public static void main(String[] args) {
			// TODO Auto-generated method stub
			CustomerSystem cs = new CustomerSystem();
		}

}
