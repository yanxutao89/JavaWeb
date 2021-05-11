package chp5.metamech.vorpal;

import java.io.*;
import java.net.*;

import junit.framework.TestCase;

import com.metamech.jabber.xml.*;

public class ServerTest extends TestCase {
  
  public ServerTest(String name){
    super(name);
  }
  
  public void testNothing(){
  }
    /*

  final static String SERVER_NAME="127.0.0.1";
  final static int PORT=5222;
  
  public void setUp(){
    try {
      ServerThread thread = new ServerThread();
      thread.start();
      Thread.sleep(500); // let server start and create socket
    } catch (Exception ex){
      fail(ex.getMessage());
    }
  }
  
  
  Reader reader;
  PacketQueue packetQueue = new PacketQueue();
  
  public void testStream(){
    try {
      Socket sock[] = {new Socket(SERVER_NAME,PORT),new Socket(SERVER_NAME,PORT)};
      Writer writer[] = new Writer[2];
      Reader reader[] = new Reader[2];
  
      for (int i = 0; i < 2; i++){
        writer[i] = new BufferedWriter(new OutputStreamWriter(sock[i].getOutputStream()));
        reader[i] = new BufferedReader(new InputStreamReader(sock[i].getInputStream()));
        writer[i].write("<stream:stream to='");
        writer[i].write(SERVER_NAME);
        writer[i].write("' xmlns='jabber:client' xmlns:stream='http://etherx.jabber.com/streams'> ");
        writer[i].flush();
      }
      
      // Grab the server response
      for (int i = 0; i < 2; i++){
        StringBuffer response = new StringBuffer();
        int b;
        while ((b=reader[i].read()) != '>'){
          response.append((char)b);
        }
        response.append((char)b);
      
        // Run some rudimentary checks
        String fragment = response.toString().trim();
        assertTrue("Should start with <stream:stream",fragment.startsWith("<stream:stream"));
        assertTrue("Should end with >",fragment.endsWith(">"));
        assertTrue("Should have a server of SERVER_NAME",
                  fragment.indexOf("from=\""+SERVER_NAME+"\"") != -1 ||
                  fragment.indexOf("from='"+SERVER_NAME+"'") != -1);
        assertTrue("Should set default namespace to jabber:client",
                  fragment.indexOf("xmlns=\"jabber:client\"") != -1 ||
                  fragment.indexOf("xmlns='jabber:client'") != -1);
        assertTrue("Should set stream namespace to http://etherx.jabber.com/streams",
                  fragment.indexOf("xmlns:stream=\"http://etherx.jabber.com/streams\"") != -1 ||
                  fragment.indexOf("xmlns:stream='http://etherx.jabber.com/streams'") != -1);
      }
      
      // End the stream
      for (int i = 0; i < 2; i++){
        writer[i].write("</stream:stream> ");
        writer[i].flush();
      }
      
      // Grab the server response
      for (int i = 0; i < 2; i++){
        StringBuffer response = new StringBuffer();
        int b;
        while ((b=reader[i].read()) != -1){
          response.append((char)b);
        }
      
        // More rudimentary checks
        String fragment = response.toString().trim();
        assertTrue("Should start with </stream:stream",fragment.startsWith("</stream:stream"));
        assertTrue("Should end with >",fragment.endsWith(">"));
      }

    } catch (Exception ex){
      ex.printStackTrace();
      fail("Exception occured: " + ex.getMessage());
    }
  }
  
  class ServerThread extends Thread{
    public void run(){
      Server server = new Server();
      String [] args = {""};
      server.main(args);
    }
  }
  */
}
