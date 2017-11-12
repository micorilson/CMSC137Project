import java.io.*;  
import java.net.*; 
 
public class Client {
 
    public static void main(String args[]) throws Exception {  
 
        int portNum = 8888;
        InetAddress inetadd = null;
        String username = "Anonymous";
        switch(args.length){
            case 3:
                username = args[2];
            case 2:
                portNum = Integer.parseInt(args[1]);
            case 1:
                inetadd = InetAddress.getByName(args[0]);
            case 0:
                break;
            default:
                System.out.println("Usage is: > java Client [Address] [portNumber] [username]");
        }

        SenderThread sender = new SenderThread(username, inetadd, portNum);
        sender.start();
        ReceiverThread receiver = new ReceiverThread(sender.getSocket());
        receiver.start();
    }
}      
 
class SenderThread extends Thread {
 
    private InetAddress serverAddress;
    private DatagramSocket clientSocket;
    private int portNum;
    private String username;
 
    public SenderThread(String uname, InetAddress address, int serverport) throws SocketException {
        this.username = uname;
        this.serverAddress = address;
        this.portNum = serverport;

        this.clientSocket = new DatagramSocket();
        this.clientSocket.connect(serverAddress, portNum);
    }
 
    public DatagramSocket getSocket() {
        return this.clientSocket;
    }

    public void run() {       
        try {
            BufferedReader input = new BufferedReader(new InputStreamReader(System.in));
            while(true){
                String message = username + ": " + input.readLine();
                if (message.equals(username + ": " + "LOGOUT")){
                    System.out.println("Goodbye");
                    break;
                }else if(message.equals(username + ": " + "")){
                    continue;
                }
 
                byte[] buf = new byte[1024];
                buf = message.getBytes();
                DatagramPacket packet = new DatagramPacket(buf, buf.length, serverAddress, portNum);
                System.out.println(message);
                clientSocket.send(packet);
                Thread.yield();
            }
        }
        catch (IOException e) {
            System.err.println(e);
        }
    }
}   
 
class ReceiverThread extends Thread {
 
    private DatagramSocket clientSocket;
 
    public ReceiverThread(DatagramSocket socket) throws SocketException {
        this.clientSocket = socket;
    }
 
    public void run() {
        byte[] buf = new byte[1024];
        while(true){            
            DatagramPacket packet = new DatagramPacket(buf, buf.length);
            try {
                clientSocket.receive(packet);
                String message =  new String(packet.getData(), 0, packet.getLength());
                System.out.println(message);
                Thread.yield();
            } 
            catch (IOException e) {
            System.err.println(e);
            }
        }
    }
}

//Reference: https://www.daniweb.com/programming/software-development/threads/392710/basic-udp-chat-system