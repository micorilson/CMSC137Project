import java.net.*; 
import java.util.*;

public class Server {	
 
	private static HashMap<Integer,InetAddress> portSet = new HashMap<Integer,InetAddress>();
 
	public static void main(String args[]) throws Exception {
 
       	int portNum = 8888;
       	switch(args.length){
       		case 1:
       			portNum = Integer.parseInt(args[0]);
       		case 0:
       			break;
       		default:
       			System.out.println("Usage is: > java Server [portNumber]");
       	}
 		DatagramSocket socket = new DatagramSocket(portNum);        
	    System.out.println("Server running...");
 
	    while(true){
			byte[] buf = new byte[1024];          
 			DatagramPacket packet = new DatagramPacket(buf, buf.length);
	 		socket.receive(packet);           
 
			String message = (new String(packet.getData())).trim();

			InetAddress address = packet.getAddress();           
			portNum = packet.getPort();
			if(!portSet.containsKey(portNum)){
				portSet.put(portNum,address);
			}
 			
			String returnMessage = message;          
			System.out.println(returnMessage);

			buf = returnMessage.getBytes();
			
			for(Integer port : portSet.keySet()) 
			{
				if(port != portNum) 
				{
					packet = new DatagramPacket(buf, buf.length, portSet.get(port), port); 
					socket.send(packet);    
				}
			}
        }
    }
}

//Reference: https://www.daniweb.com/programming/software-development/threads/392710/basic-udp-chat-system