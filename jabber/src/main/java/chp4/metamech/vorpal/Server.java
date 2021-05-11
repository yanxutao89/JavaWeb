package chp4.metamech.vorpal;

import java.io.IOException;
import java.net.Socket;
import java.net.ServerSocket;

import chp4.metamech.jabber.Session;
import chp4.metamech.jabber.xml.*;

/**
The main Jabber server class.

We listen for a connection, open a Jabber XML stream, watch for their stream,
then close down the connection.

@author Iain Shigeoka (iainshigeoka@yahoo.com)
*/
public class Server {

  final static public int    JABBER_PORT = 5222;
  final static public String SERVER_NAME = "shigeoka.com";

  // Create the global data structures everyone works with
  PacketQueue packetQueue = new PacketQueue();
  SessionIndex index = new SessionIndex();

  protected Server(){

    createQueueThread();

    ServerSocket serverSocket;

    try {
      // Create the server socket to listen to
      serverSocket = new ServerSocket(JABBER_PORT);
    } catch (IOException ex){
      // This is likely an very bad problem...
      // ... probably can't use the port...
      // ... report the problem and quit
      ex.printStackTrace();
      return;
    }

    // We accept incoming sockets and start new threads forever
    while (true){
      try {
        // Wait for the connection
        Socket newSock = serverSocket.accept();
        Session session = new Session(newSock);

        // Spin off a new thread to handle the connection
        ProcessThread processor = new ProcessThread(packetQueue,session);
        processor.start();
      } catch (IOException ie){
        ie.printStackTrace();
      }
    }
  }

  void createQueueThread(){
      // Create the Queue worker thread to process packets in the packet queue
    QueueThread qThread = new QueueThread(packetQueue);
    qThread.setDaemon(true);
    qThread.addListener(new OpenStreamHandler(index),"stream:stream");
    qThread.addListener(new CloseStreamHandler(index),"/stream:stream");
    qThread.addListener(new DeliveryHandler(index),"");
    qThread.start();
  }

  static public void main(String [] args){
    System.out.println("Jabber Server -- " + SERVER_NAME + ":"+ JABBER_PORT);
    new chp4.metamech.vorpal.Server();
  }
}
