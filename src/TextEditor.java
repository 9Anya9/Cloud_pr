/*
 * 28.05.17
 * �������� ���� ���� ���㳿���, ��������� 1 ����� Բ, �Ͳ�
 * �������� - �������� ����� ���������� ���������
 */

import java.awt.*;
import java.awt.event.*;
import java.io.*;
import javax.swing.*;
import javax.swing.text.*;

class TextEditor extends JFrame {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private JTextArea area = new JTextArea(40,120); //������ �������
	private JFileChooser dialog = new JFileChooser(System.getProperty("user.dir")); //�������� �������� ���� � ������� ������ ��������
	private String currentFile = "Goroscop"; //����� �����
	private boolean changed = false; //����� �����������
	private boolean italic = false;
	private boolean bold = false;
	private boolean green = false;
	private boolean yellow = false;
	private boolean blue = false;
	private boolean red = false;
	private boolean orange = false;
	private boolean pink = false;
	private JTextField jd= new JTextField(); //����, � ����� �������������� ����� ������
	private int size; //����� ������ ������
	
	
	public TextEditor() {
		area.setFont(new Font("Times New Roman",Font.PLAIN, 14)); //������ ����� � ����� ������
		JScrollPane scroll = new JScrollPane(area,JScrollPane.VERTICAL_SCROLLBAR_ALWAYS,JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS);//��������� �����
		pack(); //�����
		add(scroll,BorderLayout.CENTER); //������ �����
		
		JMenuBar JMB = new JMenuBar(); //��������� ����
		setJMenuBar(JMB); //����
		JMenu file = new JMenu("File"); //��������� ������� ���� �� ������ ����
		JMB.add(file); //����� ���� ���� �� ������ �������
		
		file.add(New);
		file.add(Open);
		file.add(Save); //������ ���������� ������ ���� "����"
		file.add(SaveAs);
		file.addSeparator();
		file.add(Exit);
		
		
		//������ ������ ���������� (�� ���� ������ �������������������)
		JToolBar tool = new JToolBar();
		add(tool,BorderLayout.NORTH);
		tool.add(New); //������ ������ new
		tool.add(Open);//������ ������ open
		tool.add(Save);//������ ������ save
		tool.addSeparator(); // ������ ������� (�������� �� ��������)
		tool.addSeparator();
        tool.addSeparator();
		
		//������ ������ ��� ����� �����������
		JButton cut = tool.add(Cut), cop = tool.add(Copy),pas = tool.add(Paste);
		tool.addSeparator();
		tool.addSeparator();
        tool.addSeparator();
        JButton red = tool.add(Red), yellow = tool.add(Yellow), blue = tool.add(Blue), green = tool.add(Green), orange = tool.add(Orange), pink = tool.add(Pink);
        tool.addSeparator();
        tool.addSeparator();
        tool.addSeparator();
        JButton delete = tool.add(Delete), italic = tool.add(Italic), bold = tool.add(Bold); 
        tool.add(jd);
	    JButton sz = tool.add(Sz);
	    tool.addSeparator();
        tool.addSeparator();
        tool.addSeparator();
        tool.addSeparator();
        tool.addSeparator();
        tool.addSeparator();
        tool.addSeparator();
        tool.addSeparator();
      
		
		//������ ������ ��� ����� ������
		cut.setText(null); cut.setIcon(new ImageIcon("cut.png"));
		cop.setText(null); cop.setIcon(new ImageIcon("copy.png"));
		pas.setText(null); pas.setIcon(new ImageIcon("paste.png"));
		delete.setText(null); delete.setIcon(new ImageIcon("return.jpg"));

		
		//������ ���������� ���������� ����������� (��� ���) �����
		Save.setEnabled(false);
		SaveAs.setEnabled(false);
		
		
		setDefaultCloseOperation(EXIT_ON_CLOSE); //����� � �������� ��� ��������� ��������
		pack(); 
		area.addKeyListener(k1); //������ ���������, ���� ����� �� ���� � ��������
		setTitle(currentFile); //�������� ����
		setVisible(true); //������ ��� �������
	}
	
	//���������, ���� ����� �� ���� � ��������(��������, ���������) �� ����� ���� ���������; �������� ����� ���� ��������
	private KeyListener k1 = new KeyAdapter() {
		public void keyPressed(KeyEvent e) {
			changed = true; 
			Save.setEnabled(true);
			SaveAs.setEnabled(true);
		}
	};
	
	//������ ������ ������ new
	Action New = new AbstractAction("New", new ImageIcon("new.png")) { //����������� �� �� ������, ������
		/**
		 * 
		 */
		private static final long serialVersionUID = 1L;

		public void actionPerformed(ActionEvent e) { //���� ������ ���������:
			saveOld(); //������� �����, �� ������� �������� �������� ����
			area.setText(""); //������� ���� ������ ����
			currentFile = "Untitled"; 
			setTitle(currentFile); //���� ����� "��� �����"
			changed = false; //�������� ���� �����: ��� ���
			Save.setEnabled(false); //�� ����� ��������
			SaveAs.setEnabled(false);
		}
	};
	
	//������ ������ ������ open
	Action Open = new AbstractAction("Open", new ImageIcon("open.png")) { //����������� �� �� ������, ������
		/**
		 * 
		 */
		private static final long serialVersionUID = 1L;

		public void actionPerformed(ActionEvent e) { //������ ���������
			saveOld(); //���������� �� �����, ���� ������� �������� �������� ����
			if(dialog.showOpenDialog(null)==JFileChooser.APPROVE_OPTION) { //������� ����� ������� �������� �� ������ ���� � �������� �� ������� ������ ����, 
				readInFile(dialog.getSelectedFile().getAbsolutePath()); //����� (�������) ���� ���� ����� � ����, ����� ����� - �� ���������� ���� �����, ���� ���������
			}
			SaveAs.setEnabled(true); //������ �������� ����������� �� ����� ������
		}
	};
	
	//������ ������ ������ save
	Action Save = new AbstractAction("Save", new ImageIcon("save.png")) { //����������� �� �� ������ � �������� ������
		/**
		 * 
		 */
		private static final long serialVersionUID = 1L;

		public void actionPerformed(ActionEvent e) {  //������ ���������
			if(!currentFile.equals("Untitled")) //���� ����� ����� �� Untitled, �� 
				saveFile(currentFile); //�������� ���� �� ���� � ������
			else
				saveFileAs(); // ���� �������� ���� = Untitled,�� ���������� �� �����, ���� ������� ��� �������� ����, ��� �� ����� ������
		}
	};
	
	
	//��������� ������ ������, �� ������� �� ����� ������
	Action Sz = new AbstractAction("Sz", new ImageIcon("size.jpg")){//����������� �� �� ������ � �������� ������
		/**
		 * 
		 */
		private static final long serialVersionUID = 1L;

		public void actionPerformed(ActionEvent arg0) {//������ ���������
			 String text = jd.getText(); //� ����, ���� ������� �����, ������� ����������
		     size = Integer.parseInt(text); // ������ ���������� � ���
			area.setFont (new Font ("Times New Roman", Font.PLAIN, size));//������ ����� � ����� ������
		}
	};
	
	//��������� ������ ������, �� ������� �� ������ �����
	Action Bold = new AbstractAction("Bold", new ImageIcon("bold.jpg")){
		/**
		 * 
		 */
		private static final long serialVersionUID = 1L;

		public void actionPerformed(ActionEvent arg0) {//������ ���������
			bold();
		}
	};
	
	
	private boolean bold(){
		if (bold==true){//���� ������ ��� ���������, �� ��� ��������� �� ��, �� ������ ��������� �����
			area.setFont(new Font("Times New Roman",Font.PLAIN,size));//������ ����� � ����� ������
			bold=false;
			return bold;
		}
		area.setFont(new Font("Times New Roman",Font.BOLD,size));//���� ������ �� ���������, �� ������ �����
	return bold;	
	}
	
	
	//��������� ������ ������, �� ������� �� ������
	Action Italic = new AbstractAction("Italic", new ImageIcon("italic.png")){
		/**
		 * 
		 */
		private static final long serialVersionUID = 1L;

		public void actionPerformed(ActionEvent arg0) {//������ ���������
			italic();
		}
	};
	
	
	private boolean italic(){
		if (italic==true){//���� ������ ��� ���������, �� ��� ��������� �� ��, �� ������ ��������� �����
			area.setFont(new Font("Times New Roman",Font.PLAIN,size));//������ ����� � ����� ������
			italic=false;
			return italic;
		}
		area.setFont(new Font("Times New Roman",Font.ITALIC,size));//���� ������ �� ���������, �� ������
		italic=true;
	return italic;	
	}
	
	//��������� ������ ������, �� ������� �� ������� ����
	Action Pink = new AbstractAction("Pink", new ImageIcon("pink.png")){
		/**
		 * 
		 */
		private static final long serialVersionUID = 1L;

		public void actionPerformed(ActionEvent arg0) { //������ ���������
			pink();
		}
	};
	
	
	private boolean pink(){
		if (pink==true){//���� ������ ��� ���������, �� ��� ��������� �� ��, �� ������ ������ ����
			area.setForeground(Color.BLACK);
			pink=false;
			return pink;
		}
		area.setForeground(Color.PINK);//���� ������ �� ���������, �� ������� ����
		pink=true;
	return pink;	
	}
	
	//��������� ������ ������, �� ������� �� �������� ����
	Action Red = new AbstractAction("Red", new ImageIcon("red.jpg")){
		/**
		 * 
		 */
		private static final long serialVersionUID = 1L;

		public void actionPerformed(ActionEvent arg0) { //������ ���������
			pink();
			red();
		}
	};
	
	
	private boolean red(){
		if (red==true){//���� ������ ��� ���������, �� ��� ��������� �� ��, �� ������ ������ ����
			area.setForeground(Color.BLACK);
			red=false;
			return red;
		}
		area.setForeground(Color.RED);//���� ������ �� ���������, �� �������� ����
		red=true;
	return red;	
	}
	
	//��������� ������ ������, �� ������� �� ������������ ����
	Action Orange = new AbstractAction("Orange", new ImageIcon("orange.jpg")){
		/**
		 * 
		 */
		private static final long serialVersionUID = 1L;

		public void actionPerformed(ActionEvent arg0) {//������ ��������� 
			orange();
		}
	};
	
	
	private boolean orange(){
		if (orange==true){//���� ������ ��� ���������, �� ��� ��������� �� ��, �� ������ ������ ����
			area.setForeground(Color.BLACK);
			orange=false;
			return orange;
		}
		area.setForeground(Color.ORANGE);//���� ������ �� ���������, �� ������������ ����
		orange=true;
	return orange;	
	}
	
	//��������� ������ ������, �� ������� �� ���� ����
	Action Blue = new AbstractAction("Blue", new ImageIcon("blue.png")){
		/**
		 * 
		 */
		private static final long serialVersionUID = 1L;

		public void actionPerformed(ActionEvent arg0) { //������ ���������
			blue();
		}
	};
	
	
	private boolean blue(){
		if (blue==true){//���� ������ ��� ���������, �� ��� ��������� �� ��, �� ������ ������ ����
			area.setForeground(Color.BLACK);
			blue=false;
			return blue;
		}
		area.setForeground(Color.BLUE);//���� ������ �� ���������, �� ���� ����
		blue=true;
	return blue;	
	}
	
	//��������� ������ ������, �� ������� �� ������� ����
	Action Green= new AbstractAction("Green", new ImageIcon("green.png")){
		/**
		 * 
		 */
		private static final long serialVersionUID = 1L;

		public void actionPerformed(ActionEvent arg0) {//������ ���������
			green();
		}
	};

	private boolean green(){
		if (green==true){
			area.setForeground(Color.BLACK);//���� ������ ��� ���������, �� ��� ��������� �� ��, �� ������ ������ ����
			green=false;
			return green;
		}
		area.setForeground(Color.GREEN);//���� ������ �� ���������, �� ������� ����
		green=true;
	return green;	
	}
	
	
	//��������� ������ ������, �� ������� �� ������ ����
	Action Yellow = new AbstractAction("Yellow", new ImageIcon("yellow.png")){
		/**
		 * 
		 */
		private static final long serialVersionUID = 1L;

		public void actionPerformed(ActionEvent arg0) {//������ ���������
			yellow();
		}
	};
	
	
	private boolean yellow(){
		if (yellow==true){
			area.setForeground(Color.BLACK);//���� ������ ��� ���������, �� ��� ��������� �� ��, �� ������ ������ ����
			yellow=false;
			return yellow;
		}
		area.setForeground(Color.YELLOW);//���� ������ �� ���������, �� ������ ����
		yellow=true;
	return yellow;	
	}
	
	//������ ������ ������ SaveAs (�� ������)
	Action SaveAs = new AbstractAction("Save as...") {
		/**
		 * 
		 */
		private static final long serialVersionUID = 1L;

		public void actionPerformed(ActionEvent e) { //���� ���������
			saveFileAs(); //���������� �� �����, ��� �������� ����
		}
	};
	
	//������ ������ ������ Exit (�� ������)
	Action Exit = new AbstractAction("Exit") {
		/**
		 * 
		 */
		private static final long serialVersionUID = 1L;

		public void actionPerformed(ActionEvent e) { //���� ���������
			saveOld(); //���������� �� �����, ���� ������� �������� ��� ����, �� ����� ��������
			System.exit(0); //��������� ��������
		}
	};
	
	ActionMap m = area.getActionMap(); //�� 䳿 � �������� ����
	Action Cut = m.get(DefaultEditorKit.cutAction);//�������
	Action Copy = m.get(DefaultEditorKit.copyAction);//��������
	Action Paste = m.get(DefaultEditorKit.pasteAction);//��������
	Action Delete = m.get(DefaultEditorKit.deletePrevWordAction);//�������� �������� �����
	
	//����� ���������� �����
	private void saveFileAs() { 
		if(dialog.showSaveDialog(null)==JFileChooser.APPROVE_OPTION) //���� ��'� �������� (APPROVE_OPTION), ��
			saveFile(dialog.getSelectedFile().getAbsolutePath()); //��������� �����, ���� ������� ����
	}
	
	//����� ���������� ����� ����� ���� ���������
	private void saveOld() {
		if(changed) { //���� ���� ������,��
			if(JOptionPane.showConfirmDialog(this, "Would you like to save "+ currentFile +" ?","Save",JOptionPane.YES_NO_OPTION)== JOptionPane.YES_OPTION)
				saveFile(currentFile); //������ ������ � ��������, �� �������� �������� ����, ���� ������� ���, �� ���� ���������� � ���� �������� ������
		}
	}
	
	//����� ���� ����� (������� ����)
	private void readInFile(String fileName) { //�������� ����� �����
		try {
			FileReader r = new FileReader(fileName); //��������� ����, � ���� �������� ����� ������ �����
			area.read(r,null); //������� ��� ���� �� ������ ����
			r.close(); //��������� ����
			currentFile = fileName; 
			setTitle(currentFile);//�������� ���� �������� ������ ������ �����
			changed = false; //���� ��� ���, �� ���� �������
		}
		catch(IOException e) {
			Toolkit.getDefaultToolkit().beep();
			JOptionPane.showMessageDialog(this,"Editor can't find the file called "+fileName); //���� ���� �� ��� ���������, ����������� - �������
		}
	}
	
	//�������� ����
	private void saveFile(String fileName) { //�������� ����� �����
		try {
			FileWriter w = new FileWriter(fileName); //��������� ��������� � ����
			area.write(w); //�������� � �������� ����
			w.close(); //��������� ���������
			currentFile = fileName;
			setTitle(currentFile);
			changed = false; 
			Save.setEnabled(false); //�������������� ����������, �� ����� �� ��������
		}
		catch(IOException e) {
		}
	}
	
	
}