package gamecode;

import java.awt.Canvas;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Rectangle;
import java.awt.Toolkit;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.LinkedList;
import java.util.Random;

import javax.swing.JOptionPane;

import sun.audio.*;

public class MyCanvas extends Canvas implements KeyListener{
	
	Goodguy sonic = new Goodguy(10, 600, 90, 90, "files/sonic.png");
	
	Block star = new Block(560, 360, 80, 80, "files/tenor.gif");
	
	Block menu = new Block(300, 90, 600, 250, "files/start.png");
	Block diff = new Block(950, 175, 200, 100, "files/easy.png");
	Block shop = new Block(450, 300, 300, 150, "files/shop.png");
	Block exit = new Block(450, 420, 300, 150, "files/exitgame.png");
	Block arrow = new Block(180, 70, 130, 150, "files/arrow.png");
	Block back = new Block(0, 0, 1200, 690, "files/back.jpg");
	
	Badguy boss = new Badguy(-200, 520, 160, 170, "files/bowser.png");
	Badguy jr = new Badguy(-200, 200, 120, 100, "files/jr.png");
	
	LinkedList badguys = new LinkedList();  //badguy list
	LinkedList bricks = new LinkedList();   //brick list
	LinkedList clouds = new LinkedList();   //cloud list
	LinkedList fballs = new LinkedList();   //boss fireballs
	LinkedList stones = new LinkedList();   //power up stones
	LinkedList blasts = new LinkedList();   //goodguy projectiles
	LinkedList bombs = new LinkedList();    //bowser jr projectiles
	
	String[] geoQ = {"Ontario", "Quebec", "Manitoba", "Saskatchewan", "Alberta", "British Columbia", "Newfoundland", "Nova Scotia", "New Brunswick", "P.E.I.", "Yukon", "Northwest Territories", "Nunavut", 
			         "Canada", "U.S.A.", "Mexico", "England", "France", "Spain", "Germany", "Russia", "Greece", "Italy"};
	String[] geoA = {"Toronto", "Quebec City", "Winnipeg", "Regina", "Edmonton", "Victoria", "St.John's", "Halifax", "Fredricton", "Charlottetown", "Whitehorse", "Yellowknife", "Iqaluit", 
	                 "Ottawa", "Washington D.C.", "Mexico City", "London", "Paris", "Madrid", "Berlin", "Moscow", "Athens", "Rome"};
	
	int time = 0;               //internal clock
	
	int jumping = 0;            //jumping position, state
	int falling = 0;            //falling position, state
	int rolling = 0;            //rolling position, state
	
	boolean gotStar = false;    //checks if star has been retrieved
	boolean start = false;      //checks if game has been started
	boolean bossDone = true;   //checks if boss has been beaten
	boolean combo = false;  	//checks if combo is going on
	boolean bossMusic = false;
	int difficulty = 1;         //difficulty level
	int wasHit = 0;             //times last time user hit a badguy
	int bossTime = 400;         //time since boss died
	
	int score = 0;                //user score
	int total = 500;              //total score
	int highScore = 0;            //highest score
	boolean newHighscore = false; //shows if new score is the new highscore
	int level = 1;                //current level
	
	int lastFireball = -300;    //delay between goodguy projectiles
	int lastRoll = -300;        //delay between rolling
	int immunity = 0;
	
	int damage = 1;             //goodguy damage
	int multiplier = 1;         //score multiplier
	
	String pre = "";            //prefix wihch is different for every sprite set, thus allows one to eaasily the analogous sprite for another character
	boolean sPower = false;     //true if super sonic is available
	int transformation = 0; 
	
	int transition = 0;         //transition countdown between levels
	
	String powerUp = "";
	
	boolean rightPressed = false;
	boolean leftPressed = false;
	boolean spacePressed = false;
	int spaceTime = 1;                 //how long space has been held to determine projectile size
	
	boolean gameOver = false;     // ---
	boolean inShop = false;       // state variables that determine what window the user is in
	boolean qAsked = false;       // ---
	boolean character = false;    // ---
	
	int mX = 1;    // ---
	int mY = 1;    // menu x, y z : show where in the menu the user is
	int mZ = 1;    // ---
	
	int sX = 1;   //shop x position or marker : 1, 2, or 3
	int[] price = new int[5]; //price of three powerups user can buy
	
	boolean correct = false; // is answer to question correct or not
	
	String response;  //user response to question
	
	
	Font myFont = new Font("Helvetica", 1, 60);    //generic font
	Font gFont = new Font("ObelixPro", 1, 45);     //in game font
	Font shopFont = new Font("Helvetica", 1, 60);  //shop font
	
	public MyCanvas() {
		this.setSize(1200, 800);
		this.addKeyListener(this);
		
		sonic.setHealth(3);
		this.setBackground(Color.BLACK);
		
		//clouds in start menu
		Block cl1 = new Block(450, 20, 150, 75, "files/cloud.png");
		Block cl2 = new Block(750, 20, 150, 75, "files/cloud.png");
		Block cl3 = new Block(1050, 20, 150, 75, "files/cloud.png");
		Block cl4 = new Block(150, 20, 150, 75, "files/cloud.png");
		clouds.add(cl1);
		clouds.add(cl2);
		clouds.add(cl3);
		clouds.add(cl4);
		
		for (int i = 1; i<=3; i++) {
			price[i] = 50;
		}
	}
	
	public void playIt(String filename) {
		try {
			InputStream in = new FileInputStream(filename);
			AudioStream as = new AudioStream(in);
			AudioPlayer.player.start(as);
		}
		catch(IOException e){
			System.out.println(e);
		}
	}
	
	public void paint(Graphics g) {
		
		time++;
		
		if (!character) {
			this.setBackground(Color.BLACK);
			Block s = new Block(155, 100, 200, 300, "files/sonic.png");
			Block k = new Block(460, 100, 300, 300, "files/knuckles.png");
			Block sh = new Block(850, 100, 200, 300, "files/shadow.png");
			//Block up = new Block(185+350*(mX-1), 540, 150, 130, "files/arrowUp.png");
			//g.drawImage(up.getImg(), up.getxCoord(), up.getyCoord(), up.getWidth(), up.getHeight(), this);
			for (int i = 0; i<3; i++) {
				if (mX==i+1) {
					g.setColor(Color.YELLOW);
					g.fillRect(120 + 350*i, 70, 260, 360);
				}
				else {
					g.setColor(Color.RED);
				}
				//g.fillRect(120 + 350*i, 70, 260, 360);
			}
			g.drawImage(s.getImg(), s.getxCoord(), s.getyCoord(), s.getWidth(), s.getHeight(), this);
			g.drawImage(k.getImg(), k.getxCoord(), k.getyCoord(), k.getWidth(), k.getHeight(), this);
			g.drawImage(sh.getImg(), sh.getxCoord(), sh.getyCoord(), sh.getWidth(), sh.getHeight(), this);
			g.setColor(Color.WHITE);
			Font cFont = new Font("Comic Sans", 1, 40);
			Font tFont = new Font("Heveltica", 1, 85);
			g.setFont(cFont);
			g.drawString("Sonic", 190, 480);
			g.drawString("Knuckles", 507, 480);
			g.drawString("Shadow", 865, 480);
			g.setFont(tFont);
			g.drawString("Choose a Character", 160, 700);
			g.setFont(myFont);
			repaint();
		}
		else if (inShop) { //game shop
			back.setImg("files/shop1.jpg");
			g.drawImage(back.getImg(), back.getxCoord(), back.getyCoord(), back.getWidth(), 800, this);
			g.setColor(Color.WHITE);
			g.setFont(shopFont);
			g.drawString("Total Points: " + String.valueOf(total), 360, 750);
			
			Block shopArrow = new Block(100+425*(sX-1), 535, 150, 130, "files/arrowUp.png");
			Block heart = new Block(75, 225, 200, 200, "files/heart.png");
			Block fist = new Block(480, 235, 200, 200, "files/fist.png");
			Block coins = new Block(905, 235, 200, 200, "files/coins.png");
			
			g.drawImage(shopArrow.getImg(), shopArrow.getxCoord(), shopArrow.getyCoord(), shopArrow.getWidth(), shopArrow.getHeight(), this);
			g.drawImage(heart.getImg(), heart.getxCoord(), heart.getyCoord(), heart.getWidth(), heart.getHeight(), this);
			g.drawImage(fist.getImg(), fist.getxCoord(), fist.getyCoord(), fist.getWidth(), fist.getHeight(), this);
			g.drawImage(coins.getImg(), coins.getxCoord(), coins.getyCoord(), coins.getWidth(), coins.getHeight(), this);
			
			//item subtitles && costs
			if (price[1]<=200) {
				if (total<price[1]) {
					g.setColor(Color.RED);
				}
				g.drawString("+1 Life", 70, 220);
				g.setColor(Color.WHITE);
				g.drawString("Cost:" + String.valueOf(price[1]), 60, 490);
			}
			else {
				g.setColor(Color.YELLOW);
				g.drawString("Maxed!", 70, 490);
				g.setColor(Color.WHITE);
			}
			
			if (price[2]<=200) {
				if (total<price[2]) {
					g.setColor(Color.RED);
				}
				g.drawString("Cost:" + String.valueOf(price[2]), 470, 490);
				g.setColor(Color.WHITE);
				g.drawString("+1 Damage", 410, 220);
			}
			else {
				g.setColor(Color.YELLOW);
				g.drawString("Maxed!", 480, 490);
				g.setColor(Color.WHITE);
			}
			
			if (price[3]<=200) {
				if (total<price[3]) {
					g.setColor(Color.RED);
				}
				g.drawString("Cost:" + String.valueOf(price[3]), 890, 490);
				g.setColor(Color.WHITE);
				g.drawString("x" + String.valueOf(multiplier+1) + " Score", 890, 220);
			}
			else {
				g.setColor(Color.YELLOW);
				g.drawString("Maxed!", 902, 490);
				g.setColor(Color.WHITE);
			}
			
			repaint();
		}
		else if (transformation>0 && start && !gameOver) {
			transformation--;
			this.setBackground(Color.BLACK);
			if (transformation>899) {
				Block t = new Block(450, 250, 300, 300, "files/trans8.png");
				g.drawImage(t.getImg(), t.getxCoord(), t.getyCoord(), t.getWidth(), t.getHeight(),  this);
			}
			else if (transformation>100) {
				Block t = new Block(450, 250, 300, 300, "files/trans" + String.valueOf(transformation/100) + ".png");
				g.drawImage(t.getImg(), t.getxCoord(), t.getyCoord(), t.getWidth(), t.getHeight(),  this);
			}
			else {
				Block t = new Block(450, 250, 300, 300, "files/trans1.png");
				g.drawImage(t.getImg(), t.getxCoord(), t.getyCoord(), t.getWidth(), t.getHeight(),  this);
			}
			repaint();
		}
		else if (transition>0 && start && !gameOver) {
			this.setBackground(Color.BLACK);
			g.setColor(Color.WHITE);
			Font levelFont = new Font("ObelixPro", 1, 100);
			g.setFont(levelFont);
			if (transition<1000) {
				g.drawString("Level " + String.valueOf(level), 240, 400);
				correct = false;
				transition--;
				qAsked = false;
			}
			else if (correct) {
				g.drawString("Correct!", 240, 400);
				transition-=2;
			}
			Random rand = new Random();
			if (!qAsked && transition>1000) {
				qAsked = true;
				int a = rand.nextInt(12)+1 + (level-2)*5;
				int b = rand.nextInt(12)+1 + (level-2)*5;
				int ans;
				int t = rand.nextInt(5);
				if (t==0) {
					ans = a*b;
					response = JOptionPane.showInputDialog("What is " + String.valueOf(a) + "x" + String.valueOf(b) + "?");
					if (response.equals(String.valueOf(ans))) {
						System.out.print("Correct!");
						correct = true;
						score+=3*multiplier;
						total+=3*multiplier;
					}
					else {
						gameOver = true;
						repaint();
					}
				}
				else if (t==1) {
					a = rand.nextInt(50) + 8 + (level-2)*5;
					b = rand.nextInt(50) + 8 + (level-2)*5;
					ans = a+b;
					response = JOptionPane.showInputDialog("What is " + String.valueOf(a) + "+" + String.valueOf(b) + "?");
					if (response.equals(String.valueOf(ans))) {
						System.out.print("Correct!");
						correct = true;
						score+=3*multiplier;
						total+=3*multiplier;
					}
					else {
						gameOver = true;
						repaint();
					}
				}
				else if (t==2){
					a = rand.nextInt(50) + 8 + (level-2)*5;
					b = rand.nextInt(50) + 8 + (level-2)*5;
					ans = Math.abs(a-b);
					if (a>=b) {
						response = JOptionPane.showInputDialog("What is " + String.valueOf(a) + "-" + String.valueOf(b) + "?");
						
					}
					else {
						response = JOptionPane.showInputDialog("What is " + String.valueOf(b) + "-" + String.valueOf(a) + "?");
					}
					if (response.equals(String.valueOf(ans))) {
						System.out.print("Correct!");
						correct = true;
						score+=3*multiplier;
						total+=3*multiplier;
					}
					else {
						gameOver = true;
						repaint();
					}
				}
				else {
					t = rand.nextInt(23);
					response = JOptionPane.showInputDialog("What is the capital of " + geoQ[t] + "?");
					if (response.equals(geoA[t])) {
						System.out.print("Correct!");
						correct = true;
						score+=3*multiplier;
						total+=3*multiplier;
					}
					else {
						gameOver = true;
						repaint();
					}
				}
				//response = JOptionPane.showInputDialog("What");
			}
			g.setFont(gFont);
			g.setColor(Color.BLACK);
			repaint();
		}
		else if (start && !gameOver) { //game started
			
			if (sPower) {
				transformation--;
				if (transformation<-10000) {
					pre = "";
					transformation = 0;
					sPower = false;
				}
			}
			
			//background coding
			if (badguys.size()>0 || level%3!=0) {
				back.setImg("files/back.jpg");
				//chaning backgrounds
				/*if (level%9<=3) {
					back.setImg("files/back1.png");
				}
				else if (level%9<=6) {
					back.setImg("files/back2.png");
				}
				else {
					back.setImg("files/back3.gif");
				}*/
				g.drawImage(back.getImg(), back.getxCoord(), back.getyCoord(), back.getWidth(), back.getHeight(), this);
			} 
			else {
				//dungeon background
			}
			
			if (immunity>0) {
				immunity--;
			}
			
			Rectangle sr = new Rectangle(star.getxCoord(), star.getyCoord(), star.getWidth(), star.getHeight());
			Rectangle a = new Rectangle(sonic.getxCoord(), sonic.getyCoord(), sonic.getWidth(), sonic.getHeight());
			
			if (bossDone==true && badguys.size()==0) {
				if (level%3!=0) {
					Block portal = new Block(1100, 510, 100, 180, "files/portal.png");
					g.drawImage(portal.getImg(), portal.getxCoord(), portal.getyCoord(), portal.getWidth(), portal.getHeight(), this);
				}
				else {
					Block portal = new Block(1100, 510, 100, 180, "files/portalYellow.png");
					g.drawImage(portal.getImg(), portal.getxCoord(), portal.getyCoord(), portal.getWidth(), portal.getHeight(), this);
				}
				
				if (level%3!=0) {
					Block lComp = new Block (400, 280, 400, 120, "files/level_complete.png");
					g.drawImage(lComp.getImg(), lComp.getxCoord(), lComp.getyCoord(), lComp.getWidth(), lComp.getHeight(), this);
				}
				else {
					Block lComp = new Block (400, 230, 400, 120, "files/level_complete.png");
					g.drawImage(lComp.getImg(), lComp.getxCoord(), lComp.getyCoord(), lComp.getWidth(), lComp.getHeight(), this);
				}
			}
			
			g.drawImage(sonic.getImg(), sonic.getxCoord(), sonic.getyCoord(), sonic.getWidth(), sonic.getHeight(),  this);
			
			for (int i = -5; i < this.getWidth(); i+=110) {
				Block bl = new Block(i, 690, 110, 110, "files/brick.png");
				g.drawImage(bl.getImg(), bl.getxCoord(), bl.getyCoord(), bl.getWidth(), bl.getHeight(), this);
				bricks.add(bl);
			}
			
			for (int i = 400; i < this.getWidth()-400; i+=80) {
				Block bl = new Block(i, 440, 80, 80, "files/brick.png");
				g.drawImage(bl.getImg(), bl.getxCoord(), bl.getyCoord(), bl.getWidth(), bl.getHeight(), this);
				bricks.add(bl);
			}
			
			if (badguys.size()>0 || level%3!=0) {
				
				//draw clouds
				for (int i=0; i<4; i++) {
					Block cl = (Block) clouds.get(i);
					if (time%15 == 1) {
						cl.setxCoord(cl.getxCoord()+1);
					}
					if (cl.getxCoord()>this.getWidth()) {
						cl.setxCoord(0);
						clouds.set(i, cl);
						g.drawImage(cl.getImg(), cl.getxCoord(), cl.getyCoord(), cl.getWidth(), cl.getHeight(), this);
					}
					else if (cl.getxCoord()>1050) {
						g.drawImage(cl.getImg(), cl.getxCoord(), cl.getyCoord(), cl.getWidth(), cl.getHeight(), this);
						g.drawImage(cl.getImg(), cl.getxCoord()-1200, cl.getyCoord(), cl.getWidth(), cl.getHeight(), this);
					}
					else {
						g.drawImage(cl.getImg(), cl.getxCoord(), cl.getyCoord(), cl.getWidth(), cl.getHeight(), this);
					}
				}
				
				//draw score and number of stars
				g.setFont(gFont);
				g.drawString("Score: " + String.valueOf(score), 22, 175);
				g.drawString("Total: " + String.valueOf(total), 20, 235);
				g.drawString("Level: " + String.valueOf(level), 44, 295);
			}
			else {
				//make text white during boss fight
				g.setColor(Color.WHITE);
				g.setFont(gFont);
				g.drawString("Score: " + String.valueOf(score), 20, 175);
				g.drawString("Total: " + String.valueOf(total), 28, 230);
				g.drawString("Level: " + String.valueOf(level), 24, 285);
				g.setColor(Color.BLACK);
			}
			
			for (int i = 0; i<sonic.getstartHealth(); i++) {
				if (i<sonic.getHealth()) {
					Block lf = new Block(600-(sonic.getstartHealth()*50)+(i*100), 5, 100, 100, "files/heart.png");
					g.drawImage(lf.getImg(), lf.getxCoord(), lf.getyCoord(), lf.getWidth(), lf.getHeight(), this);
				}
				else {
					Block lf = new Block(600-(sonic.getstartHealth()*50)+(i*100)+10, 20, 80, 80, "files/redX.png");
					g.drawImage(lf.getImg(), lf.getxCoord(), lf.getyCoord(), lf.getWidth(), lf.getHeight(), this);
				}
			}
			
			// bad guy code
			for (int i = 0; i < badguys.size(); i++) {
				Badguy bg = (Badguy) badguys.get(i);
				if (time%(15-bg.getSpeed())==1) {
					bg.setxCoord(bg.getxCoord()-1);
					if (bg.getxCoord()<(0-bg.getWidth())) {
						badguys.remove(bg);
						sonic.setHealth(sonic.getHealth()-1);
					}
					
				}
				Rectangle r = new Rectangle(bg.getxCoord(), bg.getyCoord(), bg.getWidth(), bg.getHeight());
				if (a.intersects(r) && (falling>0 || rolling>0 || sPower) && !bg.getlastHit()) {
					if(bg.getHealth()<=damage || sPower) {
						badguys.remove(i);
						playIt("files/poker.wav");
						System.out.println("HIT");
						score+=multiplier;
						total+=multiplier;
					}
					else {
						bg.setHealth(bg.getHealth()-damage);
						System.out.println(bg.getHealth());
						bg.setHeight(bg.getHeight()-5);
						bg.setWidth(bg.getWidth()-5);
						bg.setyCoord(bg.getyCoord()+5);
						badguys.set(i, bg);
					}
					immunity = 800;
					if (badguys.size()>0) {
						if (wasHit>0) {
							combo = true;
						}
						wasHit = 800;
					}
					bg.setlastHit(true);
				}
				else if (a.intersects(r) && jumping>0 && sonic.getyCoord()<490 && !bg.getlastHit()) {
					if(bg.getHealth()<=damage || sPower) {
						badguys.remove(i);
						playIt("files/poker.wav");
						System.out.println("HIT");
						score+=multiplier;
						total+=multiplier;
					}
					else {
						bg.setHealth(bg.getHealth()-1);
						System.out.println(bg.getHealth());
						bg.setHeight(bg.getHeight()-5);
						bg.setWidth(bg.getWidth()-5);
						bg.setyCoord(bg.getyCoord()+5);
						badguys.set(i, bg);
						immunity = 800;
					}
					if (badguys.size()>0) {
						if (wasHit>0) {
							combo = true;
						}
						wasHit = 800;
					}
					bg.setlastHit(true);
				}
				else if (a.intersects(r) && immunity<1 && rolling==0 && falling==0 && !bg.getlastHit()) {
					sonic.setHealth(sonic.getHealth()-1);
					immunity = 800;
					g.drawImage(bg.getImg(), bg.getxCoord(), bg.getyCoord(), bg.getWidth(), bg.getHeight(), this);
				}
				else {
					g.drawImage(bg.getImg(), bg.getxCoord(), bg.getyCoord(), bg.getWidth(), bg.getHeight(), this);
				}
				g.setColor(Color.BLACK);
				g.fillRect(bg.getxCoord()+2, bg.getyCoord()-17, bg.getWidth()-4, 12);
				g.setColor(Color.RED);
				g.fillRect(bg.getxCoord()+2, bg.getyCoord()-17, (bg.getWidth()-4)*bg.getHealth()/bg.getstartHealth(), 12);
				
			}
			
			
			for (int j = 0; j<blasts.size(); j++) {
				Projectile k = (Projectile) blasts.get(j);
				
				if (k.getxCoord() > this.getWidth() || k.getxCoord()<(0-k.getWidth())) {
					blasts.remove(k);
				}
				
				if (time%2==0) {
					//System.out.println(k.getDamage());
					if (k.getDirection()=="left") {
						k.setxCoord(k.getxCoord()-3);
					}
					else {
						k.setxCoord(k.getxCoord()+3);
					}
					blasts.set(j, k);
				}
				
				int x = (int) Math.round(k.getxCoord());
				int y = (int) Math.round(k.getyCoord());
				Rectangle kr = new Rectangle(x, y, k.getWidth(), k.getHeight());
				
				for (int i = 0; i<badguys.size(); i++){
					Badguy bg = (Badguy) badguys.get(i);
					Rectangle r = new Rectangle(bg.getxCoord(), bg.getyCoord(), bg.getWidth(), bg.getHeight());
					
					if (kr.intersects(r)) {
						blasts.remove(k);
						if (bg.getHealth()<=k.getDamage()) {
							badguys.remove(bg);
							playIt("files/poker.wav");
							System.out.println("HIT");
							score+=multiplier;
							total+=multiplier;
						}
						else {
							bg.setHealth(bg.getHealth()-k.getDamage());
							System.out.println(bg.getHealth());
							bg.setHeight(bg.getHeight()-5);
							bg.setWidth(bg.getWidth()-5);
							bg.setyCoord(bg.getyCoord()+5);
							badguys.set(i, bg);
						}
						
						if (badguys.size()>0) {
							if (wasHit>0) {
								combo = true;
							}
							wasHit = 800;
						}
					}
				}
				
				g.drawImage(k.getImg(), x, y, k.getWidth(), k.getHeight(), this);
				
				repaint();
			}
			
			//}
			
			if (time%25==1) {
				if (jumping == 10 && (sonic.getxCoord()>775 || sonic.getxCoord()<370 || sonic.getyCoord()<110)) {
					falling = 1;
					jumping = 0;
					sonic.fallIt(falling);
				}
				else if (jumping == 10) {
					jumping = 0;
					if (sonic.getDir()=="right") {
						sonic.setImg("files/" + pre + "stand_right.png");
					}
					else {
						sonic.setDir("left");
						sonic.setImg("files/" + pre + "stand_left.png");
					}
				}
				else if (jumping>0) {
					jumping++;
					sonic.jumpIt(jumping);
				}
				else if (falling == 10) {
					falling = 0;
					if (sonic.getDir()=="right" && (sonic.getyCoord()==600 || (sonic.getxCoord()>370 && sonic.getxCoord()<775))) {
						sonic.setImg("files/" + pre + "stand_right.png");
					}
					else if (sonic.getyCoord()==600 || (sonic.getxCoord()>370 && sonic.getxCoord()<775)){
						sonic.setImg("files/" + pre + "stand_left.png");
					}
					bgReset();
				}
				else if (falling > 0) {
					falling++;
					sonic.fallIt(falling);
				}
				else if ((sonic.getxCoord()>780 || sonic.getxCoord()<360) && sonic.getyCoord()<355){
					falling=1;
					sonic.fallIt(falling);
					sonic.setImg("files/" + pre + "spin.png");
				}
			}
			
			if (time%15==0 && rolling>0) {
				rolling++;
				if (rolling>5) {
					rolling = 0;
					bgReset();
					if (immunity<50) {
						immunity = 50;
					}
					lastRoll = time;
					if (falling+jumping>0) {
						
					}
					else if (sonic.getDir().equals("right")) {
						sonic.setImg("files/" + pre + "stand_right.png");
					}else {
						sonic.setImg("files/" + pre + "stand_left.png");
					}
				}
				else if (sonic.getxCoord()>1130) {
					lastRoll = time;
					rolling = 0;
					if (immunity<200) {
						immunity = 200;
					}
					if (falling+jumping>0) {
						
					}
					else if (sonic.getDir().equals("right")) {
						sonic.setImg("files/" + pre + "stand_right.png");
					}else {
						sonic.setImg("files/" + pre + "stand_left.png");
					}
					sonic.setxCoord(1100);
				}
				else if (sonic.getxCoord()<20) {
					lastRoll = time;
					rolling = 0;
					if (immunity<200) {
						immunity = 200;
					}
					if (falling+jumping>0) {
						
					}
					else if (sonic.getDir().equals("right")) {
						sonic.setImg("files/" + pre + "stand_right.png");
					}else {
						sonic.setImg("files/" + pre + "stand_left.png");
					}
					sonic.setxCoord(20);
				}
				else {
					sonic.setImg("files/" + pre + "spin.png");
					sonic.rollIt();
				}
			}
			
			if (time%10000==1 && stones.size()==0) {
				Random rand = new Random();
				int r = rand.nextInt(50);
				if (r<10) {
					Block s = new Block(575, 360, 50, 80, "files/red_stone.png", "red");
					stones.add(s);
				} else if (r<20) {
					Block s = new Block(575, 360, 50, 80, "files/green_stone.png", "green");
					stones.add(s);
				}
				else if (pre.equals("")) {
					Block s = new Block(575, 360, 50, 80, "files/yellow_stone.png", "yellow");
					stones.add(s);
				}
			}
			
			for (int i = 0; i<stones.size(); i++) {
				Block s = (Block) stones.get(i);
				Rectangle rStone = new Rectangle(s.getxCoord(), s.getyCoord(), s.getWidth(), s.getHeight());
				if (a.intersects(rStone)) {
					if (s.getStone().equals("red")) {
						sonic.setspeedX(sonic.getspeedX()+12);
						powerUp="speed";
						wasHit = 800;
					}
					else if (s.getStone().equals("green")) {
						if (sonic.getHealth()<3) {
							sonic.setHealth(sonic.getHealth()+1);
						}
						powerUp="life";
						wasHit = 800;
					}
					else if (s.getStone().equals("yellow")) {
						transformation = 950;
						sPower = true;
						pre = "super";
						repaint();
					}
					stones.remove(i);
				}
				else {
					g.drawImage(s.getImg(), s.getxCoord(), s.getyCoord(), s.getWidth(), s.getHeight(), this);
				}
			}
			
			if (time%1500<900) {
				if (gotStar) {
					//if star is collect make stars yellow
					Block bl = new Block(20, 20, 100, 100, "files/yellow_star.png");
					Block bl2 = new Block(1080, 20, 100, 100, "files/yellow_star.png");
					g.drawImage(bl.getImg(), bl.getxCoord(), bl.getyCoord(), bl.getWidth(), bl.getHeight(), this);
					g.drawImage(bl2.getImg(), bl2.getxCoord(), bl2.getyCoord(), bl2.getWidth(), bl2.getHeight(), this);
				}
				
				else {
					//before star is collected make stars black
					Block bl = new Block(20, 20, 100, 100, "files/black_star.png");
					Block bl2 = new Block(1080, 20, 100, 100, "files/black_star.png");
					g.drawImage(bl.getImg(), bl.getxCoord(), bl.getyCoord(), bl.getWidth(), bl.getHeight(), this);
					g.drawImage(bl2.getImg(), bl2.getxCoord(), bl2.getyCoord(), bl2.getWidth(), bl2.getHeight(), this);
				}
			}
			
			if (bossDone == true && badguys.size()==0 && level%3==0) {
				//Block bl = new Block(400, 200, 400, 130, "files/win.png");
				//g.drawImage(bl.getImg(), bl.getxCoord(), bl.getyCoord(), bl.getWidth(), bl.getHeight(), this);
				g.drawImage(star.getImg(), star.getxCoord(), star.getyCoord(), star.getWidth(), star.getHeight(), this);
				bossTime--;
				if (bossTime>0) {
					g.drawImage(boss.getImg(), boss.getxCoord(), boss.getyCoord(), boss.getWidth(), boss.getHeight(), this);
				}
			}
			else if (badguys.size()==0 && level%3==0) {
				this.setBackground(Color.BLACK);
				if (bossMusic==false) {
					bossMusic = true;
					playIt("files/tRex.wav");
				}
				
				//bowser junior code
				if (jr.getHealth()>0) {
					if (time%3==0) {
						if (jr.getxCoord()<sonic.getxCoord()) {
							jr.setxCoord(jr.getxCoord()+1);
						}
						else if (jr.getxCoord()>sonic.getxCoord()){
							jr.setxCoord(jr.getxCoord()-1);
						}
					}
					
					if (time%1000<20) {
						jr.setyCoord(jr.getyCoord()-1);
					}
					else if (time%1000>499 && time%1000<520) {
						jr.setyCoord(jr.getyCoord()+1);
					}
					
					g.setColor(Color.WHITE);
					g.fillRect(jr.getxCoord()-1, jr.getyCoord()-20, jr.getWidth()+2, 18);
					g.setColor(Color.BLACK);
					g.fillRect(jr.getxCoord()+2, jr.getyCoord()-17, jr.getWidth()-4, 12);
					g.setColor(Color.RED);
					g.fillRect(jr.getxCoord()+2, jr.getyCoord()-17, (jr.getWidth()-4)*jr.getHealth()/jr.getstartHealth(), 12);
					g.setColor(Color.BLACK);
					
					Rectangle j = new Rectangle(jr.getxCoord(), jr.getyCoord(), jr.getWidth(), jr.getHeight());
					if (a.intersects(j) && jumping+falling>0) {
						if (jr.getHealth()==1 || sPower) {
							jr.setHealth(0);
							score+=3*multiplier;
							total+=3*multiplier;
						}
						else if (time%150==0) {
							jr.setHealth(jr.getHealth()-1);
						}
					}
					else if (time%2000==0) {
						Projectile bomb = new Projectile(jr.getxCoord()+45, jr.getyCoord()+60, 30, 30, "files/bomb.png");
						bombs.add(bomb);
					}
					
					for (int i=0; i<bombs.size(); i++) {
						Projectile bomb = (Projectile) bombs.get(i);
						if (time%3==0) {
							bomb.setyCoord(bomb.getyCoord()+1);
						}
						
						if (a.contains(bomb.getxCoord()+bomb.getWidth()/2, bomb.getyCoord()+bomb.getHeight()/2)) {
							sonic.setHealth(sonic.getHealth()-1);
							bombs.remove(bomb);
						}
						else if (bomb.getyCoord()>660) {
							bombs.remove(bomb);
						}
						else if (bomb.getyCoord()>410 && bomb.getxCoord()>375 && bomb.getxCoord()<795) {
							bombs.remove(bomb);
						}
						
						bombs.set(i, bomb);
						int x = (int) Math.round(bomb.getxCoord()); 
						int y = (int) Math.round(bomb.getyCoord()); 
						g.drawImage(bomb.getImg(), x, y, bomb.getWidth(), bomb.getHeight(), this);
					}
					
					g.drawImage(jr.getImg(), jr.getxCoord(), jr.getyCoord(), jr.getWidth(), jr.getHeight(), this);
				}
					
				//big bowser code
				if (time%30==0 && boss.getxCoord()<50) {
					boss.setxCoord(boss.getxCoord()+1);
				}
				
				g.setColor(Color.WHITE);
				g.fillRect(boss.getxCoord()-3, boss.getyCoord()-22, boss.getWidth()+6, 22);
				g.setColor(Color.BLACK);
				g.fillRect(boss.getxCoord()+2, boss.getyCoord()-17, boss.getWidth()-4, 12);
				g.setColor(Color.RED);
				g.fillRect(boss.getxCoord()+2, boss.getyCoord()-17, (boss.getWidth()-4)*boss.getHealth()/boss.getstartHealth(), 12);
				g.setColor(Color.BLACK);
				
				if (time%1200==1) {
					Projectile fb = new Projectile(boss.getxCoord()+boss.getWidth()-35, boss.getyCoord()+65, 56, 42, "files/fireball.png", sonic.getxCoord()-(boss.getxCoord()+boss.getWidth()-35), sonic.getyCoord()-(boss.getyCoord()+65));
					fballs.add(fb);
				}
				
				for (int i = 0; i < fballs.size(); i++) {
					Projectile fb = (Projectile) fballs.get(i);
					
					/*if (time%2==0) {
						fb.setxCoord(fb.getxCoord()+1);
						fballs.set(i, fb);
					}*/
					if (time%3!=0) {
						fb.bossMove();
					}
					
					int x = (int) Math.round(fb.getxCoord());
					int y = (int) Math.round(fb.getyCoord());
					
					Rectangle fr = new Rectangle(x, y, fb.getWidth(), fb.getHeight());
					if (a.intersects(fr)) {
						System.out.println("Sonic hit by fireball!");
						if (difficulty==1) {
							sonic.setHealth(sonic.getHealth()-1);
						}
						else {
							sonic.setHealth(0);
						}
						fballs.remove(i);
					}
					if (fb.getxCoord()>this.getWidth()) {
						fballs.remove(i);
					}
					g.drawImage(fb.getImg(), x, y, fb.getWidth(), fb.getHeight(), this);
				}
				
				Rectangle b = new Rectangle(boss.getxCoord(), boss.getyCoord(), boss.getWidth(), boss.getHeight());
				
				for (int i = 0; i < blasts.size(); i++) {
					
					Projectile fb = (Projectile) blasts.get(i);
					if (time%2==0) {
						if (fb.getDirection()=="left") {
							fb.setxCoord(fb.getxCoord()-3);
						}
						else {
							fb.setxCoord(fb.getxCoord()+3);
						}
						blasts.set(i, fb);
					}
					
					int x = (int) Math.round(fb.getxCoord());
					int y = (int) Math.round(fb.getyCoord());
					Rectangle fr = new Rectangle(x, y, fb.getWidth(), fb.getHeight());
					if (b.intersects(fr)) {
						System.out.println("Boss hit by fireball!");
						if(boss.getHealth()<=fb.getDamage() || sPower) {
							bossDone = true;
							score+=10*multiplier;
							total+=10*multiplier;
							System.out.println("Boss Done");
							playIt("files/cheering.wav");
							star.setImg("files/tenor.gif");
							boss.setImg("files/bang.png");
							g.drawImage(boss.getImg(), boss.getxCoord(), boss.getyCoord(), boss.getWidth(), boss.getHeight(), this);
						}
						else{
							boss.setHealth(boss.getHealth()-fb.getDamage());
							System.out.println(boss.getHealth());
							g.drawImage(boss.getImg(), boss.getxCoord(), boss.getyCoord(), boss.getWidth(), boss.getHeight(), this);
						}
						blasts.remove(fb);
					}
					
					if (fb.getxCoord()>this.getWidth() || fb.getxCoord()<(0-fb.getWidth())) {
						blasts.remove(i);
					}
					
					g.drawImage(fb.getImg(), x, y, fb.getWidth(), fb.getHeight(), this);
					
					repaint();
				}
				
				if (b.intersects(a) && (rolling>0 || sPower)) {
					if(boss.getHealth()==1 || sPower) {
						bossDone = true;
						score+=10*multiplier;
						total+=10*multiplier;
						System.out.println("Boss Done");
						playIt("files/cheering.wav");
						star.setImg("files/tenor.gif");
						boss.setImg("files/bang.png");
						g.drawImage(boss.getImg(), boss.getxCoord(), boss.getyCoord(), boss.getWidth(), boss.getHeight(), this);
					}
					else if (immunity<750){
						boss.setHealth(boss.getHealth()-1);
						System.out.println(boss.getHealth());
					}
					immunity = 800;
				}
				else if (b.intersects(a) && immunity<1) {
					sonic.setHealth(sonic.getHealth()-1);
					immunity = 800;
				}
				g.drawImage(boss.getImg(), boss.getxCoord(), boss.getyCoord(), boss.getWidth(), boss.getHeight(), this);
			}
			
			if (sr.intersects(a) && bossDone==true && badguys.size()==0 && level%3==0) {
				star.setImg("nothing");
				if (gotStar == false) {
					playIt("files/chaching.wav");
					score+=5*multiplier;
					total+=5*multiplier;
				}
				gotStar = true;
			}
			
			if (powerUp!="" && wasHit>0) {
				wasHit--;
				if (powerUp.equals("speed")) {
					Block bl = new Block(490, 120, 220, 80, "files/speedup.png");
					g.drawImage(bl.getImg(), bl.getxCoord(), bl.getyCoord(), bl.getWidth(), bl.getHeight(), this);
				} 
				else if (powerUp.equals("life")) {
					Block bl = new Block(490, 120, 260, 80, "files/liferestored.png");
					g.drawImage(bl.getImg(), bl.getxCoord(), bl.getyCoord(), bl.getWidth(), bl.getHeight(), this);
				} 
			}
			else if (wasHit==0) {
				combo = false;
				powerUp="";
			}
			else if (wasHit>0) {
				wasHit--;
				if (combo==false) {
					Block bl = new Block(525, 200, 150, 75, "files/hit.png");
					g.drawImage(bl.getImg(), bl.getxCoord(), bl.getyCoord(), bl.getWidth(), bl.getHeight(), this);
				}
				else {
					Block bl = new Block(500, 200, 200, 80, "files/combo.png");
					g.drawImage(bl.getImg(), bl.getxCoord(), bl.getyCoord(), bl.getWidth(), bl.getHeight(), this);
				}
			}
			
			if (bossDone == true && sonic.getxCoord()>1050 && badguys.size()==0) {
				gotStar = false;
				level++;
				if (level%3==0) {
					bossDone = false;
					bossMusic = false;
				}
				else if (level%3==1) {
					playIt("files/brawl.wav");
				}
				combo = false;
				powerUp = "";
				wasHit = 0;
				score+=3*multiplier;
				total+=3*multiplier;
				sonic.setxCoord(0);
				//sonic.setImg("files/sonic.png");
				sonic.setWay(0);
				sonic.setHealth(3);
				sonic.setDir("mid");
				boss.setImg("files/bowser.png");
				boss.setxCoord(-200);
				jr.setxCoord(-200);
				star.setImg("files/nothing");
				this.setBackground(Color.CYAN);
				lastFireball = -250;
				transition = 1800;
				
				for (int i =0; i<fballs.size(); i++) {
					fballs.remove(i);
				}
				
				spawn();
				
			}
			
			
			if (sonic.getHealth()==0) {
				gameOver = true;
				for (int i=0; i<fballs.size(); i++) {
					fballs.remove(i);
				}
				for (int i=0; i<badguys.size(); i++) {
					Badguy bg = (Badguy) badguys.get(i);
					badguys.remove(bg);
				}
				for (int i=0; i<blasts.size(); i++) {
					blasts.remove(i);
				}
			}
			
			repaint();
		}
		else if (gameOver && start) {
			this.setBackground(Color.WHITE);
			Block gOver = new Block(100, 200, 1000, 450, "files/gameover.gif");
			g.drawImage(gOver.getImg(), gOver.getxCoord(), gOver.getyCoord(), gOver.getWidth(), gOver.getHeight(), this);
			g.setFont(myFont);
			g.drawString("Press [Enter] to Restart", 300, 700);
			//g.drawString("Press [ESC] for Shop", 350, 600);
		}
		else {
			g.setFont(myFont);
			back.setImg("files/back.jpg");
			g.drawImage(back.getImg(), back.getxCoord(), back.getyCoord(), back.getWidth(), back.getHeight(), this);
			for (int i = -5; i < this.getWidth(); i+=110) {
				Block bl = new Block(i, 690, 110, 110, "files/brick.png");
				g.drawImage(bl.getImg(), bl.getxCoord(), bl.getyCoord(), bl.getWidth(), bl.getHeight(), this);
				bricks.add(bl);
			}
			
			for (int i = 400; i < this.getWidth()-400; i+=80) {
				Block bl = new Block(i, 440, 80, 80, "files/brick.png");
				g.drawImage(bl.getImg(), bl.getxCoord(), bl.getyCoord(), bl.getWidth(), bl.getHeight(), this);
				bricks.add(bl);
			}
			g.drawString("Highscore: " + String.valueOf(highScore), 410, 650);
			g.drawImage(menu.getImg(), menu.getxCoord(), menu.getyCoord(), menu.getWidth(), menu.getHeight(), this);
			g.drawImage(diff.getImg(), diff.getxCoord(), diff.getyCoord(), diff.getWidth(), diff.getHeight(), this);
			g.drawImage(shop.getImg(), shop.getxCoord(), shop.getyCoord(), shop.getWidth(), shop.getHeight(), this);
			g.drawImage(exit.getImg(), exit.getxCoord(), exit.getyCoord(), exit.getWidth(), exit.getHeight(), this);
			if (mX==1) {
				arrow.setImg("files/arrow.png");
				if (mY==1) {
					g.drawImage(arrow.getImg(), 150, 130, 130, 150, this);
				}
				else if (mY==2) {
					g.drawImage(arrow.getImg(), 290, 310, 130, 150, this);
				}
				else {
					g.drawImage(arrow.getImg(), 290, 425, 130, 150, this);
				}
			}
			else {
				arrow.setImg("files/arrowUp.png");
				g.drawImage(arrow.getImg(), 975, 300, 150, 130, this);
			}
			//Block av = new Block (425, 420, 350, 500, "files/sonic.png");
			//g.drawImage(av.getImg(), av.getxCoord(), av.getyCoord(), av.getWidth(), av.getHeight(), this);
			//g.drawImage(sonic.getImg(), 465, 475, 270, 300, this);
			for (int i=0; i<4; i++) {
				Block cl = (Block) clouds.get(i);
				if (time%25 == 1) {
					cl.setxCoord(cl.getxCoord()+1);
				}
				if (cl.getxCoord()>this.getWidth()) {
					cl.setxCoord(0);
					clouds.set(i, cl);
					g.drawImage(cl.getImg(), cl.getxCoord(), cl.getyCoord(), cl.getWidth(), cl.getHeight(), this);
				}
				else if (cl.getxCoord()>1050) {
					g.drawImage(cl.getImg(), cl.getxCoord(), cl.getyCoord(), cl.getWidth(), cl.getHeight(), this);
					g.drawImage(cl.getImg(), cl.getxCoord()-1200, cl.getyCoord(), cl.getWidth(), cl.getHeight(), this);
				}
				else {
					g.drawImage(cl.getImg(), cl.getxCoord(), cl.getyCoord(), cl.getWidth(), cl.getHeight(), this);
				}
			}
			
			repaint();
		}
	}
	
	
	
	
	public void keyPressed(KeyEvent e) {
		System.out.println(e);
		if (character) {
			if (inShop) {
				if (e.getKeyCode()==27) {
					inShop = false;
				}
				else if (e.getKeyCode()==39) {
					sX++;
					if (sX>3) {
						sX=3;
					}
				}
				else if (e.getKeyCode()==37) {
					sX--;
					if (sX<1) {
						sX=1;
					}
				}
				else if (e.getKeyCode()==10) {
					if (price[sX]<=200 && total>=price[sX]){
						total-=price[sX];
						price[sX] = price[sX]*2;
						if (sX==1) {
							sonic.setHealth(sonic.getHealth()+1);
						}
						else if (sX==2) {
							damage++;
						}
						else {
							multiplier++;
						}
						playIt("files/tone.wav");
					}
					else{
						playIt("files/error.wav");
					}
				}
			}
			else if (gameOver) {
				if (e.getKeyCode()==10) {
					start = false;
					gameOver = false;
					
					mX = 1;
					mY = 1;
					gotStar = false;
					level = 1;
					bossDone = true;
					combo = false;
					sPower = false;
					powerUp = "";
					wasHit = 0;
					sonic.setxCoord(10);
					sonic.setWay(0);
					sonic.setDir("mid");
					sonic.setImg("files/sonic.png");
					boss.setImg("files/bowser.png");
					boss.setxCoord(-200);
					jr.setxCoord(-200);
					star.setImg("files/nothing");
					this.setBackground(Color.CYAN);
					lastFireball = -51;
					price[1] = 50;
					price[2] = 50;
					price[3] = 50;
					
					multiplier = 1;
					damage = 1;
					sonic.setHealth(3);
					
					if (pre.equals("super")) {
						pre = "";
					}
					transformation = 0;
					sPower = false;
					
					if (score>highScore) {
						highScore = score;
					}
					score = 0;
					
					for (int i=0; i<fballs.size(); i++) {
						fballs.remove(i);
					}
					for (int i=0; i<badguys.size(); i++) {
						Badguy bg = (Badguy) badguys.get(i);
						badguys.remove(bg);
					}
					for (int i=0; i<blasts.size(); i++) {
						blasts.remove(i);
					}
					
					spawn();
				}
			}
			else if ((start) && (transition<1) && (transformation<1)) {
				
				if (e.getKeyCode()==39) {
					rightPressed = true;
				}
				else if (e.getKeyCode()==37) {
					leftPressed = true;
				}
				
				if (e.getExtendedKeyCode()==32) {
					spaceTime++;
					spacePressed = true;
				}
				else if (e.getKeyCode()==38 && (jumping!=0 || falling!=0)) {
					System.out.println("Cant double jump!");
				}
				else if (rolling>0) {
					System.out.println("Cant jump when rolling!");
				}
				else if (e.getKeyCode()==38) {
					jumping = 1;
					sonic.jumpIt(jumping);
					playIt("files/jumpSound.wav");
				}
				else if (e.getKeyCode()==82 && (jumping+falling+rolling==0) && time-lastRoll>120) {
					rolling = 1;
					if (sPower) {
						sonic.rollSuper();
					}
					else {
						sonic.rollIt();
					}
					sonic.setImg("files/" + pre + "spin.png");
				}
				
				if (sPower && jumping==0 && falling==0 && rolling==0) {
					sonic.moveSuper(e.getKeyCode(), this.getWidth()-sonic.getWidth()+20, this.getHeight());
				}
				else if (sPower && rolling==0) {
					sonic.airSuper(e.getKeyCode(), this.getWidth()-sonic.getWidth()+20, this.getHeight());
				}
				else if (pre.equals("") && jumping==0 && falling==0 && rolling==0) {
					sonic.moveIt(e.getKeyCode(), this.getWidth()-sonic.getWidth()+20, this.getHeight());
				}
				else if (pre.equals("") && rolling==0){
					sonic.moveAir(e.getKeyCode(), this.getWidth()-sonic.getWidth()+20, this.getHeight());
				}
				else if (jumping==0 && falling==0 && rolling==0) {
					sonic.moveKn(e.getKeyCode(), this.getWidth()-sonic.getWidth()+20, this.getHeight(), pre);
				}
				else if (rolling==0){
					sonic.airKn(e.getKeyCode(), this.getWidth()-sonic.getWidth()+20, this.getHeight());
				}
				
				if (jumping>0 || falling>0) {
					if (sonic.getDir().equals("left")) {
						sonic.setImg("files/" + pre + "spin.png");
						sonic.setDir("left");
					}
					else {
						sonic.setImg("files/" + pre + "spin.png");
						sonic.setDir("right");
					}
				}
				
				for (int i = 0; i < badguys.size(); i++) {
					Badguy bg = (Badguy) badguys.get(i);
					Rectangle r = new Rectangle(bg.getxCoord(), bg.getyCoord(), bg.getWidth(), bg.getHeight());
					Rectangle ar = new Rectangle(sonic.getxCoord(), sonic.getyCoord(), sonic.getWidth(), sonic.getHeight());
					if (r.intersects(ar) && (falling>0 || rolling>0 || sPower)) {
						if(bg.getHealth()<=damage || sPower) {
							badguys.remove(i);
							playIt("files/poker.wav");
							System.out.println("HIT");
						}
						else if (immunity<750){
							bg.setHealth(bg.getHealth()-damage);
							System.out.println(bg.getHealth());
							bg.setHeight(bg.getHeight()-5);
							bg.setWidth(bg.getWidth()-5);
							bg.setyCoord(bg.getyCoord()+5);
							badguys.set(i, bg);
						}
						immunity = 800;
						if (wasHit>0) {
							combo = true;
						}
						wasHit = 800;
						
					}
					else if (r.intersects(ar) && immunity<1) {
						sonic.setHealth(sonic.getHealth()-1);
						immunity = 800;
						if (sonic.getHealth()==0) {
							gameOver = true;
						}
					}
				}
				
			}
			else {
				if (e.getKeyCode()==10) {
					if (mX==1 && mY==1) {
						start = true;
						spawn();
						playIt("files/brawl.wav");
						star.setImg("files/tenor.gif");
						sonic.setstartHealth(sonic.getHealth());
						if (pre.equals("")) {
							sonic.setImg("files/sonic.png");
						} 
						else if (pre.equals("kn")) {
							sonic.setImg("files/knuckles.png");
						}
						else if (pre.equals("sh")) {
							sonic.setImg("files/shadow.png");
						}
					}
					else if (mX==1 && mY==2) {
						inShop = true;
					}
					else if (mX==1 && mY==3) {
						System.exit(1);
					}
				}
				else if (e.getKeyCode()==27) {
					if (true) {
						
					}
				}
				else if (e.getKeyCode()==38) {
					if (mX==1) {
						mY--;
						if (mY<1) {
							mY=1;
						}
					}
					else if (difficulty==1) {
						difficulty++;
						diff.setImg("files/hard.png");
						//this.setBackground(Color.RED);
					}
				}
				else if (e.getKeyCode()==40) {
					if (mX==1) {
						mY++;
						if (mY>3) {
							mY=3;
						}
					}
					else if (difficulty==2) {
						difficulty--;
						diff.setImg("files/easy.png");
						//this.setBackground(Color.CYAN);
					}
				}
				else if (e.getKeyCode()==39) {
					if (mX==1) {
						mX++;
					}
				}
				else if (e.getKeyCode()==37) {
					if (mX==2) {
						mX--;
					}
				}
				
			}
		}
		else {
			if (e.getKeyCode()==10) {
				if (mX==1) {
					pre = "";
				} else if (mX==2) {
					pre = "kn";
				}
				else {
					pre = "sh";
				}
				character = true;
				mX = 1;
			} else if (e.getKeyCode()==39) {
				mX++;
				if (mX>3) {
					mX = 3;
				}
			}
			else if (e.getKeyCode()==37) {
				mX--;
				if (mX<1) {
					mX = 1;
				}
			}
		}
		repaint();
	}

	@Override
	public void keyTyped(KeyEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void keyReleased(KeyEvent e) {
		// TODO Auto-generated method stub
		if (character && start && transformation<1) {
			if (jumping>0 || falling>0 || rolling>0) {
				
			}
			else if (e.getKeyCode()==39) {
				sonic.setDir("right");
				sonic.setImg("files/" + pre + "stand_right.png");
				sonic.setWay(1);
			}
			else if (e.getKeyCode()==37) {
				sonic.setDir("left");
				sonic.setImg("files/" + pre + "stand_left.png");
				sonic.setWay(1);
			}
			else if (e.getKeyCode() == 32 && time-lastFireball>300 && falling==0 && jumping==0) {
				Projectile blast = new Projectile(sonic.getxCoord()+40, sonic.getyCoord()+30, 40, 25, "files/bblast1.png", damage);
				if (spaceTime>15) {
					blast.setWidth(80);
					blast.setHeight(40);
					blast.setDamage(damage*3);
					if (sonic.getDir().equals("left")) {
						blast.setImg("files/bigblast2.png");
						blast.setDirection("left");
						blast.setxCoord(sonic.getxCoord()+10);
					}
					else {
						blast.setImg("files/bigblast1.png");
						blast.setDirection("right");
					}
				}
				else {
					if (sonic.getDir().equals("left")) {
						blast.setImg("files/bblast2.png");
						blast.setDirection("left");
						blast.setxCoord(sonic.getxCoord()+10);
					}
					else {
						blast.setImg("files/bblast1.png");
						blast.setDirection("right");
					}
				}
				blasts.add(blast);
				lastFireball = time;
				spacePressed = true;
				if (rightPressed) {
					sonic.setDir("right");
					sonic.setImg("files/" + pre + "stand_right.png");
				} else if (leftPressed) {
					sonic.setDir("left");
					sonic.setImg("files/" + pre + "stand_left.png");
				}
			}
			/*else if (e.getKeyCode()==82 && (jumping+falling+rolling==0) && time-lastRoll>150) {
				rolling = 1;
				if (sPower) {
					sonic.rollSuper();
				}
				else {
					sonic.rollIt();
				}
				sonic.setImg("files/" + pre + "spin.png");
			}*/
				
			
			if (e.getKeyCode()==39) {
				rightPressed = false;
			}
			else if (e.getKeyCode()==37) {
				leftPressed = false;
				
			}
			else if (e.getKeyCode()==32) {
				spacePressed=false;
				spaceTime = 1;
			}
		}
		else {
			
		}
		
	}
	
	public void spawn() {
		if (difficulty==1) {
			Random rand  = new Random();
			int winwidth = this.getWidth()/2;
			for (int i = 0; i < level*4; i++) {
				if (i%6==0) {
					Badguy bg = new Badguy(rand.nextInt(winwidth)+1200, 610, 100, 80, "files/ghost.png", 11, 1+level/2);
					Rectangle r = new Rectangle(bg.getxCoord(), bg.getyCoord(), bg.getWidth(), bg.getHeight());
					if (r.contains(sonic.getxCoord(), sonic.getyCoord())) {
						System.out.println("Badguy on top of sonic");
						continue;
					}
					badguys.add(bg);
				}
				else if (i%6<3){
					Badguy bg = new Badguy(rand.nextInt(winwidth)+1200, 610, 50, 80, "files/turtle.png", 6, 1+2*level/3);
					Rectangle r = new Rectangle(bg.getxCoord(), bg.getyCoord(), bg.getWidth(), bg.getHeight());
					if (r.contains(sonic.getxCoord(), sonic.getyCoord())) {
						System.out.println("Badguy on top of sonic");
						continue;
					}
					badguys.add(bg);
				}
				else if (i%6<4){
					Badguy bg = new Badguy(rand.nextInt(winwidth)+1200, 610, 100, 80, "files/spike_top.png", 6, 1+level);
					Rectangle r = new Rectangle(bg.getxCoord(), bg.getyCoord(), bg.getWidth(), bg.getHeight());
					if (r.contains(sonic.getxCoord(), sonic.getyCoord())) {
						System.out.println("Badguy on top of sonic");
						continue;
					}
					badguys.add(bg);
				}
				else {
					Badguy bg = new Badguy(rand.nextInt(winwidth)+1500, 300, 120, 80, "files/bullet.png", 13, 1);
					Rectangle r = new Rectangle(bg.getxCoord(), bg.getyCoord(), bg.getWidth(), bg.getHeight());
					if (r.contains(sonic.getxCoord(), sonic.getyCoord())) {
						System.out.println("Badguy on top of sonic");
						continue;
					}
					badguys.add(bg);
				}
				if (i%15==0) {
					Badguy bg = new Badguy(rand.nextInt(winwidth)+1200, 540, 100, 150, "files/pokey1.png", 9, 1);
					Rectangle r = new Rectangle(bg.getxCoord(), bg.getyCoord(), bg.getWidth(), bg.getHeight());
					if (r.contains(sonic.getxCoord(), sonic.getyCoord())) {
						System.out.println("Badguy on top of sonic");
						continue;
					}
					badguys.add(bg);
				}
			}
			boss.setHealth(10*level/3);
			boss.setstartHealth(10*level/3);
			jr.setHealth(5*level/3);
			jr.setstartHealth(5*level/3);
		}
		else {
			Random rand  = new Random();
			int winwidth = this.getWidth()/2;
			for (int i = 0; i < level*10; i++) {
				if (i%6==0) {
					Badguy bg = new Badguy(rand.nextInt(winwidth)+1200, 610, 100, 80, "files/ghost.png", 9, level);
					Rectangle r = new Rectangle(bg.getxCoord(), bg.getyCoord(), bg.getWidth(), bg.getHeight());
					if (r.contains(sonic.getxCoord(), sonic.getyCoord())) {
						System.out.println("Badguy on top of sonic");
						continue;
					}
					badguys.add(bg);
				}
				else if (i%6<3){
					Badguy bg = new Badguy(rand.nextInt(winwidth)+1200, 610, 50, 80, "files/turtle.png", 5, level);
					Rectangle r = new Rectangle(bg.getxCoord(), bg.getyCoord(), bg.getWidth(), bg.getHeight());
					if (r.contains(sonic.getxCoord(), sonic.getyCoord())) {
						System.out.println("Badguy on top of sonic");
						continue;
					}
					badguys.add(bg);
				}
				else if (i%6<4){
					Badguy bg = new Badguy(rand.nextInt(winwidth)+1200, 610, 100, 80, "files/spike_top.png", 5, level*2);
					Rectangle r = new Rectangle(bg.getxCoord(), bg.getyCoord(), bg.getWidth(), bg.getHeight());
					if (r.contains(sonic.getxCoord(), sonic.getyCoord())) {
						System.out.println("Badguy on top of sonic");
						continue;
					}
					badguys.add(bg);
				}
				else{
					Badguy bg = new Badguy(rand.nextInt(winwidth)+1500, 300, 120, 80, "files/bullet.png", 14, 2);
					Rectangle r = new Rectangle(bg.getxCoord(), bg.getyCoord(), bg.getWidth(), bg.getHeight());
					if (r.contains(sonic.getxCoord(), sonic.getyCoord())) {
						System.out.println("Badguy on top of sonic");
						continue;
					}
					badguys.add(bg);
				}
				if (i%15==0) {
					Badguy bg = new Badguy(rand.nextInt(winwidth)+1200, 540, 100, 150, "files/pokey1.png", 9, 1);
					Rectangle r = new Rectangle(bg.getxCoord(), bg.getyCoord(), bg.getWidth(), bg.getHeight());
					if (r.contains(sonic.getxCoord(), sonic.getyCoord())) {
						System.out.println("Badguy on top of sonic");
						continue;
					}
					badguys.add(bg);
				}
			}
			boss.setHealth(20*level/3);
			boss.setstartHealth(20*level/3);
			jr.setHealth(10*level/3);
			jr.setstartHealth(10*level/3);
		}
	}
	
	public void bgReset() {
		for (int i = 0; i<badguys.size(); i++) {
			Badguy enemy = (Badguy) badguys.get(i);
			enemy.setlastHit(false);
			badguys.set(i, enemy);
		}
	}
	
}
