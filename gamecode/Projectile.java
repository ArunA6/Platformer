package gamecode;

import java.awt.Image;
import java.awt.Toolkit;

public class Projectile {

	private double xCoord = 0;
	private double yCoord = 0;
	private int width = 10;
	private int height = 10;
	private Image img;
	private String direction = "";
	private int damage = 1;
	private int xDis = 0;
	private int yDis = 0;
	private double angle = 0;

	public Projectile() {
		setxCoord(10);
		setyCoord(10);
		setWidth(30);
		setHeight(30);
		setImg("../files/fireball.png");
		
	}
	
	public double getAngle() {
		return angle;
	}

	public void setAngle(double angle) {
		this.angle = angle;
	}
	
	public int getxDis() {
		return xDis;
	}

	public void setxDis(int xDis) {
		this.xDis = xDis;
	}
	
	public int getyDis() {
		return yDis;
	}

	public void setyDis(int yDis) {
		this.yDis = yDis;
	}
	
	public int getDamage() {
		return damage;
	}

	public void setDamage(int damage) {
		this.damage = damage;
	}
	
	public double getxCoord() {
		return xCoord;
	}

	public void setxCoord(double xCoord) {
		this.xCoord = xCoord;
	}

	public double getyCoord() {
		return yCoord;
	}

	public void setyCoord(double yCoord) {
		this.yCoord = yCoord;
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
	
	public String getDirection() {
		return direction;
	}
	
	public void setDirection(String direction) {
		this.direction = direction;
	}

	public Image getImg() {
		return img;
	}
	
	public void setImg(Image img) {
		this.img = img;
	}

	public void setImg(String imgpath) {
		this.img = Toolkit.getDefaultToolkit().getImage(imgpath);
	}

	public Projectile(int x, int y, int w, int h, String imgpath) {
		setxCoord(x);
		setyCoord(y);
		setWidth(w);
		setHeight(h);
		setImg(imgpath);
	}
	
	public Projectile(int x, int y, int w, int h, String imgpath, int damage) {
		setxCoord(x);
		setyCoord(y);
		setWidth(w);
		setHeight(h);
		setImg(imgpath);
		setDamage(damage);
	}
	
	public Projectile(int x, int y, int w, int h, String imgpath, int xDis, int yDis) {
		setxCoord(x);
		setyCoord(y);
		setWidth(w);
		setHeight(h);
		setImg(imgpath);
		setxDis(xDis);
		setyDis(yDis);
		setAngle(Math.atan2(yDis, xDis));
	}
	
	public void bossMove() {
		double x = getxCoord();
		double y = getyCoord();
		x = x + 3 * Math.cos(angle);
		y = y + 3 * Math.sin(angle);
		setxCoord(x);
		setyCoord(y);
	}
	
}