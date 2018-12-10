package gamecode;

import java.awt.Color;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;

public class MyScreen extends JFrame{

	public MyScreen() {
		this.setSize(1200, 800);
		this.setDefaultCloseOperation(EXIT_ON_CLOSE);
		this.setVisible(true);
	}
	
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		MyScreen screen = new MyScreen();
		MyCanvas canvas = new MyCanvas();
		screen.getContentPane().add(canvas);
		
		/*JPanel jp = new JPanel();
		jp.setBounds(525, 10, 150, 75);
		
		JLabel jl = new JLabel("SCORE!!!");
		jp.add(jl);
		
		screen.getContentPane().add(jp);*/
	}
}