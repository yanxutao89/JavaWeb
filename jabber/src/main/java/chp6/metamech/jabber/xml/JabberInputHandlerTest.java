package chp6.metamech.jabber.xml;

import junit.framework.TestCase;

/**
This XML test demonstrates an ability to load an XML document and extract
information, as well as open, read and close an XML stream.

@author Iain Shigeoka (iainshigeoka@yahoo.com)
*/
public class JabberInputHandlerTest extends TestCase {

  public JabberInputHandlerTest(String name){
    super(name);
  }
  
  public void testNothing(){
  }
/*
  PacketQueue packetQueue;
  JabberInputHandler handler;
  public void setUp(){
    packetQueue = new PacketQueue();
    handler = new JabberInputHandler(packetQueue);
  }
*/
  /**
  Loads an XML document containing tags for username and password.
  We will use this later to store and retrieve configuration information
  for the server and client.  The test itself is simple, open a file,
  create a parser, parse the file, and extract information.  We will
  use the SAX interface since it lends itself to streaming information used
  in the Jabber protocols.

  Depends on reading the UserConfigTest.xml data file.
  */
/*
  public void testC2SRead(){
    try {

      handler.process(new FileSession("data/c2sStreamTest.xml"));

      Packet packet = packetQueue.pull();
      assertTrue("The first task should be an open stream packet but is: " + packet,
                 packet instanceof OpenStreamPacket);
      assertNull("The open stream packet should have no id but has: " +
                 packet.getSession().getStreamID(),
                 packet.getSession().getStreamID());

      packet = packetQueue.pull();
      assertTrue("The second task should be an iq packet instead found " + packet,
                 packet instanceof IQPacket);

      packet = packetQueue.pull();
      assertTrue("The third task should be an iq packet instead found " + packet,
                 packet instanceof IQPacket);

      packet = packetQueue.pull();
      assertTrue("The fourth task should be a close stream packet",
                 packet instanceof CloseStreamPacket);
    } catch (Exception ex){
      ex.printStackTrace();
      fail(ex.getMessage());
    }
  }

  public void testS2CRead(){
    try {
      // We can now test against the s2c stream
      handler.process(new FileSession("data/s2cStreamTest.xml"));

      Packet packet = packetQueue.pull();
      assertTrue("The first task should be an open stream packet but is: " + packet,
                 packet instanceof OpenStreamPacket);
      assertEquals("The open stream packet should have id 33ABA83 has id " +
                   packet.getSession().getStreamID(),
                   packet.getSession().getStreamID(),
                   "33ABA83");

      packet = packetQueue.pull();
      assertTrue("The second task should be an close stream packet",
                 packet instanceof CloseStreamPacket);
    } catch (Exception ex){
      ex.printStackTrace();
      fail(ex.getMessage());
    }
  }

  class FileSession extends Session{
    Reader fileReader;
    FileSession(String fileName) throws IOException {
      fileReader = new BufferedReader(new FileReader(fileName));
    }
    public Reader getReader(){
      return fileReader;
    }
  }

  public void testThreaded(){
    try {
      ThreadSession session = new ThreadSession();
      WriteThread writeThread = new WriteThread(session);
      ProcessThread processThread = new ProcessThread(session);

      processThread.start();
      writeThread.start();

      Packet packet = packetQueue.pull();
      assertTrue("The first task should be an open stream packet but is: " + packet,
                 packet instanceof OpenStreamPacket);
      assertNull("The open stream packet should have no id but has: " +
                 packet.getSession().getStreamID(),
                 packet.getSession().getStreamID());

      packet = packetQueue.pull();
      assertTrue("The second task should be an close stream packet",
                 packet instanceof CloseStreamPacket);
    } catch (Exception ex){
      ex.printStackTrace();
      fail(ex.getMessage());
    }
  }

  class ProcessThread extends Thread {
    Session session;
    public ProcessThread(Session session){
      this.session = session;
    }
    public void run(){
      try {
        handler.process(session);
      } catch (Exception ex){
        fail(ex.getMessage());
      }
    }
  }

  class WriteThread extends Thread {
    ThreadSession session;
    public WriteThread(ThreadSession session){
      this.session = session;
    }
    public void run(){
      try {
        PipedWriter out = (PipedWriter)session.getWriter();
        out.write("<stream:stream> ");
        out.flush();
        sleep(100);
        out.write("</stream:stream> ");
        out.flush();
        sleep(100);
        out.close();
      } catch (Exception ex){
        fail(ex.getMessage());
      }
    }
  }

  class ThreadSession extends Session{
    PipedReader reader;
    PipedWriter writer;
    ThreadSession() throws IOException {
      writer = new PipedWriter();
      reader = new PipedReader(writer);
    }
    public Reader getReader(){
      return reader;
    }
    public Writer getWriter(){
      return writer;
    }
  }
*/
}
