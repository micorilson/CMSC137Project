import java.net.*;
import java.io.*;
import java.util.*;

public class ChatServer{
    private static ServerSocket serverSocket = null;
    private static Socket client = null;
    private static final int maxclient = 100;
    private static final clientThread[] threads = new clientThread[maxclient];

    public static void main(String [] args){
        int port = Integer.parseInt(args[0]);

        try {
          serverSocket = new ServerSocket(port);
        } catch (IOException e) {
          System.out.println(e);
        }
        
        while (true) {
          try {
            client = serverSocket.accept();

            for (int i = 0; i < maxclient; i++) {
              if (threads[i] == null) {
                (threads[i] = new clientThread(client,threads)).start();
                break;
              }
            }
          } catch (IOException e) {
            System.out.println(e);
          }
        }
    }
}

    class clientThread extends Thread {
        private DataInputStream in = null;
        private DataOutputStream out = null;
        private Socket client = null;
        private final clientThread[] threads;
        private int maxclient;

        public clientThread(Socket client, clientThread[] threads) {
            this.client = client;
            this.threads = threads;
            maxclient = threads.length;
        }
        public void run() {
            int maxclient = this.maxclient;
            clientThread[] threads = this.threads;

            try{
                in = new DataInputStream(client.getInputStream());
                out = new DataOutputStream(client.getOutputStream());

                while(true){
                    String line = in.readUTF();

                    for (int i = 0; i < maxclient; i++) {
                        if (threads[i] != null) {
                            threads[i].out.writeUTF(line);
                        }
                    }
                }
            }catch (IOException e) {
            
            }finally{
                try{
                    in.close();
                    out.close();
                    client.close();
                }catch(IOException e){

                }
            }
        }
    } 

