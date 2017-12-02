import java.awt.*;
import javax.swing.JPanel;
import java.awt.event.*;
import java.awt.image.*;
 import java.util.Random; 
public class GamePanel extends JPanel implements Runnable {

	private Thread thread;
	private TileMap tilemap;
	private boolean running;
	public static final int HEIGHT = 670;
	public static final int WIDTH = 990;
	private int FPS = 30;
	private int targetTime = 1000/FPS;
	private Graphics2D g;
	private BufferedImage img;



	public GamePanel(){ // constructor for game panel
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
	private void draw(){ // used for putting the graphics
		Graphics g2 = getGraphics();

		g2.drawImage(img,0,0,null);
		g2.dispose();
	}
	private void init(){ // initialization
		Random rand = new Random(); 
 		int pickedNumber = rand.nextInt(40) + 1; 
		System.out.println(pickedNumber);

		running = true;
		img = new BufferedImage(WIDTH, HEIGHT, BufferedImage.TYPE_INT_RGB);
		g = (Graphics2D) img.getGraphics();

		if (pickedNumber % 5 == 0){		
		tilemap = new TileMap("testmap.txt",32);
		tilemap.loadTiles("tileset.png");
		}
		if(pickedNumber % 2 != 0 && pickedNumber % 5 != 0){
		tilemap = new TileMap("testmap2.txt",32);
		tilemap.loadTiles("map.png");		
		}
		
		if(pickedNumber % 2 == 0 && pickedNumber % 5 != 0){
		tilemap = new TileMap("testmap3.txt",32);
		tilemap.loadTiles("map3.png");
		//System.out.println("mod 5");
				
		}
		
		
	}

	private void render(){ // show the tile
		tilemap.draw(g);
	}
	private void update(){ // update
		tilemap.update();

	}


}
