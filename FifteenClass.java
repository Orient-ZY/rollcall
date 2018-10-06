package cn.zhyoung.FifteenClass;

import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.io.BufferedReader;
import java.io.FileReader;
import java.sql.*;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Date;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPasswordField;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.Timer;

public class FifteenClass extends JFrame {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String driver = "com.mysql.jdbc.Driver";
	private String	url = "jdbc:mysql://localhost:3306/fifteenclass?characterEncoding=utf8&useSSL=false";
	private String	user = "root";
	private String	pwd = "******";
	
	// 定时器flag
	private Boolean timerFlag = false;
	private Timer timer = null;
	
	private int index = 0;
	private List<String> regNames = new ArrayList<String>(); 
	private List<String> displayNames = new ArrayList<String>();
	
	// 点名页面三按钮的状态
	private Boolean startFlag = false;
	private Boolean stopFlag = false;

	// 调试状态
	private Boolean debug = true;
	
	private JButton back = null;

//	显示状态：input、call
	private String statu = "";
	private Boolean flag = true;
	
//	主页
	private JLabel mainLabel = null;
	private JPasswordField input = null;
	private JButton enter = null;
	
//	查询页
	private JTable table = null;
	private JScrollPane jsp = null;
	private JTextField deleteDate = null;
	private JTextField deleteSid = null;
	private JTextField deleteName = null;
	private JButton deleteBtn = null;
	
//	注册页
	private JTextField regTextField = null;
	private JButton regBtn = null;
	
//	点名页
	private JLabel callDisplay = null;
	private JButton callStartBtn = null;
	private JButton callStopBtn = null;
	private JButton callRecordBtn = null;
	

	public FifteenClass() {
		this.setTitle("15物联网班");
		this.setSize(500, 300);
		this.setResizable(false);
		this.setLocationRelativeTo(null);
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.setLayout(null);
		
		init();
		
		this.setVisible(true);
	}
	
	// 调试工具print
	void print(String msg, String x) {
		if (debug) {
			System.out.println(msg + x);
		}
	}
	void print(String msg, int x) {
		if (debug) {
			System.out.println(msg + x);
		}
	}
	void print(String msg, Boolean x) {
		if (debug) {
			System.out.println(msg + x);
		}
	}
	void print(String msg) {
		if (debug) {
			System.out.println(msg);
		}
	}
	
	// 移除所有组件
	void removeAllComponent() {
		this.getContentPane().removeAll();
	}
	
	void init() {
		removeAllComponent();
		if (flag) {
			initOrigin();
//			print("This is called Origin");
//			print("statu is: ", statu);
//			print("-----------------------");
		}
		else{
			if (statu.equals("input")) {
//				print("This is called Input");
//				print("statu is: ", statu);
//				print("-----------------------");
				initInput();
			}else if(statu.equals("call")) {
//				print("This is called Call");
//				print("statu is: ", statu);
//				print("-----------------------");
				initCall();
			}else if (statu.equals("result")) {
				initResult();
			}else {				
				JOptionPane.showMessageDialog(null, "请输入正确的密码！");	
				flag = true;
				removeAllComponent();
				init();
			}
		}
		this.repaint();
	}
	
	// 初始UI
	void initOrigin() {
		mainLabel = new JLabel("输入密码: ");
		mainLabel.setBounds(140, 100, 80, 30);
		
		input = new JPasswordField();
		input.setBounds(200, 100, 100, 30);		
		input.addKeyListener(new KeyListener() {
			@Override
			public void keyTyped(KeyEvent e) {
				// 回车登录
				if (e.getKeyChar() == KeyEvent.VK_ENTER) {
					inputEnter();
				}
			}
			
			@Override
			public void keyReleased(KeyEvent e) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void keyPressed(KeyEvent e) {
				// TODO Auto-generated method stub
				
			}
		});
		
		enter = new JButton("确  认");
		enter.setBounds(200, 150, 100, 40);
		enter.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				// 点击登录
				inputEnter();
				}
			}
		);
		
		this.add(mainLabel);
		this.add(input);
		this.add(enter);
		
	}
	
	// 查询界面
	void initResult() {
//		print("this is searching interface.");
		back = new JButton("首页");
		back.setBounds(20, 20, 50, 30);
		back.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub
				flag = true;
				statu = "";
				init();
			}
		});
		deleteDate = new JTextField("时间", 1);
		deleteSid = new JTextField("学号", 1);
		deleteName = new JTextField("姓名", 1);
		deleteBtn = new JButton("删除");
		deleteDate.setBounds(80, 20, 100, 30);
		deleteSid.setBounds(190, 20, 120, 30);
		deleteName.setBounds(320, 20, 80, 30);
		deleteBtn.setBounds(405, 20, 50, 30);
		deleteBtn.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				// 从记录中删除
				String date = deleteDate.getText();
				String sid = deleteSid.getText();
				String name = deleteName.getText();
				deleteFromRecord(date, sid, name);
//				刷新页面
				statu = "result";
				flag = false;
				init();
			}
		});
		
		
		String[] title = {"时  间", "学  号", "姓  名"};
		ArrayList<ArrayList<String>> temp = search();
		int m, n;
		m = temp.size();
		n = temp.get(0).size();
		String[][] data = new String[m][n];
		for (int i = 0; i < m; i++) {
			for (int j = 0; j < n; j++) {
				data[i][j] = temp.get(i).get(j);
			}
		}
		
		table = new JTable(data, title);
		
		jsp = new JScrollPane();
		jsp.setViewportView(table);
		jsp.setBounds(50, 50, 400, 200);
//		table.setBounds(50, 50, 400, 200);
		
		data = null;

		this.add(jsp);
		this.add(back);
		this.add(deleteDate);
		this.add(deleteSid);
		this.add(deleteName);
		this.add(deleteBtn);
	}
	
	// 
	void inputEnter() {
		// 退出登录界面
		flag = false;
		// 将输入赋值给statu，并对statu进行判断
		statu = new String(input.getPassword());
		init();
	}
	
	// 写入数据库UI
	void initInput() {
//		this.remove(input);
//		this.remove(enter);
		
		back = new JButton("首页");
		back.setBounds(20, 20, 50, 30);
		back.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub
				flag = true;
				statu = "";
				init();
			}
		});
		
		regTextField = new JTextField("输入文件名", 1);
		regTextField.setBounds(200, 100, 100, 30);
		
		regBtn = new JButton("导  入");
		regBtn.setBounds(200, 150, 100, 40);
		regBtn.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				String path = regTextField.getText(); 
				// 读取TXT文件录入数据库
				try {
					getRegNames(path);
//					print("This is regNames: ", regNames.get(0));
					
					// 写入数据库
					Boolean succ = write2mysql(regNames);
					
					if (succ) {
						JOptionPane.showMessageDialog(null, "导入成功！");
					}else {
						JOptionPane.showMessageDialog(null, "请检查数据库！！！");
					}
					flag = false;
					statu = "input";
					init();
				} catch (Exception e2) {
					JOptionPane.showMessageDialog(null, "导入失败！！！");
					flag = false;
					statu = "input";
					init();
				}
				
			}
		});
		
		this.add(back);
		this.add(regTextField);
		this.add(regBtn);
	}
	
	// 点名UI
	void initCall() {
//		this.remove(input);
//		this.remove(enter);
		
		back = new JButton("首页");
		back.setBounds(20, 20, 50, 30);
		back.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub
				flag = true;
				statu = "";
				init();
				startFlag = false;
				stopFlag = false;
				if (timerFlag) {
					timer.stop();
				}
			}
		});
		
		callStartBtn = new JButton("开  始");
		callStartBtn.setBounds(100, 200, 100, 40);
		callStartBtn.addActionListener(new ActionListener() {	
			@Override
			public void actionPerformed(ActionEvent e) {
				getDisplayNames();
				if (!timerFlag) {
					display();
					timer.start();	
				}
				timerFlag = true;
				startFlag = true;
			}
		});
		
		callStopBtn = new JButton("停  止");
		callStopBtn.setBounds(300, 200, 100, 40);
		callStopBtn.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				if (timerFlag) {
					timer.stop();
					timer = null;
				}				
				timerFlag = false;
				stopFlag = true;
//				print("this is index: ", index);
//				print("this is name: ", displayNames.get(index-1));
			}
		});
		
		callRecordBtn = new JButton("记  录");
		callRecordBtn.setBounds(210, 150, 80, 40);
		callRecordBtn.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub
				if (startFlag && stopFlag && !timerFlag) {
					record2mysql();
					startFlag = false;
					stopFlag = false;
				}
			}
		});
		
		callDisplay = new JLabel("15级物联网班", JLabel.CENTER);
		callDisplay.setBounds(100, 80, 300, 30);
		callDisplay.setFont(new Font(Font.DIALOG, Font.BOLD, 24));
		
		this.add(back);
		this.add(callRecordBtn);
		this.add(callStopBtn);
		this.add(callStartBtn);
		this.add(callDisplay);
	}
	
	// 查询record
	ArrayList<ArrayList<String>> search(){
		ArrayList<ArrayList<String>> out = new ArrayList<ArrayList<String>>();
		
		try {
			Class.forName(driver);
			Connection conn = DriverManager.getConnection(url, user, pwd);
			String sql = "SELECT * FROM record";
			Statement state = conn.createStatement();
			ResultSet res = state.executeQuery(sql);
			
			while(res.next()) {
				String time = res.getString("time");
				String sid = res.getString("sid");
				String name = res.getString("name");
				ArrayList<String> row = new ArrayList<String>();
				row.add(time);
				row.add(sid);
				row.add(name);
				out.add(row);
			}
			
			state.close();
			conn.close();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			JOptionPane.showMessageDialog(null, "查询失败！！！");
		}
		
		return out;
	}
	
	void deleteFromRecord(String date, String sid, String name) {
//		print("this is date: ", date);
//		print("this is sid: ", sid);
//		print("this is name: ", name);
		try {
			Class.forName(driver);
			Connection conn = DriverManager.getConnection(url, user, pwd);
			String sql = "DELETE FROM record WHERE time=? and sid=? and name=?";
			PreparedStatement state = conn.prepareStatement(sql);
			state.setString(1, date);
			state.setString(2, sid);
			state.setString(3, name);
			int count = state.executeUpdate();
			
			if (count >= 1) {
				JOptionPane.showMessageDialog(null, "删除成功！");
			}else {
				JOptionPane.showMessageDialog(null, "删除失败！！！");
			}
			
			state.close();
			conn.close();
		} catch (Exception e) {
			JOptionPane.showMessageDialog(null, "删除失败！！！");
		}
	}
	
	void record2mysql() {
		String rec = callDisplay.getText();
		SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
		Date date = new Date();
		String recordDate = df.format(date);
		String recordSid = rec.split(",")[0];
		String recordName = rec.split(",")[1];
		recordName = recordName.split(" ")[1];
//		print("this is record name:", recordName);
		
		try {
			Class.forName(driver);
			Connection conn = DriverManager.getConnection(url, user, pwd);
			String sql = "INSERT INTO record VALUES(?, ?, ?)";
			PreparedStatement state = conn.prepareStatement(sql);
			state.setString(1, recordDate);
			state.setString(2, recordSid);
			state.setString(3, recordName);
			int count = state.executeUpdate();
			
			if (count == 1) {
				JOptionPane.showMessageDialog(null, "记录成功！");
			}
			
			state.close();
			conn.close();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			JOptionPane.showMessageDialog(null, "记录失败！！！");
		}
		
//		print("this is date: ", recordDate);
//		print("this is recordSid: ", recordSid);
//		print("this is recordName: ", recordName);
	}
	
	void display() {
		timer = new Timer(10, new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				// 随机显示
				callDisplay.setText(displayNames.get(index));
				index++;
				if (index == displayNames.size()) {
					index = 0;
				}
			}
		});
	}
	
	void getRegNames(String path) {
		try {
			BufferedReader br = new BufferedReader(new FileReader(path));
			String name = br.readLine();
			while(name != null) {
				regNames.add(name);
				name = br.readLine();
			}
		} catch (Exception e) {
			JOptionPane.showMessageDialog(null, "没有此文件：" + path + "！！！请输入正确的文件名");
		}
		
	}
	
	void getDisplayNames() {
		try {
			if (displayNames.size() < 1) {
				index = 0;
				Class.forName(driver);
				Connection conn = DriverManager.getConnection(url, user, pwd);
				String sql = "Select * from reg";
				Statement state = conn.createStatement();
				ResultSet res = state.executeQuery(sql);
				
				while(res.next()) {
					String sid = res.getString("sid");
					String name = res.getString("name");
					String out = sid + ", " + name;
					displayNames.add(out);
				}
				
				Collections.shuffle(displayNames);
//				print("this is displayNames: ", displayNames.get(64));
				res.close();
				state.close();
				conn.close();
			}else {
				index = 0;
				Collections.shuffle(displayNames);
//				print("DisplayNmaes is not null");
//				print("this is displayNames: ", displayNames.get(64));
			}
		} catch (Exception e) {
			// TODO: handle exception
		}
	}
	
	Boolean write2mysql(List<String> inputs) {
		try {
			
			// 注册驱动
			Class.forName(driver);
			
			// 获取连接
			Connection conn = DriverManager.getConnection(url, user, pwd);
//			System.out.println(conn);
			
			String sql = "INSERT INTO reg(sid, name) VALUES(?, ?)";
			
			PreparedStatement state = conn.prepareStatement(sql);

			
			for (int i = 0; i < inputs.size(); i++) {
				state.setString(1, inputs.get(i).split(",")[0]);
				state.setString(2, inputs.get(i).split(",")[1]);
				
				int count = state.executeUpdate();
				print("Update rows: ", count);
			}
			
			state.close();
			conn.close();
			
			return true;
		} catch (Exception e) {
			JOptionPane.showMessageDialog(null, "数据库写入失败！！！");
			return false;
		}
	}
	
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		new FifteenClass();
	}

}
