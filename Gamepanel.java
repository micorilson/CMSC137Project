import java.awt.Graphics;
import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionAdapter;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;

import javax.swing.JPanel;
import javax.swing.*;

import javax.imageio.ImageIO;
import java.io.File;
import java.awt.Graphics2D;

import java.lang.String;

/**
 * @author 	Frances Lei N. Vargas
 Mico Rilson D. Lacson
Seth Charles Palileo
Jhon Louie Ballad
 *
 */

public class Gamepanel extends JPanel implements Runnable, Constants{
	int x=0,y=0,xspeed=30,yspeed=30,prevX,prevY;
	Thread t=new Thread(this);
	JFrame frame;
	JPanel container = new JPanel();
	String name;
	String pname;
	String server="localhost";
	boolean connected=false;
	DatagramSocket socket = new DatagramSocket();
	String serverData;

	BufferedImage offscreen;
	BufferedImage floor;
	BufferedImage brick;


	int [][]map ={ 	{0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0},
					{0, 1, 1, 1, 1, 1, 1, 1, 2, 1, 1, 0, 1, 1, 2, 1, 1, 1, 1, 2, 1, 1, 0},
					{0, 1, 1, 1, 1, 1, 2, 2, 1, 1, 1, 0, 1, 2, 2, 1, 1, 1, 2, 1, 1, 1, 0},
					{0, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 0, 1, 1, 1, 1, 2, 2, 2, 1, 1, 1, 0},
					{0, 1, 1, 2, 2, 2, 1, 2, 2, 1, 1, 0, 1, 1, 1, 1, 2, 1, 1, 1, 1, 1, 0},
					{0, 1, 1, 1, 1, 2, 1, 1, 2, 1, 1, 0, 1, 1, 1, 1, 2, 1, 1, 1, 1, 1, 0},
					{0, 1, 2, 1, 1, 2, 1, 1, 1, 1, 1, 0, 1, 2, 2, 1, 1, 1, 2, 1, 1, 1, 0},
					{0, 1, 2, 1, 1, 2, 1, 1, 1, 1, 1, 0, 2, 2, 1, 1, 1, 1, 2, 1, 1, 1, 0},
					{0, 1, 2, 1, 1, 1, 1, 2, 1, 1, 1, 0, 2, 2, 1, 2, 1, 1, 2, 1, 1, 1, 0},
					{0, 1, 1, 1, 1, 1, 1, 2, 1, 1, 1, 0, 1, 2, 2, 2, 1, 1, 2, 1, 1, 1, 0},
					{0, 1, 1, 1, 2, 2, 1, 2, 1, 1, 1, 0, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 0},
					{0, 1, 2, 1, 2, 2, 1, 1, 1, 1, 1, 0, 2, 1, 2, 1, 1, 1, 1, 1, 1, 1, 0},
					{0, 1, 2, 1, 1, 1, 1, 2, 2, 1, 1, 0, 1, 1, 2, 2, 1, 1, 1, 1, 1, 1, 0},
					{0, 1, 2, 1, 1, 1, 1, 1, 2, 1, 1, 0, 1, 1, 2, 2, 1, 1, 1, 1, 1, 1, 0},
					{0, 1, 2, 1, 1, 1, 1, 2, 2, 1, 1, 0, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 0},
					{0, 1, 1, 1, 2, 2, 1, 1, 1, 1, 1, 0, 1, 2, 2, 2, 2, 1, 1, 1, 1, 1, 0},
					{0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0}
	};

	long lastMove = System.currentTimeMillis();
	final long threshold = 500; // 500msec = half second
	public String team;

	public Gamepanel(String server,String name,String team, JFrame frame) throws Exception{
		this.server=server;
		this.name=name;
		this.team=team;
		this.frame = frame;
		if(team.equals("b")){
			this.x = 450;
		}
		InetAddress address = InetAddress.getByName(server);

		socket.setSoTimeout(100);

		container.setLayout(new BorderLayout());
		container.add(this, BorderLayout.CENTER);

		frame.setLayout(new BorderLayout());
		frame.getContentPane().add(this);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setSize(690, 510);
		frame.setVisible(true);

		offscreen=(BufferedImage)this.createImage(690, 510);

		this.addKeyListener(new KeyHandler());
		this.addMouseMotionListener(new MouseMotionHandler());
		this.setFocusable(true);
		this.requestFocus();
	}

	public void send(String msg){
		try{
			byte[] buf = msg.getBytes();
        	InetAddress address = InetAddress.getByName(server);
        	DatagramPacket packet = new DatagramPacket(buf, buf.length, address, PORT);
        	socket.send(packet);
        }catch(Exception e){}

	}

	public JPanel getContainer(){
		return this.container;
	}

	public void run(){
		while(true){
			try{
				Thread.sleep(1);
			}catch(Exception ioe){}

			byte[] buf = new byte[256];
			DatagramPacket packet = new DatagramPacket(buf, buf.length);
			try{
     			socket.receive(packet);
			}catch(Exception ioe){

			}

			serverData=new String(buf);
			serverData=serverData.trim();

			if (!connected && serverData.startsWith("CONNECTED")){
				connected=true;
				System.out.println("Connected.");
			}else if (!connected){
				System.out.println("Connecting..");
				send("CONNECT "+name);
			}else if (connected){
				repaint();
			}
		}
	}

	public void paintComponent(Graphics g){
		super.paintComponent(g);

		int xp=0,yp=0;
		g.drawImage(offscreen, 30, 30, null);
		try{
			floor = ImageIO.read(new File("Floor.PNG"));
			brick = ImageIO.read(new File("Wood.PNG"));
		}catch(IOException e){
			System.out.println("Cannot find all images");
		}

		for(int i=0;i<17;i++){
			for(int j=0;j<23;j++){
				xp=(30*j);
				yp=(30*i);

				if(this.map[i][j]==0){
					g.drawImage(floor, xp, yp, null);
				} else if(this.map[i][j]==2){
					g.drawImage(brick, xp, yp, null);
				}
			}
		}
		if (serverData.startsWith("PLAYER")){
			String[] playersInfo = serverData.split(":");
			offscreen.getGraphics().clearRect(0, 0, 630, 450);
			for (int i=0;i<playersInfo.length;i++){

				String[] playerInfo = playersInfo[i].split(" ");
				String pname =playerInfo[1];
				int x = Integer.parseInt(playerInfo[2]);
				int y = Integer.parseInt(playerInfo[3]);

				offscreen.getGraphics().fillOval(x, y, 30, 30);
				offscreen.getGraphics().drawString(pname,x,y+40);

			}
		}
	}

	class MouseMotionHandler extends MouseMotionAdapter{
		public void mouseMoved(MouseEvent me){
			correctFocus();
		}
	}

	public void correctFocus(){
		this.setFocusable(true);
		this.requestFocus();
	}
	
	class KeyHandler extends KeyAdapter{
		public void keyPressed(KeyEvent ke){
			prevX=x;
			prevY=y;

			if(ke.getKeyCode() == KeyEvent.VK_DOWN){
				if(map[(y+60)/30][(x+30)/30] == 1){
					long now = System.currentTimeMillis();
     				if (now - lastMove > threshold)
     				{
     					lastMove = now;
     					y+=yspeed;
     				}
				}
			}

			if(ke.getKeyCode() == KeyEvent.VK_UP){
				if(map[y/30][(x+30)/30] == 1){
					long now = System.currentTimeMillis();
     				if (now - lastMove > threshold)
     				{
     					lastMove = now;
						y-=yspeed;
					}
				}
			}

			if(ke.getKeyCode() == KeyEvent.VK_LEFT){
				if(map[(y+30)/30][x/30] == 1){
					long now = System.currentTimeMillis();
     				if (now - lastMove > threshold)
     				{
     					lastMove = now;
						x-=xspeed;
					}
				}
			}

			if(ke.getKeyCode() == KeyEvent.VK_RIGHT){
				if(map[(y+30)/30][(x+60)/30] == 1){
					long now = System.currentTimeMillis();
     				if (now - lastMove > threshold)
     				{
     					lastMove = now;
						x+=xspeed;
					}
				}
			}

			if (prevX != x || prevY != y){
				send("PLAYER "+name+" "+x+" "+y);
			}
		}
	}

}
