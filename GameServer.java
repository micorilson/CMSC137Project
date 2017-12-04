import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

/**
 * @author 	Frances Lei N. Vargas
 Mico Rilson D. Lacson
Seth Charles Palileo
Jhon Louie Ballad
 *
 */

public class GameServer implements Runnable, Constants{
	String playerData;
	int playerCount=0;
    DatagramSocket serverSocket = null;
	GameState game;
	int gameStage=WAITING_FOR_PLAYERS;
	int numPlayers;
	Thread t = new Thread(this);
	
	public GameServer(int numPlayers){
		this.numPlayers = numPlayers;
		try {
            serverSocket = new DatagramSocket(PORT);
			serverSocket.setSoTimeout(100);
		} catch (IOException e) {
            System.err.println("Could not listen on port: "+PORT);
            System.exit(-1);
		}catch(Exception e){}
		//Create the game state
		game = new GameState();

		System.out.println("Game created...");

		//Start the game thread
		t.start();
	}

	public void broadcast(String msg){
		for(Iterator ite=game.getPlayers().keySet().iterator();ite.hasNext();){
			String name=(String)ite.next();
			NetPlayer player=(NetPlayer)game.getPlayers().get(name);
			send(player,msg);
		}
	}

	public void send(NetPlayer player, String msg){
		DatagramPacket packet;
		byte buf[] = msg.getBytes();
		packet = new DatagramPacket(buf, buf.length, player.getAddress(),player.getPort());
		try{
			serverSocket.send(packet);
		}catch(IOException ioe){
			ioe.printStackTrace();
		}
	}

	public void run(){
		while(true){

			// Get the data from players
			byte[] buf = new byte[256];
			DatagramPacket packet = new DatagramPacket(buf, buf.length);
			try{
     			serverSocket.receive(packet);
			}catch(Exception ioe){}

			playerData=new String(buf);
			playerData = playerData.trim();

			switch(gameStage){
				  case WAITING_FOR_PLAYERS:
						if (playerData.startsWith("CONNECT")){
							String tokens[] = playerData.split(" ");
							NetPlayer player=new NetPlayer(tokens[1],packet.getAddress(),packet.getPort());
							System.out.println("Player connected: "+tokens[1]);
							game.update(tokens[1].trim(),player);
							broadcast("CONNECTED "+tokens[1]);
							playerCount++;
							if (playerCount==numPlayers){
								gameStage=GAME_START;
							}
						}
					  break;
				  case GAME_START:
					  System.out.println("Game State: START");
					  broadcast("START");
					  gameStage=IN_PROGRESS;
					  break;
				  case IN_PROGRESS:
					  if (playerData.startsWith("PLAYER")){
						  String[] playerInfo = playerData.split(" ");
						  String pname =playerInfo[1];
						  int x = Integer.parseInt(playerInfo[2].trim());
						  int y = Integer.parseInt(playerInfo[3].trim());
						  NetPlayer player=(NetPlayer)game.getPlayers().get(pname);
						  player.setX(x);
						  player.setY(y);
						  game.update(pname, player);
						  broadcast(game.toString());
					  }
					  break;
			}
		}
	}


	public static void main(String args[]){
		if (args.length != 2){
			System.out.println("Usage: java -jar circlewars-server <number of players> <port>");
			System.exit(1);
		}

		new GameServer(Integer.parseInt(args[0]));
		new ChatServer(args[1]);
	}
}
