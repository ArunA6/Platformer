package gamecode;

import java.awt.Image;
import java.awt.Toolkit;

public class Goodguy {

	private int xCoord = 0;
	private int yCoord = 0;
	private int speedX = 30;
	private int width = 10;
	private int height = 10;
	private String dir = "mid";
	private int way = 0;
	private Image img;
	private int health = 3;
	private int startHealth;

	public Goodguy() {
		setxCoord(10);
		setyCoord(10);
		setWidth(30);
		setHeight(30);
		setImg("../files/superhero.png");
		
	}
	
	public int getstartHealth() {
		return startHealth;
	}

	public void setstartHealth(int starttHealth) {
		this.startHealth = health;
	}
	
	public int getxCoord() {
		return xCoord;
	}

	public void setxCoord(int xCoord) {
		this.xCoord = xCoord;
	}

	public int getyCoord() {
		return yCoord;
	}

	public void setyCoord(int yCoord) {
		this.yCoord = yCoord;
	}
	
	public int getspeedX() {
		return speedX;
	}

	public void setspeedX(int speedX) {
		this.speedX = speedX;
	}

	public int getWidth() {
		return width;
	}

	public void setWidth(int width) {
		this.width = width;
	}

	public int getHeight() {
		return height;
	}

	public void setHeight(int height) {
		this.height = height;
	}
	
	public int getHealth() {
		return health;
	}

	public void setHealth(int health) {
		this.health = health;
	}
	
	public String getDir() {
		return dir;
	}

	public void setDir(String direction) {
		this.dir = direction;
	}
	
	public int getWay() {
		return way;
	}

	public void setWay(int direction) {
		this.way = direction;
	}

	public Image getImg() {
		return img;
	}

	public void setImg(Image img) {
		this.img = img;
	}

	public Goodguy(int x, int y, int w, int h, String imgpath) {
		setxCoord(x);
		setyCoord(y);
		setWidth(w);
		setHeight(h);
		setImg(imgpath);
	}
	
	public void moveIt(int direction, int w, int h) {
		int x = getxCoord();
		int y = getyCoord();
		if (direction==39) {
			if (x+speedX < w) {
				x = x + speedX;
				setxCoord(x);
				if (this.getDir()!="right") {
					setImg("files/right1.png");
					setWay(0);
				}
				setWay(this.getWay()+1);
				if (this.getWay()<=1) {
				}
				else if (this.getWay()<=3) {
					setImg("files/right2.png");
				}
				else if (this.getWay()<=5) {
					setImg("files/right3.png");
				}
				/*else if (this.getWay()<=7) {
					setImg("files/right4.png");
				}*/
				else if (this.getWay()==7) {
					setImg("files/right5.png");
				}
				else if (this.getWay()<=9) {
					setImg("files/right6.png");
				}
				else if (this.getWay()<=11) {
					setImg("files/right7.png");
					setWay(0);
				}
				/*else if (this.getWay()==13) {
					setImg("files/right8.png");
					setWay(0);
				}*/
				this.setDir("right");
				setWidth(90);
				setHeight(90);
			}
		}
		else if (direction==37) {
			if (x-speedX > 0) {
				x = x-speedX;
				setxCoord(x);
				if (this.getDir()!="left") {
					setImg("files/left1.png");
					setWay(0);
				}
				setWay(this.getWay()+1);
				if (this.getWay()<=1) {
				}
				else if (this.getWay()<=3) {
					setImg("files/left2.png");
				}
				else if (this.getWay()<=5) {
					setImg("files/left3.png");
				}
				/*else if (this.getWay()<=7) {
					setImg("files/left4.png");
				}*/
				else if (this.getWay()==7) {
					setImg("files/left5.png");
				}
				else if (this.getWay()<=9) {
					setImg("files/left6.png");
				}
				else if (this.getWay()<=11) {
					setImg("files/left7.png");
					setWay(0);
				}
				/*else if (this.getWay()==13) {
					setImg("files/left8.png");
					setWay(0);
				}*/
				this.setDir("left");
				setWidth(90);
				setHeight(90);
			}
		}
	}
	
	public void moveSuper(int direction, int w, int h) {
		int x = getxCoord();
		int y = getyCoord();
		if (direction==39) {
			if (x+speedX*2 < w) {
				x = x + speedX*2;
				setxCoord(x);
				setImg("files/super_fly.png");
				this.setDir("right");
				setWidth(90);
				setHeight(90);
			} else if (x+speedX < w) {
				x = x + speedX;
				setxCoord(x);
				setImg("files/super_fly.png");
				this.setDir("right");
				setWidth(90);
				setHeight(90);
			}
		}
		else if (direction==37) {
			if (x-speedX*2 > 0) {
				x = x - speedX*2;
				setxCoord(x);
				setImg("files/super_fly2.png");
				this.setDir("left");
				setWidth(90);
				setHeight(90);
			} else if (x-speedX > 0) {
				x = x - speedX;
				setxCoord(x);
				setImg("files/super_fly2.png");
				this.setDir("left");
				setWidth(90);
				setHeight(90);
			}
		}
	}
	
	public void moveKn(int direction, int w, int h, String pre) {
		int x = getxCoord();
		int y = getyCoord();
		if (direction==39) {
			if (x+speedX < w) {
				x = x + speedX;
				setWay(getWay()+1);
				if (getWay()>4 || !this.getDir().equals("right")) {
					setWay(1);
				}
				setImg("files/" + pre + "right.gif");
				setxCoord(x);
				this.setDir("right");
				setWidth(90);
				setHeight(90);
			}
		}
		else if (direction==37) {
			if (x-speedX > 0) {
				x = x - speedX;
				setWay(getWay()+1);
				if (getWay()>4 || !this.getDir().equals("right")) {
					setWay(1);
				}
				setImg("files/" + pre + "left.gif");
				setxCoord(x);
				this.setDir("left");
				setWidth(90);
				setHeight(90);
			}
		}
	}
	
	public void airSuper(int direction, int w, int h) {
		int x = getxCoord();
		int y = getyCoord();
		if (direction==39) {
			if (x+speedX*3/2 < w) {
				x = x + speedX*3/2;
				setxCoord(x);
				this.setDir("right");
				setWidth(90);
				setHeight(90);
			}
		}
		else if (direction==37) {
			if (x-speedX*3/2 > 0) {
				x = x - speedX*3/2;
				setxCoord(x);
				this.setDir("left");
				setWidth(90);
				setHeight(90);
			}
		}
	}
	
	public void airKn(int direction, int w, int h) {
		int x = getxCoord();
		int y = getyCoord();
		if (direction==39) {
			if (x+speedX/2 < w) {
				x = x + speedX/2;
				setxCoord(x);
				this.setDir("right");
				setWidth(90);
				setHeight(90);
			}
		}
		else if (direction==37) {
			if (x-speedX/2 > 0) {
				x = x - speedX/2;
				setxCoord(x);
				this.setDir("left");
				setWidth(90);
				setHeight(90);
			}
		}
	}
	
	public void moveAir(int direction, int w, int h) {
		//int speedX = 10;
		int x = getxCoord();
		if (direction==39) {
			if (x+speedX/2 < w) {
				x = x + speedX/2;
				setxCoord(x);
				setDir("right");
				setWidth(90);
				setHeight(90);
			}
		}
		else if (direction==37) {
			if (x-speedX/2 > 0) {
				x = x - speedX/2;
				setxCoord(x);
				if (this.getDir()!="left") {
					setImg("files/left1.png");
					setWay(0);
				}
				setWay(this.getWay()+1);
				if (this.getWay()<=1) {
					setImg("files/left1.png");
				}
				else if (this.getWay()<=2) {
					setImg("files/left2.png");
				}
				else if (this.getWay()<=5) {
					setImg("files/left3.png");
				}
				/*else if (this.getWay()<=7) {
					setImg("files/left4.png");
				}*/
				else if (this.getWay()==7) {
					setImg("files/left5.png");
				}
				else if (this.getWay()<=9) {
					setImg("files/left6.png");
				}
				else if (this.getWay()<=11) {
					setImg("files/left7.png");
					setWay(0);
				}
				/*else if (this.getWay()==13) {
					setImg("files/left8.png");
					setWay(0);
				}*/
				setDir("left");
				setWidth(90);
				setHeight(90);
			}
		}
	}
	
	public void jumpIt(int position) {
		int x = this.getxCoord();
		int y = this.getyCoord();
		if (position<4) {
			y -= 16*2;
		}
		else if (position<7) {
			y -= 14*2;
		}
		else if (position == 7) {
			y -= 12*2;
		}
		else if (position == 8) {
			y -= 10*2;
		}
		else if (position == 9) {
			y -= 8*2;
		}
		else if (position == 10) {
			y -= 5*2;
		}
		setyCoord(y);
	}
	
	public void fallIt(int position) {
		int x = this.getxCoord();
		int y = this.getyCoord();
		if (position == 1) {
			y += 5*2;
		}
		else if (position == 2) {
			y += 8*2;
		}
		else if (position == 3) {
			y += 10*2;
		}
		else if (position == 4) {
			y += 12*2;
		}
		else if (position < 8) {
			y += 14*2;
		}
		else if (position < 11) {
			y += 16*2;
		}
		if(y>600) {
			y = 600;
		}
		setyCoord(y);
	}
	public void rollIt() {
		int rollSpeed = 50;
		if (getDir().equals("right")) {
			setxCoord(getxCoord()+rollSpeed);
		}
		else {
			setxCoord(getxCoord()-rollSpeed);
		}
	}
	public void rollSuper() {
		int rollSpeed = 70;
		if (getDir().equals("right")) {
			setxCoord(getxCoord()+rollSpeed);
		}
		else {
			setxCoord(getxCoord()-rollSpeed);
		}
	}
	public void rollKn() {
		int rollSpeed = 50;
		if (getDir().equals("right")) {
			setxCoord(getxCoord()+rollSpeed);
		}
		else {
			setxCoord(getxCoord()-rollSpeed);
		}
	}
	
	public void setImg(String imgpath) {
		this.img = Toolkit.getDefaultToolkit().getImage(imgpath);
	}
	
}
