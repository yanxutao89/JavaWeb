package chp3.metamech.jabber.xml;

import junit.framework.TestCase;

/**
This XML test demonstrates an ability to load an XML document and extract
information, as well as open, read and close an XML stream.

@author Iain Shigeoka (iainshigeoka@yahoo.com)
*/
public class PacketQueueTest extends TestCase {

  public PacketQueueTest(String name){
    super(name);
  }
  public void testNothing(){
  }
/*
  PacketQueue q = new PacketQueue();

  public void testQueue(){
    Packet osPacket = new OpenStreamPacket(null);
    Packet msPacket = new MessagePacket(null);
    synchronized (q){
      q.push(osPacket);
      q.push(msPacket);
      Packet packet = q.pull();
      assertTrue("First in was OpenStreamPacket",packet instanceof OpenStreamPacket);
      packet = q.pull();
      assertTrue("Second in was MessagePacket", packet instanceof MessagePacket);
    }
  }

  public void testThreads(){
    PacketThread pt = new PacketThread();
    pt.start();

    Packet activePacket = q.pull();
    assertTrue("Should be OpenStreamPacket but found: " + activePacket,
               activePacket instanceof OpenStreamPacket);
    activePacket = q.pull();
    assertTrue("Should be MessagePacket but found: " + activePacket,
               activePacket instanceof MessagePacket);
  }

  class PacketThread extends Thread {
    public void run(){
      Packet osPacket = new OpenStreamPacket(null);
      Packet msPacket = new MessagePacket(null);
      q.push(osPacket);
      q.push(msPacket);
    }
  }
*/
}
