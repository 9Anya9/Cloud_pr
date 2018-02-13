/*
 * 28.05.17
 * Виконала Салій Анна Сергіївна, студентка 1 курсу ФІ, КНІТ
 * Програма - спрощена версія текстового редактору
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
	private JTextArea area = new JTextArea(40,120); //робоча область
	private JFileChooser dialog = new JFileChooser(System.getProperty("user.dir")); //дозволяє вибирати файл з поточної робочої директорії
	private String currentFile = "Goroscop"; //назва файлу
	private boolean changed = false; //стани перемикачів
	private boolean italic = false;
	private boolean bold = false;
	private boolean green = false;
	private boolean yellow = false;
	private boolean blue = false;
	private boolean red = false;
	private boolean orange = false;
	private boolean pink = false;
	private JTextField jd= new JTextField(); //поле, в якому записуватимемо розмір шрифта
	private int size; //змінна розміру шрифта
	
	
	public TextEditor() {
		area.setFont(new Font("Times New Roman",Font.PLAIN, 14)); //задаємо шрифт і розмір шрифту
		JScrollPane scroll = new JScrollPane(area,JScrollPane.VERTICAL_SCROLLBAR_ALWAYS,JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS);//оголошуємо скрол
		pack(); //скрол
		add(scroll,BorderLayout.CENTER); //додаємо скрол
		
		JMenuBar JMB = new JMenuBar(); //створюємо меню
		setJMenuBar(JMB); //меню
		JMenu file = new JMenu("File"); //створюємо елемент меню під назвою файл
		JMB.add(file); //додаєм меню файл на робочу область
		
		file.add(New);
		file.add(Open);
		file.add(Save); //додаємо функціонал розділу меню "файл"
		file.add(SaveAs);
		file.addSeparator();
		file.add(Exit);
		
		
		//додаємо панель інструметів (де наші значки розташовуватимуться)
		JToolBar tool = new JToolBar();
		add(tool,BorderLayout.NORTH);
		tool.add(New); //додаємо значок new
		tool.add(Open);//додаємо значок open
		tool.add(Save);//додаємо значок save
		tool.addSeparator(); // додаємо відстань (пропуски між значками)
		tool.addSeparator();
        tool.addSeparator();
		
		//додаємо кнопки для наших інструментів
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
      
		
		//додаємо іконки для наших кнопок
		cut.setText(null); cut.setIcon(new ImageIcon("cut.png"));
		cop.setText(null); cop.setIcon(new ImageIcon("copy.png"));
		pas.setText(null); pas.setIcon(new ImageIcon("paste.png"));
		delete.setText(null); delete.setIcon(new ImageIcon("return.jpg"));

		
		//робимо неможливим збереження початкового (без змін) файлу
		Save.setEnabled(false);
		SaveAs.setEnabled(false);
		
		
		setDefaultCloseOperation(EXIT_ON_CLOSE); //вихід з програми при натисканні хрестика
		pack(); 
		area.addKeyListener(k1); //додаємо кейлісенер, який реагує на зміни в документі
		setTitle(currentFile); //називаємо файл
		setVisible(true); //робимо все видимим
	}
	
	//кейлісенер, який реагує на зміну в документі(введення, видалення) та змінює стан документа; дозволяє тепер його зберігати
	private KeyListener k1 = new KeyAdapter() {
		public void keyPressed(KeyEvent e) {
			changed = true; 
			Save.setEnabled(true);
			SaveAs.setEnabled(true);
		}
	};
	
	//реалізує роботу кнопки new
	Action New = new AbstractAction("New", new ImageIcon("new.png")) { //створюється дія на кнопку, значок
		/**
		 * 
		 */
		private static final long serialVersionUID = 1L;

		public void actionPerformed(ActionEvent e) { //коли кнопка натиснута:
			saveOld(); //викликає метод, що пропонує зберегти поточний файл
			area.setText(""); //створює нове порожнє поле
			currentFile = "Untitled"; 
			setTitle(currentFile); //задає назву "без назви"
			changed = false; //поточний стан файлу: без змін
			Save.setEnabled(false); //не можна зберігати
			SaveAs.setEnabled(false);
		}
	};
	
	//реалізує роботу кнопки open
	Action Open = new AbstractAction("Open", new ImageIcon("open.png")) { //створюється дія на кнопку, значок
		/**
		 * 
		 */
		private static final long serialVersionUID = 1L;

		public void actionPerformed(ActionEvent e) { //кнопку натиснули
			saveOld(); //посилається на метод, який пропонує зберегти поточний файл
			if(dialog.showOpenDialog(null)==JFileChooser.APPROVE_OPTION) { //відкриває діалог поточної директорії по центру вікна і перевіряє чи обраний якийсь файл, 
				readInFile(dialog.getSelectedFile().getAbsolutePath()); //зчитує (відкриває) зміст того файла у файл, назва якого - це абсолютний шлях файлу, який відкриваємо
			}
			SaveAs.setEnabled(true); //робимо можливим збереженням під новим іменем
		}
	};
	
	//реалізує роботу кнопки save
	Action Save = new AbstractAction("Save", new ImageIcon("save.png")) { //створюється дія на кнопку і надається значок
		/**
		 * 
		 */
		private static final long serialVersionUID = 1L;

		public void actionPerformed(ActionEvent e) {  //кнопку натиснули
			if(!currentFile.equals("Untitled")) //якшо назва файлу не Untitled, то 
				saveFile(currentFile); //зберігаємо його під його ж іменем
			else
				saveFileAs(); // якщо поточний файл = Untitled,то посилаємось на метод, який пропонує нам зберегти його, але під іншим іменем
		}
	};
	
	
	//реалізація роботи кнопки, що відповідає за розмір шрифта
	Action Sz = new AbstractAction("Sz", new ImageIcon("size.jpg")){//створюється дія на кнопку і надається значок
		/**
		 * 
		 */
		private static final long serialVersionUID = 1L;

		public void actionPerformed(ActionEvent arg0) {//кнопку натиснули
			 String text = jd.getText(); //з поля, куди вводимо шрифт, зчитуємо інформацію
		     size = Integer.parseInt(text); // стрічку переводимо в інт
			area.setFont (new Font ("Times New Roman", Font.PLAIN, size));//задаємо шрифт і розмір шрифту
		}
	};
	
	//реалізація роботи кнопки, що відповідає за жирний шрифт
	Action Bold = new AbstractAction("Bold", new ImageIcon("bold.jpg")){
		/**
		 * 
		 */
		private static final long serialVersionUID = 1L;

		public void actionPerformed(ActionEvent arg0) {//кнопку натиснули
			bold();
		}
	};
	
	
	private boolean bold(){
		if (bold==true){//якщо кнопка вже натиснута, то при натисканні на неї, ми робимо звичайний шрифт
			area.setFont(new Font("Times New Roman",Font.PLAIN,size));//задаємо шрифт і розмір шрифту
			bold=false;
			return bold;
		}
		area.setFont(new Font("Times New Roman",Font.BOLD,size));//якщо кнопка не натиснута, то жирний шрифт
	return bold;	
	}
	
	
	//реалізація роботи кнопки, що відповідає за курсив
	Action Italic = new AbstractAction("Italic", new ImageIcon("italic.png")){
		/**
		 * 
		 */
		private static final long serialVersionUID = 1L;

		public void actionPerformed(ActionEvent arg0) {//кнопку натиснули
			italic();
		}
	};
	
	
	private boolean italic(){
		if (italic==true){//якщо кнопка вже натиснута, то при натисканні на неї, ми робимо звичайний шрифт
			area.setFont(new Font("Times New Roman",Font.PLAIN,size));//задаємо шрифт і розмір шрифту
			italic=false;
			return italic;
		}
		area.setFont(new Font("Times New Roman",Font.ITALIC,size));//якщо кнопка не натиснута, то курсив
		italic=true;
	return italic;	
	}
	
	//реалізація роботи кнопки, що відповідає за рожевий колір
	Action Pink = new AbstractAction("Pink", new ImageIcon("pink.png")){
		/**
		 * 
		 */
		private static final long serialVersionUID = 1L;

		public void actionPerformed(ActionEvent arg0) { //кнопку натиснули
			pink();
		}
	};
	
	
	private boolean pink(){
		if (pink==true){//якщо кнопка вже натиснута, то при натисканні на неї, ми робимо чорний колір
			area.setForeground(Color.BLACK);
			pink=false;
			return pink;
		}
		area.setForeground(Color.PINK);//якщо кнопка не натиснута, то рожевий колір
		pink=true;
	return pink;	
	}
	
	//реалізація роботи кнопки, що відповідає за червоний колір
	Action Red = new AbstractAction("Red", new ImageIcon("red.jpg")){
		/**
		 * 
		 */
		private static final long serialVersionUID = 1L;

		public void actionPerformed(ActionEvent arg0) { //кнопку натиснули
			pink();
			red();
		}
	};
	
	
	private boolean red(){
		if (red==true){//якщо кнопка вже натиснута, то при натисканні на неї, ми робимо чорний колір
			area.setForeground(Color.BLACK);
			red=false;
			return red;
		}
		area.setForeground(Color.RED);//якщо кнопка не натиснута, то червоний колір
		red=true;
	return red;	
	}
	
	//реалізація роботи кнопки, що відповідає за помаранчевий колір
	Action Orange = new AbstractAction("Orange", new ImageIcon("orange.jpg")){
		/**
		 * 
		 */
		private static final long serialVersionUID = 1L;

		public void actionPerformed(ActionEvent arg0) {//кнопку натиснули 
			orange();
		}
	};
	
	
	private boolean orange(){
		if (orange==true){//якщо кнопка вже натиснута, то при натисканні на неї, ми робимо чорний колір
			area.setForeground(Color.BLACK);
			orange=false;
			return orange;
		}
		area.setForeground(Color.ORANGE);//якщо кнопка не натиснута, то помаранчевий колір
		orange=true;
	return orange;	
	}
	
	//реалізація роботи кнопки, що відповідає за синій колір
	Action Blue = new AbstractAction("Blue", new ImageIcon("blue.png")){
		/**
		 * 
		 */
		private static final long serialVersionUID = 1L;

		public void actionPerformed(ActionEvent arg0) { //кнопку натиснули
			blue();
		}
	};
	
	
	private boolean blue(){
		if (blue==true){//якщо кнопка вже натиснута, то при натисканні на неї, ми робимо чорний колір
			area.setForeground(Color.BLACK);
			blue=false;
			return blue;
		}
		area.setForeground(Color.BLUE);//якщо кнопка не натиснута, то синій колір
		blue=true;
	return blue;	
	}
	
	//реалізація роботи кнопки, що відповідає за зелений колір
	Action Green= new AbstractAction("Green", new ImageIcon("green.png")){
		/**
		 * 
		 */
		private static final long serialVersionUID = 1L;

		public void actionPerformed(ActionEvent arg0) {//кнопку натиснули
			green();
		}
	};

	private boolean green(){
		if (green==true){
			area.setForeground(Color.BLACK);//якщо кнопка вже натиснута, то при натисканні на неї, ми робимо чорний колір
			green=false;
			return green;
		}
		area.setForeground(Color.GREEN);//якщо кнопка не натиснута, то зелений колір
		green=true;
	return green;	
	}
	
	
	//реалізація роботи кнопки, що відповідає за жовтий колір
	Action Yellow = new AbstractAction("Yellow", new ImageIcon("yellow.png")){
		/**
		 * 
		 */
		private static final long serialVersionUID = 1L;

		public void actionPerformed(ActionEvent arg0) {//кнопку натиснули
			yellow();
		}
	};
	
	
	private boolean yellow(){
		if (yellow==true){
			area.setForeground(Color.BLACK);//якщо кнопка вже натиснута, то при натисканні на неї, ми робимо чорний колір
			yellow=false;
			return yellow;
		}
		area.setForeground(Color.YELLOW);//якщо кнопка не натиснута, то жовтий колір
		yellow=true;
	return yellow;	
	}
	
	//реалізує роботу опціїї SaveAs (НЕ КНОПКИ)
	Action SaveAs = new AbstractAction("Save as...") {
		/**
		 * 
		 */
		private static final long serialVersionUID = 1L;

		public void actionPerformed(ActionEvent e) { //коли натиснули
			saveFileAs(); //посилаємось на метод, щоб зберегти файл
		}
	};
	
	//реалізує роботу опціїї Exit (НЕ КНОПКИ)
	Action Exit = new AbstractAction("Exit") {
		/**
		 * 
		 */
		private static final long serialVersionUID = 1L;

		public void actionPerformed(ActionEvent e) { //коли натиснули
			saveOld(); //посилається на метод, який пропонує зберегти той файл, що зараз відкритий
			System.exit(0); //закриваємо програму
		}
	};
	
	ActionMap m = area.getActionMap(); //усі дії з робочого поля
	Action Cut = m.get(DefaultEditorKit.cutAction);//вирізати
	Action Copy = m.get(DefaultEditorKit.copyAction);//копіювати
	Action Paste = m.get(DefaultEditorKit.pasteAction);//вставити
	Action Delete = m.get(DefaultEditorKit.deletePrevWordAction);//видалити попереднє слово
	
	//метод збереження файлу
	private void saveFileAs() { 
		if(dialog.showSaveDialog(null)==JFileChooser.APPROVE_OPTION) //якщо ім'я підходить (APPROVE_OPTION), то
			saveFile(dialog.getSelectedFile().getAbsolutePath()); //викликаємо метод, який збереже файл
	}
	
	//метод збереження файлу перед його закриттям
	private void saveOld() {
		if(changed) { //якщо файл змінено,то
			if(JOptionPane.showConfirmDialog(this, "Would you like to save "+ currentFile +" ?","Save",JOptionPane.YES_NO_OPTION)== JOptionPane.YES_OPTION)
				saveFile(currentFile); //вибиває панель з питанням, чи зберігати поточний файл, якщо відповідь так, то файл зберігається з його поточним іменем
		}
	}
	
	//зчитує зміст файлу (відкриває його)
	private void readInFile(String fileName) { //передаємо назву файлу
		try {
			FileReader r = new FileReader(fileName); //створюємо рідер, у який передаємо назву нашого файлу
			area.read(r,null); //зчитуємо наш файл на робоче поле
			r.close(); //закриваємр рідер
			currentFile = fileName; 
			setTitle(currentFile);//поточний файл називаємо назвою нашого файлу
			changed = false; //файл без змін, бо лише відкрили
		}
		catch(IOException e) {
			Toolkit.getDefaultToolkit().beep();
			JOptionPane.showMessageDialog(this,"Editor can't find the file called "+fileName); //якщо файл не зміг відкритися, прочитатися - ексепшн
		}
	}
	
	//зберігаємо файл
	private void saveFile(String fileName) { //передаємо назву файлу
		try {
			FileWriter w = new FileWriter(fileName); //створюємо записувач у файл
			area.write(w); //записуємо з робочого поля
			w.close(); //закриваємр записувач
			currentFile = fileName;
			setTitle(currentFile);
			changed = false; 
			Save.setEnabled(false); //унеможливлюємо збереження, бо тільки що зберегли
		}
		catch(IOException e) {
		}
	}
	
	
}