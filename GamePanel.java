import java.awt.*;
import javax.swing.JPanel;
import java.awt.event.*;
import java.awt.image.*;

public class GamePanel extends JPanel implements Runnable {

	private Thread thread;
	private TileMap tilemap;
	private boolean running;
	public static final int HEIGHT = 480;
	public static final int WIDTH = 640;
	private int FPS = 30;
	private int targetTime = 1000/FPS;
	private Graphics2D g;
	private BufferedImage img;



	public GamePanel(){
		super();
		setFocusable(true);
		setPreferredSize(new Dimension(WIDTH,HEIGHT));
		requestFocus();
	}
	public void run(){
		init();
		long waitingTime;
		long startingTime;
		long urdTime;


		while(running){

				startingTime = System.nanoTime();
				update();
				render();
				draw();
				urdTime = (System.nanoTime() - startingTime)/ 1000000;
				waitingTime = targetTime - urdTime;

				try{
					Thread.sleep(waitingTime);
				}catch(Exception e){

				}


		}
	}
	public void addNotify(){
		super.addNotify();
		if(thread == null){
			thread = new Thread(this);
			thread.start();
		}
	}
	private void draw(){
		Graphics g2 = getGraphics();

		g2.drawImage(img,0,0,null);
		g2.dispose();
	}
	private void init(){

		running = true;
		img = new BufferedImage(WIDTH, HEIGHT, BufferedImage.TYPE_INT_RGB);
		g = (Graphics2D) img.getGraphics();

		tilemap = new TileMap("testmap.txt",32);
		tilemap.loadTiles("tileset.png");
	}

	private void render(){
		tilemap.draw(g);
	}
	private void update(){
		tilemap.update();

	}


}
