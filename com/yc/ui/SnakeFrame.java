package com.yc.ui;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.LinkedList;
import java.util.Random;

import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.Timer;

import com.yc.utils.PlayMusicUtil;
import com.yc.utils.PropertiesUtils;

/**
 * 游戏主界面
 * @author wwwch
 *
 */
public class SnakeFrame extends JFrame{

	private static final long serialVersionUID = 8866826595307493727L;
	
	private static final int WIDTH = 800; // 这是游戏面板的宽度 而不是窗体的宽度
	private static final int HEIGHT = 600; //游戏面板的高度
	private static final int CELL = 20;  //每个单元格的大小 或者说蛇头、水果的大小
	
	private JLabel snakeHeader;  //蛇头
	private JLabel fruit;  //水果
	
	private Random random = new Random();  //这是用来生成随机数的工具类  以便于随机生成一个点
	
	private int dir = 1; // 用来标识蛇头运动的方向 1:向右  -1：向左  2：向上  -2：向下
	
	private LinkedList<JLabel> bodies = new LinkedList<JLabel>();
	
	//存储水果图片名称的数组
	private String[] fruits = {"pineapple.png","apple.png","cherry.png","grape.png","orange.png","peach.png","strawberry.png","tomato.png"};
	
	//存储蛇身体图片的数组
	private String[] snakeBody = {"green.png","red.png","yellow.png","purple.png"};
	
	private JLabel highestLabel; // 显示最高记录
	private JLabel currentLabel; // 显示当前得分
	private int highestScore; //最高得分
	private int currentScore;// 当前得分
	private PropertiesUtils prop = PropertiesUtils.getInstance();
	
	private Timer timer;  // 定时器
	private boolean status = true;// 标识运行还是餐厅  true: 运行 false ：暂停
	
	
	public SnakeFrame(){
		
		//图标
		ImageIcon icon = new ImageIcon("./src/com/yc/images/snake.jpg");
		this.setIconImage(icon.getImage());
		this.setTitle("贪吃蛇——终极炼狱难度");
		this.setSize(WIDTH+4, HEIGHT+34);
		this.setLocationRelativeTo(null);
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.setResizable(false);
		this.setLayout(null);
		//将游戏面板添加到窗体中
		SnakePanel snakePanel = new SnakePanel();
		snakePanel.setBounds(0, 0, WIDTH, HEIGHT);
		this.add(snakePanel);
		
		//给窗体添加键盘监听
		this.addKeyListener(new KeyAdapter() {
			
			@Override
			public void keyPressed(KeyEvent e) {
				//获取到按键对应的码值
				int keyCode = e.getKeyCode();
				
				//根据keyCode改变蛇头移动的方向
				switch (keyCode) {
					case KeyEvent.VK_LEFT: //左键
						if(dir != SnakeDirection.RIGHT){ //只有当前方向不是向右时才可以改变运动方向向左
							dir = SnakeDirection.LEFT;
							//将蛇头的图片改变一下
							setBackgrounImage(snakeHeader, "header_l.png");
						}
						break;
					case KeyEvent.VK_RIGHT: //右键
						if(dir != SnakeDirection.LEFT){ //只有当前方向不是向右时才可以改变运动方向向左
							dir = SnakeDirection.RIGHT;
							setBackgrounImage(snakeHeader, "header_r.png");
						}
						break;
					case KeyEvent.VK_UP: //上键
						if(dir != SnakeDirection.BOTTOM){ //只有当前方向不是向右时才可以改变运动方向向左
							dir = SnakeDirection.TOP;
							setBackgrounImage(snakeHeader, "header_t.png");
						}
						break;
					case KeyEvent.VK_DOWN: //左键
						if(dir != SnakeDirection.TOP){ //只有当前方向不是向右时才可以改变运动方向向左
							dir = SnakeDirection.BOTTOM;
							setBackgrounImage(snakeHeader, "header_b.png");
						}
						break;
					case KeyEvent.VK_SPACE: //暂停或运行
//						if(status){ // 如果是运行状态则暂停
//							status = !status;
//						}else{
//							status = !status;
//							timer.notify();
//						}
						
				}
				
			}
		});
		
		this.setVisible(true);
		
		//先调用一次
		PlayMusicUtil.playBGM();
		
		//设置一个定时器 循环播放背景音乐
		new Timer(98000, new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				
				PlayMusicUtil.playBGM();
			}
		}).start();
		
		
	}
	
	/**
	 * 给一个JLabel设置背景图片
	 * @param label		需要设置背景图的组件
	 * @param fileName	图片的文件名称
	 */
	private void setBackgrounImage(JLabel label,String fileName){
		
		ImageIcon icon = new ImageIcon("./src/com/yc/images/"+fileName);
		//设置图片按照组件大小进行缩放
		icon.setImage( icon.getImage().
				getScaledInstance(label.getWidth(),label.getHeight(),Image.SCALE_DEFAULT));
		label.setIcon(icon);
	}
	
	/**
	 * 游戏的主面板
	 * 我们等会只需要将这个面板添加到窗体中即可
	 * 用内部类的原因是因为内部类可以直接使用外部类的所有成员
	 * 
	 * @author wwwch
	 *
	 */
	class SnakePanel extends JPanel{
		
		private static final long serialVersionUID = 1L;

		public SnakePanel(){
			init();
		}

		private void init() {
			
			this.setSize(SnakeFrame.WIDTH, SnakeFrame.HEIGHT);
			this.setLayout(null);
			
			//添加最高记录的标签
			highestLabel = new JLabel();
			//获取score.properties文件中最高记录的值
			highestScore = Integer.parseInt(prop.getProperty("highest"));
			highestLabel.setText("最高记录："+highestScore);
			highestLabel.setBounds(20, 20, 300, 30);
			this.add(highestLabel);
			
			//添加当前得分
			currentLabel = new JLabel("当前得分："+currentScore);
			currentLabel.setBounds(20, 60, 300, 30);
			this.add(currentLabel);
			
			//随机创建出一个蛇头并且将蛇头添加到面板中
			createHeader();
			
			//创建一个水果
			new Thread(new Runnable() {
				
				@Override
				public void run() {
					createFruit();
				}
			}).start();
			
			
			//使用 定时器让蛇头运动起来  delay: 每隔100毫秒运动一次   
			timer = new Timer(30, new ActionListener() {
				
				@Override
				public void actionPerformed(ActionEvent e) {
					//先获取到蛇头当前的位置
					Point oldPoint = snakeHeader.getLocation();
					//计算下一个蛇头的位置
					Point newPoint = null;
					
					//根据dir决定蛇头运动的方向  以计算出蛇头下一个位置的点
					switch (dir) {
						case SnakeDirection.RIGHT: //向右
							newPoint = new Point(oldPoint.x+CELL, oldPoint.y);
							break;
						case SnakeDirection.LEFT: //向左
							newPoint = new Point(oldPoint.x-CELL, oldPoint.y);
							break;
						case SnakeDirection.BOTTOM: //向下
							newPoint = new Point(oldPoint.x, oldPoint.y+CELL);
							break;
						case SnakeDirection.TOP: //向上
							newPoint = new Point(oldPoint.x, oldPoint.y-CELL);
							break;
					}
					
					//将蛇头的位置移动到新的点
					snakeHeader.setLocation(newPoint);
					//每次蛇头移动之后，其他身体节点也必须跟着移动，都可能出现一些可能： 撞墙  或者 吃到豆子
					isHeatWall();
					
					//如果没有撞墙 判断一下是否可以吃豆子
					if(snakeHeader.getLocation().equals( fruit.getLocation())){
						eatBean();
					}
					
					//不管吃没吃到豆子 蛇的身体部分必须跟着蛇头移动   需要将蛇头原来的位置传过去
					move(oldPoint);
				}

			

				
			});
			timer.start();
			
			
		}
		
		/**
		 * 让蛇的身体跟随蛇头移动的方法 
		 * 
		 * @param oldPoint 蛇头原来的位置
		 */
		private void move(Point oldPoint) {
			
			Point p = new Point();
			//从蛇的第二节开始 每一节都要跟随前一节移动
			for(int i=1;i<bodies.size();i++){
				
				//先记录下前一节身体的位置
				p = bodies.get(i).getLocation();
				//再移动到前一节身体的位置
				bodies.get(i).setLocation(oldPoint);
				//改变oldPoint的值为当前节点的原来位置
				oldPoint = p;
			}
			
		}
		
		/**
		 * 吃水果的方法
		 */
		private void eatBean() {
			
			//将被吃掉的水果随机转换成一种颜色
			int index = random.nextInt( snakeBody.length);
			//将水果的图片设置成蛇身体的图片
			setBackgrounImage(fruit, snakeBody[index]);
			//将水果添加到蛇的身体中
			bodies.add(fruit);
			//播放声音
			new Thread(new Runnable() {
				
				@Override
				public void run() {
					PlayMusicUtil.playEatBean();
				}
			}).start();
			
			//加分
			currentScore++;
			currentLabel.setText("当前得分："+currentScore);
			
			
			//原来的豆子已经被吃掉了 需要创建新的豆子
			new Thread( new Runnable() {
				
				@Override
				public void run() {
					
					createFruit();
				}
			}).start();
			//lambda表达式
			//new Thread( ()->{createFruit();}).start(); 
			
		}
		
		/**
		 * 判断蛇头是否撞墙
		 */
		private void isHeatWall() {
			
			//获取蛇头的位置的x  y  坐标
			int x = snakeHeader.getLocation().x;
			int y = snakeHeader.getLocation().y;
			
			if(x <0 || x >780 || y<0 || y>580){
				
				new Thread( new Runnable() {
					
					@Override
					public void run() {
						PlayMusicUtil.stopBGM();
						PlayMusicUtil.playGameOver();
					}
				}).start();
				
				//判断是否打破记录
				int op = -1;
				if(currentScore > highestScore){
					op = JOptionPane.showConfirmDialog(null, "哦豁！破纪录了哦！再来一把？");
					prop.setProperty("highest", currentScore+"");
					try {
						FileWriter writer = new FileWriter(new File("./src/score.properties"));
						prop.store(writer, null);
					} catch (IOException e) {
						e.printStackTrace();
					}
				}else{
					op = JOptionPane.showConfirmDialog(null, "哦豁！撞墙了哦！再来一把？");
				}
				
				//判断是否再来一把
				if(op == 0){
					
					reStart();
				}else{
					
					System.exit(0);
				
				}
			}
		}
		/**
		 * 创建水果
		 */
		private void createFruit() {
			
			fruit = new JLabel();
			fruit.setSize(CELL, CELL);
			
			//随机生成一个水果的索引
			int index = random.nextInt( fruits.length);
			setBackgrounImage(fruit, fruits[index]);
			
			//【0,39】   [0,29]
			Point p = randomPoint(SnakeFrame.WIDTH/CELL, SnakeFrame.HEIGHT/CELL);
			System.out.println("x:"+p.x+" y:"+p.y);
			fruit.setLocation(p);
			
			this.add(fruit);
			this.repaint();
		}

		/**
		 * 创建蛇头
		 */
		private void createHeader() {
			
			snakeHeader = new JLabel();
			snakeHeader.setSize(CELL, CELL);
//			snakeHeader.setOpaque(false);
			//设置蛇头的图片 因为蛇头默认是超右移动的  所以默认是用header_r.jpg 
			//因为经常要给一些Jlabel设置背景图   所以将这个功能单独封装一个方法
			setBackgrounImage(snakeHeader, "header_r.png");
			//通过随机生成一个点 确定蛇头的位置 蛇头最好不要紧贴墙壁 不然容易一出来就撞墙  【0，800】
			//必须保证生成的点刚好是20的整数倍 【0,39】 --> [10,29] --> [0-19]+10
			Point p = randomPoint((SnakeFrame.WIDTH/CELL)/2, (SnakeFrame.HEIGHT/CELL)/2);
			p.x = p.x+10*CELL;
			p.y = p.y+10*CELL;
			snakeHeader.setLocation(p);
			
			//将蛇头添加蛇的身体中
			bodies.add(snakeHeader);
			//将蛇头添加到游戏面板中
			this.add(snakeHeader);
		}
		
		
		/**
		 * 随机生成一个点
		 * 在java中一个点用Point类来表示  Point类中包含了两个属性 x  y
		 * @return
		 * @param xScale  x坐标的取值范围
		 * @param yScale  y坐标的取值范围
		 */
		private Point randomPoint(int xScale,int yScale){
			
			//创建一个空的点
			Point point = new Point();
			//随机生成x和y的坐标  以创建出一个点的对象
			int x = random.nextInt(xScale)*CELL;
			int y = random.nextInt(yScale)*CELL;
			
			
			
			point.setLocation(x, y);
			return point;
		}
		
		
		/**
		 * 重写这个方法的目的是为了给游戏面板绘制一个背景图
		 */
		@Override
		protected void paintComponent(Graphics g) {
			super.paintComponent(g);
			
			ImageIcon icon = new ImageIcon("./src/com/yc/images/bg.png");
			g.drawImage(icon.getImage(), 0, 0, SnakeFrame.WIDTH, SnakeFrame.HEIGHT, null);
			
			g.setColor(Color.RED);
			
			for(int i=1;i<HEIGHT/CELL;i++){
				g.drawLine(0, i*CELL, 800, i*CELL);
			}
			
			for(int i=1;i<WIDTH/CELL; i++){
				g.drawLine(i*CELL, 0, i*CELL, 600);
			}
			
		}
		
		public void reStart() {
			
			if(currentScore > highestScore){
				highestScore = currentScore;
				highestLabel.setText("最高得分："+highestScore);
			}
			currentScore = 0;
			currentLabel.setText("当前得分："+currentScore);
			dir = 1;
			this.remove(fruit);
			for(JLabel body : bodies){
				this.remove(body);
			}
			
			bodies.clear();
			
			createHeader();
			createFruit();
			
			PlayMusicUtil.playBGM();
			
			super.repaint();
			
			
		}
	}
}
